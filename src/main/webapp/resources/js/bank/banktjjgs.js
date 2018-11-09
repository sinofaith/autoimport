function getBanktjjgs(obj,type) {
    var zh = "";
    var list = "";
    var result ="";
    if(type==="dfzh") {
        zh = $(obj).closest("tr").find("td:eq(3)").text();
    }
    if(type==="jyzh"){
        zh = $(obj).closest("tr").find("td:eq(1)").text()
    }
    var url = "/SINOFAITH/banktjjgs/getSeach";
    $.ajax({
        type:"post",
        dataType:"json",
        async:false,
        url:url,
        data:{
            zh:zh,
            type:type
        },
        success:function (msg) {
            list = msg.list;
            result = msg.result;
        }
     });
    var myChart = echarts.init(document.getElementById('main'));
    var nodess = [];
    var linkss = [];
    if(zh.indexOf("-")<0) {
        nodess.push({
            id: 0,
            name: result.jyzh + '\n' + result.name,
            des: '交易总次数:' + result.jyzcs + '</br>进账总次数:' + result.jzzcs + '</br>进账总金额:' + result.jzzje + '</br>出账总次数:' + result.czzcs + '</br>出账总金额:' + result.czzje + ' ',
            itemStyle: {
                normal: {
                    color: '#e5323e'
                }
            },
            symbolSize: 60,
            label: {
                normal: {
                    show: true
                }
            }
        });
    }else{
        nodess.push({
            id: 0,
            name: list[0].dfzh + '\n' + list[0].dfxm,
            des: '交易总次数:' + list[0].jyzcs + '</br>进账总次数:' + list[0].czzcs + '</br>进账总金额:' + list[0].czzje + '</br>出账总次数:' + list[0].jzzcs + '</br>出账总金额:' + list[0].jzzje + ' ',
            itemStyle: {
                normal: {
                    color: '#e5323e'
                }
            },
            symbolSize: 60,
            label: {
                normal: {
                    show: true
                }
            }
        });
    }
    if(type==="jyzh") {
        for (i in list) {
            if(list[i].jyzcs>=5&&(list[i].jzzje>50000||list[i].czzje>50000)) {
                nodess.push({
                    id: list[i].id,
                    name: list[i].dfzh + '\n' + list[i].dfxm,
                    des: '交易总次数:' + list[i].jyzcs + '</br>进账总次数:' + list[i].jzzcs + '</br>进账总金额:' + list[i].jzzje + '</br>出账总次数:' + list[i].czzcs + '</br>出账总金额:' + list[i].czzje + ' ',
                    itemStyle: {
                        normal: {
                            color: '#4cabce'
                        }
                    },
                    symbolSize: 30,
                    label: {
                        normal: {
                            show: list[i].jyzcs >= 5 && (list[i].jzzje > 50000 || list[i].czzje > 50000)
                        }
                    }
                });
            }
            if(list[i].jzzcs>0) {
                linkss.push({
                    source: list[i].id,
                    target: 0,
                    name: "流入总次数:" + list[i].jzzcs + "\n流入总金额:" + list[i].jzzje + " ",
                    des: "流入总次数:" + list[i].jzzcs + "<br>流入总金额:" + list[i].jzzje + " ",
                    label: {
                        normal: {
                            // show:list[i].jzzje>50000
                            show:true
                        }
                    }
                });
            }
            if(list[i].czzcs>0){
                linkss.push({
                    source: 0,
                    target: list[i].id,
                    name: "流出总次数:" + list[i].czzcs + "\n流出总金额:" + list[i].czzje + " ",
                    des: "流出总次数:" + list[i].czzcs + "<br>流出总金额:" + list[i].czzje + " ",
                    label: {
                        normal: {
                            // show:list[i].czzje>50000
                            show:true
                        }
                    }
                });
            }
            if(i>=100){
                break;
            }
        }
    }
    if(type==="dfzh") {
        for (i in list) {
                nodess.push({
                id:list[i].id,
                name: list[i].jyzh + '\n' + list[i].name,
                des: '交易总次数:' + list[i].jyzcs + '</br>进账总次数:' + list[i].jzzcs + '</br>进账总金额:' + list[i].jzzje + '</br>出账总次数:' + list[i].czzcs + '</br>出账总金额:' + list[i].czzje + ' ',
                itemStyle: {
                    normal: {
                        color: '#4cabce'
                    }
                },
                symbolSize: 30,
                label:{
                    normal:{
                        show:list[i].jyzcs>=5&&(list[i].jzzje>50000||list[i].czzje>50000)
                    }
                }
            });
            if(list[i].jzzcs>0) {
                linkss.push({
                    source: 0,
                    target: list[i].id,
                    name: "流出总次数:" + list[i].jzzcs + "\n流出总金额:" + list[i].jzzje + " ",
                    des: "流出总次数:" + list[i].jzzcs + "<br>流出总金额:" + list[i].jzzje + " ",
                    label: {
                        normal: {
                            // show:list[i].jzzje>50000
                            show:true
                        }
                    }
                });
            }
            if(list[i].czzcs>0){
                linkss.push({
                    source: list[i].id,
                    target: 0,
                    name: "流入总次数:" + list[i].czzcs + "\n流入总金额:" + list[i].czzje + " ",
                    des: "流入总次数:" + list[i].czzcs + "<br>流入总金额:" + list[i].czzje + " ",
                    label: {
                        normal: {
                            // show:list[i].czzje>50000
                            show:true
                        }
                    }
                });
            }
            if(i>=100){
                break;
            }
        }
    }
    option = {
        title: { text: '银行卡账户分析图谱',x:'center' },
        tooltip: {
            formatter: function (x) {
                return x.data.des;
            }
        },
        toolbox: {
            show: true,
            feature: {
                dataView:{
                    readOnly: true,
                    optionToContent: function dataView(opt) {
                        var table = '<table id="result3" class="table  table-hover table_style table_list1 " style="border-left: 1px solid #ccc; border-right: 1px solid #ccc!important;width:100%;">'
                            +'<thead style="display:table;width:100%;table-layout:fixed;width: calc( 100% - 16.5px );">'
                            + '<tr align="center"><td width="4%">序号</td>'
                            + '<td width="4%" style="color: #4cabce">交易总次数</td>'
                            + '<td width="13%" style="color: #4cabce">流入账户</td>'
                            + '<td width="7%" style="color: #4cabce">流入户名</td>'
                            + '<td width="5%" style="color: #4cabce">流入总次数</td>'
                            + '<td width="8%" style="color: #4cabce">流入总金额</td>'
                            + '<td width="13%" style="color: #e5323e">交易账户</td>'
                            + '<td width="7%" style="color: #e5323e">交易户名</td>'
                            + '<td width="5%" style="color: #e5323e">交易总次数</td>'
                            + '<td width="5%" style="color: #e5323e">进账总次数</td>'
                            + '<td width="8%" style="color: #e5323e">进账总金额</td>'
                            + '<td width="5%" style="color: #e5323e">出账总次数</td>'
                            + '<td width="8%" style="color: #e5323e">出账总金额</td>'
                            + '<td width="13%" style="color: #4cabce">流出账户</td>'
                            + '<td width="7%" style="color: #4cabce">流出户名</td>'
                            + '<td width="5%" style="color: #4cabce">流出总次数</td>'
                            + '<td width="8%" style="color: #4cabce">流出总金额</td>'
                            + '</tr></thead>'
                            +'<tbody style="display:block;height:450px;overflow-y:scroll;">';
                        var series = opt.series;
                        for (i = 0; i < list.length; i++) {
                            if(type=="jyzh"){
                                if (i % 2 == 0) {
                                    table += "<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                                } else {
                                    table += "<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                                }
                                table += '<td width="4%">' + list[i]['id'] + '</td>'
                                    + '<td width="4%">' + list[i]['jyzcs'] + '</td>'
                                    + '<td width="13%">' + list[i]['dfzh'] + '</td>'
                                    + '<td width="7%">' + list[i]['dfxm'] + '</td>'
                                    + '<td width="5%">' + list[i]['jzzcs'] + '</td>'
                                    + '<td width="8%">' + list[i]['jzzje'] + '</td>'
                                    + '<td width="13%">' + result['jyzh'] + '</td>'
                                    + '<td width="7%">' + result['name'] + '</td>'
                                    + '<td width="5%">' + result['jyzcs'] + '</td>'
                                    + '<td width="5%">' + result['jzzcs'] + '</td>'
                                    + '<td width="8%">' + result['jzzje'] + '</td>'
                                    + '<td width="5%">' + result['czzcs'] + '</td>'
                                    + '<td width="8%">' + result['czzje'] + '</td>'
                                    + '<td width="13%">' + list[i]['dfzh'] + '</td>'
                                    + '<td width="7%">' + list[i]['dfxm'] + '</td>'
                                    + '<td width="5%">' + list[i]['czzcs'] + '</td>'
                                    + '<td width="8%">' + list[i]['czzje'] + '</td></tr>';
                            }
                            if(type=="dfzh"){

                                if (i % 2 == 0) {
                                    table += "<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
                                } else {
                                    table += "<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
                                }
                                table += '<td width="4%">' + list[i]['id'] + '</td>'
                                    + '<td width="4%">' + list[i]['jyzcs'] + '</td>'
                                    + '<td width="13%">' + list[i]['jyzh'] + '</td>'
                                    + '<td width="7%">' + list[i]['name'] + '</td>'
                                    + '<td width="5%">' + list[i]['czzcs'] + '</td>'
                                    + '<td width="8%">' + list[i]['czzje'] + '</td>';
                                    if(list[0]['dfzh'].indexOf('-')<0) {
                                        table +='<td width="13%">' + result['jyzh'] + '</td>'
                                        +'<td width="7%">' + result['name'] + '</td>'
                                        + '<td width="5%">' + result['jyzcs'] + '</td>'
                                        + '<td width="5%">' + result['jzzcs'] + '</td>'
                                        + '<td width="8%">' + result['jzzje'] + '</td>'
                                        + '<td width="5%">' + result['czzcs'] + '</td>'
                                        + '<td width="8%">' + result['czzje'] + '</td>'
                                    }else{
                                        table += '<td width="13%">' + list[i]['dfzh'] + '</td>'
                                            +'<td width="7%">' + list[i]['dfxm'] + '</td>'
                                            + '<td width="5%">' + list[i]['jyzcs'] + '</td>'
                                            + '<td width="5%">' + list[i]['czzcs'] + '</td>'
                                            + '<td width="8%">' + list[i]['czzje'] + '</td>'
                                            + '<td width="5%">' + list[i]['jzzcs'] + '</td>'
                                            + '<td width="8%">' + list[i]['jzzje'] + '</td>'
                                    }
                                    table += '<td width="13%">' + list[i]['jyzh'] + '</td>'
                                    + '<td width="7%">' + list[i]['name'] + '</td>'
                                    + '<td width="5%">' + list[i]['jzzcs'] + '</td>'
                                    + '<td width="8%">' + list[i]['jzzje'] + '</td></tr>';
                            }
                        }
                        table += '</tbody></table>';
                        return table;
                    }
                },
                restore: {},
                saveAsImage: {}
            }
        },
        series: [
            {
                type: 'graph',
                layout: 'force',
                symbolSize: 50,
                roam: true,
                edgeSymbol: ['circle', 'arrow'],
                edgeSymbolSize: [4, 10],
                focusNodeAdjacency:true,
                itemStyle: {
                    normal: {
                        borderColor: '#fff',
                        borderWidth: 1,
                        shadowBlur: 10,
                        shadowColor: 'rgba(0, 0, 0, 0.4)'
                    }
                },
                force: {
                    repulsion: 1200,
                    edgeLength: [10, 500]
                },

                lineStyle: {
                    normal: {
                        color: 'source',
                        curveness: 0.2

                    }
                },
                edgeLabel: {
                    normal: {
                        formatter: function (x) {
                            return x.data.name;
                        }
                    }
                },
                label: {
                    normal: {
                        textStyle: {
                            color:'black'
                        }
                    }
                },

                nodes: nodess,
                links: linkss
            }
        ]
    };
    myChart.setOption(option);

    // var tbody = window.document.getElementById("result2");
    // var zh = "";
    // if(type==="dfzh"){
    //     zh = $(obj).closest("tr").find("td:eq(3)").text();
    //     var url = "/SINOFAITH/banktjjgs/getSeach";
    //     $.ajax({
    //         type:"post",
    //         dataType:"json",
    //         url:url,
    //         data:{
    //             zh:zh,
    //             type:type
    //         },
    //         success:function (msg) {
    //             var data = msg.list;
    //             var str = "";
    //             for (i in data) {
    //                 if (i % 2 == 0) {
    //                     str += "<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
    //                 } else {
    //                     str += "<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
    //                 }
    //                 str += "<td width=\"4%\">" + data[i].id + "</td>" +
    //                     "<td width=\"5%\">" + data[i].jyzcs + "</td>" +
    //                     "<td width=\"13%\">" + data[i].jyzh + "</td>" +
    //                     "<td width=\"7%\" title=\""+data[i].name+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].name+"</div></td>"+
    //                     "<td width=\"8%\">" + data[i].czzcs + "</td>" +
    //                     "<td width=\"8%\">" + data[i].czzje + "</td>" +
    //                     "<td width=\"13%\">" + data[i].dfzh + "</td>" +
    //                     "<td width=\"7%\" title=\""+data[i].dfxm+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].dfxm+"</div></td>"+
    //                     "<td width=\"8%\">" + data[i].jzzcs + "</td>" +
    //                     "<td width=\"8%\">" + data[i].jzzje + "</td>" +
    //                     "<td width=\"13%\">" + data[i].jyzh + "</td>" +
    //                     "<td width=\"7%\" title=\""+data[i].name+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].name+"</div></td>"+
    //                     "</tr>";
    //             }
    //             tbody.innerHTML = str
    //         }
    //     })
    // }
    // if(type==="jyzh"){
    //     zh = $(obj).closest("tr").find("td:eq(1)").text()
    //     var url = "/SINOFAITH/banktjjgs/getSeach";
    //     $.ajax({
    //         type:"post",
    //         dataType:"json",
    //         url:url,
    //         data:{
    //             zh:zh,
    //             type:type
    //         },
    //         success:function (msg) {
    //             var data = msg.list;
    //             var str = "";
    //             for (i in data) {
    //                 if (i % 2 == 0) {
    //                     str += "<tr align='center' style='display:table;width:100%;table-layout:fixed;'>"
    //                 } else {
    //                     str += "<tr align='center' class='odd' style='display:table;width:100%;table-layout:fixed;'>"
    //                 }
    //                 str += "<td width=\"4%\">" + data[i].id + "</td>" +
    //                     "<td width=\"5%\">" + data[i].jyzcs + "</td>" +
    //                     "<td width=\"13%\">" + data[i].dfzh + "</td>" +
    //                     "<td width=\"7%\" title=\""+data[i].dfxm+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].dfxm+"</div></td>"+
    //                     "<td width=\"8%\">" + data[i].jzzcs + "</td>" +
    //                     "<td width=\"8%\">" + data[i].jzzje + "</td>" +
    //                     "<td width=\"13%\">" + data[i].jyzh + "</td>" +
    //                     "<td width=\"7%\" title=\""+data[i].name+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].name+"</div></td>"+
    //                     "<td width=\"8%\">" + data[i].czzcs + "</td>" +
    //                     "<td width=\"8%\">" + data[i].czzje + "</td>" +
    //                     "<td width=\"13%\">" + data[i].dfzh + "</td>" +
    //                     "<td width=\"7%\" title=\""+data[i].dfxm+"\"> <div style=\"width:90px;white-space: nowrap;text-overflow:ellipsis; overflow:hidden;\">"+data[i].dfxm+"</div></td>"+
    //                     "</tr>";
    //             }
    //             tbody.innerHTML = str
    //         }
    //     })
    // }
}

// $(function () { $('#myModal').on('hide.bs.modal', function () {
//
//     var tbody = window.document.getElementById("result")
//     if(tbody!=null) {
//         tbody.innerHTML = ""
//     }
//     $.ajax({url:"/SINOFAITH/bankzzxx/removeDesc"})
// })
// });

function hiddenZfbCft() {
    var flg = 0

    if($("#checkbox1").is(":checked")){
        flg=1
    }
    location.href= "/SINOFAITH/banktjjgs/hiddenZfbCft?code="+flg;
}

var radio = -1;
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
                location.href="/SINOFAITH/banktjjgs/getByZhzt?code="+-1;
            } else{
                old = this;
                location.href="/SINOFAITH/banktjjgs/getByZhzt?code="+$(this).val();
            }
        }
    });

    $(":radio").click(function(){
        alert(this.val());
    });
});