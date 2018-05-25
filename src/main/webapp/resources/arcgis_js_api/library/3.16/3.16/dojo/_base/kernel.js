/*
	Copyright (c) 2004-2011, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/

//>>built
define("dojo/_base/kernel",["../has","./config","require","module"],function(k,g,h,c){var f,b;f=function(){return this}();var d={},m={},a={config:g,global:f,dijit:d,dojox:m},d={dojo:["dojo",a],dijit:["dijit",d],dojox:["dojox",m]};c=h.map&&h.map[c.id.match(/[^\/]+/)[0]];for(b in c)d[b]?d[b][0]=c[b]:d[b]=[c[b],{}];for(b in d)c=d[b],c[1]._scopeName=c[0],g.noGlobals||(f[c[0]]=c[1]);a.scopeMap=d;a.baseUrl=a.config.baseUrl=h.baseUrl;a.isAsync=h.async;a.locale=g.locale;f="$Rev: f4fef70 $".match(/[0-9a-f]{7,}/);
a.version={major:1,minor:10,patch:4,flag:"",revision:f?f[0]:NaN,toString:function(){var e=a.version;return e.major+"."+e.minor+"."+e.patch+e.flag+" ("+e.revision+")"}};Function("d","d.eval \x3d function(){return d.global.eval ? d.global.eval(arguments[0]) : eval(arguments[0]);}")(a);a.exit=function(){};"undefined"!=typeof console||(console={});c="assert count debug dir dirxml error group groupEnd info profile profileEnd time timeEnd trace warn log".split(" ");var l;for(f=0;l=c[f++];)console[l]||function(){var e=
l+"";console[e]="log"in console?function(){var a=Array.prototype.slice.call(arguments);a.unshift(e+":");console.log(a.join(" "))}:function(){};console[e]._fake=!0}();k.add("dojo-debug-messages",!!g.isDebug);a.deprecated=a.experimental=function(){};k("dojo-debug-messages")&&(a.deprecated=function(a,c,b){a="DEPRECATED: "+a;c&&(a+=" "+c);b&&(a+=" -- will be removed in version: "+b);console.warn(a)},a.experimental=function(a,c){var b="EXPERIMENTAL: "+a+" -- APIs subject to change without notice.";c&&
(b+=" "+c);console.warn(b)});if(g.modulePaths){a.deprecated("dojo.modulePaths","use paths configuration");k={};for(b in g.modulePaths)k[b.replace(/\./g,"/")]=g.modulePaths[b];h({paths:k})}a.moduleUrl=function(c,b){a.deprecated("dojo.moduleUrl()","use require.toUrl","2.0");var d=null;c&&(d=h.toUrl(c.replace(/\./g,"/")+(b?"/"+b:"")+"/*.*").replace(/\/\*\.\*/,"")+(b?"":"/"));return d};a._hasResource={};return a});