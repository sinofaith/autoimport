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
            alertify.set('notifier','position', 'top-center');
            alertify.success("导入完成!");
            $('#myModal').modal('hide');
            setTimeout(function () {document.getElementById("seachDetail").submit()},1500);
        }else{
            alertify.set('notifier','position', 'top-center');
            alertify.error("错误!请联系管理员")
            return;
        }
    };
    xhr.upload.addEventListener("progress", progressFunction, false);
    xhr.send(form);
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