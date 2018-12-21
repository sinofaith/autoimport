// 转账明细共同账户显示详情数据
function getZfbZzmxGtzhDetails(obj){
    // 对方账户
    var dfzh = $(obj).closest("tr").find("td:eq(3)").text();
    window.page = 1;
    var tbody = window.document.getElementById("result");
    var url = "/SINOFAITH/zfbZzmxGtzh/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            dfzh:dfzh,
            order:'jyzcs',
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list;
            insert(data,tbody,true);
            $("#dfzh").attr("value",dfzh);
            $("#allRow").attr("value",msg.totalRecords);
        }
    })
}

// 转账明细共同账户排序
function orderByFilter(filter){
    var tbody = window.document.getElementById("result");
    var dfzh = $("#dfzh").val();
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    window.page = 1;
    var url = "/SINOFAITH/zfbZzmxGtzh/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            dfzh:dfzh,
            order:filter,
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list;
            insert(data,tbody,true);
            $("#dfzh").attr("value",dfzh);
            $("#allRow").attr("value",msg.totalRecords);
        }
    })
}

// 转账明细共同账户滚动条加载数据
var is_running = false;
function scrollF() {
    var tbody = window.document.getElementById("result");
    var dfzh = $("#dfzh").val();
    var allRow = $("#allRow").val();
    var scrollT = parseFloat(tbody.scrollTop) + parseFloat(tbody.clientHeight)
    var scrollH = parseFloat(tbody.scrollHeight)
    if (1 >= scrollH - scrollT && tbody.scrollTop != 0 && tbody.childNodes.length < allRow) {
        if (is_running == false) {
            is_running = true;
            window.page = page += 1;
            var url = "/SINOFAITH/zfbZzmxGtzh/getDetails";
            $.ajax({
                type:"post",
                dataType:"json",
                url:url,
                data:{
                    dfzh:dfzh,
                    order:"xxx",
                    page:parseInt(window.page)
                },
                success:function (msg) {
                    var data = msg.list;
                    insert(data,tbody,false);
                    $("#dfzh").attr("value",dfzh);
                    $("#allRow").attr("value",msg.totalRecords);
                    is_running = false;
                }
            })
        }
    }
}


// 转账明细共同账户插入表记录
function insert(data,tbody,temp){
    var str = "";
    for (i in data){
        if(i%2==0){
            str+="<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
        }else{
            str+="<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
        }
        str+="<td width=\"4%\">"+data[i].id+"</td>"+
            "<td width=\"7%\">"+data[i].zfbzh+"</td>"+
            "<td width=\"6%\" title='"+data[i].zfbmc+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].zfbmc+"</div></td>"+
            "<td width=\"12%\">"+data[i].dfzh+"</td>"+
            "<td width=\"6%\">"+data[i].gthys+"</td>"+
            "<td width=\"6%\">"+data[i].jyzcs+ "</td>"+
            "<td width=\"6%\">"+data[i].fkzcs+"</td>"+
            "<td width=\"8%\">"+data[i].fkzje+"</td>"+
            "<td width=\"6%\">"+data[i].skzcs+"</td>"+
            "<td width=\"8%\">"+data[i].skzje+"</td>"+
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
        $.ajax({url:"/SINOFAITH/zfbZzmxGtzh/removeDesc"});
        $.ajax({url:"/SINOFAITH/zfbJyjl/removeDesc"});
    });
});
