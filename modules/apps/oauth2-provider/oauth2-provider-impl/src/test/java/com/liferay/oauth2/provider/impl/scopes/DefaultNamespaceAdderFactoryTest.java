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

package com.liferay.oauth2.provider.impl.scopes;

import com.liferay.oauth2.provider.api.scopes.NamespaceAdder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class DefaultNamespaceAdderFactoryTest {

	@Test
	public void testMerge() {
		DefaultNamespaceAdderFactory defaultNamespaceAdderFactory =
			new DefaultNamespaceAdderFactory();

		NamespaceAdder namespaceAdder1 = defaultNamespaceAdderFactory.create(
			"HELLO");
		NamespaceAdder namespaceAdder2 = defaultNamespaceAdderFactory.create(
			"BEAUTIFUL");
		NamespaceAdder namespaceAdder3 = defaultNamespaceAdderFactory.create(
			"COLOURFUL");

		NamespaceAdder namespaceAdder = NamespaceAdder.merge(
			Arrays.asList(namespaceAdder1, namespaceAdder2, namespaceAdder3));

		String namespaced = namespaceAdder.addNamespace("WORLD");

		assertEquals("HELLO_BEAUTIFUL_COLOURFUL_WORLD", namespaced);
	}
}
