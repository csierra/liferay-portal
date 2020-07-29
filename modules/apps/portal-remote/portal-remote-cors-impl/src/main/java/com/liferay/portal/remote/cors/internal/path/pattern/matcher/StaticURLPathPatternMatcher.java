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
 * @author Carlos Sierra Andrés
 */
public class StaticURLPathPatternMatcher<T> extends URLPathPatternMatcher<T> {

	/**
	 * If all URL patterns are known before hand, use staticurlPathPatternMapper
	 * over dynamciurlPathPatternMapper for better READ performance of higher CPU
	 * cache localities.
	 *
	 * Limitation: number of URL patterns of exact match and wildcard match
	 * need to not exceed 64. number of URL patterns of extension match need
	 * to not exceed 64.
	 */
	public StaticURLPathPatternMatcher(int longesturlPathPatternSize) {
		if (longesturlPathPatternSize < 1) {
			longesturlPathPatternSize = 64;
		}

		_extensionStaticPathPatternMatcher =
			new ExtensionStaticPathPatternMatcher<>(longesturlPathPatternSize);
		_wildcardStaticPathPatternMatcher =
			new WildcardStaticPathPatternMatcher<>(longesturlPathPatternSize);
	}

	public T getValue(String urlPath) {
		T value = _wildcardStaticPathPatternMatcher.getValue(urlPath);

		if (value != null) {
			return value;
		}

		return _extensionStaticPathPatternMatcher.getValue(urlPath);
	}

	public void insert(String urlPathPattern, T value)
		throws IllegalArgumentException {

		if (isValidExtensionPattern(urlPathPattern)) {
			_extensionStaticPathPatternMatcher.insert(
				urlPathPattern, value, false);
		}
		else {
			_wildcardStaticPathPatternMatcher.insert(
				urlPathPattern, value, true);
		}
	}

	protected static int getFirstSetBitIndex(long bitMask) {
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

	private static final long _ALL_BITS_SET = ~0;

	private static final int _SLASH_INDEX = '/' - ASCII_PRINTABLE_OFFSET;

	private static final int _STAR_INDEX = '*' - ASCII_PRINTABLE_OFFSET;

	private final ExtensionStaticPathPatternMatcher<T>
		_extensionStaticPathPatternMatcher;
	private final WildcardStaticPathPatternMatcher<T>
		_wildcardStaticPathPatternMatcher;

	private abstract static class BaseStaticPathPatternMatcher<T> {

		public BaseStaticPathPatternMatcher(int maxPatternLength) {
			this.maxPatternLength = maxPatternLength;

			trieArray = new long[2][maxPatternLength][ASCII_CHARACTER_RANGE];
		}

		protected int getExactIndex(String urlPath) {
			int row = 0;
			long bitMask = _ALL_BITS_SET;
			int column = 0;

			for (; row < urlPath.length(); ++row) {
				if (row > (maxPatternLength - 1)) {
					bitMask = 0;

					break;
				}

				char character = urlPath.charAt(row);

				column = character - ASCII_PRINTABLE_OFFSET;

				bitMask &= trieArray[0][row][column];

				if (bitMask == 0) {
					break;
				}
			}

			if (bitMask != 0) {
				bitMask &= trieArray[1][row - 1][column];

				if (bitMask != 0) {
					return getFirstSetBitIndex(bitMask);
				}
			}

			return -1;
		}

		protected void insert(String urlPathPattern, T value, boolean forward) {
			if (_count > 63) {
				throw new IllegalArgumentException(
					"Exceeding maximum number of allowed URL patterns");
			}

			int index = getExactIndex(urlPathPattern);

			if (index > -1) {
				values.add(index, value);

				return;
			}

			index = _count++;

			int row = 0;
			int column = 0;
			long bitMask = 1 << index;

			for (; row < urlPathPattern.length(); ++row) {
				char character = urlPathPattern.charAt(row);

				if (!forward) {
					character = urlPathPattern.charAt(
						urlPathPattern.length() - 1 - row);
				}

				column = character - ASCII_PRINTABLE_OFFSET;

				trieArray[0][row][column] |= bitMask;
			}

			trieArray[1][row - 1][column] |= bitMask;

			values.add(index, value);
		}

		protected int maxPatternLength;
		protected final long[][][] trieArray;
		protected List<T> values = new ArrayList<>(Long.SIZE);

		private int _count;

	}

	private static class ExtensionStaticPathPatternMatcher<T>
		extends BaseStaticPathPatternMatcher<T> {

		public ExtensionStaticPathPatternMatcher(int maxPatternLength) {
			super(maxPatternLength);
		}

		public T getValue(String urlPath) {
			int urlPathLength = urlPath.length();
			long currentBitMask = _ALL_BITS_SET;

			for (int row = 0; row < urlPathLength; ++row) {
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
						return values.get(getFirstSetBitIndex(bitMask));
					}

					break;
				}
			}

			return null;
		}

	}

	private static class WildcardStaticPathPatternMatcher<T>
		extends BaseStaticPathPatternMatcher<T> {

		public WildcardStaticPathPatternMatcher(int maxPatternLength) {
			super(maxPatternLength);
		}

		public T getValue(String urlPath) {
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
				if (row > (maxPatternLength - 1)) {
					currentBitMask = 0;

					break;
				}

				char character = urlPath.charAt(row);

				col = character - ASCII_PRINTABLE_OFFSET;

				currentBitMask &= trieArray[0][row][col];

				if (currentBitMask == 0) {
					break;
				}

				if (!onlyExact && (character == '/') &&
					((row + 1) < maxPatternLength)) {

					long bitMask =
						currentBitMask & trieArray[1][row + 1][_STAR_INDEX];

					if (bitMask != 0) {
						bestMatchBitMask = bitMask;
					}
				}
			}

			if (currentBitMask == 0) {
				if (bestMatchBitMask == 0) {
					return null;
				}

				return values.get(getFirstSetBitIndex(bestMatchBitMask));
			}

			if (onlyExact) {
				long bitMask = currentBitMask & trieArray[1][row - 1][col];

				if (bitMask != 0) {
					return values.get(getFirstSetBitIndex(bitMask));
				}

				return null;
			}

			if (!onlyWildcard) {
				long bitMask = currentBitMask & trieArray[1][row - 1][col];

				if (bitMask != 0) {
					return values.get(getFirstSetBitIndex(bitMask));
				}
			}

			long extraBitMask =
				currentBitMask & trieArray[0][row][_SLASH_INDEX];

			extraBitMask &= trieArray[0][row + 1][_STAR_INDEX];
			extraBitMask &= trieArray[1][row + 1][_STAR_INDEX];

			if (extraBitMask != 0) {
				return values.get(getFirstSetBitIndex(extraBitMask));
			}

			return values.get(getFirstSetBitIndex(bestMatchBitMask));
		}

	}

}