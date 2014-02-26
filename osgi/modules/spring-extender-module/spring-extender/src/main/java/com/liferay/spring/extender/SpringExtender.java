package com.liferay.spring.extender;

import java.util.Dictionary;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.liferay.portal.spring.bean.BeanReferenceAnnotationBeanPostProcessor;
import com.liferay.portal.spring.context.PortletBeanFactoryPostProcessor;
import com.liferay.spring.extender.context.OsgiBundleApplicationContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


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

		Class bundleClass = bundle.getClass();

		OsgiBundleApplicationContext applicationContext =
			new OsgiBundleApplicationContext(
				_parentApplicationContext, locations,
				bundleClass.getClassLoader());

		applicationContext.addBeanFactoryPostProcessor(
			new PortletBeanFactoryPostProcessor());

		applicationContext.refresh();

	}

	private void _unregisterApplicationContext(Bundle bundle) {
		Long bundleId = bundle.getBundleId();

		ConfigurableApplicationContext configurableApplicationContext =
			_springContexts.remove(bundleId);

		if (configurableApplicationContext != null) {
			// TODO Use proper logging
			Dictionary<String, String> headers = bundle.getHeaders();
			String bundleName = headers.get(Constants.BUNDLE_NAME);

			System.out.println(
				"Closing application context for bundle with id " + bundleId +
					" and name " + bundleName);

			configurableApplicationContext.close();
		}
	}

	private ApplicationContext _parentApplicationContext;
	private Map<Long, ConfigurableApplicationContext> _springContexts =
		new ConcurrentHashMap<Long, ConfigurableApplicationContext>();

}