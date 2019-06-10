package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgsEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbZzmxDao;
import cn.com.sinofaith.dao.zfbDao.ZfbZzmxTjjgDao;
import cn.com.sinofaith.dao.zfbDao.ZfbZzmxTjjgsDao;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxTjjgForm;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxTjjgssForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.util.MappingUtils;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 支付宝转账明细业务层
 * @author zd
 * create by 2018.11.28
 */
@Service
public class ZfbZzmxService {
    @Autowired
    private ZfbZzmxDao zfbZzmxDao;
    @Autowired
    private ZfbZzmxTjjgDao zfbZzmxTjjgDao;
    @Autowired
    private ZfbZzmxTjjgsDao zfbZzmxTjjgsDao;

    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = new Page();
        List<ZfbZzmxEntity> zzmxForms = null;
        int rowAll = zfbZzmxDao.getRowAll(dc);
        if(rowAll>0){
            zzmxForms = zfbZzmxDao.getDoPage(currentPage,pageSize,dc);
            if(zzmxForms!=null){
                page.setPageSize(pageSize);
                page.setList(zzmxForms);
                page.setTotalRecords(rowAll);
                page.setPageNo(currentPage);
            }
        }
        return page;
    }

    /**
     * 支付宝转账明细数据导出
     * @param dc
     * @return
     */
    public List<ZfbZzmxEntity> getZfbZzmxAll(DetachedCriteria dc) {
        List<ZfbZzmxEntity> zzmxs = null;
        int rowAll = zfbZzmxDao.getRowAll(dc);
        if(rowAll>0){
            zzmxs = zfbZzmxDao.getDoPageAll(dc);
        }
        return zzmxs;
    }

    /**
     * 导出excel表
     * @param zzmxs
     * @return
     */
    public HSSFWorkbook createExcel(List<ZfbZzmxEntity> zzmxs) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("支付宝转账明细");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("交易号");
        cell = row.createCell(2);
        cell.setCellValue("付款方账号");
        cell = row.createCell(3);
        cell.setCellValue("收款方账号");
        cell = row.createCell(4);
        cell.setCellValue("收款机构信息");
        cell = row.createCell(5);
        cell.setCellValue("到账时间");
        cell = row.createCell(6);
        cell.setCellValue("转账金额");
        cell = row.createCell(7);
        cell.setCellValue("转账产品名称");
        cell = row.createCell(8);
        cell.setCellValue("交易发生地");
        cell = row.createCell(9);
        cell.setCellValue("提现流水号");
        int b = 1;
        for(int i=0;i<zzmxs.size();i++) {
            ZfbZzmxEntity wl = zzmxs.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                sheet = wb.createSheet("支付宝转账明细(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("交易号");
                cell = row.createCell(2);
                cell.setCellValue("付款方账号");
                cell = row.createCell(3);
                cell.setCellValue("收款方账号");
                cell = row.createCell(4);
                cell.setCellValue("收款机构信息");
                cell = row.createCell(5);
                cell.setCellValue("到账时间");
                cell = row.createCell(6);
                cell.setCellValue("转账金额");
                cell = row.createCell(7);
                cell.setCellValue("转账产品名称");
                cell = row.createCell(8);
                cell.setCellValue("交易发生地");
                cell = row.createCell(9);
                cell.setCellValue("提现流水号");
                b += 1;
            }
            row = sheet.createRow((i+b)%65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getJyh());
            cell = row.createCell(2);
            cell.setCellValue(wl.getFkfzfbzh());
            cell = row.createCell(3);
            cell.setCellValue(wl.getSkfzfbzh());
            cell = row.createCell(4);
            cell.setCellValue(wl.getSkjgxx());
            cell = row.createCell(5);
            cell.setCellValue(wl.getDzsj());
            cell = row.createCell(6);
            cell.setCellValue(wl.getZzje());
            cell = row.createCell(7);
            cell.setCellValue(wl.getZzcpmc());
            cell = row.createCell(8);
            cell.setCellValue(wl.getJyfsd());
            cell = row.createCell(9);
            cell.setCellValue(wl.getTxlsh());
            if((i+b)%65536==0) {
                for (int a = 0; a < 10; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }

    /**
     * 字段映射
     * @param uploadPath
     * @return
     */
    public Map<String,Map<String,List<String>>> readExcel(String uploadPath) {
        Map<String,Map<String,List<String>>> excelMap = new HashMap<>();
        Map<String,List<String>> sheetMap = new HashMap<>();
        // 读取
        List<String> listPath = getZfbFileList(uploadPath);
        for (String path : listPath) {
            String excelName = path.substring(path.lastIndexOf(File.separator)+1);
            if(path.endsWith(".xlsx")){
                sheetMap = MappingUtils.getBy2007Excel(path);
            }else if(path.endsWith(".xls")){
                sheetMap = MappingUtils.getBy2003Excel(path);
            }
            // 将单个excel表数据放入map中
            excelMap.put(excelName,sheetMap);
        }
        return excelMap;
    }

    /**
     * 获取文件
     * @param uploadPath
     * @return
     */
    private List<String> getZfbFileList(String uploadPath) {
        List<String> listPath = new ArrayList<>();
        File dir = new File(uploadPath);
        File[] files = dir.listFiles();
        for (File file : files) {
            listPath.add(file.getAbsolutePath());
        }
        return listPath;
    }

    /**
     * 支付宝数据导入
     * @param uploadPath
     * @param field
     * @param id
     * @return
     */
    public int insertZfb(String uploadPath, List<List<String>> field, long id) {
        // 读取
        List<String> listPath = getZfbFileList(uploadPath);
        List<ZfbZzmxEntity> zfbList = null;
        int sum = 0;
        for (String path : listPath) {
            String excelName = path.substring(path.lastIndexOf(File.separator)+1);
            if(path.endsWith(".xlsx")){
                zfbList = getBy2007ExcelAll(path,excelName,field);
            }else if(path.endsWith(".xls")){
                zfbList = getBy2003ExcelAll(path,excelName,field);
            }
            sum += zfbZzmxDao.insertZzmx(zfbList,id);
        }
        // 添加转账明细统计数据
        List<ZfbZzmxTjjgForm> tjjgForms = zfbZzmxTjjgDao.selectZzmxTjjg(id);
        List<ZfbZzmxTjjgEntity> zzmxTjjgList = ZfbZzmxTjjgEntity.FormToList(tjjgForms,id);
        zfbZzmxTjjgDao.delAll(id);
        zfbZzmxTjjgDao.insertZzmxTjjg(zzmxTjjgList);
        // 添加转账明细对手账户统计数据
        List<ZfbZzmxTjjgssForm> tjjgsForms = zfbZzmxTjjgsDao.selectZzmxTjjgss(id);
        List<ZfbZzmxTjjgsEntity> zzmxTjjgsList = ZfbZzmxTjjgsEntity.FormToLists(tjjgsForms,id);
        zfbZzmxTjjgsDao.delAll(id);
        zfbZzmxTjjgsDao.insertZzmxTjjgs(zzmxTjjgsList);
        return sum;
    }

    /**
     * 删除文件
     * @param uploadPathd
     */
    public void deleteFile(File uploadPathd){
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

    /**
     * 读取2007版所有的excel数据.xlsx
     * @param path
     * @param excelName
     * @param field
     * @return
     */
    public List<ZfbZzmxEntity> getBy2007ExcelAll(String path, String excelName, List<List<String>> field) {
        // 用于存放表格中列号
        List<ZfbZzmxEntity> zzmxList = new ArrayList<>();
        File file = new File(path);
        Map<String,Integer> title = new HashMap<>();
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(file);
            Workbook wk = StreamingReader.builder()
                    .rowCacheSize(200)  //缓存到内存中的行数，默认是10
                    .bufferSize(4096)  //读取资源时，缓存到内存的字节大小，默认是1024
                    .open(fi);  //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
            for (int numSheet = 0; numSheet < wk.getNumberOfSheets(); numSheet++) {
                Sheet sheet = wk.getSheetAt(numSheet);
                for (List<String> excel : field) {
                    if (sheet == null) {
                        continue;
                    }else if(excelName.equals(excel.get(0)) && sheet.getSheetName().equals(excel.get(1))){
                        boolean temp = true;
                        for (Row row : sheet) {
                            int cellNum = row.getLastCellNum();
                            // 字段长度小于3跳出本次循环
                            if(cellNum<3){
                                continue;
                            }
                            if(temp){
                                if (row != null) {
                                    for (int i = 0; i < cellNum; i++) {
                                        Cell cell = row.getCell(i);
                                        String rowValue = MappingUtils.rowValue(cell);
                                        for(int j=2;j<excel.size();j++){
                                            if(rowValue.equals(excel.get(j))){
                                                title.put(excel.get(j),i);
                                            }
                                        }
                                    }
                                }
                                temp=false;
                            }else{
                                if (row != null) {
                                    ZfbZzmxEntity zzmx = RowToEntity(row,excel,title);
                                    if(zzmx!=null){
                                        zzmxList.add(zzmx);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(fi!=null){
                    fi.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 删除文件
        new File(path).delete();
        return zzmxList;
    }

    /**
     * 读取2003版所有的excel数据.xls
     * @param path
     * @param excelName
     * @param field
     * @return
     */
    private List<ZfbZzmxEntity> getBy2003ExcelAll(String path, String excelName, List<List<String>> field) {
        List<ZfbZzmxEntity> zzmxList = new ArrayList<>();
        InputStream is = null;
        Map<String,Integer> title = new HashMap<>();
        try {
            is = new FileInputStream(path);
            HSSFWorkbook wb = new HSSFWorkbook(is);
            for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                HSSFSheet sheet = wb.getSheetAt(numSheet);
                for (List<String> excel : field) {
                    if (sheet == null) {
                        continue;
                    } else if (excelName.equals(excel.get(0)) && sheet.getSheetName().equals(excel.get(1))) {
                        boolean temp = true;
                        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                            HSSFRow row = sheet.getRow(rowNum);
                            if(row!=null){
                                int cellNum = row.getLastCellNum();
                                // 字段长度小于3跳出本次循环
                                if(cellNum<3){
                                    continue;
                                }
                                if (temp) {
                                    for (int i = 0; i < cellNum; i++) {
                                        String cellName = row.getCell(i).getStringCellValue();
                                        for (int j = 1; j < excel.size(); j++) {
                                            if (cellName.equals(excel.get(j))) {
                                                title.put(excel.get(j), i);
                                            }
                                        }
                                    }
                                    temp = false;
                                } else {
                                    ZfbZzmxEntity zzmxs = RowToEntity(row, excel, title);
                                    if (zzmxs != null) {
                                        zzmxList.add(zzmxs);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(is!=null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 删除文件
        new File(path).delete();
        return zzmxList;
    }

    /**
     * 将行内容转换成对象
     * @param xssfRow
     * @param field
     * @return
     */
    public ZfbZzmxEntity RowToEntity(Row xssfRow, List<String> field, Map<String, Integer> title) {
        ZfbZzmxEntity zzmx = new ZfbZzmxEntity();
        if(field.get(2).equals("无") || xssfRow.getCell(title.get(field.get(2)))==null){
            zzmx.setJyh("");
        }else{
            String yjh = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(2)))).replace(",","");
            zzmx.setJyh(regexStr(yjh));
        }
        if(field.get(3).equals("无") || xssfRow.getCell(title.get(field.get(3)))==null){
            zzmx.setFkfzfbzh("");
        }else{
            String fkfzfbzh = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(3)))).replace(",","");
            zzmx.setFkfzfbzh(regexStr(fkfzfbzh));
        }
        if(field.get(4).equals("无") || xssfRow.getCell(title.get(field.get(4)))==null){
            zzmx.setSkfzfbzh("");
        }else{
            String skfzfbzh = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(4)))).replace(",","");
            zzmx.setSkfzfbzh(regexStr(skfzfbzh));
        }
        if(field.get(5).equals("无") || xssfRow.getCell(title.get(field.get(5)))==null){
            zzmx.setSkjgxx("");
        }else{
            String skjgxx = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(5)))).replace(",","");
            zzmx.setSkjgxx(regexStr(skjgxx));
        }
        if(field.get(6).equals("无") || xssfRow.getCell(title.get(field.get(6)))==null){
            zzmx.setDzsj("");
        }else{
            String dzsj = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(6)))).replace(",","");
            zzmx.setDzsj(regexStr(dzsj));
        }
        if(field.get(7).equals("无") || xssfRow.getCell(title.get(field.get(7)))==null){
            zzmx.setZzje(0);
        }else{
            String zzje = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(7)))).replace(",","");
            zzmx.setZzje(Double.parseDouble(regexStr(zzje)));
        }
        if(field.get(8).equals("无") || xssfRow.getCell(title.get(field.get(8)))==null){
            zzmx.setZzcpmc("");
        }else{
            String zzcpmc = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(8)))).replace(",","");
            zzmx.setZzcpmc(regexStr(zzcpmc));
        }
        if(field.get(9).equals("无") || xssfRow.getCell(title.get(field.get(9)))==null){
            zzmx.setJyfsd("");
        }else{
            String jyfsd = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(9)))).replace(",","");
            zzmx.setJyfsd(regexStr(jyfsd));
        }
        if(field.get(10).equals("无") || xssfRow.getCell(title.get(field.get(10)))==null){
            zzmx.setTxlsh("");
        }else{
            String txlsh = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(10)))).replace(",","");
            zzmx.setTxlsh(regexStr(txlsh));
        }
        if(field.get(11).equals("无") || xssfRow.getCell(title.get(field.get(11)))==null){
            zzmx.setDyxcsj("");
        }else{
            String dyxcsj = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(11)))).replace(",","");
            zzmx.setDyxcsj(regexStr(dyxcsj));
        }
        return zzmx;
    }

    /**
     * 正则截取
     * @param regStr
     * @return
     */
    public String regexStr(String regStr){
        if(regStr.contains("：")){
            String reg = "[^：]+：(.+)";
            Pattern pet = Pattern.compile(reg);
            Matcher matcher = pet.matcher(regStr);
            while (matcher.find()) {
                regStr = matcher.group(1);
            }
        }
        return regStr;
    }
}
