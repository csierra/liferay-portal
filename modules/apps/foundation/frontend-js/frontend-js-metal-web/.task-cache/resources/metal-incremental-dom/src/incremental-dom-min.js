define("frontend-js-metal-web@1.1.0/metal-incremental-dom/src/incremental-dom-min", [], function(){"use strict";var e="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol?"symbol":typeof e};/**
   * @license
   * Copyright 2015 The Incremental DOM Authors. All Rights Reserved.
   *
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   *      http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS-IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   */
!function(e,t){t(e.IncrementalDOM=e.IncrementalDOM||{})}(window,function(t){function n(e,t){this.attrs=a(),this.attrsArr=[],this.newAttrs=a(),this.key=t,this.keyMap=null,this.keyMapValid=!0,this.nodeName=e,this.text=null}function r(){this.created=O.nodesCreated&&[],this.deleted=O.nodesDeleted&&[]}var l=Object.prototype.hasOwnProperty,i=Object.create,o=function(e,t){return l.call(e,t)},a=function(){return i(null)},u=function(e,t,r){var l=new n(t,r);return (e.__incrementalDOMData=l, l)},f=function(e){var t=e.__incrementalDOMData;if(!t){var n=e.nodeName.toLowerCase(),r=null;e instanceof Element&&(r=e.getAttribute("key")),t=u(e,n,r)}return t},c={"default":"__default",placeholder:"__placeholder"},d=function(e){return 0===e.lastIndexOf("xml:",0)?"http://www.w3.org/XML/1998/namespace":0===e.lastIndexOf("xlink:",0)?"http://www.w3.org/1999/xlink":void 0},s=function(e,t,n){if(null==n)e.removeAttribute(t);else{var r=d(t);r?e.setAttributeNS(r,t,n):e.setAttribute(t,n)}},p=function(e,t,n){e[t]=n},h=function(e,t,n){if("string"==typeof n)e.style.cssText=n;else{e.style.cssText="";var r=e.style,l=n;for(var i in l)o(l,i)&&(r[i]=l[i])}},y=function(t,n,r){var l="undefined"==typeof r?"undefined":e(r);"object"===l||"function"===l?p(t,n,r):s(t,n,r)},v=function(e,t,n){var r=f(e),l=r.attrs;if(l[t]!==n){var i=m[t]||m[c["default"]];i(e,t,n),l[t]=n}},m=a();m[c["default"]]=y,m[c.placeholder]=function(){},m.style=h;var k=function(e,t){return"svg"===e?"http://www.w3.org/2000/svg":"foreignObject"===f(t).nodeName?null:t.namespaceURI},g=function(e,t,n,r,l){var i=k(n,t),o=void 0;if(o=i?e.createElementNS(i,n):e.createElement(n),u(o,n,r),l)for(var a=0;a<l.length;a+=2)v(o,l[a],l[a+1]);return o},w=function(e){var t=e.createTextNode("");return (u(t,"#text",null), t)},b=function(e){for(var t=a(),n=e.firstElementChild;n;){var r=f(n).key;r&&(t[r]=n),n=n.nextElementSibling}return t},x=function(e){var t=f(e);return (t.keyMap||(t.keyMap=b(e)), t.keyMap)},C=function(e,t){return t?x(e)[t]:null},M=function(e,t,n){x(e)[t]=n},O={nodesCreated:null,nodesDeleted:null};r.prototype.markCreated=function(e){this.created&&this.created.push(e)},r.prototype.markDeleted=function(e){this.deleted&&this.deleted.push(e)},r.prototype.notifyChanges=function(){this.created&&this.created.length>0&&O.nodesCreated(this.created),this.deleted&&this.deleted.length>0&&O.nodesDeleted(this.deleted)};var D=null,N=null,S=null,A=null,_=null,E=function(e){var t=function(t,n,l){var i=D,o=A,a=_,u=N,f=S;D=new r,A=t,_=t.ownerDocument,S=t.parentNode,e(t,n,l),D.notifyChanges(),D=i,A=o,_=a,N=u,S=f};return t},I=E(function(e,t,n){N=e,L(),t(n),R()}),V=E(function(e,t,n){N={nextSibling:e},t(n)}),j=function(e,t){var n=f(N);return e===n.nodeName&&t==n.key},P=function(e,t,n){if(!N||!j(e,t)){var r=void 0;t&&(r=C(S,t)),r||(r="#text"===e?w(_):g(_,S,e,t,n),t&&M(S,t,r),D.markCreated(r)),N&&f(N).key?(S.replaceChild(r,N),f(S).keyMapValid=!1):S.insertBefore(r,N),N=r}},T=function(){var e=S,t=f(e),n=t.keyMap,r=t.keyMapValid,l=e.lastChild,i=void 0;if(!(l===N&&r||t.attrs[c.placeholder]&&e!==A)){for(;l!==N;)e.removeChild(l),D.markDeleted(l),i=f(l).key,i&&delete n[i],l=e.lastChild;if(!r){for(i in n)l=n[i],l.parentNode!==e&&(D.markDeleted(l),delete n[i]);t.keyMapValid=!0}}},L=function(){S=N,N=null},B=function(){N=N?N.nextSibling:S.firstChild},R=function(){T(),N=S,S=S.parentNode},U=function(e,t,n){return (B(), P(e,t,n), L(), S)},X=function(){return (R(), N)},q=function(){return (B(), P("#text",null,null), N)},z=function(){return S},F=function(){N=S.lastChild},G=3,H=[],J=function(e,t,n,r){for(var l=U(e,t,n),i=f(l),o=i.attrsArr,a=i.newAttrs,u=!1,c=G,d=0;c<arguments.length;c+=1,d+=1)if(o[d]!==arguments[c]){u=!0;break}for(;c<arguments.length;c+=1,d+=1)o[d]=arguments[c];if(d<o.length&&(u=!0,o.length=d),u){for(c=G;c<arguments.length;c+=2)a[arguments[c]]=arguments[c+1];for(var s in a)v(l,s,a[s]),a[s]=void 0}return l},K=function(e,t,n){H[0]=e,H[1]=t,H[2]=n},Q=function(e,t){H.push(e,t)},W=function(){var e=J.apply(null,H);return (H.length=0, e)},Y=function(e){var t=X();return t},Z=function(e,t,n,r){return (J.apply(null,arguments), Y(e))},$=function(e,t,n,r){return (J.apply(null,arguments), F(), Y(e))},ee=function(e,t){var n=q(),r=f(n);if(r.text!==e){r.text=e;for(var l=e,i=1;i<arguments.length;i+=1){var o=arguments[i];l=o(l)}n.data=l}return n};t.patch=I,t.patchInner=I,t.patchOuter=V,t.currentElement=z,t.skip=F,t.elementVoid=Z,t.elementOpenStart=K,t.elementOpenEnd=W,t.elementOpen=J,t.elementClose=Y,t.elementPlaceholder=$,t.text=ee,t.attr=Q,t.symbols=c,t.attributes=m,t.applyAttr=s,t.applyProp=p,t.notifications=O})});