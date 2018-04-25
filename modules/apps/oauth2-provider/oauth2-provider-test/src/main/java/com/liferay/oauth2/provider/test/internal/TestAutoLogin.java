package com.liferay.oauth2.provider.test.internal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auto.login.AutoLogin;
import com.liferay.portal.kernel.security.auto.login.AutoLoginException;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Validator;

public class TestAutoLogin implements AutoLogin {

	public TestAutoLogin(User user) {
		_user = user;
	}
	
	@Override
	public String[] handleException(
		HttpServletRequest request, HttpServletResponse response, Exception e)
		throws AutoLoginException {
		
		return null;
	}

	@Override
	public String[] login(
		HttpServletRequest request, HttpServletResponse response) 
		throws AutoLoginException {

		if (_user == null) {
			return null;
		}
		
		return new String[] {
			String.valueOf(_user.getUserId()),
			_user.getPassword(),
			Boolean.FALSE.toString()};
	}
	
	private User _user;
}
