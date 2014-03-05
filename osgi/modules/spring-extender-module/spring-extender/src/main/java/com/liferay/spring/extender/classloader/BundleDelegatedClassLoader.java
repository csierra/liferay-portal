package com.liferay.spring.extender.classloader;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.osgi.framework.Bundle;

/**
 * @author Miguel Pastor
 */
public class BundleDelegatedClassLoader extends ClassLoader {

	public BundleDelegatedClassLoader(Bundle bundle) {
		_bundle = bundle;
	}

	@Override
	protected URL findResource(String name) {
		return _bundle.getResource(name);
	}

	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		return _bundle.getResources(name);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return _loadClass(name);
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve)
		throws ClassNotFoundException {

		Class<?> clazz = _loadClass(name);

		if (resolve) {
			resolveClass(clazz);
		}

		return clazz;
	}

	private Class<?> _loadClass(String className)
		throws ClassNotFoundException {

		try {
			return _bundle.loadClass(className);
		}
		catch (ClassNotFoundException cnfe) {
			if (_log.isErrorEnabled()) {
				_log.error(
					"Class " + className + " cannot be loaded by bundle " +
						_bundle.getSymbolicName());
			}

			throw cnfe;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BundleDelegatedClassLoader.class);

	private Bundle _bundle;

}