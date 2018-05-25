function CMupdatecheck() {
    var wdmc = $("#wdmc").val().replace(" ","");
    if(wdmc.length ==0 ){
        alert("窝点名称不能为空，或者全是空格。");
        return ;
    }
    if(confirm("是否保存")) {
        document.getElementById("formupdate").action = "/AMD/crimegroupinfo/update";
        document.getElementById("formupdate").submit();
    }
}

function CMcancelcheck(){
    if(confirm("确定取消")) {
        location="/AMD/crimegroupinfo/detail/seach?pageNo=1";
    }
}
function Crupdatecheck() {
    var photo = $("#photo").val();
    if(photo.length!=0){
        var extStart=photo.lastIndexOf(".");
        var ext=photo.substring(extStart,photo.length).toUpperCase();
        if(ext!=".BMP"&&ext!=".PNG"&&ext!=".GIF"&&ext!=".JPG"&&ext!=".JPEG"){
            alert("图片限于bmp,png,gif,jpeg,jpg格式");
            return;
        }
    }
    var sfz = $("#sfz").val();
    var gszcm = $("#gszcm").val();
    if(sfz.length <7 && gszcm.length <7){
        if(sfz.length !=0 && gszcm.length ==0){
            alert("身份证号码/证件号必须7位以上");
        } else if(sfz.length ==0 && gszcm.length !=0){
            alert("公司注册账号必须7位以上");
        } else {
            alert("身份证号码/证件号必须7位以上");
        }

        return ;
    }
    //
    if(confirm("是否保存")) {
        document.getElementById("crimeterraceform").action = "/AMD/crimeterrace/update";
        document.getElementById("crimeterraceform").submit();
    }
}

function Crcancelcheck(){
    if(confirm("确定取消")) {
        location="/AMD/crimeterrace/detail/seach?pageNo=1";
    }
}

function addCrimeterrace(){
    var sfz = $("#sfz").val();
    var gszcm = $("#gszcm").val();
    if(sfz.length <7 && gszcm.length <7){
        if(sfz.length !=0 && gszcm.length ==0){
            alert("身份证号码/证件号必须7位以上");
        } else if(sfz.length ==0 && gszcm.length !=0){
            alert("公司注册账号必须7位以上");
        } else {
            alert("身份证号码/证件号必须7位以上");
        }

        return ;
    }

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
                if(confirm("是否新增")) {
                    document.getElementById("crimeterraceform").action = "/AMD/crimeterrace/add";
                    document.getElementById("crimeterraceform").submit();
                }
            }
        }
    });
}

function addCrimegroupinfo() {
    var wdmc = $("#wdmc").val().replace(" ","");
    if(wdmc.length ==0 ){
        alert("窝点名称不能为空，或者全是空格。");
        return ;
    }
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
                if(confirm("是否保存")) {
                    document.getElementById("formupdate").action = "/AMD/crimegroupinfo/add";
                    document.getElementById("formupdate").submit();
                }
            }
        }
    });

}
