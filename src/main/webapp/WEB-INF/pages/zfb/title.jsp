<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<span class="tab_nav">
    <span class="dropdown">
        <a href="#" onclick="skip('a1','zfb')" <c:if test="${flag=='a1'}">class="addactive"</c:if> id="dropdownMenu1" data-toggle="dropdown">支付宝注册信息</a>
    </span>
    <span class="dropdown">
        <a href="#" onclick="skip('a2','zfbZhmx')" <c:if test="${flag=='a2'||flag=='a12'||flag=='a13'||flag=='a14'||flag=='a15'}">class="addactive"</c:if> id="dropdownMenu2" data-toggle="dropdown">支付宝账户明细</a>
        <span class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="width: 283px; background: white; margin-top: 6px; margin-left: -1131px">
            <li role="presentation">
                <a id="a14" role="menuitem" tabindex="1" href="JavaScript:void(0);" onclick="skip('a14','zfbZhmxJczz')" style="width: 265px; height: 30px; <c:if test="${flag=='a14'}">background: #09CEB8</c:if>">账户明细进出总账统计</a>
                <a id="a12" role="menuitem" tabindex="1" href="JavaScript:void(0);" onclick="skip('a12','zfbZhmxTjjg')" style="width: 265px; height: 30px; <c:if test="${flag=='a12'}">background: #09CEB8</c:if>">账户明细账户与账户统计</a>
                <a id="a13" role="menuitem" tabindex="1" href="JavaScript:void(0);" onclick="skip('a13','zfbZhmxTjjgs')" style="width: 265px; height: 30px; <c:if test="${flag=='a13'}">background: #09CEB8</c:if>">账户明细账户与银行卡统计</a>
                <a id="a15" role="menuitem" tabindex="1" href="JavaScript:void(0);" onclick="skip('a15','zfbZhmxJylx')" style="width: 265px; height: 30px; <c:if test="${flag=='a15'}">background: #09CEB8</c:if>">账户明细账户交易类型统计</a>
            </li>
        </span>
    </span>
    <span class="dropdown">
        <a href="#" onclick="skip('a3','zfbZzmx')" <c:if test="${flag=='a3'||flag=='a4'||flag=='a5'||flag=='a6'}">class="addactive"</c:if> id="dropdownMenu3" data-toggle="dropdown">支付宝转账明细</a>
        <span class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="width: 283px; background: white; margin-top: 6px; margin-left: -850px">
            <li role="presentation">
                <a id="a4" role="menuitem" tabindex="1" href="JavaScript:void(0);" onclick="skip('a4','zfbZzmxTjjg')" style="width: 265px; height: 30px; <c:if test="${flag=='a4'}">background: #09CEB8</c:if>">转账明细统计结果</a>
            </li>
            <li role="presentation">
                <a id="a5" role="menuitem" tabindex="2" href="JavaScript:void(0);" onclick="skip('a5','zfbZzmxTjjgs')" style="width: 265px; height: 30px; <c:if test="${flag=='a5'}">background: #09CEB8</c:if>">转账明细对手账户</a>
            </li>
            <li role="presentation">
                <a id="a6" role="menuitem" tabindex="3" href="JavaScript:void(0);" onclick="skip('a6','zfbZzmxGtzh')" style="width: 265px; height: 30px; <c:if test="${flag=='a6'}">background: #09CEB8</c:if>">转账明细共同账户</a>
            </li>
        </span>
    </span>
    <span class="dropdown">
        <a href="#" onclick="skip('a7','zfbJyjl')" <c:if test="${flag=='a7'||flag=='a8'||flag=='a9'||flag=='a10'}">class="addactive"</c:if> id="dropdownMenu4" data-toggle="dropdown">支付宝交易记录</a>
        <span class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="width: 283px; background: white; margin-top: 6px; margin-left: -566px">
            <li role="presentation">
                <a id="a10" role="menuitem" tabindex="2" href="JavaScript:void(0);" onclick="skip('a10','zfbJyjlTjjg')" style="width: 265px; height: 30px; <c:if test="${flag=='a10'}">background: #09CEB8</c:if>">交易卖家账户信息</a>
            </li>
            <li role="presentation">
                <a id="a9" role="menuitem" tabindex="2" href="JavaScript:void(0);" onclick="skip('a9','zfbJyjlTjjgs')" style="width: 265px; height: 30px; <c:if test="${flag=='a9'}">background: #09CEB8</c:if>">交易买家账户信息</a>
            </li>
             <li role="presentation">
                <a id="a8" role="menuitem" tabindex="1" href="JavaScript:void(0);" onclick="skip('a8','zfbJyjlSjdzs')" style="width: 265px; height: 30px; <c:if test="${flag=='a8'}">background: #09CEB8</c:if>">交易记录地址统计</a>
            </li>
        </span>
    </span>
    <span class="dropdown">
        <a href="#" onclick="skip('a11','zfbDlrz')" <c:if test="${flag=='a11'}">class="addactive"</c:if> id="dropdownMenu5" data-toggle="dropdown">支付宝登陆日志</a>
    </span>
</span>
<script type="text/javascript">
    function skip(obj,url){
        window.location = "${pageContext.request.contextPath}/"+url+"?flag="+obj;
    }
    //bootstrap下拉菜单 鼠标悬停显示
    $(function () {
        $(".dropdown").mouseover(function () {
            $(this).addClass("open");
        });
        $(".dropdown").mouseleave(function(){
            $(this).removeClass("open");
        })

    })
</script>
