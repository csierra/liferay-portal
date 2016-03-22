define("frontend-js-metal-web@1.0.2/metal-autocomplete/src/Autocomplete-min", ["exports","metal/src/metal","metal-debounce/src/debounce","metal-dom/src/all/dom","metal-promise/src/promise/Promise","metal-position/src/all/position","./AutocompleteBase","metal-soy/src/soy","metal-jquery-adapter/src/JQueryAdapter","./Autocomplete.soy","metal-list/src/List"], function(t,e,i,o,n,s,r,l,a){"use strict";function c(t){return t&&t.__esModule?t:{"default":t}}function u(t,e){if(!(t instanceof e))throw new TypeError("Cannot call a class as a function")}function p(t,e){if(!t)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!e||"object"!=typeof e&&"function"!=typeof e?t:e}function h(t,e){if("function"!=typeof e&&null!==e)throw new TypeError("Super expression must either be null or a function, not "+typeof e);t.prototype=Object.create(e&&e.prototype,{constructor:{value:t,enumerable:!1,writable:!0,configurable:!0}}),e&&(Object.setPrototypeOf?Object.setPrototypeOf(t,e):t.__proto__=e)}Object.defineProperty(t,"__esModule",{value:!0});var m=c(e),f=c(i),d=c(o),y=c(r),b=c(a),g=function(t){function e(){return (u(this,e), p(this,t.apply(this,arguments)))}return (h(e,t), e.prototype.attached=function(){t.prototype.attached.call(this),this.on("click",function(t){return t.stopPropagation()}),this.eventHandler_.add(d["default"].on(this.inputElement,"focus",this.handleInputFocus_.bind(this))),this.eventHandler_.add(d["default"].on(document,"click",this.handleDocClick_.bind(this))),this.eventHandler_.add(d["default"].on(window,"resize",(0,f["default"])(this.handleWindowResize_.bind(this),100))),this.visible&&this.align()}, e.prototype.align=function(){this.element.style.width=this.inputElement.offsetWidth+"px";var t=s.Align.align(this.element,this.inputElement,s.Align.Bottom);switch(d["default"].removeClasses(this.element,this.positionCss_),t){case s.Align.Top:case s.Align.TopLeft:case s.Align.TopRight:this.positionCss_="autocomplete-top";break;case s.Align.Bottom:case s.Align.BottomLeft:case s.Align.BottomRight:this.positionCss_="autocomplete-bottom";break;default:this.positionCss_=null}d["default"].addClasses(this.element,this.positionCss_)}, e.prototype.getList=function(){return this.components[this.id+"-list"]}, e.prototype.handleDocClick_=function(){document.activeElement!==this.inputElement&&(this.visible=!1)}, e.prototype.handleInputFocus_=function(){this.request(this.inputElement.value)}, e.prototype.handleWindowResize_=function(){this.visible&&this.align()}, e.prototype.request=function(e){var i=this;return t.prototype.request.call(this,e).then(function(t){t&&(t.forEach(i.assertItemObjectStructure_),i.getList().items=t),i.visible=!!(t&&t.length>0)})}, e.prototype.onListItemSelected_=function(t){var e=parseInt(t.getAttribute("data-index"),10);this.emit("select",this.getList().items[e]),this.visible=!1}, e.prototype.syncVisible=function(e){t.prototype.syncVisible.call(this,e),e&&this.align()}, e.prototype.assertItemObjectStructure_=function(t){if(!m["default"].isObject(t))throw new n.CancellablePromise.CancellationError("Autocomplete item must be an object");if(!t.hasOwnProperty("textPrimary"))throw new n.CancellablePromise.CancellationError("Autocomplete item must be an object with 'textPrimary' key")}, e)}(y["default"]);g.prototype.registerMetalComponent&&g.prototype.registerMetalComponent(g,"Autocomplete"),g.ATTRS={format:{value:function(t){return m["default"].isString(t)?{textPrimary:t}:t}}},g.RENDERER=l.SoyRenderer,t["default"]=g,b["default"].register("autocomplete",g)});