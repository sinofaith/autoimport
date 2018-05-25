/**
 * Created by zhengjiabin on 15/2/12.
 */

function centering(self, svgDom) {

    var centerPoint = {
        x: svgDom.offset().left + svgDom.width() / 2,
        y: svgDom.offset().top + svgDom.height() / 2
    };


    var transX, transY, scale;
    if (window.__VIZ_TRANSFORM != undefined &&
        !isNaN(window.__VIZ_TRANSFORM.transX) && !isNaN(window.__VIZ_TRANSFORM.transY) &&
        window.__VIZ_TRANSFORM.transX != undefined && window.__VIZ_TRANSFORM.transY != undefined) {
        transX = window.__VIZ_TRANSFORM.transX + (centerPoint.x - $(self).offset().left);
        transY = window.__VIZ_TRANSFORM.transY + (centerPoint.y - $(self).offset().top);
        scale = window.__VIZ_TRANSFORM.scale;
    } else {
        transX = centerPoint.x - $(self).offset().left;
        transY = centerPoint.y - $(self).offset().top;
        scale = 1;
    }

    svgDom.find('>g').attr('transform', 'translate('+transX+','+transY+') scale('+scale+')');

    window.__VIZ_TRANSFORM = {
        transX: transX,
        transY: transY,
        scale: scale
    };

}