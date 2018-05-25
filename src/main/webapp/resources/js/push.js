/**
 * Created by Melons on 2016/1/5.
 */
var condis;
var contents;
var condiInt;
var condiMC;
var inter = {
    "day": "每天",
    "week": "每周",
    "month": "每月",
}
var birthPlace = {
    '11': '北京市',
    '12': '天津市',
    '13': '河北省',
    '14': '山西省',
    '15': '内蒙古自治区',
    '21': '辽宁省',
    '22': '吉林省',
    '23': '黑龙江省',
    '31': '上海市',
    '32': '江苏省',
    '33': '浙江省',
    '34': '安徽省',
    '35': '福建省',
    '36': '江西省',
    '37': '山东省',
    '41': '河南省',
    '42': '湖北省',
    '43': '湖南省',
    '44': '广东省',
    '45': '广西壮族自治区',
    '46': '海南省',
    '50': '重庆市',
    '51': '四川省',
    '52': '贵州省',
    '53': '云南省',
    '54': '西藏自治区',
    '61': '陕西省',
    '62': '甘肃省',
    '63': '青海省',
    '64': '宁夏回族自治区',
    '65': '新疆维吾尔自治区'
};

$(function () {
    window.onload = function () {
        //读取用户推送条件列表
        $.ajax({
            url: "push/getPushList",
            type: "GET",
            dataType: 'json',
            async: true,
            success: function (result) {
                //alert(toDateStr(result[0][0].pushdate));
                var li = document.getElementById('pushList');
                for (var i = 0; i < result.length; i++) {
                    if (result[i].length >= 1) {
                        li.innerHTML += "<li> <a href='#' style='font-size: 16px'>" + result[i][0].pushname + "(" + result[i].length + ")"
                            + "</a><ul style='display:none' id='" + result[i][0].pushname + "' ></ul></li>";
                        var read = [];
                        var unread = [];
                        for (var j = 0; j < result[i].length; j++) {
                            if (result[i][j].state == 1) read.push(result[i][j]);
                            else unread.push(result[i][j]);
                        }
                        for (var k = 0; k < unread.length; k++)
                            document.getElementById(unread[k].pushname).innerHTML += "<li><a style='color: #f3686e;font-size: 18px' href='javascript:void(0)' onclick='updateState(this)'" +
                                "id='" + toDateStr(unread[k].pushdate) + "' value='" + unread[k].contentid + "'>"
                                + toDateStr(unread[k].pushdate) + "</a></li>";
                        for (var n = 0; n < read.length; n++)
                            document.getElementById(read[n].pushname).innerHTML += "<li><a style='font-size: 18px' href='javascript:void(0)' onclick='updateState(this)'" +
                                "id='" + toDateStr(read[n].pushdate) + "' value='" + read[n].contentid + "'>"
                                + toDateStr(read[n].pushdate) + "</a></li>";
                    }
                }

                $.ajax({
                    url: "push/getCondiDetail",
                    type: "GET",
                    dataType: 'json',
                    async: true,
                    success: function (res) {
                        condis = res;
                        var mag = document.getElementById('condiList');
                        for (var i = 0; i < condis.length; i++) {
                            mag.innerHTML += "<div class='ad' id='mag" + condis[i].pushname + "'><nobr style='font-size: 16px;padding-left: 25px' >" + condis[i].pushname + "</nobr>" +
                                "<div style='float:right;padding-top: 5px'><img class='icon' src='/AMD/resources/thirdparty/assets/images/ico9/over/003a_19.png' " +
                                "style='width:12px;height:12px;' onclick='editCondi(this)'/>" +
                                "<img class='icon' src='/AMD/resources/thirdparty/assets/images/ico9/over/003a_15.png' " +
                                "style='width:12px;height:12px;margin-left: 10px;margin-right:5px' onclick='deleteCondi(this)'/></div></div>" +
                                "<br>";
                        }
                        //console.log(condis.toString());
                    },
                    error: function () {
                        sweetAlert("", "无法获取用户推送，请检查网络环境");
                    }
                })

            },
            error: function () {
                //alert("error");
                sweetAlert("", "无法获取用户推送，请检查网络环境");
            }
        })
    }
})
;


function toDateStr(a) {
    var time = new Date(a).dateFormat("Y-m-d");
    return time.toString();
};

function editCondi(th) {
    //alert(th.parentNode.parentNode.childNodes[0].innerHTML);
    var condiContent = null;
    var modal = document.getElementsByClassName('modal-content');
    for (var i = 0; i < condis.length; i++) {
        if (th.parentNode.parentNode.childNodes[0].innerHTML == condis[i].pushname) {
            console.log(condis[i].condition);
            condiContent = condis[i].condition;
            condiMC = condis[i].mc;
            condiInt = condis[i].interval;
            break;
        }
    }
    //对推送条件condiContent进行解析展示
    if (condiContent !== null) {
        var arr = condiContent.split(';');
        console.log(arr.length);
        console.log(arr[0].split(':')[1]);
        //获取案别
        document.getElementById('vis_maincase').value = condiMC;

        document.getElementById('exampleModalLabel').innerHTML = "推送条件： " + th.parentNode.parentNode.childNodes[0].innerHTML;
        document.getElementById('interval').value = inter[condiInt];

        if (arr[1].split(':')[1] === "")document.getElementById('birthplace').value = '-无限制-';
        else document.getElementById('birthplace').value = birthPlace[arr[1].split(':')[1]];
        $('#lines').val(arr[2].split(':')[1]);
        if (arr[3].split(':')[1] === '0')document.getElementById('mobile').checked = false;
        else document.getElementById('mobile').checked = true;
        if (arr[4].split(':')[1] === '0')document.getElementById('qq').checked = false;
        else document.getElementById('qq').checked = true;
        if (arr[5].split(':')[1] === '0')document.getElementById('wx').checked = false;
        else document.getElementById('wx').checked = true;
        $('#editModal').modal({
            keyboard: true,
            show: true,
        });
    }
};


function deleteCondi(th) {
    swal({
            title: "确认删除推送条件 " + th.parentNode.parentNode.childNodes[0].innerHTML + "?",
            text: "",
            type: "warning",
            showCancelButton: true,
            confirmButtonText: "确认",
            cancelButtonText: "取消",
            closeOnConfirm: false
        },
        function () {
            $.ajax({
                url: "push/delete",
                type: "GET",
                dataType: 'json',
                data: {"value": th.parentNode.parentNode.childNodes[0].innerHTML},
                async: true,
                success: function () {
                    swal("删除成功!", "推送条件集合 " + th.parentNode.parentNode.childNodes[0].innerHTML + "已删除", "success");
                    var todel = document.getElementById("mag" + th.parentNode.parentNode.childNodes[0].innerHTML);
                    todel.parentNode.removeChild(todel);
                    var timer = setTimeout(refresh, 2000);
                    //console.log("res");
                },
                error: function () {
                    swal("", "删除失败,请检查网络环境");
                }
            })
        });
};

function refresh() {
    location.reload({bfbForceGet: true,});
}

function updateState(th) {
    var id = th.getAttribute('value');
    $('#visContainer').mask("分析中");
    $.ajax({
        url: "push/updateState",
        type: "GET",
        dataType: 'json',
        data: {"value": th.getAttribute('value')},
        async: true,
        success: function () {
            console.log("推送状态更新成功")
        },
        error: function () {
            console.log("推送状态更新失败")
        }
    });
    $.ajax({
        url:"push/getcontent",
        type:"post",
        dataType:'text',
        data:{"value":id},
        success:function(result){
            $('#visContainer').unmask();
            var data = JSON.parse(result);
            console.log(data);
            _root = data;
            dataPreHandle(data);
            draw(data);
        }
    });
}