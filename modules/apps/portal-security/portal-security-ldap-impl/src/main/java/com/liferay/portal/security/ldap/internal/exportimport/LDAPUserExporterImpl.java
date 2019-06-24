/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.security.ldap.internal.exportimport;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.security.ldap.LDAPSettings;
import com.liferay.portal.kernel.service.UserGroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.security.exportimport.UserExporter;
import com.liferay.portal.security.exportimport.UserOperation;
import com.liferay.portal.security.ldap.GroupConverterKeys;
import com.liferay.portal.security.ldap.PortalLDAP;
import com.liferay.portal.security.ldap.authenticator.configuration.LDAPAuthConfiguration;
import com.liferay.portal.security.ldap.configuration.ConfigurationProvider;
import com.liferay.portal.security.ldap.exportimport.Modifications;
import com.liferay.portal.security.ldap.exportimport.PortalToLDAPConverter;
import com.liferay.portal.security.ldap.internal.PortalLDAPContext;
import com.liferay.portal.security.ldap.util.LDAPUtil;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Binding;
import javax.naming.NameNotFoundException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SchemaViolationException;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.LdapName;

import org.apache.commons.lang.time.StopWatch;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 * @author Marcellus Tavares
 * @author Wesley Gong
 * @author Vilmos Papp
 */
@Component(immediate = true, service = UserExporter.class)
public class LDAPUserExporterImpl implements UserExporter {

	@Override
	public void exportUser(
			Contact contact, Map<String, Serializable> contactExpandoAttributes)
		throws Exception {

		long companyId = contact.getCompanyId();

		StopWatch stopWatch = new StopWatch();

		if (_log.isDebugEnabled()) {
			stopWatch.start();

			_log.debug("Exporting contact " + contact);
		}

		if (!_ldapSettings.isExportEnabled(companyId)) {
			return;
		}

		User user = _userLocalService.getUserByContactId(
			contact.getContactId());

		if (user.isDefaultUser() ||
			(user.getStatus() != WorkflowConstants.STATUS_APPROVED)) {

			return;
		}

		long ldapServerId = _portalLDAP.getLdapServerId(
			companyId, user.getScreenName(), user.getEmailAddress());

		LdapContext ldapContext = _portalLDAP.getSafeLdapContext(
			ldapServerId, companyId);

		try {
			if (ldapContext == null) {
				return;
			}

			Properties contactMappings = _ldapSettings.getContactMappings(
				ldapServerId, companyId);
			Properties contactExpandoMappings =
				_ldapSettings.getContactExpandoMappings(
					ldapServerId, companyId);

			Binding binding = _portalLDAP.getUser(
				ldapServerId, contact.getCompanyId(), user.getScreenName(),
				user.getEmailAddress());

			if (binding == null) {
				Properties userMappings = _ldapSettings.getUserMappings(
					ldapServerId, companyId);

				binding = addUser(
					ldapServerId, ldapContext, user, userMappings);
			}

			Modifications modifications =
				_portalToLDAPConverter.getLDAPContactModifications(
					contact, contactExpandoAttributes, contactMappings,
					contactExpandoMappings);

			if (modifications == null) {
				return;
			}

			ModificationItem[] modificationItems = modifications.getItems();

			ldapContext.modifyAttributes(
				LDAPUtil.asLdapName(binding.getNameInNamespace()),
				modificationItems);
		}
		finally {
			if (ldapContext != null) {
				ldapContext.close();
			}

			if (_log.isDebugEnabled()) {
				_log.debug(
					StringBundler.concat(
						"Finished exporting contact ", contact, " in ",
						stopWatch.getTime(), "ms"));
			}
		}
	}

	@Override
	public void exportUser(
			long userId, long userGroupId, UserOperation userOperation)
		throws Exception {

		User user = _userLocalService.getUser(userId);

		long companyId = user.getCompanyId();

		StopWatch stopWatch = new StopWatch();

		if (_log.isDebugEnabled()) {
			stopWatch.start();

			_log.debug(
				StringBundler.concat(
					"Exporting user ", user, " in user group ", userGroupId));
		}

		if (!_ldapSettings.isExportEnabled(companyId) ||
			!_ldapSettings.isExportGroupEnabled(companyId)) {

			return;
		}

		long ldapServerId = _portalLDAP.getLdapServerId(
			companyId, user.getScreenName(), user.getEmailAddress());

		LdapContext ldapContext = _portalLDAP.getSafeLdapContext(
			ldapServerId, companyId);

		if (ldapContext == null) {
			return;
		}

		UserGroup userGroup = _userGroupLocalService.getUserGroup(userGroupId);

		Properties groupMappings = _ldapSettings.getGroupMappings(
			ldapServerId, companyId);
		Properties userMappings = _ldapSettings.getUserMappings(
			ldapServerId, companyId);

		Binding userGroupBinding = _portalLDAP.getGroup(
			ldapServerId, companyId, userGroup.getName());

		if (userGroupBinding == null) {
			if (userOperation == UserOperation.ADD) {
				addGroup(
					ldapServerId, ldapContext, userGroup, user, groupMappings,
					userMappings);
			}
			else {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to get or add LDAP bindings for user group " +
							userGroup.getName());
				}
			}

			return;
		}

		LdapName userGroupDNLdapName = LDAPUtil.asLdapName(
			userGroupBinding.getNameInNamespace());

		try {
			Modifications modifications =
				_portalToLDAPConverter.getLDAPGroupModifications(
					ldapServerId, userGroup, user, groupMappings, userMappings,
					userOperation);

			ModificationItem[] modificationItems = modifications.getItems();

			ldapContext.modifyAttributes(
				userGroupDNLdapName, modificationItems);
		}
		catch (SchemaViolationException sve) {
			if (_log.isInfoEnabled()) {
				_log.info(
					"Unable to update LDAP bindings for user group " +
						userGroup.getName(),
					sve);
			}

			Attributes attributes = _portalLDAP.getGroupAttributes(
				ldapServerId, companyId, ldapContext, userGroupDNLdapName,
				true);

			Attribute groupMembers = attributes.get(
				groupMappings.getProperty(GroupConverterKeys.USER));

			if ((groupMembers != null) && (groupMembers.size() == 1)) {
				ldapContext.unbind(userGroupDNLdapName);
			}
		}
		finally {
			if (ldapContext != null) {
				ldapContext.close();
			}

			if (_log.isDebugEnabled()) {
				_log.debug(
					StringBundler.concat(
						"Finished exporting user ", user, " in user group ",
						userGroupId, " in ", stopWatch.getTime(), "ms"));
			}
		}
	}

	@Override
	public void exportUser(
			User user, Map<String, Serializable> userExpandoAttributes)
		throws Exception {

		if (user.isDefaultUser() ||
			(user.getStatus() != WorkflowConstants.STATUS_APPROVED)) {

			return;
		}

		long companyId = user.getCompanyId();

		if (!_ldapSettings.isExportEnabled(companyId)) {
			return;
		}

		long ldapServerId = _portalLDAP.getLdapServerId(
			companyId, user.getScreenName(), user.getEmailAddress());

		LdapContext ldapContext = _portalLDAP.getSafeLdapContext(
			ldapServerId, companyId);

		try {
			if (ldapContext == null) {
				return;
			}

			Properties userMappings = _ldapSettings.getUserMappings(
				ldapServerId, companyId);
			Properties userExpandoMappings =
				_ldapSettings.getUserExpandoMappings(ldapServerId, companyId);

			Binding userBinding = _portalLDAP.getUser(
				ldapServerId, user.getCompanyId(), user.getScreenName(),
				user.getEmailAddress(), true);

			if (userBinding == null) {
				userBinding = addUser(
					ldapServerId, ldapContext, user, userMappings);
			}
			else {
				Attributes attributes = _portalLDAP.getUserAttributes(
					ldapServerId, companyId, ldapContext,
					LDAPUtil.asLdapName(userBinding.getNameInNamespace()));

				String modifyTimestamp = LDAPUtil.getAttributeString(
					attributes, "modifyTimestamp");

				Date modifiedDate = LDAPUtil.parseDate(modifyTimestamp);

				if ((modifiedDate != null) &&
					modifiedDate.equals(user.getModifiedDate())) {

					if (_log.isDebugEnabled()) {
						_log.debug(
							"Skipping user " + user.getEmailAddress() +
								" because he is already synchronized");
					}

					return;
				}
			}

			Modifications modifications =
				_portalToLDAPConverter.getLDAPUserModifications(
					user, userExpandoAttributes, userMappings,
					userExpandoMappings);

			if (modifications == null) {
				return;
			}

			ModificationItem[] modificationItems = modifications.getItems();

			LdapName userDNLdapName = LDAPUtil.asLdapName(
				userBinding.getNameInNamespace());

			ldapContext.modifyAttributes(userDNLdapName, modificationItems);

			if (!_ldapSettings.isExportGroupEnabled(companyId)) {
				return;
			}

			List<UserGroup> userGroups =
				_userGroupLocalService.getUserUserGroups(user.getUserId());

			for (UserGroup userGroup : userGroups) {
				exportUser(
					user.getUserId(), userGroup.getUserGroupId(),
					UserOperation.ADD);
			}

			Modifications groupModifications =
				_portalToLDAPConverter.getLDAPUserGroupModifications(
					ldapServerId, userGroups, user, userMappings);

			ModificationItem[] groupModificationItems =
				groupModifications.getItems();

			if (groupModificationItems.length > 0) {
				ldapContext.modifyAttributes(
					userDNLdapName, groupModificationItems);
			}
		}
		catch (NameNotFoundException nnfe) {
			LDAPAuthConfiguration ldapAuthConfiguration =
				_ldapAuthConfigurationProvider.getConfiguration(companyId);

			if (ldapAuthConfiguration.required()) {
				throw nnfe;
			}

			_log.error(nnfe, nnfe);
		}
		finally {
			if (ldapContext != null) {
				ldapContext.close();
			}
		}
	}

	protected Binding addGroup(
			long ldapServerId, LdapContext ldapContext, UserGroup userGroup,
			User user, Properties groupMappings, Properties userMappings)
		throws Exception {

		LdapName userGroupLdapName =
			_portalToLDAPConverter.getUserGroupLdapName(
				ldapServerId, userGroup, groupMappings);

		Attributes attributes = _portalToLDAPConverter.getLDAPGroupAttributes(
			ldapServerId, userGroup, user, groupMappings, userMappings);

		ldapContext.bind(userGroupLdapName, new PortalLDAPContext(attributes));

		return _portalLDAP.getGroup(
			ldapServerId, userGroup.getCompanyId(), userGroup.getName());
	}

	protected Binding addUser(
			long ldapServerId, LdapContext ldapContext, User user,
			Properties userMappings)
		throws Exception {

		LdapName userLdapName = _portalToLDAPConverter.getUserLdapName(
			ldapServerId, user, userMappings);

		Attributes attributes = _portalToLDAPConverter.getLDAPUserAttributes(
			ldapServerId, user, userMappings);

		ldapContext.bind(userLdapName, new PortalLDAPContext(attributes));

		return _portalLDAP.getUser(
			ldapServerId, user.getCompanyId(), user.getScreenName(),
			user.getEmailAddress());
	}

	@Reference(
		target = "(factoryPid=com.liferay.portal.security.ldap.authenticator.configuration.LDAPAuthConfiguration)",
		unbind = "-"
	)
	protected void setConfigurationProvider(
		ConfigurationProvider<LDAPAuthConfiguration>
			ldapAuthConfigurationProvider) {

		_ldapAuthConfigurationProvider = ldapAuthConfigurationProvider;
	}

	@Reference(unbind = "-")
	protected void setLdapSettings(LDAPSettings ldapSettings) {
		_ldapSettings = ldapSettings;
	}

	@Reference(unbind = "-")
	protected void setUserGroupLocalService(
		UserGroupLocalService userGroupLocalService) {

		_userGroupLocalService = userGroupLocalService;
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LDAPUserExporterImpl.class);

	private ConfigurationProvider<LDAPAuthConfiguration>
		_ldapAuthConfigurationProvider;
	private LDAPSettings _ldapSettings;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile PortalLDAP _portalLDAP;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile PortalToLDAPConverter _portalToLDAPConverter;

	private UserGroupLocalService _userGroupLocalService;
	private UserLocalService _userLocalService;

}