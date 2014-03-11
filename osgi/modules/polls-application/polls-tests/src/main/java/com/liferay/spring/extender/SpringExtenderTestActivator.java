package com.liferay.spring.extender;

import com.liferay.portlet.polls.service.PollsQuestionLocalService;
import com.liferay.portlet.polls.service.PollsQuestionLocalServiceUtil;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Miguel Pastor
 */
public class SpringExtenderTestActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		ServiceReference<PollsQuestionLocalService> serviceReference = context.getServiceReference(PollsQuestionLocalService.class);

		PollsQuestionLocalService service = context.getService(serviceReference);

		System.out.println("Polls questions count " + service.getPollsQuestionsCount());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println(
			"Stopping bundle " + context.getBundle().getSymbolicName());
	}

}