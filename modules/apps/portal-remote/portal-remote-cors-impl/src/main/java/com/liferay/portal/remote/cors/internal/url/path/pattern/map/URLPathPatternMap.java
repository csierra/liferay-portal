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
import java.util.HashSet;
import java.util.List;

/**
 * @author Arthur Chan
 */
public class URLPathPatternMap {

	public URLPathPatternMap() {
		_exactMappings = new HashSet<>();
		_wildCardRoot = new TrieNode();
		_extensionMappings = new HashSet<>();
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

		if (_exactMappings.contains(path)) {
			return path;
		}

		// Second: search for wild card matching

		_foundSlashIndexes = new ArrayList<>();

		searchSlashIndexes(path, 0, _wildCardRoot.next(path.charAt(0)), true);

		if (_foundSlashIndexes.size() > 0) {
			return path.substring(0, _foundSlashIndexes.get(0)) + "/*";
		}

		// Third: search for extension matching

		String extension = getExtension(path);

		if ((extension != null) && _extensionMappings.contains(extension)) {
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

		if (_exactMappings.contains(path)) {
			patterns.add(path);
		}

		// Second: search for wild card matching

		_foundSlashIndexes = new ArrayList<>();

		searchSlashIndexes(path, 0, _wildCardRoot.next(path.charAt(0)), false);

		if (_foundSlashIndexes.size() > 0) {
			for (int i = 0; i < _foundSlashIndexes.size(); ++i) {
				patterns.add(
					path.substring(0, _foundSlashIndexes.get(i)) + "/*");
			}
		}

		// Third: search for extension matching

		String extension = getExtension(path);

		if ((extension != null) && _extensionMappings.contains(extension)) {
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
	 */
	public void insert(String pathPattern) {

		// Wild Card path pattern 1

		if (isValidWildCardPattern(pathPattern)) {
			insertWildCardPattern(pathPattern);
			System.out.println(
				"Insert " + pathPattern + " as wild card pattern");

			return;
		}

		// Wild Card path pattern 2, aka extension pattern

		if (isValidExtensionPattern(pathPattern)) {
			_extensionMappings.add(pathPattern.substring(2));
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

		_exactMappings.add(pathPattern);
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
			return _extensionMappings.remove(pathPattern.substring(2));
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

		return _exactMappings.remove(pathPattern);
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
	protected void insertWildCardPattern(String pathPattern) {
		TrieNode prev = _wildCardRoot;

		for (int i = 0; i < (pathPattern.length() - 1); ++i) {
			TrieNode current = prev.next(pathPattern.charAt(i));

			if (current == null) {
				current = prev.setNext(pathPattern.charAt(i));
			}

			// This character has to be slash, ensured by insert(String)

			if (i == (pathPattern.length() - 2)) {
				current.setEnd(true);
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
		TrieNode prev, String pathPattern, int i) {

		TrieNode current = prev.next(pathPattern.charAt(i));

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
				current.setEnd(false);
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

	protected void searchSlashIndexes(
		String path, int i, TrieNode current, boolean single) {

		// Base case for e.g.: (Trie: /abc/*), (Search: /acb)

		if (current == null) {
			return;
		}

		// We are at the last character of given path

		if (i == (path.length() - 1)) {

			// Base case for e.g.: (Trie: /*), (Search: /)

			if ((path.charAt(i) == '/') && current.isEnd()) {
				_foundSlashIndexes.add(i);

				return;
			}

			// Base case for e.g.: Trie: /abc/*, Search: /abc

			TrieNode next = current.next('/');

			if ((next != null) && next.isEnd()) {
				_foundSlashIndexes.add(i + 1);
			}

			// Base case for e.g.: (Trie: /abc/abc/*), (Search: /abc; /abc/)

			return;
		}

		// Recurse down the path first to see if there is longer match

		searchSlashIndexes(
			path, i + 1, current.next(path.charAt(i + 1)), single);

		// Try add matching index to the list, if
		// 1. single is false, or
		// 2. _foundSlashIndexes is empty

		if (!single || (_foundSlashIndexes.size() < 1)) {

			// Given the last if is met, add matching index to the list, if
			// 1. current character is slash, and
			// 2. current slash node has marked as end

			if ((path.charAt(i) == '/') && current.isEnd()) {
				_foundSlashIndexes.add(i);
			}
		}
	}

	private boolean _contextRoot;
	private boolean _defaultServlet;
	private final HashSet<String> _exactMappings;
	private final HashSet<String> _extensionMappings;
	private List<Integer> _foundSlashIndexes;
	private final TrieNode _wildCardRoot;

	private class TrieNode {

		public TrieNode() {
			_link = new HashMap<>();
		}

		public void clear() {
			_link.clear();
			_end = false;
		}

		public boolean hasNext() {
			return !_link.isEmpty();
		}

		public boolean isEnd() {
			return _end;
		}

		public TrieNode next(char character) {
			return _link.get(character);
		}

		public TrieNode removeNext(char character) {
			return _link.remove(character);
		}

		public void setEnd(boolean end) {
			_end = end;
		}

		public TrieNode setNext(char character) {
			TrieNode node = new TrieNode();

			_link.put(character, node);

			return node;
		}

		private boolean _end;
		private final HashMap<Character, TrieNode> _link;

	}

}