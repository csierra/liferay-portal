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

package com.liferay.portal.security.crypto.generator.registry;


/**
 * @author Carlos Sierra Andrés
 */
public class HashRequest {

	public interface HashRequestBuilder {

		public static PepperHashRequestBuider newBuilder() {
			return new HashRequestBuilderImpl(null, null);
		}

	}


	public interface PepperHashRequestBuider extends SaltHashRequestBuilder {

		public SaltHashRequestBuilder pepper(PepperCommand pepperCommand);
	}

	public interface InputBuilder {
		public HashRequest input(byte[] input);
	}

	public interface SaltHashRequestBuilder extends InputBuilder {
		public InputBuilder saltCommand(SaltCommand saltCommand);
	}
	public interface PepperCommand {
		public static PepperCommand usePepper(String pepper) {

		}
	}

	public interface SaltCommand {
		public static GenerateVariableSizeSaltCommand

		generateVariableSizeSalt(int size) {

			return new GenerateVariableSizeSaltCommand(size);
		}

		public static OneOf oneOf(SaltCommand ... saltCommands) {
			return new OneOf(saltCommands);
		}

		public static UseSaltCommand useSalt(byte[] salt) {
			return new UseSaltCommand(salt);
		}

		public static GenerateDefaultSaltCommand generateDefaultSizeSalt() {
			return new GenerateDefaultSaltCommand();
		}

	}

	public static class GenerateVariableSizeSaltCommand implements SaltCommand {
		private int _size;

		public GenerateVariableSizeSaltCommand(int size) {
			_size = size;
		}

	}

	public static class UseSaltCommand implements SaltCommand {
		public UseSaltCommand(byte[] salt) {
		}

	}

	public static class GenerateDefaultSaltCommand implements SaltCommand {

	}

	public static class OneOf implements SaltCommand {
		private SaltCommand[] _saltCommands;

		public OneOf(SaltCommand ... saltCommands) {
			_saltCommands = saltCommands;
		}
	}

	public static class HashRequestBuilderImpl implements PepperHashRequestBuider {

		PepperCommand _pepperCommand;
		SaltCommand _saltCommand;
		byte[] _input;

		public HashRequestBuilderImpl(
			PepperCommand pepperCommand,
			SaltCommand saltCommand) {

			_pepperCommand = pepperCommand;
			_saltCommand = saltCommand;
		}

		@Override
		public SaltHashRequestBuilder pepper(
			PepperCommand pepperCommand) {

			return new HashRequestBuilderImpl(pepperCommand, null);
		}

		@Override
		public HashRequest input(byte[] input) {
			return new HashRequestImpl(_pepperCommand, _saltCommand, input);
		}

		@Override
		public InputBuilder saltCommand(
			SaltCommand saltCommand) {

			return new HashRequestBuilderImpl(_pepperCommand, saltCommand);
		}
	}

	public static class HashRequestImpl extends HashRequest {
		private final PepperCommand _pepperCommand;
		private final SaltCommand _saltCommand;
		private final byte[] _input;

		public HashRequestImpl(
			PepperCommand pepperCommand,
			SaltCommand saltCommand, byte[] input) {

			_pepperCommand = pepperCommand;
			_saltCommand = saltCommand;
			_input = input;
		}
	}

}
