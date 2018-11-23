function hiddenZfbCft() {
    var flg = 0

    if($("#checkbox1").is(":checked")){
        flg=1
    }
    location.href= "/SINOFAITH/bankgtzh/hiddenZfbCft?code="+flg;
}