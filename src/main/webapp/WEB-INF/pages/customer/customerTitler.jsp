<%--
  Created by IntelliJ IDEA.
  User: 95645
  Date: 2016/11/30
  Time: 13:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<link href="<c:url value="/resources/css/bootstrap.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/bootstrap-theme.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/css.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/map.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/font.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/select/selectordie.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/select/selectordie_theme_02.css"/>" rel="stylesheet" media="screen">--%>
<%--<script src="<c:url value="/resources/jquery/jquery.js"/> "></script>--%>
<%--<script src="<c:url value="/resources/js/jquery-1.9.1.min.js"/> "></script>--%>
<%--<script src="<c:url value="/resources/js/bootstrap.js"/> "></script>
<script src="<c:url value="/resources/js/bank/bank.js"/> "></script>
<script src="<c:url value="/resources/js/select/selectordie.min.js"/> "></script>--%>
<script type="text/javascript">
        $(function () {
            var url = window.location.pathname.split("/");
            $(".tab_nav a").each(function(index, element) {
                var aherf = element.getAttribute('href').split("/");
                if(aherf[aherf.length-1].startsWith(url[url.length-2])){
                    element.setAttribute("class","addactive")
                }
            });
        })
</script>
<html>
<%--新加样式--%>
<body>
<span class="tab_nav">
        <a href="/SINOFAITH/customerPro?aj=${aj.aj}"  style="width: 16%">人员信息</a>
        <c:if test="${aj!=null}">
                <a href="/SINOFAITH/customerRelation" style="width: 16%">目标人物关系</a>
        </c:if>
        <%--<a href="/SINOFAITH/bank" style="width: 16%" >资金开户信息</a>--%>
        <%--<a href="/SINOFAITH/bankzzxx" style="width: 16%">资金交易明细</a>--%>
        <%--<a href="/SINOFAITH/banktjjg" style="width: 16%">账户统计信息</a>--%>
        <%--<a href="/SINOFAITH/banktjjgs"style="width: 16%">账户点对点统计信息</a>--%>
        <%--<a href="/SINOFAITH/bankgtzh"style="width: 16%">公共账户统计信息</a>--%>

</span>
</body>
</html>
