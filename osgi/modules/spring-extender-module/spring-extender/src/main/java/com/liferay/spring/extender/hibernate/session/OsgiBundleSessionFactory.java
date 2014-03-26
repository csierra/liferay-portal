package com.liferay.spring.extender.hibernate.session;

import com.liferay.portal.dao.orm.hibernate.PortletSessionFactoryImpl;
import com.liferay.portal.dao.shard.ShardDataSourceTargetSource;
import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.spring.extender.classloader.BundleResolverClassLoader;
import com.liferay.spring.extender.hibernate.configuration.OsgiBundleHibernateConfiguration;

import javax.sql.DataSource;

import org.eclipse.gemini.blueprint.context.BundleContextAware;

import org.hibernate.SessionFactory;

import org.osgi.framework.BundleContext;

/**
 * @author Miguel Pastor
 */
public class OsgiBundleSessionFactory extends PortletSessionFactoryImpl
	implements BundleContextAware {

	@Override
	public ClassLoader getSessionFactoryClassLoader() {
		return _classLoader;
	}

	@Override
	public void setBundleContext(BundleContext bundleContext) {
		_classLoader = new BundleResolverClassLoader(
			bundleContext.getBundle(), null);
	}

	@Override
	protected SessionFactory createSessionFactory(DataSource dataSource) {
		OsgiBundleHibernateConfiguration osgiBundleHibernateConfiguration =
			new OsgiBundleHibernateConfiguration();

		osgiBundleHibernateConfiguration.setDataSource(dataSource);

		SessionFactory sessionFactory;

		try {
			sessionFactory =
				osgiBundleHibernateConfiguration.buildSessionFactory();
		}
		catch (Exception e) {
			return null;
		}

		return sessionFactory;
	}

	private ClassLoader _classLoader;

}