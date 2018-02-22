package com.liferay.oauth2.provider.configuration;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(
	category = "foundation", factoryInstanceLabelAttribute = "companyId",
	scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	factory = true,
	id = "com.liferay.oauth2.provider.configuration.DefaultScopeMapperConfiguration",
	localization = "content/Language", name = "oauth2-default-scopemapper-configuration-name"
)
public interface DefaultScopeMapperConfiguration {
	
	@Meta.AD(name="company-id", required = false)
	public long companyId();	
	
	@Meta.AD(
		deflt = "GET\\,HEAD\\,OPTIONS\\=everything.readonly," 
			+ "PUT\\,POST\\,PATCH\\,DELETE=everything\\,everything.writeonly",
		id = "oauth2.default.scopemapper.mapping", 
		name = "oauth2-default-scopemapper-mapping",
		required = false)
	public String[] mapping();
	
	@Meta.AD(
		deflt = "false",
		id = "oauth2.default.scopemapper.passthrough",
		name = "oauth2-default-scopemapper-passthrough",
		required = false
	)
	public boolean passthrough();	
}
