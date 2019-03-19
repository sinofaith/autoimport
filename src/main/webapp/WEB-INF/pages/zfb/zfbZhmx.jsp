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
<script src="<c:url value="/resources/js/zfb/zfb.js"/> "></script>
<script src="<c:url value="/resources/js/zfb/zfbPreview.js"/> "></script>
<script src="<c:url value="/resources/js/select/selectordie.min.js"/> "></script>
<script src="<c:url value="/resources/js/raydreams.js"/> "></script>
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
                                        <td colspan="13"  align="center" class="dropdown_index" style="background-color: #eee;">
                                            <div class="dropdown " style="color: #333">
                                                <strong>支付宝账户明细(${aj.aj})</strong>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr align="center">
                                        <td width="4%">序号</td>
                                        <td width="7%">交易号</td>
                                        <td width="7%">商户订单号</td>
                                        <td width="11%"><a href="/SINOFAITH/zfbZhmx/seach?pageNo=1&orderby=fksj">付款时间</a></td>
                                        <td width="8%"><a href="/SINOFAITH/zfbZhmx/seach?pageNo=1&orderby=jylyd">交易来源地</a></td>
                                        <td width="9%">类型</td>
                                        <td width="9%">用户信息</td>
                                        <td width="10%">交易对方信息</td>
                                        <td width="8%"><a href="/SINOFAITH/zfbZhmx/seach?pageNo=1&orderby=xfmc">消费名称</a></td>
                                        <td width="5%"><a href="/SINOFAITH/zfbZhmx/seach?pageNo=1&orderby=je">金额</a></td>
                                        <td width="4%"><a href="/SINOFAITH/zfbZhmx/seach?pageNo=1&orderby=sz">收/支</a></td>
                                        <td width="6%"><a href="/SINOFAITH/zfbZhmx/seach?pageNo=1&orderby=jyzt">交易状态</a></td>
                                        <td width="4%">备注</td>
                                    </tr>
                                    <c:forEach items="${detailinfo}" var="item" varStatus="st">
                                        <tr class="${st.index%2==1 ? '':'odd' }">
                                            <td align="center">${(st.index+1)+(page.pageNo-1)*page.pageSize}</td>
                                            <td align="center" title="${item.jyh}"><div style="width:80px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.jyh}</div></td>
                                            <td align="center" title="${item.shddh}"><div style="width:80px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.shddh}</div></td>
                                            <td align="center">${item.fksj}</td>
                                            <td align="center" title="${item.jylyd}"><div style="width:80px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.jylyd}</div></td>
                                            <td align="center">${item.lx}</td>
                                            <td align="center" title="${item.yhxx}"><div style="width:100px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.yhxx}</div></td>
                                            <td align="center" title="${item.jydfxx}"><div style="width:110px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.jydfxx}</div></td>
                                            <td align="center" title="${item.xfmc}"><div style="width:130px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.xfmc}</div></td>
                                            <td align="center">${item.je}</td>
                                            <td align="center">${item.sz}</td>
                                            <td align="center" title="${item.jyzt}"><div style="width:60px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.jyzt}</div></td>
                                            <td align="center" title="${item.bz}"><div style="width:40px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.bz}</div></td>
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
                                        <a href="/SINOFAITH/zfbZhmx/seach?pageNo=${page.topPageNo }"><input type="button" name="fristPage" value="首页" /></a>
                                        <c:choose>
                                            <c:when test="${page.pageNo!=1}">
                                                <a href="/SINOFAITH/zfbZhmx/seach?pageNo=${page.previousPageNo }"><input type="button" name="previousPage" value="上一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="previousPage" value="上一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${page.pageNo != page.totalPages}">
                                                <a href="/SINOFAITH/zfbZhmx/seach?pageNo=${page.nextPageNo }"><input type="button" name="nextPage" value="下一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="nextPage" value="下一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="/SINOFAITH/zfbZhmx/seach?pageNo=${page.bottomPageNo }"><input type="button" name="lastPage" value="尾页" /></a>
                                        <input type="number" id="num" max="${page.totalPages}" style="width: 9%" min="1">
                                        <input type="button" value="跳转" onclick="zfbSkip('Zhmx')">
                                    </div>

                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                    <div class="sidebar_right pos_re">

                        <div class=" ">

                            <div>
                                <form action="/SINOFAITH/zfbZhmx/SeachCode" method="post">
                                    <div class="form-group_search  fl_l width100" >
                                        <span style="margin-left: 10px;color: #444;padding-bottom: 10px;">查询方式</span>
                                        <select name="seachCondition" class="width100" STYLE="margin-bottom: 20px;">
                                            <option value="jyh" <c:if test="${zhmxSeachCondition=='jyh'}">selected="selected"</c:if>>交易号</option>
                                            <option value="shddh" <c:if test="${zhmxSeachCondition=='shddh'}">selected="selected"</c:if>>商户订单号</option>
                                            <option value="yhxx"<c:if test="${zhmxSeachCondition=='yhxx'}">selected="selected"</c:if>>用户信息</option>
                                            <option value="jydfxx" <c:if test="${zhmxSeachCondition=='jydfxx'}">selected="selected"</c:if> >交易对方信息</option>
                                        </select>
                                        <textarea  class="form-control02 seachCode fl_l width100" id="seachCode" placeholder="请输入要查询内容" name="seachCode" >${zhmxSeachCode}</textarea>
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
                           <c:if test="${!fn:contains(aj.aj, ',')}">
                               <button class="sideBar_r_button" data-toggle="modal" data-target="#myModal">支付宝数据导入</button>
                           </c:if>
                           <button  type="button"  class="sideBar_r_button" <c:if test="${aj!=null && detailinfo!=null && detailinfo.size()!=0}">onclick="location.href='/SINOFAITH/zfbZhmx/download'"</c:if>>数据导出</button>
                           <button data-toggle="modal" type="button"  class="sideBar_l_button" <c:if test="${aj!=null && detailinfo.size()!=0}"> data-target="#myModal2" onclick="yjdcDownload()"</c:if>>一键导出</button>
                           <button class="sideBar_r_button" <c:if test="${aj!=null && detailinfo.size()!=0}">onclick="location.href='/SINOFAITH/zfbZzmxTjjg/createPDF'"</c:if>>生成PDF报告文档</button>
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
                        <input type="radio" id="inlineRadio1" value="csv" name="radioInline" checked>
                        <label for="inlineRadio1"> csv </label>
                    </div>
                    <div class="radio radio-inline">
                        <input type="radio" id="inlineRadio2" value="xlsx" name="radioInline">
                        <label for="inlineRadio2"> xls/xlsx </label>
                    </div>
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
                            <label for="c1">交易号</label>
                            <select	 id="c1" placeholder="交易号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c2">付款方支付宝号</label>
                            <select	 id="c2" placeholder="付款方支付宝账号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c3">收款方支付宝号</label>
                            <select	 id="c3" placeholder="收款方支付宝账号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c4">收款机构信息</label>
                            <select	 id="c4" placeholder="收款机构信息" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c5">到账时间</label>
                            <select	id="c5" placeholder="到账时间" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c6">转账金额</label>
                            <select	id="c6" placeholder="转账金额" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c7">转账产品名称</label>
                            <select id="c7" placeholder="转账产品名称" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c8">交易发生地</label>
                            <select	id="c8" placeholder="交易发生地" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c9">提现流水号</label>
                            <select id="c9" placeholder="提现流水号" onchange="selectC()">
                            </select>
                        </div>
                        <div class="col-md-1">
                            <label for="c10">对应的协查数据</label>
                            <select	id="c10" placeholder="对应的协查数据" onchange="selectC()">
                            </select>
                        </div>
                    </div>
                    <button id="nextSelect" type="button" style="margin-left: 1200px;top: 25px;" class="btn btn-primary" onclick="nextSelect()">下一个</button>
                    <button id="mapping" type="button" style="margin-left: 1280px" class="btn btn-primary" onclick="uploadMapping()">提交映射</button>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="uploadZfbExcel()">导入数据</button>
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
                <h4 class="modal-title" id="myModalLabel2">支付宝分析数据一键导出(预览)<span id="title"></span></h4>
                <select	 id="a1" placeholder="分析数据类别" onchange="select_change()">
                    <option value="zhmxJczz" selected="selected">账户明细进出总账统计</option>
                    <option value="zhmxTjjg">账户明细账户与账户统计</option>
                    <option value="zhmxTjjgs">账户明细账户与银行卡统计</option>
                    <option value="zhmxJylx">账户明细账户交易类型统计</option>
                    <option value="zzmxTjjg">转账明细统计结果</option>
                    <option value="zzmxTjjgs">转账明细对手账户</option>
                    <option value="zzmxGtzh">转账明细共同账户</option>
                    <option value="jyjlTjjg">交易卖家账户信息</option>
                    <option value="jyjlTjjgs">交易买家账户信息</option>
                    <option value="jyjlSjdzs">交易记录地址统计</option>
                </select>&nbsp;&nbsp;&nbsp;
                出账总金额阀值:<input type="number" style="width:120px" class="txt" id="czje">
                进账总金额阀值:<input type="number"     style="width:120px"  class="txt" id="jzje">
                <input type="button" class="btn btn-primary" value="预览数据" onclick="yjdcDownload(true)">
            </div>
            <div class="modal-body">
                <div id="dataTable" class="table table-striped table-bordered" style="height:400px">
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
