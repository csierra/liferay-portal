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

package com.liferay.item.selector;

import com.liferay.osgi.service.tracker.collections.ServiceReferenceServiceTuple;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerBucket;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerBucketFactory;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.framework.ServiceReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Roberto Díaz
 */
public class ItemSelectorViewServiceTrackerBucketFactory
	implements ServiceTrackerBucketFactory<
	ItemSelectorView, ItemSelectorView, List<ItemSelectorView>> {

	public ItemSelectorViewServiceTrackerBucketFactory() {
		_comparator = Collections.reverseOrder();
	}

	public ItemSelectorViewServiceTrackerBucketFactory(
		Comparator<ServiceReference<ItemSelectorView>> comparator) {

		_comparator = comparator;
	}

	@Override
	public ServiceTrackerBucket<
			ItemSelectorView, ItemSelectorView, List<ItemSelectorView>>
		create() {

		return new ListServiceTrackerBucket();
	}

	private final Comparator<ServiceReference<ItemSelectorView>>
		_comparator;

	private class ListServiceTrackerBucket
		implements
		ServiceTrackerBucket<ItemSelectorView, ItemSelectorView, List<ItemSelectorView>> {

		@Override
		public List<ItemSelectorView> getContent() {
			return _services;
		}

		@Override
		public synchronized boolean isDisposable() {
			return _serviceReferenceServiceTuples.isEmpty();
		}

		@Override
		public synchronized void remove(
			ServiceReferenceServiceTuple<ItemSelectorView, ItemSelectorView>
				serviceReferenceServiceTuple) {

			_serviceReferenceServiceTuples.remove(
				serviceReferenceServiceTuple);

			rebuild();
		}

		@Override
		public synchronized void store(
			ServiceReferenceServiceTuple<ItemSelectorView, ItemSelectorView>
				serviceReferenceServiceTuple) {

			_serviceReferenceServiceTuples.add(
				serviceReferenceServiceTuple);

			rebuild();
		}

		protected void rebuild() {
			_services =
				new ArrayList<>(_serviceReferenceServiceTuples.size());

			for (ServiceReferenceServiceTuple<
					ItemSelectorView, ItemSelectorView>
						serviceReferenceServiceTuple :
							_serviceReferenceServiceTuples) {

				ServiceReference<ItemSelectorView> serviceReference =
					serviceReferenceServiceTuple.getServiceReference();

				String overwriteViewKey = GetterUtil.getString(
					serviceReference.getProperty("overwrite.view.key"));

				if (Validator.isNotNull(overwriteViewKey)) {
					int viewOrder = GetterUtil.getInteger(
						serviceReference.getProperty(
							"item.selector.view.order"));

					for (ServiceReferenceServiceTuple<
							ItemSelectorView, ItemSelectorView>
								referenceServiceTuple :
									_serviceReferenceServiceTuples) {

						ServiceReference<ItemSelectorView> curServiceReference =
							serviceReferenceServiceTuple.getServiceReference();

						String currentOverwriteViewKey =
							GetterUtil.getString(
								curServiceReference.getProperty(
									"overwrite.view.key"));

						if (Validator.isNotNull(currentOverwriteViewKey) &&
							currentOverwriteViewKey.equals(
								overwriteViewKey)) {
							int curViewOrder = GetterUtil.getInteger(
								curServiceReference.getProperty(
									"item.selector.view.order"));

							if (curViewOrder > viewOrder) {
								_services.remove(
									referenceServiceTuple.getService());

								_services.add(
									serviceReferenceServiceTuple.getService());
							}
						}
					}
				}
				else {
					_services.add(
						serviceReferenceServiceTuple.getService());
				}
			}

			_services = Collections.unmodifiableList(_services);
		}

		private ListServiceTrackerBucket() {
			ServiceReferenceServiceTupleComparator<ItemSelectorView>
				serviceReferenceServiceTupleComparator =
				new ServiceReferenceServiceTupleComparator<>(_comparator);

			_serviceReferenceServiceTuples = new TreeSet<>(
				serviceReferenceServiceTupleComparator);
		}

		private final Set<
			ServiceReferenceServiceTuple<ItemSelectorView, ItemSelectorView>>
				_serviceReferenceServiceTuples;

		private List<ItemSelectorView> _services = new ArrayList<>();

	}

}
