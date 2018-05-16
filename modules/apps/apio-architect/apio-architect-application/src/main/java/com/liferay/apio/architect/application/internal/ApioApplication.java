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

package com.liferay.apio.architect.application.internal;

import static org.osgi.service.component.annotations.ReferenceCardinality.MULTIPLE;
import static org.osgi.service.component.annotations.ReferencePolicy.DYNAMIC;
import static org.osgi.service.component.annotations.ReferencePolicyOption.GREEDY;

import com.liferay.apio.architect.endpoint.RootEndpoint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Registers the application's root endpoint, writers, and mappers in JAX-RS.
 *
 * <p>
 * The default {@link RootEndpoint} already deploys
 * all components that implement the representor. Developers only need to
 * provide a valid {@code RootEndpoint} via this class if they want to customize
 * the default behavior.
 * </p>
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra
 * @author Jorge Ferrer
 */
@Component(
	immediate = true,
	property = {
		"liferay.apio.architect.application=true",
		"osgi.jaxrs.application.base=/api-whiteboard"
	},
	service = Application.class
)
public class ApioApplication extends Application {

}