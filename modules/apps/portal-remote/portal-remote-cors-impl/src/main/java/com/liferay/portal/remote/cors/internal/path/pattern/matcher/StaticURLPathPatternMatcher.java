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
	 * If all URL patterns are known before hand, use staticpathPatternMapper
	 * over dynamcipathPatternMapper for better READ performance of higher CPU
	 * cache localities.
	 *
	 * Limitation: number of URL patterns of exact match and wildcard match
	 * need to not exceed 64. number of URL patterns of extension match need
	 * to not exceed 64.
	 */
	public StaticURLPathPatternMatcher(int longestpathPatternSize) {
		if (longestpathPatternSize < 1) {
			longestpathPatternSize = 64;
		}

		_extensionStaticPathPatternMatcher =
			new ExtensionStaticPathPatternMatcher<>(longestpathPatternSize);
		_wildcardStaticPathPatternMatcher =
			new WildcardStaticPathPatternMatcher<>(longestpathPatternSize);
	}

	public PatternTuple<T> getPatternTuple(String path) {
		PatternTuple<T> patternTuple =
			_wildcardStaticPathPatternMatcher.getPatternTuple(path);

		if (patternTuple != null) {
			return patternTuple;
		}

		return _extensionStaticPathPatternMatcher.getPatternTuple(path);
	}

	@Override
	public List<PatternTuple<T>> getPatternTuples(String path) {
		List<PatternTuple<T>> patternTuples =
			_wildcardStaticPathPatternMatcher.getPatternTuples(path);

		PatternTuple<T> patternTuple =
			_extensionStaticPathPatternMatcher.getPatternTuple(path);

		if (patternTuple != null) {
			patternTuples.add(patternTuple);
		}

		return patternTuples;
	}

	public void insert(String pathPattern, T value)
		throws IllegalArgumentException {

		if (isValidWildCardPattern(pathPattern)) {
			_wildcardStaticPathPatternMatcher.insert(pathPattern, value, true);
		}
		else if (isValidExtensionPattern(pathPattern)) {
			_extensionStaticPathPatternMatcher.insert(
				pathPattern, value, false);
		}
		else {
			_wildcardStaticPathPatternMatcher.insert(pathPattern, value, true);
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

		protected int getExactIndex(String path) {
			int row = 0;
			long bitMask = _ALL_BITS_SET;
			int column = 0;

			for (; row < path.length(); ++row) {
				if (row > (maxPatternLength - 1)) {
					bitMask = 0;

					break;
				}

				char character = path.charAt(row);

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

		protected void insert(String pathPattern, T value, boolean forward) {
			if (_count > 63) {
				throw new IllegalArgumentException(
					"Exceeding maximum number of allowed URL patterns");
			}

			int index = getExactIndex(pathPattern);

			if (index > -1) {
				patternTuples.add(
					index, new PatternTuple<>(pathPattern, value));

				return;
			}

			index = _count++;

			int row = 0;
			int column = 0;
			long bitMask = 1 << index;

			for (; row < pathPattern.length(); ++row) {
				char character = pathPattern.charAt(row);

				if (!forward) {
					character = pathPattern.charAt(
						pathPattern.length() - 1 - row);
				}

				column = character - ASCII_PRINTABLE_OFFSET;

				trieArray[0][row][column] |= bitMask;
			}

			PatternTuple<T> patternTuple = new PatternTuple<>(
				pathPattern, value);

			trieArray[1][row - 1][column] |= bitMask;

			patternTuples.add(index, patternTuple);
		}

		protected int maxPatternLength;
		protected List<PatternTuple<T>> patternTuples = new ArrayList<>(
			Long.SIZE);
		protected final long[][][] trieArray;

		private int _count;

	}

	private static class ExtensionStaticPathPatternMatcher<T>
		extends BaseStaticPathPatternMatcher<T> {

		public ExtensionStaticPathPatternMatcher(int maxPatternLength) {
			super(maxPatternLength);
		}

		public PatternTuple<T> getPatternTuple(String path) {
			int pathLength = path.length();
			long currentBitMask = _ALL_BITS_SET;

			for (int row = 0; row < pathLength; ++row) {
				if (row > (maxPatternLength - 1)) {
					break;
				}

				char character = path.charAt(pathLength - 1 - row);

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

	}

	private static class WildcardStaticPathPatternMatcher<T>
		extends BaseStaticPathPatternMatcher<T> {

		public WildcardStaticPathPatternMatcher(int maxPatternLength) {
			super(maxPatternLength);
		}

		public PatternTuple<T> getPatternTuple(String path) {
			boolean onlyExact = false;
			boolean onlyWildcard = false;

			if (path.charAt(0) != '/') {
				onlyExact = true;
			}
			else if ((path.length() > 1) &&
					 (path.charAt(path.length() - 2) == '/') &&
					 (path.charAt(path.length() - 1) == '*')) {

				onlyWildcard = true;
			}

			int row = 0;
			int col = 0;
			long currentBitMask = _ALL_BITS_SET;
			long bestMatchBitMask = 0;

			for (; row < path.length(); ++row) {
				if (row > (maxPatternLength - 1)) {
					currentBitMask = 0;

					break;
				}

				char character = path.charAt(row);

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

				return patternTuples.get(getFirstSetBitIndex(bestMatchBitMask));
			}

			if (onlyExact) {
				long bitMask = currentBitMask & trieArray[1][row - 1][col];

				if (bitMask != 0) {
					return patternTuples.get(getFirstSetBitIndex(bitMask));
				}

				return null;
			}

			if (!onlyWildcard) {
				long bitMask = currentBitMask & trieArray[1][row - 1][col];

				if (bitMask != 0) {
					return patternTuples.get(getFirstSetBitIndex(bitMask));
				}
			}

			long extraBitMask =
				currentBitMask & trieArray[0][row][_SLASH_INDEX];

			extraBitMask &= trieArray[0][row + 1][_STAR_INDEX];
			extraBitMask &= trieArray[1][row + 1][_STAR_INDEX];

			if (extraBitMask != 0) {
				return patternTuples.get(getFirstSetBitIndex(extraBitMask));
			}

			return patternTuples.get(getFirstSetBitIndex(bestMatchBitMask));
		}

		public List<PatternTuple<T>> getPatternTuples(String path) {
			long patternTuplesBitMask = getPatternTuplesBitMask(path);

			List<PatternTuple<T>> patterns = new ArrayList<>(Long.SIZE);

			while (patternTuplesBitMask != 0) {
				patterns.add(
					patternTuples.get(
						getFirstSetBitIndex(patternTuplesBitMask)));

				patternTuplesBitMask &= patternTuplesBitMask - 1;
			}

			return patterns;
		}

		public long getPatternTuplesBitMask(String path) {
			long patternTuplesBitMask = 0;

			boolean onlyExact = false;
			boolean onlyWildcard = false;

			if (path.charAt(0) != '/') {
				onlyExact = true;
			}
			else if ((path.length() > 1) &&
					 (path.charAt(path.length() - 2) == '/') &&
					 (path.charAt(path.length() - 1) == '*')) {

				onlyWildcard = true;
			}

			int row = 0;
			int col = 0;
			long currentBitMask = _ALL_BITS_SET;

			for (; row < path.length(); ++row) {
				if (row > (maxPatternLength - 1)) {
					currentBitMask = 0;

					break;
				}

				char character = path.charAt(row);

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
						patternTuplesBitMask |= bitMask;
					}
				}
			}

			if (currentBitMask == 0) {
				return patternTuplesBitMask;
			}

			if (onlyExact) {
				long bitMask = currentBitMask & trieArray[1][row - 1][col];

				if (bitMask != 0) {
					patternTuplesBitMask |= bitMask;
				}

				return patternTuplesBitMask;
			}

			if (!onlyWildcard) {
				long bitMask = currentBitMask & trieArray[1][row - 1][col];

				if (bitMask != 0) {
					patternTuplesBitMask |= bitMask;
				}
			}

			long extraBitMask =
				currentBitMask & trieArray[0][row][_SLASH_INDEX];

			extraBitMask &= trieArray[0][row + 1][_STAR_INDEX];
			extraBitMask &= trieArray[1][row + 1][_STAR_INDEX];

			if (extraBitMask != 0) {
				patternTuplesBitMask |= extraBitMask;
			}

			return patternTuplesBitMask;
		}

	}

}