/**
 * Created by peng_an on 2016/6/30.
 */
$("#mapCountry").height($(window).height()-$(".navbar").outerHeight(true));
$(".modal.fade").height($(window).height()-$(".navbar").height()-10);
$(".modal.fade").css("top", $(".navbar").outerHeight(false));

//这里不需要加"/"
document.write("<script language='javascript' src='resources/mapjs/commonMapFunc.js'></script>");

require([
    "dojo/_base/connect",
    "dojo/dom",
    "dojo/on",
    "esri/request",
    "esri/graphic",
    "esri/InfoTemplate",
    "esri/map",
    "esri/SpatialReference",
    "esri/dijit/Legend",
    "esri/geometry/Point",
    "esri/layers/ArcGISTiledMapServiceLayer",
    "esri/layers/FeatureLayer",
    "esri/layers/LabelClass",
    "esri/renderers/ClassBreaksRenderer",
    "esri/renderers/SimpleRenderer",
    "esri/Color",
    "esri/symbols/Font",
    "esri/symbols/PictureMarkerSymbol",
    "esri/symbols/SimpleFillSymbol",
    "esri/symbols/SimpleMarkerSymbol",
    "esri/symbols/SimpleLineSymbol",
    "esri/symbols/TextSymbol",
    "dojo/domReady!"
], function(connect,dom, on,esriRequest,Graphic, InfoTemplate, Map,SpatialReference,Legend,Point,ArcGISTiledMapServiceLayer,
            FeatureLayer, LabelClass,ClassBreaksRenderer, SimpleRenderer,Color,Font,PictureMarkerSymbol,
            SimpleFillSymbol, SimpleMarkerSymbol,SimpleLineSymbol,TextSymbol) {


    var countryData=null;

    var map = new Map("mapCountry", {
        //basemap:"topo",
        center: [105, 35],
        zoom: 5,
        minZoom: 4,
        maxZoom: 7,
        showLabels : true,
        logo: false
    });
    map.infoWindow.resize(200,100);
    var TiledMap = new ArcGISTiledMapServiceLayer("http://10.15.69.25:6080/arcgis/rest/services/ChinaProvinceTiles8/MapServer");
    map.addLayer(TiledMap);

    //FeatureLayer的render即Template

    var low = {
        minValue:1,
        maxValue:3,
        symbol:new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID).setOutline(new SimpleLineSymbol().setWidth(0.1).setColor(new Color([128,128,128]))).setColor(new Color([56, 168, 0, 0.5])),
        label: "低(1-3)"
    };

    var mid = {
        minValue:3,
        maxValue:6,
        symbol:new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID).setOutline(new SimpleLineSymbol().setWidth(0.1).setColor(new Color([128,128,128]))).setColor(new Color([255, 165, 0, 0.5])),
        label: "中(4-6)"
    };
    var high={
        minValue:6,
        maxValue:9,
        symbol:new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID).setOutline(new SimpleLineSymbol().setWidth(0.1).setColor(new Color([128,128,128]))).setColor(new Color([255, 0, 0, 0.5])),
        label: "高(7-9)"
    }


     var provinceRenderer = new ClassBreaksRenderer(null, "NUM3");
    // provinceRenderer.addBreak({
    //     minValue:1,
    //     maxValue:3,
    //     symbol:new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID).setOutline(new SimpleLineSymbol().setWidth(0.1).setColor(new Color([128,128,128]))).setColor(new Color([56, 168, 0, 0.5])),
    //     label: "低(1-3)"
    // });
    // provinceRenderer.addBreak({
    //     minValue:3,
    //     maxValue:6,
    //     symbol:new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID).setOutline(new SimpleLineSymbol().setWidth(0.1).setColor(new Color([128,128,128]))).setColor(new Color([0, 0, 255, 0.5])),
    //     label: "中(4-6)"
    // });
    // provinceRenderer.addBreak({
    //     minValue:6,
    //     maxValue:9,
    //     symbol:new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID).setOutline(new SimpleLineSymbol().setWidth(0.1).setColor(new Color([128,128,128]))).setColor(new Color([255, 0, 0, 0.5])),
    //     label: "高(7-9)"
    // });




    var provinceInfoTemplate = new InfoTemplate(provinceInfoTempDef);

    //为省市单独添加地图，显示详细信息
    var mapProvince = new Map("mapProvince", {
        center: [105, 35],
        zoom: 7,
        minZoom: 4,
        maxZoom: 10,
        showLabels: true,
        logo: false
    });
    var provinceTiledMap = new ArcGISTiledMapServiceLayer("http://10.15.69.25:6080/arcgis/rest/services/ChinaProvinceTiles8/MapServer");
    mapProvince.addLayer(provinceTiledMap);
    //provinceTiledMap.hide();
    var crimeinfoTemplate = new InfoTemplate(crimeInfoTempDef);

    /**
     * 进入详情后的省一级，该省的窝点，市一级，县一级的地图
     */
    var provinceLayer = null;
    var crimeGroupLayer = null;
    var cityLayer = null;
    var townLayer = null;

    //每次请求重新加载FeatureLayer，故每次需要重新生成FeatureLayer
    var provinceUrl ="http://10.15.69.25:6080/arcgis/rest/services/ChinaProvinceCityTown/MapServer/2";
    var cityUrl = "http://10.15.69.25:6080/arcgis/rest/services/ChinaProvinceCityTown/MapServer/1";
    var townUrl = "http://10.15.69.25:6080/arcgis/rest/services/ChinaProvinceCityTown/MapServer/0";

    //大地图中的省一级的FeatureLayer
    var chinaProLayer = null;
    //大地图中的省一级的legend
    var provinceLegend = null;

    map.on("load", function() {
        var crimeInfo = getChosenCrimeKind();
        var layersRequest = esriRequest({
            url: "crimeMap/getChosenCrimeData?kinds="+crimeInfo[0]+"&status="+crimeInfo[1],
            handleAs: "json",
            callbackParamName: "callback"
        });
        layersRequest.then(
            function(data) {
                countryData=data;
                createProvinceRenderLayer(countryData);
                //为窝点种类加载事件
                addListenerForKindsAndrender();
                addListenerForSearchBtn();
            }, function(error) {
                console.log("Error: ", error.message);
            });
    });

    on(dom.byId("showMapLabel"), "click", function(){
        if(dom.byId("showMapLabel").checked == true){
            chinaProLayer.setShowLabels(true);
        }else{
            chinaProLayer.setShowLabels(false);
        }
        chinaProLayer.show();
    });


    function createLayer(url){
        var layer = new FeatureLayer(url,{
            //注意这里的mode一定要指定MODE_SNAPSHOT，否则每次放大或缩小或者是平移都会执行FeatureLayer的update-end操作
            mode: FeatureLayer.MODE_SNAPSHOT,
            outFields: ["*"]
        });
        return layer;

    }

    /**
     * 创建通过窝点渲染过的省一级的地图，同时需要改变参数
     *
     */
    function createProvinceRenderLayer(countryData){

        chinaProLayer = createLayer(provinceUrl);

        var definition = createDefinition(countryData);
        chinaProLayer.setDefinitionExpression(definition);

        map.addLayer(chinaProLayer);

        //加载地图完毕后需要立刻改变参数，并渲染地图(render)
        //updateAttrForLayer();
        chinaProLayer.on("update-end", function(){
            for(var i = 0;i<chinaProLayer.graphics.length;i++){
                //自定义数据，在每个面要素中添加相关属性,NUM3表示-人员数量，NUM4表示-窝点数量
                chinaProLayer.graphics[i].attributes["NUM1"]=0;
                chinaProLayer.graphics[i].attributes["NUM2"]=0;
                chinaProLayer.graphics[i].attributes["NUM3"]=0;
                chinaProLayer.graphics[i].attributes["NUM4"]=0;
                for(var j=0;j<countryData.length;j++){
                    if(chinaProLayer.graphics[i].attributes["NAME"] == countryData[j].PROVINCE){
                        chinaProLayer.graphics[i].attributes["NUM1"]=parseInt(countryData[j].PEOPLENUM);
                        chinaProLayer.graphics[i].attributes["NUM2"]=parseInt(countryData[j].GROUPNUM);
                        chinaProLayer.graphics[i].attributes["NUM3"]=parseInt(countryData[j].PEOPLENUM);
                        chinaProLayer.graphics[i].attributes["NUM4"]=parseInt(countryData[j].GROUPNUM);
                        break;
                    }
                }
            }

            if(provinceLegend !=null){
                provinceLegend.refresh();
            }else{
                provinceLegend = new Legend({
                    map: map,
                    layerInfos: [{
                        layer: chinaProLayer,
                        title: "人员数量(人)"
                    }]
                }, "legendDiv");
            }

            if($("#crimeRender").is(':checked')){

                var renderData = changeRenderByGroup(countryData);
                changeRender(renderData);
                provinceRenderer.attributeField = "NUM4";
                provinceLegend.layerInfos[0].title = "窝点个数(个)";
            }else{
                var renderData = changeRenderByPeople(countryData);
                changeRender(renderData);
                provinceRenderer.attributeField = "NUM3";
                provinceLegend.layerInfos[0].title = "人员数量(人)";
            }

            chinaProLayer.setRenderer(provinceRenderer);
            chinaProLayer.setInfoTemplate(provinceInfoTemplate);

            var lc = new LabelClass(provinceLabelDef);
            chinaProLayer.setLabelingInfo([ lc ]);

            if(dom.byId("showMapLabel").checked == true){
                chinaProLayer.setShowLabels(true);
            }else{
                chinaProLayer.setShowLabels(false);
            }
            chinaProLayer.redraw();

            if(provinceLegend !=null){
                provinceLegend.refresh();
            }else{
                provinceLegend.startup();
            }
        });
    }

    /**
     * 为查询按钮添加查询事件
     */
    function addListenerForSearchBtn(){
        on(dom.byId("btnSearch"),"click",function () {
            var crimeInfo = getChosenCrimeKind();
            var layersRequest = esriRequest({
                url: "crimeMap/getChosenCrimeData?kinds="+crimeInfo[0]+"&status="+crimeInfo[1],
                handleAs: "json",
                callbackParamName: "callback"
            });
            layersRequest.then(
                function(data) {
                    map.removeLayer(chinaProLayer);
                    countryData=data;
                    console.log(data);
                    createProvinceRenderLayer(countryData);
                }, function(error) {
                    console.log("Error: ", error.message);
                });
        })
    }

    /**
     * 改变render的值
     */
    function changeRender(renderData){
        console.log(renderData);

        provinceRenderer.clearBreaks();

        low.minValue=renderData[0][0];
        low.maxValue=renderData[0][1];
        low.label="低("+renderData[0][0]+"-"+renderData[0][1]+")";
        provinceRenderer.addBreak(low);
        mid.minValue=renderData[1][0];
        mid.maxValue=renderData[1][1];
        mid.label="中("+renderData[1][0]+"-"+renderData[1][1]+")";
        provinceRenderer.addBreak(mid);


        if(renderData[2]!=null){
            high.minValue=renderData[2][0];
            high.maxValue=renderData[2][1];
            high.label="高("+renderData[2][0]+"-"+renderData[2][1]+")";
            provinceRenderer.addBreak(high);
        }
    }

    /**
     * 为窝点种类的复选框添加事件
     */
    function addListenerForKindsAndrender(){
        // on(dojo.query(".kindCrimeGroup,.crimeGroupStatus"), "click", function(){
        //     var crimeInfo = getChosenCrimeKind();
        //     var layersRequest = esriRequest({
        //         url: "crimeMap/getChosenCrimeData?kinds="+crimeInfo[0]+"&status="+crimeInfo[1],
        //         handleAs: "json",
        //         callbackParamName: "callback"
        //     });
        //     layersRequest.then(
        //         function(data) {
        //             map.removeLayer(chinaProLayer);
        //             countryData=data;
        //             console.log(data);
        //             createProvinceRenderLayer(countryData);
        //         }, function(error) {
        //             console.log("Error: ", error.message);
        //         });
        // });

        on(dojo.query(".renderKind"), "click", function(){
            if($("#crimeRender").is(':checked')){
                var renderData = changeRenderByGroup(countryData);
                changeRender(renderData);
                provinceRenderer.attributeField = "NUM4";
                provinceLegend.layerInfos[0].title = "窝点个数(个)";
            }else{
                var renderData = changeRenderByPeople(countryData);
                changeRender(renderData);
                provinceRenderer.attributeField = "NUM3";
                provinceLegend.layerInfos[0].title = "人员数量(人)";
            }

            chinaProLayer.setRenderer(provinceRenderer);
            chinaProLayer.setInfoTemplate(provinceInfoTemplate);
            chinaProLayer.redraw();

            provinceLegend.refresh();
        });
    }

    /**
     * 为省市的“详情”标签添加事件
     */
    showProvinceDetail = function() {
        dom.byId("cityLevel").checked = false;
        dom.byId("townLevel").checked = false;

        var CrimeDataInPro = null;
        //点击详情后，弹出各省市的详细信息，所有js代码都将在在该函数内实现
        if(provinceLayer!=null){
            mapProvince.removeLayer(provinceLayer);
            provinceLayer.clear();
        }
        if(cityLayer!=null){
            mapProvince.removeLayer(cityLayer);
            cityLayer.clear();
        }
        if(townLayer!=null){
            mapProvince.removeLayer(townLayer);
            townLayer.clear();
        }
        $('#myModal').modal();

        var crimeInfo = getChosenCrimeKind();

        var provinceName =map.infoWindow.features[map.infoWindow.selectedIndex].attributes.NAME;
        console.log(provinceName);

        var layersRequest = esriRequest({
            url: "crimeMap/getProvinceDetail?name=" + provinceName + "&kinds=" + crimeInfo[0]+"&status="+crimeInfo[1],
            handleAs: "json",
            callbackParamName: "callback"
        });
        layersRequest.then(
            function (data) {
                $("#myModalLabel").text(provinceName);
                CrimeDataInPro = data;
                addCrimeProvinceLayer(provinceName);
                addCrimeDataPointLayer(CrimeDataInPro);
            }, function (error) {
                console.log("Error: ", error.message);
            }
        );

    }

    /**
     * 进入省一级后，首先添加省一级的图层（单个polygon）
     * @param provinceName
     */
    function addCrimeProvinceLayer(provinceName){
        provinceLayer = createLayer(provinceUrl);
        cityLayer = createLayer(cityUrl);
        townLayer = createLayer(townUrl);

        var definition = "NAME in ('" + provinceName + "')";
        provinceLayer.setDefinitionExpression(definition);
        mapProvince.addLayer(provinceLayer);
        provinceLayer.setShowLabels(false);

        var definition = "NAME_1 in('" + provinceName + "')";
        cityLayer.setDefinitionExpression(definition);

        cityLayer.setInfoTemplate(new InfoTemplate(cityInfoTempDef));
        cityLayer.hide();
        mapProvince.addLayer(cityLayer);

        var definition = "NAME_1_1 in('" + provinceName + "')";
        townLayer.setDefinitionExpression(definition);
        townLayer.setInfoTemplate(new InfoTemplate(townInfoTempDef));
        townLayer.hide();
        mapProvince.addLayer(townLayer);

        var lc = new LabelClass(cityLabelDef);
        cityLayer.setLabelingInfo([ lc ]);

        var lc = new LabelClass(townLabelDef);
        townLayer.setLabelingInfo([ lc ]);

        on(dom.byId("cityLevel"), "click", function(){
            if(dom.byId("cityLevel").checked == true){
                cityLayer.show();
            }else{
                cityLayer.hide();
            }
        });

        on(dom.byId("townLevel"), "click", function(){
            if(dom.byId("townLevel").checked == true){
                townLayer.show();
                cityLayer.setShowLabels(false);
            }else{
                cityLayer.setShowLabels(true);
                townLayer.hide();
            }
        });

        //provinceLayer只有最多一个graphic
        provinceLayer.on("update-end", function () {
            var x= provinceLayer.graphics[0].attributes.X;
            var y= provinceLayer.graphics[0].attributes.Y;
            mapProvince.centerAndZoom(new Point(x, y, new SpatialReference({ wkid: 4326 })),7);

            //provinceLayer.graphics[0].attributes["NUM1"] = 0;
            provinceLayer.graphics[0].attributes["NUM3"] = 0;
            provinceLayer.graphics[0].attributes["NUM4"] = 0;

            for (var i = 0; i < countryData.length; i++) {
                if (countryData[i].PROVINCE == provinceName) {
                    provinceLayer.graphics[0].attributes["NUM3"] = parseInt(countryData[i].PEOPLENUM);
                    provinceLayer.graphics[0].attributes["NUM4"] = parseInt(countryData[i].GROUPNUM);
                    break;
                }
            }
            provinceLayer.setRenderer(provinceRenderer);
            provinceLayer.redraw();
        });
    }

    /**
     * 加载省一级中的窝点
     * @param CrimeDataInPro
     */
    function addCrimeDataPointLayer(CrimeDataInPro){
        console.log(CrimeDataInPro);
        if(crimeGroupLayer!=null){
            mapProvince.removeLayer(crimeGroupLayer);
        }
        var graphic = new Array();
        var red=1,green=1,yellow=1,gray = 1;
        for(var i=0;i<CrimeDataInPro.length;i++){
            var point = new Point(CrimeDataInPro[i].LNG, CrimeDataInPro[i].LAT, new SpatialReference({ wkid: 4326 }));
            var crimeGroupPicMaker = null;
            if(CrimeDataInPro[i].STATUS == '未核实'){
                CrimeDataInPro[i].PIC = "red"+red++;
            }else if(CrimeDataInPro[i].STATUS == '已核实'){
                CrimeDataInPro[i].PIC = "green"+green++;
            }else if(CrimeDataInPro[i].STATUS == '已抓获'){
                CrimeDataInPro[i].PIC = "yellow"+yellow++;
            }else if(CrimeDataInPro[i].STATUS == '已落地'){
                CrimeDataInPro[i].PIC = "gray"+gray++;
            }
            crimeGroupPicMaker = new PictureMarkerSymbol('/AMD/resources/img/'+CrimeDataInPro[i].PIC+'.png', 20, 30);
            graphic[i] = new Graphic(point,crimeGroupPicMaker,CrimeDataInPro[i],crimeinfoTemplate);
        }
        crimeGroupLayer = new FeatureLayer(featureCollection);
        mapProvince.addLayer(crimeGroupLayer);

        for(var i=0;i<graphic.length;i++){
            crimeGroupLayer.applyEdits([graphic[i]]);
        }

        var lc = new LabelClass(crimeLabelDef);
        crimeGroupLayer.setLabelingInfo([ lc ]);
        crimeGroupLayer.setShowLabels(true);
        //加载div
        addCrimeGroupDiv(CrimeDataInPro);
    }
});