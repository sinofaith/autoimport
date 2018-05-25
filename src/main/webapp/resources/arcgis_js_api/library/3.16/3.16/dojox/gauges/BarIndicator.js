//>>built
define("dojox/gauges/BarIndicator",["dojo/_base/declare","dojo/_base/fx","dojo/_base/connect","dojo/_base/lang","./BarLineIndicator"],function(e,f,g,h,k){return e("dojox.gauges.BarIndicator",[k],{_getShapes:function(d){if(!this._gauge)return null;var a=this.value;a<this._gauge.min&&(a=this._gauge.min);a>this._gauge.max&&(a=this._gauge.max);a=this._gauge._getPosition(a);a==this.dataX&&(a=this.dataX+1);var b=this._gauge.dataY+Math.floor((this._gauge.dataHeight-this.width)/2)+this.offset,c=[];c[0]=d.createRect({x:this._gauge.dataX,
y:b,width:a-this._gauge.dataX,height:this.width});c[0].setStroke({color:this.color});c[0].setFill(this.color);c[1]=d.createLine({x1:this._gauge.dataX,y1:b,x2:a,y2:b});c[1].setStroke({color:this.highlight});this.highlight2&&(b--,c[2]=d.createLine({x1:this._gauge.dataX,y1:b,x2:a,y2:b}),c[2].setStroke({color:this.highlight2}));return c},_createShapes:function(d){for(var a in this.shape.children){a=this.shape.children[a];var b={},c;for(c in a)b[c]=a[c];"line"==a.shape.type?b.shape.x2=d+b.shape.x1:"rect"==
a.shape.type&&(b.width=d);a.setShape(b)}},_move:function(d){var a,b=this.value;b<this.min&&(b=this.min);b>this.max&&(b=this.max);a=this._gauge._getPosition(this.currentValue);this.currentValue=b;b=this._gauge._getPosition(b)-this._gauge.dataX;d?this._createShapes(b):a!=b&&(d=new f.Animation({curve:[a,b],duration:this.duration,easing:this.easing}),g.connect(d,"onAnimate",h.hitch(this,this._createShapes)),d.play())}})});