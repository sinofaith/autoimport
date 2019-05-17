
$(function () {
    $('#myModal').on('hide.bs.modal', function () {
        var tbody = window.document.getElementById("result")
        if(tbody!=null) {
            tbody.innerHTML = ""
        }
        $.ajax({url:"/SINOFAITH/bankzzxx/removeDesc"})
    });
    $('#myModal1').on('hide.bs.modal', function () {
        $.ajax({url:"/SINOFAITH/bankzzxx/removeDesc"})
    });
});

function bankSkip(code) {
    var totalPage = $("#totalPage").text();
    var onPage = $("#num").val();
    if(onPage ==="" || onPage === 0 || parseInt(onPage) <=0){
        alertify.error("请输入你要跳转的页数！");
        return;
    }
    if(parseInt(onPage)>parseInt(totalPage)){
        $("#num").val(totalPage);
        return;
    } else {
        location="/SINOFAITH/bank"+code+"/seach?pageNo="+onPage;
    }
}


function seachChange() {
    var seachCondition = $("#seachCondition").val()
    var seachCode = $("#seachCode")
    if(seachCondition === "jzzje" || seachCondition === "czzje"){
        seachCode.val("大于等于50000")
    }else{
        seachCode.val("")
    }
}

var excelData = [];
var optionSize;
function UploadBank() {
    optionSize = 0;
    var checkVal = $('input[name="radioInline"]:checked').val();
    var fileObj = document.getElementById("file");// js 获取文件对象
    var file = $("#file").val();
    if(file==''){
        alertify.set('notifier','position', 'top-center');
        alertify.success('请选择要上传的文件夹')
        return;
    }
    var aj = $("#aj").val();
    // var checkBox = 0
    // if($("#checkbox1").is(':checked')){
    //     checkBox=1
    // }
    if(aj==''){
        alertify.set('notifier','position', 'top-center');
        alertify.error('请填写案件名称')
        return
    }
    var FileController = ""
    if(checkVal == "") {
        FileController = "/SINOFAITH/uploadBank"; // 接收上传文件的后台地址
    }else if(checkVal == "xlsx"){
        FileController = "/SINOFAITH/uploadBankExcel";
    }
    // FormData 对象
    var form = new FormData();
    form.append("aj", aj); // 可以增加表单数据
    // form.append("checkBox",checkBox);
    for(var i=0;i<fileObj.files.length;i++){
        var fileName = fileObj.files[i].name;
        var index1=fileName.lastIndexOf(".");
        var index2=fileName.length;
        var suffix=fileName.substring(index1,index2);
        if(fileName.indexOf("~$") != 0 && (suffix==".xls"||suffix==".xlsx"||suffix==".csv")) {
            form.append("file", fileObj.files[i]); // 文件对象
        }
    }
    var xhr = new XMLHttpRequest();                // XMLHttpRequest 对象
    xhr.open("post", FileController, true);
    xhr.onload = function(e) {
        if(this.status == 200||this.status == 304){
            if(checkVal == ""){
                alertify.set('notifier','position', 'top-center');
                alertify.success("导入完成!");
                $('#myModal').modal('hide');
                setTimeout(function () {document.getElementById("seachDetail").submit()},1500);
            }else if(checkVal == "xlsx"){
                // 清除数据
                if($("#c43")!=null){
                    $("#c43").remove();
                }
                var resp = xhr.responseText;
                keyList = JSON.parse(resp);
                for(var key in keyList){
                    optionSize++;
                }
                var jsonData = JSON.stringify(resp);
                var data = $.parseJSON(jsonData);
                var temp = true;
                $("#excelName").append("<select class=\"form-control\" id=\"c43\" name=\"custSource\" onchange='insertSheet("+data+")'>");
                for(var key in keyList){
                    if(temp){
                        $("#c43").append("<option value='"+key+"' selected='selected'>"+key+"</option></select>");
                        $("#c43").load(insertSheet(keyList));
                        temp = false;
                    }else{
                        $("#c43").append("<option value='"+key+"'>"+key+"</option></select>");
                    }
                }
                excelData = [];
                $('#myModal').modal('hide');
                $('#myModal1').modal('show');
            }
        }else{
            alertify.set('notifier','position', 'top-center');
            alertify.set('notifier','delay', 0);
            alertify.error("错误!请联系管理员")
            return
        }
    };
    xhr.upload.addEventListener("progress", progressFunction, false);
    xhr.send(form);
}

// 拼接sheet字符
function insertSheet(data){
    if($("#c44")!=null){
        $("#c44").remove();
    }
    var excelName = $("#c43").val();
    var sheetName  = data[excelName];
    var jsonData = JSON.stringify(sheetName);
    $("#excelSheet").append("<select class=\"form-control\" id=\"c44\" name=\"custSource\" onchange='insertTable("+jsonData+")'></select>");
    var temp = true;
    for(var key in sheetName){
        if(temp){
            $("#c44").append("<option value='"+key+"' selected>"+key+"</option>");
            $("#c44").load(insertTable(sheetName));
            temp = false;
        }else{
            $("#c44").append("<option value='"+key+"'>"+key+"</option>");
        }
    }
}
oldSheet = [];
// 拼接table
function insertTable(sheetName){
    oldSheet = sheetName;

    var sel = document.getElementById("c43");
    var index = sel.selectedIndex;
    var selectLength = sel.length-1;
    if((selectLength==index && selectLength!=0) || optionSize==1){
        $("#nextSelect").attr("disabled",true);
    }else{
        $("#nextSelect").attr("disabled",false);
    }
    var key = $("#c44").val();
    $("#head").empty();
    var size = Math.ceil(sheetName[key].length/2);
    var content = "<thead style=\"display:table;width: 350%;table-layout:fixed;\">";
    content += "<tr align=\"center\">";
    var startNum = 0;
    var endNum = 0;
    // 表名
    var tableName = $("#c45").val();
    if(tableName=='bank_zcxx'){
        startNum = 1;endNum = 11;
    }else if(tableName=='bank_zzxx'){
        startNum = 11;endNum = 32;
    }else if(tableName=='bank_customer'){
        startNum = 32;endNum = 43;
    }
    for(j=startNum;j<endNum;j++){
        $("#c"+j).empty();
        $("#c"+j).append("<option value=\"无\" selected>无</option>")
    }
    for(i=0;i<size;i++){
        content += "<td width='10%' title='"+sheetName[key][i]+"'><div style='width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;'>"+sheetName[key][i]+"</div></td>";
        for(j=startNum;j<endNum;j++){
            if(tableName=='bank_zcxx'){
                if((sheetName[key][i].indexOf("账户状态")!=-1&&j==startNum)||(sheetName[key][i].indexOf("交易卡号")!=-1&&j==startNum+1)||
                    (sheetName[key][i].indexOf("开户名称")!=-1&&j==startNum+2)||(sheetName[key][i].indexOf("开户人证件号码")!=-1&&j==startNum+3)||
                    (sheetName[key][i].indexOf("开户时间")!=-1&&j==startNum+4)||(sheetName[key][i].indexOf("开户网点")!=-1&&j==startNum+5)||
                    (sheetName[key][i].indexOf("账户余额")!=-1&&j==startNum+6)||(sheetName[key][i].indexOf("可用余额")!=-1&&j==startNum+7)||
                    (sheetName[key][i].indexOf("交易账号")!=-1&&j==startNum+8)||(sheetName[key][i].indexOf("账号类型")!=-1&&j==startNum+9)){
                    $("#c"+j).append("<option value='"+sheetName[key][i]+"' selected>"+sheetName[key][i]+"</option>");
                }else{
                    $("#c"+j).append("<option value='"+sheetName[key][i]+"'>"+sheetName[key][i]+"</option>");
                }
            }else if(tableName=='bank_zzxx'){
                if((sheetName[key][i].indexOf("交易账卡号")!=-1&&j==startNum)||(sheetName[key][i].indexOf("交易日期")!=-1&&j==startNum+1)||
                    (sheetName[key][i].indexOf("交易金额")!=-1&&j==startNum+2)||(sheetName[key][i].indexOf("交易余额")!=-1&&sheetName[key][i]!="对手交易余额"&&j==startNum+3)||
                    (sheetName[key][i].indexOf("收付标志")!=-1&&j==startNum+4)||(sheetName[key][i].indexOf("对手卡号")!=-1&&j==startNum+5)||
                    (sheetName[key][i].indexOf("对手户名")!=-1&&j==startNum+6)||(sheetName[key][i].indexOf("对手身份证号")!=-1&&j==startNum+7)||
                    (sheetName[key][i].indexOf("摘要说明")!=-1&&j==startNum+8)||(sheetName[key][i].indexOf("交易是否成功")!=-1&&j==startNum+9)||
                    (sheetName[key][i].indexOf("交易账号")!=-1&&j==startNum+10)||(sheetName[key][i].indexOf("对手账号")!=-1&&j==startNum+11)||
                    (sheetName[key][i].indexOf("对手开户银行")!=-1&&j==startNum+12)||(sheetName[key][i].indexOf("交易网点名称")!=-1&&j==startNum+13)||
                    (sheetName[key][i].indexOf("对手交易余额")!=-1&&j==startNum+14)||(sheetName[key][i].indexOf("对手余额")!=-1&&j==startNum+15)||
                    (sheetName[key][i].indexOf("备注")!=-1&&j==startNum+16)||(sheetName[key][i].indexOf("交易证件号码")!=-1&&j==startNum+17)||
                    (sheetName[key][i].indexOf("交易发生地")!=-1&&j==startNum+18)||(sheetName[key][i].indexOf("交易户名")!=-1&&j==startNum+19)||
                    (sheetName[key][i].indexOf("补充说明")!=-1&&j==startNum+20)){
                    $("#c"+j).append("<option value='"+sheetName[key][i]+"' selected>"+sheetName[key][i]+"</option>");
                }else{
                    $("#c"+j).append("<option value='"+sheetName[key][i]+"'>"+sheetName[key][i]+"</option>");
                }
            }else if(tableName=='bank_customer'){
                if((sheetName[key][i].indexOf("证件号码")!=-1&&sheetName[key][i]!="法人代表证件号码"&&
                sheetName[key][i]!="代办人证件号码"&&sheetName[key][i]!="其它证件_证件号码"&&j==startNum)||(sheetName[key][i].indexOf("单位电话")!=-1&&j==startNum+1)||
                   (sheetName[key][i].indexOf("单位地址")!=-1&&j==startNum+2)||(sheetName[key][i].indexOf("Email")!=-1&&j==startNum+3)||
                   (sheetName[key][i].indexOf("工作单位")!=-1&&j==startNum+4)||(sheetName[key][i].indexOf("联系电话")!=-1&&j==startNum+5)||
                   (sheetName[key][i].indexOf("联系手机")!=-1&&j==startNum+6)||(sheetName[key][i].indexOf("客户名称")!=-1&&j==startNum+7)||
                   (sheetName[key][i].indexOf("现住址_行政区划")!=-1&&j==startNum+8)||(sheetName[key][i].indexOf("证件类型")!=-1
                &&sheetName[key][i]!="法人代表证件类型"&&sheetName[key][i]!="代办人证件类型"&&j==startNum+9)||
                   (sheetName[key][i].indexOf("住宅电话")!=-1&&j==startNum+10)){
                    $("#c"+j).append("<option value='"+sheetName[key][i]+"' selected>"+sheetName[key][i]+"</option>");
                }else{
                    $("#c"+j).append("<option value='"+sheetName[key][i]+"'>"+sheetName[key][i]+"</option>");
                }
            }
        }
    }
    for(j=startNum;j<endNum;j++){
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

bankList = ["bank_zcxx","bank_zzxx","bank_customer"];
function insertMappingFields(){
    // 表名
    var tableName = $("#c45").val();
    for(var i in bankList){
        if(tableName==bankList[i]){
            $("#"+bankList[i]).css("display","block");
        }else{
            $("#"+bankList[i]).css("display", "none");
        }
    }
    insertTable(oldSheet)
}

// 下一个
function nextSelect(){
    var sel = document.getElementById("c43");
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

// 封装参数
function uploadMapping(){
    var fieldList = [];
    var excelName = $("#c43").val();
    fieldList.push(excelName);
    var excelSheet = $("#c44").val();
    fieldList.push(excelSheet);
    var startNum = 0;
    var endNum = 0;
    // 表名
    var tableName = $("#c45").val();
    fieldList.push(tableName);
    if(tableName=='bank_zcxx'){
        startNum = 1;endNum = 11;
    }else if(tableName=='bank_zzxx'){
        startNum = 11;endNum = 32;
    }else if(tableName=='bank_customer'){
        startNum = 32;endNum = 43;
    }
    for(i=startNum;i<endNum;i++){
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
    console.log(excelData);

    $("#mapping").attr("disabled",true);
    alertify.set('notifier','position', 'top-center');
    alertify.success(excelSheet+"</br>"+"映射成功!");
}

//导入数据
function uploadBankExcel() {
    if(excelData.length<1){
        alertify.set('notifier','position', 'top-center');
        alertify.error("请至少设置一个字段映射!");
        return;
    }
    var list = JSON.stringify(excelData);
    $.ajax({
        type:"post",
        url:"/SINOFAITH/insertBankExcel",
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

function progressFunction(evt) {
    var progressBar = document.getElementById("progressBar");
    var percentageDiv = document.getElementById("percentage");
    if (evt.lengthComputable) {
        progressBar.max = evt.total;
        progressBar.value = evt.loaded;
        percentageDiv.innerHTML = Math.round(evt.loaded / evt.total * 100)+ "%";
        if((evt.loaded/evt.total) ==1){
            alertify.set('notifier','position', 'top-center');
            alertify.success("文件夹上传成功!请等待数据导入...");
        }
    }
}


function editBp(yhkkh,dsfzh) {
    if(dsfzh==1){
        dsfzh=0;
    }else if (dsfzh==0){
        dsfzh =1;
    }
    var url = "/SINOFAITH/banktjjg/editBp?yhkkh="+yhkkh+"&dsfzh="+dsfzh;
    $.get(url,function (data) {
        if(data=="200"){
            alertify.set('notifier','position', 'top-center');
            alertify.success("修改成功");
            setTimeout(function () {document.getElementById("seachDetail").submit()},1000);
        }else{
            alertify.set('notifier','position', 'top-center');
            alertify.error("修改失败");
        }
    })
}

function getZzDetails(obj,type) {
    var yhkkh = $(obj).closest("tr").find("td:eq(1)").attr("title");
    var dfkh = $(obj).closest("tr").find("td:eq(3)").attr("title");
    window.page = 1
    var tjsum = $(obj).closest("tr").find("td:eq(3)").text();
    var tssum = $(obj).closest("tr").find("td:eq(5)").text();
    var sum = tssum;
    if(type=="tjjg"){
        dfkh=""
        sum = tjsum;
    }

    var tbody = window.document.getElementById("result")
    var url = "/SINOFAITH/bankzzxx/getDetails"
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            yhkkh:yhkkh,
            dfkh:dfkh,
            order:"jysj",
            type:type,
            sum:sum,
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list
            var str = ""
            for (i in data){
                if(data[i].jyye ==-1){
                    data[i].jyye = "";
                }
                if(i%2==0){
                    str+="<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                }else{
                    str+="<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                }
                str+="<td width=\"4%\">"+data[i].id+"</td>"+
                    "<td width=\"13%\">"+data[i].yhkkh+"</td>"+
                    "<td width=\"5%\" title=\""+data[i].jyxm+"\"> <div style=\"width:50px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].jyxm+"</div></td>"+
                    "<td width=\"10%\">"+data[i].jysj+"</td>"+
                    "<td width=\"7%\">"+data[i].jyje+"</td>"+
                    "<td width=\"7%\">"+data[i].jyye+"</td>"+
                    "<td width=\"7%\">"+data[i].sfbz+"</td>"+
                    "<td width=\"13%\">"+data[i].dskh+"</td>"+
                    "<td width=\"13%\" title=\""+data[i].dsxm+"\"> <div style=\"width:150px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].dsxm+"</div></td>"+
                    "<td width=\"7%\" title=\""+data[i].bz+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].bz+"</div></td>"+
                    "<td width=\"7%\" title=\""+data[i].jyfsd+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].jyfsd+"</div></td>"+
                    "<td width=\"7%\" title=\""+data[i].jywdmc+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].jywdmc+"</div></td>"+
                    "<td width=\"7%\" title=\""+data[i].zysm+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].zysm+"</div></td>"+
                    // "<td width=\"8%\">"+data[i].jsje+"</td>"+
                    "</tr>";
            }
            tbody.innerHTML = str
            $("#yhkkh").attr("value",yhkkh);
            $("#dfkh").attr("value",dfkh);
            $("#allRow").attr("value",msg.totalRecords)
            // title.innerText ="<"+jyzh+","+jylx+">"
        }
    })
}


function orderByFilter(type,filter) {
    var tbody = window.document.getElementById("result")
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    var yhkkh = $("#yhkkh").val();
    var dfkh = $("#dfkh").val();
    var sum = $("#allRow").val();
    window.page = 1

    if(type=="tjjg"){
        dfkh=""
    }
    var url = "/SINOFAITH/bankzzxx/getDetails"
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            yhkkh:yhkkh,
            dfkh:dfkh,
            order:filter,
            type:type,
            sum:sum,
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list
            var str = ""
            for (i in data){
                if(data[i].jyye ==-1){
                    data[i].jyye = "";
                }
                if(i%2==0){
                    str+="<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                }else{
                    str+="<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                }
                str+="<td width=\"4%\">"+data[i].id+"</td>"+
                    "<td width=\"13%\">"+data[i].yhkkh+"</td>"+
                    "<td width=\"5%\" title=\""+data[i].jyxm+"\"> <div style=\"width:50px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].jyxm+"</div></td>"+
                    "<td width=\"10%\">"+data[i].jysj+"</td>"+
                    "<td width=\"7%\">"+data[i].jyje+"</td>"+
                    "<td width=\"7%\">"+data[i].jyye+"</td>"+
                    "<td width=\"7%\">"+data[i].sfbz+"</td>"+
                    "<td width=\"13%\">"+data[i].dskh+"</td>"+
                    "<td width=\"13%\" title=\""+data[i].dsxm+"\"> <div style=\"width:150px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].dsxm+"</div></td>"+
                    "<td width=\"7%\" title=\""+data[i].bz+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].bz+"</div></td>"+
                    "<td width=\"7%\" title=\""+data[i].jyfsd+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].jyfsd+"</div></td>"+
                    "<td width=\"7%\" title=\""+data[i].jywdmc+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].jywdmc+"</div></td>"+
                    "<td width=\"7%\" title=\""+data[i].zysm+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].zysm+"</div></td>"+
                    // "<td width=\"8%\">"+data[i].jsje+"</td>"+
                    "</tr>";
            }
            tbody.innerHTML = str
            $("#yhkkh").attr("value",yhkkh);
            $("#dfkh").attr("value",dfkh);
            $("#allRow").attr("value",msg.totalRecords)
            // title.innerText ="<"+jyzh+","+jylx+">"
        }
    })


}

var page = 1
var is_running = false
function scrollF(type) {
    var tbody = window.document.getElementById("result")
    var allRow = $("#allRow").val()
    var scrollT = parseFloat(tbody.scrollTop) + parseFloat(tbody.clientHeight)
    var scrollH = parseFloat(tbody.scrollHeight)
    if (1 >= scrollH - scrollT && tbody.scrollTop != 0 && tbody.childNodes.length < allRow) {
        if(is_running==false) {
            is_running = true
            var yhkkh = $("#yhkkh").val();
            var dfkh = $("#dfkh").val();
            window.page = page += 1

            if(type=="tjjg"){
                dfkh=""
            }
            var url = "/SINOFAITH/bankzzxx/getDetails"
            $.ajax({
                type: "post",
                dataType: "json",
                url: url,
                data: {
                    yhkkh:yhkkh,
                    dfkh:dfkh,
                    order:"xx",
                    type:type,
                    sum:allRow,
                    page: parseInt(window.page)
                },
                success: function (msg) {
                    var data = msg.list
                    var str = ""
                    for (i in data) {
                        if(data[i].jyye ==-1){
                            data[i].jyye = "";
                        }
                        if (i % 2 == 0) {
                            str += "<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                        } else {
                            str += "<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                        }
                        str+="<td width=\"4%\">"+data[i].id+"</td>"+
                            "<td width=\"13%\">"+data[i].yhkkh+"</td>"+
                            "<td width=\"5%\" title=\""+data[i].jyxm+"\"> <div style=\"width:50px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].jyxm+"</div></td>"+
                            "<td width=\"10%\">"+data[i].jysj+"</td>"+
                            "<td width=\"7%\">"+data[i].jyje+"</td>"+
                            "<td width=\"7%\">"+data[i].jyye+"</td>"+
                            "<td width=\"7%\">"+data[i].sfbz+"</td>"+
                            "<td width=\"13%\">"+data[i].dskh+"</td>"+
                            "<td width=\"13%\" title=\""+data[i].dsxm+"\"> <div style=\"width:150px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].dsxm+"</div></td>"+
                            "<td width=\"7%\" title=\""+data[i].bz+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].bz+"</div></td>"+
                            "<td width=\"7%\" title=\""+data[i].jyfsd+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].jyfsd+"</div></td>"+
                            "<td width=\"7%\" title=\""+data[i].jywdmc+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].jywdmc+"</div></td>"+
                            "<td width=\"7%\" title=\""+data[i].zysm+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].zysm+"</div></td>"+
                            // "<td width=\"8%\">"+data[i].jsje+"</td>"+
                            "</tr>";
                    }
                    $("#result").append(str)
                    $("#yhkkh").attr("value",yhkkh);
                    $("#dfkh").attr("value",dfkh);
                    $("#allRow").attr("value",msg.totalRecords)
                    is_running = false
                }
            })
        }
    }
}


function downDetailZh(){
    var yhkkh = $("#yhkkh").val();
    var dskh =$("#dfkh").val();

    location="/SINOFAITH/bankzzxx/downDetailZh?yhkkh="+yhkkh+"&dskh="+dskh
}
function downGtlxr(){
    var dfkh = $("#dfkh").val();
    location="/SINOFAITH/bankgtzh/downgtlxr?dfzh="+dfkh
}

function compare(str,target) {
    var d =  new Array();
    var n = str.length;
    var m = target.length;
    var i;                  // 遍历str的
    var j;                  // 遍历target的
    var ch1;               // str的
    var ch2;               // target的
    var temp;               // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
    if (n == 0) { return m; }
    if (m == 0) { return n; }
    for(var i =0 ;i<=n;i++){
        d[i] = new Array();
    }

    for (var i = 0; i <= n; i++)
    {                       // 初始化第一列
        d[i][0] = i;
    }
    for (var j = 0; j <= m; j++)
    {                       // 初始化第一行
        d[0][j] = j;
    }
    for (var i = 1; i <= n; i++)
    {                       // 遍历str
        ch1 = str.charAt(i - 1);
        // 去匹配target
        for (var j = 1; j <= m; j++)
        {
            ch2 = target.charAt(j - 1);
            if (ch1 == ch2 || ch1 == ch2+32 || ch1+32 == ch2)
            {
                temp = 0;
            } else
            {
                temp = 1;
            }
            // 左边+1,上边+1, 左上角+temp取最小
            d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
        }
    }
    return d[n][m];
}

function  min(one,two,three) {
    return (one = one < two ? one : two) < three ? one : three;
}

function getSimilarityRatio(str,target)
{
    return 1 - compare(str, target) / Math.max(str.length, target.length);
}
