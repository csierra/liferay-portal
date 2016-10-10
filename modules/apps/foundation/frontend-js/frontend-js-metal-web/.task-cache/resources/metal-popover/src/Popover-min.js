define("frontend-js-metal-web@1.1.0/metal-popover/src/Popover-min", ["exports","metal/src/metal","metal-soy/src/Soy","metal-tooltip/src/Tooltip","./Popover.soy","metal-jquery-adapter/src/JQueryAdapter"], function(t,e,o,n,r,i){"use strict";function l(t){return t&&t.__esModule?t:{"default":t}}function a(t,e){if(!(t instanceof e))throw new TypeError("Cannot call a class as a function")}function s(t,e){if(!t)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!e||"object"!=typeof e&&"function"!=typeof e?t:e}function c(t,e){if("function"!=typeof e&&null!==e)throw new TypeError("Super expression must either be null or a function, not "+typeof e);t.prototype=Object.create(e&&e.prototype,{constructor:{value:t,enumerable:!1,writable:!0,configurable:!0}}),e&&(Object.setPrototypeOf?Object.setPrototypeOf(t,e):t.__proto__=e)}Object.defineProperty(t,"__esModule",{value:!0});var u=l(e),p=l(o),f=l(r),y=l(i),d=function(t){function e(){return (a(this,e), s(this,t.apply(this,arguments)))}return (c(e,t), e.prototype.syncAlignElement=function(e){if(t.prototype.syncAlignElement.call(this,e),e){var o=e.getAttribute("data-content");o&&(this.content=o)}}, e.prototype.syncVisible=function(e){this.element.style.display=e?"block":"",t.prototype.syncVisible.call(this,e)}, e)}(n.TooltipBase);p["default"].register(d,f["default"]),d.STATE={content:{isHtml:!0,validator:function(t){return u["default"].isString(t)||u["default"].isFunction(t)}},triggerEvents:{validator:Array.isArray,value:["click","click"]},withArrow:{value:!0}},d.Align=n.TooltipBase.Align,t["default"]=d,y["default"].register("popover",d)});