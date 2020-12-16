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

package com.liferay.multi.factor.authentication.test.service.persistence;

import com.liferay.multi.factor.authentication.test.exception.NoSuchEntryException;
import com.liferay.multi.factor.authentication.test.model.MFATestEntry;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the mfa test entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Arthur Chan
 * @see MFATestEntryUtil
 * @generated
 */
@ProviderType
public interface MFATestEntryPersistence extends BasePersistence<MFATestEntry> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link MFATestEntryUtil} to access the mfa test entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Caches the mfa test entry in the entity cache if it is enabled.
	 *
	 * @param mfaTestEntry the mfa test entry
	 */
	public void cacheResult(MFATestEntry mfaTestEntry);

	/**
	 * Caches the mfa test entries in the entity cache if it is enabled.
	 *
	 * @param mfaTestEntries the mfa test entries
	 */
	public void cacheResult(java.util.List<MFATestEntry> mfaTestEntries);

	/**
	 * Creates a new mfa test entry with the primary key. Does not add the mfa test entry to the database.
	 *
	 * @param mfaTestEntryId the primary key for the new mfa test entry
	 * @return the new mfa test entry
	 */
	public MFATestEntry create(long mfaTestEntryId);

	/**
	 * Removes the mfa test entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param mfaTestEntryId the primary key of the mfa test entry
	 * @return the mfa test entry that was removed
	 * @throws NoSuchEntryException if a mfa test entry with the primary key could not be found
	 */
	public MFATestEntry remove(long mfaTestEntryId) throws NoSuchEntryException;

	public MFATestEntry updateImpl(MFATestEntry mfaTestEntry);

	/**
	 * Returns the mfa test entry with the primary key or throws a <code>NoSuchEntryException</code> if it could not be found.
	 *
	 * @param mfaTestEntryId the primary key of the mfa test entry
	 * @return the mfa test entry
	 * @throws NoSuchEntryException if a mfa test entry with the primary key could not be found
	 */
	public MFATestEntry findByPrimaryKey(long mfaTestEntryId)
		throws NoSuchEntryException;

	/**
	 * Returns the mfa test entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param mfaTestEntryId the primary key of the mfa test entry
	 * @return the mfa test entry, or <code>null</code> if a mfa test entry with the primary key could not be found
	 */
	public MFATestEntry fetchByPrimaryKey(long mfaTestEntryId);

	/**
	 * Returns all the mfa test entries.
	 *
	 * @return the mfa test entries
	 */
	public java.util.List<MFATestEntry> findAll();

	/**
	 * Returns a range of all the mfa test entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFATestEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa test entries
	 * @param end the upper bound of the range of mfa test entries (not inclusive)
	 * @return the range of mfa test entries
	 */
	public java.util.List<MFATestEntry> findAll(int start, int end);

	/**
	 * Returns an ordered range of all the mfa test entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFATestEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa test entries
	 * @param end the upper bound of the range of mfa test entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of mfa test entries
	 */
	public java.util.List<MFATestEntry> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<MFATestEntry>
			orderByComparator);

	/**
	 * Returns an ordered range of all the mfa test entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFATestEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa test entries
	 * @param end the upper bound of the range of mfa test entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of mfa test entries
	 */
	public java.util.List<MFATestEntry> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<MFATestEntry>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the mfa test entries from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of mfa test entries.
	 *
	 * @return the number of mfa test entries
	 */
	public int countAll();

}