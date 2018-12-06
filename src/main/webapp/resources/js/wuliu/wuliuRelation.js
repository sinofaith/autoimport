// 跳转页面
function relationSkip(code) {
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
        location="/SINOFAITH/wuliu"+code+"/seach?pageNo="+onPage;
    }
}

// 弹出框
function getZzDetails(obj) {
    var ship_phone = $(obj).closest("tr").find("td:eq(2)").text()
    var sj_phone = $(obj).closest("tr").find("td:eq(5)").text()
    window.page = 1
    /*var type = ""
    if( /^[a-zA-Z]([-_a-zA-Z0-9])*$/.test(jylx)){
        type="dfzh"
    }else{
        type="jylx"
    }*/
    var tbody = window.document.getElementById("result")
    var url = "/SINOFAITH/wuliuRelation/getDetails"
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            ship_phone:ship_phone,
            sj_phone:sj_phone,
            order:'ship_time',
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
                str+="<td width=\"5%\">"+data[i].id+"</td>"+
                    "<td width=\"8%\">"+data[i].waybill_id+"</td>"+
                    "<td width=\"10%\">"+data[i].ship_time+"</td>"+
                    "<td width=\"8%\">"+data[i].sender+"</td>"+
                    "<td width=\"8%\" title="+data[i].ship_phone+">"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].ship_phone+"</div>"+
                    "</td>"+
                    "<td width=\"15%\">"+data[i].ship_address+"</td>"+
                    "<td width=\"8%\">"+data[i].addressee+"</td>"+
                    "<td width=\"8%\" title='+data[i].sj_phone+'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].sj_phone+"</div>"+
                    "<td width=\"12%\">"+data[i].sj_address+"</td>"+
                    "<td width=\"10%\" title='+data[i].tjw+'>"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].tjw+"</div>"+
                    "</td>"+
                    "<td width=\"5%\">"+data[i].payment+"</td>"+
                    "<td width=\"5%\">"+data[i].dshk+"</td>"+
                    "<td width=\"5%\">"+data[i].freight+"</td>"+
                    "</tr>";
            }
            tbody.innerHTML = str
            $("#ship_phone").attr("value",ship_phone);
            $("#sj_phone").attr("value",sj_phone);
            $("#allRow").attr("value",msg.totalRecords)
        }
    })
}

// 每次点击将数据清空
$(function () { $('#myModal').on('hide.bs.modal', function () {

    var tbody = window.document.getElementById("result")
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    $.ajax({url:"/SINOFAITH/wuliuRelation/removeDesc"})
})
});

// 排序数据
function orderByFilter(filter) {
    var tbody = window.document.getElementById("result")
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    var ship_phone = $("#ship_phone").val();
    var sj_phone = $("#sj_phone").val();
    window.page = 1
    // var type = ""
    // if( /^[a-zA-Z]([-_a-zA-Z0-9])*$/.test(jylx)){
    //     type="dfzh"
    // }else{
    //     type="jylx"
    // }
    var url = "/SINOFAITH/wuliuRelation/getDetails"
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            ship_phone:ship_phone,
            sj_phone:sj_phone,
            order:filter,
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
                str+="<td width=\"5%\">"+data[i].id+"</td>"+
                    "<td width=\"8%\">"+data[i].waybill_id+"</td>"+
                    "<td width=\"10%\">"+data[i].ship_time+"</td>"+
                    "<td width=\"8%\">"+data[i].sender+"</td>"+
                    "<td width=\"8%\" title="+data[i].ship_phone+">"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].ship_phone+"</div>"+
                    "</td>"+
                    "<td width=\"15%\">"+data[i].ship_address+"</td>"+
                    "<td width=\"8%\">"+data[i].addressee+"</td>"+
                    "<td width=\"8%\" title="+data[i].sj_phone+">"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].sj_phone+"</div>"+
                    "<td width=\"12%\">"+data[i].sj_address+"</td>"+
                    "<td width=\"10%\" title="+data[i].tjw+">"+
                    "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].tjw+"</div>"+
                    "</td>"+
                    "<td width=\"5%\">"+data[i].payment+"</td>"+
                    "<td width=\"5%\">"+data[i].dshk+"</td>"+
                    "<td width=\"5%\">"+data[i].freight+"</td>"+
                    "</tr>";
            }
            tbody.innerHTML = str
            $("#ship_phone").attr("value",ship_phone);
            $("#sj_phone").attr("value",sj_phone);
            $("#allRow").attr("value",msg.totalRecords)
            // title.innerText ="<"+jyzh+","+jylx+">"
        }
    })
}

// 滚动条加载数据
var page = 1
var is_running = false
function scrollF() {
    var tbody = window.document.getElementById("result")
    var allRow = $("#allRow").val()
    var scrollT = parseFloat(tbody.scrollTop) + parseFloat(tbody.clientHeight)
    var scrollH = parseFloat(tbody.scrollHeight)
    if (1 >= scrollH - scrollT && tbody.scrollTop != 0 && tbody.childNodes.length < allRow) {
        if (is_running == false) {
            var ship_phone = $("#ship_phone").val();
            var sj_phone = $("#sj_phone").val();
            window.page = page += 1;
            var url = "/SINOFAITH/wuliuRelation/getDetails"
            $.ajax({
                type:"post",
                dataType:"json",
                url:url,
                data:{
                    ship_phone:ship_phone,
                    sj_phone:sj_phone,
                    order:"xxx",
                    page:parseInt(window.page)
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
                        str+="<td width=\"5%\">"+data[i].id+"</td>"+
                            "<td width=\"8%\">"+data[i].waybill_id+"</td>"+
                            "<td width=\"10%\">"+data[i].ship_time+"</td>"+
                            "<td width=\"8%\">"+data[i].sender+"</td>"+
                            "<td width=\"8%\" title="+data[i].ship_phone+">"+
                            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].ship_phone+"</div>"+
                            "</td>"+
                            "<td width=\"15%\">"+data[i].ship_address+"</td>"+
                            "<td width=\"8%\">"+data[i].addressee+"</td>"+
                            "<td width=\"8%\" title="+data[i].sj_phone+">"+
                            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].sj_phone+"</div>"+
                            "<td width=\"12%\">"+data[i].sj_address+"</td>"+
                            "<td width=\"10%\" title="+data[i].tjw+">"+
                            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].tjw+"</div>"+
                            "</td>"+
                            "<td width=\"5%\">"+data[i].payment+"</td>"+
                            "<td width=\"5%\">"+data[i].dshk+"</td>"+
                            "<td width=\"5%\">"+data[i].freight+"</td>"+
                            "</tr>";
                    }
                    $("#result").append(str)
                    $("#ship_phone").attr("value",ship_phone);
                    $("#sj_phone").attr("value",sj_phone);
                    $("#allRow").attr("value",msg.totalRecords);
                }
            })
        }
    }
}

// 数据导出
function downDetailInfo(){
    var ship_phone = $('#ship_phone').val();
    var sj_phone = $('#sj_phone').val();
    location = "/SINOFAITH/wuliuRelation/downDetailInfo?ship_phone="+ship_phone+'&sj_phone='+sj_phone;
}