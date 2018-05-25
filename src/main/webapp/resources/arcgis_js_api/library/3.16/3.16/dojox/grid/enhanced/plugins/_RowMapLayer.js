//>>built
define("dojox/grid/enhanced/plugins/_RowMapLayer",["dojo/_base/array","dojo/_base/declare","dojo/_base/kernel","dojo/_base/lang","./_StoreLayer"],function(p,q,r,l,s){var t=function(b){b.sort(function(a,b){return a-b});for(var c=[[b[0]]],a=1,d=0;a<b.length;++a)b[a]==b[a-1]+1?c[d].push(b[a]):c[++d]=[b[a]];return c},n=function(b,c){return c?l.hitch(b||r.global,c):function(){}};return q("dojox.grid.enhanced.plugins._RowMapLayer",s._StoreLayer,{tags:["reorder"],constructor:function(b){this._map={};this._revMap=
{};this.grid=b;this._oldOnDelete=b._onDelete;var c=this;b._onDelete=function(a){c._onDelete(a);c._oldOnDelete.call(b,a)};this._oldSort=b.sort;b.sort=function(){c.clearMapping();c._oldSort.apply(b,arguments)}},uninitialize:function(){this.grid._onDelete=this._oldOnDelete;this.grid.sort=this._oldSort},setMapping:function(b){this._store.forEachLayer(function(a){if("rowmap"===a.name())return!1;if(a.onRowMappingChange)a.onRowMappingChange(b);return!0},!1);var c,a,d,f={};for(c in b)c=parseInt(c,10),a=b[c],
"number"==typeof a&&(c in this._revMap?(d=this._revMap[c],delete this._revMap[c]):d=c,d==a?(delete this._map[d],f[a]="eq"):(this._map[d]=a,f[a]=d));for(a in f)"eq"===f[a]?delete this._revMap[parseInt(a,10)]:this._revMap[parseInt(a,10)]=f[a]},clearMapping:function(){this._map={};this._revMap={}},_onDelete:function(b){var c=this.grid._getItemIndex(b,!0);if(c in this._revMap){b=[];var a,d=this._revMap[c];delete this._map[d];delete this._revMap[c];for(a in this._revMap)a=parseInt(a,10),this._revMap[a]>
d&&--this._revMap[a];for(a in this._revMap)a=parseInt(a,10),a>c&&b.push(a);b.sort(function(a,b){return b-a});for(c=b.length-1;0<=c;--c)a=b[c],this._revMap[a-1]=this._revMap[a],delete this._revMap[a];this._map={};for(a in this._revMap)this._map[this._revMap[a]]=a}},_fetch:function(b){var c=0,a,d=b.start||0;for(a in this._revMap)a=parseInt(a,10),a>=d&&++c;if(0<c){var f=[],e,g={},h=0<b.count?b.count:-1;if(0<h)for(e=0;e<h;++e)a=d+e,a=a in this._revMap?this._revMap[a]:a,g[a]=e,f.push(a);else for(e=0;!(a=
d+e,a in this._revMap&&(--c,a=this._revMap[a]),g[a]=e,f.push(a),0>=c);++e);this._subFetch(b,this._getRowArrays(f),0,[],g,b.onComplete,d,h);return b}return l.hitch(this._store,this._originFetch)(b)},_getRowArrays:function(b){return t(b)},_subFetch:function(b,c,a,d,f,e,g,h){var k=c[a],m=this,l=b.start=k[0];b.count=k[k.length-1]-k[0]+1;b.onComplete=function(k){p.forEach(k,function(a,b){var c=l+b;c in f&&(d[f[c]]=a)});++a==c.length?0<h?(b.start=g,b.count=h,b.onComplete=e,n(b.scope,e)(d,b)):(b.start+=
k.length,delete b.count,b.onComplete=function(a){d=d.concat(a);b.start=g;b.onComplete=e;n(b.scope,e)(d,b)},m.originFetch(b)):m._subFetch(b,c,a,d,f,e,g,h)};m.originFetch(b)}})});