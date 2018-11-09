var radio = -1;
$(function () {
    var old = null; //用来保存原来的对象
    $(":radio").each(function(){
        if(this.checked){
            old = this; //如果当前对象选中，保存该对象
        }
        this.onclick = function(){
            if(this == old){//如果点击的对象原来是选中的，取消选中
                this.checked = false;
                old = null;
                location.href="/SINOFAITH/banktjjg/getByZhzt?code="+-1;
            } else{
                old = this;
                location.href="/SINOFAITH/banktjjg/getByZhzt?code="+$(this).val();
            }
        }
    });

    $(":radio").click(function(){
        alert(this.val());
    });
});

function hiddenZfbCft() {
    var flg = 0

    if($("#checkbox1").is(":checked")){
        flg=1
    }
    location.href= "/SINOFAITH/banktjjg/hiddenZfbCft?code="+flg;
}