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
<script src="<c:url value="/resources/thirdparty/jquery-form/jquery.form.js"/>" type="text/javascript"></script>
<%--详情模块脚本--%>
<script type="text/javascript">
    try{ace.settings.check('main-container','fixed')}catch(e){}
</script>
<style type="text/css">
    .crimeterrace{ background-color: #636B75 !important;}
</style>

<div class="tab_div">
    <%--<span class="tab_nav">
        <a style="width: 10%;" href="/SINOFAITH/zfb" class="addactive">支付宝注册信息</a>
        <a style="width: 10%;" href="/SINOFAITH/zfbZhmx">支付宝账户明细</a>
        <a style="width: 10%;" href="/SINOFAITH/zfbZzmx">支付宝转账明细</a>
        <a style="width: 10%;" href="/SINOFAITH/zfbZzmxTjjg">转账明细统计结果</a>
        <a style="width: 10%;" href="/SINOFAITH/zfbZzmxTjjgs">转账明细对手账户</a>
        <a style="width: 10%;" href="/SINOFAITH/zfbZzmxGtzh">转账明细共同账户</a>
        <a style="width: 10%;" href="/SINOFAITH/zfbJyjl">支付宝交易记录</a>
        <a style="width: 10%;" href="/SINOFAITH/zfbJyjlSjdzs">交易记录地址统计</a>
        <a style="width: 10%;" href="/SINOFAITH/zfbJyjlTjjgs">交易记录对手账户</a>
        <a style="width: 10%;" href="/SINOFAITH/zfbDlrz">支付宝登陆日志</a>
    </span>--%>
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
                                        <td colspan="11"  align="center" class="dropdown_index" style="background-color: #eee;">
                                            <div class="dropdown " style="color: #333">
                                                <strong>支付宝注册信息(${aj.aj})</strong>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr align="center">
                                        <td width="6%">序号</td>
                                        <td width="7%">用户Id</td>
                                        <td width="8%">登陆邮箱</td>
                                        <td width="10%">登陆手机</td>
                                        <td width="8%">账户名称</td>
                                        <td width="10%">证件类型</td>
                                        <td width="15%">证件号</td>
                                        <td width="6%">可用余额</td>
                                        <td width="10%">绑定手机</td>
                                        <td width="15%">绑定银行卡</td>
                                        <td width="6%">店铺名</td>
                                    </tr>
                                    <c:forEach items="${detailinfo}" var="item" varStatus="st">
                                        <tr class="${st.index%2==1 ? '':'odd' }">
                                            <td align="center">${(st.index+1)+(page.pageNo-1)*page.pageSize}</td>
                                            <td align="center">${item.yhId}</td>
                                            <td align="center">${item.dlyx}</td>
                                            <td align="center">${item.dlsj}</td>
                                            <td align="center" title="${item.zhmc}"><div style="width:70px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.zhmc}</div></td>
                                            <td align="center">${item.zjlx}</td>
                                            <td align="center">${item.zjh}</td>
                                            <td align="center">${item.kyye}</td>
                                            <td align="center">${item.bdsj}</td>
                                            <td align="center" title="${item.bdyhk}"><div style="width:150px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.bdyhk}</div></td>
                                            <td align="center">${item.dyxcsj}</td>
                                        </tr>
                                    </c:forEach>
                                    <c:choose>
                                        <c:when test="${detailinfo ==null || detailinfo.size()==0}">
                                            <tr>
                                                <td colspan="11" align="center"> 无数据 </td>
                                            </tr>
                                        </c:when>
                                    </c:choose>

                                </table>

                            </div>
                            <c:choose>
                                <c:when test="${detailinfo!=null && detailinfo.size()!=0}">
                                    <div  class="page_nmber">
                                        <div class="mar_t_15">共${page.totalRecords}条记录 共<span id="totalPage">${page.totalPages}</span>页 当前第${page.pageNo}页<br></div>
                                        <a href="/SINOFAITH/zfb/seach?pageNo=${page.topPageNo }"><input type="button" name="fristPage" value="首页" /></a>
                                        <c:choose>
                                            <c:when test="${page.pageNo!=1}">
                                                <a href="/SINOFAITH/zfb/seach?pageNo=${page.previousPageNo }"><input type="button" name="previousPage" value="上一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="previousPage" value="上一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${page.pageNo != page.totalPages}">
                                                <a href="/SINOFAITH/zfb/seach?pageNo=${page.nextPageNo }"><input type="button" name="nextPage" value="下一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="nextPage" value="下一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="/SINOFAITH/zfb/seach?pageNo=${page.bottomPageNo }"><input type="button" name="lastPage" value="尾页" /></a>
                                        <input type="number" id="num" max="${page.totalPages}" style="width: 9%" min="1">
                                        <input type="button" value="跳转" onclick="zfbSkip('')">
                                    </div>

                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                    <div class="sidebar_right pos_re">

                        <div class=" ">

                            <div>
                                <form action="/SINOFAITH/zfb/SeachCode" method="post">
                                    <div class="form-group_search  fl_l width100" >
                                        <span style="margin-left: 10px;color: #444;padding-bottom: 10px;">查询方式</span>
                                        <select name="seachCondition" class="width100" STYLE="margin-bottom: 20px;">
                                            <option value="yhId"<c:if test="${zcxxSeachCondition=='yhId'}">selected="selected"</c:if>>用户Id</option>
                                            <option value="dlsj" <c:if test="${zcxxSeachCondition=='dlsj'}">selected="selected"</c:if> >登陆手机</option>
                                            <option value="zhmc" <c:if test="${zcxxSeachCondition=='zhmc'}">selected="selected"</c:if> >账户名称</option>
                                        </select>
                                        <textarea  class="form-control02 seachCode fl_l width100" id="seachCode" placeholder="请输入要查询内容,如果使用模糊查询请加%" name="seachCode" >${zcxxSeachCode}</textarea>
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
                           <%--<button  type="button"  class="sideBar_r_button" id="btnLoadFile" >文件夹导入</button>--%>
                           <c:if test="${!fn:contains(aj.aj, ',')}">
                               <button class="sideBar_r_button" data-toggle="modal" data-target="#myModal">支付宝数据导入</button>
                           </c:if>
                           <button  type="button"  class="sideBar_r_button"  onclick="location.href='/SINOFAITH/zfb/download'" >数据导出</button>
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
                    <%--<input type="checkbox" id="checkbox1" ${aj.flg==1? 'checked':''} value="1">
                    <label for="checkbox1" style="padding-top: 8px">统计结果去除红包相关记录</label>--%>
                </div>
            </div>
            <div class="modal-footer">
                <input type="submit" name="submit" class="btn" value="上传"
                       onclick="UploadZfb()" />
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                </button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>
<%@include file="../template/newfooter.jsp" %>
