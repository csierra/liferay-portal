/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.security.cas;

import com.liferay.portal.kernel.cas.LiferayCasPrincipal;
import org.jasig.cas.client.authentication.AttributePrincipal;

import java.util.Map;

/**
 * @author Carlos Sierra Andrés
 */
public class LiferayCasPrincipalImpl implements LiferayCasPrincipal {

	public LiferayCasPrincipalImpl(AttributePrincipal attributePrincipal) {
		_attributePrincipal = attributePrincipal;
	}

	@Override
	public Map getAttributes() {
		return _attributePrincipal.getAttributes();
	}

	@Override
	public String getName() {
		return _attributePrincipal.getName();
	}

	@Override
	public String getProxyTicketFor(String service) {
		return _attributePrincipal.getProxyTicketFor(service);
	}

	private final AttributePrincipal _attributePrincipal;
}
