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


    <link href="<c:url value="/resources/thirdparty/assets/css/bootstrap.min.css"/>" rel="stylesheet" />
    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/font-awesome.min.css"/>" />


    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/ace.min.css"/>" />
    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/ace-rtl.min.css"/>" />



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
                                    </h4>

                                    <div class="space-6"></div>

                                    <form id="loginform" action="login" method="post">
                                        <fieldset>
                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" name="username" class="form-control" placeholder="用户名" value="${username}"/>
															<i class="icon-user"></i>
														</span>
                                            </label>

                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input id="passwordInput" type="password" name="password" class="form-control" placeholder="密码" />
															<i class="icon-lock"></i>
														</span>
                                            </label>

                                            <div class="space"></div>

                                            <div class="clearfix">
                                                <span style="color:red;font-weight: bold;">${result}</span>

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

<%--<embed width="100%" height="100%" name="plugin" id="plugin" src="http://localhost:8080/file/安徽5.18特大销售假药案简要案件及分省线索（1）.pdf" type="application/pdf" internalinstanceid="14">--%>

<!-- basic scripts -->

<!--[if !IE]> -->

<script type="text/javascript">
    window.jQuery || document.write("<script src='assets/js/jquery-2.0.3.min.js'>"+"<"+"/script>")
</script>

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

    function login(){
        $("#loginform").submit();
    }

</script>
</body>
</html>
