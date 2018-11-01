<%--
  Created by IntelliJ IDEA.
  User: guibin
  Date: 15/1/15
  Time: 下午6:06
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta charset="utf-8"/>
    <title>SINOFAITH</title>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/main.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/css/sidebar.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/css/widgets.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/thirdparty/sweetalert/dist/sweetalert.css"/>" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/resources/thirdparty/underscore/underscore.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/thirdparty/jquery/jquery-1.7.2.min.js"/>"></script>
    <link href="<c:url value="/resources/thirdparty/datetimepicker/jquery.datetimepicker.css"/> " rel="stylesheet">
    <script src="<c:url value="/resources/thirdparty/datetimepicker/jquery.datetimepicker.js"/> "
            type="text/javascript"></script>
    <link href="<c:url value="/resources/thirdparty/jquery-loadmask/jquery.loadmask.css"/> " rel="stylesheet">
    <script src="<c:url value="/resources/thirdparty/jquery-loadmask/jquery.loadmask.min.js"/> "
            type="text/javascript"></script>
    <script src="<c:url value="/resources/thirdparty/jquery-cookie/jquery.cookie.js"/> "
            type="text/javascript"></script>
    <script src="<c:url value="/resources/thirdparty/sweetalert/dist/sweetalert.min.js"/>"
            type="text/javascript"></script>

    <%--<script type="text/javascript">--%>
    <%--jQuery(function($) {--%>

    <%--$('#nav-enable').click(function () {--%>
    <%--alertify.prompt('请输入口令','',function(evt,value) {--%>
    <%--if (value == "admin") {--%>

    <%--if($('#nav-search').css('display')=='none')--%>
    <%--{--%>
    <%--$('#nav-search').show();--%>
    <%--$('#basicquery').attr('href','/AMD/BasicQuery');--%>
    <%--}--%>
    <%--else{--%>
    <%--$('#nav-search').hide();--%>
    <%--$('#basicquery').attr('href','http://10.15.69.15/beta/search/index.php?username=${USERINFO.username}&passwd=${passwd}');--%>

    <%--}--%>
    <%--$.ajax('/AMD/system/model');--%>

    <%--} else {--%>
    <%--alertify.alert("口令错误");--%>
    <%--}--%>
    <%--});--%>
    <%--});--%>


    <%--});--%>


    <%--</script>--%>
    <script src="<c:url value="/resources/thirdparty/alertify/js/alertify.min.js"/> "></script>
    <link href="<c:url value="/resources/thirdparty/alertify/css/alertify.min.css"/> " rel="stylesheet">
    <link href="<c:url value="/resources/thirdparty/alertify/css/default.min.css"/> " rel="stylesheet">
    <script src="<c:url value="/resources/js/main.js"/> "></script>

    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <!-- basic styles -->

    <link href="<c:url value="/resources/thirdparty/assets/css/bootstrap.min.css" />" rel="stylesheet"/>
    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/font-awesome.min.css"/>"/>


    <!-- page specific plugin styles -->

    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/jquery-ui-1.10.3.full.min.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/datepicker.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/ui.jqgrid.css"/>"/>


    <!-- ace styles -->

    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/ace.min.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/ace-rtl.min.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/ace-skins.min.css"/>"/>

    <!-- basic scripts -->
    <%--新加样式--%>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/style/style.css"/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/style/css.css"/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value="/resources/style/default.css"/>"/>


    <%--<script src="<c:url value="/resources/thirdparty/assets/js/bootstrap.min.js"/>"></script>--%>
    <script src="<c:url value="/resources/thirdparty/assets/js/typeahead-bs2.min.js"/>"></script>

    <!-- page specific plugin scripts -->

    <script src="<c:url value="/resources/thirdparty/assets/js/date-time/bootstrap-datepicker.min.js"/>"></script>
    <script src="<c:url value="/resources/thirdparty/assets/js/jqGrid/jquery.jqGrid.min.js"/>"></script>
    <script src="<c:url value="/resources/thirdparty/assets/js/jqGrid/i18n/grid.locale-en.js"/>"></script>


    <script src="<c:url value="/resources/thirdparty/assets/js/jquery-ui-1.10.3.full.min.js"/>"></script>
    <script src="<c:url value="/resources/thirdparty/assets/js/jquery.ui.touch-punch.min.js"/>"></script>

    <!-- ace scripts -->

    <script src="<c:url value="/resources/thirdparty/assets/js/ace-elements.min.js"/>"></script>
    <script src="<c:url value="/resources/thirdparty/assets/js/ace.min.js"/>"></script>

    <style>
        ul li a:hover {
            background-color: #636B75;
        }
    </style>
</head>

<body style="height: 100%;"><!-- #BeginLibraryItem "/Library/top.lbi" -->
<header id="navbar">
    <%--<a class="logo"><img src="<c:url value="/resources/img/logo02.png"/>"style=" width: 63px ;float: left"/></a>--%>

    <span  class="fl_l padd_l_100  font15 color_black" style="margin-left: 75px;">
          数据清洗展示模型
        </span>
    <!-- /.navbar-header -->
    <%--<div class="top_nav" style="width: 100%;">
        <ul class="top_Navigate">


            &lt;%&ndash;<c:choose>&ndash;%&gt;
            &lt;%&ndash;<c:when test="${model==1}">&ndash;%&gt;
            &lt;%&ndash;<li><a href='/AMD/BasicQuery' target="_blank" id='basicquery'>基础查询</a></li>&ndash;%&gt;
            &lt;%&ndash;</c:when>&ndash;%&gt;
            &lt;%&ndash;<c:otherwise>&ndash;%&gt;
            &lt;%&ndash;<li><a href='http://10.15.69.15/beta/search/index.php?username=${USERINFO.username}&passwd=${passwd}' target="_blank" id='basicquery'>基础查询</a></li>&ndash;%&gt;
            &lt;%&ndash;</c:otherwise>&ndash;%&gt;
            &lt;%&ndash;</c:choose>&ndash;%&gt;
                <li class="crimeterrace"><a href="/AMD/crimeterrace">人员信息</a></li>
                <li class="crimegroupinfo"><a href="/AMD/crimegroupinfo">窝点信息</a></li>
                <li class="crimeMap"><a href="/AMD/crimeMap">地图显示</a></li>
                <li class="criteriaquery"><a href="/AMD/criteriaquery">查询比对</a></li>
                <li class="fileupload"><a href="/AMD/fileuploading">文件上传</a></li>
            &lt;%&ndash;<li><a href="/AMD/personnelInfo">通讯信息分析</a></li>&ndash;%&gt;
            &lt;%&ndash;<li><a href='/AMD/BasicQuery' id='basicquery'>基础查询</a></li>&ndash;%&gt;
            &lt;%&ndash;<li><a href="/AMD/extend">可视化分析</a></li>&ndash;%&gt;
            &lt;%&ndash;<li><a href="/AMD/relation">综合分析</a></li>&ndash;%&gt;
            &lt;%&ndash;<li><a href="/AMD/system">应用管理</a></li>&ndash;%&gt;
            &lt;%&ndash;<li><a href="/AMD/analysis">主动研判</a></li>&ndash;%&gt;
            &lt;%&ndash;<li style="position: relative">&ndash;%&gt;
                &lt;%&ndash;<div id="red" style="background-color:#ff2f17 ;height: 23px;width: 23px;position: absolute;&ndash;%&gt;
            &lt;%&ndash;border-radius: 10px;right: -3px;top: -3px;display: none"></div>&ndash;%&gt;
                &lt;%&ndash;<a href="/AMD/push">推送</a></li>&ndash;%&gt;

        </ul>
    </div>--%>
        <span class="fl_r font_075 padd_r_30">
            <%--<span>警员：${USERINFO.name}</span>，您已登录，<a id="btnLogout" href="/AMD/logout" class="color_black">退出系统</a>--%>
        </span>


    <div class="nav-search" style="width: 300px">
        <%--<a  id="nav-enable" style="display:block ;float: right" title="模式切换"><img src="<c:url value="/resources/img/enable.png"/>" width="35" height="30"></a>--%>

        <%--<c:choose>--%>
        <%--<c:when test="${model==1}">--%>
        <%--<form class="form-search" action="/AMD/onekeyquery" id="nav-search" style="margin-right: 10px;float: right">--%>
        <%--</c:when>--%>
        <%--<c:otherwise>--%>
        <%--<form class="form-search" action="/AMD/onekeyquery" id="nav-search" style="display: none" >--%>
        <%--</c:otherwise>--%>
        <%--</c:choose>--%>
        <form class="form-search" action="/AMD/onekeyquery" id="nav-search" style="margin-right: 10px;float: right">
             	<span class="input-icon">
			    	<%--<input type="text" placeholder="请输入文本查询条件" class="nav-search-input" name="querycondition"--%>
                           <%--id="nav-search-input" autocomplete="off"/>--%>
					<%--<i class="icon-search nav-search-icon"></i>--%>
				</span>
        </form>

    </div>
    <!-- #nav-search -->
    <!-- /.navbar-header -->

    <!-- /.container -->
</header>


<script type="text/javascript">
    <%--$(function(){--%>
    <%--var session_value = ${sessionScope.hasUnread};--%>
    <%--console.log(session_value);--%>
        <%--if(session_value)$('#red').css('display', 'block');--%>
        <%--else $('#red').css('display', 'none');--%>
    <%--})--%>
</script>