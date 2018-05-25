/**
 * Created by zhengjiabin on 14/12/5.
 */

$(function() {

    init();

    $('#phoneEntry').bind('keypress', function(event) {
        if(event.keyCode == '13') {
            $('#btnAddPhone').click();
        }
    });

    // 添加手机号
    $('#btnAddPhone').click(function() {
        if ($('#phoneTable').length > 0) {
            alertify.alert('仅支持分析一个号码');
            return ;
        }
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

    $('.btn-pattern').click(function() {
        if(!$(this).hasClass('selected')) {
            $('#patternSelection>.selected').removeClass('selected');
            $(this).addClass('selected');
        }
    });


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

            var phoneDomList= $("#phoneTable li span[type='text']");
            option.number = $(phoneDomList[0]).text();

            option.minTime = startTimePicker.val();
            option.maxTime = endTimePicker.val();
            option.analyseType = $('#analyseTypeSelector').val();
            option.chartType = $('.chartTypeSelector').val();
            option.range = option.chartType == '柱状图'? $("#rangeDiv input[name='range']:checked").val() : '';

            $('body').mask('正在拼命创建统计图中，请稍候...');
            $.ajax({
                url : '/AMD/statistics/chartData',
                type: 'POST',
                data: JSON.stringify(option),
                dataType: 'json',
                contentType: 'application/json;charset=UTF-8',
                success: function(result) {

                    window.__VIZ_STATISTICS_DATA = undefined;
                    if (option.chartType == '折线图') {
                        ChartModule.setting('#highchartsContainer', '24小时联系趋势', result.seriesData);
                        ChartModule.createLineChart();
                        $('#sortDiv').hide();
                    } else {

                        if (option.analyseType == '手机号') {
                            window.__VIZ_STATISTICS_DATA = result;
                            var cloneCallItem = clonePhoneDataSeriesData(window.__VIZ_STATISTICS_DATA.callSeriesData);
                            ChartModule.setting('#highchartsContainer', '重点联系人', cloneCallItem);
                            ChartModule.createBarChart(result.callXaxisCategories);
                            $('#sortDiv').show();
                        } else {
                            ChartModule.setting('#highchartsContainer', '重点联系人', result.seriesData);
                            ChartModule.createBarChart(result.xAxisCategories);
                            $('#sortDiv').hide();
                        }
                    }
                    $('body').unmask();
                },
                error: function() {
                    $('body').unmask();
                    alertify.alert('错误！请检查输入条件或网络环境！');
                }
            });
        }
    });
});


function init() {
//    $('#b_bsfx').addClass('hover');


    // 启动时间插件
//    $('#startTimePicker').val(getTimeNow());
    $('#startTimePicker').val('2010/10/01 00:00:00');
    $('#startTimePicker').datetimepicker();
    $('#endTimePicker').datetimepicker();

    $('.chartTypeSelector').change(function() {

        var type = $(this).val();
        if (type == '折线图') {
            $('#rangeDiv').hide();
        } else {
            $('#rangeDiv').show();
        }
    });

    $('#btnSort').click(function() {
        $('#sortDiv>ul').toggle();
    });

    $('#sortDiv li').click(function() {
        if (window.__VIZ_STATISTICS_DATA != undefined) {
            $('.highcharts-container').remove();
            var sortType = $(this).text();
            if (sortType == '按通话排序') {

                var cloneCallItem = clonePhoneDataSeriesData(window.__VIZ_STATISTICS_DATA.callSeriesData);
                ChartModule.setting('#highchartsContainer', '重点联系人', cloneCallItem);
                ChartModule.createBarChart(__VIZ_STATISTICS_DATA.callXaxisCategories);

            } else {

                var cloneSmsItem = clonePhoneDataSeriesData(window.__VIZ_STATISTICS_DATA.smsSeriesData);
                ChartModule.setting('#highchartsContainer', '重点联系人', cloneSmsItem);
                ChartModule.createBarChart(__VIZ_STATISTICS_DATA.smsXaxisCategories, true);
            }
        }
    });
}

function clonePhoneDataSeriesData(seriesDataItem) {

    var cloneItem = jQuery.extend(true, {}, seriesDataItem);
    var dataArray = [];
    dataArray.push(cloneItem[0]);
    dataArray.push(cloneItem[1]);

    return dataArray;
}

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

function insertPhoneData(phone) {
    if(phone !== '') {

        if($('#phoneTable').length == 0) {
            $('#numberSetting').append("<ul id='phoneTable'></ul>");
        }

        phone = phone.trim();

        var token = createCircleToken($('#phoneTable>li').length + 1);
        var phoneShow = createPhoneShow(phone);
        var btnClear = createCircleTokenClear();
        $('#phoneTable').append("<li onmouseover='showRemoveButton(this)' onmouseout='hideRemoveButton(this)'>"+token+phoneShow+btnClear+"</li>");
        $('#phoneEntry').val('');

        if ($('#phoneEntry').hasClass('warning')) {
            $('#phoneEntry').removeClass('warning');
        }

        //////    加入历史记录，用做智能提示     ///////
        var cookieInput = $.cookie('STATISTICS_COOKIE_INPUT');
        if (cookieInput == undefined || cookieInput == null) {
            $.cookie('STATISTICS_COOKIE_INPUT', '', {expires: 30});
            cookieInput = $.cookie('STATISTICS_COOKIE_INPUT');
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
            $.cookie('STATISTICS_COOKIE_INPUT', numberList, {expires: 30});
            $('#autoList').append("<option label='"+phone+"' value='"+phone+"'/>");
        }

        //////    加入历史记录，来用作智能提示     ///////
    }
}

// 显示/隐藏清除按钮
function showRemoveButton(source) {
    $(source).find('.circleToken-red').css('display', '');
}

function hideRemoveButton(source) {
    $(source).find('.circleToken-red').css('display', 'none');
}