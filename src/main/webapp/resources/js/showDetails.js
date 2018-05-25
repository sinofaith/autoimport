/**
 * Created by zhengjiabin on 14/12/17.
 */

var DetailsTool = (function() {

    var tool = {};
    var containerId = '#tj_DetailsDiv';

//    详情信息
    var type,  // 手机号，qq, 微信
        sourceNumber, // 查看源的号码
        image, // 采集人图片
        name,
        birthPlace,
        mobile,
        objectId,
        caseId,
        certificateType,
        certificateId,
        collectId,
        policeCode,
        areaCode,
        address,
        takeTime,
        policeName,
        policeId,
        ip,
        deviceNum,
        uploadTime;

    // fancybox 设置
    // Disable opening and closing animations, change title type
    $(".fancybox").fancybox({
        openEffect  : 'none',
        closeEffect	: 'none',

        helpers : {
            title : {
                type : 'over'
            }
        }
    });

    tool.setType = function(_type) {
        type = _type == null? '' : _type;
    };

    tool.setSourceNumber = function(_sourceNumber) {
        sourceNumber = _sourceNumber == null? '' : _sourceNumber;
    };

    tool.setImage = function(_image) {
        image = _image == null? '' : _image;
    };

    tool.setName = function(_name) {
        name = _name == null? '' : _name;
    };

    tool.setBirthPlace = function(_birthPlace) {
        birthPlace = _birthPlace == null? '' : _birthPlace;
    };

    tool.setMobile = function(_mobile) {
        mobile = _mobile == null? '' : _mobile;
    };

    tool.setObjectId = function(_objectId) {
        objectId = _objectId == null? '' : _objectId;
    };

    tool.setCaseId = function(_caseId) {
        caseId = _caseId == null? '' : _caseId;
    };

    tool.setCertificateType = function(_certificateType) {
        certificateType = _certificateType == null? '' : _certificateType;
    };

    tool.setCertificateId = function(_certificateId) {
        certificateId = _certificateId == null? '' : _certificateId;
    };

    tool.setCollectId = function(_collectId) {
        collectId = _collectId == null? '' : _collectId;
    };

    tool.setPoliceCode = function(_policeCode) {
        policeCode = _policeCode == null? '' : _policeCode;
    };

    tool.setAreaCode = function(_areaCode) {
        areaCode = _areaCode == null? '' : _areaCode;
    };

    tool.setAddress = function(_address) {
        address = _address == null? '' : _address;
    };

    tool.setTakeTime = function(_takeTime) {
        takeTime = _takeTime == null? '' : _takeTime;
        takeTime = takeTime == ''? '' : toDateStr(takeTime);
    };

    tool.setPoliceName = function(_policeName) {
        policeName = _policeName == null? '' : _policeName;
    };

    tool.setPoliceId = function(_policeId) {
        policeId = _policeId == null? '' : _policeId;
    };

    tool.setIp = function(_ip) {
        ip = _ip == null? '' : _ip;
    };

    tool.setDeviceNum = function(_deviceNum) {
        deviceNum = _deviceNum == null? '' : _deviceNum;
    };

    tool.setUploadTime = function(_uploadTime) {
        uploadTime = _uploadTime == null? '' : _uploadTime;
        uploadTime = uploadTime == ''? '' : toDateStr(uploadTime);
    };


    tool.updateInfo = function(_info) {
        $('#tj_DetailInfo').text(_info);
    };

    tool.removeContent = function() {

        if (containerId == undefined) return;

        $(containerId).children().remove();
    };

    tool.createInfo = function(_info) {

        if (containerId == undefined) return;

        $(containerId).html("<div id='tj_DetailInfo' style='text-align: center;position: relative; top: 50%;'>"+_info+
        "<button style='position: absolute ;top:10px;right: 20px; height: 30px;width: 30px;opacity:0.3' onclick='DetailsTool.hide()'> "+
        "<span  style='font-size: x-large;opacity: 1;color: #181515' >X</span></button>" +


        "</div>");
    };
    tool.createTable = function(_info) {

        if (containerId == undefined) return;

        $(containerId).html("<div id='tj_TableInfo' style='margin:20px 20px;'>"+_info+
        "<button style='position: absolute ;top:10px;right: 20px; height: 30px;width: 30px;opacity:0.3' onclick='DetailsTool.hide()'> "+
        "<span  style='font-size: x-large;opacity: 1;color: #181515' >X</span></button>" +

        "</div>");
    };

    tool.toggle = function() {
        $(containerId).toggle('normal');
    };

    tool.show = function() {
        $(containerId).show('normal');
    };

    tool.hide = function() {
        $(containerId).hide('normal');
    };


    tool.createDetail = function() {

        if (containerId == undefined) return;
        var accountImage = type == 'qq'? '/AMD/resources/img/qqicon.png' :
            (type == 'weixin'? '/AMD/resources/img/weixinicon.png' : '/AMD/resources/img/phoneicon.png');

        $(containerId).html("<div id='tj_Details' style='font-size:13px;font-family: Open Sans;overflow-y:scroll ' >" +
                                "<p id='tj_p_account' style='margin:10px;font-size:medium;'><img id='tj_accountImage' src='"+accountImage+"'>&nbsp;&nbsp;&nbsp;"+'<a target="_blank" href="BasicQuery/detail?id='+collectId+'">'+sourceNumber+"</a>"+"</p>" +
                                "<button style='position: absolute ;top:10px;right: 20px; height: 30px;width: 30px;opacity:0.3' onclick='DetailsTool.hide()'> "+
                                "<span  style='font-size: 20px;opacity: 1;color: #181515' >X</span></button>" +
                                "<a class='fancybox' href='"+image+"' title='"+name+"'><img style='z-index:2;width:80px;position:absolute;left:200px;top:50px;' src='"+image+"'></a>" +
                                "<div style='margin-left:10px;position:relative;top:10px;'>" +
                                    "<p id='tj_p_name'>姓名:"+name+"</p>" +
                                    "<p id='tj_p_birthPlace'>籍贯:"+birthPlace+"</p>" +
                                    "<p id='tj_p_mobile'>手机号:"+mobile+"</p>" +
                                    "<p id='tj_p_objectId'>对象编号:"+objectId+"</p>" +
                                    "<p id='tj_p_caseId'>案件编号:"+caseId+"</p>" +
                                    "<p id='tj_p_certificateType'>身份类型:"+certificateType+"</p>" +
                                    "<p id='tj_p_certificateId'>身份编号:"+certificateId+"</p>" +
                                    "<p id='tj_p_collectId'>采集编号:"+collectId+"</p>" +
                                    "<p id='tj_p_policeCode'>采集单位编号:"+policeCode+"</p>" +
                                    "<p id='tj_p_arraCode'>所属区域:"+areaCode+"</p>" +
                                    "<p id='tj_p_address'>采集地址:"+address+"</p>" +
                                    "<p id='tj_p_takeTime'>采集时间:"+takeTime+"</p>" +
                                    "<p id='tj_p_policeName'>警员姓名:"+policeName+"</p>" +
                                    "<p id='tj_p_policeId'>警员编号:"+policeId+"</p>" +
                                    "<p id='tj_p_ip'>IP地址:"+ip+"</p>" +
                                    "<p id='tj_p_deviceNum'>设备数量:"+deviceNum+"</p>" +
                                    "<p id='tj_p_uploadTime'>上传时间:"+uploadTime+"</p>" +
                                "</div>" +
                            "</div>");
    };

    return tool;


    function toDateStr(a){
        var time = new Date(a).dateFormat("Y-m-d h:m:s");
        return time.toString();
    }
}());