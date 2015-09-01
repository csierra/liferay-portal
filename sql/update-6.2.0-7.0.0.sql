alter table AssetEntry add listable BOOLEAN;

alter table AssetTag add uuid_ VARCHAR(75);

COMMIT_TRANSACTION;

update AssetEntry set listable = TRUE;

drop table AssetTagProperty;

alter table BlogsEntry add subtitle STRING null;
alter table BlogsEntry add coverImageCaption STRING null;
alter table BlogsEntry add coverImageFileEntryId LONG;
alter table BlogsEntry add coverImageURL STRING null;
alter table BlogsEntry add smallImageFileEntryId LONG;

alter table DLFileEntryMetadata drop column fileEntryTypeId;

drop index IX_F8E90438 on DLFileEntryMetadata;

alter table DLFolder add restrictionType INTEGER;

update DLFolder set restrictionType = 1 where overrideFileEntryTypes = 1;

alter table DLFolder drop column overrideFileEntryTypes;

create table ExportImportConfiguration (
	mvccVersion LONG default 0,
	exportImportConfigurationId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	name VARCHAR(200) null,
	description STRING null,
	type_ INTEGER,
	settings_ TEXT null,
	status INTEGER,
	statusByUserId LONG,
	statusByUserName VARCHAR(75) null,
	statusDate DATE null
);

alter table Group_ add groupKey STRING;

update Group_ set groupKey = name;

alter table Group_ add inheritContent BOOLEAN;

alter table Layout drop column iconImage;

alter table LayoutRevision drop column iconImage;

alter table LayoutSet drop column logo;

alter table LayoutSetBranch drop column logo;

alter table Organization_ add logoId LONG;

alter table RatingsEntry add uuid_ VARCHAR(75) null;

insert into Region (regionId, countryId, regionCode, name, active_) values (33001, 33, 'AT-1', 'Burgenland', TRUE);
insert into Region (regionId, countryId, regionCode, name, active_) values (33002, 33, 'AT-2', 'Kärnten', TRUE);
insert into Region (regionId, countryId, regionCode, name, active_) values (33003, 33, 'AT-3', 'Niederösterreich', TRUE);
insert into Region (regionId, countryId, regionCode, name, active_) values (33004, 33, 'AT-4', 'Oberösterreich', TRUE);
insert into Region (regionId, countryId, regionCode, name, active_) values (33005, 33, 'AT-5', 'Salzburg', TRUE);
insert into Region (regionId, countryId, regionCode, name, active_) values (33006, 33, 'AT-6', 'Steiermark', TRUE);
insert into Region (regionId, countryId, regionCode, name, active_) values (33007, 33, 'AT-7', 'Tirol', TRUE);
insert into Region (regionId, countryId, regionCode, name, active_) values (33008, 33, 'AT-8', 'Vorarlberg', TRUE);
insert into Region (regionId, countryId, regionCode, name, active_) values (33009, 33, 'AT-9', 'Vienna', TRUE);

INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (2,'com.liferay.amazon.rankings.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (3,'com.liferay.announcements.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (4,'com.liferay.asset.browser.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (5,'com.liferay.asset.categories.admin.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (6,'com.liferay.asset.categories.navigation.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (7,'com.liferay.asset.publisher.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (8,'com.liferay.asset.tags.admin.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (9,'com.liferay.asset.tags.compiler.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (10,'com.liferay.asset.tags.navigation.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (11,'com.liferay.blogs.recent.bloggers.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (12,'com.liferay.blogs.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (13,'com.liferay.bookmarks.service','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (14,'com.liferay.bookmarks.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (15,'com.liferay.calendar.service','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (16,'com.liferay.calendar.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (17,'com.liferay.comment.page.comments.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (18,'com.liferay.currency.converter.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (19,'com.liferay.dictionary.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (20,'com.liferay.document.library.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (21,'com.liferay.dynamic.data.lists.service','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (22,'com.liferay.dynamic.data.lists.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (23,'com.liferay.dynamic.data.mapping.service','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (24,'com.liferay.expando.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (25,'com.liferay.exportimport.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (26,'com.liferay.flags.page.flags.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (27,'com.liferay.hello.velocity.web.portlet.HelloVelocityPortlet','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (28,'com.liferay.iframe.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (29,'com.liferay.invitation.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (30,'com.liferay.item.selector.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (31,'com.liferay.journal.content.search.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (32,'com.liferay.journal.content.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (33,'com.liferay.journal.service','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (34,'com.liferay.journal.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (35,'com.liferay.layout.admin.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (36,'com.liferay.layout.prototype.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (37,'com.liferay.layout.set.prototype.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (38,'com.liferay.loan.calculator.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (39,'com.liferay.marketplace.service','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (40,'com.liferay.message.boards.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (41,'com.liferay.microblogs.service','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (42,'com.liferay.microblogs.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (43,'com.liferay.mobile.device.rules.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (44,'com.liferay.my.account.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (45,'com.liferay.nested.portlets.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (46,'com.liferay.network.utilities.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (47,'com.liferay.password.generator.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (48,'com.liferay.password.policies.admin.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (49,'com.liferay.plugins.admin.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (50,'com.liferay.polls.service','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (51,'com.liferay.portal.instances.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (52,'com.liferay.portal.lock.service','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (53,'com.liferay.portal.workflow.kaleo.service','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (54,'com.liferay.portlet.configuration.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (55,'com.liferay.portlet.css.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (56,'com.liferay.quick.note.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (57,'com.liferay.ratings.page.ratings.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (58,'com.liferay.roles.admin.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (59,'com.liferay.rss.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (60,'com.liferay.search.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (61,'com.liferay.shopping.service','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (62,'com.liferay.shopping.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (63,'com.liferay.site.admin.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (64,'com.liferay.site.browser.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (65,'com.liferay.site.memberships.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (66,'com.liferay.site.my.sites','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (67,'com.liferay.site.navigation.breadcrumb.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (68,'com.liferay.site.navigation.directory.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (69,'com.liferay.site.navigation.language.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (70,'com.liferay.site.navigation.menu.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (71,'com.liferay.site.navigation.site.map.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (72,'com.liferay.site.teams.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (73,'com.liferay.social.activities.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (74,'com.liferay.social.activity.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (75,'com.liferay.social.group.statistics.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (76,'com.liferay.social.requests.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (77,'com.liferay.social.user.statistics.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (78,'com.liferay.staging.bar.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (79,'com.liferay.translator.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (80,'com.liferay.trash.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (81,'com.liferay.unit.converter.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (82,'com.liferay.user.groups.admin.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (83,'com.liferay.users.admin.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (84,'com.liferay.web.proxy.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (85,'com.liferay.wiki.service','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (86,'com.liferay.wiki.web','0.0.1',1,NULL,1,0,'');
INSERT INTO Release_ (releaseId,servletContextName,schemaVersion,buildNumber,buildDate,verified,state_,testString) VALUES (87,'com.liferay.xsl.content.web','0.0.1',1,NULL,1,0,'');


update Region set regionCode = 'BB' where regionId = 4004 and regionCode = 'BR';
update Region set name = 'Monza e Brianza', regionCode = 'MB' where regionId = 8060 and regionCode = 'MZ';

alter table ResourcePermission add primKeyId LONG;
alter table ResourcePermission add viewActionId BOOLEAN;

alter table Subscription add groupId LONG;

alter table Team add uuid_ VARCHAR(75);

alter table UserNotificationEvent add deliveryType INTEGER;
alter table UserNotificationEvent add actionRequired BOOLEAN;