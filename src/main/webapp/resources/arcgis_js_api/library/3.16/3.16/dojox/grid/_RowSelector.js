//>>built
define("dojox/grid/_RowSelector",["dojo/_base/declare","./_View"],function(d,e){return d("dojox.grid._RowSelector",e,{defaultWidth:"2em",noscroll:!0,padBorderWidth:2,buildRendering:function(){this.inherited("buildRendering",arguments);this.scrollboxNode.style.overflow="hidden";this.headerNode.style.visibility="hidden"},getWidth:function(){return this.viewWidth||this.defaultWidth},buildRowContent:function(a,b){b.innerHTML='\x3ctable class\x3d"dojoxGridRowbarTable" style\x3d"width:'+(this.contentWidth||
0)+'px;height:1px;" border\x3d"0" cellspacing\x3d"0" cellpadding\x3d"0" role\x3d"presentation"\x3e\x3ctr\x3e\x3ctd class\x3d"dojoxGridRowbarInner"\x3e\x26nbsp;\x3c/td\x3e\x3c/tr\x3e\x3c/table\x3e'},renderHeader:function(){},updateRow:function(){},resize:function(){this.adaptHeight()},adaptWidth:function(){!("contentWidth"in this)&&(this.contentNode&&0<this.contentNode.offsetWidth)&&(this.contentWidth=this.contentNode.offsetWidth-this.padBorderWidth)},doStyleRowNode:function(a,b){var c=["dojoxGridRowbar dojoxGridNonNormalizedCell"];
this.grid.rows.isOver(a)&&c.push("dojoxGridRowbarOver");this.grid.selection.isSelected(a)&&c.push("dojoxGridRowbarSelected");b.className=c.join(" ")},domouseover:function(a){this.grid.onMouseOverRow(a)},domouseout:function(a){if(!this.isIntraRowEvent(a))this.grid.onMouseOutRow(a)}})});