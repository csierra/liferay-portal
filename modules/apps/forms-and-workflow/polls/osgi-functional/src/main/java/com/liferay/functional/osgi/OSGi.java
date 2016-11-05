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

import com.liferay.functional.Applicative;
import com.liferay.functional.Monad;
import com.liferay.functional.osgi.OSGiOperation.OSGiResult;
import javaslang.Function1;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.Deque;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Carlos Sierra Andrés
 */
public class OSGi<T>
	implements Applicative<OSGi<?>, T>, Monad<OSGi<?>, T> {

	private OSGiOperation<T> _operation;

	public OSGi(OSGiOperation<T> operation) {
		_operation = operation;
	}

	@Override
	public <S> OSGi<S> map(Function1<T, S> function) {
		return new OSGi<>(((bundleContext) -> {
			OSGiResult<T> osGiResult = _operation.run(bundleContext);

			return new OSGiResult<>(
				osGiResult.t.thenApply(function),
				osGiResult.cleanUp,
				osGiResult.close);
		}));
	}

	@Override
	public <S, U> OSGi<U> apply(Applicative<OSGi<?>, S> ap) {

		return new OSGi<>(
			(bundleContext) -> {
				OSGiResult<Function1<S, U>> osgiResult =
					((OSGiOperation<Function1<S, U>>) _operation).run(
						bundleContext);

				OSGiResult<S> osgiResult2 =
					((OSGi<S>) ap)._operation.run(bundleContext);

				return new OSGiResult<>(
					osgiResult.t.thenCompose(f -> osgiResult2.t.thenApply(f)),
					osgiResult.cleanUp.applyToEither(
						osgiResult2.cleanUp, x -> x),
					osgiResult.close.andThen(osgiResult2.close));
			});
	}

	@Override
	public <S> Applicative<OSGi<?>, S> pure(S s) {
		return just(s);
	}

	public static <S> OSGi<S> just(S s) {
		return new OSGi<>(((bundleContext) -> {
			CompletableFuture<S> completableFuture = new CompletableFuture<>();

			completableFuture.complete(s);

			return new OSGiResult<>(
				completableFuture, new CompletableFuture<>(), x -> {});
		}));
	}

	@Override
	public <S> OSGi<S> bind(
		Function1<T, Monad<OSGi<?>, S>> fun) {

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
					OSGi<S> result = (OSGi<S>) fun.apply(t);

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

	public static <T> OSGi<T> service(Class<T> clazz) {
		return new OSGi<>(bundleContext -> {
			CompletableFuture<T> future = new CompletableFuture<>();

			CompletableFuture<Boolean> completed = new CompletableFuture<>();

			ServiceTracker<T, T> serviceTracker = new ServiceTracker<T, T>(
				bundleContext, clazz, null){

				@Override
				public T addingService(ServiceReference<T> reference) {
					T t = super.addingService(reference);

					future.complete(t);

					return t;
				}

				@Override
				public void removedService(
					ServiceReference<T> reference, T service) {

					super.removedService(reference, service);

					completed.complete(true);
				}

				@Override
				public void close() {
					super.close();

					System.out.println("CLOSED!");
				}
			};

			serviceTracker.open();

			return new OSGiResult<>(
				future, completed, x -> serviceTracker.close());
		});
	}

	public static <T, S> OSGi<Void> forEach(
		Class<T> clazz, String filterString, Function1<T, OSGi<S>> program) {

		return new OSGi<>(bundleContext -> {
			CompletableFuture<Void> future = new CompletableFuture<>();

			future.complete(null);

			Filter filter;

			try {
				filter = bundleContext.createFilter(
					"(&(objectClass=" + clazz.getName() + ")" +
					filterString + ")");
			}
			catch (InvalidSyntaxException e) {
				throw new RuntimeException(e);
			}

			ServiceTracker<T, OSGiResult<Void>> serviceTracker =
				new ServiceTracker<>(bundleContext, filter,
					new ServiceTrackerCustomizer<T, OSGiResult<Void>>() {
						@Override
						public OSGiResult<Void> addingService(
							ServiceReference<T> serviceReference) {

							T service =
								bundleContext.getService(serviceReference);


							OSGi<S> apply = program.apply(service);

							OSGiResult<S> result = runOsgi(
								bundleContext, apply, x -> {});

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
				future, new CompletableFuture<>(), x -> serviceTracker.close());
		});
	}

	public static <T> OSGi<Void> forEver(OSGi<T> program) {
		return new OSGi<>(bundleContext -> {
			AtomicBoolean completed = new AtomicBoolean(false);

			OSGiResult<T> result =
				forEver0(bundleContext, program, completed);

			return new OSGiResult<>(
				result.t.thenAccept(x -> {}),
				new CompletableFuture<>(),
				result.close.andThen(x -> completed.set(true)));
		});
	}

	private static <T> OSGiResult<T> forEver0(
		BundleContext bundleContext, OSGi<T> program, AtomicBoolean completed) {

		return runOsgi(bundleContext, program, x ->
		{
			if (!completed.get()) {
				forEver0(bundleContext, program, completed);
			}
		});
	}

	public static <T> OSGi<T> service(Class<T> clazz, String filterString) {
		return new OSGi<>(bundleContext -> {
			CompletableFuture<T> future = new CompletableFuture<>();

			Filter filter;

			try {
				filter = bundleContext.createFilter(
					"(&(objectClass=" + clazz.getName() + ")" +
						filterString + ")");
			}
			catch (InvalidSyntaxException e) {
				throw new RuntimeException(e);
			}

			CompletableFuture<Boolean> completed =
				new CompletableFuture<>();

			ServiceTracker<T, T> serviceTracker = new ServiceTracker<T, T>(
				bundleContext, filter, null){

				@Override
				public T addingService(ServiceReference<T> reference) {

					T t = super.addingService(reference);

					future.complete(t);

					return t;
				}

				@Override
				public void removedService(
					ServiceReference<T> reference, T service) {

					super.removedService(reference, service);

					completed.complete(true);
				}

				@Override
				public void close() {
					super.close();

					System.out.println("CLOSED!");
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
				future, cleanUp,
				x -> serviceRegistration.unregister());
		});
	}

	public static <T> OSGiResult<T> runOsgi(
			BundleContext bundleContext, OSGi<T> container,
			Consumer<Void> cleanUp) {

		AtomicBoolean executed = new AtomicBoolean(false);

		OSGiResult<T> osgiResult = container._operation.run(bundleContext);

		Consumer<Void> myConsumer = x -> {
			boolean hasBeenExecuted = executed.getAndSet(true);

			if (!hasBeenExecuted) {
				osgiResult.close.andThen(cleanUp).accept(null);
			}
		};

		return new OSGiResult<>(
			osgiResult.t,
			osgiResult.cleanUp.thenApply(x -> {
				myConsumer.accept(null); return x;
			}),
			myConsumer);
	}

}
