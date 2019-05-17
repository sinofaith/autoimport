<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@include file="../template/sideBar_left.jsp" %>
<%@include file="../template/newmain.jsp" %>

<%--详情模块脚本--%>

<link href="<c:url value="/resources/css/bootstrap.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/bootstrap-theme.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/css.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/map.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/font.css"/>" rel="stylesheet" media="screen">
<script src="<c:url value="/resources/jquery/jquery.js"/> "></script>
<script src="<c:url value="/resources/js/jquery-1.9.1.min.js"/> "></script>
<script src="<c:url value="/resources/js/bootstrap.js"/> "></script>
<script src="<c:url value="/resources/thirdparty/jquery-form/jquery.form.js"/>" type="text/javascript"></script>
<script src="<c:url value="/resources/js/aj.js"/>" type="text/javascript"></script>
<%--详情模块脚本--%>
<script type="text/javascript">
    try{ace.settings.check('main-container','fixed')}catch(e){}
</script>
<style type="text/css">
    .crimeterrace{ background-color: #636B75 !important;}

    .selected{
        background-color:#bfd9f3;
    }

</style>

<div class="tab_div">
    <span class="tab_nav">  <a  href="/SINOFAITH/aj" class="addactive">案件信息</a></span>
    <ul >
        <div class="main-container-inner " style="margin-bottom: 10px">
            <div class="width_100 pos_re_block">
                <div class="cantent_block ">
                    <div class="sidebar_left ">
                        <div class="ddr">
                            <div >
                                <input name="label" id="label" hidden="hidden">
                                <table class="table  table-hover table_style table_list1 " style="border-left: 1px solid #ccc; border-right: 1px solid #ccc!important;">
                                    <tr>
                                        <td colspan="10"  align="center" class="dropdown_index" style="background-color: #eee;">
                                            <div class="dropdown " style="color: #333">
                                                <strong>案件列表</strong>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr align="center">
                                        <td width="6%">序号</td>
                                        <td width="7%">案件名</td>
                                        <td width="7%">银行卡交易数据</td>
                                        <td width="7%">财付通交易数据</td>
                                        <td width="7%">支付宝账户数据</td>
                                        <td width="7%">物流寄件数据</td>
                                        <td width="7%">传销会员数据</td>
                                        <td width="12%">案件创建时间</td>
                                        <td width="6%">操作</td>
                                    </tr>
                                    <form action="" method="post" id="_form">

                                    </form>
                                    <c:forEach items="${detailinfo}" var="item" varStatus="st">
                                        <tr class="${st.index%2==1 ? '':'odd' }">
                                            <td align="center">${item.xh} &nbsp;<c:if test="${!fn:contains(item.aj, ',')}"><input type="checkbox" name="ajval" value="${item.aj}"></c:if></td>
                                            <td align="center">${item.aj}</td>
                                            <td align="center"><a href="/SINOFAITH/aj/ajm?aj=${item.aj}&type=2">${item.banknum}</a></td>
                                            <td align="center"><a href="/SINOFAITH/aj/ajm?aj=${item.aj}&type=1">${item.cftnum}</a></td>
                                            <td align="center"><a href="/SINOFAITH/aj/ajm?aj=${item.aj}&type=5">${item.zfbnum}</a></td>
                                            <td align="center"><a href="/SINOFAITH/aj/ajm?aj=${item.aj}&type=3">${item.wuliunum}</a></td>
                                            <td align="center"><a href="/SINOFAITH/aj/ajm?aj=${item.aj}&type=4">${item.psnum}</a></td>
                                            <td align="center">${item.inserttime}</td>
                                            <td align="center">
                                                <c:choose>
                                                    <c:when test="${user.id==item.userId}">
                                                        <button  data-toggle="modal" data-target="#myModal1" onclick="deleteAj('${item.aj}')">删除</button> |
                                                        <button  data-toggle="modal" data-target="#myModal2" onclick="getUser(${item.id},'${item.aj}','${user.username}')">授权</button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        授权查看无法操作
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:choose>
                                        <c:when test="${detailinfo ==null || detailinfo.size()==0}">
                                            <tr>
                                                <td colspan="10" align="center"> 无数据 </td>
                                            </tr>
                                        </c:when>
                                    </c:choose>

                                </table>

                            </div>
                            <c:choose>
                                <c:when test="${detailinfo!=null && detailinfo.size()!=0}">
                                    <div  class="page_nmber">
                                        <div class="mar_t_15">共${page.totalRecords}条记录 共<span id="totalPage">${page.totalPages}</span>页 当前第${page.pageNo}页<br></div>
                                        <a href="/SINOFAITH/aj/seach?pageNo=${page.topPageNo }"><input type="button" name="fristPage" value="首页" /></a>
                                        <c:choose>
                                            <c:when test="${page.pageNo!=1}">
                                                <a href="/SINOFAITH/aj/seach?pageNo=${page.previousPageNo }"><input type="button" name="previousPage" value="上一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="previousPage" value="上一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${page.pageNo != page.totalPages}">
                                                <a href="/SINOFAITH/aj/seach?pageNo=${page.nextPageNo }"><input type="button" name="nextPage" value="下一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="nextPage" value="下一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="/SINOFAITH/aj/seach?pageNo=${page.bottomPageNo }"><input type="button" name="lastPage" value="尾页" /></a>
                                        <input type="number" id="num" max="${page.totalPages}" style="width: 9%" min="1">
                                        <input type="button" value="跳转" onclick="ajSkip()">
                                        <input type="button" value="多案件分析" onclick="ajsCount()">
                                    </div>

                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                    <div class="sidebar_right pos_re">

                        <div class=" ">

                            <div>
                                <form action="/SINOFAITH/aj/SeachCode" method="post">
                                    <div class="form-group_search  fl_l width100" >
                                        <span style="margin-left: 10px;color: #444;padding-bottom: 10px;">查询方式</span>
                                        <select name="seachCondition" class="width100" STYLE="margin-bottom: 20px;">
                                            <option value="aj"<c:if test="${ajseachCondition=='aj'}">selected="selected"</c:if>>案件名</option>
                                        </select>
                                        <textarea  class="form-control02 seachCode fl_l width100" id="seachCode" placeholder="请输入要查询案件名,多个案件请用回车换行输入" name="seachCode" >${ajseachCode}</textarea>
                                    </div>

                                    <button type="submit" class="right_a_nav margin_none" >查询</button>
                                </form>
                            </div>
                            <div class="width100" style="margin-top: 10px;float: left;">
                                <span style="margin-left: 10px;color: #444;padding-bottom: 10px;margin-top: 20px;">案件操作</span>

                                <div class="form-group_search loadFile width100" style="margin-top: 5px;height: auto;">
                                    <div class="if_tel width100">
                       <span class="fl_l width100 " style="padding-bottom: 10px;margin-top: 10px;">

                <button class="sideBar_r_button" data-toggle="modal"
                        data-target="#myModal">新增案件</button>
                       </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%--<input type="file" webkitdirectory name="file" id="file" style="display: none;">--%>
                <%--<input type="text" id="updatestate" name="updatestate" style="display: none;" value="1">--%>
                <%--</form>--%>

                <form id="seachDetail" action="<c:url value=""/>"  method="post" style="display: none;">
                </form>

            </div>
        </div>
    </ul>
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×</button>
            </div>
            <div class="modal-body">
                <span id="percentage" style="color:blue;"></span> <br>
                <div class="file-box">

                    案件名:<input type="text" name = 'aj' id ='aj'
                               class='txt'  data-toggle="tooltip" data-placement="top" oninput="destroyTooltip()" >
                    <input type="submit" name="submit" class="btn" value="确定"
                           onclick="addAj()"/>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                </button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>

<div class="modal fade" id="myModal1" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×</button>
            </div>
            <div class="modal-body">
                <span id="percentage1" style="color:blue;"></span> <br>
                <div class="file-box">

                    案件名:<input type="text" name = 'aj1' id ='aj1'readonly
                               class='txt'  data-toggle="tooltip" data-placement="top"  >
                    <br>
                    <br>
                    <label><input id="sel_1" onchange="selectAll()" type="checkbox" value="0"/>全选/全不选</label>
                    <label><input name="deleteAj" type="checkbox" value="2"/>银行卡</label>
                    <label><input name="deleteAj" type="checkbox" value="1"/>财付通</label>
                    <label><input name="deleteAj" type="checkbox" value="5"/>支付宝</label>
                    <label><input name="deleteAj" type="checkbox" value="3"/>物流</label>
                    <label><input name="deleteAj" type="checkbox" value="4"/>传销</label>
                </div>
            </div>
            <div class="modal-footer">
                <input type="submit" name="submit" class="btn" value="删除" onclick="deleteAjByFilter()"/>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>

<div class="modal fade" id="myModal2" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="top: 0%; min-width: 50%;left: 25%;right: 25%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×</button>
                <div id="ajm">
                </div>
            </div>
            <div class="modal-body">

                <div style="padding-left: 10px; width: 100%; height: 380px">
                    <div class="panel panel-info" style="width: 340px; float: left">
                        <div class="panel-heading">已授权用户</div>
                        <ul class="list-group" id="grand" style="display:block;height:360px;overflow-y:auto;">
                        </ul>
                    </div>
                    <div class="panel panel-danger" style="margin-left:20px;width: 340px; float: left">
                        <div class="panel-heading">未授权用户</div>
                        <ul class="list-group" id="noGrand" style="display:block;height:360px; overflow-y:auto;">
                        </ul>
                    </div>
                </div>
                <input type="hidden" id = "ajid" value=""/>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" onclick="editGrand()">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>
<%@include file="../template/newfooter.jsp" %>
