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
		_exactTrie = new TrieNode<>();
		_extentionTrie = new TrieNode<>();
		_wildCardTrie = new TrieNode<>();
	}

	/**
	 * Removes all of the urlPath patterns.
	 */
	public void clear() {
		_extentionTrie.clear();
		_exactTrie.clear();
		_wildCardTrie.clear();
	}

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1
	 *
	 * Get cargos of the best matching pattern of the given urlPath.
	 *
	 * The best matching pattern is searched in the order of:
	 * 1. Exact matching pattern
	 * 2. Wild card matching the longest pattern
	 * 3. Extension pattern
	 * 4. Default pattern
	 *
	 * The returned cargos preserve their natural order during insertion, e.g.
	 * the first inserted cargo is always returned before rest cargos.
	 *
	 * @param urlPath a legal urlPath from a URL
	 * @return cargos of the best matching pattern, preserving insertion order
	 */
	public List<T> getCargoList(String urlPath) {
		PatternPackage<T> patternPackage = getExactPatternPackage(urlPath);

		if (patternPackage != null) {
			return patternPackage.getCargoList();
		}

		List<PatternPackage<T>> patternPackages = getWildcardPatternPackages(
			urlPath, true);

		if (!patternPackages.isEmpty()) {
			patternPackage = patternPackages.get(0);

			return patternPackage.getCargoList();
		}

		patternPackage = getExtensionPatternPackage(urlPath);

		if (patternPackage != null) {
			return patternPackage.getCargoList();
		}

		return null;
	}

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1
	 *
	 * Get cargos of all matching patterns of the given urlPath.
	 *
	 * patterns include:
	 * 1. Exact matching pattern
	 * 2. Wild card matching patterns
	 * 3. Extension pattern
	 * 4. Default pattern
	 *
	 * @param urlPath a legal urlPath from a URL
	 * @return cargos of all matching patterns
	 */
	public List<List<T>> getCargoLists(String urlPath) {
		List<PatternPackage<T>> patternPackages = getWildcardPatternPackages(
			urlPath, false);

		List<List<T>> cargoLists = new ArrayList<>(patternPackages.size() + 2);

		for (PatternPackage<T> patternPackage : patternPackages) {
			cargoLists.add(patternPackage.getCargoList());
		}

		PatternPackage<T> patternPackage = getExactPatternPackage(urlPath);

		if (patternPackage != null) {
			cargoLists.add(patternPackage.getCargoList());
		}

		patternPackage = getExtensionPatternPackage(urlPath);

		if (patternPackage != null) {
			cargoLists.add(patternPackage.getCargoList());
		}

		return cargoLists;
	}

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1
	 *
	 * Get the matching pattern of the given urlPath, following the order of:
	 * 1. Exact matching pattern
	 * 2. Wild card matching the longest pattern
	 * 3. Extension pattern
	 * 4. Default pattern
	 *
	 * @param urlPath a legal urlPath from a URL
	 * @return the matched pattern
	 */
	public String getPattern(String urlPath) {
		PatternPackage<T> patternPackage = getExactPatternPackage(urlPath);

		if (patternPackage != null) {
			return patternPackage.getPattern();
		}

		List<PatternPackage<T>> patternPackages = getWildcardPatternPackages(
			urlPath, true);

		if (!patternPackages.isEmpty()) {
			patternPackage = patternPackages.get(0);

			return patternPackage.getPattern();
		}

		patternPackage = getExtensionPatternPackage(urlPath);

		if (patternPackage != null) {
			return patternPackage.getPattern();
		}

		return null;
	}

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1
	 *
	 * Get all matching patterns of the given urlPath, including:
	 * 1. Exact matching pattern
	 * 2. Wild card matching patterns
	 * 3. Extension pattern
	 *
	 * @param urlPath a legal urlPath from a URL
	 * @return all the matched patterns
	 */
	public List<String> getPatterns(String urlPath) {
		List<PatternPackage<T>> patternPackages = getWildcardPatternPackages(
			urlPath, false);

		List<String> patterns = new ArrayList<>(patternPackages.size() + 2);

		for (PatternPackage<T> patternPackage : patternPackages) {
			patterns.add(patternPackage.getPattern());
		}

		PatternPackage<T> patternPackage = getExactPatternPackage(urlPath);

		if (patternPackage != null) {
			patterns.add(patternPackage.getPattern());
		}

		patternPackage = getExtensionPatternPackage(urlPath);

		if (patternPackage != null) {
			patterns.add(patternPackage.getPattern());
		}

		return patterns;
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
	 * @param cargo an non null object associated with urlPattern
	 */
	public void insert(String urlPattern, T cargo)
		throws IllegalArgumentException {

		if (cargo == null) {
			throw new IllegalArgumentException("cargo cannot be null");
		}

		// Wild Card urlPath pattern 1

		if (isValidWildCardPattern(urlPattern)) {
			insert(urlPattern, cargo, 1);

			return;
		}

		// Wild Card urlPath pattern 2, aka extension pattern

		if (isValidExtensionPattern(urlPattern)) {
			insert(urlPattern, cargo, 2);

			return;
		}

		// Exact pattern

		insert(urlPattern, cargo, 0);
	}

	protected PatternPackage<T> getExactPatternPackage(String urlPath) {
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
			return current.getPatternPackage();
		}

		return null;
	}

	protected PatternPackage<T> getExtensionPatternPackage(String urlPath) {
		TrieNode<T> prev = _extentionTrie;

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
					return next.getPatternPackage();
				}
			}

			prev = current;
		}

		return null;
	}

	protected List<PatternPackage<T>> getWildcardPatternPackages(
		String urlPath, boolean bestMatch) {

		List<PatternPackage<T>> patternPackages = null;

		if (bestMatch) {
			patternPackages = new ArrayList<>(1);
		}
		else {
			patternPackages = new ArrayList<>(32);
		}

		TrieNode<T> prev = _wildCardTrie;

		TrieNode<T> current = null;

		// Use a local variable to store best match, instead
		// of pushing current best to patternPackages and remove
		// it everytime for better performance.
		// There is around 16% performance increase for best match.

		PatternPackage<T> bestMatchSoFar = null;

		for (int i = 0; i < urlPath.length(); ++i) {
			current = prev.next(urlPath.charAt(i));

			if (current == null) {
				break;
			}

			if (urlPath.charAt(i) == '/') {
				TrieNode<T> next = current.next('*');

				if ((next != null) && next.isEnd()) {
					if (bestMatch) {
						bestMatchSoFar = next.getPatternPackage();
					}
					else {
						patternPackages.add(next.getPatternPackage());
					}
				}
			}

			prev = current;
		}

		// if current node is null, it means trie travesaling
		// did not match every character.

		if (current == null) {
			if (bestMatch && (bestMatchSoFar != null)) {
				patternPackages.add(bestMatchSoFar);
			}

			return patternPackages;
		}

		TrieNode<T> next = current.next('/');

		if (next == null) {
			if (bestMatch && (bestMatchSoFar != null)) {
				patternPackages.add(bestMatchSoFar);
			}

			return patternPackages;
		}

		next = next.next('*');

		if ((next == null) || !next.isEnd()) {
			if (bestMatch && (bestMatchSoFar != null)) {
				patternPackages.add(bestMatchSoFar);
			}

			return patternPackages;
		}

		patternPackages.add(next.getPatternPackage());

		return patternPackages;
	}

	protected void insert(String urlPattern, T cargo, int insertType) {
		TrieNode<T> prev = null;

		if (insertType == 0) {
			prev = _exactTrie;
		}
		else if (insertType == 1) {
			prev = _wildCardTrie;
		}
		else {
			prev = _extentionTrie;
		}

		TrieNode<T> current = null;

		for (int i = 0; i < urlPattern.length(); ++i) {
			int index = i;

			if ((insertType != 0) && (insertType != 1)) {
				index = urlPattern.length() - 1 - i;
			}

			current = prev.next(urlPattern.charAt(index));

			if (current == null) {
				current = prev.setNext(urlPattern.charAt(index));
			}

			prev = current;
		}

		current.fillPatternPackage(urlPattern, cargo);
	}

	private final TrieNode<T> _exactTrie;
	private final TrieNode<T> _extentionTrie;
	private final TrieNode<T> _wildCardTrie;

	private static class TrieNode<T> {

		public TrieNode() {
			_link = new ArrayList<>(CHARACTER_RANGE);

			for (int i = 0; i < CHARACTER_RANGE; ++i) {
				_link.add(null);
			}

			_patternPackage = new PatternPackage<>();
		}

		public void clear() {
			_patternPackage.clear();
			_link.clear();
		}

		public void fillPatternPackage(String urlPattern, T cargo) {
			_patternPackage.set(urlPattern, cargo);
		}

		public PatternPackage<T> getPatternPackage() {
			return _patternPackage;
		}

		public boolean isEnd() {
			return !_patternPackage.isEmpty();
		}

		public TrieNode<T> next(char character) {
			int index = character - '\0' - PRINTABLE_OFFSET;

			if ((index < 0) ||
				(index >= (CHARACTER_RANGE + PRINTABLE_OFFSET))) {

				throw new IllegalArgumentException();
			}

			return _link.get(index);
		}

		public TrieNode<T> setNext(char character) {
			TrieNode<T> node = new TrieNode<>();

			int index = character - '\0' - PRINTABLE_OFFSET;

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

		private final PatternPackage<T> _patternPackage;

	}

}