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

package com.liferay.portal.security.credential.model.impl;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The extended model implementation for the CredentialEntry service. Represents a row in the &quot;CredentialEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.portal.security.credential.model.CredentialEntry</code> interface.
 * </p>
 *
 * @author arthurchan35
 */
@ProviderType
public class CredentialEntryImpl extends CredentialEntryBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. All methods that expect a credential entry model instance should use the {@link com.liferay.portal.security.credential.model.CredentialEntry} interface instead.
	 */
	public CredentialEntryImpl() {
	}

	/**
	 * Test if this given secret is the same as this credential's secret by using a timing-attack-proof comparison algorithm
	 * See http://bryanavery.co.uk/cryptography-net-avoiding-timing-attack/
	 *
	 * @param SecretToCompare the given secret to be compared with
	 * @return The result of if two secrets are the same
	 */
	@Override
	public boolean timingAttackProofEquals(String secretToCompare) {
		String secretSelf = getSecret();

		byte[] self = secretSelf.getBytes();

		byte[] input = secretToCompare.getBytes();

		int diff = self.length ^ input.length;

		for (int i = 0; (i < self.length) && (i < input.length); ++i) {
			diff |= self[i] ^ input[i];
		}

		if (diff == 0) {
			return true;
		}

		return false;
	}

}