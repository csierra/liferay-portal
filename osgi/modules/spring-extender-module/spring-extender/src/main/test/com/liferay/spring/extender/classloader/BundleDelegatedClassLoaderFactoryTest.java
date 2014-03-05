package com.liferay.spring.extender.classloader;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.Mockito;
import org.osgi.framework.Bundle;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Miguel Pastor
 */
@RunWith(PowerMockRunner.class)
public class BundleDelegatedClassLoaderFactoryTest extends PowerMockito {

	@Test
	public void testCreateFirstBundleClassLoader() {
		_mockBundleInfo(_BUNDLE_LAST_MODIFIED);

		ClassLoader classLoader =
			BundleDelegatedClassLoaderFactory.createClassLoader(_bundle);

		Assert.assertEquals(
			classLoader,
			BundleDelegatedClassLoaderFactory.createClassLoader(_bundle));

		_verifyBundleInfo();
	}

	@Test
	public void testCreateClassLoaderOnBundleReload() {
		_mockBundleInfo(_BUNDLE_LAST_MODIFIED);

		ClassLoader classLoader =
			BundleDelegatedClassLoaderFactory.createClassLoader(_bundle);

		_mockBundleInfo(_BUNDLE_LAST_MODIFIED + 1);

		Assert.assertNotEquals(
			classLoader,
			BundleDelegatedClassLoaderFactory.createClassLoader(_bundle));

		_verifyBundleInfo();
	}

	private void _mockBundleInfo(long lastModified) {
		when(
			_bundle.getBundleId()
		).thenReturn(
			_BUNDLE_ID
		);

		when(
			_bundle.getSymbolicName()
		).thenReturn(
			_BUNDLE_SYMBOLIC_NAME
		);

		when(
			_bundle.getLastModified()
		).thenReturn(
			lastModified
		);
	}

	private void _verifyBundleInfo() {
		Mockito.verify(_bundle, Mockito.times(2));

		_bundle.getBundleId();
		_bundle.getSymbolicName();
		_bundle.getLastModified();
	}

	private static final long _BUNDLE_ID = 11;
	private static final long _BUNDLE_LAST_MODIFIED = 1;
	private static final String _BUNDLE_SYMBOLIC_NAME = "spring-test-bundle";

	@Mock
	private Bundle _bundle;

}