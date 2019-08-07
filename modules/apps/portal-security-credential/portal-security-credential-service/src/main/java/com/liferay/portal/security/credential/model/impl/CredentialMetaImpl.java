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

import com.liferay.portal.security.credential.model.AlgorithmEntry;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The extended model implementation for the CredentialMeta service. Represents a row in the &quot;CredentialMeta&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.portal.security.credential.model.CredentialMeta</code> interface.
 * </p>
 *
 * @author arthurchan35
 */
@ProviderType
public class CredentialMetaImpl extends CredentialMetaBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. All methods that expect a credential meta model instance should use the {@link com.liferay.portal.security.credential.model.CredentialMeta} interface instead.
	 */
	public CredentialMetaImpl() {
	}

	public AlgorithmEntry getAlgorithmEntry() {
		return _algorithmEntry;
	}

	public void setAlgorithmEntry(AlgorithmEntry algorithmEntry) {
		_algorithmEntry = algorithmEntry;
	}

	private AlgorithmEntry _algorithmEntry;

}