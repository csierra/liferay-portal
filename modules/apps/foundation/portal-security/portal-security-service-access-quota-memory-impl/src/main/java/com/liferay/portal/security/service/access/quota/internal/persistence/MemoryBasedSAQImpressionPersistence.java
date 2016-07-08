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

package com.liferay.portal.security.service.access.quota.internal.persistence;

import com.liferay.portal.security.service.access.quota.metric.SAQMetricMatcher;
import com.liferay.portal.security.service.access.quota.persistence.BaseIndexedSAQImpressionPersistence;
import com.liferay.portal.security.service.access.quota.persistence.SAQImpression;
import com.liferay.portal.security.service.access.quota.persistence.SAQImpressionPersistence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;

/**
 * @author Stian Sigvartsen
 */
@Component (service = SAQImpressionPersistence.class)
public class MemoryBasedSAQImpressionPersistence
	extends BaseIndexedSAQImpressionPersistence {

	@Override
	public void createImpression(
		long companyId, Map<String, String> metrics,
		long expiryIntervalMillis) {

		long bucketStartMillis = _getBucketStartMillis();

		LinkedList<SAQImpressionsBucket> buckets = _getBuckets(companyId);
		SAQImpressionsBucket currentBucket = buckets.peekLast();

		if ((currentBucket == null) ||
			(bucketStartMillis != currentBucket.getStartMillis())) {

			currentBucket = new SAQImpressionsBucket(
				bucketStartMillis, bucketStartMillis + expiryIntervalMillis);
			buckets.add(currentBucket);
		}

		AggregateSAQImpression matchedImpression = _fetchCompleteMetricsMatch(
			metrics, currentBucket);

		if (matchedImpression != null) {
			matchedImpression.incrementWeight();
		}
		else {
			AggregateSAQImpression impression = new AggregateSAQImpression(
				String.valueOf(_nextKey), metrics, bucketStartMillis);

			currentBucket.indexImpression(impression);
			_nextKey++;
		}
	}

	@Override
	public Iterator<SAQImpression> findImpressionsMatchingMetric(
		long companyId, String metricName, SAQMetricMatcher metricMatcher) {

		return _findImpressions(
			companyId, metricName, metricMatcher).iterator();
	}

	@Override
	public int getImpressionsCount(long companyId, long expiryIntervalMillis) {
		long nowMillis = System.currentTimeMillis();
		int totalWeight = 0;

		List<SAQImpressionsBucket> buckets = _getBuckets(companyId);
		Iterator<SAQImpressionsBucket> i = buckets.iterator();

		while (i.hasNext()) {
			SAQImpressionsBucket bucket = i.next();

			if (bucket.getExpiryMillis() < nowMillis) {
				i.remove();
			}
			else if ((bucket.getStartMillis() + expiryIntervalMillis) >
						nowMillis) {

				for (AggregateSAQImpression impression :
						bucket.getAllImpressions()) {

					totalWeight += impression.getWeight();
				}
			}
		}

		return totalWeight;
	}

	@Override
	public Iterator<SAQImpression> findAllImpressions(long companyId) {
		return _findImpressions(companyId, null, null).iterator();
	}

	private AggregateSAQImpression _fetchCompleteMetricsMatch(
		Map<String, String> metrics, SAQImpressionsBucket currentBucket) {

		Set<AggregateSAQImpression> intersectSet = null;

		for (Map.Entry<String, String> entry : metrics.entrySet()) {
			Set<AggregateSAQImpression> metricImpressions =
				currentBucket.getImpressions(entry.getKey(), entry.getValue());

			if (metricImpressions.size() == 0) {
				return null;
			}
			else if (intersectSet == null) {
				intersectSet = metricImpressions;
			}
			else {
				intersectSet.retainAll(metricImpressions);
			}
		}

		if ((intersectSet != null) && (intersectSet.size() > 0)) {
			return intersectSet.iterator().next();
		}
		else {
			return null;
		}
	}

	@Override
	public Iterator<String> findAllMetricValues(
		long companyId, String metricName) {

		Set<String> metricValues = new HashSet<>();

		for (SAQImpressionsBucket bucket : _getBuckets(companyId)) {
			metricValues.addAll(bucket.getAllMetricValues(metricName));
		}

		return metricValues.iterator();
	}

	private Set<SAQImpression> _findImpressions(
		long companyId, String metricName, SAQMetricMatcher metricMatcher) {

		long nowMillis = System.currentTimeMillis();

		List<SAQImpressionsBucket> buckets = _getBuckets(companyId);

		Set<SAQImpression> union = new HashSet<>();

		Iterator<SAQImpressionsBucket> i = buckets.iterator();

		while (i.hasNext()) {
			SAQImpressionsBucket bucket = i.next();

			if (bucket.getExpiryMillis() < nowMillis) {
				i.remove();
			}
			else {
				if (metricMatcher == null) {
					union.addAll(bucket.getImpressions(metricName, null));
				}
				else {
					for (String value : bucket.getAllMetricValues(metricName)) {
						if (metricMatcher.matches(value)) {
							union.addAll(
								bucket.getImpressions(metricName, value));
						}
					}
				}
			}
		}

		return union;
	}

	private LinkedList<SAQImpressionsBucket> _getBuckets(long companyId) {
		Long companyIdLong = Long.valueOf(companyId);
		LinkedList<SAQImpressionsBucket> buckets = _buckets.get(companyIdLong);

		if (buckets == null) {
			buckets = new LinkedList<>();
			_buckets.put(companyIdLong, buckets);
		}

		return buckets;
	}

	private long _getBucketStartMillis() {

		// Round up current time millis to bucket boundary
		// So that the quota intervalMillis is honored (as minimum)

		long bucketStart = System.currentTimeMillis();
		return
			bucketStart - (bucketStart % _BUCKET_INTERVAL) + _BUCKET_INTERVAL;
	}

	private static final long _BUCKET_INTERVAL = 1000;

	private final Map<Long, LinkedList<SAQImpressionsBucket>> _buckets =
		new HashMap<>();
	private long _nextKey = 0;

}