/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.polls.model.v2;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.util.Function;
import com.liferay.portal.model.GroupedModel;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Carlos Sierra Andrés
 */
public interface PollsQuestionQuerier extends GroupedModel {
	/**
	 * Returns the uuid of this polls question.
	 *
	 * @return the uuid of this polls question
	 */
	@AutoEscape
	String getUuid();

	/**
	 * Returns the question ID of this polls question.
	 *
	 * @return the question ID of this polls question
	 */
	long getQuestionId();

	/**
	 * Returns the create date of this polls question.
	 *
	 * @return the create date of this polls question
	 */
	Date getCreateDate();

	/**
	 * Returns the modified date of this polls question.
	 *
	 * @return the modified date of this polls question
	 */
	Date getModifiedDate();

	/**
	 * Returns the title of this polls question.
	 *
	 * @return the title of this polls question
	 */
	String getTitle();

	/**
	 * Returns the localized title of this polls question in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param locale the locale of the language
	 * @return the localized title of this polls question
	 */
	@AutoEscape
	String getTitle(Locale locale);

	/**
	 * Returns the localized title of this polls question in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param locale the local of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized title of this polls question. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	 */
	@AutoEscape
	String getTitle(Locale locale, boolean useDefault);

	/**
	 * Returns the localized title of this polls question in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @return the localized title of this polls question
	 */
	@AutoEscape
	String getTitle(String languageId);

	/**
	 * Returns the localized title of this polls question in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized title of this polls question
	 */
	@AutoEscape
	String getTitle(String languageId, boolean useDefault);

	@AutoEscape
	String getTitleCurrentLanguageId();

	@AutoEscape
	String getTitleCurrentValue();

	/**
	 * Returns a map of the locales and localized titles of this polls question.
	 *
	 * @return the locales and localized titles of this polls question
	 */
	Map<Locale, String> getTitleMap();

	/**
	 * Returns the description of this polls question.
	 *
	 * @return the description of this polls question
	 */
	String getDescription();

	/**
	 * Returns the localized description of this polls question in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param locale the locale of the language
	 * @return the localized description of this polls question
	 */
	@AutoEscape
	String getDescription(Locale locale);

	/**
	 * Returns the localized description of this polls question in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param locale the local of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized description of this polls question. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	 */
	@AutoEscape
	String getDescription(Locale locale, boolean useDefault);

	/**
	 * Returns the localized description of this polls question in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @return the localized description of this polls question
	 */
	@AutoEscape
	String getDescription(String languageId);

	/**
	 * Returns the localized description of this polls question in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized description of this polls question
	 */
	@AutoEscape
	String getDescription(String languageId, boolean useDefault);

	@AutoEscape
	String getDescriptionCurrentLanguageId();

	@AutoEscape
	String getDescriptionCurrentValue();

	/**
	 * Returns a map of the locales and localized descriptions of this polls question.
	 *
	 * @return the locales and localized descriptions of this polls question
	 */
	Map<Locale, String> getDescriptionMap();

	/**
	 * Returns the expiration date of this polls question.
	 *
	 * @return the expiration date of this polls question
	 */
	Date getExpirationDate();

	/**
	 * Returns the last vote date of this polls question.
	 *
	 * @return the last vote date of this polls question
	 */
	Date getLastVoteDate();

	public <T> List<T> getChoices(Function<PollsChoiceQuerier, T> function);

	public <T> List<T> getVotes(Function<PollsVoteQuerier, T> function);


	int getVotesCount();

	boolean isExpired();
}
