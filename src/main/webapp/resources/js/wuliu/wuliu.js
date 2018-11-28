// 跳转页面
function wuliuSkip(code) {
    var totalPage = $("#totalPage").text();
    var onPage = $("#num").val();
    if(onPage ==="" || onPage === 0 || parseInt(onPage) <=0){
        alert("请输入你要跳转的页数！");
        return;
    }
    if(parseInt(onPage)>parseInt(totalPage)){
        $("#num").val(totalPage);
        return;
    } else {
        location="/SINOFAITH/wuliu"+code+"/seach?pageNo="+onPage;
    }
}

// 数据上传
function UploadWuliu() {
    var fileObj = document.getElementById("file");// js 获取文件对象
    var file = $("#file").val();
    if(file==''){
        alertify.set('notifier','position', 'top-center');
        alertify.error('请选择要上传的文件夹')
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
        return;
    }
    var FileController = "/SINOFAITH/uploadWuliu"; // 接收上传文件的后台地址
    // FormData 对象
    var form = new FormData();
    form.append("aj", aj); // 可以增加表单数据
    // form.append("checkBox",checkBox);
    for(i=0;i<fileObj.files.length;i++){
        form.append("file", fileObj.files[i]); // 文件对象
    }
    var xhr = new XMLHttpRequest();                // XMLHttpRequest 对象
    xhr.open("post", FileController, true);
    xhr.onload = function(e) {
        if(this.status == 200||this.status == 304){
            if($("#c18")!=null){
                $("#c18").remove();
            }
            var resp = xhr.responseText;
            var keyList = JSON.parse(resp);
            var jsonData = JSON.stringify(resp);
            var data = $.parseJSON(jsonData);
            var i=1;
            $("#excelName").append("<select class=\"form-control\" id=\"c18\" name=\"custSource\" onchange='insertSheet("+data+")'>");
            for(var key in keyList){
                if(i==1){
                    $("#c18").append("<option value='"+key+"' selected>"+key+"</option></select>");
                    $("#c18").load(insertSheet(keyList));
                    i++;
                }else{
                    $("#c18").append("<option value='"+key+"'>"+key+"</option></select>");
                }
            }
            $('#myModal').modal('hide');
            $('#myModal1').modal('show');
        }else{
            alertify.set('notifier','position', 'top-center');
            alertify.error("错误!请联系管理员")
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
    var i = 1;
    for(var key in sheetName){
        if(i==1){
            $("#c19").append("<option value='"+key+"' selected>"+key+"</option>");
            $("#c19").load(insertTable(sheetName));
            i++;
        }else{
            $("#c19").append("<option value='"+key+"'>"+key+"</option>");
        }
    }
}

// 拼接table
function insertTable(sheetName){
    var key = $("#c19").val();
    $("#head").empty();
    for(i=1;i<18;i++){
        $("#c"+i).empty();
    }
    var size = Math.ceil(sheetName[key].length/2);
    var content = "<thead style=\"display:table;width: 350%;table-layout:fixed;\">";
    content += "<tr align=\"center\">";
    for(j=1;j<18;j++){
        $("#c"+j).append("<option value=\"无\" selected>无</option>")
    }
    for(i=0;i<size;i++){
        content += "<td width='10%' title='"+sheetName[key][i]+"'><div style='width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;'>"+sheetName[key][i]+"</div></td>";
        for(j=1;j<18;j++){
            if((sheetName[key][i].indexOf("运单号")!=-1&&j==1)||(sheetName[key][i].indexOf("单号")!=-1&&j==1)||
                (sheetName[key][i].indexOf("寄件时间")!=-1&&j==2)||(sheetName[key][i].indexOf("寄时间")!=-1&&j==2)||
                (sheetName[key][i].indexOf("寄件地址")!=-1&&j==3)||(sheetName[key][i].indexOf("寄地址")!=-1&&j==2)||
                (sheetName[key][i].indexOf("寄件人")!=-1&&j==4)||(sheetName[key][i].indexOf("寄件联系人")!=-1&&j==4)||
                (sheetName[key][i].indexOf("寄件电话")!=-1&&j==5)||(sheetName[key][i].indexOf("寄电话")!=-1&&j==5)||
                (sheetName[key][i].indexOf("寄件手机")!=-1&&j==6)||(sheetName[key][i].indexOf("托寄内容")!=-1&&j==12)||
                (sheetName[key][i].indexOf("收件地址")!=-1&&j==7)||(sheetName[key][i].indexOf("收地址")!=-1&&j==7)||
                (sheetName[key][i].indexOf("收件人")!=-1&&j==8)||(sheetName[key][i].indexOf("收件联系人")!=-1&&j==8)||
                (sheetName[key][i].indexOf("收件电话")!=-1&&j==9)||(sheetName[key][i].indexOf("收电话")!=-1&&j==9)||
                (sheetName[key][i].indexOf("收件手机")!=-1&&j==10)||(sheetName[key][i].indexOf("收件员")!=-1&&j==11)||
                (sheetName[key][i].indexOf("托寄物")!=-1&&j==12)||(sheetName[key][i].indexOf("托物")!=-1&&j==12)||
                (sheetName[key][i].indexOf("付款方式")!=-1&&j==13)||(sheetName[key][i].indexOf("付款")!=-1&&j==13)||
                (sheetName[key][i].indexOf("代收货款")!=-1&&j==14)||(sheetName[key][i].indexOf("代收货款金额")!=-1&&j==14)||
                (sheetName[key][i].indexOf("计费重量")!=-1&&j==15)||(sheetName[key][i].indexOf("重量")!=-1&&j==15)||
                (sheetName[key][i].indexOf("件数")!=-1&&j==16)||(sheetName[key][i].indexOf("运费")!=-1&&j==17)||
                (sheetName[key][i].indexOf("费用")!=-1&&j==17)){
                $("#c"+j).append("<option value='"+sheetName[key][i]+"' selected>"+sheetName[key][i]+"</option>");
            }else{
                $("#c"+j).append("<option value='"+sheetName[key][i]+"'>"+sheetName[key][i]+"</option>");
            }
        }
    }
    for(j=1;j<18;j++){
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

// 数据去重
function wuliuCount(aj) {
    var flg = 0
    if($("#checkbox1").is(":checked")){
        flg=1
    }
    var url = "/SINOFAITH/wuliujjxx/distinctCount?ajm="+aj+"&flg="+flg
    // $.get(url,function (data) {
    //     if(data == 303){
    //         alertify.alert("数据分析中..请等待跳转")
    //         setTimeout(function () {location="/SINOFAITH/wuliujjxx/seach?pageNo=1"},10000);
    //     }
    // })
    window.location=url;
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
            alertify.success("文件上传成功,等待设置字段映射");
        }
    }
}

// 封装参数
var excelData = [];
function uploadMapping(){
    var fieldList = [];
    var excelName = $("#c18").val();
    fieldList.push(excelName);
    var excelSheet = $("#c19").val();
    fieldList.push(excelSheet);
    for(i=1;i<18;i++){
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
function uploadWuliuExcel() {

    if(excelData.length<1){
        alertify.set('notifier','position', 'top-center');
        alertify.error("请至少设置一个字段映射!");
        return;
    }
    var list = JSON.stringify(excelData);
    $.ajax({
        type:"post",
        url:"/SINOFAITH/uploadWuliuExcel",
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

// 每次点击将数据清空
var page = 1
var pageNo = 1
$(function () {
    $('#myModal1').on('hide.bs.modal', function () {
        pageNo = 1;
        var tbody = window.document.getElementById("result1");
        if(tbody!=null) {
            tbody.innerHTML = ""
        }
        excelData = [];
    });
});

