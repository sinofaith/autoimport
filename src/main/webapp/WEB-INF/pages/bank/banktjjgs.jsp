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
<script src="<c:url value="/resources/js/bank/echarts.min.js"/> "></script>
<script src="<c:url value="/resources/js/bootstrap.js"/> "></script>
<script src="<c:url value="/resources/js/bank/bank.js"/> "></script>
<script src="<c:url value="/resources/js/bank/banktjjgs.js"/> "></script>
<script src="<c:url value="/resources/thirdparty/jquery-form/jquery.form.js"/>" type="text/javascript"></script>
<%--详情模块脚本--%>
<script type="text/javascript">
    try{ace.settings.check('main-container','fixed')}catch(e){}
</script> 

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
                                        <td colspan="14"  align="center" class="dropdown_index" style="background-color: #eee;">
                                            <div class="dropdown " style="color: #333">
                                                <strong>账户点对点统计信息(${aj.aj})</strong>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr align="center">
                                        <td width="4%">序号</td>
                                        <td width="6%">交易卡号</td>
                                        <td width="5%"><a href="/SINOFAITH/banktjjgs/order?orderby=khxm">交易户名</a></td>
                                        <td width="12%">对方卡号<br>
                                            <input type="checkbox" id="checkbox1"  value="1" <c:if test="${hcode == 1 }">checked='checked'</c:if> onclick="hiddenZfbCft()" />
                                            <label for="checkbox1" class="label_c">去除第三方账户</label>
                                            <br>
                                            <label style="color:#0a36e9;" class="label_c"><input name="zhzt" type="checkbox"  value="0" <c:if test="${code.contains('0') }">checked='checked'</c:if> onclick="getZhzt()" />已调单 </label>
                                            <label style="color:red;" class="label_c"><input name="zhzt" type="checkbox"  value="1" <c:if test="${code.contains('1') }">checked='checked'</c:if> onclick="getZhzt()"/>未调单 </label>
                                            <label style="color: #FF00FE;" class="label_c"><input name="zhzt" type="checkbox"  value="2" <c:if test="${code.contains('2') }">checked='checked'</c:if> onclick="getZhzt()"/>人为设计 </label> </td>
                                        <td width="5%"><a href="/SINOFAITH/banktjjgs/order?orderby=khxms">对方户名</a></td>
                                        <td width="4%"><a href="/SINOFAITH/banktjjgs/order?orderby=jyzcs">交 易<br>总次数</a></td>
                                        <td width="4%"><a href="/SINOFAITH/banktjjgs/order?orderby=jzzcs">进 账<br>总次数</a></td>
                                        <td width="6%"><a href="/SINOFAITH/banktjjgs/order?orderby=jzzje">进 账<br>总金额</a></td>
                                        <td width="4%"><a href="/SINOFAITH/banktjjgs/order?orderby=czzcs">出 账<br>总次数</a></td>
                                        <td width="6%"><a href="/SINOFAITH/banktjjgs/order?orderby=czzje">出 账<br>总金额</a></td>
                                        <td width="12%"><a href="/SINOFAITH/banktjjgs/order?orderby=minsj">最早交易时间</a></td>
                                        <td width="12%"><a href="/SINOFAITH/banktjjgs/order?orderby=maxsj">最晚交易时间</a></td>
                                        <td width="4%">间隔天数(天)</td>
                                        <td width="3%">详情</td>
                                    </tr>
                                        <c:forEach items="${detailinfo}" var="item" varStatus="st">
                                            <tr class="${st.index%2==1 ? '':'odd' }">
                                                <td align="center">${item.id}</td>
                                                <td align="center" title="${item.jyzh}"><button style="color: #666">${item.jyzh}</button></td>
                                                <%--<td align="center" title="${item.jyzh}"><button data-toggle="modal" data-target="#myModal2" style="color: #666" onclick="getBanktjjgs(this,'jyzh')">${item.jyzh}</button></td>--%>
                                                <td align="center" title="${item.name}"><div style="width:50px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.name}</div></td>
                                                    <td align="center"  title="${item.dfzh}">
                                                        <div class="dropCss" >
                                                            <div style="width:160px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">
                                                                <c:if test="${item.zhlx eq 2}">
                                                                    <button style="color: #FF00FE">${item.dfzh}</button>
                                                                    <%--<button data-toggle="modal" data-target="#myModal2"  onclick="getBanktjjgs(this,'dfzh')" style="color: #FF00FE">${item.dfzh}</button>--%>
                                                                </c:if>
                                                                <c:if test="${item.zhlx eq 1}">
                                                                    <button style="color: red">${item.dfzh}</button>
                                                                    <%--<button data-toggle="modal" data-target="#myModal2" onclick="getBanktjjgs(this,'dfzh')"  style="color: red">${item.dfzh}</button>--%>
                                                                </c:if>
                                                                <c:if test="${item.zhlx eq 0}">
                                                                    <button onclick="getBanktjjgs(this,'dfzh')" style="color: blue">${item.dfzh}</button>
                                                                    <%--<button data-toggle="modal" data-target="#myModal2" onclick="getBanktjjgs(this,'dfzh')" style="color: blue">${item.dfzh}</button>--%>
                                                                </c:if>
                                                            </div>
                                                            <div class="dropCss-content">
                                                                <c:if test="${item.zhlx!=2}">
                                                                    <c:choose>
                                                                        <c:when test="${item.dsfzh==1}">
                                                                            <a href='#' onclick="editBp('${item.dfzh}',${item.dsfzh})">取消第三方账户</a>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <a href='#' onclick="editBp('${item.dfzh}',${item.dsfzh})">添加第三方账户</a>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:if>
                                                            </div>
                                                        </div>
                                                    </td>
                                                <td align="center"title="${item.dfxm}">
                                                    <div class="dropCss" >
                                                        <div style="width:50px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;">${item.dfxm}</div>
                                                        <div class="dropCss-content">
                                                            <a data-toggle="modal" data-target="#myModal3" onclick="editPersonRelation('${item.name}','${item.dfxm}')">添加目标关系</a>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td align="center">${item.jyzcs}</td>
                                                <td align="center">${item.jzzcs}</td>
                                                <td align="center"><fmt:formatNumber value="${item.jzzje}" pattern="#,##0"/></td>
                                                <td align="center">${item.czzcs}</td>
                                                <td align="center"><fmt:formatNumber value="${item.czzje}" pattern="#,##0"/></td>
                                                <td align="center">${item.minsj}</td>
                                                <td align="center">${item.maxsj}</td>
                                                <td align="center">${item.jgsj}</td>
                                                <td align="center">
                                                    <button  data-toggle="modal" class="btna" data-target="#myModal" onclick="getZzDetails(this,'tjjgs','-1')">详情</button>
                                                </td>
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
                                        <a href="/SINOFAITH/banktjjgs/seach?pageNo=${page.topPageNo }"><input type="button" name="fristPage" value="首页" /></a>
                                        <c:choose>
                                            <c:when test="${page.pageNo!=1}">
                                                <a href="/SINOFAITH/banktjjgs/seach?pageNo=${page.previousPageNo }"><input type="button" name="previousPage" value="上一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="previousPage" value="上一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${page.pageNo != page.totalPages}">
                                                <a href="/SINOFAITH/banktjjgs/seach?pageNo=${page.nextPageNo }"><input type="button" name="nextPage" value="下一页" /></a>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" disabled="disabled" name="nextPage" value="下一页" />
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="/SINOFAITH/banktjjgs/seach?pageNo=${page.bottomPageNo }"><input type="button" name="lastPage" value="尾页" /></a>
                                        <input type="number" id="num" max="${page.totalPages}" style="width: 9%" min="1">
                                        <input type="button" value="跳转" onclick="bankSkip('tjjgs')">
                                    </div>

                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                    <div class="sidebar_right pos_re">
                        <%--<input type="checkbox" id="checkbox1" ${aj.flg==1? 'checked':''} value="1" onclick="ajCount('${aj.aj}')">--%>
                        <%--<label for="checkbox1" style="padding-top: 8px">统计结果去除红包相关记录</label>--%>
                        <div class=" ">

                            <div>
                                <form action="/SINOFAITH/banktjjgs/SeachCode" method="post">
                                    <div class="form-group_search  fl_l width100" >
                                        <span style="margin-left: 10px;color: #444;padding-bottom: 10px;">查询方式</span>
                                        <select name="seachCondition" id="seachCondition" onchange="seachChange()" class="width100" STYLE="margin-bottom: 20px;">
                                            <option value="jyzh"<c:if test="${tjsseachCondition=='jyzh'}">selected="selected"</c:if>>交易卡号</option>
                                            <option value="khxm"<c:if test="${tjsseachCondition=='khxm'}">selected="selected"</c:if>>交易户名</option>
                                            <option value="dfzh" <c:if test="${tjsseachCondition=='dfzh'}">selected="selected"</c:if> >对方卡号</option>
                                            <option value="dsxm" <c:if test="${tjsseachCondition=='dsxm'}">selected="selected"</c:if> >对方户名</option>
                                            <option value="jzzje"<c:if test="${tjsseachCondition=='jzzje'}">selected="selected"</c:if>>进账总金额阀值</option>
                                            <option value="czzje"<c:if test="${tjsseachCondition=='czzje'}">selected="selected"</c:if>>出账总金额阀值</option>
                                            <%--<option value="sfzhm" <c:if test="${seachCondition=='sfzhm'}">selected="selected"</c:if> >身份证号码</option>--%>
                                        </select>
                                        <%--<input  style="margin-left: 10px;" type="checkbox" name="usable" value="1" <c:if test="${usable eq '1'}">checked="checked"</c:if>>上次条件有效--%>
                                        <textarea  class="form-control02 seachCode fl_l width100" id="seachCode" placeholder="请输入要查询内容" name="seachCode" >${tjsseachCode}</textarea>
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
                                           <c:choose>
                                               <c:when test="${user.id==aj.userId}">
                                                   <button  type="button"  class="sideBar_r_button"  onclick="location.href='/SINOFAITH/banktjjgs/download'" >数据导出</button>
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
                <h4 class="modal-title" id="myModalLabel">转账详情<span id="title"></span></h4>
            </div>
            <div class="modal-body">
                <table class="table  table-hover table_style table_list1 "
                       style="border-left: 1px solid #ccc; border-right: 1px solid #ccc!important;">
                    <thead style="display:table;width:100%;table-layout:fixed;width: calc( 100% - 16.5px );">
                    <tr align="center">
                        <td width="4%">序号</td>
                        <td width="13%">交易账卡号</td>
                        <td width="5%">交易户名</td>
                        <td width="10%">
                            <button onclick="orderByFilter('tjjgs','jysj')">交易时间</button>
                        </td>
                        <td width="7%">
                            <button onclick="orderByFilter('tjjgs','jyje')">交易金额</button>
                        </td>
                        <td width="7%">交易余额</td>
                        <td width="7%">
                            <button onclick="orderByFilter('tjjgs','sfbz')">收付标志</button>
                        </td>
                        <td width="13%">对手账卡号</td>
                        <td width="13%">对手户名</td>
                        <td width="7%">摘要说明</td>
                        <td width="7%">交易发生地</td>
                        <td width="7%">交易网点名称</td>
                        <td width="7%">备注</td>
                        <%--<td width="8%">接收金额(元)</td>--%>
                    </tr>
                    <input name="label" id="yhkkh" hidden="hidden" value="">
                    <input name="label" id="dfkh" hidden="hidden" value="">
                    <input name="label" id="allRow" hidden="hidden" value="">
                    <input name="label" id="zhlx" hidden="hidden" value="-1">
                    </thead>
                    <tbody id="result" style="display:block;height:340px;overflow-y:scroll;"
                           onscroll="scrollF('tjjgs')">

                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" onclick="downDetailZh()">导出</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>

<div class="modal fade" id="myModal2" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="top: 0%; min-width: 96%;left: 2%;right: 2%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabela">对手账户统计<span id="titlea"></span></h4>
            </div>
            <div class="modal-body" >
                <div id="main" style="width: 1450px;height:590px;">
                </div>
            </div>
            <div class="modal-footer">
                <%--<button type="button" class="btn btn-default" onclick="downDetailZh()">导出</button>--%>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>


<div class="modal fade" id="myModal3" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="top: 10%; min-width: 30%;left: 30%;right: 30%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabel3">目标关系<span id="title3"></span></h4>
            </div>
            <div class="modal-body">
                <form action="" method="post" class="basic-grey">
                    <label>
                        <span>目标姓名:</span>
                        <input id="name" type="text"  name="name" placeholder="目标人物姓名"
                               data-toggle="tooltip" data-placement="top" oninput="$('#name').tooltip('destroy');"/>
                    </label>
                    <label>
                        <span>对象:</span>
                        <input id="pname" type="text" name="pname" placeholder="对象"
                               data-toggle="tooltip" data-placement="top" oninput="$('#pname').tooltip('destroy');"/>
                    </label>
                    <label>
                        <span>关系 :</span>
                        <select id="relationName" name="relationName" onchange="changeRelationShow()">
                            <option value="资金关联">资金关联</option>
                            <option value="公司关联">公司关联</option>
                            <option value="户籍关联">户籍关联</option>
                        </select>
                    </label>
                    <label>
                        <span>关系说明 :</span>
                        <select id="relationShow" name="relationShow">
                            <option value="上家">上家</option>
                            <option value="下家">下家</option>
                            <option value="仓储物流">仓储物流</option>
                            <option value='电商平台'>电商平台</option>
                            <option value='支付平台'>支付平台</option>
                            <option value='日常消费'>日常消费</option>
                        </select>
                    </label>
                    <label>
                        <span>备注 :</span>
                        <input id="relationMark" type="text" name="relationMark" placeholder="备注(公司关联填公司名)"></label>
                    </label>
                    <label>
                        <span>进账总金额 :</span>
                        <input id="jzzje" type="text" name="name" readonly />
                    </label>
                    <label>
                        <span>出账总金额 :</span>
                        <input id="czzje" type="text" name="name" readonly />
                    </label>
                    <label>
                        <span>交易净值 :</span>
                        <input id="jyjz" type="text" name="name" readonly />
                    </label>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" onclick="addRelation()">添加</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>
<%@include file="../template/newfooter.jsp" %>
