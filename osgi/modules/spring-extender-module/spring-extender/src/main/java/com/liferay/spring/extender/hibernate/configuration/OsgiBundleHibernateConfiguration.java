package com.liferay.spring.extender.hibernate.configuration;

import com.liferay.portal.spring.hibernate.PortletHibernateConfiguration;

import com.liferay.spring.extender.classloader.BundleDelegatedClassLoader;
import org.eclipse.gemini.blueprint.context.BundleContextAware;

import org.osgi.framework.BundleContext;

/**
 * @author Miguel Pastor
 */
public class OsgiBundleHibernateConfiguration extends
	PortletHibernateConfiguration implements BundleContextAware {

	@Override
	protected ClassLoader getConfigurationClassLoader() {
		return _classLoader;
	}

	@Override
	protected String[] getConfigurationResources() {
		return new String[] {"META-INF/module-hbm.xml"};
	}

	@Override
	public void setBundleContext(BundleContext bundleContext) {
		_classLoader = new BundleDelegatedClassLoader(
			bundleContext.getBundle());
	}

	private ClassLoader _classLoader;

}
