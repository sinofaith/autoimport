var yjdcList = [];
var f = ['zhmxJczz','zhmxTjjg','zhmxTjjgs','zhmxJylx','zzmxTjjg','zzmxTjjgs','zzmxGtzh','jyjlTjjg','jyjlTjjgs','jyjlSjdzs'];
for(var value in f){
    yjdcList.push({field:f[value],czje:'',jzje:''});
}

function select_change(){
    $("#jzje").val("");
    $("#czje").val("");
    yjdcDownload(true);
}

// 结果预览
function yjdcDownload(flag){
    var select1 = $("#a1").val();
    // 判断是否是最后一个
    if(select1!='jyjlSjdzs' || flag){
        // $("#next").attr("disabled",false);
        $("#yjdc").attr("disabled",false);
    }
    $("#jzje").attr("disabled",false);
    // 清空table
    var dataTable = window.document.getElementById("dataTable");
    dataTable.innerHTML = "";
    var czje = $('#czje').val();
    var jzje = $('#jzje').val();
    $.ajax({
        type:"post",
        url:"/SINOFAITH/zfbZzmx/previewTable",
        data:{
            currentPage:1,
            name:select1,
            czje:czje,
            jzje:jzje
        },
        success:function (msg) {
            var data = msg.list;
            data.push({"size":msg.totalRecords});
            // 插入表头
            insertTable1(data);
        },
        dataType:"json"
    });
}

function pushColumns(columns, name){
    columns.push({ field: "jyzcs", title: "交易总次数", sort: true});
    if(name=="zhmxJczz" || name=="zhmxTjjg" || name=="zhmxTjjgs"||name=="zhmxJylx"){
        columns.push({ field: "czzcs", title: "出账总次数", sort: true});
        columns.push({ field: "czzje", title: "出账总金额", sort: true});
        columns.push({ field: "jzzcs", title: "进账总次数", sort: true});
        columns.push({ field: "jzzje", title: "进账总金额", sort: true});
    }else{
        columns.push({ field: "fkzcs", title: "出账总次数", sort: true});
        columns.push({ field: "fkzje", title: "出账总金额", sort: true});
        columns.push({ field: "skzcs", title: "进账总次数", sort: true});
        columns.push({ field: "skzje", title: "进账总金额", sort: true});
    }
}

// 插入表格
function insertTable1(data){
    var selectValue = $("#a1").val();
    var columns = [];
    columns.push({ field: "id", title: "序号", sort:true });
    if(selectValue == "zhmxJczz"){
        columns.push({field:"jyzfbzh",title:"交易支付宝账号", sort: true});
        columns.push({field: "jymc", title: "账户名称"});
        pushColumns(columns, selectValue);
    }else if(selectValue == "zhmxTjjg" || selectValue == "zhmxTjjgs"){
        columns.push({ field: "jyzfbzh", title: "交易支付宝账号", sort: true });
        columns.push({ field: "jymc", title: "账户名称" });
        columns.push(selectValue=='zhmxTjjg'?{ field: "dszfbzh", title: "对手支付宝账号" }:{ field: "dszfbzh", title: "银行卡信息" });
        columns.push(selectValue=='zhmxTjjg'?{ field: "dsmc", title: "对手名称" }:{ field: "xfmc", title: "消费名称" });
        pushColumns(columns, selectValue);
    }else if(selectValue == "zhmxJylx"){
        columns.push({ field: "jyzfbzh", title: "交易支付宝账号", sort: true});
        columns.push({ field: "jymc", title: "账户名称"});
        columns.push({ field: "xfmc", title: "消费名称"});
        pushColumns(columns, selectValue);
    }else if(selectValue == "zzmxTjjg"){
        columns.push({ field: "zfbzh", title: "支付宝账号", sort: true});
        columns.push({ field: "zfbmc", title: "账户名称"});
        columns.push({ field: "zzcpmc", title: "转账类型"});
        pushColumns(columns, selectValue);
    }else if(selectValue == "zzmxTjjg"){
        columns.push({ field: "zfbzh", title: "支付宝账号", sort: true});
        columns.push({ field: "zfbmc", title: "账户名称"});
        columns.push({ field: "zzcpmc", title: "转账类型"});
        pushColumns(columns, selectValue);
    }else if(selectValue == "zzmxTjjgs"){
        columns.push({ field: "zfbzh", title: "支付宝账号", sort: true});
        columns.push({ field: "zfbmc", title: "账户名称"});
        columns.push({ field: "dfzh", title: "对方账号"});
        columns.push({ field: "dfmc", title: "对方名称"});
        pushColumns(columns, selectValue);
    }else if(selectValue == "zzmxGtzh"){
        columns.push({ field: "zfbzh", title: "支付宝账号", sort: true});
        columns.push({ field: "zfbmc", title: "账户名称" });
        columns.push({ field: "dfzh", title: "共同账户", sort: true});
        columns.push({ field: "gthys", title: "共同联系人数", sort: true});
        pushColumns(columns, selectValue);
    }else if(selectValue == "jyjlTjjg"){
        columns.push({ field: "dyxcsj", title: "店铺名", sort: true});
        columns.push({ field: "dfzh", title: "卖家账号", sort: true});
        columns.push({ field: "dfmc", title: "账户名称"});
        pushColumns(columns, selectValue);
    }else if(selectValue == "jyjlTjjgs"){
        $("#jzje").attr("disabled",true);
        columns.push({ field: "zfbzh", title: "买家用户Id", sort: true});
        columns.push({ field: "zfbmc", title: "买家信息", sort: true});
        columns.push({ field: "jyzt", title: "交易状态"});
        columns.push({ field: "dfzh", title: "卖家用户Id", sort: true});
        columns.push({ field: "dfmc", title: "卖家信息", sort: true});
        columns.push({ field: "fkzcs", title: "购买总次数", sort: true});
        columns.push({ field: "fkzje", title: "购买总金额", sort: true});
    }else if(selectValue == "jyjlSjdzs"){
        $("#jzje").attr("disabled",true);
        columns.push({ field: "mjyhid", title: "买家用户Id", sort: true});
        columns.push({ field: "mjxx", title: "买家信息", sort: true});
        columns.push({ field: "sjzcs", title: "收件总次数", sort: true});
        columns.push({ field: "czzje", title: "出账总金额", sort: true});
        columns.push({ field: "sjdzs", title: "收件地址数", sort: true});
    }
    jQuery("#dataTable").raytable({
        datasource: { data: data, keyfield: 'id'},
        columns: columns
    });
}

// 阈值提交
function determineThresholdValue(){
    var selectValue = $("#a1").val();
    var czje = $('#czje').val();
    var jzje = $('#jzje').val();

    for(var i=0;i<yjdcList.length;i++){
        if(yjdcList[i]['field']==selectValue){
            yjdcList[i]['czje'] = czje;
            yjdcList[i]['jzje'] = jzje;
        }
    }
    alertify.set('notifier','position', 'top-center');
    if(selectValue=='zhmxJczz'){
        alertify.success("账户明细进出总账统计阈值设置成功!");
    }else if(selectValue=='zhmxTjjg') {
        alertify.success("账户明细账户与账户统计阈值设置成功!");
    }else if(selectValue=='zhmxTjjgs'){
        alertify.success("账户明细账户与银行卡统计阈值设置成功!");
    }else if(selectValue=='zhmxJylx'){
        alertify.success("账户明细账户交易类型统计阈值设置成功!");
    }else if(selectValue=='zzmxTjjg'){
        alertify.success("转账明细统计结果阈值设置成功!");
    }else if(selectValue=='zzmxTjjgs'){
        alertify.success("转账明细对手账户阈值设置成功!");
    }else if(selectValue=='zzmxGtzh'){
        alertify.success("转账明细共同账户阈值设置成功!");
    }else if(selectValue=='jyjlTjjg'){
        alertify.success("交易卖家账户信息阈值设置成功!");
    }else if(selectValue=='jyjlTjjgs'){
        alertify.success("交易买家账户信息阈值设置成功!");
    }else{
        alertify.success("交易记录地址统计阈值设置成功!");
    }
    if(selectValue=='jyjlSjdzs'){
        $("#yjdc").attr("disabled",true);
    }else{
        $("#yjdc").attr("disabled",false);
        $("#next").attr("disabled",false);
    }
}

// 下一个
function nextYjdc(){
    $("#jzje").val("");
    $("#czje").val("");
    var sel = document.getElementById("a1");
    var index = sel.selectedIndex;
    var selectLength = sel.length-1;
    if(index<selectLength){
        sel[index+1].selected=true;
    }
    $("#next").attr("disabled",true);
    yjdcDownload(true)
}

// 批量导出
function batchExport(){
    var url = "http://localhost:8080/SINOFAITH/zfbZzmx/batchExport";
    var form = $("<form></form>").attr("action", url).attr("method", "post");
    form.append($("<input></input>").attr("type", "hidden").attr("name", "yjdcList").attr("value",JSON.stringify(yjdcList)));
    form.appendTo('body').submit().remove();
    alertify.set('notifier','position', 'top-center');
    alertify.success("正在导出数据，请等待.....");
}
