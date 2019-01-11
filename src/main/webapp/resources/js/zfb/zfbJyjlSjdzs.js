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
            "<td width=\"6%\"><button  data-toggle=\"modal\" data-target=\"#myModal1\"" +
            " onclick=\"getJyjlSingleDetails(this)\">"+data[i].sjcs+"</button></td>"+
            "<td width=\"6%\">"+data[i].czje+"</td>"+
            "</tr>";
    }
    if(temp){
        tbody.innerHTML = str;
    }else{
        tbody.innerHTML += str;
    }
}

// 单个收件地址数据展示
function getJyjlSingleDetails(obj){
    // 用户Id
    var mjyhid = $(obj).closest("tr").find("td:eq(1)").text();
    // 地址信息
    var shrdz = $(obj).closest("tr").find("td:eq(3)").text();
    var tbody = window.document.getElementById("result1");
    var url = "/SINOFAITH/zfbJyjlSjdzs/getSingleDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            mjyhid:mjyhid,
            shrdz:shrdz,
            order:'jyje',
            page:parseInt(page1)
        },
        success:function (msg) {
            var data = msg.list;
            insert1(data,tbody,true);
            $("#mjyhid1").attr("value",mjyhid);
            $("#shrdz1").attr("value",shrdz);
            $("#allRow1").attr("value",msg.totalRecords);
        }
    })
}

// 交易记录单个地址排序
function orderByFilter1(filter){
    var tbody = window.document.getElementById("result1");
    var mjyhid = $("#mjyhid1").val();
    var shrdz = $("#shrdz1").val();
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    window.page1 = 1;
    var url = "/SINOFAITH/zfbJyjlSjdzs/getSingleDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            mjyhid:mjyhid,
            shrdz:shrdz,
            order:filter,
            page:parseInt(page1)
        },
        success:function (msg) {
            var data = msg.list;
            insert1(data,tbody,true);
            $("#mjyhid1").attr("value",mjyhid);
            $("#shrdz1").attr("value",shrdz);
            $("#allRow1").attr("value",msg.totalRecords);
        }
    })
}

// 单个地址滚动条加载数据
var is_running1 = false;
function scrollF1() {
    var tbody = window.document.getElementById("result1");
    var mjyhid = $("#mjyhid1").val();
    var shrdz = $("#shrdz1").val();
    var allRow = $("#allRow1").val();
    var scrollT = parseFloat(tbody.scrollTop) + parseFloat(tbody.clientHeight);
    var scrollH = parseFloat(tbody.scrollHeight);
    if (1 >= scrollH - scrollT && tbody.scrollTop != 0 && tbody.childNodes.length < allRow) {
        if (is_running1 == false) {
            is_running1 = true;
            window.page1 = page1 += 1;
            var url = "/SINOFAITH/zfbJyjlSjdzs/getSingleDetails";
            $.ajax({
                type:"post",
                dataType:"json",
                url:url,
                data:{
                    mjyhid:mjyhid,
                    shrdz:shrdz,
                    order:"xxx",
                    page:parseInt(window.page1)
                },
                success:function (msg) {
                    var data = msg.list;
                    insert1(data,tbody,false);
                    $("#mjyhid1").attr("value",mjyhid);
                    $("#shrdz1").attr("value",shrdz);
                    $("#allRow1").attr("value",msg.totalRecords);
                    is_running1 = false;
                }
            })
        }
    }
}

// 单个地址插入表记录
function insert1(data,tbody,temp){
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
            "<td width=\"12%\">"+(data[i].sksj!=null?data[i].sksj:"")+"</td>"+
            "<td width=\"12%\" title='"+data[i].spmc+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].spmc+"</div></td>"+
            "<td width=\"8%\" title='"+(data[i].shrdz!=null?data[i].shrdz:"")+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].shrdz!=null?data[i].shrdz:"")+"</div></td>"+
            "</tr>";
    }
    if(temp){
        tbody.innerHTML = str;
    }else{
        tbody.innerHTML += str;
    }
}

var isFlag = true;
// 条件筛选
function filterJyjlByspmc(aj){
    var filterInput = $("#filterInput").val();
    if(isFlag){
        isFlag = false;
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
                    isFlag = true;
                }
            }
        );
        alertify.set('notifier','position', 'top-center');
        alertify.set('notifier','delay', 0);
        alertify.success("数据分析中,请等待跳转...");
    }
}

// 每次点击将数据清空
var page = 1;
var page1 = 1;
$(function () {
    $('#myModal').on('hide.bs.modal', function () {
        var tbody = window.document.getElementById("result");
        page = 1;
        if(tbody!=null) {
            tbody.innerHTML = "";
        }
        $.ajax({url:"/SINOFAITH/zfbJyjlSjdzs/removeDesc"});
    });
    $('#myModal1').on('hide.bs.modal', function () {
        var tbody = window.document.getElementById("result1");
        page1 = 1;
        if(tbody!=null) {
            tbody.innerHTML = "";
        }
        $.ajax({url:"/SINOFAITH/zfbJyjlSjdzs/removeDesc"});
    });
});

// 详情数据导出
function downJyjlSjdzsDetailInfo(){
    var mjyhid = $("#mjyhid").val();
    location = "/SINOFAITH/zfbJyjlSjdzs/downDetailInfo?mjyhid="+mjyhid;
}

// 单个地址详情数据导出
function downDetailInfo1(){
    var mjyhid = $("#mjyhid").val();
    var shrdz = $("#shrdz1").val();
    location = "/SINOFAITH/zfbJyjlSjdzs/downDetailInfo1?mjyhid="+mjyhid+"&shrdz="+shrdz;
}