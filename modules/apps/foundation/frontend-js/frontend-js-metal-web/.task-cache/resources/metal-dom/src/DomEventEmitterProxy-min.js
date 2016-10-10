define("frontend-js-metal-web@1.1.0/metal-dom/src/DomEventEmitterProxy-min", ["exports","./dom","metal-events/src/events"], function(t,e,n){"use strict";function r(t){return t&&t.__esModule?t:{"default":t}}function i(t,e){if(!(t instanceof e))throw new TypeError("Cannot call a class as a function")}function o(t,e){if(!t)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!e||"object"!=typeof e&&"function"!=typeof e?t:e}function s(t,e){if("function"!=typeof e&&null!==e)throw new TypeError("Super expression must either be null or a function, not "+typeof e);t.prototype=Object.create(e&&e.prototype,{constructor:{value:t,enumerable:!1,writable:!0,configurable:!0}}),e&&(Object.setPrototypeOf?Object.setPrototypeOf(t,e):t.__proto__=e)}Object.defineProperty(t,"__esModule",{value:!0});var u=r(e),a=function(t){function e(){return (i(this,e), o(this,t.apply(this,arguments)))}return (s(e,t), e.prototype.addListener_=function(e,n){if(this.originEmitter_.addEventListener){if(this.isDelegateEvent_(e)){var r=e.indexOf(":",9),i=e.substring(9,r),o=e.substring(r+1);return u["default"].delegate(this.originEmitter_,i,o,n)}return u["default"].on(this.originEmitter_,e,n)}return t.prototype.addListener_.call(this,e,n)}, e.prototype.isDelegateEvent_=function(t){return"delegate:"===t.substr(0,9)}, e.prototype.isSupportedDomEvent_=function(t){return this.originEmitter_&&this.originEmitter_.addEventListener?this.isDelegateEvent_(t)&&-1!==t.indexOf(":",9)||u["default"].supportsEvent(this.originEmitter_,t):!0}, e.prototype.shouldProxyEvent_=function(e){return t.prototype.shouldProxyEvent_.call(this,e)&&this.isSupportedDomEvent_(e)}, e)}(n.EventEmitterProxy);t["default"]=a});