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

function UploadBank() {
    var fileObj = document.getElementById("file");// js 获取文件对象
    var file = $("#file").val();
    if(file==''){
        alertify.alert('请选择要上传的文件夹')
        return;
    }
    var aj = $("#aj").val();
    // var checkBox = 0
    // if($("#checkbox1").is(':checked')){
    //     checkBox=1
    // }
    if(aj==''){
        alertify.alert('请填写案件名称')
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
            alertify.alert("导入完成!");
            $('#myModal').modal('hide');
            setTimeout(function () {document.getElementById("seachDetail").submit()},1500);
        }else{
            alertify.alert("错误!请联系管理员")
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
            alertify.alert("文件夹上传成功\n请等待数据导入...");
        }
    }
}