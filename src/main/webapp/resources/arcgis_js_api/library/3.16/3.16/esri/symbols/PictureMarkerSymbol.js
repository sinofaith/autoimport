// All material copyright ESRI, All Rights Reserved, unless otherwise specified.
// See http://js.arcgis.com/3.16/esri/copyright.txt for details.
//>>built
define("esri/symbols/PictureMarkerSymbol","dojo/_base/declare dojo/_base/lang dojo/sniff dojox/gfx/_base ../kernel ../lang ../urlUtils ./MarkerSymbol".split(" "),function(d,f,h,e,k,l,m,n){var g={url:"",width:12,height:12,angle:0,xoffset:0,yoffset:0};d=d(n,{declaredClass:"esri.symbol.PictureMarkerSymbol",type:"picturemarkersymbol",constructor:function(a,c,b){a?f.isString(a)?(this.url=a,c&&(this.width=c),b&&(this.height=b)):(this.width=e.pt2px(a.width),this.height=e.pt2px(a.height),c=a.imageData,!(9>
h("ie"))&&c&&(b=this.url,this.url="data:"+(a.contentType||"image")+";base64,"+c,this.imageData=b)):(f.mixin(this,g),this.width=e.pt2px(this.width),this.height=e.pt2px(this.height))},getStroke:function(){return null},getFill:function(){return null},setWidth:function(a){this.width=a;return this},setHeight:function(a){this.height=a;return this},setUrl:function(a){a!==this.url&&(delete this.imageData,delete this.contentType);this.url=a;return this},getShapeDescriptors:function(){return{defaultShape:{type:"image",
x:-Math.round(this.width/2),y:-Math.round(this.height/2),width:this.width,height:this.height,src:this.url||""},fill:null,stroke:null}},toJson:function(){var a=this.url,c=this.imageData;if(0===a.indexOf("data:"))var b=a,a=c,c=b.indexOf(";base64,")+8,c=b.substr(c);if(f.isString(a)&&(0===a.indexOf("/")||0===a.indexOf("//")||0===a.indexOf("./")||0===a.indexOf("../")))a=m.getAbsoluteUrl(a);var b=e.px2pt(this.width),b=isNaN(b)?void 0:b,d=e.px2pt(this.height),d=isNaN(d)?void 0:d,a=l.fixJson(f.mixin(this.inherited("toJson",
arguments),{type:"esriPMS",url:a,imageData:c,contentType:this.contentType,width:b,height:d}));delete a.color;delete a.size;a.imageData||delete a.imageData;return a}});d.defaultProps=g;h("extend-esri")&&(f.setObject("symbol.PictureMarkerSymbol",d,k),k.symbol.defaultPictureMarkerSymbol=g);return d});