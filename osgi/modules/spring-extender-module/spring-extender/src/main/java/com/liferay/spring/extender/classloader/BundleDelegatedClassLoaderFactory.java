package com.liferay.spring.extender.classloader;

import org.osgi.framework.Bundle;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Miguel Pastor
 */
public class BundleDelegatedClassLoaderFactory {

	public static ClassLoader createClassLoader(Bundle bundle) {
		BundleClassLoaders bundleClassLoaders;

		synchronized (_classLoadersCache) {
			bundleClassLoaders = _classLoadersCache.get(bundle);

			if (bundleClassLoaders == null) {
				bundleClassLoaders = new BundleClassLoaders();

				_classLoadersCache.put(bundle, bundleClassLoaders);
			}

			return bundleClassLoaders.createClassLoader(bundle);
		}
	}

	private static WeakHashMap<Bundle, BundleClassLoaders> _classLoadersCache =
		new WeakHashMap<Bundle, BundleClassLoaders>();

	private static class BundleClassLoaders {

		public ClassLoader createClassLoader(Bundle bundle) {
			String key = _composeBundleKey(bundle);

			synchronized(_bundleClassLoaders) {

				WeakReference<ClassLoader> classLoaderWeakReference =
					_bundleClassLoaders.get(key);

				if (classLoaderWeakReference != null) {
					return classLoaderWeakReference.get();
				}

				BundleDelegatedClassLoader bundleDelegatedClassLoader =
					new BundleDelegatedClassLoader(bundle);

				_bundleClassLoaders.put(
					key,
					new WeakReference<ClassLoader>(bundleDelegatedClassLoader));

				return bundleDelegatedClassLoader;
			}
		}


		private String _composeBundleKey(Bundle bundle) {
			StringBuilder sb = new StringBuilder(5);

			sb.append(bundle.getBundleId());
			sb.append(_DELIMITER);
			sb.append(bundle.getSymbolicName());
			sb.append(_DELIMITER);
			sb.append(Long.toHexString(bundle.getLastModified()));

			return sb.toString();
		}

		private static final String _DELIMITER = "#";

		private Map<String, WeakReference<ClassLoader>> _bundleClassLoaders =
			new HashMap<String, WeakReference<ClassLoader>>();
	}

}