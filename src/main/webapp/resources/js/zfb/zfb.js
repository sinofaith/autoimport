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
function UploadZfb() {
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
    var FileController = "/SINOFAITH/uploadZfb"; // 接收上传文件的后台地址
    // FormData 对象
    var form = new FormData();
    form.append("aj", aj); // 可以增加表单数据
    for(var i=0;i<fileObj.files.length;i++){
        var fileName = fileObj.files[i].name;
        var index1=fileName.lastIndexOf(".");
        var index2=fileName.length;
        var suffix=fileName.substring(index1,index2);
        if(suffix==".txt"||suffix==".doc"||suffix==".docx"||suffix==".csv") {
            form.append("file", fileObj.files[i]); // 文件对象
        }
    }
    var xhr = new XMLHttpRequest();                // XMLHttpRequest 对象
    xhr.open("post", FileController, true);
    xhr.onload = function() {
        if(this.status == 200||this.status == 304){
            alertify.set('notifier','position', 'top-center');
            alertify.success("导入完成!");
            $('#myModal').modal('hide');
            setTimeout(function () {document.getElementById("seachDetail").submit()},1500);
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
            alertify.success("文件夹上传成功\n等待数据导入");
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
            insert(data,tbody,true);
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
            insert(data,tbody,true);
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
                    insert(data,tbody,false);
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
function insert(data,tbody,temp){
    var str = "";
    for (i in data){
        if(i%2==0){
            str+="<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
        }else{
            str+="<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
        }
        str+="<td width=\"3%\">"+data[i].id+"</td>"+
            "<td width=\"14%\">"+data[i].jyh+"</td>"+
            "<td width=\"8%\">"+data[i].fkfzfbzh+"</td>"+
            "<td width=\"8%\">"+data[i].zzcpmc+"</td>"+
            "<td width=\"8%\">"+data[i].skfzfbzh+"</td>"+
            "<td width=\"5%\">"+data[i].skjgxx+ "</td>"+
            "<td width=\"7%\">"+data[i].dzsj+"</td>"+
            "<td width=\"4%\">"+data[i].zzje+"</td>"+
            "<td width=\"12%\">"+data[i].txlsh+"</td>"+
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

// 交易记录详情
function getZfbJyjlDetails(obj){
    // 买家用户Id
    var mjyhid = $(obj).closest("tr").find("td:eq(1)").text();
    var mjxx = $(obj).closest("tr").find("td:eq(2)").text();
    // 卖家用户Id
    var mijyhid = $(obj).closest("tr").find("td:eq(4)").text();
    var mijxx = $(obj).closest("tr").find("td:eq(5)").text();
    // 商品名称
    var direction = $(obj).closest("tr").find("td:eq(3)").text();
    var spmc = $(obj).closest("tr").find("td:eq(6)").text();
    window.page = 1;
    var tbody = window.document.getElementById("result");
    var url = "/SINOFAITH/zfbJyjl/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            mjyhid:mjyhid,
            mjxx:mjxx,
            mijyhid:mijyhid,
            mijxx:mijxx,
            direction:direction,
            spmc:spmc,
            order:'jyje',
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list;
            jyjlInsert(data,tbody,true);
            $("#mjyhid").attr("value",mjyhid);
            $("#mjxx").attr("value",mjxx);
            $("#mijyhid").attr("value",mijyhid);
            $("#mijxx").attr("value",mijxx);
            $("#spmc").attr("value",spmc);
            $("#direction").attr("value",direction);
            $("#allRow").attr("value",msg.totalRecords);
        }
    })
}

// 交易记录排序
function orderByJyjlFilter(filter){
    var tbody = window.document.getElementById("result");
    // 买家用户Id
    var mjyhid = $("#mjyhid").val();
    var mjxx = $("#mjxx").val();
    // 卖家用户Id
    var mijyhid = $("#mijyhid").val();
    var mijxx = $("#mijxx").val();
    // 商品名称
    var direction = $("#direction").val();
    var spmc = $("#spmc").val();
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    window.page = 1;
    var url = "/SINOFAITH/zfbJyjl/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            mjyhid:mjyhid,
            mjxx:mjxx,
            mijyhid:mijyhid,
            mijxx:mijxx,
            direction:direction,
            spmc:spmc,
            order:filter,
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list;
            jyjlInsert(data,tbody,true);
            $("#mjyhid").attr("value",mjyhid);
            $("#mjxx").attr("value",mjxx);
            $("#mijyhid").attr("value",mijyhid);
            $("#mijxx").attr("value",mijxx);
            $("#spmc").attr("value",spmc);
            $("#direction").attr("value",direction);
            $("#allRow").attr("value",msg.totalRecords);
        }
    })
}

// 转账明细滚动条加载数据
var jyjlIs_running = false;
function scrollFJyjl() {
    var tbody = window.document.getElementById("result");
    // 买家用户Id
    var mjyhid = $("#mjyhid").val();
    var mjxx = $("#mjxx").val();
    // 卖家用户Id
    var mijyhid = $("#mijyhid").val();
    var mijxx = $("#mijxx").val();
    // 商品名称
    var direction = $("#direction").val();
    var spmc = $("#spmc").val();
    var allRow = $("#allRow").val();
    var scrollT = parseFloat(tbody.scrollTop) + parseFloat(tbody.clientHeight)
    var scrollH = parseFloat(tbody.scrollHeight)
    if (1 >= scrollH - scrollT && tbody.scrollTop != 0 && tbody.childNodes.length < allRow) {
        if (jyjlIs_running == false) {
            jyjlIs_running = true;
            window.page = page += 1;
            var url = "/SINOFAITH/zfbJyjl/getDetails";
            $.ajax({
                type:"post",
                dataType:"json",
                url:url,
                data:{
                    mjyhid:mjyhid,
                    mjxx:mjxx,
                    mijyhid:mijyhid,
                    mijxx:mijxx,
                    direction:direction,
                    spmc:spmc,
                    order:"xxx",
                    page:parseInt(window.page)
                },
                success:function (msg) {
                    var data = msg.list;
                    jyjlInsert(data,tbody,false);
                    $("#mjyhid").attr("value",mjyhid);
                    $("#mjxx").attr("value",mjxx);
                    $("#mijyhid").attr("value",mijyhid);
                    $("#mijxx").attr("value",mijxx);
                    $("#spmc").attr("value",spmc);
                    $("#direction").attr("value",direction);
                    $("#allRow").attr("value",msg.totalRecords);
                    jyjlIs_running = false;
                }
            })
        }
    }
}

// 交易记录插入表记录
function jyjlInsert(data,tbody,temp){
    var str = "";
    for (i in data){
        if(i%2==0){
            str+="<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
        }else{
            str+="<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
        }
        str+="<td width=\"3%\">"+data[i].id+"</td>"+
            "<td width=\"12%\">"+data[i].jyh+"</td>"+
            "<td width=\"7%\">"+data[i].mjyhId+"</td>"+
            "<td width=\"9%\" title='"+data[i].mjxx+"'>"+"<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].mjxx+"</div></td>"+
            "<td width=\"7%\">"+data[i].mijyhId+ "</td>"+
            "<td width=\"7%\" title='"+data[i].mijxx+"'>"+"<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].mijxx+"</div></td>"+
            "<td width=\"4%\">"+data[i].jyje+"</td>"+
            "<td width=\"8%\">"+data[i].sksj+"</td>"+
            "<td width=\"6%\">"+data[i].jylx+"</td>"+
            "<td width=\"6%\" title='"+data[i].spmc+"'>"+"<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].spmc+"</div></td>"+
            "<td width=\"8%\" title='"+data[i].shrdz+"'>"+"<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].shrdz+"</div></td>"+
            "</tr>";
    }
    if(temp){
        tbody.innerHTML = str;
    }else{
        tbody.innerHTML += str;
    }
}

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