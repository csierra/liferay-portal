/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.polls.dsl;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public interface QuestionBuilder {

	default Step1 title(String title) {
		return title(new HashMap<Locale, String>() {{
			put(Locale.getDefault(), title);
		}});
	}

	Step1 title(Map<Locale, String> titles);

	interface Step1 {
		default Step2 description(String description) {
			return description(new HashMap<Locale, String>() {{
				put(Locale.getDefault(), description);
			}});
		}

		Step2 description(Map<Locale, String> descriptions);
	}

	interface Step2 {
		Step3 expires(Date date);
		Step3 neverExpires();
	}

	interface Step3 {
		Step4 addChoice(
			Function<PollsChoiceBuilder, PollsChoiceBuilder.Final>
				choiceBuilder);
	}

	interface Step4 {
		Final addChoice(
			Function<PollsChoiceBuilder, PollsChoiceBuilder.Final>
				choiceBuilder);
	}

	interface Final {
		Final addChoice(
			Function<PollsChoiceBuilder, PollsChoiceBuilder.Final>
				choiceBuilder);
	}
}
