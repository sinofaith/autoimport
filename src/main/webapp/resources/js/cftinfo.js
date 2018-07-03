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
    $('#btnLoadFile').click(function() {
        $("input[type='file']").click();
    });

    $("#uploadFileForm>input[type='file']").change(function(event) {
        var file = $("#file").val();
        if(file==''){
            alertify.alert('请选择要上传的文件夹')
            return;
        }

        var files = event.target.files;
        var actual_filesCount=files.length;

        if(actual_filesCount>1000){
            alertify.alert("文件过多,单次可上传1000个文件");
            return;
        }
        var actual_Size = 0
        for(var i=0,f;f=files[i];++i){
            actual_Size+=f.size;
            if(actual_Size>1024*1024*1024){
                alertify.alert("单次文件夹不能超过1024M")
                return;
            }
        }

        $('#uploadFileForm').ajaxSubmit({
            dataType: 'text',
            success: function(result) {
                if (result == "") {
                    alertify.alert('上传文件出现问题！请检查网络环境是否正常或文件格式及内容是否符合标准！');
                } else{
                    alertify.alert("导入成功.");
                    setTimeout(function () {document.getElementById("seachDetail").submit()},1000);
                }
            }
        });

        $(this).val('');
    });

    $('#accessory').click(function() {
        $("input[type='file']").click();
    });

    $("#accessoryForm>input[type='file']").change(function(event) {
        //document.getElementById("accessoryForm").action = "/AMD/crimegroupinfo/file/accessory";
        //document.getElementById("accessoryForm").submit();
        $('#accessoryForm').ajaxSubmit({
            dataType: 'json',
            success: function(result) {
                if (result == null) {
                    alertify.alert('上传文件出现问题！可能服务器存在问题,请联系管理员！');
                } else{
                    $('.accessory').append("<li>"+result.fileName+"."+result.suffix+" <img width='16px' class='delete' title='删除' src='/AMD/resources/thirdparty/assets/images/images/delete.png'></li>");
                    $(".accessory li .delete").bind({'click':function(){
                        var that = this;
                        var file = $(this).parent().text().trim();
                        var fileName = file.substring(0,file.lastIndexOf("."));
                        var suffix = file.substring(file.lastIndexOf(".")+1,file.length );
                        var crimegroupinfoid = $("#crimegroupinfoid").val();
                        var path = "/AMD/crimegroupinfo/accessorydelete/"+crimegroupinfoid+"/"+fileName+"/"+suffix
                        $.ajax({
                            url: path,
                            type: 'POST',
                            dataType: 'text',
                            success: function() {
                                $('.accessory').find($(that).parent()).remove();
                            }

                        });
                    }});
                    alert("上传成功！");
                }
            }
        });
    });
    $(".accessory li .delete").click(function(){
        var that = this;
        var file = $(this).parent().text().trim();
        var fileName = file.substring(0,file.lastIndexOf("."));
        var suffix = file.substring(file.lastIndexOf(".")+1,file.length );
        var crimegroupinfoid = $("#crimegroupinfoid").val();
        var path = "/AMD/crimegroupinfo/accessorydelete/"+crimegroupinfoid+"/"+fileName+"/"+suffix
        $.ajax({
                url: path,
                type: 'POST',
                dataType: 'text',
                success: function() {
                    $('.accessory').find($(that).parent()).remove();
                }

        });
        });

    //导入按钮绑定事件
    $('#btnduibi').click(function() {
        $("#dbfile").click();
    });

    $("#dbfile").change(function(event) {
        var filepath = $("#dbfile").val();
        var extStart = filepath.lastIndexOf(".");
        var ext = filepath.substring(extStart,filepath.length).toUpperCase();
        if(ext != ".XLS" && ext !=".XLSX"){
            alert("文件限于EXCEL格式");
            $("#dbfile").val('');
            return false;
        }
        var maxsize = 5*1024*1024;
        var obj_file = document.getElementById("dbfile");
        var filesize = obj_file.files[0].size;
        if(filesize>maxsize){
            alert("上传文件太大,请处理一下！");
            $("#dbfile").val('');
            return false;
        }
        $('#duibiFileForm').ajaxSubmit({
            dataType: 'text',
        });

        $("#dbfile").val('');
        alert("上传成功，请等待处理！");
    });


});

function AddCrimeterrace() {
    $.ajax({
        url: "/AMD/crimegroupinfo/editcheck",
        type: 'POST',
        dataType: 'text',
        success: function(result) {
            if(result==="1"){
                if(confirm("你还没有登录，需要登录？")){
                    location="/AMD/homepage";
                }
            } else {
                location="/AMD/crimeterrace/linkAddCrimeterrace/0"
            }
        }
    });
}

function AddCrimegroupinfo() {
    $.ajax({
        url: "/AMD/crimegroupinfo/editcheck",
        type: 'POST',
        dataType: 'text',
        success: function(result) {
            if(result==="1"){
                if(confirm("你还没有登录，需要登录？")){
                    location="/AMD/homepage";
                }
            } else {
                location="/AMD/crimegroupinfo/linkAddCrimegroup"
            }
        }
    });
}

function CGdownload() {
    location="/AMD/crimegroupinfo/download"
}

function ZGdownload() {
    location="/AMD/crimegroupinfo/zgdownload"
}

function submitSeach(){
    var seachCode = $("#seachCode").val();

    if(seachCode ===""){
        alert("查询条件不能为空！");
    } else {
        if(seachCode ==="000" || seachCode ==="未实名"){
            alert("未实名的数据太多，请优化查询条件！");
        } else{
            document.getElementById("SeachCodeFrom").action = "/AMD/criteriaquery/SeachCode";
            document.getElementById("SeachCodeFrom").submit();
        }
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




function fileOnclick(){
    $("input[id='files1']").click();
}

function importfile(){
    var resultFile = document.getElementById("files1").files[0];
    if (resultFile) {
        var reader = new FileReader();
        reader.readAsText(resultFile, 'utf-8');
        reader.onload = function (e) {
            var urlData = this.result;
            document.getElementById("seachCode").value = urlData;
        }
    }
}

function zcxxSkip(){
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
        location="/SINOFAITH/cft/seach?pageNo="+onPage;
    }
}


function tjjgSkip(){
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
        location="/SINOFAITH/cfttjjg/seach?pageNo="+onPage;
    }
}

function gtzhSkip() {
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
        location="/SINOFAITH/cftgtzh/seach?pageNo="+onPage;
    }
}


function tjjgsSkip(){
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
        location="/SINOFAITH/cfttjjgs/seach?pageNo="+onPage;
    }
}

function zzxxSkip(){
    var totalPage = $("#totalPage").text();
    var onPage = $("#num").val();
    if(onPage ==="" || onPage === 0|| parseInt(onPage) <=0){
        alert("请输入你要跳转的页数！");
        return;
    }
    if(parseInt(onPage)>parseInt(totalPage)){
        $("#num").val(totalPage);
        return;
    } else {
        location="/SINOFAITH/cftzzxx/seach?pageNo="+onPage;
    }
}

function dbResult(){
    $.ajax({
        type:"get",
        url:"/AMD/fileuploading/file/dbfilename",
        success: function(result){
            var filecontent = "<tr><td width='33%'>上传人姓名</td><td width='33%'>上传文件</td><td width='34%'>对比结果</td></tr>";
            var strLine = result.split(",");
            for(var i=strLine.length-1;i>=0;i--){
                var oneline = strLine[i].split("_");
                var resultfile = "Result"+oneline[1].split(".")[0]+".csv";
                var resultfilename = oneline[0]+"_"+ resultfile;
                filecontent = filecontent + "<tr><td  width='33%'>"+oneline[0]+"</td><td  width='33%'>" +
                    "<a href='/AMD/criteriaquery/upload/download?filename="+strLine[i]+"'>"+oneline[1]+"</a></td>" +
                    "<td  width='34%'><a href='/AMD/criteriaquery/result/download?filename="+resultfilename+"'>"+resultfile+"</a></td></tr>"

            }
            $("#filenames").append(filecontent);
        }
    });
    $("#filename").show();
}

function closeFile(){
    $("#filename").hide();
    $("#filenames").empty();
}

function relationextension(){
    obj = document.getElementsByName("sfzhmval");
    check_val = [];
    for(k in obj){
        if(obj[k].checked)
            check_val.push(obj[k].value);
    }

    window.open("http://10.38.14.209:9000/relation2.html?person="+check_val);

}

function matrix() {
    obj = document.getElementsByName("sfzhmval");
    check_val = [];
    for (k in obj) {
        if (obj[k].checked)
            check_val.push(obj[k].value);
    }
    window.open("http://10.38.14.209.83:9000/matrix2.html#" + check_val);
}

function UploadCft() {
    var fileObj = document.getElementById("file");// js 获取文件对象
    var file = $("#file").val();
    if(file==''){
        alertify.alert('请选择要上传的文件夹')
        return;
    }
    var aj = $("#aj").val();
    var checkBox = 0
    if($("#checkbox1").is(':checked')){
        checkBox=1
    }
    if(aj==''){
        alertify.alert('请填写案件名称')
        return
    }
    var FileController = "/SINOFAITH/uploadCft"; // 接收上传文件的后台地址
    // FormData 对象
    var form = new FormData();
    form.append("aj", aj); // 可以增加表单数据
    form.append("checkBox",checkBox);
    for(i=0;i<fileObj.files.length;i++){
        form.append("file", fileObj.files[i]); // 文件对象
    }
    var xhr = new XMLHttpRequest();                // XMLHttpRequest 对象
    xhr.open("post", FileController, true);
    xhr.onload = function() {
        alertify.alert("导入完成!");
        $('#myModal').modal('hide');
        setTimeout(function () {document.getElementById("seachDetail").submit()},1500);
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

        if((evt.loaded/evt.total) ==1 ){
            alertify.alert("文件夹上传成功\n请等待数据导入...");
        }
    }

}

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
                if (/^[a-zA-Z]([-_a-zA-Z0-9])*$/.test(jylx)) {
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
                        type: type,
                        page: parseInt(window.page)
                    },
                    success: function (msg) {
                        var data = msg.list
                        var str = ""
                        for (i in data) {
                            str += "<tr align='center' style='display:table;width:100%;table-layout:fixed;'>" +
                                "<td width=\"4%\">" + data[i].id + "</td>" +
                                "<td width=\"5%\">" + data[i].name + "</td>" +
                                "<td width=\"15%\">" + data[i].zh + "</td>" +
                                "<td width=\"6%\">" + data[i].jdlx + "</td>" +
                                "<td width=\"10%\">" + data[i].jylx + "</td>" +
                                "<td width=\"14%\">" + data[i].shmc + "</td>" +
                                "<td width=\"8%\">" + data[i].jyje + "</td>" +
                                "<td width=\"13%\">" + data[i].jysj + "</td>" +
                                "<td width=\"15%\">" + data[i].fsf + "</td>" +
                                "<td width=\"8%\">" + data[i].fsje + "</td>" +
                                "<td width=\"15%\">" + data[i].jsf + "</td>" +
                                "<td width=\"8%\">" + data[i].jsje + "</td>" +
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

function getZzDetails(obj) {
    var jyzh = $(obj).closest("tr").find("td:eq(2)").text()
    var jylx = $(obj).closest("tr").find("td:eq(3)").text()
    window.page = 1
    var type = ""
    if( /^[a-zA-Z]([-_a-zA-Z0-9])*$/.test(jylx)){
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
                    "<td width=\"15%\">"+data[i].zh+"</td>"+
                    "<td width=\"6%\">"+data[i].jdlx+"</td>"+
                    "<td width=\"10%\">"+data[i].jylx+"</td>"+
                    "<td width=\"14%\">"+data[i].shmc+"</td>"+
                    "<td width=\"8%\">"+data[i].jyje+"</td>"+
                    "<td width=\"13%\">"+data[i].jysj+"</td>"+
                    "<td width=\"15%\">"+data[i].fsf+"</td>"+
                    "<td width=\"8%\">"+data[i].fsje+"</td>"+
                    "<td width=\"15%\">"+data[i].jsf+"</td>"+
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
})
});

function downDetailJylx(){
  var zh = $("#zh").val();
  var jylx =$("#jylx").val();
  var type = ""
    var tbody = window.document.getElementById("result")

    if( /^[a-zA-Z]([-_a-zA-Z0-9])*$/.test(jylx)){
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
