$(function () {
    var old = null; //用来保存原来的对象
    $(":radio").each(function(){
        if(this.checked){
            old = this; //如果当前对象选中，保存该对象
        }
        this.onclick = function(){
            if(this == old){//如果点击的对象原来是选中的，取消选中
                this.checked = false;
                old = null;
                location.href="/SINOFAITH/banktjjg/getByZhzt?code="+-1;
            } else{
                old = this;
                location.href="/SINOFAITH/banktjjg/getByZhzt?code="+$(this).val();
            }
        }
    });
    $("#start_time").datetimepicker(
        {
            language: 'zh-CN',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            clearBtn: true,
            todayHighlight: 1,
            startView: 2,
            minView: 2,
            forceParse: 0,
        }).on('changeDate', function (ev) {
        if (ev.date) {
            $("#end_time").datetimepicker('setStartDate', new Date(ev.date.valueOf()))
        } else {
            $("#end_time").datetimepicker('setStartDate', null);
        }
    });

    $("#end_time").datetimepicker(
        {
            language: 'zh-CN',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            clearBtn: true,
            todayHighlight: 1,
            startView: 2,
            minView: 2,
            forceParse: 0,
        }).on('changeDate', function (ev) {
        if (ev.date) {
            $("#start_time").datetimepicker('setEndDate', new Date(ev.date.valueOf()))
        } else {
            $("#start_time").datetimepicker('setEndDate', new Date());
        }

    });
    // $(":radio").click(function(){
    //     alert(this.val());
    // });
    $.ajaxSetup({
        complete : function(xhr, status) {
            //拦截器实现超时跳转到登录页面
            // 通过xhr取得响应头
            var REDIRECT = xhr.getResponseHeader("REDIRECT");
            //如果响应头中包含 REDIRECT 则说明是拦截器返回的
            if (REDIRECT == "REDIRECT")
            {
                var win = window;
                while (win != win.top)
                {
                    win = win.top;
                }
                //重新跳转到 login.html
                win.location.href = xhr.getResponseHeader("CONTEXTPATH");
            }
        }
    });

    // $(":radio").click(function(){
    //     alert(this.val());
    // });
    $.ajaxSetup({
        complete : function(xhr, status) {
            //拦截器实现超时跳转到登录页面
            // 通过xhr取得响应头
            var REDIRECT = xhr.getResponseHeader("REDIRECT");
            //如果响应头中包含 REDIRECT 则说明是拦截器返回的
            if (REDIRECT == "REDIRECT")
            {
                var win = window;
                while (win != win.top)
                {
                    win = win.top;
                }
                //重新跳转到 login.html
                win.location.href = xhr.getResponseHeader("CONTEXTPATH");
            }
        }
    });
});

function hiddenZfbCft() {
    var flg = 0

    if($("#checkbox1").is(":checked")){
        flg=1
    }
    location.href= "/SINOFAITH/banktjjg/hiddenZfbCft?code="+flg;
}


function getZhxxByFilter() {
    var tbody = window.document.getElementById("result1");
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    var czje = $("#czje").val();
    var jzje =  $("#jzje").val();
    if(czje==0||czje===''){
        czje=50000;
    }
    if(jzje==0||jzje===''){
        jzje=50000;
    }
    window.page = 1
    var url = "/SINOFAITH/banktjjg/getZhxx"
    $.ajax({
        type:"get",
        dataType:"json",
        url:url,
        data:{
            czje:czje,
            jzje:jzje,
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
                str+="<td width=\"4%\">"+data[i].id+"</td>"+
                    "<td width=\"13%\">"+data[i].jyzh+"</td>"+
                    "<td width=\"12%\">"+data[i].name+"</td>"+
                    "<td width=\"10%\">"+data[i].jyzcs+"</td>"+
                    "<td width=\"7%\">"+data[i].jzzcs+"</td>"+
                    "<td width=\"7%\">"+data[i].jzzje+"</td>"+
                    "<td width=\"7%\">"+data[i].czzcs+"</td>"+
                    "<td width=\"7%\">"+data[i].czzje+"</td>"+
                    "<td width=\"7%\"> "+data[i].zhlb+"</td>"+
                    "</tr>";
            }
            tbody.innerHTML = str
            $("#allRow1").attr("value",msg.totalRecords);
            $("#test").text(msg.totalRecords)
            // title.innerText ="<"+jyzh+","+jylx+">"
        }
    })

    $("#czje").attr("value",czje);
    $("#jzje").attr("value",jzje);
}

function countBysj() {
    var minsj=$("#start_time").val();
    var maxsj=$("#end_time").val();
    var url = "/SINOFAITH/banktjjg/countBysj?minsj="+minsj+"&maxsj="+maxsj;

    var tx="";
    if(minsj.length<1 && maxsj<1){
        tx = "确认将分析条件清空?";
    }else if(minsj.length>1 && maxsj.length<1){
        tx = "确认将分析条件改为"+minsj+"至今的数据?";
    }else if(minsj.length<1 && maxsj.length>1){
        tx = "确认将分析条件改为"+maxsj+"以前的数据?";
    }else {
        tx = "确认将分析条件改为"+minsj+"至"+maxsj+"之间的数据?";
    }
    alertify.confirm(tx,function () {
        alertify.set('notifier','delay', 0);
        alertify.set('notifier','position', 'top-center');
        alertify.success("数据分析中,请等待跳转...");
        $.get(url,function (data) {
            if(data==201){
                alertify.success("分析条件未改变");
                setTimeout(function (){document.getElementById("seachDetail").submit()},2000);
            }
            if(data==202){
                alertify.success("分析条件区间数据条数为0");
                setTimeout(function (){document.getElementById("seachDetail").submit()},2000);
            }
            if(data==200){
                alertify.success("分析完成..正在跳转..");
                setTimeout(function (){document.getElementById("seachDetail").submit()},1500);
            }
        })
    }, function() {
        // 用户点击"cancel"按钮
        return
    });

}

function clearTime() {
    $("#start_time").val("");
    $("#end_time").val("");
}


var page = 1
var is_running = false
function getZhxx() {
    var tbody = window.document.getElementById("result1");
    var czje = $("#czje").val();
    var jzje =  $("#jzje").val();
    if(czje==0||czje===''){
        czje=50000;
    }
    if(jzje==0||jzje===''){
        jzje=50000;
    }
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    window.page = 1
    var url = "/SINOFAITH/banktjjg/getZhxx"
    $.ajax({
        type:"get",
        dataType:"json",
        url:url,
        data:{
            czje:czje,
            jzje:jzje,
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
                str+="<td width=\"4%\">"+data[i].id+"</td>"+
                    "<td width=\"13%\">"+data[i].jyzh+"</td>"+
                    "<td width=\"12%\">"+data[i].name+"</td>"+
                    "<td width=\"10%\">"+data[i].jyzcs+"</td>"+
                    "<td width=\"7%\">"+data[i].jzzcs+"</td>"+
                    "<td width=\"7%\">"+data[i].jzzje+"</td>"+
                    "<td width=\"7%\">"+data[i].czzcs+"</td>"+
                    "<td width=\"7%\">"+data[i].czzje+"</td>"+
                    "<td width=\"7%\"> "+data[i].zhlb+"</td>"+
                    "</tr>";
            }
            tbody.innerHTML = str
            $("#allRow1").attr("value",msg.totalRecords);
            $("#test").text(msg.totalRecords)
            // title.innerText ="<"+jyzh+","+jylx+">"
        }
    })

    $("#czje").attr("value",czje);
    $("#jzje").attr("value",jzje);
}

function scrollD() {
    var tbody = window.document.getElementById("result1");
    var czje = $("#czje").val();
    var jzje =  $("#jzje").val();
    var allRow = $("#allRow1").val()
    var scrollT = parseFloat(tbody.scrollTop) + parseFloat(tbody.clientHeight)
    var scrollH = parseFloat(tbody.scrollHeight)
    if (1 >= scrollH - scrollT && tbody.scrollTop != 0 && tbody.childNodes.length < allRow) {
        if(is_running==false) {
            is_running = true
            window.page = page += 1
            var url = "/SINOFAITH/banktjjg/getZhxx"
            $.ajax({
                type:"get",
                dataType:"json",
                url:url,
                data:{
                    czje:czje,
                    jzje:jzje,
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
                        str+="<td width=\"4%\">"+data[i].id+"</td>"+
                            "<td width=\"13%\">"+data[i].jyzh+"</td>"+
                            "<td width=\"12%\">"+data[i].name+"</td>"+
                            "<td width=\"10%\">"+data[i].jyzcs+"</td>"+
                            "<td width=\"7%\">"+data[i].jzzcs+"</td>"+
                            "<td width=\"7%\">"+data[i].jzzje+"</td>"+
                            "<td width=\"7%\">"+data[i].czzcs+"</td>"+
                            "<td width=\"7%\">"+data[i].czzje+"</td>"+
                            "<td width=\"7%\"> "+data[i].zhlb+"</td>"+
                            "</tr>";
                    }
                    $("#result1").append(str)
                    $("#allRow1").attr("value",msg.totalRecords);
                    $("#test").text(msg.totalRecords)
                    is_running = false
                }
            })
            $("#czje").attr("value",czje);
            $("#jzje").attr("value",jzje);
        }
    }
}
function destroyTooltip() {
    $("#wstitle").tooltip('destroy');
}


function downWs(){
    var czje = $("#czje").val();
    var jzje =  $("#jzje").val();
    var wstitle = $("#wstitle").val();
    if(wstitle==''){
        $("#wstitle").attr('title',"请填写文书抬头 例:xxx公安局").tooltip('show');
        return
    }
    location="/SINOFAITH/banktjjg/downWs?czje="+czje+"&jzje="+jzje+"&wstitle="+wstitle
}