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

package com.liferay.oauth2.provider.constants;

/**
 * @author Tomas Polesovsky
 */
public enum GrantType {
	AUTHORIZATION_CODE(true, true, false),
	AUTHORIZATION_CODE_PKCE(true, false, true),
	CLIENT_CREDENTIALS(false, true, false),
	RESOURCE_OWNER_PASSWORD(false, true, true),
	REFRESH_TOKEN(false, true, true);

	GrantType(
		boolean requiresRedirectURI, boolean supportsConfidentialClients,
		boolean supportsPublicClients) {

		_requiresRedirectURI = requiresRedirectURI;
		_supportsConfidentialClients = supportsConfidentialClients;
		_supportsPublicClients = supportsPublicClients;
	}

	public boolean isRequiresRedirectURI() {
		return _requiresRedirectURI;
	}

	public boolean isSupportsConfidentialClients() {
		return _supportsConfidentialClients;
	}

	public boolean isSupportsPublicClients() {
		return _supportsPublicClients;
	}

	private boolean _requiresRedirectURI;
	private boolean _supportsConfidentialClients;
	private boolean _supportsPublicClients;
}
