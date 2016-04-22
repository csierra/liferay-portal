/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.osgi.service.tracker.collections;

import com.liferay.osgi.service.tracker.collections.internal.map.MultiValueServiceTrackerBucketFactory;
import com.liferay.osgi.service.tracker.collections.internal.map.ServiceTrackerMapImpl;
import com.liferay.osgi.service.tracker.collections.internal.map.SingleValueServiceTrackerBucketFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceReferenceMapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerBucketFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapListener;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Carlos Sierra Andrés
 */
public class ServiceTrackerMapBuilder {

	public static <K, SR, TR, R> ServiceTrackerMap<K, R> create(
		Function<Builder, Builder.Final<K, SR, TR, R>> function) {

		BuilderImpl.Step2Impl<SR, TR>.Step3Impl<K, SR, TR, R>.
			FinalImpl<K, SR, TR, R> aFinal =
				(BuilderImpl.Step2Impl<SR, TR>.Step3Impl<K, SR, TR, R>.
					FinalImpl<K, SR, TR, R>)function.apply(new BuilderImpl());

		BundleContext bundleContext = aFinal.getBundleContext();

		ServiceReferenceMapper<K, SR> serviceReferenceMapper =
			aFinal.getServiceReferenceMapper();
		ServiceTrackerCustomizer<SR, TR> serviceTrackerCustomizer =
			aFinal.getServiceTrackerCustomizer();
		ServiceTrackerMapListener<K, TR, R> serviceTrackerMapListener =
			aFinal.getServiceTrackerMapListener();
		Class<SR> trackingClass = aFinal.getTrackingClass();
		String filter = aFinal.getFilter();

		return new ServiceTrackerMapImpl<>(
			bundleContext, trackingClass, filter, serviceReferenceMapper,
			serviceTrackerCustomizer, aFinal.getBucketFactory(),
			serviceTrackerMapListener);
	}

	public static <K, SR, TR, R> ServiceTrackerMap<K, R> open(
		Function<Builder, Builder.Final<K, SR, TR, R>> function) {

		ServiceTrackerMap<K, R> serviceTrackerMap = create(function);

		serviceTrackerMap.open();

		return serviceTrackerMap;
	}

	static class BuilderImpl implements Builder {
		private BundleContext _bundleContext;

		public Step1 context(BundleContext bundleContext) {
			_bundleContext = bundleContext;

			return new Step1Impl();
		}

		private class Step1Impl implements Step1 {

			public <SR, NR> Step2<SR, NR> tracking(
				Function<TrackingBuilder, TrackingBuilder.Final<SR, NR>>
					trackingBuilderFunction) {

				TrackingBuilderImpl.TrackingFinalImpl<SR, NR> trackingBuilder =
						(TrackingBuilderImpl.TrackingFinalImpl<SR, NR>)
							trackingBuilderFunction.apply(
								new TrackingBuilderImpl());

				return new Step2Impl<>(trackingBuilder);
			}

		}

		private class TrackingBuilderImpl implements TrackingBuilder {

			@Override
			public Final<Object, Object> clazz(String className) {
				return new TrackingFinalImpl<>(
					Object.class, "(objectClass=" + className + ")", null);
			}

			@Override
			public <T> Final<T, T> clazz(Class<T> clazz) {
				return new TrackingFinalImpl<>(clazz, null, null);
			}

			@Override
			public Final<Object, Object> filter(String filter) {
				return new TrackingFinalImpl<>(Object.class, filter, null);
			}

			@Override
			public <T> Final<T, T> clazz(Class<T> clazz, String filter) {
				return new TrackingFinalImpl<>(clazz, filter, null);
			}

			class TrackingFinalImpl<T, NR> implements Final<T, NR> {
				private Class<T> _clazz;
				private String _filter;
				private ServiceTrackerCustomizer<T, NR> _customizer;

				public TrackingFinalImpl(
					Class<T> clazz, String filter,
					ServiceTrackerCustomizer<T, NR> customizer) {

					_clazz = clazz;
					_filter = filter;
					_customizer = customizer;
				}

				@Override
				public <NR> Final<T, NR> customize(
					ServiceTrackerCustomizer<T, NR> customizer) {
						return new TrackingFinalImpl<>(
							_clazz, _filter, customizer);
				}

				@Override
				public <NR> Final<T, NR> customize(
					Function<BundleContext,
					ServiceTrackerCustomizer<T, NR>> function) {

					return new TrackingFinalImpl<>(
						_clazz, _filter, function.apply(_bundleContext));
				}

			}
		}

		class Step2Impl<SR, NR> implements Step2<SR, NR> {

			private TrackingBuilderImpl.TrackingFinalImpl<SR, NR>
				_trackingBuilder;

			public Step2Impl(
					TrackingBuilderImpl.TrackingFinalImpl<SR, NR>
					trackingBuilder) {

				_trackingBuilder = trackingBuilder;
			}

			@Override
			public <K> Step3<K, SR, NR, NR> mapping(
					ServiceReferenceMapper<K, SR> mapper) {

				return new Step3Impl<K, SR, NR, NR>(mapper, null);
			}

			@Override
			public <K> Step3<K, SR, NR, NR> mapping(
					Function<BundleContext, ServiceReferenceMapper<K, SR>>
					function) {

				return new Step3Impl<>(function.apply(_bundleContext), null);
			}

			@Override
			public Step3<String, SR, NR, NR> property(String property) {
				return new Step3Impl<>(
						(sr, em) -> em.emit(
								sr.getProperty(property).toString()),
						_trackingBuilder._filter == null ?
							"(" + property + "=*)" : _trackingBuilder._filter);
			}

			class Step3Impl<K, SR, NR, R>
				implements Step3<K, SR, NR, R>,
					Filterable<Step3<K, SR, NR, R>> {

				private ServiceReferenceMapper<K, SR> _mapper;
				private String _filter;

				public Step3Impl(
						ServiceReferenceMapper<K, SR> mapper, String filter) {

					_mapper = mapper;
					_filter = filter;
				}

				@Override
				public Final<K, SR, NR, NR> single() {
					return new FinalImpl<>(
						new SingleValueServiceTrackerBucketFactory<>(), null);
				}

				@Override
				public Final<K, SR, NR, NR> single(
					Comparator<ServiceReference<SR>> comparator) {

					return new FinalImpl<>(
						new SingleValueServiceTrackerBucketFactory<>(
							comparator),
						null);
				}

				@Override
				public Final<K, SR, NR, List<NR>> multi() {
					return new FinalImpl<>(
						new MultiValueServiceTrackerBucketFactory<>(), null);
				}

				@Override
				public Final<K, SR, NR, List<NR>> multi(
					Comparator<ServiceReference<SR>> comparator) {

					return new FinalImpl<>(
						new MultiValueServiceTrackerBucketFactory<>(comparator),
						null);
				}

				@Override
				public Final<K, SR, NR, R> withFactory(
					ServiceTrackerBucketFactory<SR, NR, R> bucketFactory) {

					return new FinalImpl<>(bucketFactory, null);
				}

				@Override
				public Step3<K, SR, NR, R> filter(String filter) {
					return new Step3Impl<>(_mapper, filter);
				}

				class FinalImpl<K, SR, NR, R> implements Final<K, SR, NR, R> {
					private String _type;
					private ServiceTrackerBucketFactory<SR, NR, R>
						_bucketFactory;

					public ServiceTrackerBucketFactory<SR, NR, R>
						getBucketFactory() {

						return _bucketFactory;
					}

					private ServiceTrackerMapListener<K, NR, R>
						_serviceTrackerMapListener;

					public FinalImpl(
						ServiceTrackerBucketFactory<SR, NR, R> bucketFactory,
						ServiceTrackerMapListener<K, NR, R>
							serviceTrackerMapListener) {

						_bucketFactory = bucketFactory;
						_serviceTrackerMapListener = serviceTrackerMapListener;
					}

					@Override
					public Final<K, SR, NR, R> withListener(
							ServiceTrackerMapListener<K, NR, R>
									serviceTrackerMapListener) {

						return new FinalImpl<>(
							_bucketFactory, serviceTrackerMapListener);
					}

					public ServiceTrackerMapListener<K, NR, R>
						getServiceTrackerMapListener() {

						return _serviceTrackerMapListener;
					}

					public String getType() {
						return _type;
					}

					public ServiceReferenceMapper<K, SR>
						getServiceReferenceMapper() {

						return (ServiceReferenceMapper<K, SR>) _mapper;
					}

					public String getFilter() {
						return _filter;
					}

					public Class<SR> getTrackingClass() {
						return (Class<SR>) _trackingBuilder._clazz;
					}

					public ServiceTrackerCustomizer<SR, NR>
						getServiceTrackerCustomizer() {

						return (ServiceTrackerCustomizer<SR, NR>)
							_trackingBuilder._customizer;
					}

					public BundleContext getBundleContext() {
						return _bundleContext;
					}

				}
			}
		}

	}

	public interface Builder {

		public Step1 context(BundleContext bundleContext);

		public interface Step1 {

			public <SR, NR> Step2<SR, NR> tracking(
				Function<TrackingBuilder, TrackingBuilder.Final<SR, NR>>
					trackingBuilder);
		}

		public interface Step2<SR, NR> {

			public <K> Step3<K, SR, NR, NR> mapping(
				ServiceReferenceMapper<K, SR> mapper);

			public <K> Step3<K, SR, NR, NR> mapping(
					Function<BundleContext, ServiceReferenceMapper<K, SR>>
						customizer);

			public Step3<String, SR, NR, NR> property(String property);
		}

		public interface Filterable<F> {

			public F filter(String filter);
		}

		public interface Step3<K, SR, NR, R> {

			public Final<K, SR, NR, NR> single();

			public Final<K, SR, NR, NR> single(
				Comparator<ServiceReference<SR>> comparator);

			public Final<K, SR, NR, List<NR>> multi();

			public Final<K, SR, NR, List<NR>> multi(
				Comparator<ServiceReference<SR>> comparator);

			public Final<K, SR, NR, R> withFactory(
				ServiceTrackerBucketFactory<SR, NR, R> bucketFactory);
		}

		public interface Final<K, SR, NR, R> {

			public Final<K, SR, NR, R> withListener(
				ServiceTrackerMapListener<K, NR, R> serviceTrackerMapListener);
		}

	}

	public interface TrackingBuilder {

		public Final<Object, Object> clazz(String className);

		public <T> Final<T, T> clazz(Class<T> clazz);

		public Final<Object, Object> filter(String filter);

		public <T> Final<T, T> clazz(Class<T> clazz, String filter);

		public interface Final<SR, NR> {

			public <NR> Final<SR, NR> customize(
				ServiceTrackerCustomizer<SR, NR> customizer);

			public <NR> Final<SR, NR> customize(
				Function<BundleContext, ServiceTrackerCustomizer<SR, NR>>
					customizer);
		}
	}

	public static <S, T> Function<BundleContext, ServiceTrackerCustomizer<S, T>>
		customizerFromFunction(
			BiFunction<ServiceReference<S>, S, T> function) {

		return b -> new ServiceTrackerCustomizer<S, T>() {
			public T addingService(ServiceReference<S> serviceReference) {
				S service = b.getService(serviceReference);

				try {
					return function.apply(serviceReference, service);
				}
				catch (Exception e) {
					b.ungetService(serviceReference);

					throw e;
				}
			}

			public void modifiedService(
				ServiceReference<S> serviceReference, T t) {

				removedService(serviceReference, t);

				addingService(serviceReference);
			}

			public void removedService(
				ServiceReference<S> serviceReference, T t) {

				b.ungetService(serviceReference);
			}

		};
	}

	public static <K, S> Function<BundleContext, ServiceReferenceMapper<K, S>>
		mapperFromFunction(
			BiFunction<ServiceReference<S>, S, K> function) {

		return b -> (serviceReference, emitter) -> {
			S service = b.getService(serviceReference);

			try {
				emitter.emit(function.apply(serviceReference, service));
			}
			catch (Exception e) {
				b.ungetService(serviceReference);
			}
		};
	}

}