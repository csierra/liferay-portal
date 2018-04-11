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

import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.provider.json.JSONProvider;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.w3c.dom.Document;

/**
 * @author Carlos Sierra Andrés
 */
@RunAsClient
@RunWith(Arquillian.class)
public class Oauth2ProviderTest {

	@Test
	public void testWebServiceContextAppearsInTheSummary() throws Exception {
		Client client = ClientBuilder.newClient().register(JSONProvider.class);

		WebTarget tokenTarget = client.target(
			_url.toURI()).path("o").path("oauth2").path("token");

		MultivaluedHashMap<String, String> formData =
			new MultivaluedHashMap<>();

		formData.add("client_id", "oauthTestApplicationAfter");
		formData.add("client_secret", "oauthTestApplicationSecret");
		formData.add("grant_type", "client_credentials");

		Response tokenResponse =
			tokenTarget.request().post(Entity.form(formData));

		Document document = tokenResponse.readEntity(Document.class);

		String tokenString = document.getElementsByTagName(
			"access_token").item(0).getTextContent();

		WebTarget applicationTarget =
			client.target(_url.toURI()).path("o").path("oauth2-test").path(
				"/annotated");

		String responseString =
			applicationTarget.request().header("Authorization", "Bearer " + tokenString).get(String.class);

		Assert.assertEquals("everything.readonly", responseString);

		tokenTarget = client.target(
			_url.toURI()).path("o").path("oauth2").path("token");

		formData = new MultivaluedHashMap<>();

		formData.add("client_id", "oauthTestApplicationBefore");
		formData.add("client_secret", "oauthTestApplicationSecret");
		formData.add("grant_type", "client_credentials");

		tokenResponse =
			tokenTarget.request().post(Entity.form(formData));

		document = tokenResponse.readEntity(Document.class);

		tokenString = document.getElementsByTagName(
			"access_token").item(0).getTextContent();

		applicationTarget =
			client.target(_url.toURI()).path("o").path("oauth2-test").path(
				"/annotated");

		Response response =
			applicationTarget.request().header("Authorization", "Bearer " + tokenString).get();

		Assert.assertEquals(403, response.getStatus());
	}

	@ArquillianResource
	private URL _url;

}