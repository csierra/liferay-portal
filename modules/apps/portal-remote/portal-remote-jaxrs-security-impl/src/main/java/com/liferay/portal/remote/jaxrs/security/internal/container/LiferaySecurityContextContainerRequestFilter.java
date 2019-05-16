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

package com.liferay.portal.remote.jaxrs.security.internal.container;

import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.access.control.AccessControlUtil;
import com.liferay.portal.kernel.security.auth.AccessControlContext;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierResult;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.servlet.ProtectedPrincipal;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;

import java.io.IOException;

import java.security.Principal;

import java.util.List;
import java.util.Map;

import javax.annotation.Priority;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	property = {
		JaxrsWhiteboardConstants.JAX_RS_APPLICATION_SELECT + "=(!(liferay.security.context=false))",
		JaxrsWhiteboardConstants.JAX_RS_EXTENSION + "=true",
		JaxrsWhiteboardConstants.JAX_RS_NAME + "=Liferay.Security.Context"
	},
	scope = ServiceScope.PROTOTYPE, service = ContainerRequestFilter.class
)
@PreMatching
@Priority(Priorities.AUTHENTICATION - 10)
public class LiferaySecurityContextContainerRequestFilter
	implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext containerRequestContext)
		throws IOException {

		containerRequestContext.setSecurityContext(
			new LiferaySecurityContext());
	}

	@Context
	private HttpServletRequest _httpServletRequest;

	@Reference
	private Portal _portal;

	private class LiferaySecurityContext implements SecurityContext {

		@Override
		public String getAuthenticationScheme() {
			AccessControlContext accessControlContext =
				AccessControlUtil.getAccessControlContext();

			if (accessControlContext == null) {
				return null;
			}

			AuthVerifierResult authVerifierResult =
				accessControlContext.getAuthVerifierResult();

			if (authVerifierResult == null) {
				return null;
			}

			if (AuthVerifierResult.State.SUCCESS.equals(
					authVerifierResult.getState())) {

				Map<String, Object> settings = authVerifierResult.getSettings();

				return MapUtil.getString(settings, "auth.type");
			}

			return null;
		}

		@Override
		public Principal getUserPrincipal() {
			PermissionChecker permissionChecker =
				PermissionThreadLocal.getPermissionChecker();

			if (permissionChecker == null) {
				return null;
			}

			return new ProtectedPrincipal(
				String.valueOf(permissionChecker.getUserId()));
		}

		@Override
		public boolean isSecure() {
			return _portal.isSecure(_httpServletRequest);
		}

		@Override
		public boolean isUserInRole(String roleName) {
			PermissionChecker permissionChecker =
				PermissionThreadLocal.getPermissionChecker();

			if (permissionChecker == null) {
				return false;
			}

			User user = permissionChecker.getUser();

			List<Role> roles = user.getRoles();

			for (Role role : roles) {
				if (roleName.equals(role.getName())) {
					return true;
				}
			}

			return false;
		}

	}

}