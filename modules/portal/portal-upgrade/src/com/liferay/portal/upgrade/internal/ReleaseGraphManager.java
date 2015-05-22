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
import com.liferay.portal.kernel.util.Function;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.upgrade.internal.ReleaseManager.UpgradeProcessInfo;

import java.util.Arrays;
import java.util.List;

import org.jgrapht.EdgeFactory;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

/**
 * @author Carlos Sierra Andrés
 */
public class ReleaseGraphManager {

	public static void main(String args[]) {
		final ReleaseGraphManager releaseGraphManager =
			new ReleaseGraphManager(Arrays.asList(
				new UpgradeProcessInfo(
					"0.0.0", "1.0.0", new UpgradeProcess() {}),
				new UpgradeProcessInfo(
					"1.0.0", "2.0.0", new UpgradeProcess() {}),
				new UpgradeProcessInfo(
					"2.0.0", "3.0.0", new UpgradeProcess() {}),
				new UpgradeProcessInfo(
					"3.0.0", "3.1.0", new UpgradeProcess() {}),
				new UpgradeProcessInfo(
					"0.0.0", "3.1.0", new UpgradeProcess() {})
			));

		final List<UpgradeProcess> upgradePath =
			releaseGraphManager.getUpgradePath("0.0.0", "3.1.0");

		System.out.println(upgradePath);
	}

	public ReleaseGraphManager(
		final List<UpgradeProcessInfo> upgradeProcessInfos) {

		_upgradeProcessInfos = upgradeProcessInfos;

		_directedGraph = new DefaultDirectedGraph<>(
			new UpgradeProcessEdgeFactory(upgradeProcessInfos));

		for (UpgradeProcessInfo upgradeProcessInfo : upgradeProcessInfos) {
			_directedGraph.addVertex(upgradeProcessInfo.getFrom());
			_directedGraph.addVertex(upgradeProcessInfo.getTo());

			_directedGraph.addEdge(
				upgradeProcessInfo.getFrom(), upgradeProcessInfo.getTo(),
				new UpgradeProcessEdge(upgradeProcessInfo.getUpgradeProcess()));
		}
	}

	public List<UpgradeProcess> getUpgradePath(String from, String to) {
		if (!_directedGraph.containsVertex(from)) {
			throw new IllegalArgumentException(
				"There is not a UpgradeProcess starting in " + from);
		}

		if (!_directedGraph.containsVertex(to)) {
			throw new IllegalArgumentException(
				"There is not a UpgradeProcess ending in " + to);
		}

		DijkstraShortestPath<String, UpgradeProcessEdge> dijkstraShortestPath =
			new DijkstraShortestPath<>(_directedGraph, from, to);

		final List<UpgradeProcessEdge> pathEdgeList =
			dijkstraShortestPath.getPathEdgeList();

		if (pathEdgeList == null) {
			throw new IllegalArgumentException(
				"There is no possible upgrade path between " + from + " and " +
					to);
		}

		return ListUtil.toList(
			pathEdgeList, new Function<UpgradeProcessEdge, UpgradeProcess>() {

			@Override
			public UpgradeProcess apply(UpgradeProcessEdge upgradeProcessEdge) {
				return upgradeProcessEdge._upgradeProcess;
			}
		});
	}

	private final DefaultDirectedGraph<String, UpgradeProcessEdge>
		_directedGraph;
	private List<UpgradeProcessInfo> _upgradeProcessInfos;

	private static class UpgradeProcessEdge extends DefaultEdge {

		public UpgradeProcessEdge(UpgradeProcess upgradeProcess) {
			_upgradeProcess = upgradeProcess;
		}

		public UpgradeProcess getUpgradeProcess() {
			return _upgradeProcess;
		}

		private final UpgradeProcess _upgradeProcess;

	}

	private static class UpgradeProcessEdgeFactory
		implements EdgeFactory<String, UpgradeProcessEdge> {

		public UpgradeProcessEdgeFactory(
			List<UpgradeProcessInfo> upgradeProcessInfos) {

			_upgradeProcessInfos = upgradeProcessInfos;
		}

		@Override
		public UpgradeProcessEdge createEdge(
			String sourceVertex, String targetVertex) {

			for (UpgradeProcessInfo upgradeProcessInfo : _upgradeProcessInfos) {
				if (upgradeProcessInfo.getFrom().equals(sourceVertex) &&
					upgradeProcessInfo.getTo().equals(targetVertex)) {

					return new UpgradeProcessEdge(
						upgradeProcessInfo.getUpgradeProcess());
				}
			}

			return null;
		}

		private final List<UpgradeProcessInfo> _upgradeProcessInfos;

	}

}