<%--
  Created by IntelliJ IDEA.
  User: guibin
  Date: 14-7-29
  Time: 下午4:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>电子数据信息应用系统</title>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/main.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/css/sidebar.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/css/widgets.css"/>" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/resources/thirdparty/jquery/jquery-1.7.2.min.js"/>"></script>
    <link href="<c:url value="/resources/thirdparty/datetimepicker/jquery.datetimepicker.css"/> " rel="stylesheet">
    <script src="<c:url value="/resources/thirdparty/datetimepicker/jquery.datetimepicker.js"/> " type="text/javascript"></script>
    <link href="<c:url value="/resources/thirdparty/jquery-loadmask/jquery.loadmask.css"/> " rel="stylesheet">
    <script src="<c:url value="/resources/thirdparty/jquery-loadmask/jquery.loadmask.min.js"/> " type="text/javascript"></script>
    <script src="<c:url value="/resources/thirdparty/jquery-cookie/jquery.cookie.js"/> "></script>
    <script type="text/javascript">
        function s(){
            $("#onekeyqueryform").submit();
        }
    </script>
    <script src="<c:url value="/resources/thirdparty/alertify/js/alertify.min.js"/> " ></script>
    <link href="<c:url value="/resources/thirdparty/alertify/css/alertify.min.css"/> " rel="stylesheet">
    <link href="<c:url value="/resources/thirdparty/alertify/css/default.min.css"/> " rel="stylesheet">
    <script src="<c:url value="/resources/js/main.js"/> "></script>
</head>
<body>
    <div id="topbar">
        <ul>
            <form id="onekeyqueryform" method="get" action="/AMD/onekeyquery">
                <a href="/AMD/logout"><li id="logout">退出</li></a>
                <a href="/AMD/system"><li id="b_xtgl">系统管理</li></a>
                <a href="/AMD/conjugate"><li id="b_bsfx">亲密度分析</li></a>
                <a href="/AMD/relation"><li id="b_gxqfx">关系圈分析</li></a>
                <a href="/AMD/extend"><li id="b_kzfx">扩展分析</li></a>
                <a href="/AMD/BasicQuery"><li id="b_jccx">基础查询</li></a>
                <img id = 'logo'src="<c:url value="/resources/img/banner.png"/>" />
                <input id="querycondition" name="querycondition" type="text"/>
                <img id="btnMagnifier" src="<c:url value="/resources/img/magnifier.png" />" onclick="s()"/>

            </form>
        </ul>
    </div>
    <div id="content">

