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

package com.liferay.oauth2.provider.sample.oauth;

import com.liferay.portal.kernel.util.ResourceBundleLoader;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component(service = Application.class)
public class Test extends Application{

	public String method() {
		return "hello";
	}

	@Override
	public Set<Object> getSingletons() {
		return new HashSet<>(
			Arrays.asList(new MyContainerRequestFilter(), new MyResource()));
	}

	@Override
	public Set<Class<?>> getClasses() {
		return Collections.singleton(MyFeature.class);
	}

	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> properties = new HashMap<>();

		properties.put("scopes.resource.loader", _resourceBundleLoader);

		return properties;
	}

	@Provider
	public class MyFeature implements Feature {

		@Override
		public boolean configure(FeatureContext context) {
			Configuration configuration =
				context.getConfiguration();



			return true;
		}

		@Context
		Application _application;
	}

	@Provider
	public static class MyDynamicFeature implements DynamicFeature {

		@Override
		public void configure(
			ResourceInfo resourceInfo, FeatureContext context) {

			Map<String, Object> properties =
				context.getConfiguration().getProperties();

			if (properties.containsKey("scopes.resource.loader")) {

			}
		}
	}

	@Provider
	public static class MyContainerRequestFilter implements
		ContainerRequestFilter {

		@Override
		public void filter(ContainerRequestContext requestContext) throws
			IOException {
			System.out.println(
				"METHOD: " + _resourceInfo.getResourceMethod());
			System.out.println(
				"CLASS: " + _resourceInfo.getResourceClass());
			System.out.println("APPLICATION:" + _application);
		}

		@Context
		ResourceInfo _resourceInfo;

		@Context
		Application _application;
	}

	public class MyResource {

		@Path("/sub")
		public SubResource getSub() {
			return new SubResource();
		}

	}

	public class SubResource {

		@GET
		public String get() {
			return "hello subresource";
		}

	}

	@Reference ResourceBundleLoader _resourceBundleLoader;
}
