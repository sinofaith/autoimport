function ajSkip(){
    var totalPage = $("#totalPage").text();
    var onPage = $("#num").val();
    if(onPage ==="" || onPage === 0 || parseInt(onPage) <=0){
        alert("请输入你要跳转的页数！");
        return;
    }
    if(parseInt(onPage)>parseInt(totalPage)){
        $("#num").val(totalPage);
        return;
    } else {
        location="/SINOFAITH/aj/seach?pageNo="+onPage;
    }
}

function addAj() {

    var aj = $("#aj").val();
    if(aj==''){
        alertify.alert('请填写案件名称')
        return
    }
    var Controller = "/SINOFAITH/aj/add"; // 接收上传文件的后台地址
    // FormData 对象
    var form = new FormData();
    form.append("aj", aj); // 可以增加表单数据
    var xhr = new XMLHttpRequest();                // XMLHttpRequest 对象
    xhr.open("post", Controller, true);
    xhr.onload = function() {
        if(xhr.responseText==200){
            alertify.alert("添加完成!");
            $('#myModal').modal('hide');
            setTimeout(function () {document.getElementById("seachDetail").submit()},1000);
        }
        if(xhr.responseText==303){
            alertify.alert("案件名已存在")
        }
        if(xhr.responseText==404){
            alertify.alert("添加失败")
        }
    };
    xhr.send(form);
}

$(function () {
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
                        alertify.alert("请先删除包含此案件的并案案件")
                    }
                },
                error:function (e) {
                  alertify.alert("错误")
                }
            })
        }
        return false;
    })
})

function ajsCount() {
    obj = document.getElementsByName("ajval");
    check_val =[];
    for(k in obj ){
        if(obj[k].checked)
            check_val.push(obj[k].value);
    }
    if(check_val.length>1){
        alertify.alert("数据分析中,请等待跳转...");
        $.get("/SINOFAITH/aj/ajsCount?ajm="+check_val,function (data) {
            if(data==200){
                alertify.alert("分析完成..正在跳转..");
                setTimeout(function (){
                    window.location="/SINOFAITH/aj/ajm?aj="+check_val;
                },1500);
            }
            if(data==303){
                alertify.alert("多案件分析结果已存在,正在跳转..");
                setTimeout(function (){
                    window.location="/SINOFAITH/aj/ajm?aj="+check_val;
                },1500);
            }
        })
    }else{
        alertify.alert("请至少选择两个案件");
    }
}


