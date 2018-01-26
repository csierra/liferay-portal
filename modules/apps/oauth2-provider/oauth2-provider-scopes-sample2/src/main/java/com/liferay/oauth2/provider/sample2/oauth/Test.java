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

package com.liferay.oauth2.provider.sample2.oauth;

import com.liferay.oauth2.provider.scopes.api.RequiresScope;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;

import java.util.Collections;
import java.util.Set;

@ApplicationPath("/sample2")
@Component(
	immediate = true,
	service = Application.class,
	property = {
		"oauth2.scopechecker.type=annotations",
		"requires.oauth2=true"
	}
)
public class Test extends Application {

	@Override
	public Set<Object> getSingletons() {
		return Collections.singleton(this);
	}

	@GET
	@RequiresScope("everything.readonly")
	public String hello() {
		return "hello";
	}

	@Path("/everything")
	@GET
	@RequiresScope("everything")
	public String helloEverything() {
		return "hello everything";
	}

}