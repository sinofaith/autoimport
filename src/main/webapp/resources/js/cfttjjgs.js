$(document).ready(function(){
    // 载入文件按钮状态转换
    $('#btnLoadFile').mouseover(function(){
        $(this).attr('src', '/AMD/resources/img/loadFile_hover.png');
    });

    $('#btnLoadFile').mouseout(function(){
        $(this).attr('src', '/AMD/resources/img/loadFile.png');
    });
    //导入按钮绑定事件
    $('#btnLoadFile').click(function() {
        $("input[type='file']").click();
    });

    $("#uploadFileForm>input[type='file']").change(function(event) {

        $('#uploadFileForm').ajaxSubmit({
            dataType: 'text',
            success: function(result) {
                if (result == "") {
                    alertify.alert('上传文件出现问题！请检查网络环境是否正常或文件格式及内容是否符合标准！');
                } else{
                    alertify.alert("导入成功.");
                    setTimeout(function () {document.getElementById("seachDetail").submit()},3000);
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

function suspicionerSkip(){
    var totalPage = $("#totalPage").text();
    var onPage = $("#num").val();
    if(onPage ==="" || onPage === 0 || parseInt(onPage) <=0){
        alert("请输入你要跳转的页数！");
        return;
    }
    if(parseInt(onPage)>parseInt(totalPage)){
        alert("你输入的页数已经超过总页数，请从新输入！");
        return;
    } else {
        location="/SINOFAITH/cfttjjgs/seach?pageNo="+onPage;
    }
}

function crimegroupSkip(){
    var totalPage = $("#totalPage").text();
    var onPage = $("#num").val();
    if(onPage ==="" || onPage === 0|| parseInt(onPage) <=0){
        alert("请输入你要跳转的页数！");
        return;
    }
    if(parseInt(onPage)>parseInt(totalPage)){
        alert("你输入的页数已经超过总页数，请从新输入！");
        return;
    } else {
        location="/AMD/crimegroupinfo/detail/seach?pageNo="+onPage;
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
