<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<script src="<c:url value="/resources/js/cftinfo.js"/> "></script>
<script src="<c:url value="/resources/js/raydreams.js"/> "></script>
<script src="<c:url value="/resources/js/cft/cftPreview.js"/> "></script>
<script src="<c:url value="/resources/thirdparty/jquery-form/jquery.form.js"/>" type="text/javascript"></script>
<%--详情模块脚本--%>
<script type="text/javascript">
    try{ace.settings.check('main-container','fixed')}catch(e){}

    running = true;
    function getBq(obj){
        try {
            var wxzh = obj.innerText
            var skip = window.document.getElementById("skip" + obj.parentElement.rowIndex)
            if (skip.innerText == "" && running) {
                running = false;
                $.get("/SINOFAITH/cft/getBq?wxzh=" + wxzh, function (result) {
                    var str = "";
                    if (result.zzsum > 0) {
                        str += "<a href='/SINOFAITH/cftzzxx/seachByUrl?wxzh=" + wxzh + "'>交易信息</a>"
                    }
                    if (result.tjsum > 0) {
                        str += "<a href='/SINOFAITH/cfttjjg/seachByUrl?wxzh=" + wxzh + "'>账户信息</a>"
                    }
                    if (result.tssum > 0) {
                        str += "<a href='/SINOFAITH/cfttjjgs/seachByUrl?wxzh=" + wxzh + "'>对手账户信息</a>"
                    }
                    if (result.gtsum > 0) {
                        str += "<a href='/SINOFAITH/cftgtzh/seachByUrl?wxzh=" + wxzh + "'>共同账户信息</a>"
                    }
                    if (result.zzsum + result.tjsum + result.tssum + result.gtsum == 0) {
                        str += "<a href='#'>无更多信息</a>"
                    }
                    skip.innerHTML = str;
                    running = true
                }, "json")
            }
        }catch(e){
            running = true;
        }
    }
</script>
<style type="text/css">
    .crimeterrace{ background-color: #636B75 !important;}
    .dropCss {
        position: relative;
        display: inline-block;
    }

    .dropCss-content {
        display: none;
        position: absolute;
        top:-10%;
        left:100%;
        background-color: #f9f9f9;
        min-width: 100px;
        box-shadow: 0px 4px 4px 0px rgba(0,0,0,0.4);
        border-radius: 6px;
    }

    .dropCss-content a {
        color: black;
        padding: 3px 3px;
        text-decoration: none;
        display: block;
    }

    .dropCss-content a:hover {
        background-color: #bbb;
    }

    .dropCss:hover .dropCss-content {
        display: block;
    }
</style>

<div class="tab_div">
    <span class="tab_nav">  <a  href="/SINOFAITH/cft" class="addactive">财付通注册信息</a><a href="/SINOFAITH/cftzzxx">财付通转账信息</a><a href="/SINOFAITH/cfttjjg">财付通账户信息</a><a href="/SINOFAITH/cfttjjgs">财付通对手账户信息</a><a href="/SINOFAITH/cftgtzh">财付通共同账户信息</a></span>
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
                                                <strong>财付通注册信息(${aj.aj})</strong>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr align="center">
                                        <td width="6%">序号</td>
                                        <td width="7%">账户状态</td>
                                        <td width="12%">微信账户</td>
                                        <td width="6%">姓名</td>
                                        <td width="14%">注册时间</td>
                                        <td width="14%">身份证号</td>
                                        <td width="10%">绑定手机</td>
                                        <td width="18%">开户行</td>
                                        <td width="16%">银行账号</td>
                                    </tr>
                                        <c:forEach items="${detailinfo}" var="item" varStatus="st">
                                            <tr class="${st.index%2==1 ? '':'odd' }">
                                                <td align="center">${item.id}</td>
                                                <td align="center">${item.zhzt}</td>
                                                <td align="center" onmouseout="getBq(this)">
                                                    <div class="dropCss">
                                                        <span style="color: #6698ce">${item.zh}</span>
                                                        <div class="dropCss-content" id="skip${st.index+2}">
                                                        </div>
                                                    </div>
                                                </td>
                                                <td align="center">${item.xm}</td>
                                                <td align="center">${item.zcsj}</td>
                                                <td align="center">${item.sfzhm}</td>
                                                <td align="center">${item.bdsj}</td>
                                                <td align="center">${item.khh}</td>
                                                <td align="center">${item.yhzh}</td>
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
                                        <a href="/SINOFAITH/cft/seach?pageNo=${page.topPageNo }"><input type="button" name="fristPage" value="首页" /></a>
                                        <c:choose>
                                            <c:when test="${page.pageNo!=1}">
                                                <a href="/SINOFAITH/cft/seach?pageNo=${page.previousPageNo }"><input type="button" name="previousPage" value="上一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="previousPage" value="上一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${page.pageNo != page.totalPages}">
                                                <a href="/SINOFAITH/cft/seach?pageNo=${page.nextPageNo }"><input type="button" name="nextPage" value="下一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="nextPage" value="下一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="/SINOFAITH/cft/seach?pageNo=${page.bottomPageNo }"><input type="button" name="lastPage" value="尾页" /></a>
                                        <input type="number" id="num" max="${page.totalPages}" style="width: 9%" min="1">
                                        <input type="button" value="跳转" onclick="cftSkip('')">
                                    </div>

                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                    <div class="sidebar_right pos_re">

                        <div class=" ">

                            <div>
                                <form action="/SINOFAITH/cft/SeachCode" method="post">
                                    <div class="form-group_search  fl_l width100" >
                                        <span style="margin-left: 10px;color: #444;padding-bottom: 10px;">查询方式</span>
                                        <select name="seachCondition" class="width100" STYLE="margin-bottom: 20px;">
                                            <option value="zh"<c:if test="${zcseachCondition=='zh'}">selected="selected"</c:if>>微信账户</option>
                                            <option value="xm" <c:if test="${zcseachCondition=='xm'}">selected="selected"</c:if> >姓名</option>
                                            <option value="sfzhm" <c:if test="${zcseachCondition=='sfzhm'}">selected="selected"</c:if> >身份证号码</option>
                                            <%--<option value="gszcm" <c:if test="${seachCondition=='gszcm'}">selected="selected"</c:if> >公司注册账号</option>--%>
                                            <%--<option value="gsmc" <c:if test="${seachCondition=='gsmc'}">selected="selected"</c:if> >公司名称</option>--%>
                                            <option value="bdsj" <c:if test="${zcseachCondition=='bdsj'}">selected="selected"</c:if> >手机号</option>
                                            <option value="yhzh" <c:if test="${zcseachCondition=='yhzh'}">selected="selected"</c:if> >银行账号</option>

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
                                        <textarea  class="form-control02 seachCode fl_l width100" id="seachCode" placeholder="请输入要查询内容" name="seachCode" >${zcseachCode}</textarea>
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
                           <%--<button  type="button"  class="sideBar_r_button" id="btnLoadFile" >文件夹导入</button>--%>
                           <c:if test="${!fn:contains(aj.aj, ',')}">
                                <button class="sideBar_r_button" data-toggle="modal" data-target="#myModal">财付通数据导入</button>
                           </c:if>
                           <button  type="button"  class="sideBar_r_button" onclick="location.href='/SINOFAITH/cft/download'" >数据导出</button>
                           <button  type="button"  class="sideBar_l_button" data-toggle="modal" data-target="#myModal2" onclick="yjdcDownload()">一键导出</button>
                           <button  type="button"  class="sideBar_r_button" <c:if test="${aj!=null && detailinfo.size()!=0}">onclick="location.href='/SINOFAITH/cft/createPDF'"</c:if>>生成PDF报告文档</button>
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
                    <label for="checkbox1" class="label_c" style="padding-top: 8px">统计结果去除红包相关记录</label>
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
                            <label for="excelName">Excel名</label>
                        </span>
                        <span class="col-md-1" id="excelSheet" style="width: 200px;">
                            <label for="excelSheet">Sheet名</label>
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
                            <label for="c1">账号</label>
                            <select	 id="c1" placeholder="账号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c2">交易单号</label>
                            <select	 id="c2" placeholder="交易单号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c3">借贷类型</label>
                            <select	 id="c3" placeholder="借贷类型" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c4">交易类型</label>
                            <select	 id="c4" placeholder="交易类型" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c5">交易金额</label>
                            <select	id="c5" placeholder="交易金额" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c6">账户余额</label>
                            <select	id="c6" placeholder="账户余额" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c7">交易时间</label>
                            <select id="c7" placeholder="交易时间" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c8">银行类型</label>
                            <select	id="c8" placeholder="银行类型" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c9">交易说明</label>
                            <select id="c9" placeholder="交易说明" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c10">商户名称</label>
                            <select	id="c10" placeholder="商户名称" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c11">发送方</label>
                            <select	id="c11" placeholder="发送方" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c12">发送金额</label>
                            <select	id="c12" placeholder="发送金额" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c13">接收方</label>
                            <select	id="c13" placeholder="接收方" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c14">接收时间</label>
                            <select	id="c14" placeholder="接收时间" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c15">接收金额</label>
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
<div class="modal fade" id="myModal2" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="top: 0%; min-width: 60%;margin-left: 5%;right: 5%;height: 500px">
        <div class="modal-content" style="margin-left: -50%;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabel2">财付通分析数据一键导出(预览)<span id="title"></span></h4>
                <select	 id="a1" placeholder="分析数据类别" onchange="select_change()">
                    <option value="cftTjjg" selected="selected">财付通账户信息</option>
                    <option value="cftTjjgs">财付通对手账户信息</option>
                    <option value="cftGtzh">财付通共同账户信息</option>
                </select>&nbsp;&nbsp;&nbsp;
                出账总金额阀值:<input type="number" style="width:120px" value="50000" class="txt" id="czje">
                进账总金额阀值:<input type="number" style="width:120px" value="50000" class="txt" id="jzje">
                <input type="button" class="btn btn-primary" value="预览数据" onclick="yjdcDownload(true)">
            </div>
            <div class="modal-body">
                <div id="dataTable" class="table table-striped table-bordered" style="height:402px">
                </div>
                <button id="yjdc" type="button" style="margin-left: 1280px;top: 50px;" class="btn btn-primary" onclick="determineThresholdValue()">确定阀值</button>
                <button id="next" type="button" style="margin-left: 1200px;top: 25px;" class="btn btn-primary" onclick="nextYjdc()">下一个</button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="batchExport()">批量导出</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>
<%@include file="../template/newfooter.jsp" %>
