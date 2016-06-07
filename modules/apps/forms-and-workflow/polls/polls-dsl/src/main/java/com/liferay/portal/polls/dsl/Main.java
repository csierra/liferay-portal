/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

import static com.liferay.portal.polls.dsl.Monad.sequence;
import static com.liferay.portal.polls.dsl.PollsOperations.addChoice;
import static com.liferay.portal.polls.dsl.PollsOperations.voteFor;

/**
 * @author Carlos Sierra Andrés
 */
public class Main {

	public static void main(String[] args) {
		PollsOperations operations = sequence(
			addChoice(c -> c.name("name").description("a description")),
			voteFor("c")
		);

	}

}
