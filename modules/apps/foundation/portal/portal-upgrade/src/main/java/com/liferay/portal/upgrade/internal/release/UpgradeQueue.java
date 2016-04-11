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

package com.liferay.portal.upgrade.internal.release;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true,
	property = {"osgi.command.function=queue", "osgi.command.scope=upgrade"},
	service = UpgradeQueue.class
)
public class UpgradeQueue {

	private final BlockingDeque<Callable<Void>> _queue =
		new LinkedBlockingDeque<>();

	private UpgradeThread _upgradeThread;

	@Deactivate
	public void deactivate() {
		_upgradeThread.cancel();
	}

	@Activate
	public void activate() {
		_upgradeThread = new UpgradeThread();

		_upgradeThread.start();
	}

	public void push(Callable<Void> voidCallable) {
		_queue.push(voidCallable);
	}

	public void queue() {
		System.out.println("Pending Jobs: " + _queue.size());

		for (Callable<Void> callable : _queue) {
			System.out.println(callable);
		}
	}

	private class UpgradeThread extends Thread {
		@Override
		public void run() {
			while (!isInterrupted() && !_isCanceled()) {
				try {
					Callable<Void> callable = _queue.takeLast();

					callable.call();
				}
				catch (InterruptedException ie) {
					_canceled = true;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					if (_queue.isEmpty()) {
						synchronized (UpgradeQueue.this) {
							UpgradeQueue.this.notifyAll();
						}
					}
				}
			}
		}

		public void cancel() {
			_canceled = true;

			interrupt();
		}

		private boolean _isCanceled() {
			return _canceled;
		}

		private volatile boolean _canceled = false;
	}

}
