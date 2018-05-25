/**
 * Created by zhengjiabin on 15/1/19.
 */

$(function() {
    init();

    $('#btnAnalyse').click(function() {

        var startTimePicker = $('#startTimePicker');
        var endTimePicker = $('#endTimePicker');

        var type = $("#analyseTypeDiv input[type='radio']:checked").parent().find('span').text().trim();
        if (type == '对端号码分析') {
            return;
        }


        var numberRecords = $('#phoneTable .realnumber');
        if (numberRecords.length > 1 ) {
            alertify.alert("仅支持分析一个号码");
            return ;
        }
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

            var phoneDomList = $('#phoneTable .realnumber>span');
            option.number = $(phoneDomList[0]).text();

            option.minTime = startTimePicker.val();
            option.maxTime = endTimePicker.val();
            option.analyseType = $('#analyseTypeSelector').val();
            option.chartType = type == '重点号码分析'? '柱状图' : '折线图';
            option.pageNum = 1;
            option.phoneType = $("#phoneTypeDiv input[type='radio']:checked").val();

            $('body').mask('正在拼命创建统计图中，请稍候...');
            $.ajax({
                url : '/AMD/statistics/chartData',
                type: 'POST',
                data: JSON.stringify(option),
                dataType: 'json',
                contentType: 'application/json;charset=UTF-8',
                success: function(result) {
                    window.__VIZ_STATISTICS_OPTION = option;
                    window.__VIZ_STATISTICS_DATA = undefined;
                    if (option.chartType == '折线图') {
                        ChartModule.setting('#highchartsContainer', '24小时联系趋势', result.seriesData);
                        ChartModule.createLineChart();
//                        $('#sortDiv').hide();
                        $('#pageTool').hide();
                    } else {

                        if (option.analyseType == '手机号') {
                            window.__VIZ_STATISTICS_DATA = result;

                            if (option.phoneType == '通话') {
                                var cloneCallItem = clonePhoneDataSeriesData(window.__VIZ_STATISTICS_DATA.callSeriesData);
                                ChartModule.setting('#highchartsContainer', '重点联系人', cloneCallItem);
                                ChartModule.createBarChart(result.callXaxisCategories);
                            } else {
                                var cloneSmsItem = clonePhoneDataSeriesData(window.__VIZ_STATISTICS_DATA.smsSeriesData);
                                ChartModule.setting('#highchartsContainer', '重点联系人', cloneSmsItem);
                                ChartModule.createBarChart(result.smsXaxisCategories, true);
                            }

//                            $('#sortDiv').show();
                        } else {
                            ChartModule.setting('#highchartsContainer', '重点联系人', result.seriesData);
                            ChartModule.createBarChart(result.xAxisCategories);
//                            $('#sortDiv').hide();
                        }

                        $('#analyseMsg').text('多维分析 > 重点号码分析 ' + result.pageNum + ' / ' + result.totalPage);
                        window.__VIZ_STATISTICS_TOTALPAGE = result.totalPage;
                        $('#pageTool').show();
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
    });
    // ===========    插入号码    ===========

    $("#analyseTypeDiv input[type='radio']").change(function() {
        var text = $(this).parent().find('span').text().trim();

        if (text == '对端号码分析') {
            window.location = '/AMD/timeline';
            return;
        }

        $('#pageTool').hide();
        $('#analyseMsg').text('多维分析 > ' + text);

        if (text == '重点号码分析') {
            $('.statisticsController').show();
            if ($('#analyseTypeSelector').val() == '手机号') {
                $('#phoneTypeDiv').show();
            } else {
                $('#phoneTypeDiv').hide();
            }

            if (window.__VIZ_STATISTICS_OPTION != undefined && window.__VIZ_STATISTICS_TOTALPAGE != undefined) {
                $('#analyseMsg').text('多维分析 > 重点号码分析 ' + window.__VIZ_STATISTICS_OPTION.pageNum + ' / ' + window.__VIZ_STATISTICS_TOTALPAGE);
                $('#pageTool').show();
            }

        } else {
            $('.statisticsController').hide();
            $('#phoneTypeDiv').hide();
        }
    });

    $('#analyseTypeSelector').change(function() {
        var text = $("#analyseTypeDiv input[type='radio']:checked").parent().find('span').text().trim();
        if (text == '重点号码分析' && $(this).val() == '手机号') {
            $('#phoneTypeDiv').show();
        } else {
            $('#phoneTypeDiv').hide();
        }
    });

//    $('#btnSort').click(function() {
//        $('#sortDiv>ul').toggle();
//    });

//    $('#sortDiv li').click(function() {
//        if (window.__VIZ_STATISTICS_DATA != undefined) {
//
//            $('#sortDiv .selected').removeClass('selected');
//            $(this).addClass('selected');
//            $('.highcharts-container').remove();
//            var sortType = $(this).text();
//            if (sortType == '按通话排序') {
//
//                var cloneCallItem = clonePhoneDataSeriesData(window.__VIZ_STATISTICS_DATA.callSeriesData);
//                ChartModule.setting('#highchartsContainer', '重点联系人', cloneCallItem);
//                ChartModule.createBarChart(__VIZ_STATISTICS_DATA.callXaxisCategories);
//
//            } else {
//
//                var cloneSmsItem = clonePhoneDataSeriesData(window.__VIZ_STATISTICS_DATA.smsSeriesData);
//                ChartModule.setting('#highchartsContainer', '重点联系人', cloneSmsItem);
//                ChartModule.createBarChart(__VIZ_STATISTICS_DATA.smsXaxisCategories, true);
//            }
//        }
//    });
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

}

function insertPhoneData(phone) {
    if(phone !== '') {
        var token = createCircleToken($('#phoneTable tr').length + 1);
        var phoneShow = createPhoneShow(phone);
        $('#phoneTable').append("<tr><td width='20'>"+token+'、</td><td class="realnumber">'+phoneShow+'</td><td align="right"><img onclick="removeSinglePhone(this)" src="/AMD/resources/thirdparty/assets/images/ico9/on/003_15.png" ></td></tr>');
        $('#phoneEntry').val('');

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

function clonePhoneDataSeriesData(seriesDataItem) {

    var cloneItem = jQuery.extend(true, {}, seriesDataItem);
    var dataArray = [];
    dataArray.push(cloneItem[0]);
    dataArray.push(cloneItem[1]);

    return dataArray;
}

