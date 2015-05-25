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

package com.liferay.portal.upgrade.internal;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.upgrade.internal.ReleaseManager.UpgradeProcessInfo;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Carlos Sierra Andrés
 */
public class ReleaseGraphManagerTest {

	@Test
	public void testFindEndNode() {
		UpgradeProcessInfo up1 = createUpgradeProcessInfo("0.0.0", "0.1.0");
		UpgradeProcessInfo up2 = createUpgradeProcessInfo("0.1.0", "0.2.0");
		UpgradeProcessInfo up3 = createUpgradeProcessInfo("0.2.0", "1.0.0");
		UpgradeProcessInfo up4 = createUpgradeProcessInfo("1.0.0", "2.0.0");

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			Arrays.asList(up1, up2, up3, up4));

		String endNode = releaseGraphManager.findEndNode();

		Assert.assertEquals("200", endNode);
	}

	@Test(expected = IllegalStateException.class)
	public void testFindEndNodeWithMultipleEndNodes() {
		UpgradeProcessInfo up1 = createUpgradeProcessInfo("0.0.0", "0.1.0");
		UpgradeProcessInfo up2 = createUpgradeProcessInfo("0.1.0", "0.2.0");
		UpgradeProcessInfo up3 = createUpgradeProcessInfo("0.2.0", "1.0.0");
		UpgradeProcessInfo up4 = createUpgradeProcessInfo("1.0.0", "2.0.0");
		UpgradeProcessInfo up5 = createUpgradeProcessInfo("1.0.0", "2.2.0");

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			Arrays.asList(up1, up2, up3, up4, up5));

		releaseGraphManager.findEndNode();
	}

	@Test
	public void testGetUpgragePath() {
		UpgradeProcessInfo up1 = createUpgradeProcessInfo("0.0.0", "0.1.0");
		UpgradeProcessInfo up2 = createUpgradeProcessInfo("0.1.0", "0.2.0");
		UpgradeProcessInfo up3 = createUpgradeProcessInfo("0.2.0", "1.0.0");
		UpgradeProcessInfo up4 = createUpgradeProcessInfo("1.0.0", "2.0.0");

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			Arrays.asList(up1, up2, up3, up4));

		List<UpgradeProcessInfo> upgradePath =
			releaseGraphManager.getUpgradePath("0", "200");

		Assert.assertEquals(Arrays.asList(up1, up2, up3, up4), upgradePath);
	}

	@Test
	public void testGetUpgragePathNotInOrder() {
		UpgradeProcessInfo up1 = createUpgradeProcessInfo("0.0.0", "0.1.0");
		UpgradeProcessInfo up2 = createUpgradeProcessInfo("0.1.0", "0.2.0");
		UpgradeProcessInfo up3 = createUpgradeProcessInfo("0.2.0", "1.0.0");
		UpgradeProcessInfo up4 = createUpgradeProcessInfo("1.0.0", "2.0.0");

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			Arrays.asList(up4, up2, up1, up3));

		List<UpgradeProcessInfo> upgradePath =
			releaseGraphManager.getUpgradePath("0", "200");

		Assert.assertEquals(Arrays.asList(up1, up2, up3, up4), upgradePath);
	}

	@Test
	public void testGetUpgragePathWithCyclesReturnsShortestPath() {
		UpgradeProcessInfo up1 = createUpgradeProcessInfo("0.0.0", "0.1.0");
		UpgradeProcessInfo up2 = createUpgradeProcessInfo("0.1.0", "0.2.0");
		UpgradeProcessInfo up3 = createUpgradeProcessInfo("0.2.0", "1.0.0");
		UpgradeProcessInfo up4 = createUpgradeProcessInfo("1.0.0", "2.0.0");
		UpgradeProcessInfo up5 = createUpgradeProcessInfo("0.0.0", "2.0.0");

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			Arrays.asList(up1, up2, up3, up4, up5));

		List<UpgradeProcessInfo> upgradePath =
			releaseGraphManager.getUpgradePath("0", "200");

		Assert.assertEquals(Arrays.asList(up5), upgradePath);
	}

	@Test
	public void testGetUpgragePathWithCyclesReturnsShortestPathWhenNotZero() {
		UpgradeProcessInfo up1 = createUpgradeProcessInfo("0.0.0", "0.1.0");
		UpgradeProcessInfo up2 = createUpgradeProcessInfo("0.1.0", "0.2.0");
		UpgradeProcessInfo up3 = createUpgradeProcessInfo("0.2.0", "1.0.0");
		UpgradeProcessInfo up4 = createUpgradeProcessInfo("1.0.0", "2.0.0");
		UpgradeProcessInfo up5 = createUpgradeProcessInfo("0.0.0", "2.0.0");

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			Arrays.asList(up1, up2, up3, up4, up5));

		List<UpgradeProcessInfo> upgradePath =
			releaseGraphManager.getUpgradePath("10", "200");

		Assert.assertEquals(Arrays.asList(up2, up3, up4), upgradePath);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetUpgragePathWithIllegalArguments() {
		UpgradeProcessInfo up1 = createUpgradeProcessInfo("0.0.0", "0.1.0");
		UpgradeProcessInfo up2 = createUpgradeProcessInfo("0.1.0", "0.2.0");
		UpgradeProcessInfo up3 = createUpgradeProcessInfo("0.2.0", "1.0.0");
		UpgradeProcessInfo up4 = createUpgradeProcessInfo("1.0.0", "2.0.0");

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			Arrays.asList(up4, up2, up1, up3));

		releaseGraphManager.getUpgradePath("0", "201");
	}

	protected UpgradeProcessInfo createUpgradeProcessInfo(
		String from, String to) {

		return new UpgradeProcessInfo(
			from, to, new TestUpgradeProcess(from + " -> " + to));
	}

	private static class TestUpgradeProcess extends UpgradeProcess {

		public TestUpgradeProcess(String name) {
			this.name = name;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)return true;

			if (o == null || getClass() != o.getClass()) return false;

			TestUpgradeProcess that = (TestUpgradeProcess)o;

			if (!name.equals(that.name))return false;

			return true;
		}

		@Override
		public int hashCode() {
			return name.hashCode();
		}

		private final String name;

	}

}