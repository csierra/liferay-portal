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

package com.liferay.account.retriever.test;

import com.liferay.account.model.AccountEntry;
import com.liferay.account.model.AccountEntryUserRel;
import com.liferay.account.retriever.AccountUserRetriever;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.account.service.AccountEntryUserRelLocalService;
import com.liferay.account.service.test.AccountEntryTestUtil;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Drew Brokke
 */
@RunWith(Arquillian.class)
public class AccountUserRetrieverTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_accountEntry = AccountEntryTestUtil.addAccountEntry(
			_accountEntryLocalService);
	}

	@Test
	public void testGetAccountUsers() throws Exception {
		_users.add(UserTestUtil.addUser());
		_users.add(UserTestUtil.addUser());
		_users.add(UserTestUtil.addUser());

		for (User user : _users) {
			_accountEntryUserRels.add(
				_accountEntryUserRelLocalService.addAccountEntryUserRel(
					_accountEntry.getAccountEntryId(), user.getUserId()));
		}

		long[] expectedUserIds = ListUtil.toLongArray(
			_users, User.USER_ID_ACCESSOR);

		Arrays.sort(expectedUserIds);

		List<User> actualUsers = _accountUserRetriever.getAccountUsers(
			_accountEntry.getAccountEntryId());

		long[] actualUserIds = ListUtil.toLongArray(
			actualUsers, User::getUserId);

		Arrays.sort(actualUserIds);

		Assert.assertArrayEquals(expectedUserIds, actualUserIds);
	}

	@Test
	public void testSearchAccountUsers() throws Exception {

		// Add a user that is part of the account but will not hit a keyword
		// search

		_users.add(UserTestUtil.addUser());

		// Add a user that is part of the account and will hit a keyword search

		String searchTerm = RandomTestUtil.randomString();

		_users.add(
			UserTestUtil.addUser(
				searchTerm + RandomTestUtil.randomString(), null));

		for (User user : _users) {
			_accountEntryUserRels.add(
				_accountEntryUserRelLocalService.addAccountEntryUserRel(
					_accountEntry.getAccountEntryId(), user.getUserId()));
		}

		// Assert that null keyword search hits only account users

		_assertSearch(null, 2);

		// Assert that non-null keyword search hits only account users that
		// match

		_assertSearch(searchTerm, 1);
	}

	@Test
	public void testSearchAccountUsersWithNoAccountUsers() throws Exception {

		// Add a user that is not part of the account

		_users.add(UserTestUtil.addUser());

		// Assert that null keyword search does not hit non-account users

		_assertSearch(null, 0);
	}

	@Test
	public void testSearchAccountUsersWithPagination() throws Exception {
		String searchTerm = RandomTestUtil.randomString();

		_users.add(UserTestUtil.addUser(searchTerm + 1, null));
		_users.add(UserTestUtil.addUser(searchTerm + 2, null));
		_users.add(UserTestUtil.addUser(searchTerm + 3, null));
		_users.add(UserTestUtil.addUser(searchTerm + 4, null));

		for (User user : _users) {
			_accountEntryUserRels.add(
				_accountEntryUserRelLocalService.addAccountEntryUserRel(
					_accountEntry.getAccountEntryId(), user.getUserId()));
		}

		// Assert unpaginated search

		BaseModelSearchResult<User> baseModelSearchResult = _searchAccountUsers(
			searchTerm, 0, 4, false);

		List<User> actualUsers = baseModelSearchResult.getBaseModels();

		Assert.assertEquals(4, baseModelSearchResult.getLength());

		Assert.assertEquals(actualUsers.toString(), 4, actualUsers.size());
		Assert.assertEquals(_users.get(0), actualUsers.get(0));

		// Test paginated search has a partial list, but full count

		baseModelSearchResult = _searchAccountUsers(searchTerm, 1, 2, false);

		actualUsers = baseModelSearchResult.getBaseModels();

		Assert.assertEquals(4, baseModelSearchResult.getLength());

		Assert.assertEquals(actualUsers.toString(), 2, actualUsers.size());
		Assert.assertEquals(_users.get(1), actualUsers.get(0));

		// Test reversed sorting

		baseModelSearchResult = _searchAccountUsers(searchTerm, 0, 4, true);

		actualUsers = baseModelSearchResult.getBaseModels();

		Assert.assertEquals(4, baseModelSearchResult.getLength());

		Assert.assertEquals(actualUsers.toString(), 4, actualUsers.size());
		Assert.assertEquals(_users.get(3), actualUsers.get(0));
	}

	private void _assertSearch(String keywords, int expectedSize)
		throws Exception {

		BaseModelSearchResult<User> baseModelSearchResult = _searchAccountUsers(
			keywords, QueryUtil.ALL_POS, QueryUtil.ALL_POS, false);

		List<User> actualUsers = baseModelSearchResult.getBaseModels();

		Assert.assertEquals(
			actualUsers.toString(), expectedSize, actualUsers.size());

		Assert.assertEquals(expectedSize, baseModelSearchResult.getLength());
	}

	private BaseModelSearchResult<User> _searchAccountUsers(
			String keywords, int cur, int delta, boolean reverse)
		throws Exception {

		return _accountUserRetriever.searchAccountUsers(
			_accountEntry.getAccountEntryId(), keywords,
			WorkflowConstants.STATUS_APPROVED, cur, delta, "screenName",
			reverse);
	}

	@DeleteAfterTestRun
	private AccountEntry _accountEntry;

	@Inject
	private AccountEntryLocalService _accountEntryLocalService;

	@Inject
	private AccountEntryUserRelLocalService _accountEntryUserRelLocalService;

	@DeleteAfterTestRun
	private final List<AccountEntryUserRel> _accountEntryUserRels =
		new ArrayList<>();

	@Inject
	private AccountUserRetriever _accountUserRetriever;

	@DeleteAfterTestRun
	private final List<User> _users = new ArrayList<>();

}