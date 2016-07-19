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
package com.liferay.frontend.editor.alloyeditor.accessibility.editor.web.filter;

import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.SingleVMPool;
import com.liferay.portal.kernel.cache.SingleVMPoolUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.BaseFilter;
import com.liferay.portal.kernel.servlet.TryFilter;
import com.liferay.portal.kernel.util.HttpUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.util.StreamUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author Antonio Pol
 */
@Component(
	immediate = true,
	property = {
		HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=(osgi.http.whiteboard.context.name=frontend-editor-alloyeditor-web)",
		HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_NAME + "=AlloyEditorAccessibility Filter",
		HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN + "=/alloyeditor/liferay-alloy-editor-all-min.js",
		HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_DISPATCHER + "=" + HttpWhiteboardConstants.DISPATCHER_ASYNC,
		HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_DISPATCHER + "=" + HttpWhiteboardConstants.DISPATCHER_FORWARD,
		HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_DISPATCHER + "=" + HttpWhiteboardConstants.DISPATCHER_INCLUDE,
		HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_DISPATCHER + "=" + HttpWhiteboardConstants.DISPATCHER_REQUEST
	}
)
public class AlloyEditorAccessibilityFilter implements Filter {

	private Bundle _bundle;

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundle = bundleContext.getBundle();
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		MultiVMPoolUtil.clear();

		SingleVMPoolUtil.clear();
	}

	public void doFilter(
		ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		chain.doFilter(request, response);

		Enumeration<URL> files =
			_bundle.findEntries("/dir/to/files", "*.js", true);

		if (files == null) {
			return;
		}

		ServletOutputStream outputStream = response.getOutputStream();

		while (files.hasMoreElements()) {
			URL url = files.nextElement();

			try (InputStream inputStream = url.openStream()) {
				StreamUtil.transfer(inputStream, outputStream);
			}
			catch (Exception e) {
				continue;
			}
		}
	}

	public void destroy() {
		MultiVMPoolUtil.clear();

		SingleVMPoolUtil.clear();
	}

	private static final Log _log = LogFactoryUtil.getLog(AlloyEditorAccessibilityFilter.class);

}