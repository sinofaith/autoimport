$(document).ready(function() {
    $(".criteria li").click(function(){
        $(this).css("background","#636B75").siblings().css("background","#0e3d5b");
    });
})

function logisticsdownload(txt,type){
    location="/AMD/criteriaquery/logistics/download/"+txt+"/"+type;
}

function capitalInfodownload(numbers,txt){
    location="/AMD/criteriaquery/capitalInfo/download/"+numbers+"/"+txt;

}

function callRecords(number,txt){
    location="/AMD/criteriaquery/callRecords/download/"+number+"/"+txt;
}

function callRecordsdb(number,txt){
    location="/AMD/criteriaquery/callRecordsdb/download/"+number+"/"+txt;
}

function bankAccountdownload(sfzh){
    location="/AMD/criteriaquery/bankAccount/download/"+sfzh;
}

function bankTradedownload(iphoneNum,yhzh){
    location="/AMD/criteriaquery/bankTrade/download/"+iphoneNum+"/"+yhzh;
}

function casedownload(sfzh){
    location="/AMD/criteriaquery/case/download/"+sfzh;
}

function jassdownload(number,type){
    location="/AMD/criteriaquery/jass/download/"+number+"/"+type;
}

function zfbzhdownload(sfz){
    location="/AMD/criteriaquery/zfbzh/download/"+sfz;
}

function tbzhdownload(sfz){
    location ="/AMD/criteriaquery/tbzh/download/"+sfz;
}

function tbjydownload(sfz,type){
    location ="/AMD/criteriaquery/tbjy/download/"+sfz+"/"+type;
}

function cftzhdownload(sfz){
    location="/AMD/criteriaquery/cftzh/download/"+sfz;
}
function  cftjydownload(sfz,type){
    location="/AMD/criteriaquery/cftjy/download/"+sfz+"/"+type;
}

function zfbjydownload(zfbzh,type){
    location="/AMD/criteriaquery/zfbjy/download/"+zfbzh+"/"+type;
}

function moneyLaunderingdownload(sfzhm,yhzh){
    location="/AMD/criteriaquery/moneyLaundering/download/"+sfzhm+"/"+yhzh;
}

function qqdownload(iphoneNum,type){
    location="/AMD/criteriaquery/qq/download/"+iphoneNum+"/"+type;
}

function smsdownload(iphoneNum,type){
    location="/AMD/criteriaquery/sms/download/"+iphoneNum + "/"+type;
}

function smsdbdownload(sfzh,iphoneNum){
    location="/AMD/criteriaquery/smsdb/download/"+sfzh+"/"+iphoneNum;
}

function communicatedownload(number,type){
    location="/AMD/criteriaquery/communicate/download/"+number+"/"+type;
}

function communicatedbdownload(sfzm,iphoneNum){
    location="/AMD/criteriaquery/communicatedb/download/"+sfzm+"/"+iphoneNum;
}

function wechatdownload(iphoneNum,type){
    location="/AMD/criteriaquery/wechat/download/"+iphoneNum+"/"+type;
}