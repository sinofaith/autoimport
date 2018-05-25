// All material copyright ESRI, All Rights Reserved, unless otherwise specified.
// See http://js.arcgis.com/3.16/esri/copyright.txt for details.
//>>built
define("esri/tasks/datareviewer/ReviewerLifecycle",["dojo/_base/declare","dojo/has","dojo/_base/lang","../../kernel"],function(c,g,h,k){c={declaredClass:"esri.tasks.datareviewer.ReviewerLifecycle",UNKNOWN:0,REVIEWED:1,RESOLVED:2,MARK_AS_EXCPETION:3,ACCEPTABLE:4,UNRESOLVED_EXCEPTION:5,UNACCEPTABLE:6,UNRESOLVED_UNACCEPTABLE:7,UNRESOLVED_ACCEPTABLE:8,EXCEPTION:9,NEW:10,PASSED:11,FAILED:12,REVIEW:2,CORRECTION:4,VERIFICATION:6,LIFECYCLESTATUS_DESCRIPTIONS:{"0":"Unknown",1:"Reviewed",2:"Resolved",3:"Mark As Exception",
4:"Acceptable",6:"Unacceptable",9:"Exception",10:"New",11:"Passed",12:"Failed"},LIFECYCLEPHASE_DESCRIPTIONS:{2:"Review",4:"Correction",6:"Verification"},getLifecycleInfo:function(b){if(null===b||0===b.length)return null;b.sort();for(var a=[],a=this._getNextStatus(b[0]),c=1;c<b.length;c++){for(var e=this._getNextStatus(b[c]),f=[],d=0;d<e.length;d++)-1!=a.indexOf(e[d])&&f.push(e[d]);if(0<f.length)a=f;else{a.length=0;break}}b=null;0<a.length&&(b={nextLifecycleStatus:a,nextLifecyclePhase:this._getNextLifecyclePhase(a[0])});
return b},toLifecycleStatusString:function(b){return isNaN(b)||!1===this.LIFECYCLESTATUS_DESCRIPTIONS.hasOwnProperty(b)?null:this.LIFECYCLESTATUS_DESCRIPTIONS[b]},toLifecyclePhaseString:function(b){return isNaN(b)||!1===this.LIFECYCLEPHASE_DESCRIPTIONS.hasOwnProperty(b)?null:this.LIFECYCLEPHASE_DESCRIPTIONS[b]},getCurrentLifecyclePhase:function(b){if(isNaN(b)||!1===this.LIFECYCLESTATUS_DESCRIPTIONS.hasOwnProperty(b))return null;var a=-1;switch(b){case this.UNKNOWN:case this.REVIEWED:case this.UNACCEPTABLE:case this.UNRESOLVED_UNACCEPTABLE:case this.NEW:a=
this.REVIEW;break;case this.RESOLVED:case this.MARK_AS_EXCPETION:a=this.CORRECTION;break;case this.ACCEPTABLE:case this.EXCEPTION:case this.UNRESOLVED_EXCEPTION:case this.UNRESOLVED_ACCEPTABLE:case this.PASSED:case this.FAILED:a=this.VERIFICATION}return this.LIFECYCLEPHASE_DESCRIPTIONS[a]},_getNextStatus:function(b){var a=[];switch(b){case this.UNKNOWN:a.push(this.RESOLVED);a.push(this.MARK_AS_EXCPETION);a.push(this.PASSED);a.push(this.FAILED);break;case this.REVIEWED:a.push(this.RESOLVED);a.push(this.MARK_AS_EXCPETION);
break;case this.NEW:a.push(this.PASSED);a.push(this.FAILED);break;case this.UNACCEPTABLE:case this.UNRESOLVED_UNACCEPTABLE:a.push(this.RESOLVED);a.push(this.MARK_AS_EXCPETION);break;case this.RESOLVED:a.push(this.ACCEPTABLE);a.push(this.UNACCEPTABLE);break;case this.MARK_AS_EXCPETION:case this.UNRESOLVED_EXCEPTION:case this.UNRESOLVED_ACCEPTABLE:a.push(this.EXCEPTION);a.push(this.UNACCEPTABLE);break;case this.ACCEPTABLE:a.push(this.UNACCEPTABLE);break;case this.EXCEPTION:a.push(this.UNACCEPTABLE);
break;case this.PASSED:a.push(this.FAILED);break;case this.FAILED:a.push(this.PASSED)}return a},_getNextLifecyclePhase:function(b){var a=-1;switch(b){case this.UNKNOWN:case this.REVIEWED:case this.UNACCEPTABLE:case this.UNRESOLVED_UNACCEPTABLE:a=this.CORRECTION;break;case this.NEW:case this.RESOLVED:case this.MARK_AS_EXCPETION:case this.PASSED:case this.FAILED:a=this.VERIFICATION;break;case this.ACCEPTABLE:case this.EXCEPTION:case this.UNRESOLVED_EXCEPTION:case this.UNRESOLVED_ACCEPTABLE:a=this.REVIEW}return a}};
g("extend-esri")&&h.setObject("tasks.datareviewer.ReviewerLifecycle",c,k);return c});