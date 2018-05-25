// All material copyright ESRI, All Rights Reserved, unless otherwise specified.
// See http://js.arcgis.com/3.16/esri/copyright.txt for details.
//>>built
define("esri/arcade/functions/string",["require","exports","../languageUtils"],function(m,l,c){l.registerFunctions=function(h,g){function k(){var c=(new Date).getTime();return"xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g,function(e){var b=(c+16*Math.random())%16|0;c=Math.floor(c/16);return("x"===e?b:b&3|8).toString(16)})}h.trim=function(d,e){return g(d,e,function(b,f,a){c.pcCheck(a,1,1);return c.toString(a[0]).trim()})};h.len=function(d,e){return g(d,e,function(b,f,a){c.pcCheck(a,1,1);return c.isArray(a[0])?
a[0].length:c.toString(a[0]).length})};h.upper=function(d,e){return g(d,e,function(b,f,a){c.pcCheck(a,1,1);return c.toString(a[0]).toUpperCase()})};h.proper=function(d,e){return g(d,e,function(b,f,a){c.pcCheck(a,1,2);b=1;2===a.length&&"firstword"===c.toString(a[1]).toLowerCase()&&(b=2);f=/\s/;a=c.toString(a[0]);for(var e="",d=!0,h=0;h<a.length;h++){var g=a[h];f.test(g)?1===b&&(d=!0):g.toUpperCase()!==g.toLowerCase()&&(d?(g=g.toUpperCase(),d=!1):g=g.toLowerCase());e+=g}return e})};h.lower=function(d,
e){return g(d,e,function(b,f,a){c.pcCheck(a,1,1);return c.toString(a[0]).toLowerCase()})};h.guid=function(d,e){return g(d,e,function(b,f,a){c.pcCheck(a,0,1);if(0<a.length)switch(c.toString(a[0]).toLowerCase()){case "digits":return k().replace("-","").replace("-","").replace("-","").replace("-","");case "digits-hyphen":return k();case "digits-hyphen-parentheses":return"("+k()+")"}return"{"+k()+"}"})};h.mid=function(d,e){return g(d,e,function(b,f,a){c.pcCheck(a,2,3);b=c.toNumber(a[1]);if(isNaN(b))return"";
0>b&&(b=0);if(2===a.length)return c.toString(a[0]).substr(b);f=c.toNumber(a[2]);if(isNaN(f))return"";0>f&&(f=0);return c.toString(a[0]).substr(b,f)})};h.find=function(d,e){return g(d,e,function(b,f,a){c.pcCheck(a,2,3);b=0;if(2<a.length){b=c.toNumber(c.defaultUndefined(a[2],0));if(isNaN(b))return-1;0>b&&(b=0)}return c.toString(a[1]).indexOf(c.toString(a[0]),b)})};h.left=function(d,e){return g(d,e,function(b,f,a){c.pcCheck(a,2,2);b=c.toNumber(a[1]);if(isNaN(b))return"";0>b&&(b=0);return c.toString(a[0]).substr(0,
b)})};h.right=function(d,e){return g(d,e,function(b,f,a){c.pcCheck(a,2,2);b=c.toNumber(a[1]);if(isNaN(b))return"";0>b&&(b=0);return c.toString(a[0]).substr(-1*b,b)})};h.split=function(d,e){return g(d,e,function(b,f,a){c.pcCheck(a,2,4);b=c.toNumber(c.defaultUndefined(a[2],-1));-1===b||null===b?b=c.toString(a[0]).split(c.toString(a[1])):(isNaN(b)&&(b=-1),-1>b&&(b=-1),b=c.toString(a[0]).split(c.toString(a[1]),b));a=c.defaultUndefined(a[3],!1);if(!1===c.isBoolean(a))throw Error("Invalid Parameter");if(!1===
a)return b;a=[];for(f=0;f<b.length;f++)""!==b[f]&&void 0!==b[f]&&a.push(b[f]);return a})};h.text=function(d,e){return g(d,e,function(b,f,a){c.pcCheck(a,1,2);if(c.isArray(a[0])){b=c.defaultUndefined(a[2],"");f=[];for(var e=0;e<a[0].length;e++)f[e]=c.toString(a[0][e],b);return f.join(",")}return c.toString(a[0],a[1])})};h.concatenate=function(d,e){return g(d,e,function(b,e,a){b=[];if(1>a.length)return"";if(c.isArray(a[0])){e=c.defaultUndefined(a[2],"");for(var d=0;d<a[0].length;d++)b[d]=c.toString(a[0][d],
e);return 1<a.length?b.join(a[1]):b.join("")}for(d=0;d<a.length;d++)b[d]=c.toString(a[d]);return b.join("")})};h.reverse=function(d,e){return g(d,e,function(b,d,a){c.pcCheck(a,1,1);if(c.isArray(a[0]))return b=a[0].slice(0),b.reverse(),b;b=c.toString(a[0]);d="";for(a=b.length-1;0<=a;a--)d+=b[a];return d})}}});