/**
 * Created by guibin on 14-8-4.
 */

function addphone2table(_phonenum){
    $.getJSON("system/addphone2table",{phone:_phonenum},function(data){
    });
}

function delphone2table(_phonenum){
    $.getJSON("system/delphone2table",{phone:_phonenum},function(data){
    });
}

function delalltable(){
    $.getJSON("system/delalltable",{},function(data){
    });
}

$(function() {

    var cookieInput = $.cookie('CONJUGATE_COOKIE_INPUT');
    if (cookieInput != undefined && cookieInput != null) {
        var optionList = cookieInput.split(',');
        for (var i = 0; i < optionList.length; i ++) {
            $('#autoList').append("<option label='"+optionList[i]+"' value='"+optionList[i]+"'/>");
        }
    }

    $("a").each(function(){
        if($(this).attr('href') == "#")
            $(this).attr('href',"javascript:void(0)");
    });
    $(".selectwho input").click(function(){

        var nodes=root.nodes,i=0;
        if($(this).attr("checked")=="checked"){
            while ( i < nodes.length){
                if (nodes[i]["group"]==$(this).attr("myvalue")){
                    nodes[i]["selected"] = true;
                }
                i++;
            }
        }
        else{
            while ( i < nodes.length){
                if (nodes[i]["group"]==$(this).attr("myvalue")){
                    nodes[i]["selected"] = false;
                }
                i++;
            }
        }
        tick();
    });

    $('#b_kzfx').parent().attr("href","#");
    // 扩展模式选择
    $('.btn-pattern').click(function() {
        if(!$(this).hasClass('selected')) {
            $(this).addClass('selected');
        }
        else{
            $(this).removeClass('selected');
        }
    });

    // 启动时间插件
    $('#startTimePicker').datetimepicker();
    $('#endTimePicker').datetimepicker();

//缩放

    function zoomIn(){
        if ($('#mygraph>svg').length != 0  && window.__VIZ_TRANSFORM != undefined) {
        var graph = $('#mygraph>svg>g');

        var transX = window.__VIZ_TRANSFORM.transX - 30;
        var transY = window.__VIZ_TRANSFORM.transY - 30;
        var scale = window.__VIZ_TRANSFORM.scale / 0.9;
        graph.attr('transform', 'translate('+transX+','+transY+') scale('+scale+')');
        window.__VIZ_TRANSFORM.transX = transX;
        window.__VIZ_TRANSFORM.transY = transY;
        window.__VIZ_TRANSFORM.scale = scale;
        //-------------------
        $('#mygraph>svg>g>g').each(function(){
            $(this).attr('transform', $(this).attr('transform').split(' ')[0]+' scale('+(window.__VIZ_TRANSFORM.scale>1?1/window.__VIZ_TRANSFORM.scale:1)+')');
        });    }

    }
    function zoomOut(){
        if ($('#mygraph>svg').length != 0 && window.__VIZ_TRANSFORM != undefined) {
            var graph = $('#mygraph>svg>g');

            var transX = window.__VIZ_TRANSFORM.transX + 30;
            var transY = window.__VIZ_TRANSFORM.transY + 30;
            var scale = window.__VIZ_TRANSFORM.scale * 0.9;
            graph.attr('transform', 'translate('+transX+','+transY+') scale('+scale+')');
            window.__VIZ_TRANSFORM.transX = transX;
            window.__VIZ_TRANSFORM.transY = transY;
            window.__VIZ_TRANSFORM.scale = scale;
            //-------------------
            $('#mygraph>svg>g>g').each(function(){
                $(this).attr('transform', $(this).attr('transform').split(' ')[0]+' scale('+(window.__VIZ_TRANSFORM.scale>1?1/window.__VIZ_TRANSFORM.scale:1)+')');
            });
        }
    }
    $('#mygraph').mousewheel(function(e, delta){
        e = e || window.event;
        if (e.stopPropagation) e.stopPropagation();
        else e.cancelBubble = true;
        if (e.preventDefault) e.preventDefault();
        else e.returnValue = false;
        if(delta>0){
            zoomIn();
        }else{
            zoomOut();
        }
    })
    $('#btnZoomIn').click(function() {
       zoomIn();
    });

    $('#btnZoomOut').click(function() {
       zoomOut()
    });


    $('#phoneEntry').bind('keypress', function(event) {
        if(event.keyCode == '13') {
            $('#btnAddPhone').click();
        }
    });
    // 添加手机号
    $('#btnAddPhone').click(function() {
        var phone = $('#phoneEntry').val().trim();
        //集合处理
        if(phone[0]=="@" && $(this).attr('setmethod')!="ignore"){
            $.getJSON("amdset/setphonesbyname",{setname:phone},function(data){
                if(data==null)
                    $('#phoneEntry').val('');
                else {
                    for (var i = 0; i < data.length; i++) {
                        insertPhoneData(data[i]);
                        addphone2table(data[i]);
                    }
                }
            });
        }
        else {
            insertPhoneData(phone);
            addphone2table(phone);
        }

        $("#extendBtn").text("新建扩展分析");
    });






    // 清除全部号码
    $('#btnRemoveAll').click(function() {
        if($('#phoneTable').length != 0) {
            $('#phoneTable').remove();
            $("#extendBtn").text("扩  展");
        }
    });

    $('#btnLoadFile').click(function() {
        $("input[type='file']").click();
    });
    $("#uploadFileForm>input[type='file']").change(function(event) {
        $("body").mask("正在拼命上传文件中，请稍候....");
        $('#uploadFileForm').ajaxSubmit({
            dataType: 'json',
            success: function(result) {
                if (result == null) {
                    alertify.alert('上传文件出现问题！请检查网络环境是否正常或文件格式及内容是否符合标准！');
                } else if(result.length === 0) {
                    alertify.alert('文件内容为空.');
                } else {
                    for(var i = 0; i < result.length; i ++) {
                        insertPhoneData(result[i]);
                    }
                }
                $("body").unmask();
            },
            error: function() {
                alertify.alert('上传文件出现问题！请检查网格环境是否正常或文件格式及内容是否符合标准！');
                $("body").unmask();
            }
        });

        $(this).val('');
    });





    $('#btnDownload').click(function() {

        var _type = "";
        $('.mypattern:checked').each(function(){
            _type= parseInt($(this).attr("myval"));
        });

        var _number="";
        $("#phoneTable span[type='text']").each(function(){
            _number = _number+";"+$(this).text();
        });
        if(_number==""||_type=="") {
            alert("分析对象和分析方式不能为空");
            return;
        }
        $("body").mask("正在拼命下载记录中，请稍候...");

        $.ajax({
            url: '/AMD/relation/logFilePath',
            type: 'GET',
            data: 'type='+_type+'&'+'source='+_number,
            dataType: 'text',
            contentType: 'application/json;charset=UTF-8',
            success: function(result) {
                console.log('log path:' + result);
                $('#downloadForm>input').val(result);
                $('#downloadForm').submit();
                $("body").unmask();
            },
            error: function(error) {
                alert('错误！请检查网络环境！');
                $("body").unmask();
            }
        });


    });
    /* 分析按钮响应
    $('#btnAnalyse').click(function() {

        if($('#phoneTable').length === 0) {
            alert('先添加手机号');
        } else {
            var option = {};

            option.phoneList = [];
            var phoneDomList = $("#phoneTable li span[type='text']");
            for(var i = 0; i < phoneDomList.length; i ++) {
                option.phoneList.push($(phoneDomList[i]).text());
            }

            option.minTime = $('#startTimePicker').val();
            option.maxTIme = $('#endTimePicker').val();

            option.s_province = $('#s_province').val();
            option.s_city = $('#s_city').val();
            option.s_county = $('#s_county').val();

            option.analysePattern = $('#patternSelection>.selected').text();
            option.vizPattern = $('#rightbar>select').val();
            option.showPattern = $('#modelToggle>.selected').text();

            $.ajax({
                url: 'conjugate/option',
                type: 'POST',
                data: JSON.stringify(option),
//                data: JSON.stringify({phones:'111;',startTime:'2014-09-09 12:00:45',endTime:'2014-09-10 12:00:00',s_province:'福建省',s_city:'泉州市',s_county:'晋江市',analysePattern:'通信分析',vizPattern:'普通模式',showPattern:'图形模式'}),
                dataType: 'json',
                contentType: 'application/json;charset=UTF-8',
                success: function(result) {
                    console.log(result);
                }
            });
        }
    });
*/


    $('#btnPicDownload').click(function() {
        var svg = $('#mygraph>svg');
        if (svg.length == 1) {

            var origin_g = svg.find('>g');
            var transform = origin_g.attr('transform');
            origin_g.removeAttr('transform');
            var box = svg[0].getBBox();
            origin_g.attr('transform', transform);

            var picSvg = document.createElement('svg');
            picSvg.setAttribute('version','1.1');
            picSvg.setAttribute('xmlns', 'http://www.w3.org/2000/svg');
            picSvg.setAttribute('xmlns:xlink','http://www.w3.org/1999/xlink');
            picSvg.setAttribute('width', (box.width+Math.abs(box.x)+50).toString());
            picSvg.setAttribute('height', (box.height+Math.abs(box.y)+50).toString());
            picSvg.setAttribute('xml:space', 'preserve');

            picSvg.innerHTML = "<defs><style type='text/css'>" +
            "<![CDATA["
            + ".node{}"
            + ".link{ stroke:#999;stroke-opacity:.6;}"
            + "circle:hover, polygon:hover, rect:hover{ stroke: #ccc; stroke-width: 2px;}"
            + "circle.selected, polygon.selected, rect.selected{ stroke: #CC0000; stroke-width: 4px;}"
            + ".stared{ stroke: #104e8b;stroke-width: 2px;}"
            + ".searched{ stroke: #cc0000; stroke" +
            "-width: 2px;}"
            + "]]></style></defs>";

            var g = svg.find('>g')[0];
            var cloneG = document.createElement("g");
            cloneG.innerHTML = g.innerHTML;
            cloneG.setAttribute('transform', 'translate('+(-box.x+25).toString()+','+(-box.y+25).toString()+')');
            picSvg.appendChild(cloneG);
            var content = "<?xml version='1.0' encoding='utf-8'?>"
                + "<!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.1//EN' 'http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd'>"
                + picSvg.outerHTML;

            content = content.replace(new RegExp(/<img/g), '<image')
                .replace(new RegExp(/imageflag=\"true\">/g), '></image>')
                .replace(new RegExp(/\/AMD\/resources/g), window.location.origin + '/AMD/resources');

            $('#vizDownloadForm>input').val(content);
            $('#vizDownloadForm').submit();

        } else {
            console.log('none');
        }
    });
});

function createCircleToken(num) {
    return "<span class='circleTokenP'><text class='count'>"+num+"</text></span>";
}

function createPhoneShow(phone) {
    return "<span type='text'>"+phone+"</span>";
}

function removeSinglePhone(source) {

    var parent = $(source).parent().parent();
    var id = $(parent.children()[0]).find('text').text();
    var list = $('.circleTokenP .count');
    for(var i = id; i < list.length; i ++) {
        var item = list[i];
        $(item).text(i);
    }

    parent.remove();
    delphone2table($(parent.children()[1]).children("span").html());
}

function insertPhoneData(phone) {
    if(phone !== '') {
        var token = createCircleToken($('#phoneTable tr').length + 1);
        var phoneShow = createPhoneShow(phone);
        $('#phoneTable').append("<tr><td width='20'>"+token+'、</td><td class="realnumber">'+phoneShow+'</td><td align="right"><img onclick="removeSinglePhone(this)" src="/AMD/resources/thirdparty/assets/images/ico9/on/003_15.png" ></td></tr>');
        $('#phoneEntry').val('');

        var cookieInput;
        //////    加入历史记录，用做智能提示     ///////
        if (window.location.pathname == '/AMD/extend') {
            cookieInput = $.cookie('EXTEND_COOKIE_INPUT');
            if (cookieInput == undefined || cookieInput == null) {
                $.cookie('EXTEND_COOKIE_INPUT', '', {expires: 30});
                cookieInput = $.cookie('EXTEND_COOKIE_INPUT');
            }
        } else if (window.location.pathname == '/AMD/relation') {
            cookieInput = $.cookie('RELATION_COOKIE_INPUT');
            if (cookieInput == undefined || cookieInput == null) {
                $.cookie('RELATION_COOKIE_INPUT', '', {expires: 30});
                cookieInput = $.cookie('RELATION_COOKIE_INPUT');
            }
        }

        var numberList = cookieInput.split(',');
        var foundSome = false;
        for (var i = 0; i < numberList.length; i ++) {
            if (phone == numberList[i]) {
                foundSome = true;
                break;
            }
        }

        if (!foundSome) {
            //   最多存二十个记录
            if (numberList.length >= 20) {
                numberList.splice(1, 1);
            }
            numberList.push(phone);
            if (window.location.pathname == '/AMD/extend') {
                $.cookie('EXTEND_COOKIE_INPUT', numberList, {expires: 30});
            } else if (window.location.pathname == '/AMD/relation') {
                $.cookie('RELATION_COOKIE_INPUT', numberList, {expires: 30});
            }
            $('#autoList').append("<option label='"+phone+"' value='"+phone+"'/>");
        }

        //////    加入历史记录，来用作智能提示     ///////
    }
}



//d3 相关
function delSelectedNode(){
    var nodes=root.nodes,i=0;
    while ( i < nodes.length){
        if (nodes[i]["selected"]){
            delNode(i);
            i=0;
        }
        else
            i++;
    }
    delSingleNode();
    update();
    $("#filterbox").hide();
}

function delUnselectedNode(){
    var nodes=root.nodes,i=0;
    while ( i < nodes.length){
        if (!nodes[i]["selected"]){
            delNode(i);
            i=0;
        }
        else
            i++;
    }
    delSingleNode();
    update();

    $("#filterbox").hide();
}
function starSelectedNode(){
    var nodes=root.nodes,i=0;
    while ( i < nodes.length){
        if (nodes[i]["selected"]){
            if(!nodes[i]["stared"])
                nodes[i]["stared"] = true;
            else
                nodes[i]["stared"] = false;

            nodes[i]["selected"] = false;
        }
        i++;
    }
    tick();
}
function delNode(index){
    var i=0,
        n=root.nodes[index],
        links=root.links;
    if(n==null) return;
    while ( i < links.length){
        links[i]['source']== n || links[i]['target'] ==n ? links.splice(i,1) : ++i;
    }
    root.nodes.splice(index,1);
}
function reSelect(){
    var nodes=root.nodes,i=0;
    while ( i < nodes.length){
        nodes[i]["selected"] = false;
        nodes[i]["stared"] = false;
        i++;
    }
    $(".selectwho input").removeAttr("checked");
    tick();
}
function reverseSelect(){
    var nodes=root.nodes,i=0;
    while ( i < nodes.length){
        nodes[i]["selected"] = !nodes[i]["selected"];
        i++;
    }
    tick();
}
function allSelect() {
    var nodes = root.nodes, i = 0;
    while (i < nodes.length) {
        nodes[i]["selected"] = true;
        i++;
    }
    $(".selectwho input").prop("checked","checked");
    tick();
}
function delSingleNode(){
    var nodes=root.nodes,i=0;
    while ( i < nodes.length){
        if (findNodeRelation(nodes[i])==0)
            root.nodes.splice(i,1);
        else
            i++;
    }
}
function findNodeRelation(n){
    var num= 0,i= 0,links=root.links;
    while ( i < links.length){
        if(links[i]['source']== n || links[i]['target'] ==n) num++ ;
        i++;
    }
    return num;
}
function findNode(id){
    var nodes=root.nodes;
    for (var i in nodes){
        if (nodes[i]['id']==id ) return nodes[i];
    }
    return null;
}
function findNodeIndex(id){
    var nodes=root.nodes;
    for (var i in nodes){
        if (nodes[i]['id']==id) return i;
    }
    return -1;
}
//新需求
function doRfilter(){
    var nodes=root.nodes,i=0;

    while (i < nodes.length) {
        if (nodes[i].group==1 && findNodeRelation(nodes[i]) < 2)
            delNode(i);
        else
            i++;
    }
    delSingleNode();
    update();
}
//筛选
function doFilter(){
    //筛选
    var nodes=root.nodes,i=0;
    if(parseInt($('#filterId').val())==0) {
        if (parseInt($('#filterOpt').val()) == 1) {
            while (i < nodes.length) {
                if (findNodeRelation(nodes[i]) < parseInt($('#filterNum').val()))
                    delNode(i);
                else
                    i++;
            }
        }
        else {
            while (i < nodes.length) {
                if (findNodeRelation(nodes[i]) > parseInt($('#filterNum').val()))
                    delNode(i);
                else
                    i++;
            }
        }
    }
    else{
        while (i < nodes.length) {
            if (nodes[i].group == parseInt($('#filterId').val()))
                delNode(i);
            else
                i++;
        }
    }
    delSingleNode();
    update();
}
function searchNode(kw){
    $("#nodesearch").val(kw);
    $("#nodesearch").change();
}