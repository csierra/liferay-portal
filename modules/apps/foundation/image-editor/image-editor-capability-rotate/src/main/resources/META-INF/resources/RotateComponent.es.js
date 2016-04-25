import Component from 'metal-component/src/Component';
import Soy from 'metal-soy/src/Soy';

import core from 'metal/src/core';
import { CancellablePromise } from 'metal-promise/src/promise/Promise';

import componentTemplates from './RotateComponent.soy';
import controlsTemplates from './RotateControls.soy';

/**
 * Rotate Component
 */
class RotateComponent extends Component {
	/**
	 * @inheritDoc
	 */
	attached() {
		this.cache_ = {};
		this.rotationAngle_ = 0;
	}

	/**
	 * @inheritDoc
	 */
	detached() {
		this.cache_ = {};
		this.rotationAngle_ = 0;
	}

	rotate_(imageData, rotationAngle) {
		let cancellablePromise = new CancellablePromise((resolve, reject) => {
			let imageWidth = imageData.width;
			let imageHeight = imageData.height;

			let swapDimensions  = (rotationAngle / 90) % 2;

			let imageCanvas = document.createElement('canvas');
			imageCanvas.width = imageWidth;
			imageCanvas.height = imageHeight;
			imageCanvas.getContext('2d').putImageData(imageData, 0, 0);

			let offscreenCanvas = document.createElement('canvas');
			offscreenCanvas.width = swapDimensions ? imageHeight : imageWidth;
			offscreenCanvas.height = swapDimensions ? imageWidth : imageHeight;

			let offscreenContext = offscreenCanvas.getContext('2d');
			offscreenContext.save();
			offscreenContext.translate(offscreenCanvas.width / 2, offscreenCanvas.height / 2);
			offscreenContext.rotate(rotationAngle * Math.PI / 180);
			offscreenContext.drawImage(imageCanvas, -imageCanvas.width / 2, -imageCanvas.height / 2);
			offscreenContext.restore();

			resolve(offscreenContext.getImageData(0, 0, offscreenCanvas.width, offscreenCanvas.height));
		});

		return cancellablePromise;
	}

	preview(imageData) {
		return this.process(imageData);
	}

	process(imageData) {
		let promise = this.cache_[this.rotationAngle_];

		if (!promise) {
			promise = this.rotate_(imageData, this.rotationAngle_);
			this.cache_[this.rotationAngle_] = promise;
		}

		return promise;
	}

	rotateLeft() {
		this.rotationAngle_ -= 90;
		this.requestEditorPreview();
	}

	rotateRight() {
		this.rotationAngle_ += 90;
		this.requestEditorPreview();
	}
}

/**
 * State definition.
 * @type {!Object}
 * @static
 */
RotateComponent.STATE = {
	/**
	 * Path of this module
	 * @type {Function}
	 */
	modulePath: {
		validator: core.isString
	},

	requestEditorPreview: {
		validator: core.isFunction
	}
};

// Register component
Soy.register(RotateComponent, componentTemplates);

export default RotateComponent;