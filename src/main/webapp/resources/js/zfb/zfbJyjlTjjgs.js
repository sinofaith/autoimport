// 转账明细显示详情数据
function getZfbJyjlTjjgsDetails(obj){
    // 用户Id
    var zfbzh = $(obj).closest("tr").find("td:eq(1)").text();
    // 交易状态
    var jyzt = $(obj).closest("tr").find("td:eq(3)").text();
    // 对方Id
    var dfzh = $(obj).closest("tr").find("td:eq(4)").text();
    window.page = 1;
    var tbody = window.document.getElementById("result");
    var url = "/SINOFAITH/zfbJyjlTjjgs/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            zfbzh:zfbzh,
            jyzt:jyzt,
            dfzh:dfzh,
            order:'jyje',
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list;
            insert(data,tbody,true);
            $("#zfbzh").attr("value",zfbzh);
            $("#jyzt").attr("value",jyzt);
            $("#dfzh").attr("value",dfzh);
            $("#allRow").attr("value",msg.totalRecords);
        }
    })
}

// 转账明细排序
function orderByFilter(filter){
    var tbody = window.document.getElementById("result");
    var zfbzh = $("#zfbzh").val();
    var jyzt = $("#jyzt").val();
    var dfzh = $("#dfzh").val();
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    window.page = 1;
    var url = "/SINOFAITH/zfbJyjlTjjgs/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            zfbzh:zfbzh,
            jyzt:jyzt,
            dfzh:dfzh,
            order:filter,
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list;
            insert(data,tbody,true);
            $("#zfbzh").attr("value",zfbzh);
            $("#jyzt").attr("value",jyzt);
            $("#dfzh").attr("value",dfzh);
            $("#allRow").attr("value",msg.totalRecords);
        }
    })
}

// 转账明细滚动条加载数据
var is_running = false;
function scrollF() {
    var tbody = window.document.getElementById("result");
    var zfbzh = $("#zfbzh").val();
    var jyzt = $("#jyzt").val();
    var dfzh = $("#dfzh").val();
    var allRow = $("#allRow").val();
    var scrollT = parseFloat(tbody.scrollTop) + parseFloat(tbody.clientHeight)
    var scrollH = parseFloat(tbody.scrollHeight)
    if (1 >= scrollH - scrollT && tbody.scrollTop != 0 && tbody.childNodes.length < allRow) {
        if (is_running == false) {
            is_running = true;
            window.page = page += 1;
            var url = "/SINOFAITH/zfbJyjlTjjgs/getDetails";
            $.ajax({
                type:"post",
                dataType:"json",
                url:url,
                data:{
                    zfbzh:zfbzh,
                    jyzt:jyzt,
                    dfzh:dfzh,
                    order:"xxx",
                    page:parseInt(window.page)
                },
                success:function (msg) {
                    var data = msg.list;
                    insert(data,tbody,false);
                    $("#zfbzh").attr("value",zfbzh);
                    $("#jyzt").attr("value",jyzt);
                    $("#dfzh").attr("value",dfzh);
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
            "<td width=\"6%\" title='"+data[i].jyh+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].jyh+"</div></td>"+
            "<td width=\"8%\">"+data[i].mjyhId+"</td>"+
            "<td width=\"8%\" title='"+data[i].mjxx+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].mjxx+"</div></td>"+
            "<td width=\"4%\">"+data[i].jyzt+"</td>"+
            "<td width=\"8%\">"+data[i].mijyhId+ "</td>"+
            "<td width=\"8%\" title='"+data[i].mijxx+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].mijxx+"</div></td>"+
            "<td width=\"7%\">"+data[i].jyje+"</td>"+
            "<td width=\"12%\">"+data[i].sksj+"</td>"+
            "<td width=\"12%\" title='"+data[i].spmc+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].spmc+"</div></td>"+
            "<td width=\"8%\" title='"+data[i].shrdz+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].shrdz+"</div></td>"+
            "</tr>";
    }
    if(temp){
        tbody.innerHTML = str;
    }else{
        tbody.innerHTML += str;
    }
}

// 条件筛选
function filterJyjlByspmc(aj){
    var filterInput = $("#filterInput").val();
    $.post(
        "/SINOFAITH/aj/filterJyjlBySpmc",
        {aj:aj,filterInput:filterInput},
        function(data){
            alertify.set('notifier','position', 'top-center');
            if(data==200){
                alertify.success("分析完成..正在跳转..");
                setTimeout(function (){document.getElementById("seachDetail").submit()},1500);
            }else{
                alertify.set('notifier','delay', 0);
                alertify.error("无"+filterInput+"分析结果,请重新输入");
            }
        }
    );
    alertify.set('notifier','position', 'top-center');
    alertify.set('notifier','delay', 0);
    alertify.success("数据分析中,请等待跳转...");
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
        $.ajax({url:"/SINOFAITH/zfbJyjlTjjgs/removeDesc"});
    });
});
