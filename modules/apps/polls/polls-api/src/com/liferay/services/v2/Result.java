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

import java.util.List;
import com.liferay.portal.kernel.util.Function;

/**
 * @author Carlos Sierra Andrés
 */
public interface Result<T> {

	public List<ConstraintViolation> getViolations();

	public T get();

	public boolean isEmpty();

	public static class ResultImpl<Q, T> implements Result {

		private CommandContext<?> _commandContext;
		private Q _q;
		private Function<Q, T> _function;

		public ResultImpl(
			CommandContext<?> commandContext, Q q, Function<Q, T> function) {

			_commandContext = commandContext;
			_q = q;
			_function = function;
		}

		@Override
		public List<ConstraintViolation> getViolations() {
			return _commandContext.getViolations();
		}

		@Override
		public T get() {
			return _function.apply(_q);
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

	}

}
