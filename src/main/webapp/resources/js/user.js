$(function () {
    alertify.set('notifier','position', 'top-center');
    $('.form_date').datetimepicker({
        language: 'zh-CN',
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0,
        startDate:new Date()
    });
})

function userSkip(type){
    var totalPage = $("#totalPage").text();
    var onPage = $("#num").val();
    if(onPage ==="" || onPage === 0 || parseInt(onPage) <=0){
        alertify.set('notifier','position', 'top-center');
        alertify.error("请输入你要跳转的页数！");
        return;
    }
    if(parseInt(onPage)>parseInt(totalPage)){
        $("#num").val(totalPage);
        return;
    } else {
        location="/SINOFAITH/"+type+"/seach?pageNo="+onPage;
    }
}

function zcpz(obj) {
    var userId = $(obj).closest("tr").find("input").val();
    var name = $(obj).closest("tr").find("td:eq(0)").text();
    var username = $(obj).closest("tr").find("td:eq(1)").text();
    $("#userId1").val(userId);
    $("#name1").val(name);
    $("#username1").val(username);

}

function zcpzSubmit() {
    var userId = $("#userId1").val();
    var loginTime = $("#loginTime").val();
    if(loginTime.trim().length==0){
        alertify.error("请选择试用时间!");
        return
    }

    $.ajax({
        url:"/SINOFAITH/user/zcpz",
        type:"POST",
        data:{
            userId:userId,
            loginTime:loginTime
        },
        success:function (msg) {
            if(msg==="200"){
                alertify.success("审批成功!");
                $('#myModal').modal('hide');
                setTimeout(function () {
                    document.getElementById("seachDetail").submit()
                }, 1000);
            }else{
                alertify.error("审批失败!");
            }
        }
    })
}

function destroyTooltip() {
    $(".txt").tooltip('destroy');
}

function checkUsername() {
    var flag = true;
    var username = $("#newuser").val();
    $.ajax({
        url:"/SINOFAITH/user/checkUsername",
        type:"POST",
        data:{
            username:username
        },
        success:function (msg) {
            if(msg===0){
            }else {
                $(".newuser").attr('title', "账号已存在").tooltip('show');
                flag = false;
            }
        }
    });
    return flag;
}

function addUser() {
    var username = $("#newuser").val();
    var password = $("#newpass").val();
    var name = $("#name").val();
    var role = $("#role").val();
    var zcpz = 1;
    var flag = true;
    if (username == '') {
        $(".newuser").attr('title', "账号不能为空").tooltip('show');
        flag = false;
    }
    if(!checkUsername()){
        $(".newuser").attr('title', "账号已存在").tooltip('show');
        flag = false;
        alert(checkUsername())
    }
    if (password == '') {
        $(".newpass").attr('title', "密码不能为空").tooltip('show');
        flag = false;
    }else if(password.length<6){
        $(".newpass").attr('title', "密码不能小于6位").tooltip('show');
        flag = false;
    }
    if(flag == false){
        return
    }
    $(".btn").attr("disabled", "true")
    var Controller = "/SINOFAITH/user/add"; // 接收后台地址
    // FormData 对象
    var form = new FormData();
    form.append("name", name); // 可以增加表单数据
    form.append("username", username); // 可以增加表单数据
    form.append("password", password); // 可以增加表单数据
    form.append("role", role); // 可以增加表单数据
    form.append("zcpz", zcpz); // 可以增加表单数据
    var xhr = new XMLHttpRequest();                // XMLHttpRequest 对象
    xhr.open("post", Controller, true);
    xhr.onload = function () {
        if (xhr.responseText == 200) {
            alertify.success("添加完成!");
            $('#myModal').modal('hide');
            setTimeout(function () {
                document.getElementById("seachDetail").submit()
            }, 1000);
        }
        if (xhr.responseText == 303) {
            $(".newuser").attr('title', "用户已存在").tooltip('show');
        }
        if (xhr.responseText == 404) {
            alertify.error("添加失败")
        }
        $(".btn").removeAttr("disabled", "disabled");
    };
    xhr.send(form);
}