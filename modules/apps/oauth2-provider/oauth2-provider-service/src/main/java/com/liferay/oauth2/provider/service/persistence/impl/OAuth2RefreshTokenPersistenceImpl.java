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

import aQute.bnd.annotation.ProviderType;

import com.liferay.oauth2.provider.exception.NoSuchOAuth2RefreshTokenException;
import com.liferay.oauth2.provider.model.OAuth2RefreshToken;
import com.liferay.oauth2.provider.model.impl.OAuth2RefreshTokenImpl;
import com.liferay.oauth2.provider.model.impl.OAuth2RefreshTokenModelImpl;
import com.liferay.oauth2.provider.service.persistence.OAuth2RefreshTokenPersistence;

import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.persistence.CompanyProvider;
import com.liferay.portal.kernel.service.persistence.CompanyProviderWrapper;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The persistence implementation for the o auth2 refresh token service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2RefreshTokenPersistence
 * @see com.liferay.oauth2.provider.service.persistence.OAuth2RefreshTokenUtil
 * @generated
 */
@ProviderType
public class OAuth2RefreshTokenPersistenceImpl extends BasePersistenceImpl<OAuth2RefreshToken>
	implements OAuth2RefreshTokenPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link OAuth2RefreshTokenUtil} to access the o auth2 refresh token persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = OAuth2RefreshTokenImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
			OAuth2RefreshTokenModelImpl.FINDER_CACHE_ENABLED,
			OAuth2RefreshTokenImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
			OAuth2RefreshTokenModelImpl.FINDER_CACHE_ENABLED,
			OAuth2RefreshTokenImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
			OAuth2RefreshTokenModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_A = new FinderPath(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
			OAuth2RefreshTokenModelImpl.FINDER_CACHE_ENABLED,
			OAuth2RefreshTokenImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByA",
			new String[] {
				String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A = new FinderPath(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
			OAuth2RefreshTokenModelImpl.FINDER_CACHE_ENABLED,
			OAuth2RefreshTokenImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByA",
			new String[] { String.class.getName() },
			OAuth2RefreshTokenModelImpl.OAUTH2APPLICATIONID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_A = new FinderPath(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
			OAuth2RefreshTokenModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByA",
			new String[] { String.class.getName() });

	/**
	 * Returns all the o auth2 refresh tokens where oAuth2ApplicationId = &#63;.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @return the matching o auth2 refresh tokens
	 */
	@Override
	public List<OAuth2RefreshToken> findByA(String oAuth2ApplicationId) {
		return findByA(oAuth2ApplicationId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the o auth2 refresh tokens where oAuth2ApplicationId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param start the lower bound of the range of o auth2 refresh tokens
	 * @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	 * @return the range of matching o auth2 refresh tokens
	 */
	@Override
	public List<OAuth2RefreshToken> findByA(String oAuth2ApplicationId,
		int start, int end) {
		return findByA(oAuth2ApplicationId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the o auth2 refresh tokens where oAuth2ApplicationId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param start the lower bound of the range of o auth2 refresh tokens
	 * @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching o auth2 refresh tokens
	 */
	@Override
	public List<OAuth2RefreshToken> findByA(String oAuth2ApplicationId,
		int start, int end,
		OrderByComparator<OAuth2RefreshToken> orderByComparator) {
		return findByA(oAuth2ApplicationId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the o auth2 refresh tokens where oAuth2ApplicationId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param start the lower bound of the range of o auth2 refresh tokens
	 * @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching o auth2 refresh tokens
	 */
	@Override
	public List<OAuth2RefreshToken> findByA(String oAuth2ApplicationId,
		int start, int end,
		OrderByComparator<OAuth2RefreshToken> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A;
			finderArgs = new Object[] { oAuth2ApplicationId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_A;
			finderArgs = new Object[] {
					oAuth2ApplicationId,
					
					start, end, orderByComparator
				};
		}

		List<OAuth2RefreshToken> list = null;

		if (retrieveFromCache) {
			list = (List<OAuth2RefreshToken>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (OAuth2RefreshToken oAuth2RefreshToken : list) {
					if (!Objects.equals(oAuth2ApplicationId,
								oAuth2RefreshToken.getOAuth2ApplicationId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_OAUTH2REFRESHTOKEN_WHERE);

			boolean bindOAuth2ApplicationId = false;

			if (oAuth2ApplicationId == null) {
				query.append(_FINDER_COLUMN_A_OAUTH2APPLICATIONID_1);
			}
			else if (oAuth2ApplicationId.equals("")) {
				query.append(_FINDER_COLUMN_A_OAUTH2APPLICATIONID_3);
			}
			else {
				bindOAuth2ApplicationId = true;

				query.append(_FINDER_COLUMN_A_OAUTH2APPLICATIONID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(OAuth2RefreshTokenModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindOAuth2ApplicationId) {
					qPos.add(oAuth2ApplicationId);
				}

				if (!pagination) {
					list = (List<OAuth2RefreshToken>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<OAuth2RefreshToken>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63;.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching o auth2 refresh token
	 * @throws NoSuchOAuth2RefreshTokenException if a matching o auth2 refresh token could not be found
	 */
	@Override
	public OAuth2RefreshToken findByA_First(String oAuth2ApplicationId,
		OrderByComparator<OAuth2RefreshToken> orderByComparator)
		throws NoSuchOAuth2RefreshTokenException {
		OAuth2RefreshToken oAuth2RefreshToken = fetchByA_First(oAuth2ApplicationId,
				orderByComparator);

		if (oAuth2RefreshToken != null) {
			return oAuth2RefreshToken;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("oAuth2ApplicationId=");
		msg.append(oAuth2ApplicationId);

		msg.append("}");

		throw new NoSuchOAuth2RefreshTokenException(msg.toString());
	}

	/**
	 * Returns the first o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63;.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching o auth2 refresh token, or <code>null</code> if a matching o auth2 refresh token could not be found
	 */
	@Override
	public OAuth2RefreshToken fetchByA_First(String oAuth2ApplicationId,
		OrderByComparator<OAuth2RefreshToken> orderByComparator) {
		List<OAuth2RefreshToken> list = findByA(oAuth2ApplicationId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63;.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching o auth2 refresh token
	 * @throws NoSuchOAuth2RefreshTokenException if a matching o auth2 refresh token could not be found
	 */
	@Override
	public OAuth2RefreshToken findByA_Last(String oAuth2ApplicationId,
		OrderByComparator<OAuth2RefreshToken> orderByComparator)
		throws NoSuchOAuth2RefreshTokenException {
		OAuth2RefreshToken oAuth2RefreshToken = fetchByA_Last(oAuth2ApplicationId,
				orderByComparator);

		if (oAuth2RefreshToken != null) {
			return oAuth2RefreshToken;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("oAuth2ApplicationId=");
		msg.append(oAuth2ApplicationId);

		msg.append("}");

		throw new NoSuchOAuth2RefreshTokenException(msg.toString());
	}

	/**
	 * Returns the last o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63;.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching o auth2 refresh token, or <code>null</code> if a matching o auth2 refresh token could not be found
	 */
	@Override
	public OAuth2RefreshToken fetchByA_Last(String oAuth2ApplicationId,
		OrderByComparator<OAuth2RefreshToken> orderByComparator) {
		int count = countByA(oAuth2ApplicationId);

		if (count == 0) {
			return null;
		}

		List<OAuth2RefreshToken> list = findByA(oAuth2ApplicationId, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the o auth2 refresh tokens before and after the current o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63;.
	 *
	 * @param oAuth2RefreshTokenId the primary key of the current o auth2 refresh token
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next o auth2 refresh token
	 * @throws NoSuchOAuth2RefreshTokenException if a o auth2 refresh token with the primary key could not be found
	 */
	@Override
	public OAuth2RefreshToken[] findByA_PrevAndNext(
		String oAuth2RefreshTokenId, String oAuth2ApplicationId,
		OrderByComparator<OAuth2RefreshToken> orderByComparator)
		throws NoSuchOAuth2RefreshTokenException {
		OAuth2RefreshToken oAuth2RefreshToken = findByPrimaryKey(oAuth2RefreshTokenId);

		Session session = null;

		try {
			session = openSession();

			OAuth2RefreshToken[] array = new OAuth2RefreshTokenImpl[3];

			array[0] = getByA_PrevAndNext(session, oAuth2RefreshToken,
					oAuth2ApplicationId, orderByComparator, true);

			array[1] = oAuth2RefreshToken;

			array[2] = getByA_PrevAndNext(session, oAuth2RefreshToken,
					oAuth2ApplicationId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected OAuth2RefreshToken getByA_PrevAndNext(Session session,
		OAuth2RefreshToken oAuth2RefreshToken, String oAuth2ApplicationId,
		OrderByComparator<OAuth2RefreshToken> orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(4 +
					(orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_OAUTH2REFRESHTOKEN_WHERE);

		boolean bindOAuth2ApplicationId = false;

		if (oAuth2ApplicationId == null) {
			query.append(_FINDER_COLUMN_A_OAUTH2APPLICATIONID_1);
		}
		else if (oAuth2ApplicationId.equals("")) {
			query.append(_FINDER_COLUMN_A_OAUTH2APPLICATIONID_3);
		}
		else {
			bindOAuth2ApplicationId = true;

			query.append(_FINDER_COLUMN_A_OAUTH2APPLICATIONID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(OAuth2RefreshTokenModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindOAuth2ApplicationId) {
			qPos.add(oAuth2ApplicationId);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(oAuth2RefreshToken);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<OAuth2RefreshToken> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the o auth2 refresh tokens where oAuth2ApplicationId = &#63; from the database.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 */
	@Override
	public void removeByA(String oAuth2ApplicationId) {
		for (OAuth2RefreshToken oAuth2RefreshToken : findByA(
				oAuth2ApplicationId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(oAuth2RefreshToken);
		}
	}

	/**
	 * Returns the number of o auth2 refresh tokens where oAuth2ApplicationId = &#63;.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @return the number of matching o auth2 refresh tokens
	 */
	@Override
	public int countByA(String oAuth2ApplicationId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_A;

		Object[] finderArgs = new Object[] { oAuth2ApplicationId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_OAUTH2REFRESHTOKEN_WHERE);

			boolean bindOAuth2ApplicationId = false;

			if (oAuth2ApplicationId == null) {
				query.append(_FINDER_COLUMN_A_OAUTH2APPLICATIONID_1);
			}
			else if (oAuth2ApplicationId.equals("")) {
				query.append(_FINDER_COLUMN_A_OAUTH2APPLICATIONID_3);
			}
			else {
				bindOAuth2ApplicationId = true;

				query.append(_FINDER_COLUMN_A_OAUTH2APPLICATIONID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindOAuth2ApplicationId) {
					qPos.add(oAuth2ApplicationId);
				}

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_A_OAUTH2APPLICATIONID_1 = "oAuth2RefreshToken.oAuth2ApplicationId IS NULL";
	private static final String _FINDER_COLUMN_A_OAUTH2APPLICATIONID_2 = "oAuth2RefreshToken.oAuth2ApplicationId = ?";
	private static final String _FINDER_COLUMN_A_OAUTH2APPLICATIONID_3 = "(oAuth2RefreshToken.oAuth2ApplicationId IS NULL OR oAuth2RefreshToken.oAuth2ApplicationId = '')";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_A_U = new FinderPath(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
			OAuth2RefreshTokenModelImpl.FINDER_CACHE_ENABLED,
			OAuth2RefreshTokenImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByA_U",
			new String[] {
				String.class.getName(), String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_U = new FinderPath(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
			OAuth2RefreshTokenModelImpl.FINDER_CACHE_ENABLED,
			OAuth2RefreshTokenImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByA_U",
			new String[] { String.class.getName(), String.class.getName() },
			OAuth2RefreshTokenModelImpl.OAUTH2APPLICATIONID_COLUMN_BITMASK |
			OAuth2RefreshTokenModelImpl.USERNAME_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_A_U = new FinderPath(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
			OAuth2RefreshTokenModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByA_U",
			new String[] { String.class.getName(), String.class.getName() });

	/**
	 * Returns all the o auth2 refresh tokens where oAuth2ApplicationId = &#63; and userName = &#63;.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param userName the user name
	 * @return the matching o auth2 refresh tokens
	 */
	@Override
	public List<OAuth2RefreshToken> findByA_U(String oAuth2ApplicationId,
		String userName) {
		return findByA_U(oAuth2ApplicationId, userName, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the o auth2 refresh tokens where oAuth2ApplicationId = &#63; and userName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param userName the user name
	 * @param start the lower bound of the range of o auth2 refresh tokens
	 * @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	 * @return the range of matching o auth2 refresh tokens
	 */
	@Override
	public List<OAuth2RefreshToken> findByA_U(String oAuth2ApplicationId,
		String userName, int start, int end) {
		return findByA_U(oAuth2ApplicationId, userName, start, end, null);
	}

	/**
	 * Returns an ordered range of all the o auth2 refresh tokens where oAuth2ApplicationId = &#63; and userName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param userName the user name
	 * @param start the lower bound of the range of o auth2 refresh tokens
	 * @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching o auth2 refresh tokens
	 */
	@Override
	public List<OAuth2RefreshToken> findByA_U(String oAuth2ApplicationId,
		String userName, int start, int end,
		OrderByComparator<OAuth2RefreshToken> orderByComparator) {
		return findByA_U(oAuth2ApplicationId, userName, start, end,
			orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the o auth2 refresh tokens where oAuth2ApplicationId = &#63; and userName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param userName the user name
	 * @param start the lower bound of the range of o auth2 refresh tokens
	 * @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching o auth2 refresh tokens
	 */
	@Override
	public List<OAuth2RefreshToken> findByA_U(String oAuth2ApplicationId,
		String userName, int start, int end,
		OrderByComparator<OAuth2RefreshToken> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_U;
			finderArgs = new Object[] { oAuth2ApplicationId, userName };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_A_U;
			finderArgs = new Object[] {
					oAuth2ApplicationId, userName,
					
					start, end, orderByComparator
				};
		}

		List<OAuth2RefreshToken> list = null;

		if (retrieveFromCache) {
			list = (List<OAuth2RefreshToken>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (OAuth2RefreshToken oAuth2RefreshToken : list) {
					if (!Objects.equals(oAuth2ApplicationId,
								oAuth2RefreshToken.getOAuth2ApplicationId()) ||
							!Objects.equals(userName,
								oAuth2RefreshToken.getUserName())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_OAUTH2REFRESHTOKEN_WHERE);

			boolean bindOAuth2ApplicationId = false;

			if (oAuth2ApplicationId == null) {
				query.append(_FINDER_COLUMN_A_U_OAUTH2APPLICATIONID_1);
			}
			else if (oAuth2ApplicationId.equals("")) {
				query.append(_FINDER_COLUMN_A_U_OAUTH2APPLICATIONID_3);
			}
			else {
				bindOAuth2ApplicationId = true;

				query.append(_FINDER_COLUMN_A_U_OAUTH2APPLICATIONID_2);
			}

			boolean bindUserName = false;

			if (userName == null) {
				query.append(_FINDER_COLUMN_A_U_USERNAME_1);
			}
			else if (userName.equals("")) {
				query.append(_FINDER_COLUMN_A_U_USERNAME_3);
			}
			else {
				bindUserName = true;

				query.append(_FINDER_COLUMN_A_U_USERNAME_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(OAuth2RefreshTokenModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindOAuth2ApplicationId) {
					qPos.add(oAuth2ApplicationId);
				}

				if (bindUserName) {
					qPos.add(userName);
				}

				if (!pagination) {
					list = (List<OAuth2RefreshToken>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<OAuth2RefreshToken>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63; and userName = &#63;.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param userName the user name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching o auth2 refresh token
	 * @throws NoSuchOAuth2RefreshTokenException if a matching o auth2 refresh token could not be found
	 */
	@Override
	public OAuth2RefreshToken findByA_U_First(String oAuth2ApplicationId,
		String userName, OrderByComparator<OAuth2RefreshToken> orderByComparator)
		throws NoSuchOAuth2RefreshTokenException {
		OAuth2RefreshToken oAuth2RefreshToken = fetchByA_U_First(oAuth2ApplicationId,
				userName, orderByComparator);

		if (oAuth2RefreshToken != null) {
			return oAuth2RefreshToken;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("oAuth2ApplicationId=");
		msg.append(oAuth2ApplicationId);

		msg.append(", userName=");
		msg.append(userName);

		msg.append("}");

		throw new NoSuchOAuth2RefreshTokenException(msg.toString());
	}

	/**
	 * Returns the first o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63; and userName = &#63;.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param userName the user name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching o auth2 refresh token, or <code>null</code> if a matching o auth2 refresh token could not be found
	 */
	@Override
	public OAuth2RefreshToken fetchByA_U_First(String oAuth2ApplicationId,
		String userName, OrderByComparator<OAuth2RefreshToken> orderByComparator) {
		List<OAuth2RefreshToken> list = findByA_U(oAuth2ApplicationId,
				userName, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63; and userName = &#63;.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param userName the user name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching o auth2 refresh token
	 * @throws NoSuchOAuth2RefreshTokenException if a matching o auth2 refresh token could not be found
	 */
	@Override
	public OAuth2RefreshToken findByA_U_Last(String oAuth2ApplicationId,
		String userName, OrderByComparator<OAuth2RefreshToken> orderByComparator)
		throws NoSuchOAuth2RefreshTokenException {
		OAuth2RefreshToken oAuth2RefreshToken = fetchByA_U_Last(oAuth2ApplicationId,
				userName, orderByComparator);

		if (oAuth2RefreshToken != null) {
			return oAuth2RefreshToken;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("oAuth2ApplicationId=");
		msg.append(oAuth2ApplicationId);

		msg.append(", userName=");
		msg.append(userName);

		msg.append("}");

		throw new NoSuchOAuth2RefreshTokenException(msg.toString());
	}

	/**
	 * Returns the last o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63; and userName = &#63;.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param userName the user name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching o auth2 refresh token, or <code>null</code> if a matching o auth2 refresh token could not be found
	 */
	@Override
	public OAuth2RefreshToken fetchByA_U_Last(String oAuth2ApplicationId,
		String userName, OrderByComparator<OAuth2RefreshToken> orderByComparator) {
		int count = countByA_U(oAuth2ApplicationId, userName);

		if (count == 0) {
			return null;
		}

		List<OAuth2RefreshToken> list = findByA_U(oAuth2ApplicationId,
				userName, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the o auth2 refresh tokens before and after the current o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63; and userName = &#63;.
	 *
	 * @param oAuth2RefreshTokenId the primary key of the current o auth2 refresh token
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param userName the user name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next o auth2 refresh token
	 * @throws NoSuchOAuth2RefreshTokenException if a o auth2 refresh token with the primary key could not be found
	 */
	@Override
	public OAuth2RefreshToken[] findByA_U_PrevAndNext(
		String oAuth2RefreshTokenId, String oAuth2ApplicationId,
		String userName, OrderByComparator<OAuth2RefreshToken> orderByComparator)
		throws NoSuchOAuth2RefreshTokenException {
		OAuth2RefreshToken oAuth2RefreshToken = findByPrimaryKey(oAuth2RefreshTokenId);

		Session session = null;

		try {
			session = openSession();

			OAuth2RefreshToken[] array = new OAuth2RefreshTokenImpl[3];

			array[0] = getByA_U_PrevAndNext(session, oAuth2RefreshToken,
					oAuth2ApplicationId, userName, orderByComparator, true);

			array[1] = oAuth2RefreshToken;

			array[2] = getByA_U_PrevAndNext(session, oAuth2RefreshToken,
					oAuth2ApplicationId, userName, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected OAuth2RefreshToken getByA_U_PrevAndNext(Session session,
		OAuth2RefreshToken oAuth2RefreshToken, String oAuth2ApplicationId,
		String userName,
		OrderByComparator<OAuth2RefreshToken> orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(5 +
					(orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(4);
		}

		query.append(_SQL_SELECT_OAUTH2REFRESHTOKEN_WHERE);

		boolean bindOAuth2ApplicationId = false;

		if (oAuth2ApplicationId == null) {
			query.append(_FINDER_COLUMN_A_U_OAUTH2APPLICATIONID_1);
		}
		else if (oAuth2ApplicationId.equals("")) {
			query.append(_FINDER_COLUMN_A_U_OAUTH2APPLICATIONID_3);
		}
		else {
			bindOAuth2ApplicationId = true;

			query.append(_FINDER_COLUMN_A_U_OAUTH2APPLICATIONID_2);
		}

		boolean bindUserName = false;

		if (userName == null) {
			query.append(_FINDER_COLUMN_A_U_USERNAME_1);
		}
		else if (userName.equals("")) {
			query.append(_FINDER_COLUMN_A_U_USERNAME_3);
		}
		else {
			bindUserName = true;

			query.append(_FINDER_COLUMN_A_U_USERNAME_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(OAuth2RefreshTokenModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindOAuth2ApplicationId) {
			qPos.add(oAuth2ApplicationId);
		}

		if (bindUserName) {
			qPos.add(userName);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(oAuth2RefreshToken);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<OAuth2RefreshToken> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the o auth2 refresh tokens where oAuth2ApplicationId = &#63; and userName = &#63; from the database.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param userName the user name
	 */
	@Override
	public void removeByA_U(String oAuth2ApplicationId, String userName) {
		for (OAuth2RefreshToken oAuth2RefreshToken : findByA_U(
				oAuth2ApplicationId, userName, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null)) {
			remove(oAuth2RefreshToken);
		}
	}

	/**
	 * Returns the number of o auth2 refresh tokens where oAuth2ApplicationId = &#63; and userName = &#63;.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID
	 * @param userName the user name
	 * @return the number of matching o auth2 refresh tokens
	 */
	@Override
	public int countByA_U(String oAuth2ApplicationId, String userName) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_A_U;

		Object[] finderArgs = new Object[] { oAuth2ApplicationId, userName };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_OAUTH2REFRESHTOKEN_WHERE);

			boolean bindOAuth2ApplicationId = false;

			if (oAuth2ApplicationId == null) {
				query.append(_FINDER_COLUMN_A_U_OAUTH2APPLICATIONID_1);
			}
			else if (oAuth2ApplicationId.equals("")) {
				query.append(_FINDER_COLUMN_A_U_OAUTH2APPLICATIONID_3);
			}
			else {
				bindOAuth2ApplicationId = true;

				query.append(_FINDER_COLUMN_A_U_OAUTH2APPLICATIONID_2);
			}

			boolean bindUserName = false;

			if (userName == null) {
				query.append(_FINDER_COLUMN_A_U_USERNAME_1);
			}
			else if (userName.equals("")) {
				query.append(_FINDER_COLUMN_A_U_USERNAME_3);
			}
			else {
				bindUserName = true;

				query.append(_FINDER_COLUMN_A_U_USERNAME_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindOAuth2ApplicationId) {
					qPos.add(oAuth2ApplicationId);
				}

				if (bindUserName) {
					qPos.add(userName);
				}

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_A_U_OAUTH2APPLICATIONID_1 = "oAuth2RefreshToken.oAuth2ApplicationId IS NULL AND ";
	private static final String _FINDER_COLUMN_A_U_OAUTH2APPLICATIONID_2 = "oAuth2RefreshToken.oAuth2ApplicationId = ? AND ";
	private static final String _FINDER_COLUMN_A_U_OAUTH2APPLICATIONID_3 = "(oAuth2RefreshToken.oAuth2ApplicationId IS NULL OR oAuth2RefreshToken.oAuth2ApplicationId = '') AND ";
	private static final String _FINDER_COLUMN_A_U_USERNAME_1 = "oAuth2RefreshToken.userName IS NULL";
	private static final String _FINDER_COLUMN_A_U_USERNAME_2 = "oAuth2RefreshToken.userName = ?";
	private static final String _FINDER_COLUMN_A_U_USERNAME_3 = "(oAuth2RefreshToken.userName IS NULL OR oAuth2RefreshToken.userName = '')";

	public OAuth2RefreshTokenPersistenceImpl() {
		setModelClass(OAuth2RefreshToken.class);
	}

	/**
	 * Caches the o auth2 refresh token in the entity cache if it is enabled.
	 *
	 * @param oAuth2RefreshToken the o auth2 refresh token
	 */
	@Override
	public void cacheResult(OAuth2RefreshToken oAuth2RefreshToken) {
		entityCache.putResult(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
			OAuth2RefreshTokenImpl.class, oAuth2RefreshToken.getPrimaryKey(),
			oAuth2RefreshToken);

		oAuth2RefreshToken.resetOriginalValues();
	}

	/**
	 * Caches the o auth2 refresh tokens in the entity cache if it is enabled.
	 *
	 * @param oAuth2RefreshTokens the o auth2 refresh tokens
	 */
	@Override
	public void cacheResult(List<OAuth2RefreshToken> oAuth2RefreshTokens) {
		for (OAuth2RefreshToken oAuth2RefreshToken : oAuth2RefreshTokens) {
			if (entityCache.getResult(
						OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
						OAuth2RefreshTokenImpl.class,
						oAuth2RefreshToken.getPrimaryKey()) == null) {
				cacheResult(oAuth2RefreshToken);
			}
			else {
				oAuth2RefreshToken.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all o auth2 refresh tokens.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(OAuth2RefreshTokenImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the o auth2 refresh token.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(OAuth2RefreshToken oAuth2RefreshToken) {
		entityCache.removeResult(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
			OAuth2RefreshTokenImpl.class, oAuth2RefreshToken.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<OAuth2RefreshToken> oAuth2RefreshTokens) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (OAuth2RefreshToken oAuth2RefreshToken : oAuth2RefreshTokens) {
			entityCache.removeResult(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
				OAuth2RefreshTokenImpl.class, oAuth2RefreshToken.getPrimaryKey());
		}
	}

	/**
	 * Creates a new o auth2 refresh token with the primary key. Does not add the o auth2 refresh token to the database.
	 *
	 * @param oAuth2RefreshTokenId the primary key for the new o auth2 refresh token
	 * @return the new o auth2 refresh token
	 */
	@Override
	public OAuth2RefreshToken create(String oAuth2RefreshTokenId) {
		OAuth2RefreshToken oAuth2RefreshToken = new OAuth2RefreshTokenImpl();

		oAuth2RefreshToken.setNew(true);
		oAuth2RefreshToken.setPrimaryKey(oAuth2RefreshTokenId);

		oAuth2RefreshToken.setCompanyId(companyProvider.getCompanyId());

		return oAuth2RefreshToken;
	}

	/**
	 * Removes the o auth2 refresh token with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param oAuth2RefreshTokenId the primary key of the o auth2 refresh token
	 * @return the o auth2 refresh token that was removed
	 * @throws NoSuchOAuth2RefreshTokenException if a o auth2 refresh token with the primary key could not be found
	 */
	@Override
	public OAuth2RefreshToken remove(String oAuth2RefreshTokenId)
		throws NoSuchOAuth2RefreshTokenException {
		return remove((Serializable)oAuth2RefreshTokenId);
	}

	/**
	 * Removes the o auth2 refresh token with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the o auth2 refresh token
	 * @return the o auth2 refresh token that was removed
	 * @throws NoSuchOAuth2RefreshTokenException if a o auth2 refresh token with the primary key could not be found
	 */
	@Override
	public OAuth2RefreshToken remove(Serializable primaryKey)
		throws NoSuchOAuth2RefreshTokenException {
		Session session = null;

		try {
			session = openSession();

			OAuth2RefreshToken oAuth2RefreshToken = (OAuth2RefreshToken)session.get(OAuth2RefreshTokenImpl.class,
					primaryKey);

			if (oAuth2RefreshToken == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchOAuth2RefreshTokenException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(oAuth2RefreshToken);
		}
		catch (NoSuchOAuth2RefreshTokenException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected OAuth2RefreshToken removeImpl(
		OAuth2RefreshToken oAuth2RefreshToken) {
		oAuth2RefreshToken = toUnwrappedModel(oAuth2RefreshToken);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(oAuth2RefreshToken)) {
				oAuth2RefreshToken = (OAuth2RefreshToken)session.get(OAuth2RefreshTokenImpl.class,
						oAuth2RefreshToken.getPrimaryKeyObj());
			}

			if (oAuth2RefreshToken != null) {
				session.delete(oAuth2RefreshToken);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (oAuth2RefreshToken != null) {
			clearCache(oAuth2RefreshToken);
		}

		return oAuth2RefreshToken;
	}

	@Override
	public OAuth2RefreshToken updateImpl(OAuth2RefreshToken oAuth2RefreshToken) {
		oAuth2RefreshToken = toUnwrappedModel(oAuth2RefreshToken);

		boolean isNew = oAuth2RefreshToken.isNew();

		OAuth2RefreshTokenModelImpl oAuth2RefreshTokenModelImpl = (OAuth2RefreshTokenModelImpl)oAuth2RefreshToken;

		Session session = null;

		try {
			session = openSession();

			if (oAuth2RefreshToken.isNew()) {
				session.save(oAuth2RefreshToken);

				oAuth2RefreshToken.setNew(false);
			}
			else {
				oAuth2RefreshToken = (OAuth2RefreshToken)session.merge(oAuth2RefreshToken);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (!OAuth2RefreshTokenModelImpl.COLUMN_BITMASK_ENABLED) {
			finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}
		else
		 if (isNew) {
			Object[] args = new Object[] {
					oAuth2RefreshTokenModelImpl.getOAuth2ApplicationId()
				};

			finderCache.removeResult(FINDER_PATH_COUNT_BY_A, args);
			finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A,
				args);

			args = new Object[] {
					oAuth2RefreshTokenModelImpl.getOAuth2ApplicationId(),
					oAuth2RefreshTokenModelImpl.getUserName()
				};

			finderCache.removeResult(FINDER_PATH_COUNT_BY_A_U, args);
			finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_U,
				args);

			finderCache.removeResult(FINDER_PATH_COUNT_ALL, FINDER_ARGS_EMPTY);
			finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL,
				FINDER_ARGS_EMPTY);
		}

		else {
			if ((oAuth2RefreshTokenModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						oAuth2RefreshTokenModelImpl.getOriginalOAuth2ApplicationId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_A, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A,
					args);

				args = new Object[] {
						oAuth2RefreshTokenModelImpl.getOAuth2ApplicationId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_A, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A,
					args);
			}

			if ((oAuth2RefreshTokenModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_U.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						oAuth2RefreshTokenModelImpl.getOriginalOAuth2ApplicationId(),
						oAuth2RefreshTokenModelImpl.getOriginalUserName()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_A_U, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_U,
					args);

				args = new Object[] {
						oAuth2RefreshTokenModelImpl.getOAuth2ApplicationId(),
						oAuth2RefreshTokenModelImpl.getUserName()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_A_U, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_A_U,
					args);
			}
		}

		entityCache.putResult(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
			OAuth2RefreshTokenImpl.class, oAuth2RefreshToken.getPrimaryKey(),
			oAuth2RefreshToken, false);

		oAuth2RefreshToken.resetOriginalValues();

		return oAuth2RefreshToken;
	}

	protected OAuth2RefreshToken toUnwrappedModel(
		OAuth2RefreshToken oAuth2RefreshToken) {
		if (oAuth2RefreshToken instanceof OAuth2RefreshTokenImpl) {
			return oAuth2RefreshToken;
		}

		OAuth2RefreshTokenImpl oAuth2RefreshTokenImpl = new OAuth2RefreshTokenImpl();

		oAuth2RefreshTokenImpl.setNew(oAuth2RefreshToken.isNew());
		oAuth2RefreshTokenImpl.setPrimaryKey(oAuth2RefreshToken.getPrimaryKey());

		oAuth2RefreshTokenImpl.setOAuth2RefreshTokenId(oAuth2RefreshToken.getOAuth2RefreshTokenId());
		oAuth2RefreshTokenImpl.setCompanyId(oAuth2RefreshToken.getCompanyId());
		oAuth2RefreshTokenImpl.setUserId(oAuth2RefreshToken.getUserId());
		oAuth2RefreshTokenImpl.setUserName(oAuth2RefreshToken.getUserName());
		oAuth2RefreshTokenImpl.setCreateDate(oAuth2RefreshToken.getCreateDate());
		oAuth2RefreshTokenImpl.setLifeTime(oAuth2RefreshToken.getLifeTime());
		oAuth2RefreshTokenImpl.setOAuth2ApplicationId(oAuth2RefreshToken.getOAuth2ApplicationId());

		return oAuth2RefreshTokenImpl;
	}

	/**
	 * Returns the o auth2 refresh token with the primary key or throws a {@link com.liferay.portal.kernel.exception.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the o auth2 refresh token
	 * @return the o auth2 refresh token
	 * @throws NoSuchOAuth2RefreshTokenException if a o auth2 refresh token with the primary key could not be found
	 */
	@Override
	public OAuth2RefreshToken findByPrimaryKey(Serializable primaryKey)
		throws NoSuchOAuth2RefreshTokenException {
		OAuth2RefreshToken oAuth2RefreshToken = fetchByPrimaryKey(primaryKey);

		if (oAuth2RefreshToken == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchOAuth2RefreshTokenException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return oAuth2RefreshToken;
	}

	/**
	 * Returns the o auth2 refresh token with the primary key or throws a {@link NoSuchOAuth2RefreshTokenException} if it could not be found.
	 *
	 * @param oAuth2RefreshTokenId the primary key of the o auth2 refresh token
	 * @return the o auth2 refresh token
	 * @throws NoSuchOAuth2RefreshTokenException if a o auth2 refresh token with the primary key could not be found
	 */
	@Override
	public OAuth2RefreshToken findByPrimaryKey(String oAuth2RefreshTokenId)
		throws NoSuchOAuth2RefreshTokenException {
		return findByPrimaryKey((Serializable)oAuth2RefreshTokenId);
	}

	/**
	 * Returns the o auth2 refresh token with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the o auth2 refresh token
	 * @return the o auth2 refresh token, or <code>null</code> if a o auth2 refresh token with the primary key could not be found
	 */
	@Override
	public OAuth2RefreshToken fetchByPrimaryKey(Serializable primaryKey) {
		Serializable serializable = entityCache.getResult(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
				OAuth2RefreshTokenImpl.class, primaryKey);

		if (serializable == nullModel) {
			return null;
		}

		OAuth2RefreshToken oAuth2RefreshToken = (OAuth2RefreshToken)serializable;

		if (oAuth2RefreshToken == null) {
			Session session = null;

			try {
				session = openSession();

				oAuth2RefreshToken = (OAuth2RefreshToken)session.get(OAuth2RefreshTokenImpl.class,
						primaryKey);

				if (oAuth2RefreshToken != null) {
					cacheResult(oAuth2RefreshToken);
				}
				else {
					entityCache.putResult(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
						OAuth2RefreshTokenImpl.class, primaryKey, nullModel);
				}
			}
			catch (Exception e) {
				entityCache.removeResult(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
					OAuth2RefreshTokenImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return oAuth2RefreshToken;
	}

	/**
	 * Returns the o auth2 refresh token with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param oAuth2RefreshTokenId the primary key of the o auth2 refresh token
	 * @return the o auth2 refresh token, or <code>null</code> if a o auth2 refresh token with the primary key could not be found
	 */
	@Override
	public OAuth2RefreshToken fetchByPrimaryKey(String oAuth2RefreshTokenId) {
		return fetchByPrimaryKey((Serializable)oAuth2RefreshTokenId);
	}

	@Override
	public Map<Serializable, OAuth2RefreshToken> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, OAuth2RefreshToken> map = new HashMap<Serializable, OAuth2RefreshToken>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			OAuth2RefreshToken oAuth2RefreshToken = fetchByPrimaryKey(primaryKey);

			if (oAuth2RefreshToken != null) {
				map.put(primaryKey, oAuth2RefreshToken);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			Serializable serializable = entityCache.getResult(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
					OAuth2RefreshTokenImpl.class, primaryKey);

			if (serializable != nullModel) {
				if (serializable == null) {
					if (uncachedPrimaryKeys == null) {
						uncachedPrimaryKeys = new HashSet<Serializable>();
					}

					uncachedPrimaryKeys.add(primaryKey);
				}
				else {
					map.put(primaryKey, (OAuth2RefreshToken)serializable);
				}
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_OAUTH2REFRESHTOKEN_WHERE_PKS_IN);

		for (int i = 0; i < uncachedPrimaryKeys.size(); i++) {
			query.append("?");

			query.append(",");
		}

		query.setIndex(query.index() - 1);

		query.append(")");

		String sql = query.toString();

		Session session = null;

		try {
			session = openSession();

			Query q = session.createQuery(sql);

			QueryPos qPos = QueryPos.getInstance(q);

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				qPos.add((String)primaryKey);
			}

			for (OAuth2RefreshToken oAuth2RefreshToken : (List<OAuth2RefreshToken>)q.list()) {
				map.put(oAuth2RefreshToken.getPrimaryKeyObj(),
					oAuth2RefreshToken);

				cacheResult(oAuth2RefreshToken);

				uncachedPrimaryKeys.remove(oAuth2RefreshToken.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				entityCache.putResult(OAuth2RefreshTokenModelImpl.ENTITY_CACHE_ENABLED,
					OAuth2RefreshTokenImpl.class, primaryKey, nullModel);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the o auth2 refresh tokens.
	 *
	 * @return the o auth2 refresh tokens
	 */
	@Override
	public List<OAuth2RefreshToken> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the o auth2 refresh tokens.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of o auth2 refresh tokens
	 * @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	 * @return the range of o auth2 refresh tokens
	 */
	@Override
	public List<OAuth2RefreshToken> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the o auth2 refresh tokens.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of o auth2 refresh tokens
	 * @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of o auth2 refresh tokens
	 */
	@Override
	public List<OAuth2RefreshToken> findAll(int start, int end,
		OrderByComparator<OAuth2RefreshToken> orderByComparator) {
		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the o auth2 refresh tokens.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of o auth2 refresh tokens
	 * @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of o auth2 refresh tokens
	 */
	@Override
	public List<OAuth2RefreshToken> findAll(int start, int end,
		OrderByComparator<OAuth2RefreshToken> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<OAuth2RefreshToken> list = null;

		if (retrieveFromCache) {
			list = (List<OAuth2RefreshToken>)finderCache.getResult(finderPath,
					finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_OAUTH2REFRESHTOKEN);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_OAUTH2REFRESHTOKEN;

				if (pagination) {
					sql = sql.concat(OAuth2RefreshTokenModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<OAuth2RefreshToken>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<OAuth2RefreshToken>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the o auth2 refresh tokens from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (OAuth2RefreshToken oAuth2RefreshToken : findAll()) {
			remove(oAuth2RefreshToken);
		}
	}

	/**
	 * Returns the number of o auth2 refresh tokens.
	 *
	 * @return the number of o auth2 refresh tokens
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_OAUTH2REFRESHTOKEN);

				count = (Long)q.uniqueResult();

				finderCache.putResult(FINDER_PATH_COUNT_ALL, FINDER_ARGS_EMPTY,
					count);
			}
			catch (Exception e) {
				finderCache.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return OAuth2RefreshTokenModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the o auth2 refresh token persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		entityCache.removeCache(OAuth2RefreshTokenImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@ServiceReference(type = CompanyProviderWrapper.class)
	protected CompanyProvider companyProvider;
	@ServiceReference(type = EntityCache.class)
	protected EntityCache entityCache;
	@ServiceReference(type = FinderCache.class)
	protected FinderCache finderCache;
	private static final String _SQL_SELECT_OAUTH2REFRESHTOKEN = "SELECT oAuth2RefreshToken FROM OAuth2RefreshToken oAuth2RefreshToken";
	private static final String _SQL_SELECT_OAUTH2REFRESHTOKEN_WHERE_PKS_IN = "SELECT oAuth2RefreshToken FROM OAuth2RefreshToken oAuth2RefreshToken WHERE oAuth2RefreshTokenId IN (";
	private static final String _SQL_SELECT_OAUTH2REFRESHTOKEN_WHERE = "SELECT oAuth2RefreshToken FROM OAuth2RefreshToken oAuth2RefreshToken WHERE ";
	private static final String _SQL_COUNT_OAUTH2REFRESHTOKEN = "SELECT COUNT(oAuth2RefreshToken) FROM OAuth2RefreshToken oAuth2RefreshToken";
	private static final String _SQL_COUNT_OAUTH2REFRESHTOKEN_WHERE = "SELECT COUNT(oAuth2RefreshToken) FROM OAuth2RefreshToken oAuth2RefreshToken WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "oAuth2RefreshToken.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No OAuth2RefreshToken exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No OAuth2RefreshToken exists with the key {";
	private static final Log _log = LogFactoryUtil.getLog(OAuth2RefreshTokenPersistenceImpl.class);
}