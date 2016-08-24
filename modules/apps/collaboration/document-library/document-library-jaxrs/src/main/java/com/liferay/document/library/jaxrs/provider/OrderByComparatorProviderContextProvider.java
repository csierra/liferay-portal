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

package com.liferay.document.library.jaxrs.provider;

import com.liferay.document.library.jaxrs.util.AggregateOrderByComparator;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import org.apache.cxf.jaxrs.ext.ContextProvider;
import org.apache.cxf.message.Message;

import javax.servlet.ServletRequest;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Carlos Sierra Andrés
 */
@Provider
public class OrderByComparatorProviderContextProvider
	implements ContextProvider<OrderBySelector> {

	@Override
	public OrderBySelector createContext(Message message) {
		ServletRequest request =
			(ServletRequest) message.getContextualProperty(
				"HTTP.REQUEST");

		String[] orders = request.getParameterValues("order");

		if (orders == null) {
			return new OrderBySelector() {
				public <T> Optional<OrderByComparator<T>> select(
					Function<String, Optional<Function<Boolean, OrderByComparator<T>>>> f) {

					return Optional.empty();
				}
			};
		}

		return new OrderBySelector() {
			public <T> Optional<OrderByComparator<T>> select(
				Function<String, Optional<Function<Boolean, OrderByComparator<T>>>> f) {

				List<OrderByComparator<T>> orderByComparators = Arrays.stream(orders).
					map(parseOrder(f)).
					filter(Optional::isPresent).
					map(Optional::get).
					collect(Collectors.toList());

				if (orderByComparators.isEmpty()) {
					return Optional.empty();
				}

				if (orderByComparators.size() == 1) {
					return Optional.of(orderByComparators.get(0));
				}

				return Optional.of(
					new AggregateOrderByComparator<>(orderByComparators));
			}
		};
	}

	protected <T> Function<String, Optional<OrderByComparator<T>>> parseOrder(
		Function<String, Optional<Function<Boolean, OrderByComparator<T>>>> f) {

		return order -> {
			String[] split = order.split(":");

			if (split.length != 2) {
				return Optional.empty();
			}

			String column = split[0];

			boolean asc = GetterUtil.getBoolean(split[1], false);

			return f.apply(column).map(f2 -> f2.apply(asc));
		};
	}

}