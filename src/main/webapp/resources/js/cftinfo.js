$(document).ready(function(){
    // 载入文件按钮状态转换
    $('#btnLoadFile').mouseover(function(){
        $(this).attr('src', '/AMD/resources/img/loadFile_hover.png');
    });

    $('#btnLoadFile').mouseout(function(){
        $(this).attr('src', '/AMD/resources/img/loadFile.png');
    });


// //文件数量限制
//     var filesCount=2000;
// //文件夹大小限制 2000M
//     var filesSize=2147483648;
// //实际的文件数量
//     var actual_filesCount=0;
// //实际的文件夹大小
//     var actual_filesSize=0;
//
//     function commit(){
//         //判断是否选中文件夹
//         var file=$("#fileFolder").val();
//         if(file==''){
//             $("#msg").text('请选择要上传的文件夹');
//             return;
//         }
//
//
//
//         $("#fileUploadForm").submit();
//
//     }
    // 导入按钮绑定事件
    // $('#btnLoadFile').click(function() {
    //     $("input[type='file']").click();
    // });
    //
    // $("#uploadFileForm>input[type='file']").change(function(event) {
    //     var file = $("#file").val();
    //     if(file==''){
    //         alertify.alert('请选择要上传的文件夹')
    //         return;
    //     }
    //
    //     var files = event.target.files;
    //     var actual_filesCount=files.length;
    //
    //     if(actual_filesCount>1000){
    //         alertify.alert("文件过多,单次可上传1000个文件");
    //         return;
    //     }
    //     var actual_Size = 0
    //     for(var i=0,f;f=files[i];++i){
    //         actual_Size+=f.size;
    //         if(actual_Size>1024*1024*1024){
    //             alertify.alert("单次文件夹不能超过1024M")
    //             return;
    //         }
    //     }
    //
    //     $('#uploadFileForm').ajaxSubmit({
    //         dataType: 'text',
    //         success: function(result) {
    //             if (result == "") {
    //                 alertify.alert('上传文件出现问题！请检查网络环境是否正常或文件格式及内容是否符合标准！');
    //             } else{
    //                 alertify.alert("导入成功.");
    //                 setTimeout(function () {document.getElementById("seachDetail").submit()},1000);
    //             }
    //         }
    //     });
    //
    //     $(this).val('');
    // });
    //
    // $('#accessory').click(function() {
    //     $("input[type='file']").click();
    // });
    //
    // $("#accessoryForm>input[type='file']").change(function(event) {
    //     //document.getElementById("accessoryForm").action = "/AMD/crimegroupinfo/file/accessory";
    //     //document.getElementById("accessoryForm").submit();
    //     $('#accessoryForm').ajaxSubmit({
    //         dataType: 'json',
    //         success: function(result) {
    //             if (result == null) {
    //                 alertify.alert('上传文件出现问题！可能服务器存在问题,请联系管理员！');
    //             } else{
    //                 $('.accessory').append("<li>"+result.fileName+"."+result.suffix+" <img width='16px' class='delete' title='删除' src='/AMD/resources/thirdparty/assets/images/images/delete.png'></li>");
    //                 $(".accessory li .delete").bind({'click':function(){
    //                     var that = this;
    //                     var file = $(this).parent().text().trim();
    //                     var fileName = file.substring(0,file.lastIndexOf("."));
    //                     var suffix = file.substring(file.lastIndexOf(".")+1,file.length );
    //                     var crimegroupinfoid = $("#crimegroupinfoid").val();
    //                     var path = "/AMD/crimegroupinfo/accessorydelete/"+crimegroupinfoid+"/"+fileName+"/"+suffix
    //                     $.ajax({
    //                         url: path,
    //                         type: 'POST',
    //                         dataType: 'text',
    //                         success: function() {
    //                             $('.accessory').find($(that).parent()).remove();
    //                         }
    //
    //                     });
    //                 }});
    //                 alert("上传成功！");
    //             }
    //         }
    //     });
    // });
    // $(".accessory li .delete").click(function(){
    //     var that = this;
    //     var file = $(this).parent().text().trim();
    //     var fileName = file.substring(0,file.lastIndexOf("."));
    //     var suffix = file.substring(file.lastIndexOf(".")+1,file.length );
    //     var crimegroupinfoid = $("#crimegroupinfoid").val();
    //     var path = "/AMD/crimegroupinfo/accessorydelete/"+crimegroupinfoid+"/"+fileName+"/"+suffix
    //     $.ajax({
    //             url: path,
    //             type: 'POST',
    //             dataType: 'text',
    //             success: function() {
    //                 $('.accessory').find($(that).parent()).remove();
    //             }
    //
    //     });
    //     });
    //
    // //导入按钮绑定事件
    // $('#btnduibi').click(function() {
    //     $("#dbfile").click();
    // });
    //
    // $("#dbfile").change(function(event) {
    //     var filepath = $("#dbfile").val();
    //     var extStart = filepath.lastIndexOf(".");
    //     var ext = filepath.substring(extStart,filepath.length).toUpperCase();
    //     if(ext != ".XLS" && ext !=".XLSX"){
    //         alert("文件限于EXCEL格式");
    //         $("#dbfile").val('');
    //         return false;
    //     }
    //     var maxsize = 5*1024*1024;
    //     var obj_file = document.getElementById("dbfile");
    //     var filesize = obj_file.files[0].size;
    //     if(filesize>maxsize){
    //         alert("上传文件太大,请处理一下！");
    //         $("#dbfile").val('');
    //         return false;
    //     }
    //     $('#duibiFileForm').ajaxSubmit({
    //         dataType: 'text',
    //     });
    //
    //     $("#dbfile").val('');
    //     alert("上传成功，请等待处理！");
    // });
    //这里给所有ajax请求添加一个complete函数
});

// function AddCrimeterrace() {
//     $.ajax({
//         url: "/AMD/crimegroupinfo/editcheck",
//         type: 'POST',
//         dataType: 'text',
//         success: function(result) {
//             if(result==="1"){
//                 if(confirm("你还没有登录，需要登录？")){
//                     location="/AMD/homepage";
//                 }
//             } else {
//                 location="/AMD/crimeterrace/linkAddCrimeterrace/0"
//             }
//         }
//     });
// }

// function AddCrimegroupinfo() {
//     $.ajax({
//         url: "/AMD/crimegroupinfo/editcheck",
//         type: 'POST',
//         dataType: 'text',
//         success: function(result) {
//             if(result==="1"){
//                 if(confirm("你还没有登录，需要登录？")){
//                     location="/AMD/homepage";
//                 }
//             } else {
//                 location="/AMD/crimegroupinfo/linkAddCrimegroup"
//             }
//         }
//     });
// }
//
// function CGdownload() {
//     location="/AMD/crimegroupinfo/download"
// }
//
// function ZGdownload() {
//     location="/AMD/crimegroupinfo/zgdownload"
// }
//
// function submitSeach(){
//     var seachCode = $("#seachCode").val();
//
//     if(seachCode ===""){
//         alert("查询条件不能为空！");
//     } else {
//         if(seachCode ==="000" || seachCode ==="未实名"){
//             alert("未实名的数据太多，请优化查询条件！");
//         } else{
//             document.getElementById("SeachCodeFrom").action = "/AMD/criteriaquery/SeachCode";
//             document.getElementById("SeachCodeFrom").submit();
//         }
//     }
// }

function seachChange() {
        var seachCondition = $("#seachCondition").val()
        var seachCode = $("#seachCode")
        if(seachCondition === "jzzje" || seachCondition === "czzje"){
            seachCode.val("50000")
        }else{
            seachCode.val("")
        }
}


//
// function fileOnclick(){
//     $("input[id='files1']").click();
// }
//
// function importfile(){
//     var resultFile = document.getElementById("files1").files[0];
//     if (resultFile) {
//         var reader = new FileReader();
//         reader.readAsText(resultFile, 'utf-8');
//         reader.onload = function (e) {
//             var urlData = this.result;
//             document.getElementById("seachCode").value = urlData;
//         }
//     }
// }

function cftSkip(code){
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
        location="/SINOFAITH/cft"+code+"/seach?pageNo="+onPage;
    }
}


// function dbResult(){
//     $.ajax({
//         type:"get",
//         url:"/AMD/fileuploading/file/dbfilename",
//         success: function(result){
//             var filecontent = "<tr><td width='33%'>上传人姓名</td><td width='33%'>上传文件</td><td width='34%'>对比结果</td></tr>";
//             var strLine = result.split(",");
//             for(var i=strLine.length-1;i>=0;i--){
//                 var oneline = strLine[i].split("_");
//                 var resultfile = "Result"+oneline[1].split(".")[0]+".csv";
//                 var resultfilename = oneline[0]+"_"+ resultfile;
//                 filecontent = filecontent + "<tr><td  width='33%'>"+oneline[0]+"</td><td  width='33%'>" +
//                     "<a href='/AMD/criteriaquery/upload/download?filename="+strLine[i]+"'>"+oneline[1]+"</a></td>" +
//                     "<td  width='34%'><a href='/AMD/criteriaquery/result/download?filename="+resultfilename+"'>"+resultfile+"</a></td></tr>"
//
//             }
//             $("#filenames").append(filecontent);
//         }
//     });
//     $("#filename").show();
// }
//
// function relationextension(){
//     obj = document.getElementsByName("sfzhmval");
//     check_val = [];
//     for(k in obj){
//         if(obj[k].checked)
//             check_val.push(obj[k].value);
//     }
//
//     window.open("http://10.38.14.209:9000/relation2.html?person="+check_val);
//
// }
//
// function matrix() {
//     obj = document.getElementsByName("sfzhmval");
//     check_val = [];
//     for (k in obj) {
//         if (obj[k].checked)
//             check_val.push(obj[k].value);
//     }
//     window.open("http://10.38.14.209.83:9000/matrix2.html#" + check_val);
// }

var optionSize;
function UploadCft() {
    optionSize = 0;
    var checkVal = $('input[name="radioInline"]:checked').val();
    var fileObj = document.getElementById("file");// js 获取文件对象
    var file = $("#file").val();
    if(file==''){
        alertify.set('notifier','position', 'top-center');
        alertify.alert('请选择要上传的文件夹')
        return;
    }
    var aj = $("#aj").val();
    var checkBox = 0;
    if($("#checkbox1").is(':checked')){
        checkBox=1
    }
    if(aj==''){
        alertify.set('notifier','position', 'top-center');
        alertify.set('notifier','delay', 0);
        alertify.error('请填写案件名称');
        return
    }
    var FileController = "/SINOFAITH/uploadCft"+checkVal; // 接收上传文件的后台地址
    // FormData 对象
    var form = new FormData();
    form.append("aj", aj); // 可以增加表单数据
    form.append("checkBox",checkBox);
    for(var i=0;i<fileObj.files.length;i++){
        var fileName = fileObj.files[i].name;
        var index1=fileName.lastIndexOf(".");
        var index2=fileName.length;
        var suffix=fileName.substring(index1,index2);
        if(fileName.indexOf("~$") != 0 && (suffix=="."+checkVal || (suffix==".xls" && checkVal==".xlsx"))) {
            form.append("file", fileObj.files[i]); // 文件对象
        }
    }
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
            if(checkVal == 'txt'){
                alertify.set('notifier','position', 'top-center');
                alertify.success("导入完成!");
                $('#myModal').modal('hide');
                setTimeout(function () {document.getElementById("seachDetail").submit()},1500);
            }else if(checkVal == 'xlsx'){
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
    for(j=1;j<16;j++){
        $("#c"+j).empty();
        $("#c"+j).append("<option value=\"无\" selected>无</option>")
    }
    for(i=0;i<size;i++){
        content += "<td width='10%' title='"+sheetName[key][i]+"'><div style='width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;'>"+sheetName[key][i]+"</div></td>";
        for(j=1;j<16;j++){
            if((sheetName[key][i].indexOf("付款支付帐号")!=-1&&j==1) ||(sheetName[key][i].indexOf("交易流水号")!=-1&&j==2&&sheetName[key][i]!="*银行外部渠道交易流水号")||
                (sheetName[key][i].indexOf("*交易主体的出入账标识")!=-1&&j==3) ||(sheetName[key][i].indexOf("交易类型")!=-1&&j==4)||
                (sheetName[key][i].indexOf("交易金额")!=-1&&j==5)||(sheetName[key][i].indexOf("交易余额")!=-1&&j==6)||
                (sheetName[key][i].indexOf("交易时间")!=-1&&j==7) || (sheetName[key][i].indexOf("银行类型")!=-1&&j==8)||
                (sheetName[key][i].indexOf("交易说明")!=-1&&j==9) || (sheetName[key][i].indexOf("商户名称")!=-1&&j==10)||
                (sheetName[key][i].indexOf("付款支付帐号")!=-1&&j==11) || (sheetName[key][i].indexOf("交易金额")!=-1&&j==12)||
                (sheetName[key][i].indexOf("收款支付帐号")!=-1&&j==13) || (sheetName[key][i].indexOf("接收时间")!=-1&&j==14)||
                (sheetName[key][i].indexOf("接收金额")!=-1&&j==15)){
                $("#c"+j).append("<option value='"+sheetName[key][i]+"' selected>"+sheetName[key][i]+"</option>");
            }else{
                $("#c"+j).append("<option value='"+sheetName[key][i]+"'>"+sheetName[key][i]+"</option>");
            }
        }
    }
    for(j=1;j<16;j++){
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
    for(i=1;i<16;i++){
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
function uploadCftExcel() {
    if(excelData.length<1){
        alertify.set('notifier','position', 'top-center');
        alertify.error("请至少设置一个字段映射!");
        return;
    }
    var list = JSON.stringify(excelData);
    $.ajax({
        type:"post",
        url:"/SINOFAITH/uploadCftExcel",
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

function progressFunction(evt) {
    var progressBar = document.getElementById("progressBar");
    var percentageDiv = document.getElementById("percentage");
    if (evt.lengthComputable) {
        progressBar.max = evt.total;
        progressBar.value = evt.loaded;
        percentageDiv.innerHTML = Math.round(evt.loaded / evt.total * 100)+ "%";
        if((evt.loaded/evt.total) ==1 ){
            alertify.set('notifier','position', 'top-center');
            alertify.set('notifier','delay', 0);
            alertify.success("文件夹上传成功\n请等待数据导入...");
        }
    }
}


zzbds = /^[\u4E00-\u9FA5\uF900-\uFA2D]/

var page = 1
var is_running = false
function scrollF() {
        var tbody = window.document.getElementById("result")
        var allRow = $("#allRow").val()
        var scrollT = parseFloat(tbody.scrollTop) + parseFloat(tbody.clientHeight)
        var scrollH = parseFloat(tbody.scrollHeight)
        if (1 >= scrollH - scrollT && tbody.scrollTop != 0 && tbody.childNodes.length < allRow) {
            if(is_running==false) {
                is_running = true
                var jyzh = $("#zh").val();
                var jylx = $("#jylx").val();
                window.page = page += 1

                var type = ""
                if (!zzbds.test(jylx)) {
                    type = "dfzh"
                } else {
                    type = "jylx"
                }
                var url = "/SINOFAITH/cftzzxx/getDetails"
                $.ajax({
                    type: "post",
                    dataType: "json",
                    url: url,
                    data: {
                        jyzh: jyzh,
                        jylx: jylx,
                        sum:allRow,
                        order:'xx',
                        type: type,
                        page: parseInt(window.page)
                    },
                    success: function (msg) {
                        var data = msg.list
                        var str = ""
                        for (i in data) {
                            if (i % 2 == 0) {
                                str += "<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                            } else {
                                str += "<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                            }
                            str+="<td width=\"4%\">"+data[i].id+"</td>"+
                                "<td width=\"5%\">"+data[i].name+"</td>"+
                                "<td width=\"15%\" title='"+(data[i].zh!=null?data[i].zh:"")+"'>"+
                                "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].zh!=null?data[i].zh:"")+"</div></td>"+
                                "<td width=\"6%\">"+data[i].jdlx+"</td>"+
                                "<td width=\"10%\">"+data[i].jylx+"</td>"+
                                "<td width=\"14%\" title='"+(data[i].shmc!=null?data[i].shmc:"")+"'>"+
                                "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].shmc!=null?data[i].shmc:"")+"</div></td>"+
                                "<td width=\"8%\">"+data[i].jyje+"</td>"+
                                "<td width=\"13%\">"+data[i].jysj+"</td>"+
                                "<td width=\"15%\" title='"+(data[i].fsf!=null?data[i].fsf:"")+"'>"+
                                "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].fsf!=null?data[i].fsf:"")+"</div></td>"+
                                "<td width=\"8%\">"+data[i].fsje+"</td>"+
                                "<td width=\"15%\" title='"+(data[i].jsf!=null?data[i].jsf:"")+"'>"+
                                "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].jsf!=null?data[i].jsf:"")+"</div></td>"+
                                "<td width=\"8%\">"+data[i].jsje+"</td>"+
                                "</tr>";
                        }
                        $("#result").append(str)
                        $("#zh").attr("value", jyzh);
                        $("#jylx").attr("value", jylx);
                        $("#allRow").attr("value", msg.totalRecords)
                    // title.innerText ="<"+jyzh+","+jylx+">"
                    is_running = false
                }
            })
        }
    }
}

function getZzGtlxrByorder(filter) {
    var tbody = window.document.getElementById("result1")
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    var dfzh = $("#dfzh").val();
    window.page = 1
    var url = "/SINOFAITH/cftgtzh/getDetails"
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            dfzh:dfzh,
            order:filter,
            page:page
        },
        success:function (msg) {
            var data = msg.list
            var str = ""
            for (i in data) {
                if (i % 2 == 0) {
                    str += "<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                } else {
                    str += "<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                }
                str += "<td width=\"7%\">" + data[i].id + "</td>" +
                    "<td width=\"7%\">" + data[i].name + "</td>" +
                    "<td width=\"9%\">" + data[i].jyzh + "</td>" +
                    "<td width=\"9%\">" + data[i].dfzh + "</td>" +
                    "<td width=\"8%\">" + data[i].jyzcs + "</td>" +
                    "<td width=\"8%\">" + data[i].jzzcs + "</td>" +
                    "<td width=\"10%\">" + data[i].jzzje + "</td>" +
                    "<td width=\"8%\">" + data[i].czzcs + "</td>" +
                    "<td width=\"10%\">" + data[i].czzje + "</td>" +
                    "<td width=\"5%\"><button  data-toggle=\"modal\" data-target=\"#myModal\" onclick=\"getZzDetails(this)\">详情</button></td>"+
                    "</tr>";
            }
            tbody.innerHTML = str
            $("#dfzh").attr("value", dfzh);
            $("#allRow1").attr("value", msg.totalRecords)
        }
    })
}

function getZzGtlxr(obj) {
    var dfzh = $(obj).closest("tr").find("td:eq(3)").text()
    window.page = 1
    var tbody = window.document.getElementById("result1")
    var url = "/SINOFAITH/cftgtzh/getDetails"
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            dfzh:dfzh,
            order:"jyzcs",
            page:page
        },
        success:function (msg) {
            var data = msg.list
            var str = ""
            for (i in data) {
                if (i % 2 == 0) {
                    str += "<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                } else {
                    str += "<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                }
                str += "<td width=\"7%\">" + data[i].id + "</td>" +
                    "<td width=\"7%\">" + data[i].name + "</td>" +
                    "<td width=\"9%\">" + data[i].jyzh + "</td>" +
                    "<td width=\"9%\">" + data[i].dfzh + "</td>" +
                    "<td width=\"8%\">" + data[i].jyzcs + "</td>" +
                    "<td width=\"8%\">" + data[i].jzzcs + "</td>" +
                    "<td width=\"10%\">" + data[i].jzzje + "</td>" +
                    "<td width=\"8%\">" + data[i].czzcs + "</td>" +
                    "<td width=\"10%\">" + data[i].czzje + "</td>" +
                    "<td width=\"5%\"><button  data-toggle=\"modal\" data-target=\"#myModal\" onclick=\"getZzDetails(this)\">详情</button></td>"+
                    "</tr>";
            }
            tbody.innerHTML = str
            $("#dfzh").attr("value", dfzh);
            $("#allRow1").attr("value", msg.totalRecords)
        }
    })
}

function downGtlxr(){
    var dfzh = $("#dfzh").val();
    location="/SINOFAITH/cftgtzh/downgtlxr?dfzh="+dfzh
}

function orderByFilter(filter) {
    var tbody = window.document.getElementById("result")
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    var jyzh = $("#zh").val();
    var jylx = $("#jylx").val();
    var allRow = $("#allRow").val()
    window.page = 1
    var type = ""
    if(!zzbds.test(jylx)){
        type="dfzh"
    }else{
        type="jylx"
    }
    var url = "/SINOFAITH/cftzzxx/getDetails"
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            jyzh:jyzh,
            jylx:jylx,
            sum:allRow,
            order:filter,
            type:type,
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list
            var str = ""
            for (i in data){
                if(i%2==0){
                    str+="<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                }else{
                    str+="<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                }
                str+="<td width=\"4%\">"+data[i].id+"</td>"+
                    "<td width=\"5%\">"+data[i].name+"</td>"+
                    "<td width=\"15%\" title='"+(data[i].zh!=null?data[i].zh:"")+"'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].zh!=null?data[i].zh:"")+"</div></td>"+
                    "<td width=\"6%\">"+data[i].jdlx+"</td>"+
                    "<td width=\"10%\">"+data[i].jylx+"</td>"+
                    "<td width=\"14%\" title='"+(data[i].shmc!=null?data[i].shmc:"")+"'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].shmc!=null?data[i].shmc:"")+"</div></td>"+
                    "<td width=\"8%\">"+data[i].jyje+"</td>"+
                    "<td width=\"13%\">"+data[i].jysj+"</td>"+
                    "<td width=\"15%\" title='"+(data[i].fsf!=null?data[i].fsf:"")+"'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].fsf!=null?data[i].fsf:"")+"</div></td>"+
                    "<td width=\"8%\">"+data[i].fsje+"</td>"+
                    "<td width=\"15%\" title='"+(data[i].jsf!=null?data[i].jsf:"")+"'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].jsf!=null?data[i].jsf:"")+"</div></td>"+
                    "<td width=\"8%\">"+data[i].jsje+"</td>"+
                    "</tr>";
            }
            tbody.innerHTML = str
            $("#zh").attr("value",jyzh);
            $("#jylx").attr("value",jylx);
            $("#allRow").attr("value",msg.totalRecords)
            // title.innerText ="<"+jyzh+","+jylx+">"
        }
    })
}

function getZzDetails(obj) {
    var jyzh = $(obj).closest("tr").find("td:eq(2)").text()
    var jylx = $(obj).closest("tr").find("td:eq(3)").text()
    var sum = $(obj).closest("tr").find("td:eq(4)").text()
    window.page = 1

    var type = ""
    if(!zzbds.test(jylx)){
        type="dfzh"
    }else{
        type="jylx"
    }
    var tbody = window.document.getElementById("result")
    var url = "/SINOFAITH/cftzzxx/getDetails"
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            jyzh:jyzh,
            jylx:jylx,
            sum:sum,
            order:'jysj',
            type:type,
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list
            var str = ""
            for (i in data){
                if(data[i].fsje===0){
                    data[i].fsje="";
                }

                if(data[i].jsje===0){
                    data[i].jsje="";
                }
                if(i%2==0){
                    str+="<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                }else{
                    str+="<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                }
                str+="<td width=\"4%\">"+data[i].id+"</td>"+
                    "<td width=\"5%\">"+data[i].name+"</td>"+
                    "<td width=\"15%\" title='"+(data[i].zh!=null?data[i].zh:"")+"'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].zh!=null?data[i].zh:"")+"</div></td>"+
                    "<td width=\"6%\">"+data[i].jdlx+"</td>"+
                    "<td width=\"10%\">"+data[i].jylx+"</td>"+
                    "<td width=\"14%\" title='"+(data[i].shmc!=null?data[i].shmc:"")+"'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].shmc!=null?data[i].shmc:"")+"</div></td>"+
                    "<td width=\"8%\">"+data[i].jyje+"</td>"+
                    "<td width=\"13%\">"+data[i].jysj+"</td>"+
                    "<td width=\"15%\" title='"+(data[i].fsf!=null?data[i].fsf:"")+"'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].fsf!=null?data[i].fsf:"")+"</div></td>"+
                    "<td width=\"8%\">"+data[i].fsje+"</td>"+
                    "<td width=\"15%\" title='"+(data[i].jsf!=null?data[i].jsf:"")+"'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].jsf!=null?data[i].jsf:"")+"</div></td>"+
                    "<td width=\"8%\">"+data[i].jsje+"</td>"+
                    "</tr>";
            }
            tbody.innerHTML = str
            $("#zh").attr("value",jyzh);
            $("#jylx").attr("value",jylx);
            $("#allRow").attr("value",msg.totalRecords)
            // title.innerText ="<"+jyzh+","+jylx+">"
        }
    })
}
$(function () { $('#myModal').on('hide.bs.modal', function () {
    var tbody = window.document.getElementById("result")
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    $.ajax({url:"/SINOFAITH/bankzzxx/removeDesc"})
    })
});

$(function () { $('#myModal1').on('hide.bs.modal', function () {
    var tbody = window.document.getElementById("result1")
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    $.ajax({url:"/SINOFAITH/bankgtzh/removeDesc"})
})
});

function downDetailJylx(){
  var zh = $("#zh").val();
  var jylx =$("#jylx").val();
  var type = ""

    if(!zzbds.test(jylx)){
        type="dfzh"
    }else{
        type="jylx"
    }
  location="/SINOFAITH/cftzzxx/downDetailJylx?zh="+zh+"&jylx="+jylx+"&type="+type
}

function ajCount(aj) {
    var flg = 0

    if($("#checkbox1").is(":checked")){
        flg=1
    }
    var url = "/SINOFAITH/aj/ajCount?ajm="+aj+"&flg="+flg
    alertify.alert("数据分析中,请等待跳转...");
    $.get(url,function (data) {
        if(data==200){
            alertify.alert("分析完成..正在跳转..");
            setTimeout(function (){document.getElementById("seachDetail").submit()},1500);
        }
        if(data == 303){
            alertify.alert("数据分析中..请等待跳转")
            setTimeout(function () {location="/SINOFAITH/cfttjjg/seach?pageNo=1"},10000);
        }
    })

}
