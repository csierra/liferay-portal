package com.liferay.spring.extender.context.event;

import com.liferay.portal.bean.BeanLocatorImpl;
import com.liferay.portal.kernel.bean.BeanLocator;
import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.util.Portal;
import com.liferay.spring.extender.classloader.BundleResolverClassLoader;

import org.eclipse.gemini.blueprint.context.event.OsgiBundleApplicationContextEvent;
import org.eclipse.gemini.blueprint.context.event.OsgiBundleApplicationContextListener;
import org.eclipse.gemini.blueprint.context.event.OsgiBundleContextClosedEvent;
import org.eclipse.gemini.blueprint.context.event.OsgiBundleContextRefreshedEvent;

import org.osgi.framework.Bundle;

/**
 * @author Miguel Pastor
 */
public class BeanLocatorLifecycleManager
	implements OsgiBundleApplicationContextListener {

	@Override
	public void onOsgiApplicationEvent(
		OsgiBundleApplicationContextEvent event) {

		String symbolicName = event.getBundle().getSymbolicName();

		if (event instanceof OsgiBundleContextRefreshedEvent) {
			PortletBeanLocatorUtil.setBeanLocator(
				_buildBeanLocatorName(symbolicName), _buildBeanLocator(event));
		}
		else if (event instanceof OsgiBundleContextClosedEvent) {
			OsgiBundleContextClosedEvent closedEvent =
				(OsgiBundleContextClosedEvent)event;

			Throwable error = closedEvent.getFailureCause();

			if (error != null) {
				PortletBeanLocatorUtil.setBeanLocator(
					_buildBeanLocatorName(symbolicName), null);
			}
		}
	}

	private BeanLocator _buildBeanLocator(
		OsgiBundleApplicationContextEvent event) {

		Bundle bundle = event.getBundle();

		ClassLoader classLoader = new BundleResolverClassLoader(bundle);

		return new BeanLocatorImpl(classLoader, event.getApplicationContext());
	}

	private String _buildBeanLocatorName(String bundleSymbolicName) {
		return (Portal.PATH_MODULE + "/" + bundleSymbolicName).substring(1);
	}
}