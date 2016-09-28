define("frontend-js-metal-web@1.1.0/metal/src/async/async-min", ["exports"], function(e){/*!
  * Polyfill from Google's Closure Library.
  * Copyright 2013 The Closure Library Authors. All Rights Reserved.
  */
"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={};n.throwException=function(e){n.nextTick(function(){throw e})},n.run=function(e,t){n.run.workQueueScheduled_||(n.nextTick(n.run.processWorkQueue),n.run.workQueueScheduled_=!0),n.run.workQueue_.push(new n.run.WorkItem_(e,t))},n.run.workQueueScheduled_=!1,n.run.workQueue_=[],n.run.processWorkQueue=function(){for(;n.run.workQueue_.length;){var e=n.run.workQueue_;n.run.workQueue_=[];for(var t=0;t<e.length;t++){var o=e[t];try{o.fn.call(o.scope)}catch(u){n.throwException(u)}}}n.run.workQueueScheduled_=!1},n.run.WorkItem_=function(e,n){this.fn=e,this.scope=n},n.nextTick=function(e,t){var o=e;return (t&&(o=e.bind(t)), o=n.nextTick.wrapCallback_(o), "function"==typeof setImmediate?void setImmediate(o):(n.nextTick.setImmediate_||(n.nextTick.setImmediate_=n.nextTick.getSetImmediateEmulator_()),void n.nextTick.setImmediate_(o)))},n.nextTick.setImmediate_=null,n.nextTick.getSetImmediateEmulator_=function(){var e;if("function"==typeof MessageChannel&&(e=MessageChannel),"undefined"==typeof e&&"undefined"!=typeof window&&window.postMessage&&window.addEventListener&&(e=function(){var e=document.createElement("iframe");e.style.display="none",e.src="",document.documentElement.appendChild(e);var n=e.contentWindow,t=n.document;t.open(),t.write(""),t.close();var o="callImmediate"+Math.random(),u=n.location.protocol+"//"+n.location.host,r=function(e){e.origin!==u&&e.data!==o||this.port1.onmessage()}.bind(this);n.addEventListener("message",r,!1),this.port1={},this.port2={postMessage:function(){n.postMessage(o,u)}}}),"undefined"!=typeof e){var n=new e,t={},o=t;return (n.port1.onmessage=function(){t=t.next;var e=t.cb;t.cb=null,e()}, function(e){o.next={cb:e},o=o.next,n.port2.postMessage(0)})}return"undefined"!=typeof document&&"onreadystatechange"in document.createElement("script")?function(e){var n=document.createElement("script");n.onreadystatechange=function(){n.onreadystatechange=null,n.parentNode.removeChild(n),n=null,e(),e=null},document.documentElement.appendChild(n)}:function(e){setTimeout(e,0)}},n.nextTick.wrapCallback_=function(e){return e},e["default"]=n});