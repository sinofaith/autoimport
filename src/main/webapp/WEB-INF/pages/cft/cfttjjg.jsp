<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@include file="../template/sideBar_left.jsp" %>
<%@include file="../template/newmain.jsp" %>

<%--详情模块脚本--%>

<link href="<c:url value="/resources/css/bootstrap.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/bootstrap-theme.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/css.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/map.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/font.css"/>" rel="stylesheet" media="screen">
<script src="<c:url value="/resources/js/jquery-1.9.1.min.js"/> "></script>
<script src="<c:url value="/resources/jquery/jquery.js"/> "></script>
<script src="<c:url value="/resources/js/bootstrap.js"/> "></script>
<script src="<c:url value="/resources/js/cftinfo.js"/> "></script>
<script src="<c:url value="/resources/thirdparty/jquery-form/jquery.form.js"/>" type="text/javascript"></script>


<link href="<c:url value="/resources/css/bootstrap-datetimepicker.min.css"/>" rel="stylesheet" media="screen">
<script src="<c:url value="/resources/js/bootstrap-datetimepicker.js"/> "></script>
<script src="<c:url value="/resources/js/bootstrap-datetimepicker.zh-CN.js"/> "></script>
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
//                    if (result.zzsum > 0) {
//                        str += "<a href='/SINOFAITH/cftzzxx/seachByUrl?wxzh=" + wxzh + "'>交易信息</a>"
//                    }
//                    if (result.tjsum > 0) {
//                        str += "<a href='/SINOFAITH/cfttjjg/seachByUrl?wxzh=" + wxzh + "'>账户信息</a>"
//                    }
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
                    running = true;
                }, "json")
            }
        }catch(e){
            running = true;
        }
    }
</script>
<style>
    .crimeterrace{ background-color: #636B75 !important;}
    .dropCss {
        position: relative;
        display: inline-block;
    }

    .dropCss-content {
        display: none;
        position: absolute;
        top:-10%;
        left:80%;
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
    <span class="tab_nav">  <a href="/SINOFAITH/cft">财付通注册信息</a><a href="/SINOFAITH/cftzzxx">财付通转账信息</a><a href="/SINOFAITH/cfttjjg"  class="addactive">财付通账户信息</a><a href="/SINOFAITH/cfttjjgs">财付通对手账户信息</a><a href="/SINOFAITH/cftgtzh">财付通共同账户信息</a></span>
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
                                                <strong>财付通账户信息(${aj.aj})</strong>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr align="center">
                                        <td width="7%">序号</td>
                                        <td width="7%"><a href="/SINOFAITH/cfttjjg/order?orderby=xm">姓名</a></td>
                                        <td width="8%">微信账户</td>
                                        <td width="12%">交易类型</td>
                                        <td width="4%"><a href="/SINOFAITH/cfttjjg/order?orderby=jyzcs">交 易<br>总次数</a></td>
                                        <td width="4%"><a href="/SINOFAITH/cfttjjg/order?orderby=jzzcs">进 账<br>总次数</a></td>
                                        <td width="8%"><a href="/SINOFAITH/cfttjjg/order?orderby=jzzje">进 账<br>总金额</a></td>
                                        <td width="4%"><a href="/SINOFAITH/cfttjjg/order?orderby=czzcs">出 账<br>总次数</a></td>
                                        <td width="8%"><a href="/SINOFAITH/cfttjjg/order?orderby=czzje">出 账<br>总金额</a></td>
                                        <td width="12%"><a href="/SINOFAITH/banktjjg/order?orderby=minsj">最早交易时间</a></td>
                                        <td width="12%"><a href="/SINOFAITH/banktjjg/order?orderby=maxsj">最晚交易时间</a></td>
                                        <td width="4%">间隔天数(天)</td>
                                        <td width="7%">详情</td>
                                    </tr>
                                        <c:forEach items="${detailinfo}" var="item" varStatus="st">
                                            <tr class="${st.index%2==1 ? '':'odd' }">
                                                <td align="center">${item.id}</td>
                                                <td align="center">${item.name}</td>
                                                <td align="center"  title="${item.jyzh}"onmouseout="getBq(this)">
                                                    <div class="dropCss">
                                                        <div style="width:150px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden; color:#6698ce;">${item.jyzh}</div>
                                                        <div class="dropCss-content" id="skip${st.index+2}">
                                                        </div>
                                                    </div>
                                                </td>
                                                <td align="center">${item.jylx}</td>
                                                <td align="center">${item.jyzcs}</td>
                                                <td align="center">${item.jzzcs}</td>
                                                <td align="center"><fmt:formatNumber value="${item.jzzje}" pattern="#,##0"/></td>
                                                <td align="center">${item.czzcs}</td>
                                                <td align="center"><fmt:formatNumber value="${item.czzje}" pattern="#,##0"/></td>
                                                <td align="center">${item.minsj}</td>
                                                <td align="center">${item.maxsj}</td>
                                                <td align="center">${item.jgsj}</td>
                                                <td align="center">
                                                    <button  data-toggle="modal" class="btna" data-target="#myModal" onclick="getZzDetails(this)">详情</button>
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
                                        <a href="/SINOFAITH/cfttjjg/seach?pageNo=${page.topPageNo }"><input type="button" name="fristPage" value="首页" /></a>
                                        <c:choose>
                                            <c:when test="${page.pageNo!=1}">
                                                <a href="/SINOFAITH/cfttjjg/seach?pageNo=${page.previousPageNo }"><input type="button" name="previousPage" value="上一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="previousPage" value="上一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${page.pageNo != page.totalPages}">
                                                <a href="/SINOFAITH/cfttjjg/seach?pageNo=${page.nextPageNo }"><input type="button" name="nextPage" value="下一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="nextPage" value="下一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="/SINOFAITH/cfttjjg/seach?pageNo=${page.bottomPageNo }"><input type="button" name="lastPage" value="尾页" /></a>
                                        <input type="number" id="num" max="${page.totalPages}" style="width: 9%" min="1">
                                        <input type="button" value="跳转" onclick="cftSkip('tjjg')">
                                    </div>

                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                    <div class="sidebar_right pos_re">

                        <input type="checkbox" id="checkbox1" ${aj.flg==1? 'checked':''} value="1" onclick="ajCount('${aj.aj}')" />
                        <label for="checkbox1" style="padding-top: 8px;font-size: 12px;font-weight:bold">统计结果去除红包相关记录</label>
                        <div class=" ">
                            <div class="form-group">
                                <label for="loginTime" class="col-md-2 control-label" style="width: 25%">开始时间:</label>
                                <div class="input-group date form_date col-md-5" data-link-field="loginTime" >
                                    <input id="start_time" name="start_time"  size="16" type="text" value="${aj.cftminsj}" readonly placeholder="选择起始时间">
                                </div>
                                <label for="loginTime" class="col-md-2 control-label"style="width: 25%">结束时间:</label>
                                <div class="input-group date form_date col-md-5" data-link-field="loginTime" >
                                    <input id="end_time" name="end_time"  size="16" type="text" value="${aj.cftmaxsj}" readonly placeholder="选择结束时间">
                                </div>

                                <input type="hidden" id="loginTime" value="" />
                                <button style="width: 26%;margin-right: 6px; margin-top: 2px;height: 25px;line-height: 25px" onclick="clearTime()" type="button" class="right_a_nav margin_none">清除时间</button>
                                <button style="width: 47%; margin-top: 2px;height: 25px;line-height: 25px" type="button" class="right_a_nav margin_none" onclick="countBysj()">开始分析</button>

                            </div>
                            <div>
                                <form action="/SINOFAITH/cfttjjg/SeachCode" method="post">
                                    <div class="form-group_search  fl_l width100" >
                                        <span style="margin-left: 10px;color: #444;padding-bottom: 10px;">查询方式</span>
                                        <select name="seachCondition" id = "seachCondition" onchange="seachChange()" class="width100" STYLE="margin-bottom: 20px;">
                                            <option value="jyzh"<c:if test="${tjseachCondition=='jyzh'}">selected="selected"</c:if>>微信账户</option>
                                            <option value="xm"<c:if test="${tjseachCondition=='xm'}">selected="selected"</c:if>>姓名</option>
                                            <option value="jylx"<c:if test="${tjseachCondition=='jylx'}">selected="selected"</c:if>>交易类型</option>
                                            <option value="jzzje"<c:if test="${tjseachCondition=='jzzje'}">selected="selected"</c:if>>进账总金额阀值</option>
                                            <option value="czzje"<c:if test="${tjseachCondition=='czzje'}">selected="selected"</c:if>>出账总金额阀值</option>
                                            <%--<option value="xm" <c:if test="${seachCondition=='xm'}">selected="selected"</c:if> >姓名</option>--%>
                                            <%--<option value="sfzhm" <c:if test="${seachCondition=='sfzhm'}">selected="selected"</c:if> >身份证号码</option>--%>
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
                                        <textarea  class="form-control02 seachCode fl_l width100" id="seachCode" placeholder="请输入要查询内容" name="seachCode" >${tjseachCode}</textarea>
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
                                                   <button  type="button"  class="sideBar_r_button"  onclick="location.href='/SINOFAITH/cfttjjg/download'" >数据导出</button>
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
                <form id="uploadFileForm" action="<c:url value=""/>" method="post" style="display: none;">
                    <input type="file" name="file" style="display: none;">
                    <input type="text" id="updatestate" name="updatestate" style="display: none;" value="1">
                </form>

                <form id="seachDetail" action="<c:url value=""/>"  method="post" style="display: none;">
                </form>

                <form id = "zzDetails" action="<c:url value="" />" method="post" style="display: none">
                </form>
            </div>
        </div>
    </ul>
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="top: 0%; min-width: 80%;left: 10%;right: 10%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabel">转账详情<span id="title"></span></h4>
            </div>
            <div class="modal-body">
                <table class="table  table-hover table_style table_list1 " style="border-left: 1px solid #ccc; border-right: 1px solid #ccc!important;">
                    <thead style="display:table;width:100%;table-layout:fixed;width: calc( 100% - 16.5px );">
                        <tr align="center">
                            <td width="4%">序号</td>
                            <td width="5%">姓名</td>
                            <td width="15%">微信账户</td>
                            <td width="6%">
                                <button onclick="orderByFilter('jdlx')">借贷类型</button>
                            </td>
                            <td width="10%">
                                <button onclick="orderByFilter('jylx')">交易类型</button>
                            </td>
                            <td width="14%">商户名称</td>
                            <td width="8%">
                                <button onclick="orderByFilter('jyje')">交易金额</button>
                            </td>
                            <td width="13%">
                                <button onclick="orderByFilter('jysj')">交易时间</button>
                            </td>
                            <td width="15%">
                                <button onclick="orderByFilter('fsf')">发送方</button>
                            </td>
                            <td width="8%">
                                <button onclick="orderByFilter('fsje')">发送金额</button>
                            </td>
                            <td width="15%">
                                <button onclick="orderByFilter('jsf')">接收方</button>
                            </td>
                            <td width="8%">
                                <button onclick="orderByFilter('jsje')">接收金额</button>
                            </td>
                        </tr>
                        <input name="label" id="zh" hidden="hidden" value="">
                        <input name="label" id="jylx" hidden="hidden" value="">
                        <input name="label" id="allRow" hidden="hidden" value="">
                    </thead>
                    <tbody id="result" style="display:block;height:340px;overflow-y:scroll;" onscroll="scrollF()">

                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" onclick="downDetailJylx()">导出</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>
<%@include file="../template/newfooter.jsp" %>
