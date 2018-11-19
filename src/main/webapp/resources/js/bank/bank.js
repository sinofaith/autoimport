
$(function () {
    $('#myModal').on('hide.bs.modal', function () {

        var tbody = window.document.getElementById("result")
        if(tbody!=null) {
            tbody.innerHTML = ""
        }
        $.ajax({url:"/SINOFAITH/bankzzxx/removeDesc"})
    })
});


function bankSkip(code) {
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
        location="/SINOFAITH/bank"+code+"/seach?pageNo="+onPage;
    }
}


function seachChange() {
    var seachCondition = $("#seachCondition").val()
    var seachCode = $("#seachCode")
    if(seachCondition === "jzzje" || seachCondition === "czzje"){
        seachCode.val("50000")
    }else{
        seachCode.val("")
    }
}

function UploadBank() {
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
    var FileController = "/SINOFAITH/uploadBank"; // 接收上传文件的后台地址
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
            alertify.set('notifier','position', 'top-center');
            alertify.success("导入完成!");
            $('#myModal').modal('hide');
            setTimeout(function () {document.getElementById("seachDetail").submit()},1500);
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

function progressFunction(evt) {

    var progressBar = document.getElementById("progressBar");

    var percentageDiv = document.getElementById("percentage");

    if (evt.lengthComputable) {

        progressBar.max = evt.total;

        progressBar.value = evt.loaded;

        percentageDiv.innerHTML = Math.round(evt.loaded / evt.total * 100)+ "%";

        if((evt.loaded/evt.total) ==1){
            alertify.set('notifier','position', 'top-center');
            alertify.set('notifier','delay', 0);
            alertify.success("文件夹上传成功\n请等待数据导入...");
        }
    }
}

function getZzGtlxr(obj) {
    var dfkh = $(obj).closest("tr").find("td:eq(3)").text()
    window.page = 1
    var tbody = window.document.getElementById("result")
    var url = "/SINOFAITH/bankgtzh/getDetails"
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            dfzh:dfkh,
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
                str += "<td width=\"5%\">" + data[i].id + "</td>" +
                    "<td width=\"5%\">" + data[i].name + "</td>" +
                    "<td width=\"12%\">" + data[i].jyzh + "</td>" +
                    "<td width=\"12%\">" + data[i].dfzh + "</td>" +
                    "<td width=\"5%\" title=\""+data[i].dfxm+"\"> <div style=\"width:70px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].dfxm+"</div></td>"+
                    "<td width=\"8%\">" + data[i].count + "</td>" +
                    "<td width=\"7%\">" + data[i].jyzcs + "</td>" +
                    "<td width=\"7%\">" + data[i].jzzcs + "</td>" +
                    "<td width=\"10%\">" + data[i].jzzje + "</td>" +
                    "<td width=\"7%\">" + data[i].czzcs + "</td>" +
                    "<td width=\"10%\">" + data[i].czzje + "</td>" +
                    "</tr>";
            }
            tbody.innerHTML = str
            $("#dfkh").attr("value", dfkh);
            $("#allRow").attr("value", msg.totalRecords)
        }
    })
}


function getZzDetails(obj,type) {
    var yhkkh = $(obj).closest("tr").find("td:eq(1)").text()
    var dfkh = $(obj).closest("tr").find("td:eq(3)").text()
    window.page = 1

    if(type=="tjjg"){
        dfkh=""
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
                    "<td width=\"13%\">"+data[i].yhkkh+"</td>"+
                    "<td width=\"5%\">"+data[i].jyxm+"</td>"+
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
                            "<td width=\"13%\">"+data[i].yhkkh+"</td>"+
                            "<td width=\"5%\">"+data[i].jyxm+"</td>"+
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
