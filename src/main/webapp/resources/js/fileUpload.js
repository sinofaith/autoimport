$(document).ready(function(){

    $('#btnWuliuLoadFile').click(function() {
        $("#wuliuFile").click();
    });

    $("#wuliuFile").change(function(event) {
        $('.loading_content').show();
        $('#uploadFileWuliuForm').ajaxSubmit({
            dataType: 'text',
            success: function(result) {
                $('.loading_content').hide();
                if (result == "") {
                    alertify.alert('上传文件出现问题！请检查网络环境是否正常或文件格式及内容是否符合标准！');
                } else{
                    alertify.alert("物流文件导入成功.导入条数:"+result);
                }
            }
        });
        $("#wuliuFile").val("");
    });

    $('#btnTonghuaQingdanLoadFile').click(function() {
        $("#thqdFile").click();
    });

    $("#thqdFile").change(function(event) {
        $('.loading_content').show();
        $('#uploadFilethqdForm').ajaxSubmit({
            url:'/AMD/fileuploading/file/thqdcontents',
            dataType: 'text',
            success: function(result) {
                $('.loading_content').hide();
                if (result == "") {
                    alertify.alert('上传文件出现问题！请检查网络环境是否正常或文件格式及内容是否符合标准！');
                } else{
                    var columnsWidth = 0;
                    var strLine = result.split(";");
                    for(var i = 0;i<strLine.length;i++){
                        var filecontent="";
                        if(i==0){
                            var selectoption = "";
                            var oneLine = strLine[i].split(",");
                            filecontent = "<tr><td width='100%' align='center' colspan="+oneLine.length+">导入文件部分内容</td></tr><tr>";
                            columnsWidth = 100/oneLine.length;
                            for(var j=0;j<oneLine.length;j++){
                                var optionvalue = oneLine[j].split(" ")[0];
                                var selectvalue = oneLine[j].split(" ")[1];
                                filecontent = filecontent +"<td width='"+columnsWidth+"%'>"+selectvalue+"</td>";
                                selectoption = selectoption + "<option value='"+optionvalue+"'>"+selectvalue+"</option>"
                            }
                            $("#yfhm").append(selectoption);
                            $("#yfhm").val(0);
                            $("#dfhm").append(selectoption);
                            $("#dfhm").val(1);
                            $("#hjlx").append(selectoption);
                            $("#hjlx").val(2);
                            $("#hjrq").append(selectoption);
                            $("#hjrq").val(3);
                            $("#hjsj").append(selectoption);
                            $("#hjsj").val(4);
                            $("#hjsc").append(selectoption);
                            $("#hjsc").val(5);
                        } else {
                            var otherLine = strLine[i].split(",");
                            filecontent = filecontent + "<tr>"
                            for(var j=0;j<otherLine.length;j++){
                                filecontent = filecontent +"<td width='"+columnsWidth+"%'>"+otherLine[j]+"</td>";
                            }
                        }
                        filecontent = filecontent + "</tr>"
                        $("#filecontent").append(filecontent);
                    }
                    $("#fileContents").show();
                }
            }
        });
        $("#thqdFile").val("");
    });

    $('#btnWuliuexcelLoadFile').click(function() {
        $("#wleFile").click();
    });

    $("#wleFile").change(function(event) {
        $('.loading_content').show();
        $('#uploadwleFilethqdForm').ajaxSubmit({
            url:'/AMD/fileuploading/file/thqdcontents',
            dataType: 'text',
            success: function(result) {
                $('.loading_content').hide();
                if (result == "") {
                    alertify.alert('上传文件出现问题！请检查网络环境是否正常或文件格式及内容是否符合标准！');
                } else{
                    var columnsWidth = 0;
                    var strLine = result.split(";");
                    for(var i = 0;i<strLine.length;i++){
                        var filecontent="";
                        if(i==0){
                            var selectoption = "<option value='-1'>空栏</option>";
                            var oneLine = strLine[i].split(",");
                            filecontent = "<tr><td width='100%' align='center' colspan="+oneLine.length+">导入文件部分内容</td></tr><tr>";
                            columnsWidth = 100/oneLine.length;
                            for(var j=0;j<oneLine.length;j++){
                                var optionvalue = oneLine[j].split(" ")[0];
                                var selectvalue = oneLine[j].split(" ")[1];
                                filecontent = filecontent +"<td width='"+columnsWidth+"%'>"+selectvalue+"</td>";
                                selectoption = selectoption + "<option value='"+optionvalue+"'>"+selectvalue+"</option>"
                            }
                            $("#numbers").append(selectoption);
                            $("#numbers").val(0);
                            $("#shipmentsdata").append(selectoption);
                            $("#shipmentsdata").val(1);
                            $("#addresser").append(selectoption);
                            $("#addresser").val(2);
                            $("#shipmentphone1").append(selectoption);
                            $("#shipmentphone1").val(3);
                            $("#shipmentphone2").append(selectoption);
                            $("#shipmentphone2").val(4);
                            $("#shipmentphone3").append(selectoption);
                            $("#shipmentphone3").val(5);
                            $("#shipmentaddress").append(selectoption);
                            $("#shipmentaddress").val(6);
                            $("#recipients").append(selectoption);
                            $("#recipients").val(7);
                            $("#recipientsphone1").append(selectoption);
                            $("#recipientsphone1").val(8);
                            $("#recipientsphone2").append(selectoption);
                            $("#recipientsphone2").val(9);
                            $("#recipientsphone3").append(selectoption);
                            $("#recipientsphone3").val(10);
                            $("#recipientaddress").append(selectoption);
                            $("#recipientaddress").val(11);
                            $("#remark").append(selectoption);
                            $("#remark").val(12);
                        } else {
                            var otherLine = strLine[i].split(",");
                            filecontent = filecontent + "<tr>"
                            for(var j=0;j<otherLine.length;j++){
                                filecontent = filecontent +"<td width='"+columnsWidth+"%'>"+otherLine[j]+"</td>";
                            }
                        }
                        filecontent = filecontent + "</tr>"
                        $("#wlefilecontent").append(filecontent);
                    }
                    $("#wlefileContents").show();
                }
            }
        });
        $("#wleFile").val("");
    });

});


function cancel(){
    if(confirm("确定要取消吗？")){
        $("#fileContents").hide();
        $("#filecontent tr").remove();
        $(".selectchange option").remove();
    }
}

function wlecancel(){
    if(confirm("确定要取消吗？")){
        $("#wlefileContents").hide();
        $("#wlefilecontent tr").remove();
        $(".selectchange option").remove();
    }
}

function uploadFile(){
    var filePath = $("#Path").val();
    if(filePath.length==0){
        alert("请指定文件的路径")
        return ;
    }
    $("#filepath").val($("#Path").val());
    $("#Path").val("");
    $('.loading_content').show();
    $('#uploadFilethqdForm').ajaxSubmit({
        url:"/AMD/fileuploading/file/thqdwrite",
        dataType: 'text',
        type:'post',
        success: function(result) {
            $('.loading_content').hide();
            $("#fileContents").hide();
            $("#filecontent tr").remove();
            $(".selectchange option").remove();
            if (result == "") {
                alertify.alert("指定文件路径不对!");
            } else{
                alertify.alert("指定文件夹内容已经导入成功！");
            }
            $("#yfhmval").val("0");
            $("#dfhmval").val("1");
            $("#hjlxval").val("2");
            $("#hjrqval").val("3");
            $("#hjsjval").val("4");
            $("#hjscval").val("5");
        },
        error: function(result){
            $('.loading_content').hide();
            $("#fileContents").hide();
            $("#filecontent tr").remove();
            $(".selectchange option").remove();
            alert("导入失败，请检查网络环境是否正常！")
        }
    });
}

function uploadwleFile(){
    var filePath = $("#wlePath").val();
    if(filePath.length==0){
        alert("请指定文件的路径")
        return ;
    }
    $("#wlefilepath").val($("#wlePath").val());
    $("#Path").val("");
    $('.loading_content').show();
    $('#uploadwleFilethqdForm').ajaxSubmit({
        url:"/AMD/fileuploading/file/wlewrite",
        dataType: 'text',
        type:'post',
        success: function(result) {
            $('.loading_content').hide();
            $("#fileContents").hide();
            $("#filecontent tr").remove();
            $(".selectchange option").remove();
            if (result == "") {
                alertify.alert("指定文件路径不对!");
            } else{
                alertify.alert("指定文件夹内容已经导入成功！");
            }
            $("#numbersval").val("0");
            $("#shipmentsdataval").val("1");
            $("#addresserval").val("2");
            $("#shipmentphone1val").val("3");
            $("#shipmentphone2val").val("4");
            $("#shipmentphone3val").val("5");
            $("#shipmentaddressval").val("6");
            $("#recipientsval").val("7");
            $("#recipientsphone1val").val("8");
            $("#recipientsphone2val").val("9");
            $("#recipientsphone3val").val("10");
            $("#recipientaddressval").val("11");
            $("#remarkval").val("12");
            $('.loading_content').hide();
            $("#wlefileContents").hide();
            $("#wlefilecontent tr").remove();
            $(".selectchange option").remove();
        },
        error: function(result){
            $('.loading_content').hide();
            $("#wlefileContents").hide();
            $("#wlefilecontent tr").remove();
            $(".selectchange option").remove();
            alert("导入失败，请检查网络环境是否正常！")
        }
    });
}

function selectchange(obj){
    if("yfhm" === obj){
        $("#yfhmval").val($("#yfhm").val());
    } else if("dfhm" === obj){
        $("#dfhmval").val($("#dfhm").val());
    } else if("hjlx" ===obj){
        $("#hjlxval").val($("#hjlx").val());
    }else if("hjrq" ===obj){
        $("#hjrqval").val($("#hjrq").val());
    }else if("hjsj" ===obj){
        $("#hjsjval").val($("#hjsj").val());
    }else if("hjsc" ===obj){
        $("#hjscval").val($("#hjsc").val());
    }else if("numbers" ===obj){
        $("#numbersval").val($("#numbers").val());
    }else if("shipmentsdata" ===obj){
        $("#shipmentsdataval").val($("#shipmentsdata").val());
    }else if("addresser" ===obj){
        $("#addresserval").val($("#addresser").val());
    }else if("shipmentphone1" ===obj){
        $("#shipmentphone1val").val($("#shipmentphone1").val());
    }else if("shipmentphone2" ===obj){
        $("#shipmentphone2val").val($("#shipmentphone2").val());
    }else if("shipmentphone3" ===obj){
        $("#shipmentphone3val").val($("#shipmentphone3").val());
    }else if("shipmentaddress" ===obj){
        $("#shipmentaddressval").val($("#shipmentaddress").val());
    }else if("recipients" ===obj){
        $("#recipientsval").val($("#recipients").val());
    }else if("recipientsphone1" ===obj){
        $("#recipientsphone1val").val($("#recipientsphone1").val());
    }else if("recipientsphone2" ===obj){
        $("#recipientsphone2val").val($("#recipientsphone2").val());
    }else if("recipientsphone3" ===obj){
        $("#recipientsphone3val").val($("#recipientsphone3").val());
    }else if("recipientaddress" ===obj){
        $("#recipientaddressval").val($("#recipientaddress").val());
    }else if("remark" ===obj){
        $("#remarkval").val($("#remark").val());
    }

}