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

package com.liferay.portal.security.password.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.security.password.exception.NoSuchHashProviderException;
import com.liferay.portal.security.password.model.PasswordHashProvider;
import com.liferay.portal.security.password.model.impl.PasswordHashProviderImpl;
import com.liferay.portal.security.password.model.impl.PasswordHashProviderModelImpl;
import com.liferay.portal.security.password.service.persistence.PasswordHashProviderPersistence;
import com.liferay.portal.security.password.service.persistence.impl.constants.PasswordPersistenceConstants;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the password hash provider service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Arthur Chan
 * @generated
 */
@Component(service = PasswordHashProviderPersistence.class)
public class PasswordHashProviderPersistenceImpl
	extends BasePersistenceImpl<PasswordHashProvider>
	implements PasswordHashProviderPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>PasswordHashProviderUtil</code> to access the password hash provider persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		PasswordHashProviderImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByUuid;
	private FinderPath _finderPathWithoutPaginationFindByUuid;
	private FinderPath _finderPathCountByUuid;

	/**
	 * Returns all the password hash providers where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the password hash providers where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @return the range of matching password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findByUuid(
		String uuid, int start, int end) {

		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the password hash providers where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the password hash providers where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid;
				finderArgs = new Object[] {uuid};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid;
			finderArgs = new Object[] {uuid, start, end, orderByComparator};
		}

		List<PasswordHashProvider> list = null;

		if (useFinderCache) {
			list = (List<PasswordHashProvider>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (PasswordHashProvider passwordHashProvider : list) {
					if (!uuid.equals(passwordHashProvider.getUuid())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_PASSWORDHASHPROVIDER_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(PasswordHashProviderModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				list = (List<PasswordHashProvider>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first password hash provider in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider findByUuid_First(
			String uuid,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException {

		PasswordHashProvider passwordHashProvider = fetchByUuid_First(
			uuid, orderByComparator);

		if (passwordHashProvider != null) {
			return passwordHashProvider;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchHashProviderException(sb.toString());
	}

	/**
	 * Returns the first password hash provider in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider fetchByUuid_First(
		String uuid,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		List<PasswordHashProvider> list = findByUuid(
			uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last password hash provider in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider findByUuid_Last(
			String uuid,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException {

		PasswordHashProvider passwordHashProvider = fetchByUuid_Last(
			uuid, orderByComparator);

		if (passwordHashProvider != null) {
			return passwordHashProvider;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchHashProviderException(sb.toString());
	}

	/**
	 * Returns the last password hash provider in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider fetchByUuid_Last(
		String uuid,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<PasswordHashProvider> list = findByUuid(
			uuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the password hash providers before and after the current password hash provider in the ordered set where uuid = &#63;.
	 *
	 * @param passwordHashProviderId the primary key of the current password hash provider
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password hash provider
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	@Override
	public PasswordHashProvider[] findByUuid_PrevAndNext(
			long passwordHashProviderId, String uuid,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException {

		uuid = Objects.toString(uuid, "");

		PasswordHashProvider passwordHashProvider = findByPrimaryKey(
			passwordHashProviderId);

		Session session = null;

		try {
			session = openSession();

			PasswordHashProvider[] array = new PasswordHashProviderImpl[3];

			array[0] = getByUuid_PrevAndNext(
				session, passwordHashProvider, uuid, orderByComparator, true);

			array[1] = passwordHashProvider;

			array[2] = getByUuid_PrevAndNext(
				session, passwordHashProvider, uuid, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected PasswordHashProvider getByUuid_PrevAndNext(
		Session session, PasswordHashProvider passwordHashProvider, String uuid,
		OrderByComparator<PasswordHashProvider> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_PASSWORDHASHPROVIDER_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_UUID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(PasswordHashProviderModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						passwordHashProvider)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<PasswordHashProvider> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the password hash providers where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (PasswordHashProvider passwordHashProvider :
				findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(passwordHashProvider);
		}
	}

	/**
	 * Returns the number of password hash providers where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching password hash providers
	 */
	@Override
	public int countByUuid(String uuid) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid;

		Object[] finderArgs = new Object[] {uuid};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_PASSWORDHASHPROVIDER_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_UUID_2 =
		"passwordHashProvider.uuid = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3 =
		"(passwordHashProvider.uuid IS NULL OR passwordHashProvider.uuid = '')";

	private FinderPath _finderPathWithPaginationFindByUuid_C;
	private FinderPath _finderPathWithoutPaginationFindByUuid_C;
	private FinderPath _finderPathCountByUuid_C;

	/**
	 * Returns all the password hash providers where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findByUuid_C(
		String uuid, long companyId) {

		return findByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the password hash providers where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @return the range of matching password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return findByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the password hash providers where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		return findByUuid_C(
			uuid, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the password hash providers where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid_C;
				finderArgs = new Object[] {uuid, companyId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid_C;
			finderArgs = new Object[] {
				uuid, companyId, start, end, orderByComparator
			};
		}

		List<PasswordHashProvider> list = null;

		if (useFinderCache) {
			list = (List<PasswordHashProvider>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (PasswordHashProvider passwordHashProvider : list) {
					if (!uuid.equals(passwordHashProvider.getUuid()) ||
						(companyId != passwordHashProvider.getCompanyId())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_PASSWORDHASHPROVIDER_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(PasswordHashProviderModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				list = (List<PasswordHashProvider>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first password hash provider in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException {

		PasswordHashProvider passwordHashProvider = fetchByUuid_C_First(
			uuid, companyId, orderByComparator);

		if (passwordHashProvider != null) {
			return passwordHashProvider;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchHashProviderException(sb.toString());
	}

	/**
	 * Returns the first password hash provider in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		List<PasswordHashProvider> list = findByUuid_C(
			uuid, companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last password hash provider in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException {

		PasswordHashProvider passwordHashProvider = fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);

		if (passwordHashProvider != null) {
			return passwordHashProvider;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchHashProviderException(sb.toString());
	}

	/**
	 * Returns the last password hash provider in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<PasswordHashProvider> list = findByUuid_C(
			uuid, companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the password hash providers before and after the current password hash provider in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param passwordHashProviderId the primary key of the current password hash provider
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password hash provider
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	@Override
	public PasswordHashProvider[] findByUuid_C_PrevAndNext(
			long passwordHashProviderId, String uuid, long companyId,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException {

		uuid = Objects.toString(uuid, "");

		PasswordHashProvider passwordHashProvider = findByPrimaryKey(
			passwordHashProviderId);

		Session session = null;

		try {
			session = openSession();

			PasswordHashProvider[] array = new PasswordHashProviderImpl[3];

			array[0] = getByUuid_C_PrevAndNext(
				session, passwordHashProvider, uuid, companyId,
				orderByComparator, true);

			array[1] = passwordHashProvider;

			array[2] = getByUuid_C_PrevAndNext(
				session, passwordHashProvider, uuid, companyId,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected PasswordHashProvider getByUuid_C_PrevAndNext(
		Session session, PasswordHashProvider passwordHashProvider, String uuid,
		long companyId,
		OrderByComparator<PasswordHashProvider> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_PASSWORDHASHPROVIDER_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
		}

		sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(PasswordHashProviderModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						passwordHashProvider)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<PasswordHashProvider> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the password hash providers where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (PasswordHashProvider passwordHashProvider :
				findByUuid_C(
					uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(passwordHashProvider);
		}
	}

	/**
	 * Returns the number of password hash providers where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching password hash providers
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid_C;

		Object[] finderArgs = new Object[] {uuid, companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_PASSWORDHASHPROVIDER_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_C_UUID_2 =
		"passwordHashProvider.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3 =
		"(passwordHashProvider.uuid IS NULL OR passwordHashProvider.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 =
		"passwordHashProvider.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByHashProviderName;
	private FinderPath _finderPathWithoutPaginationFindByHashProviderName;
	private FinderPath _finderPathCountByHashProviderName;

	/**
	 * Returns all the password hash providers where hashProviderName = &#63;.
	 *
	 * @param hashProviderName the hash provider name
	 * @return the matching password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findByHashProviderName(
		String hashProviderName) {

		return findByHashProviderName(
			hashProviderName, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the password hash providers where hashProviderName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param hashProviderName the hash provider name
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @return the range of matching password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findByHashProviderName(
		String hashProviderName, int start, int end) {

		return findByHashProviderName(hashProviderName, start, end, null);
	}

	/**
	 * Returns an ordered range of all the password hash providers where hashProviderName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param hashProviderName the hash provider name
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findByHashProviderName(
		String hashProviderName, int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		return findByHashProviderName(
			hashProviderName, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the password hash providers where hashProviderName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param hashProviderName the hash provider name
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findByHashProviderName(
		String hashProviderName, int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator,
		boolean useFinderCache) {

		hashProviderName = Objects.toString(hashProviderName, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByHashProviderName;
				finderArgs = new Object[] {hashProviderName};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByHashProviderName;
			finderArgs = new Object[] {
				hashProviderName, start, end, orderByComparator
			};
		}

		List<PasswordHashProvider> list = null;

		if (useFinderCache) {
			list = (List<PasswordHashProvider>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (PasswordHashProvider passwordHashProvider : list) {
					if (!hashProviderName.equals(
							passwordHashProvider.getHashProviderName())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_PASSWORDHASHPROVIDER_WHERE);

			boolean bindHashProviderName = false;

			if (hashProviderName.isEmpty()) {
				sb.append(_FINDER_COLUMN_HASHPROVIDERNAME_HASHPROVIDERNAME_3);
			}
			else {
				bindHashProviderName = true;

				sb.append(_FINDER_COLUMN_HASHPROVIDERNAME_HASHPROVIDERNAME_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(PasswordHashProviderModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindHashProviderName) {
					queryPos.add(hashProviderName);
				}

				list = (List<PasswordHashProvider>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first password hash provider in the ordered set where hashProviderName = &#63;.
	 *
	 * @param hashProviderName the hash provider name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider findByHashProviderName_First(
			String hashProviderName,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException {

		PasswordHashProvider passwordHashProvider =
			fetchByHashProviderName_First(hashProviderName, orderByComparator);

		if (passwordHashProvider != null) {
			return passwordHashProvider;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("hashProviderName=");
		sb.append(hashProviderName);

		sb.append("}");

		throw new NoSuchHashProviderException(sb.toString());
	}

	/**
	 * Returns the first password hash provider in the ordered set where hashProviderName = &#63;.
	 *
	 * @param hashProviderName the hash provider name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider fetchByHashProviderName_First(
		String hashProviderName,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		List<PasswordHashProvider> list = findByHashProviderName(
			hashProviderName, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last password hash provider in the ordered set where hashProviderName = &#63;.
	 *
	 * @param hashProviderName the hash provider name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider findByHashProviderName_Last(
			String hashProviderName,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException {

		PasswordHashProvider passwordHashProvider =
			fetchByHashProviderName_Last(hashProviderName, orderByComparator);

		if (passwordHashProvider != null) {
			return passwordHashProvider;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("hashProviderName=");
		sb.append(hashProviderName);

		sb.append("}");

		throw new NoSuchHashProviderException(sb.toString());
	}

	/**
	 * Returns the last password hash provider in the ordered set where hashProviderName = &#63;.
	 *
	 * @param hashProviderName the hash provider name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider fetchByHashProviderName_Last(
		String hashProviderName,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		int count = countByHashProviderName(hashProviderName);

		if (count == 0) {
			return null;
		}

		List<PasswordHashProvider> list = findByHashProviderName(
			hashProviderName, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the password hash providers before and after the current password hash provider in the ordered set where hashProviderName = &#63;.
	 *
	 * @param passwordHashProviderId the primary key of the current password hash provider
	 * @param hashProviderName the hash provider name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password hash provider
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	@Override
	public PasswordHashProvider[] findByHashProviderName_PrevAndNext(
			long passwordHashProviderId, String hashProviderName,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException {

		hashProviderName = Objects.toString(hashProviderName, "");

		PasswordHashProvider passwordHashProvider = findByPrimaryKey(
			passwordHashProviderId);

		Session session = null;

		try {
			session = openSession();

			PasswordHashProvider[] array = new PasswordHashProviderImpl[3];

			array[0] = getByHashProviderName_PrevAndNext(
				session, passwordHashProvider, hashProviderName,
				orderByComparator, true);

			array[1] = passwordHashProvider;

			array[2] = getByHashProviderName_PrevAndNext(
				session, passwordHashProvider, hashProviderName,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected PasswordHashProvider getByHashProviderName_PrevAndNext(
		Session session, PasswordHashProvider passwordHashProvider,
		String hashProviderName,
		OrderByComparator<PasswordHashProvider> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_PASSWORDHASHPROVIDER_WHERE);

		boolean bindHashProviderName = false;

		if (hashProviderName.isEmpty()) {
			sb.append(_FINDER_COLUMN_HASHPROVIDERNAME_HASHPROVIDERNAME_3);
		}
		else {
			bindHashProviderName = true;

			sb.append(_FINDER_COLUMN_HASHPROVIDERNAME_HASHPROVIDERNAME_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(PasswordHashProviderModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindHashProviderName) {
			queryPos.add(hashProviderName);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						passwordHashProvider)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<PasswordHashProvider> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the password hash providers where hashProviderName = &#63; from the database.
	 *
	 * @param hashProviderName the hash provider name
	 */
	@Override
	public void removeByHashProviderName(String hashProviderName) {
		for (PasswordHashProvider passwordHashProvider :
				findByHashProviderName(
					hashProviderName, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(passwordHashProvider);
		}
	}

	/**
	 * Returns the number of password hash providers where hashProviderName = &#63;.
	 *
	 * @param hashProviderName the hash provider name
	 * @return the number of matching password hash providers
	 */
	@Override
	public int countByHashProviderName(String hashProviderName) {
		hashProviderName = Objects.toString(hashProviderName, "");

		FinderPath finderPath = _finderPathCountByHashProviderName;

		Object[] finderArgs = new Object[] {hashProviderName};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_PASSWORDHASHPROVIDER_WHERE);

			boolean bindHashProviderName = false;

			if (hashProviderName.isEmpty()) {
				sb.append(_FINDER_COLUMN_HASHPROVIDERNAME_HASHPROVIDERNAME_3);
			}
			else {
				bindHashProviderName = true;

				sb.append(_FINDER_COLUMN_HASHPROVIDERNAME_HASHPROVIDERNAME_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindHashProviderName) {
					queryPos.add(hashProviderName);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String
		_FINDER_COLUMN_HASHPROVIDERNAME_HASHPROVIDERNAME_2 =
			"passwordHashProvider.hashProviderName = ?";

	private static final String
		_FINDER_COLUMN_HASHPROVIDERNAME_HASHPROVIDERNAME_3 =
			"(passwordHashProvider.hashProviderName IS NULL OR passwordHashProvider.hashProviderName = '')";

	private FinderPath _finderPathFetchByN_M;
	private FinderPath _finderPathCountByN_M;

	/**
	 * Returns the password hash provider where hashProviderName = &#63; and hashProviderMeta = &#63; or throws a <code>NoSuchHashProviderException</code> if it could not be found.
	 *
	 * @param hashProviderName the hash provider name
	 * @param hashProviderMeta the hash provider meta
	 * @return the matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider findByN_M(
			String hashProviderName, String hashProviderMeta)
		throws NoSuchHashProviderException {

		PasswordHashProvider passwordHashProvider = fetchByN_M(
			hashProviderName, hashProviderMeta);

		if (passwordHashProvider == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("hashProviderName=");
			sb.append(hashProviderName);

			sb.append(", hashProviderMeta=");
			sb.append(hashProviderMeta);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchHashProviderException(sb.toString());
		}

		return passwordHashProvider;
	}

	/**
	 * Returns the password hash provider where hashProviderName = &#63; and hashProviderMeta = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param hashProviderName the hash provider name
	 * @param hashProviderMeta the hash provider meta
	 * @return the matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider fetchByN_M(
		String hashProviderName, String hashProviderMeta) {

		return fetchByN_M(hashProviderName, hashProviderMeta, true);
	}

	/**
	 * Returns the password hash provider where hashProviderName = &#63; and hashProviderMeta = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param hashProviderName the hash provider name
	 * @param hashProviderMeta the hash provider meta
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider fetchByN_M(
		String hashProviderName, String hashProviderMeta,
		boolean useFinderCache) {

		hashProviderName = Objects.toString(hashProviderName, "");
		hashProviderMeta = Objects.toString(hashProviderMeta, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {hashProviderName, hashProviderMeta};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByN_M, finderArgs, this);
		}

		if (result instanceof PasswordHashProvider) {
			PasswordHashProvider passwordHashProvider =
				(PasswordHashProvider)result;

			if (!Objects.equals(
					hashProviderName,
					passwordHashProvider.getHashProviderName()) ||
				!Objects.equals(
					hashProviderMeta,
					passwordHashProvider.getHashProviderMeta())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_PASSWORDHASHPROVIDER_WHERE);

			boolean bindHashProviderName = false;

			if (hashProviderName.isEmpty()) {
				sb.append(_FINDER_COLUMN_N_M_HASHPROVIDERNAME_3);
			}
			else {
				bindHashProviderName = true;

				sb.append(_FINDER_COLUMN_N_M_HASHPROVIDERNAME_2);
			}

			boolean bindHashProviderMeta = false;

			if (hashProviderMeta.isEmpty()) {
				sb.append(_FINDER_COLUMN_N_M_HASHPROVIDERMETA_3);
			}
			else {
				bindHashProviderMeta = true;

				sb.append(_FINDER_COLUMN_N_M_HASHPROVIDERMETA_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindHashProviderName) {
					queryPos.add(hashProviderName);
				}

				if (bindHashProviderMeta) {
					queryPos.add(hashProviderMeta);
				}

				List<PasswordHashProvider> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByN_M, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {
									hashProviderName, hashProviderMeta
								};
							}

							_log.warn(
								"PasswordHashProviderPersistenceImpl.fetchByN_M(String, String, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					PasswordHashProvider passwordHashProvider = list.get(0);

					result = passwordHashProvider;

					cacheResult(passwordHashProvider);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(_finderPathFetchByN_M, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (PasswordHashProvider)result;
		}
	}

	/**
	 * Removes the password hash provider where hashProviderName = &#63; and hashProviderMeta = &#63; from the database.
	 *
	 * @param hashProviderName the hash provider name
	 * @param hashProviderMeta the hash provider meta
	 * @return the password hash provider that was removed
	 */
	@Override
	public PasswordHashProvider removeByN_M(
			String hashProviderName, String hashProviderMeta)
		throws NoSuchHashProviderException {

		PasswordHashProvider passwordHashProvider = findByN_M(
			hashProviderName, hashProviderMeta);

		return remove(passwordHashProvider);
	}

	/**
	 * Returns the number of password hash providers where hashProviderName = &#63; and hashProviderMeta = &#63;.
	 *
	 * @param hashProviderName the hash provider name
	 * @param hashProviderMeta the hash provider meta
	 * @return the number of matching password hash providers
	 */
	@Override
	public int countByN_M(String hashProviderName, String hashProviderMeta) {
		hashProviderName = Objects.toString(hashProviderName, "");
		hashProviderMeta = Objects.toString(hashProviderMeta, "");

		FinderPath finderPath = _finderPathCountByN_M;

		Object[] finderArgs = new Object[] {hashProviderName, hashProviderMeta};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_PASSWORDHASHPROVIDER_WHERE);

			boolean bindHashProviderName = false;

			if (hashProviderName.isEmpty()) {
				sb.append(_FINDER_COLUMN_N_M_HASHPROVIDERNAME_3);
			}
			else {
				bindHashProviderName = true;

				sb.append(_FINDER_COLUMN_N_M_HASHPROVIDERNAME_2);
			}

			boolean bindHashProviderMeta = false;

			if (hashProviderMeta.isEmpty()) {
				sb.append(_FINDER_COLUMN_N_M_HASHPROVIDERMETA_3);
			}
			else {
				bindHashProviderMeta = true;

				sb.append(_FINDER_COLUMN_N_M_HASHPROVIDERMETA_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindHashProviderName) {
					queryPos.add(hashProviderName);
				}

				if (bindHashProviderMeta) {
					queryPos.add(hashProviderMeta);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_N_M_HASHPROVIDERNAME_2 =
		"passwordHashProvider.hashProviderName = ? AND ";

	private static final String _FINDER_COLUMN_N_M_HASHPROVIDERNAME_3 =
		"(passwordHashProvider.hashProviderName IS NULL OR passwordHashProvider.hashProviderName = '') AND ";

	private static final String _FINDER_COLUMN_N_M_HASHPROVIDERMETA_2 =
		"passwordHashProvider.hashProviderMeta = ?";

	private static final String _FINDER_COLUMN_N_M_HASHPROVIDERMETA_3 =
		"(passwordHashProvider.hashProviderMeta IS NULL OR passwordHashProvider.hashProviderMeta = '')";

	public PasswordHashProviderPersistenceImpl() {
		setModelClass(PasswordHashProvider.class);

		setModelImplClass(PasswordHashProviderImpl.class);
		setModelPKClass(long.class);

		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");

		setDBColumnNames(dbColumnNames);
	}

	/**
	 * Caches the password hash provider in the entity cache if it is enabled.
	 *
	 * @param passwordHashProvider the password hash provider
	 */
	@Override
	public void cacheResult(PasswordHashProvider passwordHashProvider) {
		entityCache.putResult(
			entityCacheEnabled, PasswordHashProviderImpl.class,
			passwordHashProvider.getPrimaryKey(), passwordHashProvider);

		finderCache.putResult(
			_finderPathFetchByN_M,
			new Object[] {
				passwordHashProvider.getHashProviderName(),
				passwordHashProvider.getHashProviderMeta()
			},
			passwordHashProvider);

		passwordHashProvider.resetOriginalValues();
	}

	/**
	 * Caches the password hash providers in the entity cache if it is enabled.
	 *
	 * @param passwordHashProviders the password hash providers
	 */
	@Override
	public void cacheResult(List<PasswordHashProvider> passwordHashProviders) {
		for (PasswordHashProvider passwordHashProvider :
				passwordHashProviders) {

			if (entityCache.getResult(
					entityCacheEnabled, PasswordHashProviderImpl.class,
					passwordHashProvider.getPrimaryKey()) == null) {

				cacheResult(passwordHashProvider);
			}
			else {
				passwordHashProvider.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all password hash providers.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(PasswordHashProviderImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the password hash provider.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(PasswordHashProvider passwordHashProvider) {
		entityCache.removeResult(
			entityCacheEnabled, PasswordHashProviderImpl.class,
			passwordHashProvider.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(
			(PasswordHashProviderModelImpl)passwordHashProvider, true);
	}

	@Override
	public void clearCache(List<PasswordHashProvider> passwordHashProviders) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (PasswordHashProvider passwordHashProvider :
				passwordHashProviders) {

			entityCache.removeResult(
				entityCacheEnabled, PasswordHashProviderImpl.class,
				passwordHashProvider.getPrimaryKey());

			clearUniqueFindersCache(
				(PasswordHashProviderModelImpl)passwordHashProvider, true);
		}
	}

	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				entityCacheEnabled, PasswordHashProviderImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		PasswordHashProviderModelImpl passwordHashProviderModelImpl) {

		Object[] args = new Object[] {
			passwordHashProviderModelImpl.getHashProviderName(),
			passwordHashProviderModelImpl.getHashProviderMeta()
		};

		finderCache.putResult(
			_finderPathCountByN_M, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByN_M, args, passwordHashProviderModelImpl, false);
	}

	protected void clearUniqueFindersCache(
		PasswordHashProviderModelImpl passwordHashProviderModelImpl,
		boolean clearCurrent) {

		if (clearCurrent) {
			Object[] args = new Object[] {
				passwordHashProviderModelImpl.getHashProviderName(),
				passwordHashProviderModelImpl.getHashProviderMeta()
			};

			finderCache.removeResult(_finderPathCountByN_M, args);
			finderCache.removeResult(_finderPathFetchByN_M, args);
		}

		if ((passwordHashProviderModelImpl.getColumnBitmask() &
			 _finderPathFetchByN_M.getColumnBitmask()) != 0) {

			Object[] args = new Object[] {
				passwordHashProviderModelImpl.getOriginalHashProviderName(),
				passwordHashProviderModelImpl.getOriginalHashProviderMeta()
			};

			finderCache.removeResult(_finderPathCountByN_M, args);
			finderCache.removeResult(_finderPathFetchByN_M, args);
		}
	}

	/**
	 * Creates a new password hash provider with the primary key. Does not add the password hash provider to the database.
	 *
	 * @param passwordHashProviderId the primary key for the new password hash provider
	 * @return the new password hash provider
	 */
	@Override
	public PasswordHashProvider create(long passwordHashProviderId) {
		PasswordHashProvider passwordHashProvider =
			new PasswordHashProviderImpl();

		passwordHashProvider.setNew(true);
		passwordHashProvider.setPrimaryKey(passwordHashProviderId);

		String uuid = PortalUUIDUtil.generate();

		passwordHashProvider.setUuid(uuid);

		passwordHashProvider.setCompanyId(CompanyThreadLocal.getCompanyId());

		return passwordHashProvider;
	}

	/**
	 * Removes the password hash provider with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordHashProviderId the primary key of the password hash provider
	 * @return the password hash provider that was removed
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	@Override
	public PasswordHashProvider remove(long passwordHashProviderId)
		throws NoSuchHashProviderException {

		return remove((Serializable)passwordHashProviderId);
	}

	/**
	 * Removes the password hash provider with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the password hash provider
	 * @return the password hash provider that was removed
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	@Override
	public PasswordHashProvider remove(Serializable primaryKey)
		throws NoSuchHashProviderException {

		Session session = null;

		try {
			session = openSession();

			PasswordHashProvider passwordHashProvider =
				(PasswordHashProvider)session.get(
					PasswordHashProviderImpl.class, primaryKey);

			if (passwordHashProvider == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchHashProviderException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(passwordHashProvider);
		}
		catch (NoSuchHashProviderException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected PasswordHashProvider removeImpl(
		PasswordHashProvider passwordHashProvider) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(passwordHashProvider)) {
				passwordHashProvider = (PasswordHashProvider)session.get(
					PasswordHashProviderImpl.class,
					passwordHashProvider.getPrimaryKeyObj());
			}

			if (passwordHashProvider != null) {
				session.delete(passwordHashProvider);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (passwordHashProvider != null) {
			clearCache(passwordHashProvider);
		}

		return passwordHashProvider;
	}

	@Override
	public PasswordHashProvider updateImpl(
		PasswordHashProvider passwordHashProvider) {

		boolean isNew = passwordHashProvider.isNew();

		if (!(passwordHashProvider instanceof PasswordHashProviderModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(passwordHashProvider.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					passwordHashProvider);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in passwordHashProvider proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom PasswordHashProvider implementation " +
					passwordHashProvider.getClass());
		}

		PasswordHashProviderModelImpl passwordHashProviderModelImpl =
			(PasswordHashProviderModelImpl)passwordHashProvider;

		if (Validator.isNull(passwordHashProvider.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			passwordHashProvider.setUuid(uuid);
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (passwordHashProvider.getCreateDate() == null)) {
			if (serviceContext == null) {
				passwordHashProvider.setCreateDate(now);
			}
			else {
				passwordHashProvider.setCreateDate(
					serviceContext.getCreateDate(now));
			}
		}

		if (!passwordHashProviderModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				passwordHashProvider.setModifiedDate(now);
			}
			else {
				passwordHashProvider.setModifiedDate(
					serviceContext.getModifiedDate(now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (passwordHashProvider.isNew()) {
				session.save(passwordHashProvider);

				passwordHashProvider.setNew(false);
			}
			else {
				passwordHashProvider = (PasswordHashProvider)session.merge(
					passwordHashProvider);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (!_columnBitmaskEnabled) {
			finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}
		else if (isNew) {
			Object[] args = new Object[] {
				passwordHashProviderModelImpl.getUuid()
			};

			finderCache.removeResult(_finderPathCountByUuid, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByUuid, args);

			args = new Object[] {
				passwordHashProviderModelImpl.getUuid(),
				passwordHashProviderModelImpl.getCompanyId()
			};

			finderCache.removeResult(_finderPathCountByUuid_C, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByUuid_C, args);

			args = new Object[] {
				passwordHashProviderModelImpl.getHashProviderName()
			};

			finderCache.removeResult(_finderPathCountByHashProviderName, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByHashProviderName, args);

			finderCache.removeResult(_finderPathCountAll, FINDER_ARGS_EMPTY);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindAll, FINDER_ARGS_EMPTY);
		}
		else {
			if ((passwordHashProviderModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByUuid.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					passwordHashProviderModelImpl.getOriginalUuid()
				};

				finderCache.removeResult(_finderPathCountByUuid, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByUuid, args);

				args = new Object[] {passwordHashProviderModelImpl.getUuid()};

				finderCache.removeResult(_finderPathCountByUuid, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByUuid, args);
			}

			if ((passwordHashProviderModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByUuid_C.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					passwordHashProviderModelImpl.getOriginalUuid(),
					passwordHashProviderModelImpl.getOriginalCompanyId()
				};

				finderCache.removeResult(_finderPathCountByUuid_C, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByUuid_C, args);

				args = new Object[] {
					passwordHashProviderModelImpl.getUuid(),
					passwordHashProviderModelImpl.getCompanyId()
				};

				finderCache.removeResult(_finderPathCountByUuid_C, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByUuid_C, args);
			}

			if ((passwordHashProviderModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByHashProviderName.
					 getColumnBitmask()) != 0) {

				Object[] args = new Object[] {
					passwordHashProviderModelImpl.getOriginalHashProviderName()
				};

				finderCache.removeResult(
					_finderPathCountByHashProviderName, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByHashProviderName, args);

				args = new Object[] {
					passwordHashProviderModelImpl.getHashProviderName()
				};

				finderCache.removeResult(
					_finderPathCountByHashProviderName, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByHashProviderName, args);
			}
		}

		entityCache.putResult(
			entityCacheEnabled, PasswordHashProviderImpl.class,
			passwordHashProvider.getPrimaryKey(), passwordHashProvider, false);

		clearUniqueFindersCache(passwordHashProviderModelImpl, false);
		cacheUniqueFindersCache(passwordHashProviderModelImpl);

		passwordHashProvider.resetOriginalValues();

		return passwordHashProvider;
	}

	/**
	 * Returns the password hash provider with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the password hash provider
	 * @return the password hash provider
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	@Override
	public PasswordHashProvider findByPrimaryKey(Serializable primaryKey)
		throws NoSuchHashProviderException {

		PasswordHashProvider passwordHashProvider = fetchByPrimaryKey(
			primaryKey);

		if (passwordHashProvider == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchHashProviderException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return passwordHashProvider;
	}

	/**
	 * Returns the password hash provider with the primary key or throws a <code>NoSuchHashProviderException</code> if it could not be found.
	 *
	 * @param passwordHashProviderId the primary key of the password hash provider
	 * @return the password hash provider
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	@Override
	public PasswordHashProvider findByPrimaryKey(long passwordHashProviderId)
		throws NoSuchHashProviderException {

		return findByPrimaryKey((Serializable)passwordHashProviderId);
	}

	/**
	 * Returns the password hash provider with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param passwordHashProviderId the primary key of the password hash provider
	 * @return the password hash provider, or <code>null</code> if a password hash provider with the primary key could not be found
	 */
	@Override
	public PasswordHashProvider fetchByPrimaryKey(long passwordHashProviderId) {
		return fetchByPrimaryKey((Serializable)passwordHashProviderId);
	}

	/**
	 * Returns all the password hash providers.
	 *
	 * @return the password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the password hash providers.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @return the range of password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the password hash providers.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findAll(
		int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the password hash providers.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of password hash providers
	 */
	@Override
	public List<PasswordHashProvider> findAll(
		int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<PasswordHashProvider> list = null;

		if (useFinderCache) {
			list = (List<PasswordHashProvider>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_PASSWORDHASHPROVIDER);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_PASSWORDHASHPROVIDER;

				sql = sql.concat(PasswordHashProviderModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<PasswordHashProvider>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the password hash providers from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (PasswordHashProvider passwordHashProvider : findAll()) {
			remove(passwordHashProvider);
		}
	}

	/**
	 * Returns the number of password hash providers.
	 *
	 * @return the number of password hash providers
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(
					_SQL_COUNT_PASSWORDHASHPROVIDER);

				count = (Long)query.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "passwordHashProviderId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_PASSWORDHASHPROVIDER;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return PasswordHashProviderModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the password hash provider persistence.
	 */
	@Activate
	public void activate() {
		PasswordHashProviderModelImpl.setEntityCacheEnabled(entityCacheEnabled);
		PasswordHashProviderModelImpl.setFinderCacheEnabled(finderCacheEnabled);

		_finderPathWithPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			PasswordHashProviderImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			PasswordHashProviderImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll",
			new String[0]);

		_finderPathCountAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0]);

		_finderPathWithPaginationFindByUuid = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			PasswordHashProviderImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByUuid = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			PasswordHashProviderImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
			new String[] {String.class.getName()},
			PasswordHashProviderModelImpl.UUID_COLUMN_BITMASK);

		_finderPathCountByUuid = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
			new String[] {String.class.getName()});

		_finderPathWithPaginationFindByUuid_C = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			PasswordHashProviderImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid_C",
			new String[] {
				String.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByUuid_C = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			PasswordHashProviderImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()},
			PasswordHashProviderModelImpl.UUID_COLUMN_BITMASK |
			PasswordHashProviderModelImpl.COMPANYID_COLUMN_BITMASK);

		_finderPathCountByUuid_C = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()});

		_finderPathWithPaginationFindByHashProviderName = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			PasswordHashProviderImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByHashProviderName",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByHashProviderName = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			PasswordHashProviderImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByHashProviderName",
			new String[] {String.class.getName()},
			PasswordHashProviderModelImpl.HASHPROVIDERNAME_COLUMN_BITMASK);

		_finderPathCountByHashProviderName = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByHashProviderName", new String[] {String.class.getName()});

		_finderPathFetchByN_M = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			PasswordHashProviderImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByN_M",
			new String[] {String.class.getName(), String.class.getName()},
			PasswordHashProviderModelImpl.HASHPROVIDERNAME_COLUMN_BITMASK |
			PasswordHashProviderModelImpl.HASHPROVIDERMETA_COLUMN_BITMASK);

		_finderPathCountByN_M = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByN_M",
			new String[] {String.class.getName(), String.class.getName()});
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(PasswordHashProviderImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	@Reference(
		target = PasswordPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
		super.setConfiguration(configuration);

		_columnBitmaskEnabled = GetterUtil.getBoolean(
			configuration.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.security.password.model.PasswordHashProvider"),
			true);
	}

	@Override
	@Reference(
		target = PasswordPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = PasswordPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	private boolean _columnBitmaskEnabled;

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_PASSWORDHASHPROVIDER =
		"SELECT passwordHashProvider FROM PasswordHashProvider passwordHashProvider";

	private static final String _SQL_SELECT_PASSWORDHASHPROVIDER_WHERE =
		"SELECT passwordHashProvider FROM PasswordHashProvider passwordHashProvider WHERE ";

	private static final String _SQL_COUNT_PASSWORDHASHPROVIDER =
		"SELECT COUNT(passwordHashProvider) FROM PasswordHashProvider passwordHashProvider";

	private static final String _SQL_COUNT_PASSWORDHASHPROVIDER_WHERE =
		"SELECT COUNT(passwordHashProvider) FROM PasswordHashProvider passwordHashProvider WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS =
		"passwordHashProvider.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No PasswordHashProvider exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No PasswordHashProvider exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		PasswordHashProviderPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid"});

	static {
		try {
			Class.forName(PasswordPersistenceConstants.class.getName());
		}
		catch (ClassNotFoundException classNotFoundException) {
			throw new ExceptionInInitializerError(classNotFoundException);
		}
	}

}