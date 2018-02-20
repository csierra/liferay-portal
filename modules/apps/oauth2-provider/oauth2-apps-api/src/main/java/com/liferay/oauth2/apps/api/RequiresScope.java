package com.liferay.oauth2.apps.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresScope {

	String[] value();

	boolean allNeeded() default true;

}