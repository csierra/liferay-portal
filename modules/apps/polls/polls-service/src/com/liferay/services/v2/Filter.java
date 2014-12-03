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
public abstract class Filter<S extends ServiceContext<S>> {

	public abstract void execute(S context);

	public final Filter<S> and(final Filter<S> next) {
		return new Filter<S>() {

			@Override
			public void execute(S context) {
				Filter.this.execute(context);

				next.execute(context);
			}
		};
	};

}
