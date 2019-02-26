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
 * @author Carlos Sierra
 */
public final class AllowedIPAddressesValidatorFactory {

	public static AllowedIPAddressesValidator create(String filter) {
		String[] ipAddressAndNetmask = StringUtil.split(
			filter, StringPool.SLASH);

		try {
			if (Validator.isIPv4Address(ipAddressAndNetmask[0])) {
				return new AllowedIPv4AddressesValidator(
					InetAddress.getByName(ipAddressAndNetmask[0]),
					ipAddressAndNetmask);
			}
			else if (Validator.isIPv6Address(ipAddressAndNetmask[0])) {
				return new AllowedIPv6AddressesValidator(
					InetAddress.getByName(ipAddressAndNetmask[0]),
					ipAddressAndNetmask);
			}
			else {
				return new AllowedIPAddressesDummyValidator();
			}
		}
		catch (Exception e) {
			_log.error("Invalid configured address: ", e);

			return new AllowedIPAddressesDummyValidator();
		}
	}

	private static final int[] _BYTE = {
		0b00000000, 0b10000000, 0b11000000, 0b11100000, 0b11110000, 0b11111000,
		0b11111100, 0b11111110, 0b11111111
	};

	private static final Log _log = LogFactoryUtil.getLog(
		AllowedIPAddressesValidatorFactory.class);

	private static class AllowedIPAddressesDummyValidator
		implements AllowedIPAddressesValidator {

		@Override
		public boolean isAllowedIPAddress(String ipAddress) {
			return false;
		}

	}

	private static class AllowedIPv4AddressesValidator
		extends BaseAllowedIPAddressesValidator {

		public AllowedIPv4AddressesValidator(
				InetAddress inetAddress, String[] ipAddressAndNetmask)
			throws UnknownHostException {

			super(inetAddress, ipAddressAndNetmask);
		}

		@Override
		public byte[] getEmptyNetmask() {
			return new byte[4];
		}

		@Override
		public boolean isValidCIDRNetmask(int cidrNetmask) {
			if ((cidrNetmask >= 0) && (cidrNetmask <= 32)) {
				return true;
			}

			return false;
		}

		@Override
		public boolean isValidDotNotationNetmask(String dotNotationNetmask) {
			return Validator.isIPv4Address(dotNotationNetmask);
		}

	}

	private static class AllowedIPv6AddressesValidator
		extends BaseAllowedIPAddressesValidator {

		@Override
		public byte[] getEmptyNetmask() {
			return new byte[16];
		}

		@Override
		public boolean isValidCIDRNetmask(int cidrNetmask) {
			if ((cidrNetmask >= 0) && (cidrNetmask <= 128)) {
				return true;
			}

			return false;
		}

		@Override
		public boolean isValidDotNotationNetmask(String dotNotationNetmask) {
			return Validator.isIPv6Address(dotNotationNetmask);
		}

		protected AllowedIPv6AddressesValidator(
				InetAddress inetAddress, String[] ipAddressAndNetmask)
			throws UnknownHostException {

			super(inetAddress, ipAddressAndNetmask);
		}

	}

	private abstract static class BaseAllowedIPAddressesValidator
		implements AllowedIPAddressesValidator {

		@Override
		public boolean isAllowedIPAddress(String ipAddress) {
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

			if (_netmask == null) {
				return _allowedIpAddress.equals(inetAddress);
			}

			for (int i = 0; i < _netmask.length; i++) {
				if ((inetAddressBytes[i] & _netmask[i]) !=
						(_allowedIpAddressBytes[i] & _netmask[i])) {

					return false;
				}
			}

			return true;
		}

		protected abstract byte[] getEmptyNetmask();

		protected boolean isSameProtocol(byte[] ipAddressBytes) {
			if (_allowedIpAddressBytes.length == ipAddressBytes.length) {
				return true;
			}

			return false;
		}

		protected abstract boolean isValidCIDRNetmask(int cidrNetmask);

		protected abstract boolean isValidDotNotationNetmask(
			String dotNotationNetmask);

		private BaseAllowedIPAddressesValidator(
				InetAddress inetAddress, String[] ipAddressAndNetmask)
			throws UnknownHostException {

			_allowedIpAddress = inetAddress;

			_allowedIpAddressBytes = _allowedIpAddress.getAddress();

			if (_hasNetmaskFilter(ipAddressAndNetmask)) {
				String maskFilter = GetterUtil.getString(
					ipAddressAndNetmask[1]);

				if (Validator.isNumber(maskFilter)) {
					_netmask = _getNetmaskFromCIDR(maskFilter);
				}
				else {
					_netmask = _getNetmaskFromDotNotation(maskFilter);
				}
			}
		}

		private byte[] _getNetmaskFromCIDR(String maskFilter) {
			int cidr = GetterUtil.getInteger(maskFilter);

			int netmaskBytes = cidr / 8;

			byte[] bytesNetmask = getEmptyNetmask();
			int byteOffset = cidr % 8;

			for (int i = 0; i < netmaskBytes; i++) {
				bytesNetmask[i] = (byte)_BYTE[8];
			}

			if (netmaskBytes < bytesNetmask.length) {
				bytesNetmask[netmaskBytes] = (byte)_BYTE[byteOffset];
			}

			return bytesNetmask;
		}

		private byte[] _getNetmaskFromDotNotation(String maskFilter)
			throws UnknownHostException {

			InetAddress address = InetAddress.getByName(maskFilter);

			return address.getAddress();
		}

		private boolean _hasNetmaskFilter(String[] ipAddressAndNetmask) {
			if (ipAddressAndNetmask.length > 1) {
				return true;
			}

			return false;
		}

		private final InetAddress _allowedIpAddress;
		private final byte[] _allowedIpAddressBytes;
		private byte[] _netmask;

	}

}