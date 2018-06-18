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

package com.liferay.dynamic.data.mapping.type.document.library.internal;

import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.AggregateResourceBundleLoader;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleLoaderUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.URLCodec;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Pedro Queiroz
 */
@Component(
	immediate = true, property = "ddm.form.field.type.name=document_library",
	service = {
		DDMFormFieldTemplateContextContributor.class,
		DocumentLibraryDDMFormFieldTemplateContextContributor.class
	}
)
public class DocumentLibraryDDMFormFieldTemplateContextContributor
	implements DDMFormFieldTemplateContextContributor {

	public FileEntry getFileEntry(JSONObject valueJSONObject) {
		try {
			return dlAppService.getFileEntryByUuidAndGroupId(
				valueJSONObject.getString("uuid"),
				valueJSONObject.getLong("groupId"));
		}
		catch (PortalException pe) {
			_log.error("Unable to retrieve file entry ", pe);

			return null;
		}
	}

	public String getFileEntryTitle(FileEntry fileEntry) {
		if (fileEntry == null) {
			return StringPool.BLANK;
		}

		return html.escape(fileEntry.getTitle());
	}

	public String getFileEntryURL(
		HttpServletRequest request, FileEntry fileEntry) {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (fileEntry == null) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(9);

		sb.append(themeDisplay.getPathContext());
		sb.append("/documents/");
		sb.append(fileEntry.getRepositoryId());
		sb.append(StringPool.SLASH);
		sb.append(fileEntry.getFolderId());
		sb.append(StringPool.SLASH);
		sb.append(
			URLCodec.encodeURL(html.unescape(fileEntry.getTitle()), true));
		sb.append(StringPool.SLASH);
		sb.append(fileEntry.getUuid());

		return html.escape(sb.toString());
	}

	public String getLexiconIconsPath(HttpServletRequest request) {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (themeDisplay == null) {
			return null;
		}

		StringBundler sb = new StringBundler(3);

		sb.append(themeDisplay.getPathThemeImages());
		sb.append("/lexicon/icons.svg");
		sb.append(StringPool.POUND);

		return sb.toString();
	}

	@Override
	public Map<String, Object> getParameters(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Map<String, Object> parameters = new HashMap<>();

		HttpServletRequest request =
			ddmFormFieldRenderingContext.getHttpServletRequest();

		if (ddmFormFieldRenderingContext.isReadOnly() &&
			Validator.isNotNull(ddmFormFieldRenderingContext.getValue())) {

			JSONObject valueJSONObject = getValueJSONObject(
				ddmFormFieldRenderingContext.getValue());

			if ((valueJSONObject != null) && (valueJSONObject.length() > 0)) {
				FileEntry fileEntry = getFileEntry(valueJSONObject);

				parameters.put("fileEntryTitle", getFileEntryTitle(fileEntry));
				parameters.put(
					"fileEntryURL", getFileEntryURL(request, fileEntry));
			}
		}

		parameters.put(
			"groupId", ddmFormFieldRenderingContext.getProperty("groupId"));
		parameters.put("lexiconIconsPath", getLexiconIconsPath(request));

		Map<String, String> stringsMap = new HashMap<>();

		ResourceBundle resourceBundle = resourceBundleLoader.loadResourceBundle(
			ddmFormFieldRenderingContext.getLocale());

		stringsMap.put("select", LanguageUtil.get(resourceBundle, "select"));

		parameters.put("strings", stringsMap);
		parameters.put(
			"value",
			jsonFactory.looseDeserialize(
				ddmFormFieldRenderingContext.getValue()));

		return parameters;
	}

	public JSONObject getValueJSONObject(String value) {
		try {
			return jsonFactory.createJSONObject(value);
		}
		catch (JSONException jsone) {
			if (_log.isDebugEnabled()) {
				_log.debug(jsone, jsone);
			}

			return null;
		}
	}

	@Reference(
		cardinality = ReferenceCardinality.MANDATORY,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(&(bundle.symbolic.name=com.liferay.dynamic.data.mapping.type.document.library)(resource.bundle.base.name=content.Language))"
	)
	protected void setResourceBundleLoader(
		ResourceBundleLoader resourceBundleLoader) {

		this.resourceBundleLoader = new AggregateResourceBundleLoader(
			resourceBundleLoader,
			ResourceBundleLoaderUtil.getPortalResourceBundleLoader());
	}

	protected void unsetResourceBundleLoader(
		ResourceBundleLoader resourceBundleLoader) {
	}

	@Reference
	protected DLAppService dlAppService;

	@Reference
	protected Html html;

	@Reference
	protected JSONFactory jsonFactory;

	protected volatile ResourceBundleLoader resourceBundleLoader;

	private static final Log _log = LogFactoryUtil.getLog(
		DocumentLibraryDDMFormFieldTemplateContextContributor.class);

}