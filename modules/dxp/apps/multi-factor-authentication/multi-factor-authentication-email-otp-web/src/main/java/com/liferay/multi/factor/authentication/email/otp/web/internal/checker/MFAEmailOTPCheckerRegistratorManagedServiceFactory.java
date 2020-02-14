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

package com.liferay.multi.factor.authentication.email.otp.web.internal.checker;

import com.liferay.multi.factor.authentication.email.otp.web.internal.configuration.MFAEmailOTPConfiguration;
import com.liferay.multi.factor.authentication.email.otp.web.internal.system.configuration.MFAEmailOTPSystemConfiguration;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;

import java.util.Dictionary;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	configurationPid = "com.liferay.multi.factor.authentication.email.otp.web.internal.system.configuration.MFAEmailOTPSystemConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true,
	property = Constants.SERVICE_PID + "=com.liferay.multi.factor.authentication.email.otp.web.internal.configuration.MFAEmailOTPConfiguration.scoped",
	service = {}
)
public class MFAEmailOTPCheckerRegistratorManagedServiceFactory
	implements ManagedServiceFactory {

	@Override
	public void deleted(String pid) {
		_pidMap.computeIfPresent(
			pid,
			(__, ___) -> {
				if (_atomicInteger.decrementAndGet() == 0) {
					_componentContext.disableComponent(_COMPONENT_NAME);
				}

				return null;
			});
	}

	@Override
	public String getName() {
		return "Multi-Factor Authentication Email One-Time Password " +
			"Registrator";
	}

	@Override
	public void updated(String pid, Dictionary<String, ?> properties)
		throws ConfigurationException {

		final MFAEmailOTPConfiguration mfaEmailOTPConfiguration =
			ConfigurableUtil.createConfigurable(
				MFAEmailOTPConfiguration.class, properties);

		_pidMap.compute(
			pid,
			(__, prev) -> {
				if (mfaEmailOTPConfiguration.enabled()) {
					if (prev == null) {
						if (_atomicInteger.getAndIncrement() == 0) {
							_componentContext.enableComponent(_COMPONENT_NAME);
						}

						return pid;
					}

					return prev;
				}
				else {
					if (prev != null) {
						if (_atomicInteger.decrementAndGet() == 0) {
							_componentContext.disableComponent(_COMPONENT_NAME);
						}

						return null;
					}
				}

				return null;
			});
	}

	@Activate
	protected void activate(
		BundleContext bundleContext, ComponentContext componentContext,
		Map<String, Object> properties) {

		_componentContext = componentContext;

		MFAEmailOTPSystemConfiguration mfaEmailOTPSystemConfiguration =
			ConfigurableUtil.createConfigurable(
				MFAEmailOTPSystemConfiguration.class, properties);

		if (mfaEmailOTPSystemConfiguration.disableGlobally()) {
			return;
		}

		_serviceRegistration = bundleContext.registerService(
			ManagedServiceFactory.class, this,
			new HashMapDictionary<>(properties));
	}

	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		if (_serviceRegistration == null) {
			return;
		}

		_serviceRegistration.unregister();

		_serviceRegistration = null;

		componentContext.disableComponent(
			"com.liferay.multi.factor.authentication.email.otp.web.internal." +
				"checker.MFAEmailOTPChecker");
	}

	private static final String _COMPONENT_NAME =
		"com.liferay.multi.factor.authentication.email.otp.web.internal." +
			"checker.MFAEmailOTPChecker";

	private final AtomicInteger _atomicInteger = new AtomicInteger();
	private volatile ComponentContext _componentContext;
	private final ConcurrentHashMap<String, String> _pidMap =
		new ConcurrentHashMap<>();
	private volatile ServiceRegistration<ManagedServiceFactory>
		_serviceRegistration;

}