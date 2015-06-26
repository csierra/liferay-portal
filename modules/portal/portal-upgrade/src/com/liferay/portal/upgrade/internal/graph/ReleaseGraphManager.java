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

import com.liferay.portal.kernel.util.Function;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.upgrade.internal.UpgradeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.EdgeFactory;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

/**
 * @author Carlos Sierra Andrés
 * @author Miguel Pastor
 */
public class ReleaseGraphManager {

	public ReleaseGraphManager(
		final List<UpgradeInfo> upgradeInfos) {

		_directedGraph = new DefaultDirectedGraph<>(
			new UpgradeProcessEdgeFactory(upgradeInfos));

		for (UpgradeInfo upgradeInfo : upgradeInfos) {
			_directedGraph.addVertex(upgradeInfo.getFrom());
			_directedGraph.addVertex(upgradeInfo.getTo());

			_directedGraph.addEdge(
				upgradeInfo.getFrom(), upgradeInfo.getTo(),
				new UpgradeProcessEdge(upgradeInfo));
		}
	}

	public List<UpgradeInfo> getUpgradePath(String from) {
		String endNode = findEndNode();

		return getUpgradePath(from, endNode);
	}

	public List<UpgradeInfo> getUpgradePath(String from, String to) {
		if (!_directedGraph.containsVertex(from)) {
			throw new IllegalArgumentException(
				"There is not an UpgradeProcess starting in " + from);
		}

		if (!_directedGraph.containsVertex(to)) {
			throw new IllegalArgumentException(
				"There is not an UpgradeProcess ending in " + to);
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
			pathEdgeList,
			new Function<UpgradeProcessEdge, UpgradeInfo>() {

				@Override
				public UpgradeInfo apply(
					UpgradeProcessEdge upgradeProcessEdge) {

					return upgradeProcessEdge._upgradeInfo;
				}
			});
	}

	protected String findEndNode() {
		final List<String> endVertices = new ArrayList<>();

		final Set<String> vertices = _directedGraph.vertexSet();

		for (String vertex : vertices) {
			Set<UpgradeProcessEdge> upgradeProcessEdges =
				_directedGraph.outgoingEdgesOf(vertex);

			if (upgradeProcessEdges.isEmpty()) {
				endVertices.add(vertex);
			}
		}

		if (endVertices.size() == 1) {
			return endVertices.get(0);
		}

		if (endVertices.size() > 1) {
			throw new IllegalStateException(
				"There are more that one possible end nodes " + endVertices);
		}

		throw new IllegalStateException("No end nodes!");
	}

	private final DefaultDirectedGraph<String, UpgradeProcessEdge>
		_directedGraph;

	private static class UpgradeProcessEdge extends DefaultEdge {

		public UpgradeProcessEdge(UpgradeInfo upgradeInfo) {
			_upgradeInfo = upgradeInfo;
		}

		public UpgradeInfo getUpgradeInfo() {
			return _upgradeInfo;
		}

		private final UpgradeInfo _upgradeInfo;

	}

	private static class UpgradeProcessEdgeFactory
		implements EdgeFactory<String, UpgradeProcessEdge> {

		public UpgradeProcessEdgeFactory(
			List<UpgradeInfo> upgradeInfos) {

			_upgradeInfos = upgradeInfos;
		}

		@Override
		public UpgradeProcessEdge createEdge(
			String sourceVertex, String targetVertex) {

			for (UpgradeInfo upgradeInfo : _upgradeInfos) {
				String from = upgradeInfo.getFrom();
				String to = upgradeInfo.getTo();

				if (from.equals(sourceVertex) && to.equals(targetVertex)) {
					return new UpgradeProcessEdge(upgradeInfo);
				}
			}

			return null;
		}

		private final List<UpgradeInfo> _upgradeInfos;

	}

}