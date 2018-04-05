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

package com.liferay.oauth2.provider.scope.internal.jaxrs;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

/**
 * @author Carlos Sierra Andrés
 */
public class RunnableExecutorContainerResponseFilter
	implements ContainerResponseFilter {

	public RunnableExecutorContainerResponseFilter(Runnable action) {
		_action = action;
	}

	@Override
	public void filter(
			ContainerRequestContext requestContext,
			ContainerResponseContext responseContext)
		throws IOException {

		_action.run();
	}

	private final Runnable _action;

}