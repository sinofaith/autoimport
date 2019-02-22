package cn.com.sinofaith.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;

/**
 * 生成图片水印
 * @author zd
 * create by 2019.01.21
 */
public class WatermarkImageUtils extends PdfPageEventHelper {


    @Override
    public void onStartPage(PdfWriter pdfWriter, Document document) {
        Rectangle pageRect = pdfWriter.getPageSize();
        int width = (int) pageRect.getWidth();
        int height = (int) pageRect.getHeight();
        // 加入水印
        PdfContentByte waterMar = pdfWriter.getDirectContentUnder();
        // 开始设置水印
        waterMar.beginText();
        // 设置水印透明度
        PdfGState gs = new PdfGState();
        gs.setFillOpacity(0.1f);
        // 设置笔触字体不透明度为0.4f
        gs.setStrokeOpacity(0.4f);
        Image image = null;
        try {
            String path = WatermarkImageUtils.class.getClassLoader()
                    .getResource("../../resources/image/sinofaith.png").getPath();
            image = Image.getInstance(path);
            for (int i = 0; i <= width; i += 70) {
                for (int j = 0; j <= height+30; j += 90) {
                    // 设置坐标 绝对位置 X Y
                    image.setAbsolutePosition(i, 1500 - j);
                    // 设置旋转弧度
                    image.setRotation(30);// 旋转 弧度
                    // 设置旋转角度
                    image.setRotationDegrees(45);// 旋转 角度
                    // 设置等比缩放
                    image.scalePercent(60);// 依照比例缩放
                    // image.scaleAbsolute(200,100);//自定义大小
                    // 设置透明度
                    waterMar.setGState(gs);
                    // 添加水印图片
                    waterMar.addImage(image);
                    // 设置透明度
                    waterMar.setGState(gs);
                }
            }
            //结束设置
            waterMar.endText();
            waterMar.stroke();
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
