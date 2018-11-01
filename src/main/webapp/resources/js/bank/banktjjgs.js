function getBanktjjgs(obj,type) {
    var tbody = window.document.getElementById("result2");
    var zh = "";
    if(type==="dfzh"){
        zh = $(obj).closest("tr").find("td:eq(3)").text();
        var url = "/SINOFAITH/banktjjgs/getSeach";
        $.ajax({
            type:"post",
            dataType:"json",
            url:url,
            data:{
                zh:zh,
                type:type
            },
            success:function (msg) {
                var data = msg.list;
                var str = "";
                for (i in data) {
                    if (i % 2 == 0) {
                        str += "<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                    } else {
                        str += "<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                    }
                    str += "<td width=\"4%\">" + data[i].id + "</td>" +
                        "<td width=\"5%\">" + data[i].jyzcs + "</td>" +
                        "<td width=\"13%\">" + data[i].jyzh + "</td>" +
                        "<td width=\"7%\" title=\""+data[i].name+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].name+"</div></td>"+
                        "<td width=\"8%\">" + data[i].czzcs + "</td>" +
                        "<td width=\"8%\">" + data[i].czzje + "</td>" +
                        "<td width=\"13%\">" + data[i].dfzh + "</td>" +
                        "<td width=\"7%\" title=\""+data[i].dfxm+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].dfxm+"</div></td>"+
                        "<td width=\"8%\">" + data[i].jzzcs + "</td>" +
                        "<td width=\"8%\">" + data[i].jzzje + "</td>" +
                        "<td width=\"13%\">" + data[i].jyzh + "</td>" +
                        "<td width=\"7%\" title=\""+data[i].name+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].name+"</div></td>"+
                        "</tr>";
                }
                tbody.innerHTML = str
            }
        })
    }
    if(type==="jyzh"){
        zh = $(obj).closest("tr").find("td:eq(1)").text()
        var url = "/SINOFAITH/banktjjgs/getSeach";
        $.ajax({
            type:"post",
            dataType:"json",
            url:url,
            data:{
                zh:zh,
                type:type
            },
            success:function (msg) {
                var data = msg.list;
                var str = "";
                for (i in data) {
                    if (i % 2 == 0) {
                        str += "<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                    } else {
                        str += "<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                    }
                    str += "<td width=\"4%\">" + data[i].id + "</td>" +
                        "<td width=\"5%\">" + data[i].jyzcs + "</td>" +
                        "<td width=\"13%\">" + data[i].dfzh + "</td>" +
                        "<td width=\"7%\" title=\""+data[i].dfxm+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].dfxm+"</div></td>"+
                        "<td width=\"8%\">" + data[i].jzzcs + "</td>" +
                        "<td width=\"8%\">" + data[i].jzzje + "</td>" +
                        "<td width=\"13%\">" + data[i].jyzh + "</td>" +
                        "<td width=\"7%\" title=\""+data[i].name+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].name+"</div></td>"+
                        "<td width=\"8%\">" + data[i].czzcs + "</td>" +
                        "<td width=\"8%\">" + data[i].czzje + "</td>" +
                        "<td width=\"13%\">" + data[i].dfzh + "</td>" +
                        "<td width=\"7%\" title=\""+data[i].dfxm+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].dfxm+"</div></td>"+
                        "</tr>";
                }
                tbody.innerHTML = str
            }
        })
    }
}

var radio = -1;
$(function () { $('#myModal2').on('hide.bs.modal', function () {

    var tbody = window.document.getElementById("result2")
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    // $.ajax({url:"/SINOFAITH/bankzzxx/removeDesc"})

})

    var old = null; //用来保存原来的对象
    $(":radio").each(function(){
        if(this.checked){
            old = this; //如果当前对象选中，保存该对象
        }
        this.onclick = function(){
            if(this == old){//如果点击的对象原来是选中的，取消选中
                this.checked = false;
                old = null;
                location.href="/SINOFAITH/banktjjgs/getByZhzt?code="+-1;
            } else{
                old = this;
                location.href="/SINOFAITH/banktjjgs/getByZhzt?code="+$(this).val();
            }
        }
        // location.href="/SINOFAITH/banktjjgs/getByZhzt?code="+radio;
    });

    $(":radio").click(function(){
        alert(this.val());

        // location.href="/SINOFAITH/banktjjgs/getByZhzt?code="+radio;
    });
});