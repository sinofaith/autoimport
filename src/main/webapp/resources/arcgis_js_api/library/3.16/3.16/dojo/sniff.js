/*
	Copyright (c) 2004-2011, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/

//>>built
define("dojo/sniff",["./has"],function(a){var c=navigator,b=c.userAgent,c=c.appVersion,d=parseFloat(c);a.add("air",0<=b.indexOf("AdobeAIR"));a.add("msapp",parseFloat(b.split("MSAppHost/")[1])||void 0);a.add("khtml",0<=c.indexOf("Konqueror")?d:void 0);a.add("webkit",parseFloat(b.split("WebKit/")[1])||void 0);a.add("chrome",parseFloat(b.split("Chrome/")[1])||void 0);a.add("safari",0<=c.indexOf("Safari")&&!a("chrome")?parseFloat(c.split("Version/")[1]):void 0);a.add("mac",0<=c.indexOf("Macintosh"));
a.add("quirks","BackCompat"==document.compatMode);if(b.match(/(iPhone|iPod|iPad)/)){var f=RegExp.$1.replace(/P/,"p"),e=b.match(/OS ([\d_]+)/)?RegExp.$1:"1",e=parseFloat(e.replace(/_/,".").replace(/_/g,""));a.add(f,e);a.add("ios",e)}a.add("android",parseFloat(b.split("Android ")[1])||void 0);a.add("bb",(0<=b.indexOf("BlackBerry")||0<=b.indexOf("BB10"))&&parseFloat(b.split("Version/")[1])||void 0);a.add("trident",parseFloat(c.split("Trident/")[1])||void 0);a.add("svg","undefined"!==typeof SVGAngle);
a("webkit")||(0<=b.indexOf("Opera")&&a.add("opera",9.8<=d?parseFloat(b.split("Version/")[1])||d:d),0<=b.indexOf("Gecko")&&(!a("khtml")&&!a("webkit")&&!a("trident"))&&a.add("mozilla",d),a("mozilla")&&a.add("ff",parseFloat(b.split("Firefox/")[1]||b.split("Minefield/")[1])||void 0),document.all&&!a("opera")&&(b=parseFloat(c.split("MSIE ")[1])||void 0,(c=document.documentMode)&&(5!=c&&Math.floor(b)!=c)&&(b=c),a.add("ie",b)),a.add("wii","undefined"!=typeof opera&&opera.wiiremote));return a});