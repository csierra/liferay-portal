package com.liferay.functional; /**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

import javaslang.Function1;

/**
 * @author Carlos Sierra Andrés
 */
public interface Functor<T> {

	<S> Functor<S> map(Function1<T, S> function);

	/**
	 * So a Functor<T> represents a container, a context, in which we can operate with
	 * the content without having to know its meaning.
	 */

}
