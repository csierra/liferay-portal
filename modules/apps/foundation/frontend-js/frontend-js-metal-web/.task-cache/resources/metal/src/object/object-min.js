define("frontend-js-metal-web@1.1.0/metal/src/object/object-min", ["exports"], function(n){"use strict";function e(n,e){if(!(n instanceof e))throw new TypeError("Cannot call a class as a function")}Object.defineProperty(n,"__esModule",{value:!0});var t=function(){function n(){e(this,n)}return (n.mixin=function(n){for(var e,t,r=1;r<arguments.length;r++){t=arguments[r];for(e in t)n[e]=t[e]}return n}, n.getObjectByName=function(n,e){var t=e||window,r=n.split(".");return r.reduce(function(n,e){return n[e]},t)}, n.map=function(n,e){for(var t={},r=Object.keys(n),u=0;u<r.length;u++)t[r[u]]=e(r[u],n[r[u]]);return t}, n.shallowEqual=function(n,e){if(n===e)return!0;var t=Object.keys(n),r=Object.keys(e);if(t.length!==r.length)return!1;for(var u=0;u<t.length;u++)if(n[t[u]]!==e[t[u]])return!1;return!0}, n)}();n["default"]=t});