/**
 * Created by zhengjiabin on 14-7-31.
 */

$(function() {
    $('#b_bsfx').parent().attr("href","#");
    init();

    // 分析模式选择
    $('.btn-pattern').click(function() {

        if($(this).hasClass('selected')) return;

        // 选中效果
        $('#patternSelection>.selected').removeClass('selected');
        $(this).addClass('selected');

        // 移除图例
        $('.phototip').children().remove();

        if ($('#showBoard>svg').length !== 0) {
            $('#showBoard>svg').remove();
        }
        var text = $(this).text();
        if (text === '即时消息分析') {
            $('#IMFilterDiv').show();
            $('#zoneFilterDiv').hide();
            $('.communicateController').hide();
            $('#btnDetails').hide();

            createIMLegend();

        } else if (text === '通信分析') {
            $('#IMFilterDiv').hide();
            $('#zoneFilterDiv').show();
            $('#btnDetails').show();

            createCommunicateLegend();

            if ($('#modelToggle>.selected').text() === '图形模式') {

                $('.communicateController').show();
                $('#tableDiv').hide();
                if (window.__VIZ_JSON_DATA != undefined) {

                    var mode = $('#showController>select').val();
                    if (mode === '直线关系') {
                        createCommunicateD3Viz(window.__VIZ_JSON_DATA, true);
                    } else if (mode === '关联关系') {
                        createCommunicateD3Viz(window.__VIZ_JSON_DATA, false);
                    }

                    window.__VIZ_TRANSFORM = {
                        transX: 0,
                        transY: 0,
                        scale: 1
                    };
                }
            } else {
                $('#modelToggle').show();
                $('#tableDiv').show();
            }

        } else if (text === '区域分析') {
            $('#IMFilterDiv').hide();
            $('#zoneFilterDiv').show();
            $('.communicateController').hide();
            $('#btnDetails').hide();
        }
    });

    // 图形模式/列表模式状态转换
    $('#modelToggle>a').click(function() {
        if(!$(this).hasClass('selected')) {

            var model = $(this).attr('model');
            if(model === 'viz') {
                $("a[model='table']").removeClass('selected');
                $(this).find('img').attr('src','/AMD/resources/img/vizModel_selected.png');
                $("a[model='table']>img").attr('src','/AMD/resources/img/tableModel.png');

                $('#btgScale').show();
                $('#btnDownload').show();
                $('#tableDiv').hide();
                $('#showController>select').show();
                var svg = $('#showBoard>svg');
                if (svg.length != 0) {
                    svg.show();
                } else {
                    var pattern = $('#patternSelection .selected').text();
                    if (pattern === '通信分析' && window.__VIZ_JSON_DATA !== undefined) {
                        createCommunicateD3Viz(window.__VIZ_JSON_DATA, $('#showController>select').val() == '直线关系');
                    }
                }
            } else {
                $("a[model='viz']").removeClass('selected');
                $(this).find('img').attr('src','/AMD/resources/img/tableModel_selected.png');
                $("a[model='viz']>img").attr('src','/AMD/resources/img/vizModel.png');

                $('#btgScale').hide();
                $('#btnDownload').hide();

                $('#tableDiv').show();
                if ($('#myTable>tbody').length === 0) {
                    $('#myTable').hide();
                }

                $('#showController>select').hide();
                DetailsTool.hide();

                if ($('#showBoard>svg').length !== 0) {
                    $('#showBoard>svg').hide();
                }
            }
            $(this).addClass('selected');
        }
    });

    $('#phoneEntry').bind('keypress', function(event) {
        if(event.keyCode == '13') {
            $('#btnAddPhone').click();
        }
    });

    // 添加手机号
    $('#btnAddPhone').click(function() {
        var phone = $('#phoneEntry').val().trim();
        insertPhoneData(phone);
    });

    // 清除信息
    $('#btnRemoveAll').click(function() {
        if($('#phoneTable').length != 0) {
            $('#phoneTable').remove();
        }

        $('#startTimePicker').val('');
        $('#endTimePicker').val('');

        $('#s_province').val('省份');
        $('#s_city').val('地级市');
        $('#s_county').val('市、县级市');

        if ($('#phoneEntry').hasClass('warning')) {
            $('#phoneEntry').removeClass('warning');
        }

        if ($('#startTimePicker').hasClass('warning')) {
            $('#startTimePicker').removeClass('warning');
        }

        if ($('#endTimePicker').hasClass('warning')) {
            $('#endTimePicker').removeClass('warning');
        }
    });

    $('#startTimePicker').focus(function() {
        if ($('#startTimePicker').hasClass('warning')) {
            $('#startTimePicker').removeClass('warning');
        }

        if ($('#endTimePicker').hasClass('warning')) {
            $('#endTimePicker').removeClass('warning');
        }
    });

    $('#endTimePicker').focus(function() {
        if ($('#startTimePicker').hasClass('warning')) {
            $('#startTimePicker').removeClass('warning');
        }

        if ($('#endTimePicker').hasClass('warning')) {
            $('#endTimePicker').removeClass('warning');
        }
    });

    // 分析按钮响应
    $('#btnAnalyse').click(function() {

        var phoneTable = $('#phoneTable');
        var startTimePicker = $('#startTimePicker');
        var endTimePicker = $('#endTimePicker');
        if(phoneTable.length === 0 || (startTimePicker.val() === '' && endTimePicker.val() === '')) {

            var phoneFlag = false;
            if (phoneTable.length === 0) {
                $('#phoneEntry').addClass("warning");
                phoneFlag = true;
            }

            var timeFlag = false;
            if (startTimePicker.val() === '' && endTimePicker.val() === '') {
                startTimePicker.addClass("warning");
                endTimePicker.addClass("warning");
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
            var phoneDomList = $("#phoneTable li span[type='text']");
            for(var i = 0; i < phoneDomList.length; i ++) {
                var phone = $(phoneDomList[i]).text();
                option.numberList.push(phone);

                phones = phones + phone + ';';
            }

            option.minTime = startTimePicker.val();
            option.maxTIme = endTimePicker.val();

            option.s_province = $('#s_province').val();
            option.s_city = $('#s_city').val();
            option.s_county = $('#s_county').val();

            option.analysePattern = $('#patternSelection>.selected').text();
            option.vizPattern = $('#showController>select').val();
            option.showPattern = $('#modelToggle>.selected').text();
            option.keyWord = $('#keywordInput').val().trim();
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

            if (((option.analysePattern === '通信分析') && (option.showPattern === '图形模式')) || option.analysePattern === '即时消息分析' || option.analysePattern === '区域分析') {
                $("html").mask("正在拼命创建可视化分析图中，请稍候...");
                $.ajax({
                    url: '/AMD/conjugate/option',
                    type: 'POST',
                    data: JSON.stringify(option),
                    dataType: 'json',
                    contentType: 'application/json;charset=UTF-8',
                    success: function(result) {
                        var svgDom = $('#showBoard>svg');
                        if (svgDom.length !== 0) {
                            svgDom.remove();
                        }
                        window.__VIZ_JSON_DATA = result;
                        if (option.analysePattern === '通信分析') {
                            if (option.vizPattern === '直线关系') {
                                createCommunicateD3Viz(result, true);
                            } else {
                                createCommunicateD3Viz(result, false);
                            }
                        } else if (option.analysePattern === '即时消息分析'){
                            createIMD3Viz(result);
                        } else {
                            createZoneD3Viz(result);
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
            } else if ((option.analysePattern === '通信分析') && (option.showPattern === '列表模式')) {
                $("#tablePatternForm>input[name='phones']").val(phones);
                $("#tablePatternForm>input[name='maxTime']").val(option.maxTIme);
                $("#tablePatternForm>input[name='minTime']").val(option.minTime);
                $("#tablePatternForm>input[name='s_province']").val(option.s_province);
                $("#tablePatternForm>input[name='s_city']").val(option.s_city);
                $("#tablePatternForm>input[name='s_county']").val(option.s_county);
                $("#tablePatternForm>input[name='analysePattern']").val(option.analysePattern);

                $("html").mask("正在拼命抓取列表数据中，请稍候...");
                $('#tablePatternForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(result) {
                        $("html").unmask();
                        $('#myTable').DataTable().destroy();
                        var table = $('#myTable').dataTable({
                            "data": result,
                            "columns": [
                                {"title": "手机号"},
                                {"title": "对端号码"},
                                {"title": "记录类型"},
                                {"title": "通信类型"},
                                {"title": "开始时间"},
                                {"title": "结束时间"},
                                {"title": "备注"}
                            ],
                            "createdRow": function(row, data, index) {
                                if (data[2] === '短信') {
                                    $('td', row).eq(6).html("<a href='javascript:void(0)' onclick='showDetail(this)'>详情</a>");
                                }
                            }
                        });
                        table.show();
                    },
                    error: function() {
                        $("html").unmask();
                        alertify.alert('错误！请检查网络环境！');
                    }
                });
            }
        }
    });

    $('#showController>select').change(function() {
        var mode = $(this).val();
        if (window.__VIZ_JSON_DATA != undefined && $('#showBoard>svg').length != 0) {
            $('#showBoard>svg').remove();

            if (mode === '直线关系') {
                createCommunicateD3Viz(window.__VIZ_JSON_DATA, true);
            } else if (mode === '关联关系') {
                createCommunicateD3Viz(window.__VIZ_JSON_DATA, false);
            }

            window.__VIZ_TRANSFORM = {
                transX: 0,
                transY: 0,
                scale: 1
            };
        }
    });

    // 可视化图变形
    $('#btnZoomIn').click(function() {

        if ($('#showBoard>svg').length != 0  && window.__VIZ_TRANSFORM != undefined) {

            window.__VIZ_TRANSFORM.transX = window.__VIZ_TRANSFORM.transX - 30;
            window.__VIZ_TRANSFORM.transY = window.__VIZ_TRANSFORM.transY - 30;
            window.__VIZ_TRANSFORM.scale = window.__VIZ_TRANSFORM.scale / 0.9;
            doTransform(window.__VIZ_TRANSFORM.transX, window.__VIZ_TRANSFORM.transY, window.__VIZ_TRANSFORM.scale);
        }
    });

    $('#btnZoomOut').click(function() {

        if ($('#showBoard>svg').length != 0 && window.__VIZ_TRANSFORM != undefined) {

            window.__VIZ_TRANSFORM.transX = window.__VIZ_TRANSFORM.transX + 30;
            window.__VIZ_TRANSFORM.transY = window.__VIZ_TRANSFORM.transY + 30;
            window.__VIZ_TRANSFORM.scale = window.__VIZ_TRANSFORM.scale * 0.9;
            doTransform(window.__VIZ_TRANSFORM.transX, window.__VIZ_TRANSFORM.transY, window.__VIZ_TRANSFORM.scale);
        }
    });

    $('#btnLeft').click(function() {

        if ($('#showBoard>svg').length != 0 && window.__VIZ_TRANSFORM != undefined) {

            window.__VIZ_TRANSFORM.transX = window.__VIZ_TRANSFORM.transX - 40;
            doTransform(window.__VIZ_TRANSFORM.transX, window.__VIZ_TRANSFORM.transY, window.__VIZ_TRANSFORM.scale);
        }
    });

    $('#btnRight').click(function() {

        if ($('#showBoard>svg').length != 0 && window.__VIZ_TRANSFORM != undefined) {

            window.__VIZ_TRANSFORM.transX = window.__VIZ_TRANSFORM.transX + 40;
            doTransform(window.__VIZ_TRANSFORM.transX, window.__VIZ_TRANSFORM.transY, window.__VIZ_TRANSFORM.scale);
        }
    });

    $('#btnUp').click(function() {

        if ($('#showBoard>svg').length != 0 && window.__VIZ_TRANSFORM != undefined) {

            window.__VIZ_TRANSFORM.transY = window.__VIZ_TRANSFORM.transY - 40;
            doTransform(window.__VIZ_TRANSFORM.transX, window.__VIZ_TRANSFORM.transY, window.__VIZ_TRANSFORM.scale);
        }
    });

    $('#btnDown').click(function() {

        if ($('#showBoard>svg').length != 0 && window.__VIZ_TRANSFORM != undefined) {

            window.__VIZ_TRANSFORM.transY = window.__VIZ_TRANSFORM.transY + 40;
            doTransform(window.__VIZ_TRANSFORM.transX, window.__VIZ_TRANSFORM.transY, window.__VIZ_TRANSFORM.scale);
        }
    });

    $('#btnDetails').click(function() {
        DetailsTool.toggle();
    });

    $('#btnDownload').click(function() {
        if (('#rightbar>svg').length != 0 && window.__VIZ_JSON_DATA != undefined) {
            $("html").mask("正在拼命下载记录中，请稍候...");

            var _data = $('#showController>select').val() === '直线关系'?
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
                    console.log('log path:' + result);
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

    $('#unstatic').click(function(){

    });

});

function createCircleToken(num) {
    return "<span class='circleToken'><text class='count'>"+num+"</text></span>";
}

function createCircleTokenClear() {
    return "<span class='circleToken circleToken-red' style='float: right;cursor: pointer;display: none;' onclick='removeSinglePhone(this)'><text style='font-size: medium;vertical-align: top;'>X</text></span>";
}

function createPhoneShow(phone) {
    return "<span type='text'>"+phone+"</span>";
}

function removeSinglePhone(source) {

    var parent = $(source).parent();
    var id = $(parent.children()[0]).find('text').text();
    var list = $('.circleToken .count');
    for(var i = id; i < list.length; i ++) {
        var item = list[i];
        $(item).text(i);
    }

    parent.remove();

    if($('#phoneTable>li').length === 0) {
        $('#phoneTable').remove();
    }
}

// 显示/隐藏清除按钮
function showRemoveButton(source) {
    $(source).find('.circleToken-red').css('display', '');
}

function hideRemoveButton(source) {
    $(source).find('.circleToken-red').css('display', 'none');
}

function init() {

    $('#b_bsfx').addClass('hover');

    // 载入文件按钮状态转换
    $('#btnLoadFile').mouseover(function(){
        $(this).attr('src', '/AMD/resources/img/loadFile_hover.png');
    });


    $('#btnLoadFile').mouseout(function(){
        $(this).attr('src', '/AMD/resources/img/loadFile.png');
    });

    $('#btnLoadFile').click(function() {
        $("#phoneSetting>form>input[type='file']").click();
    });

    $("#phoneSetting>form>input[type='file']").change(function(event) {
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
    // 启动时间插件
//    $('#startTimePicker').val(getTimeNow());
    $('#startTimePicker').val('2010/10/01 00:00:00');
    $('#startTimePicker').datetimepicker({
        lang:'ch'
    });
    $('#endTimePicker').datetimepicker({
        lang:'ch'
    });

    $('#btnViz').addClass('selected');
    $('#btnViz').find('img').attr('src', '/AMD/resources/img/vizModel_selected.png');
    $('btnTable').find('img').attr('src', '/AMD/resources/img/tableModel.png');
    $('#tableDiv').hide();
}

function doTransform(transX,transY,scale) {

    $('#showBoard>svg>g').attr('transform', 'translate('+transX+','
        +transY+') scale('
        +scale+')');
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

function insertPhoneData(phone) {
    if(phone !== '') {

        var text = $('#patternSelection>.selected').text();
        if(!verifyData(text, phone)) {
            alertify.alert(phone + ':     输入数据格式有误！');
            return;
        }

        if($('#phoneTable').length == 0) {
            $('#phoneSetting').append("<ul id='phoneTable'></ul>");
        }
        var token = createCircleToken($('#phoneTable>li').length + 1);
        var phoneShow = createPhoneShow(phone);
        var btnClear = createCircleTokenClear();
        $('#phoneTable').append("<li onmouseover='showRemoveButton(this)' onmouseout='hideRemoveButton(this)'>"+token+phoneShow+btnClear+"</li>");
        $('#phoneEntry').val('');

        if ($('#phoneEntry').hasClass('warning')) {
            $('#phoneEntry').removeClass('warning');
        }

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

function verifyData(pattern, data) {

    data = data.trim();
    // 要全为数字才通过
    if (pattern == '通信分析' || pattern == '区域分析') {
        return isDigital(data);
    } else if (pattern  == '即时消息分析') {
        var prefix = data.substring(0, 3).toLowerCase();
        if (prefix == 'qq:' || prefix == 'qq：' || prefix == 'wx:' || prefix == 'wx：') {
            return true;
        } else
            return false;
    } else
        return false;
}

function isDigital(data) {
    return /^\d+$/.test(data);
}



function createCommunicateLegend() {
    $('.phototip').html("<li style='background-color:#FF9900'>目标号码</li>" +
        "<li style='background-color:#FFDF6B'>联系号码</li>");
}

function createIMLegend() {
    $('.phototip').html("<li style='background-color:#C6DBEF'>簇节点</li>" +
        "<li style='background-color:#1FBF41'>目标微信号</li>" +
        "<li style='background-color:#82E042'>联系微信号</li>" +
        "<li style='background-color:#00AAEF'>目标QQ号</li>" +
        "<li style='background-color:#6BDFFF'>联系QQ号</li>");
}


