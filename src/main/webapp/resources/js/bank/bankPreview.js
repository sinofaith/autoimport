var yjdcList = [];
var f = ['bankTjjg','bankTjjgs','bankGtzh'];
for(var value in f){
    yjdcList.push({field:f[value],czje:'',jzje:''});
}

function select_change(){
    $("#jzje").val("");
    $("#czje").val("");
    yjdcDownload(true);
}

function yjdcDownload(flag){
    var select1 = $("#a1").val();
    // 判断是否是最后一个
    if(select1!='bankGtzh' || flag){
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
        url:"/SINOFAITH/bank/previewTable",
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

function pushColumns(columns){
    columns.push({ field: "jyzcs", title: "交易总次数", sort: true});
    columns.push({ field: "jzzcs", title: "进账总次数", sort: true});
    columns.push({ field: "jzzje", title: "进账总金额", sort: true});
    columns.push({ field: "czzcs", title: "出账总次数", sort: true});
    columns.push({ field: "czzje", title: "出账总金额", sort: true});
}

// 插入表格
function insertTable1(data){
    var selectValue = $("#a1").val();
    var columns = [];
    columns.push({ field: "id", title: "序号", sort:true });
    if(selectValue != "bankGtzh"){
        columns.push({ field: "jyzh", title: "交易卡号", sort:true });
        columns.push({ field: "name", title: "交易户名", sort:true });
    }else{
        columns.push({ field: "name", title: "姓名", sort:true });
        columns.push({ field: "jyzh", title: "交易卡号", sort:true });
    }
    if(selectValue == "bankTjjg"){
        pushColumns(columns);
        columns.push({field:"zhlb",title:"账户类别", sort: true});
    }else if(selectValue == "bankTjjgs"){
        columns.push({ field: "dfzh", title: "对方卡号", sort: true });
        columns.push({ field: "dfxm", title: "对方户名", sort: true });
        pushColumns(columns);
    }else if(selectValue == "bankGtzh"){
        columns.push({ field: "dfzh", title: "对方卡号", sort: true });
        columns.push({ field: "dfxm", title: "对方姓名", sort: true });
        columns.push({ field: "count", title: "共同联系人数", sort: true });
        pushColumns(columns);
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
    if(selectValue=='bankTjjg'){
        alertify.success("账户统计信息阈值设置成功!");
    }else if(selectValue=='bankTjjgs') {
        alertify.success("账户点对点统计信息阈值设置成功!");
    }else{
        alertify.success("公共账户统计信息阈值设置成功!");
    }
    console.log(yjdcList);
    if(selectValue=='bankGtzh'){
        $("#yjdc").attr("disabled",true);
    }else{
        $("#yjdc").attr("disabled",false);
        $("#next").attr("disabled",false);
    }
}

// 下一个
function nextYjdc(){
    $("#jzje").val("50000");
    $("#czje").val("50000");
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
    var url = "http://localhost:8080/SINOFAITH/bank/batchExport";
    var form = $("<form></form>").attr("action", url).attr("method", "post");
    form.append($("<input></input>").attr("type", "hidden").attr("name", "yjdcList").attr("value",JSON.stringify(yjdcList)));
    form.appendTo('body').submit().remove();
    alertify.set('notifier','position', 'top-center');
    alertify.success("正在导出数据，请等待.....");
}