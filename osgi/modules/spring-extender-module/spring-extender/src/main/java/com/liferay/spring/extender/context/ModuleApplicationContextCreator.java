package com.liferay.spring.extender.context;

import org.eclipse.gemini.blueprint.context.DelegatedExecutionOsgiBundleApplicationContext;
import org.eclipse.gemini.blueprint.context.support.OsgiBundleXmlApplicationContext;
import org.eclipse.gemini.blueprint.extender.OsgiApplicationContextCreator;

import org.osgi.framework.BundleContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * @author Miguel Pastor
 */
public class ModuleApplicationContextCreator
	implements OsgiApplicationContextCreator {

	@Override
	public DelegatedExecutionOsgiBundleApplicationContext
			createApplicationContext(BundleContext bundleContext)
		throws Exception {

		ApplicationContext parentAppContext = new ClassPathXmlApplicationContext(
			"/META-INF/spring/parent/*.xml");

		String configs =
			bundleContext.getBundle().getHeaders().get("Spring-Context");

		System.out.println("Creating app context for " + bundleContext.getBundle().getSymbolicName() + " with location " + configs);

		if (configs == null) {
			return null;
		}

		return new OsgiBundleXmlApplicationContext(
			configs.split(","), parentAppContext);
	}

}
