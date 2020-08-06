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

package com.liferay.portal.remote.cors.internal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur Chan
 * @author Carlos Sierra Andrés
 */
public class StaticURLtoCORSSupportMapper extends BaseURLtoCORSSupportMapper {

	public StaticURLtoCORSSupportMapper(int maxURLPatternLength) {
		if (maxURLPatternLength < 1) {
			_maxURLPatternLength = 64;
		}
		else {
			_maxURLPatternLength = maxURLPatternLength;
		}

		_extensionTrieMatrix =
			new long[2][maxURLPatternLength][ASCII_CHARACTER_RANGE];
		_wilcardTrieMatrix =
			new long[2][maxURLPatternLength][ASCII_CHARACTER_RANGE];
	}

	@Override
	protected CORSSupport getExtensionCORSSupport(String urlPath) {
		int urlPathLength = urlPath.length();
		long currentBitMask = _ALL_BITS_SET;

		for (int row = 0; row < urlPathLength; ++row) {
			if (row > (_maxURLPatternLength - 1)) {
				break;
			}

			char character = urlPath.charAt(urlPathLength - 1 - row);

			if (character == '/') {
				break;
			}

			int column = character - ASCII_PRINTABLE_OFFSET;

			currentBitMask &= _extensionTrieMatrix[0][row][column];

			if (currentBitMask == 0) {
				break;
			}

			if ((character == '.') && ((row + 1) < _maxURLPatternLength)) {
				long bitMask =
					currentBitMask &
					_extensionTrieMatrix[1][row + 1][_INDEX_STAR];

				if (bitMask != 0) {
					return _corsSupports.get(_getFirstSetBitIndex(bitMask));
				}

				break;
			}
		}

		return null;
	}

	@Override
	protected CORSSupport getWildcardCORSSupport(String urlPath) {
		boolean onlyExact = false;
		boolean onlyWildcard = false;

		if (urlPath.charAt(0) != '/') {
			onlyExact = true;
		}
		else if ((urlPath.length() > 1) &&
				 (urlPath.charAt(urlPath.length() - 2) == '/') &&
				 (urlPath.charAt(urlPath.length() - 1) == '*')) {

			onlyWildcard = true;
		}

		int row = 0;
		int col = 0;
		long currentBitMask = _ALL_BITS_SET;
		long bestMatchBitMask = 0;

		for (; row < urlPath.length(); ++row) {
			if (row > (_maxURLPatternLength - 1)) {
				currentBitMask = 0;

				break;
			}

			char character = urlPath.charAt(row);

			col = character - ASCII_PRINTABLE_OFFSET;

			currentBitMask &= _wilcardTrieMatrix[0][row][col];

			if (currentBitMask == 0) {
				break;
			}

			if (!onlyExact && (character == '/') &&
				((row + 1) < _maxURLPatternLength)) {

				long bitMask =
					currentBitMask &
					_wilcardTrieMatrix[1][row + 1][_INDEX_STAR];

				if (bitMask != 0) {
					bestMatchBitMask = bitMask;
				}
			}
		}

		if (currentBitMask == 0) {
			if (bestMatchBitMask == 0) {
				return null;
			}

			return _corsSupports.get(_getFirstSetBitIndex(bestMatchBitMask));
		}

		if (onlyExact) {
			long bitMask = currentBitMask & _wilcardTrieMatrix[1][row - 1][col];

			if (bitMask != 0) {
				return _corsSupports.get(_getFirstSetBitIndex(bitMask));
			}

			return null;
		}

		if (!onlyWildcard) {
			long bitMask = currentBitMask & _wilcardTrieMatrix[1][row - 1][col];

			if (bitMask != 0) {
				return _corsSupports.get(_getFirstSetBitIndex(bitMask));
			}
		}

		long extraBitMask =
			currentBitMask & _wilcardTrieMatrix[0][row][_INDEX_SLASH];

		extraBitMask &= _wilcardTrieMatrix[0][row + 1][_INDEX_STAR];
		extraBitMask &= _wilcardTrieMatrix[1][row + 1][_INDEX_STAR];

		if (extraBitMask != 0) {
			return _corsSupports.get(_getFirstSetBitIndex(extraBitMask));
		}

		return _corsSupports.get(_getFirstSetBitIndex(bestMatchBitMask));
	}

	@Override
	protected void put(
		String urlPattern, CORSSupport corsSupport, boolean forward) {

		if (_storedURLPatterns > 63) {
			throw new IllegalArgumentException(
				"Exceeding maximum number of allowed URL patterns");
		}

		long[][][] trieMatrix = null;

		if (forward) {
			trieMatrix = _wilcardTrieMatrix;
		}
		else {
			trieMatrix = _extensionTrieMatrix;
		}

		int index = _getExactIndex(urlPattern, trieMatrix);

		if (index > -1) {
			_corsSupports.add(index, corsSupport);

			return;
		}

		index = _storedURLPatterns++;

		int row = 0;
		int column = 0;
		long bitMask = 1 << index;

		for (; row < urlPattern.length(); ++row) {
			char character = urlPattern.charAt(row);

			if (!forward) {
				character = urlPattern.charAt(urlPattern.length() - 1 - row);
			}

			column = character - ASCII_PRINTABLE_OFFSET;

			trieMatrix[0][row][column] |= bitMask;
		}

		trieMatrix[1][row - 1][column] |= bitMask;

		_corsSupports.add(index, corsSupport);
	}

	private static int _getFirstSetBitIndex(long bitMask) {
		int firstSetBitIndex = -1;

		if (bitMask == 0) {
			return firstSetBitIndex;
		}

		firstSetBitIndex = 63;

		long currentBitMask = bitMask << 32;

		if (currentBitMask != 0) {
			firstSetBitIndex -= 32;
			bitMask = currentBitMask;
		}

		currentBitMask = bitMask << 16;

		if (currentBitMask != 0) {
			firstSetBitIndex -= 16;
			bitMask = currentBitMask;
		}

		currentBitMask = bitMask << 8;

		if (currentBitMask != 0) {
			firstSetBitIndex -= 8;
			bitMask = currentBitMask;
		}

		currentBitMask = bitMask << 4;

		if (currentBitMask != 0) {
			firstSetBitIndex -= 4;
			bitMask = currentBitMask;
		}

		currentBitMask = bitMask << 2;

		if (currentBitMask != 0) {
			firstSetBitIndex -= 2;
			bitMask = currentBitMask;
		}

		currentBitMask = bitMask << 1;

		if (currentBitMask != 0) {
			firstSetBitIndex -= 1;
		}

		return firstSetBitIndex;
	}

	private int _getExactIndex(String urlPath, long[][][] trieMatrix) {
		int row = 0;
		long bitMask = _ALL_BITS_SET;
		int column = 0;

		for (; row < urlPath.length(); ++row) {
			if (row > (_maxURLPatternLength - 1)) {
				bitMask = 0;

				break;
			}

			char character = urlPath.charAt(row);

			column = character - ASCII_PRINTABLE_OFFSET;

			bitMask &= trieMatrix[0][row][column];

			if (bitMask == 0) {
				break;
			}
		}

		if (bitMask != 0) {
			bitMask &= trieMatrix[1][row - 1][column];

			if (bitMask != 0) {
				return _getFirstSetBitIndex(bitMask);
			}
		}

		return -1;
	}

	private static final long _ALL_BITS_SET = ~0;

	private static final int _INDEX_SLASH = '/' - ASCII_PRINTABLE_OFFSET;

	private static final int _INDEX_STAR = '*' - ASCII_PRINTABLE_OFFSET;

	private List<CORSSupport> _corsSupports = new ArrayList<>(Long.SIZE);
	private final long[][][] _extensionTrieMatrix;
	private final int _maxURLPatternLength;
	private int _storedURLPatterns;
	private final long[][][] _wilcardTrieMatrix;

}