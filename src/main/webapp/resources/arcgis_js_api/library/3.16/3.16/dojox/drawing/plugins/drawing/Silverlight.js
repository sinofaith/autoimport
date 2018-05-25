//>>built
define("dojox/drawing/plugins/drawing/Silverlight",["dijit","dojo","dojox"],function(k,c,g){c.provide("dojox.drawing.plugins.drawing.Silverlight");g.drawing.plugins.drawing.Silverlight=g.drawing.util.oo.declare(function(e){"silverlight"==g.gfx.renderer&&(this.mouse=e.mouse,this.stencils=e.stencils,this.anchors=e.anchors,this.canvas=e.canvas,this.util=e.util,c.connect(this.stencils,"register",this,function(a){var d,b,h,f,e,g=this;(function(){d=a.container.connect("onmousedown",function(b){b.superTarget=
a;g.mouse.down(b)})})();b=c.connect(a,"setTransform",this,function(){});h=c.connect(a,"onBeforeRender",function(){});f=c.connect(a,"onRender",this,function(){});e=c.connect(a,"destroy",this,function(){c.forEach([d,b,h,f,e],c.disconnect,c)})}),c.connect(this.anchors,"onAddAnchor",this,function(a){var d=a.shape.connect("onmousedown",this.mouse,function(b){b.superTarget=a;this.down(b)}),b=c.connect(a,"disconnectMouse",this,function(){c.disconnect(d);c.disconnect(b)})}),this.mouse._down=function(a){var d=
this._getXY(a),b=d.x-this.origin.x,c=d.y-this.origin.y,b=b*this.zoom,c=c*this.zoom;this.origin.startx=b;this.origin.starty=c;this._lastx=b;this._lasty=c;this.drawingType=this.util.attr(a,"drawingType")||"";d=this._getId(a);b={x:b,y:c,id:d};console.log(" \x3e \x3e \x3e id:",d,"drawingType:",this.drawingType,"evt:",a);this.onDown(b);this._clickTime=(new Date).getTime();this._lastClickTime&&this._clickTime-this._lastClickTime<this.doublClickSpeed&&(a=this.eventName("doubleClick"),console.warn("DOUBLE CLICK",
a,b),this._broadcastEvent(a,b));this._lastClickTime=this._clickTime},this.mouse.down=function(a){clearTimeout(this.__downInv);"surface"==this.util.attr(a,"drawingType")?this.__downInv=setTimeout(c.hitch(this,function(){this._down(a)}),500):this._down(a)},this.mouse._getXY=function(a){if(a.pageX)return{x:a.pageX,y:a.pageY,cancelBubble:!0};console.log("EVT",a);for(var c in a);console.log("EVTX",a.x);return void 0!==a.x?{x:a.x+this.origin.x,y:a.y+this.origin.y}:{x:a.pageX,y:a.pageY}},this.mouse._getId=
function(a){return this.util.attr(a,"id")},this.util.attr=function(a,d,b,e){if(!a)return!1;try{var f;f=a.superTarget?a.superTarget:a.superClass?a.superClass:a.target?a.target:a;if(void 0!==b)return a[d]=b;if(f.tagName){if("drawingType"==d&&"object"==f.tagName.toLowerCase())return"surface";var g=c.attr(f,d)}return g=f[d]}catch(k){return!1}})},{})});