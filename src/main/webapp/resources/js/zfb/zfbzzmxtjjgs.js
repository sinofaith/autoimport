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

// 转账明细显示详情数据
function getZfbZzxxTjjgsDetails(obj){
    // 用户Id
    var zfbzh = $(obj).closest("tr").find("td:eq(1)").text();
    // 转账产品名称
    var dfzh = $(obj).closest("tr").find("td:eq(3)").text();
    window.page = 1;
    var tbody = window.document.getElementById("result");
    var url = "/SINOFAITH/zfbZzmxTjjgs/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            zfbzh:zfbzh,
            dfzh:dfzh,
            order:'dzsj',
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list;
            insert(data,tbody,zfbzh,true);
            $("#zfbzh").attr("value",zfbzh);
            $("#dfzh").attr("value",dfzh);
            $("#allRow").attr("value",msg.totalRecords);
        }
    })
}

// 转账明细排序
function orderByFilter(filter){
    var tbody = window.document.getElementById("result");
    var zfbzh = $("#zfbzh").val();
    var dfzh = $("#dfzh").val();
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    window.page = 1;
    var url = "/SINOFAITH/zfbZzmxTjjgs/getDetails";
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            zfbzh:zfbzh,
            dfzh:dfzh,
            order:filter,
            page:parseInt(page)
        },
        success:function (msg) {
            var data = msg.list;
            insert(data,tbody,zfbzh,true);
            $("#zfbzh").attr("value",zfbzh);
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
    var dfzh = $("#dfzh").val();
    var allRow = $("#allRow").val();
    var scrollT = parseFloat(tbody.scrollTop) + parseFloat(tbody.clientHeight)
    var scrollH = parseFloat(tbody.scrollHeight)
    if (1 >= scrollH - scrollT && tbody.scrollTop != 0 && tbody.childNodes.length < allRow) {
        if (is_running == false) {
            is_running = true;
            window.page = page += 1;
            var url = "/SINOFAITH/zfbZzmxTjjgs/getDetails";
            $.ajax({
                type:"post",
                dataType:"json",
                url:url,
                data:{
                    zfbzh:zfbzh,
                    dfzh:dfzh,
                    order:"xxx",
                    page:parseInt(window.page)
                },
                success:function (msg) {
                    var data = msg.list;
                    insert(data,tbody,zfbzh,false);
                    $("#zfbzh").attr("value",zfbzh);
                    $("#dfzh").attr("value",dfzh);
                    $("#allRow").attr("value",msg.totalRecords);
                    is_running = false;
                }
            })
        }
    }
}

// 转账明细插入表记录
function insert(data,tbody,zfbzh,temp){
    var str = "";
    for (i in data){
        if(i%2==0){
            str+="<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
        }else{
            str+="<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
        }
        str+="<td width=\"3%\">"+data[i].id+"</td>"+
            "<td width=\"14%\" title='"+data[i].jyh+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].jyh+"</div></td>"+
            "<td width=\"10%\" "+(data[i].fkfzfbzh==zfbzh?"style=color:red":"")+" title='"+data[i].fkfzfbzh+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].fkfzfbzh!=null?data[i].fkfzfbzh:"")+"</div></td>"+
            "<td width=\"8%\">"+data[i].zzcpmc+"</td>"+
            "<td width=\"10%\" "+(data[i].skfzfbzh==zfbzh?"style=color:red":"")+" title='"+data[i].skfzfbzh+"'>"+
            "<div style=\"width: 100%;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+(data[i].skfzfbzh!=null?data[i].skfzfbzh:"")+"</div></td>"+
            "<td width=\"5%\">"+(data[i].skjgxx!=null?data[i].skjgxx:"")+"</td>"+
            "<td width=\"7%\">"+data[i].dzsj+"</td>"+
            "<td width=\"4%\">"+data[i].zzje+"</td>"+
            "<td width=\"12%\">"+(data[i].txlsh!=null?data[i].txlsh:"")+"</td>"+
            "</tr>";
    }
    if(temp){
        tbody.innerHTML = str;
    }else{
        tbody.innerHTML += str;
    }
}

var isFlag = true;
function filterJyjlByspmc(aj){
    var filterInput = $("#filterInput").val();
    if(isFlag) {
        isFlag = false;
        $.post(
            "/SINOFAITH/aj/filterJyjlBySpmc",
            {aj: aj, filterInput: filterInput},
            function (data) {
                alertify.set('notifier', 'position', 'top-center');
                if (data == 200) {
                    alertify.success("分析完成..正在跳转..");
                    setTimeout(function () {
                        document.getElementById("seachDetail").submit()
                    }, 1500);
                } else {
                    alertify.set('notifier', 'delay', 0);
                    alertify.error("无" + filterInput + "分析结果,请重新输入");
                    isFlag = true;
                }
            }
        );
        alertify.set('notifier', 'position', 'top-center');
        alertify.set('notifier', 'delay', 0);
        alertify.success("数据分析中,请等待跳转...");
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
        $.ajax({url:"/SINOFAITH/zfbZzmxTjjgs/removeDesc"});
    });
});

// 阀值设置
function seachChange() {
    var seachCondition = $("#seachCondition").val();
    if(seachCondition === "fkzje" || seachCondition === "skzje"){
        $("#seachCode").val("50000")
    }else{
        $("#seachCode").val("")
    }
}

function isNum(obj){
    var seachCondition = $("#seachCondition").val();
    if(seachCondition === "fkzje" || seachCondition === "skzje"){
        obj.value=obj.value.replace(/[^\d]/g,'')
    }
}

// 数据导出
function downDetailInfo(){
    var zfbzh = $("#zfbzh").val();
    var dfzh = $("#dfzh").val();
    location = "/SINOFAITH/zfbZzmxTjjgs/downDetailInfo?zfbzh="+zfbzh+'&dfzh='+dfzh;
}