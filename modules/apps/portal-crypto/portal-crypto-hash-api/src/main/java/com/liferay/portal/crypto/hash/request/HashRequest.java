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

import com.liferay.portal.crypto.hash.request.salt.SaltGenerator;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 * @author Arthur Chan
 */
public class HashRequest {

	public byte[] getInput() {
		return Arrays.copyOf(_input, _input.length);
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
		public SaltBuilder pepper(byte[] pepper) {
			if (pepper == null) {
				throw new IllegalArgumentException(
					"pepperCommand can not be null");
			}

			return new Builder(pepper, null);
		}

		@Override
		public InputBuilder salt(
			Function<SaltGenerator, byte[]> saltCommand) {

			if (saltCommand == null) {
				throw new IllegalArgumentException(
					"saltCommand can not be null");
			}

			return new Builder(_pepperCommand, saltCommand);
		}

		@Override
		public InputBuilder salt(byte[] salt) {
			if (salt == null) {
				throw new IllegalArgumentException(
					"salt can not be null");
			}

			return new Builder(_pepperCommand, __ -> salt);
		}

		private Builder(
			byte[] pepperCommand, Function<SaltGenerator, byte[]> saltCommand) {
			_pepperCommand = pepperCommand;
			_saltCommand = saltCommand;
		}

		private byte[] _pepperCommand;
		private Function<SaltGenerator, byte[]> _saltCommand;

	}

	public interface InputBuilder {

		public HashRequest input(byte[] input);

	}

	public interface PepperBuilder extends SaltBuilder {

		public SaltBuilder pepper(byte[] pepper);

	}

	public interface SaltBuilder extends InputBuilder {

		public InputBuilder salt(
			Function<SaltGenerator, byte[]> saltCommand);

		public InputBuilder salt(byte[] salt);

	}

	private HashRequest(
		byte[] pepperCommand, Function<SaltGenerator, byte[]> saltCommand, byte[] input) {

		_pepperCommand = Optional.ofNullable(pepperCommand);
		_saltCommand = Optional.ofNullable(saltCommand);
		_input = input;
	}

	private final byte[] _input;
	private Optional<byte[]> _pepperCommand;
	private Optional<Function<SaltGenerator, byte[]>> _saltCommand;

	public static void main(String[] args) {
		Builder.newBuilder().pepper(
			"pepper".getBytes()
		).salt(
			saltGenerator -> {
				Optional<byte[]> optionalBytes =
					saltGenerator.generateVariableSizeSalt(32);

				return optionalBytes.orElseGet(
					saltGenerator::generateDefaultSizeSalt);
			}
		);

	}
}