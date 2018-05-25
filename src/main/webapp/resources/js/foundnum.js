$(document).ready(function (){
        var num = $(".tel_number").text();
        $(".tel_content i").each(function(){
            if($(this).text()==num){
                $(this).css("color","#f00");
            }
        });
})