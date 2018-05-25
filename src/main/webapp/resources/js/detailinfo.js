/**
 * Created by caijiang on 14/12/9.
 */
$(function(){
    $("#b_jjcx").addClass("hover");
    $("#basic").dataTable({
        "ordering":false,
        "filter":false,
        "paging":false
    });
    $("#device").dataTable({
        "ordering":false,
        "filter":false,
        "paging":false
    });
    $('#microblog').dataTable();
    $('#netaccount').dataTable({
        "ordering":false,
        "filter":false
    });
    $('#file').dataTable();
    $('#calendar').dataTable();
    $('#mail').dataTable();
    $('#netlog').dataTable();
    $('table').addClass("cell-border");
    if($('#iminform').length>0){
        $('#iminform').mask("加载中请耐心等待");
        $.ajax({
            url:'/AMD/BasicQuery/detailim',
            type:'POST',
            data: {"value":$('#iminform').attr('name')},
            dataType: 'JSON',
            success: function(data) {
                // console.log(data);
                $.each(data,function(idx,item){
                    $('#im').append("<tr><td>"+item['fromId']+"</td>" +
                        "<td>" + item['fromNickname'] +"</td>" +
                        "<td>" + item['toId'] +"</td>" +
                        "<td>" + item['toNickname'] +"</td>" +
                        "<td>" + addlinkstyle(item['content']) +"</td>" +
                        "<td>" + toDateStr(item['time']) +"</td>" +
                        "<td>" + convert(item['type']) +"</td>" +
                        "<td>" + convert(item['imType']) +"</td>"  +
                        "<td>" + item['username'] +"</td>" +
                        "<td>" + item['groupname'] +"</td>" +
                        "<td>" + item['groupnum'] +"</td>" +
                        "<td>" + mesgtag(item['action']) +"</td>" +
                        "<td>" + isdelete(item['isDeleted'])  +"</td>" +
                        "</tr>"
                    );
                });
                $('#im').dataTable();
                $('#iminform').unmask();
            },
            error: function () {
                $('#iminform').unmask();
            }
        });
    }
    if($('#smsinform').length>0){
        $('#smsinform').mask("正在加载中请耐心等待");
        $.ajax({
            url:'/AMD/BasicQuery/detailsms',
            type:'POST',
            data: {"value":$('#smsinform').attr('name')},
            dataType: 'JSON',
            success: function(result) {
                // console.log(data);
                $.each(result,function(idx,item){
                    $('#sms').append("<tr><td>"+item['name']+"</td>" +

                        "<td>" + item['number'] +"</td>" +
                        "<td>" + toDateStr(item['time']) +"</td>" +
                        "<td>" + convert(item['type']) +"</td>" +
                        "<td>" + item['content'] +"</td>" +
                        "<td>" + isview(item['isview']) +"</td>" +
                        "<td>" + isdelete(item['isDeleted']) +"</td>" +
                        "</tr>"
                    );
                  });
                $('#sms').dataTable();

                $('#smsinform').unmask();
            },
            error: function () {
                $('#smsinform').unmask();
            }
        });
    }
    if($('#friendinform').length>0){
        $('#friendinform').mask("正在加载中请耐心等待");
        $.ajax({
            url:'/AMD/BasicQuery/detailfriendList',
            type:'POST',
            data: {"value":$('#friendinform').attr('name')},
            dataType: 'JSON',
            success: function(data) {
                // console.log(data);
                $.each(data,function(idx,item){
                    $('#friend').append("<tr><td>"+convertIm(item['imType'])+"</td>" +
                        "<td>" + item['username'] +"</td>" +
                        "<td>" + item['friendId'] +"</td>" +
                        "<td>" + item['friendNickname'] +"</td>" +
                        "<td>" + item['groupId'] +"</td>" +
                        "<td>" + isview(item['displayName']) +"</td>" +
                        "<td>" + convertfriendtype(item['friendType']) +"</td>" +
                        "<td>" + isdelete(item['isDeleted']) + "</td>" +
                        "</tr>"
                    );
                });
                $('#friend').dataTable();
                $('#friendinform').unmask();
            },
            error: function () {
                $('#friendinform').unmask();
            }
        });
    }
    if($('#callloginform').length>0){
        $('#callloginform').mask("正在加载中请耐心等待");
        $.ajax({
            url:'/AMD/BasicQuery/detailcalllog',
            type:'post',
            data :{"value":$('#callloginform').attr('name')},
            dataType:'JSON',
            success:function(data){
                $.each(data,function(idx,item){
                    $('#calllog').append(
                        "<tr><td>" + item['name'] + "</td>" +
                        "<td>" + item['number'] + "</td>" +
                        "<td>" + toDateStr(item['startTm']) + "</td>" +
                        "<td>" + toDateStr(item['endTm']) + "</td>" +
                        "<td>" + convertcalllogType(item['type']) + "</td>"  +
                        "<td>" + convertcalllogisconnect(item['isConnect']) + "</td>" +
                        "<td>" + isdelete(item['isDelete']) + "</td>" + "</tr>"
                    );
                });
               $('#calllog').dataTable();
                $('#callloginform').unmask();
            },
            error:function(){
                $('#callloginform').unmask();
            }
        });
    }
    if($('#phonedirinform').length>0){
        $('#phonedirinform').mask("正在加载中请耐心等待");
        $.ajax({
            url:'/AMD/BasicQuery/detailphonedir',
            type:'post',
            data :{"value":$('#phonedirinform').attr('name')},
            dataType:'JSON',
            success:function(data) {
                $.each(data, function (idx, item) {
                    $('#phonedir').append(
                        "<tr><td>" + item['name'] + "</td>" +
                        "<td>" + item['number'] + "</td>" +
                        "<td>" + item['email'] + "</td>" +
                        "<td>" + phonedirPosition(item['position']) + "</td>" +
                        "<td>" + phoneType(Number(item['type'])) + "</td>" +
                        "<td>" + item['group'] + "</td>" +
                        "<td>" + item['postal'] + "</td>" +
                        "<td>" + isdelete(item['isDelete']) + "</td>" +
                        "<td>" + item['remark'] + "</td>" + "</tr>"
                    );
                });
                $('#phonedir').dataTable();
                $('#phonedirinform').unmask();
            },
            error:function(){
                $('#phonedirinform').unmask();
            }
        });
    }
    if($('#gpsinform').length>0){
        $('#gpsinform').mask('正在加载中请耐心等待');
        $.ajax({
            url:'/AMD/BasicQuery/detailgps',
            type:'post',
            data :{'value':$('#gpsinform').attr('name')},
            dataType:'json',
            success:function(data){
                $.each(data,function(idx,item){
                    $('#gps').append(
                        "<tr><td>" + item['gpsId'] + "</td>" +
                            "<td>" + item['longitude'] + "</td>" +
                            "<td>" + item['latitude'] + "</td>" +
                            "<td>" + item['time'] + "</td>" +
                            "<td>" + item['gpsType'] + "</td>" +
                            "<td>" + item['name'] + "</td>" +
                            "<td>" + item['address'] + "</td>" +
                            "<td>" + item['url'] + "</td>" +
                            "<td>" + item['remark'] + "</td>" +
                            "<td>" + isdelete(item['isDeleted'])+"</td>" +"</tr>"
                     );
                });
                $('#gps').dataTable();
                $('#gpsinform').unmask();
            },
            error:function(){
                $('#gpsinform').unmask();
            }

        });
    }
    function phoneType(a){

        switch(a)
        {
            case 1:{
                return "移动电话";
            }

            case 2:
            {
                return "移动电话";
            }
            case 3:
                return "工作电话";
            case 4:
                return "其他电话";

            case 5:
                return "EMAIL";
            case 6:
                return "住宅地址";
            case 7:
                return "公司地址";
            case 8:
                return "其他地址";
            case 9:
                return "其他信息";
            default :
                return "其他";
        }
    }
    function phonedirPosition(a){
        if(a==1) return "手机";
        if(a==2) return "SIM卡";
        else return "其他";

    }
    function convert(a){
        if(a==1) return "发送";
        if(a==2) return "接收";
        if(a==1001) return "QQ";
        if(a==3034) return "陌陌";
        if(a == 3100) return "微信";
    }
    function mesgtag(a){
        if(a==1) return "好友信息";
        if(a==2) return "群消息";
        if(a==3) return "系统消息";
        else return "其他";
    }
    function isdelete(a){
        if(a==1) return "删除";
        else return "未删除";

    }
    function isview(a){
        if(a==1)return "已读";
        if(a==2)return "未读";
        else return "其他";
    }
    function convertIm(a){
        if(a==1001) return "QQ";
        if(a==3100) return "微信";
        if(a == 2141) return "新浪微博";
        if(a==3019) return "旺旺";
        if(a==3032) return "Skype";
        else return "其他";
    }
    function convertfriendtype(a){
        if(a==1) return "一般好友";
        if(a==2) return "群成员";
        if(a ==3) return "收听";
        if(a==4) return "听众";

        else return "其他";
    }
    function convertcalllogType(a){
        if(a==1) return "拨打";
        if(a==2) return "接听";
        else return "其他";
    }
    function convertcalllogisconnect(a){
        if(a==1) return "未接";
        if(a==2) return "接通";
        else return "其他";

    }
    function toDateStr(a){
        var time = new Date(a).dateFormat("Y-m-d h:m:s");
        return time.toString();
    }
    function addlinkstyle(content){
        var Exp = /[a-zA-z]+:\/\/[^\s]*/g;
        return content.replace(Exp,'<a href="javascript:void(0);">$&</a>');
    }
});