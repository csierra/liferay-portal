package com.liferay.portlet.polls.internal;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.polls.service.PollsQuestionLocalService;

/**
 * @author Miguel Pastor
 */
@Component(provide = PollsServiceCounter.class)
public class PollsServiceCounter {

	public int countNumberOfCuestions() throws SystemException {
		return _pollQuestionLocalService.getPollsQuestionsCount();
	}

	@Reference(unbind = "unsetPollsQuestionService")
	public void setPollsQuestionService(
		PollsQuestionLocalService pollsQuestionLocalService) {

		_pollQuestionLocalService = pollsQuestionLocalService;
	}

	public void unsetPollsQuestionService(
		PollsQuestionLocalService pollsQuestionLocalService) {

		_pollQuestionLocalService = null;
	}

	private PollsQuestionLocalService _pollQuestionLocalService;

}
