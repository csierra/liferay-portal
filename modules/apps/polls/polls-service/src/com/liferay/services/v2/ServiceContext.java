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

package com.liferay.services.v2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Sierra Andrés
 */
public class ServiceContext<S> {

	private List<ConstraintViolation> _violations = new ArrayList<>();
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ConstraintViolation> getViolations() {
		return _violations;
	}

	public void addViolation(ConstraintViolation violation) {
		_violations.add(violation);
	}

}
