package com.liferay.oauth2.provider.internal.configuration;

import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.liferay.oauth2.provider.configuration.DefaultConfigurableScopeMapperRegistratorConfiguration;
import com.liferay.oauth2.provider.scope.impl.scopemapper.ConfigurableScopeMapper;
import com.liferay.oauth2.provider.scope.spi.scopemapper.ScopeMapper;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.util.GetterUtil;

@Component(
	immediate = true,
	configurationPid = 
		"com.liferay.oauth2.provider.configuration."
			+ "DefaultConfigurableScopeMapperRegistratorConfiguration"
)
public class DefaultConfigurableScopeMapperRegistrator {

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {
		
		boolean enabled = 
			GetterUtil.getBoolean(properties.get("enabled"), true);
		
		if (enabled) {
			
			DefaultConfigurableScopeMapperRegistratorConfiguration configuration = 
				ConfigurableUtil.createConfigurable(
					DefaultConfigurableScopeMapperRegistratorConfiguration.class, properties);
			
			Hashtable<String, Object> scopeMapperProperties = new Hashtable<>();
			
			scopeMapperProperties.put("default", true);
			
			_defaultScopeMapperServiceRegistration = 
				bundleContext.registerService(
					ScopeMapper.class, 
					new ConfigurableScopeMapper(
						configuration.mapping(), configuration.passthrough()), 
					scopeMapperProperties);
		}
	}
	
	@Deactivate
	protected void deactivate() {
		if (_defaultScopeMapperServiceRegistration != null) {
			_defaultScopeMapperServiceRegistration.unregister();
		}
	}
	
	private ServiceRegistration<ScopeMapper> _defaultScopeMapperServiceRegistration;
}
