package com.liferay.oauth2.provider.api.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresScope {

	Class<? extends OAuth2Scopes.Scope> value();

}
