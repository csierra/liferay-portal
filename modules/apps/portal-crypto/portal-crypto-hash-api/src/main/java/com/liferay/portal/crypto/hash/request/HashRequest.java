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
			return new HashRequest(input, _saltCommand, _pepperCommand);
		}

		public PepperBuilder newBuilder() {
			return new Builder();
		}

		@Override
		public SaltBuilder pepperCommand(PepperCommand pepperCommand) {
			_pepperCommand = pepperCommand;

			return this;
		}

		@Override
		public InputBuilder saltCommand(SaltCommand saltCommand) {
			_saltCommand = saltCommand;

			return this;
		}

		private Builder() {
		}

		private PepperCommand _pepperCommand;
		private SaltCommand _saltCommand;

	}

	private HashRequest(
		byte[] input, SaltCommand saltCommand, PepperCommand pepperCommand) {

		_input = input;
		_saltCommand = saltCommand;
		_pepperCommand = pepperCommand;
	}

	private final byte[] _input;
	private PepperCommand _pepperCommand;
	private SaltCommand _saltCommand;

}