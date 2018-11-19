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
<script src="<c:url value="/resources/jquery/jquery.js"/> "></script>
<script src="<c:url value="/resources/js/jquery-1.9.1.min.js"/> "></script>
<script src="<c:url value="/resources/js/bootstrap.js"/> "></script>

<script src="<c:url value="/resources/js/bank/bank.js"/> "></script>
<script src="<c:url value="/resources/js/wuliu/wuliu.js"/> "></script>

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
        <a  href="/SINOFAITH/wuliujjxx" class="addactive">物流寄件信息</a>
        <a  href="/SINOFAITH/wuliuRelation">物流寄收关系信息</a>
        <a  href="/SINOFAITH/wuliuShip">物流寄件人信息</a>
        <a  href="/SINOFAITH/wuliuSj">物流收件人信息</a>
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
                                        <td colspan="13"  align="center" class="dropdown_index" style="background-color: #eee;">
                                            <div class="dropdown " style="color: #333">
                                                <strong>物流寄件信息(${aj.aj})</strong>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr align="center">
                                        <td width="5%">序号</td>
                                        <td width="8%">运单号</td>
                                        <td width="14%"><a href="/SINOFAITH/wuliujjxx/seach?pageNo=1&orderby=ship_time">寄件时间</a></td>
                                        <td width="6%">寄件人</td>
                                        <td width="6%">寄件电话</td>
                                        <td width="12%">寄件地址</td>
                                        <td width="6%">收件人</td>
                                        <td width="6%">收件电话</td>
                                        <td width="12%">收件地址</td>
                                        <td width="8%">托寄物</td>
                                        <td width="5%">付款方式</td>
                                        <td width="5%">代收货款</td>
                                        <td width="5%">运费</td>
                                    </tr>
                                    <c:forEach items="${detailinfo}"
                                               var="item" varStatus="st">
                                        <tr class="${st.index%2==1 ? '':'odd' }">
                                            <td align="center">${item.id}</td>
                                            <td align="center">${item.waybill_id}</td>
                                            <td align="center">${item.ship_time}</td>
                                            <td align="center" title="${item.sender}"><div style="width:60px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.sender}</div></td>
                                            <td align="center">${item.ship_phone}</td>
                                            <td align="center" title="${item.ship_address}"><div style="width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.ship_address}</div></td>
                                            <td align="center">${item.addressee}</td>
                                            <td align="center">${item.sj_phone}</td>
                                            <td align="center" title="${item.sj_address}"><div style="width:100px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.sj_address}</div></td>
                                            <td align="center" title="${item.tjw}"><div style="width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.tjw}</div></td>
                                            <td align="center">${item.payment}</td>
                                            <td align="center">${item.dshk}</td>
                                            <td align="center">${item.freight}</td>
                                        </tr>
                                    </c:forEach>
                                    <c:choose>
                                        <c:when test="${detailinfo ==null || detailinfo.size()==0}">
                                            <tr>
                                                <td colspan="13" align="center"> 无数据 </td>
                                            </tr>
                                        </c:when>
                                    </c:choose>

                                </table>

                            </div>
                            <c:choose>
                                <c:when test="${detailinfo!=null && detailinfo.size()!=0}">
                                    <div  class="page_nmber">
                                        <div class="mar_t_15">共${page.totalRecords}条记录 共<span id="totalPage">${page.totalPages}</span>页 当前第${page.pageNo}页<br></div>
                                        <a href="/SINOFAITH/wuliujjxx/seach?pageNo=${page.topPageNo }"><input type="button" name="fristPage" value="首页" /></a>
                                        <c:choose>
                                            <c:when test="${page.pageNo!=1}">
                                                <a href="/SINOFAITH/wuliujjxx/seach?pageNo=${page.previousPageNo }"><input type="button" name="previousPage" value="上一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="previousPage" value="上一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${page.pageNo != page.totalPages}">
                                                <a href="/SINOFAITH/wuliujjxx/seach?pageNo=${page.nextPageNo }"><input type="button" name="nextPage" value="下一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="nextPage" value="下一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="/SINOFAITH/wuliujjxx/seach?pageNo=${page.bottomPageNo }"><input type="button" name="lastPage" value="尾页" /></a>
                                        <input type="number" id="num" max="${page.totalPages}" style="width: 9%" min="1" >
                                        <input type="button" value="跳转" onclick="wuliuSkip('jjxx')">
                                    </div>

                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                    <div class="sidebar_right pos_re">

                        <input type="checkbox" id="checkbox1" ${aj.flg==1? 'checked':''} value="1" onclick="wuliuCount('${aj.aj}')" />
                        <label for="checkbox1" style="padding-top: 8px">数据去重</label>
                        <div class=" ">
                            <div>
                                <form action="/SINOFAITH/wuliujjxx/SeachCode" method="post">
                                    <div class="form-group_search  fl_l width100" >
                                        <span style="margin-left: 10px;color: #444;padding-bottom: 10px;">查询方式</span>
                                        <select name="seachCondition" class="width100" STYLE="margin-bottom: 20px;">
                                            <option value="waybill_id"<c:if test="${seachCondition=='waybill_id'}">selected="selected"</c:if>>运单号</option>
                                            <option value="sender" <c:if test="${seachCondition=='sender'}">selected="selected"</c:if> >寄件人</option>
                                            <option value="addressee" <c:if test="${seachCondition=='addressee'}">selected="selected"</c:if> >收件人</option>
                                            <option value="tjw" <c:if test="${seachCondition=='tjw'}">selected="selected"</c:if> >托寄物</option>

                                        </select>
                                        <%--<input  style="margin-left: 10px;" type="checkbox" name="usable" value="1" <c:if test="${usable eq '1'}">checked="checked"</c:if>>上次条件有效--%>
                                        <textarea  class="form-control02 seachCode fl_l width100" id="seachCode" placeholder="请输入要查询内容,如果使用模糊查询请加%" name="seachCode" >${seachCode}</textarea>
                                    </div>

                                    <button type="submit" class="right_a_nav margin_none" >查询</button>
                                    <%--<button type="button" class="right_a_nav margin_none add_button" onclick="AddCrimeterrace()">新增人员信息</button>--%>
                                </form>
                            </div>
                            <div class="width100" style="margin-top: 10px;float: left;">
                                <span style="margin-left: 10px;color: #444;padding-bottom: 10px;margin-top: 20px;">物流数据导入/导出</span>
                                <div class="form-group_search loadFile width100" style="margin-top: 5px;height: auto;">
                                    <div class="if_tel width100">
                       <span class="fl_l width100 " style="padding-bottom: 10px;margin-top: 10px;">
                           <%--<button  type="button"  class="sideBar_r_button" id="btnLoadFile" >文件夹导入</button>--%>
                           <c:if test="${!fn:contains(aj.aj, ',')}">
                               <button class="sideBar_r_button" data-toggle="modal" data-target="#myModal">物流数据导入</button>
                           </c:if>
                           <button  type="button"  class="sideBar_r_button"  <c:if test="${aj!=null}">onclick="location.href='/SINOFAITH/wuliujjxx/download'"</c:if>>数据导出</button>
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
    </ul>
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabel">文件上传进度</h4>
            </div>
            <div class="modal-body">
                <progress id="progressBar" value="0" max="100"
                          style="width: 100%;height: 20px; "> </progress>
                <span id="percentage" style="color:blue;"></span> <br>
                <br>
                <div class="file-box">
                    文件夹:<input type='text' name='textfield' id='textfield' class='txt'/>
                    <input type='button' class='btn' value='浏览...' />
                    <input
                            type="file" name="file" webkitdirectory class="file" id="file" size="28"
                            onchange="document.getElementById('textfield').value=this.value;" />
                    <br>
                    案件名:<input type="text" name = 'aj' id ='aj' class='txt' readonly="readonly" value="${aj.aj}">
                    <br>
                    <%--<input type="checkbox" id="checkbox1" ${aj.flg==1? 'checked':''} value="1">--%>
                    <%--<label for="checkbox1" style="padding-top: 8px">统计结果去除红包相关记录</label>--%>
                </div>
            </div>
            <div class="modal-footer">
                <input type="submit" name="submit" class="btn" value="上传"
                       onclick="UploadWuliu()" />
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                </button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>
<%@include file="../template/newfooter.jsp" %>
