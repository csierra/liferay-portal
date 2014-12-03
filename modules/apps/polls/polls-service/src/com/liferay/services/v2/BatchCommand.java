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

package com.liferay.services.v2;

/**
 * @author Carlos Sierra Andrés
 */
public abstract class BatchCommand<S extends ServiceContext> {

	public abstract void execute(S context);

	public final BatchCommand<S> and(final BatchCommand<S> next) {
		return new BatchCommand<S>() {

			@Override
			public void execute(S context) {
				BatchCommand.this.execute(context);

				next.execute(context);
			};
		};
	};

	public final <Q> Command<S, Q> and(final Command<S, Q> next) {
		return new Command<S, Q>() {

			@Override
			public void execute(S context, Q querier) {
				BatchCommand.this.execute(context);

				next.execute(context, querier);
			};
		};
	}

}
