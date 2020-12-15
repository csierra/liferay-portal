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

package com.liferay.petra.string;

/**
 * @author Arthur Chan
 */
public final class XSSProofString {

	public XSSProofString(String original) {
		_original = original;
	}

	public String toString() {
		return _original;
	}

	public String toXSSProofString() {

		// do some stuff make the return string XSSProof

		return "XSS Proof: " + _original;
	}

	private final String _original;

}