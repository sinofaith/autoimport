<%--
  Created by IntelliJ IDEA.
  User: guibin
  Date: 15/1/22
  Time: 下午5:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta charset="utf-8" />
    <title>电子数据综合分析系统</title>

    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <script src="<c:url value="/resources/thirdparty/alertify/js/alertify.min.js"/> "></script>
    <link href="<c:url value="/resources/thirdparty/alertify/css/alertify.min.css"/> " rel="stylesheet">
    <link href="<c:url value="/resources/thirdparty/alertify/css/default.min.css"/> " rel="stylesheet">
    <link href="<c:url value="/resources/css/widgets.css"/>" rel="stylesheet">

    <link href="<c:url value="/resources/thirdparty/assets/css/bootstrap.min.css"/>" rel="stylesheet" />
    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/font-awesome.min.css"/>" />

    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/ace.min.css"/>" />
    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/ace-rtl.min.css"/>" />
    <script src="<c:url value="/resources/js/jquery-1.9.1.min.js"/> "></script>

    <script src="<c:url value="/resources/jquery/jquery.js"/> "></script>
    <script src="<c:url value="/resources/js/bootstrap.js"/> "></script>


</head>


<body class="login-layout">
<div class="main-container">
    <div class="main-content">
        <div class="row">
            <div class="col-sm-10 col-sm-offset-1">
                <div class="login-container">

                    <br><br><br><br><br><br>
                    <div class="space-6"></div>

                    <div class="position-relative">
                        <div id="login-box" class="login-box visible widget-box">
                            <div class="widget-body">
                                <div class="widget-main">
                                    <h4 class="header blue lighter bigger icon_login">

                                        <b>电子数据综合分析系统</b>
                                        <%--<span class="fl_r font_075 padd_r_30" >--%>
                                            <%--<a style="color:  #6698ce;font-size: 16px;padding-left: 139px;" target="_blank"--%>
                                               <%--href="<c:url value="/resources/ali_icon/电子数据综合分析系统操作手册.pdf"/>">操作手册</a>--%>
                                        <%--</span>--%>
                                    </h4>

                                    <div class="space-6"></div>

                                    <form id="loginform" action="login" method="post">
                                        <fieldset>
                                            <label class="block clearfix" >
														<span class="block input-icon input-icon-right">
															<input style="height: 35px; font-size: 16px" type="text" name="username" oninput="removeResult()"  id="username" class="form-control" placeholder="用户名" value="${username}"/>
															<i class="icon-user"></i>
														</span>
                                            </label>

                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input style="height: 35px; font-size: 16px" id="passwordInput" type="password" name="password" class="form-control" placeholder="密码" />
															<i class="icon-lock"></i>
														</span>
                                            </label>

                                            <div class="space"></div>

                                            <div class="clearfix">
                                                <span style="color:red;font-weight: bold;" id="result">${result}</span>
                                                <br>
                                                <%--<button type="button" class="width-10 pull-left btn btn-sm btn-info yuanjiao5"  data-toggle="modal" data-target="#myModal">--%>
                                                    <%--试用申请--%>
                                                <%--</button>--%>
                                                <button type="button" class="width-35 pull-right btn btn-sm btn-primary yuanjiao5" onclick="login()">
                                                    <i class="icon-key"></i>
                                                    登录
                                                </button>
                                            </div>

                                            <div class="space-4"></div>
                                        </fieldset>
                                    </form>


                                </div><!-- /widget-main -->

                                <div class="toolbar clearfix">
                                    <!-- 忘记密码
                                    <div>
                                        <a href="#" onclick="show_box('forgot-box'); return false;" class="forgot-password-link">
                                            <i class="icon-arrow-left"></i>
                                            忘记密码
                                        </a>
                                    </div>
                                     -->

                                </div>
                            </div><!-- /widget-body -->
                        </div><!-- /login-box -->


                    </div><!-- /position-relative -->
                </div>
            </div><!-- /.col -->
        </div><!-- /.row -->
    </div>
</div><!-- /.main-container -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 25%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h4 class="modal-title" id="myModalLabel">注册<span id="title"></span></h4>
            </div>
            <div class="modal-body" style="padding-left: 23%;">
                姓名:<br><input type="text" autocomplete="new-username" name = 'name' id ='name'
                          class='txt'  data-toggle="tooltip" data-placement="top" oninput="destroyTooltip()" ><br>
                账号:<br><input type="text" onKeyUp="value=value.replace(/[^\w\.\/]/ig,'')" autocomplete="new-username" name = 'newuser' id ='newuser'
                          class='txt newuser'  data-toggle="tooltip" data-placement="top" oninput="destroyTooltip()" onblur="checkUsername()"><br>
                密码:<br><input type="password" onKeyUp="value=value.replace(/[^\w\.\/]/ig,'')" autocomplete="new-password" name = 'newpass' id ='newpass'
                          class='txt newpass'  data-toggle="tooltip" data-placement="top" oninput="destroyTooltip()" ><br>
                重复密码:<br><input type="password" onKeyUp="value=value.replace(/[^\w\.\/]/ig,'')" autocomplete="new-password" name = 'newpass' id ='renewpass'
                            class='txt renewpass'  data-toggle="tooltip" data-placement="top" oninput="destroyTooltip()" >
                <input type="hidden" id="role" value="2">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default btn-sm btn-primary yuanjiao5" onclick="addUser()">提交</button>
                <button type="button" class="btn btn-default btn-sm btn-primary yuanjiao5" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>

<!-- basic scripts -->

<!--[if !IE]> -->

<!-- <![endif]-->

<!--[if IE]-->
<script type="text/javascript">
    window.jQuery || document.write("<script src='<c:url value="/resources/thirdparty/assets/js/jquery-1.10.2.min.js"/>'>"+"<"+"/script>")
</script>
<!--[endif]-->

<!--[if !IE]> -->

<script type="text/javascript">
    window.jQuery || document.write("<script src='<c:url value="/resources/thirdparty/assets/js/jquery-2.0.3.min.js"/>'>"+"<"+"/script>")
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
</script>
<![endif]-->

<script type="text/javascript">
    if("ontouchend" in document) document.write("<script src='<c:url value="/resources/thirdparty/assets/js/jquery.mobile.custom.min.js"/>'>"+"<"+"/script>")
</script>

<!-- inline scripts related to this page -->

<script type="text/javascript">
    function show_box(id) {
        jQuery('.widget-box.visible').removeClass('visible')
        jQuery('#'+id).addClass('visible')
    }
</script>
<script type="text/javascript">
    $(function(){
        $("#logout").hide();

        $('#passwordInput').bind('keypress', function(event) {
            if(event.keyCode == '13') {
                login();
            }
        });
    });

    function removeResult() {
        $("#result").text("");
    }

    function login(){
        $("#loginform").submit();
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
        var repassword = $("#renewpass").val();
        var name = $("#name").val();
        var role = $("#role").val();
        var zcpz = 0;
        var flag = true;
        if (username == '') {
            $(".newuser").attr('title', "账号不能为空").tooltip('show');
            flag = false;
        }
        if(!checkUsername()){
            $(".newuser").attr('title', "账号已存在").tooltip('show');
            flag = false;
        }
        if (password == '') {
            $(".newpass").attr('title', "密码不能为空").tooltip('show');
            flag = false;
        }else if(password.length<6){
            $(".newpass").attr('title', "密码不能小于6位").tooltip('show');
            flag = false;
        }else if(password!=repassword){
            $(".renewpass").attr('title',"两次密码不一致").tooltip('show');
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
                alertify.set('notifier','position', 'top-center');
                alertify.success("申请成功!请联系管理员批准登陆");
                $("#username").val(username);
                $("#passwordInput").val("");
                $('#myModal').modal('hide');
            }
            if (xhr.responseText == 303) {
                $(".newuser").attr('title', "用户已存在").tooltip('show');
            }
            if (xhr.responseText == 404) {
                alertify.error("申请失败")
            }
            $(".btn").removeAttr("disabled", "disabled");
        };
        xhr.send(form);
    };

    $('#myModal').on('hide.bs.modal', function () {
        // 执行一些动作...
        $("#newuser").val("");
        $("#newpass").val("");
        $("#name").val("");
    });
</script>
</body>
</html>
