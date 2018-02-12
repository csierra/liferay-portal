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

package com.liferay.oauth2.provider.scopes.impl;

import static org.junit.Assert.assertEquals;


import java.util.Arrays;

import com.liferay.oauth2.provider.scopes.impl.scopenamespace.DefaultNamespaceApplicatorBuilder;
import com.liferay.oauth2.provider.scopes.spi.NamespaceApplicator;
import com.liferay.oauth2.provider.scopes.spi.NamespaceApplicatorBuilder;
import org.junit.Test;

public class DefaultPrefixHandlerFactoryTest {

	@Test
	public void testMerge() {
		NamespaceApplicatorBuilder defaultNamespaceApplicatorBuilder =
			new DefaultNamespaceApplicatorBuilder();

		NamespaceApplicator namespaceApplicator1 = defaultNamespaceApplicatorBuilder.build(
			"HELLO");
		NamespaceApplicator namespaceApplicator2 = defaultNamespaceApplicatorBuilder.build(
			"BEAUTIFUL");
		NamespaceApplicator namespaceApplicator3 = defaultNamespaceApplicatorBuilder.build(
			"COLOURFUL");

		NamespaceApplicator namespaceApplicator = NamespaceApplicator.intersect(
			Arrays.asList(namespaceApplicator1, namespaceApplicator2, namespaceApplicator3));

		String namespaced = namespaceApplicator.applyNamespace("WORLD");

		assertEquals("HELLO/BEAUTIFUL/COLOURFUL/WORLD", namespaced);
	}

	@Test
	public void testCreateWithSeveralStrings() {
		DefaultNamespaceApplicatorBuilder defaultNamespaceApplicatorBuilder =
			new DefaultNamespaceApplicatorBuilder();

		NamespaceApplicator namespaceApplicator = defaultNamespaceApplicatorBuilder.build(
			"HELLO", "BEAUTIFUL", "COLOURFUL");

		assertEquals("HELLO/BEAUTIFUL/COLOURFUL/WORLD", namespaceApplicator.applyNamespace(
			"WORLD"));
	}

}