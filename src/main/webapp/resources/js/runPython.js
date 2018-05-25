$(document).ready(function(){

    $('#renyuan').click(function() {
        $.ajax({
            type: "get",
            url: "/AMD/runPython/renyuan"
        });
    });

    $('#wuliu').click(function() {
        alert("");
    });

    $('#shouji').click(function() {
        $.ajax({
            type: "get",
            url: "/AMD/runPython/shouji"
        });
    });

    $('#zijin').click(function() {
        alert("");
    });

    $('#QQ').click(function() {
        alert("");
    });

    $('#weixin').click(function() {
        alert("");
    });
    $('#refresh').click(function() {
        location="/AMD/runPython";
    });


});