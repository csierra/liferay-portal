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

package com.liferay.portal.upgrade.v7_0_0;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.model.ReleaseConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * @author Miguel Pastor
 */
public class UpgradeModules extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"INSERT INTO Release_ VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			for (String app : _apps) {
				ps.setLong(1, increment());
				ps.setTimestamp(2, timestamp);
				ps.setTimestamp(3, timestamp);
				ps.setString(4, app);
				ps.setString(5, "-1.-1.-1.-1");
				ps.setTimestamp(6, timestamp);
				ps.setInt(7, 1);
				ps.setInt(8, 0);
				ps.setString(9, ReleaseConstants.TEST_STRING);
				ps.setInt(10, 1);

				ps.addBatch();
			}

			ps.executeBatch();
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	private static final String[] _apps = new String[] {
		"com.liferay.amazon.rankings.web",
		"com.liferay.announcements.web",
		"com.liferay.application.list.api",
		"com.liferay.application.list.taglib",
		"com.liferay.asset.browser.web",
		"com.liferay.asset.categories.admin.web",
		"com.liferay.asset.categories.navigation.web",
		"com.liferay.asset.publisher.layout.prototype",
		"com.liferay.asset.publisher.web",
		"com.liferay.asset.tag.admin.web",
		"com.liferay.asset.tags.compiler.web",
		"com.liferay.asset.tags.navigation.web",
		"com.liferay.blogs.api",
		"com.liferay.blogs.editor.config",
		"com.liferay.blogs.item.selector",
		"com.liferay.blogs.item.selector.web",
		"com.liferay.blogs.layout.prototype",
		"com.liferay.blogs.portlet.toolbar.contributor",
		"com.liferay.blogs.recent.bloggers.web",
		"com.liferay.blogs.service",
		"com.liferay.blogs.web",
		"com.liferay.bookmarks.api",
		"com.liferay.bookmarks.service",
		"com.liferay.bookmarks.web",
		"com.liferay.calendar.api",
		"com.liferay.calendar.service",
		"com.liferay.calendar.test",
		"com.liferay.calendar.web",
		"com.liferay.comment.api",
		"com.liferay.comment.editor.config",
		"com.liferay.comment.page.comments.web",
		"com.liferay.comment.ratings.definition",
		"com.liferay.comment.sanitizer",
		"com.liferay.comment.web",
		"com.liferay.configuration.admin.web",
		"com.liferay.control.menu.web",
		"com.liferay.control.panel.menu.web",
		"com.liferay.currency.converter.web",
		"com.liferay.dictionary.web",
		"com.liferay.document.library.google.docs",
		"com.liferay.document.library.item.selector.web",
		"com.liferay.document.library.layout.set.prototype",
		"com.liferay.document.library.ratings.definition",
		"com.liferay.document.library.repository.cmis",
		"com.liferay.document.library.repository.cmis.impl",
		"com.liferay.document.library.repository.search",
		"com.liferay.document.library.service",
		"com.liferay.dynamic.data.lists.api",
		"com.liferay.dynamic.data.lists.form.web",
		"com.liferay.dynamic.data.lists.service",
		"com.liferay.dynamic.data.lists.web",
		"com.liferay.dynamic.data.mapping.api",
		"com.liferay.dynamic.data.mapping.form.renderer",
		"com.liferay.dynamic.data.mapping.form.values.factory",
		"com.liferay.dynamic.data.mapping.form.values.query",
		"com.liferay.dynamic.data.mapping.service",
		"com.liferay.dynamic.data.mapping.taglib",
		"com.liferay.dynamic.data.mapping.test.util",
		"com.liferay.dynamic.data.mapping.type.checkbox",
		"com.liferay.dynamic.data.mapping.type.options",
		"com.liferay.dynamic.data.mapping.type.radio",
		"com.liferay.dynamic.data.mapping.type.select",
		"com.liferay.dynamic.data.mapping.type.text",
		"com.liferay.dynamic.data.mapping.validator",
		"com.liferay.dynamic.data.mapping.web",
		"com.liferay.expando.web",
		"com.liferay.exportimport.api",
		"com.liferay.exportimport.service",
		"com.liferay.exportimport.web",
		"com.liferay.flags.page.flags.web",
		"com.liferay.hello.velocity.web",
		"com.liferay.iframe.web",
		"com.liferay.invitation.web",
		"com.liferay.item.selector.api",
		"com.liferay.item.selector.criteria.api",
		"com.liferay.item.selector.taglib",
		"com.liferay.item.selector.upload.web",
		"com.liferay.item.selector.url.web",
		"com.liferay.item.selector.web",
		"com.liferay.journal.api",
		"com.liferay.journal.content.asset.addon.entry.comments",
		"com.liferay.journal.content.asset.addon.entry.common",
		"com.liferay.journal.content.asset.addon.entry.conversions",
		"com.liferay.journal.content.asset.addon.entry.locales",
		"com.liferay.journal.content.asset.addon.entry.print",
		"com.liferay.journal.content.asset.addon.entry.ratings",
		"com.liferay.journal.content.asset.addon.entry.related.assets",
		"com.liferay.journal.content.search.web",
		"com.liferay.journal.content.web",
		"com.liferay.journal.ratings.definition",
		"com.liferay.journal.service",
		"com.liferay.journal.terms.of.use",
		"com.liferay.journal.web",
		"com.liferay.layout.admin.test",
		"com.liferay.layout.admin.web",
		"com.liferay.layout.item.selector.web",
		"com.liferay.layout.prototype.web",
		"com.liferay.layout.set.prototype.we",
		"com.liferay.loan.calculator.web",
		"com.liferay.marketplace.api",
		"com.liferay.marketplace.app.manager.web",
		"com.liferay.marketplace.service",
		"com.liferay.marketplace.store.web",
		"com.liferay.mentions.api",
		"com.liferay.mentions.service",
		"com.liferay.mentions.web",
		"com.liferay.message.boards.comment",
		"com.liferay.message.boards.layout.set.prototype",
		"com.liferay.message.boards.ratings.definition",
		"com.liferay.message.boards.service",
		"com.liferay.message.boards.web",
		"com.liferay.microblogs.api",
		"com.liferay.microblogs.service",
		"com.liferay.microblogs.web",
		"com.liferay.mobile.device.rules.api",
		"com.liferay.mobile.device.rules.service",
		"com.liferay.mobile.device.rules.web",
		"com.liferay.nested.portlets.web",
		"com.liferay.network.utilities.web",
		"com.liferay.password.generator.web",
		"com.liferay.password.policies.admin.web",
		"com.liferay.polls.api",
		"com.liferay.polls.service",
		"com.liferay.polls.web",
		"com.liferay.portal.instances.web",
		"com.liferay.portal.lock.api",
		"com.liferay.portal.lock.service",
		"com.liferay.portal.workflow.kaleo.api",
		"com.liferay.portal.workflow.kaleo.service",
		"com.liferay.portlet.configuration.web",
		"com.liferay.portlet.css.web",
		"com.liferay.portlet.display.template.web",
		"com.liferay.portlet.display.template",
		"com.liferay.product.menu.control.panel.service",
		"com.liferay.product.menu.my.space.service",
		"com.liferay.product.menu.site.administration.service",
		"com.liferay.product.menu.web",
		"com.liferay.quick.note.web",
		"com.liferay.ratings.page.ratings.web",
		"com.liferay.ratings.service",
		"com.liferay.roles.selector.web",
		"com.liferay.rss.web",
		"com.liferay.search.web",
		"com.liferay.service.access.policy.api",
		"com.liferay.service.access.policy.service",
		"com.liferay.service.access.policy.web",
		"com.liferay.site.navigation.breadcrumb.web",
		"com.liferay.site.navigation.directory.web",
		"com.liferay.site.navigation.language.web",
		"com.liferay.site.navigation.menu.web",
		"com.liferay.site.navigation.site.map.web",
		"com.liferay.site.admin.web",
		"com.liferay.site.browser.web",
		"com.liferay.site.memberships.web",
		"com.liferay.site.my.sites.web",
		"com.liferay.site.teams.web",
		"com.liferay.social.office.announcements.web",
		"com.liferay.social.activities.web",
		"com.liferay.social.activity",
		"com.liferay.social.activity.web",
		"com.liferay.social.networking.api",
		"com.liferay.social.networking.service",
		"com.liferay.social.networking.web",
		"com.liferay.social.requests.web",
		"com.liferay.staging.bar.web",
		"com.liferay.staging.taglib",
		"com.liferay.staging.test",
		"com.liferay.translator.web",
		"com.liferay.trash.web",
		"com.liferay.unit.converter.web",
		"com.liferay.user.groups.admin.web",
		"com.liferay.user.personal.bar.web",
		"com.liferay.users.admin.service",
		"com.liferay.web.proxy.web",
		"com.liferay.wiki.api",
		"com.liferay.wiki.editor.config",
		"com.liferay.wiki.engine.creole",
		"com.liferay.wiki.engine.html",
		"com.liferay.wiki.engine.input.editor.common",
		"com.liferay.wiki.engine.jspwiki",
		"com.liferay.wiki.engine.mediawiki",
		"com.liferay.wiki.engine.text",
		"com.liferay.wiki.service",
		"com.liferay.wiki.web",
		"com.liferay.workflow.definition.link.web",
		"com.liferay.workflow.definition.web",
		"com.liferay.workflow.instance.web",
		"com.liferay.workflow.task.web",
		"com.liferay.xsl.content.web"
	};

}