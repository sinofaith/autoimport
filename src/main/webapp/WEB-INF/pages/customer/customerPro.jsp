<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@include file="../template/sideBar_left.jsp" %>
<%@include file="../template/newmain.jsp" %>

<%--详情模块脚本--%>

<link href="<c:url value="/resources/css/bootstrap.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/bootstrap-theme.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/css.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/map.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/font.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/select/selectordie.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/select/selectordie_theme_02.css"/>" rel="stylesheet" media="screen">
<script src="<c:url value="/resources/jquery/jquery.js"/> "></script>
<script src="<c:url value="/resources/js/jquery-1.9.1.min.js"/> "></script>
<script src="<c:url value="/resources/js/bootstrap.js"/> "></script>
<script src="<c:url value="/resources/js/bank/bank.js"/> "></script>
<script src="<c:url value="/resources/js/customerpro/customerPro.js"/> "></script>
<script src="<c:url value="/resources/js/select/selectordie.min.js"/> "></script>

<script src="<c:url value="/resources/thirdparty/jquery-form/jquery.form.js"/>" type="text/javascript"></script>
<%--详情模块脚本--%>
<script type="text/javascript">
    try{ace.settings.check('main-container','fixed')}catch(e){}
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
    <%@include file="../customer/customerTitler.jsp" %>
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
                                        <td colspan="10"  align="center" class="dropdown_index" style="background-color: #eee;">
                                            <div class="dropdown " style="color: #333">
                                                <c:if test="${aj!=null}">
                                                    <strong>人员信息(${aj.aj})</strong>
                                                </c:if>
                                                <c:if test="${aj==null}">
                                                    <strong>人员信息</strong>
                                                </c:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr align="center">
                                        <td width="6%">序号</td>
                                        <td width="10%">姓名</td>
                                        <td width="10%">证件号码</td>
                                        <td width="10%">案件数</td>
                                        <td width="10%">银行卡数</td>
                                        <td width="10%">微信数</td>
                                        <td width="10%">支付宝数</td>
                                        <td width="10%">手机数</td>
                                    </tr>
                                        <c:forEach items="${detailinfo}" var="item" varStatus="st">
                                            <tr class="${st.index%2==1 ? '':'odd' }">
                                                <td align="center">${item.xh}</td>
                                                <td align="center" title="${item.name}">
                                                    <div style="width:100px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">
                                                        <c:choose>
                                                            <c:when test="${aj!=null}">
                                                                <button  data-toggle="modal" class="btna" data-target="#myModal" onclick="getPersonDetails('${item.name}')">${item.name}</button>
                                                            </c:when>
                                                            <c:otherwise>
                                                                ${item.name}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </td>
                                                <td align="center">${item.zjhm}</td>
                                                <td align="center">${item.aj}</td>
                                                <td align="center">${item.yhkkh}</td>
                                                <td align="center">${item.cftzh}</td>
                                                <td align="center">${item.zfbzh}</td>
                                                <td align="center">${item.sjh}</td>

                                                <%--<td align="center" title="${item.zjlx}"><div style="width:80px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.zjlx}</div></td>--%>
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
                                        <a href="/SINOFAITH/customerPro/seach?pageNo=${page.topPageNo }"><input type="button" name="fristPage" value="首页" /></a>
                                        <c:choose>
                                            <c:when test="${page.pageNo!=1}">
                                                <a href="/SINOFAITH/customerPro/seach?pageNo=${page.previousPageNo }"><input type="button" name="previousPage" value="上一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="previousPage" value="上一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${page.pageNo != page.totalPages}">
                                                <a href="/SINOFAITH/customerPro/seach?pageNo=${page.nextPageNo }"><input type="button" name="nextPage" value="下一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="nextPage" value="下一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="/SINOFAITH/customerPro/seach?pageNo=${page.bottomPageNo }"><input type="button" name="lastPage" value="尾页" /></a>
                                        <input type="number" id="num" max="${page.totalPages}" style="width: 9%" min="1">
                                        <input type="button" value="跳转" onclick="bankSkip('customer')">
                                    </div>

                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                    <div class="sidebar_right pos_re">

                        <div class=" ">

                            <div>
                                <form action="/SINOFAITH/customerPro/SeachCode" method="post">
                                    <div class="form-group_search  fl_l width100" >
                                        <span style="margin-left: 10px;color: #444;padding-bottom: 10px;">查询方式</span>
                                        <select name="seachCondition" class="width100" STYLE="margin-bottom: 20px;">
                                            <option value="name"<c:if test="${cpseachCondition=='name'}">selected="selected"</c:if>>姓名</option>
                                            <option value="zjh"<c:if test="${cpseachCondition=='zjh'}">selected="selected"</c:if>>证件号码</option>
                                        </select>
                                        <%--<input  style="margin-left: 10px;" type="checkbox" name="usable" value="1" <c:if test="${usable eq '1'}">checked="checked"</c:if>>上次条件有效--%>
                                        <textarea  class="form-control02 seachCode fl_l width100" id="seachCode" placeholder="请输入要查询内容" name="seachCode" >${cpseachCode}</textarea>
                                    </div>

                                    <button type="submit" class="right_a_nav margin_none" >查询</button>
                                    <%--<button type="button" class="right_a_nav margin_none add_button" onclick="AddCrimeterrace()">新增人员信息</button>--%>
                                </form>
                            </div>
                            <c:if test="${aj!=null}">

                                <div class="width100" style="margin-top: 10px;float: left;">
                                    <span style="margin-left: 10px;color: #444;padding-bottom: 10px;margin-top: 20px;">数据导入/导出</span>
                                    <div class="form-group_search loadFile width100" style="margin-top: 5px;height: auto;">
                                        <div class="if_tel width100">
                                            <span class="fl_l width100 " style="padding-bottom: 10px;margin-top: 10px;">
                                             <button  type="button"  class="sideBar_r_button"  onclick="location.href='/SINOFAITH/customerPro/download'" >数据导出</button>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
                <%--<form id="uploadFileForm" action="/SINOFAITH/uploadFolder" method="post"  style="display: none;">--%>
                    <%--<input type="file" webkitdirectory name="file" id="file" style="display: none;">--%>
                    <%--<input type="text" id="updatestate" name="updatestate" style="display: none;" value="1">--%>
                <%--</form>--%>

                <form id="seachDetail" action="<c:url value=""/>"  method="post" style="display: none;">
                </form>
    </ul>
            </div>
        </div>
    </ul>
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="top: 0%; min-width: 96%;left: 2%;right: 2%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabel">目标人员详情</h4>
            </div>
            <div class="modal-body">
                <div align="center">公司信息</div>
                <table class="table  table-hover table_style table_list1 "
                       style="border-left: 1px solid #ccc; border-right: 1px solid #ccc!important;">
                    <thead style="display:table;width:100%;table-layout:fixed;width: calc( 100% - 16.5px );">
                    <tr align="center">
                        <td width="2%">序号</td>
                        <td width="4%">姓名</td>
                        <td width="10%">关联公司</td>
                        <td width="6%">网站链接</td>
                        <td width="15%">地址</td>
                        <td width="20%">备注</td>
                        <td width="10%">公司电话</td>
                        <td width="10%">公司邮箱</td>
                        <td width="5%">更多</td>
                    </tr>
                    <input type="hidden" id="glname" value="">
                    </thead>
                    <tbody id="personCompany" style="display:block;height:50%;overflow-y:scroll;overflow-x: hidden">

                    </tbody>
                </table>
                <hr>
                <div align="center">号码信息</div>
                <table class="table  table-hover table_style table_list1 "
                       style="border-left: 1px solid #ccc; border-right: 1px solid #ccc!important;">
                    <thead style="display:table;width:100%;table-layout:fixed;width: calc( 100% - 16.5px );">
                    <tr align="center">
                        <td width="2%">序号</td>
                        <td width="4%">姓名</td>
                        <td width="8%">手机号</td>
                        <td width="8%">关联账号</td>
                        <td width="8%">关联账号名称</td>
                        <td width="6%">性别</td>
                        <td width="6%">年龄</td>
                        <td width="8%">定位地址</td>
                        <td width="8%">关联账号类型</td>
                        <td width="5%">更多</td>
                    </tr>
                    <input name="label" id="mbname" hidden="hidden" value="">
                    </thead>
                    <tbody id="personNumber" style="display:block;height:50%;overflow-y:scroll;">

                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <%--<input type="submit" name="submit" class="btn" value="上传"--%>
                       <%--onclick="UploadBank()" />--%>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                </button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>

<div class="modal fade" id="myModalCompany" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="top: 10%; min-width: 30%;left: 30%;right: 30%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabelCompany">目标人物公司信息<span id="titleCompany"></span></h4>
            </div>
            <div class="modal-body">
                <form action="" method="post" class="basic-grey" id="companyForm">
                    <input type="hidden" id="companyId" value="">
                    <label>
                        <span>姓名:</span>
                        <input id="cpname" type="text"  name="name" data-toggle="tooltip" data-placement="top" readonly/>
                    </label>
                    <label>
                        <span>公司名称:</span>
                        <input id="companyName" type="text" name="companyName" placeholder="公司名称"
                               data-toggle="tooltip" data-placement="top" />
                    </label>
                    <label>
                        <span>网站链接:</span>
                        <input id="companyWeb" type="text" name="companyWeb" placeholder="网站链接"></label>
                    </label>
                    <label>
                        <span>地址:</span>
                        <input id="companyAdd" type="text" name="companyAdd" placeholder="地址"/>
                    </label>
                    <label>
                        <span>备注:</span>
                        <input id="companyRemark" type="text" name="companyRemark" placeholder="备注"/>
                    </label>
                    <label>
                        <span>公司电话:</span>
                        <input id="companyPhone" type="text" name="companyRemark" placeholder="公司电话"/>
                    </label>
                    <label>
                        <span>公司邮箱:</span>
                        <input id="companyEmail" type="text" name="companyRemark" placeholder="公司邮箱"/>
                    </label>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" id="submitCompany" onclick="addCompany()">添加</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>

<div class="modal fade" id="myModalPhoneNumber" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="top: 10%; min-width: 30%;left: 30%;right: 30%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabel3">目标人物号码信息<span id="titleNumber"></span></h4>
            </div>
            <div class="modal-body">
                <form action="" method="post" class="basic-grey" id="numberForm">
                    <input type="hidden" id="numberId" value="">
                    <label>
                        <span>姓名:</span>
                        <input id="personName" type="text"  name="name" readonly/>
                    </label>
                    <label>
                        <span>手机号:</span>
                        <input id="phone" type="text" name="phone" placeholder="手机号"
                               data-toggle="tooltip" data-placement="top" oninput = "value=value.replace(/[^\d]/g,'')"/>
                    </label>
                    <label>
                        <span>关联账号:</span>
                        <input id="number" type="text" name="number" placeholder="关联账号" oninput = "value=value.replace(/[^\d]/g,'')"></label>
                    </label>
                    <label>
                        <span>关联账号昵称:</span>
                        <input id="numberName" type="text" name="numberName" placeholder="关联账户昵称"/>
                    </label>
                    <label style="padding-bottom: 20px">
                        <span>性别:</span>
                        <input name="sex" type="radio" style="margin-top: 12px" value="男"/><i>男</i>
                        <input name="sex" type="radio" value="女"/><i>女</i>
                    </label>
                    <label>
                        <span>年龄:</span>
                        <input id="age" type="text" name="age" placeholder="年龄"oninput = "value=value.replace(/[^\d]/g,'')"/>
                    </label>
                    <label>
                        <span>定位地址:</span>
                        <input id="address" type="text" name="address" placeholder="定位地址"/>
                    </label>
                    <label>
                        <span>账号类型:</span>
                        <input id="numberType" type="text" name="numberType" placeholder="账号类型"/>
                    </label>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" id="submitNumber" onclick="addNumber()">添加</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>
<%@include file="../template/newfooter.jsp" %>
