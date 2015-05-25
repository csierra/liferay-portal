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

package com.liferay.portal.upgrade.internal.bundle;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Release;
import com.liferay.portal.service.ReleaseLocalService;

import java.io.InputStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Miguel Pastor
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true)
public final class SyntheticBundleBuilder {

	@Activate
	protected void activate(BundleContext bundleContext) {
		List<Release> releases = _releaseLocalService.getReleases(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (Release release : releases) {
			try {
				Bundle bundle = bundleContext.installBundle(
					"SyntheticBundle" + release.getServletContextName(),
					buildBundle(release));

				bundle.start();

				_syntheticBundles.put(release.getServletContextName(), bundle);
			}
			catch (BundleException be) {
				throw new RuntimeException(
					"Unable to activate the schema provider", be);
			}
		}
	}

	protected InputStream buildBundle(Release release) {
		StringBundler sb = new StringBundler(18);

		sb.append("Bundle-Name: ");
		sb.append(release.getServletContextName());
		sb.append(" Schema Capabilities Provider");
		sb.append(StringPool.NEW_LINE);
		sb.append("Bundle-SymbolicName: ");
		sb.append("synthetic.");
		sb.append(release.getServletContextName());
		sb.append(StringPool.NEW_LINE);
		sb.append("Bundle-Version: ");
		sb.append(release.getBuildNumber());
		sb.append(StringPool.NEW_LINE);
		sb.append("Provide-Capability: schema.provider;");
		sb.append("schema.provider=\"");
		sb.append(release.getServletContextName());
		sb.append("\";");
		sb.append("version:Version=\"");
		sb.append(release.getBuildNumber());
		sb.append("\"");

		JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class);

		javaArchive.add(new StringAsset(sb.toString()), "META-INF/MANIFEST.MF");

		ZipExporter zipExporter = javaArchive.as(ZipExporter.class);

		return zipExporter.exportAsInputStream();
	}

	@Deactivate
	protected void deactivate() {
		for (Bundle bundle : _syntheticBundles.values()) {
			try {
				bundle.uninstall();
			}
			catch (BundleException be) {
				throw new RuntimeException(
					"Unable to deactivate the schema provider", be);
			}
		}
	}

	@Reference
	protected void setReleaseLocalService(
		ReleaseLocalService releaseLocalService) {

		_releaseLocalService = releaseLocalService;
	}

	@Reference(target = "(original.bean=true)")
	protected void setServletContext(ServletContext servletContext) {
	}

	private ReleaseLocalService _releaseLocalService;
	private final Map<String, Bundle> _syntheticBundles = new HashMap<>();

}