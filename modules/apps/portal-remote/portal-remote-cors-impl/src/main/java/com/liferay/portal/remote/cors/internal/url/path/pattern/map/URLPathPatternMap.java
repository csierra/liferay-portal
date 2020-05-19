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

package com.liferay.portal.remote.cors.internal.url.path.pattern.map;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author Arthur Chan
 */
public class URLPathPatternMap<T> {

	public URLPathPatternMap() {
		_exactMappings = new HashMap<>();
		_wildCardRoot = new TrieNode<>();
		_extensionMappings = new HashMap<>();
	}

	/**
	 * Removes all of the path patterns from this URLPathPatternMap.
	 */
	public void clear() {
		_exactMappings.clear();
		_wildCardRoot.clear();
		_extensionMappings.clear();
	}

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1
	 *
	 * Get cargos of the matching pattern of the given path, following the order of:
	 * 1. Exact matching pattern
	 * 2. Wild card matching the longest pattern
	 * 3. Extension pattern
	 * 4. Default pattern
	 *
	 * @param path a legal path from a URL
	 * @return the matched pattern
	 */
	public List<T> getCargosOfMatchingPattern(String path) {

		// Second: search for exact matching

		if (_exactMappings.containsKey(path)) {
			return _exactMappings.get(path);
		}

		// Second: search for wild card matching

		List<List<T>> cargos = searchCargosInWildCardPatterns(path, false);

		if (!cargos.isEmpty()) {
			return cargos.get(0);
		}

		// Third: search for extension matching

		String extension = getExtension(path);

		if ((extension != null) && _extensionMappings.containsKey(extension)) {
			return _extensionMappings.get(extension);
		}

		return new ArrayList<>();
	}

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1
	 *
	 * Get cargos of all matching patterns of the given path, including:
	 * 1. Exact matching pattern
	 * 2. Wild card matching the longest pattern
	 * 3. Extension pattern
	 * 4. Default pattern
	 *
	 * @param path a legal path from a URL
	 * @return all cargos of matching patterns
	 */
	public List<List<T>> getCargosOfMatchingPatterns(String path) {

		// First: search for wild card matching

		List<List<T>> cargos = searchCargosInWildCardPatterns(path, true);

		// Second: search for exact matching

		if (_exactMappings.containsKey(path)) {
			cargos.add(_exactMappings.get(path));
		}

		// Third: search for extension matching

		String extension = getExtension(path);

		if ((extension != null) && _extensionMappings.containsKey(extension)) {
			cargos.add(_extensionMappings.get(extension));
		}

		return cargos;
	}

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1
	 *
	 * Get the best matching pattern, with its cargos, of the given path, following the order of:
	 * 1. Exact matching pattern
	 * 2. Wild card matching the longest pattern
	 * 3. Extension pattern
	 * 4. Default pattern
	 *
	 * @param path a legal path from a URL
	 * @return Map of the best matching pattern, or empty map
	 */
	public Map<String, List<T>> getMatching(String path) {
		Map<String, List<T>> match = new HashMap<>();

		// First: search for exact matching

		if (_exactMappings.containsKey(path)) {
			match.put(path, _exactMappings.get(path));

			return match;
		}

		// Second: search for wild card matching

		match = searchInWildCardPatterns(path, false);

		if (!match.isEmpty()) {
			return match;
		}

		// Third: search for extension matching

		String extension = getExtension(path);

		if ((extension != null) && _extensionMappings.containsKey(extension)) {
			match.put("*." + extension, _extensionMappings.get(extension));
		}

		return match;
	}

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1
	 *
	 * Get all matching patterns and their cargos of the given path, including:
	 * 1. Exact matching pattern
	 * 2. Wild card matching patterns
	 * 3. Extension pattern
	 *
	 * @param path a legal path from a URL
	 * @return all the matching patterns and their cargos
	 */
	public Map<String, List<T>> getMatchings(String path) {

		// First: search for wild card matching

		Map<String, List<T>> patterns = searchInWildCardPatterns(path, true);

		// Second: search for exact matching

		if (_exactMappings.containsKey(path)) {
			patterns.put(path, _exactMappings.get(path));
		}

		// Third: search for extension matching

		String extension = getExtension(path);

		if ((extension != null) && _extensionMappings.containsKey(extension)) {
			patterns.put("*." + extension, _extensionMappings.get(extension));
		}

		return patterns;
	}

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1
	 *
	 * Get the matching pattern of the given path, following the order of:
	 * 1. Exact matching pattern
	 * 2. Wild card matching the longest pattern
	 * 3. Extension pattern
	 * 4. Default pattern
	 *
	 * @param path a legal path from a URL
	 * @return the matched pattern
	 */
	public String getPattern(String path) {

		// First: search for exact matching

		if (_exactMappings.containsKey(path)) {
			return path;
		}

		// Second: search for wild card matching

		Stack<Integer> foundSlashIndexes = searchIndexesInWildCardPatterns(
			path);

		if (!foundSlashIndexes.empty()) {
			return path.substring(0, foundSlashIndexes.peek()) + "/*";
		}

		// Third: search for extension matching

		String extension = getExtension(path);

		if ((extension != null) && _extensionMappings.containsKey(extension)) {
			return "*." + extension;
		}

		// Forth: return default

		return "";
	}

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1
	 *
	 * Get all matching patterns of the given path, including:
	 * 1. Exact matching pattern
	 * 2. Wild card matching patterns
	 * 3. Extension pattern
	 *
	 * @param path a legal path from a URL
	 * @return all the matched patterns
	 */
	public List<String> getPatterns(String path) {
		List<String> patterns = new ArrayList<>();

		// First: search for exact matching

		if (_exactMappings.containsKey(path)) {
			patterns.add(path);
		}

		// Second: search for wild card matching

		Stack<Integer> foundSlashIndexes = searchIndexesInWildCardPatterns(
			path);

		while (!foundSlashIndexes.empty()) {
			patterns.add(path.substring(0, foundSlashIndexes.pop()) + "/*");
		}

		// Third: search for extension matching

		String extension = getExtension(path);

		if ((extension != null) && _extensionMappings.containsKey(extension)) {
			patterns.add("*." + extension);
		}

		return patterns;
	}

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.2
	 *
	 * In the Web application deployment descriptor, the following syntax is
	 * used to define mappings:
	 * 1. Wild Card path pattern 1:
	 *        A string beginning with a ' / ' character and ending with a ' /* '
	 *        suffix is used for path mapping.
	 * 2. Wild Card path pattern 2, aka extension matching:
	 *        A string beginning with a ' *. ' prefix is used as an extension
	 *        mapping.
	 * 3. Special path pattern 1:
	 *        The empty string ("") is a special URL pattern that exactly maps
	 *        to the application's context root, i.e., requests of the form
	 *        http://host:port/'<'context-root'>'/. In this case the path info is
	 *        ' / ' and the servlet path and context path is empty string ("").
	 * 4. Special path pattern 2:
	 *        A string containing only the ' / ' character indicates the
	 *        "default" servlet of the application. In this case the servlet
	 *        path is the request URI minus the context path and the path info
	 *        is null.
	 * 5. Exact path pattern:
	 *        All other strings are used for exact matches only.
	 *
	 * @param pathPattern the pattern of path, used for pattern matching
	 * @param cargo an non null object associated with pathPattern
	 */
	public void insert(String pathPattern, T cargo)
		throws IllegalArgumentException {

		if (cargo == null) {
			throw new IllegalArgumentException("cargo cannot be null");
		}

		// Wild Card path pattern 1

		if (isValidWildCardPattern(pathPattern)) {
			insertWildCardPattern(pathPattern, cargo);
			System.out.println(
				"Insert " + pathPattern + " as wild card pattern");

			return;
		}

		// Wild Card path pattern 2, aka extension pattern

		if (isValidExtensionPattern(pathPattern)) {
			List<T> cargos = _extensionMappings.get(pathPattern);

			if (cargos == null) {
				cargos = new ArrayList<>();
			}

			cargos.add(cargo);

			_extensionMappings.put(pathPattern.substring(2), cargos);

			System.out.println(
				"Insert " + pathPattern + " as extenssion pattern");

			return;
		}

		// Special path pattern 2

		if ((pathPattern.length() == 1) && (pathPattern.charAt(0) == '/')) {
			_defaultServlet = true;
			System.out.println("Insert " + pathPattern + " default");

			return;
		}

		// Special path pattern 1

		if (pathPattern.length() < 1) {
			_contextRoot = true;
			System.out.println("Insert " + pathPattern + " context root");

			return;
		}

		// Exact pattern

		List<T> cargos = _exactMappings.get(pathPattern);

		if (cargos == null) {
			cargos = new ArrayList<>();
		}

		cargos.add(cargo);

		_exactMappings.put(pathPattern, cargos);

		System.out.println("Insert " + pathPattern + " as exact pattern");
	}

	public boolean isContextRoot(String path) {
		if (_contextRoot & path.equals("/")) {
			return true;
		}

		return false;
	}

	public boolean isDefautServlet(String path) {
		if (_defaultServlet & path.equals("")) {
			return true;
		}

		return false;
	}

	/**
	 * Removes path pattern from this URLPathPatternMap.
	 *
	 * @param the pathPattern
	 * @return true if such path pattern exists, otherwise false
	 */
	public boolean remove(String pathPattern) {

		// Wild Card path pattern 1

		if (isValidWildCardPattern(pathPattern)) {
			return removeWildCardPattern(_wildCardRoot, pathPattern, 0);
		}

		// Wild Card path pattern 2, aka extension pattern

		if (isValidExtensionPattern(pathPattern)) {
			if (_extensionMappings.remove(pathPattern.substring(2)) != null) {
				return true;
			}

			return false;
		}

		// Special path pattern 2

		if ((pathPattern.length() == 1) && (pathPattern.charAt(0) == '/')) {
			if (_defaultServlet) {
				_defaultServlet = false;

				return true;
			}

			return false;
		}

		// Special path pattern 1

		if (pathPattern.length() < 1) {
			if (_contextRoot) {
				_contextRoot = false;

				return true;
			}

			return false;
		}

		// Exact pattern

		if (_exactMappings.remove(pathPattern) != null) {
			return true;
		}

		return false;
	}

	/**
	 * Get the extension from the path, an extension is defined as the
	 * part of the last segment after the last ' . ' character. See:
	 *
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1.3
	 *
	 * @param path A legal path of a URL
	 * @return string of extension from the path
	 */
	protected String getExtension(String path) {
		int i = path.length() - 1;

		while (i > -1) {
			char c = path.charAt(i);

			if (c == '.') {
				return path.substring(i + 1);
			}

			if (c == '/') {
				break;
			}

			--i;
		}

		return null;
	}

	/**
	 * The last star character is not stored in the trie, in order to not to
	 * confuse with actual star characters. Instead the last slash character
	 * will be marked as end.
	 */
	protected void insertWildCardPattern(String pathPattern, T cargo) {
		TrieNode<T> prev = _wildCardRoot;

		for (int i = 0; i < (pathPattern.length() - 1); ++i) {
			TrieNode<T> current = prev.next(pathPattern.charAt(i));

			if (current == null) {
				current = prev.setNext(pathPattern.charAt(i));
			}

			// This character has to be slash, ensured by insert(String)

			if (i == (pathPattern.length() - 2)) {
				current.addCargo(cargo);
			}

			prev = current;
		}
	}

	/**
	 * A valid ExtensionPattern:
	 * 1. Without the leading '*', it abides by the format of a segment of path
	 *         of URI specification: https://tools.ietf.org/html/rfc3986#section-3.3
	 * 2. It also abides by:
	 *         https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1.3, and
	 *         https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.2
	 *
	 * @param pathPattern the given pathPattern
	 * @return a boolean value indicating if the pathPattern a valid extensionPattern
	 */
	protected boolean isValidExtensionPattern(String pathPattern) {
		if ((pathPattern.length() < 3) || (pathPattern.charAt(0) != '*') ||
			(pathPattern.charAt(1) != '.')) {

			return false;
		}

		for (int i = 2; i < pathPattern.length(); ++i) {
			if (pathPattern.charAt(i) == '/') {
				return false;
			}

			if (pathPattern.charAt(i) == '.') {
				return false;
			}
		}

		return true;
	}

	/**
	 * A valid WildCardPattern:
	 * 1. Without the trailing '*', it abides by the format of path of URI specification:
	 *         https://tools.ietf.org/html/rfc3986#section-3.3
	 * 2. It also abides by:
	 *         https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.2
	 *
	 * @param pathPattern the given pathPattern
	 * @return a boolean value indicating if the pathPattern a valid wildCardPattern
	 */
	protected boolean isValidWildCardPattern(String pathPattern) {
		if ((pathPattern.length() < 2) || (pathPattern.charAt(0) != '/') ||
			(pathPattern.charAt(pathPattern.length() - 1) != '*') ||
			(pathPattern.charAt(pathPattern.length() - 2) != '/')) {

			return false;
		}

		try {
			String path = pathPattern.substring(0, pathPattern.length() - 1);

			URI uri = new URI("https://test" + path);

			if (!path.contentEquals(uri.getPath())) {
				return false;
			}
		}
		catch (URISyntaxException uriSyntaxException) {
			return false;
		}

		return true;
	}

	protected boolean removeWildCardPattern(
		TrieNode<T> prev, String pathPattern, int i) {

		TrieNode<T> current = prev.next(pathPattern.charAt(i));

		if (current == null) {
			return false;
		}

		// The last character is star, so the recursing ends at
		// second last character.

		if (i == (pathPattern.length() - 2)) {
			if (!current.isEnd()) {
				return false;
			}

			if (!current.hasNext()) {
				prev.removeNext(pathPattern.charAt(i));
			}
			else {
				current.clearCargos();
			}

			return true;
		}

		boolean found = removeWildCardPattern(current, pathPattern, i + 1);

		if (!found) {
			return false;
		}

		if (!current.hasNext() && !current.isEnd()) {
			prev.removeNext(pathPattern.charAt(i));
		}

		return true;
	}

	protected List<List<T>> searchCargosInWildCardPatterns(
		String path, boolean all) {

		List<List<T>> cargos = new ArrayList<>();

		TrieNode<T> prev = _wildCardRoot;
		TrieNode<T> current = null;

		int i = 0;

		while (i < path.length()) {
			current = prev.next(path.charAt(i));

			if (current == null) {
				break;
			}

			if ((path.charAt(i) == '/') && current.isEnd()) {
				if (!all) {
					cargos.clear();
				}

				cargos.add(current.getCargos());
			}

			prev = current;
			++i;
		}

		// if it's able to reach the last character in path, two cases:
		//     1. the last character is '/', handled in while loop.
		//     2. the last character is not '/':
		//         we need to test if next node is an ending '/'. e.g.:
		//         Trie: /abc/*, search for: /abc

		if ((i == path.length()) && (path.charAt(i - 1) != '/')) {
			current = prev.next('/');

			if ((current != null) && current.isEnd()) {
				if (!all) {
					cargos.clear();
				}

				cargos.add(current.getCargos());
			}
		}

		return cargos;
	}

	protected Stack<Integer> searchIndexesInWildCardPatterns(String path) {
		Stack<Integer> foundSlashIndexes = new Stack<>();

		TrieNode<T> prev = _wildCardRoot;
		TrieNode<T> current = null;

		int i = 0;

		while (i < path.length()) {
			current = prev.next(path.charAt(i));

			if (current == null) {
				break;
			}

			if ((path.charAt(i) == '/') && current.isEnd()) {
				foundSlashIndexes.push(i);
			}

			prev = current;
			++i;
		}

		// if it's able to reach the last character in path, two cases:
		//     1. the last character is '/', handled in while loop.
		//     2. the last character is not '/':
		//         we need to test if next node is an ending '/'. e.g.:
		//         Trie: /abc/*, search for: /abc

		if ((i == path.length()) && (path.charAt(i - 1) != '/')) {
			current = prev.next('/');

			if ((current != null) && current.isEnd()) {
				foundSlashIndexes.push(i);
			}
		}

		return foundSlashIndexes;
	}

	protected Map<String, List<T>> searchInWildCardPatterns(
		String path, boolean all) {

		Map<String, List<T>> matchings = new HashMap<>();

		TrieNode<T> prev = _wildCardRoot;
		TrieNode<T> current = null;

		int i = 0;

		while (i < path.length()) {
			current = prev.next(path.charAt(i));

			if (current == null) {
				break;
			}

			if ((path.charAt(i) == '/') && current.isEnd()) {
				if (!all) {
					matchings.clear();
				}

				matchings.put(path.substring(0, i) + "/*", current.getCargos());
			}

			prev = current;
			++i;
		}

		// if it's able to reach the last character in path, two cases:
		//     1. the last character is '/', handled in while loop.
		//     2. the last character is not '/':
		//         we need to test if next node is an ending '/'. e.g.:
		//         Trie: /abc/*, search for: /abc

		if ((i == path.length()) && (path.charAt(i - 1) != '/')) {
			current = prev.next('/');

			if ((current != null) && current.isEnd()) {
				if (!all) {
					matchings.clear();
				}

				matchings.put(path.substring(0, i) + "/*", current.getCargos());
			}
		}

		return matchings;
	}

	private boolean _contextRoot;
	private boolean _defaultServlet;
	private final HashMap<String, List<T>> _exactMappings;
	private final HashMap<String, List<T>> _extensionMappings;
	private final TrieNode<T> _wildCardRoot;

	private static class TrieNode<T> {

		public TrieNode() {
			_link = new HashMap<>();
			_cargos = new ArrayList<>();
		}

		public void addCargo(T cargo) {
			if (_cargos == null) {
				_cargos = new ArrayList<>();
			}

			_cargos.add(cargo);
		}

		public void clear() {
			_link.clear();
			_cargos.clear();
		}

		public void clearCargos() {
			_cargos.clear();
		}

		public List<T> getCargos() {
			return _cargos;
		}

		public boolean hasNext() {
			return !_link.isEmpty();
		}

		public boolean isEnd() {
			if (_cargos.isEmpty()) {
				return false;
			}

			return true;
		}

		public TrieNode<T> next(char character) {
			return _link.get(character);
		}

		public TrieNode<T> removeNext(char character) {
			return _link.remove(character);
		}

		public TrieNode<T> setNext(char character) {
			TrieNode<T> node = new TrieNode<>();

			_link.put(character, node);

			return node;
		}

		private List<T> _cargos;
		private final HashMap<Character, TrieNode<T>> _link;

	}

}