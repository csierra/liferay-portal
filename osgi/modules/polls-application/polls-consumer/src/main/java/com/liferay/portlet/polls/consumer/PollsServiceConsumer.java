package com.liferay.portlet.polls.consumer;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.polls.internal.PollsServiceCounter;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;

import java.util.Dictionary;

/**
 * @author Miguel Pastor
 */

@Component(
	immediate = true,
	properties = { "service.pid=polls.service.consumer.configuration" }
)
public class PollsServiceConsumer implements ManagedService {

	@Activate
	public void activate(ComponentContext componentContext)
		throws SystemException {

		System.out.println(
			"Number of registered questions " +
				_pollServiceCounter.countNumberOfCuestions());
	}

	@Reference(unbind = "unsetConfigurationAdmin")
	public void setConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
		_configurationAdmin = configurationAdmin;
	}

	public void unsetConfigurationAdmin(ConfigurationAdmin configurationAdmin){
		_configurationAdmin = null;
	}

	@Reference (unbind = "unsetPollsServiceCounter")
	public void setPollsServiceCounter(
		PollsServiceCounter pollsServiceCounter) {

		_pollServiceCounter = pollsServiceCounter;
	}

	public void unsetPollsServiceCounter(
		PollsServiceCounter pollsServiceCounter) {

		System.out.println(
			"Unregistering service. Comsumer will not work anymore");

		pollsServiceCounter = null;
	}

	private ConfigurationAdmin _configurationAdmin;
	private PollsServiceCounter _pollServiceCounter;

	@Override
	public void updated(Dictionary<String, ?> properties)
		throws ConfigurationException {

		if (properties == null) {
			return;
		}

		System.out.println("Somebody has configured me with new " + properties);
		System.out.println("The new value of key is " + properties.get("key"));
	}


}
