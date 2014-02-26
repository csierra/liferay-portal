package com.liferay.spring.extender.internal;

import com.liferay.spring.extender.SpringExtender;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceReference;
import org.springframework.context.ApplicationContext;

/**
 * @author Miguel Pastor
 */
public class SpringExtenderActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		ServiceReference<ApplicationContext> portalAppContextServiceReference =
			context.getServiceReference(ApplicationContext.class);

		ApplicationContext portalApplicationContext = context.getService(
			portalAppContextServiceReference);

		_bundleListener = new SpringExtender(portalApplicationContext);

		context.addBundleListener(_bundleListener);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		context.removeBundleListener(_bundleListener);
	}

	private BundleListener _bundleListener;

}
