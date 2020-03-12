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

import com.liferay.portal.crypto.hash.request.pepper.command.PepperCommand;
import com.liferay.portal.crypto.hash.request.salt.command.SaltCommand;

import java.util.Arrays;
import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 * @author Arthur Chan
 */
public class HashRequest {

	public byte[] getInput() {
		return Arrays.copyOf(_input, _input.length);
	}

	public Function<PepperCommand, byte[]> getPepperCommand() {
		return _pepperCommand;
	}

	public Function<SaltCommand, byte[]> getSaltCommand() {
		return _saltCommand;
	}

	public static class Builder implements PepperBuilder {

		public static PepperBuilder newBuilder() {
			return new Builder(null, null);
		}

		@Override
		public HashRequest input(byte[] input) {
			if (input == null) {
				throw new IllegalArgumentException("input can not be null");
			}

			return new HashRequest(_pepperCommand, _saltCommand, input);
		}

		@Override
		public SaltBuilder pepper(
			Function<PepperCommand, byte[]> pepperCommand) {

			if (pepperCommand == null) {
				throw new IllegalArgumentException(
					"pepperCommand can not be null");
			}

			return new Builder(pepperCommand, null);
		}

		@Override
		public InputBuilder salt(Function<SaltCommand, byte[]> saltCommand) {
			if (saltCommand == null) {
				throw new IllegalArgumentException(
					"saltCommand can not be null");
			}

			return new Builder(_pepperCommand, saltCommand);
		}

		private Builder(
			Function<PepperCommand, byte[]> pepperCommand,
			Function<SaltCommand, byte[]> saltCommand) {

			_pepperCommand = pepperCommand;
			_saltCommand = saltCommand;
		}

		private Function<PepperCommand, byte[]> _pepperCommand;
		private Function<SaltCommand, byte[]> _saltCommand;

	}

	public interface InputBuilder {

		public HashRequest input(byte[] input);

	}

	public interface PepperBuilder extends SaltBuilder {

		public SaltBuilder pepper(
			Function<PepperCommand, byte[]> pepperCommand);

	}

	public interface SaltBuilder extends InputBuilder {

		public InputBuilder salt(Function<SaltCommand, byte[]> saltCommand);

	}

	private HashRequest(
		Function<PepperCommand, byte[]> pepperCommand,
		Function<SaltCommand, byte[]> saltCommand, byte[] input) {

		_pepperCommand = pepperCommand;
		_saltCommand = saltCommand;
		_input = input;
	}

	private final byte[] _input;
	private final Function<PepperCommand, byte[]> _pepperCommand;
	private final Function<SaltCommand, byte[]> _saltCommand;

}