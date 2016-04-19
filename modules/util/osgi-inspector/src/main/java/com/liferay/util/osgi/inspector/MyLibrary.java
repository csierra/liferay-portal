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

import alice.tuprolog.Library;
import alice.tuprolog.Number;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.TermVisitor;
import alice.tuprolog.Var;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.runtime.ServiceComponentRuntime;
import org.osgi.service.component.runtime.dto.ComponentDescriptionDTO;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
		return "My Library";
	}

	private Map<Term, Iterator<Bundle>> _state = new HashMap<>();

	public boolean bundle_1(Term term) {
		Term t = term.getTerm();

		if (t instanceof Var) {
			return matchAllBundles((Var)t);
		}

		return matchBundleNamed(t);
	}

	protected boolean matchAllBundles(Var var) {
		if (_state.containsKey(var)) {
			Iterator<Bundle> bundleIterator = _state.get(var);

			if (!bundleIterator.hasNext()) {
				return false;
			}

			Bundle next = bundleIterator.next();

			var.unify(getEngine(), new Struct(next.getSymbolicName()));

			return true;
		}

		Bundle[] bundles = _bundleContext.getBundles();

		if (bundles == null) {
			return true;
		}

		Iterable<Bundle> iterable = Arrays.asList(bundles);

		_state.put(var, iterable.iterator());

		return matchAllBundles(var);
	}

	protected boolean matchBundleNamed(Term term) {
		String atomName = TermUtil.asString(term);

		for (Bundle bundle : _bundleContext.getBundles()) {
			if (atomName.equals(bundle.getSymbolicName())) {
				return true;
			}
		}

		return false;
	}

	@Reference
	private ServiceComponentRuntime _serviceComponentRuntime;
}
