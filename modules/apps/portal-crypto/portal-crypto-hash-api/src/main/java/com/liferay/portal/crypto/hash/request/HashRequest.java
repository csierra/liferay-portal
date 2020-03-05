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

package com.liferay.portal.crypto.hash.request;

import com.liferay.portal.crypto.hash.request.builder.InputBuilder;
import com.liferay.portal.crypto.hash.request.builder.PepperBuilder;
import com.liferay.portal.crypto.hash.request.builder.SaltBuilder;
import com.liferay.portal.crypto.hash.request.command.pepper.PepperCommand;
import com.liferay.portal.crypto.hash.request.command.salt.SaltCommand;

/**
 * @author Carlos Sierra Andrés
 * @author Arthur Chan
 */
public class HashRequest {

	public byte[] getInput() {
		return _input;
	}

	public PepperCommand getPepperCommand() {
		return _pepperCommand;
	}

	public SaltCommand getSaltCommand() {
		return _saltCommand;
	}

	public static class Builder implements PepperBuilder {

		@Override
		public HashRequest input(byte[] input) {
			return new HashRequest(_pepperCommand, _saltCommand, input);
		}

		public PepperBuilder newBuilder() {
			return new Builder(null, null);
		}

		@Override
		public SaltBuilder pepperCommand(PepperCommand pepperCommand) {
			return new Builder(pepperCommand, null);
		}

		@Override
		public InputBuilder saltCommand(SaltCommand saltCommand) {
			return new Builder(_pepperCommand, saltCommand);
		}

		private Builder(
			PepperCommand pepperCommand, SaltCommand saltCommand) {

			_pepperCommand = pepperCommand;
			_saltCommand = saltCommand;
		}

		private PepperCommand _pepperCommand;
		private SaltCommand _saltCommand;

	}

	private HashRequest(
		PepperCommand pepperCommand, SaltCommand saltCommand, byte[] input) {

		_pepperCommand = pepperCommand;
		_saltCommand = saltCommand;
		_input = input;
	}

	private final byte[] _input;
	private PepperCommand _pepperCommand;
	private SaltCommand _saltCommand;

}