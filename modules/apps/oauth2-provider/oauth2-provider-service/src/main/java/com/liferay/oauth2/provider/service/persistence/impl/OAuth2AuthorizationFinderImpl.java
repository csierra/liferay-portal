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

package com.liferay.oauth2.provider.service.persistence.impl;


import com.liferay.oauth2.provider.model.OAuth2Authorization;
import com.liferay.oauth2.provider.service.persistence.OAuth2AuthorizationFinder;
import com.liferay.portal.dao.orm.custom.sql.CustomSQLUtil;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Tomas Polesovsky
 */
public class OAuth2AuthorizationFinderImpl
	extends OAuth2AuthorizationFinderBaseImpl
	implements OAuth2AuthorizationFinder {

	public static final String FIND_ALL_AUTHORIZATIONS =
		OAuth2AuthorizationFinder.class.getName() + ".findAuthorizations";

	@Override
	public int countByApplicationId(long companyId, long applicationId) {
		Session session = null;

		try {
			session = openSession();

			StringBundler query = new StringBundler(4);

			query.append("SELECT count(*) AS " + COUNT_COLUMN_NAME + " FROM (");

			query.append(
				CustomSQLUtil.get(getClass(), FIND_ALL_AUTHORIZATIONS));

			query.append(_FIND_BY_APPLICATION_ID_FILTER);

			query.append(") TEMP_TABLE");

			SQLQuery q = session.createSQLQuery(query.toString());

			q.addScalar(COUNT_COLUMN_NAME, Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(applicationId);
			qPos.add(companyId);

			Iterator<Long> itr = q.iterate();

			if (itr.hasNext()) {
				Long count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	public List<OAuth2Authorization> findByApplicationId(
		long companyId, long applicationId, int start, int end,
		OrderByComparator<OAuth2Authorization> orderByComparator) {

		Session session = null;

		try {
			session = openSession();

			int length = 2;

			if (orderByComparator != null) {
				length += orderByComparator.getOrderByFields().length * 2;
			}
			else {
				length += 1;
			}

			StringBundler query = new StringBundler(length);

			query.append(
				CustomSQLUtil.get(getClass(), FIND_ALL_AUTHORIZATIONS));

			query.append(_FIND_BY_APPLICATION_ID_FILTER);

			if (orderByComparator != null) {
				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				query.append(_ORDER_BY_CREATE_DATE_DESC);
			}

			SQLQuery q = session.createSQLQuery(query.toString());

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(applicationId);
			qPos.add(companyId);

			List<OAuth2Authorization> result = new ArrayList<>();

			Iterator<Object[]> iterator =
				(Iterator<Object[]>)QueryUtil.iterate(
					q, getDialect(), start, end);

			while (iterator.hasNext()) {
				Object[] row = iterator.next();

				OAuth2Authorization oAuth2Authorization =
					new OAuth2Authorization();

				int i = 0;

				oAuth2Authorization.setAccessTokenExpiresDate(_date(row[i++]));
				oAuth2Authorization.setCompanyId(_long(row[i++]));
				oAuth2Authorization.setCreateDate(_date(row[i++]));
				oAuth2Authorization.setoAuth2ApplicationId(_long(row[i++]));
				oAuth2Authorization.setoAuth2RefreshTokenId(_long(row[i++]));
				oAuth2Authorization.setoAuth2TokenId(_long(row[i++]));
				oAuth2Authorization.setRefreshTokenExpiresDate(_date(row[i++]));
				oAuth2Authorization.setRemoteIPInfo(_string(row[i++]));
				oAuth2Authorization.setScopes(_string(row[i++]));
				oAuth2Authorization.setUserId(_long(row[i++]));
				oAuth2Authorization.setUserName(_string(row[i++]));

				result.add(oAuth2Authorization);
			}

			return result;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	private static final Date _date(Object o) {
		return (Date)o;
	}

	private static final long _long(Object o) {
		if (o == null) {
			return 0;
		}
		if (o instanceof BigInteger) {
			return BigInteger.class.cast(o).longValue();
		}
		return (Long)o;
	}

	private static final String _string(Object o) {
		return (String)o;
	}

	private static final String _ORDER_BY_CREATE_DATE_DESC =
		" ORDER BY createDate DESC";
	private static final String _ORDER_BY_ENTITY_ALIAS = "OAuth2Authorization.";
	private static final String _FIND_BY_APPLICATION_ID_FILTER =
		" WHERE OAuth2Authorization.oAuth2ApplicationId = ?" +
		" AND OAuth2Authorization.companyId = ?";
}
