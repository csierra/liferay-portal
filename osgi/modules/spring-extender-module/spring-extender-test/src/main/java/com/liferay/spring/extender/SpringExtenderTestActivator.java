package com.liferay.spring.extender;

import com.liferay.portlet.polls.service.PollsQuestionLocalServiceUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author Miguel Pastor
 */
public class SpringExtenderTestActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		int pollsQuestionsCount =
			PollsQuestionLocalServiceUtil.getPollsQuestionsCount();

		System.out.println("Polls questions count " + pollsQuestionsCount);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println(
			"Stopping bundle " + context.getBundle().getSymbolicName());
	}
}