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

import com.liferay.oauth2.provider.test.internal.TestApplication;
import com.liferay.oauth2.provider.test.internal.activator.configuration.BaseTestPreparatorBundleActivator;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author Carlos Sierra Andrés
 */
@RunAsClient
@RunWith(Arquillian.class)
public class AuthorizationCodeGrantClientTest extends BaseClientTest {

	@Deployment
	public static Archive<?> getDeployment() throws Exception {
		return BaseClientTest.getDeployment(
			AuthorizationCodeGrantTestPreparator.class);
	}

	@Test
	public void test() throws Exception {
		
		String clientId = "oauthTestApplication";
		String clientSecret = "oauthTestApplicationSecret";
		
		String redirectURI = "http://clienthost:8080";
		
		String code = executeCodeGrant(
			clientId, clientSecret, "localhost:8080",
			getAuthorizationCodeGrantExecutor(
				redirectURI, 
				"GET"), 
			this::parseAuthorizationCodeString);
		
		Assert.assertTrue(
			"Failed to get authorization code", 
			Validator.isNotNull(code));
		
		String accessToken = getTokenForAuthorizationCodeGrant(
			clientId, clientSecret, "localhost:8080",
			getCodeToAccessTokenExchangeExecutor(code, redirectURI),
			this::parseTokenString);
		
		Assert.assertTrue(
			"Failed to get access token", 
			Validator.isNotNull(accessToken));
	}
		
	public static class AuthorizationCodeGrantTestPreparator
		extends BaseTestPreparatorBundleActivator {

		@Override
		protected void prepareTest() throws Exception {
			long defaultCompanyId = PortalUtil.getDefaultCompanyId();

			User user = UserTestUtil.getAdminUser(defaultCompanyId);

			Dictionary<String, Object> properties = new Hashtable<>();

			properties.put("oauth2.test.application", true);

			registerJaxRsApplication(new TestApplication(), properties);

			registerTestAutoLogin(user);
			
			createOauth2Application(
				defaultCompanyId, user, "oauthTestApplication",
				Arrays.asList("HEAD", "GET", "OPTIONS", "POST"));
		}

	}

}