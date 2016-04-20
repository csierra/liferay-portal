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
import alice.tuprolog.Long;
import alice.tuprolog.Number;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.runtime.ServiceComponentRuntime;

import java.util.Arrays;
import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true, service = MyLibrary.class)
public class MyLibrary extends Library {

	private BundleContext _bundleContext;
	private Bundle[] _bundles;

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}

	public String getName() {
		return "OSGi inspector";
	}

	public void onSolveBegin(Term term) {
		Library lib = engine.getLibrary("alice.tuprolog.lib.OOLibrary");

		_bundles = _bundleContext.getBundles();
	}

	public boolean bundles_1(Term bundles) {
		return bundles.unify(
			getEngine(),
			toList(_bundles, b -> new Long(b.getBundleId())));
	}

	public boolean services_2(Term bundleTerm, Term services) {
		Term term = bundleTerm.getTerm();

		long bundleId = ((Number)term).longValue();

		Bundle bundle = _bundleContext.getBundle(bundleId);

		return services.unify(
			getEngine(),
			toList(
				bundle.getRegisteredServices() != null ? bundle.getRegisteredServices() : new ServiceReference[0],
				sr -> new Long(java.lang.Long.parseLong(sr.getProperty("service.id").toString())
			)));
	}

	public boolean interfaces_2(Term serviceTerm, Term interfacesTerm)
		throws InvalidSyntaxException {

		Term term = serviceTerm.getTerm();

		long serviceId = ((Number) term).longValue();

		ServiceReference<?>[] serviceReferences =
			_bundleContext.getAllServiceReferences(
				null, "(service.id=" + serviceId + ")");

		ServiceReference<?> serviceReference = serviceReferences[0];

		return interfacesTerm.unify(
			getEngine(),
			toList(
				(Object[])serviceReference.getProperty("objectClass"),
				s -> new Struct(s.toString())));
	}

	private <T> Struct toList(T[] array, Function<T, Term> function) {
		return new Struct(
			Arrays.stream(
				array).
				map(function).
				toArray(Term[]::new)
		);
	}

	public String getTheory() {
		return "bundle(X) :- bundles(Bundles), member(X, Bundles).\n" +
			   "service(B, X) :- bundle(B), services(B, S), member(X, S).\n" +
			   "interface(X, Y) :- service(X), interfaces(X, Interfaces), member(Y, Interfaces).";
	}

	@Reference
	private ServiceComponentRuntime _serviceComponentRuntime;
}
