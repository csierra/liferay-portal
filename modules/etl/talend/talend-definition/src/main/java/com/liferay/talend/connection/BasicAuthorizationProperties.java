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

package com.liferay.talend.connection;

import com.liferay.talend.ui.UIKeys;
import com.liferay.talend.utils.PropertiesUtils;

import java.util.EnumSet;

import org.talend.daikon.properties.PropertiesImpl;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

/**
 * @author Igor Beslic
 */
public class BasicAuthorizationProperties extends PropertiesImpl {

	public BasicAuthorizationProperties(String name) {
		super(name);

		anonymousLogin = PropertyFactory.newBoolean("anonymousLogin");
		password = PropertyFactory.newString("password");

		password.setFlags(
			EnumSet.of(
				Property.Flags.ENCRYPT, Property.Flags.SUPPRESS_LOGGING));

		userId = PropertyFactory.newString("userId");
	}

	public void afterAnonymousLogin() {
		getForms().forEach(this::refreshLayout);
	}

	@Override
	public void refreshLayout(Form form) {
		super.refreshLayout(form);

		boolean hidden = false;

		if (anonymousLogin.getValue()) {
			hidden = true;
		}

		PropertiesUtils.setHidden(form, password, hidden);
		PropertiesUtils.setHidden(form, userId, hidden);
	}

	@Override
	public void setupLayout() {
		super.setupLayout();

		Form referenceForm = new Form(this, UIKeys.FORM_BASIC_AUTHORIZATION);

		referenceForm.addRow(anonymousLogin);
		referenceForm.addRow(userId);

		referenceForm.addColumn(password);
	}

	@Override
	public void setupProperties() {
		super.setupProperties();

		password.setValue(UIKeys.LIFERAY_DEFAULT_PASSWORD);
		userId.setValue(UIKeys.LIFERAY_DEFAULT_USER_ID);
	}

	public Property<Boolean> anonymousLogin;
	public Property<String> password;
	public Property<String> userId;

}