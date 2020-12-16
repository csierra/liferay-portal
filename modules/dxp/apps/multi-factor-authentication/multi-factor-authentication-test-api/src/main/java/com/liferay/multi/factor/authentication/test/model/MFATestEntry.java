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

package com.liferay.multi.factor.authentication.test.model;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The extended model interface for the MFATestEntry service. Represents a row in the &quot;MFATestEntry&quot; database table, with each column mapped to a property of this class.
 *
 * @author Arthur Chan
 * @see MFATestEntryModel
 * @generated
 */
@ImplementationClassName(
	"com.liferay.multi.factor.authentication.test.model.impl.MFATestEntryImpl"
)
@ProviderType
public interface MFATestEntry extends MFATestEntryModel, PersistedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.liferay.multi.factor.authentication.test.model.impl.MFATestEntryImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<MFATestEntry, Long>
		MFA_TEST_ENTRY_ID_ACCESSOR = new Accessor<MFATestEntry, Long>() {

			@Override
			public Long get(MFATestEntry mfaTestEntry) {
				return mfaTestEntry.getMfaTestEntryId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<MFATestEntry> getTypeClass() {
				return MFATestEntry.class;
			}

		};

}