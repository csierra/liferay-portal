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

package com.liferay.oauth2.provider.rest;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.servlet.ProtectedServletRequest;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import org.apache.cxf.rs.security.oauth2.provider.AccessTokenGrantHandler;
import org.apache.cxf.rs.security.oauth2.provider.OAuthJSONProvider;
import org.apache.cxf.rs.security.oauth2.services.AccessTokenService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component(
	immediate = true,
	service = Application.class
)
@ApplicationPath("/")
public class OAuth2EndpointApplication extends Application {

	@Activate
	public void activate(
			BundleContext bundleContext, final Map<String, Object> properties)
		throws IOException {

		ServiceReference<ConfigurationAdmin> serviceReference =
			bundleContext.getServiceReference(ConfigurationAdmin.class);

		try {
			ConfigurationAdmin configurationAdmin = bundleContext.getService(
				serviceReference);

			String contextPath = MapUtil.getString(
				properties, "contextPath", "/oauth2");

			Configuration[] cxfConfigurations =
				configurationAdmin.listConfigurations(
					"(&(service.factoryPid=com.liferay.portal.remote.cxf." +
					"common.configuration.CXFEndpointPublisherConfiguration)" +
					"(contextPath=" + escapeFilterArgument(contextPath) + "))");

			if (cxfConfigurations != null) {
				for (Configuration configuration : cxfConfigurations) {
					configuration.delete();
				}
			}

			_cxfConfiguration = configurationAdmin.createFactoryConfiguration(
				"com.liferay.portal.remote.cxf.common.configuration." +
				"CXFEndpointPublisherConfiguration",
				"?");

			Dictionary<String, Object> dictionary = new Hashtable<>();

			dictionary.put("contextPath", contextPath);

			_cxfConfiguration.update(dictionary);

			String restComponentNameFilter =
				"(component.name=" + getClass().getName() + ")";

			Configuration[] restConfigurations =
				configurationAdmin.listConfigurations(
					"(&(service.factoryPid=com.liferay.portal.remote.rest." +
					"extender.configuration.RestExtenderConfiguration)" +
					"(jaxRsApplicationFilterStrings=" +
					escapeFilterArgument(restComponentNameFilter) + "))");

			if	(restConfigurations != null) {
				for (Configuration configuration : restConfigurations) {
					configuration.delete();
				}
			}

			_restConfiguration = configurationAdmin.createFactoryConfiguration(
				"com.liferay.portal.remote.rest.extender.configuration." +
				"RestExtenderConfiguration",
				"?");

			dictionary = new Hashtable<>();

			dictionary.put("contextPaths", new String[]{contextPath});
			dictionary.put(
				"jaxRsApplicationFilterStrings",
				new String[]{restComponentNameFilter});

			_restConfiguration.update(dictionary);

			dictionary = new Hashtable<>();

			String contextName = contextPath.substring(1);

			contextName = contextName.replace("/", ".");

			dictionary.put(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT,
				contextName);
			dictionary.put(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN,
				new String[]{"/authorize", "/authorize/*"});

			_authorizeEndpointFilterServiceRegistration =
				bundleContext.registerService(
					Filter.class, new Filter() {
						@Override
						public void init(FilterConfig filterConfig)
							throws ServletException {

						}

						@Override
						public void doFilter(
							ServletRequest servletRequest,
							ServletResponse servletResponse,
							FilterChain chain)
							throws IOException, ServletException {

							HttpServletRequest request =
								(HttpServletRequest) servletRequest;

							try {
								User user = _portal.getUser(request);

								if ((user != null) && !user.isDefaultUser()) {
									request = new ProtectedServletRequest(
										request,
										String.valueOf(user.getUserId()),
										null);

									chain.doFilter(request, servletResponse);

									return;
								}

								String loginPage = MapUtil.getString(
									properties, "loginPage");

								if (Validator.isBlank(loginPage)) {
									StringBundler sb = new StringBundler();
									sb.append(_portal.getPortalURL(request));
									sb.append(_portal.getPathContext());
									sb.append(_portal.getPathMain());
									sb.append("/portal/login");
									loginPage = sb.toString();
								}

								StringBundler redirect = new StringBundler();
								redirect.append(request.getRequestURI());
								redirect.append("?");
								redirect.append(request.getQueryString());

								loginPage = HttpUtil.addParameter(
									loginPage, "redirect", redirect.toString());

								HttpServletResponse response =
									(HttpServletResponse) servletResponse;

								response.sendRedirect(loginPage);
							}
							catch (Exception e) {
								_log.error(
									"Unable to resolve authenticated user", e);
							}
						}

						@Override
						public void destroy() {

						}
					}, dictionary);


		}
		catch (InvalidSyntaxException e) {
			_log.error(e);
		}
		finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	@Deactivate
	public void deactivate() {
		_authorizeEndpointFilterServiceRegistration.unregister();

		try {
			_cxfConfiguration.delete();
		}
		catch (IOException e) {
			_log.error(e);
		}
		try {
			_restConfiguration.delete();
		}
		catch (IOException e) {
			_log.error(e);
		}
	}

	@Override
	public Set<Object> getSingletons() {
		AccessTokenService accessTokenService = new AccessTokenService();

		accessTokenService.setBlockUnsecureRequests(true);
		accessTokenService.setCanSupportPublicClients(true);
		accessTokenService.setDataProvider(_liferayOAuthDataProvider);
		accessTokenService.setGrantHandlers(_accessTokenGrantHandlers);

		ArrayList<Object> endpoints = new ArrayList<>(_liferayOauth2Endpoints);
		endpoints.addAll(
			Arrays.asList(accessTokenService, _authorizationMessageBodyWriter));

		return new HashSet<>(endpoints);
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>(_liferayOAuth2Classes);

		classes.add(OAuthJSONProvider.class);

		return classes;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2EndpointApplication.class);

	@Reference(
		cardinality = ReferenceCardinality.AT_LEAST_ONE,
		policyOption = ReferencePolicyOption.GREEDY,
		unbind = "removeAccessTokenGrantHandler"
	)
	public void addAccessTokenGrantHandler(AccessTokenGrantHandler accessTokenGrantHandler) {
		_accessTokenGrantHandlers.add(accessTokenGrantHandler);
	}
	
	public void removeAccessTokenGrantHandler(AccessTokenGrantHandler accessTokenGrantHandler) {
		_accessTokenGrantHandlers.remove(accessTokenGrantHandler);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(liferay.oauth2.class=true)",
		unbind = "removeOAuth2Class"
	)
	public void addOAuth2Class(Class<?> cls) {
		_liferayOAuth2Classes.add(cls);
	}

	public void removeOAuth2Class(Class<?> cls) {
		_liferayOAuth2Classes.remove(cls);
	}

	@Reference(
		cardinality = ReferenceCardinality.AT_LEAST_ONE,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(liferay.oauth2.endpoint=true)",
		unbind = "removeOauth2Endpoint"
	)
	public void addOauth2Endpoint(Object endpoint) {
		_liferayOauth2Endpoints.add(endpoint);
	}

	public void removeOauth2Endpoint(Object endpoint) {
		_liferayOauth2Endpoints.remove(endpoint);
	}

	private String escapeFilterArgument(String filter) {
		return StringUtil.replace(
			filter, new String[]{"\\", "(", ")"},
			new String[]{"\\\\", "\\(", "\\)"});
	}

	private List<Object> _liferayOauth2Endpoints = new ArrayList<>();
	private List<Class<?>> _liferayOAuth2Classes = new ArrayList<>();

	private List<AccessTokenGrantHandler> _accessTokenGrantHandlers = new ArrayList<>();

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private LiferayOAuthDataProvider _liferayOAuthDataProvider;

	@Reference
	private AuthorizationMessageBodyWriter _authorizationMessageBodyWriter;

	@Reference
	private Portal _portal;

	private ServiceRegistration<Filter>
		_authorizeEndpointFilterServiceRegistration;
	private Configuration _cxfConfiguration;
	private Configuration _restConfiguration;

}