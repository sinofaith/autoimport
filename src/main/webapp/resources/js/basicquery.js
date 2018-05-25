/**
 * Created by Administrator on 2014/8/14.
 */


$(function() {

    var setting = {
        view: {
            selectedMulti: false
        },
        async: {
            enable: true,
            url:"/AMD/BasicQuery/test",
            autoParam:["id", "name", "dm"],
            otherParam:{"otherParam":"zTreeAsyncTest"}
            //dataFilter: filter
        },
        callback: {
            beforeClick: beforeClick,
            beforeAsync: beforeAsync,
            onAsyncError: onAsyncError,
            onAsyncSuccess: onAsyncSuccess
        }
    };

    function filter(treeId, parentNode, childNodes) {
        if (!childNodes) return null;
        for (var i=0, l=childNodes.length; i<l; i++) {
            childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
        }
        return childNodes;
    }
    function beforeClick(treeId, treeNode) {
        $('.vis_zab').attr('value',treeNode.name);
        $('.vis_zab').attr('param',treeNode.dm);
        if (!treeNode.isParent) {
            $('.zab').attr('value','\''+treeNode.dm+'\'');
            $('.zTreeDemoBackground').css('display','none');

            if($(".vis_zab").attr("param") != ""){
                $.ajax({
                    url:'/AMD/BasicQuery/fab',
                    type:'POST',
                    data: {"value":$(".vis_zab").attr("param")},
                    dataType: 'JSON',
                    success: function(data) {
                        // console.log(data);
                        $('#fab').empty();
                        $('#fab').append("<option value =''> 副案别 </option>");
                        $.each(data, function (idx,item) {
                            $('#fab').append("<option value = '" + item['dm'] + "'>" + item['mc'] + "</option>");
                        });
                    },
                    error: function () {
                    }
                });
            }else{
                $('#fab').empty();
                $('#fab').append("<option value = ''> 副案别</option>");
            }
        } else {
            $('.zTreeDemoBackground').mask();

            $.ajax({
                url:'/AMD/BasicQuery/fab',
                type:'POST',
                data: {"value":treeNode.dm},
                dataType: 'JSON',
                success: function(data) {
                    // console.log(data);
                    $('#fab').empty();

                    $('#fab').append("<option value =''> 副案别 </option>");
                    $.each(data, function (idx,item) {
                        $('#fab').append("<option value = '" + item['dm'] + "'>" + item['mc'] + "</option>");
                    });
                    $('.zTreeDemoBackground').unmask();
                    $('.zTreeDemoBackground').css('display','none');
                },
                error: function () {
                    $('.zTreeDemoBackground').unmask();
                    $('.zTreeDemoBackground').css('display','none');
                }
            });


            $.ajax({
                url:'/AMD/BasicQuery/zab',
                type:'post',
                data:{"value":treeNode.dm},
                success:function(data){
                    console.log(data);
                    $('.zab').attr('value',data);
                    $('.zTreeDemoBackground').unmask();

                    $('.zTreeDemoBackground').css('display','none');

                }
            });
        }
    }
    var log, className = "dark";
    function beforeAsync(treeId, treeNode) {
        className = (className === "dark" ? "":"dark");
        showLog("[ "+getTime()+" beforeAsync ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
        return true;
    }
    function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
        showLog("[ "+getTime()+" onAsyncError ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
    }
    function onAsyncSuccess(event, treeId, treeNode, msg) {
        showLog("[ "+getTime()+" onAsyncSuccess ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
    }

    function showLog(str) {
        if (!log) log = $("#log");
        log.append("<li class='"+className+"'>"+str+"</li>");
        if(log.children("li").length > 8) {
            log.get(0).removeChild(log.children("li")[0]);
        }
    }
    function getTime() {
        var now= new Date(),
            h=now.getHours(),
            m=now.getMinutes(),
            s=now.getSeconds(),
            ms=now.getMilliseconds();
        return (h+":"+m+":"+s+ " " +ms);
    }

    function refreshNode(e) {
        var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
            type = e.data.type,
            silent = e.data.silent,
            nodes = zTree.getSelectedNodes();
        if (nodes.length == 0) {

        }
        for (var i=0, l=nodes.length; i<l; i++) {
            zTree.reAsyncChildNodes(nodes[i], type, silent);
            if (!silent) zTree.selectNode(nodes[i]);
        }
    }
    $.fn.zTree.init($("#treeDemo"), setting);


    $("#b_jccx").addClass("hover");
    $('#b_jccx').parent().attr("href","#");
    $('#cancle').hide();
    $('#checkall').click(function () {
        if(this.checked){
            $("[name = 'item']").prop("checked","checked");
        }else{
            $("[name = 'item']").removeAttr("checked");
        }

    });
    $('#tablecontent').delegate('#extend','click',function(){
        var value = '';
        ($("[name = 'item']:checked").each(function () {
            // console.log($(this).parent().attr("id"));
            value += $(this).parent().siblings().eq(4).text()+':';

        }));
        $('#extend').attr('href','/AMD/extend?num='+value);

    });
    $('#tablecontent').delegate('#btnDownload','click',function(){
        var value = '';
        ($("[name = 'item']:checked").each(function () {

            value += $(this).attr("param") + "-";
        }));
        if(value.length == 0) alertify.alert("选择查看内容");

        else{
            $('#gotodownloaderForm>input').val(value);
            $('#gotodownloaderForm').submit();
        }
    });

    $('#tablecontent').delegate('#createSet','click',function(){
        var value = '';
        ($("[name = 'item']:checked").each(function () {
            value += $(this).attr("param") + ":";
        }));
        if(value.length == 0) alertify.alert("选择创建内容");
        else{

            alertify.prompt('创建集合名字','',function(evt,value){
                var nvalue = '';
                ($("[name = 'item']:checked").each(function () {

                    nvalue += $(this).attr("param") + ":";
                }));

                if(nvalue != null){
                    $('#setname').val(value);
                    $('#cids').val(nvalue);
                    var option = {
                        success:function(result){
                            alertify.alert("创建成功");
                        },
                        error:function(){
                            alertify.alert("创建失败");
                        }
                    }
                    console.log($('#setPost').ajaxSubmit(option));
                }
            });
        }


    });
    $('.ico_close ').next().click(function(){
        console.log($(this).text())
    });
    $('tbody').delegate('.brief_intro','click',function(){
        $('.sub-pagecontent').mask();
        $.ajax({
            url:'/AMD/BasicQuery/basicinfor',
            type:'post',
            data:{"value":$(this).attr('param')},
            dataType:'json',
            success:function(data){
                console.log(data);
                $('.modal-dialog table').empty();
                $('.modal-dialog table').append('<tr>'+
                    '<td width="100"> 姓名： </td>'+
                    '<td>'+ data['oname'] +'</td>'+
                    '</tr>'+
                    '<tr class="item1">'+
                    '<td>籍贯： </td>'+
                    '<td>'+data['birthPlace']+'</td>'+
                    '</tr>'+
                    '<tr>'+
                    '<td>手机号： </td>'+
                    '<td>'+data['mobile'] +'</td>'+
                    '</tr>'+
                    '<tr class="item1">'+
                    '<td>对象编号：</td>'+
                    '<td>'+data['oid']+'</td>'+
                    '</tr>'+
                    '<tr>'+
                    '<td>案件编号： </td>'+
                    '<td>'+data['caseId']+'</td>'+
                    '</tr>'+
                    '<tr class="item1">'+
                    '<td>身份证号： </td>'+
                    '<td>'+data['certificateId']+'</td>'+
                    '</tr>'+
                    '<tr>'+
                    '<td>采集编号：</td>'+
                    '<td>'+data['collectId']+'</td>'+
                    '</tr>'+
                    '<tr class="item1">'+
                    '<td>采集单位编号：</td>'+
                    '<td>'+data['policeCode']+'</td>'+
                    '</tr>'+
                    '<tr>'+
                    '<td>所属区域：</td>'+
                    '<td>'+data['areaCode']+'</td>'+
                    '</tr>'+
                    '<tr class="item1">'+
                    '<td>采集地址：</td>'+
                    '<td>'+data['addres']+'</td>'+
                    '</tr>'+

                    '<tr>' +
                    '<td>采集时间：</td>'+
                    '<td>'+toDateStr(data['takeTime'])+'</td>'+
                    '</tr>'+
                    '<tr class="item1">'+
                    '<td>警员姓名</td>'+
                    '<td>'+data['policeName']+'</td>'+
                    '</tr>'
                );

                $('.modal-dialog').slideDown(100);
            }
        });

    });
    $('.widget-toolbar a').click(function(){
        if($(this).children(":first").hasClass('icon-chevron-up')){
            $(this).children(":first").removeClass('icon-chevron-up');
            $(this).children(":first").addClass('icon-chevron-down');
            $('.widget-body').hide();

        }else{
            $(this).children(":first").removeClass('icon-chevron-down');
            $(this).children(":first").addClass('icon-chevron-up');
            $('.widget-body').show();
        }
    });
    $('#region').change(function(event){
        if($(this).val() != ""){
            $.ajax({
                url:'/AMD/BasicQuery/area',
                type:'POST',
                data: {"value":$(this).val()},
                dataType: 'JSON',
                success: function(data) {
                    $('#area').empty();
                    $.each(data, function (idx,item) {
                        $('#area').append("<option value = '" + item + "'>" + item + "</option>");
                    });
                },
                error: function () {}
            });
        }else{
            $('#area').empty();
            $('#area').append("<option value = ''> 区域</option>");
        }
    });
    //启动时间插件
    $('.timeformat').datetimepicker({
        lang:'ch',
        timepicker:false,
        format:'Y-m-d'
    });
    //树
    $('.vis_zab').click(function(){
    if($(".zTreeDemoBackground").css("display")=="block"){
        $('.zTreeDemoBackground').css('display','none');
    }else{
        $('.zTreeDemoBackground').css('display',"block");
    }
        event.stopPropagation();
});


});
function convert(a) {
    if (a == "name") return "姓名";
    else if(a == "certificate_id") return "身份证号码"
    else if (a == "fage") return "年龄从";
    else if (a == "tage") return "年龄到";
    else if (a == "birthplace") return "籍贯";
    else if (a == "o_id") return "人员编号";
    else if (a == "farrest_time") return "抓获时间从";
    else if (a == "tarrest_time") return "抓获时间到";
    else if (a == "maincase")  return  "主案别";
    else if (a == "seccase") return "副案别";
    else if (a == "casereign") return "案件地域";
    else if (a == "casearea") return "案件区域";
    else if (a == "freport_time") return "接报时间从";
    else if (a == "treport_time") return "接报时间到";
    else if (a == "case_id") return "案件编号";
    else if (a == "qq") return "QQ号码";
    else if (a == "qq_nickname") return "QQ昵称";
    else if (a == "weibo") return "微博账号";
    else if (a == "weibo_nickname") return "微博昵称";
    else if (a == "weixin") return "微信号";
    else if (a == "weixin_nickname") return "微信昵称";
    else if (a == "qita_type") return "其他账号类型";
    else if (a == "qita_account") return "其他账号";
    else if (a == "qita_nickname") return "其他账号昵称";
    else if (a == "contact_qq") return "对端QQ号";
    else if( a == "contact_qq_nickname") return "对端QQ昵称";
    else if ( a == "contact_weibo") return "对端微博";
    else if ( a == "contact_weibo_nickname" ) return "对端微博昵称";
    else if (  a == "contact_weixin") return "对端微信";
    else if (a  == "contact_weixin_nickname") return "对端微信昵称";
    else if ( a  == "contact_qita_type") return "其他对端账号种类";
    else if (a  == "contact_qita_account")return "其他对端账号";
    else if (a  == "contact_qita_nickname") return "其他对端昵称";
    else if (a  == "phone")return "手机号";
    else if(a  == "IMEI")return "IMEI";
    else if (a  === "IMSI")return "IMSI";
    else if(a  == "contact_phone")return "对端手机号";
    else if (a  == "contact_nickname") return "对端手机昵称";
    else if (a == "sex") return "性别";
    else if( a == null||a=="") return " 无";
    else return a;
}
function selectAll(){
    $("[name = 'item']").prop("checked","checked");
}
function reverseSelect(){
    $("[name = 'item']").each(function(){
        if($(this).prop("checked"))
        {
            $(this).removeAttr("checked");
        }
        else
        {
            $(this).prop("checked","checked");
        }
    });

}
function clearSelect(){
    $("[name = 'item']").removeAttr("checked");
}
function closeBasicInfor(){
    $('.modal-dialog').hide();
    $('.sub-pagecontent').unmask();
}
function doSubmit(){
    var tag = false;//是否输入查询条件

    var content = $("#write_condi");
    content.empty();
    $("#condiform :input").each(
        function(){
            var item =  $(this);
            var input = item.val();
            var def = $(this).attr('placeholder');
            if(input != 0 && input !=null&&input != def&&input!=='确认查询'){
                content.append("<span class = 'lab'>" + convert(item.attr("name")) + ":</span> &nbsp;" + input + "&nbsp;&nbsp;" );
                tag = true;

            }
        }
    );

    if(!tag){
        alertify.alert('请输入查询条件');
        return;
    }

    $('.result').show();
    $('.widget-body').hide();
    $('.widget-toolbar a i').removeClass('icon-chevron-up');
    $('.widget-toolbar a i').addClass('icon-chevron-down');
    $('.result').mask();
    var option = {
        url:"/AMD/BasicQuery/Search",
        type:"GET",
        dataType: 'json',
        success: function(result) {
            $(".result").unmask();
            $('#myTable').DataTable().destroy();
            var table = $('#myTable').dataTable({
                "processing" :true ,
                "serverSide":true,
                "ordering":false,

                "filter":false,
                "oLanguage":{
                    "sProcessing":"正在加载中。。。。"
                },
                "ajax":"/AMD/BasicQuery/getpart"

            });
            $('.dataTables_length').append(' <div id = "tool" class="fright page_act" style="margin-bottom: 10px; margin-top:0px; display: none">'+

                '<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: separate">'+
                '<tr>'+
                '<td>'+
                '<a class="select-all"  title="全选"><img src="/AMD/resources/image/quanxuan.jpg" width="34" height="34" alt="全选" onclick="selectAll()"></a></td>'+
                '<td>'+
                '<a class="select-reverse"  title="反选"><img src="/AMD/resources/image/fanxuan.jpg" width="34" height="34" alt="反选" onclick="reverseSelect()"></a></td>'+
                '<td>'+
                '<a class="select-none"  title="取消"><img src="/AMD/resources/image/quxiao.jpg" width="34" height="34" alt="重选" onclick="clearSelect()"></a></td>'+
                '<td>'+
                '<a  id="extend" title="一键扩展" target="_blank" ><img src="/AMD/resources/image/kuozhan.jpg" width="34" height="34" alt="一键扩展"></a></td>'+
                '<td>'+
                '<td>'+
                '<a  id="btnDownload" title="数据下载"><img src="/AMD/resources/image/shujuxiazai.jpg" width="34" height="34" alt="下载" ></a></td>'+
                '<td>'+
                '<a id="createSet"  title="存为集合"><img src="/AMD/resources/image/cunjihe.jpg" width="34" height="34" alt="保存为集合"></a></td>'+
                '</tr>'+
                '</table>'+
                '</div>'
            );
            $('.dataTables_length').css('width','100%');
            $('#tool').show();
            // $('.myTable_wrapper').prepend('#tool');
            $('#myTable').css('width','100%');

        },
        error: function() {
            $(".result").unmask();
            alertify.alert('错误！请检查网络环境！');
        }
    };
    var form =  $("#condiform");
    form.ajaxSubmit(option);
}
function toDateStr(a){
    var time = new Date(a).dateFormat("Y-m-d h:m:s");
    return time.toString();
}
