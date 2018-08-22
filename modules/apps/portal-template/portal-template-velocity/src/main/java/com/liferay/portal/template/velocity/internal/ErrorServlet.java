package com.liferay.portal.template.velocity.internal;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component(
	service=Servlet.class,
	property = {
		"osgi.http.whiteboard.context.select=(osgi.http.whiteboard.context.name=*)",
		"osgi.http.whiteboard.servlet.errorPage=java.lang.Throwable"
	},
	scope = ServiceScope.PROTOTYPE
)
public class ErrorServlet extends HttpServlet {

	@Override
	protected void service(
		HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		writer.print("Error!");
		writer.flush();
		super.service(req, resp);
	}

	@Activate
	private void activate(){
		System.out.println("Activated!");
	}
}

