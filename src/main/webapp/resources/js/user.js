function userSkip(){
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
        location="/SINOFAITH/user/seach?pageNo="+onPage;
    }
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
    })
    return flag;
}

function addUser() {
    var username = $("#newuser").val();
    var password = $("#newpass").val();
    var name = $("#name").val();
    var role = $("#role").val();
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