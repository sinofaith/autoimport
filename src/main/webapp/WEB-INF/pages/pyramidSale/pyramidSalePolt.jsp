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
    <div style="width: 1430px;height: 600px;margin-left: 14px;margin-top: 40px;overflow-x:scroll;">
        <div id="container" style="height: 100%;"></div>
    </div>
</div>

<script type="text/javascript">
    var dom = document.getElementById("container");
    var myChart = echarts.init(dom);
    myChart.on("click", clickFun);
    var app = {};
    option = null;
    myChart.showLoading();
    $.get('${pageContext.request.contextPath}/pyramidSalePolt/tree', function (data) {
        myChart.hideLoading();
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
                            position: 'top',
                            rotate: -90,
                            verticalAlign: 'middle',
                            align: 'right',
                            fontSize: 11
                        }
                    },
                    leaves: {
                        label: {
                            distance:-45,
                            normal: {
                                position: 'bottom',
                                verticalAlign: 'middle',
                                align: 'left'
                            }
                        }
                    },
                    animationDurationUpdate: 750
                }
            ]
        });
    });
    function clickFun(param){
        var allNode=0;
        var nodes=myChart._chartsViews[0]._data._graphicEls;
        for(var i=0,count =nodes.length;i<count;i++){
            var node=nodes[i];
            if(node===undefined)
                continue;
            allNode++;
        }
        var height=window.innerHeight;
        var currentHeight=25*allNode;
        var newWidth=Math.max(currentHeight,height);
        container.style.width = newWidth + 'px';
        // container.style.height = window.innerHeight + 'px';
        myChart.resize();
    }
</script>

<%@include file="../template/newfooter.jsp" %>
