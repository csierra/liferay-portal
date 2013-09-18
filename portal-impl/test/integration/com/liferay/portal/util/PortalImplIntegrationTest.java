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

package com.liferay.portal.util;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Manuel de la Peña
 */
@ExecutionTestListeners(listeners = {EnvironmentExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class PortalImplIntegrationTest {

	@Test
	public void testGetI18nPathDuplicatedLanguageCode() {
		testGetI18nPathLanguageId(Locale.CANADA_FRENCH, StringPool.BLANK);
	}

	@Test
	public void testGetI18nPathDuplicatedLanguageCodeDefaultLocale() {
		Locale locale = Locale.CANADA_FRENCH;

		testGetI18nPathLanguageId(locale, LocaleUtil.toLanguageId(locale));
	}

	@Test
	public void testGetI18nPathNotDuplicatedLanguageCode() {
		testGetI18nPathLanguageId(Locale.GERMANY, StringPool.BLANK);
	}

	@Test
	public void testGetI18nPathNotDuplicatedLanguageCodeDefaultLocale() {
		Locale locale = Locale.GERMANY;

		testGetI18nPathLanguageId(locale, LocaleUtil.toLanguageId(locale));
	}

	@Test
	public void testGetI18nPathDuplicatedPriorityLanguageCode() {
		testGetI18nPathLanguageId(Locale.FRANCE, StringPool.BLANK);
	}

	protected void testGetI18nPathLanguageId(
		Locale locale, String defaultI18nPathLanguageId) {

		String i18nPathLanguageId = PortalUtil.getI18nPathLanguageId(
			locale, defaultI18nPathLanguageId);

		Assert.assertEquals(locale.getLanguage(), i18nPathLanguageId);
	}

}