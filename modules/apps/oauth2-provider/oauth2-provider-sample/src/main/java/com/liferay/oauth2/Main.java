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

package com.liferay.oauth2;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;

import javax.ws.rs.GET;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.RuntimeDelegate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {
	public static void main(String[] args) {
		Application application = new Application() {

			@Override
			public Set<Object> getSingletons() {
				return new HashSet<>(
					Arrays.asList(
						new MyFeature(),
						this));
			}

			@Override
			public Map<String, Object> getProperties() {
				return new HashMap<String, Object>() {{
					put("melon", "chino");
				}};
			}

			@GET
			public String hello() {
				return "hello";
			}
		};

		JAXRSServerFactoryBean bean =
			RuntimeDelegate.getInstance().createEndpoint(
				application, JAXRSServerFactoryBean.class);

		Server server = bean.create();

		server.start();

		server.stop();
	}

	@Provider
	private static class MyFeature implements Feature {

		@Override
		public boolean configure(FeatureContext context) {
			System.out.println(
				"PROPERTIES: " + context.getConfiguration().getProperties());

			return false;
		}

	}

	@Provider
	private static class ConfigurationFeature implements Feature {

		@Override
		public boolean configure(FeatureContext context) {
			context.property("janderpora", "atitican");
		}

	}
}
