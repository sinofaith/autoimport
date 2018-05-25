//>>built
define("dojox/charting/widget/Legend","dojo/_base/declare dijit/_WidgetBase dojox/gfx dojo/_base/array dojo/has dojo/has!dojo-bidi?../bidi/widget/Legend dojox/lang/functional dojo/dom dojo/dom-construct dojo/dom-class dijit/registry".split(" "),function(m,g,q,f,h,r,k,s,d,l,n){g=m(h("dojo-bidi")?"dojox.charting.widget.NonBidiLegend":"dojox.charting.widget.Legend",g,{chartRef:"",horizontal:!0,swatchSize:18,legendBody:null,postCreate:function(){!this.chart&&this.chartRef&&((this.chart=n.byId(this.chartRef)||
n.byNode(s.byId(this.chartRef)))||console.log("Could not find chart instance with id: "+this.chartRef));this.chart=this.chart.chart||this.chart;this.refresh()},buildRendering:function(){this.domNode=d.create("table",{role:"group","aria-label":"chart legend","class":"dojoxLegendNode"});this.legendBody=d.create("tbody",null,this.domNode);this.inherited(arguments)},destroy:function(){this._surfaces&&f.forEach(this._surfaces,function(c){c.destroy()});this.inherited(arguments)},refresh:function(){this._surfaces&&
f.forEach(this._surfaces,function(a){a.destroy()});for(this._surfaces=[];this.legendBody.lastChild;)d.destroy(this.legendBody.lastChild);this.horizontal&&(l.add(this.domNode,"dojoxLegendHorizontal"),this._tr=d.create("tr",null,this.legendBody),this._inrow=0);var c=this.series||this.chart.series;if(0!=c.length)if("dojox.charting.plot2d.Pie"==c[0].chart.stack[0].declaredClass){var b=c[0].chart.stack[0];"number"==typeof b.run.data[0]?(c=k.map(b.run.data,"Math.max(x, 0)"),c=k.map(c,"/this",k.foldl(c,
"+",0)),f.forEach(c,function(a,c){this._addLabel(b.dyn[c],b._getLabel(100*a)+"%")},this)):f.forEach(b.run.data,function(a,c){this._addLabel(b.dyn[c],a.legend||a.text||a.y)},this)}else f.forEach(c,function(a){this._addLabel(a.dyn,a.legend||a.name)},this)},_addLabel:function(c,b){var a=d.create("td"),p=d.create("div",null,a),e=d.create("label",null,a),f=d.create("div",{style:{width:this.swatchSize+"px",height:this.swatchSize+"px","float":"left"}},p);l.add(p,"dojoxLegendIcon dijitInline");l.add(e,"dojoxLegendText");
this._tr?(this._tr.appendChild(a),++this._inrow===this.horizontal&&(this._tr=d.create("tr",null,this.legendBody),this._inrow=0)):d.create("tr",null,this.legendBody).appendChild(a);this._makeIcon(f,c);e.innerHTML=String(b);h("dojo-bidi")&&(e.dir=this.getTextDir(b,e.dir))},_makeIcon:function(c,b){var a=this.swatchSize,d=this.swatchSize,e=q.createSurface(c,d,a);this._surfaces.push(e);if(b.fill)e.createRect({x:2,y:2,width:d-4,height:a-4}).setFill(b.fill).setStroke(b.stroke);else if(b.stroke||b.marker){var f=
{x1:0,y1:a/2,x2:d,y2:a/2};b.stroke&&e.createLine(f).setStroke(b.stroke);b.marker&&e.createPath({path:"M"+d/2+" "+a/2+" "+b.marker}).setFill(b.markerFill).setStroke(b.markerStroke)}else e.createRect({x:2,y:2,width:d-4,height:a-4}).setStroke("black"),e.createLine({x1:2,y1:2,x2:d-2,y2:a-2}).setStroke("black"),e.createLine({x1:2,y1:a-2,x2:d-2,y2:2}).setStroke("black")}});return h("dojo-bidi")?m("dojox.charting.widget.Legend",[g,r]):g});