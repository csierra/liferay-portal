package com.liferay.oauth2.provider.api.scopes;

import java.util.Map;

@FunctionalInterface
public interface PropertyGetter {

	Object get(String propertyName);

	public static PropertyGetter fromMap(Map<String, Object> map) {
		return map::get;
	}

}
