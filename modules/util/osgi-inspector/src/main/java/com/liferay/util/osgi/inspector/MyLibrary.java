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
import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Library;
import alice.tuprolog.Long;
import alice.tuprolog.Prolog;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.lib.OOLibrary;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.runtime.ServiceComponentRuntime;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true, service = MyLibrary.class)
public class MyLibrary extends Library {

	private BundleContext _bundleContext;
	private OOLibrary _ooLibrary;

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}

	public void setEngine(Prolog prolog) {
		super.setEngine(prolog);

		_ooLibrary = (OOLibrary) getEngine().getLibrary(
			"alice.tuprolog.lib.OOLibrary");

		try {
			prolog.addTheory(new Theory(
				toList(
					_bundleContext.getBundles(),
					b -> new Struct(
						"bundle",
						new Long(b.getBundleId()),
						new Struct(b.getSymbolicName()),
						_ooLibrary.register(b)))));
			prolog.addTheory(new Theory(
				toList(
					_serviceComponentRuntime.getComponentDescriptionDTOs(
						_bundleContext.getBundles()).stream(),
					c -> new Struct(
						"component",
						new Struct(c.name),
						new Struct(c.implementationClass),
						toList(c.serviceInterfaces, Struct::new),
						toList(c.references, r -> new Struct(
							"reference",
							new Struct(r.name),
							new Struct(r.interfaceName))),
						toList(
							_serviceComponentRuntime.getComponentConfigurationDTOs(c).stream(),
							cc ->
								new Struct("configuration",
									new Long(cc.id),
									new Int(cc.state),
									toList(cc.satisfiedReferences, sr -> new Struct("reference", new Struct(sr.name), new Struct(sr.target != null ? sr.target : ""))),
									toList(cc.unsatisfiedReferences, ur -> new Struct("reference", new Struct(ur.name), new Struct(ur.target != null ? ur.target : ""))),
									_ooLibrary.register(c))),
						_ooLibrary.register(_bundleContext.getBundle(c.bundle.id)),
						_ooLibrary.register(c)))));

			List<DependencyManager> dependencyManagers = DependencyManager.getDependencyManagers();

			prolog.addTheory(
				new Theory(
					toList(dependencyManagers.stream().flatMap(
					dm -> (Stream<org.apache.felix.dm.ComponentDeclaration>)dm.getComponents().stream()),
					cd -> new Struct(
						"dm",
						new Struct(cd.getName())
					))));
		}
		catch (InvalidTheoryException e) {
			throw new RuntimeException(e);
		}
	}

	public String getName() {
		return "OSGi inspector";
	}

	private <T> Struct toList(T[] array, Function<T, Term> function) {
		return toList(Arrays.stream(array), function);
	}

	private <T> Struct toList(Stream<T> stream, Function<T, Term> function) {
		return new Struct(stream.map(function).toArray(Term[]::new));
	}

	public String getTheory() {
		URL theoryResource = MyLibrary.class.getResource("theory.pl");

		try {
			return new Theory(theoryResource.openStream()).toString();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Reference
	private ServiceComponentRuntime _serviceComponentRuntime;

}
