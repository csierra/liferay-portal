package com.liferay.spring.extender;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.spring.context.PortletBeanFactoryPostProcessor;
import com.liferay.spring.extender.classloader.BundleDelegatedClassLoaderFactory;
import com.liferay.spring.extender.context.OsgiBundleApplicationContext;

import java.util.Dictionary;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Miguel Pastor
 */
public class SpringExtender implements BundleListener {

	public SpringExtender(ApplicationContext parentApplicationContext) {
		_parentApplicationContext = parentApplicationContext;
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		Bundle bundle = event.getBundle();

		Dictionary<String,String> headers = bundle.getHeaders();

		String springContextHeader = headers.get("Spring-Context");

		if (springContextHeader == null) {
			System.out.println(
				"Bundle " + bundle.getBundleId()  + " has no metadata defined");

			return;
		}

		switch (event.getType()) {

			case BundleEvent.STARTED:
				String[] springConfigs = springContextHeader.split(",");

				_registerApplicationContext(bundle, springConfigs);

				break;

			case BundleEvent.STOPPED:
				_unregisterApplicationContext(bundle);

				break;

			default:
				// TODO
				System.out.println(
					"In the rest of the cases we don't need to do anything." +
						"That's true?");
		}
	}

	private void _registerApplicationContext(
		Bundle bundle, String[] locations) {

		ClassLoader bundleClassLoader =
			BundleDelegatedClassLoaderFactory.createClassLoader(bundle);

		OsgiBundleApplicationContext applicationContext =
			new OsgiBundleApplicationContext(
				_parentApplicationContext, locations, bundleClassLoader);

		applicationContext.addBeanFactoryPostProcessor(
			new PortletBeanFactoryPostProcessor());

		applicationContext.refresh();

	}

	private void _unregisterApplicationContext(Bundle bundle) {
		Long bundleId = bundle.getBundleId();

		ConfigurableApplicationContext configurableApplicationContext =
			_springContexts.remove(bundleId);

		if (configurableApplicationContext != null) {
			Dictionary<String, String> headers = bundle.getHeaders();
			String bundleName = headers.get(Constants.BUNDLE_NAME);

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Closing application context for bundle with id " +
						bundleId + " and name " + bundleName);
			}

			configurableApplicationContext.close();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(SpringExtender.class);

	private ApplicationContext _parentApplicationContext;
	private Map<Long, ConfigurableApplicationContext> _springContexts =
		new ConcurrentHashMap<Long, ConfigurableApplicationContext>();

}