$(function () {
    $('#myModal3').on('hide.bs.modal', function () {
        $("#myModal3 input").val("");
        $("#relationName").find("option:selected").attr("selected",false);
        changeRelationShow();
        $("#submitRelation").text("添加");
    });
    $('#myModalCompany').on('hide.bs.modal', function () {
        var name = $("#glname").val();
        $("#myModalCompany input").val("");
        $("#cpname").val(name);
        $("#submitCompany").text("添加");
    });
    $('#myModalPhoneNumber').on('hide.bs.modal', function () {
        var name = $("#glname").val();
        $("#myModalPhoneNumber input[type='text']").val("");
        $('input:radio[name="sex"]').prop('checked',false);
        $("#personName").val(name);
    });

    $("[data-toggle='tooltip']").tooltip();

})

function getPersonDetails(name) {
    $("#glname").val(name)
    var personCompany = window.document.getElementById("personCompany");
    var personNumber = window.document.getElementById("personNumber");
    $("#cpname").val(name);
    $("#personName").val(name);
    var url = "/SINOFAITH/customerPro/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            name:name
        },
        success:function (msg) {
            var companyResult = msg.personCompany;
            var numberResult = msg.personNumber;
            // var data = msg.list
            var str = ""
            if(companyResult.length>0){
                for (i in companyResult){
                    if(i%2==0){
                        str+="<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                    }else{
                        str+="<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                    }
                    str+="<td width=\"2%\">"+(parseInt(i)+1)+"</td>"+
                        "<td width=\"4%\" title=\""+companyResult[i].name+"\"><div style=\"width:50px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+companyResult[i].name+"</div></td>"+
                        "<td width=\"10%\" title=\""+companyResult[i].company+"\"><div style=\"width:160px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+companyResult[i].company+"</div></td>"+
                        // "<td width=\"10%\" title=\""+companyResult[i].companyPerson+"\"><div style=\"width:200px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+companyResult[i].companyPerson+"</div></td>"+
                        "<td width=\"6%\" title=\""+companyResult[i].companyWeb+"\"><div style=\"width:100px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+companyResult[i].companyWeb+"</div></td>"+
                        "<td width=\"15%\" title=\""+companyResult[i].companyAdd+"\"><div style=\"width:250px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+companyResult[i].companyAdd+"</div></td>"+
                        // "<td width=\"20%\"> "+companyResult[i].companyRemark+"</td>"+
                        "<td width=\"20%\" title=\""+companyResult[i].companyRemark+"\"><div style=\"width:250px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+companyResult[i].companyRemark+"</div></td>"+
                        "<td width=\"10%\" title=\""+companyResult[i].companyPhone+"\"><div style=\"width:150px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+companyResult[i].companyPhone+"</div></td>"+
                        "<td width=\"10%\" title=\""+companyResult[i].companyEmail+"\"><div style=\"width:150px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+companyResult[i].companyEmail+"</div></td>"+
                        "<td width=\"5%\"><button  data-toggle=\"modal\" data-target=\"#myModalCompany\" class=\"btna\" onclick=\"editCompany("+companyResult[i].id+")\">编辑</button> |" +
                        "<button  class=\"btna\" onclick=\"deleteCompany("+companyResult[i].id+")\">删除</button></td>"+
                        "</tr>";
                }
            }
            str+= "<tr align='center' style='display:table;width:100%;table-layout:fixed;'><td colspan='10' data-toggle=\"modal\" class=\"btna\" data-target=\"#myModalCompany\"><button>添加数据</button></td></tr>"
            personCompany.innerHTML = str

            str = ""
            if(numberResult.length>0){
                for (i in numberResult){
                    if(i%2==0){
                        str+="<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                    }else{
                        str+="<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                    }
                    str+="<td width=\"2%\">"+(parseInt(i)+1)+"</td>"+
                        "<td width=\"4%\" title=\""+numberResult[i].name+"\"><div style=\"width:50px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+numberResult[i].name+"</div></td>"+
                        "<td width=\"8%\" title=\""+numberResult[i].phone+"\"><div style=\"width:100px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+numberResult[i].phone+"</div></td>"+
                        "<td width=\"8%\" title=\""+numberResult[i].numbers+"\"><div style=\"width:100px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+numberResult[i].numbers+"</div></td>"+
                        "<td width=\"8%\" title=\""+numberResult[i].numberName+"\"><div style=\"width:100px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+numberResult[i].numberName+"</div></td>"+
                        "<td width=\"6%\" title=\""+numberResult[i].sex+"\"><div style=\"width:100px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+numberResult[i].sex+"</div></td>"+
                        "<td width=\"6%\" title=\""+numberResult[i].age+"\"><div style=\"width:100px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+numberResult[i].age+"</div></td>"+
                        "<td width=\"8%\" title=\""+numberResult[i].address+"\"><div style=\"width:100px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+numberResult[i].address+"</div></td>"+
                        "<td width=\"8%\" title=\""+numberResult[i].numberType+"\"><div style=\"width:100px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+numberResult[i].numberType+"</div></td>"+
                        "<td width=\"5%\"><button  data-toggle=\"modal\" data-target=\"#myModalPhoneNumber\" class=\"btna\" onclick=\"editNumber("+numberResult[i].id+")\">编辑</button> |" +
                        "<button  class=\"btna\" onclick=\"deleteNumbers("+numberResult[i].id+")\">删除</button></td>"+
                        "</tr>";
                }
            }
            str+= "<tr align='center' style='display:table;width:100%;table-layout:fixed;'><td colspan='10' data-toggle=\"modal\" class=\"btna\" data-target=\"#myModalPhoneNumber\" ><button>添加数据</button></td></tr>"
            personNumber.innerHTML = str
        }
    });
}

function addNumber() {
    var id = $("#numberId").val();
    var name = $("#personName").val();
    var phone=$("#phone").val();
    if(name.trim()==''){
        $("#personName").attr('data-original-title',"目标姓名不能为空").tooltip('show');
        return
    }
    if(phone.trim()==''){
        $("#phone").attr('data-original-title',"公司名称不能为空").tooltip('show');
        return
    }
    var data = {
        id:id,
        name:name,
        phone:phone,
        numbers:$("#number").val(),
        numberName:$("#numberName").val(),
        sex:$('input[name="sex"]:checked').val(),
        age:$("#age").val(),
        address:$("#address").val(),
        numberType:$("#numberType").val(),
    };
    if(id===""){
        var url = "/SINOFAITH/customerPro/addNumbers";
        result = "添加成功";
    }else{
        var url = "/SINOFAITH/customerPro/editNumbers";
        result= "修改成功";
    }
    $.ajax({
        type:"post",
        dataType:"json",
        contentType:"application/json",
        url:url,
        data:JSON.stringify(data),
        success:function (msg) {
            if(msg=="200") {
                alertify.success(result);
                $('#myModalPhoneNumber').modal('hide');
                getPersonDetails(name);
                $("#personName").val(name);
            }else{
                alertify.set('notifier','position', 'top-center');
                alertify.error("错误");
            }
        }
    })
}

function addCompany() {
    var id = $("#companyId").val();
    var name = $("#cpname").val();
    var companyName=$("#companyName").val();
    if(name.trim()==''){
        $("#cpname").attr('data-original-title',"目标姓名不能为空").tooltip('show');
        return
    }
    if(companyName.trim()==''){
        $("#companyName").attr('data-original-title',"公司名称不能为空").tooltip('show');
        return
    }
    var data = {
        id:id,
        name:name,
        company:companyName,
        companyWeb:$("#companyWeb").val(),
        companyAdd:$("#companyAdd").val(),
        companyRemark:$("#companyRemark").val(),
        companyPhone:$("#companyPhone").val(),
        companyEmail:$("#companyEmail").val(),
    }

    if(id===""){
        var url = "/SINOFAITH/customerPro/addCompany";
        result = "添加成功";
    }else{
        var url = "/SINOFAITH/customerPro/editCompany";
        result= "修改成功";
    }
    $.ajax({
        type:"post",
        dataType:"json",
        contentType:"application/json",
        url:url,
        data:JSON.stringify(data),
        success:function (msg) {
            if(msg=="200") {
                alertify.success(result);
                $('#myModalCompany').modal('hide');
                getPersonDetails(name);
            }else{
                alertify.set('notifier','position', 'top-center');
                alertify.error("错误");
            }
        }
    })
}

function addRelation() {
    var id = $("#id").val();
    var name = $("#name").val().trim().toUpperCase().replace("（","(").replace("）",")").replace(" ","");
    var pname = $("#pname").val().trim().toUpperCase().replace("（","(").replace("）",")").replace(" ","");
    if(name.trim()==''){
        $("#name").attr('data-original-title',"目标姓名不能为空").tooltip('show');
        return
    }
    if(pname.trim()==''){
        $("#pname").attr('data-original-title',"对象不能为空").tooltip('show');
        return
    }
    if($("#jyjz").val()==''&&$("#relationName").val()=='资金关联'){
        alertify.set('notifier','position', 'top-center');
        alertify.error("未查询到账户点对点统计,无法添加资金关联");
        return
    }
    if(name===pname){
        alertify.set('notifier','position', 'top-center');
        alertify.error("姓名一致,不可添加关系");
        return ;
    }
    var data = {
        id:id,
        name:name,
        pname:pname,
        relationName:$("#relationName").val(),
        relationShow:$("#relationShow").val(),
        relationMark:$("#relationMark").val()
    }
    var url = "";
    var result = "";
    if(id===""){
         url = "/SINOFAITH/customerRelation/addRelation";
         result = "添加成功";
    }else{
         url = "/SINOFAITH/customerRelation/editRelation";
         result= "修改成功";
    }
    $.ajax({
        type:"post",
        dataType:"json",
        contentType:"application/json",
        url:url,
        data:JSON.stringify(data),
        success:function (msg) {
            if(msg=="200") {
                alertify.success(result);
                $('#myModal3').modal('hide');
                setTimeout(function () {document.getElementById("seachDetail").submit()},1500);
            }else{
                alertify.set('notifier','position', 'top-center');
                alertify.error("错误");
            }
        }
    })
}
function changeRelationShow() {
    $("#relationShow").empty();
    var relationName = $("#relationName").find("option:selected").val();
    if(relationName=="资金关联"){
        $("#relationShow").append("<option value='上家'>上家</option>");
        $("#relationShow").append("<option value='下家'>下家</option>");
        $("#relationShow").append("<option value='仓储物流'>仓储物流</option>");
        $("#relationShow").append("<option value='电商平台'>电商平台</option>");
        $("#relationShow").append("<option value='支付平台'>支付平台</option>");
        $("#relationShow").append("<option value='日常消费'>日常消费</option>");
    }
    if(relationName=="公司关联"){
        $("#relationShow").append("<option value='股东'>股东</option>");
        $("#relationShow").append("<option value='董事'>董事</option>");
        $("#relationShow").append("<option value='监事'>监事</option>");
        $("#relationShow").append("<option value='经理'>经理</option>");
        $("#relationShow").append("<option value='高管'>高管</option>");
        $("#relationShow").append("<option value='职工'>职工</option>");
        $("#relationShow").append("<option value='其他'>其他</option>");
    }
    if(relationName=="户籍关联"){
        $("#relationShow").append("<option value='丈夫'>丈夫</option>");
        $("#relationShow").append("<option value='妻子'>妻子</option>");
        $("#relationShow").append("<option value='父亲'>父亲</option>");
        $("#relationShow").append("<option value='母亲'>母亲</option>");
        $("#relationShow").append("<option value='儿子'>儿子</option>");
        $("#relationShow").append("<option value='女儿'>女儿</option>");
        $("#relationShow").append("<option value='其他亲属'>其他亲属</option>");
    }
}

function getCaseNameOnfocus(id) {
    var e = jQuery.Event("keydown");//模拟一个键盘事件
    e.keyCode = 8;//keyCode=8是空格
    $("#"+id).trigger(e);
    $("#"+id).autocomplete({
        source: "/SINOFAITH/bank/getName",
        minLength: 1
    });
}

function deleteCompany(id) {
    alertify.confirm("确定删除?",function () {
        $.ajax({
            type:"get",
            dataType:"json",
            url:"/SINOFAITH/customerPro/deleteCompany",
            data:{
                id:id
            },
            success:function (data) {
                if(data==200){
                    alertify.success("删除成功");
                    getPersonDetails($("#glname").val());
                }

            },
            error:function (e) {
                alertify.error("错误")}
        })
    }, function() {
        // 用户点击"cancel"按钮
        return
    });
}

function deleteNumbers(id) {
    alertify.confirm("确定删除?",function () {
        $.ajax({
            type:"get",
            dataType:"json",
            url:"/SINOFAITH/customerPro/deleteNumbers",
            data:{
                id:id
            },
            success:function (data) {
                if(data==200){
                    alertify.success("删除成功");
                    getPersonDetails($("#glname").val());
                }

            },
            error:function (e) {
                alertify.error("错误")}
        })
    }, function() {
        // 用户点击"cancel"按钮
        return
    });
}

function deleteRelation(id) {
    alertify.confirm("确定删除?",function () {
        $.ajax({
            type:"get",
            dataType:"json",
            url:"/SINOFAITH/customerRelation/deleteRelation",
            data:{
                id:id
            },
            success:function (data) {
                if(data==200){
                    alertify.success("删除成功")
                }
                setTimeout(function () {document.getElementById("seachDetail").submit()},1000);
            },
            error:function (e) {
                alertify.error("错误")}
        })
    }, function() {
        // 用户点击"cancel"按钮
        return
    });
}

function editCompany(id) {
    var url = "/SINOFAITH/customerPro/getCompany";
    $.ajax({
        type:"get",
        dataType:"json",
        url:url,
        data:{
            id:id
        },
        success:function (msg) {
            $("#cpname").val(msg.name);
            $("#companyName").val(msg.company);
            $("#companyId").val(msg.id);
            $("#companyWeb").val(msg.companyWeb);
            $("#companyAdd").val(msg.companyAdd);
            $("#companyRemark").val((msg.companyRemark));
            $("#companyPhone").val((msg.companyPhone));
            $("#companyEmail").val((msg.companyEmail));
            $("#submitCompany").text("修改");
        }
    });
}

function editNumber(id) {
    var url = "/SINOFAITH/customerPro/getNumber";
    $.ajax({
        type:"get",
        dataType:"json",
        url:url,
        data:{
            id:id
        },
        success:function (msg) {
            $("#numberId").val(msg.id);
            $("#personName").val(msg.name);
            $("#phone").val(msg.phone);
            $("#number").val(msg.numbers);
            $("#numberName").val(msg.numberName);
            if(msg.sex!="") {
                $(":radio[name='sex'][value='" + msg.sex + "']").prop("checked", true);
            }
            $("#age").val(msg.age);
            $("#address").val((msg.address));
            $("#numberType").val((msg.numberType));
            $("#submitNumber").text("修改");
        }
    });
}

function editRelation(id) {
    var url = "/SINOFAITH/customerRelation/getRelation";
    $.ajax({
        type:"get",
        dataType:"json",
        url:url,
        data:{
            id:id
        },
        success:function (msg) {
            $("#name").val(msg.name);
            $("#pname").val(msg.pname);
            $("#id").val(msg.id);
            $("#jzzje").val(msg.jzzje);
            $("#czzje").val(msg.czzje);
            $("#jyjz").val((msg.jzzje-msg.czzje).toFixed(0));
            $("#relationName").find("option[value='"+msg.relationName+"']").prop("selected",true);
            changeRelationShow();
            $("#relationShow").find("option[value='"+msg.relationShow+"']").prop("selected",true);
            $("#submitRelation").text("修改");
        }
    })
}

function getJczz() {
    $("#jzzje").val("");
    $("#czzje").val("");
    $("#jyjz").val("");
    var url = "/SINOFAITH/banktjjgs/getJczz";
    var name = $("#name").val().trim();
    var pname = $("#pname").val().trim();
    if(name==''||pname==''){
        return;
    }
    $.ajax({
        type:"get",
        dataType:"json",
        url:url,
        data:{
            name:name,
            pname:pname
        },
        success:function (msg) {
            $("#jzzje").val(msg.jzzje);
            $("#czzje").val(msg.czzje);
            $("#jyjz").val((msg.jzzje-msg.czzje).toFixed(0));
            if($("#jyjz").val()>0){
                $("#relationShow").find("option[value='上家']").attr("selected",true);
            }
            if($("#jyjz").val()<0){
                $("#relationShow").find("option[value='下家']").attr("selected",true);
            }
        }
    });
}
