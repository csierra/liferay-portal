package com.liferay.spring.extender.context;

import com.liferay.hibernate.configuration.OsgiBundleHibernateConfiguration;
import com.liferay.hibernate.session.OsgiBundleSessionFactory;
import com.liferay.portal.spring.aop.ServiceBeanAutoProxyCreator;
import com.liferay.portal.spring.aop.ServiceBeanMatcher;
import com.liferay.portal.spring.bean.BeanReferenceAnnotationBeanPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanNameReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author Miguel Pastor
 */
public class OsgiBundleBeanDefinitionPostProcessor implements
	BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanDefinitionRegistry(
			BeanDefinitionRegistry beanDefinitionRegistry)
		throws BeansException {

		BeanDefinition referenceAnnotationBean =
			_buildReferenceAnnotationBean();

		beanDefinitionRegistry.registerBeanDefinition(
			"referenceAnnotationBean", referenceAnnotationBean);

		BeanDefinition autoProxyCreator = buildAutoProxyCreator();

		beanDefinitionRegistry.registerBeanDefinition(
			"autoProxyCreator", autoProxyCreator);

		BeanDefinition sessionFactory = _buildBundleHibernateSessionFactory();

		beanDefinitionRegistry.registerBeanDefinition(
			"sessionFactory", sessionFactory);
	}

	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory configurableListableBeanFactory)
		throws BeansException {
	}

	private BeanDefinition buildAutoProxyCreator() {
		GenericBeanDefinition autoProxyCreatorBean =
			new GenericBeanDefinition();

		autoProxyCreatorBean.setBeanClassName(
			ServiceBeanAutoProxyCreator.class.getName());

		MutablePropertyValues mutablePropertyValues =
			new MutablePropertyValues();

		BeanDefinition serviceBeanMatcher = new GenericBeanDefinition();
		serviceBeanMatcher.setBeanClassName(ServiceBeanMatcher.class.getName());

		mutablePropertyValues.addPropertyValue(
			"beanMatcher", serviceBeanMatcher);
		mutablePropertyValues.addPropertyValue(
			"methodInterceptor", new RuntimeBeanNameReference("serviceAdvice"));

		autoProxyCreatorBean.setPropertyValues(mutablePropertyValues);

		return autoProxyCreatorBean;
	}

	private BeanDefinition _buildBundleHibernateSessionFactory() {
		GenericBeanDefinition bundleSessionConfiguration =
			new GenericBeanDefinition();

		bundleSessionConfiguration.setBeanClassName(
			OsgiBundleHibernateConfiguration.class.getName());

		MutablePropertyValues mutablePropertyValues =
			new MutablePropertyValues();
		mutablePropertyValues.addPropertyValue(
			"dataSource", new RuntimeBeanNameReference("liferayDataSource"));

		bundleSessionConfiguration.setPropertyValues(mutablePropertyValues);

		GenericBeanDefinition bundleSessionFactory =
			new GenericBeanDefinition();

		bundleSessionFactory.setBeanClassName(
			OsgiBundleSessionFactory.class.getName());

		mutablePropertyValues = new MutablePropertyValues();

		mutablePropertyValues.addPropertyValue(
			"dataSource", new RuntimeBeanNameReference("liferayDataSource"));
		mutablePropertyValues.addPropertyValue(
			"sessionFactoryImplementor", bundleSessionConfiguration);

		bundleSessionFactory.setPropertyValues(mutablePropertyValues);

		return bundleSessionFactory;
	}

	private BeanDefinition _buildReferenceAnnotationBean() {
		Class<BeanReferenceAnnotationBeanPostProcessor> clazz =
			BeanReferenceAnnotationBeanPostProcessor.class;

		return new RootBeanDefinition(clazz);
	}

}
