/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.functional.osgi;

import com.liferay.functional.osgi.OSGiOperation.OSGiResult;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public class OSGi<T> {

	private OSGiOperation<T> _operation;

	public OSGi(OSGiOperation<T> operation) {
		_operation = operation;
	}

	public <S> OSGi<S> map(Function<T, S> function) {
		return new OSGi<>(((bundleContext) -> {
			OSGiResult<T> osGiResult = _operation.run(bundleContext);

			return new OSGiResult<>(
				osGiResult.t.thenApply(function),
				osGiResult.cleanUp,
				osGiResult.close);
		}));
	}

	public static <S> OSGi<S> just(S s) {
		return new OSGi<>(((bundleContext) -> {
			CompletableFuture<S> completableFuture = new CompletableFuture<>();

			completableFuture.complete(s);

			return new OSGiResult<>(
				completableFuture, new CompletableFuture<>(), x -> {});
		}));
	}

	public <S> OSGi<S> flatMap(
		Function<T, OSGi<S>> fun) {

		return new OSGi<>(
			((bundleContext) -> {

				OSGiResult<T> or1 = _operation.run(bundleContext);

				CompletableFuture<S> futureResult =
					new CompletableFuture<>();

				AtomicReference<CompletionStage<Boolean>> otherCleanup =
					new AtomicReference<>(new CompletableFuture<>());

				AtomicReference<Consumer<Void>> otherClose =
					new AtomicReference<>(x -> {});

				or1.t.thenAccept(t -> {
					OSGi<S> result = fun.apply(t);

					OSGiResult<S> or2 = result._operation.run(bundleContext);

					otherCleanup.set(or2.cleanUp);
					otherClose.set(or2.close);
					or2.t.thenAccept(futureResult::complete);
				});

				return new OSGiResult<>(
					futureResult,
					or1.cleanUp.applyToEither(otherCleanup.get(), x -> x),
					x -> otherClose.get().andThen(or1.close).accept(null));
			}
		));
	}

	public <S> OSGi<S> then(OSGi<S> next) {
		return flatMap(ignored -> next);
	}

	public static MOSGi<Dictionary<String, ?>> configurations(String factoryPid) {
		return new MOSGi<Dictionary<String, ?>>(bundleContext -> {

			CompletableFuture<Iterable<Dictionary<String, ?>>>
				future = new CompletableFuture<>();

			Collection<Dictionary<String, ?>> list = new CopyOnWriteArrayList<>();

			future.complete(list);

			Map<String, Dictionary<String, ?>> results =
				new ConcurrentHashMap<>();

			ServiceRegistration<ManagedServiceFactory> serviceRegistration =
				bundleContext.registerService(
					ManagedServiceFactory.class, new ManagedServiceFactory() {
						@Override
						public String getName() {
							return "Functional OSGi Managed Service Factory";
						}

						@Override
						public void updated(
							String s, Dictionary<String, ?> dictionary)
							throws ConfigurationException {

							results.put(s, dictionary);

							list.add(dictionary);
						}

						@Override
						public void deleted(String s) {
							list.remove(results.get(s));
						}
					}, new Hashtable<String, Object>() {{
						put("service.pid", factoryPid);
					}});

			return new OSGiResult<>(
				future, new CompletableFuture<>(), x -> {

				serviceRegistration.unregister();

				list.clear();
			});
		}) {
			@Override
			public OSGi<Void> foreach(
				Function<Dictionary<String, ?>, OSGi<?>> program) {

				return new OSGi<>(bundleContext -> {
					CompletableFuture<Void> future = new CompletableFuture<>();

					future.complete(null);

					CompletableFuture<Boolean> completed = new CompletableFuture<>();

					Map<String, OSGiResult<?>> results = new ConcurrentHashMap<>();

					ServiceRegistration<ManagedServiceFactory> serviceRegistration =
						bundleContext.registerService(
							ManagedServiceFactory.class, new ManagedServiceFactory() {
								@Override
								public String getName() {
									return "Functional OSGi Managed Service Factory";
								}

								@Override
								public void updated(
									String s, Dictionary<String, ?> dictionary)
									throws ConfigurationException {

									OSGiResult<?> result = runOsgi(
										bundleContext, program.apply(dictionary));

									results.put(s, result);
								}

								@Override
								public void deleted(String s) {
									OSGiResult<?> result = results.remove(s);

									result.close.accept(null);
								}
							}, new Hashtable<String, Object>() {{
								put("service.pid", factoryPid);
							}});

					return new OSGiResult<>(
						future, completed, x -> {
						serviceRegistration.unregister();

						results.forEach((s, r) -> r.close.accept(null));
					});
				});
			}
		};
	}

	public static OSGi<Dictionary<String, ?>> configuration(String pid) {
		return new OSGi<>(bundleContext -> {
			CompletableFuture<Dictionary<String, ?>>
				future = new CompletableFuture<>();

			CompletableFuture<Boolean> completed =
				new CompletableFuture<>();

			ServiceRegistration<ManagedService>
				serviceRegistration =
				bundleContext.registerService(
					ManagedService.class,
					properties -> {
						if (properties == null) {
							completed.complete(true);
						}
						else {
							future.complete(properties);
						}
					},
					new Hashtable<String, Object>() {{
						put("service.pid", pid);
					}});

			return new OSGiResult<>(
				future, completed, x -> serviceRegistration.unregister());
		});
	}

	public static OSGi<Void> onClose(Consumer<Void> action) {
		return new OSGi<>(bundleContext -> {
			CompletableFuture<Void> future = new CompletableFuture<>();

			future.complete(null);

			return new OSGiResult<>(
				future, new CompletableFuture<>(),
				action::accept);
		});
	}

	public static <T> OSGi<Void> forEver(OSGi<T> program) {
		return new OSGi<>(bundleContext -> {
			AtomicBoolean completed = new AtomicBoolean(false);

			AtomicReference<Consumer<Void>> close = new AtomicReference<>(x -> {});

			OSGiResult<Void> result =
				forEver0(bundleContext, program, completed, close);

			return new OSGiResult<>(
				result.t.thenAccept(x -> {}),
				new CompletableFuture<>(),
				result.close.andThen(x -> {
					completed.set(true);

					close.get().accept(null);
				}));
		});
	}

	private static <T> OSGiResult<Void> forEver0(
		BundleContext bundleContext, OSGi<T> program,
		AtomicBoolean completed, AtomicReference<Consumer<Void>> close) {

		return runOsgi(bundleContext, program.then(onClose(x ->
		{
			if (!completed.get()) {
				OSGiResult<Void> osgiResult =
					forEver0(bundleContext, program, completed, close);

				close.set(osgiResult.close);
			}
		})));
	}

	public static <T> MOSGi<T> services(Class<T> clazz) {
		return services(clazz, null);
	}

	public static <T> MOSGi<T> services(Class<T> clazz, String filterString) {
		return new MOSGi<T>(bundleContext -> {
			CompletableFuture<Iterable<T>>future =
				new CompletableFuture<>();

			ServiceTrackerList<T, T> list = ServiceTrackerListFactory.open(
				bundleContext, clazz, filterString);

			future.complete(list);

			return new OSGiResult<>(
				future, new CompletableFuture<>(), x -> list.close());
		}) {
			@Override
			public OSGi<Void> foreach(Function<T, OSGi<?>> program) {
				return new OSGi<>(bundleContext -> {
					CompletableFuture<Void> future = new CompletableFuture<>();

					future.complete(null);

					ServiceTracker<T, OSGiResult<Void>> serviceTracker =
						new ServiceTracker<>(
							bundleContext,
							buildFilter(bundleContext, filterString, clazz),
							new ServiceTrackerCustomizer<T, OSGiResult<Void>>() {
								@Override
								public OSGiResult<Void> addingService(
									ServiceReference<T> serviceReference) {

									T service =
										bundleContext.getService(serviceReference);


									OSGi<?> apply = program.apply(service);

									OSGiResult<?> result = runOsgi(
										bundleContext, apply);

									return new OSGiResult<>(
										result.t.thenAccept(x -> {}), result.cleanUp,
										result.close);
								}

								@Override
								public void modifiedService(
									ServiceReference<T> serviceReference,
									OSGiResult<Void> osgiResult) {

									removedService(serviceReference, osgiResult);

									addingService(serviceReference);
								}

								@Override
								public void removedService(
									ServiceReference<T> serviceReference,
									OSGiResult<Void> result) {

									bundleContext.ungetService(serviceReference);

									result.close.accept(null);
								}
							});

					serviceTracker.open();

					return new OSGiResult<>(
						future, new CompletableFuture<>(),
						x -> serviceTracker.close());

				});
			}
		};
	}

	private static <T> Filter buildFilter(
		BundleContext bundleContext, String filterString, Class<T> clazz) {
		Filter filter;
		try {
			if (filterString == null) {
				filter = bundleContext.createFilter(
					"(objectClass=" + clazz.getName() + ")");
			}
			else {
				filter = bundleContext.createFilter(
					"(&(objectClass=" + clazz.getName() + ")" +
					filterString + ")");
			}
		}
		catch (InvalidSyntaxException e) {
			throw new RuntimeException(e);
		}
		return filter;
	}

	public static <T> OSGi<T> service(Class<T> clazz) {
		return service(clazz, null);
	}

	public static <T> OSGi<T> service(Class<T> clazz, String filterString) {
		return new OSGi<>(bundleContext -> {
			CompletableFuture<T> future = new CompletableFuture<>();

			CompletableFuture<Boolean> completed = new CompletableFuture<>();

			ServiceTracker<T, T> serviceTracker = new ServiceTracker<T, T>(
				bundleContext,
				buildFilter(bundleContext, filterString, clazz), null) {

				@Override
				public T addingService(ServiceReference<T> reference) {

					T t = super.addingService(reference);

					boolean completed = future.complete(t);

					if (completed) {
						return t;
					}

					return null;
				}

				@Override
				public void removedService(
					ServiceReference<T> reference, T service) {

					super.removedService(reference, service);

					completed.complete(true);
				}
			};

			serviceTracker.open();

			return new OSGiResult<>(
				future, completed, x -> serviceTracker.close());
		});
	}

	public static <T, S extends T> OSGi<ServiceRegistration<T>> register(
		Class<T> clazz, S service, Map<String, Object> properties) {

		return new OSGi<>(bundleContext -> {
			ServiceRegistration<T> serviceRegistration =
				bundleContext.registerService(
					clazz, service, new Hashtable<>(properties));

			CompletableFuture<ServiceRegistration<T>>
				future = new CompletableFuture<>();

			CompletableFuture<Boolean> cleanUp = new CompletableFuture<>();

			future.complete(serviceRegistration);

			return new OSGiResult<>(
				future, cleanUp, x -> serviceRegistration.unregister());
		});
	}

	public static <T> OSGiResult<T> runOsgi(
		BundleContext bundleContext, OSGi<T> program) {

		AtomicBoolean executed = new AtomicBoolean(false);

		OSGiResult<T> osgiResult = program._operation.run(bundleContext);

		Consumer<Void> close = x -> {
			boolean hasBeenExecuted = executed.getAndSet(true);

			if (!hasBeenExecuted) {
				osgiResult.close.accept(null);
			}
		};

		Function<Boolean, Boolean> cleanup = x -> {
			close.accept(null);

			return x;
		};

		return new OSGiResult<>(
			osgiResult.t, osgiResult.cleanUp.thenApply(cleanup), close);
	}

	public static void close(OSGiResult<?> osgiResult) {
		osgiResult.close.accept(null);
	}

	public static abstract class MOSGi<T> extends OSGi<Iterable<T>> {

		public MOSGi(OSGiOperation<Iterable<T>> operation) {
			super(operation);
		}

		abstract public OSGi<Void> foreach(Function<T, OSGi<?>> program);
	}

}


