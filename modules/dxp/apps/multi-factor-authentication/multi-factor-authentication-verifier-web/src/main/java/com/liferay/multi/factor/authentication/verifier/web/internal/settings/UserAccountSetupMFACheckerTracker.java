/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.multi.factor.authentication.verifier.web.internal.settings;

import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationEntry;
import com.liferay.multi.factor.authentication.verifier.spi.checker.MFASetupChecker;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;

import java.util.Dictionary;

import javax.servlet.ServletContext;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Tomas Polesovsky
 */
@Component(immediate = true, service = {})
public class UserAccountSetupMFACheckerTracker {

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_serviceTracker = ServiceTrackerFactory.open(
			bundleContext, MFASetupChecker.class,
			new MFACheckerSetupServiceTrackerCustomizer());
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();
	}

	private BundleContext _bundleContext;
	private ServiceTracker
		<MFASetupChecker, ServiceRegistration<ScreenNavigationEntry>>
			_serviceTracker;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.multi.factor.authentication.verifier.web)"
	)
	private ServletContext _servletContext;

	private class MFACheckerSetupServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<MFASetupChecker, ServiceRegistration<ScreenNavigationEntry>> {

		@Override
		public ServiceRegistration<ScreenNavigationEntry> addingService(
			ServiceReference<MFASetupChecker> serviceReference) {

			MFASetupChecker mfaSetupChecker = _bundleContext.getService(
				serviceReference);

			return _bundleContext.registerService(
				ScreenNavigationEntry.class,
				new UserAccountSetupMFAScreenNavigationEntry(
					mfaSetupChecker, _servletContext),
				_buildProperties(serviceReference));
		}

		@Override
		public void modifiedService(
			ServiceReference<MFASetupChecker> serviceReference,
			ServiceRegistration<ScreenNavigationEntry> serviceRegistration) {

			serviceRegistration.setProperties(
				_buildProperties(serviceReference));
		}

		@Override
		public void removedService(
			ServiceReference<MFASetupChecker> serviceReference,
			ServiceRegistration<ScreenNavigationEntry> serviceRegistration) {

			serviceRegistration.unregister();

			_bundleContext.ungetService(serviceReference);
		}

		private Dictionary<String, Object> _buildProperties(
			ServiceReference<MFASetupChecker> serviceReference) {

			Dictionary<String, Object> dictionary = new HashMapDictionary<>();

			dictionary.put(
				"screen.navigation.entry.order",
				GetterUtil.getInteger(
					serviceReference.getProperty(
						"user.account.screen.navigation.entry.order"),
					Integer.MAX_VALUE));

			return dictionary;
		}

	}

}