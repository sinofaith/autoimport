// All material copyright ESRI, All Rights Reserved, unless otherwise specified.
// See http://js.arcgis.com/3.16/esri/copyright.txt for details.
//>>built
define("esri/arcade/functions/geometry","require exports ../../geometry/Geometry ../../geometry/Polygon ../../geometry/Polyline ../../geometry/Point ../../geometry/Extent ../../geometry/Multipoint ../../geometry/jsonUtils ../languageUtils ../Dictionary ../Feature".split(" "),function(x,v,n,p,q,s,t,r,w,e,h,f){v.registerFunctions=function(k,l,u){k.polygon=function(c,d){return l(c,d,function(b,g,a){e.pcCheck(a,1,1);b=null;if(a[0]instanceof h){if(b=e.fixSpatialReference(f.parseGeometryFromDictionary(a[0]),
c.spatialReference),!1===b instanceof p)throw Error("Illegal Parameter");}else b=e.fixSpatialReference(new p(JSON.parse(a[0])),c.spatialReference);if(!1===b.spatialReference.equals(c.spatialReference))throw Error("Cannot create Geometry in this SpatialReference. Engine is using a different spatial reference.");return b})};k.polyline=function(c,d){return l(c,d,function(b,g,a){e.pcCheck(a,1,1);b=null;if(a[0]instanceof h){if(b=e.fixSpatialReference(f.parseGeometryFromDictionary(a[0]),c.spatialReference),
!1===b instanceof q)throw Error("Illegal Parameter");}else b=e.fixSpatialReference(new q(JSON.parse(a[0])),c.spatialReference);if(!1===b.spatialReference.equals(c.spatialReference))throw Error("Cannot create Geometry in this SpatialReference. Engine is using a different spatial reference.");return b})};k.point=function(c,d){return l(c,d,function(b,g,a){e.pcCheck(a,1,1);b=null;if(a[0]instanceof h){if(b=e.fixSpatialReference(f.parseGeometryFromDictionary(a[0]),c.spatialReference),!1===b instanceof s)throw Error("Illegal Parameter");
}else b=e.fixSpatialReference(new s(JSON.parse(a[0])),c.spatialReference);if(!1===b.spatialReference.equals(c.spatialReference))throw Error("Cannot create Geometry in this SpatialReference. Engine is using a different spatial reference.");return b})};k.multipoint=function(c,d){return l(c,d,function(b,g,a){e.pcCheck(a,1,1);b=null;if(a[0]instanceof h){if(b=e.fixSpatialReference(f.parseGeometryFromDictionary(a[0]),c.spatialReference),!1===b instanceof r)throw Error("Illegal Parameter");}else b=e.fixSpatialReference(new r(JSON.parse(a[0])),
c.spatialReference);if(!1===b.spatialReference.equals(c.spatialReference))throw Error("Cannot create Geometry in this SpatialReference. Engine is using a different spatial reference.");return b})};k.envelope=function(c,d){return l(c,d,function(b,g,a){e.pcCheck(a,1,1);b=null;if(a[0]instanceof h)b=e.fixSpatialReference(f.parseGeometryFromDictionary(a[0]),c.spatialReference);else{if(a[0]instanceof p||a[0]instanceof q||a[0]instanceof r)return a[0].extent;if(a[0]instanceof t)return a[0];b=e.fixSpatialReference(new t(JSON.parse(a[0])),
c.spatialReference)}if(!1===b.spatialReference.equals(c.spatialReference))throw Error("Cannot create Geometry in this SpatialReference. Engine is using a different spatial reference.");return b})};k.geometry=function(c,d){return l(c,d,function(b,g,a){e.pcCheck(a,1,1);b=null;b=a[0]instanceof f?e.fixSpatialReference(a[0].geometry,c.spatialReference):a[0]instanceof h?e.fixSpatialReference(f.parseGeometryFromDictionary(a[0]),c.spatialReference):e.fixSpatialReference(w.fromJson(JSON.parse(a[0])),c.spatialReference);
if(null!==b&&!1===b.spatialReference.equals(c.spatialReference))throw Error("Cannot create Geometry in this SpatialReference. Engine is using a different spatial reference.");return b})};k.feature=function(c,d){return l(c,d,function(b,g,a){if(0===a.length)throw Error("Missing Parameters");b=null;if(1===a.length)if(e.isString(a[0]))b=f.fromJson(JSON.parse(a[0]));else if(a[0]instanceof f)b=new f(a[0]);else if(a[0]instanceof n)b=new f(null,a[0]);else if(a[0]instanceof h)b=a[0].hasField("geometry")?a[0].field("geometry"):
null,g=a[0].hasField("attributes")?a[0].field("attributes"):null,null!==b&&b instanceof h&&(b=f.parseGeometryFromDictionary(b)),null!==g&&(g=f.parseAttributesFromDictionary(g)),b=new f(g,b);else throw Error("Illegal Argument");else{if(2===a.length){g=b=null;if(null!==a[0])if(a[0]instanceof n)b=a[0];else if(b instanceof h)b=f.parseGeometryFromDictionary(a[0]);else throw Error("Illegal Argument");if(null!==a[1])if(a[1]instanceof h)g=f.parseAttributesFromDictionary(a[1]);else throw Error("Illegal Argument");
}else{b=null;g={};if(null!==a[0])if(a[0]instanceof n)b=a[0];else if(b instanceof h)b=f.parseGeometryFromDictionary(a[0]);else throw Error("Illegal Argument");for(var m=1;m<a.length;m+=2)if(e.isString(a[m])){var d=a[m+1];if(null===d||void 0===d||e.isString(d)||isNaN(d)||e.isDate(d)||e.isNumber(d)||e.isBoolean(d)){if(u(d))throw Error("Illegal Argument");g[a[m]]=d}else throw Error("Illegal Argument");}else throw Error("Illegal Argument");}b=new f(g,b)}b.geometry=e.fixSpatialReference(b.geometry,c.spatialReference);
return b})};k.dictionary=function(c,d){return l(c,d,function(b,c,a){if(0===a.length)throw Error("Missing Parameters");if(0!==a.length%2)throw Error("Missing Parameters");b={};for(c=0;c<a.length;c+=2)if(e.isString(a[c])){var d=a[c+1];if(null===d||void 0===d||e.isString(d)||isNaN(d)||e.isDate(d)||e.isNumber(d)||e.isBoolean(d)){if(u(d))throw Error("Illegal Argument");b[a[c]]=d}else throw Error("Illegal Argument");}else throw Error("Illegal Argument");return new h(b)})}}});