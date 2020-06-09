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

package com.liferay.portal.remote.cors.internal.path.pattern.matcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur Chan
 */
public class PatternPackage<T> {

	public PatternPackage() {
		_cargoList = new ArrayList<>(_BIG_ENOUGH);
	}

	public void clear() {
		_pattern = null;
		_cargoList.clear();
	}

	public List<T> getCargoList() {
		return _cargoList;
	}

	public String getPattern() {
		return _pattern;
	}

	public boolean isEmpty() {
		if (_pattern == null) {
			return true;
		}

		return false;
	}

	public void set(String pattern, T cargo) {
		if (_pattern == null) {
			_pattern = pattern;
		}

		_cargoList.add(cargo);
	}

	private static final byte _BIG_ENOUGH = 32;

	private final List<T> _cargoList;
	private String _pattern;

}