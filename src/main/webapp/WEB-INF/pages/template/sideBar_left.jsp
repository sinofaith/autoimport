<%--
  Created by IntelliJ IDEA.
  User: 95645
  Date: 2016/11/30
  Time: 13:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="<c:url value="/resources/js/jquery-1.9.1.min.js"/> "></script>
<script src="<c:url value="/resources/jquery/jquery.js"/> "></script>
<html>
<%--新加样式--%>
<head>
    <title>电子数据综合分析系统</title>
    <style type="text/css">
        .icon {
            width: 32px; height: 32px;
            fill: currentColor;
        }
    </style>
</head>
<body>
<aside class="sideMenu_left" style="top: 0px;">
    <a class="logo" ><img src="<c:url value="/resources/img/logo02.png"/>"style="padding:20px 0 20px 10px; width: 63px ;float: left"/> </a>
 <span>
    <a href="/SINOFAITH/aj" >
        <svg class="icon">
            <use xlink:href="#icon-xiangqing"></use>
        </svg><br>案件</a>
     <a href="/SINOFAITH/customerPro?aj=" class="sidebar_left_addative">
        <svg class="icon">
            <use xlink:href="#icon-renyuanxinxi"></use>
        </svg><br>人员信息</a>
    <a href="/SINOFAITH/bank">
        <svg class="icon">
            <use xlink:href="#icon-icon"></use>
        </svg><br>银行卡</a>
    <a href="/SINOFAITH/cft">
        <svg class="icon">
            <use xlink:href="#icon-caifutong1"></use>
        </svg><br>财付通</a>
    <a href="/SINOFAITH/zfb">
        <svg class="icon">
            <use xlink:href="#icon-zhifubao"></use>
        </svg><br>支付宝</a>
    <a href="/SINOFAITH/wuliu">
        <svg class="icon">
            <use xlink:href="#icon-wuliu"></use>
        </svg><br>物流</a>
    <%--<a href="/SINOFAITH/pyramidSale">--%>
        <%--<svg class="icon">--%>
            <%--<use xlink:href="#icon-cengji"></use>--%>
        <%--</svg><br>传销</a>--%>
     <c:if test="${user.role==0}">
     <a href="/SINOFAITH/user">
        <svg class="icon">
            <use xlink:href="#icon-yonghuguanli"></use>
        </svg><br>用户管理</a>
     </c:if>
 </span>
</aside>
</body>
</html>
