<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@include file="../template/sideBar_left.jsp" %>
<%@include file="../template/newmain.jsp" %>

<%--详情模块脚本--%>

<link href="<c:url value="/resources/css/bootstrap.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/build.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/bootstrap-theme.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/css.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/map.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/font.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/select/selectordie.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/select/selectordie_theme_02.css"/>" rel="stylesheet" media="screen">
<script src="<c:url value="/resources/js/jquery-1.9.1.min.js"/> "></script>
<script src="<c:url value="/resources/jquery/jquery.js"/> "></script>
<script src="<c:url value="/resources/js/select/selectordie.min.js"/> "></script>
<script src="<c:url value="/resources/js/bootstrap.js"/> "></script>
<script src="<c:url value="/resources/js/cftinfo.js"/> "></script>
<script src="<c:url value="/resources/js/raydreams.js"/> "></script>
<script src="<c:url value="/resources/js/cft/cftPreview.js"/> "></script>
<script src="<c:url value="/resources/thirdparty/jquery-form/jquery.form.js"/>" type="text/javascript"></script>
<%--详情模块脚本--%>
<script type="text/javascript">
    try{ace.settings.check('main-container','fixed')}catch(e){}
</script>
<style>
    .crimeterrace{ background-color: #636B75 !important;}


</style>

<div class="tab_div">
    <span class="tab_nav">  <a  href="/SINOFAITH/cft">财付通注册信息</a><a href="/SINOFAITH/cftzzxx" class="addactive">财付通转账信息</a><a href="/SINOFAITH/cfttjjg">财付通账户信息</a><a href="/SINOFAITH/cfttjjgs">财付通对手账户信息</a><a href="/SINOFAITH/cftgtzh">财付通共同账户信息</a></span>
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
                                        <td colspan="12"  align="center" class="dropdown_index" style="background-color: #eee;">
                                            <div class="dropdown " style="color: #333">
                                                <strong>财付通转账信息(${aj.aj})</strong>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr align="center">
                                        <td width="4%">序号</td>
                                        <td width="5%">姓名</td>
                                        <td width="7%">微信账户</td>
                                        <td width="6%">借贷类型</td>
                                        <td width="8%">交易类型</td>
                                        <td width="14%">商户名称</td>
                                        <td width="8%"><a href="/SINOFAITH/cftzzxx/order?orderby=jyje">交易金额</a></td>
                                        <td width="15%"><a href="/SINOFAITH/cftzzxx/order?orderby=jysj">交易时间</a></td>
                                        <td width="8%">发送方</td>
                                        <td width="8%"><a href="/SINOFAITH/cftzzxx/order?orderby=fsje">发送金额</a></td>
                                        <td width="8%">接收方</td>
                                        <td width="8%"><a href="/SINOFAITH/cftzzxx/order?orderby=jsje">接收金额</a></td>
                                    </tr>
                                        <c:forEach items="${detailinfo}" var="item" varStatus="st">
                                            <tr class="${st.index%2==1 ? '':'odd' }">
                                                <td align="center">${item.id}</td>
                                                <td align="center">${item.name}</td>
                                                <td align="center" title="${item.zh}"><div style="width:70px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.zh}</div></td>
                                                <td align="center">${item.jdlx}</td>
                                                <td align="center">${item.jylx}</td>
                                                <td align="center" title="${item.shmc}"><div style="width:150px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.shmc}</div></td>
                                                <td align="center">${item.jyje}</td>
                                                <td align="center">${item.jssj eq null? item.jysj:item.jssj}</td>
                                                <td align="center" title="${item.fsf}"><div style="width:80px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.fsf}</div></td>
                                                <td align="center">${item.fsje eq 0 ? "":item.fsje}</td>
                                                <td align="center" title="${item.jsf}"><div style="width:80px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.jsf}</div></td>
                                                <td align="center">${item.jsje eq 0 ? "":item.jsje}</td>
                                            </tr>

                                        </c:forEach>
                                        <c:choose>
                                            <c:when test="${detailinfo ==null || detailinfo.size()==0}">
                                                <tr>
                                                    <td colspan="12" align="center"> 无数据 </td>
                                                </tr>
                                            </c:when>
                                        </c:choose>

                                </table>

                            </div>
                            <c:choose>
                                <c:when test="${detailinfo!=null && detailinfo.size()!=0}">
                                    <div  class="page_nmber">
                                        <div class="mar_t_15">共${page.totalRecords}条记录 共<span id="totalPage">${page.totalPages}</span>页 当前第${page.pageNo}页<br></div>
                                        <a href="/SINOFAITH/cftzzxx/seach?pageNo=${page.topPageNo }"><input type="button" name="fristPage" value="首页" /></a>
                                        <c:choose>
                                            <c:when test="${page.pageNo!=1}">
                                                <a href="/SINOFAITH/cftzzxx/seach?pageNo=${page.previousPageNo }"><input type="button" name="previousPage" value="上一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="previousPage" value="上一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${page.pageNo != page.totalPages}">
                                                <a href="/SINOFAITH/cftzzxx/seach?pageNo=${page.nextPageNo }"><input type="button" name="nextPage" value="下一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="nextPage" value="下一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="/SINOFAITH/cftzzxx/seach?pageNo=${page.bottomPageNo }"><input type="button" name="lastPage" value="尾页" /></a>
                                        <input type="number" id="num" max="${page.totalPages}" style="width: 9%" min="1">
                                        <input type="button" value="跳转" onclick="cftSkip('zzxx')">
                                    </div>

                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                    <div class="sidebar_right pos_re">

                        <div class=" ">

                            <div>
                                <form action="/SINOFAITH/cftzzxx/SeachCode" method="post">
                                    <div class="form-group_search  fl_l width100" >
                                        <span style="margin-left: 10px;color: #444;padding-bottom: 10px;">查询方式</span>
                                        <select name="seachCondition" class="width100" STYLE="margin-bottom: 20px;">
                                            <option value="zh"<c:if test="${zzseachCondition=='zh'}">selected="selected"</c:if>>微信账户</option>
                                            <option value="xm"<c:if test="${zzseachCondition=='xm'}">selected="selected"</c:if>>姓名</option>
                                            <option value="jylx"<c:if test="${zzseachCondition=='jylx'}">selected="selected"</c:if>>交易类型</option>
                                            <option value="fsf" <c:if test="${zzseachCondition=='fsf'}">selected="selected"</c:if> >发送方</option>
                                            <option value="jsf" <c:if test="${zzseachCondition=='jsf'}">selected="selected"</c:if> >接收方</option>
                                            <%--<option value="gszcm" <c:if test="${seachCondition=='gszcm'}">selected="selected"</c:if> >公司注册账号</option>--%>
                                            <%--<option value="gsmc" <c:if test="${seachCondition=='gsmc'}">selected="selected"</c:if> >公司名称</option>--%>
                                            <%--<option value="bdsj" <c:if test="${seachCondition=='bdsj'}">selected="selected"</c:if> >手机号</option>--%>
                                            <%--<option value="yhzh" <c:if test="${seachCondition=='yhzh'}">selected="selected"</c:if> >银行账号</option>--%>

                                            <%--<option value="zfb" <c:if test="${seachCondition=='zfb'}">selected="selected"</c:if> >支付宝账号</option>--%>
                                            <%--<option value="zh" <c:if test="${seachCondition=='zh'}">selected="selected"</c:if> >财付通账号</option>--%>
                                            <%--<option value="tbmemberid" <c:if test="${seachCondition=='tb'}">selected="selected"</c:if> >淘宝账号</option>--%>
                                            <%--<option value="wxh" <c:if test="${seachCondition=='wxh'}">selected="selected"</c:if> >微信号</option>--%>
                                            <%--<option value="qqh" <c:if test="${seachCondition=='qqh'}">selected="selected"</c:if> >QQ号</option>--%>
                                            <%--<option value="js" <c:if test="${seachCondition=='js'}">selected="selected"</c:if> >角色</option>--%>
                                            <%--<option value="zt" <c:if test="${seachCondition=='zt'}">selected="selected"</c:if> >状态</option>--%>
                                            <%--<option value="gzd" <c:if test="${seachCondition=='gzd'}">selected="selected"</c:if> >关注度</option>--%>
                                            <%--<option value="ssypz" <c:if test="${seachCondition=='ssypz'}">selected="selected"</c:if> >所属研判组</option>--%>
                                            <%--<option value="sfbsdfhc" <c:if test="${seachCondition=='sfbsdfhc'}">selected="selected"</c:if> >是否部署地方核查</option>--%>
                                        </select>
                                        <%--<input  style="margin-left: 10px;" type="checkbox" name="usable" value="1" <c:if test="${usable eq '1'}">checked="checked"</c:if>>上次条件有效--%>
                                        <textarea  class="form-control02 seachCode fl_l width100" id="seachCode" placeholder="请输入要查询内容" name="seachCode" >${zzseachCode}</textarea>
                                    </div>

                                    <button type="submit" class="right_a_nav margin_none" >查询</button>
                                    <%--<button type="button" class="right_a_nav margin_none add_button" onclick="AddCrimeterrace()">新增人员信息</button>--%>
                                </form>
                            </div>
                            <div class="width100" style="margin-top: 10px;float: left;">
                                <span style="margin-left: 10px;color: #444;padding-bottom: 10px;margin-top: 20px;">财付通数据导入/导出</span>
                                <div class="form-group_search loadFile width100" style="margin-top: 5px;height: auto;">
                                    <div class="if_tel width100">
                                       <span class="fl_l width100 " style="padding-bottom: 10px;margin-top: 10px;">
                                        <c:choose>
                                            <c:when test="${user.id==aj.userId}">
                                                <button class="sideBar_r_button" data-toggle="modal" data-target="#myModal">财付通数据导入</button>
                                                <button  type="button"  class="sideBar_r_button"  onclick="location.href='/SINOFAITH/cftzzxx/download'">数据导出</button>                                                                                          </c:when>
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
                <%--<form id="uploadFileForm" action="/SINOFAITH/uploadFolder" method="post" style="display: none;">--%>
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
                    文&nbsp;&nbsp;件&nbsp;&nbsp;夹:<input type='text' name='textfield' id='textfield' class='txt'/>
                    <input type='button' class='btn' value='浏览...' />
                    <input
                            type="file" name="file" webkitdirectory class="file" id="file" size="28"
                            onchange="document.getElementById('textfield').value=this.value;" />
                    <br>
                    案&nbsp;&nbsp;件&nbsp;&nbsp;名:<input type="text" name = 'aj' id ='aj' class='txt' readonly="readonly" value="${aj.aj}">
                    <br>
                    文件类型:
                    <div class="radio radio-info radio-inline">
                        <input type="radio" id="inlineRadio1" value="txt" name="radioInline" checked>
                        <label class="label_c" for="inlineRadio1"> txt </label>
                    </div>
                    <div class="radio radio-inline">
                        <input type="radio" id="inlineRadio2" value="xlsx" name="radioInline">
                        <label class="label_c" for="inlineRadio2"> xls/xlsx </label>
                    </div>
                    <br>
                    <input type="checkbox" id="checkbox1" checked value="1">
                    <label for="checkbox1" class="label_c">统计结果去除红包相关记录</label>
                </div>
            </div>
            <div class="modal-footer">
                <input type="submit" name="submit" class="btn" value="上传"
                       onclick="UploadCft()" />
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
    <div class="modal-dialog" style="top: 0%; min-width: 90%;left: 5%;right: 5%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel1">多文件字段映射</h4>
            </div>
            <div class="modal-body" >
                <div class="form-group">
                    <div class="row" style="width: 600px;">
                        <span class="col-md-1" id="excelName" style="width: 350px;">
                            <label class="label_c" for="excelName">Excel名</label>
                        </span>
                        <span class="col-md-1" id="excelSheet" style="width: 200px;">
                            <label class="label_c" for="excelSheet">Sheet名</label>
                        </span>
                    </div>
                </div>

                <div class="modal-body">
                    <div id="roll" style="overflow-x: auto; overflow-y: auto; height: 100px; width:1300px;">
                        <table id="head" class="table  table-hover table_style table_list1 " style="border-left: 1px solid #ccc;">
                        </table>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <div class="col-md-1">
                            <label class="label_c" for="c1">账号</label>
                            <select	 id="c1" placeholder="账号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c2">交易单号</label>
                            <select	 id="c2" placeholder="交易单号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c3">借贷类型</label>
                            <select	 id="c3" placeholder="借贷类型" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c4">交易类型</label>
                            <select	 id="c4" placeholder="交易类型" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c5">交易金额</label>
                            <select	id="c5" placeholder="交易金额" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c6">账户余额</label>
                            <select	id="c6" placeholder="账户余额" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c7">交易时间</label>
                            <select id="c7" placeholder="交易时间" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c8">银行类型</label>
                            <select	id="c8" placeholder="银行类型" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c9">交易说明</label>
                            <select id="c9" placeholder="交易说明" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c10">商户名称</label>
                            <select	id="c10" placeholder="商户名称" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c11">发送方</label>
                            <select	id="c11" placeholder="发送方" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c12">发送金额</label>
                            <select	id="c12" placeholder="发送金额" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c13">接收方</label>
                            <select	id="c13" placeholder="接收方" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c14">接收时间</label>
                            <select	id="c14" placeholder="接收时间" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c15">接收金额</label>
                            <select	id="c15" placeholder="接收金额" onchange="selectC()">
                            </select>
                        </div>
                    </div>
                    <button id="nextSelect" type="button" style="margin-left: 1200px;top: 25px;" class="btn btn-primary" onclick="nextSelect()">下一个</button>
                    <button id="mapping" type="button" style="margin-left: 1280px" class="btn btn-primary" onclick="uploadMapping()">提交映射</button>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="uploadCftExcel()">导入数据</button>
            </div>
        </div>
    </div>
</div>
<%@include file="../template/newfooter.jsp" %>
