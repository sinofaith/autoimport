/**
 * Created by guibin on 14-8-4.
 */
$(function() {
    $('#b_kzfx').parent().attr("href","#");
    // 扩展模式选择
    $('.btn-pattern').click(function() {
        if(!$(this).hasClass('selected')) {
            $(this).addClass('selected');
        }
        else{
            $(this).removeClass('selected');
        }
    });

    // 启动时间插件
    $('#startTimePicker').datetimepicker();
    $('#endTimePicker').datetimepicker();

//缩放

    $('#btnZoomIn').click(function() {
        if ($('#rightbar>svg').length != 0  && window.__VIZ_TRANSFORM != undefined) {
            var graph = $('#rightbar>svg>g');

            var transX = window.__VIZ_TRANSFORM.transX - 30;
            var transY = window.__VIZ_TRANSFORM.transY - 30;
            var scale = window.__VIZ_TRANSFORM.scale / 0.9;
            graph.attr('transform', 'translate('+transX+','+transY+') scale('+scale+')');
            window.__VIZ_TRANSFORM.transX = transX;
            window.__VIZ_TRANSFORM.transY = transY;
            window.__VIZ_TRANSFORM.scale = scale;
            //-------------------
            $('#rightbar>svg>g>g').each(function(){
                $(this).attr('transform', $(this).attr('transform').split(' ')[0]+' scale('+(window.__VIZ_TRANSFORM.scale>1?1/window.__VIZ_TRANSFORM.scale:1)+')');
            });
        }
    });

    $('#btnZoomOut').click(function() {
        if ($('#rightbar>svg').length != 0 && window.__VIZ_TRANSFORM != undefined) {
            var graph = $('#rightbar>svg>g');

            var transX = window.__VIZ_TRANSFORM.transX + 30;
            var transY = window.__VIZ_TRANSFORM.transY + 30;
            var scale = window.__VIZ_TRANSFORM.scale * 0.9;
            graph.attr('transform', 'translate('+transX+','+transY+') scale('+scale+')');
            window.__VIZ_TRANSFORM.transX = transX;
            window.__VIZ_TRANSFORM.transY = transY;
            window.__VIZ_TRANSFORM.scale = scale;
            //-------------------
            $('#rightbar>svg>g>g').each(function(){
                $(this).attr('transform', $(this).attr('transform').split(' ')[0]+' scale('+(window.__VIZ_TRANSFORM.scale>1?1/window.__VIZ_TRANSFORM.scale:1)+')');
            });
        }
    });
    // 载入文件按钮状态转换
    $('#btnLoadFile').mouseover(function(){
        $(this).attr('src', '/AMD/resources/img/loadFile_hover.png');
    });

    $('#btnLoadFile').mouseout(function(){
        $(this).attr('src', '/AMD/resources/img/loadFile.png');
    });

    $('#phoneEntry').bind('keypress', function(event) {
        if(event.keyCode == '13') {
            $('#btnAddPhone').click();
        }
    });
    // 添加手机号
    $('#btnAddPhone').click(function() {
        var phone = $('#phoneEntry').val().trim();
        //集合处理
        if(phone[0]=="@"){
            $.getJSON("amdset/setphonesbyname",{setname:phone},function(data){
                if(data==null)
                    $('#phoneEntry').val('');
                else {
                    for (var i = 0; i < data.length; i++) {
                        insertPhoneData(data[i]);
                    }
                }
            });
        }
        else
            insertPhoneData(phone);

        $("#extendBtn").text("新建扩展分析");
    });

    $("#phoneSetting>form>input[type='file']").change(function(event) {
        $("body").mask("正在拼命上传文件中，请稍候....");
        $('#uploadFileForm').ajaxSubmit({
            dataType: 'json',
            success: function(result) {
                if (result == null) {
                    alert('上传文件出现问题！请检查网络环境是否正常或文件格式及内容是否符合标准！');
                } else if(result.length === 0) {
                    alert('文件内容为空.');
                } else {
                    for(var i = 0; i < result.length; i ++) {
                        insertPhoneData(result[i]);
                    }
                }
                $("body").unmask();
            },
            error: function(result) {
                alert('上传文件出现问题！请检查网格环境是否正常或文件格式及内容是否符合标准！');
                $("body").unmask();
            }
        });

        $(this).val('');
    });

    // 清除全部号码
    $('#btnRemoveAll').click(function() {
        if($('#phoneTable').length != 0) {
            $('#phoneTable').remove();
            $("#extendBtn").text("扩  展");
        }
    });

    $('#btnLoadFile').click(function() {
        $("#phoneSetting>form>input[type='file']").click();
    });

    /* 分析按钮响应
    $('#btnAnalyse').click(function() {

        if($('#phoneTable').length === 0) {
            alert('先添加手机号');
        } else {
            var option = {};

            option.phoneList = [];
            var phoneDomList = $("#phoneTable li span[type='text']");
            for(var i = 0; i < phoneDomList.length; i ++) {
                option.phoneList.push($(phoneDomList[i]).text());
            }

            option.minTime = $('#startTimePicker').val();
            option.maxTIme = $('#endTimePicker').val();

            option.s_province = $('#s_province').val();
            option.s_city = $('#s_city').val();
            option.s_county = $('#s_county').val();

            option.analysePattern = $('#patternSelection>.selected').text();
            option.vizPattern = $('#rightbar>select').val();
            option.showPattern = $('#modelToggle>.selected').text();

            $.ajax({
                url: 'conjugate/option',
                type: 'POST',
                data: JSON.stringify(option),
//                data: JSON.stringify({phones:'111;',startTime:'2014-09-09 12:00:45',endTime:'2014-09-10 12:00:00',s_province:'福建省',s_city:'泉州市',s_county:'晋江市',analysePattern:'通信分析',vizPattern:'普通模式',showPattern:'图形模式'}),
                dataType: 'json',
                contentType: 'application/json;charset=UTF-8',
                success: function(result) {
                    console.log(result);
                }
            });
        }
    });
*/
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

        $("#extendBtn").text("扩  展");
    }
}

function insertPhoneData(phone) {
    if(phone !== '') {
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
        var cookieInput = $.cookie('EXTEND_COOKIE_INPUT');
        if (cookieInput == undefined || cookieInput == null) {
            $.cookie('EXTEND_COOKIE_INPUT', '', {expires: 30});
            cookieInput = $.cookie('EXTEND_COOKIE_INPUT');
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
            $.cookie('EXTEND_COOKIE_INPUT', numberList, {expires: 30});
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



//d3 相关
