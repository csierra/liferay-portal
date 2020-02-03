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
		public static SaltHashRequestBuilder saltCommand(
			SaltCommand saltCommand){

			return null;
		}

		public static PepperHashRequestBuilder pepper(byte[] pepper) { return null;}

		public static HashRequest input(byte[] input) { return null;}
	}

	public interface SaltHashRequestBuilder {
		public HashRequest input(byte[] input);
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

	public static interface PepperCommand {
		public static PepperCommand usePepper(byte[] pepper) {
			return new UsePepperCommand(pepper);
		}

		public static PepperCommand generatePepper(int size) {
			return new GeneratePepperCommand(size);
		}
	}

	public static class UsePepperCommand implements PepperCommand {
		public UsePepperCommand(byte[] pepper) {
		}
	}

	public static class GeneratePepperCommand implements PepperCommand {
		private int _size;

		public GeneratePepperCommand(int size) {
			_size = size;
		}
	}

	public static interface PepperHashRequestBuilder {
		public HashRequest input(byte[] input);

		public SaltHashRequestBuilder saltCommand(SaltCommand saltCommand);
	}
}
