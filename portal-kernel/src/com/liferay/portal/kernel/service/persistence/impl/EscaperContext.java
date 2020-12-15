/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

package com.liferay.portal.kernel.service.persistence.impl;

import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeRunnable;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.lang.CentralizedThreadLocal;

/**
 * @author Carlos Sierra Andrés
 */
public final class EscaperContext {

	public static void setEscaper(Escaper escaper) {
		_escaperThreadLocal.set(escaper);
	}

	public static Escaper getEscaper() {
		return _escaperThreadLocal.get();
	}

	public static <T, E extends Throwable> T withEscaper(
			Escaper escaper, UnsafeSupplier<T, E> unsafeSupplier)
		throws E {

		final Escaper currentEscaper = getEscaper();

		try {
			return unsafeSupplier.get();
		}
		finally {
			setEscaper(currentEscaper);
		}
	}

	public static <E extends Throwable> void withEscaper(
		Escaper escaper, UnsafeRunnable<E> unsafeRunnable)
		throws E {

		final Escaper currentEscaper = getEscaper();

		try {
			unsafeRunnable.run();
		}
		finally {
			setEscaper(currentEscaper);
		}
	}

	private static ThreadLocal<Escaper> _escaperThreadLocal =
		new CentralizedThreadLocal<>("_escaperThreadLocal");
}
