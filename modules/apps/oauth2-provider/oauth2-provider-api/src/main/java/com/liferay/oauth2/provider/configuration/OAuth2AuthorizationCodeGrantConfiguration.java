package com.liferay.oauth2.provider.configuration;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(
	category = "foundation", 
	scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	id = "com.liferay.oauth2.configuration.OAuth2AuthorizationCodeGrantConfiguration",
	localization = "content/Language", name = "oauth2-authorization-code-grant-configuration-name"
)
public interface OAuth2AuthorizationCodeGrantConfiguration {
	
	@Meta.AD(
		deflt = "true",
		id = "oauth2.authorization.code.grant.enabled", 
		name = "oauth2-authorization-code-grant-enabled",
		required = false)
	public boolean enabled();
}
