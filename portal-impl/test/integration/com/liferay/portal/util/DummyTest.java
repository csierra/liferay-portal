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

package com.liferay.portal.util;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.TransactionalExecutionTestListener;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Eduardo Garcia
 */
@ExecutionTestListeners(
	listeners = {
		EnvironmentExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class DummyTest {
	@Test
	public void testUpdateGroupParentGroupId()
		throws Exception {

		Group group = GroupTestUtil.addGroup();

		Group parentGroup = GroupTestUtil.addGroup();

		group.setParentGroupId(parentGroup.getGroupId());

		GroupLocalServiceUtil.updateGroup(group);

		group = GroupLocalServiceUtil.getGroup(group.getGroupId());

		Assert.assertEquals(parentGroup.getGroupId(), group.getParentGroupId());
	}

	@Test
	public void testUpdateLayoutParentLayoutId()
		throws Exception {

		Group group = GroupTestUtil.addGroup();

		Layout layout = LayoutTestUtil.addLayout(group);

		Layout parentLayout = LayoutTestUtil.addLayout(group);

		layout.setParentLayoutId(parentLayout.getPlid());

		LayoutLocalServiceUtil.updateLayout(layout);

		layout = LayoutLocalServiceUtil.getLayout(layout.getPlid());

		Assert.assertEquals(parentLayout.getPlid(), layout.getParentLayoutId());
	}

}