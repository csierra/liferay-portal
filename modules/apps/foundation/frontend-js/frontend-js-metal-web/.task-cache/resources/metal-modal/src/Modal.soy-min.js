define("frontend-js-metal-web@1.1.0/metal-modal/src/Modal.soy-min", ["exports","metal-component/src/Component","metal-soy/src/Soy"], function(e,o,t){"use strict";function n(e){return e&&e.__esModule?e:{"default":e}}function l(e,o){if(!(e instanceof o))throw new TypeError("Cannot call a class as a function")}function s(e,o){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!o||"object"!=typeof o&&"function"!=typeof o?e:o}function r(e,o){if("function"!=typeof o&&null!==o)throw new TypeError("Super expression must either be null or a function, not "+typeof o);e.prototype=Object.create(o&&o.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),o&&(Object.setPrototypeOf?Object.setPrototypeOf(e,o):e.__proto__=o)}Object.defineProperty(e,"__esModule",{value:!0}),e.templates=e.Modal=void 0;var a,i=n(o),d=n(t);goog.loadModule(function(e){function o(e,o,l){e=e||{},t.asserts.assertType(null==e.body||e.body instanceof Function||e.body instanceof n.UnsanitizedText||goog.isString(e.body),"body",e.body,"?soydata.SanitizedHtml|string|undefined");var a=e.body;t.asserts.assertType(null==e.elementClasses||e.elementClasses instanceof goog.soy.data.SanitizedContent||goog.isString(e.elementClasses),"elementClasses",e.elementClasses,"null|string|undefined");var d=e.elementClasses;t.asserts.assertType(null==e.footer||e.footer instanceof Function||e.footer instanceof n.UnsanitizedText||goog.isString(e.footer),"footer",e.footer,"?soydata.SanitizedHtml|string|undefined");var u=e.footer;t.asserts.assertType(null==e.header||e.header instanceof Function||e.header instanceof n.UnsanitizedText||goog.isString(e.header),"header",e.header,"?soydata.SanitizedHtml|string|undefined");var c=e.header;t.asserts.assertType(null==e.noCloseButton||goog.isBoolean(e.noCloseButton)||1===e.noCloseButton||0===e.noCloseButton,"noCloseButton",e.noCloseButton,"boolean|null|undefined");var f=e.noCloseButton;t.asserts.assertType(null==e.role||e.role instanceof goog.soy.data.SanitizedContent||goog.isString(e.role),"role",e.role,"null|string|undefined");var g=e.role;s("div",null,null,"class","modal"+(d?" "+d:""),"role",g?g:"dialog"),s("div",null,null,"class","modal-dialog","tabindex","0"),s("div",null,null,"class","modal-content"),s("header",null,null,"class","modal-header"),c&&(f||(s("button",null,null,"type","button","class","close","data-onclick","hide","aria-label","Close"),s("span",null,null,"aria-hidden","true"),i("×"),r("span"),r("button")),c()),r("header"),s("section",null,null,"class","modal-body"),a&&a(),r("section"),s("footer",null,null,"class","modal-footer"),u&&u(),r("footer"),r("div"),r("div"),r("div")}goog.module("Modal.incrementaldom");var t=goog.require("soy"),n=goog.require("soydata");goog.require("goog.asserts"),goog.require("soy.asserts"),goog.require("goog.i18n.bidi");var l=goog.require("incrementaldom"),s=l.elementOpen,r=l.elementClose,i=(l.elementVoid,l.elementOpenStart,l.elementOpenEnd,l.text);l.attr;return (e.render=o, goog.DEBUG&&(o.soyTemplateName="Modal.render"), e.render.params=["body","elementClasses","footer","header","noCloseButton","role"], e.render.types={body:"html",elementClasses:"string",footer:"html",header:"html",noCloseButton:"bool",role:"string"}, e.templates=a=e, e)});var u=function(e){function o(){return (l(this,o), s(this,e.apply(this,arguments)))}return (r(o,e), o)}(i["default"]);d["default"].register(u,a),e["default"]=a,e.Modal=u,e.templates=a});