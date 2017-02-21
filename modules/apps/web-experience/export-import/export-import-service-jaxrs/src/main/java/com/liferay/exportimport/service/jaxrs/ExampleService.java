package com.liferay.exportimport.service.jaxrs;

import org.osgi.service.component.annotations.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Component(
	immediate = true, property = {"staging.jax.rs.service=true"},
	service = ExampleService.class
)
@Path("/ExampleService")
public class ExampleService {

	@GET
	public Response myCall() {

		return Response.status(200).entity("myCall is called").build();
	}
}