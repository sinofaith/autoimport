// 交易记录地址显示详情数据
function getJyjlSjdzsDetails(obj){
    // 用户Id
    var mjyhid = $(obj).closest("tr").find("td:eq(1)").text();
    window.page = 1;
    var tbody = window.document.getElementById("result");
    var url = "/SINOFAITH/zfbJyjlSjdzs/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            mjyhid:mjyhid,
            order:'sjcs',
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list;
            insert(data,tbody,true);
            $("#mjyhid").attr("value",mjyhid);
            $("#allRow").attr("value",msg.totalRecords);
        }
    })
}

// 交易记录地址排序
function orderByJyjlSjdzsFilter(filter){
    var tbody = window.document.getElementById("result");
    var mjyhid = $("#mjyhid").val();
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    window.page = 1;
    var url = "/SINOFAITH/zfbJyjlSjdzs/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            mjyhid:mjyhid,
            order:filter,
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list;
            insert(data,tbody,true);
            $("#mjyhid").attr("value",mjyhid);
            $("#allRow").attr("value",msg.totalRecords);
        }
    })
}

// 交易记录地址滚动条加载数据
var is_running = false;
function scrollFJyjlSjdzs() {
    var tbody = window.document.getElementById("result");
    var mjyhid = $("#mjyhid").val();
    var allRow = $("#allRow").val();
    var scrollT = parseFloat(tbody.scrollTop) + parseFloat(tbody.clientHeight)
    var scrollH = parseFloat(tbody.scrollHeight)
    if (1 >= scrollH - scrollT && tbody.scrollTop != 0 && tbody.childNodes.length < allRow) {
        if (is_running == false) {
            is_running = true;
            window.page = page += 1;
            var url = "/SINOFAITH/zfbJyjlSjdzs/getDetails";
            $.ajax({
                type:"post",
                dataType:"json",
                url:url,
                data:{
                    mjyhid:mjyhid,
                    order:"xxx",
                    page:parseInt(window.page)
                },
                success:function (msg) {
                    var data = msg.list;
                    insert(data,tbody,false);
                    $("#mjyhid").attr("value",mjyhid);
                    $("#allRow").attr("value",msg.totalRecords);
                    is_running = false;
                }
            })
        }
    }
}


// 交易记录地址插入表记录
function insert(data,tbody,temp){
    var str = "";
    for (i in data){
        if(i%2==0){
            str+="<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
        }else{
            str+="<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
        }
        str+="<td width=\"5%\">"+data[i].id+"</td>"+
            "<td width=\"7%\">"+data[i].mjyhid+"</td>"+
            "<td width=\"9%\">"+data[i].mjxx+"</td>"+
            "<td width=\"20%\" title='"+data[i].shrdz+"'>"+
                "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].shrdz+"</div>" +
            "</td>"+
            "<td width=\"6%\">"+data[i].sjcs+"</td>"+
            "<td width=\"6%\">"+data[i].czje+"</td>"+
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
        $.ajax({url:"/SINOFAITH/zfbJyjlSjdzs/removeDesc"});
    });
});

// 数据导出
function downJyjlSjdzsDetailInfo(){
    var mjyhid = $("#mjyhid").val();
    location = "/SINOFAITH/zfbJyjlSjdzs/downDetailInfo?mjyhid="+mjyhid;
}