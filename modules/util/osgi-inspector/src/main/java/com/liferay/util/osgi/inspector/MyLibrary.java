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

import alice.tuprolog.Int;
import alice.tuprolog.Library;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Var;
import alice.tuprolog.lib.InvalidObjectIdException;
import alice.tuprolog.lib.OOLibrary;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.runtime.ServiceComponentRuntime;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true, service = MyLibrary.class)
public class MyLibrary extends Library {

	private BundleContext _bundleContext;

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}

	public String getName() {
		return "OSGi inspector";
	}

	public void onSolveBegin(Term term) {
		Library lib = engine.getLibrary("alice.tuprolog.lib.OOLibrary");

		try {
			((OOLibrary)lib).register(
				new Struct("bundlesArray"), _bundleContext.getBundles());
		}
		catch (InvalidObjectIdException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean bundleNumber_2(Term bundle, Term pos)
		throws InvalidObjectIdException {

		Term bundleTerm = bundle.getTerm();
		Term posTerm = pos.getTerm();

		Bundle[] bundles = _bundleContext.getBundles();

		if (posTerm instanceof Var) {
			String bsn = ((Struct)bundleTerm).getName();


			for (int i = 0; i < bundles.length; i++) {
				if (bsn.equals(bundles[i].getSymbolicName())) {
					return pos.unify(getEngine(), new Int(i));
				}
			}

			return false;
		}

		if (posTerm instanceof Int) {
			int position = ((Int)posTerm).intValue();

			Bundle requested = bundles[position];

			return bundleTerm.unify(
				getEngine(), new Struct(requested.getSymbolicName()));
		}

		return false;
	}

	public String getTheory() {
		return "bundleAccum(X, Y) :- bundleNumber(X, Y); Z is Y + 1, bundleAccum(X, Z).\n" +
			   "bundle(X) :- bundleAccum(X, 0).\n";
	}

	@Reference
	private ServiceComponentRuntime _serviceComponentRuntime;
}
