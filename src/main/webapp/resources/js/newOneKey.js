
$(function() {

    $('#invertCheck').click(function(){
        var ids = $("#grid-table").jqGrid("getDataIDs");
        for(var i=0;i<ids.length;i++)
        {
            console.log(ids[i]);
            $("#grid-table").jqGrid("setSelection", ids[i]);
        }
    });
    $('#reselect').click(function(){
        $("#grid-table").resetSelection();
    });

    $('#checkAll').click(function(){

        $("#grid-table").resetSelection();
        var ids = $("#grid-table").jqGrid("getDataIDs");
        for(var i=0;i<ids.length;i++)
        {
            console.log(ids[i]);
            $("#grid-table").jqGrid("setSelection", ids[i]);
        }

    });
    $('#btnDownload').click(function(){
        $('#gotodownloaderForm').submit();

    });
    $('#createSet').click(function () {

        var myidarr=new Array();
        var selectedIds = $("#grid-table").jqGrid("getGridParam", "selarrrow");
        var ids = $("#grid-table").jqGrid("getDataIDs");
        if(selectedIds.length)
        {
            for(var i=0;i<selectedIds.length;i++)
            {
                console.log(selectedIds[i]);
                var selectedRow = $("#grid-table").jqGrid("getRowData", selectedIds[i]);
                var cid = selectedRow["collectId"];
                myidarr.push(cid);
            }
        }
        var value="";
        var array=uniQueue(myidarr);

        for ( var i=0 ; i < array.length ; ++i ){
            value+=array[i]+":";
        }
        console.log(value);
        if(value.length == 0) alertify.alert("选择创建内容");
        else{

            alertify.prompt('输创建集合的名字','',function(evt,value){
                var arr = new Array();
                var nvalue = '';
                ($("[type = 'checkbox']:checked").each(function () {
                    arr.push($(this).parent().siblings().eq(9).text());
                }));
                var array=uniQueue(arr);
                for ( var i=0 ; i < array.length ; i++ ){
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
        }
    });

    $('#extend').click(function () {
        var value = '';
        var mynumarr=new Array();
        var selectedIds = $("#grid-table").jqGrid("getGridParam", "selarrrow");
        var ids = $("#grid-table").jqGrid("getDataIDs");
        if(selectedIds.length)
        {
            for(var i=0;i<selectedIds.length;i++)
            {
                console.log(selectedIds[i]);
                var selectedRow = $("#grid-table").jqGrid("getRowData", selectedIds[i]);
                var mobile = selectedRow["mobile"];
                mynumarr.push(mobile);
            }
        }
        var array= uniQueue(mynumarr);
        for ( var i=0 ; i < array.length ; ++i ){
            value+=array[i]+":";
        }
        console.log(value);
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