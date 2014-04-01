package com.liferay.portlet.polls.console;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.polls.internal.PollsServiceCounter;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author Miguel Pastor
 */

@Component(
	properties = {
		"osgi.command.function=updateConfig",
		"osgi.command.scope=polls"
	},
	provide = Object.class,
	immediate = true
)
public class PollsServiceConsumerCommand {

	@Activate
	public void activate (ComponentContext componentContext) {

	}

	@Deactivate
	public void deactive(ComponentContext componentContext) {


	}

	public void updateConfig(String newValue) {
		Configuration configuration = getConfiguration();

		Dictionary<String, Object> properties = configuration.getProperties();

		if (properties == null) {
			properties = new Hashtable<String, Object>();
		}

		properties.put("key", newValue);

		try {
			configuration.update(properties);
		} catch (IOException e) {
			System.err.println("An error has occurred updating the properties");
		}
	}

	@Reference(unbind = "unsetConfigurationAdmin")
	public void setConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
		_configurationAdmin = configurationAdmin;
	}

	public void unsetConfigurationAdmin(ConfigurationAdmin configurationAdmin){
		_configurationAdmin = null;
	}

	private Configuration getConfiguration() {
		if (_configurationAdmin == null) {
			throw new IllegalStateException("Config admin is missing");
		}

		try {
			return _configurationAdmin.getConfiguration(
				"polls.service.consumer.configuration");
		}
		catch (IOException e) {
			System.err.println(
				"An unexpected error has occurred while retrieving " +
					"the configuration polls.service.consumer.configuration");
		}

		return null;
	}

	private ConfigurationAdmin _configurationAdmin;

}
