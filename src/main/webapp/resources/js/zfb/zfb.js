// 跳转页面
function zfbSkip(code){
    var totalPage = $("#totalPage").text();
    var onPage = $("#num").val();
    if(onPage ==="" || onPage === 0 || parseInt(onPage) <=0){
        alertify.set('notifier','position', 'top-center');
        alertify.error("请输入你要跳转的页数！");
        return;
    }
    if(parseInt(onPage)>parseInt(totalPage)){
        $("#num").val(totalPage);
        return;
    } else {
        location="/SINOFAITH/zfb"+code+"/seach?pageNo="+onPage;
    }
}

// 上传
var optionSize;
function UploadZfb() {
    optionSize = 0;
    var checkVal = $('input[name="radioInline"]:checked').val();
    var fileObj = document.getElementById("file");// js 获取文件对象
    var file = $("#file").val();
    if(file==''){
        alertify.set('notifier','position', 'top-center');
        alertify.set('notifier','delay', 0);
        alertify.error('请选择要上传的文件夹')
        return;
    }
    var aj = $("#aj").val();
    var checkBox = 0
    if($("#checkbox1").is(':checked')){
        checkBox=1
    }
    if(aj==''){
        alertify.set('notifier','position', 'top-center');
        alertify.set('notifier','delay', 0);
        alertify.error('请填写案件名称')
        return
    }
    var FileController = "/SINOFAITH/uploadZfb"+checkVal; // 接收上传文件的后台地址
    // FormData 对象
    var form = new FormData();
    form.append("aj", aj); // 可以增加表单数据
    for(var i=0;i<fileObj.files.length;i++){
        var fileName = fileObj.files[i].name;
        var index1=fileName.lastIndexOf(".");
        var index2=fileName.length;
        var suffix=fileName.substring(index1,index2);
        if(suffix=="."+checkVal || (suffix==".xls" && checkVal=="xlsx")) {
            form.append("file", fileObj.files[i]); // 文件对象
        }
    }
    console.log(form.get("file"));
    // 上传文件为空
    if(form.get("file")==null){
        alertify.set('notifier','position', 'top-center');
        alertify.error("请至少上传一个选择的"+checkVal+"文件!");
        return;
    }
    var xhr = new XMLHttpRequest();                // XMLHttpRequest 对象
    xhr.open("post", FileController, true);
    xhr.onload = function() {
        if(this.status == 200||this.status == 304){
            if(checkVal=="xlsx"){
                if($("#c18")!=null){
                    $("#c18").remove();
                }
                var resp = xhr.responseText;
                keyList = JSON.parse(resp);
                for(var key in keyList){
                    optionSize++;
                }
                var jsonData = JSON.stringify(resp);
                var data = $.parseJSON(jsonData);
                var temp = true;
                $("#excelName").append("<select class=\"form-control\" id=\"c18\" name=\"custSource\" onchange='insertSheet("+data+")'>");
                for(var key in keyList){
                    if(temp){
                        $("#c18").append("<option value='"+key+"' selected='selected'>"+key+"</option></select>");
                        $("#c18").load(insertSheet(keyList));
                        temp = false;
                    }else{
                        $("#c18").append("<option value='"+key+"'>"+key+"</option></select>");
                    }
                }
                $('#myModal').modal('hide');
                $('#myModal1').modal('show');
            }else{
                alertify.set('notifier','position', 'top-center');
                alertify.success("导入完成!");
                $('#myModal').modal('hide');
                setTimeout(function () {document.getElementById("seachDetail").submit()},1500);
            }
        }else{
            alertify.set('notifier','position', 'top-center');
            alertify.set('notifier','delay', 0);
            alertify.error("错误!请联系管理员");
            return;
        }
    };
    xhr.upload.addEventListener("progress", progressFunction, false);
    xhr.send(form);
}

// 拼接sheet字符
function insertSheet(data){
    if($("#c19")!=null){
        $("#c19").remove();
    }
    var excelName = $("#c18").val();
    var sheetName  = data[excelName];
    var jsonData = JSON.stringify(sheetName);
    $("#excelSheet").append("<select class=\"form-control\" id=\"c19\" name=\"custSource\" onchange='insertTable("+jsonData+")'></select>");
    var temp = true;
    for(var key in sheetName){
        if(temp){
            $("#c19").append("<option value='"+key+"' selected>"+key+"</option>");
            $("#c19").load(insertTable(sheetName));
            temp = false;
        }else{
            $("#c19").append("<option value='"+key+"'>"+key+"</option>");
        }
    }
}

// 拼接table
function insertTable(sheetName){
    var sel = document.getElementById("c18");
    var index = sel.selectedIndex;
    var selectLength = sel.length-1;
    if((selectLength==index && selectLength!=0) || optionSize==1){
        $("#nextSelect").attr("disabled",true);
    }else{
        $("#nextSelect").attr("disabled",false);
    }
    var key = $("#c19").val();
    $("#head").empty();
    var size = Math.ceil(sheetName[key].length/2);
    var content = "<thead style=\"display:table;width: 350%;table-layout:fixed;\">";
    content += "<tr align=\"center\">";
    for(j=1;j<11;j++){
        $("#c"+j).empty();
        $("#c"+j).append("<option value=\"无\" selected>无</option>")
    }
    for(i=0;i<size;i++){
        content += "<td width='10%' title='"+sheetName[key][i]+"'><div style='width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;'>"+sheetName[key][i]+"</div></td>";
        for(j=1;j<11;j++){
            if((sheetName[key][i].indexOf("交易流水号")!=-1&&j==1&&sheetName[key][i]!="*银行外部渠道交易流水号")|| (sheetName[key][i].indexOf("付款支付帐号")!=-1&&j==2)||
                (sheetName[key][i].indexOf("收款支付帐号")!=-1&&j==3)|| (sheetName[key][i].indexOf("交易时间")!=-1&&j==5)||
                (sheetName[key][i].indexOf("交易金额")!=-1&&j==6)|| (sheetName[key][i].indexOf("交易类型")!=-1&&j==7)){
                $("#c"+j).append("<option value='"+sheetName[key][i]+"' selected>"+sheetName[key][i]+"</option>");
            }else{
                $("#c"+j).append("<option value='"+sheetName[key][i]+"'>"+sheetName[key][i]+"</option>");
            }
        }
    }
    for(j=1;j<11;j++){
        $("#c"+j).selectOrDie();
        $("#c"+j).selectOrDie("update");
    }
    content += "</tr></thead>";
    var tbody = "<tbody style=\"display:table;width: 350%;table-layout:fixed;\"><tr align='center'>";
    for(var i=size;i<sheetName[key].length;i++){
        tbody += "<td width=\"10%\" title='"+sheetName[key][i]+"'><div style='width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;'>"+sheetName[key][i]+"</div></td>";
    }
    tbody += "</tr></tbody>";
    $("#head").append(content);
    $("#head").append(tbody);
    $("#mapping").attr("disabled",false);
}

// 封装参数
var excelData = [];
function uploadMapping(){
    var fieldList = [];
    var excelName = $("#c18").val();
    fieldList.push(excelName);
    var excelSheet = $("#c19").val();
    fieldList.push(excelSheet);
    for(i=1;i<11;i++){
        fieldList.push($("#c"+i).val());
    }
    if(excelData.length>0){
        for(j=0;j<excelData.length;j++){
            var field = excelData[j];
            if(field[0]==fieldList[0]&&field[1]==fieldList[1]){
                excelData[j] = fieldList;
                break;
            }else if(j==excelData.length-1){
                excelData.push(fieldList);
            }
        }
    }else{
        excelData.push(fieldList);
    }
    $("#mapping").attr("disabled",true);
    alertify.set('notifier','position', 'top-center');
    alertify.success(excelSheet+"</br>"+"映射成功!");
}

//导入数据
function uploadZfbExcel() {
    if(excelData.length<1){
        alertify.set('notifier','position', 'top-center');
        alertify.error("请至少设置一个字段映射!");
        return;
    }
    var list = JSON.stringify(excelData);
    $.ajax({
        type:"post",
        url:"/SINOFAITH/uploadZfbExcel",
        contentType : 'application/json;charset=utf-8',
        data:list,
        success:function (data) {
            if(data=="200"){
                alertify.set('notifier','position', 'top-center');
                alertify.success("导入完成!");
                setTimeout(function () {document.getElementById("seachDetail").submit()},1500);
            }else{
                alertify.set('notifier','position', 'top-center');
                alertify.set('notifier','delay', 0);
                alertify.error("错误!请联系管理员");
                return;
            }
        },
        dataType:"json"
    });
    alertify.set('notifier','position', 'top-center');
    alertify.set('notifier','delay', 0);
    alertify.success("正在导入数据，请等待.....");
}

// 下一个
function nextSelect(){
    var sel = document.getElementById("c18");
    var index = sel.selectedIndex;
    var selectLength = sel.length-1;
    if(index<sel.length-1){
        sel[index+1].selected=true;
    }
    insertSheet(keyList);
    if(selectLength==index+1){
        $("#nextSelect").attr("disabled",true);
    }
}

// 改变映射字段后
function selectC() {
    $("#mapping").attr("disabled",false);
}

// 文件上传数据加载
function progressFunction(evt) {
    var progressBar = document.getElementById("progressBar");
    var percentageDiv = document.getElementById("percentage");
    if (evt.lengthComputable) {
        progressBar.max = evt.total;
        progressBar.value = evt.loaded;
        percentageDiv.innerHTML = Math.round(evt.loaded / evt.total * 100)+ "%";
        if((evt.loaded/evt.total) ==1 ){
            alertify.set('notifier','position', 'top-center');
            alertify.success("文件夹上传成功\n等待导入/映射");
        }
    }
}

// 转账明细显示详情数据
function getZfbZzxxDetails(obj){
    // 用户Id
    var zfbzh = $(obj).closest("tr").find("td:eq(1)").text();
    // 转账产品名称
    var zzcpmc = $(obj).closest("tr").find("td:eq(3)").text();
    window.page = 1;
    var tbody = window.document.getElementById("result");
    var url = "/SINOFAITH/zfbZzmxTjjg/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            zfbzh:zfbzh,
            zzcpmc:zzcpmc,
            order:'dzsj',
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list;
            insert(data,tbody,zfbzh,true);
            $("#zfbzh").attr("value",zfbzh);
            $("#zzcpmc").attr("value",zzcpmc);
            $("#allRow").attr("value",msg.totalRecords);
        }
    })
}

// 转账明细排序
function orderByFilter(filter){
    var tbody = window.document.getElementById("result");
    var zfbzh = $("#zfbzh").val();
    var zzcpmc = $("#zzcpmc").val();
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    window.page = 1;
    var url = "/SINOFAITH/zfbZzmxTjjg/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            zfbzh:zfbzh,
            zzcpmc:zzcpmc,
            order:filter,
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list;
            insert(data,tbody,zfbzh,true);
            $("#zfbzh").attr("value",zfbzh);
            $("#zzcpmc").attr("value",zzcpmc);
            $("#allRow").attr("value",msg.totalRecords);
        }
    })
}

// 转账明细滚动条加载数据
var is_running = false;
function scrollF() {
    var tbody = window.document.getElementById("result");
    var zfbzh = $("#zfbzh").val();
    var zzcpmc = $("#zzcpmc").val();
    var allRow = $("#allRow").val();
    var scrollT = parseFloat(tbody.scrollTop) + parseFloat(tbody.clientHeight)
    var scrollH = parseFloat(tbody.scrollHeight)
    if (1 >= scrollH - scrollT && tbody.scrollTop != 0 && tbody.childNodes.length < allRow) {
        if (is_running == false) {
            is_running = true;
            window.page = page += 1;
            var url = "/SINOFAITH/zfbZzmxTjjg/getDetails"
            $.ajax({
                type:"post",
                dataType:"json",
                url:url,
                data:{
                    zfbzh:zfbzh,
                    zzcpmc:zzcpmc,
                    order:"xxx",
                    page:parseInt(window.page)
                },
                success:function (msg) {
                    var data = msg.list;
                    insert(data,tbody,zfbzh,false);
                    $("#zfbzh").attr("value",zfbzh);
                    $("#zzcpmc").attr("value",zzcpmc);
                    $("#allRow").attr("value",msg.totalRecords);
                    is_running = false;
                }
            })
        }
    }
}


// 转账明细插入表记录
function insert(data,tbody,zfbzh,temp){
    var str = "";
    for (i in data){
        if(i%2==0){
            str+="<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
        }else{
            str+="<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
        }
        str+="<td width=\"3%\">"+data[i].id+"</td>"+
            "<td width=\"14%\" title='"+data[i].jyh+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].jyh+"</div></td>"+
            "<td width=\"8%\" "+(data[i].fkfzfbzh==zfbzh?"style=color:red":"")+" title='"+(data[i].fkfzfbzh!=null?data[i].fkfzfbzh:"")+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].fkfzfbzh!=null?data[i].fkfzfbzh:"")+"</div></td>"+
            "<td width=\"8%\" title='"+data[i].zzcpmc+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].zzcpmc+"</div></td>"+
            "<td width=\"8%\" "+(data[i].skfzfbzh==zfbzh?"style=color:red":"")+" title='"+(data[i].skfzfbzh!=null?data[i].skfzfbzh:"")+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].skfzfbzh!=null?data[i].skfzfbzh:"")+"</div></td>"+
            "<td width=\"5%\">"+(data[i].skjgxx!=null?data[i].skjgxx:"")+ "</td>"+
            "<td width=\"7%\">"+data[i].dzsj+"</td>"+
            "<td width=\"4%\">"+data[i].zzje+"</td>"+
            "<td width=\"12%\">"+(data[i].txlsh!=null?data[i].txlsh:"")+"</td>"+
            "</tr>";
    }
    if(temp){
        tbody.innerHTML = str;
    }else{
        tbody.innerHTML += str;
    }
}

// 每次点击将数据清空
var page = 1;
var pageNo = 1;
$(function () {
    $('#myModal').on('hide.bs.modal', function () {
        var tbody = window.document.getElementById("result");
        page = 1;
        if(tbody!=null) {
            tbody.innerHTML = "";
        }
        $.ajax({url:"/SINOFAITH/zfbZzmxTjjg/removeDesc"});
    });
});

// 阀值设置
function seachChange() {
    var seachCondition = $("#seachCondition").val();
    if(seachCondition === "fkzje" || seachCondition === "skzje"){
        $("#seachCode").val("50000")
    }else{
        $("#seachCode").val("")
    }
}

function isNum(obj){
    var seachCondition = $("#seachCondition").val();
    if(seachCondition === "fkzje" || seachCondition === "skzje"){
        obj.value=obj.value.replace(/[^\d]/g,'')
    }
}

// 数据导出
function downDetailInfo(){
    var zfbzh = $("#zfbzh").val();
    var zzcpmc = $("#zzcpmc").val();
    location = "/SINOFAITH/zfbZzmxTjjg/downDetailInfo?zfbzh="+zfbzh+'&zzcpmc='+zzcpmc;
}
