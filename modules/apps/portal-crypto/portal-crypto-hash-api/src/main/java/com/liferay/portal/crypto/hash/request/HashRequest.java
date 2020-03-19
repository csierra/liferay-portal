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

import com.liferay.portal.crypto.hash.request.salt.command.SaltProvider;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 * @author Arthur Chan
 */
public class HashRequest {

	public static PepperBuilder newBuilder() {
		return new Builder(null, null);
	}

	public byte[] getInput() {
		return Arrays.copyOf(_input, _input.length);
	}

	public Optional<byte[]> getPepper() {
		return _pepper;
	}

	public Optional<Function<SaltProvider, byte[]>> getSaltFunction() {
		return _saltCommand;
	}

	public static class Builder implements PepperBuilder {

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
				throw new IllegalArgumentException("pepper can not be null");
			}

			return new Builder(pepper, null);
		}

		@Override
		public InputBuilder salt(byte[] saltBytes) {
			if (saltBytes == null) {
				throw new IllegalArgumentException("saltBytes can not be null");
			}

			return new Builder(
				_pepperCommand,
				__ -> Arrays.copyOf(saltBytes, saltBytes.length));
		}

		@Override
		public InputBuilder saltProvider(
			Function<SaltProvider, byte[]> saltCommand) {

			if (saltCommand == null) {
				throw new IllegalArgumentException(
					"saltCommand can not be null");
			}

			return new Builder(_pepperCommand, saltCommand);
		}

		private Builder(
			byte[] pepperCommand, Function<SaltProvider, byte[]> saltCommand) {

			_pepperCommand = pepperCommand;
			_saltCommand = saltCommand;
		}

		private final byte[] _pepperCommand;
		private Function<SaltProvider, byte[]> _saltCommand;

	}

	public interface InputBuilder {

		public HashRequest input(byte[] input);

	}

	public interface PepperBuilder extends SaltBuilder {

		public SaltBuilder pepper(byte[] pepper);

	}

	public interface SaltBuilder extends InputBuilder {

		public InputBuilder salt(byte[] saltBytes);

		public InputBuilder saltProvider(
			Function<SaltProvider, byte[]> saltCommand);

	}

	private HashRequest(
		byte[] pepper, Function<SaltProvider, byte[]> saltFunction,
		byte[] input) {

		_pepper = Optional.ofNullable(pepper);
		_saltCommand = Optional.ofNullable(saltFunction);
		_input = input;
	}

	private final byte[] _input;
	private final Optional<byte[]> _pepper;
	private final Optional<Function<SaltProvider, byte[]>> _saltCommand;

}