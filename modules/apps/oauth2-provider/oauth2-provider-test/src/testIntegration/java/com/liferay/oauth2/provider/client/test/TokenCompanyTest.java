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

package com.liferay.oauth2.provider.client.test;

import com.liferay.oauth2.provider.test.internal.activator.configuration.BaseTestActivator;
import com.liferay.portal.kernel.exception.PortalException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

/**
 * @author Carlos Sierra Andrés
 */
@RunAsClient
@RunWith(Arquillian.class)
public class TokenCompanyTest extends BaseClientTest {

	@Deployment
	public static Archive<?> getDeployment() throws Exception {
		return BaseClientTest.getDeployment(
			AnnotatedApplicationBundleActivator.class);
	}

	@Test
	public void testDifferentRoles() throws Exception {
		getToken("oauthTestApplication", "myhost.xyz");

		// This does not fail

		try {
			getToken("oauthTestApplicationDefault", "myhostdefaultuser.xyz");

			//This fails because of lack of permissions

			Assert.fail("This should have failed");
		}
		catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains("invalid_grant"));
		}

		getToken("oauthTestApplicationAllowed", "myhostallowed.xyz");
	}

	public static class AnnotatedApplicationBundleActivator
		extends BaseTestActivator {

		@Override
		protected List<Oauth2Runnable<?>> getTestRunnables() throws Exception {
			return Arrays.asList(
				createCompany("myhost").flatMap(
					this::addUser
				).flatMap(
					user -> createOauth2Application(
						user.getCompanyId(), user, "oauthTestApplication")),
				createCompany("myhostallowed").flatMap(
					this::addAdminUser
				).flatMap(
					user -> createOauth2Application(
						user.getCompanyId(), user,
						"oauthTestApplicationAllowed")),
				createCompany("myhostdefaultuser").flatMap(
					company -> {
						try {
							return createOauth2Application(
								company.getCompanyId(),
								company.getDefaultUser(),
								"oauthTestApplicationDefault");
						}
						catch (PortalException e) {
							throw new IllegalArgumentException(e);
						}
					})
			);
		}

	}

}