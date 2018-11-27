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

<script src="<c:url value="/resources/js/pyramidSale/pyramidSale.js"/> "></script>

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
        <a href="/SINOFAITH/pyramidSaleTier" class="addactive">传销层级信息</a>
        <a href="/SINOFAITH/pyramidSalePolt">传销层级图</a>
        <%--<a  href="/SINOFAITH/wuliuShip">物流寄件人信息</a>
        <a  href="/SINOFAITH/wuliuSj">物流收件人信息</a>--%>
    </span>
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
                                        <td colspan="14"  align="center" class="dropdown_index" style="background-color: #eee;">
                                            <div class="dropdown " style="color: #333">
                                                <strong>传销层级信息(${aj.aj})</strong>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr align="center">
                                        <td width="3%">序号</td>
                                        <td width="4%">会员号</td>
                                        <td width="6%">推荐会员号</td>
                                        <td width="5%">姓名</td>
                                        <td width="8%">身份证号码</td>
                                        <td width="6%">手机号</td>
                                        <td width="12%">详细地址</td>
                                        <td width="9%">银行卡号</td>
                                        <td width="5%"><a href="/SINOFAITH/pyramidSaleTier/seach?pageNo=1&orderby=tier">当前层级</a></td>
                                        <td width="5%"><a href="/SINOFAITH/pyramidSaleTier/seach?pageNo=1&orderby=containsTier">包含层级</a></td>
                                        <td width="5%"><a href="/SINOFAITH/pyramidSaleTier/seach?pageNo=1&orderby=directDrive">直推下线</a></td>
                                        <td width="5%"><a href="/SINOFAITH/pyramidSaleTier/seach?pageNo=1&orderby=directReferNum">下线会员</a></td>
                                        <td width="3%">层图</td>
                                    </tr>
                                    <c:forEach items="${detailinfo}"
                                               var="item" varStatus="st">
                                        <tr class="${st.index%2==1 ? '':'odd' }">
                                            <td align="center">${(st.index+1)+(page.pageNo-1)*page.pageSize}</td>
                                            <td align="center">${item.psId}</td>
                                            <td align="center">${item.sponsorid}</td>
                                            <td align="center">${item.psAccountholder}</td>
                                            <td align="center">${item.sfzhm}</td>
                                            <td align="center">${item.mobile}</td>
                                            <td align="center" title="${item.address}"><xmp style="font-family: 'Microsoft YaHei UI';width:180px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;font-size: 12px !important;color: #666;">${item.address}</xmp></td>
                                            <td align="center">${item.accountnumber}</td>
                                            <td align="center">${item.tier}</td>
                                            <td align="center">${item.containsTier}</td>
                                            <td align="center">
                                                <c:if test="${item.directDrive!=0}">
                                                    <button  data-toggle="modal" data-target="#myModal" onclick="getPyramSaleDetails(this,true)">${item.directDrive}</button>
                                                </c:if>
                                                <c:if test="${item.directDrive==0}">${item.directDrive}</c:if>
                                            </td>
                                            <td align="center">
                                                <c:if test="${item.directReferNum!=0}">
                                                    <button  data-toggle="modal" data-target="#myModal1" onclick="getPyramSaleDetails(this,false)">${item.directReferNum}</button>
                                                </c:if>
                                                <c:if test="${item.directReferNum==0}">${item.directReferNum}</c:if>
                                            </td>
                                            <td align="center"><a href="${pageContext.request.contextPath}/pyramidSalePolt?psId=${item.psId}">详情</a></td>
                                        </tr>
                                    </c:forEach>
                                    <c:choose>
                                        <c:when test="${detailinfo ==null || detailinfo.size()==0}">
                                            <tr>
                                                <td colspan="14" align="center"> 无数据 </td>
                                            </tr>
                                        </c:when>
                                    </c:choose>

                                </table>

                            </div>
                            <c:choose>
                                <c:when test="${detailinfo!=null && detailinfo.size()!=0}">
                                    <div  class="page_nmber">
                                        <div class="mar_t_15">共${page.totalRecords}条记录 共<span id="totalPage">${page.totalPages}</span>页 当前第${page.pageNo}页<br></div>
                                        <a href="/SINOFAITH/pyramidSaleTier/seach?pageNo=${page.topPageNo }"><input type="button" name="fristPage" value="首页" /></a>
                                        <c:choose>
                                            <c:when test="${page.pageNo!=1}">
                                                <a href="/SINOFAITH/pyramidSaleTier/seach?pageNo=${page.previousPageNo }"><input type="button" name="previousPage" value="上一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="previousPage" value="上一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${page.pageNo != page.totalPages}">
                                                <a href="/SINOFAITH/pyramidSaleTier/seach?pageNo=${page.nextPageNo }"><input type="button" name="nextPage" value="下一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="nextPage" value="下一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="/SINOFAITH/pyramidSaleTier/seach?pageNo=${page.bottomPageNo }"><input type="button" name="lastPage" value="尾页" /></a>
                                        <input type="number" id="num" max="${page.totalPages}" style="width: 9%" min="1" >
                                        <input type="button" value="跳转" onclick="pyramidSaleSkip('pyramidSaleTier')">
                                    </div>

                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                    <div class="sidebar_right pos_re">

                        <%--<input type="checkbox" id="checkbox1" ${aj.flg==1? 'checked':''} value="1" onclick="wuliuCount('${aj.aj}')" />
                        <label for="checkbox1" style="padding-top: 8px">数据去重</label>--%>
                        <div class=" ">
                            <div>
                                <form action="/SINOFAITH/pyramidSaleTier/SeachCode" method="post">
                                    <div class="form-group_search  fl_l width100" >
                                        <span style="margin-left: 10px;color: #444;padding-bottom: 10px;">查询方式</span>
                                        <select id="seachCondition" name="seachCondition" class="width100" STYLE="margin-bottom: 20px;"  onchange="seachChange()">
                                            <option value="psId"<c:if test="${psTierSeachCondition=='psId'}">selected="selected"</c:if>>会员编号</option>
                                            <option value="sponsorid" <c:if test="${psTierSeachCondition=='sponsorid'}">selected="selected"</c:if> >推荐会员编号</option>
                                            <option value="psAccountholder" <c:if test="${psTierSeachCondition=='psAccountholder'}">selected="selected"</c:if> >姓名</option>
                                            <option value="directDrive" <c:if test="${psTierSeachCondition=='directDrive'}">selected="selected"</c:if> >直推下线数阈值</option>
                                            <option value="directReferNum" <c:if test="${psTierSeachCondition=='directReferNum'}">selected="selected"</c:if> >下线会员数阈值</option>
                                        </select>
                                        <textarea  class="form-control02 seachCode fl_l width100" id="seachCode" placeholder="请输入要查询内容,如果使用模糊查询请加%" name="seachCode" >${psTierSeachCode}</textarea>
                                    </div>

                                    <button type="submit" class="right_a_nav margin_none" >查询</button>
                                    <%--<button type="button" class="right_a_nav margin_none add_button" onclick="AddCrimeterrace()">新增人员信息</button>--%>
                                </form>
                            </div>
                            <div class="width100" style="margin-top: 10px;float: left;">
                                <span style="margin-left: 10px;color: #444;padding-bottom: 10px;margin-top: 20px;">传销数据导入/导出</span>
                                <div class="form-group_search loadFile width100" style="margin-top: 5px;height: auto;">
                                    <div class="if_tel width100">
                       <span class="fl_l width100 " style="padding-bottom: 10px;margin-top: 10px;">
                           <%--<button  type="button"  class="sideBar_r_button" id="btnLoadFile" >文件夹导入</button>--%>
                           <%--<c:if test="${!fn:contains(aj.aj, ',')}">
                               <button class="sideBar_r_button" data-toggle="modal" data-target="#myModal">传销数据导入</button>
                           </c:if>--%>
                           <button  type="button"  class="sideBar_r_button"  <c:if test="${aj!=null}">onclick="location.href='/SINOFAITH/pyramidSaleTier/download'"</c:if>>数据导出</button>
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
        </div>
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
             aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog" style="top: 0%; min-width: 90%;left: 5%;right: 5%;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                                aria-hidden="true">×</button>
                        <h4 class="modal-title" id="myModalLabel">直推下线详情<span id="title"></span></h4>
                    </div>
                    <div class="modal-body">
                        <table class="table  table-hover table_style table_list1 " style="border-left: 1px solid #ccc; border-right: 1px solid #ccc!important;">
                            <thead style="display:table;width:100%;table-layout:fixed;width: calc( 100% - 16.5px );">
                            <tr align="center">
                                <td width="5%">序号</td>
                                <td width="8%"><button onclick="orderByFilter('psid',true)">会员编号</button></td>
                                <td width="8%">推荐会员编号</td>
                                <td width="8%">姓名</td>
                                <td width="8%">手机号码</td>
                                <td width="5%">性别</td>
                                <td width="15%">详细地址</td>
                                <td width="13%">身份证号</td>
                                <td width="12%">持卡人</td>
                                <td width="13%">银行名称</td>
                                <td width="13%">银行卡号</td>
                            </tr>
                            <input name="label" id="psid" hidden="hidden" value="">
                            <input name="label" id="allRow" hidden="hidden" value="">
                            </thead>
                            <tbody id="result" style="display:block;height:340px;overflow-y:scroll;" onscroll="scrollF(true)">

                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" onclick="downDetailInfo()">导出</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    </div>
                </div>
                <!-- /.modal-content -->
            </div>
            <!-- /.modal -->
        </div>
        <div class="modal fade" id="myModal1" tabindex="-1" role="dialog"
             aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog" style="top: 0%; min-width: 90%;left: 5%;right: 5%;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                                aria-hidden="true">×</button>
                        <h4 class="modal-title" id="myModalLabel1">下线会员详情<span id="title1"></span></h4>
                    </div>
                    <div class="modal-body">
                        <table class="table  table-hover table_style table_list1 " style="border-left: 1px solid #ccc; border-right: 1px solid #ccc!important;">
                            <thead style="display:table;width:100%;table-layout:fixed;width: calc( 100% - 16.5px );">
                            <tr align="center">
                                <td width="5%">序号</td>
                                <td width="8%"><%--<button onclick="orderByFilter('psid',false)">--%>会员编号<%--</button>--%></td>
                                <td width="8%">推荐会员编号</td>
                                <td width="8%">姓名</td>
                                <td width="8%">手机号码</td>
                                <td width="5%">性别</td>
                                <td width="15%">详细地址</td>
                                <td width="13%">身份证号</td>
                                <td width="12%">持卡人</td>
                                <td width="13%">银行名称</td>
                                <td width="13%">银行卡号</td>
                            </tr>
                            <input name="label" id="psid1" hidden="hidden" value="">
                            <input name="label" id="allRow1" hidden="hidden" value="">
                            </thead>
                            <tbody id="result1" style="display:block;height:340px;overflow-y:scroll;" onscroll="scrollF(false)">

                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" onclick="downDetailInfo()">导出</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    </div>
                </div>
                <!-- /.modal-content -->
            </div>
            <!-- /.modal -->
        </div>
<%@include file="../template/newfooter.jsp" %>
