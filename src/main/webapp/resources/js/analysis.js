/**
 * Created by Melons on 2015/12/28.
 */

var _root;
var nicknames;
function getnumber(id){
    for(var j=0;j<_root['nodes'].length;j++){
        if(_root['nodes'][j]['id']==id)
        return _root['nodes'][j]['number'];
    }
}
function dataPreHandle(data) {
    _.each(data.nodes, function(node) {
        if(node.name=="null")
        node.label = node.number;
        else
        node.label = node.name;
        node.image = '/AMD/resources/image/Spy.png';
        node.shape ='image';
        node.size =  15;
        if(node.input=="false"||!node.input)
        node.size = 8;
        if(!node.input||node.input=="false"){
            if(node.collected||node.collected=="true"){
                node.borderWidth = 2;
                node.shapeProperties = {
                    useBorderWithImage: true
                };
                node.color = {
                    background: 'transparent',
                    border: 'green'
                };
            }
            else{
                node.borderWidth = 2;
                node.shapeProperties = {
                    useBorderWithImage: true
                };
                node.color = {
                    background: 'transparent',
                    border: 'grey'
                };
            }
        }
        //if (node.first) {
        //    node.borderWidth = 2;
        //    node.shapeProperties = {
        //        useBorderWithImage: true
        //    };
        //    node.color = {
        //        background: 'transparent',
        //        border: 'orange'
        //    };
        //} else if (!node.input) {
        //    node.borderWidth = 2;
        //    node.shapeProperties = {
        //        useBorderWithImage: true
        //    };
        //    node.color = {
        //        background: 'transparent',
        //        border: 'red'
        //    };
        //}
        //
        //if (node.poison) {
        //    node.font = {
        //        strokeWidth: 2,
        //        strokeColor: 'green'
        //    };
        //}
    });

    _.each(data.edges, function(edge) {
        edge.color = {
            color: '#848688'
        }
    });
}

function draw(data) {
    // create a network
    var container = document.getElementById('visContainer');//visContainer
    var options = {};
    network = new vis.Network(container, data, options);
    network.on('doubleClick', function(params) {
        if (params.nodes.length > 0) {
            var node = _.findWhere(data.nodes, {
                id: params.nodes[0]
            });
            node.address = node.address!="null"?node.address:"未知";
            node.name = node.name!="null"?node.name: '未知';
            node.gmsfhm = node.gmsfhm!="null"? node.gmsfhm: '未知';
            var contact = node.id;
            if(node.collected){
                var count = 0;
                var contact = node.id+':';
                for(var i = 0;i<_root['edges'].length;i++){
                    if(_root['edges'][i].from==node.id) {
                        contact += _root['edges'][i].label + "-" + getnumber(_root['edges'][i].to) + ";";
                        count++;
                    }
                    else if(_root['edges'][i].to==node.id){
                        contact+=_root['edges'][i].label+"-"+getnumber(_root['edges'][i].from)+";";
                        count++;
                    }
                }
                if(count>0){
                    $('#visContainer').mask("数据获取中");
                    $.ajax({
                        url:'/AMD/analysis/nickname',
                        type:'post',
                        data:{"value":contact},
                        success:function(data){
                            $('#visContainer').unmask();
                            var textcontent = "<div style='text-align: left;margin-left: 120px;'>姓名: " + node.name + '</div><br>' +
                                "<div style='text-align: left;margin-left: 120px;'>身份证: " + node.gmsfhm + '</div><br>' +
                                "<div style='text-align: left;margin-left: 120px;'>号码: " + node.number + '</div><br>';

                            for(var i = 0;i<data.length;i++){
                                var nickname = data[i]['nickname'];
                                var number = data[i]['number'];
                                nickname = nickname==null?"未知":nickname;
                                textcontent+="<div style='text-align: left;margin-left: 120px;'>"+nickname+":" + number + "</div><br>"
                            }
                            swal({
                                title: '',
                                text:  textcontent,
                                //"<div style='text-align: left;margin-left: 120px;'>地址: " + node.address + '</div>',
                                html: true
                            });
                        }
                    });
                }else{
                    $('#visContainer').unmask();
                    swal({
                        title: '',
                        text: "<div style='text-align: left;margin-left: 120px;'>姓名: " + node.name + '</div><br>' +
                        "<div style='text-align: left;margin-left: 120px;'>身份证: " + node.gmsfhm + '</div><br>' +
                        "<div style='text-align: left;margin-left: 120px;'>号码: " + node.number + '</div><br>' ,
                        //"<div style='text-align: left;margin-left: 120px;'>地址: " + node.address + '</div>',
                        html: true
                    });
                }

            }else{
                $('#visContainer').unmask();
                swal({
                    title: '',
                    text: "<div style='text-align: left;margin-left: 120px;'>姓名: " + node.name + '</div><br>' +
                    "<div style='text-align: left;margin-left: 120px;'>身份证: " + node.gmsfhm + '</div><br>' +
                    "<div style='text-align: left;margin-left: 120px;'>号码: " + node.number + '</div><br>' ,
                    //"<div style='text-align: left;margin-left: 120px;'>地址: " + node.address + '</div>',
                    html: true
                });
            }

        }
    });
}

function zabClick(th) {
    $('.zTreeDemoBackground').css({
        'position': 'absolute',
        'left': $('#'+th.id).offset().left,
        'top': $('#'+th.id).offset().top + 30,
    });
    if ($(".zTreeDemoBackground").css("display") == "block") {
        $('.zTreeDemoBackground').css('display', 'none');
    } else {
        $('.zTreeDemoBackground').css('display', "block");
    }
};

$(function () {
    $('#startTimePicker').datetimepicker({
        lang: 'ch',
        timepicker: false,
        format: 'Y-m-d'
    });
    $('#endTimePicker').datetimepicker({
        lang: 'ch',
        timepicker: false,
        format: 'Y-m-d'
    });

    $('#extendBtn').click(function () {
        var sttime = eval(document.getElementById('startTimePicker')).value;
        var endtime = eval(document.getElementById('endTimePicker')).value;
        if (sttime == null || sttime == '')swal('','抓获时间起始不能为空');
        else if (endtime == null || endtime == '')swal('','抓获时间截止不能为空');
        else if (!comTime(sttime, endtime))swal('','抓获时间范围错误');

        else {

            $('#visContainer').mask("分析中");
            var option = {
                url:'/AMD/analysis/do',
                type:'post',
                dataType:'json',
                success:function(result){
                    $('#visContainer').unmask("分析中");
                    _root = result;
                    console.log(result);
                    dataPreHandle(result);
                    draw(result);
                },
                error:function(){

                }

            };
            var form = $('#analysisform');
            form.ajaxSubmit(option);
        }
    });

    $('#saveBtn').click(function () {
        var sttime = eval(document.getElementById('startTimePicker')).value;
        var endtime = eval(document.getElementById('endTimePicker')).value;
        if (sttime == null || sttime == '')swal('','抓获时间起始不能为空');
        else if (endtime == null || endtime == '')swal('','抓获时间截止不能为空');
        else if (!comTime(sttime, endtime))swal('','抓获时间范围错误');
        else {
            swal({
                title: '',
                text: "<h3>推送周期:</h3><br>  <select id=interval style='width:444px;height:43px'>" +
                "<option value='day'><h3>每天</h3></option>" +
                "<option value='week'><h3>每周</h3></option>" +
                "<option value='month'><h3>每月</h3></option>" +
                "</select><br><br>" +
                "<h3>推送名称:</h3>",
                type: 'input',
                html: true,
                showCancelButton: true,
                closeOnConfirm: false,
                animation: "slide-from-top"
            }, function (inputValue) {
                if (inputValue === false)
                    return false;
                if (inputValue === "") {
                    swal.showInputError("请输入推送名称");
                    return false;
                }
                //保存推送
                //
                //
                $('.period').val($('#interval').val());
                $('.condiname').val(inputValue);

                var option = {
                    url:'/AMD/analysis/savecondi',
                    type:'post',
                    dataType:'json',
                    success:function(result){
                        },
                    error:function(){

                    }

                };
                var form = $('#analysisform');
                form.ajaxSubmit(option);

                swal("保存成功", "已保存推送条件：" + inputValue, "success");
            });
        }
    });

    function comTime(a, b) {
        var arr = a.split("-");
        var starttime = new Date(arr[0], arr[1], arr[2]);
        var starttimes = starttime.getTime();

        var arrs = b.split("-");
        var lktime = new Date(arrs[0], arrs[1], arrs[2]);
        var lktimes = lktime.getTime();

        if (starttimes > lktimes)
            return false;
        else
            return true;
    }

    var setting = {
        view: {
            selectedMulti: false
        },
        async: {
            enable: true,
            url:"/AMD/BasicQuery/test",
            autoParam:["id", "name", "dm"],
            otherParam:{"otherParam":"zTreeAsyncTest"}
            //dataFilter: filter
        },
        callback: {
            beforeClick: beforeClick,
            beforeAsync: beforeAsync,
            onAsyncError: onAsyncError,
            onAsyncSuccess: onAsyncSuccess
        }
    };

    function filter(treeId, parentNode, childNodes) {
        if (!childNodes) return null;
        for (var i=0, l=childNodes.length; i<l; i++) {
            childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
        }
        return childNodes;
    }
    function beforeClick(treeId, treeNode) {
        $('.vis_zab').attr('value',treeNode.name);
        $('.vis_zab').attr('param',treeNode.dm);
        if (!treeNode.isParent) {
            $('.zab').attr('value','\''+treeNode.dm+'\'');
            $('.zTreeDemoBackground').css('display','none');

            if($(".vis_zab").attr("param") != ""){
                $.ajax({
                    url:'/AMD/BasicQuery/fab',
                    type:'POST',
                    data: {"value":$(".vis_zab").attr("param")},
                    dataType: 'JSON',
                    success: function(data) {
                        // console.log(data);
                        $('#fab').empty();
                        $('#fab').append("<option value =''> 副案别 </option>");
                        $.each(data, function (idx,item) {
                            $('#fab').append("<option value = '" + item['dm'] + "'>" + item['mc'] + "</option>");
                        });
                    },
                    error: function () {
                    }
                });
            }else{
                $('#fab').empty();
                $('#fab').append("<option value = ''> 副案别</option>");
            }
        } else {
            $('.zTreeDemoBackground').mask();

            $.ajax({
                url:'/AMD/BasicQuery/fab',
                type:'POST',
                data: {"value":treeNode.dm},
                dataType: 'JSON',
                success: function(data) {
                    // console.log(data);
                    $('#fab').empty();

                    $('#fab').append("<option value =''> 副案别 </option>");
                    $.each(data, function (idx,item) {
                        $('#fab').append("<option value = '" + item['dm'] + "'>" + item['mc'] + "</option>");
                    });
                    $('.zTreeDemoBackground').unmask();
                    $('.zTreeDemoBackground').css('display','none');
                },
                error: function () {
                    $('.zTreeDemoBackground').unmask();
                    $('.zTreeDemoBackground').css('display','none');
                }
            });


            $.ajax({
                url:'/AMD/BasicQuery/zab',
                type:'post',
                data:{"value":treeNode.dm},
                success:function(data){
                    console.log(data);
                    $('.zab').attr('value',data);
                    $('.zTreeDemoBackground').unmask();

                    $('.zTreeDemoBackground').css('display','none');

                }
            });
        }
    }
    var log, className = "dark";
    function beforeAsync(treeId, treeNode) {
        className = (className === "dark" ? "":"dark");
        showLog("[ "+getTime()+" beforeAsync ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
        return true;
    }
    function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
        showLog("[ "+getTime()+" onAsyncError ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
    }
    function onAsyncSuccess(event, treeId, treeNode, msg) {
        showLog("[ "+getTime()+" onAsyncSuccess ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
    }

    function showLog(str) {
        if (!log) log = $("#log");
        log.append("<li class='"+className+"'>"+str+"</li>");
        if(log.children("li").length > 8) {
            log.get(0).removeChild(log.children("li")[0]);
        }
    }
    function getTime() {
        var now= new Date(),
            h=now.getHours(),
            m=now.getMinutes(),
            s=now.getSeconds(),
            ms=now.getMilliseconds();
        return (h+":"+m+":"+s+ " " +ms);
    }

    function refreshNode(e) {
        var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
            type = e.data.type,
            silent = e.data.silent,
            nodes = zTree.getSelectedNodes();
        if (nodes.length == 0) {

        }
        for (var i=0, l=nodes.length; i<l; i++) {
            zTree.reAsyncChildNodes(nodes[i], type, silent);
            if (!silent) zTree.selectNode(nodes[i]);
        }
    }
    $.fn.zTree.init($("#treeDemo"), setting);

});
    function fkDownload(){
        var postdata = "" ;
        var edges = _root['edges'];
        for(var i = 0;i<edges.length;i++){
            postdata+=edges[i]['from']+"-"+edges[i]['to']+';'
        }
        $("input[name='content']").val(postdata);
        $('#download').submit();

    }