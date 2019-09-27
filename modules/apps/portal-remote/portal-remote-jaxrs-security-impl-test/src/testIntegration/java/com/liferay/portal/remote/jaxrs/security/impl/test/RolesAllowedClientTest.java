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

package com.liferay.portal.remote.jaxrs.security.impl.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Dictionary;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

import javax.xml.bind.DatatypeConverter;

import org.apache.cxf.jaxrs.client.spec.ClientBuilderImpl;
import org.apache.cxf.jaxrs.impl.RuntimeDelegateImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Carlos Sierra Andrés
 */
@RunWith(Arquillian.class)
public class RolesAllowedClientTest extends BaseJaxrsSecurityClientTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		Dictionary<String, Object> properties = new HashMapDictionary<>();

		properties.put("osgi.jaxrs.name", "test-roles-allowed");

		registerJaxRsApplication(
			new RolesAllowedTestApplication(), "roles-url", properties);
	}

	@Test
	public void testRolesAllowedApplication() throws Exception {
		User user = addUser();

		String credentials = StringBundler.concat(
			user.getEmailAddress(), ":", user.getPasswordUnencrypted());

		WebTarget target = _getWebTarget();

		Response response = _makeRequest(credentials, target);

		Assert.assertEquals("get", response.readEntity(String.class));

		response = _makeRequest(credentials, target.path("protected"));

		Assert.assertEquals(403, response.getStatus());

		response = _makeRequest(
			"test@liferay.com:test", target.path("protected"));

		Assert.assertEquals("only admin", response.readEntity(String.class));
	}

	private WebTarget _getWebTarget() {
		ClientBuilder clientBuilder = new ClientBuilderImpl();

		Client client = clientBuilder.build();

		RuntimeDelegate runtimeDelegate = new RuntimeDelegateImpl();

		UriBuilder uriBuilder = runtimeDelegate.createUriBuilder();

		WebTarget target = client.target(
			uriBuilder.uri("http://localhost:8080"));

		target = target.path("o");
		target = target.path("roles-url");

		return target;
	}

	private Response _makeRequest(String credentials, WebTarget target) {
		return target.request(
		).header(
			"Authorization",
			"BASIC " +
				DatatypeConverter.printBase64Binary(credentials.getBytes())
		).get();
	}

}