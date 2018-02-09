create index IX_523E5C67 on OAuth2Application (companyId, clientId[$COLUMN_LENGTH:75$]);

create index IX_5D141D2B on OAuth2RefreshToken (oAuth2ApplicationId, userName[$COLUMN_LENGTH:75$]);

create index IX_C2AF1FC8 on OAuth2ScopeGrant (applicationName[$COLUMN_LENGTH:75$], bundleSymbolicName[$COLUMN_LENGTH:75$], companyId, oAuth2ScopeName[$COLUMN_LENGTH:75$], oAuth2TokenId);
create index IX_310FAE60 on OAuth2ScopeGrant (oAuth2TokenId);

create index IX_1351786 on OAuth2Token (oAuth2ApplicationId, userName[$COLUMN_LENGTH:75$]);
create index IX_6CD4E94C on OAuth2Token (oAuth2RefreshTokenId[$COLUMN_LENGTH:75$]);
create index IX_194A5E73 on OAuth2Token (oAuth2TokenContent[$COLUMN_LENGTH:2000000$]);