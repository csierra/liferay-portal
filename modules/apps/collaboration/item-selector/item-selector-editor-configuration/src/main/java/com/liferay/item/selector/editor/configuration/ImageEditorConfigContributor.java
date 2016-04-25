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

package com.liferay.item.selector.editor.configuration;

import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.ItemSelectorCriterion;
import com.liferay.item.selector.criteria.image.criterion.ImageItemSelectorCriterion;
import com.liferay.item.selector.criteria.url.criterion.URLItemSelectorCriterion;
import com.liferay.portal.kernel.editor.configuration.EditorConfigContributor;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Image;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(service = EditorConfigContributor.class)
public class ImageEditorConfigContributor extends BaseEditorConfigContributor {

	@Override
	public void populateConfigJSONObject(
		JSONObject jsonObject, Map<String, Object> inputEditorTaglibAttributes,
		ThemeDisplay themeDisplay,
		RequestBackedPortletURLFactory requestBackedPortletURLFactory) {

		List<ItemSelectorCriterion> itemSelectorCriteria = new ArrayList<>();

		boolean allowBrowseDocuments = GetterUtil.getBoolean(
			inputEditorTaglibAttributes.get(
				"liferay-ui:input-editor:allowBrowseDocuments"));

		if (allowBrowseDocuments) {
			itemSelectorCriteria.add(new ImageItemSelectorCriterion());
		}

		itemSelectorCriteria.add(new URLItemSelectorCriterion());

		PortletURL itemSelectorURL = getItemSelectorPortletURL(
			inputEditorTaglibAttributes, requestBackedPortletURLFactory,
			itemSelectorCriteria.toArray(
				new ItemSelectorCriterion[itemSelectorCriteria.size()]));

		PortletURL imageEditUrl =
			requestBackedPortletURLFactory.createRenderURL(
				PortletProviderUtil.getPortletId(
					Image.class.getName(), PortletProvider.Action.EDIT));

		PortletURL imageSaveUrl =
			requestBackedPortletURLFactory.createActionURL(
				"com_liferay_document_library_web_portlet_DLPortlet");

		imageEditUrl.setParameter("mvcRenderCommandName", "/image_editor/view");

		imageSaveUrl.setParameter("javax.portlet.action", "/document_library/edit_file_entry");
		imageSaveUrl.setParameter("cmd", "add");
		imageSaveUrl.setParameter("folderId", "0");
		imageSaveUrl.setParameter("repositoryId", "20233");

		try {
			imageEditUrl.setWindowState(LiferayWindowState.POP_UP);
		}
		catch (WindowStateException wse) {
		}

		if (itemSelectorURL != null) {
			jsonObject.put(
				"filebrowserImageBrowseLinkUrl", itemSelectorURL.toString());
			jsonObject.put(
				"filebrowserImageBrowseUrl", itemSelectorURL.toString());
			jsonObject.put("imageEditUrl", imageEditUrl);
			jsonObject.put("imageSaveUrl", imageSaveUrl);
		}
	}

	@Reference(unbind = "-")
	public void setItemSelector(ItemSelector itemSelector) {
		_itemSelector = itemSelector;
	}

	@Override
	protected ItemSelector getItemSelector() {
		return _itemSelector;
	}

	private ItemSelector _itemSelector;

}