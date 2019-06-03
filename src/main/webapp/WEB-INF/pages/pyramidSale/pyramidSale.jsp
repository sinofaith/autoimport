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
<link href="<c:url value="/resources/css/select/selectordie.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/select/selectordie_theme_02.css"/>" rel="stylesheet" media="screen">

<script src="<c:url value="/resources/jquery/jquery.js"/> "></script>
<script src="<c:url value="/resources/js/jquery-1.9.1.min.js"/> "></script>
<script src="<c:url value="/resources/js/bootstrap.js"/> "></script>
<script src="<c:url value="/resources/thirdparty/alertify/js/alertify.min.js"/> "></script>
<script src="<c:url value="/resources/js/multiselect.min.js"/> "></script>
<script src="<c:url value="/resources/js/select/selectordie.min.js"/> "></script>
<script src="<c:url value="/resources/thirdparty/jquery-form/jquery.form.js"/>" type="text/javascript"></script>

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
        <a href="/SINOFAITH/pyramidSale" class="addactive">传销会员信息</a>
        <a href="/SINOFAITH/pyramidSaleTier">传销层级信息</a>
        <a href="/SINOFAITH/pyramidSalePolt">传销层级图</a>
        <%--<a  href="/SINOFAITH/wuliuSj">物流收件人信息</a>--%>
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
                                        <td colspan="11"  align="center" class="dropdown_index" style="background-color: #eee;">
                                            <div class="dropdown " style="color: #333">
                                                <strong>传销会员信息(${aj.aj})</strong>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr align="center">
                                        <td width="4%">序号</td>
                                        <td width="6%"><a href="/SINOFAITH/pyramidSale/seach?pageNo=1&orderby=psId">会员编号</a></td>
                                        <td width="8%"><a href="/SINOFAITH/pyramidSale/seach?pageNo=1&orderby=sponsorId">推荐会员编号</a></td>
                                        <td width="6%"><a href="/SINOFAITH/pyramidSale/seach?pageNo=1&orderby=nick_name">姓名</a></td>
                                        <td width="6%">手机号</td>
                                        <td width="4%">性别</td>
                                        <td width="15%">详细地址</td>
                                        <td width="10%">身份证号</td>
                                        <td width="6%"><a href="/SINOFAITH/pyramidSale/seach?pageNo=1&orderby=accountHolder">持卡人</a></td>
                                        <td width="10%">银行名称</td>
                                        <td width="10%">银行卡号</td>
                                    </tr>
                                    <c:forEach items="${detailinfo}"
                                               var="item" varStatus="st">
                                        <tr class="${st.index%2==1 ? '':'odd' }">
                                            <td align="center">${(st.index+1)+(page.pageNo-1)*page.pageSize}</td>
                                            <td align="center">${item.psId}</td>
                                            <td align="center">${item.sponsorId}</td>
                                            <td align="center" title="${item.nick_name}"><div style="width:80px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.nick_name}</div></td>
                                            <td align="center">${item.mobile}</td>
                                            <td align="center">${item.sex}</td>
                                            <td align="center" title="${item.address}"><xmp style="font-family: 'Microsoft YaHei UI';width:200px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;font-size: 12px !important;color: #666;">${item.address}</xmp></td>
                                            <td align="center">${item.sfzhm}</td>
                                            <td align="center">${item.accountHolder}</td>
                                            <td align="center" title="${item.bankName}"><div style="width:160px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.bankName}</div></td>
                                            <td align="center">${item.accountNumber}</td>
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
                                        <a href="/SINOFAITH/pyramidSale/seach?pageNo=${page.topPageNo }"><input type="button" name="fristPage" value="首页" /></a>
                                        <c:choose>
                                            <c:when test="${page.pageNo!=1}">
                                                <a href="/SINOFAITH/pyramidSale/seach?pageNo=${page.previousPageNo }"><input type="button" name="previousPage" value="上一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="previousPage" value="上一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${page.pageNo != page.totalPages}">
                                                <a href="/SINOFAITH/pyramidSale/seach?pageNo=${page.nextPageNo }"><input type="button" name="nextPage" value="下一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="nextPage" value="下一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="/SINOFAITH/pyramidSale/seach?pageNo=${page.bottomPageNo }"><input type="button" name="lastPage" value="尾页" /></a>
                                        <input type="number" id="num" max="${page.totalPages}" style="width: 9%" min="1" >
                                        <input type="button" value="跳转" onclick="pyramidSaleSkip('pyramidSale')">
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
                                <form action="/SINOFAITH/pyramidSale/SeachCode" method="post">
                                    <div class="form-group_search  fl_l width100" >
                                        <span style="margin-left: 10px;color: #444;padding-bottom: 10px;">查询方式</span>
                                        <select name="seachCondition" class="width100" STYLE="margin-bottom: 20px;">
                                            <option value="psId"<c:if test="${psSeachCondition=='psId'}">selected="selected"</c:if>>会员编号</option>
                                            <option value="sponsorId" <c:if test="${psSeachCondition=='sponsorId'}">selected="selected"</c:if> >推荐会员编号</option>
                                            <option value="nick_name" <c:if test="${psSeachCondition=='nick_name'}">selected="selected"</c:if> >姓名</option>
                                            <option value="accountHolder" <c:if test="${psSeachCondition=='accountHolder'}">selected="selected"</c:if> >持卡人</option>

                                        </select>
                                        <%--<input  style="margin-left: 10px;" type="checkbox" name="usable" value="1" <c:if test="${usable eq '1'}">checked="checked"</c:if>>上次条件有效--%>
                                        <textarea  class="form-control02 seachCode fl_l width100" id="seachCode" placeholder="请输入要查询内容" name="seachCode" >${psSeachCode}</textarea>
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
                           <c:if test="${!fn:contains(aj.aj, ',')}">
                               <button class="sideBar_r_button" data-toggle="modal" data-target="#myModal">传销数据导入</button>
                           </c:if>
                           <button  type="button"  class="sideBar_r_button"  <c:if test="${aj!=null && detailinfo!=null && detailinfo.size()!=0}">onclick="location.href='/SINOFAITH/pyramidSale/download'"</c:if>>数据导出</button>
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
                    <input type="file" name="file" webkitdirectory class="file" id="file" size="50"
                            onchange="document.getElementById('textfield').value=this.value;" />
                    <br>
                    案件名:<input type="text" name = 'aj' id ='aj' class='txt' readonly="readonly" value="${aj.aj}">
                    <br>
                </div>
            </div>
            <div class="modal-footer">
                <input type="submit" name="submit" class="btn" value="上传"
                       onclick="UploadPyramidSale()" />
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
                <h4 class="modal-title" id="myModalLabel1">字段映射</h4>
            </div>
            <div class="modal-body" >
                <div class="form-group">
                    <div class="row">
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
                            <label class="label_c" for="c1">会员编号</label>
                            <select id="c1" placeholder="会员编号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c2">推荐人编号</label>
                            <select id="c2" placeholder="推荐人编号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c3">手机号</label>
                            <select id="c3" placeholder="手机号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c4">电话</label>
                            <select id="c4" placeholder="手机号2" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c5">姓名</label>
                            <select id="c5" placeholder="姓名" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c6">性别</label>
                            <select id="c6" placeholder="性别" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c7">详细地址</label>
                            <select id="c7" placeholder="详细地址" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c8">身份证号</label>
                            <select id="c8" placeholder="身份证号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c9">开户行</label>
                            <select id="c9" placeholder="开户行" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c10">持卡人姓名</label>
                            <select id="c10" placeholder="持卡人姓名" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label class="label_c" for="c11">银行卡号</label>
                            <select id="c11" placeholder="银行卡号" onchange="selectC()">
                            </select>
                        </div>
                    </div>
                    <button id="nextSelect" type="button" style="margin-left: 1200px;top: 25px;" class="btn btn-primary" onclick="nextSelect()">下一个</button>
                    <button id="mapping" type="button" style="margin-left: 1280px" class="btn btn-primary" onclick="uploadMapping()">提交映射</button>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="uploadPsSheet()">导入数据</button>
            </div>
        </div>
    </div>
</div>
<%--<script type="text/javascript">
    $(function(){
        $('#multiselect').multiselect();
    });
</script>--%>
<%@include file="../template/newfooter.jsp" %>
