package cn.com.sinofaith.service.PyramidSaleService;

import cn.com.sinofaith.bean.pyramidSaleBean.PsHierarchyEntity;
import cn.com.sinofaith.bean.pyramidSaleBean.PyramidSaleEntity;
import cn.com.sinofaith.dao.pyramidSaleDao.PsHierarchyDao;
import cn.com.sinofaith.dao.pyramidSaleDao.PyramidSaleDao;
import cn.com.sinofaith.page.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * 传销数据业务层
 * @author zd
 * create by 2018.10.26
 */
@Service
public class PyramidSaleService {

    @Autowired
    private PyramidSaleDao pyramidSaleDao;
    @Autowired
    private PsHierarchyDao psHierarchyDao;
    /**
     * 读取excel表
     * @param uploadPath
     * @return
     */
    public Map<String,List<String>> readExcel(String uploadPath) {
        Map<String,List<String>> readMap = null;
        // 读取
        List<String> listPath = getPsFileList(uploadPath);
        for (String path : listPath) {
            if(path.endsWith(".xlsx")){
                readMap = getBy2007Excel(path);
            }else if(path.endsWith(".xls")){
                readMap = getBy2003Excel(path);
            }
        }
        return readMap;
    }

    /**
     * 获取2003版excel.xls版本数据
     * @param path
     * @return
     */
    private Map<String,List<String>> getBy2003Excel(String path) {
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

    /**
     * 获取2007版excel.xlsx
     * @param path
     * @return
     */
    private Map<String,List<String>> getBy2007Excel(String path) {
        File file = new File(path);
        Map<String,List<String>> sheetMap = new HashMap<>();
        XSSFWorkbook xssfWorkbook = null;
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(file);
            xssfWorkbook = new XSSFWorkbook(fi);
            for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
                List<String> readList = new ArrayList<String>();
                XSSFSheet sheet = xssfWorkbook.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }
                for (int rowNum = 0; rowNum <= 1; rowNum++) {
                    XSSFRow xssfRow = sheet.getRow(rowNum);
                    if (xssfRow != null) {
                        int cell = xssfRow.getLastCellNum();
                        for (int i = 0; i < cell; i++) {
                            String rawValue = null;
                            if(xssfRow.getCell(i)==null){
                                rawValue = "";
                            }else{
                                xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
                                rawValue = xssfRow.getCell(i).getStringCellValue();
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
        } finally{
            try {
                if(fi!=null){
                    fi.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sheetMap;
    }

    /**
     * 获取文件
     * @param uploadPath
     * @return
     */
    private List<String> getPsFileList(String uploadPath) {
        List<String> listPath = new ArrayList<>();
        File dir = new File(uploadPath);
        File[] files = dir.listFiles();
        for (File file : files) {
            listPath.add(file.getAbsolutePath());
        }
        return listPath;
    }

    /**
     * 插入数据PyramidSale表中
     * @param uploadPath
     * @param field
     * @return
     */
    public int insertPyramidSale(String uploadPath, ArrayList<String> field,long aj_id) {
        // 读取
        List<String> listPath = getPsFileList(uploadPath);
        List<PyramidSaleEntity> psList = null;
        int sum = 0;
        for (String path : listPath) {
            if(path.endsWith(".xlsx")){
                psList = getBy2007ExcelAll(path,field);
            }else if(path.endsWith(".xls")){
                psList = getBy2003ExcelAll(path,field);
            }
//            for (int i=0;i<psList.size();i++) {
//                psList.get(i).set
//            }

            sum = pyramidSaleDao.insertPyramidSale(psList,aj_id);
        }
        return sum;
    }

    /**
     * 读取2003版所有的excel数据.xlsx
     * @param path
     * @param field
     * @return
     */
    private List<PyramidSaleEntity> getBy2003ExcelAll(String path, ArrayList<String> field) {
        List<PyramidSaleEntity> psList = new ArrayList<>();
        InputStream is = null;
        Map<String,Integer> title = new HashMap<>();
        try {
            is = new FileInputStream(path);
            HSSFWorkbook wb = new HSSFWorkbook(is);
            for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                HSSFSheet sheet = wb.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }else if(sheet.getSheetName().equals(field.get(0))){
                    for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                        HSSFRow row = sheet.getRow(rowNum);
                        if(rowNum==0){
                            if (row != null) {
                                for (int i = 0; i < row.getLastCellNum(); i++) {
                                    String cellName = row.getCell(i).getStringCellValue();
                                    for(int j=1;j<field.size();j++){
                                        if(cellName.equals(field.get(j))){
                                            title.put(field.get(j),i);
                                        }
                                    }
                                }
                            }
                        }else{
                            if (row != null) {
                                PyramidSaleEntity ps = RowToEntity(row,field,title);
                                if(ps!=null){
                                    psList.add(ps);
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
        return psList;
    }

    /**
     * 读取2007版所有的excel数据.xlsx
     * @param path
     * @param field
     * @return
     */
    private List<PyramidSaleEntity> getBy2007ExcelAll(String path,ArrayList<String> field) {
        // 用于存放表格中列号
        List<PyramidSaleEntity> psList = new ArrayList<>();
        File file = new File(path);
        XSSFWorkbook xssfWorkbook = null;
        Map<String,Integer> title = new HashMap<>();
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(file);
            xssfWorkbook = new XSSFWorkbook(fi);
            for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet sheet = xssfWorkbook.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }else if(sheet.getSheetName().equals(field.get(0))){
                    for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                        XSSFRow xssfRow = sheet.getRow(rowNum);
                        if(rowNum==0){
                            if (xssfRow != null) {
                                for (int i = 0; i < xssfRow.getLastCellNum(); i++) {
                                    String cellName = xssfRow.getCell(i).getStringCellValue();
                                    for(int j=1;j<field.size();j++){
                                        if(cellName.equals(field.get(j))){
                                            title.put(field.get(j),i);
                                        }
                                    }
                                }
                            }
                        }else{
                            if (xssfRow != null) {
                                PyramidSaleEntity ps = RowToEntity(xssfRow,field,title);
                                if(ps!=null){
                                    psList.add(ps);
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
        return psList;
    }

    /**
     * 将行内容转换成对象
     * @param xssfRow
     * @param field
     * @return
     */
    private PyramidSaleEntity RowToEntity(Row xssfRow, ArrayList<String> field, Map<String,Integer> title) {
        // 设置单元格为String类型
        for(int i=1;i<12;i++){
            if(!field.get(i).equals("无") && xssfRow.getCell(title.get(field.get(i)))!=null){
                xssfRow.getCell(title.get(field.get(i))).setCellType(Cell.CELL_TYPE_STRING);
            }
        }
        PyramidSaleEntity ps = new PyramidSaleEntity();
        if(field.get(1).equals("无") || xssfRow.getCell(title.get(field.get(1)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(1))).getStringCellValue())){
            ps.setPsId("");
        }else{
            ps.setPsId(xssfRow.getCell(title.get(field.get(1))).getStringCellValue());
        }
        if(field.get(2).equals("无") || xssfRow.getCell(title.get(field.get(2)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(2))).getStringCellValue())){
            ps.setSponsorId("");
        }else{
            ps.setSponsorId(xssfRow.getCell(title.get(field.get(2))).getStringCellValue());
        }
        if(field.get(3).equals("无") || xssfRow.getCell(title.get(field.get(3)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(3))).getStringCellValue())){
            ps.setMobile("");
        }else{
            ps.setMobile(xssfRow.getCell(title.get(field.get(3))).getStringCellValue());
        }
        if(field.get(4).equals("无") || xssfRow.getCell(title.get(field.get(4)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(4))).getStringCellValue())){
            ps.setTelphone("");
        }else{
            ps.setTelphone(xssfRow.getCell(title.get(field.get(4))).getStringCellValue());
        }
        // 判断手机号为空电话不为空时
        if(StringUtils.isBlank(ps.getMobile()) && StringUtils.isNotBlank(ps.getTelphone())){
            // 手机号码=电话号码
            ps.setMobile(ps.getTelphone());
        }
        if(field.get(5).equals("无") || xssfRow.getCell(title.get(field.get(5)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(5))).getStringCellValue())){
            ps.setNick_name("");
        }else{
            ps.setNick_name(xssfRow.getCell(title.get(field.get(5))).getStringCellValue());
        }
        if(field.get(6).equals("无") || xssfRow.getCell(title.get(field.get(6)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(6))).getStringCellValue())){
            ps.setSex("");
        }else{
            ps.setSex(xssfRow.getCell(title.get(field.get(6))).getStringCellValue());
        }
        if(field.get(7).equals("无") || xssfRow.getCell(title.get(field.get(7)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(7))).getStringCellValue())){
            ps.setAddress("");
        }else{
            ps.setAddress(xssfRow.getCell(title.get(field.get(7))).getStringCellValue());
        }
        if(field.get(8).equals("无") || xssfRow.getCell(title.get(field.get(8)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(8))).getStringCellValue())){
            ps.setSfzhm("");
        }else{
            ps.setSfzhm(xssfRow.getCell(title.get(field.get(8))).getStringCellValue());
        }
        if(field.get(9).equals("无") || xssfRow.getCell(title.get(field.get(9)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(9))).getStringCellValue())){
            ps.setBankName("");
        }else{
            ps.setBankName(xssfRow.getCell(title.get(field.get(9))).getStringCellValue());
        }
        if(field.get(10).equals("无") || xssfRow.getCell(title.get(field.get(10)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(10))).getStringCellValue())){
            ps.setAccountHolder("");
        }else{
            ps.setAccountHolder(xssfRow.getCell(title.get(field.get(10))).getStringCellValue());
        }
        if(field.get(11).equals("无") || xssfRow.getCell(title.get(field.get(11)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(11))).getStringCellValue())){
            ps.setAccountNumber("");
        }else{
            ps.setAccountNumber(xssfRow.getCell(title.get(field.get(11))).getStringCellValue());
        }
        return ps;
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
     * 获取分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = null;
        int rowAll = pyramidSaleDao.getRowAll(dc);
        if(rowAll>0){
            List<PyramidSaleEntity> psList = pyramidSaleDao.getDoPage(currentPage, pageSize, dc);
            if(psList!=null){
                page = new Page();
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(psList);
                page.setPageSize(pageSize);
            }
        }
        return page;
    }

    /**
     * 路径表插入数据
     * @param id
     */
    public int insertPsHierarchy(long id) {
        // 查出所需要的数据
        List<PsHierarchyEntity> pshierList = pyramidSaleDao.selectPyramidSaleByAj_id(id);
        if(pshierList==null){
            return 0;
        }
        List<PsHierarchyEntity> pshList = psHierarchyDao.selectPsHierarchyByAj_Id(id);
        for (int i=0;i<pshierList.size();i++) {
            for (PsHierarchyEntity psh : pshList) {
                if(pshierList.get(i).getPsId().equals(psh.getPsId())){
                    pshierList.get(i).setDirectReferNum(psh.getDirectReferNum());
                }
            }
        }
        // 插入数据
        int sum = psHierarchyDao.insertpsHierarchy(pshierList,id);
        return sum;
    }

}