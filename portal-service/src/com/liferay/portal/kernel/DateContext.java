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

package com.liferay.portal.kernel;

import com.liferay.portal.kernel.util.AutoResetThreadLocal;

import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Carlos Sierra Andrés
 */
public abstract class DateContext implements AutoCloseable {

	public static class Stackable extends DateContext {
		public Stackable() {
			new Fixed(new Date());
		}
	}

	public static class Provided extends DateContext {
		public Provided() {
			Deque<Date> dates = dateStack.get();

			Date currentDate = dates.peek();

			if (currentDate == null) {
				dates.push(new Date());
			}
		}
	}

	public static class Fixed extends DateContext {
		public Fixed(Date date) {
			Deque<Date> dates = dateStack.get();

			dates.push(date);
		}
	}

	@Override
	public void close() {
		Deque<Date> dates = dateStack.get();

		dates.pop();
	}

	public Date getCurrentDate() {
		return dateStack.get().peek();
	}

	public static AutoResetThreadLocal<Deque<Date>> dateStack =
		new AutoResetThreadLocal<Deque<Date>>("foo", new LinkedList<Date>());
}
