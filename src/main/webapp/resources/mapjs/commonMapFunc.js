/**
 * Created by peng_an on 2016/7/9.
 */

/**
 * 本js主要存放常量json
 */

/**
 * 参数定义,主要包括json格式的参数定义
 */
//省市地图窝点（Graphics）渲染的参数(点)

var provinceInfoTempDef = {
    title:"${NAME}",
    content:"窝点数量: &nbsp;${NUM2}<br>"+
            "嫌疑人数量:&nbsp;${NUM1}<br>"+
            "<a style='float:right;font-size: 18px' id='provinceDetial' onclick='showProvinceDetail()'>详情</a>"
};

var crimeInfoTempDef = {
    title:"${NAME}",
    content:"窝点类型: &nbsp;${CRIMEGROUPTYPE}<br>"+
            "地点:&nbsp;${LOCATION}<br>"+
            "状态:&nbsp;${STATUS}<br>"+
            "所在省:&nbsp;${PROVINCE}<br>"+
            "市:&nbsp;${CITY}<br>"+
            "县:&nbsp;${TOWN}"
};

var cityInfoTempDef = {
    title:"${NAME_1}",
    content:"市：${CITY}"
};

var townInfoTempDef = {
    title:"${NAME_1_1}",
    content:"市：${CITY}<br>区(县)：${TOWN}"
};

var layerDefinition = {
    "objectIdField": "OBJECTID",
    "geometryType": "esriGeometryPoint",
    "fields": [{
        "name": "OBJECTID",
        "type": "esriFieldTypeOID",
        "alias": "OBJECTID",
        "sqlType": "sqlTypeOther"
    },{
        "name": "LNG",
        "type": "esriFieldTypeInteger",
        "alias": "LNG"
    },{
        "name": "LAT",
        "type": "esriFieldTypeInteger",
        "alias": "LAT"
    },{
        "name": "LOCATION",
        "type": "esriFieldTypeString",
        "alias": "LOCATION"
    },{
        "name": "NAME",
        "type": "esriFieldTypeString",
        "alias": "NAME"
    },{
        "name": "CRIMEGROUPTYPE",
        "type": "esriFieldTypeString",
        "alias": "CRIMEGROUPTYPE"
    },{
        "name": "CLUE",
        "type": "esriFieldTypeString",
        "alias": "CLUE"
    },{
        "name": "NUM",
        "type": "esriFieldTypeInteger",
        "alias": "NUM"
    },{
        "name": "PROVINCE",
        "type": "esriFieldTypeString",
        "alias": "PROVINCE"
    },{
        "name": "CITY",
        "type": "esriFieldTypeString",
        "alias": "CITY"
    },{
        "name": "TOWN",
        "type": "esriFieldTypeString",
        "alias": "TOWN"
    }]
};

//窝点图层定义
var featureCollection = {
    layerDefinition: layerDefinition,
    featureSet: null
};

//省市地图的内容及显示方式的定义
var provinceLabelDef = {
    "labelExpressionInfo": {"value": "{NAME}:{NUM2}个窝点，{NUM1}个嫌疑人"},
    "useCodedValues": true,
    "symbol":{
        color:[0,0,255,255],
        font:{
            size:12,
            family:"黑体",
        },
        xoffset: 50,
        yoffset: 20
    },
    "labelPlacement":"always-horizontal"
};

var crimeLabelDef = {
    "labelExpressionInfo": {"value": "{CITY}:{TOWN}"},
    "useCodedValues": true,
    "symbol":{
        color:[0,0,255,255],
        font:{
            size:12,
            family:"黑体"
        },
        xoffset: -25,
        yoffset: -30
    },
    "labelPlacement":"always-horizontal"
};

var cityLabelDef = {
    "labelExpressionInfo": {"value": "{CITY}"},
    "useCodedValues": true,
    "symbol": {
        color:[160, 161, 164,255],
        font:{
            size:10,
            family:"黑体"
        },
        xoffset: 0,
        yoffset: 0
    },
    "labelPlacement":"above-right"
};

var townLabelDef = {
    "labelExpressionInfo": {"value": "{TOWN}"},
    "useCodedValues": true,
    "symbol":{
        color:[160, 161, 164,255],
        font:{
            size:7,
            family:"黑体"
        },
        xoffset: 0,
        yoffset: 0
    },
    //"minScale": 1,
    //"maxScale": 10,
    "labelPlacement":"always-horizontal"
};

/**
 * 函数定义，主要是通用的函数定义
 *
 */

//countryData数据格式:上海,江苏省,重庆省。转换为:NAME in ('上海','江苏省','重庆省')
function createDefinition(countryData){
    var definition = "NAME in ('";
    if(countryData.length<1){
        definition = "NAME in (null)";
        //console.log(definition);
        return definition;
    }
    definition = definition+countryData[0].PROVINCE+"'";
    for(var i=1;i<countryData.length;i++){
        definition += ",'"+countryData[i].PROVINCE+"'"
    }
    definition+=")";
    //console.log(definition);
    return definition;
}



/**
 * 为窝点添加详情概况，位于省市地图的右侧
 * @param CrimeDataInPro
 */
function addCrimeGroupDiv(CrimeDataInPro){
    $('.crimeGroups').empty();
    $.each(CrimeDataInPro, function(key,kind) {
        var n=key+1;
        $(".crimeGroups").append("<div class='crimeGroupInfo'></div>");
        var dom = $("div.crimeGroups > div.crimeGroupInfo:eq("+key+")");
        dom.append("<div class='imgLocationDiv'><div>");
        dom.find(".imgLocationDiv").append("<img class='crimeGroupNumber' src='/AMD/resources/img/"+kind.PIC+".png'/><img>");
        dom.append("<div class='crimeGroup'></div>");
        dom.find(".crimeGroup").append("<div class='clue'>"+kind.NAME+"</div>");
        dom.find(".crimeGroup").append("<div class='crimeGroupType'>"+kind.CRIMEGROUPTYPE+"</div>");
    });
}

/**
 * 获取选中的窝点类型:生产制造窝点,烟机制造窝点,仓储窝点
 */
function getChosenCrimeKind() {
    var crimeKinds = $("div#crimeGroupCheckBox input");
    var kinds = Array();
    for(var i= 0,j=0;i<crimeKinds.length;i++){
        if(crimeKinds[i].checked == true){
            kinds[j]="'"+crimeKinds[i].parentNode.innerText+"'";
            j++;
        }
    }

    var crimeStatus = $("div#crimeGroupStatusCheckBox input");
    var status = Array();
    for(var i= 0,j=0;i<crimeStatus.length;i++){
        if(crimeStatus[i].checked == true){
            status[j]="'"+crimeStatus[i].parentNode.innerText+"'";
            j++;
        }
    }

    return [kinds.join(","),status.join(",")];
}

/**
 * 动态更改classbreaksrender
 */
function changeRenderByGroup(countryData){
    var max = 0;
    for(var i=0;i<countryData.length;i++){
        if(max<parseInt(countryData[i].GROUPNUM)){
            max = parseInt(countryData[i].GROUPNUM);
        }
    }
    return getRenderClass(max);
}

function changeRenderByPeople(countryData){
    var max = 0;
    for(var i=0;i<countryData.length;i++){
        if(max<parseInt(countryData[i].PEOPLENUM)){
            max = parseInt(countryData[i].PEOPLENUM);
        }
    }
    return getRenderClass(max);
}

function getRenderClass(max){
    if(max<5){
        console.log(max);
        return [[1,2],[3,4]];

    }
    var max2 = Math.ceil(max/3)*3;
    var cen2 = Math.ceil(max/3)*2;
    var min2 = Math.ceil(max/3);
    var max1 = cen2+1;
    var cen1 = min2+1;
    var min1=1;
    return [[min1,min2],[cen1,cen2],[max1,max2]];
}
