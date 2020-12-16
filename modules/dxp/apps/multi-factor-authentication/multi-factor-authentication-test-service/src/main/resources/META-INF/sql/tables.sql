create table MFATestEntry (
	mvccVersion LONG default 0 not null,
	mfaTestEntryId LONG not null primary key,
	testString VARCHAR(75) null,
	testUserInputString invalid
);