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

// 账户明细账户与账户统计详情数据
function getZfbZhmxTjjgDetails(obj, url){
    var jyzfbzh = $(obj).closest("tr").find("td:eq(1)").text();
    var zhmc = $(obj).closest("tr").find("td:eq(2)").text();
    var dszfbzh = $(obj).closest("tr").find("td:eq(3)").text();
    var dsmc = $(obj).closest("tr").find("td:eq(4)").text();
    window.page = 1;
    var tbody = window.document.getElementById("result");
    var url = "/SINOFAITH/"+url+"/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            jyzfbzh:jyzfbzh,
            dszfbzh:dszfbzh,
            zhmc:zhmc,
            dsmc:dsmc,
            order:'je',
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list;
            insert(data,tbody,true);
            $("#jyzfbzh").attr("value",jyzfbzh);
            $("#dszfbzh").attr("value",dszfbzh);
            $("#zhmc").attr("value",zhmc);
            $("#dsmc").attr("value",dsmc);
            $("#allRow").attr("value",msg.totalRecords);
        }
    })
}

// 转账明细排序
function orderByFilter(filter, url){
    var tbody = window.document.getElementById("result");
    var jyzfbzh = $("#jyzfbzh").val();
    var dszfbzh = $("#dszfbzh").val();
    var zhmc = $("#zhmc").val();
    var dsmc = $("#dsmc").val();
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    window.page = 1;
    var url = "/SINOFAITH/"+url+"/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            jyzfbzh:jyzfbzh,
            dszfbzh:dszfbzh,
            zhmc:zhmc,
            dsmc:dsmc,
            order:filter,
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list;
            insert(data,tbody,true);
            $("#jyzfbzh").attr("value",jyzfbzh);
            $("#dszfbzh").attr("value",dszfbzh);
            $("#zhmc").attr("value",zhmc);
            $("#dsmc").attr("value",dsmc);
            $("#allRow").attr("value",msg.totalRecords);
        }
    })
}

// 转账明细滚动条加载数据
var is_running = false;
function scrollF(url) {
    var tbody = window.document.getElementById("result");
    var jyzfbzh = $("#jyzfbzh").val();
    var dszfbzh = $("#dszfbzh").val();
    var zhmc = $("#zhmc").val();
    var dsmc = $("#dsmc").val();
    var allRow = $("#allRow").val();
    var scrollT = parseFloat(tbody.scrollTop) + parseFloat(tbody.clientHeight);
    var scrollH = parseFloat(tbody.scrollHeight);
    if (1 >= scrollH - scrollT && tbody.scrollTop != 0 && tbody.childNodes.length < allRow) {
        if (is_running == false) {
            is_running = true;
            window.page = page += 1;
            var url = "/SINOFAITH/"+url+"/getDetails";
            $.ajax({
                type:"post",
                dataType:"json",
                url:url,
                data:{
                    jyzfbzh:jyzfbzh,
                    dszfbzh:dszfbzh,
                    zhmc:zhmc,
                    dsmc:dsmc,
                    order:"xxx",
                    page:parseInt(window.page)
                },
                success:function (msg) {
                    var data = msg.list;
                    insert(data,tbody,false);
                    $("#jyzfbzh").attr("value",jyzfbzh);
                    $("#dszfbzh").attr("value",dszfbzh);
                    $("#zhmc").attr("value",zhmc);
                    $("#dsmc").attr("value",dsmc);
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
            "<td width=\"10%\" title='"+data[i].jyh+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].jyh+"</div></td>"+
            "<td width=\"8%\" title='"+data[i].shddh+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].shddh!=null?data[i].shddh:"")+"</div></td>"+
            "<td width=\"8%\">"+data[i].fksj+"</td>"+
            "<td width=\"6%\">"+data[i].lx+"</td>"+
            "<td width=\"8%\" title='"+data[i].yhxx+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].yhxx!=null?data[i].yhxx:"")+"</div></td>"+
            "<td width=\"8%\" title='"+data[i].jydfxx+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].jydfxx!=null?data[i].jydfxx:"")+"</div></td>"+
            "<td width=\"8%\" title='"+data[i].xfmc+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].xfmc!=null?data[i].xfmc:"")+"</div></td>"+
            "<td width=\"4%\">"+data[i].je+"</td>"+
            "<td width=\"4%\">"+data[i].sz+"</td>"+
            "<td width=\"7%\">"+data[i].jyzt+"</td>"+
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
        $.ajax({url:"/SINOFAITH/zfbZhmxTjjg/removeDesc"});
    });
});

// 阀值设置
function seachChange() {
    var seachCondition = $("#seachCondition").val();
    if(seachCondition === "fkzje" || seachCondition === "skzje" ||
        seachCondition === "czzje" || seachCondition === "jzzje"){
        $("#seachCode").val("50000")
    }else{
        $("#seachCode").val("")
    }
}

function isNum(obj){
    var seachCondition = $("#seachCondition").val();
    if(seachCondition === "fkzje" || seachCondition === "skzje" ||
        seachCondition === "czzje" || seachCondition === "jzzje"){
        obj.value=obj.value.replace(/[^\d]/g,'')
    }
}

// 数据导出
function downZhmxTjjgDetailInfo(url){
    var jyzfbzh = $("#jyzfbzh").val();
    var dszfbzh = $("#dszfbzh").val();
    var zhmc = $("#zhmc").val();
    var dsmc = $("#dsmc").val();
    location = "/SINOFAITH/zfbZhmx"+url+"/downDetailInfo?jyzfbzh="+jyzfbzh+'&dszfbzh='+dszfbzh+'&zhmc='+zhmc+'&dsmc='+dsmc;
}