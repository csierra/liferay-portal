package com.liferay.oauth2.provider.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

@ExtendedObjectClassDefinition(
	category = "foundation", scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	id = "com.liferay.oauth2.provider.configuration.OAuth2RefreshTokenGrantConfiguration",
	localization = "content/Language", name = "oauth2-refresh-token-grant-configuration-name"
)
public interface OAuth2RefreshTokenGrantConfiguration {

	@Meta.AD(
		deflt = "true", id = "oauth2.refresh.token.grant.enabled",
		name = "oauth2-refresh-token-grant-enabled", required = false
	)
	public boolean enabled();

}