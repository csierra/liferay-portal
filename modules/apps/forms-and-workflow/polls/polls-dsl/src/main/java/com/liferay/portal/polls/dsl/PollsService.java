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

import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public interface PollsService {

	PollsContext.SimplePollsContext create(
		Function<QuestionBuilder, QuestionBuilder.Final> builderFunction);

	PollsContext.SimplePollsContext findById(long id);

	PollsContext.MultiplePollsContext all();

	PollsContext.MultiplePollsContext findBy(FinderBuilder finderBuilder);

	interface FinderBuilder {
		<F extends FinderBuilder & Final> F byTitle(String title);
		<F extends FinderBuilder & Final> F byDescription(String description);

		interface Final {}
	}
}
