/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.multi.factor.authentication.email.otp.service.persistence.impl;

import com.liferay.multi.factor.authentication.email.otp.exception.NoSuchEmailOTPException;
import com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP;
import com.liferay.multi.factor.authentication.email.otp.model.impl.MFAEmailOTPImpl;
import com.liferay.multi.factor.authentication.email.otp.model.impl.MFAEmailOTPModelImpl;
import com.liferay.multi.factor.authentication.email.otp.service.persistence.MFAEmailOTPPersistence;
import com.liferay.multi.factor.authentication.email.otp.service.persistence.impl.constants.MFAPersistenceConstants;
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

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the mfa email otp service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Arthur Chan
 * @generated
 */
@Component(service = MFAEmailOTPPersistence.class)
public class MFAEmailOTPPersistenceImpl
	extends BasePersistenceImpl<MFAEmailOTP> implements MFAEmailOTPPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>MFAEmailOTPUtil</code> to access the mfa email otp persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		MFAEmailOTPImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathFetchByUserId;
	private FinderPath _finderPathCountByUserId;

	/**
	 * Returns the mfa email otp where userId = &#63; or throws a <code>NoSuchEmailOTPException</code> if it could not be found.
	 *
	 * @param userId the user ID
	 * @return the matching mfa email otp
	 * @throws NoSuchEmailOTPException if a matching mfa email otp could not be found
	 */
	@Override
	public MFAEmailOTP findByUserId(long userId)
		throws NoSuchEmailOTPException {

		MFAEmailOTP mfaEmailOTP = fetchByUserId(userId);

		if (mfaEmailOTP == null) {
			StringBundler msg = new StringBundler(4);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("userId=");
			msg.append(userId);

			msg.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(msg.toString());
			}

			throw new NoSuchEmailOTPException(msg.toString());
		}

		return mfaEmailOTP;
	}

	/**
	 * Returns the mfa email otp where userId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param userId the user ID
	 * @return the matching mfa email otp, or <code>null</code> if a matching mfa email otp could not be found
	 */
	@Override
	public MFAEmailOTP fetchByUserId(long userId) {
		return fetchByUserId(userId, true);
	}

	/**
	 * Returns the mfa email otp where userId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param userId the user ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching mfa email otp, or <code>null</code> if a matching mfa email otp could not be found
	 */
	@Override
	public MFAEmailOTP fetchByUserId(long userId, boolean useFinderCache) {
		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {userId};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByUserId, finderArgs, this);
		}

		if (result instanceof MFAEmailOTP) {
			MFAEmailOTP mfaEmailOTP = (MFAEmailOTP)result;

			if (userId != mfaEmailOTP.getUserId()) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_SELECT_MFAEMAILOTP_WHERE);

			query.append(_FINDER_COLUMN_USERID_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				List<MFAEmailOTP> list = q.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByUserId, finderArgs, list);
					}
				}
				else {
					MFAEmailOTP mfaEmailOTP = list.get(0);

					result = mfaEmailOTP;

					cacheResult(mfaEmailOTP);
				}
			}
			catch (Exception e) {
				if (useFinderCache) {
					finderCache.removeResult(
						_finderPathFetchByUserId, finderArgs);
				}

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (MFAEmailOTP)result;
		}
	}

	/**
	 * Removes the mfa email otp where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @return the mfa email otp that was removed
	 */
	@Override
	public MFAEmailOTP removeByUserId(long userId)
		throws NoSuchEmailOTPException {

		MFAEmailOTP mfaEmailOTP = findByUserId(userId);

		return remove(mfaEmailOTP);
	}

	/**
	 * Returns the number of mfa email otps where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching mfa email otps
	 */
	@Override
	public int countByUserId(long userId) {
		FinderPath finderPath = _finderPathCountByUserId;

		Object[] finderArgs = new Object[] {userId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_MFAEMAILOTP_WHERE);

			query.append(_FINDER_COLUMN_USERID_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

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

	private static final String _FINDER_COLUMN_USERID_USERID_2 =
		"mfaEmailOTP.userId = ?";

	public MFAEmailOTPPersistenceImpl() {
		setModelClass(MFAEmailOTP.class);

		setModelImplClass(MFAEmailOTPImpl.class);
		setModelPKClass(long.class);
	}

	/**
	 * Caches the mfa email otp in the entity cache if it is enabled.
	 *
	 * @param mfaEmailOTP the mfa email otp
	 */
	@Override
	public void cacheResult(MFAEmailOTP mfaEmailOTP) {
		entityCache.putResult(
			entityCacheEnabled, MFAEmailOTPImpl.class,
			mfaEmailOTP.getPrimaryKey(), mfaEmailOTP);

		finderCache.putResult(
			_finderPathFetchByUserId, new Object[] {mfaEmailOTP.getUserId()},
			mfaEmailOTP);

		mfaEmailOTP.resetOriginalValues();
	}

	/**
	 * Caches the mfa email otps in the entity cache if it is enabled.
	 *
	 * @param mfaEmailOTPs the mfa email otps
	 */
	@Override
	public void cacheResult(List<MFAEmailOTP> mfaEmailOTPs) {
		for (MFAEmailOTP mfaEmailOTP : mfaEmailOTPs) {
			if (entityCache.getResult(
					entityCacheEnabled, MFAEmailOTPImpl.class,
					mfaEmailOTP.getPrimaryKey()) == null) {

				cacheResult(mfaEmailOTP);
			}
			else {
				mfaEmailOTP.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all mfa email otps.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(MFAEmailOTPImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the mfa email otp.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(MFAEmailOTP mfaEmailOTP) {
		entityCache.removeResult(
			entityCacheEnabled, MFAEmailOTPImpl.class,
			mfaEmailOTP.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache((MFAEmailOTPModelImpl)mfaEmailOTP, true);
	}

	@Override
	public void clearCache(List<MFAEmailOTP> mfaEmailOTPs) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (MFAEmailOTP mfaEmailOTP : mfaEmailOTPs) {
			entityCache.removeResult(
				entityCacheEnabled, MFAEmailOTPImpl.class,
				mfaEmailOTP.getPrimaryKey());

			clearUniqueFindersCache((MFAEmailOTPModelImpl)mfaEmailOTP, true);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				entityCacheEnabled, MFAEmailOTPImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		MFAEmailOTPModelImpl mfaEmailOTPModelImpl) {

		Object[] args = new Object[] {mfaEmailOTPModelImpl.getUserId()};

		finderCache.putResult(
			_finderPathCountByUserId, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByUserId, args, mfaEmailOTPModelImpl, false);
	}

	protected void clearUniqueFindersCache(
		MFAEmailOTPModelImpl mfaEmailOTPModelImpl, boolean clearCurrent) {

		if (clearCurrent) {
			Object[] args = new Object[] {mfaEmailOTPModelImpl.getUserId()};

			finderCache.removeResult(_finderPathCountByUserId, args);
			finderCache.removeResult(_finderPathFetchByUserId, args);
		}

		if ((mfaEmailOTPModelImpl.getColumnBitmask() &
			 _finderPathFetchByUserId.getColumnBitmask()) != 0) {

			Object[] args = new Object[] {
				mfaEmailOTPModelImpl.getOriginalUserId()
			};

			finderCache.removeResult(_finderPathCountByUserId, args);
			finderCache.removeResult(_finderPathFetchByUserId, args);
		}
	}

	/**
	 * Creates a new mfa email otp with the primary key. Does not add the mfa email otp to the database.
	 *
	 * @param emailOTPId the primary key for the new mfa email otp
	 * @return the new mfa email otp
	 */
	@Override
	public MFAEmailOTP create(long emailOTPId) {
		MFAEmailOTP mfaEmailOTP = new MFAEmailOTPImpl();

		mfaEmailOTP.setNew(true);
		mfaEmailOTP.setPrimaryKey(emailOTPId);

		mfaEmailOTP.setCompanyId(CompanyThreadLocal.getCompanyId());

		return mfaEmailOTP;
	}

	/**
	 * Removes the mfa email otp with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param emailOTPId the primary key of the mfa email otp
	 * @return the mfa email otp that was removed
	 * @throws NoSuchEmailOTPException if a mfa email otp with the primary key could not be found
	 */
	@Override
	public MFAEmailOTP remove(long emailOTPId) throws NoSuchEmailOTPException {
		return remove((Serializable)emailOTPId);
	}

	/**
	 * Removes the mfa email otp with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the mfa email otp
	 * @return the mfa email otp that was removed
	 * @throws NoSuchEmailOTPException if a mfa email otp with the primary key could not be found
	 */
	@Override
	public MFAEmailOTP remove(Serializable primaryKey)
		throws NoSuchEmailOTPException {

		Session session = null;

		try {
			session = openSession();

			MFAEmailOTP mfaEmailOTP = (MFAEmailOTP)session.get(
				MFAEmailOTPImpl.class, primaryKey);

			if (mfaEmailOTP == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchEmailOTPException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(mfaEmailOTP);
		}
		catch (NoSuchEmailOTPException nsee) {
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
	protected MFAEmailOTP removeImpl(MFAEmailOTP mfaEmailOTP) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(mfaEmailOTP)) {
				mfaEmailOTP = (MFAEmailOTP)session.get(
					MFAEmailOTPImpl.class, mfaEmailOTP.getPrimaryKeyObj());
			}

			if (mfaEmailOTP != null) {
				session.delete(mfaEmailOTP);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (mfaEmailOTP != null) {
			clearCache(mfaEmailOTP);
		}

		return mfaEmailOTP;
	}

	@Override
	public MFAEmailOTP updateImpl(MFAEmailOTP mfaEmailOTP) {
		boolean isNew = mfaEmailOTP.isNew();

		if (!(mfaEmailOTP instanceof MFAEmailOTPModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(mfaEmailOTP.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(mfaEmailOTP);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in mfaEmailOTP proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom MFAEmailOTP implementation " +
					mfaEmailOTP.getClass());
		}

		MFAEmailOTPModelImpl mfaEmailOTPModelImpl =
			(MFAEmailOTPModelImpl)mfaEmailOTP;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (mfaEmailOTP.getCreateDate() == null)) {
			if (serviceContext == null) {
				mfaEmailOTP.setCreateDate(now);
			}
			else {
				mfaEmailOTP.setCreateDate(serviceContext.getCreateDate(now));
			}
		}

		if (!mfaEmailOTPModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				mfaEmailOTP.setModifiedDate(now);
			}
			else {
				mfaEmailOTP.setModifiedDate(
					serviceContext.getModifiedDate(now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (mfaEmailOTP.isNew()) {
				session.save(mfaEmailOTP);

				mfaEmailOTP.setNew(false);
			}
			else {
				mfaEmailOTP = (MFAEmailOTP)session.merge(mfaEmailOTP);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (!_columnBitmaskEnabled) {
			finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}
		else if (isNew) {
			finderCache.removeResult(_finderPathCountAll, FINDER_ARGS_EMPTY);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindAll, FINDER_ARGS_EMPTY);
		}

		entityCache.putResult(
			entityCacheEnabled, MFAEmailOTPImpl.class,
			mfaEmailOTP.getPrimaryKey(), mfaEmailOTP, false);

		clearUniqueFindersCache(mfaEmailOTPModelImpl, false);
		cacheUniqueFindersCache(mfaEmailOTPModelImpl);

		mfaEmailOTP.resetOriginalValues();

		return mfaEmailOTP;
	}

	/**
	 * Returns the mfa email otp with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the mfa email otp
	 * @return the mfa email otp
	 * @throws NoSuchEmailOTPException if a mfa email otp with the primary key could not be found
	 */
	@Override
	public MFAEmailOTP findByPrimaryKey(Serializable primaryKey)
		throws NoSuchEmailOTPException {

		MFAEmailOTP mfaEmailOTP = fetchByPrimaryKey(primaryKey);

		if (mfaEmailOTP == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchEmailOTPException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return mfaEmailOTP;
	}

	/**
	 * Returns the mfa email otp with the primary key or throws a <code>NoSuchEmailOTPException</code> if it could not be found.
	 *
	 * @param emailOTPId the primary key of the mfa email otp
	 * @return the mfa email otp
	 * @throws NoSuchEmailOTPException if a mfa email otp with the primary key could not be found
	 */
	@Override
	public MFAEmailOTP findByPrimaryKey(long emailOTPId)
		throws NoSuchEmailOTPException {

		return findByPrimaryKey((Serializable)emailOTPId);
	}

	/**
	 * Returns the mfa email otp with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param emailOTPId the primary key of the mfa email otp
	 * @return the mfa email otp, or <code>null</code> if a mfa email otp with the primary key could not be found
	 */
	@Override
	public MFAEmailOTP fetchByPrimaryKey(long emailOTPId) {
		return fetchByPrimaryKey((Serializable)emailOTPId);
	}

	/**
	 * Returns all the mfa email otps.
	 *
	 * @return the mfa email otps
	 */
	@Override
	public List<MFAEmailOTP> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the mfa email otps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFAEmailOTPModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa email otps
	 * @param end the upper bound of the range of mfa email otps (not inclusive)
	 * @return the range of mfa email otps
	 */
	@Override
	public List<MFAEmailOTP> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the mfa email otps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFAEmailOTPModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa email otps
	 * @param end the upper bound of the range of mfa email otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of mfa email otps
	 */
	@Override
	public List<MFAEmailOTP> findAll(
		int start, int end, OrderByComparator<MFAEmailOTP> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the mfa email otps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFAEmailOTPModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa email otps
	 * @param end the upper bound of the range of mfa email otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of mfa email otps
	 */
	@Override
	public List<MFAEmailOTP> findAll(
		int start, int end, OrderByComparator<MFAEmailOTP> orderByComparator,
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

		List<MFAEmailOTP> list = null;

		if (useFinderCache) {
			list = (List<MFAEmailOTP>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_MFAEMAILOTP);

				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_MFAEMAILOTP;

				sql = sql.concat(MFAEmailOTPModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				list = (List<MFAEmailOTP>)QueryUtil.list(
					q, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception e) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the mfa email otps from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (MFAEmailOTP mfaEmailOTP : findAll()) {
			remove(mfaEmailOTP);
		}
	}

	/**
	 * Returns the number of mfa email otps.
	 *
	 * @return the number of mfa email otps
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_MFAEMAILOTP);

				count = (Long)q.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception e) {
				finderCache.removeResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "emailOTPId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_MFAEMAILOTP;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return MFAEmailOTPModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the mfa email otp persistence.
	 */
	@Activate
	public void activate() {
		MFAEmailOTPModelImpl.setEntityCacheEnabled(entityCacheEnabled);
		MFAEmailOTPModelImpl.setFinderCacheEnabled(finderCacheEnabled);

		_finderPathWithPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, MFAEmailOTPImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, MFAEmailOTPImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll",
			new String[0]);

		_finderPathCountAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0]);

		_finderPathFetchByUserId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, MFAEmailOTPImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByUserId",
			new String[] {Long.class.getName()},
			MFAEmailOTPModelImpl.USERID_COLUMN_BITMASK);

		_finderPathCountByUserId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUserId",
			new String[] {Long.class.getName()});
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(MFAEmailOTPImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	@Reference(
		target = MFAPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
		super.setConfiguration(configuration);

		_columnBitmaskEnabled = GetterUtil.getBoolean(
			configuration.get(
				"value.object.column.bitmask.enabled.com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP"),
			true);
	}

	@Override
	@Reference(
		target = MFAPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = MFAPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
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

	private static final String _SQL_SELECT_MFAEMAILOTP =
		"SELECT mfaEmailOTP FROM MFAEmailOTP mfaEmailOTP";

	private static final String _SQL_SELECT_MFAEMAILOTP_WHERE =
		"SELECT mfaEmailOTP FROM MFAEmailOTP mfaEmailOTP WHERE ";

	private static final String _SQL_COUNT_MFAEMAILOTP =
		"SELECT COUNT(mfaEmailOTP) FROM MFAEmailOTP mfaEmailOTP";

	private static final String _SQL_COUNT_MFAEMAILOTP_WHERE =
		"SELECT COUNT(mfaEmailOTP) FROM MFAEmailOTP mfaEmailOTP WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "mfaEmailOTP.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No MFAEmailOTP exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No MFAEmailOTP exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		MFAEmailOTPPersistenceImpl.class);

	static {
		try {
			Class.forName(MFAPersistenceConstants.class.getName());
		}
		catch (ClassNotFoundException cnfe) {
			throw new ExceptionInInitializerError(cnfe);
		}
	}

}