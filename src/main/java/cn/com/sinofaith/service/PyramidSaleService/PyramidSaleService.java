package cn.com.sinofaith.service.PyramidSaleService;

import cn.com.sinofaith.bean.pyramidSaleBean.PsHierarchyEntity;
import cn.com.sinofaith.bean.pyramidSaleBean.PyramidSaleEntity;
import cn.com.sinofaith.dao.pyramidSaleDao.PsHierarchyDao;
import cn.com.sinofaith.dao.pyramidSaleDao.PyramidSaleDao;
import cn.com.sinofaith.form.psForm.PsPoltForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.util.MappingUtils;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

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
    private static Long contain = 0l;
     /**
     * 读取excel表
     * @param uploadPath
     * @return
     */
    public Map<String,Map<String,List<String>>> readExcel(String uploadPath) {
        Map<String,Map<String,List<String>>> excelMap = new HashMap<>();
        Map<String,List<String>> sheetMap = new HashMap<>();
        // 读取
        List<String> listPath = getPsFileList(uploadPath);
        for (String path : listPath) {
            String excelName = path.substring(path.lastIndexOf("\\")+1);
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
    public int insertPyramidSale(String uploadPath, List<List<String>> field,long aj_id) {
        // 读取
        List<String> listPath = getPsFileList(uploadPath);
        List<PyramidSaleEntity> psList = null;
        List<PyramidSaleEntity> psListAll = new ArrayList<>();
        int sum = 0;
        for (String path : listPath) {
            String excelName = path.substring(path.lastIndexOf("\\")+1);
            if(path.endsWith(".xlsx")){
                psList = getBy2007ExcelAll(path, excelName, field);
            }else if(path.endsWith(".xls")){
                psList = getBy2003ExcelAll(path, excelName, field);
            }
            sum += pyramidSaleDao.insertPyramidSale(psList,aj_id);
            psListAll.addAll(psList);
        }
        Map<String,List<PyramidSaleEntity>> listMap = psListAll.stream().collect(Collectors.groupingBy(PyramidSaleEntity::getSponsorId));
        // 分析路径数据
        getData(listMap,aj_id);
        return sum;
    }

    private void getData(Map<String, List<PyramidSaleEntity>> listMap,long aj_id) {
        List<String> rootNodes = pyramidSaleDao.getRootNode(aj_id);
        List<PsPoltForm> pfLists = new ArrayList<>();
        for(String rootNode : rootNodes){
            List<PsPoltForm> list = null;
            if(rootNode==null){
                list = getTreeData(null,"",listMap,1l, pfLists);
            }else{
                list = getTreeData(null, rootNode, listMap,1l, pfLists);
            }
            contain = 0l;
        }
        psHierarchyDao.insertPsHierarchy(pfLists,aj_id);
    }

    public List<PsPoltForm> getTreeData(PsPoltForm pp, String sponsorid,Map<String, List<PyramidSaleEntity>> listMap,Long tier,
                                        List<PsPoltForm> pfLists) {
        List<PyramidSaleEntity> psList = listMap.get(sponsorid);
        if(psList!=null && psList.size()>0) {
            List<PsPoltForm> pfList = new ArrayList<>();
            for (PyramidSaleEntity ps : psList) {
                PsPoltForm pf = new PsPoltForm();
                pf.setPsid(ps.getPsId());
                pf.setSponsorid(ps.getSponsorId());
                pf.setName(ps.getNick_name());
                pfList.add(pf);
                pf.setParentNode(pp);
            }
            getChildNode(pfList, tier, listMap, pfLists);
            return pfList;
        }else{
            return null;
        }
    }

    private void getChildNode(List<PsPoltForm> pfList,Long tier, Map<String, List<PyramidSaleEntity>> listMap,
                              List<PsPoltForm> pfLists) {
        for(PsPoltForm ps : pfList){
            //ps.setPath(ps.getPsid());
            // 当前层级
            ps.setTier(tier);
            //查询Psid下的所有子节点
            if(!ps.getPsid().equals(ps.getSponsorid())){
                List<PsPoltForm> ns = getTreeData(ps, ps.getPsid(), listMap,tier+1, pfLists); //递归
                if(ns!=null && ns.size()>0){
                    // 下线会员数
                    for(PsPoltForm n : ns){
                        ps.setContainNum(ps.getContainNum()+n.getContainNum());

                        ps.setPath(ps.getPath()+"/"+n.getPsid());
                    }
//                    if(ps.getParentNode()!=null){
//                        ps.getParentNode().setPath(ps.getParentNode().getPath()+"/"+ps.getPath());
//                    }

                    ps.setContainNum(ps.getContainNum()+ns.size());
                    // 包含层级数
                    ps.setContain(contain-ps.getTier());
                    // 直系会员数
                    ps.setLineal(Long.parseLong(String.valueOf(ns.size())));
                    // 下线人
                    ps.setChildren(ns);
                }else{
                    // 直系会员数
                    ps.setLineal(0l);
                    // 包含层级数
                    ps.setContain(0l);
                    // 下线会员数
                    ps.setContainNum(0l);
                    if(ps.getTier()>contain){
                        contain = ps.getTier();
                    }
                }
            }else{
                // 直系会员数
                ps.setLineal(0l);
                // 包含层级数
                ps.setContain(0l);
                // 下线会员数
                ps.setContainNum(0l);
            }
            pfLists.add(ps);
        }
    }


    /**
     * 读取2003版所有的excel数据.xls
     * @param path
     * @param field
     * @return
     */
    private List<PyramidSaleEntity> getBy2003ExcelAll(String path, String excelName, List<List<String>> field) {
        List<PyramidSaleEntity> psList = new ArrayList<>();
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
                            if (row!=null) {
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
                                }else {
                                    if (row != null) {
                                        PyramidSaleEntity ps = RowToEntity(row, excel, title);
                                        if (ps != null) {
                                            psList.add(ps);
                                        }
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
        return psList;
    }

    /**
     * 读取2007版所有的excel数据.xlsx
     * @param path
     * @param field
     * @return
     */
    private List<PyramidSaleEntity> getBy2007ExcelAll(String path,String excelName, List<List<String>> field) {
        // 用于存放表格中列号
        List<PyramidSaleEntity> psList = new ArrayList<>();
        File file = new File(path);
        Map<String,Integer> title = new HashMap<>();
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(file);
            Workbook wk = StreamingReader.builder()
                    .rowCacheSize(100)  //缓存到内存中的行数，默认是10
                    .bufferSize(512)  //读取资源时，缓存到内存的字节大小，默认是1024
                    .open(fi);  //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
            for (int numSheet = 0; numSheet < wk.getNumberOfSheets(); numSheet++) {
                Sheet sheet = wk.getSheetAt(numSheet);
                for (List<String> excel : field) {
                    if (sheet == null) {
                        continue;
                    } else if (excelName.equals(excel.get(0)) && sheet.getSheetName().equals(excel.get(1))) {
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
                                temp = false;
                            } else {
                                if (row != null) {
                                    PyramidSaleEntity ps = RowToEntity(row, excel, title);
                                    if (ps != null) {
                                        psList.add(ps);
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
        return psList;
    }

    /**
     * 将行内容转换成对象
     * @param xssfRow
     * @param field
     * @return
     */
    private PyramidSaleEntity RowToEntity(Row xssfRow, List<String> field, Map<String,Integer> title) {
        // 设置单元格为String类型
        /*for(int i=1;i<12;i++){
            if(!field.get(i).equals("无") && xssfRow.getCell(title.get(field.get(i)))!=null){
                xssfRow.getCell(title.get(field.get(i))).setCellType(Cell.CELL_TYPE_STRING);
            }
        }*/
        PyramidSaleEntity ps = new PyramidSaleEntity();
        if(field.get(2).equals("无") || xssfRow.getCell(title.get(field.get(2)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(2))).getStringCellValue())){
            ps.setPsId("");
        }else{
            ps.setPsId(xssfRow.getCell(title.get(field.get(2))).getStringCellValue());
        }
        if(field.get(3).equals("无") || xssfRow.getCell(title.get(field.get(3)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(3))).getStringCellValue())){
            ps.setSponsorId("");
        }else{
            ps.setSponsorId(xssfRow.getCell(title.get(field.get(3))).getStringCellValue());
        }
        if(field.get(4).equals("无") || xssfRow.getCell(title.get(field.get(4)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(4))).getStringCellValue())){
            ps.setMobile("");
        }else{
            ps.setMobile(xssfRow.getCell(title.get(field.get(4))).getStringCellValue());
        }
        if(field.get(5).equals("无") || xssfRow.getCell(title.get(field.get(5)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(5))).getStringCellValue())){
            ps.setTelphone("");
        }else{
            ps.setTelphone(xssfRow.getCell(title.get(field.get(5))).getStringCellValue());
        }
        // 判断手机号为空电话不为空时
        if(StringUtils.isBlank(ps.getMobile()) && StringUtils.isNotBlank(ps.getTelphone())){
            // 手机号码=电话号码
            ps.setMobile(ps.getTelphone());
        }
        if(field.get(6).equals("无") || xssfRow.getCell(title.get(field.get(6)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(6))).getStringCellValue())){
            ps.setNick_name("");
        }else{
            ps.setNick_name(xssfRow.getCell(title.get(field.get(6))).getStringCellValue());
        }
        if(field.get(7).equals("无") || xssfRow.getCell(title.get(field.get(7)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(7))).getStringCellValue())){
            ps.setSex("");
        }else{
            ps.setSex(xssfRow.getCell(title.get(field.get(7))).getStringCellValue());
        }
        if(field.get(8).equals("无") || xssfRow.getCell(title.get(field.get(8)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(8))).getStringCellValue())){
            ps.setAddress("");
        }else{
            ps.setAddress(xssfRow.getCell(title.get(field.get(8))).getStringCellValue());
        }
        if(field.get(9).equals("无") || xssfRow.getCell(title.get(field.get(9)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(9))).getStringCellValue())){
            ps.setSfzhm("");
        }else{
            ps.setSfzhm(xssfRow.getCell(title.get(field.get(9))).getStringCellValue());
        }
        if(field.get(10).equals("无") || xssfRow.getCell(title.get(field.get(10)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(10))).getStringCellValue())){
            ps.setBankName("");
        }else{
            ps.setBankName(xssfRow.getCell(title.get(field.get(10))).getStringCellValue());
        }
        if(field.get(11).equals("无") || xssfRow.getCell(title.get(field.get(11)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(11))).getStringCellValue())){
            ps.setAccountHolder("");
        }else{
            ps.setAccountHolder(xssfRow.getCell(title.get(field.get(11))).getStringCellValue());
        }
        if(field.get(12).equals("无") || xssfRow.getCell(title.get(field.get(12)))==null || StringUtils.isBlank(xssfRow.getCell(title.get(field.get(12))).getStringCellValue())){
            ps.setAccountNumber("");
        }else{
            ps.setAccountNumber(xssfRow.getCell(title.get(field.get(12))).getStringCellValue());
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
        pyramidSaleDao.delete("delete from PsHierarchyEntity where aj_id="+id);
        // 查出所需要的数据
        int rowNum = pyramidSaleDao.selectPyramidSaleByAj_id(id);
        return rowNum;
    }

    /**
     * 数据导出
     * @param search
     * @return
     */
    public List<PyramidSaleEntity> getPyramidSaleAll(String search) {
        List<PyramidSaleEntity> psList = null;
        int rowAll = pyramidSaleDao.getRowAllCount(search);
        if(rowAll>0){
            psList = pyramidSaleDao.getPyramidSaleAll(search);
        }
        return psList;
    }

    /**
     * 数据导出excel表
     * @param psList
     * @return
     */
    public HSSFWorkbook createExcel(List<PyramidSaleEntity> psList) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("传销会员信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("会员编号");
        cell = row.createCell(2);
        cell.setCellValue("推荐会员编号");
        cell = row.createCell(3);
        cell.setCellValue("姓名");
        cell = row.createCell(4);
        cell.setCellValue("手机号");
        cell = row.createCell(5);
        cell.setCellValue("性别");
        cell = row.createCell(6);
        cell.setCellValue("详细地址");
        cell = row.createCell(7);
        cell.setCellValue("身份证号");
        cell = row.createCell(8);
        cell.setCellValue("持卡人");
        cell = row.createCell(9);
        cell.setCellValue("银行名称");
        cell = row.createCell(10);
        cell.setCellValue("银行卡号");
        int b = 1;
        for(int i=0;i<psList.size();i++) {
            PyramidSaleEntity wl = psList.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                sheet = wb.createSheet("传销会员信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("会员编号");
                cell = row.createCell(2);
                cell.setCellValue("推荐会员编号");
                cell = row.createCell(3);
                cell.setCellValue("姓名");
                cell = row.createCell(4);
                cell.setCellValue("手机号");
                cell = row.createCell(5);
                cell.setCellValue("性别");
                cell = row.createCell(6);
                cell.setCellValue("详细地址");
                cell = row.createCell(7);
                cell.setCellValue("身份证号");
                cell = row.createCell(8);
                cell.setCellValue("持卡人");
                cell = row.createCell(9);
                cell.setCellValue("银行名称");
                cell = row.createCell(10);
                cell.setCellValue("银行卡号");
                b += 1;
            }
            row = sheet.createRow((i+b) % 65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getPsId());
            cell = row.createCell(2);
            cell.setCellValue(wl.getSponsorId());
            cell = row.createCell(3);
            cell.setCellValue(wl.getNick_name());
            cell = row.createCell(4);
            cell.setCellValue(wl.getMobile());
            cell = row.createCell(5);
            cell.setCellValue(wl.getSex());
            cell = row.createCell(6);
            cell.setCellValue(wl.getAddress());
            cell = row.createCell(7);
            cell.setCellValue(wl.getSfzhm());
            cell = row.createCell(8);
            cell.setCellValue(wl.getAccountHolder());
            cell = row.createCell(9);
            cell.setCellValue(wl.getBankName());
            cell = row.createCell(10);
            cell.setCellValue(wl.getAccountNumber());
            if((i+b)%65536==0) {
                for (int a = 0; a < 11; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }
}
