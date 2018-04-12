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

import com.liferay.oauth2.provider.test.internal.TestAnnotatedApplication;
import com.liferay.oauth2.provider.test.internal.activator.configuration.BaseTestActivator;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Carlos Sierra Andrés
 */
@RunAsClient
@RunWith(Arquillian.class)
public class AnnotatedApplicationClientTest extends BaseClientTest {

	@Deployment
	public static Archive<?> getDeployment() throws Exception {
		return BaseClientTest.getDeployment(
			AnnotatedApplicationBundleActivator.class);
	}

	@Test
	public void testAnnotatedApplication() throws Exception {
		WebTarget applicationTarget = getWebTarget("/annotated");

		Invocation.Builder builder = authorize(
			applicationTarget.request(), getToken("oauthTestApplicationAfter"));

		Assert.assertEquals("everything.readonly", builder.get(String.class));

		builder = authorize(
			applicationTarget.request(),
			getToken("oauthTestApplicationBefore"));

		Response response = builder.get();

		Assert.assertEquals(403, response.getStatus());
	}

	public static class AnnotatedApplicationBundleActivator
		extends BaseTestActivator {

		@Override
		protected List<Oauth2Runnable> getTestRunnables() throws Exception {
			long defaultCompanyId = PortalUtil.getDefaultCompanyId();

			User user = UserTestUtil.getAdminUser(defaultCompanyId);

			Dictionary<String, Object> properties = new Hashtable<>();

			properties.put("oauth2.test.application", true);
			properties.put("oauth2.scopechecker.type", "annotations");

			return Arrays.asList(
				createOauth2Application(
					defaultCompanyId, user, "oauthTestApplicationBefore"),
				registerJaxRsApplication(
					new TestAnnotatedApplication(), properties),
				createOauth2Application(
					defaultCompanyId, user, "oauthTestApplicationAfter"));
		}

	}

}