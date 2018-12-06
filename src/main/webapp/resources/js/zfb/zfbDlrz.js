// 跳转页面
function zfbDlrzSkip(code){
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
        location="/SINOFAITH/zfb"+code+"/search?pageNo="+onPage;
    }
}

function getZfbDlrzDetails(obj){
    // 支付宝用户ID
    var zfbyhId = $(obj).closest("tr").find("td:eq(1)").text();
    // 账户名
    var zhmc = $(obj).closest("tr").find("td:eq(2)").text();
    $.ajax({
        type:"post",
        dataType:"json",
        url:"/SINOFAITH/zfbDlrz/getDetails",
        data:{
            zfbyhId:zfbyhId,
            zhmc:zhmc
        },
        success:function (data) {
            var dom = document.getElementById("container");
            var myChart = echarts.init(dom);
            var app = {};
            option = null;
            var dataA = [];
            for(i=0;i<data.length;i++){
                var dataB = [];
                dataB.push(data[i].hour);
                dataB.push(data[i].num);
                dataB.push(data[i].khdip);
                dataA.push(dataB);
            }

            var schema = [
                {name: 'date', index: 0, text: '点'},
                {name: 'login', index: 1, text: '登陆次数'},
                {name: 'IP', index: 2, text: 'IP地址'}
            ];


            var itemStyle = {
                normal: {
                    opacity: 0.8,
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowOffsetY: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            };

            option = {
                backgroundColor: '#404a59',
                color: [
                    '#80F1BE'
                ],
                legend: {
                    y: 'top',
                    data: [data[0].yhm],
                    textStyle: {
                        color: '#fff',
                        fontSize: 16
                    }
                },
                grid: {
                    x: '10%',
                    x2: 150,
                    y: '18%',
                    y2: '10%'
                },
                tooltip: {
                    padding: 10,
                    backgroundColor: '#222',
                    borderColor: '#777',
                    borderWidth: 1,
                    formatter: function (obj) {
                        var value = obj.value;
                        var content = '<div style="border-bottom: 1px solid rgba(255,255,255,.3); font-size: 18px;padding-bottom: 7px;margin-bottom: 7px">'
                            + obj.seriesName + ' ' + value[0] + '点'
                            + '</div>'
                            + schema[1].text + '：' + value[1] + '<br>';
                        var j = 1;
                        for(i=0;i<dataA.length;i++){
                            if(dataA[i][0]==value[0] && dataA[i][1]==value[1]){
                                if(j%2==0){
                                    content += schema[2].text+(j++)+ ': ' + dataA[i][2] + '<br>';
                                }else{
                                    content += schema[2].text+(j++)+ ': ' + dataA[i][2] + '\t';
                                }
                            }
                        }
                        return content;
                    }
                },
                xAxis: {
                    type: 'value',
                    name: '小时',
                    nameGap: 16,
                    nameTextStyle: {
                        color: '#fff',
                        fontSize: 14
                    },
                    max: 23,
                    splitLine: {
                        show: false
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#eee'
                        }
                    }
                },
                yAxis: {
                    type: 'value',
                    name: '登陆次数',
                    nameLocation: 'end',
                    nameGap: 20,
                    nameTextStyle: {
                        color: '#fff',
                        fontSize: 16
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#eee'
                        }
                    },
                    splitLine: {
                        show: false
                    }
                },
                visualMap: [
                    {
                        left: 'right',
                        top: '10%',
                        dimension: 1,
                        min: 0,
                        max: 30,
                        itemWidth: 30,
                        itemHeight: 120,
                        calculable: true,
                        precision: 0.1,
                        text: ['圆形大小：PM2.5'],
                        textGap: 30,
                        textStyle: {
                            color: '#fff'
                        },
                        inRange: {
                            symbolSize: [10, 70]
                        },
                        outOfRange: {
                            symbolSize: [10, 70],
                            color: ['rgba(255,255,255,.2)']
                        },
                        controller: {
                            inRange: {
                                color: ['#c23531']
                            },
                            outOfRange: {
                                color: ['#444']
                            }
                        },
                        show:false
                    },
                    {
                        left: 'right',
                        bottom: '5%',
                        dimension: 1,
                        min: 0,
                        max: 15,
                        itemHeight: 120,
                        calculable: true,
                        precision: 0.1,
                        text: ['明暗：二氧化硫'],
                        textGap: 30,
                        textStyle: {
                            color: '#fff'
                        },
                        inRange: {
                            colorLightness: [1, 0.5]
                        },
                        outOfRange: {
                            color: ['rgba(255,255,255,.2)']
                        },
                        controller: {
                            inRange: {
                                color: ['#c23531']
                            },
                            outOfRange: {
                                color: ['#444']
                            }
                        },
                        show:false
                    }
                ],
                series: [
                    {
                        name: data[0].yhm,
                        type: 'scatter',
                        itemStyle: itemStyle,
                        data: dataA
                    }
                ]
            };
            if (option && typeof option === "object") {
                myChart.setOption(option, true);
            }
        }
    })
}
