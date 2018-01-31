package com.liferay.oauth2.provider.configuration;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(
	category = "foundation", 
	scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	id = "com.liferay.oauth2.configuration.OAuth2ClientCredentialsGrantConfiguration",
	localization = "content/Language", name = "oauth2-client-credentials-grant-configuration-name"
)
public interface OAuth2ClientCredentialsGrantConfiguration {
	
	@Meta.AD(
		deflt = "true",
		id = "oauth2.client.crendentials.grant.enabled", 
		name = "oauth2-client-credentials-grant-enabled",
		required = false)
	public boolean enabled();
}
