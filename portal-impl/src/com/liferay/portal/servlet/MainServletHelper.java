/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.servlet;

import com.liferay.portal.NoSuchLayoutException;
import com.liferay.portal.dao.shard.ShardDataSourceTargetSource;
import com.liferay.portal.events.EventsProcessorUtil;
import com.liferay.portal.events.StartupAction;
import com.liferay.portal.events.StartupHelperUtil;
import com.liferay.portal.kernel.cache.Lifecycle;
import com.liferay.portal.kernel.cache.ThreadLocalCacheManager;
import com.liferay.portal.kernel.deploy.hot.HotDeployUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.plugin.PluginPackage;
import com.liferay.portal.kernel.servlet.DynamicServletRequest;
import com.liferay.portal.kernel.servlet.PortalSessionThreadLocal;
import com.liferay.portal.kernel.servlet.ProtectedServletRequest;
import com.liferay.portal.kernel.servlet.SerializableSessionAttributeListener;
import com.liferay.portal.kernel.util.*;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.*;
import com.liferay.portal.plugin.PluginPackageUtil;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.jaas.JAASHelper;
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.liferay.portal.server.capabilities.ServerCapabilitiesUtil;
import com.liferay.portal.service.*;
import com.liferay.portal.servlet.filters.absoluteredirects.AbsoluteRedirectsResponse;
import com.liferay.portal.servlet.filters.i18n.I18nFilter;
import com.liferay.portal.setup.SetupWizardUtil;
import com.liferay.portal.struts.PortletRequestProcessor;
import com.liferay.portal.struts.StrutsUtil;
import com.liferay.portal.util.*;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.*;
import com.liferay.portlet.social.util.SocialConfigurationUtil;
import com.liferay.util.ContentUtil;
import com.liferay.util.servlet.EncryptedServletRequest;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ControllerConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.tiles.TilesUtilImpl;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 * @author Jorge Ferrer
 * @author Brian Myunghun Kim
 */
public class MainServletHelper {

	public MainServletHelper(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return _servletContext;
	}

	public void destroy() {
		if (_log.isDebugEnabled()) {
			_log.debug("Destroy plugins");
		}

		PortalLifecycleUtil.flushDestroys();

		List<Portlet> portlets = PortletLocalServiceUtil.getPortlets();

		if (_log.isDebugEnabled()) {
			_log.debug("Destroy portlets");
		}

		try {
			destroyPortlets(portlets);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Destroy companies");
		}

		try {
			destroyCompanies();
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Process global shutdown events");
		}

		try {
			processGlobalShutdownEvents();
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Destroy");
		}
	}

	public void init() throws ServletException {
		if (_log.isDebugEnabled()) {
			_log.debug("Initialize listeners");
		}

		initListeners();

		if (_log.isDebugEnabled()) {
			_log.debug("Process startup events");
		}

		try {
			processStartupEvents();
		}
		catch (Exception e) {
			_log.error(e, e);

			System.out.println(
				"Stopping the server due to unexpected startup errors");

			System.exit(0);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize server detector");
		}

		try {
			initServerDetector();
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize plugin package");
		}

		PluginPackage pluginPackage = null;

		try {
			pluginPackage = initPluginPackage();
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize portlets");
		}

		List<Portlet> portlets = new ArrayList<Portlet>();

		try {
			portlets.addAll(initPortlets(pluginPackage));
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize layout templates");
		}

		try {
			initLayoutTemplates(pluginPackage, portlets);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize social");
		}

		try {
			initSocial(pluginPackage);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize themes");
		}

		try {
			initThemes(pluginPackage, portlets);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize web settings");
		}

		try {
			initWebSettings();
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize extension environment");
		}

		try {
			initExt();
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Process global startup events");
		}

		try {
			processGlobalStartupEvents();
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize resource actions");
		}

		try {
			initResourceActions(portlets);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize companies");
		}

		try {
			initCompanies();
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialize plugins");
		}

		try {
			initPlugins();
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		getServletContext().setAttribute(WebKeys.STARTUP_FINISHED, true);

		StartupHelperUtil.setStartupFinished(true);

		ThreadLocalCacheManager.clearAll(Lifecycle.REQUEST);
	}


	protected void checkWebSettings(String xml) throws DocumentException {
		Document doc = SAXReaderUtil.read(xml);

		Element root = doc.getRootElement();

		int timeout = PropsValues.SESSION_TIMEOUT;

		Element sessionConfig = root.element("session-config");

		if (sessionConfig != null) {
			String sessionTimeout = sessionConfig.elementText(
				"session-timeout");

			timeout = GetterUtil.getInteger(sessionTimeout, timeout);
		}

		PropsUtil.set(PropsKeys.SESSION_TIMEOUT, String.valueOf(timeout));

		PropsValues.SESSION_TIMEOUT = timeout;

		I18nServlet.setLanguageIds(root);
		I18nFilter.setLanguageIds(I18nServlet.getLanguageIds());
	}

	protected void destroyCompanies() throws Exception {
		long[] companyIds = PortalInstances.getCompanyIds();

		for (long companyId : companyIds) {
			destroyCompany(companyId);
		}
	}

	protected void destroyCompany(long companyId) {
		if (_log.isDebugEnabled()) {
			_log.debug("Process shutdown events");
		}

		try {
			EventsProcessorUtil.process(
				PropsKeys.APPLICATION_SHUTDOWN_EVENTS,
				PropsValues.APPLICATION_SHUTDOWN_EVENTS,
				new String[] {String.valueOf(companyId)});
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	protected void destroyPortlets(List<Portlet> portlets) throws Exception {
		for (Portlet portlet : portlets) {
			PortletInstanceFactoryUtil.destroy(portlet);
		}
	}


	protected void initCompanies() throws Exception {
		ServletContext servletContext = getServletContext();

		try {
			String[] webIds = PortalInstances.getWebIds();

			for (String webId : webIds) {
				PortalInstances.initCompany(servletContext, webId);
			}
		}
		finally {
			CompanyThreadLocal.setCompanyId(
				PortalInstances.getDefaultCompanyId());

			ShardDataSourceTargetSource shardDataSourceTargetSource =
				(ShardDataSourceTargetSource)
					InfrastructureUtil.getShardDataSourceTargetSource();

			if (shardDataSourceTargetSource != null) {
				shardDataSourceTargetSource.resetDataSource();
			}
		}
	}

	protected void initExt() throws Exception {
		ServletContext servletContext = getServletContext();

		ExtRegistry.registerPortal(servletContext);
	}

	protected void initLayoutTemplates(
			PluginPackage pluginPackage, List<Portlet> portlets)
		throws Exception {

		ServletContext servletContext = getServletContext();

		String[] xmls = new String[] {
			HttpUtil.URLtoString(
				servletContext.getResource(
					"/WEB-INF/liferay-layout-templates.xml")),
			HttpUtil.URLtoString(
				servletContext.getResource(
					"/WEB-INF/liferay-layout-templates-ext.xml"))
		};

		List<LayoutTemplate> layoutTemplates =
			LayoutTemplateLocalServiceUtil.init(
				servletContext, xmls, pluginPackage);

		servletContext.setAttribute(
			WebKeys.PLUGIN_LAYOUT_TEMPLATES, layoutTemplates);
	}

	protected void initListeners() {
		SerializableSessionAttributeListener.initialize();
	}

	protected PluginPackage initPluginPackage() throws Exception {
		ServletContext servletContext = getServletContext();

		return PluginPackageUtil.readPluginPackageServletContext(
			servletContext);
	}

	/**
	 * @see com.liferay.portal.setup.SetupWizardUtil#_initPlugins
	 */
	protected void initPlugins() throws Exception {

		// See LEP-2885. Don't flush hot deploy events until after the portal
		// has initialized.

		if (SetupWizardUtil.isSetupFinished()) {
			HotDeployUtil.setCapturePrematureEvents(false);

			PortalLifecycleUtil.flushInits();
		}
	}

	protected void initPortletApp(
			Portlet portlet, ServletContext servletContext)
		throws PortletException {

		PortletApp portletApp = portlet.getPortletApp();

		PortletConfig portletConfig = PortletConfigFactoryUtil.create(
			portlet, servletContext);

		PortletContext portletContext = portletConfig.getPortletContext();

		Set<PortletFilter> portletFilters = portletApp.getPortletFilters();

		for (PortletFilter portletFilter : portletFilters) {
			PortletFilterFactory.create(portletFilter, portletContext);
		}

		Set<PortletURLListener> portletURLListeners =
			portletApp.getPortletURLListeners();

		for (PortletURLListener portletURLListener : portletURLListeners) {
			PortletURLListenerFactory.create(portletURLListener);
		}
	}

	protected List<Portlet> initPortlets(PluginPackage pluginPackage)
		throws Exception {

		ServletContext servletContext = getServletContext();

		String[] xmls = new String[] {
			HttpUtil.URLtoString(
				servletContext.getResource(
					"/WEB-INF/" + Portal.PORTLET_XML_FILE_NAME_CUSTOM)),
			HttpUtil.URLtoString(
				servletContext.getResource("/WEB-INF/portlet-ext.xml")),
			HttpUtil.URLtoString(
				servletContext.getResource("/WEB-INF/liferay-portlet.xml")),
			HttpUtil.URLtoString(
				servletContext.getResource("/WEB-INF/liferay-portlet-ext.xml")),
			HttpUtil.URLtoString(
				servletContext.getResource("/WEB-INF/web.xml"))
		};

		PortletLocalServiceUtil.initEAR(servletContext, xmls, pluginPackage);

		PortletBagFactory portletBagFactory = new PortletBagFactory();

		portletBagFactory.setClassLoader(
			ClassLoaderUtil.getPortalClassLoader());
		portletBagFactory.setServletContext(servletContext);
		portletBagFactory.setWARFile(false);

		List<Portlet> portlets = PortletLocalServiceUtil.getPortlets();

		for (int i = 0; i < portlets.size(); i++) {
			Portlet portlet = portlets.get(i);

			portletBagFactory.create(portlet);

			if (i == 0) {
				initPortletApp(portlet, servletContext);
			}
		}

		servletContext.setAttribute(WebKeys.PLUGIN_PORTLETS, portlets);

		return portlets;
	}

	protected void initResourceActions(List<Portlet> portlets)
		throws Exception {

		for (Portlet portlet : portlets) {
			List<String> portletActions =
				ResourceActionsUtil.getPortletResourceActions(portlet);

			ResourceActionLocalServiceUtil.checkResourceActions(
				portlet.getPortletId(), portletActions);

			List<String> modelNames =
				ResourceActionsUtil.getPortletModelResources(
					portlet.getPortletId());

			for (String modelName : modelNames) {
				List<String> modelActions =
					ResourceActionsUtil.getModelResourceActions(modelName);

				ResourceActionLocalServiceUtil.checkResourceActions(
					modelName, modelActions);
			}
		}
	}

	protected void initServerDetector() throws Exception {
		ServerCapabilitiesUtil.determineServerCapabilities(getServletContext());
	}

	protected void initSocial(PluginPackage pluginPackage) throws Exception {
		ClassLoader classLoader = ClassLoaderUtil.getPortalClassLoader();

		ServletContext servletContext = getServletContext();

		String[] xmls = new String[] {
			HttpUtil.URLtoString(
				servletContext.getResource("/WEB-INF/liferay-social.xml")),
			HttpUtil.URLtoString(
				servletContext.getResource("/WEB-INF/liferay-social-ext.xml"))
		};

		SocialConfigurationUtil.read(classLoader, xmls);
	}

	protected void initThemes(
			PluginPackage pluginPackage, List<Portlet> portlets)
		throws Exception {

		ServletContext servletContext = getServletContext();

		String[] xmls = new String[] {
			HttpUtil.URLtoString(
				servletContext.getResource(
					"/WEB-INF/liferay-look-and-feel.xml")),
			HttpUtil.URLtoString(
				servletContext.getResource(
					"/WEB-INF/liferay-look-and-feel-ext.xml"))
		};

		List<Theme> themes = ThemeLocalServiceUtil.init(
			servletContext, null, true, xmls, pluginPackage);

		servletContext.setAttribute(WebKeys.PLUGIN_THEMES, themes);
	}

	protected void initWebSettings() throws Exception {
		ServletContext servletContext = getServletContext();

		String xml = HttpUtil.URLtoString(
			servletContext.getResource("/WEB-INF/web.xml"));

		checkWebSettings(xml);
	}

	protected void processGlobalShutdownEvents() throws Exception {
		EventsProcessorUtil.process(
			PropsKeys.GLOBAL_SHUTDOWN_EVENTS,
			PropsValues.GLOBAL_SHUTDOWN_EVENTS);
	}

	protected void processGlobalStartupEvents() throws Exception {
		EventsProcessorUtil.process(
			PropsKeys.GLOBAL_STARTUP_EVENTS, PropsValues.GLOBAL_STARTUP_EVENTS);
	}


	protected void processStartupEvents() throws Exception {
		StartupAction startupAction = new StartupAction();

		startupAction.run(null);
	}

	private static Log _log = LogFactoryUtil.getLog(MainServletHelper.class);

	private ServletContext _servletContext;

}