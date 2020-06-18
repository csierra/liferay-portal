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

		_exactPathPatternMatcher = new ExactPathPatternMatcher<>(
			(byte)longestURLPatternSize);
		_extensionPathPatternMatcher = new ExtensionPathPatternMatcher<>(
			(byte)longestURLPatternSize);
		_wildcardPathPatternMatcher = new WildcardPathPatternMatcher<>(
			(byte)longestURLPatternSize);
	}

	public PatternTuple<T> getPatternTuple(String urlPath) {
		PatternTuple<T> patternTuple = _exactPathPatternMatcher.getPatternTuple(
			urlPath);

		if (patternTuple != null) {
			return patternTuple;
		}

		patternTuple = _wildcardPathPatternMatcher.getPatternTuple(urlPath);

		if (patternTuple != null) {
			return patternTuple;
		}

		return _extensionPathPatternMatcher.getPatternTuple(urlPath);
	}

	@Override
	public List<PatternTuple<T>> getPatternTuples(String urlPath) {
		List<PatternTuple<T>> patternTuples = getWilcardPatternTuples(urlPath);

		PatternTuple<T> patternTuple = _exactPathPatternMatcher.getPatternTuple(
			urlPath);

		if (patternTuple != null) {
			patternTuples.add(patternTuple);
		}

		patternTuple = _extensionPathPatternMatcher.getPatternTuple(urlPath);

		if (patternTuple != null) {
			patternTuples.add(patternTuple);
		}

		return patternTuples;
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

		if (isValidWildCardPattern(urlPattern)) {
			_wildcardPathPatternMatcher.insert(urlPattern, value);
		}
		else if (isValidExtensionPattern(urlPattern)) {
			_extensionPathPatternMatcher.insert(urlPattern, value);
		}
		else {
			_exactPathPatternMatcher.insert(urlPattern, value);
		}
	}

	protected static byte getFirstSetBitIndex(long x) {
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

	protected List<PatternTuple<T>> getWilcardPatternTuples(String urlPath) {
		List<PatternTuple<T>> patternTuples = new ArrayList<>(
			(byte)Long.SIZE + 2);

		long wildcardMatchesBitMask =
			_wildcardPathPatternMatcher.getWildcardMatchesBitMask(urlPath);

		while (wildcardMatchesBitMask != 0) {
			patternTuples.add(
				_wildcardPathPatternMatcher.patternTuples.get(
					getFirstSetBitIndex(wildcardMatchesBitMask)));

			wildcardMatchesBitMask &= wildcardMatchesBitMask - 1;
		}

		return patternTuples;
	}

	@Override
	protected PatternTuple<T> getWildcardPatternTuple(String urlPath) {
		return _wildcardPathPatternMatcher.getPatternTuple(urlPath);
	}

	private static final long _ALL_BITS_SET = ~0;

	private static final byte _SLASH_INDEX =
		(byte)(Character.getNumericValue('/') - ASCII_PRINTABLE_OFFSET);

	private static final byte _STAR_INDEX =
		(byte)(Character.getNumericValue('*') - ASCII_PRINTABLE_OFFSET);

	private final ExactPathPatternMatcher<T> _exactPathPatternMatcher;
	private final ExtensionPathPatternMatcher<T> _extensionPathPatternMatcher;
	private final WildcardPathPatternMatcher<T> _wildcardPathPatternMatcher;

	private abstract static class BasePatternMatcher<T> {

		public BasePatternMatcher(byte maxPatternLength) {
			this.maxPatternLength = maxPatternLength;

			trieArray = new long[2][maxPatternLength][ASCII_CHARACTER_RANGE];
		}

		protected byte getExactIndex(String urlPath) {

			byte row = 0;
			long current = _ALL_BITS_SET;
			int column = 0;

			for (; row < urlPath.length(); ++row) {
				if (row > (maxPatternLength - 1)) {
					current = 0;

					break;
				}

				char character = urlPath.charAt(row);

				column = character - ASCII_PRINTABLE_OFFSET;

				current &= trieArray[0][row][column];

				if (current == 0) {
					break;
				}
			}

			if (current != 0) {
				current &= trieArray[1][row - 1][column];

				if (current != 0) {
					return getFirstSetBitIndex(current);
				}
			}

			return -1;
		}

		protected void insert(String urlPattern, T value) {
			if (_count > 63) {
				throw new IllegalArgumentException(
					"Exceeding maximum number of allowed URL patterns");
			}

			byte index = getExactIndex(urlPattern);

			if (index > -1) {

				// Indicating the end of the pattern

				patternTuples.add(index, new PatternTuple<>(urlPattern, value));

				return;
			}

			index = _count++;

			byte row = 0;
			int column = 0;
			long bitMask = 1 << index;

			for (; row < urlPattern.length(); ++row) {
				char character = urlPattern.charAt(row);

				column = character - ASCII_PRINTABLE_OFFSET;

				trieArray[0][row][column] |= bitMask;
			}

			// Indicating the end of the pattern

			PatternTuple<T> patternTuple = new PatternTuple<>(
				urlPattern, value);

			trieArray[1][row - 1][column] |= bitMask;

			patternTuples.add(index, patternTuple);
		}

		protected byte maxPatternLength;
		protected List<PatternTuple<T>> patternTuples = new ArrayList<>(
			(byte)Long.SIZE);
		protected final long[][][] trieArray;

		private byte _count;

	}

	private static class ExactPathPatternMatcher<T>
		extends BasePatternMatcher<T> {

		public ExactPathPatternMatcher(byte maxPatternLength) {
			super(maxPatternLength);
		}

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
			super(maxPatternLength);
		}

		public PatternTuple<T> getPatternTuple(String urlPath) {
			int urlPathLength = urlPath.length();
			long currentBitMask = _ALL_BITS_SET;

			for (byte row = 0; row < urlPathLength; ++row) {
				if (row > (maxPatternLength - 1)) {
					break;
				}

				char character = urlPath.charAt(urlPathLength - 1 - row);

				if (character == '/') {
					break;
				}

				int column = character - ASCII_PRINTABLE_OFFSET;

				currentBitMask &= trieArray[0][row][column];

				if (currentBitMask == 0) {
					break;
				}

				if ((character == '.') && ((row + 1) < maxPatternLength)) {
					long bitMask =
						currentBitMask & trieArray[1][row + 1][_STAR_INDEX];

					if (bitMask != 0) {
						return patternTuples.get(getFirstSetBitIndex(bitMask));
					}

					break;
				}
			}

			return null;
		}

		@Override
		protected void insert(String urlPattern, T value) {
			StringBuilder stringBuilder = new StringBuilder(urlPattern);

			stringBuilder.reverse();

			super.insert(stringBuilder.toString(), value);
		}

	}

	private static class WildcardPathPatternMatcher<T>
		extends BasePatternMatcher<T> {

		public WildcardPathPatternMatcher(byte maxPatternLength) {
			super(maxPatternLength);
		}

		public PatternTuple<T> getPatternTuple(String urlPath) {
			byte row = 0;
			long currentBitMask = _ALL_BITS_SET;
			long bestMatchBitMask = 0;

			// This loop tries to find every wildcard match at
			// every '/' character.
			// Variable current indicates if current character
			// exists as part of a pattern in the matrix.

			for (; row < urlPath.length(); ++row) {
				if (row > (maxPatternLength - 1)) {
					currentBitMask = 0;

					break;
				}

				char character = urlPath.charAt(row);

				int column = character - ASCII_PRINTABLE_OFFSET;

				currentBitMask &= trieArray[0][row][column];

				if (currentBitMask == 0) {
					row++;

					break;
				}

				if ((character == '/') && ((row + 1) < maxPatternLength)) {
					long bitMask =
						currentBitMask & trieArray[1][row + 1][_STAR_INDEX];

					if (bitMask != 0) {
						bestMatchBitMask = bitMask;
					}
				}
			}

			// if current is zero, it means trie travesaling
			// did not match till the last character.

			if ((currentBitMask != 0) && ((row + 1) < maxPatternLength)) {
				long extraBitMask =
					currentBitMask & trieArray[0][row][_SLASH_INDEX];

				extraBitMask &= trieArray[0][row + 1][_STAR_INDEX];
				extraBitMask &= trieArray[1][row + 1][_STAR_INDEX];

				if (extraBitMask != 0) {
					bestMatchBitMask = extraBitMask;
				}
			}

			if (bestMatchBitMask == 0) {
				return null;
			}

			return patternTuples.get(getFirstSetBitIndex(bestMatchBitMask));
		}

		public long getWildcardMatchesBitMask(String urlPath) {
			byte row = 0;
			long current = _ALL_BITS_SET;
			long bitMask = 0;

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

				int column = character - ASCII_PRINTABLE_OFFSET;

				current &= trieArray[0][row][column];

				if (current == 0) {
					row++;

					break;
				}

				if ((character == '/') && ((row + 1) < maxPatternLength)) {
					long nextBitMask =
						current & trieArray[1][row + 1][_STAR_INDEX];

					if (nextBitMask != 0) {
						bitMask |= nextBitMask;
					}
				}
			}

			// if current is zero, it means trie travesaling
			// did not match till the last character.

			if ((current != 0) && ((row + 1) < maxPatternLength)) {
				long extraBitMask = current & trieArray[0][row][_SLASH_INDEX];

				extraBitMask &= trieArray[0][row + 1][_STAR_INDEX];
				extraBitMask &= trieArray[1][row + 1][_STAR_INDEX];

				if (extraBitMask != 0) {
					bitMask |= extraBitMask;
				}
			}

			return bitMask;
		}

	}

}