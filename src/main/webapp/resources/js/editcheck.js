function crimegroupEditCheck(){
    var crimegroupinfoid = $("#crimegroupinfoid").val();
    $.ajax({
        url: "/AMD/crimegroupinfo/editcheck",
        type: 'POST',
        dataType: 'text',
        success: function(result) {
            if(result==="1"){
                if(confirm("你还没有登录，需要登录？")){
                    location="/AMD/homepage";
                }
            } else {
                location="/AMD/crimegroupinfo/updateDetail/"+crimegroupinfoid;
            }
        }
    });
}

function crimeterraceEditCheck(){
    var suspicionerinfoid = $("#suspicionerinfoid").val();
    $.ajax({
        url: "/AMD/crimegroupinfo/editcheck",
        type: 'POST',
        dataType: 'text',
        success: function(result) {
            if(result==="1"){
                if(confirm("你还没有登录，需要登录？")){
                    location="/AMD/homepage";
                }
            } else {
                location="/AMD/crimeterrace/updateDetail/"+suspicionerinfoid;
            }
        }
    });
}