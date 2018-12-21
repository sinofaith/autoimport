// 传销数据导入
function UploadPyramidSale(){
    var fileObj = document.getElementById("file");// js 获取文件对象
    var file = $("#file").val();
    if(file==''){
        alertify.set('notifier','position', 'top-center');
        alertify.error('请选择要上传的文件夹')
        return;
    }
    var aj = $("#aj").val();
    if(aj==''){
        alertify.set('notifier','position', 'top-center');
        alertify.error('请填写案件名称')
        return;
    }
    var FileController = "/SINOFAITH/uploadPyramidSale"; // 接收上传文件的后台地址
    // FormData 对象
    var form = new FormData();
    form.append("aj", aj); // 可以增加表单数据
    for(i=0;i<fileObj.files.length;i++){
        form.append("file", fileObj.files[i]); // 文件对象
    }
    var xhr = new XMLHttpRequest();                // XMLHttpRequest 对象
    xhr.open("post", FileController, true);
    xhr.onload = function(e) {
        if(this.status == 200||this.status == 304){
            $("#c").empty();
            var resp = xhr.responseText;
            var keyList = JSON.parse(resp);
            var jsonData = JSON.stringify(resp);
            var data = $.parseJSON(jsonData);
            var i=1;
            $("#c").append("<label for=\"c12\">Sheet名</label>");
            $("#c").append("<select\tclass=\"form-control\" id=\"c12\" name=\"custSource\" onchange='insertTable("+data+")'>");
            for(var key in keyList){
                if(i==1){
                    $("#c12").append("<option value='"+key+"' selected>"+key+"</option>");
                    $("#c12").load(insertTable(keyList));
                    i++;
                }else{
                    $("#c12").append("<option value='"+key+"'>"+key+"</option>");
                }
            }
            $("#c").append("</select>");

            $('#myModal').modal('hide');
            $('#myModal1').modal('show');

        }else{
            alertify.set('notifier','delay', 0);
            alertify.error("错误!请联系管理员");
            return;
        }
    };
    xhr.upload.addEventListener("progress", progressFunction, false);
    xhr.send(form);
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

function insertTable(data){
    $("#head").empty();
    for(i=1;i<12;i++){
        $("#c"+i).empty();
    }
    var key = $('#c12').val();
    var content = "<thead style=\"display:table;width: 350%;table-layout:fixed;\">";
    content += "<tr align=\"center\">";
    var size = Math.ceil(data[key].length/2);
    for(j=1;j<12;j++){
        $("#c"+j).append("<option value=\"无\" selected>无</option>")
    }
    for(i=0;i<size;i++){
        content += "<td width='10%' title='"+data[key][i]+"'><div style='width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;'>"+data[key][i]+"</div></td>";
        for(j=1;j<12;j++){
            if((i==0&&j==1)||(data[key][i].indexOf("推荐人")!=-1&&j==2)||(data[key][i].indexOf("mobile")!=-1&&j==3)||
                (data[key][i].indexOf("telphone")!=-1&&j==4)||(data[key][i].indexOf("用户名")!=-1&&j==5)||
                (data[key][i].indexOf("性别")!=-1&&j==6)||(data[key][i].indexOf("详细地址")!=-1&&j==7)||
                (data[key][i].indexOf("身份证")!=-1&&j==8)||(data[key][i].indexOf("开户行")!=-1&&j==9)||
                (data[key][i].indexOf("持卡人")!=-1&&j==10)||(data[key][i].indexOf("银行卡")!=-1&&j==11)){
                $("#c"+j).append("<option value='"+data[key][i]+"' selected>"+data[key][i]+"</option>");
            }else{
                $("#c"+j).append("<option value='"+data[key][i]+"'>"+data[key][i]+"</option>");
            }
        }
    }
    for(j=1;j<12;j++){
        $("#c"+j).selectOrDie();
        $("#c"+j).selectOrDie("update");
    }
    content += "</tr></thead>";
    var tbody = "<tbody style=\"display:table;width: 350%;table-layout:fixed;\"><tr align='center'>";
    for(var i=size;i<data[key].length;i++){
        tbody += "<td width=\"10%\" title='"+data[key][i]+"'><div style='width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;'>"+data[key][i]+"</div></td>";
    }
    tbody += "</tr></tbody>";
    $("#head").append(content);
    $("#head").append(tbody);
}

function uploadPsSheet(){
    var key = $('#c12').val();
    var field = [];
    field.push(key);
    for(i=1;i<12;i++){
        field.push($('#c'+i).val());
    }
    var list = JSON.stringify(field);
    $.ajax({
        type:"post",
        url:"/SINOFAITH/uploadPsSheet",
        contentType : 'application/json;charset=utf-8',
        data:list,
        success:function (data) {
           if(data=="200"){
               alertify.set('notifier','position', 'top-center');
               alertify.success("导入完成!");
               setTimeout(function () {document.getElementById("seachDetail").submit()},1500);
           }else if(data=="403"){
               alertify.set('notifier','position', 'top-center');
               alertify.set('notifier','delay', 0);
               alertify.error("错误!该EXCEL文件存在多个根会员");
               return;
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

// 跳转页面
function pyramidSaleSkip(code){
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
        location="/SINOFAITH/"+code+"/seach?pageNo="+onPage;
    }
}

function getPyramSaleDetails(obj,temp){
    var psId = $(obj).closest("tr").find("td:eq(1)").text();
    window.page = 1
    var tbody;
    if(temp){
        tbody = window.document.getElementById("result");
    }else{
        tbody = window.document.getElementById("result1");
    }
    var url = "/SINOFAITH/pyramidSaleTier/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            psId:psId,
            order:'sponsorId',
            temp:temp,
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list;
            var str = "";
            for (i in data){
                if(i%2==0){
                    str+="<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                }else{
                    str+="<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                }
                str+="<td width=\"5%\">"+data[i].id+"</td>"+
                    "<td width=\"8%\">"+data[i].psId+"</td>"+
                    "<td width=\"8%\">"+data[i].sponsorId+"</td>"+
                    "<td width=\"8%\">"+data[i].nick_name+"</td>"+
                    "<td width=\"8%\">"+data[i].mobile+ "</td>"+
                    "<td width=\"5%\">"+data[i].sex+"</td>"+
                    "<td width=\"15%\" title='"+data[i].address+"'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].address+"</div></td>"+
                    "<td width=\"13%\">"+data[i].sfzhm+"</td>"+
                    "<td width=\"12%\">"+data[i].accountHolder+"</td>"+
                    "<td width=\"13%\" title='"+data[i].bankName+"'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].bankName+"</div></td>"+
                    "<td width=\"13%\" title='"+data[i].accountNumber+"'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].accountNumber+"</div></td>"+
                    "</tr>";
            }
            tbody.innerHTML = str;
            if(temp){
                $("#psid").attr("value",psId);
                $("#allRow").attr("value",msg.totalRecords);
            }else{
                $("#psid1").attr("value",psId);
                $("#allRow1").attr("value",msg.totalRecords);
            }
        }
    })
}

// 每次点击将数据清空
var page = 1
var pageNo = 1
$(function () {
    $('#myModal').on('hide.bs.modal', function () {
        var tbody = window.document.getElementById("result");
        page = 1;
        if(tbody!=null) {
            tbody.innerHTML = ""
        }
        $.ajax({url:"/SINOFAITH/pyramidSaleTier/removeDesc"})
    });
    $('#myModal1').on('hide.bs.modal', function () {
        pageNo = 1;
        var tbody = window.document.getElementById("result1");
        if(tbody!=null) {
            tbody.innerHTML = ""
        }
        //$.ajax({url:"/SINOFAITH/pyramidSaleTier/removeDesc"})
    })
});

function orderByFilter(filter,temp){
    var tbody;
    var psId;
    if(temp){
        tbody = window.document.getElementById("result");
        psId = $("#psid").val();
    }else{
        tbody = window.document.getElementById("result1");
        psId = $("#psid1").val();
    }
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    window.page = 1
    var url = "/SINOFAITH/pyramidSaleTier/getDetails"
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            psId:psId,
            order:filter,
            temp:temp,
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
                str+="<td width=\"5%\">"+data[i].id+"</td>"+
                    "<td width=\"8%\">"+data[i].psId+"</td>"+
                    "<td width=\"8%\">"+data[i].sponsorId+"</td>"+
                    "<td width=\"8%\">"+data[i].nick_name+"</td>"+
                    "<td width=\"8%\">"+data[i].mobile+ "</td>"+
                    "<td width=\"5%\">"+data[i].sex+"</td>"+
                    "<td width=\"15%\" title='"+data[i].address+"'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].address+"</div></td>"+
                    "<td width=\"13%\">"+data[i].sfzhm+"</td>"+
                    "<td width=\"12%\">"+data[i].accountHolder+"</td>"+
                    "<td width=\"13%\" title='"+data[i].bankName+"'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].bankName+"</div></td>"+
                    "<td width=\"13%\" title='"+data[i].accountNumber+"'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].accountNumber+"</div></td>"+
                    "</tr>";
            }
            tbody.innerHTML = str
            if(temp){
                $("#psid").attr("value",psId);
                $("#allRow").attr("value",msg.totalRecords)
            }else{
                $("#psid1").attr("value",psId);
                $("#allRow1").attr("value",msg.totalRecords)
            }
        }
    })
}

// 滚动条加载数据
var is_running = false
function scrollF(temp) {
    var tbody;
    var allRow;
    var psId;
    if(temp){
        tbody = window.document.getElementById("result");
        allRow = $("#allRow").val();
        psId = $("#psid").val();
    }else{
        tbody = window.document.getElementById("result1");
        allRow = $("#allRow1").val();
        psId = $("#psid1").val();
    }
    var scrollT = parseFloat(tbody.scrollTop) + parseFloat(tbody.clientHeight)
    var scrollH = parseFloat(tbody.scrollHeight)
    if (1 >= scrollH - scrollT && tbody.scrollTop != 0 && tbody.childNodes.length < allRow) {
        if (is_running == false) {
            is_running = true;
            if(temp){
                window.page = page += 1;
            }else{
                window.page = pageNo += 1;
            }
            var url = "/SINOFAITH/pyramidSaleTier/getDetails"
            $.ajax({
                type:"post",
                dataType:"json",
                url:url,
                data:{
                    psId:psId,
                    order:"xxx",
                    temp:temp,
                    page:parseInt(window.page)
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
                        str+="<td width=\"5%\">"+data[i].id+"</td>"+
                            "<td width=\"8%\">"+data[i].psId+"</td>"+
                            "<td width=\"8%\">"+data[i].sponsorId+"</td>"+
                            "<td width=\"8%\">"+data[i].nick_name+"</td>"+
                            "<td width=\"8%\">"+data[i].mobile+ "</td>"+
                            "<td width=\"5%\">"+data[i].sex+"</td>"+
                            "<td width=\"15%\" title='"+data[i].address+"'>"+
                            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].address+"</div></td>"+
                            "<td width=\"13%\">"+data[i].sfzhm+"</td>"+
                            "<td width=\"12%\">"+data[i].accountHolder+"</td>"+
                            "<td width=\"13%\" title='"+data[i].bankName+"'>"+
                            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].bankName+"</div></td>"+
                            "<td width=\"13%\" title='"+data[i].accountNumber+"'>"+
                            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].accountNumber+"</div></td>"+
                            "</tr>";
                    }
                    if(temp){
                        $("#result").append(str)
                        $("#psid").attr("value",psId);
                        $("#allRow").attr("value",msg.totalRecords);
                    }else{
                        $("#result1").append(str)
                        $("#psid1").attr("value",psId);
                        $("#allRow1").attr("value",msg.totalRecords);
                    }
                    is_running = false;
                }
            })
        }
    }
}

// 阀值设置
function seachChange() {
    var seachCondition = $("#seachCondition").val();
    if(seachCondition === "directDrive"){
        $("#seachCode").val("50")
    }else if(seachCondition === "directReferNum"){
        $("#seachCode").val("5000")
    }else{
        $("#seachCode").val("")
    }
}

function isNum(obj){
    var seachCondition = $("#seachCondition").val();
    if(seachCondition === "directDrive" || seachCondition === "directReferNum"){
        obj.value=obj.value.replace(/[^\d]/g,'')
    }
}