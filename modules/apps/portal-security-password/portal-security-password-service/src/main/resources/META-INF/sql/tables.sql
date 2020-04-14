create table PasswordEntry (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	passwordEntryId LONG not null primary key,
	userId LONG,
	companyId LONG,
	createDate DATE null,
	hash VARCHAR(75) null
);

create table PasswordHashProvider (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	passwordHashProviderId LONG not null primary key,
	companyId LONG,
	createDate DATE null,
	hashProviderName VARCHAR(75) null,
	hashProviderMeta VARCHAR(75) null
);

create table PasswordMeta (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	passwordMetaId LONG not null primary key,
	companyId LONG,
	createDate DATE null,
	passwordEntryId LONG,
	salt VARCHAR(75) null
);