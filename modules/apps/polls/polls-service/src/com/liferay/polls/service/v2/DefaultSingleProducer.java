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

import com.liferay.portal.kernel.util.Function;
import com.liferay.services.v2.Command;
import com.liferay.services.v2.CommandContext;
import com.liferay.services.v2.Result;
import com.liferay.services.v2.SingleProducer;
import com.liferay.services.v2.SingleRenderer;

/**
* @author Carlos Sierra Andrés
*/
class DefaultSingleProducer<Q, S extends CommandContext<S>>
	implements SingleProducer<Q, S> {

	private final S _context;
	private final Q _querier;

	public DefaultSingleProducer(S _context, Q querier) {

		this._context = _context;
		_querier = querier;
	}

	@Override
	public SingleRenderer<Q> execute(
		Command<S, Q> consumer) {

		consumer.execute(_context, _querier);

		return new SingleRenderer<Q>() {
			@Override
			public <R> Result<R> map(
				final Function<Q, R> function) {

				return new SingleResult<>(
					_context.getViolations(),
					function.apply(_querier));
			}
		};
	}

	@Override
	public <R> Result<R> map(
		final Function<Q, R> function) {

		return new SingleResult<>(
			_context.getViolations(),
			function.apply(_querier));
	}
}
