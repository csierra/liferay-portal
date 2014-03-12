package com.liferay.spring.extender.hibernate.configuration;

import com.liferay.portal.spring.hibernate.PortletHibernateConfiguration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Miguel Pastor
 */
public class OsgiBundleHibernateConfiguration
	extends PortletHibernateConfiguration implements ApplicationContextAware {

	@Override
	public void setApplicationContext(
		ApplicationContext applicationContext)
		throws BeansException {

		_classLoader = applicationContext.getClassLoader();
	}

	@Override
	protected ClassLoader getConfigurationClassLoader() {
		return _classLoader;
	}

	@Override
	protected String[] getConfigurationResources() {
		return new String[] {"META-INF/module-hbm.xml"};
	}

	private ClassLoader _classLoader;

}
