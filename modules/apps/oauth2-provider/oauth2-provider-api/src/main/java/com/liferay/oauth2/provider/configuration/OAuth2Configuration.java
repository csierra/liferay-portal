package com.liferay.oauth2.provider.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

@ExtendedObjectClassDefinition(
	category = "foundation", scope = ExtendedObjectClassDefinition.Scope.SYSTEM
)
@Meta.OCD(
	id = "com.liferay.oauth2.provider.configuration.OAuth2Configuration",
	localization = "content/Language", name = "oauth2-configuration-name"
)
public interface OAuth2Configuration {

	@Meta.AD(
		deflt = "true", id = "oauth2.allow.authorization.code.grant",
		name = "oauth2-allow-authorization-code-grant", required = false
	)
	public boolean allowAuthorizationCodeGrant();

	@Meta.AD(
		deflt = "true",
		id = "oauth2.allow.resource.owner.password.credentials.grant",
		name = "oauth2-allow-resource-owner-password-credentials-grant",
		required = false
	)
	public boolean allowResourceOwnerPasswordCredentialsGrant();

	@Meta.AD(
		deflt = "true", id = "oauth2.allow.client.credentials.grant",
		name = "oauth2-allow-client-credentials-grant", required = false
	)
	public boolean allowClientCredentialsGrant();

	@Meta.AD(
		deflt = "true", id = "oauth2.allow.refresh.token.grant",
		name = "oauth2-allow-refresh-token-grant", required = false
	)
	public boolean allowRefreshTokenGrant();

}