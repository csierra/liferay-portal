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

package com.liferay.portal.search.admin.web.internal.util;

import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.portal.instances.service.PortalInstancesLocalService;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.CompaniesUtil;

/**
 * @author Adam Brandizzi
 */
public class DictionaryReindexer {

	public DictionaryReindexer(IndexWriterHelper indexWriterHelper) {
		_indexWriterHelper = indexWriterHelper;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #DictionaryReindexer(IndexWriterHelper)}
	 */
	@Deprecated
	public DictionaryReindexer(
		IndexWriterHelper indexWriterHelper,
		PortalInstancesLocalService portalInstancesLocalService) {

		_indexWriterHelper = indexWriterHelper;
	}

	public void reindexDictionaries() throws SearchException {
		reindexDictionaries(CompanyConstants.SYSTEM);

		CompaniesUtil.runCompanyIds(
			(UnsafeConsumer<Long, SearchException>)
				companyId -> reindexDictionaries(companyId));
	}

	protected void reindexDictionaries(long companyId) throws SearchException {
		_indexWriterHelper.indexQuerySuggestionDictionaries(companyId);
		_indexWriterHelper.indexSpellCheckerDictionaries(companyId);
	}

	private final IndexWriterHelper _indexWriterHelper;

}