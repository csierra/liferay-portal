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

package com.liferay.multi.factor.authentication.email.otp.service.persistence;

import com.liferay.multi.factor.authentication.email.otp.exception.NoSuchEmailOTPException;
import com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the mfa email otp service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Arthur Chan
 * @see MFAEmailOTPUtil
 * @generated
 */
@ProviderType
public interface MFAEmailOTPPersistence extends BasePersistence<MFAEmailOTP> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link MFAEmailOTPUtil} to access the mfa email otp persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns the mfa email otp where userId = &#63; or throws a <code>NoSuchEmailOTPException</code> if it could not be found.
	 *
	 * @param userId the user ID
	 * @return the matching mfa email otp
	 * @throws NoSuchEmailOTPException if a matching mfa email otp could not be found
	 */
	public MFAEmailOTP findByUserId(long userId) throws NoSuchEmailOTPException;

	/**
	 * Returns the mfa email otp where userId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param userId the user ID
	 * @return the matching mfa email otp, or <code>null</code> if a matching mfa email otp could not be found
	 */
	public MFAEmailOTP fetchByUserId(long userId);

	/**
	 * Returns the mfa email otp where userId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param userId the user ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching mfa email otp, or <code>null</code> if a matching mfa email otp could not be found
	 */
	public MFAEmailOTP fetchByUserId(long userId, boolean useFinderCache);

	/**
	 * Removes the mfa email otp where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @return the mfa email otp that was removed
	 */
	public MFAEmailOTP removeByUserId(long userId)
		throws NoSuchEmailOTPException;

	/**
	 * Returns the number of mfa email otps where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching mfa email otps
	 */
	public int countByUserId(long userId);

	/**
	 * Caches the mfa email otp in the entity cache if it is enabled.
	 *
	 * @param mfaEmailOTP the mfa email otp
	 */
	public void cacheResult(MFAEmailOTP mfaEmailOTP);

	/**
	 * Caches the mfa email otps in the entity cache if it is enabled.
	 *
	 * @param mfaEmailOTPs the mfa email otps
	 */
	public void cacheResult(java.util.List<MFAEmailOTP> mfaEmailOTPs);

	/**
	 * Creates a new mfa email otp with the primary key. Does not add the mfa email otp to the database.
	 *
	 * @param emailOTPId the primary key for the new mfa email otp
	 * @return the new mfa email otp
	 */
	public MFAEmailOTP create(long emailOTPId);

	/**
	 * Removes the mfa email otp with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param emailOTPId the primary key of the mfa email otp
	 * @return the mfa email otp that was removed
	 * @throws NoSuchEmailOTPException if a mfa email otp with the primary key could not be found
	 */
	public MFAEmailOTP remove(long emailOTPId) throws NoSuchEmailOTPException;

	public MFAEmailOTP updateImpl(MFAEmailOTP mfaEmailOTP);

	/**
	 * Returns the mfa email otp with the primary key or throws a <code>NoSuchEmailOTPException</code> if it could not be found.
	 *
	 * @param emailOTPId the primary key of the mfa email otp
	 * @return the mfa email otp
	 * @throws NoSuchEmailOTPException if a mfa email otp with the primary key could not be found
	 */
	public MFAEmailOTP findByPrimaryKey(long emailOTPId)
		throws NoSuchEmailOTPException;

	/**
	 * Returns the mfa email otp with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param emailOTPId the primary key of the mfa email otp
	 * @return the mfa email otp, or <code>null</code> if a mfa email otp with the primary key could not be found
	 */
	public MFAEmailOTP fetchByPrimaryKey(long emailOTPId);

	/**
	 * Returns all the mfa email otps.
	 *
	 * @return the mfa email otps
	 */
	public java.util.List<MFAEmailOTP> findAll();

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
	public java.util.List<MFAEmailOTP> findAll(int start, int end);

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
	public java.util.List<MFAEmailOTP> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<MFAEmailOTP>
			orderByComparator);

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
	public java.util.List<MFAEmailOTP> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<MFAEmailOTP>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the mfa email otps from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of mfa email otps.
	 *
	 * @return the number of mfa email otps
	 */
	public int countAll();

}