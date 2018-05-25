$(document).ready(function(){
    //$.ajax({
    //    url: "personnelInfo/seachCode",
    //    type: "POST",
    //    dataType: 'json',
    //    data: {"value": ""},
    //    async: true,
    //    success: function (result) {
    //        var li = document.getElementById('ul_height01');
    //        li.innerHTML= "";
    //        for (var i = 0; i < result.length; i++) {
    //            li.innerHTML+="<li><span>"+result[i]+"</span><i class='icon-icon_delete2 fl_r ' onclick=deleteSeachCode('"+result[i]+"')></i></li>";
    //        }
    //    }
    //});
    //$.ajax({
    //    url: "detail/personnelInfo/seachCode",
    //    type: "GET",
    //    dataType: 'json',
    //    data: {"value": ""},
    //    async: true,
    //    success: function (result) {
    //        var li = document.getElementById('ul_height01');
    //        li.innerHTML= "";
    //        for (var i = 0; i < result.length; i++) {
    //            li.innerHTML+="<li><span>"+result[i]+"</span><i class='icon-icon_delete2 fl_r ' onclick=deleteSeachCode('"+result[i]+"')></i></li>";
    //        }
    //    }
    //});
    var seachCode = $(".seachCode");
    //seachCode.keyup(function(e){
    //    if(e.keyCode == 13){
    //        var seachCode = $(".seachCode").val();
    //        if(!seachCode && typeof(seachCode)!="undefined" && seachCode!=0 ){
    //            return;
    //        }
    //        var checkCode = /^[0-9a-zA-Z]*$/g;
    //        if(!checkCode.test(seachCode.replace(/\ +/g,""))){
    //            alertify.alert('你要查询的信息格式不正确! 请从新输入...');
    //            return;
    //        }
    //        $.ajax({
    //            url: "personnelInfo/addSeachCode",
    //            type: "GET",
    //            dataType: 'json',
    //            data: {"value": seachCode},
    //            async: true,
    //            success: function (result) {
    //                var li = document.getElementById('ul_height01');
    //                li.innerHTML= "";
    //                for (var i = 0; i < result.length; i++) {
    //                    li.innerHTML+="<li><span>"+result[i]+"</span><i class='icon-icon_delete2 fl_r ' onclick=deleteSeachCode('"+result[i]+"')></i></li>";
    //                }
    //            }
    //        });
    //        $(".seachCode").val("");
    //    }
    //});

    // 载入文件按钮状态转换
    $('#btnLoadFile').mouseover(function(){
        $(this).attr('src', '/AMD/resources/img/loadFile_hover.png');
    });

    $('#btnLoadFile').mouseout(function(){
        $(this).attr('src', '/AMD/resources/img/loadFile.png');
    });
    //导入按钮绑定事件
    $('#btnLoadFile').click(function() {
        $("input[type='file']").click();
    });

    $("#uploadFileForm>input[type='file']").change(function(event) {

        $('#uploadFileForm').ajaxSubmit({
            dataType: 'text',
            success: function(result) {
                if (result == "") {
                    alertify.alert('上传文件出现问题！请检查网络环境是否正常或文件格式及内容是否符合标准！');
                } else{
                    alertify.alert("导入成功.");
                    setTimeout(function () {document.getElementById("seachDetail").submit()},3000);
                }
            }
        });

        $(this).val('');
    });

    $(".myTip").mouseover(function(event){
        var label =  $(this).attr("title");
        $("#label").val(label);
        $(this).attr("title","");
        var tooltipHtml = "<div id='tipalert' class='tipalert'>"+label+"<span class='arrow'></span></div>";
        $(this).before(tooltipHtml);
        $(".tipalert").css({
            "top": (event.pageY) -120+ "px",
            "left": ($(this).width()) +"px"  //紧跟在内容的后面
        }) //设置提示框的坐标，并显示

    }).mouseout(function(){
        $(".tipalert").remove();
        $(this).attr("title",$("#label").val());
    });

    $(".icon-copytext").click(function(){
        $(".seachCode").val(window.clipboardData.getData('Text'));
    });

    $(".radio").click(function(){
        var radionValue = $("input[type='radio']:checked").val();
        $("#updatestate").val(radionValue);
    });
});

function removePrint(){
    $(".tipalert").remove();
}

function deleteSeachCode(seachCode) {
    $.ajax({
        url: "personnelInfo/delSeachCode",
        type: "GET",
        dataType: 'json',
        data: {"value": String(seachCode)},
        async: true,
        success: function (result) {
            var li = document.getElementById('ul_height01');
            li.innerHTML= "";
            for (var i = 0; i < result.length; i++) {
                li.innerHTML+="<li><span>"+result[i]+"</span><i class='icon-icon_delete2 fl_r ' onclick=deleteSeachCode('"+result[i]+"')></i></li>";
            }
        }
    });
};
