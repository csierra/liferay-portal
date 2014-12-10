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

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Carlos Sierra Andrés
 */
public interface PollsChoiceQuerier {
	/*
		 * NOTE FOR DEVELOPERS:
		 *
		 * Never modify this interface directly. Add methods to {@link com.liferay.polls.model.impl.PollsChoiceImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
		 */
	int getVotesCount();

	/**
	 * Returns the uuid of this polls choice.
	 *
	 * @return the uuid of this polls choice
	 */
	@AutoEscape
	String getUuid();

	/**
	 * Returns the choice ID of this polls choice.
	 *
	 * @return the choice ID of this polls choice
	 */
	long getChoiceId();

	/**
	 * Returns the user ID of this polls choice.
	 *
	 * @return the user ID of this polls choice
	 */
	long getUserId();

	/**
	 * Returns the create date of this polls choice.
	 *
	 * @return the create date of this polls choice
	 */
	Date getCreateDate();

	/**
	 * Returns the modified date of this polls choice.
	 *
	 * @return the modified date of this polls choice
	 */
	Date getModifiedDate();

	/**
	 * Returns the name of this polls choice.
	 *
	 * @return the name of this polls choice
	 */
	@AutoEscape
	String getName();

	/**
	 * Returns the description of this polls choice.
	 *
	 * @return the description of this polls choice
	 */
	String getDescription();

	/**
	 * Returns the localized description of this polls choice in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param locale the locale of the language
	 * @return the localized description of this polls choice
	 */
	@AutoEscape
	String getDescription(Locale locale);

	/**
	 * Returns the localized description of this polls choice in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param locale     the local of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized description of this polls choice. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	 */
	@AutoEscape
	String getDescription(Locale locale, boolean useDefault);

	/**
	 * Returns the localized description of this polls choice in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @return the localized description of this polls choice
	 */
	@AutoEscape
	String getDescription(String languageId);

	/**
	 * Returns the localized description of this polls choice in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized description of this polls choice
	 */
	@AutoEscape
	String getDescription(String languageId, boolean useDefault);

	@AutoEscape
	String getDescriptionCurrentLanguageId();

	@AutoEscape
	String getDescriptionCurrentValue();

	/**
	 * Returns a map of the locales and localized descriptions of this polls choice.
	 *
	 * @return the locales and localized descriptions of this polls choice
	 */
	Map<Locale, String> getDescriptionMap();

	public <T> T getQuestion(Function<PollsQuestionQuerier, T> function);

}