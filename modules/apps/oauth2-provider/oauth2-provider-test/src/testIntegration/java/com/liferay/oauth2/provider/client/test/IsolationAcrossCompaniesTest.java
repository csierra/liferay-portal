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
import com.liferay.oauth2.provider.test.internal.activator.configuration.BaseTestPreparatorBundleActivator;
import com.liferay.portal.kernel.concurrent.test.TestUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author Carlos Sierra Andrés
 */
@RunAsClient
@RunWith(Arquillian.class)
public class IsolationAcrossCompaniesTest extends BaseClientTest {

	@Deployment
	public static Archive<?> getDeployment() throws Exception {
		return BaseClientTest.getDeployment(
			IsolationAccrossCompaniesTestPreparator.class);
	}

	@Test
	public void test() throws Exception {
		WebTarget applicationTarget = getWebTarget("/annotated");

		String tokenForHost1 = getToken("oauthTestApplication", "host1.xyz");

		Invocation.Builder builder = authorize(
			applicationTarget.request(), tokenForHost1);

		builder = builder.header("Host", "host1.xyz");

		Assert.assertEquals("everything.readonly", builder.get(String.class));

		builder = builder.header("Host", "host2.xyz");

		Response response = builder.get();

		Assert.assertEquals(400, response.getStatus());
	}

	public static class IsolationAccrossCompaniesTestPreparator
		extends BaseTestPreparatorBundleActivator {

		@Override
		protected void prepareTest() throws Exception {
			Dictionary<String, Object> properties = new Hashtable<>();

			properties.put("oauth2.scopechecker.type", "annotations");

			registerJaxRsApplication(
				new TestAnnotatedApplication(), "annotated", properties);

			Company company1 = createCompany("host1");

			createOauth2Application(
				company1.getCompanyId(),
				UserTestUtil.getAdminUser(company1.getCompanyId()),
				"oauthTestApplication");

			Company company2 = createCompany("host2");

			createOauth2Application(
				company2.getCompanyId(),
				UserTestUtil.getAdminUser(company2.getCompanyId()),
				"oauthTestApplication");
		}

	}

}