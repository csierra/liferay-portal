package com.liferay.hibernate.session;

import com.liferay.hibernate.configuration.OsgiBundleHibernateConfiguration;
import com.liferay.portal.dao.orm.hibernate.PortletSessionFactoryImpl;
import com.liferay.portal.dao.shard.ShardDataSourceTargetSource;
import com.liferay.portal.kernel.util.InfrastructureUtil;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;

/**
 * @author Miguel Pastor
 */
public class OsgiBundleSessionFactory extends PortletSessionFactoryImpl {

	@Override
	protected SessionFactory getSessionFactory() {
		ShardDataSourceTargetSource shardDataSourceTargetSource =
			(ShardDataSourceTargetSource)
				InfrastructureUtil.getShardDataSourceTargetSource();

		if (shardDataSourceTargetSource == null) {
			return getSessionFactoryImplementor();
		}

		DataSource dataSource = shardDataSourceTargetSource.getDataSource();

		// TODO Add proper caching (need to rewrite parent class)

		SessionFactory sessionFactory = null;

		OsgiBundleHibernateConfiguration osgiBundleHibernateConfiguration =
			new OsgiBundleHibernateConfiguration();

		osgiBundleHibernateConfiguration.setDataSource(dataSource);

		try {
			sessionFactory =
				osgiBundleHibernateConfiguration.buildSessionFactory();
		}
		catch (Exception e) {
			return null;
		}

		return sessionFactory;
	}

}