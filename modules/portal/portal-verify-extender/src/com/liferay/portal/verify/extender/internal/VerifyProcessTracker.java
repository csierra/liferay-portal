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

package com.liferay.portal.verify.extender.internal;

import com.liferay.osgi.service.tracker.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.map.ServiceTrackerMapFactory;
import com.liferay.portal.DatabaseContext;
import com.liferay.portal.DatabaseProcessContext;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.spring.transaction.ThreadLocalTransactionModeProvider;
import com.liferay.portal.upgrade.api.OutputStreamProvider;
import com.liferay.portal.upgrade.api.OutputStreamProvider.OutputStreamInformation;
import com.liferay.portal.upgrade.api.OutputStreamProviderTracker;
import com.liferay.portal.verify.Verifier;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true,
	property = {
		"osgi.command.function=list", "osgi.command.function=execute",
		"osgi.command.function=executeAll", "osgi.command.function=reports",
		"osgi.command.scope=verify"
	},
	service = {VerifyProcessTracker.class}
)
public class VerifyProcessTracker {

	private OutputStreamProviderTracker _outputStreamProviderTracker;

	public void execute(final String verifierName) {
		OutputStreamProvider outputStreamProvider =
			_outputStreamProviderTracker.getDefaultOutputStreamProvider();

		final OutputStream outputStream = createOutputStream(
			verifierName, outputStreamProvider);

		StreamUtil.withStdOut(outputStream, new Runnable() {
			@Override
			public void run() {
				_executeVerifierByName(verifierName, outputStream);
			}
		});

		_safeCloseOutputStream(outputStream);
	}

	public void execute(String verifierName, String outputStreamProviderName) {

		OutputStreamProvider outputStreamProvider =
			_outputStreamProviderTracker.getOutputStreamProvider(
				outputStreamProviderName);

		OutputStream outputStream = createOutputStream(
			verifierName, outputStreamProvider);

		_executeVerifierByName(verifierName, outputStream);

		_safeCloseOutputStream(outputStream);
	}

	public void execute(Verifier verifier, final OutputStream outputStream) {
		boolean readOnlyTransaction =
			ThreadLocalTransactionModeProvider.getReadOnlyTransaction();

		ThreadLocalTransactionModeProvider.setReadOnlyTransaction(true);

		try {
			verifier.verify(new DatabaseProcessContext() {

				@Override
				public DatabaseContext getDatabaseContext() {
					return new DatabaseContext();
				}

				@Override
				public OutputStream getOutputStream() {
					return StreamUtil.uncloseable(outputStream);
				}

			});
		}
		finally {
			ThreadLocalTransactionModeProvider.setReadOnlyTransaction(
				readOnlyTransaction);
		}
	}

	public void executeAll() {
		Set<String> keySet = _verifiers.keySet();

		OutputStreamProvider outputStreamProvider =
			_outputStreamProviderTracker.getDefaultOutputStreamProvider();

		OutputStream outputStream = createOutputStream(
			"all", outputStreamProvider);

		for (String verifierName : keySet) {
			_executeVerifierByName(verifierName, outputStream);
		}

		_safeCloseOutputStream(outputStream);
	}

	public void executeAll(String reportName) {
		Set<String> keySet = _verifiers.keySet();

		OutputStreamProvider outputStreamProvider = _outputStreamProviderTracker.getOutputStreamProvider(
			reportName);

		OutputStream outputStream = createOutputStream(
			"all", outputStreamProvider);

		for (String verifierName : keySet) {
			_executeVerifierByName(verifierName, outputStream);
		}

		_safeCloseOutputStream(outputStream);
	}

	public Verifier getVerifier(String verifierName) {
		Verifier verifier = _verifiers.getService(verifierName);

		if (verifier == null) {
			throw new IllegalArgumentException(
				"Verifier with name " + verifierName + " is not registered");
		}

		return verifier;
	}

	public void list() {
		for (String key : _verifiers.keySet()) {
			list(key);
		}
	}

	public void list(String verifierName) {
		Verifier verifier = _verifiers.getService(verifierName);

		if (verifier != null) {
			System.out.println("Registered verifier: " + verifierName);
		}
		else {
			System.out.println(
				"No verifier registered with name: " + verifierName);
		}
	}

	public void reports() {
		Set<String> outputStreamProviderNames =
			_outputStreamProviderTracker.getOutputStreamProviderNames();

		for (String s : outputStreamProviderNames) {
			System.out.println(s);
		}
	}


	public ServiceTrackerMap<String, Verifier> _verifiers;

	@Activate
	protected void activate(BundleContext bundleContext) {
		try {
			_verifiers = ServiceTrackerMapFactory.singleValueMap(
				bundleContext, Verifier.class, "verifier.name");

			_verifiers.open();
		}
		catch (InvalidSyntaxException e) {
			throw new IllegalStateException(e);
		}
	}

	protected OutputStream createOutputStream(
		String hint, OutputStreamProvider provider) {

		OutputStreamInformation outputStreamInformation = provider.create(hint);

		System.out.println(
			"Sending output to: " + outputStreamInformation.getDescription());

		return outputStreamInformation.getOutputStream();
	}

	@Deactivate
	protected void deactivate() {
		_verifiers.close();
	}

	private void _safeCloseOutputStream(OutputStream outputStream) {
		try {
			outputStream.close();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void _executeVerifierByName(
		String verifierName, OutputStream outputStream) {

		PrintWriter printWriter = new PrintWriter(outputStream);

		printWriter.println("Executing " + verifierName);

		printWriter.flush();

		Verifier verifier = getVerifier(verifierName);

		execute(verifier, outputStream);
	}

	@Reference
	public void setOutputStreamTracker(
		OutputStreamProviderTracker outputStreamProviderTracker) {

		_outputStreamProviderTracker = outputStreamProviderTracker;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		VerifyProcessTracker.class);

}