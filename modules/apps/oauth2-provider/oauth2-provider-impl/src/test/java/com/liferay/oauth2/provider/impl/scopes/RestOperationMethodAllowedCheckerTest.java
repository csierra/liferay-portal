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

import org.junit.Test;

import javax.ws.rs.GET;
import java.util.Collections;

import static junit.framework.TestCase.assertTrue;

public class RestOperationMethodAllowedCheckerTest {

	@Test
	public void testMethodAllowed() throws NoSuchMethodException {
		ThreadLocalScopeChecker threadLocalScopeChecker =
			new ThreadLocalScopeChecker();

		threadLocalScopeChecker.setAllowedScopes(
			Collections.singleton(new TestOauth2Scope("READ")));

		MethodAllowedChecker methodAllowedChecker =
			new RestOperationMethodAllowedChecker(threadLocalScopeChecker);

		assertTrue(
			methodAllowedChecker.isAllowed(
				EndpointSample.class.getMethod("hello")));
	}

	private static class EndpointSample {

		@GET
		public String hello() {
			return "hello";
		}

	}
}
