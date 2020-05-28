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

package com.liferay.multi.factor.authentication.ip.address.web.internal.checker;

import com.liferay.multi.factor.authentication.ip.address.web.internal.audit.MFAIpAddressAuditMessageBuilder;
import com.liferay.multi.factor.authentication.ip.address.web.internal.configuration.MFAIpAddressConfiguration;
import com.liferay.multi.factor.authentication.spi.checker.headless.MFAHeadlessChecker;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.access.control.AccessControlUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.HashMapDictionary;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

/**
 * @author Marta Medio
 */
@Component(
	configurationPid = "com.liferay.multi.factor.authentication.ip.address.web.internal.configuration.MFAIpAddressConfiguration.scoped",
	configurationPolicy = ConfigurationPolicy.OPTIONAL,
	property = "mfa.visibility.configuration.pid=com.liferay.multi.factor.authentication.ip.address.web.internal.configuration.MFAIpAddressConfiguration",
	service = {}
)
public class MFAIpAddressMFAHeadlessChecker implements MFAHeadlessChecker {

	@Override
	public boolean verifyHeadlessRequest(
		HttpServletRequest httpServletRequest, long userId) {

		User user = _userLocalService.fetchUser(userId);

		if (user == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Requested ip address verification for nonexistent user " +
						userId);
			}

			_routeAuditMessage(
				_mfaIpAddressAuditMessageBuilder.
					buildNonexistentUserVerificationFailureAuditMessage(
						CompanyThreadLocal.getCompanyId(), userId,
						_getClassName()));

			return false;
		}

		if (AccessControlUtil.isAccessAllowed(
				httpServletRequest, _allowedIPsWithMasks)) {

			_routeAuditMessage(
				_mfaIpAddressAuditMessageBuilder.
					buildVerificationSuccessAuditMessage(
						user, _getClassName()));

			return true;
		}

		_routeAuditMessage(
			_mfaIpAddressAuditMessageBuilder.
				buildVerificationFailureAuditMessage(
					user, _getClassName(), "Ip not allowed"));

		return false;
	}

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		MFAIpAddressConfiguration mfaIpAddressConfiguration =
			ConfigurableUtil.createConfigurable(
				MFAIpAddressConfiguration.class, properties);

		if (!mfaIpAddressConfiguration.enabled()) {
			return;
		}

		_allowedIPsWithMasks = new HashSet<>(
			Arrays.asList(mfaIpAddressConfiguration.allowedIPsWithMasks()));

		_serviceRegistration = bundleContext.registerService(
			MFAHeadlessChecker.class, this,
			new HashMapDictionary<>(properties));
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration == null) {
			return;
		}

		_serviceRegistration.unregister();
	}

	private String _getClassName() {
		Class<?> clazz = getClass();

		return clazz.getName();
	}

	private void _routeAuditMessage(AuditMessage auditMessage) {
		if (_mfaIpAddressAuditMessageBuilder != null) {
			_mfaIpAddressAuditMessageBuilder.routeAuditMessage(auditMessage);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MFAIpAddressMFAHeadlessChecker.class);

	private Set<String> _allowedIPsWithMasks;

	@Reference(cardinality = ReferenceCardinality.OPTIONAL)
	private MFAIpAddressAuditMessageBuilder _mfaIpAddressAuditMessageBuilder;

	private ServiceRegistration<MFAHeadlessChecker> _serviceRegistration;

	@Reference
	private UserLocalService _userLocalService;

}