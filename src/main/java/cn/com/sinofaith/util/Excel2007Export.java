package cn.com.sinofaith.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class Excel2007Export {
    public static Row createRow(Sheet sheet,String[] title){
        Row row = sheet.createRow(0);
        row.setHeight((short) 350);// 设定行的高度
        sheet.createFreezePane(0,1,0,1);

        for(int i=0;i<title.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }
        return row;
    }
}
