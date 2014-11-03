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

package com.liferay.portal.viex.extender;

import com.liferay.kernel.servlet.taglib.TagExtension;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true)
public class ViewBundleTracker {

	@Activate
	public void activate(ComponentContext context) {
		_bundleContext = context.getBundleContext();

		Dictionary<String, Object> properties = new Hashtable<String, Object>();

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME,
			"view-extensions");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH,
			"/view-extensions");

		_servletContextHelperRegistration = _bundleContext.registerService(
			ServletContextHelper.class, new ServletContextHelper() {
				@Override
				public URL getResource(String name) {
					String[] tokens = name.split("/");

					if (tokens.length == 4) {
						long bundleId = Long.parseLong(tokens[0]);

						Bundle bundle = _bundleContext.getBundle(bundleId);

						String extensionPath = bundle.getHeaders().get(
							"View-Extensions");

						return bundle.getEntry(
							extensionPath + tokens[1] + "/" + tokens[2] + "/" +
								tokens[3]);
					}

					throw new IllegalArgumentException(name);
				}
			}, properties
		);

		_bundleTracker = new BundleTracker<>(
			_bundleContext, Bundle.RESOLVED,
			new BundleTrackerCustomizer<Bundle>() {

			@Override
			public Bundle addingBundle(Bundle bundle, BundleEvent event) {
				Dictionary<String, String> headers = bundle.getHeaders();

				String extensionPath = headers.get("View-Extensions");

				if (extensionPath != null) {
					registerExtensions(extensionPath, bundle);

					return bundle;
				}

				return null;
			}

			@Override
			public void modifiedBundle(
				Bundle bundle, BundleEvent event, Bundle object) {

			}

			@Override
			public void removedBundle(
				Bundle bundle, BundleEvent event, Bundle object) {

				ServiceRegistration<TagExtension>
					tagExtensionServiceRegistration =
					_tagExtensionRegistrations.get(bundle.getBundleId());

				tagExtensionServiceRegistration.unregister();
			}
		});

	}

	@Deactivate
	public void deactivate() {
		_servletContextHelperRegistration.unregister();

		_bundleTracker.close();
	}

	private void registerExtensions(
		final String extensionPath, final Bundle bundle) {
		_tagExtensionRegistrations.put(bundle.getBundleId(),
			_bundleContext.registerService(TagExtension.class,
				new TagExtension() {
					@Override
					public void include(
						HttpServletRequest request,
						HttpServletResponse response, String tagClassName,
						String tagKey, String itemKey) {

						String requestPath =
							"/o/view-extensions/" + Long.toString(
								bundle.getBundleId()) + "/" + tagClassName +
								"/" + tagKey + "/" + itemKey;

						RequestDispatcher dispatcher =
							request.getRequestDispatcher(requestPath);

						if (dispatcher != null) {
							try {
								dispatcher.include(request, response);
							}
							catch (Exception e) {
								// TODO: DO SOMETHING SENSIBLE
							}
						}

					}

					@Override
					public void register(TagItemRegistry registry) {
						Enumeration<URL> entries = bundle.findEntries(
							extensionPath, "*.jsp", true);

						while (entries.hasMoreElements()) {
							URL url = entries.nextElement();

							String path = url.getPath().substring(
								extensionPath.length());

							String[] tokens = path.split("/");

							if (tokens.length == 3) {
								registry.register(
									tokens[0], tokens[1], tokens[2]);
							}
						}
					}
				}, null
			)
		);
	}

	private BundleContext _bundleContext;

	private BundleTracker<Bundle> _bundleTracker;

	private ConcurrentHashMap<Long, ServiceRegistration<TagExtension>>
		_tagExtensionRegistrations = new ConcurrentHashMap<>();

	private ServiceRegistration<ServletContextHelper>
		_servletContextHelperRegistration;

}
