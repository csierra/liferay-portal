package com.liferay.spring.extender.hibernate.configuration;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Miguel Pastor
 */
public class OsgiBundleHibernateConfigurationTest {

	@Test
	public void testConfigurationPaths() {
		String[] configurationResources =
			_configuration.getConfigurationResources();

		Assert.assertEquals(1, configurationResources.length);
		Assert.assertEquals(
			"META-INF/module-hbm.xml",
			configurationResources[0]);
	}

	private OsgiBundleHibernateConfiguration _configuration =
		new OsgiBundleHibernateConfiguration();

}