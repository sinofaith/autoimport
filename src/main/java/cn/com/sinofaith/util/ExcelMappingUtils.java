package cn.com.sinofaith.util;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel映射工具类
 * @author zd
 * create by 2018.11.23
 */
public class ExcelMappingUtils {

    public static String rowValue(Cell cell){
        String rawValue = null;
        if(cell == null){
            rawValue = "";
        }else{
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    rawValue = cell.getRichStringCellValue().getString().trim();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    NumberFormat df = NumberFormat.getInstance();
                    rawValue = df.format(cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    rawValue =String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    rawValue =cell.getCellFormula();
            }
        }
        return rawValue;
    }
    /**
     * 读取2007版数据映射
     * @param path
     * @return
     */
    public static Map<String,List<String>> getBy2007Excel(String path) {
        // 所有sheet工作簿数据
        Map<String,List<String>> sheetMap = new HashMap<>();
        // 数据内容
        File file = new File(path);
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(file);
            Workbook wk = StreamingReader.builder()
                    .rowCacheSize(2)  //缓存到内存中的行数，默认是10
                    .bufferSize(512)  //读取资源时，缓存到内存的字节大小，默认是1024
                    .open(fi);  //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
            for (int numSheet = 0; numSheet < wk.getNumberOfSheets(); numSheet++) {
                List<String> readList = new ArrayList<String>();
                Sheet sheet = wk.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }
                int temp = 0;
                for(Row row : sheet){
                    if (row != null && temp < 2) {
                        int cellNum = row.getLastCellNum();
                        for (int i = 0; i < cellNum; i++) {
                            String rawValue = null;
                            Cell cell = row.getCell(i);
                            if(cell == null){
                                rawValue = "";
                            }else{
                                switch (cell.getCellType()) {
                                    case Cell.CELL_TYPE_STRING:
                                        rawValue = cell.getRichStringCellValue().getString().trim();
                                        break;
                                    case Cell.CELL_TYPE_NUMERIC:
                                        NumberFormat df = NumberFormat.getInstance();
                                        rawValue = df.format(cell.getNumericCellValue());
                                        break;
                                    case Cell.CELL_TYPE_BOOLEAN:
                                        rawValue =String.valueOf(cell.getBooleanCellValue());
                                        break;
                                    case Cell.CELL_TYPE_FORMULA:
                                        rawValue =cell.getCellFormula();
                                }
                            }
                            if(rawValue==null){
                                readList.add("");
                            }else{
                                readList.add(rawValue);
                            }
                        }
                        temp++;
                    }
                    if(temp>1){
                        break;
                    }
                }
                if(readList.size()>0){
                    sheetMap.put(sheet.getSheetName(),readList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fi!=null){
                try {
                    fi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sheetMap;
    }

    /**
     * 获取2003版excel.xls版本数据
     * @param path
     * @return
     */
    public static Map<String,List<String>> getBy2003Excel(String path) {
        InputStream is = null;
        Map<String,List<String>> sheetMap = new HashMap<>();
        try{
            is = new FileInputStream(path);
            HSSFWorkbook wb = new HSSFWorkbook(is);
            for(int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++){
                List<String> readList = new ArrayList<>();
                HSSFSheet sheet = wb.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }
                for (int rowNum = 0; rowNum <= 1; rowNum++) {
                    HSSFRow row = sheet.getRow(rowNum);
                    if (row != null) {
                        int cell = row.getLastCellNum();
                        for (int i = 0; i < cell; i++) {
                            String rawValue = null;
                            if(row.getCell(i)==null){
                                rawValue = "";
                            }else{
                                row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
                                rawValue = row.getCell(i).getStringCellValue();
                            }
                            readList.add(rawValue);
                        }
                    }
                }
                if(readList.size()>0){
                    sheetMap.put(sheet.getSheetName(),readList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(is!=null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sheetMap;
    }
}
