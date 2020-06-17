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
		_trieNodeHeap = new TrieNodeHeap<>();

		_exactTrie = _trieNodeHeap.nextNode();
		_extensionTrie = _trieNodeHeap.nextNode();
		_wildCardTrie = _trieNodeHeap.nextNode();
	}

	@Override
	public List<PatternTuple<T>> getPatternPackages(String urlPath) {
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
			insert(urlPattern, value, _wildCardTrie, false);

			return;
		}

		// Wild Card urlPath pattern 2, aka extension pattern

		if (isValidExtensionPattern(urlPattern)) {
			insert(urlPattern, value, _extensionTrie, true);

			return;
		}

		// Exact pattern

		insert(urlPattern, value, _exactTrie, false);
	}

	@Override
	protected PatternTuple<T> getExactPatternTuple(String urlPath) {
		TrieNode<T> prev = _exactTrie;

		TrieNode<T> current = null;

		for (int i = 0; i < urlPath.length(); ++i) {
			char character = urlPath.charAt(i);

			current = prev.next(character);

			if (current == null) {
				break;
			}

			prev = current;
		}

		if ((current != null) && current.isEnd()) {
			return current.getPatternTuple();
		}

		return null;
	}

	@Override
	protected PatternTuple<T> getExtensionPatternTuple(String urlPath) {
		TrieNode<T> prev = _extensionTrie;

		TrieNode<T> current = null;

		for (int i = 0; i < urlPath.length(); ++i) {
			int index = urlPath.length() - 1 - i;

			char character = urlPath.charAt(index);

			if (character == '/') {
				break;
			}

			current = prev.next(character);

			if (current == null) {
				break;
			}

			if (urlPath.charAt(index) == '.') {
				TrieNode<T> next = current.next('*');

				if ((next != null) && next.isEnd()) {
					return next.getPatternTuple();
				}
			}

			prev = current;
		}

		return null;
	}

	@Override
	protected PatternTuple<T> getWildcardPatternTuple(String urlPath) {
		PatternTuple<T> bestMatch = null;

		TrieNode<T> prev = _wildCardTrie;

		TrieNode<T> current = null;

		for (int i = 0; i < urlPath.length(); ++i) {
			current = prev.next(urlPath.charAt(i));

			if (current == null) {
				break;
			}

			if (urlPath.charAt(i) == '/') {
				TrieNode<T> next = current.next('*');

				if ((next != null) && next.isEnd()) {
					bestMatch = next.getPatternTuple();
				}
			}

			prev = current;
		}

		// if current node is null, it means trie travesaling
		// did not match every character.

		if (current != null) {
			current = current.next('/');

			if (current != null) {
				current = current.next('*');

				if ((current != null) && current.isEnd()) {
					bestMatch = current.getPatternTuple();
				}
			}
		}

		return bestMatch;
	}

	protected List<PatternTuple<T>> getWildcardPatternTuples(String urlPath) {
		List<PatternTuple<T>> patternTuples = new ArrayList<>(64);

		TrieNode<T> prev = _wildCardTrie;

		TrieNode<T> current = null;

		for (int i = 0; i < urlPath.length(); ++i) {
			current = prev.next(urlPath.charAt(i));

			if (current == null) {
				break;
			}

			if (urlPath.charAt(i) == '/') {
				TrieNode<T> next = current.next('*');

				if ((next != null) && next.isEnd()) {
					patternTuples.add(next.getPatternTuple());
				}
			}

			prev = current;
		}

		// if current node is null, it means trie travesaling
		// did not match every character.

		if (current != null) {
			current = current.next('/');

			if (current != null) {
				current = current.next('*');

				if ((current != null) && current.isEnd()) {
					patternTuples.add(current.getPatternTuple());
				}
			}
		}

		return patternTuples;
	}

	protected void insert(
		String urlPattern, T value, TrieNode<T> prev, boolean reverseIndex) {

		TrieNode<T> current = null;

		for (int i = 0; i < urlPattern.length(); ++i) {
			int index = i;

			if (reverseIndex) {
				index = urlPattern.length() - 1 - i;
			}

			current = prev.next(urlPattern.charAt(index));

			if (current == null) {
				current = prev.setNext(urlPattern.charAt(index), _trieNodeHeap);
			}

			prev = current;
		}

		if (current != null) {
			current.fillPatternPackage(urlPattern, value);
		}
	}

	private final TrieNode<T> _exactTrie;
	private final TrieNode<T> _extensionTrie;
	private final TrieNodeHeap<T> _trieNodeHeap;
	private final TrieNode<T> _wildCardTrie;

	private static class TrieNode<T> {

		public TrieNode() {
			_link = new ArrayList<>(CHARACTER_RANGE);

			for (int i = 0; i < CHARACTER_RANGE; ++i) {
				_link.add(null);
			}
		}

		public void fillPatternPackage(String urlPattern, T value) {
			_patternTuple = new PatternTuple<>(urlPattern, value);
		}

		public PatternTuple<T> getPatternTuple() {
			return _patternTuple;
		}

		public boolean isEnd() {
			return _patternTuple != null;
		}

		public TrieNode<T> next(char character) {
			int index = character - PRINTABLE_OFFSET;

			if ((index < 0) ||
				(index >= (CHARACTER_RANGE + PRINTABLE_OFFSET))) {

				throw new IllegalArgumentException();
			}

			return _link.get(index);
		}

		public TrieNode<T> setNext(
			char character, TrieNodeHeap<T> trieNodeHeap) {

			TrieNode<T> node = trieNodeHeap.nextNode();

			int index = character - PRINTABLE_OFFSET;

			if ((index < 0) ||
				(index >= (CHARACTER_RANGE + PRINTABLE_OFFSET))) {

				throw new IllegalArgumentException();
			}

			_link.set(index, node);

			return node;
		}

		/**
		 * Use list over hashMap for better performance
		 * There is around 70% performance increase for best match,
		 * and 40% performance increase for all matches.
		 */
		private final List<TrieNode<T>> _link;

		private PatternTuple<T> _patternTuple;

	}

	private static class TrieNodeHeap<T> {

		public TrieNodeHeap() {
			_trieNodeHeap = new ArrayList<>(_INIT_SIZE);

			for (int i = 0; i < _INIT_SIZE; ++i) {
				_trieNodeHeap.add(new TrieNode<>());
			}
		}

		public TrieNode<T> nextNode() {
			if (_index >= _trieNodeHeap.size()) {
				_trieNodeHeap.ensureCapacity(_trieNodeHeap.size() + _INIT_SIZE);

				for (int i = 0; i < _INIT_SIZE; ++i) {
					_trieNodeHeap.add(new TrieNode<>());
				}
			}

			return _trieNodeHeap.get(_index++);
		}

		private static final int _INIT_SIZE = 1024;

		private int _index;
		private ArrayList<TrieNode<T>> _trieNodeHeap;

	}

}