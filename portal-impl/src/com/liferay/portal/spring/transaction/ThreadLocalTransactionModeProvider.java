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

package com.liferay.portal.spring.transaction;

/**
 * @author Carlos Sierra Andrés
 */
public class ThreadLocalTransactionModeProvider
	implements TransactionModeProvider {

	public static void setReadOnlyTransaction(boolean readOnly) {
		ThreadLocalTransactionModeProvider._readOnlyTransaction.set(readOnly);
	}

	@Override
	public boolean isReadOnly() {
		return ThreadLocalTransactionModeProvider._readOnlyTransaction.get();
	}

	private static final ThreadLocal<Boolean> _readOnlyTransaction;

	static {
		_readOnlyTransaction = new ThreadLocal<>();

		_readOnlyTransaction.set(false);
	}

}