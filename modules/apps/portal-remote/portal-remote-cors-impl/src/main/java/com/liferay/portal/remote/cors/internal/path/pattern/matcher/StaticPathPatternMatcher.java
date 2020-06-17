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

		_exactPathPatternMatcher = new ExactPathPatternMatcher<>(
			_maxPatternLength);
		_extensionPathPatternMatcher = new ExtensionPathPatternMatcher<>(
			_maxPatternLength);
		_wildcardPathPatternMatcher = new WildcardPathPatternMatcher<>(
			_maxPatternLength);
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
	 * @param value      an non null object associated with urlPattern
	 */
	public void insert(String urlPattern, T value)
		throws IllegalArgumentException {

		// Wild Card path pattern 1

		if (isValidWildCardPattern(urlPattern)) {
			if (_wildcardPathPatternMatcher.count > 63) {
				throw new IllegalArgumentException(
					"Exceeding maximum number of allowed URL patterns");
			}

			_wildcardPathPatternMatcher.insert(urlPattern, value);
		}

		// Wild Card path pattern 2, aka extension pattern

		else if (isValidExtensionPattern(urlPattern)) {
			if (_extensionPathPatternMatcher.count > 63) {
				throw new IllegalArgumentException(
					"Exceeding maximum number of allowed URL patterns");
			}

			_extensionPathPatternMatcher.insert(urlPattern, value);
		}

		// Exact pattern

		else {
			if (_exactPathPatternMatcher.count > 63) {
				throw new IllegalArgumentException(
					"Exceeding maximum number of allowed URL patterns");
			}

			_exactPathPatternMatcher.insert(urlPattern, value);
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

	@Override
	protected PatternTuple<T> getExactPatternTuple(String urlPath) {
		return _exactPathPatternMatcher.getPatternTuple(urlPath);
	}

	@Override
	protected PatternTuple<T> getExtensionPatternTuple(String urlPath) {
		return _extensionPathPatternMatcher.getPatternTuple(urlPath);
	}

	@Override
	protected PatternTuple<T> getWildcardPatternTuple(String urlPath) {
		return _wildcardPathPatternMatcher.getPatternTuple(urlPath);
	}

	@Override
	protected List<PatternTuple<T>> getWildcardPatternTuples(String urlPath) {
		return _wildcardPathPatternMatcher.getPatternTuples(urlPath);
	}

	private static final long _ALL_BITS = ~0;

	private static final byte _LONG_BITS_SIZE = 64;

	private static final byte _SLASH_INDEX = 47 - PRINTABLE_OFFSET;

	private static final byte _STAR_INDEX = 42 - PRINTABLE_OFFSET;

	private final ExactPathPatternMatcher<T> _exactPathPatternMatcher;
	private final ExtensionPathPatternMatcher<T> _extensionPathPatternMatcher;

	/**
	 * Length of longest URL pattern
	 */
	private final byte _maxPatternLength;

	private final WildcardPathPatternMatcher<T> _wildcardPathPatternMatcher;

	private abstract static class BasePatternMatcher<T> {

		public BasePatternMatcher(byte maxPatternLength, boolean invertIndex) {
			this.maxPatternLength = maxPatternLength;
			this.invertIndex = invertIndex;
			trieMap = new long[2][maxPatternLength][CHARACTER_RANGE];
		}

		public abstract PatternTuple<T> getPatternTuple(String urlPath);

		public byte count;
		public boolean invertIndex;
		public List<PatternTuple<T>> patternTuples = new ArrayList<>(
			_LONG_BITS_SIZE);
		public final long[][][] trieMap;

		protected byte getExactIndex(String urlPath) {
			long current = _ALL_BITS;

			byte row = 0;

			int col = 0;

			for (; row < urlPath.length(); ++row) {
				if (row > (maxPatternLength - 1)) {
					current = 0;

					break;
				}

				char character = '/';

				if (invertIndex) {
					character = urlPath.charAt(urlPath.length() - 1 - row);
				}
				else {
					character = urlPath.charAt(row);
				}

				col = character - PRINTABLE_OFFSET;

				current &= trieMap[0][row][col];

				if (current == 0) {
					break;
				}
			}

			if (current != 0) {
				current &= trieMap[1][row - 1][col];

				if (current != 0) {
					return getSetBitIndex(current);
				}
			}

			return -1;
		}

		protected void insert(String urlPattern, T value) {
			byte bitIndex = getExactIndex(urlPattern);

			if (bitIndex > -1) {

				// Indicating the end of the pattern

				patternTuples.add(
					bitIndex, new PatternTuple<>(urlPattern, value));

				return;
			}

			bitIndex = count++;

			long bitMask = 1;

			bitMask <<= bitIndex;

			int row = 0;
			int col = 0;

			for (; row < urlPattern.length(); ++row) {
				char character = '\0';

				if (invertIndex) {
					character = urlPattern.charAt(
						urlPattern.length() - 1 - row);
				}
				else {
					character = urlPattern.charAt(row);
				}

				col = character - PRINTABLE_OFFSET;

				trieMap[0][row][col] |= bitMask;
			}

			// Indicating the end of the pattern

			PatternTuple<T> patternTuple = new PatternTuple<>(
				urlPattern, value);

			trieMap[1][row - 1][col] |= bitMask;

			patternTuples.add(bitIndex, patternTuple);
		}

		protected byte maxPatternLength;

	}

	private static class ExactPathPatternMatcher<T>
		extends BasePatternMatcher<T> {

		public ExactPathPatternMatcher(byte maxPatternLength) {
			super(maxPatternLength, false);
		}

		@Override
		public PatternTuple<T> getPatternTuple(String urlPath) {
			byte index = getExactIndex(urlPath);

			if (index < 0) {
				return null;
			}

			return patternTuples.get(index);
		}

	}

	private static class ExtensionPathPatternMatcher<T>
		extends BasePatternMatcher<T> {

		public ExtensionPathPatternMatcher(byte maxPatternLength) {
			super(maxPatternLength, true);
		}

		@Override
		public PatternTuple<T> getPatternTuple(String urlPath) {
			long current = _ALL_BITS;

			for (byte row = 0; row < urlPath.length(); ++row) {
				if (row > (maxPatternLength - 1)) {
					break;
				}

				char character = urlPath.charAt(urlPath.length() - 1 - row);

				if (character == '/') {
					break;
				}

				int col = character - PRINTABLE_OFFSET;

				current &= trieMap[0][row][col];

				if (current == 0) {
					break;
				}

				if ((character == '.') && ((row + 1) < maxPatternLength)) {
					long extensionPattern =
						current & trieMap[1][row + 1][_STAR_INDEX];

					if (extensionPattern != 0) {
						return patternTuples.get(
							getSetBitIndex(extensionPattern));
					}

					break;
				}
			}

			return null;
		}

	}

	private static class WildcardPathPatternMatcher<T>
		extends BasePatternMatcher<T> {

		public WildcardPathPatternMatcher(byte maxPatternLength) {
			super(maxPatternLength, false);
		}

		@Override
		public PatternTuple<T> getPatternTuple(String urlPath) {
			byte row = 0;

			long current = _ALL_BITS;

			long bestMatch = 0;

			// This loop tries to find every wildcard match at
			// every '/' character.
			// Variable current indicates if current character
			// exists as part of a pattern in the matrix.

			for (; row < urlPath.length(); ++row) {
				if (row > (maxPatternLength - 1)) {
					current = 0;

					break;
				}

				char character = urlPath.charAt(row);

				int col = character - PRINTABLE_OFFSET;

				current &= trieMap[0][row][col];

				if (current == 0) {
					row++;

					break;
				}

				if ((character == '/') && ((row + 1) < maxPatternLength)) {
					long wildcardPattern =
						current & trieMap[1][row + 1][_STAR_INDEX];

					if (wildcardPattern != 0) {
						bestMatch = wildcardPattern;
					}
				}
			}

			// if current is zero, it means trie travesaling
			// did not match till the last character.

			if ((current != 0) && ((row + 1) < maxPatternLength)) {
				long extra = current & trieMap[0][row][_SLASH_INDEX];

				extra &= trieMap[0][row + 1][_STAR_INDEX];
				extra &= trieMap[1][row + 1][_STAR_INDEX];

				if (extra != 0) {
					bestMatch = extra;
				}
			}

			if (bestMatch == 0) {
				return null;
			}

			return patternTuples.get(getSetBitIndex(bestMatch));
		}

		public List<PatternTuple<T>> getPatternTuples(String urlPath) {
			List<PatternTuple<T>> patternTuples = new ArrayList<>(
				_LONG_BITS_SIZE);

			byte row = 0;

			long current = _ALL_BITS;

			// This loop tries to find every wildcard match at
			// every '/' character.
			// Variable current indicates if current character
			// exists as part of a pattern in the matrix.

			for (; row < urlPath.length(); ++row) {
				if (row > (maxPatternLength - 1)) {
					current = 0;

					break;
				}

				char character = urlPath.charAt(row);

				int col = character - PRINTABLE_OFFSET;

				current &= trieMap[0][row][col];

				if (current == 0) {
					row++;

					break;
				}

				if ((character == '/') && ((row + 1) < maxPatternLength)) {
					long wildcardPattern =
						current & trieMap[1][row + 1][_STAR_INDEX];

					if (wildcardPattern != 0) {
						patternTuples.add(
							patternTuples.get(getSetBitIndex(wildcardPattern)));
					}
				}
			}

			// if current is zero, it means trie travesaling
			// did not match till the last character.

			if ((current != 0) && ((row + 1) < maxPatternLength)) {
				long extra = current & trieMap[0][row][_SLASH_INDEX];

				extra &= trieMap[0][row + 1][_STAR_INDEX];
				extra &= trieMap[1][row + 1][_STAR_INDEX];

				if (extra != 0) {
					patternTuples.add(patternTuples.get(getSetBitIndex(extra)));
				}
			}

			return patternTuples;
		}

	}

}