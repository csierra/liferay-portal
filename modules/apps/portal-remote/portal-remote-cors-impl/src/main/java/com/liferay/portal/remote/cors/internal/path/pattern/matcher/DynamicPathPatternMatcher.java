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
public class DynamicPathPatternMatcher<T> extends PathPatternMatcher<T> {

	public DynamicPathPatternMatcher() {
		_trieNodeArrayList = new TrieNodeArrayList<>();

		_exactTrieNode = _trieNodeArrayList.nextNode();
		_extensionTrieNode = _trieNodeArrayList.nextNode();
		_wildCardTrieNode = _trieNodeArrayList.nextNode();
	}

	@Override
	public List<PatternTuple<T>> getPatternTuples(String urlPath) {
		List<PatternTuple<T>> patternTuples = getWildcardPatternTuples(urlPath);

		PatternTuple<T> patternTuple = getExactPatternTuple(urlPath);

		if (patternTuple != null) {
			patternTuples.add(patternTuple);
		}

		patternTuple = getExtensionPatternTuple(urlPath);

		if (patternTuple != null) {
			patternTuples.add(patternTuple);
		}

		return patternTuples;
	}

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.2
	 *
	 * In the Web application deployment descriptor, the following syntax is
	 * used to define mappings:
	 * 1. Wild Card urlPath pattern 1:
	 *        A string beginning with a ' / ' character and ending with a ' /* '
	 *        suffix is used for urlPath mapping.
	 * 2. Wild Card urlPath pattern 2, aka extension matching:
	 *        A string beginning with a ' *. ' prefix is used as an extension
	 *        mapping.
	 * 3. Special urlPath pattern 1:
	 *        The empty string ("") is a special URL pattern that exactly maps
	 *        to the application's context root, i.e., requests of the form
	 *        http://host:port/'<'context-root'>'/. In this case the urlPath info is
	 *        ' / ' and the servlet urlPath and context urlPath is empty string ("").
	 * 4. Special urlPath pattern 2:
	 *        A string containing only the ' / ' character indicates the
	 *        "default" servlet of the application. In this case the servlet
	 *        urlPath is the request URI minus the context urlPath and the urlPath info
	 *        is null.
	 * 5. Exact urlPath pattern:
	 *        All other strings are used for exact matches only.
	 *
	 * @param urlPattern the pattern of urlPath, used for pattern matching
	 * @param value an non null object associated with urlPattern
	 */
	public void insert(String urlPattern, T value)
		throws IllegalArgumentException {

		if (value == null) {
			throw new IllegalArgumentException("Value can not be null");
		}

		// Wild Card urlPath pattern 1

		if (isValidWildCardPattern(urlPattern)) {
			insert(urlPattern, value, _wildCardTrieNode);

			return;
		}

		// Wild Card urlPath pattern 2, aka extension pattern

		if (isValidExtensionPattern(urlPattern)) {
			StringBuilder stringBuilder = new StringBuilder(urlPattern);

			stringBuilder.reverse();

			insert(stringBuilder.toString(), value, _extensionTrieNode);

			return;
		}

		// Exact pattern

		insert(urlPattern, value, _exactTrieNode);
	}

	@Override
	protected PatternTuple<T> getExactPatternTuple(String urlPath) {
		TrieNode<T> currentTrieNode = null;
		TrieNode<T> previousTrieNode = _exactTrieNode;

		for (int i = 0; i < urlPath.length(); ++i) {
			char character = urlPath.charAt(i);

			currentTrieNode = previousTrieNode.next(character);

			if (currentTrieNode == null) {
				break;
			}

			previousTrieNode = currentTrieNode;
		}

		if ((currentTrieNode != null) && currentTrieNode.isEnd()) {
			return currentTrieNode.getPatternTuple();
		}

		return null;
	}

	@Override
	protected PatternTuple<T> getExtensionPatternTuple(String urlPath) {
		TrieNode<T> currentTrieNode = null;
		TrieNode<T> previousTrieNode = _extensionTrieNode;

		for (int i = 0; i < urlPath.length(); ++i) {
			int index = urlPath.length() - 1 - i;

			char character = urlPath.charAt(index);

			if (character == '/') {
				break;
			}

			currentTrieNode = previousTrieNode.next(character);

			if (currentTrieNode == null) {
				break;
			}

			if (urlPath.charAt(index) == '.') {
				TrieNode<T> nextTrieNode = currentTrieNode.next('*');

				if ((nextTrieNode != null) && nextTrieNode.isEnd()) {
					return nextTrieNode.getPatternTuple();
				}
			}

			previousTrieNode = currentTrieNode;
		}

		return null;
	}

	@Override
	protected PatternTuple<T> getWildcardPatternTuple(String urlPath) {
		PatternTuple<T> patternTuple = null;

		TrieNode<T> currentTrieNode = null;
		TrieNode<T> previousTrieNode = _wildCardTrieNode;

		for (int i = 0; i < urlPath.length(); ++i) {
			currentTrieNode = previousTrieNode.next(urlPath.charAt(i));

			if (currentTrieNode == null) {
				break;
			}

			if (urlPath.charAt(i) == '/') {
				TrieNode<T> nextTrieNode = currentTrieNode.next('*');

				if ((nextTrieNode != null) && nextTrieNode.isEnd()) {
					patternTuple = nextTrieNode.getPatternTuple();
				}
			}

			previousTrieNode = currentTrieNode;
		}

		// if current node is null, it means trie travesaling
		// did not match every character.

		if (currentTrieNode != null) {
			currentTrieNode = currentTrieNode.next('/');

			if (currentTrieNode != null) {
				currentTrieNode = currentTrieNode.next('*');

				if ((currentTrieNode != null) && currentTrieNode.isEnd()) {
					patternTuple = currentTrieNode.getPatternTuple();
				}
			}
		}

		return patternTuple;
	}

	protected List<PatternTuple<T>> getWildcardPatternTuples(String urlPath) {
		List<PatternTuple<T>> patternTuples = new ArrayList<>(64);

		TrieNode<T> currentTrieNode = null;
		TrieNode<T> previousTrieNode = _wildCardTrieNode;

		for (int i = 0; i < urlPath.length(); ++i) {
			currentTrieNode = previousTrieNode.next(urlPath.charAt(i));

			if (currentTrieNode == null) {
				break;
			}

			if (urlPath.charAt(i) == '/') {
				TrieNode<T> nextTrieNode = currentTrieNode.next('*');

				if ((nextTrieNode != null) && nextTrieNode.isEnd()) {
					patternTuples.add(nextTrieNode.getPatternTuple());
				}
			}

			previousTrieNode = currentTrieNode;
		}

		// if current node is null, it means trie travesaling
		// did not match every character.

		if (currentTrieNode != null) {
			currentTrieNode = currentTrieNode.next('/');

			if (currentTrieNode != null) {
				currentTrieNode = currentTrieNode.next('*');

				if ((currentTrieNode != null) && currentTrieNode.isEnd()) {
					patternTuples.add(currentTrieNode.getPatternTuple());
				}
			}
		}

		return patternTuples;
	}

	protected void insert(
		String urlPattern, T value, TrieNode<T> previousTrieNode) {

		TrieNode<T> currentTrieNode = null;

		for (int i = 0; i < urlPattern.length(); ++i) {
			currentTrieNode = previousTrieNode.next(urlPattern.charAt(i));

			if (currentTrieNode == null) {
				currentTrieNode = previousTrieNode.setNext(
					urlPattern.charAt(i), _trieNodeArrayList);
			}

			previousTrieNode = currentTrieNode;
		}

		if (currentTrieNode != null) {
			currentTrieNode.setPatternTuple(
				new PatternTuple<>(urlPattern, value));
		}
	}

	private final TrieNode<T> _exactTrieNode;
	private final TrieNode<T> _extensionTrieNode;
	private final TrieNodeArrayList<T> _trieNodeArrayList;
	private final TrieNode<T> _wildCardTrieNode;

	private static class TrieNode<T> {

		public TrieNode() {
			_trieNodes = new ArrayList<>(ASCII_CHARACTER_RANGE);
		}

		public PatternTuple<T> getPatternTuple() {
			return _patternTuple;
		}

		public boolean isEnd() {
			if (_patternTuple != null) {
				return true;
			}

			return false;
		}

		public TrieNode<T> next(char character) {
			int index = character - ASCII_PRINTABLE_OFFSET;

			if ((index < 0) ||
				(index >= (ASCII_CHARACTER_RANGE + ASCII_PRINTABLE_OFFSET))) {

				throw new IllegalArgumentException();
			}

			return _trieNodes.get(index);
		}

		public TrieNode<T> setNext(
			char character, TrieNodeArrayList<T> trieNodeArrayList) {

			TrieNode<T> trieNode = trieNodeArrayList.nextNode();

			int index = character - ASCII_PRINTABLE_OFFSET;

			if ((index < 0) ||
				(index >= (ASCII_CHARACTER_RANGE + ASCII_PRINTABLE_OFFSET))) {

				throw new IllegalArgumentException();
			}

			_trieNodes.set(index, trieNode);

			return trieNode;
		}

		public void setPatternTuple(PatternTuple<T> patternTuple) {
			_patternTuple = patternTuple;
		}

		private PatternTuple<T> _patternTuple;

		/**
		 * Use list over hashMap for better performance
		 * There is around 70% performance increase for best match,
		 * and 40% performance increase for all matches.
		 */
		private final List<TrieNode<T>> _trieNodes;

	}

	private static class TrieNodeArrayList<T> {

		public TrieNodeArrayList() {
			_trieNodes = new ArrayList<>(_INIT_SIZE);

			for (int i = 0; i < _INIT_SIZE; ++i) {
				_trieNodes.add(new TrieNode<>());
			}
		}

		public TrieNode<T> nextNode() {
			if (_index >= _trieNodes.size()) {
				_trieNodes.ensureCapacity(_trieNodes.size() + _INIT_SIZE);

				for (int i = 0; i < _INIT_SIZE; ++i) {
					_trieNodes.add(new TrieNode<>());
				}
			}

			return _trieNodes.get(_index++);
		}

		private static final int _INIT_SIZE = 1024;

		private int _index;
		private ArrayList<TrieNode<T>> _trieNodes;

	}

}