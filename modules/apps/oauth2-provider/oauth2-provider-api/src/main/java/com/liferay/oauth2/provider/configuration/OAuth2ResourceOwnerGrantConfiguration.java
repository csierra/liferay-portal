package com.liferay.oauth2.provider.configuration;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(
	category = "foundation", 
	scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	id = "com.liferay.oauth2.configuration.OAuth2ResourceOwnerGrantConfiguration",
	localization = "content/Language", name = "oauth2-resource-owner-grant-configuration-name"
)
public interface OAuth2ResourceOwnerGrantConfiguration {
	
	@Meta.AD(
		deflt = "true",
		id = "oauth2.resource.owner.grant.enabled", 
		name = "oauth2-resource-owner-grant-enabled",
		required = false) 
	public boolean enabled();
}