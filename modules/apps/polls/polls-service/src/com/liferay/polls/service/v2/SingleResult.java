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
class SingleResult<R> implements Result<R> {
	private final List<ConstraintViolation> _violations;
	private final R result;

	public SingleResult(List<ConstraintViolation> violations, R result) {
		_violations = violations;
		this.result = result;
	}

	@Override
	public List<ConstraintViolation> getViolations() {
		return _violations;
	}

	@Override
	public R get() {
		return result;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

}
