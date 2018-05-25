<%--
  Created by IntelliJ IDEA.
  User: Achol.Xu
  Date: 2015/4/11
  Time: 22:21
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta charset="utf-8" />
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
    <script src="<c:url value="/resources/thirdparty/alertify/js/alertify.min.js"/> " ></script>
    <link href="<c:url value="/resources/thirdparty/alertify/css/alertify.min.css"/> " rel="stylesheet">
            <link href="<c:url value="/resources/thirdparty/alertify/css/default.min.css"/> " rel="stylesheet">
            <script src="<c:url value="/resources/js/main.js"/> "></script>

    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <!-- basic styles -->

    <link href="<c:url value="/resources/thirdparty/assets/css/bootstrap.min.css" />" rel="stylesheet" />
    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/font-awesome.min.css"/>" />


    <!-- page specific plugin styles -->

    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/jquery-ui-1.10.3.full.min.css"/>" />
    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/datepicker.css"/>" />
    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/ui.jqgrid.css"/>" />

    <!-- ace styles -->

    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/ace.min.css"/>" />
    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/ace-rtl.min.css"/>" />
    <link rel="stylesheet" href="<c:url value="/resources/thirdparty/assets/css/ace-skins.min.css"/>" />
    <!-- basic scripts -->
    <script src="<c:url value="/resources/thirdparty/assets/js/bootstrap.min.js"/>" ></script>
    <script src="<c:url value="/resources/thirdparty/assets/js/typeahead-bs2.min.js"/>" ></script>

    <script src="<c:url value="/resources/thirdparty/assets/js/jquery-ui-1.10.3.full.min.js"/>" ></script>
    <script src="<c:url value="/resources/thirdparty/assets/js/jquery.ui.touch-punch.min.js"/>" ></script>
    <!-- ace scripts -->
    <script src="<c:url value="/resources/thirdparty/assets/js/ace-elements.min.js"/>" ></script>
    <script src="<c:url value="/resources/thirdparty/assets/js/ace.min.js"/>" ></script>

    </head>

<form id="downloadForm" action="<c:url value="/extend/logs"/>" method="get">
    <input type="hidden" name="path"/>
</form>
<form id = "details" action="<c:url value="/BasicQuery/downexcel"/>" >
    <input type="hidden" name="value"/>
</form>
    <body><!-- #BeginLibraryItem "/Library/top.lbi" --><div class="navbar navbar-default" id="navbar">
        <div class="navbar-container" id="navbar-container">
            <div class="navbar-header pull-left">
                <a href="#" class="navbar-brand"><img src="<c:url value="/resources/thirdparty/assets/images/logo.赚.jpg"/>"/> </a><!-- /.brand -->
            </div><!-- /.navbar-header -->
        </div><!-- /.container -->
    </div><!-- #EndLibraryItem -->
    <div class="main-container" id="main-container">
    </div><!-- /.main-container -->


    <div class="sidebar" id="sidebar">

        <!-- #sidebar-shortcuts -->
        <div class="sub_menu">
            <h3>数据下载页</h3>
        </div>
    </div>

    <div class="main-content">

        <div class="page-content">
            <div class="page-header">
                <h1 id="h1title">信息</h1></div></div>
    </div>

    </body>


<script>
    $(function(){
        var downloadtype=${downloadtype};
        var downloadcontent=${downloadcontent}
        switch (downloadtype)
        {
            case 1:
                $('#h1title').html("扩展分析数据下载");

                //$("body").mask("正在生成下载数据");
                $.ajax({
                    url: '/AMD/extend/logFilePath',
                    type: 'POST',
                    data: JSON.stringify(downloadcontent),
                    dataType: 'text',
                    contentType: 'application/json;charset=UTF-8',
                    success: function(result) {
                        console.log('log path:' + result);
                        $('#downloadForm>input').val(result);
                        $('#downloadForm').submit();
                       // $("body").unmask();
                    },
                    error: function(error) {
                        alert('错误！请检查网络环境！');
                        $("body").unmask();
                    }
                });
                break;
            case 2:

                $('#h1title').html("基础查询数据下载");
               // $("body").mask("正在生成下载数据");
                $('#details>input').val(downloadcontent);
                $('#details').submit();
                break;
            case 3:
                $('#h1title').html("主动研判数据下载");
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }
    });
</script>
</html>
