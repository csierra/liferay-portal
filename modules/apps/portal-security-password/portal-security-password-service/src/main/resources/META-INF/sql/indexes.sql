create index IX_86A4C3B7 on PasswordEntry (userId);
create index IX_F3037531 on PasswordEntry (uuid_[$COLUMN_LENGTH:75$], companyId);

create index IX_BBBB73B6 on PasswordHashProvider (hashProviderName[$COLUMN_LENGTH:75$], hashProviderMeta[$COLUMN_LENGTH:75$]);
create index IX_DC034EB4 on PasswordHashProvider (uuid_[$COLUMN_LENGTH:75$], companyId);

create index IX_717D8C5C on PasswordMeta (passwordEntryId);
create index IX_6F00716F on PasswordMeta (passwordHashProviderId);
create index IX_76B0C39A on PasswordMeta (uuid_[$COLUMN_LENGTH:75$], companyId);