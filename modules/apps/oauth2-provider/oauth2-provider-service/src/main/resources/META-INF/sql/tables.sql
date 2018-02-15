create table OAuth2Application (
	oAuth2ApplicationId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	clientId VARCHAR(75) null,
	clientSecret VARCHAR(75) null,
	redirectUri VARCHAR(75) null,
	clientConfidential BOOLEAN,
	description VARCHAR(75) null,
	name VARCHAR(75) null,
	webUrl VARCHAR(75) null,
	scopes TEXT null
);

create table OAuth2RefreshToken (
	oAuth2RefreshTokenId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	lifeTime LONG,
	oAuth2RefreshTokenContent TEXT null,
	oAuth2ApplicationId LONG
);

create table OAuth2ScopeGrant (
	applicationName VARCHAR(75) not null,
	bundleSymbolicName VARCHAR(75) not null,
	companyId LONG not null,
	oAuth2ScopeName VARCHAR(75) not null,
	oAuth2TokenId LONG not null,
	createDate DATE null,
	primary key (applicationName, bundleSymbolicName, companyId, oAuth2ScopeName, oAuth2TokenId)
);

create table OAuth2Token (
	oAuth2TokenId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	lifeTime LONG,
	oAuth2TokenContent TEXT null,
	oAuth2ApplicationId LONG,
	oAuth2TokenType VARCHAR(75) null,
	oAuth2RefreshTokenId LONG,
	scopes TEXT null
);