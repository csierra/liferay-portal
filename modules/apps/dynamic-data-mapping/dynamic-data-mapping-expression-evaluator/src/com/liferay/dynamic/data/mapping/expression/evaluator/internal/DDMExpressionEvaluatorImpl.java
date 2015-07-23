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

package com.liferay.dynamic.data.mapping.expression.evaluator.internal;

import com.liferay.dynamic.data.mapping.expression.evaluator.DDMExpressionEvaluationException;
import com.liferay.dynamic.data.mapping.expression.evaluator.DDMExpressionEvaluator;
import com.liferay.portal.expression.ExpressionFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pablo Carvalho
 */
@Component(immediate = true)
public class DDMExpressionEvaluatorImpl implements DDMExpressionEvaluator {

	public String evaluate(
			DDMForm ddmForm, DDMFormValues ddmFormValues, String languageId)
		throws DDMExpressionEvaluationException {

		try {
			DDMExpressionEvaluatorHelper ddmExpressionEvaluatorHelper =
				new DDMExpressionEvaluatorHelper(
					ddmForm, ddmFormValues, languageId);

			ddmExpressionEvaluatorHelper.setExpressionFactory(
				_expressionFactory);
			ddmExpressionEvaluatorHelper.setJSONFactory(_jsonFactory);

			return ddmExpressionEvaluatorHelper.evaluate();
		}
		catch (PortalException pe) {
			throw new DDMExpressionEvaluationException(pe);
		}
	}

	@Reference
	protected void setExpressionFactory(ExpressionFactory expressionFactory) {
		_expressionFactory = expressionFactory;
	}

	@Reference
	protected void setJSONFactory(JSONFactory jsonFactory) {
		_jsonFactory = jsonFactory;
	}

	private ExpressionFactory _expressionFactory;
	private JSONFactory _jsonFactory;

}