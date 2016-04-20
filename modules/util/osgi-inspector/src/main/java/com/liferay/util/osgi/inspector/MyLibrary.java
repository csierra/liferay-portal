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
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.runtime.ServiceComponentRuntime;
import org.osgi.service.component.runtime.dto.ComponentConfigurationDTO;
import org.osgi.service.component.runtime.dto.ComponentDescriptionDTO;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true, service = MyLibrary.class)
public class MyLibrary extends Library {

	private BundleContext _bundleContext;
	private Bundle[] _bundles;
	private Collection<ComponentDescriptionDTO>
			_componentDescriptionDTOs;
	private ServiceReference[] _serviceReferences;

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
		_serviceReferences = Arrays.stream(_bundles).flatMap(
				b ->
				{
					ServiceReference<?>[] registeredServices =
						b.getRegisteredServices();
					if (registeredServices != null) {
						return Arrays.stream(registeredServices);
					}
					else {
						return Stream.empty();
					}
				}).collect(
				Collectors.toList()).toArray(new ServiceReference[0]);
		_componentDescriptionDTOs =
			_serviceComponentRuntime.getComponentDescriptionDTOs(_bundles);

		try {
			((OOLibrary)lib).register(
				new Struct("bundlesArray"), _bundles);
		}
		catch (InvalidObjectIdException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean bundleNumber_2(Term bundle, Term pos)
		throws InvalidObjectIdException {

		Term bundleTerm = bundle.getTerm();
		Term posTerm = pos.getTerm();

		if (posTerm instanceof Var) {
			String bsn = ((Struct)bundleTerm).getName();


			for (int i = 0; i < _bundles.length; i++) {
				if (bsn.equals(_bundles[i].getSymbolicName())) {
					return pos.unify(getEngine(), new Int(i));
				}
			}

			return false;
		}

		if (posTerm instanceof Int) {
			int position = ((Int)posTerm).intValue();

			Bundle requested = _bundles[position];

			return bundleTerm.unify(
				getEngine(), new Struct(requested.getSymbolicName()));
		}

		return false;
	}

	public boolean serviceNumber_2(Term service, Term pos)
			throws InvalidObjectIdException {

		Term serviceTerm = service.getTerm();
		Term posTerm = pos.getTerm();

		if (posTerm instanceof Var) {
			String interfaceName = ((Struct)serviceTerm).getName();

			for (int i = 0; i < _serviceReferences.length; i++) {
				if (interfaceName.equals(_serviceReferences[i].getProperty("objectClass"))) {
					return pos.unify(getEngine(), new Int(i));
				}
			}

			return false;
		}

		if (posTerm instanceof Int) {
			int position = ((Int)posTerm).intValue();

			ServiceReference<?> requested = _serviceReferences[position];

			return serviceTerm.unify(
				getEngine(), new Int(Integer.parseInt(requested.getProperty("service.id").toString())));
		}

		return false;
	}

	public boolean componentNumber_2(Term component, Term pos)
			throws InvalidObjectIdException {

		Term bundleTerm = component.getTerm();
		Term posTerm = pos.getTerm();

		if (posTerm instanceof Var) {
			String componentName = ((Struct)bundleTerm).getName();

			int i = 0;

			for (ComponentDescriptionDTO componentDescriptionDTO : _componentDescriptionDTOs) {
				if (componentName.equals(componentDescriptionDTO.name)) {

					return pos.unify(getEngine(), new Int(i));
				}

				i++;
			}

			return false;
		}

		if (posTerm instanceof Int) {
			int position = ((Int)posTerm).intValue();

			Iterator<ComponentDescriptionDTO> iterator =
				_componentDescriptionDTOs.iterator();

			while (position > 0 && iterator.hasNext()) {
				iterator.next();

				position--;
			}

			return bundleTerm.unify(
				getEngine(), new Struct(iterator.next().name));
		}

		return false;
	}

	public boolean state_2(Term component, Term state) {
		String name = ((Struct) component.getTerm()).getName();
		Term stateTerm = state.getTerm();

		Optional<Boolean> maybe =
			_componentDescriptionDTOs.stream().filter(
				c -> c.name.equals(name)).
			findFirst().flatMap(cd ->
				_serviceComponentRuntime.getComponentConfigurationDTOs(cd).
			stream().findFirst()).map(
				cc -> stateTerm.unify(getEngine(), new Int(cc.state)));

		return maybe.orElse(false);
	}

	public String getTheory() {
		return "bundleAccum(X, Y) :- bundleNumber(X, Y); Z is Y + 1, bundleAccum(X, Z).\n" +
			   "bundle(X) :- bundleAccum(X, 0).\n" +
			   "componentAccum(X, Y) :- componentNumber(X, Y); Z is Y + 1, componentAccum(X, Z).\n" +
			   "component(X) :- componentAccum(X, 0).\n" +
			   "serviceAccum(X, Y) :- serviceNumber(X, Y); Z is Y + 1, serviceAccum(X, Z).\n" +
			   "service(X) :- serviceAccum(X, 0).\n" +
			   "active(X) :- component(X), state(X, 8).\n" +
			   "inactive(X) :- component(X), not(state(X, 8)).";
	}

	@Reference
	private ServiceComponentRuntime _serviceComponentRuntime;
}
