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

package com.liferay.portal.kernel.internal.security.access.control;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Mariano Álvaro Sáiz
 */
public final class AllowedIpsValidatorFactory {

	public static AllowedIpsValidator create(String allowedIpsMask) {
		String[] ipAndMask = StringUtil.split(allowedIpsMask, StringPool.SLASH);

		try {
			if (Validator.isIPv4Address(ipAndMask[0])) {
				return new AllowedIPsv4Validator(
					InetAddress.getByName(ipAndMask[0]), ipAndMask);
			}
			else if (Validator.isIPv6Address(ipAndMask[0])) {
				return new AllowedIPsv6Validator(
					InetAddress.getByName(ipAndMask[0]), ipAndMask);
			}
			else {
				return new AllowedIPsDummyValidator();
			}
		}
		catch (Exception e) {
			_log.error("Invalid configured address: ", e);

			return new AllowedIPsDummyValidator();
		}
	}

	private static final int[] _BYTE = {
		0b00000000, 0b10000000, 0b11000000, 0b11100000, 0b11110000, 0b11111000,
		0b11111100, 0b11111110, 0b11111111
	};

	private static final Log _log = LogFactoryUtil.getLog(
		AllowedIpsValidatorFactory.class);

	private static class AllowedIPsDummyValidator
		implements AllowedIpsValidator {

		public byte[] getEmptyMask() {
			return new byte[0];
		}

		public boolean isAllowedIP(String ipAddress) {
			return false;
		}

		@Override
		public boolean isSameProtocol(byte[] ipAddressBytes) {
			return false;
		}

		public boolean isValidCIDR(int cidr) {
			return false;
		}

		public boolean isValidSubnetMask(String mask) {
			return false;
		}

	}

	private static class AllowedIPsv4Validator extends BaseAllowedIpsValidator {

		public AllowedIPsv4Validator(
				InetAddress inetAddress, String[] ipAndMask)
			throws UnknownHostException {

			super(inetAddress, ipAndMask);
		}

		public byte[] getEmptyMask() {
			return new byte[4];
		}

		public boolean isValidCIDR(int cidr) {
			if ((cidr >= 0) && (cidr <= 32)) {
				return true;
			}

			return false;
		}

		public boolean isValidSubnetMask(String mask) {
			return Validator.isIPv4Address(mask);
		}

	}

	private static class AllowedIPsv6Validator extends BaseAllowedIpsValidator {

		public byte[] getEmptyMask() {
			return new byte[16];
		}

		public boolean isValidCIDR(int cidr) {
			if ((cidr >= 0) && (cidr <= 128)) {
				return true;
			}

			return false;
		}

		public boolean isValidSubnetMask(String mask) {
			return Validator.isIPv6Address(mask);
		}

		protected AllowedIPsv6Validator(
				InetAddress inetAddress, String[] ipAndMask)
			throws UnknownHostException {

			super(inetAddress, ipAndMask);
		}

	}

	private abstract static class BaseAllowedIpsValidator
		implements AllowedIpsValidator {

		@Override
		public boolean isAllowedIP(String ipAddress) {
			InetAddress inetAddress = null;

			try {
				inetAddress = InetAddress.getByName(ipAddress);
			}
			catch (UnknownHostException uhe) {
				return false;
			}

			byte[] inetAddressBytes = inetAddress.getAddress();

			if (!isSameProtocol(inetAddressBytes)) {
				return false;
			}

			if (_mask == null) {
				return _allowedAddress.equals(inetAddress);
			}

			for (int i = 0; i < _mask.length; i++) {
				if ((inetAddressBytes[i] & _mask[i]) !=
						(_allowedAddressBytes[i] & _mask[i])) {

					return false;
				}
			}

			return true;
		}

		@Override
		public boolean isSameProtocol(byte[] ipAddressBytes) {
			if (_allowedAddressBytes.length == ipAddressBytes.length) {
				return true;
			}

			return false;
		}

		protected BaseAllowedIpsValidator(
				InetAddress inetAddress, String[] ipAndMask)
			throws UnknownHostException {

			_allowedAddress = inetAddress;

			_allowedAddressBytes = _allowedAddress.getAddress();

			if (_hasMask(ipAndMask)) {
				String mask = GetterUtil.getString(ipAndMask[1]);

				if (Validator.isNumber(mask)) {
					_mask = _getMaskFromCIDR(mask);
				}
				else {
					_mask = _getMaskFromSubnet(mask);
				}
			}
		}

		private byte[] _getMaskFromCIDR(String mask) {
			int cidr = GetterUtil.getInteger(mask);

			int maskBytes = cidr / 8;

			byte[] bytesMask = getEmptyMask();
			int byteOffset = cidr % 8;

			for (int i = 0; i < maskBytes; i++) {
				bytesMask[i] = (byte)_BYTE[8];
			}

			if (maskBytes < bytesMask.length) {
				bytesMask[maskBytes] = (byte)_BYTE[byteOffset];
			}

			return bytesMask;
		}

		private byte[] _getMaskFromSubnet(String mask)
			throws UnknownHostException {

			InetAddress address = InetAddress.getByName(mask);

			return address.getAddress();
		}

		private boolean _hasMask(String[] ipAndMask) {
			if (ipAndMask.length > 1) {
				return true;
			}

			return false;
		}

		private final InetAddress _allowedAddress;
		private final byte[] _allowedAddressBytes;
		private byte[] _mask;

	}

}