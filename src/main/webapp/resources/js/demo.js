$(function(){
    $(".tab dl dt>a:first").addClass("tabActive");
    $(".tab dl dd .ddr").not(":first").hide();
    $(".tab dl dt>a").unbind("click").bind("click", function(){
        $(this).siblings("a").removeClass("tabActive").end().addClass("tabActive");
        var index = $(".tab dl dt>a").index( $(this) );
        var index=$(".tab dl dt>a").index($(this))
        $(".tab dl dd .ddr").eq(index).siblings(".tab dl dd .ddr").hide().end().fadeIn("slow");
    });
})


$(document).ready(function() {
    $(".she_but02 ").click(function(){
        $(".sheshi02").append($(".dre:first ").clone());
    });
    $(".she_but ").click(function(){
        $(".sheshi").append($(".dre:first ").clone());
    });
    $(".family_but ").click(function () {
        var x=document.getElementById("x");
        var y=document.getElementById("y");
        $(x).parent().append($(".family_add_content:first").clone())
        $(y).parent().append($(".family_add_content02:first").clone())
    })
    $(".fu_family_add ").click(function () {
        var a=document.getElementById("a");
        var b=document.getElementById("b");
        $(b).parent().append($(".family_add_content03:first").clone())
        $(a).parent().append($(".family_add_content04:first").clone())
    })

 $(".dan_delect").click(function(){
     $(this).parent().parent().remove();

 })
    $(".right_content_list li").click(function(){
        $(".right_content_list li").removeClass("green_bi");
        $(this).addClass("green_bi");
    })
    $(".right_content_list22 a").click(function(){
        $(".right_content_list22 a").removeClass("green_bi");
        $(this).addClass("green_bi");
    })
/*右侧气泡弹出*/
    $(".search_tip_alert").hide()
    $(".icon-copytext").mousemove(function(){
        $(".search_tip_alert").show(100,"swing");
    })
    $(".icon-copytext").mouseleave(function(){
        $(".search_tip_alert").hide(100,"swing");
    })

    $(".search_tip_text").hide()
    $(".lf_tel_butto").mousemove(function(){
        $(".search_tip_text").show(100,"swing");
    })
    $(".lf_tel_butto").mouseleave(function(){
        $(".search_tip_text").hide(100,"swing");
    })
/*模板下载*/
    $(".search_tip_lond").hide()
    $(".d_lond_icon").mousemove(function(){
        $(".search_tip_lond").show(100,"swing");
    })
    $(".d_lond_icon").mouseleave(function(){
        $(".search_tip_lond").hide(100,"swing");
    })
/*
    $(".top_alert").hide()
    $(".icon-Star").mousemove(function(){
        $(".top_alert").show();
    })
    $(".icon-Star").mouseleave(function(){
        $(".top_alert").hide();
    })
*/


});

function RQaddcheck() {
    var phoneNumber = $("#phoneNumber").val();

    if($("#name").val().length ==0){
        alert("姓名必须输入。");
        return ;
    }

    var code = $("#code").val();
    if(code.length < 18 || isNaN(code.substring(code.length-2,code.length-1))){
        alert("身份证不正确。");
        return ;
    }

    var pattern = /^([0-9.]+)$/;
    if(phoneNumber.length<7 || !pattern.test(phoneNumber)){
        alert("手机号码不正确。");
        return;
    }

    if($("#domicile").val().length ==0){
        alert("户籍地必须输入。");
        return ;
    }

    if($("#dataSource").val().length ==0){
        alert("信息来源必须输入。");
        return ;
    }
    if(confirm("确认注册吗")) {
        document.getElementById("formadd").action = "/AMD/personnelInfo/add";
        document.getElementById("formadd").submit();
    }

}

function RQupdatecheck() {
    var phoneNumber = $("#phoneNumber").val();

    if($("#name").val().length ==0){
        alert("姓名必须输入。");
        return ;
    }

    var code = $("#code").val();
    if(code.length < 18 || isNaN(code.substring(code.length-2,code.length-1))){
        alert("身份证不正确。");
        return ;
    }

    if($("#dataSource").val().length ==0){
        alert("信息来源必须输入。");
        return ;
    }

    if($("#domicile").val().length ==0){
        alert("户籍地必须输入。");
        return ;
    }

    var pattern = /^([0-9.]+)$/;
    if(phoneNumber.length<7 || !pattern.test(phoneNumber)){
        alert("手机号码不正确。");
        return;
    } else {
        if(confirm("确认保存吗")) {
            document.getElementById("formupdate").action = "/AMD/personnelInfo/update";
            document.getElementById("formupdate").submit();
        }
    }
}
function RQdeletecheck(id){
    if(confirm("确认删除吗")){
        location="/AMD/personnelInfo/detail/delete/0/"+id;
    }
}

function RQadd() {
    location="/AMD/personnelInfo/add"
}

function RQdown() {
    location="/AMD/personnelInfo/detail/download"
}

function RQcancelcheck(){
    if(confirm("确认取消吗")){
        location="/AMD/personnelInfo/detail/seach?seachId=0&pageNo=1";
    }
}



/*用户名验证*/


