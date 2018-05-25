// All material copyright ESRI, All Rights Reserved, unless otherwise specified.
// See http://js.arcgis.com/3.16/esri/copyright.txt for details.
//>>built
require({cache:{"url:esri/dijit/editing/templates/AttachmentEditor.html":"\x3cdiv class\x3d\"attachmentEditor\"\x3e\r\n    \x3cbr /\x3e\r\n    \x3cdiv\x3e\r\n        \x3cb\x3e${NLS_attachments}\x3c/b\x3e\r\n        \x3chr /\x3e\r\n        \x3cdiv dojoAttachPoint\x3d\"_attachmentError\" style\x3d'color:red;display:none'\x3e\x3c/div\x3e\r\n        \x3cbr /\x3e\r\n        \x3cspan dojoAttachPoint\x3d'_attachmentList' style\x3d'word-wrap: break-word;'\x3e\x3c/span\x3e\r\n        \x3cbr\x3e\x3cbr\x3e\r\n        \x3cdiv data-dojo-type\x3d\"dijit/ProgressBar\" dojoAttachPoint\x3d\"_attachmentProgress\" indeterminate\x3d\"true\" style\x3d'display:none'\x3e\x3c/div\x3e\r\n        \x3cbr /\x3e        \r\n        \x3cform dojoAttachPoint\x3d'_uploadForm'\x3e ${NLS_add}:\x26nbsp;\x26nbsp;\x3cinput type\x3d'file' name\x3d'attachment' dojoAttachPoint\x3d'_uploadField' /\x3e \x3c/form\x3e\r\n    \x3c/div\x3e\r\n\x3c/div\x3e"}});
define("esri/dijit/editing/AttachmentEditor","dojo/_base/declare dojo/_base/lang dojo/_base/connect dojo/_base/array dojo/_base/kernel dojo/has dojo/query dojo/io-query dojo/dom-attr dijit/_Widget dijit/_Templated dijit/ProgressBar ../../kernel ../../lang ../../domUtils dojo/text!./templates/AttachmentEditor.html dojo/i18n!../../nls/jsapi dojo/NodeList-dom".split(" "),function(g,e,c,f,k,l,u,m,n,p,q,v,r,h,d,s,t){g=g([p,q],{declaredClass:"esri.dijit.editing.AttachmentEditor",widgetsInTemplate:!0,templateString:s,
_listHtml:"\x3cspan id\x3d'node_${oid}_${attid}'\x3e\x3ca href\x3d'${href}' target\x3d'_blank'\x3e${name}\x3c/a\x3e",_deleteBtnHtml:"(\x3cspan style\x3d'cursor:pointer;color:red;font-weight:bold;' class\x3d'deleteAttachment' id\x3d'${attid}');'\x3eX\x3c/span\x3e)",_endHtml:"\x3cbr/\x3e\x3c/span\x3e",_aeConnects:[],_layerEditingCapChecked:{},_layerEditingCap:{},constructor:function(a,b){e.mixin(this,t.widgets.attachmentEditor)},startup:function(){this.inherited(arguments);this._uploadField_connect=
c.connect(this._uploadField,"onchange",this,function(){0<this._uploadField.value.length&&this._addAttachment()});this._uploadFieldFocus_connect=c.connect(this._uploadField,"onfocus",e.hitch(this,function(a){d.hide(this._attachmentError)}))},destroy:function(){f.forEach(this._aeConnects,c.disconnect);c.disconnect(this._uploadField_connect);c.disconnect(this._uploadFieldFocus_connect);this.inherited(arguments)},showAttachments:function(a,b){this._attachmentList.innerHTML=this.NLS_none;this._uploadField.value=
"";f.forEach(this.domNode.children,function(a,b){d.show(a)});d.hide(this._attachmentError);if(a&&(this._featureLayer=a.getLayer()||b))if("esri.layers.FeatureLayer"!==this._featureLayer.declaredClass||!this._featureLayer.getEditCapabilities){if(!b||!b.getEditCapabilities())d.hide(this._uploadForm),f.forEach(this.domNode.children,function(a,b){d.hide(a)})}else this._currentLayerId=this._featureLayer.id,this._layerEditingCapChecked[this._currentLayerId]||(this._layerEditingCap[this._currentLayerId]=
this._featureLayer.getEditCapabilities(),this._layerEditingCapChecked[this._currentLayerId]=!0),this._featureCanUpdate=this._featureLayer.getEditCapabilities({feature:a}).canUpdate,this._oid=a.attributes[this._featureLayer.objectIdField],this._getAttachments(a)},_getAttachments:function(a){this._featureLayer&&this._featureLayer.queryAttachmentInfos&&this._featureLayer.queryAttachmentInfos(this._oid,e.hitch(this,"_onQueryAttachmentInfosComplete"))},_addAttachment:function(){d.hide(this._attachmentError);
!this._featureLayer||!this._featureLayer.addAttachment?this._tempUpload=this._uploadForm:(d.show(this._attachmentProgress),this._featureLayer.addAttachment(this._oid,this._uploadForm,e.hitch(this,"_onAddAttachmentComplete"),e.hitch(this,"_onAddAttachmentError")))},_chainAttachment:function(a,b){this._tempUpload&&(d.show(this._attachmentProgress),b.addAttachment(a,this._tempUpload,e.hitch(this,"_onAddAttachmentComplete"),e.hitch(this,"_onAddAttachmentError")));this._tempUpload=null},_deleteAttachment:function(a,
b){d.show(this._attachmentProgress);this._featureLayer.deleteAttachments(a,[b],e.hitch(this,"_onDeleteAttachmentComplete"))},_onQueryAttachmentInfosComplete:function(a){var b=this._listHtml+this._deleteBtnHtml+this._endHtml;this._uploadForm.style.display="block";!this._featureCanUpdate&&this._layerEditingCap[this._currentLayerId].canUpdate||!this._layerEditingCap[this._currentLayerId].canCreate&&!this._layerEditingCap[this._currentLayerId].canUpdate?(b=this._listHtml+this._endHtml,this._uploadForm.style.display=
"none"):this._layerEditingCap[this._currentLayerId].canCreate&&!this._layerEditingCap[this._currentLayerId].canUpdate&&(b=this._listHtml+this._endHtml);var d=this._attachmentList;a=f.map(a,e.hitch(this,function(a){return h.substitute({href:a.url,name:a.name,oid:a.objectId,attid:a.id},b)}));d.innerHTML=a.join("")||this.NLS_none;this._updateConnects()},_onAddAttachmentComplete:function(a){d.hide(this._attachmentProgress.domNode);var b=this._attachmentList,e=this._uploadField,c=e.value,f=c.lastIndexOf("\\");
-1<f&&(c=c.substring(f+1,c.length));var c=c.replace(/\ /g,"_"),f=m.objectToQuery({gdbVersion:this._featureLayer.gdbVersion,token:this._featureLayer._getToken()}),g=this._listHtml+this._deleteBtnHtml+this._endHtml;this._layerEditingCap[this._currentLayerId].canCreate&&!this._layerEditingCap[this._currentLayerId].canUpdate&&(g=this._listHtml+this._endHtml);a=h.substitute({href:this._featureLayer._url.path+"/"+a.objectId+"/attachments/"+a.attachmentId+(f?"?"+f:""),name:c,oid:a.objectId,attid:a.attachmentId},
g);b.innerHTML=b.innerHTML==this.NLS_none?a:b.innerHTML+a;this._updateConnects();e.value=""},_onAddAttachmentError:function(a){d.hide(this._attachmentProgress.domNode);if(a&&h.isDefined(a.code)){var b=this._attachmentError;n.set(b,"innerHTML",(400===a.code?this.NLS_fileNotSupported:a.message||a.details&&a.details.length&&a.details[0])||this.NLS_error);d.show(b)}},_onDeleteAttachmentComplete:function(a){d.hide(this._attachmentProgress.domNode);var b=this._attachmentList;if(f.every(a,function(a){return a.success})&&
(k.query("#node_"+a[0].objectId+"_"+a[0].attachmentId).orphan(),!b.children||!b.children.length))b.innerHTML=this.NLS_none},_updateConnects:function(){f.forEach(this._aeConnects,c.disconnect);k.query(".deleteAttachment").forEach(function(a){this._aeConnects.push(c.connect(a,"onclick",e.hitch(this,"_deleteAttachment",this._oid,a.id)))},this)}});l("extend-esri")&&e.setObject("dijit.editing.AttachmentEditor",g,r);return g});