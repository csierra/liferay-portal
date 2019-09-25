package com.liferay.journal.internal.upgrade;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.InitialDataGeneration;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true, property = "Bundle-SymbolicName=com.liferay.journal.service",
	service = InitialDataGeneration.class
)
public class JournalInitialDataGeneration implements InitialDataGeneration {

	public void execute() throws UpgradeException {
		_log.error("JournalInitialDataGeneration ejecutado");
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JournalInitialDataGeneration.class);
}
