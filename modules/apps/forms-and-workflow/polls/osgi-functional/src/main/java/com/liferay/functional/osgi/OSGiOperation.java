/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.functional.osgi;

import org.osgi.framework.BundleContext;
import rx.Observable;

import java.util.function.Consumer;

/**
 * @author Carlos Sierra Andrés
 */
public interface OSGiOperation<T> {

	OSGiResult<T> run(BundleContext bundleContext);

	class OSGiResult<T> {
		Observable<T> added;
		Observable<T> removed;
		Consumer<Void> start;
		Consumer<Void> close;

		OSGiResult(
			Observable<T> added, Observable<T> removed,
			Consumer<Void> start, Consumer<Void> close) {

			this.added = added;
			this.removed = removed;
			this.start = start;
			this.close = close;
		}
	}



}
