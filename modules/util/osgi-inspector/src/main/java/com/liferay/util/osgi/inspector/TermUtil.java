/*
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

package com.liferay.util.osgi.inspector;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;

/**
 * @author Adolfo Pérez
 */
public class TermUtil {

	private TermUtil() {}

	public static String asString(Term term) {
		if (!(term instanceof Struct)) {
			throw new IllegalArgumentException(
				String.format("term %s not an atom or list"));
		}

		Struct struct = (Struct)term;

		if (!struct.isCompound()) {
			return struct.getName();
		}

		StringBuilder sb = new StringBuilder();

		appendTerm(struct, sb);

		return sb.toString();
	}

	private static void appendTerm(Term term, StringBuilder sb) {
		if (!term.isCompound()) {
			sb.append(term.toString());
		}
		else {
			Struct struct = (Struct)term;

			appendTerm(struct.getArg(0), sb);

			sb.append('.');

			appendTerm(struct.getArg(1), sb);
		}
	}

}
