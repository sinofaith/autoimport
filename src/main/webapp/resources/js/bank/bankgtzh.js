function hiddenZfbCft() {
    var flg = 0

    if($("#checkbox1").is(":checked")){
        flg=1
    }
    location.href= "/SINOFAITH/bankgtzh/hiddenZfbCft?code="+flg;
}

function getZzGtlxr(obj) {
    var dfkh = $(obj).closest("tr").find("td:eq(3)").text()
    window.page = 1
    var tbody = window.document.getElementById("result1")
    var url = "/SINOFAITH/bankgtzh/getDetails"
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            dfzh:dfkh,
            order:'czzje',
            page:page
        },
        success:function (msg) {
            var data = msg.list
            var str = ""
            for (i in data) {
                if (i % 2 == 0) {
                    str += "<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                } else {
                    str += "<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                }
                str += "<td width=\"5%\">" + data[i].id + "</td>" +
                    "<td width=\"12%\">" + data[i].jyzh + "</td>" +
                    "<td width=\"5%\">" + data[i].name + "</td>" +
                    "<td width=\"12%\">" + data[i].dfzh + "</td>" +
                    "<td width=\"5%\" title=\""+data[i].dfxm+"\"> <div style=\"width:70px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].dfxm+"</div></td>"+
                    "<td width=\"7%\">" + data[i].jyzcs + "</td>" +
                    "<td width=\"7%\">" + data[i].jzzcs + "</td>" +
                    "<td width=\"10%\">" + data[i].jzzje + "</td>" +
                    "<td width=\"7%\">" + data[i].czzcs + "</td>" +
                    "<td width=\"10%\">" + data[i].czzje + "</td>" +
                    "<td width=\"4%\"><button  data-toggle=\"modal\" data-target=\"#myModal\" onclick=\"getZzDetails(this,'tjjgs')\">详情</button></td>"+
                    "</tr>";
            }
            tbody.innerHTML = str
            $("#dfkh1").attr("value", dfkh);
            $("#allRow1").attr("value", msg.totalRecords)
        }
    })
}

function getZzGtlxrByorder(filter) {
    var tbody = window.document.getElementById("result1")
    if(tbody!=null) {
        tbody.innerHTML = ""
    }
    var dfkh = $("#dfkh1").val();
    window.page = 1
    var url = "/SINOFAITH/bankgtzh/getDetails"
    $.ajax({
        type:"post",
        dataType:"json",
        url:url,
        data:{
            dfzh:dfkh,
            order:filter,
            page:page
        },
        success:function (msg) {
            var data = msg.list
            var str = ""
            for (i in data) {
                if (i % 2 == 0) {
                    str += "<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                } else {
                    str += "<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                }
                str += "<td width=\"5%\">" + data[i].id + "</td>" +
                    "<td width=\"12%\">" + data[i].jyzh + "</td>" +
                    "<td width=\"5%\">" + data[i].name + "</td>" +
                    "<td width=\"12%\">" + data[i].dfzh + "</td>" +
                    "<td width=\"5%\" title=\""+data[i].dfxm+"\"> <div style=\"width:70px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].dfxm+"</div></td>"+
                    "<td width=\"7%\">" + data[i].jyzcs + "</td>" +
                    "<td width=\"7%\">" + data[i].jzzcs + "</td>" +
                    "<td width=\"10%\">" + data[i].jzzje + "</td>" +
                    "<td width=\"7%\">" + data[i].czzcs + "</td>" +
                    "<td width=\"10%\">" + data[i].czzje + "</td>" +
                    "<td width=\"4%\"><button  data-toggle=\"modal\" data-target=\"#myModal\" onclick=\"getZzDetails(this,'tjjgs')\">详情</button></td>"+
                    "</tr>";
            }
            tbody.innerHTML = str
            $("#dfkh1").attr("value", dfkh);
            $("#allRow1").attr("value", msg.totalRecords)
        }
    })
}