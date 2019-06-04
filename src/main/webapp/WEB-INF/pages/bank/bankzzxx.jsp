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
<script src="<c:url value="/resources/jquery/jquery.js"/> "></script>
<script src="<c:url value="/resources/js/jquery-1.9.1.min.js"/> "></script>
<script src="<c:url value="/resources/js/bootstrap.js"/> "></script>
<script src="<c:url value="/resources/js/select/selectordie.min.js"/> "></script>
<script src="<c:url value="/resources/js/raydreams.js"/> "></script>
<script src="<c:url value="/resources/js/bank/bank.js"/> "></script>
<script src="<c:url value="/resources/js/bank/bankPreview.js"/> "></script>
<script src="<c:url value="/resources/thirdparty/jquery-form/jquery.form.js"/>" type="text/javascript"></script>
<%--详情模块脚本--%>
<script type="text/javascript">
    try{ace.settings.check('main-container','fixed')}catch(e){}
</script>
<style type="text/css">
    .crimeterrace{ background-color: #636B75 !important;}
</style>

<div class="tab_div">
    <%@include file="../bank/bankTitler.jsp" %>
    <ul>
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
                                                <strong>资金交易明细(${aj.aj})</strong>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr align="center">
                                        <td width="5%">序号</td>
                                        <td width="12%">交易卡号</td>
                                        <td width="6%">交易户名</td>
                                        <td width="12%"><a href="/SINOFAITH/bankzzxx/order?orderby=jysj">交易时间</a></td>
                                        <td width="9%"><a href="/SINOFAITH/bankzzxx/order?orderby=jyje">交易金额</a></td>
                                        <td width="9%">交易余额</td>
                                        <td width="5%">收付标志</td>
                                        <td width="12%">对手卡号</td>
                                        <td width="6%">对手户名</td>
                                        <td width="4%">摘要说明</td>
                                        <td width="4%">交易发生地</td>
                                        <td width="4%">交易网点名称</td>
                                        <td width="5%">备注</td>
                                    </tr>
                                        <c:forEach items="${detailinfo}" var="item" varStatus="st">
                                            <tr class="${st.index%2==1 ? '':'odd' }">
                                                <td align="center">${item.id}</td>
                                                <td align="center">${item.yhkkh}</td>
                                                <td align="center" title="${item.jyxm}"><div style="width:40px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.jyxm}</div></td>
                                                <td align="center">${item.jysj}</td>
                                                <td align="center"><fmt:formatNumber value="${item.jyje}" pattern="#,##0"/></td>
                                                <td align="center"><fmt:formatNumber value="${item.jyye==-1?'':item.jyye}" pattern="#,##0"/></td>
                                                <td align="center">${item.sfbz}</td>
                                                <td align="center" title="${item.dskh}"><div style="width:140px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.dskh}</div></td>
                                                <td align="center" title="${item.dsxm}"><div style="width:40px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.dsxm}</div></td>
                                                <td align="center" title="${item.zysm}"><div style="width:50px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.zysm}</div></td>
                                                <td align="center" title="${item.jyfsd}"><div style="width:60px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.jyfsd}</div></td>
                                                <td align="center" title="${item.jywdmc}"><div style="width:80px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.jywdmc}</div></td>
                                                <td align="center" title="${item.bz}"><div style="width:60px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.bz}</div></td>
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
                                        <a href="/SINOFAITH/bankzzxx/seach?pageNo=${page.topPageNo }"><input type="button" name="fristPage" value="首页" /></a>
                                        <c:choose>
                                            <c:when test="${page.pageNo!=1}">
                                                <a href="/SINOFAITH/bankzzxx/seach?pageNo=${page.previousPageNo }"><input type="button" name="previousPage" value="上一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="previousPage" value="上一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${page.pageNo != page.totalPages}">
                                                <a href="/SINOFAITH/bankzzxx/seach?pageNo=${page.nextPageNo }"><input type="button" name="nextPage" value="下一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="nextPage" value="下一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="/SINOFAITH/bankzzxx/seach?pageNo=${page.bottomPageNo }"><input type="button" name="lastPage" value="尾页" /></a>
                                        <input type="number" id="num" max="${page.totalPages}" style="width: 9%" min="1">
                                        <input type="button" value="跳转" onclick="bankSkip('zzxx')">
                                    </div>

                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                    <div class="sidebar_right pos_re">

                        <div class=" ">

                            <div>
                                <form action="/SINOFAITH/bankzzxx/SeachCode" method="post">
                                    <div class="form-group_search  fl_l width100" >
                                        <span style="margin-left: 10px;color: #444;padding-bottom: 10px;">查询方式</span>
                                        <select name="seachCondition" class="width100" STYLE="margin-bottom: 20px;">
                                            <option value="yhkkh" <c:if test="${bzzseachCondition=='yhkkh'}">selected="selected"</c:if> >交易卡号</option>
                                            <option value="khxm"<c:if test="${bzzseachCondition=='khxm'}">selected="selected"</c:if>>交易户名</option>
                                            <option value="dskh" <c:if test="${bzzseachCondition=='dskh'}">selected="selected"</c:if> >对手卡号</option>
                                            <option value="dsxm" <c:if test="${bzzseachCondition=='dsxm'}">selected="selected"</c:if> >对手户名</option>
                                            <option value="zysm" <c:if test="${bzzseachCondition=='zysm'}">selected="selected"</c:if> >摘要说明</option>
                                            <option value="jywdmc" <c:if test="${bzzseachCondition=='jywdmc'}">selected="selected"</c:if> >交易网点名称</option>
                                            <option value="bz" <c:if test="${bzzseachCondition=='bz'}">selected="selected"</c:if> >备注</option>

                                        </select>
                                        <%--<input  style="margin-left: 10px;" type="checkbox" name="usable" value="1" <c:if test="${usable eq '1'}">checked="checked"</c:if>>上次条件有效--%>
                                        <textarea  class="form-control02 seachCode fl_l width100" id="seachCode" placeholder="请输入要查询内容" name="seachCode" >${bzzseachCode}</textarea>
                                    </div>

                                    <button type="submit" class="right_a_nav margin_none" >查询</button>
                                    <%--<button type="button" class="right_a_nav margin_none add_button" onclick="AddCrimeterrace()">新增人员信息</button>--%>
                                </form>
                            </div>
                            <div class="width100" style="margin-top: 10px;float: left;">
                                <span style="margin-left: 10px;color: #444;padding-bottom: 10px;margin-top: 20px;">银行卡数据导入/导出</span>
                                <div class="form-group_search loadFile width100" style="margin-top: 5px;height: auto;">
                                    <div class="if_tel width100">
                       <span class="fl_l width100 " style="padding-bottom: 10px;margin-top: 10px;">
                           <%--<button  type="button"  class="sideBar_r_button" id="btnLoadFile" >文件夹导入</button>--%>
                           <c:if test="${!fn:contains(aj.aj, ',')}">
                                <button class="sideBar_r_button" data-toggle="modal" data-target="#myModal5">银行卡数据导入</button>
                           </c:if>
                           <button  type="button"  class="sideBar_r_button"  onclick="location.href='/SINOFAITH/bankzzxx/download'" >数据导出</button>
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

<div class="modal fade" id="myModal5" tabindex="-1" role="dialog"
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
                    导入方式:
                    <div class="radio radio-info radio-inline">
                        <input type="radio" id="inlineRadio1" value="" name="radioInline" checked>
                        <label class="label_c" for="inlineRadio1"> 自动导入 </label>
                    </div>
                    <div class="radio radio-inline">
                        <input type="radio" id="inlineRadio2" value="xlsx" name="radioInline">
                        <label class="label_c" for="inlineRadio2"> 映射导入 </label>
                    </div>
                    <%--<input type="checkbox" id="checkbox1" ${aj.flg==1? 'checked':''} value="1">--%>
                    <%--<label for="checkbox1" style="padding-top: 8px">统计结果去除红包相关记录</label>--%>
                </div>
            </div>
            <div class="modal-footer">
                <input type="submit" name="submit" class="btn" value="上传"
                       onclick="UploadBank()" />
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
                    <div class="row" style="width: 900px;">
                        <span class="col-md-1" id="excelName" style="width: 350px;">
                            <label class="label_c" for="excelName">Excel名</label>
                        </span>
                        <span class="col-md-1" id="excelSheet" style="width: 200px;">
                            <label class="label_c" for="excelSheet">Sheet名</label>
                        </span>
                        <span class="col-md-1" id="tableName" style="width: 200px;">
                            <label class="label_c" for="excelSheet">数据库表名</label>
                            <select class="form-control" id="c45" onchange='insertMappingFields()'>
                                <option value='bank_zcxx' selected>资金开户信息表</option>
                                <option value='bank_zzxx'>资金交易明细表</option>
                                <option value='bank_customer'>资金人员信息表</option>
                            </select>
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
                    <div class="row" id="bank_zcxx">
                        <div class="col-md-1">
                            <label class="label_c" for="c1">账号状态</label>
                            <select	 id="c1" placeholder="账号状态" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c2">交易卡号</label>
                            <select	 id="c2" placeholder="交易卡号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c3">姓名</label>
                            <select	 id="c3" placeholder="姓名" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c4">证件号</label>
                            <select	 id="c4" placeholder="证件号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c5">开户时间</label>
                            <select	id="c5" placeholder="开户时间" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c6">开户行</label>
                            <select	id="c6" placeholder="开户行" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c7">账户余额</label>
                            <select id="c7" placeholder="账户余额" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c8">可用余额</label>
                            <select	id="c8" placeholder="可用余额" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c9">交易账号</label>
                            <select id="c9" placeholder="交易账号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c10">账号类型</label>
                            <select id="c10" placeholder="账号类型" onchange="selectC()">
                            </select>
                        </div>
                    </div>
                    <div class="row" id="bank_zzxx" style="display: none">
                        <div class="col-md-1">
                            <label class="label_c" for="c11">交易卡号*</label>
                            <select	 id="c11" placeholder="交易账卡号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c30">交易姓名</label>
                            <select id="c30" placeholder="交易姓名" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c28">交易证件号</label>
                            <select id="c28" placeholder="交易证件号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c12">交易日期*</label>
                            <select	 id="c12" placeholder="交易日期" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c31">交易时间</label>
                            <select id="c31" placeholder="交易时间" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c13">交易金额/借方发生额*</label>
                            <select	 id="c13" placeholder="交易金额" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c14">交易余额</label>
                            <select	 id="c14" placeholder="交易余额" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <!--<label for="c15"><a href="#" onclick="$('#myModal3').modal('show')">收付标志</a></label>-->
                            <label class="label_c" style="text-decoration:underline" data-toggle="tooltip"
                                   data-placement="top" data-html="true"  title="收付标志仅支持<br>进/出、收/付、贷/借" for="c15">收付标志/贷方发生额*</label>
                            <select	id="c15" placeholder="收付标志" onchange="selectC()">
                            </select>
                        </div>
                        <%--<div class="col-md-1">--%>
                        <%--<label for="c16">对手账号</label>--%>
                        <%--<select	id="c16" placeholder="对手卡号" onchange="selectC()">--%>
                        <%--</select>--%>
                        <%--</div>--%>



                        <%--<div class="col-md-1">--%>
                        <%--<label for="c20">交易是否成功</label>--%>
                        <%--<select id="c20" placeholder="交易是否成功" onchange="selectC()">--%>
                        <%--</select>--%>
                        <%--</div>--%>
                        <%--<div class="col-md-1">--%>
                        <%--<label for="c21">交易账号</label>--%>
                        <%--<select id="c21" placeholder="交易账号" onchange="selectC()">--%>
                        <%--</select>--%>
                        <%--</div>--%>
                        <div class="col-md-1">
                            <label class="label_c" for="c22">对手卡号*</label>
                            <select id="c22" placeholder="对手卡号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c17">对手户名</label>
                            <select id="c17" placeholder="对手户名" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c18">对手证件号</label>
                            <select	id="c18" placeholder="对手证件号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c23">对手开户行</label>
                            <select id="c23" placeholder="对手开户行" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c19">摘要说明</label>
                            <select id="c19" placeholder="摘要说明" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c24">交易网点名称</label>
                            <select id="c24" placeholder="交易网点名称" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c29">交易发生地</label>
                            <select id="c29" placeholder="交易发生地" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c27">备注</label>
                            <select id="c27" placeholder="备注" onchange="selectC()">
                            </select>
                        </div>
                        <%--<div class="col-md-1">--%>
                        <%--<label for="c25">对手交易余额</label>--%>
                        <%--<select id="c25" placeholder="对手交易余额" onchange="selectC()">--%>
                        <%--</select>--%>
                        <%--</div>--%>
                        <%--<div class="col-md-1">--%>
                        <%--<label for="c26">对手余额</label>--%>
                        <%--<select id="c26" placeholder="对手余额" onchange="selectC()">--%>
                        <%--</select>--%>
                        <%--</div>--%>



                        <%--<div class="col-md-1">--%>
                        <%--<label for="c31">补充说明</label>--%>
                        <%--<select id="c31" placeholder="交易姓名" onchange="selectC()">--%>
                        <%--</select>--%>
                        <%--</div>--%>
                    </div>
                    <div class="row" id="bank_customer" style="display: none">
                        <div class="col-md-1">
                            <label class="label_c" for="c32">证件号码</label>
                            <select	 id="c32" placeholder="证件号码" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" class="label_c" for="c33">单位电话</label>
                            <select	 id="c33" placeholder="单位电话" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" class="label_c" for="c34">单位地址</label>
                            <select	 id="c34" placeholder="单位地址" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c35">邮箱</label>
                            <select	 id="c35" placeholder="邮箱" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c36">工作单位</label>
                            <select	 id="c36" placeholder="工作单位" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c37">联系电话</label>
                            <select	 id="c37" placeholder="联系电话" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c38">联系手机</label>
                            <select	 id="c38" placeholder="联系手机" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c39">姓名</label>
                            <select	 id="c39" placeholder="姓名" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c40">现住址</label>
                            <select	 id="c40" placeholder="现住址" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c41">证件类型</label>
                            <select	 id="c41" placeholder="证件类型" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c42">住宅电话</label>
                            <select	 id="c42" placeholder="住宅电话" onchange="selectC()">
                            </select>
                        </div>
                    </div>
                    <button id="nextSelect" type="button" style="margin-left: 1200px;top: 25px;" class="btn btn-primary" onclick="nextSelect()">下一个</button>
                    <button id="mapping" type="button" style="margin-left: 1280px" class="btn btn-primary" onclick="uploadMapping()">提交映射</button>
                    <span style="float: right;">
                        <input type="checkbox" id="cb1" checked value="1" onclick="cbxNextSelect()">
                        <label style="color: black; font-size: 14px;font-weight:bold;padding-top: 6px;" for="cb1">映射应用到所有文件</label>
                    </span>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="uploadBankExcel()">导入数据</button>
            </div>
        </div>
    </div>
</div>
<%@include file="../template/newfooter.jsp" %>
