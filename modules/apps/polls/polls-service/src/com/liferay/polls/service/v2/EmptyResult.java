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

package com.liferay.polls.service.v2;

import com.liferay.services.v2.ConstraintViolation;
import com.liferay.services.v2.Result;

import java.util.List;

/**
* @author Carlos Sierra Andrés
*/
class EmptyResult<R> implements Result<List<R>> {

	private List<ConstraintViolation> _violations;

	public EmptyResult(List<ConstraintViolation> violations) {

		_violations = violations;
	}

	@Override
	public List<ConstraintViolation> getViolations() {
		return _violations;
	}

	@Override
	public List<R> get() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}
}
