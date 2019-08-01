package cn.com.sinofaith.util;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * excel映射工具类
 * @author zd
 * create by 2018.11.23
 */
public class MappingUtils {

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
        if(rawValue==null)
            return "";
        else
            return rawValue;
    }

    /**
     * 读取2007版数据映射 表头和一行数据
     * @param path
     * @return
     */
    public static Map<String,List<String>>
    getBy2007Excel(String path) {
        // 所有sheet工作簿数据
        Map<String,List<String>> sheetMap = new LinkedHashMap<>();
        // 数据内容
        File file = new File(path);
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(file);
            Workbook wk = StreamingReader.builder()
                    .rowCacheSize(10)  //缓存到内存中的行数，默认是10
                    .bufferSize(512)  //读取资源时，缓存到内存的字节大小，默认是1024
                    .open(fi);  //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
            for (int numSheet = 0; numSheet < wk.getNumberOfSheets(); numSheet++) {
                List<String> readList = new ArrayList<String>();
                Sheet sheet = wk.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }
                int temp = 0;
                // 字段长度
                int cellNum = 0;
                for(Row row : sheet){
                    if (row != null && temp < 2) {
                        if(temp==0){
                            cellNum = row.getLastCellNum();
                        }
                        // 字段少于三个不添加
                        if(cellNum<3){
                            continue;
                        }
                        for (int i = 0; i < cellNum; i++) {
                            Cell cell = row.getCell(i);
                            String rowValue = rowValue(cell);
                            if(rowValue==null){
                                readList.add("");
                            }else{
//                                if(rowValue.contains("：")){
//                                    String reg = "[^：]+：(.+)";
//                                    Pattern pet = Pattern.compile(reg);
//                                    Matcher matcher = pet.matcher(rowValue);
//                                    while (matcher.find()) {
//                                        rowValue = matcher.group(1);
//                                    }
//                                }
                                readList.add(rowValue);
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
     * 获取2003版excel.xls版本 表头和一行数据
     * @param path
     * @return
     */
    public static Map<String,List<String>> getBy2003Excel(String path) {
        InputStream is = null;
        Map<String,List<String>> sheetMap = new LinkedHashMap<>();
        try{
            is = new FileInputStream(path);
            HSSFWorkbook wb = new HSSFWorkbook(is);
            for(int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++){
                List<String> readList = new ArrayList<>();
                List<String> tempList;
                HSSFSheet sheet = wb.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }
                int lastRowNum = sheet.getLastRowNum();
                int temp = 0;
                for (int rowNum = 0; rowNum <= lastRowNum; rowNum++) {
                    HSSFRow row = sheet.getRow(rowNum);
                    if (row != null) {
                        int cell = row.getLastCellNum();
                        // 字段少于三个不添加
                        if(cell<3){
                            continue;
                        }
                        int a = 0;
                        tempList = new ArrayList<>();
                        for (int i = 0; i < cell; i++) {
                            String rawValue = null;
                            if(row.getCell(i)==null){
                                rawValue = "";
                            }else{
                                row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
                                rawValue = row.getCell(i).getStringCellValue();
                            }
                            tempList.add(rawValue);
                            if(rawValue.trim().length()>0){
                                a+=1;
                            }
                        }
                        if(a<3){
                            continue;
                        }else{
                            readList.addAll(tempList);
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

    /**
     * 读取html文件table表头和一行数据
     * @param path
     * @return
     */
    public static List<String> getByJsoupHtml(String path) {
        List<String> dataList = new ArrayList<>();
        File in = new File(path);
        try {
            // 获取文档对象
            Document doc = Jsoup.parse(in, "utf-8");
            Element table = doc.select("table").get(1);
            Elements trs = table.select("tr");
            // 标题
            if(trs.size()>2 && trs!=null){
                for(int i=0;i<2;i++){
                    Element element = trs.get(i);
                    String text = element.text();
                    String[] split = text.split(" ");
                    for(int j=0;j<split.length;j++){
                        if(split[j].contains("：")){
                            String reg = "[^：]+：(.+)";
                            Pattern pet = Pattern.compile(reg);
                            Matcher matcher = pet.matcher(split[j]);
                            while (matcher.find()) {
                                split[j] = matcher.group(1);
                            }
                        }
                        dataList.add(split[j]);
                    }
                }
            }else if(trs!=null){
                for(int i=0;i<trs.size();i++){
                    Element element = trs.get(i);
                    String text = element.text();
                    String[] split = text.split(" ");
                    for(int j=0;j<split.length;j++){
                        if(split[j].contains("：")){
                            String reg = "[^：]+：(.+)";
                            Pattern pet = Pattern.compile(reg);
                            Matcher matcher = pet.matcher(split[j]);
                            while (matcher.find()) {
                                split[j] = matcher.group(1);
                            }
                        }
                        dataList.add(split[j]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * String数据转换
     * @param xssfRow
     * @param fieldValue
     * @param title
     * @return
     */
    public static String mappingFieldString(Row xssfRow, String fieldValue, Map<String, Integer> title){
        String value = null;
        if(fieldValue.equals("无") || xssfRow.getCell(title.get(fieldValue))==null){
            value = "";
        }else{
            value = rowValue(xssfRow.getCell(title.get(fieldValue))).replace(",","");
        }
        return value;
    }

    /**
     * BigDecimal数据转换
     * @param xssfRow
     * @param fieldValue
     * @param title
     * @return
     */
    public static BigDecimal mappingFieldBigDecimal(Row xssfRow, String fieldValue, Map<String, Integer> title){
        BigDecimal value;
        if("无".equals(fieldValue) || xssfRow.getCell(title.get(fieldValue))==null){
            value = new BigDecimal(-1);
        }else{
            String fieV = MappingUtils.rowValue(xssfRow.getCell(title.get(fieldValue)));
            value = new BigDecimal("".equals(fieV) ? "-1" : fieV.replace(",", ""));
        }
        return value;
    }

    /**
     * 删除文件
     * @param uploadPathd
     */
    public static void deleteFile(File uploadPathd){
        if(uploadPathd.exists()){
            for(File file : uploadPathd.listFiles()){
                if(file.isFile()){
                    file.delete();
                }else{
                    deleteFile(file);
                }
            }
        }
        uploadPathd.delete();
    }
}
