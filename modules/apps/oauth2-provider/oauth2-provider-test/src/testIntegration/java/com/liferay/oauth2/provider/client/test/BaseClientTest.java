/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.oauth2.provider.client.test;

import aQute.bnd.osgi.Analyzer;
import aQute.bnd.osgi.Jar;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.shrinkwrap.osgi.api.BndProjectBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.cxf.jaxrs.provider.json.JSONProvider;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.BundleActivator;

import org.w3c.dom.Document;

public class BaseClientTest {

	@BeforeClass
	public static void setupClass() {
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
	}

	public static Archive<?> getDeployment(
		Class<? extends BundleActivator> bundleActivator) throws Exception {

		BndProjectBuilder bndProjectBuilder = ShrinkWrap.create(
			BndProjectBuilder.class);

		String javaClassPathString = System.getProperty("java.class.path");

		String[] javaClassPaths = javaClassPathString.split(File.pathSeparator);

		for (String javaClassPath : javaClassPaths) {
			File file = new File(javaClassPath);

			if (file.isDirectory() ||
				javaClassPath.toLowerCase().endsWith(".zip") ||
				javaClassPath.toLowerCase().endsWith(".jar")) {

				bndProjectBuilder.addClassPath(file);
			}
		}

		Analyzer analyzer = new Analyzer();

		File bndFile = new File("bnd.bnd");

		JavaArchive javaArchive = bndProjectBuilder.setBndFile(
			bndFile
		).as(
			JavaArchive.class
		);

		javaArchive.addClass(bundleActivator);

		ZipExporter zipExporter = javaArchive.as(ZipExporter.class);

		Jar jar = new Jar(
			javaArchive.getName(), zipExporter.exportAsInputStream());

		Properties analyzerProperties = new Properties();

		analyzerProperties.putAll(analyzer.loadProperties(bndFile));

		analyzer.setJar(jar);

		Manifest firstPassManifest = new Manifest(
			javaArchive.get("META-INF/MANIFEST.MF").getAsset().openStream());

		firstPassManifest.getMainAttributes().remove(new Attributes.Name("Import-Package"));

		firstPassManifest.getMainAttributes().putValue("Bundle-Activator", bundleActivator.getName());
		analyzer.mergeManifest(firstPassManifest);
		Manifest manifest = analyzer.calcManifest();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		manifest.write(baos);
		ByteArrayAsset byteArrayAsset = new ByteArrayAsset(baos.toByteArray());

		javaArchive.delete("META-INF/MANIFEST.MF");

		javaArchive.add(byteArrayAsset, "META-INF/MANIFEST.MF");

		return javaArchive;
	}

	public URL getUrl() {
		return _url;
	}

	protected static Client getClient() {
		return ClientBuilder.newClient().register(JSONProvider.class);
	}

	protected Invocation.Builder authorize(
		Invocation.Builder builder, String tokenString) {

		return builder.header("Authorization", "Bearer " + tokenString);
	}

	protected <T> T executeCodeGrant(
		String clientId, String clientSecret, String hostname,
		BiFunction<ClientCredentials, WebTarget, Response> credentials,
		Function<Response, T> authorizationCodeResponseParser) throws URISyntaxException {
	
		WebTarget webTarget = getAuthorizeEndpointInvocationWebTarget();
		
		return authorizationCodeResponseParser.apply(
			credentials.apply(
				new ClientCredentials(clientId, clientSecret, hostname), 
				webTarget));
	}

	protected String getToken(String clientId) throws URISyntaxException {
		return getToken(clientId, null);
	}

	protected String getToken(String clientId, String hostname)
		throws URISyntaxException {

		return parseTokenString(
			getClientCredentials(
				clientId, getTokenInvocationBuilder(hostname)));
	}

	protected <T> T getToken(
		String clientId, String hostname,
		BiFunction<String, Invocation.Builder, Response> credentials,
		Function<Response, T> tokenParser) throws URISyntaxException {

		return tokenParser.apply(
			credentials.apply(clientId, getTokenInvocationBuilder(hostname)));
	}

	protected <T> T getTokenForAuthorizationCodeGrant(
		String clientId, String clientSecret, String hostname,
		BiFunction<ClientCredentials, Invocation.Builder, Response> credentials,
		Function<Response, T> tokenParser) throws URISyntaxException {

		return tokenParser.apply(
			credentials.apply(
				new ClientCredentials(clientId, clientSecret, hostname), 
				getTokenInvocationBuilder(hostname)));
	}

	protected String parseAuthorizationCodeString(Response response) {
		String redirectLocation = response.getHeaderString("Location");
		
		if (redirectLocation == null) {
			throw new IllegalArgumentException(
				"Authorization service response missing Location header from " +
					"which code is extracted");
		}
		
		return _getParameterValue(redirectLocation, "code");
	}

	protected String parseTokenString(Response tokenResponse) {
		String jsonString = tokenResponse.readEntity(String.class);

		try {
			JSONObject jsonObject = new JSONObject(jsonString);

			return jsonObject.getString("access_token");
		}
		catch (JSONException e) {
			throw new IllegalArgumentException(
				"The token service returned " + jsonString);
		}
	}

	protected JSONObject parseJsonObject(Response tokenResponse) {
		String jsonString = tokenResponse.readEntity(String.class);

		try {
			return  new JSONObject(jsonString);
		}
		catch (JSONException e) {
			throw new IllegalArgumentException(
				"The token service returned " + jsonString);
		}
	}

	protected String parseScopeString(Response tokenResponse) {
		String jsonString = tokenResponse.readEntity(String.class);

		try {
			JSONObject jsonObject = new JSONObject(jsonString);

			return jsonObject.getString("scope");
		}
		catch (JSONException e) {
			throw new IllegalArgumentException(
				"The token service returned " + jsonString);
		}
	}

	protected String parseError(Response tokenResponse) {
		String jsonString = tokenResponse.readEntity(String.class);

		try {
			JSONObject jsonObject = new JSONObject(jsonString);

			return jsonObject.getString("error");
		}
		catch (JSONException e) {
			throw new IllegalArgumentException(
				"The token service returned " + jsonString);
		}
	}

	protected BiFunction<ClientCredentials, WebTarget, Response>
		getAuthorizationCodeGrantExecutor(String redirectURI, String scope) {
	
		return (clientCredentials, webTarget) -> {
			
			webTarget = webTarget.queryParam(
				"client_id", clientCredentials.clientId
			).queryParam(
				"client_secret", clientCredentials.clientSecret
			).queryParam(
				"response_type", "code"
			).queryParam(
				"redirect_uri", redirectURI
			).queryParam(
				"scope", scope
			);

			Invocation.Builder builder = webTarget.request();
			
			if (clientCredentials.hostname != null) {
				builder = builder.header("Host", clientCredentials.hostname);
			}
			
			builder = builder.header("Accept", "text/html");
			
			Response response = builder.get();
			
			Client client = getClient();
			
			Set<Cookie> cookies = new HashSet<>();
			
			while (_isRedirectResponse(response)) {
				
				String redirect = response.getHeaderString("Location");
				
				if (redirect.startsWith(redirectURI)) {
					return response;
				}
				
				String oauth2ReplyTo = _getParameterValue(redirect, "oauth2_reply_to");

				WebTarget redirectWebTarget;
				if (oauth2ReplyTo != null) {
				
					try {
						oauth2ReplyTo = URLDecoder.decode(oauth2ReplyTo, "UTF-8");
					} 
					catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					
					// Simulate user consent
					redirectWebTarget = getDecisionWebTarget(client, redirect, response, oauth2ReplyTo);
					
				}
				else {
					redirectWebTarget = client.target(redirect);
				}
				
				int protoEnd = redirect.indexOf("://");
				int contextStart = redirect.indexOf('/', protoEnd + 3);
				
				String hostname = redirect.substring(protoEnd + 3, contextStart);
				
				Invocation.Builder redirectBuilder = 
					redirectWebTarget.request().header(
						"Host", hostname
					).header(
						"Accept", "text/html"
					);

				for (Cookie cookie : response.getCookies().values()) {
					cookies.add(cookie);					
				}
				
				for (Cookie cookie : cookies) {
					redirectBuilder = redirectBuilder.cookie(cookie);
				}
				
				response = redirectBuilder.get();
			}
			
			return response;
		};
	}

	protected BiFunction<String, Invocation.Builder, Response>
	getClientCredentials(String scope) {

		return (clientId, builder) -> {
			MultivaluedHashMap<String, String> formData =
				new MultivaluedHashMap<>();

			formData.add("client_id", clientId);
			formData.add("client_secret", "oauthTestApplicationSecret");
			formData.add("grant_type", "client_credentials");
			formData.add("scope", scope);

			return builder.post(Entity.form(formData));
		};
	}

	protected Response getClientCredentials(
		String clientId, Invocation.Builder builder) {
		MultivaluedHashMap<String, String> formData =
			new MultivaluedHashMap<>();

		formData.add("client_id", clientId);
		formData.add("client_secret", "oauthTestApplicationSecret");
		formData.add("grant_type", "client_credentials");

		return builder.post(Entity.form(formData));
	}

	protected BiFunction<String, Invocation.Builder, Response>
		getResourceOwnerPassword(String user, String password) {

		return (clientId, builder) -> {
			MultivaluedHashMap<String, String> formData =
				new MultivaluedHashMap<>();

			formData.add("client_id", clientId);
			formData.add("client_secret", "oauthTestApplicationSecret");
			formData.add("grant_type", "password");
			formData.add("username", user);
			formData.add("password", password);

			return builder.post(Entity.form(formData));
		};
	}

	protected BiFunction<String, Invocation.Builder, Response>
		getResourceOwnerPassword(String user, String password, String scope) {

		return (clientId, builder) -> {
			MultivaluedHashMap<String, String> formData =
				new MultivaluedHashMap<>();

			formData.add("client_id", clientId);
			formData.add("client_secret", "oauthTestApplicationSecret");
			formData.add("grant_type", "password");
			formData.add("username", user);
			formData.add("password", password);
			formData.add("scope", scope);

			return builder.post(Entity.form(formData));
		};
	}

	protected BiFunction<ClientCredentials, Invocation.Builder, Response>
		getCodeToAccessTokenExchangeExecutor(String code, String redirectURI) {
	
		return (clientCredentials, builder) -> {
			MultivaluedHashMap<String, String> formData =
				new MultivaluedHashMap<>();
	
			formData.add("client_id", clientCredentials.clientId);
			formData.add("client_secret", clientCredentials.clientSecret);
			formData.add("grant_type", "authorization_code");
			formData.add("code", code);
			formData.add("redirect_uri", redirectURI);
	
			return builder.post(Entity.form(formData));
		};
	}
	
	protected WebTarget getAuthorizeEndpointInvocationWebTarget()
		throws URISyntaxException {
	
		Client client = getClient();
	
		WebTarget tokenTarget = client.target(
			_url.toURI()).path("o").path("oauth2").path("authorize");
	
		return tokenTarget;
	}

	protected Invocation.Builder getTokenInvocationBuilder(String hostname)
		throws URISyntaxException {

		WebTarget tokenTarget = getTokenWebTarget();

		Invocation.Builder builder = tokenTarget.request();

		if (hostname != null) {
			builder = builder.header("Host", hostname);
		}

		return builder;
	}

	protected WebTarget getTokenWebTarget() throws URISyntaxException {
		Client client = getClient();
	
		return client.target(
			_url.toURI()).path("o").path("oauth2").path("token");
	}

	protected static WebTarget getDecisionWebTarget(
		Client client, String locationString, Response response, String oauth2ReplyTo) {
		
		Map<String, String> params = _getParameterMap(locationString);
		Stream<String> stream = params.keySet().stream();
		
		StringBundler sb = new StringBundler(oauth2ReplyTo);
		
		sb.append('?');
		
		stream.filter(
			key -> key.startsWith("oauth2_") && !key.equals("oauth2_reply_to")
		).forEach(key -> {
			sb.append(key.substring("oauth2_".length())).append('=').append(params.get(key)).append('&');
		});
		
		sb.append("oauthDecision=allow");
		
		return client.target(sb.toString());		
	}

	public static void printDocument(Document doc, OutputStream out) throws
		IOException, TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(
			"{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(new DOMSource(doc),
			new StreamResult(new OutputStreamWriter(out, "UTF-8")));
	}

	protected WebTarget getWebTarget(String... paths)
		throws URISyntaxException {

		Client client = getClient();

		WebTarget target = client.target(getUrl().toURI());

		target = target.path("o").path("oauth2-test");

		for (String path : paths) {
			target = target.path(path);
		}

		return target;
	}

	protected WebTarget getJsonWebTarget(String... paths)
		throws URISyntaxException {

		Client client = getClient();

		WebTarget target = client.target(getUrl().toURI());

		target = target.path("api").path("jsonws");

		for (String path : paths) {
			target = target.path(path);
		}

		return target;
	}

	private static boolean _isRedirectResponse(Response response) {
		return 
				response.getStatus() > 300 
				&& response.getStatus() < 400 
				&& response.getHeaderString("Location") != null;
	}

	private static Map<String, String> _getParameterMap(String uRIString) {
		
		int queryStringStartIndex = uRIString.indexOf('?');
		
		if (queryStringStartIndex < 0) {
			return Collections.EMPTY_MAP;
		}
	
		Pattern p = Pattern.compile("[?&]([^$=&]*)=([^$&]*){0,1}");
		
		Matcher m = p.matcher(uRIString.substring(queryStringStartIndex));
		
		Map<String, String> paramMap = new HashMap<>();
		
		while (m.find()) {
			paramMap.put(m.group(1), m.group(2));
		}
		
		return paramMap;
	}

	private static String _getParameterValue(String uRIString, String paramName) {
		Pattern p = Pattern.compile("[?&]" + paramName + "=([^$&]*)");
		
		Matcher m = p.matcher(uRIString);
		
		if (m.find()) {
			return m.group(1);
		}
		
		return null;
	}

	@ArquillianResource
	protected URL _url;
	
	private static class ClientCredentials {
		
		public ClientCredentials(String clientId, String clientSecret, String hostname) {
			this.clientId = clientId;
			this.clientSecret = clientSecret;
			this.hostname = hostname;
		}
		
		public String clientId, clientSecret, hostname;
	}
}