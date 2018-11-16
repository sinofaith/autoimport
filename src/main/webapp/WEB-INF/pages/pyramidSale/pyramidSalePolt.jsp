<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@include file="../template/sideBar_left.jsp" %>
<%@include file="../template/newmain.jsp" %>

<%--详情模块脚本--%>

<link href="<c:url value="/resources/css/bootstrap.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/bootstrap-theme.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/css.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/map.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/font.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/thirdparty/alertify/css/bootstrap.css"/> " rel="stylesheet">
<script src="<c:url value="/resources/jquery/jquery.js"/> "></script>
<script src="<c:url value="/resources/js/jquery-1.9.1.min.js"/> "></script>
<script src="<c:url value="/resources/js/bootstrap.js"/> "></script>
<script src="<c:url value="/resources/thirdparty/alertify/js/alertify.min.js"/> "></script>
<script src="<c:url value="/resources/js/multiselect.min.js"/> "></script>

<script src="<c:url value="/resources/js/echars/echarts4.min.js"/> "></script>
<script src="<c:url value="/resources/js/echars/ecStat.min.js"/> "></script>
<script src="<c:url value="/resources/js/echars/dataTool.min.js"/> "></script>
<script src="<c:url value="/resources/js/echars/china.js"/> "></script>
<script src="<c:url value="/resources/js/echars/world.js"/> "></script>
<script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=ZUONbpqGBsYGXNIYHicvbAbM"></script>
<script src="<c:url value="/resources/js/echars/bmap.min.js"/> "></script>
<script src="<c:url value="/resources/js/echars/simplex.js"/> "></script>

<script src="<c:url value="/resources/thirdparty/jquery-form/jquery.form.js"/>" type="text/javascript"></script>
<%--详情模块脚本--%>
<script type="text/javascript">
    try{ace.settings.check('main-container','fixed')}catch(e){}
</script>
<style type="text/css">
    .crimeterrace{ background-color: #636B75 !important;}
</style>

<div class="tab_div">
    <span class="tab_nav">
        <a href="/SINOFAITH/pyramidSale">传销会员信息</a>
        <a href="/SINOFAITH/pyramidSaleTier">传销层级信息</a>
        <a href="/SINOFAITH/pyramidSalePolt" class="addactive">传销层级图</a>
        <%--<a  href="/SINOFAITH/wuliuSj">物流收件人信息</a>--%>
    </span>
    <div style="width: 1430px;height: 600px;margin-left: 14px;margin-top: 40px">
        <div id="container" style="height: 100%"></div>
    </div>
</div>

<script type="text/javascript">
    /*var data = {
        "id" : 1,
        "psid" : 2,
        "name": "flare",
        "value" : "",
        "children": [
            {
                "name": "analytics",
                "value" : 1597,
                "children": [
                    {
                        "name": "cluster",
                        "children": [
                            {"name": "AgglomerativeCluster", "value": 3938},
                            {"name": "CommunityStructure", "value": 3812},
                            {"name": "HierarchicalCluster", "value": 6714},
                            {"name": "MergeEdge", "value": 743}
                        ]
                    },
                    {
                        "name": "graph",
                        "children": [
                            {"name": "BetweennessCentrality", "value": 3534},
                            {"name": "LinkDistance", "value": 5731},
                            {"name": "MaxFlowMinCut", "value": 7840},
                            {"name": "ShortestPaths", "value": 5914},
                            {"name": "SpanningTree", "value": 3416}
                        ]
                    },
                    {
                        "name": "optimization",
                        "children": [
                            {"name": "AspectRatioBanker", "value": 7074}
                        ]
                    }
                ]
            }]
        };*/
    var dom = document.getElementById("container");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    $.get('${pageContext.request.contextPath}/pyramidSalePolt/tree', function (data) {
        console.log(data)
        myChart.setOption(option = {
            tooltip: {
                trigger: 'item',
                triggerOn: 'mousemove'
            },
            series:[
                {
                    type: 'tree',
                    data: data,
                    left: '2%',
                    right: '2%',
                    top: '8%',
                    bottom: '20%',
                    symbol: 'emptyCircle',
                    orient: 'vertical',
                    expandAndCollapse: true,
                    label: {
                        normal: {
                            position: 'bottom',
                            rotate: -0,
                            verticalAlign: 'middle',
                            align: 'left',
                            fontSize: 16
                        }
                    },
                    leaves: {
                        label: {
                            normal: {
                                position: 'bottom',
                                rotate: -90,
                                verticalAlign: 'middle',
                                align: 'left'
                            }
                        }
                    },
                    animationDurationUpdate: 750,
                    //expandAndCollapse:false
                }
            ]
        });
        if (option && typeof option === "object") {
            myChart.setOption(option, true);
        }
    });
</script>

<%@include file="../template/newfooter.jsp" %>
