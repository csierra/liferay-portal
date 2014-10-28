/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.polls.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.LocaleException;
import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.LocalizedModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * The base model interface for the PollsQuestion service. Represents a row in the &quot;PollsQuestion&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see PollsQuestion
 * @see PollsQuestionModelImpl
 * @generated
 */
@ProviderType
public interface PollsQuestionModel extends BaseModel<PollsQuestion>,
	LocalizedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a polls question model instance should use the {@link PollsQuestion} interface instead.
	 */

	/**
	 * Returns the primary key of this polls question.
	 *
	 * @return the primary key of this polls question
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this polls question.
	 *
	 * @param primaryKey the primary key of this polls question
	 */
	public void setPrimaryKey(long primaryKey);


	public long getQuestionId();

	/**
	 * Sets the question ID of this polls question.
	 *
	 * @param questionId the question ID of this polls question
	 */
	public void setQuestionId(long questionId);

	/**
	 * Returns the title of this polls question.
	 *
	 * @return the title of this polls question
	 */
	public String getTitle();

	/**
	 * Returns the localized title of this polls question in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param locale the locale of the language
	 * @return the localized title of this polls question
	 */
	@AutoEscape
	public String getTitle(Locale locale);

	/**
	 * Returns the localized title of this polls question in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param locale the local of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized title of this polls question. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	 */
	@AutoEscape
	public String getTitle(Locale locale, boolean useDefault);

	/**
	 * Returns the localized title of this polls question in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @return the localized title of this polls question
	 */
	@AutoEscape
	public String getTitle(String languageId);

	/**
	 * Returns the localized title of this polls question in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized title of this polls question
	 */
	@AutoEscape
	public String getTitle(String languageId, boolean useDefault);

	@AutoEscape
	public String getTitleCurrentLanguageId();

	@AutoEscape
	public String getTitleCurrentValue();

	/**
	 * Returns a map of the locales and localized titles of this polls question.
	 *
	 * @return the locales and localized titles of this polls question
	 */
	public Map<Locale, String> getTitleMap();

	/**
	 * Sets the title of this polls question.
	 *
	 * @param title the title of this polls question
	 */
	public void setTitle(String title);

	/**
	 * Sets the localized title of this polls question in the language.
	 *
	 * @param title the localized title of this polls question
	 * @param locale the locale of the language
	 */
	public void setTitle(String title, Locale locale);

	/**
	 * Sets the localized title of this polls question in the language, and sets the default locale.
	 *
	 * @param title the localized title of this polls question
	 * @param locale the locale of the language
	 * @param defaultLocale the default locale
	 */
	public void setTitle(String title, Locale locale, Locale defaultLocale);

	public void setTitleCurrentLanguageId(String languageId);

	/**
	 * Sets the localized titles of this polls question from the map of locales and localized titles.
	 *
	 * @param titleMap the locales and localized titles of this polls question
	 */
	public void setTitleMap(Map<Locale, String> titleMap);

	/**
	 * Sets the localized titles of this polls question from the map of locales and localized titles, and sets the default locale.
	 *
	 * @param titleMap the locales and localized titles of this polls question
	 * @param defaultLocale the default locale
	 */
	public void setTitleMap(Map<Locale, String> titleMap, Locale defaultLocale);

	/**
	 * Returns the description of this polls question.
	 *
	 * @return the description of this polls question
	 */
	public String getDescription();

	/**
	 * Returns the localized description of this polls question in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param locale the locale of the language
	 * @return the localized description of this polls question
	 */
	@AutoEscape
	public String getDescription(Locale locale);

	/**
	 * Returns the localized description of this polls question in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param locale the local of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized description of this polls question. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	 */
	@AutoEscape
	public String getDescription(Locale locale, boolean useDefault);

	/**
	 * Returns the localized description of this polls question in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @return the localized description of this polls question
	 */
	@AutoEscape
	public String getDescription(String languageId);

	/**
	 * Returns the localized description of this polls question in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized description of this polls question
	 */
	@AutoEscape
	public String getDescription(String languageId, boolean useDefault);

	@AutoEscape
	public String getDescriptionCurrentLanguageId();

	@AutoEscape
	public String getDescriptionCurrentValue();

	/**
	 * Returns a map of the locales and localized descriptions of this polls question.
	 *
	 * @return the locales and localized descriptions of this polls question
	 */
	public Map<Locale, String> getDescriptionMap();

	/**
	 * Sets the description of this polls question.
	 *
	 * @param description the description of this polls question
	 */
	public void setDescription(String description);

	/**
	 * Sets the localized description of this polls question in the language.
	 *
	 * @param description the localized description of this polls question
	 * @param locale the locale of the language
	 */
	public void setDescription(String description, Locale locale);

	/**
	 * Sets the localized description of this polls question in the language, and sets the default locale.
	 *
	 * @param description the localized description of this polls question
	 * @param locale the locale of the language
	 * @param defaultLocale the default locale
	 */
	public void setDescription(String description, Locale locale,
		Locale defaultLocale);

	public void setDescriptionCurrentLanguageId(String languageId);

	/**
	 * Sets the localized descriptions of this polls question from the map of locales and localized descriptions.
	 *
	 * @param descriptionMap the locales and localized descriptions of this polls question
	 */
	public void setDescriptionMap(Map<Locale, String> descriptionMap);

	/**
	 * Sets the localized descriptions of this polls question from the map of locales and localized descriptions, and sets the default locale.
	 *
	 * @param descriptionMap the locales and localized descriptions of this polls question
	 * @param defaultLocale the default locale
	 */
	public void setDescriptionMap(Map<Locale, String> descriptionMap,
		Locale defaultLocale);

	/**
	 * Returns the expiration date of this polls question.
	 *
	 * @return the expiration date of this polls question
	 */
	public Date getExpirationDate();

	/**
	 * Sets the expiration date of this polls question.
	 *
	 * @param expirationDate the expiration date of this polls question
	 */
	public void setExpirationDate(Date expirationDate);

	/**
	 * Returns the last vote date of this polls question.
	 *
	 * @return the last vote date of this polls question
	 */
	public Date getLastVoteDate();

	/**
	 * Sets the last vote date of this polls question.
	 *
	 * @param lastVoteDate the last vote date of this polls question
	 */
	public void setLastVoteDate(Date lastVoteDate);

	@Override
	public boolean isNew();

	@Override
	public void setNew(boolean n);

	@Override
	public boolean isCachedModel();

	@Override
	public void setCachedModel(boolean cachedModel);

	@Override
	public boolean isEscapedModel();

	@Override
	public Serializable getPrimaryKeyObj();

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	@Override
	public ExpandoBridge getExpandoBridge();

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel);

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge);

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	@Override
	public String[] getAvailableLanguageIds();

	@Override
	public String getDefaultLanguageId();

	@Override
	public void prepareLocalizedFieldsForImport() throws LocaleException;

	@Override
	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale)
		throws LocaleException;

	@Override
	public Object clone();

	@Override
	public int compareTo(PollsQuestion pollsQuestion);

	@Override
	public int hashCode();

	@Override
	public CacheModel<PollsQuestion> toCacheModel();

	@Override
	public PollsQuestion toEscapedModel();

	@Override
	public PollsQuestion toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}