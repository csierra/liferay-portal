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
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.services.v2.Command;
import com.liferay.services.v2.CommandContext;
import com.liferay.services.v2.MultipleProducer;
import com.liferay.services.v2.MultipleRenderer;
import com.liferay.services.v2.Result;

import java.util.List;

/**
* @author Carlos Sierra Andrés
*/
class DefaultMultipleProducer<Q, S extends CommandContext<S>>
	implements MultipleProducer<Q, S> {

	private final S _context;
	private final List<? extends Q> _queriers;

	public DefaultMultipleProducer(
		S pollsContext, List<? extends Q> questions) {

		_context = pollsContext;
		_queriers = questions;
	}

	@Override
	public <R> Result<List<R>> map(
		Function<Q, R> function) {

		return new MultipleResult<>(
			_context.getViolations(), ListUtil.map(function, _queriers));
	}

	@Override
	public MultipleRenderer<Q> execute(
		Command<S, Q> command) {

		for (Q querier : _queriers) {
			command.execute(_context, querier);
		}

		return new MultipleRenderer<Q>() {

			@Override
			public <R> Result<List<R>> materialize(
				Function<Q, R> function) {

				return new MultipleResult<>(
					_context.getViolations(),
					ListUtil.map(function, _queriers));
			}
		};
	}

}
