function ajSkip(){
    var totalPage = $("#totalPage").text();
    var onPage = $("#num").val();
    if(onPage ==="" || onPage === 0 || parseInt(onPage) <=0){
        alertify.error("请输入你要跳转的页数！");
        return;
    }
    if(parseInt(onPage)>parseInt(totalPage)){
        $("#num").val(totalPage);
        return;
    } else {
        location="/SINOFAITH/aj/seach?pageNo="+onPage;
    }
}

function editGrand() {
    var ajid = $("#ajid").val();
    var ul = $("#grand");
    var listUserId = [];
    ul.each(function () {
        $(this).find('li').each(function() {
            listUserId.push($(this).attr("value"));
        });
    });
    $.ajax({
        url: "/SINOFAITH/aj/grandAj",
        type: 'post',
        data:{
            ajid:ajid,
            listUserId:listUserId
        },
        traditional: true,

        success: function(result) {
            alertify.success("授权成功!");
            $('#myModal2').modal('hide');
        }
    })

}

function getUser(id,ajm,username) {
    $("#ajm").text(ajm);
    $("#ajid").val(id);
    var ul_noGrand = $("#noGrand");
    var ul_grand = $("#grand");
    var temp = new Array();
    if(ul_grand!=null){
        ul_grand.html("");
    }
    if(ul_noGrand!=null){
        ul_noGrand.html("");
    }

    $.ajax({
        url: "/SINOFAITH/user/getGrandUser",
        type: 'post',
        dataType: 'json',
        data:{
          ajid:id
        },
        success: function(result) {
            for(var j = 0;j<result.grand.length;j++){
                var li = $("<li class=\"list-group-item\" onselectstart=\"return false\" value="+result.grand[j].id+">"+result.grand[j].username+"("+result.grand[j].name+"</li>");
                temp.push(result.grand[j].username);
                li.click(function(){
                    if($(this).attr('class').indexOf("selected")>1){
                        $(this).removeClass("selected");
                    }else{
                        $(this).addClass("selected").siblings().removeClass("selected");
                    }
                });

                li.dblclick(function () {
                    if($(this).parent().attr("id")=="grand"){
                        $(this).prependTo($("#noGrand"));
                    }else{
                        $(this).prependTo($("#grand"));
                    }
                    $(this).addClass("selected").siblings().removeClass("selected");
                })
                li.appendTo(ul_grand);
            }
            for(var i=0;i<result.all.length;i++){
                var li = $("<li class=\"list-group-item\" onselectstart=\"return false\" value="+result.all[i].id+">"+result.all[i].username+"("+result.all[i].name+")</li>");
                li.click(function(){
                    if($(this).attr('class').indexOf("selected")>1){
                        $(this).removeClass("selected");
                    }else{
                        $(this).addClass("selected").siblings().removeClass("selected");
                    }
                });

                li.dblclick(function () {
                    if($(this).parent().attr("id")=="grand"){
                        $(this).prependTo($("#noGrand"));
                    }else{
                        $(this).prependTo($("#grand"));
                    }
                    $(this).addClass("selected").siblings().removeClass("selected");
                })
                if($.inArray( result.all[i].username, temp)==-1&&result.all[i].username!=username){
                    li.appendTo(ul_noGrand);
                }
            }
        }
    });
}

function addAj() {
    var aj = $("#aj").val();
    if(aj==''){
        $(".txt").attr('title',"案件名不能为空").tooltip('show');
        return
    }
    $(".btn").attr("disabled","true")
    var Controller = "/SINOFAITH/aj/add"; // 接收后台地址
    // FormData 对象
    var form = new FormData();
    form.append("aj", aj); // 可以增加表单数据
    var xhr = new XMLHttpRequest();                // XMLHttpRequest 对象
    xhr.open("post", Controller, true);
    xhr.onload = function() {
        if(xhr.responseText==200){
            alertify.success("添加完成!");
            $('#myModal').modal('hide');
            setTimeout(function () {document.getElementById("seachDetail").submit()},1000);
        }
        if(xhr.responseText==303){
            $(".txt").attr('title',"案件名重复").tooltip('show');
        }
        if(xhr.responseText==404){
            alertify.error("添加失败")
        }
        $(".btn").removeAttr("disabled","disabled");
    };
    xhr.send(form);
}

$(function () {
    alertify.set('notifier','position', 'top-center');
    $(".delete").click(function () {
        var label = $(this).next(":hidden").val();
        var flag = confirm("确定删除 "+label+" ?");
        if(flag){
            var urla = $(this).attr("href");
            $("#_form").ajaxSubmit({
                url:urla,
                type:"GET",
                beforeSend: function () {
                    $('.delete').css('disabled','true')
                    setTimeout(function () {document.getElementById("seachDetail").submit()},2000);
                },
                success:function (data) {
                    if(data==303){
                        alertify.success("请先删除包含此案件的并案案件")
                    }
                },
                error:function (e) {
                  alertify.error("错误")
                }
            })
        }
        return false;
    });
})

function destroyTooltip() {
    $(".txt").tooltip('destroy');
}

function ajsCount() {
    obj = document.getElementsByName("ajval");
    check_val =[];
    for(k in obj ){
        if(obj[k].checked)
            check_val.push(obj[k].value);
    }
    if(check_val.length>1){
        alertify.set('notifier','delay', 0);
        alertify.success("数据分析中,请等待跳转...");
        $.get("/SINOFAITH/aj/ajsCount?ajm="+check_val,function (data) {
            if(data==200){
                alertify.success("分析完成..正在跳转..");
                setTimeout(function (){
                    window.location="/SINOFAITH/aj/ajm?aj="+check_val+"&type=1";
                },1500);
            }
            if(data==303){
                alertify.set('notifier','delay', 0);
                alertify.error("多案件分析结果已存在,正在跳转..");
                setTimeout(function (){
                    window.location="/SINOFAITH/aj/ajm?aj="+check_val+"&type=1";
                },1500);
            }
        })
    }else{
        alertify.error("请至少选择两个案件");
    }
}

function selectAll(){
    var isCheck=$("#sel_1").is(':checked');  //获得全选复选框是否选中
    $("input[name='deleteAj']").each(function() {
        this.checked = isCheck;       //循环赋值给每个复选框是否选中
    });
}

function deleteAj(ajm) {
    $("#aj1").attr("value", ajm);
}

function deleteAjByFilter() {
    var ajm = $("#aj1").val();
    var obj = document.getElementsByName("deleteAj");
    var check_val = [];
    var check_text = [];
    for(var k in obj){
        if(obj[k].checked) {
            check_val.push(obj[k].value);
            check_text.push(obj[k].nextSibling.nodeValue);
        }
    }
    if(check_val.length>0){
        alertify.confirm("确认删除 \""+ajm+"\" 内 \""+check_text+"\" 数据？", function () {
            // 用户点击"ok"按钮
            alertify.set('notifier','delay', 0);
            alertify.success("删除中")
            $.ajax({
                type:"post",
                dataType:"json",
                url:"/SINOFAITH/aj/delete",
                data:{
                    ajm:ajm,
                    list:check_val.toString()
                },
                success:function (data) {
                    if(data==303){
                        alertify.success("请先删除包含此案件的并案案件")
                        return
                    }
                    setTimeout(function () {document.getElementById("seachDetail").submit()},1000);
                    if(data==200){
                        alertify.success("删除成功")
                    }
                },
                error:function (e) {
                    alertify.error("错误")}
            })
        }, function() {
            // 用户点击"cancel"按钮
            return
        });
    }else{
        alertify.error("请选择要删除的数据")
    }
}
