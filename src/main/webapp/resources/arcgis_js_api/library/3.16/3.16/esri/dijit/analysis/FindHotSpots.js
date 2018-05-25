// All material copyright ESRI, All Rights Reserved, unless otherwise specified.
// See http://js.arcgis.com/3.16/esri/copyright.txt for details.
//>>built
require({cache:{"url:esri/dijit/analysis/templates/FindHotSpots.html":'\x3cdiv class\x3d"esriAnalysis"\x3e\r\n  \x3cdiv data-dojo-type\x3d"dijit/layout/ContentPane" style\x3d"margin-top:0.5em; margin-bottom: 0.5em;"\x3e\r\n    \x3cdiv data-dojo-attach-point\x3d"_hotspotsToolContentTitle" class\x3d"analysisTitle"\x3e\r\n         \x3ctable class\x3d"esriFormTable" \x3e \r\n            \x3ctr\x3e\r\n              \x3ctd class\x3d"esriToolIconTd"\x3e\x3cdiv class\x3d"findHotSpotsIcon"\x3e\x3c/div\x3e\x3c/td\x3e\r\n              \x3ctd class\x3d"esriAlignLeading esriAnalysisTitle" data-dojo-attach-point\x3d"_toolTitle"\x3e${i18n.findHotSpots}\x3c/td\x3e\r\n              \x3ctd\x3e\r\n                \x3cdiv class\x3d"esriFloatTrailing" style\x3d"padding:0;"\x3e\r\n                    \x3cdiv class\x3d"esriFloatLeading"\x3e\r\n                      \x3ca href\x3d"#" class\x3d\'esriFloatLeading helpIcon\' esriHelpTopic\x3d"toolDescription"\x3e\x3c/a\x3e\r\n                    \x3c/div\x3e\r\n                    \x3cdiv class\x3d"esriFloatTrailing"\x3e\r\n                      \x3ca href\x3d"#" data-dojo-attach-point\x3d"_closeBtn" title\x3d"${i18n.close}" class\x3d"esriAnalysisCloseIcon"\x3e\x3c/a\x3e\r\n                    \x3c/div\x3e              \r\n                \x3c/div\x3e  \r\n              \x3c/td\x3e\r\n            \x3c/tr\x3e\r\n         \x3c/table\x3e\r\n    \x3c/div\x3e\r\n    \x3cdiv style\x3d"clear:both; border-bottom: #CCC thin solid; height:1px;width:100%;"\x3e\x3c/div\x3e\r\n  \x3c/div\x3e\r\n  \x3cdiv data-dojo-type\x3d"dijit/form/Form" data-dojo-attach-point\x3d"_form" readOnly\x3d"true"\x3e\r\n     \x3ctable class\x3d"esriFormTable"  data-dojo-attach-point\x3d"_hotspotsTable"\x3e \r\n       \x3ctbody\x3e\r\n        \x3ctr data-dojo-attach-point\x3d"_titleRow"\x3e\r\n          \x3ctd  colspan\x3d"3" class\x3d"sectionHeader" data-dojo-attach-point\x3d"_hotspotsToolDescription" \x3e\x3c/td\x3e\r\n        \x3c/tr\x3e\r\n        \x3ctr data-dojo-attach-point\x3d"_analysisLabelRow" style\x3d"display:none;"\x3e\r\n          \x3ctd colspan\x3d"2" style\x3d"padding-bottom:0;"\x3e\r\n            \x3clabel class\x3d"esriFloatLeading  esriTrailingMargin025 esriAnalysisNumberLabel"\x3e${i18n.oneLabel}\x3c/label\x3e\r\n            \x3clabel class\x3d"esriAnalysisStepsLabel"\x3e${i18n.analysisLayerLabel}\x3c/label\x3e\r\n          \x3c/td\x3e\r\n          \x3ctd class\x3d"shortTextInput" style\x3d"padding-bottom:0;"\x3e\r\n            \x3ca href\x3d"#" class\x3d\'esriFloatTrailing helpIcon\' esriHelpTopic\x3d"analysisLayer"\x3e\x3c/a\x3e\r\n          \x3c/td\x3e\r\n        \x3c/tr\x3e           \r\n        \x3ctr data-dojo-attach-point\x3d"_selectAnalysisRow" style\x3d"display:none;"\x3e\r\n          \x3ctd  colspan\x3d"3" style\x3d"padding-top:0;"\x3e\r\n            \x3cselect class\x3d"esriLeadingMargin1 longInput esriLongLabel"  style\x3d"margin-top:1.0em;" data-dojo-type\x3d"dijit/form/Select" data-dojo-attach-point\x3d"_analysisSelect" data-dojo-attach-event\x3d"onChange:_handleAnalysisLayerChange"\x3e\x3c/select\x3e\r\n          \x3c/td\x3e\r\n        \x3c/tr\x3e          \r\n        \x3ctr\x3e\r\n          \x3ctd colspan\x3d"2" style\x3d"padding-bottom:0;"\x3e\r\n            \x3clabel data-dojo-attach-point\x3d"_labelOne" class\x3d"esriFloatLeading esriTrailingMargin025 esriAnalysisNumberLabel"\x3e${i18n.oneLabel}\x3c/label\x3e\r\n            \x3clabel data-dojo-attach-point\x3d"_polylabel" class\x3d"esriAnalysisStepsLabel"\x3e${i18n.chooseAttributeLabel}\x3c/label\x3e\r\n          \x3c/td\x3e\r\n          \x3ctd class\x3d"shortTextInput" style\x3d"padding-bottom:0;"\x3e\r\n            \x3ca href\x3d"#" class\x3d\'esriFloatTrailing helpIcon\' data-dojo-attach-point\x3d"_analysisFieldHelpLink" esriHelpTopic\x3d"analysisField"\x3e\x3c/a\x3e \r\n          \x3c/td\x3e \r\n        \x3c/tr\x3e\r\n        \x3ctr\x3e\r\n         \x3ctd colspan\x3d"3" style\x3d"padding-top:0;"\x3e \r\n          \x3cselect class\x3d"esriLeadingMargin1 longTextInput"  style\x3d"margin-top:10px;" data-dojo-type\x3d"dijit/form/Select" data-dojo-attach-point\x3d"_analysFieldSelect" data-dojo-attach-event\x3d"onChange:_handleFieldChange"\x3e\r\n          \x3c/select\x3e         \r\n         \x3c/td\x3e \r\n        \x3c/tr\x3e \r\n        \x3ctr data-dojo-attach-point\x3d"_optionsRow"\x3e\r\n          \x3ctd colspan\x3d"3" class\x3d"optionsTd"\x3e\r\n            \x3cdiv class\x3d"esriLeadingMargin2 optionsOpen" style\x3d"border:none;" data-dojo-attach-point\x3d"_optionsDiv"\x3e\r\n              \x3ctable class\x3d"esriFormTable optionsTable" style\x3d"border:  none;"\x3e\r\n                \x3ctbody\x3e\r\n                  \x3ctr\x3e\r\n                    \x3ctd colspan\x3d"2"\x3e\r\n                        \x3clabel\x3e${i18n.provideAggLabel}\x3c/label\x3e\r\n                    \x3c/td\x3e                    \r\n                    \x3ctd class\x3d"shortTextInput"\x3e\r\n                      \x3ca href\x3d"#" class\x3d\'esriFloatTrailing helpIcon\' esriHelpTopic\x3d"aggregationPolygonLayer"\x3e\x3c/a\x3e \r\n                    \x3c/td\x3e \r\n                  \x3c/tr\x3e\r\n                  \x3ctr\x3e\r\n                    \x3ctd colspan\x3d"2" style\x3d"width:68%;"\x3e\r\n                        \x3cselect  class\x3d"longInput esriLongLabel"  data-dojo-type\x3d"dijit/form/Select" data-dojo-attach-point\x3d"_aggAreaSelect" data-dojo-attach-event\x3d"onChange:_handleAggAreaSelectChange"\x3e\x3c/select\x3e\r\n                    \x3c/td\x3e   \r\n                  \x3c/tr\x3e   \r\n                  \x3ctr data-dojo-attach-point\x3d"_boundingAreaLabelRow"\x3e\r\n                    \x3ctd colspan\x3d"2"\x3e\r\n                        \x3clabel\x3e${i18n.defineBoundingLabel}\x3c/label\x3e\r\n                    \x3c/td\x3e\r\n                    \x3ctd class\x3d"shortTextInput"\x3e\r\n                       \x3ca href\x3d"#" class\x3d\'esriFloatTrailing helpIcon\' esriHelpTopic\x3d"boundingPolygonLayer"\x3e\x3c/a\x3e \r\n                    \x3c/td\x3e                     \r\n                  \x3c/tr\x3e\r\n                  \x3ctr data-dojo-attach-point\x3d"_boundingAreaSelectRow"\x3e\r\n                    \x3ctd colspan\x3d"2" style\x3d"width:40%;"\x3e\r\n                      \x3cselect class\x3d"longInput esriLongLabel" data-dojo-type\x3d"dijit/form/Select" data-dojo-attach-point\x3d"_boundingAreaSelect" data-dojo-attach-event\x3d"onChange:_handleBoundingSelectChange"\x3e\x3c/select\x3e                      \r\n                    \x3c/td\x3e\r\n                    \x3ctd style\x3d"width:15%"\x3e\r\n                      \x3cdiv data-dojo-type\x3d"dijit/form/ToggleButton" class\x3d"esriFloatLeading esriActionButton" data-dojo-props\x3d"showLabel:false,iconClass:\'toolbarIcon polygonIcon\',label:\'${i18n.drawLabel}\'" data-dojo-attach-point\x3d"_boundingDrawBtn" data-dojo-attach-event\x3d"onChange:_handleBoundingBtnClick"\x3e\x3c/div\x3e                      \r\n                    \x3c/td\x3e \r\n                  \x3c/tr\x3e      \r\n                \x3c/tbody\x3e\r\n              \x3c/table\x3e\r\n            \x3c/div\x3e\r\n          \x3c/td\x3e\r\n        \x3c/tr\x3e\r\n        \x3ctr\x3e\r\n          \x3ctd colspan\x3d"2" style\x3d"padding-bottom:0;"\x3e\r\n            \x3clabel data-dojo-attach-point\x3d"_labelOne" class\x3d"esriFloatLeading esriTrailingMargin025 esriAnalysisNumberLabel"\x3e${i18n.twoLabel}\x3c/label\x3e\r\n            \x3clabel data-dojo-attach-point\x3d"_polylabel" class\x3d"esriAnalysisStepsLabel"\x3e${i18n.divideByLabel}\x3c/label\x3e\r\n          \x3c/td\x3e\r\n          \x3ctd class\x3d"shortTextInput" style\x3d"padding-bottom:0;"\x3e\r\n            \x3ca href\x3d"#" class\x3d\'esriFloatTrailing helpIcon\' data-dojo-attach-point\x3d"_analysisFieldHelpLink" esriHelpTopic\x3d"dividedByField"\x3e\x3c/a\x3e \r\n          \x3c/td\x3e \r\n        \x3c/tr\x3e         \r\n        \x3ctr\x3e\r\n         \x3ctd colspan\x3d"3" style\x3d"padding-top:0;"\x3e \r\n          \x3cselect class\x3d"esriLeadingMargin1 longTextInput"  style\x3d"margin-top:10px;" data-dojo-type\x3d"dijit/form/Select" data-dojo-attach-point\x3d"_divideFieldSelect" data-dojo-attach-event\x3d"onChange:_handleDividedByFieldChange"\x3e\r\n           \x3c/select\x3e         \r\n         \x3c/td\x3e \r\n        \x3c/tr\x3e  \r\n        \x3ctr\x3e\r\n          \x3ctd colspan\x3d"2"\x3e\r\n            \x3clabel class\x3d"esriFloatLeading esriTrailingMargin025 esriAnalysisNumberLabel"\x3e${i18n.threeLabel}\x3c/label\x3e\r\n            \x3clabel class\x3d"esriAnalysisStepsLabel"\x3e${i18n.outputLayerLabel}\x3c/label\x3e\r\n          \x3c/td\x3e\r\n          \x3ctd class\x3d"shortTextInput"\x3e\r\n            \x3ca href\x3d"#" class\x3d\'esriFloatTrailing helpIcon\' esriHelpTopic\x3d"hotSpotsResultLayer"\x3e\x3c/a\x3e \r\n          \x3c/td\x3e             \r\n        \x3c/tr\x3e\r\n        \x3ctr\x3e\r\n          \x3ctd colspan\x3d"3"\x3e\r\n            \x3cinput type\x3d"text" data-dojo-type\x3d"dijit/form/ValidationTextBox" data-dojo-props\x3d"trim:true,required:true" class\x3d"longTextInput esriLeadingMargin1" data-dojo-attach-point\x3d"_outputLayerInput" value\x3d"${i18n.hotspots}"\x3e\x3c/input\x3e\r\n          \x3c/td\x3e                \r\n        \x3c/tr\x3e\r\n        \x3ctr\x3e\r\n          \x3ctd colspan\x3d"3"\x3e\r\n             \x3cdiv class\x3d"esriLeadingMargin1" data-dojo-attach-point\x3d"_chooseFolderRow"\x3e\r\n               \x3clabel style\x3d"width:9px;font-size:smaller;"\x3e${i18n.saveResultIn}\x3c/label\x3e\r\n               \x3cinput class\x3d"longInput" data-dojo-attach-point\x3d"_webMapFolderSelect" data-dojo-type\x3d"dijit/form/FilteringSelect" trim\x3d"true" style\x3d"width:60%;"\x3e\x3c/input\x3e\r\n             \x3c/div\x3e              \r\n          \x3c/td\x3e\r\n        \x3c/tr\x3e                                              \r\n      \x3c/tbody\x3e         \r\n     \x3c/table\x3e\r\n   \x3c/div\x3e\r\n  \x3cdiv style\x3d"padding:5px;margin-top:5px;border-top:solid 1px #BBB;"\x3e\r\n    \x3cdiv class\x3d"esriExtentCreditsCtr"\x3e\r\n      \x3ca class\x3d"esriFloatTrailing esriSmallFont"  href\x3d"#" data-dojo-attach-point\x3d"_showCreditsLink" data-dojo-attach-event\x3d"onclick:_handleShowCreditsClick"\x3e${i18n.showCredits}\x3c/a\x3e\r\n     \x3clabel data-dojo-attach-point\x3d"_chooseExtentDiv" class\x3d"esriSelectLabel esriExtentLabel"\x3e\r\n       \x3cinput type\x3d"radio" data-dojo-attach-point\x3d"_useExtentCheck" data-dojo-type\x3d"dijit/form/CheckBox" data-dojo-props\x3d"checked:true" name\x3d"extent" value\x3d"true"/\x3e\r\n         ${i18n.useMapExtent}\r\n     \x3c/label\x3e\r\n    \x3c/div\x3e\r\n    \x3cbutton data-dojo-type\x3d"dijit/form/Button" type\x3d"submit" data-dojo-attach-point\x3d"_saveBtn" class\x3d"esriLeadingMargin4 esriAnalysisSubmitButton" data-dojo-attach-event\x3d"onClick:_handleSaveBtnClick"\x3e\r\n        ${i18n.runAnalysis}\r\n    \x3c/button\x3e\r\n  \x3c/div\x3e\r\n  \x3cdiv data-dojo-type\x3d"dijit/Dialog" title\x3d"${i18n.creditTitle}" data-dojo-attach-point\x3d"_usageDialog" style\x3d"width:40em;"\x3e\r\n    \x3cdiv data-dojo-type\x3d"esri/dijit/analysis/CreditEstimator"  data-dojo-attach-point\x3d"_usageForm"\x3e\x3c/div\x3e\r\n  \x3c/div\x3e    \r\n\x3c/div\x3e\r\n'}});
define("esri/dijit/analysis/FindHotSpots","require dojo/_base/declare dojo/_base/lang dojo/_base/array dojo/_base/connect dojo/_base/Color dojo/_base/json dojo/has dojo/json dojo/string dojo/dom-style dojo/dom-attr dojo/dom-construct dojo/query dojo/dom-class dijit/_WidgetBase dijit/_TemplatedMixin dijit/_WidgetsInTemplateMixin dijit/_OnDijitClickMixin dijit/_FocusMixin dijit/registry dijit/form/Button dijit/form/CheckBox dijit/form/Form dijit/form/Select dijit/form/TextBox dijit/form/ToggleButton dijit/form/ValidationTextBox dijit/layout/ContentPane dijit/form/FilteringSelect dijit/Dialog ../../kernel ../../lang ./AnalysisBase ./_AnalysisOptions ../../symbols/SimpleFillSymbol ../../symbols/SimpleLineSymbol ../../toolbars/draw ../PopupTemplate ../../layers/FeatureLayer ../../graphic ./utils ./CreditEstimator dojo/i18n!../../nls/jsapi dojo/text!./templates/FindHotSpots.html".split(" "),
function(p,u,g,f,n,v,h,w,J,m,d,q,K,L,k,x,y,z,A,B,M,N,O,P,Q,R,S,T,U,V,W,C,l,D,E,r,s,t,X,F,G,e,Y,H,I){p=u([x,y,z,A,B,E,D],{declaredClass:"esri.dijit.analysis.FindHotSpots",templateString:I,widgetsInTemplate:!0,analysisLayer:null,analysisField:null,aggregationPolygonLayer:null,boundingPolygonLayer:null,outputLayerName:null,returnProcessInfo:!0,i18n:null,map:null,toolName:"FindHotSpots",helpFileName:"FindHotSpots",resultParameter:"HotSpotsResultLayer",constructor:function(a,b){this._pbConnects=[];a.containerNode&&
(this.container=a.containerNode)},destroy:function(){this.inherited(arguments);f.forEach(this._pbConnects,n.disconnect);delete this._pbConnects},postMixInProperties:function(){this.inherited(arguments);g.mixin(this.i18n,H.findHotSpotsTool);this.set("drawLayerName",this.i18n.blayerName)},postCreate:function(){this.inherited(arguments);k.add(this._form.domNode,"esriSimpleForm");this._outputLayerInput.set("validator",g.hitch(this,this.validateServiceName));this._buildUI()},startup:function(){},_onClose:function(a){a&&
this._featureLayer&&(this.map.removeLayer(this._featureLayer),f.forEach(this.boundingPolygonLayers,function(a,c){a===this._featureLayer&&(this._boundingAreaSelect.removeOption({value:c+1,label:this._featureLayer.name}),this.boundingPolygonLayers.splice(c,1))},this));this._handleBoundingBtnClick(!1);this.emit("close",{save:!a})},clear:function(){this._featureLayer&&(this.map.removeLayer(this._featureLayer),f.forEach(this.boundingPolygonLayers,function(a,b){a===this._featureLayer&&(this._boundingAreaSelect.removeOption({value:b+
1,label:this._featureLayer.name}),this.boundingPolygonLayers.splice(b,1))},this),this._featureLayer=null);this._boundingDrawBtn.reset();this._handleBoundingBtnClick(!1)},_handleShowCreditsClick:function(a){a.preventDefault();a={};var b;this._form.validate()&&(a.analysisLayer=h.toJson(e.constructAnalysisInputLyrObj(this.analysisLayer)),"0"!==this._analysFieldSelect.get("value")&&(a.analysisField=this._analysFieldSelect.get("value")),this._isPoint&&"0"===this._analysFieldSelect.get("value")&&("-1"!==
this._boundingAreaSelect.get("value")&&(b=this.boundingPolygonLayers[this._boundingAreaSelect.get("value")-1],a.boundingPolygonLayer=h.toJson(e.constructAnalysisInputLyrObj(b))),"-1"!==this._aggAreaSelect.get("value")&&(b=this.aggregationPolygonLayers[this._aggAreaSelect.get("value")-1],a.aggregationPolygonLayer=h.toJson(e.constructAnalysisInputLyrObj(b)))),this.returnFeatureCollection||(a.OutputName=h.toJson({serviceProperties:{name:this._outputLayerInput.get("value")}})),this.showChooseExtent&&
!this.get("DisableExtent")&&this._useExtentCheck.get("checked")&&(a.context=h.toJson({extent:this.map.extent._normalize(!0)})),this.getCreditsEstimate(this.toolName,a).then(g.hitch(this,function(a){this._usageForm.set("content",a);this._usageDialog.show()})))},_handleSaveBtnClick:function(a){if(this._form.validate()){this._saveBtn.set("disabled",!0);a={};var b={},c;a.analysisLayer=h.toJson(e.constructAnalysisInputLyrObj(this.analysisLayer));"0"!==this.get("analysisField")&&(a.analysisField=this.get("analysisField"));
this._isPoint&&"0"===this._analysFieldSelect.get("value")&&("-1"!==this._boundingAreaSelect.get("value")&&(c=this.boundingPolygonLayers[this._boundingAreaSelect.get("value")-1],a.boundingPolygonLayer=h.toJson(e.constructAnalysisInputLyrObj(c))),"-1"!==this._aggAreaSelect.get("value")&&(c=this.aggregationPolygonLayers[this._aggAreaSelect.get("value")-1],a.aggregationPolygonLayer=h.toJson(e.constructAnalysisInputLyrObj(c))));"0"!==this.get("dividedByField")&&(a.dividedByField=this.get("dividedByField"));
this.returnFeatureCollection||(a.OutputName=h.toJson({serviceProperties:{name:this._outputLayerInput.get("value")}}));this.showChooseExtent&&!this.get("DisableExtent")&&this._useExtentCheck.get("checked")&&(a.context=h.toJson({extent:this.map.extent._normalize(!0)}));this.returnFeatureCollection&&(c={outSR:this.map.spatialReference},this.showChooseExtent&&this._useExtentCheck.get("checked")&&(c.extent=this.map.extent._normalize(!0)),a.context=h.toJson(c));a.returnProcessInfo=this.returnProcessInfo;
b.jobParams=a;b.itemParams={description:this.i18n.itemDescription,tags:m.substitute(this.i18n.itemTags,{layername:this.analysisLayer.name,fieldname:!a.analysisField?"":a.analysisField}),snippet:this.i18n.itemSnippet};this.showSelectFolder&&(b.itemParams.folder=this.get("folderId"));this.execute(b)}},_save:function(){},_buildUI:function(){var a=!0;this._loadConnections();this.signInPromise.then(g.hitch(this,e.initHelpLinks,this.domNode,this.showHelp,{analysisGpServer:this.analysisGpServer}));this.get("showSelectAnalysisLayer")&&
(!this.get("allowChooseLabel")&&(!this.get("analysisLayer")&&this.get("analysisLayers"))&&this.set("analysisLayer",this.analysisLayers[0]),e.populateAnalysisLayers(this,"analysisLayer","analysisLayers",{chooseLabel:this.get("allowChooseLabel")}));this.outputLayerName&&(this._outputLayerInput.set("value",this.outputLayerName),a=!1);this._boundingAreaSelect.addOption({value:"-1",label:this.i18n.defaultBoundingOption,selected:!0});this.boundingPolygonLayers&&f.forEach(this.boundingPolygonLayers,function(a,
c){"esriGeometryPolygon"===a.geometryType&&this._boundingAreaSelect.addOption({value:c+1,label:a.name,selected:!1})},this);this._aggAreaSelect.addOption({value:"-1",label:this.i18n.defaultAggregationOption,selected:!0});this.aggregationPolygonLayers&&f.forEach(this.aggregationPolygonLayers,function(a,c){"esriGeometryPolygon"===a.geometryType&&this._aggAreaSelect.addOption({value:c+1,label:a.name,selected:!1})},this);e.addReadyToUseLayerOption(this,[this._analysisSelect]);this._updateAnalysisLayerUI(a);
d.set(this._chooseFolderRow,"display",!0===this.showSelectFolder?"block":"none");this.showSelectFolder&&this.getFolderStore().then(g.hitch(this,function(a){this.folderStore=a;e.setupFoldersUI({folderStore:this.folderStore,folderId:this.folderId,folderName:this.folderName,folderSelect:this._webMapFolderSelect,username:this.portalUser?this.portalUser.username:""})}));d.set(this._chooseExtentDiv,"display",!0===this.showChooseExtent?"inline-block":"none");d.set(this._showCreditsLink,"display",!0===this.showCredits?
"block":"none")},_updateAnalysisLayerUI:function(a){if(this.analysisLayer)if("esriGeometryPolygon"===this.analysisLayer.geometryType)this._isPoint=!1,q.set(this._hotspotsToolDescription,"innerHTML",m.substitute(this.i18n.hotspotsPolyDefine,{layername:this.analysisLayer.name})),d.set(this._optionsRow,"display","none");else{if("esriGeometryPoint"===this.analysisLayer.geometryType||"esriGeometryMultipoint"===this.analysisLayer.geometryType)this._isPoint=!0,q.set(this._hotspotsToolDescription,"innerHTML",
m.substitute(this.i18n.hotspotsPointDefine,{layername:this.analysisLayer.name})),k.add(this._analysFieldSelect.domNode,"esriLeadingMargin1"),d.set(this._optionsRow,"display",""),a&&(this.outputLayerName=m.substitute(this.i18n.outputLayerName,{layername:this.analysisLayer.name}))}else d.set(this._optionsRow,"display","none"),d.set(this._optionsRow,"display","none"),a&&(this.outputLayerName=m.substitute(this.i18n.outputLayerName,{layername:""})),this._isPoint=!1;this.set("analysisFields",this.analysisLayer);
this._isPoint?this._aggAreaSelect.set("value","-1"):this.set("dividedByFields",this.analysisLayer);this.analysisLayer&&("esriGeometryPolygon"===this.analysisLayer.geometryType&&a)&&(this.outputLayerName=m.substitute(this.i18n.outputLayerName,{layername:l.isDefined(this._analysFieldSelect.getOptions(0))?this._analysFieldSelect.getOptions(0).label:""}));this._outputLayerInput.set("value",this.outputLayerName)},_handleAnalysisLayerChange:function(a){"browse"===a?(this._analysisquery||(this._analysisquery=
this._browsedlg.browseItems.get("query")),this._browsedlg.browseItems.set("query",this._analysisquery+' AND (tags:"point" OR tags:"polygon") '),this._selectedwidget=0,this._browsedlg.show()):(-1!==a?(this.get("allowChooseLabel")&&(a-=1),this.analysisLayer=this.analysisLayers[a]):this.analysisLayer=null,this._updateAnalysisLayerUI(!0))},_handleFieldChange:function(a){"0"===this._analysFieldSelect.get("value")?(this._outputLayerInput.set("value",m.substitute(this.i18n.outputLayerName,{layername:this.analysisLayer.name})),
this._isPoint&&(d.set(this._optionsRow,"display",""),k.remove(this._optionsDiv,"disabled"),k.remove(this._optionsDiv,"optionsClose"),k.add(this._optionsDiv,"optionsOpen"))):(this._outputLayerInput.set("value",m.substitute(this.i18n.outputLayerName,{layername:this._analysFieldSelect.getOptions(a).label})),this._isPoint&&(k.add(this._optionsDiv,"disabled"),d.set(this._optionsRow,"display","none"),this._boundingAreaSelect.set("value","-1"),this.clear(),k.contains(this._optionsDiv,"optionsOpen")&&(k.remove(this._optionsDiv,
"optionsOpen"),k.add(this._optionsDiv,"optionsClose"))));this.set("analysisField",this._analysFieldSelect.get("value"));a="-1"!==this._aggAreaSelect.get("value");var b=null;"0"!==this._analysFieldSelect.get("value")?b=this.analysisLayer:a&&this._isPoint&&this._aggAreaSelect.set("value","-1");this.set("dividedByFields",b)},_handleDividedByFieldChange:function(a){},_handleBoundingSelectChange:function(a){"browse"===a?(this._analysisquery||(this._analysisquery=this._browsedlg.browseItems.get("query")),
this._browsedlg.browseItems.set("query",this._analysisquery+' AND tags:"polygon"'),this._selectedwidget=2,this._browsedlg.show()):"-1"!==a?(a=this.boundingPolygonLayers[this._boundingAreaSelect.get("value")-1],a.id!==this.drawLayerName&&this.clear()):this.clear();this._boundingDrawBtn.reset()},_handleAggAreaSelectChange:function(a){"browse"===a?(this._analysisquery||(this._analysisquery=this._browsedlg.browseItems.get("query")),this._browsedlg.browseItems.set("query",this._analysisquery+' AND tags:"polygon"'),
this._selectedwidget=1,this._browsedlg.show()):(a="-1"!==this._aggAreaSelect.get("value"),this._boundingAreaSelect.set("disabled",a),k.toggle(this._boundingAreaSelect.domNode,"esriAnalysisTextDisabled",a),this._boundingDrawBtn.set("disabled",a),a?(this.clear(),d.set(this._boundingAreaLabelRow,"display","none"),d.set(this._boundingAreaSelectRow,"display","none"),this._boundingAreaSelect.set("value","-1")):(d.set(this._boundingAreaLabelRow,"display",""),d.set(this._boundingAreaSelectRow,"display","")),
k.toggle(this._boundingDrawBtn.domNode,"esriAnalysisTextDisabled",a),this.set("dividedByFields",a?this.aggregationPolygonLayers[this._aggAreaSelect.get("value")-1]:null))},_handleBoundingBtnClick:function(a){a?(this.emit("drawtool-activate",{}),this._featureLayer||this._createBoundingPolyFeatColl(),this._toolbar.activate(t.POLYGON)):(this._toolbar.deactivate(),this.emit("drawtool-deactivate",{}))},_handleBrowseItemsSelect:function(a){var b={};a&&a.selection&&l.isDefined(this._selectedwidget)&&(0===
this._selectedwidget?(b.layers=this.analysisLayers,b.layersSelect=this._analysisSelect):1===this._selectedwidget?(b.layers=this.aggregationPolygonLayers,b.layersSelect=this._aggAreaSelect,b.posIncrement=1):2===this._selectedwidget&&(b.layers=this.boundingPolygonLayers,b.layersSelect=this._boundingAreaSelect),b.item=a.selection,b.browseDialog=this._browsedlg,b.widget=this,e.addAnalysisReadyLayer(b).always(g.hitch(this,this._updateAnalysisLayerUI,!0)))},_loadConnections:function(){this.on("start",g.hitch(this,
"_onClose",!1));this._connect(this._closeBtn,"onclick",g.hitch(this,"_onClose",!0))},_createBoundingPolyFeatColl:function(){var a=e.createPolygonFeatureCollection(this.drawLayerName);this._featureLayer=new F(a,{id:this.drawLayerName});this.map.addLayer(this._featureLayer);n.connect(this._featureLayer,"onClick",g.hitch(this,function(a){this.map.infoWindow.setFeatures([a.graphic])}))},_addFeatures:function(a){var b=[],c={},d=new r(r.STYLE_NULL,new s(s.STYLE_SOLID,new v([0,0,0]),4));a=new G(a,d);this.map.graphics.add(a);
c.description="blayer desc";c.title="blayer";a.setAttributes(c);b.push(a);this._featureLayer.applyEdits(b,null,null);if(0===this.boundingPolygonLayers.length||this.boundingPolygonLayers[this.boundingPolygonLayers.length-1]!==this._featureLayer)b=this.boundingPolygonLayers.push(this._featureLayer),c=this._boundingAreaSelect.getOptions(),this._boundingAreaSelect.removeOption(c),c=f.map(c,function(a){a.selected=!1;return a}),c.push({value:b,label:this._featureLayer.name,selected:!0}),this._boundingAreaSelect.addOption(c),
this._handleBoundingSelectChange(b)},_setAnalysisGpServerAttr:function(a){a&&(this.analysisGpServer=a,this.set("toolServiceUrl",this.analysisGpServer+"/"+this.toolName))},_setAnalysisLayerAttr:function(a){this.analysisLayer=a},_getAnalysisLayerAttr:function(a){return this.analysisLayer},_getAnalysisFieldAttr:function(){this._analysFieldSelect&&(this.analysisField=this._analysFieldSelect.get("value"));return this.analysisField},_setAnalysisFieldAttr:function(a){this.analysisField=a},_setDividedByFieldAttr:function(a){this.dividedByField=
a},_getDividedByFieldAttr:function(){this._divideFieldSelect&&(this.dividedByField=this._divideFieldSelect.get("value"));return this.dividedByField},_setAnalysisFieldsAttr:function(a){var b=l.isDefined(a)&&l.isDefined(a.fields)?a.fields:[],c,d;this._analysFieldSelect&&(this._analysFieldSelect.removeOption(this._analysFieldSelect.getOptions()),this._isPoint?this._analysFieldSelect.addOption({value:"0",label:this.i18n.pointCounts}):!this._isPoint&&this.get("allowChooseLabel")&&this._analysFieldSelect.addOption({value:"0",
label:this.i18n.chooseLabel}),f.forEach(b,function(b,e){-1===f.indexOf(["GiZScore","GiPValue","Gi_Bin",a.objectIdField],b.name)&&-1!==f.indexOf(["esriFieldTypeSmallInteger","esriFieldTypeInteger","esriFieldTypeSingle","esriFieldTypeDouble"],b.type)&&(c={value:b.name,label:l.isDefined(b.alias)&&""!==b.alias?b.alias:b.name},this.analysisField&&c.label===this.analysisField&&(c.selected="selected",d=b.name),this._analysFieldSelect.addOption(c))},this),d?this._analysFieldSelect.set("value",d):this.set("analysisField",
this._analysFieldSelect.get("value")))},_setAnalysisLayersAttr:function(a){this.analysisLayers=a},_setDividedByFieldsAttr:function(a){var b=l.isDefined(a)&&l.isDefined(a.fields)?a.fields:[],c,d;this._divideFieldSelect&&(this._divideFieldSelect.removeOption(this._divideFieldSelect.getOptions()),this._divideFieldSelect.addOption({value:"0",label:this.i18n.noneLabel}),(!this._isPoint||this._isPoint&&(!l.isDefined(this.analysisField)||"0"===this.analysisField))&&this._divideFieldSelect.addOption({value:"esriPopulation",
label:this.i18n.enrichLabel,disabled:!this.get("enableEnrichmentFields")}),f.forEach(b,function(b,e){-1===f.indexOf(["GiZScore","GiPValue","Gi_Bin",a.objectIdField,l.isDefined(this.analysisField)&&this.analysisField],b.name)&&-1!==f.indexOf(["esriFieldTypeSmallInteger","esriFieldTypeInteger","esriFieldTypeSingle","esriFieldTypeDouble"],b.type)&&(c={value:b.name,label:l.isDefined(b.alias)&&""!==b.alias?b.alias:b.name},this.dividedByField&&c.label===this.dividedByField&&(c.selected="selected",d=b.name),
this._divideFieldSelect.addOption(c))},this),d&&this._divideFieldSelect.set("value",d))},_setEnableEnrichmentFieldsAttr:function(a){this.enableEnrichmentFields=a},_getEnableEnrichmentFieldsAttr:function(){return this.enableEnrichmentFields},_setMapAttr:function(a){this.map=a;this._toolbar=new t(this.map);n.connect(this._toolbar,"onDrawEnd",g.hitch(this,this._addFeatures))},_getMapAttr:function(){return this.map},_setDrawLayerNameAttr:function(a){this.drawLayerName=a},_getDrawLayerNameAttr:function(){return this._featureLayer.name},
_getDrawLayerAttr:function(){return this._featureLayer},_getDrawToolbarAttr:function(){return this._toolbar},_setDisableRunAnalysisAttr:function(a){this._saveBtn.set("disabled",a)},validateServiceName:function(a){return e.validateServiceName(a,{textInput:this._outputLayerInput})},_setDisableExtentAttr:function(a){this._useExtentCheck.set("checked",!a);this._useExtentCheck.set("disabled",a)},_getDisableExtentAttr:function(){this._useExtentCheck.get("disabled")},_connect:function(a,b,c){this._pbConnects.push(n.connect(a,
b,c))}});w("extend-esri")&&g.setObject("dijit.analysis.FindHotSpots",p,C);return p});