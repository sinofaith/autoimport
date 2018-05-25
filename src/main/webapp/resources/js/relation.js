/**
 * Created by guibin on 14-8-4.
 */
$(function() {

    // 分析模式选择
    $('.btn-pattern').click(function() {
        if(!$(this).hasClass('selected')) {
            $('#patternSelection>.selected').removeClass('selected');
            $(this).addClass('selected');
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

            insertPhoneData(phone);

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
        }
    });


    $('#btnDownload').click(function() {
            $("body").mask("正在拼命下载记录中，请稍候...");

            var _type = "";
            $("#patternSelection a").each(function(){
                if($(this).hasClass("selected"))
                    _type = $(this).attr("value");
            });
            var _number="";
            $("#phoneTable>li").each(function(){
                _number = _number+";"+$(this).children("span[type='text']").html();
            });
            if(_number==""||_type=="") {
                alert("分析对象和分析方式不能为空");
                return;
            }

            $.ajax({
                url: '/AMD/relation/logFilePath',
                type: 'GET',
                data: 'type='+_type+'&'+'source='+_number,
                dataType: 'text',
                contentType: 'application/json;charset=UTF-8',
                success: function(result) {
                    console.log('log path:' + result);
                    $('#downloadForm>input').val(result);
                    $('#downloadForm').submit();
                    $("body").unmask();
                },
                error: function(error) {
                    alert('错误！请检查网络环境！');
                    $("body").unmask();
                }
            });


    });

    $('#btnLoadFile').click(function() {
        $("#phoneSetting>form>input[type='file']").click();
    });
});

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
    }
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

// 显示/隐藏清除按钮
function showRemoveButton(source) {
    $(source).find('.circleToken-red').css('display', '');
}

function hideRemoveButton(source) {
    $(source).find('.circleToken-red').css('display', 'none');
}

function changeview(v){
    if(v=="all"){
        $("tr").show();
    }
    else{
        $("tbody tr").hide();
        $(v).show();
    }
}