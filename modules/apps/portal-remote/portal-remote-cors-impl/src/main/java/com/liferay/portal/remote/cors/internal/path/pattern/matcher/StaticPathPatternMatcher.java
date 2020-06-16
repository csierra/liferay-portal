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

package com.liferay.portal.remote.cors.internal.path.pattern.matcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur Chan
 */
public class StaticPathPatternMatcher<T> extends PathPatternMatcher<T> {

	/**
	 * If all URL patterns are known before hand, use staticURLPatternMapper over
	 * dynamciURLPatternMapper for better READ performance of higher CPU cache
	 * localities.
	 *
	 * Limitation: number of URL patterns of exact match and wildcard match need to
	 * not exceed 64. number of URL patterns of extension match need to not exceed
	 * 64.
	 */
	public StaticPathPatternMatcher(int longestURLPatternSize) {
		if (longestURLPatternSize < 1) {
			throw new IllegalArgumentException(
				"URL Pattern Size has to be least 1");
		}

		_maxPatternLength = (byte)longestURLPatternSize;

		_exactTrieMap = new long[2][_maxPatternLength][CHARACTER_RANGE];

		_wildcardTrieMap = new long[2][_maxPatternLength][CHARACTER_RANGE];

		_extensionTrieMap = new long[2][_maxPatternLength][CHARACTER_RANGE];

		exactList = new ArrayList<>(_LONG_BITS_SIZE);

		wildcardList = new ArrayList<>(_LONG_BITS_SIZE);

		extensionList = new ArrayList<>(_LONG_BITS_SIZE);

		for (byte i = 0; i < _LONG_BITS_SIZE; ++i) {
			exactList.add(new PatternPackage<>());
			wildcardList.add(new PatternPackage<>());
			extensionList.add(new PatternPackage<>());
		}
	}

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.2
	 *
	 * In the Web application deployment descriptor, the following syntax is used to
	 * define mappings: 1. Wild Card path pattern 1: A string beginning with a ' / '
	 * character and ending with a ' /* ' suffix is used for path mapping. 2. Wild
	 * Card path pattern 2, aka extension matching: A string beginning with a ' *. '
	 * prefix is used as an extension mapping. 3. Special path pattern 1: The empty
	 * string ("") is a special URL pattern that exactly maps to the application's
	 * context root, i.e., requests of the form
	 * http://host:port/'<'context-root'>'/. In this case the path info is ' / ' and
	 * the servlet path and context path is empty string (""). 4. Special path
	 * pattern 2: A string containing only the ' / ' character indicates the
	 * "default" servlet of the application. In this case the servlet path is the
	 * request URI minus the context path and the path info is null. 5. Exact path
	 * pattern: All other strings are used for exact matches only.
	 *
	 * @param urlPattern the pattern of path, used for pattern matching
	 * @param cargo      an non null object associated with urlPattern
	 */
	public void insert(String urlPattern, T cargo)
		throws IllegalArgumentException {

		super.insert(urlPattern, cargo);

		// Wild Card path pattern 1

		if (isValidWildCardPattern(urlPattern)) {
			if (wildcardPatterns > 63) {
				throw new IllegalArgumentException(
					"Exceeding maximum number of allowed URL patterns");
			}

			insert(urlPattern, cargo, 1);
		}

		// Wild Card path pattern 2, aka extension pattern

		else if (isValidExtensionPattern(urlPattern)) {
			if (extensionPatterns > 63) {
				throw new IllegalArgumentException(
					"Exceeding maximum number of allowed URL patterns");
			}

			insert(urlPattern, cargo, 2);
		}

		// Exact pattern

		else {
			if (exactPatterns > 63) {
				throw new IllegalArgumentException(
					"Exceeding maximum number of allowed URL patterns");
			}

			insert(urlPattern, cargo, 0);
		}
	}

	protected static byte getSetBitIndex(long x) {
		if (x == 0) {
			return -1;
		}

		byte n = 63;

		long y = x << 32;

		if (y != 0) {
			n -= 32;
			x = y;
		}

		y = x << 16;

		if (y != 0) {
			n -= 16;
			x = y;
		}

		y = x << 8;

		if (y != 0) {
			n -= 8;
			x = y;
		}

		y = x << 4;

		if (y != 0) {
			n -= 4;
			x = y;
		}

		y = x << 2;

		if (y != 0) {
			n -= 2;
			x = y;
		}

		y = x << 1;

		if (y != 0) {
			n -= 1;
		}

		return n;
	}

	protected byte getExactIndex(String urlPath, int type) {
		long current = _ALL_BITS;

		byte row = 0;

		int col = 0;

		for (; row < urlPath.length(); ++row) {
			if (row > (_maxPatternLength - 1)) {
				current = 0;

				break;
			}

			char character = '/';

			if ((type == 0) || (type == 1)) {
				character = urlPath.charAt(row);
			}
			else {
				character = urlPath.charAt(urlPath.length() - 1 - row);
			}

			col = character - '\0' - PRINTABLE_OFFSET;

			if (type == 0) {
				current &= _exactTrieMap[0][row][col];
			}
			else if (type == 1) {
				current &= _wildcardTrieMap[0][row][col];
			}
			else {
				current &= _extensionTrieMap[0][row][col];
			}

			if (current == 0) {
				break;
			}
		}

		if (current != 0) {
			if (type == 0) {
				current &= _exactTrieMap[1][row - 1][col];
			}
			else if (type == 1) {
				current &= _wildcardTrieMap[1][row - 1][col];
			}
			else {
				current &= _extensionTrieMap[1][row - 1][col];
			}

			if (current != 0) {
				return getSetBitIndex(current);
			}
		}

		return -1;
	}

	@Override
	protected PatternPackage<T> getExactPatternPackage(String urlPath) {
		byte index = getExactIndex(urlPath, 0);

		if (index < 0) {
			return null;
		}

		return exactList.get(index);
	}

	@Override
	protected PatternPackage<T> getExtensionPatternPackage(String urlPath) {
		long current = _ALL_BITS;

		for (byte row = 0; row < urlPath.length(); ++row) {
			if (row > (_maxPatternLength - 1)) {
				break;
			}

			char character = urlPath.charAt(urlPath.length() - 1 - row);

			if (character == '/') {
				break;
			}

			int col = character - '\0' - PRINTABLE_OFFSET;

			current &= _extensionTrieMap[0][row][col];

			if (current == 0) {
				break;
			}

			if ((character == '.') && ((row + 1) < _maxPatternLength)) {
				long extensionPattern =
					current & _extensionTrieMap[1][row + 1][_STAR_INDEX];

				if (extensionPattern != 0) {
					return extensionList.get(getSetBitIndex(extensionPattern));
				}

				break;
			}
		}

		return null;
	}

	@Override
	protected PatternPackage<T> getWildcardPatternPackage(String urlPath) {
		byte row = 0;

		long current = _ALL_BITS;

		long bestMatch = 0;

		// This loop tries to find every wildcard match at
		// every '/' character.
		// Variable current indicates if current character
		// exists as part of a pattern in the matrix.

		for (; row < urlPath.length(); ++row) {
			if (row > (_maxPatternLength - 1)) {
				current = 0;

				break;
			}

			char character = urlPath.charAt(row);

			int col = character - '\0' - PRINTABLE_OFFSET;

			current &= _wildcardTrieMap[0][row][col];

			if (current == 0) {
				row++;

				break;
			}

			if ((character == '/') && ((row + 1) < _maxPatternLength)) {
				long wildcardPattern =
					current & _wildcardTrieMap[1][row + 1][_STAR_INDEX];

				if (wildcardPattern != 0) {
					bestMatch = wildcardPattern;
				}
			}
		}

		// if current is zero, it means trie travesaling
		// did not match till the last character.

		if ((current != 0) && ((row + 1) < _maxPatternLength)) {
			long extra = current & _wildcardTrieMap[0][row][_SLASH_INDEX];

			extra &= _wildcardTrieMap[0][row + 1][_STAR_INDEX];
			extra &= _wildcardTrieMap[1][row + 1][_STAR_INDEX];

			if (extra != 0) {
				bestMatch = extra;
			}
		}

		if (bestMatch == 0) {
			return null;
		}

		return wildcardList.get(getSetBitIndex(bestMatch));
	}

	@Override
	protected List<PatternPackage<T>> getWildcardPatternPackages(
		String urlPath) {

		List<PatternPackage<T>> patternPackages = new ArrayList<>(
			_LONG_BITS_SIZE);

		byte row = 0;

		long current = _ALL_BITS;

		// This loop tries to find every wildcard match at
		// every '/' character.
		// Variable current indicates if current character
		// exists as part of a pattern in the matrix.

		for (; row < urlPath.length(); ++row) {
			if (row > (_maxPatternLength - 1)) {
				current = 0;

				break;
			}

			char character = urlPath.charAt(row);

			int col = character - '\0' - PRINTABLE_OFFSET;

			current &= _wildcardTrieMap[0][row][col];

			if (current == 0) {
				row++;

				break;
			}

			if ((character == '/') && ((row + 1) < _maxPatternLength)) {
				long wildcardPattern =
					current & _wildcardTrieMap[1][row + 1][_STAR_INDEX];

				if (wildcardPattern != 0) {
					patternPackages.add(
						wildcardList.get(getSetBitIndex(wildcardPattern)));
				}
			}
		}

		// if current is zero, it means trie travesaling
		// did not match till the last character.

		if ((current != 0) && ((row + 1) < _maxPatternLength)) {
			long extra = current & _wildcardTrieMap[0][row][_SLASH_INDEX];

			extra &= _wildcardTrieMap[0][row + 1][_STAR_INDEX];
			extra &= _wildcardTrieMap[1][row + 1][_STAR_INDEX];

			if (extra != 0) {
				patternPackages.add(wildcardList.get(getSetBitIndex(extra)));
			}
		}

		return patternPackages;
	}

	protected void insert(String urlPattern, T cargo, int insertType) {
		PatternPackage<T> patternPackage = null;

		byte bitIndex = getExactIndex(urlPattern, insertType);

		if (bitIndex > -1) {

			// Indicating the end of the pattern

			if (insertType == 0) {
				patternPackage = exactList.get(bitIndex);
			}
			else if (insertType == 1) {
				patternPackage = wildcardList.get(bitIndex);
			}
			else {
				patternPackage = extensionList.get(bitIndex);
			}

			patternPackage.set(urlPattern, cargo);

			return;
		}

		if (insertType == 0) {
			bitIndex = exactPatterns++;
		}
		else if (insertType == 1) {
			bitIndex = wildcardPatterns++;
		}
		else {
			bitIndex = extensionPatterns++;
		}

		long bitMask = 1;

		bitMask <<= bitIndex;

		int row = 0;
		int col = 0;

		for (; row < urlPattern.length(); ++row) {
			char character = '\0';

			if ((insertType == 0) || (insertType == 1)) {
				character = urlPattern.charAt(row);
			}
			else {
				character = urlPattern.charAt(urlPattern.length() - 1 - row);
			}

			col = character - '\0' - PRINTABLE_OFFSET;

			if (insertType == 0) {
				_exactTrieMap[0][row][col] |= bitMask;
			}
			else if (insertType == 1) {
				_wildcardTrieMap[0][row][col] |= bitMask;
			}
			else {
				_extensionTrieMap[0][row][col] |= bitMask;
			}
		}

		// Indicating the end of the pattern

		if (insertType == 0) {
			_exactTrieMap[1][row - 1][col] |= bitMask;
			patternPackage = exactList.get(bitIndex);
		}
		else if (insertType == 1) {
			_wildcardTrieMap[1][row - 1][col] |= bitMask;
			patternPackage = wildcardList.get(bitIndex);
		}
		else {
			_extensionTrieMap[1][row - 1][col] |= bitMask;
			patternPackage = extensionList.get(bitIndex);
		}

		patternPackage.set(urlPattern, cargo);
	}

	protected List<PatternPackage<T>> exactList;
	protected byte exactPatterns;
	protected List<PatternPackage<T>> extensionList;
	protected byte extensionPatterns;
	protected List<PatternPackage<T>> wildcardList;
	protected byte wildcardPatterns;

	private static final long _ALL_BITS = ~0;

	private static final byte _LONG_BITS_SIZE = 64;

	private static final byte _SLASH_INDEX = 47 - PRINTABLE_OFFSET;

	private static final byte _STAR_INDEX = 42 - PRINTABLE_OFFSET;

	private final long[][][] _exactTrieMap;
	private final long[][][] _extensionTrieMap;

	/**
	 * Length of longest URL pattern
	 */
	private final byte _maxPatternLength;

	private final long[][][] _wildcardTrieMap;

}