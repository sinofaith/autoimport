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
<script src="<c:url value="/resources/js/zfb/zfb.js"/> "></script>
<script src="<c:url value="/resources/js/zfb/zfbJyjlTjjg.js"/> "></script>
<script src="<c:url value="/resources/thirdparty/jquery-form/jquery.form.js"/>" type="text/javascript"></script>
<%--详情模块脚本--%>
<script type="text/javascript">
    try{ace.settings.check('main-container','fixed')}catch(e){}
</script>
<style type="text/css">
    .crimeterrace{ background-color: #636B75 !important;}
</style>

<div class="tab_div">
    <%@include file="title.jsp" %>
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
                                                <strong>交易卖家账户信息(${aj.aj})</strong>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr align="center">
                                        <td width="4%">序号</td>
                                        <td width="6%">店铺名</td>
                                        <td width="5%">卖家账号</td>
                                        <td width="4%"><a href="/SINOFAITH/zfbJyjlTjjg/seach?pageNo=1&orderby=dfzh">账户名称</a></td>
                                        <td width="6%"><a href="/SINOFAITH/zfbJyjlTjjg/seach?pageNo=1&orderby=jyzcs">交易总次数</a></td>
                                        <td width="6%"><a href="/SINOFAITH/zfbJyjlTjjg/seach?pageNo=1&orderby=skzcs">进账总次数</a></td>
                                        <td width="6%"><a href="/SINOFAITH/zfbJyjlTjjg/seach?pageNo=1&orderby=skzje">进账总金额</a></td>
                                        <td width="6%"><a href="/SINOFAITH/zfbJyjlTjjg/seach?pageNo=1&orderby=fkzcs">出账总次数</a></td>
                                        <td width="6%"><a href="/SINOFAITH/zfbJyjlTjjg/seach?pageNo=1&orderby=fkzje">出账总金额</a></td>
                                        <td width="3%">详情</td>
                                    </tr>
                                    <c:forEach items="${detailinfo}" var="item" varStatus="st">
                                        <tr class="${st.index%2==1 ? '':'odd' }">
                                            <td align="center">${(st.index+1)+(page.pageNo-1)*page.pageSize}</td>
                                            <td align="center">${item.dyxcsj}</td>
                                            <td align="center">${item.dfzh}</td>
                                            <td align="center" title="${item.dfmc}"><div style="width:150px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.dfmc}</div></td>
                                            <td align="center">${item.jyzcs}</td>
                                            <td align="center">${item.skzcs}</td>
                                            <td align="center">${item.skzje}</td>
                                            <td align="center">${item.fkzcs}</td>
                                            <td align="center">${item.fkzje}</td>
                                            <td align="center">
                                                <button class="btna" data-toggle="modal" data-target="#myModal" onclick="getZfbJyjlTjjgDetails(this)">详情</button>
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
                                        <a href="/SINOFAITH/zfbJyjlTjjg/seach?pageNo=${page.topPageNo }"><input type="button" name="fristPage" value="首页" /></a>
                                        <c:choose>
                                            <c:when test="${page.pageNo!=1}">
                                                <a href="/SINOFAITH/zfbJyjlTjjg/seach?pageNo=${page.previousPageNo }"><input type="button" name="previousPage" value="上一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="previousPage" value="上一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${page.pageNo != page.totalPages}">
                                                <a href="/SINOFAITH/zfbJyjlTjjg/seach?pageNo=${page.nextPageNo }"><input type="button" name="nextPage" value="下一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="nextPage" value="下一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="/SINOFAITH/zfbJyjlTjjg/seach?pageNo=${page.bottomPageNo }"><input type="button" name="lastPage" value="尾页" /></a>
                                        <input type="number" id="num" max="${page.totalPages}" style="width: 9%" min="1">
                                        <input type="button" value="跳转" onclick="zfbSkip('JyjlTjjg')">
                                    </div>

                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                    <div class="sidebar_right pos_re">

                        <div class=" ">

                            <div>
                               <div class="col-lg-15">
                                    <div class="input-group">
                                        <input type="text" id="filterInput" class="form-control" value="${aj.filter}" placeholder="请输入要筛选的内容,例如：MCM">
                                        <span class="input-group-btn">
                                            <button class="btn btn-default" type="button" onclick="filterJyjlByspmc('${aj.aj}')">
                                                筛选
                                            </button>
                                        </span>
                                    </div><!-- /input-group -->
                                </div><!-- /.col-lg-6 --></div><br>
                                <form action="/SINOFAITH/zfbJyjlTjjg/SeachCode" method="post">
                                    <div class="form-group_search  fl_l width100" >
                                        <span style="margin-left: 10px;color: #444;padding-bottom: 10px;">查询方式</span>
                                        <select id="seachCondition" name="seachCondition" class="width100" STYLE="margin-bottom: 20px;" onchange="seachChange()">
                                            <option value="dfzh" <c:if test="${jyjlTjjgSeachCondition=='dfzh'}">selected="selected"</c:if>>卖家账号</option>
                                            <option value="dfmc" <c:if test="${jyjlTjjgSeachCondition=='dfmc'}">selected="selected"</c:if>>账号名称</option>
                                            <option value="skzje"<c:if test="${jyjlTjjgSeachCondition=='skzje'}">selected="selected"</c:if>>进账总金额阀值</option>
                                            <option value="fkzje"<c:if test="${jyjlTjjgSeachCondition=='fkzje'}">selected="selected"</c:if>>出账总金额阀值</option>
                                        </select>
                                        <textarea  class="form-control02 seachCode fl_l width100" id="seachCode" placeholder="请输入要查询内容" name="seachCode" onkeyup="isNum(this)">${jyjlTjjgSeachCode}</textarea>
                                    </div>

                                    <button type="submit" class="right_a_nav margin_none" >查询</button>
                                    <%--<button type="button" class="right_a_nav margin_none add_button" onclick="AddCrimeterrace()">新增人员信息</button>--%>
                                </form>
                            </div>
                            <div class="width100" style="margin-top: 10px;float: left;">
                                <span style="margin-left: 10px;color: #444;padding-bottom: 10px;margin-top: 20px;">支付宝数据导入/导出</span>
                                <div class="form-group_search loadFile width100" style="margin-top: 5px;height: auto;">
                                    <div class="if_tel width100">
                                       <span class="fl_l width100 " style="padding-bottom: 10px;margin-top: 10px;">
                                           <c:choose>
                                               <c:when test="${user.id==aj.userId}">
                                                   <button  type="button"  class="sideBar_r_button"  <c:if test="${aj!=null && detailinfo.size()!=0}">onclick="location.href='/SINOFAITH/zfbJyjlTjjg/download'"</c:if>>数据导出</button>
                                               </c:when>
                                               <c:otherwise>
                                                   <button  type="button"  class="sideBar_r_button" >授权查看无法操作</button>
                                               </c:otherwise>
                                           </c:choose>
                                       </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%--<form id="uploadFileForm" action="/SINOFAITH/uploadFolder" method="post"  style="display: none;">--%>
                <%--<input type="file" webkitdirectory name="file" id="file" style="display: none;">--%>
                <%--<input type="text" id="updatestate" name="updatestate" style="display: none;" value="1">--%>
                <%--</form>--%>

                <form id="seachDetail" action="<c:url value=""/>"  method="post" style="display: none;">
                </form>

            </div>
    </ul>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="top: 0%; min-width: 90%;left: 5%;right: 5%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabel">支付宝交易记录卖家详情<span id="title"></span></h4>
            </div>
            <div class="modal-body">
                <table class="table  table-hover table_style table_list1 " style="border-left: 1px solid #ccc; border-right: 1px solid #ccc!important;">
                    <thead style="display:table;width:100%;table-layout:fixed;width: calc( 100% - 16.5px );">
                    <tr align="center">
                        <td width="3%">序号</td>
                        <td width="6%"><button onclick="orderByFilter('jyh')">交易号</button></td>
                        <td width="8%"><button onclick="orderByFilter('mjyhid')">买家用户Id</button></td>
                        <td width="8%"><button onclick="orderByFilter('mjxx')">买家信息</button></td>
                        <td width="4%">交易状态</td>
                        <td width="8%"><button onclick="orderByFilter('mijyhid')">卖家用户Id</button></td>
                        <td width="8%"><button onclick="orderByFilter('mijxx')">卖家信息</button></td>
                        <td width="7%"><button onclick="orderByFilter('jyje')">交易金额</button></td>
                        <td width="8%"><button onclick="orderByFilter('sksj')">收款时间</button></td>
                        <td width="13%">商品名称</td>
                        <td width="15%">收货人地址</td>
                    </tr>
                    <input name="label" id="dfzh" hidden="hidden" value="">
                    <input name="label" id="dfmc" hidden="hidden" value="">
                    <input name="label" id="allRow" hidden="hidden" value="">
                    </thead>
                    <tbody id="result" style="display:block;height:340px;overflow-y:scroll;" onscroll="scrollF()">

                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <span style="position:absolute;width:250px;height:20px;margin-left: -1250px;color: blue;font-size: 13px">提示：蓝色标注为已调单账户</span>
                <button type="button" class="btn btn-default" onclick="downJyjlTjjgDetailInfo()">导出</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>
<%@include file="../template/newfooter.jsp" %>
