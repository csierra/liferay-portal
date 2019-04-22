package com.liferay.oauth2.provider.jsonws.internal.scope.spi.application.descriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.oauth2.provider.scope.spi.application.descriptor.ApplicationDescriptor;
import com.liferay.oauth2.provider.scope.spi.scope.descriptor.ScopeDescriptor;

/**
 * @author Stian Sigvartsen
 */
@Component(
	property = {
		"osgi.jaxrs.name=Liferay.Headless.Admin.Taxonomy",
		"osgi.jaxrs.name=Liferay.Headless.Admin.Workflow",
		"osgi.jaxrs.name=Liferay.Headless.Form",
		"osgi.jaxrs.name=Liferay.Headless.Delivery",
		"osgi.jaxrs.name=Liferay.Bulk.REST",
		"osgi.jaxrs.name=Liferay.Headless.Admin.User"
	},
	service = ScopeDescriptor.class
)
public class HeadlessScopeDescriptor 
	implements ScopeDescriptor {
	
	@Override
	public String describeScope(String scope, Locale locale) {
		return ResourceBundleUtil.getString(
			ResourceBundleUtil.getBundle(
				locale, 
				HeadlessScopeDescriptor.class),
			"oauth2.scope." + scope);
	}
}
