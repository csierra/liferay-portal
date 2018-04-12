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

import com.liferay.shrinkwrap.osgi.api.BndProjectBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.net.URISyntaxException;
import java.net.URL;

import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.provider.json.JSONProvider;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import org.osgi.framework.BundleActivator;

import org.w3c.dom.Document;

public class BaseClientTest {

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

	protected String getToken(String clientId) throws URISyntaxException {
		Client client = getClient();

		WebTarget tokenTarget = client.target(
			_url.toURI()).path("o").path("oauth2").path("token");

		MultivaluedHashMap<String, String> formData =
			new MultivaluedHashMap<>();

		formData.add("client_id", clientId);
		formData.add("client_secret", "oauthTestApplicationSecret");
		formData.add("grant_type", "client_credentials");

		Response tokenResponse =
			tokenTarget.request().post(Entity.form(formData));

		Document document = tokenResponse.readEntity(Document.class);

		return document.getElementsByTagName(
			"access_token").item(0).getTextContent();
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

	@ArquillianResource
	private URL _url;

}