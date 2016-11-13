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
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.util.tracker.ServiceTracker;
import rx.Observer;
import rx.subjects.PublishSubject;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public class OSGi<T> {

	private static final Consumer<Void> NOOP = x -> {
	};
	private OSGiOperation<T> _operation;

	public OSGi(OSGiOperation<T> operation) {
		_operation = operation;
	}

	public <S> OSGi<S> map(Function<T, S> function) {
		return new OSGi<>(((bundleContext) -> {
			OSGiResult<T> osgiResult = _operation.run(bundleContext);

			return new OSGiResult<>(
				osgiResult.added.map(function::apply),
				osgiResult.removed.map(function::apply),
				osgiResult.start,
				osgiResult.close);
		}));
	}

	public static <S> OSGi<S> just(S s) {
		return new OSGi<>(((bundleContext) -> {
			PublishSubject<S> publishSubject = PublishSubject.create();

			return new OSGiResult<>(
				publishSubject, PublishSubject.create(),
				x -> publishSubject.onNext(s), NOOP);
		}));
	}

	public <S> OSGi<S> flatMap(Function<T, OSGi<S>> fun) {

		return new OSGi<>(
			((bundleContext) -> {

				Map<T, OSGiResult<S>> map = new IdentityHashMap<>();

				PublishSubject<S> added = PublishSubject.create();

				AtomicReference<Consumer<Void>> closeReference =
					new AtomicReference<>(x -> {});

				Consumer<Void> start = s -> {
					OSGiResult<T> or1 = _operation.run(bundleContext);

					or1.added.subscribe(t -> {
						OSGi<S> program = fun.apply(t);

						OSGiResult<S> or2 = program._operation.run(
							bundleContext);

						map.put(t, or2);

						Consumer<Boolean> close = x -> {
							synchronized (map) {
								OSGiResult<S> closer = map.remove(t);

								if (closer != null) {
									OSGi.close(closer);
								}
							}
						};

						or2.added.subscribe(new Observer<S>() {
							@Override
							public void onCompleted() {
								close.accept(null);
							}

							@Override
							public void onError(Throwable e) {

							}

							@Override
							public void onNext(S s) {
								added.onNext(s);
							}
						});

						or2.start.accept(null);
					});

					or1.removed.subscribe(t -> {
						synchronized (map) {
							OSGiResult<S> osgiResult = map.remove(t);

							if (osgiResult != null) {
								OSGi.close(osgiResult);
							}
						}
					});

					closeReference.set(or1.close);

					or1.start.accept(null);
				};

				return new OSGiResult<>(
					added, PublishSubject.create(), start,
					x -> {
						synchronized (map) {
							for (OSGiResult<S> result : map.values()) {
								close(result);
							}
						}

						closeReference.get().accept(null);
					});
			}
		));
	}

	public <S> OSGi<S> then(OSGi<S> next) {
		return flatMap(ignored -> next);
	}

	public <S> OSGi<Void> foreach(Function<T, OSGi<S>> fun) {
		return this.flatMap(fun).map(x -> null);
	}

	public static OSGi<Dictionary<String, ?>> configurations(
		String factoryPid) {

		return new OSGi<>(bundleContext -> {
			PublishSubject<Dictionary<String, ?>> added =
				PublishSubject.create();

			PublishSubject<Dictionary<String, ?>> removed =
				PublishSubject.create();

			Map<String, Dictionary<String, ?>> results =
				new ConcurrentHashMap<>();

			AtomicReference<ServiceRegistration<ManagedServiceFactory>>
				serviceRegistrationReference = new AtomicReference<>(null);

			Consumer<Void> start = x ->
				serviceRegistrationReference.set(bundleContext.registerService(
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

							added.onNext(dictionary);
						}

						@Override
						public void deleted(String s) {
							Dictionary<String, ?> remove = results.remove(s);

							removed.onNext(remove);

						}
					},
					new Hashtable<String, Object>() {{
						put("service.pid", factoryPid);
					}}));

			return new OSGiResult<>(added, removed, start,
				x -> {
					serviceRegistrationReference.get().unregister();

					for (Dictionary<String, ?> dictionary : results.values()) {
						removed.onNext(dictionary);
					}
				});
		});
	}

	public static OSGi<Dictionary<String, ?>> configuration(String pid) {
		return new OSGi<>(bundleContext -> {
			PublishSubject<Dictionary<String, ?>> added =
				PublishSubject.create();

			PublishSubject<Dictionary<String, ?>> removed =
				PublishSubject.create();

			AtomicReference<Dictionary<?, ?>> atomicReference =
				new AtomicReference<>(null);

			AtomicReference<ServiceRegistration<ManagedService>>
				serviceRegistrationReferece = new AtomicReference<>(null);

			Consumer<Void> start = x ->
				serviceRegistrationReferece.set(
					bundleContext.registerService(
						ManagedService.class,
						properties -> {
							if (properties == null) {
								added.onCompleted();
							}
							else {
								if (atomicReference.compareAndSet(
									null, properties)) {

									added.onNext(properties);
								}
								else {
									added.onCompleted();
								}
							}
						},
						new Hashtable<String, Object>() {{
							put("service.pid", pid);
						}}));


			return new OSGiResult<>(
				added, removed, start,
				x -> serviceRegistrationReferece.get().unregister());
		});
	}

	public static OSGi<Void> onClose(Consumer<Void> action) {
		return new OSGi<>(bundleContext -> new OSGiResult<>(
			PublishSubject.create(), PublishSubject.create(),
			NOOP, action::accept));
	}

	public static <T> MOSGi<T> services(Class<T> clazz) {
		return services(clazz, null);
	}

	public static <T> MOSGi<T> services(Class<T> clazz, String filterString) {
		return new MOSGi<T>(bundleContext -> {
			PublishSubject<T> added = PublishSubject.create();

			PublishSubject<T> removed = PublishSubject.create();

			ServiceTracker<T, T> serviceTracker =
				getTServiceTracker(clazz, filterString, bundleContext, added,
					removed);

			return new OSGiResult<>(
				added, removed, x -> serviceTracker.open(),
				x -> serviceTracker.close());
		}) {
			public OSGi<T> once() {
				return new OSGi<>(bundleContext -> {
					PublishSubject<T> added = PublishSubject.create();

					PublishSubject<T> removed = PublishSubject.create();

					ServiceTracker<T, T> serviceTracker =
						getTServiceTracker(clazz, filterString, bundleContext, added,
							removed);

					AtomicReference<T> atomicReference =
						new AtomicReference<>(null);

					removed.subscribe(t -> {
						if (atomicReference.get() == t) {
							added.onCompleted();
						}
					});

					return new OSGiResult<>(
						added.filter(t -> atomicReference.compareAndSet(null, t)),
						removed, x -> serviceTracker.open(),
						x -> serviceTracker.close());
				});
			}
		};
	}

	private static <T> ServiceTracker<T, T> getTServiceTracker(
		final Class<T> clazz, final String filterString,
		final BundleContext bundleContext,
		final PublishSubject<T> added, final PublishSubject<T> removed) {
		return new ServiceTracker<T, T>(
					bundleContext,
					buildFilter(bundleContext, filterString, clazz), null) {

					@Override
					public T addingService(ServiceReference<T> reference) {

						T t = super.addingService(reference);

						added.onNext(t);

						return t;
					}

					@Override
					public void removedService(ServiceReference<T> reference, T t) {

						super.removedService(reference, t);

						removed.onNext(t);
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

	public static <T, S extends T> OSGi<ServiceRegistration<T>> register(
		Class<T> clazz, S service, Map<String, Object> properties) {

		return new OSGi<>(bundleContext -> {
			ServiceRegistration<T> serviceRegistration =
				bundleContext.registerService(
					clazz, service, new Hashtable<>(properties));

			PublishSubject<ServiceRegistration<T>>
				publishSubject = PublishSubject.create();
			return new OSGiResult<>(
				publishSubject, PublishSubject.create(),
				x -> publishSubject.onNext(serviceRegistration),
				x -> {
					try {
						serviceRegistration.unregister();
					}
					catch (Exception e) {
					}
				});
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

		osgiResult.added.subscribe(new Observer<T>() {
			@Override
			public void onCompleted() {
				close.accept(null);
			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(T t) {

			}
		});
		osgiResult.removed.subscribe();

		osgiResult.start.accept(null);

		return new OSGiResult<>(
			osgiResult.added, osgiResult.removed,
			osgiResult.start, close);
	}

	public static void close(OSGiResult<?> osgiResult) {
		osgiResult.close.accept(null);
	}

	public static abstract class MOSGi<T> extends OSGi<T> {
		public MOSGi(OSGiOperation<T> operation) {
			super(operation);
		}

		public abstract OSGi<T> once();
	}

}


