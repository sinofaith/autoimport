//>>built
define("dojox/data/ItemExplorer",["dijit","dojo","dojox","dojo/require!dijit/Tree,dijit/Dialog,dijit/Menu,dijit/form/ValidationTextBox,dijit/form/Textarea,dijit/form/Button,dijit/form/RadioButton,dijit/form/FilteringSelect"],function(d,c,u){c.provide("dojox.data.ItemExplorer");c.require("dijit.Tree");c.require("dijit.Dialog");c.require("dijit.Menu");c.require("dijit.form.ValidationTextBox");c.require("dijit.form.Textarea");c.require("dijit.form.Button");c.require("dijit.form.RadioButton");c.require("dijit.form.FilteringSelect");
(function(){var p=function(b,a,c){var e=b.getValues(a,c);2>e.length&&(e=b.getValue(a,c));return e};c.declare("dojox.data.ItemExplorer",d.Tree,{useSelect:!1,refSelectSearchAttr:null,constructor:function(b){c.mixin(this,b);var a=this,f={},e=this.rootModelNode={value:f,id:"root"};this._modelNodeIdMap={};this._modelNodePropMap={};var g=1;this.model={getRoot:function(a){a(e)},mayHaveChildren:function(a){return a.value&&"object"==typeof a.value&&!(a.value instanceof Date)},getChildren:function(b,c,e){function g(){if(n)d=
a.store.getAttributes(h),m=h;else if(h&&"object"==typeof h){m=b.value;d=[];for(var e in h)h.hasOwnProperty(e)&&("__id"!=e&&"__clientId"!=e)&&d.push(e)}if(d){for(var f=0;e=d[f++];)r.push({property:e,value:n?p(a.store,h,e):h[e],parent:m});r.push({addNew:!0,parent:m,parentNode:b})}c(r)}var d,m,h=b.value,r=[];if(h==f)c([]);else{var n=a.store&&a.store.isItem(h,!0);n&&!a.store.isItemLoaded(h)?a.store.loadItem({item:h,onItem:function(a){h=a;g()}}):g()}},getIdentity:function(b){if(!b.id&&(b.addNew&&(b.property=
"--addNew"),b.id=g++,a.store)){if(a.store.isItem(b.value)){var c=a.store.getIdentity(b.value);(a._modelNodeIdMap[c]=a._modelNodeIdMap[c]||[]).push(b)}b.parent&&(c=a.store.getIdentity(b.parent)+"."+b.property,(a._modelNodePropMap[c]=a._modelNodePropMap[c]||[]).push(b))}return b.id},getLabel:function(a){return a===e?"Object Properties":a.addNew?a.parent instanceof Array?"Add new value":"Add new property":a.property+": "+(a.value instanceof Array?"("+a.value.length+" elements)":a.value)},onChildrenChange:function(a){},
onChange:function(a){}}},postCreate:function(){this.inherited(arguments);c.connect(this,"onClick",function(a,b){this.lastFocused=b;a.addNew?this._addProperty():this._editProperty()});var b=new d.Menu({targetNodeIds:[this.rootNode.domNode],id:"contextMenu"});c.connect(b,"_openMyself",this,function(a){if(a=d.getEnclosingWidget(a.target)){var f=a.item;this.store.isItem(f.value,!0)&&!f.parent?(c.forEach(b.getChildren(),function(a){a.attr("disabled","Add"!=a.label)}),this.lastFocused=a):f.value&&"object"==
typeof f.value&&!(f.value instanceof Date)?(c.forEach(b.getChildren(),function(a){a.attr("disabled","Add"!=a.label&&"Delete"!=a.label)}),this.lastFocused=a):f.property&&0<=c.indexOf(this.store.getIdentityAttributes(),f.property)?(this.focusNode(a),alert("Cannot modify an Identifier node.")):f.addNew?this.focusNode(a):(c.forEach(b.getChildren(),function(a){a.attr("disabled","Edit"!=a.label&&"Delete"!=a.label)}),this.lastFocused=a)}});b.addChild(new d.MenuItem({label:"Add",onClick:c.hitch(this,"_addProperty")}));
b.addChild(new d.MenuItem({label:"Edit",onClick:c.hitch(this,"_editProperty")}));b.addChild(new d.MenuItem({label:"Delete",onClick:c.hitch(this,"_destroyProperty")}));b.startup()},store:null,setStore:function(b){this.store=b;var a=this;this._editDialog&&(this._editDialog.destroyRecursive(),delete this._editDialog);c.connect(b,"onSet",function(b,c,g,d){var p=a.store.getIdentity(b);if((b=a._modelNodeIdMap[p])&&(void 0===g||void 0===d||g instanceof Array||d instanceof Array||"object"==typeof g||"object"==
typeof d))for(g=0;g<b.length;g++)(function(b){a.model.getChildren(b,function(c){a.model.onChildrenChange(b,c)})})(b[g]);if(b=a._modelNodePropMap[p+"."+c])for(g=0;g<b.length;g++)b[g].value=d,a.model.onChange(b[g])});this.rootNode.setChildItems([])},setItem:function(b){(this._modelNodeIdMap={})[this.store.getIdentity(b)]=[this.rootModelNode];this._modelNodePropMap={};this.rootModelNode.value=b;var a=this;this.model.getChildren(this.rootModelNode,function(b){a.rootNode.setChildItems(b)})},refreshItem:function(){this.setItem(this.rootModelNode.value)},
_createEditDialog:function(){this._editDialog=new d.Dialog({title:"Edit Property",execute:c.hitch(this,"_updateItem"),preload:!0});this._editDialog.placeAt(c.body());this._editDialog.startup();var b=c.doc.createElement("div"),a=c.doc.createElement("label");c.attr(a,"for","property");c.style(a,"fontWeight","bold");c.attr(a,"innerHTML","Property:");b.appendChild(a);(new d.form.ValidationTextBox({name:"property",value:"",required:!0,disabled:!0})).placeAt(b);b.appendChild(c.doc.createElement("br"));
b.appendChild(c.doc.createElement("br"));(new d.form.RadioButton({name:"itemType",value:"value",onClick:c.hitch(this,function(){this._enableFields("value")})})).placeAt(b);a=c.doc.createElement("label");c.attr(a,"for","value");c.attr(a,"innerHTML","Value (JSON):");b.appendChild(a);a=c.doc.createElement("div");c.addClass(a,"value");(new d.form.Textarea({name:"jsonVal"})).placeAt(a);b.appendChild(a);(new d.form.RadioButton({name:"itemType",value:"reference",onClick:c.hitch(this,function(){this._enableFields("reference")})})).placeAt(b);
a=c.doc.createElement("label");c.attr(a,"for","_reference");c.attr(a,"innerHTML","Reference (ID):");b.appendChild(a);b.appendChild(c.doc.createElement("br"));a=c.doc.createElement("div");c.addClass(a,"reference");this.useSelect?(new d.form.FilteringSelect({name:"_reference",store:this.store,searchAttr:this.refSelectSearchAttr||this.store.getIdentityAttributes()[0],required:!1,value:null,pageSize:10})).placeAt(a):(new d.form.ValidationTextBox({name:"_reference",value:"",promptMessage:"Enter the ID of the item to reference",
isValid:c.hitch(this,function(a){return!0})})).placeAt(a);b.appendChild(a);b.appendChild(c.doc.createElement("br"));b.appendChild(c.doc.createElement("br"));a=document.createElement("div");a.setAttribute("dir","rtl");(new d.form.Button({type:"reset",label:"Cancel"})).placeAt(a).onClick=c.hitch(this._editDialog,"onCancel");(new d.form.Button({type:"submit",label:"OK"})).placeAt(a);b.appendChild(a);this._editDialog.attr("content",b)},_enableFields:function(b){switch(b){case "reference":c.query(".value [widgetId]",
this._editDialog.containerNode).forEach(function(a){d.getEnclosingWidget(a).attr("disabled",!0)});c.query(".reference [widgetId]",this._editDialog.containerNode).forEach(function(a){d.getEnclosingWidget(a).attr("disabled",!1)});break;case "value":c.query(".value [widgetId]",this._editDialog.containerNode).forEach(function(a){d.getEnclosingWidget(a).attr("disabled",!1)}),c.query(".reference [widgetId]",this._editDialog.containerNode).forEach(function(a){d.getEnclosingWidget(a).attr("disabled",!0)})}},
_updateItem:function(b){function a(){try{var a,c=[],n=b.property;if(t){for(;!k.isItem(e.parent,!0);)f=f.getParent(),c.push(e.property),e=f.item;if(0==c.length)k.setValue(e.parent,e.property,d);else{l=p(k,e.parent,e.property);l instanceof Array&&(l=l.concat());for(a=l;1<c.length;)a=a[c.pop()];a[c]=d;k.setValue(e.parent,e.property,l)}}else if(k.isItem(q,!0))k.isItemLoaded(q)?(q instanceof Array&&(n=q.length),k.setValue(q,n,d)):k.loadItem({item:q,onItem:function(a){a instanceof Array&&(n=a.length);k.setValue(a,
n,d)}});else{for(e.value instanceof Array?c.push(e.value.length):c.push(b.property);!k.isItem(e.parent,!0);)f=f.getParent(),c.push(e.property),e=f.item;for(a=l=p(k,e.parent,e.property);1<c.length;)a=a[c.pop()];a[c]=d;k.setValue(e.parent,e.property,l)}}catch(m){alert(m)}}var f,e,d,l,t="Edit Property"==this._editDialog.attr("title"),s=this._editDialog,k=this.store;if(s.validate()){f=this.lastFocused;e=f.item;var q=e.value;e.addNew&&(q=f.item.parent,f=f.getParent(),e=f.item);d=null;switch(b.itemType){case "reference":this.store.fetchItemByIdentity({identity:b._reference,
onItem:function(b){d=b;a()},onError:function(){alert("The id could not be found")}});break;case "value":var m=b.jsonVal;d=c.fromJson(m);"function"==typeof d&&(d.toString=function(){return m});a()}}else s.show()},_editProperty:function(){var b=c.mixin({},this.lastFocused.item);this._editDialog?this._editDialog.reset():this._createEditDialog();if(0<=c.indexOf(this.store.getIdentityAttributes(),b.property))alert("Cannot Edit an Identifier!");else if(this._editDialog.attr("title","Edit Property"),d.getEnclosingWidget(c.query("input",
this._editDialog.containerNode)[0]).attr("disabled",!0),this.store.isItem(b.value,!0))b.parent&&(b.itemType="reference",this._enableFields(b.itemType),b._reference=this.store.getIdentity(b.value),this._editDialog.attr("value",b),this._editDialog.show());else if(!b.value||"object"!=typeof b.value||b.value instanceof Date)b.itemType="value",this._enableFields(b.itemType),b.jsonVal="function"==typeof b.value?b.value.toString():b.value instanceof Date?'new Date("'+b.value+'")':c.toJson(b.value),this._editDialog.attr("value",
b),this._editDialog.show()},_destroyProperty:function(){for(var b=this.lastFocused,a=b.item,d=[];!this.store.isItem(a.parent,!0)||a.parent instanceof Array;)b=b.getParent(),d.push(a.property),a=b.item;if(0<=c.indexOf(this.store.getIdentityAttributes(),a.property))alert("Cannot Delete an Identifier!");else try{if(0<d.length){var e,g=p(this.store,a.parent,a.property);for(e=g;1<d.length;)e=e[d.pop()];c.isArray(e)?e.splice(d,1):delete e[d];this.store.setValue(a.parent,a.property,g)}else this.store.unsetAttribute(a.parent,
a.property)}catch(l){alert(l)}},_addProperty:function(){var b=this.lastFocused.item,a=b.value,f=c.hitch(this,function(){var b=null;this._editDialog?this._editDialog.reset():this._createEditDialog();a instanceof Array?(b=a.length,d.getEnclosingWidget(c.query("input",this._editDialog.containerNode)[0]).attr("disabled",!0)):d.getEnclosingWidget(c.query("input",this._editDialog.containerNode)[0]).attr("disabled",!1);this._editDialog.attr("title","Add Property");this._enableFields("value");this._editDialog.attr("value",
{itemType:"value",property:b});this._editDialog.show()});b.addNew&&(b=this.lastFocused.getParent().item,a=this.lastFocused.item.parent);b.property&&0<=c.indexOf(this.store.getIdentityAttributes(),b.property)?alert("Cannot add properties to an ID node!"):this.store.isItem(a,!0)&&!this.store.isItemLoaded(a)?this.store.loadItem({item:a,onItem:function(b){a=b;f()}}):f()}})})()});