define(['exports', 'metal/src/metal', 'metal-dom/src/all/dom', 'metal-component/src/all/component', './IncrementalDomAop', './incremental-dom'], function (exports, _metal, _dom, _component, _IncrementalDomAop) {
	'use strict';

	Object.defineProperty(exports, "__esModule", {
		value: true
	});

	var _dom2 = _interopRequireDefault(_dom);

	var _IncrementalDomAop2 = _interopRequireDefault(_IncrementalDomAop);

	function _interopRequireDefault(obj) {
		return obj && obj.__esModule ? obj : {
			default: obj
		};
	}

	function _classCallCheck(instance, Constructor) {
		if (!(instance instanceof Constructor)) {
			throw new TypeError("Cannot call a class as a function");
		}
	}

	function _possibleConstructorReturn(self, call) {
		if (!self) {
			throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
		}

		return call && (typeof call === "object" || typeof call === "function") ? call : self;
	}

	function _inherits(subClass, superClass) {
		if (typeof superClass !== "function" && superClass !== null) {
			throw new TypeError("Super expression must either be null or a function, not " + typeof superClass);
		}

		subClass.prototype = Object.create(superClass && superClass.prototype, {
			constructor: {
				value: subClass,
				enumerable: false,
				writable: true,
				configurable: true
			}
		});
		if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass;
	}

	var IncrementalDomRenderer = function (_ComponentRenderer) {
		_inherits(IncrementalDomRenderer, _ComponentRenderer);

		/**
   * @inheritDoc
   */

		function IncrementalDomRenderer(comp) {
			_classCallCheck(this, IncrementalDomRenderer);

			var _this = _possibleConstructorReturn(this, _ComponentRenderer.call(this, comp));

			_this.changes_ = {};
			_this.eventsCollector_ = new _component.EventsCollector(comp);
			comp.on('stateKeyChanged', _this.handleStateKeyChanged_.bind(_this));
			comp.on('detached', _this.handleDetached_.bind(_this));
			return _this;
		}

		/**
   * Adds all inline listener attributes included in the given config.
   * @param {!Array} listeners
   * @protected
   */


		IncrementalDomRenderer.prototype.addInlineListeners_ = function addInlineListeners_(listeners) {
			for (var i = 0; i < listeners.length; i += 2) {
				var name = listeners[i];
				var fn = listeners[i + 1];
				if (name.startsWith('data-on') && _metal.core.isString(fn)) {
					this.listenersToAttach_.push({
						eventName: name.substr(7),
						fn: fn
					});
				}
			}
		};

		IncrementalDomRenderer.prototype.attachInlineListeners_ = function attachInlineListeners_() {
			this.eventsCollector_.startCollecting();
			for (var i = 0; i < this.listenersToAttach_.length; i++) {
				var listener = this.listenersToAttach_[i];
				this.eventsCollector_.attachListener(listener.eventName, listener.fn);
			}
		};

		IncrementalDomRenderer.prototype.buildChildrenFn_ = function buildChildrenFn_(calls) {
			var _this2 = this;

			return function () {
				_this2.intercept_();
				for (var i = 0; i < calls.length; i++) {
					IncrementalDOM[calls[i].name].apply(null, _metal.array.slice(calls[i].args, 1));
				}
				_IncrementalDomAop2.default.stopInterception();
			};
		};

		IncrementalDomRenderer.prototype.disposeUnusedSubComponents_ = function disposeUnusedSubComponents_() {
			var keys = Object.keys(this.component_.components);
			var unused = [];
			for (var i = 0; i < keys.length; i++) {
				if (!this.subComponentsFound_[keys[i]]) {
					unused.push(keys[i]);
				}
			}
			this.component_.disposeSubComponents(unused);
		};

		IncrementalDomRenderer.prototype.getSubComponent_ = function getSubComponent_(key, tagOrCtor, config) {
			var comp = this.component_.addSubComponent(key, tagOrCtor, config);
			if (comp.wasRendered) {
				comp.setState(config);
			}
			return comp;
		};

		IncrementalDomRenderer.prototype.guaranteeParent_ = function guaranteeParent_() {
			var element = this.component_.element;
			if (!element || !element.parentNode) {
				var parent = document.createElement('div');
				if (element) {
					_dom2.default.append(parent, element);
				}
				return parent;
			}
		};

		IncrementalDomRenderer.prototype.handleDetached_ = function handleDetached_() {
			this.eventsCollector_.detachAllListeners();
		};

		IncrementalDomRenderer.prototype.handleInterceptedAttributesCall_ = function handleInterceptedAttributesCall_(originalFn, element, name, value) {
			if (name.startsWith('data-on')) {
				var eventName = name.substr(7);
				if (_metal.core.isFunction(element[name])) {
					element.removeEventListener(eventName, element[name]);
				}
				if (_metal.core.isFunction(value)) {
					_dom2.default.on(element, eventName, value);
				}
			} else if (name === 'checked') {
				// This is a temporary fix to account for incremental dom setting
				// "checked" as an attribute only, which can cause bugs since that won't
				// necessarily check/uncheck the element it's set on. See
				// https://github.com/google/incremental-dom/issues/198 for more details.
				element.checked = _metal.core.isDefAndNotNull(value) && value !== false;
			}
			originalFn(element, name, value);
		};

		IncrementalDomRenderer.prototype.handleInterceptedChildrenCloseCall_ = function handleInterceptedChildrenCloseCall_(originalFn, callTag) {
			if (this.isCurrentComponentTag_(callTag) && --this.componentToRender_.tagsCount === 0) {
				var _componentToRender_ = this.componentToRender_;
				var calls = _componentToRender_.calls;
				var config = _componentToRender_.config;
				var tag = _componentToRender_.tag;

				config.children = this.buildChildrenFn_(calls);
				this.componentToRender_ = null;
				_IncrementalDomAop2.default.stopInterception();
				return this.renderSubComponent_(tag, config).element;
			}
			this.componentToRender_.calls.push({
				name: 'elementClose',
				args: arguments
			});
		};

		IncrementalDomRenderer.prototype.handleInterceptedChildrenOpenCall_ = function handleInterceptedChildrenOpenCall_(originalFn, tag) {
			if (this.isCurrentComponentTag_(tag)) {
				this.componentToRender_.tagsCount++;
			}
			this.componentToRender_.calls.push({
				name: 'elementOpen',
				args: arguments
			});
		};

		IncrementalDomRenderer.prototype.handleInterceptedChildrenTextCall_ = function handleInterceptedChildrenTextCall_() {
			this.componentToRender_.calls.push({
				name: 'text',
				args: arguments
			});
		};

		IncrementalDomRenderer.prototype.handleInterceptedOpenCall_ = function handleInterceptedOpenCall_(originalFn, tag) {
			if (this.isComponentTag_(tag)) {
				return this.handleSubComponentCall_.apply(this, arguments);
			} else {
				return this.handleRegularCall_.apply(this, arguments);
			}
		};

		IncrementalDomRenderer.prototype.handleRegularCall_ = function handleRegularCall_(originalFn, tag, key, statics) {
			var attrsArr = _metal.array.slice(arguments, 4);
			this.addInlineListeners_((statics || []).concat(attrsArr));
			var args = _metal.array.slice(arguments, 1);
			if (!this.rootElementReached_ && this.component_.config.key) {
				args[1] = this.component_.config.key;
			}
			var node = originalFn.apply(null, args);
			if (!this.rootElementReached_) {
				this.rootElementReached_ = true;
				if (this.component_.element !== node) {
					this.component_.element = node;
				}
			}
			return node;
		};

		IncrementalDomRenderer.prototype.handleStateKeyChanged_ = function handleStateKeyChanged_(data) {
			if (data.key !== 'element') {
				this.changes_[data.key] = data;
			}
		};

		IncrementalDomRenderer.prototype.handleSubComponentCall_ = function handleSubComponentCall_(originalFn, tag, key, statics) {
			var config = { key: key };
			var attrsArr = (statics || []).concat(_metal.array.slice(arguments, 4));
			for (var i = 0; i < attrsArr.length; i += 2) {
				config[attrsArr[i]] = attrsArr[i + 1];
			}

			this.componentToRender_ = {
				calls: [],
				config: config,
				tag: tag,
				tagsCount: 1
			};
			_IncrementalDomAop2.default.startInterception({
				elementClose: this.handleInterceptedChildrenCloseCall_.bind(this),
				elementOpen: this.handleInterceptedChildrenOpenCall_.bind(this),
				text: this.handleInterceptedChildrenTextCall_.bind(this)
			});
		};

		IncrementalDomRenderer.prototype.intercept_ = function intercept_() {
			_IncrementalDomAop2.default.startInterception({
				attributes: this.handleInterceptedAttributesCall_.bind(this),
				elementOpen: this.handleInterceptedOpenCall_.bind(this)
			});
		};

		IncrementalDomRenderer.prototype.isComponentTag_ = function isComponentTag_(tag) {
			return !_metal.core.isString(tag) || tag[0] === tag[0].toUpperCase();
		};

		IncrementalDomRenderer.prototype.isCurrentComponentTag_ = function isCurrentComponentTag_(tag) {
			return this.isComponentTag_(tag) && this.componentToRender_.tag === tag;
		};

		IncrementalDomRenderer.prototype.render = function render() {
			this.patch();
		};

		IncrementalDomRenderer.prototype.renderIncDom = function renderIncDom() {
			IncrementalDOM.elementVoid('div');
		};

		IncrementalDomRenderer.prototype.renderWithoutPatch = function renderWithoutPatch() {
			// Mark that there shouldn't be an update for state changes so far, since
			// render has already been called.
			this.changes_ = {};

			this.rootElementReached_ = false;
			this.subComponentsFound_ = {};
			this.generatedKeyCount_ = 0;
			this.listenersToAttach_ = [];
			this.intercept_();
			this.renderIncDom();
			_IncrementalDomAop2.default.stopInterception();
			this.attachInlineListeners_();
		};

		IncrementalDomRenderer.prototype.shouldUpdate = function shouldUpdate() {
			return true;
		};

		IncrementalDomRenderer.prototype.patch = function patch() {
			var tempParent = this.guaranteeParent_();
			if (tempParent) {
				IncrementalDOM.patch(tempParent, this.renderWithoutPatch.bind(this));
				_dom2.default.exitDocument(this.component_.element);
			} else {
				IncrementalDOM.patchOuter(this.component_.element, this.renderWithoutPatch.bind(this));
			}
		};

		IncrementalDomRenderer.prototype.update = function update() {
			var changedKeys = Object.keys(this.changes_);
			if (changedKeys.length > 0 && this.shouldUpdate(this.changes_)) {
				this.patch();
				this.eventsCollector_.detachUnusedListeners();
				this.disposeUnusedSubComponents_();
			}
		};

		IncrementalDomRenderer.prototype.renderSubComponent_ = function renderSubComponent_(tagOrCtor, config) {
			var key = config.key || 'sub' + this.generatedKeyCount_++;
			var comp = this.getSubComponent_(key, tagOrCtor, config);
			var renderer = comp.getRenderer();
			if (renderer instanceof IncrementalDomRenderer) {
				renderer.renderWithoutPatch();
			} else {
				console.warn('IncrementalDomRenderer doesn\'t support rendering sub components ' + 'that don\'t use IncrementalDomRenderer as well, like:', comp);
			}
			if (!comp.wasRendered) {
				comp.renderAsSubComponent();
			}
			this.subComponentsFound_[key] = true;
			return comp;
		};

		return IncrementalDomRenderer;
	}(_component.ComponentRenderer);

	exports.default = IncrementalDomRenderer;
});
//# sourceMappingURL=IncrementalDomRenderer.js.map