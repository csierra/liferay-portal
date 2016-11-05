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

package com.liferay.functional;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

/**
 * @author Carlos Sierra Andrés
 */
public class ScratchPad {

	public interface Request {
		Optional<String> getParameter(String parameter);
	}



	public static void main(String[] args) {

		Request request = parameter -> null;

		Optional<String> maybeName = request.getParameter("name");
		Optional<String> maybeSurname = request.getParameter("surname");
		Optional<Integer> maybeDay = request.getParameter("day").map(Integer::parseInt);
		Optional<Integer> maybeMonth = request.getParameter("month").map(Integer::parseInt);
		Optional<Integer> maybeYear = request.getParameter("year").map(Integer::parseInt);








	}
}
