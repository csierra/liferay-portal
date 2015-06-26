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

package com.liferay.portal.upgrade.internal.graph;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.upgrade.internal.UpgradeInfo;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Carlos Sierra Andrés
 * @author Miguel Pastor
 */
public class ReleaseGraphManagerTest {

	@Test
	public void testFindEndNode() {
		UpgradeInfo upi1 = createUpgradeProcessInfo("0.0.0", "0.1.0");
		UpgradeInfo upi2 = createUpgradeProcessInfo("0.1.0", "0.2.0");
		UpgradeInfo upi3 = createUpgradeProcessInfo("0.2.0", "1.0.0");
		UpgradeInfo upi4 = createUpgradeProcessInfo("1.0.0", "2.0.0");

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			Arrays.asList(upi1, upi2, upi3, upi4));

		String endNode = releaseGraphManager.findEndNode();

		Assert.assertEquals("200", endNode);
	}

	@Test(expected = IllegalStateException.class)
	public void testFindEndNodeWithMultipleEndNodes() {
		UpgradeInfo upi1 = createUpgradeProcessInfo("0.0.0", "0.1.0");
		UpgradeInfo upi2 = createUpgradeProcessInfo("0.1.0", "0.2.0");
		UpgradeInfo upi3 = createUpgradeProcessInfo("0.2.0", "1.0.0");
		UpgradeInfo upi4 = createUpgradeProcessInfo("1.0.0", "2.0.0");
		UpgradeInfo upi5 = createUpgradeProcessInfo("1.0.0", "2.2.0");

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			Arrays.asList(upi1, upi2, upi3, upi4, upi5));

		releaseGraphManager.findEndNode();
	}

	@Test
	public void testGetUpgradePath() {
		UpgradeInfo upi1 = createUpgradeProcessInfo("0.0.0", "0.1.0");
		UpgradeInfo upi2 = createUpgradeProcessInfo("0.1.0", "0.2.0");
		UpgradeInfo upi3 = createUpgradeProcessInfo("0.2.0", "1.0.0");
		UpgradeInfo upi4 = createUpgradeProcessInfo("1.0.0", "2.0.0");

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			Arrays.asList(upi1, upi2, upi3, upi4));

		List<UpgradeInfo> upgradePath =
			releaseGraphManager.getUpgradePath("0", "200");

		Assert.assertEquals(Arrays.asList(upi1, upi2, upi3, upi4), upgradePath);
	}

	@Test
	public void testGetUpgradePathNotInOrder() {
		UpgradeInfo upi1 = createUpgradeProcessInfo("0.0.0", "0.1.0");
		UpgradeInfo upi2 = createUpgradeProcessInfo("0.1.0", "0.2.0");
		UpgradeInfo upi3 = createUpgradeProcessInfo("0.2.0", "1.0.0");
		UpgradeInfo upi4 = createUpgradeProcessInfo("1.0.0", "2.0.0");

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			Arrays.asList(upi4, upi2, upi1, upi3));

		List<UpgradeInfo> upgradePath =
			releaseGraphManager.getUpgradePath("0", "200");

		Assert.assertEquals(Arrays.asList(upi1, upi2, upi3, upi4), upgradePath);
	}

	@Test
	public void testGetUpgradePathWithCyclesReturnsShortestPath() {
		UpgradeInfo upi1 = createUpgradeProcessInfo("0.0.0", "0.1.0");
		UpgradeInfo upi2 = createUpgradeProcessInfo("0.1.0", "0.2.0");
		UpgradeInfo upi3 = createUpgradeProcessInfo("0.2.0", "1.0.0");
		UpgradeInfo upi4 = createUpgradeProcessInfo("1.0.0", "2.0.0");
		UpgradeInfo upi5 = createUpgradeProcessInfo("0.0.0", "2.0.0");

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			Arrays.asList(upi1, upi2, upi3, upi4, upi5));

		List<UpgradeInfo> upgradePath =
			releaseGraphManager.getUpgradePath("0", "200");

		Assert.assertEquals(Arrays.asList(upi5), upgradePath);
	}

	@Test
	public void testGetUpgradePathWithCyclesReturnsShortestPathWhenNotZero() {
		UpgradeInfo upi1 = createUpgradeProcessInfo("0.0.0", "0.1.0");
		UpgradeInfo upi2 = createUpgradeProcessInfo("0.1.0", "0.2.0");
		UpgradeInfo upi3 = createUpgradeProcessInfo("0.2.0", "1.0.0");
		UpgradeInfo upi4 = createUpgradeProcessInfo("1.0.0", "2.0.0");
		UpgradeInfo upi5 = createUpgradeProcessInfo("0.0.0", "2.0.0");

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			Arrays.asList(upi1, upi2, upi3, upi4, upi5));

		List<UpgradeInfo> upgradePath =
			releaseGraphManager.getUpgradePath("10", "200");

		Assert.assertEquals(Arrays.asList(upi2, upi3, upi4), upgradePath);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetUpgradePathWithIllegalArguments() {
		UpgradeInfo upi1 = createUpgradeProcessInfo("0.0.0", "0.1.0");
		UpgradeInfo upi2 = createUpgradeProcessInfo("0.1.0", "0.2.0");
		UpgradeInfo upi3 = createUpgradeProcessInfo("0.2.0", "1.0.0");
		UpgradeInfo upi4 = createUpgradeProcessInfo("1.0.0", "2.0.0");

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			Arrays.asList(upi4, upi2, upi1, upi3));

		releaseGraphManager.getUpgradePath("0", "201");
	}

	protected UpgradeInfo createUpgradeProcessInfo(
		String from, String to) {

		return new UpgradeInfo(
			from, to, new TestUpgradeProcess(from + " -> " + to));
	}

	private static class TestUpgradeProcess extends UpgradeProcess {

		public TestUpgradeProcess(String name) {
			_name = name;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}

			if ((o == null) || (getClass() != o.getClass())) {
				return false;
			}

			TestUpgradeProcess testUpgradeProcess = (TestUpgradeProcess)o;

			if (!_name.equals(testUpgradeProcess._name)) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return _name.hashCode();
		}

		private final String _name;

	}

}