package cn.com.sinofaith.util;

import cn.com.sinofaith.form.cftForm.CftTjjgForm;
import cn.com.sinofaith.form.cftForm.CftTjjgsForm;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;

public class CreatePdfUtils {

    public static void createHead(Document document, String title, Font blackFont, Font textFont, String ajName) throws DocumentException {
        //规格名称
        Paragraph p = new Paragraph(title, blackFont);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        PdfPTable head = new PdfPTable(1);
        head.setTotalWidth(new float[]{520}); //设置列宽
        head.setLockedWidth(true); //锁定列宽
        head.setSpacingBefore(3f); // 前间距
        head.setSpacingAfter(3f); // 后间距
        PdfPCell cell1 = new PdfPCell(new Phrase(ajName, textFont));
        cell1.setBorderWidth(0);
        cell1.setMinimumHeight(20); //设置单元格高度
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER); //设置水平居中
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE); //设置垂直居中
        head.addCell(cell1);
        document.add(head);
    }

    public static PdfPTable createTable(List list, Font tableFont, Font tableFont1, Font boldFont, Font boldFont1, String columnNames[], int rowNum, int flag) {
        PdfPTable table = new PdfPTable(columnNames.length);
        table.setWidthPercentage(100); // 宽度100%填充
        table.setSpacingBefore(10f); // 前间距
        table.setSpacingAfter(10f); // 后间距
        List<PdfPRow> listRow = table.getRows();
        //行1
        PdfPCell cells[] = new PdfPCell[columnNames.length];
        PdfPRow row = new PdfPRow(cells);
        //设置列名
        for (int i = 0; i < columnNames.length; i++) {
            if(i == rowNum){
                cells[i] = new PdfPCell(new Paragraph(columnNames[i], boldFont1));//单元格内容
            }else{
                cells[i] = new PdfPCell(new Paragraph(columnNames[i], boldFont));//单元格内容
            }
            cells[i].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
            cells[i].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
            cells[i].setFixedHeight(26f);
        }
        //把第一行添加到集合
        listRow.add(row);
        if (null != list && list.size() >= 1) {
            // 实体类
            String[] obj = null;
            for (int i = 0; i < list.size(); i++) {
                if(flag == 0){
                    obj = list.get(i).toString().split("//");
                }else{
                    if (list.get(i) instanceof CftTjjgsForm) {
                        CftTjjgsForm cft = (CftTjjgsForm) list.get(i);
                        if(flag == 1) {
                            obj = cft.toStrings().split("//");
                        }else if(flag == 2) {
                            obj = cft.toStringss().split("//");
                        }
                    }
                    if (list.get(i) instanceof CftTjjgForm) {
                        if(flag == 1) {
                            CftTjjgForm cft = (CftTjjgForm) list.get(i);
                            obj = cft.toStrings().split("//");
                        }
                    }
                }
                PdfPCell cells1[] = new PdfPCell[columnNames.length];
                PdfPRow row1 = new PdfPRow(cells1);
                cells1[0] = new PdfPCell(new Paragraph(String.valueOf(i + 1), tableFont));//单元格内容
                cells1[0].setFixedHeight(20f);
                cells1[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells1[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                for (int j = 0; j < obj.length; j++) {
                    if(j == rowNum-1){
                        cells1[j + 1] = new PdfPCell(new Paragraph(obj[j] != null ? obj[j] : "", tableFont1));//单元格内容
                    }else{
                        cells1[j + 1] = new PdfPCell(new Paragraph(obj[j] != null ? obj[j] : "", tableFont));//单元格内容
                    }
                    cells1[j + 1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                    cells1[j + 1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                }
                listRow.add(row1);
            }
        }
        return table;
    }
}
