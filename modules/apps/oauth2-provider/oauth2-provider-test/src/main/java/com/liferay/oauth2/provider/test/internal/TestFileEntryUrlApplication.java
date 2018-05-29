package com.liferay.oauth2.provider.test.internal;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.core.Application;

/**
 * @author Víctor Galán Grande
 */
public class TestFileEntryUrlApplication extends Application {

	public TestFileEntryUrlApplication(String fileEntryUrl) {
		_fileEntryUrl = fileEntryUrl;
	}

	@GET
	public String getFileEntryUrl() {
		return _fileEntryUrl;
	}

	@Override
	public Set<Object> getSingletons() {
		return Collections.<Object>singleton(this);
	}

	private final String _fileEntryUrl;

}