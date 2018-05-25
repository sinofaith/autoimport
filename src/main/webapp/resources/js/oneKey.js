/**
 * Created by Achol.Xu on 2014/9/25.
 */

$(function() {
    $("#result_table").dataTable(  {
        "sScrollX": "100%",
        "sScrollXInner": "110%",
        "bScrollCollapse": true,
        "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ] }]
    } );

    var myidarr=new Array();
    var mynumarr=new Array();

    $('tbody').delegate("[name='item']",{change:function(){
        if(this.checked){
            myidarr.push($(this).val());
            mynumarr.push($(this).parents('tr').children().eq(3).html());
        }else{
            myidarr.splice(myidarr.indexOf($(this).val()),1);
            mynumarr.splice(mynumarr.indexOf($(this).parents('tr').children().eq(3).html()),1);
        }

        console.log(myidarr);
    }})

    $('#checkall').click(function () {

        if(this.checked){
            $("[name='item']").each(function(){
                if(!this.checked){
                    $(this).prop("checked","checked");
                    myidarr.push($(this).val());
                    mynumarr.push($(this).parents('tr').children().eq(3).html());
                }
            })
        }else{
            $("[name='item']").each(function(){
                if(this.checked){
                    $(this).removeAttr("checked");
                    myidarr.splice(myidarr.indexOf($(this).val()),1);
                    mynumarr.splice(mynumarr.indexOf($(this).parents('tr').children().eq(3).html()),1);
                }
            })
        }
        console.log(myidarr);
    });
    $('#createSet').click(function () {
        var value="";
        var array=uniQueue(myidarr);
        for ( var i=0 ; i < array.length ; ++i ){
            value+=array[i]+":";
        }
        if(value.length == 0) alertify.alert("选择创建内容");
        else{

            alertify.prompt('输创建集合的名字','',function(evt,value){
                var arr = new Array();
                var nvalue = '';
                ($("[name = 'item']:checked").each(function () {
                    arr.push($(this).val());
                }));
                var array=uniQueue(arr);
                for ( var i=0 ; i < array.length ; ++i ){
                    nvalue+=array[i]+":";
                }
                $('#setname').val(value);
                $('#cids').val(nvalue);
                var option = {
                    success:function(result){
                        alertify.alert("创建成功");
                    },
                    error:function(){
                        alertify.alert("创建失败"+nvalue);
                    }
                }
                console.log($('#setPost').ajaxSubmit(option));

            });
            //var name=prompt("请输入您创建集合的名字","");
            //$('#setname').val(name);
            //$('#cids').val(value);
            //var option = {
            //    success:function(result){
            //        alert(result);
            //    },
            //    error:function(){
            //        alert("创建失败"+value);
            //    }
            //}
            //console.log($('#setPost').ajaxSubmit(option));
        }
    });
    $('#extend').click(function () {
        var value = '';
        var array=uniQueue(mynumarr);
        for ( var i=0 ; i < array.length ; ++i ){
            value+=array[i]+":";
        }
        if(value.length == 0) alertify.alert("请选择要进行扩展分析的内容");
        else{
            $('#extendform>input').val(value);
            $('#extendform').submit();
        }
    });
})
function uniQueue(array){
    var arr=[];
    var m;
    while(array.length>0){
        m=array[0];
        arr.push(m);
        array=$.grep(array,function(n,i){
            return n==m;
        },true);
    }
    return arr;
}