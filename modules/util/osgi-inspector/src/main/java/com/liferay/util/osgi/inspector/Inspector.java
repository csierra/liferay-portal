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

package com.liferay.util.osgi.inspector;

import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.event.OutputEvent;
import alice.tuprolog.event.OutputListener;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true,
	property = {
		"osgi.command.function=query", "osgi.command.scope=ins"
	},
	service = Object.class
)
public class Inspector {

	public void query(String input) throws Exception {
		Prolog prolog = new Prolog();

		prolog.loadLibrary("alice.tuprolog.lib.BasicLibrary");
		prolog.loadLibrary("alice.tuprolog.lib.OOLibrary");
		prolog.loadLibrary(_myLibraryProvider.getService());
		prolog.addOutputListener(new OutputListener() {
			public void onOutput(OutputEvent outputEvent) {
				System.out.println("OUTPUT: " + outputEvent.getMsg());
			}
		});

		try {
			SolveInfo info = prolog.solve(input);

			System.out.println(info);

			while (prolog.hasOpenAlternatives()) {
				info = prolog.solveNext();

				System.out.println(info);
			}

			System.out.println("true.");
		}
		catch (NoMoreSolutionException e) {
			e.printStackTrace();

			System.out.println("true.");
		}
	}

	@Reference
	private ComponentServiceObjects<MyLibrary> _myLibraryProvider;
}
