/**
 * Created by zhengjiabin on 15/1/18.
 */
function addphone2table(_phonenum){
    $.getJSON("system/addphone2table",{phone:_phonenum},function(data){
    });
}

function delphone2table(_phonenum){
    $.getJSON("system/delphone2table",{phone:_phonenum},function(data){
    });
}

function delalltable(){
    $.getJSON("system/delalltable",{},function(data){
    });
}
var global_nodes;

$(function() {
    init();

    // 分析按钮响应
    $('#btnAnalyse').click(function() {

        var numberRecords = $('#phoneTable .realnumber');
        var startTimePicker = $('#startTimePicker');
        var endTimePicker = $('#endTimePicker');
        if(numberRecords.length === 0 || (startTimePicker.val() === '' && endTimePicker.val() === '')) {

            var phoneFlag = false;
            if (numberRecords.length === 0) {
                phoneFlag = true;
            }

            var timeFlag = false;
            if (startTimePicker.val() === '' && endTimePicker.val() === '') {
                timeFlag = true;
            }

            if (phoneFlag && !timeFlag) {
                alertify.alert("请输入查询的目标号码");
            } else if (!phoneFlag && timeFlag) {
                alertify.alert("请输入查询的时间段");
            }  else if (phoneFlag && timeFlag) {
                alertify.alert("请输入目标号码与相关时间段");
            }

        } else {
            var option = {};

            var phones = '';
            option.numberList = [];
            var phoneDomList = $('#phoneTable .realnumber>span');
            for(var i = 0; i < phoneDomList.length; i ++) {
                var phone = $(phoneDomList[i]).text();
                option.numberList.push(phone);

                phones = phones + phone + ';';
            }

            option.minTime = startTimePicker.val();
            option.maxTIme = endTimePicker.val();

//            option.s_province = $('#s_province').val();
//            option.s_city = $('#s_city').val();
//            option.s_county = $('#s_county').val();
            option.s_province = '';
            option.s_city = '';
            option.s_county = '';

            var description = $("#mytabs input[type='radio']:checked").parent().find('span').attr('description');
            option.analysePattern = translateAnalysePattern(description);
            option.vizPattern = $('#vizPatternSelector').val();
            option.showPattern = $('#showPatternDropdown>li>.selected').text();
            option.keyWord = '';
//            option.keyWord = $('#keywordInput').val().trim();
            var valMinImLimitation = $('#minImLimitation').val().trim();
            var valMaxImLimitation = $('#maxImLimitation').val().trim();

            if (option.analysePattern === '即时消息分析') {

                valMinImLimitation = valMinImLimitation == ''? '0' : valMinImLimitation;
                valMaxImLimitation = valMaxImLimitation == ''? '0' : valMaxImLimitation;
                var flag = isDigital(valMinImLimitation) && isDigital(valMaxImLimitation)
                    && (parseInt(valMaxImLimitation) >= parseInt(valMinImLimitation));

                if (!flag) {
                    alertify.alert('聊天频率范围的输入格式有误');
                    return;
                }

            }

            option.minImLimitation = valMinImLimitation == '' ? 0 : parseInt(valMinImLimitation);
            option.maxImLimitation = valMaxImLimitation == '' ? 0 : parseInt(valMaxImLimitation);

            var valCallDurationFactor = $('#callDurationFactor').val().trim();
            var valCallCountFactor = $('#callCountFactor').val().trim();
            var valSmsCountFactor = $('#smsCountFactor').val().trim();
            var valCallRateFactor = $('#callRateFactor').val().trim();
            var valSmsRateFactor = $('#smsRateFactor').val().trim();

            var callDurationFlag = isDigital(valCallDurationFactor) || isFloat(valCallDurationFactor);
            var callCountFlag = isDigital(valCallCountFactor) || isFloat(valCallCountFactor);
            var smsCountFlag = isDigital(valSmsCountFactor) || isFloat(valSmsCountFactor);
            var callRateFlag = isDigital(valCallRateFactor) || isFloat(valCallRateFactor);
            var smsRateFlag = isDigital(valSmsRateFactor) || isFloat(valSmsRateFactor);

            if (!callDurationFlag || !callCountFlag || !smsCountFlag || !callRateFlag || !smsRateFlag) {
                alertify.alert('请配置合法的联系因子权值');
                return ;
            }

            var callDurationFactor = parseFloat(valCallDurationFactor);
            var callCountFactor = parseFloat(valCallCountFactor);
            var smsCountFactor = parseFloat(valSmsCountFactor);
            var callRateFactor = parseFloat(valCallRateFactor);
            var smsRateFactor = parseFloat(valSmsRateFactor);

            if (callDurationFactor <= 0 || callCountFactor <= 0 || smsCountFactor <= 0
                || callRateFactor <= 0 || smsRateFactor <= 0) {
                alertify.alert('联系因子权值必须大于零');
                return;
            }

            option.callDurationFactor = callDurationFactor;
            option.callCountFactor = callCountFactor;
            option.smsCountFactor = smsCountFactor;
            option.callRateFactor = callRateFactor;
            option.smsRateFactor = smsRateFactor;

            if ((option.analysePattern === '通信分析') || option.analysePattern === '即时消息分析' || option.analysePattern === '区域分析') {
                $("html").mask("正在拼命创建可视化分析图中，请稍候...");
                $.ajax({
                    url: '/AMD/conjugate/option',
                    type: 'POST',
                    data: JSON.stringify(option),
                    dataType: 'json',
                    contentType: 'application/json;charset=UTF-8',
                    success: function(result) {
                        var svgDom = $('#mygraph>svg');
                        if (svgDom.length !== 0) {
                            svgDom.remove();
                        }

                        if (option.analysePattern === '通信分析') {

                            var graphData = {};
                            graphData.nodes = result.nodes;
                            graphData.links = result.links;
                            global_nodes = result.nodes;//
                            graphData.polyLinks = result.polyLinks;
                            graphData.circleLinks = result.circleLinks;
                            window.__VIZ_JSON_DATA = graphData;
                            window.__TABLE_DATA = result.tableData;

                            if (option.showPattern == '图形模式') {
                                if (option.vizPattern === '直线关系') {
                                    createCommunicateD3Viz(graphData, true);
                                    window.__VIZ_TYPE_NAME = 'COMMUNICATE_NORMAL';
                                } else {
                                    createCommunicateD3Viz(graphData, false);
                                    window.__VIZ_TYPE_NAME = 'COMMUNICATE_POLY';
                                }
                                window.__TABLE_CREATED = false;
                            } else {
                                createTable();
                            }


                        } else if (option.analysePattern === '即时消息分析'){
                            createIMD3Viz(result);
                            window.__VIZ_TYPE_NAME = 'IM';

                            window.__TABLE_DATA = undefined;
                            window.__TABLE_CREATED = undefined;
                        } else {
                            createZoneD3Viz(result);
                            window.__VIZ_TYPE_NAME = 'ZONE';

                            window.__TABLE_DATA = undefined;
                            window.__TABLE_CREATED = undefined;
                        }

                        $('#minTimeViz').val(option.minTime);
                        $('#maxTimeViz').val(option.maxTIme);

                        window.__VIZ_TRANSFORM = {
                            transX: 0,
                            transY: 0,
                            scale: 1
                        };

                        $("html").unmask();
                    },
                    error: function() {
                        $("html").unmask();
                        alertify.alert('错误！请检查网络环境！');
                    }
                });
            }
        }
    });
});

function init() {
    $('#startTimePicker').val('2010/10/01 00:00');
    $('#startTimePicker').datetimepicker({
        lang:'ch'
    });
    $('#endTimePicker').datetimepicker({
        lang:'ch'
    });

    initEvent();
}

function initEvent() {

    $("#mytabs input[type='radio']").change(function() {
        var description = $(this).parent().find('span').attr('description');
        switch(description) {
            case 'COMMUNICATE':
                $('.IMController').hide();
                $('.communicateController').show();
                if ($('#showPatternDropdown .selected').text().trim() == '列表模式') {
                    $('#vizPatternSelectorTd').hide();
                    $('.zoomTd').hide();
                    $('.detailTd').hide();
                    $('.filterTd').hide();
                    $('.tablePatternTd').show();
                    $('#tableDiv').show();
                    $('.legendTd').hide();

                } else {
                    $('.zoomTd').show();
                    $('.detailTd').show();
                    $('.filterTd').show();
                    $('.tablePatternTd').hide();
                    $('#tableDiv').hide();
                    $('legendTd').show();
                }
                break;
            case 'IM':
                $('.IMController').show();
                $('.communicateController').hide();
                $('.zoomTd').show();
                $('.filterTd').show();
                $('.tablePatternTd').hide();
                $('#tableDiv').hide();
                $('.legendTd').show();
                break;
            case 'LOCATION':
                $('.IMController').hide();
                $('.communicateController').hide();
                $('.zoomTd').show();
                $('.filterTd').hide();
                $('.tablePatternTd').hide();
                $('#tableDiv').hide();
                $('.legendTd').hide();
                break;
        }
    });

    // ===========    插入号码    ===========
    $('#phoneEntry').bind('keypress', function(event) {
        if(event.keyCode == '13') {
            $('#btnAddPhone').click();
        }
    });
    // 添加手机号
    $('#btnAddPhone').click(function() {
        var phone = $('#phoneEntry').val().trim();
        insertPhoneData(phone);
        addphone2table(phone);
    });
    // ===========    插入号码    ===========

    // ===============   缩放   ===============
    function zoomIn(){
        if ($('#mygraph>svg').length != 0  && window.__VIZ_TRANSFORM != undefined) {
            var graph = $('#mygraph>svg>g');

            var transX = window.__VIZ_TRANSFORM.transX - 30;
            var transY = window.__VIZ_TRANSFORM.transY - 30;
            var scale = window.__VIZ_TRANSFORM.scale / 0.9;
            graph.attr('transform', 'translate('+transX+','+transY+') scale('+scale+')');
            window.__VIZ_TRANSFORM.transX = transX;
            window.__VIZ_TRANSFORM.transY = transY;
            window.__VIZ_TRANSFORM.scale = scale;
        }
    }
    function zoomOut(){
        if ($('#mygraph>svg').length != 0 && window.__VIZ_TRANSFORM != undefined) {
            var graph = $('#mygraph>svg>g');

            var transX = window.__VIZ_TRANSFORM.transX + 30;
            var transY = window.__VIZ_TRANSFORM.transY + 30;
            var scale = window.__VIZ_TRANSFORM.scale * 0.9;
            graph.attr('transform', 'translate('+transX+','+transY+') scale('+scale+')');
            window.__VIZ_TRANSFORM.transX = transX;
            window.__VIZ_TRANSFORM.transY = transY;
            window.__VIZ_TRANSFORM.scale = scale;
        }
    }
    $('#mygraph').mousewheel(function(e, delta){
        e = e || window.event;
        if (e.stopPropagation) e.stopPropagation();
        else e.cancelBubble = true;
        if (e.preventDefault) e.preventDefault();
        else e.returnValue = false;
        if(delta>0){
            zoomIn();
        }else{
            zoomOut();
        }
    });

    $('#btnZoomIn').click(function() {
        zoomIn();
    });

    $('#btnZoomOut').click(function() {
       zoomOut();
    });
    // ===============   缩放   ===============

    $('#vizPatternSelector').change(function() {
        var mode = $(this).val();
        if (window.__VIZ_JSON_DATA != undefined && $('#mygraph>svg').length != 0) {
            $('#mygraph>svg').remove();

            if (mode === '直线关系') {
                createCommunicateD3Viz(window.__VIZ_JSON_DATA, true);
                window.__VIZ_TYPE_NAME = 'COMMUNICATE_NORMAL';
            } else if (mode === '关联关系') {
                createCommunicateD3Viz(window.__VIZ_JSON_DATA, false);
                window.__VIZ_TYPE_NAME ='COMMUNICATE_POLY';
            }

            window.__VIZ_TRANSFORM = {
                transX: 0,
                transY: 0,
                scale: 1
            };
        }
    });

    $('#showPatternDropdown>li>a').click(function() {

        var showPattern = $(this).text();
        switch (showPattern) {
            case '图形模式':
                $('.vizPatternTd').show();
                $('.tablePatternTd').hide();
                $('.detailTd').show();
                $('.filterTd').show();
                break;
            case '列表模式':
                $('.vizPatternTd').hide();
                $('.tablePatternTd').show();
                $('.detailTd').hide();
                $('.filterTd').hide();
                break;
        }

        if(!$(this).hasClass('selected')) {
            $('#showPatternDropdown>li>.selected').removeClass('selected');
            $(this).addClass('selected');

            if(showPattern === '图形模式') {
                $('#tableDiv').hide();
                var svg = $('#mygraph>svg');
                if (svg.length != 0) {
                    svg.show();
                } else {

                    var description = $("#mytabs input[type='radio']:checked").parent().find('span').attr('description');
                    var pattern = translateAnalysePattern(description);
                    if (pattern === '通信分析' && window.__VIZ_JSON_DATA !== undefined) {
                        var isNormalType = $('#vizPatternSelector').val() == '直线关系';
                        createCommunicateD3Viz(window.__VIZ_JSON_DATA, isNormalType);
                        window.__VIZ_TYPE_NAME = isNormalType? 'COMMUNICATE_NORMAL' : 'COMMUNICATE_POLY';
                    }
                }
            } else {
                if (!window.__TABLE_CREATED) {
                    createTable();
                }

                $('#tableDiv').show();

                if ($('#myTable>tbody').length === 0) {
                    $('#myTable').hide();
                }

                if ($('#mygraph>svg').length !== 0) {
                    $('#mygraph>svg').hide();
                }
            }
        }
    });

    $('#btnDetails').click(function() {

        $.ajax({
            url: '/AMD/conjugate/phoneInfo',
            type: 'GET',
            data: 'phoneNum=' + selected.name,
            dataType: 'json',
            success: function (infoObj) {

                DetailsTool.removeContent();

                if (infoObj == null) {
                    DetailsTool.createInfo('['+selected.name+']尚未被采集.');
                    DetailsTool.show();
                } else {
                    // 数据设置
                    DetailsTool.setType('phone');
                    DetailsTool.setSourceNumber(selected.name);
                    DetailsTool.setImage('/AMD/resources/img/suspectMan.jpg');
                    DetailsTool.setName(infoObj.oname);
                    DetailsTool.setMobile(infoObj.mobile);
                    DetailsTool.setBirthPlace(infoObj.birthPlace);
                    DetailsTool.setObjectId(infoObj.oid);
                    DetailsTool.setCaseId(infoObj.caseId);
                    DetailsTool.setCertificateType(infoObj.certificateType);
                    DetailsTool.setCertificateId(infoObj.certificateId);
                    DetailsTool.setCollectId(infoObj.collectId);
                    DetailsTool.setPoliceCode(infoObj.policeCode);
                    DetailsTool.setAreaCode(infoObj.areaCode);
                    DetailsTool.setAddress(infoObj.addres);
                    DetailsTool.setTakeTime(infoObj.takeTime);
                    DetailsTool.setPoliceName(infoObj.policeName);
                    DetailsTool.setPoliceId(infoObj.policeId);
                    DetailsTool.setIp(infoObj.ip);
                    DetailsTool.setDeviceNum(infoObj.deviceNum);
                    DetailsTool.setUploadTime(infoObj.uploadTime);

                    // 详情创建与展示
                    DetailsTool.createDetail();
                    DetailsTool.show();
                }
            },
            error: function () {
                alertify.alert('错误！请检查网络环境！');
            }
        });




    });

    $('#btnLoadFile').click(function() {
        $("#uploadFileForm>input[type='file']").click();
    });

    $("#uploadFileForm>input[type='file']").change(function(event) {
        $("html").mask("正在拼命上传文件中，请稍候....");
        $('#uploadFileForm').ajaxSubmit({
            dataType: 'json',
            success: function(result) {
                if (result == null) {
                    alertify.alert('上传文件出现问题！请检查网络环境是否正常或文件格式及内容是否符合标准！');
                } else if(result.length === 0) {
                    alertify.alert('文件内容为空.');
                } else {
                    for(var i = 0; i < result.length; i ++) {
                        insertPhoneData(result[i]);
                    }
                }
                $("html").unmask();
            },
            error: function() {
                alertify.alert('上传文件出现问题！请检查网格环境是否正常或文件格式及内容是否符合标准！');
                $("html").unmask();
            }
        });

        $(this).val('');
    });

    $('#btnDownload').click(function() {
        if (('#mygraph>svg').length != 0 && window.__VIZ_JSON_DATA != undefined) {
            $("html").mask("正在拼命下载记录中，请稍候...");

            var _data = $('#vizPatternSelector').val() === '直线关系'?
                window.__VIZ_JSON_DATA.links : window.__VIZ_JSON_DATA.polyLinks;
            var data = {};
            data.links = [];
            for (var i = 0; i < _data.length; i ++) {
                data.links.push(_data[i].sourceNum + ';' + _data[i].targetNum);
            }

            var minTime = $('#minTimeViz').val();
            var maxTime = $('#maxTimeViz').val();
            minTime = minTime === ''? 'null':minTime;
            maxTime = maxTime === ''? 'null':maxTime;
            data.links.push(minTime + ';' + maxTime);

            $.ajax({
                url: '/AMD/conjugate/logFilePath',
                type: 'POST',
                data: JSON.stringify(data),
                dataType: 'text',
                contentType: 'application/json;charset=UTF-8',
                success: function(result) {
                    $('#downloadForm>input').val(result);
                    $('#downloadForm').submit();
                    $("html").unmask();
                },
                error: function(error) {
                    alertify.alert('错误！请检查网络环境！');
                    $("html").unmask();
                }
            });

        } else {
            alertify.alert('请先创建可视化分析图');
        }
    });

    $('#inputFilter').bind('keypress', function(event) {
        if(event.keyCode == '13') {
            $('#btnFilter').click();
        }
    });

    $('#btnResume').click(function() {
        var hitList = document.getElementsByClassName('filterHit');
        while (hitList.length > 0) {
            var className = hitList[0].getAttribute('class');
            className = className.replace('filterHit', '').trim();
            hitList[0].setAttribute('class', className);
        }
    });

    $('#btnPicDownload').click(function() {
        var svg = $('#mygraph>svg');
        if (svg.length == 1) {

            var origin_g = svg.find('>g');
            var transform = origin_g.attr('transform');
            origin_g.removeAttr('transform');
            var box = svg[0].getBBox();
            origin_g.attr('transform', transform);

            var picSvg = document.createElement('svg');
            picSvg.setAttribute('version','1.1');
            picSvg.setAttribute('xmlns', 'http://www.w3.org/2000/svg');
            picSvg.setAttribute('xmlns:xlink','http://www.w3.org/1999/xlink');
            picSvg.setAttribute('width', (box.width+Math.abs(box.x)+50).toString());
            picSvg.setAttribute('height', (box.height+Math.abs(box.y)+50).toString());
            picSvg.setAttribute('xml:space', 'preserve');

            picSvg.innerHTML = "<defs><style type='text/css'>" +
                "<![CDATA["
                + ".node {font-size: 10px;} "
                + ".node>circle {cursor: pointer;} "
                + ".node>rect {cursor: pointer;} "
                + ".node>polygon {cursor: pointer;} "
                + ".recorded {stroke: #00AAEF;stroke-width: 2px;} "
                + "circle:hover {stroke: #BCBAB8;stroke-width: 2px;} "
                + "rect:hover {stroke: #BCBAB8;stroke-width: 2px;} "
                + "polygon:hover {stroke: #BCBAB8;stroke-width: 2px;} "
                + ".link {font-size: 10px;} "
                + ".background {fill: none;pointer-events: all;} "
                + "#IMgraph .node {stroke: #fff;stroke-width: 1.5px;} "
                + "#IMgraph .link {stroke: #999;stroke-opacity: .6;stroke-width: 1.5px;} "
                + ".ImNode {cursor: pointer;stroke: #000;stroke-width: .5px;} "
                + ".ImLink {fill: none;stroke: #9ecae1;stroke-width: 1.5px;cursor: pointer;} "
                + ".zoneCircle {fill: rgb(31, 119, 180);fill-opacity: .25;stroke: rgb(31, 119, 180);stroke-width: 1px;} "
                + ".leaf .zoneCircle {fill: #ff7f0e;fill-opacity: 1;} "
                + ".linkShowTitle {cursor: pointer;} "
                + ".communicateNormalLine {stroke-width: 1px;stroke: #666666;} "
                + ".communicatePolyLine {stroke-width: 2px;stroke: #0066FF;stroke-opacity: 0.6;} "
                + ".IMLine {stroke-width: 1px;stroke: #666666;} "
                + ".filterHit {stroke: red;} "
                + "]]></style></defs>";

            var g = svg.find('>g')[0];
            var cloneG = document.createElement('g');
            cloneG.innerHTML = g.innerHTML;
            cloneG.setAttribute('transform', 'translate('+(-box.x+25).toString()+','+(-box.y+25).toString()+')');
            picSvg.appendChild(cloneG);
            var content = "<?xml version='1.0' encoding='utf-8'?>"
                + "<!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.1//EN' 'http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd'>"
                + picSvg.outerHTML;

            content = content.replace(new RegExp(/<img/g), '<image')
                            .replace(new RegExp(/imageflag=\"true\">/g), '></image>')
                            .replace(new RegExp(/\/AMD\/resources/g), window.location.origin + '/AMD/resources');
            $('#vizDownloadForm>input').val(content);
            $('#vizDownloadForm').submit();

            //$.ajax({
            //    url: '/AMD/conjugate/downloadSvg',
            //    type: 'POST',
            //    data: 'content=' + content,
            //    dataType: 'text',
            //    error: function() {
            //        alertify.alert('下载失败');
            //    }
            //});
        } else {
            console.log('none');
        }
    });

    $('#btnFilter').click(function() {

        if (window.__VIZ_TYPE_NAME == 'COMMUNICATE_NORMAL' || window.__VIZ_TYPE_NAME == 'COMMUNICATE_POLY'
            || window.__VIZ_TYPE_NAME == 'IM') {

            var lineList = [];
            var lineDomList = document.getElementsByTagName('line');
            for (var i = 0; i < lineDomList.length; i ++) {
                var item = lineDomList[i];
                if (window.__VIZ_TYPE_NAME == 'IM') {

                    var rawSourceNum = item.getAttribute('sourceNum');
                    var rawTargetNum = item.getAttribute('targetNum');
                    if (rawSourceNum != '' && rawTargetNum != '') {
                        var imNumber = rawSourceNum + ';' + rawTargetNum;
                        lineList.push(imNumber);
                    }

                } else {
                    var number = item.getAttribute('sourceNum') + ';' + item.getAttribute('targetNum');
                    lineList.push(number);
                }
            }
            var data = {
                type: window.__VIZ_TYPE_NAME == 'IM'? 'IM' : 'SMS',
                keyword: $('#inputFilter').val().trim(),
                lineList: lineList
            };

            $("html").mask('搜索中，请稍候...');
            $.ajax({
                url: '/AMD/conjugate/filter',
                type: 'POST',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: 'application/json;charset=UTF-8',
                success: function(resultList) {

                    $('#btnResume').click();
                    for (var i = 0; i < lineDomList.length; i ++) {

                        var domItem = lineDomList[i];
                        var sourceNumAttr = domItem.getAttribute('sourceNum');
                        var targetNumAttr = domItem.getAttribute('targetNum');
                        var found = false;
                        for (var j = 0; j < resultList.length; j ++) {
                            var resultItem = resultList[j];
                            if ((sourceNumAttr == resultItem.first && targetNumAttr == resultItem.second)
                                || sourceNumAttr == resultItem.second && targetNumAttr == resultItem.first) {
                                found = true;
                                break;
                            }
                        }

                        if (found) {
                            var className = domItem.getAttribute('class');
                            className = className === null? 'filterHit' : className + ' filterHit';
                            domItem.setAttribute('class', className);
                        }
                    }
                    $("html").unmask();
                },
                error: function() {
                    alertify.alert('搜索出现意外问题.');
                    $("html").unmask();
                }
            });


        }
    });
}

function createCircleToken(num) {
    return "<span class='circleTokenP'><text class='count'>"+num+"</text></span>";
}

function createPhoneShow(phone) {
    return "<span type='text'>"+phone+"</span>";
}

function removeSinglePhone(source) {

    var parent = $(source).parent().parent();
    var id = $(parent.children()[0]).find('text').text();
    var list = $('.circleTokenP .count');
    for(var i = id; i < list.length; i ++) {
        var item = list[i];
        $(item).text(i);
    }

    parent.remove();
    delphone2table($(parent.children()[1]).children("span").html());
}

function insertPhoneData(phone) {
    if(phone !== '') {
        var token = createCircleToken($('#phoneTable tr').length + 1);
        var phoneShow = createPhoneShow(phone);
        $('#phoneTable').append("<tr><td width='20'>"+token+'、</td><td class="realnumber">'+phoneShow+'</td><td align="right"><img onclick="removeSinglePhone(this)" src="/AMD/resources/thirdparty/assets/images/ico9/on/003_15.png" ></td></tr>');
        $('#phoneEntry').val('');

        //////    加入历史记录，用做智能提示     ///////
        var cookieInput = $.cookie('CONJUGATE_COOKIE_INPUT');
        if (cookieInput == undefined || cookieInput == null) {
            $.cookie('CONJUGATE_COOKIE_INPUT', '', {expires: 30});
            cookieInput = $.cookie('CONJUGATE_COOKIE_INPUT');
        }

        var numberList = cookieInput.split(',');
        var foundSome = false;
        for (var i = 0; i < numberList.length; i ++) {
            if (phone == numberList[i]) {
                foundSome = true;
                break;
            }
        }

        if (!foundSome) {
            //   最多存二十个记录
            if (numberList.length >= 20) {
                numberList.splice(1, 1);
            }
            numberList.push(phone);
            $.cookie('CONJUGATE_COOKIE_INPUT', numberList, {expires: 30});
            $('#autoList').append("<option label='"+phone+"' value='"+phone+"'/>");
        }

        //////    加入历史记录，来用作智能提示     ///////
    }
}

function translateAnalysePattern(description) {

    switch(description) {
        case 'COMMUNICATE': return '通信分析';
        case 'IM': return '即时消息分析';
        case 'LOCATION': return '区域分析';
    }
}

function isDigital(data) {
    return /^\d+$/.test(data);
}

function isFloat(data) {
    return /^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$/.test(data);
}


function showDetail(source) {

    var item = $(source).parent().parent();
    var msgItem = {
        source: $(item.children()[0]).text(),
        target: $(item.children()[1]).text(),
        type: $(item.children()[3]).text(),
        time:$(item.children()[4]).text()
    };

    $.ajax({
        url: '/AMD/conjugate/message',
        type: 'POST',
        data: JSON.stringify(msgItem),
        dataType: 'text',
        contentType: 'application/json;charset=UTF-8',
        success: function(result) {
            alertify.alert(result);
        },
        error: function() {
            alertify.alert('错误！请检查网络环境！');
        }
    });
}

function createTable() {

    $('#myTable').DataTable().destroy();
    var table = $('#myTable').dataTable({
        "data": window.__TABLE_DATA,
        "columns": [
            {"title": "手机号"},
            {"title": "对端号码"},
            {"title": "记录类型"},
            {"title": "通信类型"},
            {"title": "开始时间"},
            {"title": "结束时间"},
            {"title": "通信总次数"},
            {"title": "备注"}
        ],
        "createdRow": function (row, data, index) {
            if (data[2] === '短信') {
                $('td', row).eq(7).html("<a href='javascript:void(0)' onclick='showDetail(this)'>详情</a>");
            }
        }
    });
    var lengthFilterItem = $('.dataTables_length');
    $('#tableLengthFilterTd').append(lengthFilterItem);

    if (lengthFilterItem.length > 1) {
        lengthFilterItem.eq(0).remove();
    }

    table.show();
    window.__TABLE_CREATED = true;
}


function setStatic(){
    var nodes = global_nodes;
    for(var i=0;i<nodes.length;i++) {
        nodes[i].fixed = 1;
    }
}
function unStatic(){
    $('#mygraph').mask("视图处理中");

    var nodes = global_nodes;
    for(var i=0;i<nodes.length;i++) {
        nodes[i].fixed = 0;
    }
    $('#mygraph').unmask();
    //  $("body").unmask();


    setTimeout('setStatic()',2000);
}

