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

package com.liferay.portlet.activities.action;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.rss.DefaultRSSRenderer;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityFeedEntry;
import com.liferay.portlet.social.service.SocialActivityInterpreterLocalServiceUtil;
import com.liferay.portlet.social.service.SocialActivityLocalServiceUtil;
import com.liferay.util.RSSUtil;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
public class ActivitiesRSSRenderer extends DefaultRSSRenderer {
	
	@Override
	public String getFeedURL() throws PortalException, SystemException {
		return PortalUtil.getLayoutFullURL(_themeDisplay) +
			Portal.FRIENDLY_URL_SEPARATOR + "activities/rss";
	}

	@Override
	public String getRSSName() {
		return ParamUtil.getString(_portletRequest, "feedTitle");
	}
	
	@Override
	public void populateFeedEntries(List<? super SyndEntry> syndEntries)
		throws PortalException, SystemException {

		for (SocialActivity activity : activities) {
			SocialActivityFeedEntry activityFeedEntry =
				SocialActivityInterpreterLocalServiceUtil.interpret(
					StringPool.BLANK, activity, serviceContext);

			if (activityFeedEntry == null) {
				continue;
			}

			SyndEntry syndEntry = new SyndEntryImpl();

			SyndContent syndContent = new SyndContentImpl();

			syndContent.setType(RSSUtil.ENTRY_TYPE_DEFAULT);

			String value = null;

			if (displayStyle.equals(RSSUtil.DISPLAY_STYLE_TITLE)) {
				value = StringPool.BLANK;
			}
			else {
				value = activityFeedEntry.getBody();
			}

			syndContent.setValue(value);

			syndEntry.setDescription(syndContent);

			if (Validator.isNotNull(activityFeedEntry.getLink())) {
				syndEntry.setLink(activityFeedEntry.getLink());
			}

			syndEntry.setPublishedDate(new Date(activity.getCreateDate()));
			syndEntry.setTitle(
				HtmlUtil.extractText(activityFeedEntry.getTitle()));
			syndEntry.setUri(syndEntry.getLink());

			syndEntries.add(syndEntry);
		}

	}
	
	protected List<SocialActivity> getActivities(
		PortletRequest portletRequest, int max)
		throws Exception {

		ThemeDisplay themeDisplay =
			(ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);

		Group group =
			GroupLocalServiceUtil.getGroup(themeDisplay.getScopeGroupId());

		int start = 0;

		if (group.isOrganization()) {
			return SocialActivityLocalServiceUtil.getOrganizationActivities(
				group.getOrganizationId(), start, max);
		}
		else if (group.isRegularSite()) {
			return SocialActivityLocalServiceUtil.getGroupActivities(
				group.getGroupId(), start, max);
		}
		else if (group.isUser()) {
			return SocialActivityLocalServiceUtil.getUserActivities(
				group.getClassPK(), start, max);
		}

		return Collections.emptyList();
	}
	
}