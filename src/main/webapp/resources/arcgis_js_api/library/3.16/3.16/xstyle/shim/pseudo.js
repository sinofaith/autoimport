//>>built
define("xstyle/shim/pseudo",[],function(){function c(b,a,c){e[b]||(e[b]=!0,document.attachEvent(b,function(b){for(var d=b.srcElement;b=d.currentStyle;)b.xstyle&&(a?d.className+=" "+c:function(a){setTimeout(function(){a.className=(" "+a.className+" ").replace(" "+c+" "," ").slice(1)},0)}(d)),d=d.parentNode}))}var e={};return{onPseudo:function(b,a){"hover"==b?(c("onmouseover",!0,"xstyle-hover"),c("onmouseout",!1,"xstyle-hover"),a.add(a.selector.replace(/:hover/,""),"xstyle: true"),a.add(a.selector.replace(/:hover/,
".xstyle-hover"),a.cssText)):"focus"==b&&(c("onactivate",!0,"xstyle-focus"),c("ondeactivate",!1,"xstyle-focusr"),a.add(a.selector.replace(/:hover/,""),"xstyle: true"),a.add(a.selector.replace(/:hover/,".xstyle-focus"),a.cssText))}}});