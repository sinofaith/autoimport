//>>built
define("dojox/io/scriptFrame",["dojo/main","dojo/io/script","dojo/io/iframe"],function(e,f,g){e.deprecated("dojox.io.scriptFrame","dojo.io.script now supports parallel requests without dojox.io.scriptFrame","2.0");e.getObject("io.scriptFrame",!0,dojox);dojox.io.scriptFrame={_waiters:{},_loadedIds:{},_getWaiters:function(a){return this._waiters[a]||(this._waiters[a]=[])},_fixAttachUrl:function(a){},_loaded:function(a){var b=this._getWaiters(a);this._loadedIds[a]=!0;this._waiters[a]=null;for(var d=
0;d<b.length;d++){var c=b[d];c.frameDoc=g.doc(e.byId(a));f.attach(c.id,c.url,c.frameDoc)}}};var k=f._canAttach,h=dojox.io.scriptFrame;f._canAttach=function(a){var b=a.args.frameDoc;if(b&&e.isString(b)){var d=e.byId(b),c=h._getWaiters(b);d?h._loadedIds[b]?(a.frameDoc=g.doc(d),this.attach(a.id,a.url,a.frameDoc)):c.push(a):(c.push(a),g.create(b,dojox._scopeName+".io.scriptFrame._loaded('"+b+"');"));return!1}return k.apply(this,arguments)};return dojox.io.scriptFrame});