package com.liferay.oauth2.provider.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

@ExtendedObjectClassDefinition(
	category = "foundation"
)
@Meta.OCD(
	id = "com.liferay.oauth2.provider.configuration.DefaultConfigurableScopeMapperRegistratorConfiguration",
	localization = "content/Language", name = "oauth2-default-configurable-scopemapper-configuration-name"
)
public interface DefaultConfigurableScopeMapperRegistratorConfiguration {

	@Meta.AD(
		deflt = "GET\\,HEAD\\,OPTIONS\\=everything.readonly," +
			"PUT\\,POST\\,PATCH\\,DELETE=everything\\,everything.writeonly",
		required = false
	)
	public String[] mapping();

	@Meta.AD(
		deflt = "false", required = false
	)
	public boolean passthrough();
	
	@Meta.AD(
		deflt = "true",
		required = false
	)
	public boolean enabled();

}