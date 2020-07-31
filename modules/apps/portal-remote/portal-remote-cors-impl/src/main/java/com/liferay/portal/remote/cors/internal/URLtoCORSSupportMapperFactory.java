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

package com.liferay.portal.remote.cors.internal;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Carlos Sierra Andrés
 */
public class URLtoCORSSupportMapperFactory {

	public static URLtoCORSSupportMapper create(
		Map<String, CORSSupport> corsSupports) {

		URLtoCORSSupportMapper urLtoCORSSupportMapper;

		if (corsSupports.size() > 64) {
			urLtoCORSSupportMapper = new DynamicURLtoCORSSupportMapper();
		}
		else {
			Set<String> keys = corsSupports.keySet();

			Stream<String> stream = keys.stream();

			urLtoCORSSupportMapper = new StaticURLtoCORSSupportMapper(
				stream.map(
					String::length
				).max(
					Comparator.naturalOrder()
				).orElse(
					0
				));
		}

		for (Map.Entry<String, CORSSupport> entry : corsSupports.entrySet()) {
			urLtoCORSSupportMapper.put(entry.getKey(), entry.getValue());
		}

		return urLtoCORSSupportMapper;
	}

}