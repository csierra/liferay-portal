package com.liferay.spring.extender.context;

import com.liferay.spring.extender.blueprint.ModuleBeanFactoryPostProcessor;
import com.liferay.spring.extender.classloader.BundleResolverClassLoader;
import org.eclipse.gemini.blueprint.context.DelegatedExecutionOsgiBundleApplicationContext;
import org.eclipse.gemini.blueprint.context.support.OsgiBundleXmlApplicationContext;
import org.eclipse.gemini.blueprint.extender.OsgiApplicationContextCreator;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import org.osgi.framework.FrameworkUtil;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author Miguel Pastor
 */
public class ModuleApplicationContextCreator
	implements OsgiApplicationContextCreator {

	@Override
	public DelegatedExecutionOsgiBundleApplicationContext
			createApplicationContext(BundleContext bundleContext)
		throws Exception {

		String configs =
			bundleContext.getBundle().getHeaders().get("Spring-Context");

		if (configs == null) {
			return null;
		}

		Bundle bundle = bundleContext.getBundle();
		Bundle extenderBundle = _getExtenderBundle();

		ClassLoader classLoader = new BundleResolverClassLoader(
			bundle, extenderBundle);

		ApplicationContext parentAppContext = _buildParentContext(
			extenderBundle, classLoader);

		OsgiBundleXmlApplicationContext osgiBundleXmlApplicationContext =
			new OsgiBundleXmlApplicationContext(
				configs.split(","), parentAppContext);

		osgiBundleXmlApplicationContext.setBundleContext(bundleContext);

		osgiBundleXmlApplicationContext.addBeanFactoryPostProcessor(
			new ModuleBeanFactoryPostProcessor(classLoader));

		return osgiBundleXmlApplicationContext;
	}

	private ApplicationContext _buildParentContext(
			Bundle bundle, ClassLoader classLoader)
		throws IOException {

		GenericApplicationContext applicationContext =
			new GenericApplicationContext();

		XmlBeanDefinitionReader xmlBeanDefinitionReader =
			new XmlBeanDefinitionReader(applicationContext);

		xmlBeanDefinitionReader.setValidating(false);
		xmlBeanDefinitionReader.setValidationMode(
			XmlBeanDefinitionReader.VALIDATION_NONE);

		Enumeration<URL> entries = bundle.findEntries(
			"META-INF/spring/parent", "*.xml", true);

		applicationContext.setClassLoader(classLoader);

		while (entries.hasMoreElements()) {
			URL xmlBeanDefinitionUrl = entries.nextElement();

			InputStream inputStream = xmlBeanDefinitionUrl.openStream();

			xmlBeanDefinitionReader.loadBeanDefinitions(
				new InputStreamResource(inputStream));
		}

		applicationContext.refresh();

		return applicationContext;
	}

	private Bundle _getExtenderBundle() {
		return FrameworkUtil.getBundle(ModuleApplicationContextCreator.class);
	}

}
