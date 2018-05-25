/**
 * Created by zhengjiabin on 14/11/2.
 */

$(function() {
    $('.slidebar>img').mouseover(function() {

        var img = $(this).attr('src');
        if (img.indexOf('hover') == -1) {
            var index_partStart = img.indexOf('slidebar');
            var index_partEnd = img.lastIndexOf('_');
            img = img.substring(0, index_partStart) + 'slidebar_hover' + img.substring(index_partEnd);

            $(this).attr('src', img);

        }
    });

    $('.slidebar>img').mouseout(function() {

        var img = $(this).attr('src');
        var index_partStart = img.indexOf('slidebar');
        var index_partEnd = img.lastIndexOf('_');
        img = img.substring(0, index_partStart) + 'slidebar' + img.substring(index_partEnd);
        $(this).attr('src', img);
    });

    $('.slidebar>img').click(function() {

        // 记录状态
        var state = isHidden('#leftbar');
        $('#leftbar').toggle('fast');

        var img = $(this).attr('src');
        var btgScale = $('#btgScale');
        var svgItem = $('#rightbar svg');
        var highchartContainer = $('#highchartsContainer');
        var containerDiv = $('.highcharts-container');
        // 操作其他控件元素的状态
        if(state) {
            // 弹开
            $(this).attr('src', img.replace('right', 'left'));
            $('#rightbar').animate({
                width: '730px'
            }, 'fast');

            if (btgScale.length != 0 && btgScale.attr('data-value') == 'conjugate') {
                btgScale.animate({
                    left: '420px'
                }, 'fast');

                $('ul.phototip').animate({
                    left: '520px'
                }, 'fast');
            }

            if (svgItem.length != 0) {
                var width = parseInt(svgItem.attr('width'));
                width = width - 390;
                svgItem.attr('width', width);
            }

            if (containerDiv.length != 0) {
                containerDiv.animate({
                    width: '768px'
                }, 'fast', 'swing', function() {
                    ChartModule.reflow();
                });
            }


        } else {
            // 缩入
            $(this).attr('src', img.replace('left', 'right'));
            $('#rightbar').animate({
                width: '1130px'
            }, 'fast');

            if (btgScale.length != 0 && btgScale.attr('data-value') == 'conjugate') {
                $('#btgScale').animate({
                    left: '40px'
                }, 'fast');

                $('ul.phototip').animate({
                    left: '130px'
                }, 'fast');
            }

            if (svgItem.length != 0) {
                width = parseInt(svgItem.attr('width'));
                width = width + 390;
                svgItem.attr('width', width);
            }

            if (containerDiv.length != 0) {
                containerDiv.animate({
                    width: '1130px'
                }, 'fast', 'swing', function() {
                    ChartModule.reflow();
                });
            }
        }
    });

    function isHidden(selector) {
        return $(selector).css('display') == 'none';
    }

});
