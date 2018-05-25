$(document).ready(function() {

    $('#bsfs').bind('change',function(){
        setxsmc($("#bsfs").val());
    });
    $(".sfbsdfhc :radio").click(function(){
        setsfbsdfhc($('.sfbsdfhc input:radio:checked').val());
    });
    $(".sfzk :radio").click(function(){
        setsfzk($('.sfzk input:radio:checked').val());
    });
    setxsmc($("#bsfs").val());
    setsfbsdfhc($('#sfbsdfhc').val());
    setsfzk($('#sfzk').val());

    $("#hiddenPhoto").click(function(){
        $("#photo").hide();
    });
    $("#displayPhoto").click(function(){
        $("#photo").show();
    });

    $('#changePhoto').click(function() {
        $("input[type='file']").click();
    });
    $("#crimeterraceform>input[type='file']").change(function(event) {
        var photo = $("#photo").val()
        var extStart=photo.lastIndexOf(".");
        var ext=photo.substring(extStart,photo.length).toUpperCase();
        if(ext!=".BMP"&&ext!=".PNG"&&ext!=".GIF"&&ext!=".JPG"&&ext!=".JPEG"){
            alert("图片限于bmp,png,gif,jpeg,jpg格式");
        }

    });

    $("#morewd").click(function(){
        $("#morewd").hide();
        $("#hidewd").show();
        $(".wdlist").show();
    });

    $("#hidewd").click(function(){
        $("#morewd").show();
        $("#hidewd").hide();
        $(".wdlist").hide();
        $(".Appoint").hide();
    });

    $(".wdlist li .AppointWd").click(function(){
        var sf = $(this).find("input").attr("value");
        var path = "/AMD/crimeterrace/seach?sf="+sf;
        $.ajax({
            url: path,
            type: 'GET',
            dataType: 'json',
            success: function(result) {
                $(".Appoint li").remove();
                $(".Appoint").show();
                $.each(result,function(i, item) {
                    $('.Appoint').append("<li><input type='checkbox' name='Appointwd' value='"+item.crimegroupinfoid+"'>"+item.wdmc+"</li>");

                });
                var wd_list = $(".wdinfo li a input");
                for(var i=0;i<wd_list.length;i++){
                    $(".Appoint li").find("input[value='"+$(wd_list[i]).attr('value')+"']").attr("checked",true);
                }

                $(".Appoint li input").bind({'click':function(){
                    var that = this;
                    var file = $(this).parent().text().trim();

                    var crimegroupinfoid = $(that).val();
                    var title = $(that).parent().parent().text();
                    if($(that).is(':checked')) {
                        $('.wdinfo').append("<li title ="+file+"><p>"+file+"</p> <a href='javascript:void(0)'><input  hidden='hidden' name='sswd' value="+crimegroupinfoid+"><img width='16px' class='delete' title='删除' style='cursor:pointer;' src='/AMD/resources/thirdparty/assets/images/images/delete.png'></a></li>");
                        bindListener();
                    }  else {
                        console.log("jjuuuuuu", $('.wdinfo').find("li[title='"+file+"']"));
                        $('.wdinfo').find("li[title='"+file+"']").remove();
                    }
                }});
            }});
    });

    function bindListener(){
        $(".wdinfo li a").unbind().click(function(){
            var that = this;
            var tit = $(that).val();
            var id = $(that).find("input").attr("value");
            $(".Appoint li").find("input[value='"+id+"']").attr("checked",false);
            $(this).parent().remove();
        });
    }

    $(".wdinfo li .delete").click(function(){
        var that = this;
        $(that).parent().parent().remove();
    });

});

function setsfzk(sfzk){
    if("是"===sfzk){
        $("#zkdw").removeAttr("readonly");
    }else {
        $("#zkdw").val("");
        $("#zkdw").attr("readonly","readonly");
    }
}
function setsfbsdfhc(sfbsdfhc){
    if("是"===sfbsdfhc){
        $("#hcdw").removeAttr("readonly");
        $("#dflxr").removeAttr("readonly");
        $("#lxfs").removeAttr("readonly");
    } else {
        $("#hcdw").val("");
        $("#dflxr").val("");
        $("#lxfs").val("");
        $("#hcdw").attr("readonly","readonly");
        $("#dflxr").attr("readonly","readonly");
        $("#lxfs").attr("readonly","readonly");
    }
}

function setxsmc(bsfs){
    if("集群战役" ===bsfs){
        $("#xsmc").html("集群战役名称");
        $("#xsbh").html("集群战役编号");
    }else if("核查通知" === bsfs){
        $("#xsmc").html("核查通知名称");
        $("#xsbh").html("核查通知编号");
    }else if("地方自侦" ===bsfs ){
        $("#xsmc").html("地方自侦名称");
        $("#xsbh").html("地方自侦编号");
    } else {
        $("#xsmc").html("线索名称");
        $("#xsbh").html("线索编号");
    }
}
