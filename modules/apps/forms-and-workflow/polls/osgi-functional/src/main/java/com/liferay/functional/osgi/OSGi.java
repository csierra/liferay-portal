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

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

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
			CompletionStage<OSGiResult<T>> futureResult =
				_operation.run(bundleContext);

			return futureResult.thenApply(
				result -> new OSGiResult<>(
					function.apply(result.t), result.cleanUp, result.close));
			}));
	}

	@Override
	public <S, U> OSGi<U> apply(Applicative<OSGi<?>, S> ap) {

		return new OSGi<>(
			(bundleContext) -> {
				CompletionStage<OSGiResult<Function1<S, U>>> osgiResult =
					((OSGiOperation<Function1<S, U>>) _operation).run(
						bundleContext);

				CompletionStage<OSGiResult<S>> apCompletionStage =
					((OSGi<S>) ap)._operation.run(bundleContext);

				return osgiResult.thenCompose(or1 ->
					apCompletionStage.thenCompose(or2 -> {
						CompletableFuture<OSGiResult<U>> future =
							new CompletableFuture<>();

						future.complete(
							new OSGiResult<>(
								or1.t.apply(or2.t),
								or1.cleanUp.applyToEither(or2.cleanUp, x -> x),
								or1.close.andThen(or2.close)));

						return future;
					}));
			});
	}

	@Override
	public <S> Applicative<OSGi<?>, S> pure(S s) {
		return new OSGi<>(((bundleContext) -> {
			CompletableFuture<OSGiResult<S>> completableFuture =
				new CompletableFuture<>();

			completableFuture.complete(
				new OSGiResult<>(s, new CompletableFuture<>(), x -> {}));

			return completableFuture;
		}));
	}

	@Override
	public <S> OSGi<S> bind(
		Function1<T, Monad<OSGi<?>, S>> fun) {

		return new OSGi<>(
			((bundleContext) -> {
				CompletionStage<OSGiResult<T>> futureResult =
					_operation.run(bundleContext);

				Function1<T, CompletionStage<OSGiResult<S>>> funfun =
					fun.andThen(
						oc -> ((OSGi<S>) oc)._operation.run(
							bundleContext));

				return futureResult.thenCompose(or ->
					funfun.apply(or.t).thenApply(
						or2 -> new OSGiResult<>(
							or2.t,
							or2.cleanUp.applyToEither(or.cleanUp, x -> x),
							or.close.andThen(or2.close))));
			}
		));
	}

	public static <T> OSGi<T> service(Class<T> clazz) {
		return new OSGi<>(bundleContext -> {
			CompletableFuture<OSGiResult<T>> future = new CompletableFuture<>();

			CompletableFuture<Boolean> completed = new CompletableFuture<>();

			ServiceTracker<T, T> serviceTracker = new ServiceTracker<T, T>(
				bundleContext, clazz, null){

				@Override
				public T addingService(ServiceReference<T> reference) {
					T t = super.addingService(reference);


					future.complete(
						new OSGiResult<>(t, completed, x -> close()));

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

			return future;
		});
	}

	public static <T> OSGi<T> service(Class<T> clazz, String filterString) {
		return new OSGi<>(bundleContext -> {
			CompletableFuture<OSGiResult<T>> future = new CompletableFuture<>();

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

					future.complete(
						new OSGiResult<>(t, completed, x -> close()));

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

			return future;
		});
	}

	public static <T, S extends T> OSGi<ServiceRegistration<T>> register(
		Class<T> clazz, S service, Map<String, Object> properties) {

		return new OSGi<>(bundleContext -> {
			ServiceRegistration<T> serviceRegistration =
				bundleContext.registerService(
					clazz, service, new Hashtable<>(properties));

			CompletableFuture<OSGiResult<ServiceRegistration<T>>>
				future = new CompletableFuture<>();

			CompletableFuture<Boolean> cleanUp = new CompletableFuture<>();

			future.complete(
				new OSGiResult<>(
					serviceRegistration, cleanUp,
					x -> serviceRegistration.unregister()));

			return future;
		});
	}

	public static <T> CompletionStage<T> runOsgi(
			BundleContext bundleContext, OSGi<T> container,
			Consumer<T> cleanUp)
		throws ExecutionException, InterruptedException {

		CountDownLatch latch = new CountDownLatch(2);

		AtomicReference<Consumer<Void>> later = new AtomicReference<>();

		AtomicReference<T> t = new AtomicReference<>();

		AtomicBoolean executed = new AtomicBoolean(false);

		Consumer<T> myConsumer = x -> {
			latch.countDown();

			try {
				latch.await();

				if (!executed.get()) {
					executed.set(true);

					cleanUp.accept(t.get());
					later.get().accept(null);
				}
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		};

		return container._operation.run(bundleContext).
			thenApply(oc -> {
				t.set(oc.t);
				oc.cleanUp.thenAccept(x -> myConsumer.accept(null));
				later.set(oc.close);
				latch.countDown();

				return oc.t;
			}).
			toCompletableFuture();
	}

}
