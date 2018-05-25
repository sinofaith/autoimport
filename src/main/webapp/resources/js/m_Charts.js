/**
 * Created by zhengjiabin on 14/12/6.
 */

var ChartModule = (function() {

    var module = {};
    var container, title, data;

    var colors_normal = ['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'];
    var colors_smsFirst = ['#50B432', '#058DC7', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'];

    module.setting = function(_container, _title, _data) {
        container = _container;
        title = _title;
        data = _data;
    };

    module.setContainer = function(_container) {
        container = _container;
    };
    module.getContainer = function() {
        return container;
    };
    module.setData = function(_data) {
        data = _data;
    };
    module.getData = function() {
        return data;
    };

    module.updateDataSource = function(_data, _xAxisCategories) {

        var chart = $(container).highcharts();
        if (chart != undefined) {
            data = _data;
            chart.xAxis[0].setCategories(_xAxisCategories, false);
            for (var i = 0; i < _data.length; i ++) {
                chart.series[i].name = _data[i].name;
                chart.series[i].setData(_data[i].data, false);
            }

            chart.redraw();
        }
    };

    module.createLineChart = function() {
        if (container == undefined || title == undefined || data == undefined) {
            console.log('================== Create Chart Error ==================');
            return;
        }

        $(container).highcharts({
            title: {
                text: title,
                x: -20 //center
            },
            xAxis: {
                categories: ['0:00', '1:00', '2:00', '3:00', '4:00', '5:00','6:00', '7:00', '8:00', '9:00', '10:00', '11:00',
                '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00']
            },
            yAxis: {
                title: {
                    text: '平均次数'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: data,
            credits: {
                enabled: false   // 禁用版权信息
            }
        });
    };

    module.createBarChart = function(xAxisCategories, smsFirst) {

        if (container == undefined || title == undefined || data == undefined) {
            console.log('================== Create Chart Error ==================');
            return;
        }

        var usedColors = smsFirst? colors_smsFirst : colors_normal;

        $(container).highcharts({
            chart: {
                type: 'column'
            },
            colors: usedColors,
            title: {
                text: title
            },
            xAxis: {
                categories: xAxisCategories
            },
            yAxis: {
                min: 0,
                title: {
                    text: '次数'
                }
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: data,
            credits: {
                enabled: false   // 禁用版权信息
            }
        });
    };

    module.createPerformanceLineChart=function(){
        if (container == undefined || title == undefined || data == undefined) {
            console.log('================== Create Chart Error ==================');
            return;
        }

        $(container).highcharts({
            title: {
                text: title,
                x: -20 //center
            },
            xAxis: {
                categories:data.xAxisCategories
            },
            yAxis: {
                title: {
                    text: '绩效评分'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: data.seriesData,
            credits: {
                enabled: false   // 禁用版权信息
            }
        });
    };

    module.reflow = function() {
        var chart = $(container).highcharts();
        if (chart != undefined) {
            chart.reflow();
        }
    };

    return module;

}());