package com.liferay.exportimport.service.jaxrs;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;

import java.util.Collections;
import java.util.Set;

@Component(
	immediate = true, property = {"staging.jaxrs.application=true"},
	service=Application.class
)
@Path("/ExampleService")
public class ExampleService extends Application {

	public Set<Object> getSingletons() {
		return Collections.<Object>singleton(this);
	}

	@GET
	public Response myCall() {
		return Response.status(200).entity("myCall is called").build();
	}

}