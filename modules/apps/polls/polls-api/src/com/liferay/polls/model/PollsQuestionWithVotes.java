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

import com.liferay.kernel.LazyPageableResult;
import com.liferay.polls.service.PollsVoteLocalService;
import com.liferay.portal.LocaleException;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.lar.StagedModelType;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Carlos Sierra Andrés
 */
public class PollsQuestionWithVotes extends PollsQuestion {

	@Override
	public void addChoice(PollsChoice pollsChoice) {
		_question.addChoice(pollsChoice);
	}

	@Override
	public void addChoices(List<PollsChoice> choices) {
		_question.addChoices(choices);
	}

	@Override
	public List<PollsChoice> getChoices() {
		return _question.getChoices();
	}

	@Override
	public boolean isExpired() {
		return _question.isExpired();
	}

	@Override
	public boolean isExpired(ServiceContext serviceContext, Date defaultCreateDate) {
		return _question.isExpired(serviceContext, defaultCreateDate);
	}

	@Override
	public void setGroupPermissions(String... permissions) {
		_question.setGroupPermissions(permissions);
	}

	@Override
	public void setGuestPermissions(String... permissions) {
		_question.setGuestPermissions(permissions);
	}

	@Override
	public String[] getGroupPermissions() {
		return _question.getGroupPermissions();
	}

	@Override
	public String[] getGuestPermissions() {
		return _question.getGuestPermissions();
	}

	@Override
	public void validate() throws PortalException {
		_question.validate();
	}

	@Override
	public long getPrimaryKey() {
		return _question.getPrimaryKey();
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		_question.setPrimaryKey(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _question.getPrimaryKeyObj();
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_question.setPrimaryKeyObj(primaryKeyObj);
	}

	@Override
	public Class<?> getModelClass() {
		return _question.getModelClass();
	}

	@Override
	public String getModelClassName() {
		return _question.getModelClassName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		return _question.getModelAttributes();
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		_question.setModelAttributes(attributes);
	}

	@JSON
	@Override
	public String getUuid() {
		return _question.getUuid();
	}

	@Override
	public void setUuid(String uuid) {
		_question.setUuid(uuid);
	}

	@Override
	public String getOriginalUuid() {
		return _question.getOriginalUuid();
	}

	@JSON
	@Override
	public long getQuestionId() {
		return _question.getQuestionId();
	}

	@Override
	public void setQuestionId(long questionId) {
		_question.setQuestionId(questionId);
	}

	@JSON
	@Override
	public long getGroupId() {
		return _question.getGroupId();
	}

	@Override
	public void setGroupId(long groupId) {
		_question.setGroupId(groupId);
	}

	@Override
	public long getOriginalGroupId() {
		return _question.getOriginalGroupId();
	}

	@JSON
	@Override
	public long getCompanyId() {
		return _question.getCompanyId();
	}

	@Override
	public void setCompanyId(long companyId) {
		_question.setCompanyId(companyId);
	}

	@Override
	public long getOriginalCompanyId() {
		return _question.getOriginalCompanyId();
	}

	@JSON
	@Override
	public long getUserId() {
		return _question.getUserId();
	}

	@Override
	public void setUserId(long userId) {
		_question.setUserId(userId);
	}

	@Override
	public String getUserUuid() {
		return _question.getUserUuid();
	}

	@Override
	public void setUserUuid(String userUuid) {
		_question.setUserUuid(userUuid);
	}

	@JSON
	@Override
	public String getUserName() {
		return _question.getUserName();
	}

	@Override
	public void setUserName(String userName) {
		_question.setUserName(userName);
	}

	@JSON
	@Override
	public Date getCreateDate() {
		return _question.getCreateDate();
	}

	@Override
	public void setCreateDate(Date createDate) {
		_question.setCreateDate(createDate);
	}

	@JSON
	@Override
	public Date getModifiedDate() {
		return _question.getModifiedDate();
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_question.setModifiedDate(modifiedDate);
	}

	@JSON
	@Override
	public String getTitle() {
		return _question.getTitle();
	}

	@Override
	public String getTitle(Locale locale) {
		return _question.getTitle(locale);
	}

	@Override
	public String getTitle(Locale locale, boolean useDefault) {
		return _question.getTitle(locale, useDefault);
	}

	@Override
	public String getTitle(String languageId) {
		return _question.getTitle(languageId);
	}

	@Override
	public String getTitle(String languageId, boolean useDefault) {
		return _question.getTitle(languageId, useDefault);
	}

	@Override
	public String getTitleCurrentLanguageId() {
		return _question.getTitleCurrentLanguageId();
	}

	@JSON
	@Override
	public String getTitleCurrentValue() {
		return _question.getTitleCurrentValue();
	}

	@Override
	public Map<Locale, String> getTitleMap() {
		return _question.getTitleMap();
	}

	@Override
	public void setTitle(String title) {
		_question.setTitle(title);
	}

	@Override
	public void setTitle(String title, Locale locale) {
		_question.setTitle(title, locale);
	}

	@Override
	public void setTitle(String title, Locale locale, Locale defaultLocale) {
		_question.setTitle(title, locale, defaultLocale);
	}

	@Override
	public void setTitleCurrentLanguageId(String languageId) {
		_question.setTitleCurrentLanguageId(languageId);
	}

	@Override
	public void setTitleMap(Map<Locale, String> titleMap) {
		_question.setTitleMap(titleMap);
	}

	@Override
	public void setTitleMap(Map<Locale, String> titleMap, Locale defaultLocale) {
		_question.setTitleMap(titleMap, defaultLocale);
	}

	@JSON
	@Override
	public String getDescription() {
		return _question.getDescription();
	}

	@Override
	public String getDescription(Locale locale) {
		return _question.getDescription(locale);
	}

	@Override
	public String getDescription(Locale locale, boolean useDefault) {
		return _question.getDescription(locale, useDefault);
	}

	@Override
	public String getDescription(String languageId) {
		return _question.getDescription(languageId);
	}

	@Override
	public String getDescription(String languageId, boolean useDefault) {
		return _question.getDescription(languageId, useDefault);
	}

	@Override
	public String getDescriptionCurrentLanguageId() {
		return _question.getDescriptionCurrentLanguageId();
	}

	@JSON
	@Override
	public String getDescriptionCurrentValue() {
		return _question.getDescriptionCurrentValue();
	}

	@Override
	public Map<Locale, String> getDescriptionMap() {
		return _question.getDescriptionMap();
	}

	@Override
	public void setDescription(String description) {
		_question.setDescription(description);
	}

	@Override
	public void setDescription(String description, Locale locale) {
		_question.setDescription(description, locale);
	}

	@Override
	public void setDescription(String description, Locale locale, Locale defaultLocale) {
		_question.setDescription(description, locale, defaultLocale);
	}

	@Override
	public void setDescriptionCurrentLanguageId(String languageId) {
		_question.setDescriptionCurrentLanguageId(languageId);
	}

	@Override
	public void setDescriptionMap(Map<Locale, String> descriptionMap) {
		_question.setDescriptionMap(descriptionMap);
	}

	@Override
	public void setDescriptionMap(Map<Locale, String> descriptionMap, Locale defaultLocale) {
		_question.setDescriptionMap(descriptionMap, defaultLocale);
	}

	@JSON
	@Override
	public Date getExpirationDate() {
		return _question.getExpirationDate();
	}

	@Override
	public void setExpirationDate(Date expirationDate) {
		_question.setExpirationDate(expirationDate);
	}

	@JSON
	@Override
	public Date getLastVoteDate() {
		return _question.getLastVoteDate();
	}

	@Override
	public void setLastVoteDate(Date lastVoteDate) {
		_question.setLastVoteDate(lastVoteDate);
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _question.getStagedModelType();
	}

	@Override
	public long getColumnBitmask() {
		return _question.getColumnBitmask();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _question.getExpandoBridge();
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_question.setExpandoBridgeAttributes(serviceContext);
	}

	@Override
	public String[] getAvailableLanguageIds() {
		return _question.getAvailableLanguageIds();
	}

	@Override
	public String getDefaultLanguageId() {
		return _question.getDefaultLanguageId();
	}

	@Override
	public void prepareLocalizedFieldsForImport() throws LocaleException {
		_question.prepareLocalizedFieldsForImport();
	}

	@Override
	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale) throws LocaleException {
		_question.prepareLocalizedFieldsForImport(defaultImportLocale);
	}

	@Override
	public PollsQuestion toEscapedModel() {
		return _question.toEscapedModel();
	}

	@Override
	public Object clone() {
		return _question.clone();
	}

	@Override
	public int compareTo(PollsQuestion pollsQuestion) {
		return _question.compareTo(pollsQuestion);
	}

	@Override
	public boolean equals(Object obj) {
		return _question.equals(obj);
	}

	@Override
	public int hashCode() {
		return _question.hashCode();
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _question.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _question.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_question.resetOriginalValues();
	}

	@Override
	public CacheModel<PollsQuestion> toCacheModel() {
		return _question.toCacheModel();
	}

	@Override
	public String toString() {
		return _question.toString();
	}

	@Override
	public String toXmlString() {
		return _question.toXmlString();
	}

	@Override
	public boolean isCachedModel() {
		return _question.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _question.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _question.isNew();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_question.setCachedModel(cachedModel);
	}

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
		_question.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_question.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setNew(boolean n) {
		_question.setNew(n);
	}

	@Override
	public PollsQuestion toUnescapedModel() {
		return _question.toUnescapedModel();
	}

	protected PollsQuestionWithVotes(
		PollsQuestion question, LazyPageableResult<PollsVote> votes) {

		_question = question;
		_votes = votes;
	}

	public LazyPageableResult<PollsVote> getVotes() {
		return _votes;
	}

	public int getVotesCount() {
		//FIXME: We should not implement this like this

		return _votes.all().size();
	}

	protected PollsQuestion getWrappedQuestion() {
		return _question;
	}

	private PollsQuestion _question;
	private LazyPageableResult<PollsVote> _votes;
}
