/**
 * Created by Administrator on 2016/12/1.
 */
$(document).ready(function (){
    $(".tab_div ul").not(":first").hide();
    $(".tab_div .tab_nav a").unbind("click").bind("click", function(){
        $(this).siblings("a").removeClass("addactive").end().addClass("addactive");
        var index = $(".tab_div .tab_nav a").index( $(this) );
        $(".tab_div  ul").eq(index).siblings(".tab_div  ul").hide().end().fadeIn("slow");
    })
    $(".criteria li a ").click( function (){
        $(this).parent().parent().find('a').removeClass("addTable")
        $(this).addClass("addTable");
    })

    $(".sideMenu_left  span a").each(function (){
        $this=$(this);
        if(String(window.location).lastIndexOf($this[0])==0){
            $(this).siblings("a").removeClass("sidebar_left_addative").end().addClass("sidebar_left_addative");
        }

    })
})