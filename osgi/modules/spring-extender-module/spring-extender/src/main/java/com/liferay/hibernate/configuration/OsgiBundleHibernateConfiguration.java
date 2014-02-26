package com.liferay.hibernate.configuration;

import com.liferay.portal.spring.hibernate.PortalHibernateConfiguration;

/**
 * @author Miguel Pastor
 */
public class OsgiBundleHibernateConfiguration extends
	PortalHibernateConfiguration {

	@Override
	protected ClassLoader getConfigurationClassLoader() {
		return OsgiBundleHibernateConfiguration.class.getClassLoader();
	}

}
