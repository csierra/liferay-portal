define("frontend-js-metal-web@1.1.0/metal-autocomplete/src/Autocomplete.soy-min", ["exports","metal-component/src/Component","metal-soy/src/Soy"], function(e,t,o){"use strict";function n(e){return e&&e.__esModule?e:{"default":e}}function r(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}function l(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!=typeof t&&"function"!=typeof t?e:t}function s(e,t){if("function"!=typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}Object.defineProperty(e,"__esModule",{value:!0}),e.templates=e.Autocomplete=void 0;var i,a=n(t),u=n(o);goog.loadModule(function(e){function t(e,t,o){e=e||{},n("div",null,null,"class","autocomplete autocomplete-list component "+(e.elementClasses?" "+e.elementClasses:"")),l({events:{itemSelected:e.onListItemSelected_},key:"list"},null,o),r("div")}goog.module("Autocomplete.incrementaldom");goog.require("soy"),goog.require("soydata");goog.require("goog.i18n.bidi"),goog.require("goog.asserts");var o=goog.require("incrementaldom"),n=o.elementOpen,r=o.elementClose,l=(o.elementVoid,o.elementOpenStart,o.elementOpenEnd,o.text,o.attr,u["default"].getTemplate("List.incrementaldom","render"));return (e.render=t, goog.DEBUG&&(t.soyTemplateName="Autocomplete.render"), e.render.params=["elementClasses","onListItemSelected_"], e.templates=i=e, e)});var c=function(e){function t(){return (r(this,t), l(this,e.apply(this,arguments)))}return (s(t,e), t)}(a["default"]);u["default"].register(c,i),e["default"]=i,e.Autocomplete=c,e.templates=i});