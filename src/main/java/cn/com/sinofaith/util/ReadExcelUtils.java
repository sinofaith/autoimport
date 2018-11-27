package cn.com.sinofaith.util;

import cn.com.sinofaith.bean.bankBean.BankZcxxEntity;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class ReadExcelUtils {
    private Workbook wb;
    private Sheet sheet;
    private Row row;

    public ReadExcelUtils(String filepath) {
        if(filepath==null){
            return;
        }
        String ext = filepath.substring(filepath.lastIndexOf("."));
        try {
            InputStream is = new FileInputStream(filepath);
            if(".xls".equals(ext)){
                wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(ext)){
                wb = new XSSFWorkbook(is);
            }else{
                wb=null;
            }
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    /**
     * 读取Excel表格表头的内容
     *
     * @return String 表头内容的数组
     */
    public String[] readExcelTitle() throws Exception{
        if(wb==null){
            throw new Exception("Workbook对象为空！");
        }
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            if(row.getCell(i)!=null) {

                title[i] = getCellValue(row.getCell(i));
            }
        }
        return title;
    }

    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString().trim();
            case Cell.CELL_TYPE_NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());

            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    /**
     * 读取Excel数据内容
     *
     * @return Map 包含单元格数据内容的Map对象
     * @author zengwendong
     */
    public Map<Integer, Map<Integer,Object>> readExcelContent() throws Exception{
        if(wb==null){
            throw new Exception("Workbook对象为空！");
        }
        Map<Integer, Map<Integer,Object>> content = new HashMap<Integer, Map<Integer,Object>>();
        int sheetNum = wb.getNumberOfSheets();

        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for(int x =0;x<sheetNum;x++) {
            sheet = wb.getSheetAt(x);
            for (int i = 1; i <= rowNum; i++) {
                row = sheet.getRow(i);
                int j = 0;
                Map<Integer, Object> cellValue = new HashMap<Integer, Object>();
                while (j < colNum) {
                    Object obj = getCellFormatValue(row.getCell(j));
                    cellValue.put(j, obj);
                    j++;
                }
                content.put(i, cellValue);
            }
        }
        return content;
    }

    /**
     *
     * 根据Cell类型设置数据
     *
     * @param cell
     * @return
     * @author zengwendong
     */
    private Object getCellFormatValue(Cell cell) {
        Object cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:// 如果当前Cell的Type为NUMERIC
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA: {
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_STRING:// 如果当前Cell的Type为STRING
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                default:// 默认的Cell值
                    cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    public static void main(String[] args) {
        try {
            String filepath = "D:\\work\\数据模型\\资金\\开户信息\\农行开户基本信息表.xlsx";
            ReadExcelUtils excelReader = new ReadExcelUtils(filepath);
////             对读取Excel表格标题测试
//			String[] title = excelReader.readExcelTitle();
//			System.out.println("获得Excel表格的标题:");
//			for (String s : title) {
//				System.out.print(s + " ");
//			}

            // 对读取Excel表格内容测试
            String[] titles = excelReader.readExcelTitle();
            List<String> strTitle = new ArrayList<>(Arrays.asList(titles));
            Map<String,Integer> title = new HashMap<>();
            for(int i=0;i<strTitle.size();i++){
                String str=strTitle.get(i);
                String lb = "";
                if(str!=null&&str.length()>0) {
                    if (str.contains("账户开户名称")) {
                        lb = "khxm";
                    } else if (str.contains("开户人证件号码")) {
                        lb = "khzjh";
                    } else if (str.contains("交易卡号")) {
                        lb = "yhkzh";
                    } else if (str.contains("账号开户时间")) {
                        lb = "khsj";
                    } else if (str.contains("开户网点")) {
                        lb = "khh";
                    } else if (str.contains("账户状态")) {
                        lb = "zhzt";
                    }
                    title.put(lb, i);
                }
            }

            Map<Integer, Map<Integer,Object>> map = excelReader.readExcelContent();
            List<BankZcxxEntity> bankZcs = new ArrayList<>();
            for(int i=1;i<=map.size();i++){
                if(!bankZcs.contains(BankZcxxEntity.mapToObj(map.get(i),title))){
                    bankZcs.add(BankZcxxEntity.mapToObj(map.get(i),title));
                }
            }
            for (BankZcxxEntity zc:bankZcs){
                System.out.println(zc);
            }
        } catch (FileNotFoundException e) {
            System.out.println("未找到指定路径的文件!");
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
