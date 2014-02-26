package com.liferay.spring.extender.context;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Miguel Pastor
 */
public class OsgiBundleApplicationContext extends
	ClassPathXmlApplicationContext {

	public OsgiBundleApplicationContext(
		ApplicationContext parentApplicationContext, String[] configLocations,
		ClassLoader classLoader) {

		super(configLocations, false, parentApplicationContext);

		_classLoader = classLoader;
	}


	@Override
	protected void initBeanDefinitionReader(XmlBeanDefinitionReader reader) {
		super.initBeanDefinitionReader(reader);

		// TODO Is this required?
		// reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);

		reader.setBeanClassLoader(_classLoader);
	}

	private ClassLoader _classLoader;

}
