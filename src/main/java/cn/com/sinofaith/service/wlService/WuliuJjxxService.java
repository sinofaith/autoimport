package cn.com.sinofaith.service.wlService;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.bean.wlBean.WuliuRelationEntity;
import cn.com.sinofaith.dao.wuliuDao.WuliuDao;
import cn.com.sinofaith.dao.wuliuDao.WuliuJjxxDao;
import cn.com.sinofaith.dao.wuliuDao.WuliuRelationDao;
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

@Service
public class WuliuJjxxService {

    @Autowired
    private WuliuDao wld;
    @Autowired
    private WuliuJjxxDao jjxxDao;
    @Autowired
    private WuliuRelationDao wlDao;
    /**
     * 获取案件id获取物流数据
     * @param id
     * @return
     */
    public List<WuliuEntity> getAll(long id) {
        String hql = "from WuliuEntity where aj_id = "+id;
        List<WuliuEntity> wulius = wld.find(hql);
        return wulius;
    }

    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc, AjEntity aj, String seach) {
        Page page = new Page();
        int rowAll = jjxxDao.getRowAll(seach,aj);
        List<WuliuEntity> wls = null;
        if(rowAll>0){
            wls = jjxxDao.getDoPage(currentPage, pageSize, dc, aj);
            for (int i = 0; i <wls.size(); i++) {
                wls.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            page.setPageSize(pageSize);
            page.setTotalRecords(rowAll);
            page.setList(wls);
            page.setPageNo(currentPage);
        }
        return page;
    }

    /**
     * 获取寄收件人关系信息
     * @param id
     * @return
     */
    public void insertRelation(long id) {
        int rowCount = wlDao.getRowCount(id);
        if(rowCount>0){
            wlDao.deleteWuliuRelation(id);
        }
        List<WuliuRelationEntity> wlrs = jjxxDao.insertRelation(id);
        wlDao.insertWuliuRelation(wlrs);
    }

    /**
     * 获取全部数据
     * @param dc
     * @return
     */
    public List<WuliuEntity> getWuliuAll(String seach,DetachedCriteria dc, AjEntity aj) {
        int rowAll = jjxxDao.getRowAll(seach,aj);
        List<WuliuEntity> wls = null;
        if(rowAll>0){
            wls = jjxxDao.getWuliuAll(dc, aj);
        }
        return wls;
    }

    /**
     * 创建excel文件
     * @param wls
     * @return
     */
    public HSSFWorkbook createExcel(List<WuliuEntity> wls) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("物流寄件信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("运单号");
        cell = row.createCell(2);
        cell.setCellValue("寄件时间");
        cell = row.createCell(3);
        cell.setCellValue("寄件人");
        cell = row.createCell(4);
        cell.setCellValue("寄件电话");
        cell = row.createCell(5);
        cell.setCellValue("寄件地址");
        cell = row.createCell(6);
        cell.setCellValue("收件人");
        cell = row.createCell(7);
        cell.setCellValue("收件电话");
        cell = row.createCell(8);
        cell.setCellValue("收件地址");
        cell = row.createCell(9);
        cell.setCellValue("托寄物");
        cell = row.createCell(10);
        cell.setCellValue("付款方式");
        cell = row.createCell(11);
        cell.setCellValue("代收货款");
        cell = row.createCell(12);
        cell.setCellValue("运费");
        int b = 1;
        for(int i=0;i<wls.size();i++) {
            WuliuEntity wl = wls.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                sheet = wb.createSheet("物流寄件信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("运单号");
                cell = row.createCell(2);
                cell.setCellValue("寄件时间");
                cell = row.createCell(3);
                cell.setCellValue("寄件人");
                cell = row.createCell(4);
                cell.setCellValue("寄件电话");
                cell = row.createCell(5);
                cell.setCellValue("寄件地址");
                cell = row.createCell(6);
                cell.setCellValue("收件人");
                cell = row.createCell(7);
                cell.setCellValue("收件电话");
                cell = row.createCell(8);
                cell.setCellValue("收件地址");
                cell = row.createCell(9);
                cell.setCellValue("托寄物");
                cell = row.createCell(10);
                cell.setCellValue("付款方式");
                cell = row.createCell(11);
                cell.setCellValue("代收货款");
                cell = row.createCell(12);
                cell.setCellValue("运费");
                b += 1;
            }
            row = sheet.createRow((i+b)%65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getWaybill_id());
            cell = row.createCell(2);
            cell.setCellValue(wl.getShip_time());
            cell = row.createCell(3);
            cell.setCellValue(wl.getSender());
            cell = row.createCell(4);
            cell.setCellValue(wl.getShip_phone());
            cell = row.createCell(5);
            cell.setCellValue(wl.getShip_address());
            cell = row.createCell(6);
            cell.setCellValue(wl.getAddressee());
            cell = row.createCell(7);
            cell.setCellValue(wl.getSj_phone());
            cell = row.createCell(8);
            cell.setCellValue(wl.getSj_address());
            cell = row.createCell(9);
            cell.setCellValue(wl.getTjw());
            cell = row.createCell(10);
            cell.setCellValue(wl.getPayment());
            cell = row.createCell(11);
            cell.setCellValue(wl.getDshk());
            cell = row.createCell(12);
            cell.setCellValue(wl.getFreight());
            if((i+b)%65536==0) {
                for (int a = 0; a < 13; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }

    /**
     * 物流表数据映射
     * @param uploadPath
     * @return
     */
    public Map<String,Map<String,List<String>>> readExcel(String uploadPath) {
        Map<String,Map<String,List<String>>> excelMap = new HashMap<>();
        Map<String,List<String>> sheetMap = new HashMap<>();
        // 读取
        List<String> listPath = getPsFileList(uploadPath);
        String excelName = null;
        for (String path : listPath) {
            excelName = path.substring(path.lastIndexOf(File.separator)+1);
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
     * 物流数据导入数据库
     * @param uploadPath
     * @param field
     * @param id
     * @return
     */
    public int insertWuliu(String uploadPath, List<List<String>> field, long id) {
        // 读取
        List<String> listPath = getPsFileList(uploadPath);
        List<WuliuEntity> wuliuList = null;
        int sum = 0;
        for (String path : listPath) {
            String excelName = path.substring(path.lastIndexOf(File.separator)+1);
            if(path.endsWith(".xlsx")){
                wuliuList = getBy2007ExcelAll(path,excelName,field);
            }else if(path.endsWith(".xls")){
                wuliuList = getBy2003ExcelAll(path,excelName,field);
            }
            sum += jjxxDao.insertJjxx(wuliuList, id);
        }
        return sum;
    }

    /**
     * 读取2003版所有的excel数据.xls
     * @param path
     * @param excelName
     * @param field
     * @return
     */
    private List<WuliuEntity> getBy2003ExcelAll(String path, String excelName, List<List<String>> field) {
        List<WuliuEntity> psList = new ArrayList<>();
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
                        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                            HSSFRow row = sheet.getRow(rowNum);
                            if (rowNum == 0) {
                                if (row != null) {
                                    for (int i = 0; i < row.getLastCellNum(); i++) {
                                        String cellName = row.getCell(i).getStringCellValue();
                                        for (int j = 2; j < excel.size(); j++) {
                                            if (cellName.equals(excel.get(j))) {
                                                title.put(excel.get(j), i);
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (row != null) {
                                    WuliuEntity ps = RowToEntity(row, excel, title);
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
     * @param excelName
     * @param field
     * @return
     */
    private List<WuliuEntity> getBy2007ExcelAll(String path,String excelName,List<List<String>> field) {
        // 用于存放表格中列号
        List<WuliuEntity> psList = new ArrayList<>();
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
                        int temp = 0;
                        for (Row row : sheet) {
                            if(temp==0){
                                if (row != null) {
                                    for (int i = 0; i < row.getLastCellNum(); i++) {
                                        Cell cell = row.getCell(i);
                                        String rowValue = MappingUtils.rowValue(cell);
                                        for(int j=2;j<excel.size();j++){
                                            if(rowValue.equals(excel.get(j))){
                                                title.put(excel.get(j),i);
                                            }
                                        }
                                    }
                                }
                                temp=1;
                            }else{
                                if (row != null) {
                                    WuliuEntity ps = RowToEntity(row,excel,title);
                                    if(ps!=null){
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
    private WuliuEntity RowToEntity(Row xssfRow, List<String> field, Map<String,Integer> title) {
        WuliuEntity wuliu = new WuliuEntity();
        // 运单号
        if(field.get(2).equals("无") || xssfRow.getCell(title.get(field.get(2)))==null){
            wuliu.setWaybill_id("");
        }else{
            String waybill_id = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(2)))).replace(",","");
            wuliu.setWaybill_id(waybill_id);
        }
        // 寄件时间
        if(field.get(3).equals("无") || xssfRow.getCell(title.get(field.get(3)))==null){
            wuliu.setShip_time("");
        }else{
            String ship_time = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(3)))).replace(",","");
            wuliu.setShip_time(ship_time);
        }
        // 寄件地址
        if(field.get(4).equals("无") || xssfRow.getCell(title.get(field.get(4)))==null){
            wuliu.setShip_address("");
        }else{
            String ship_address = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(4)))).replace(",","");
            wuliu.setShip_address(ship_address);
        }
        // 寄件人
        if(field.get(5).equals("无") || xssfRow.getCell(title.get(field.get(5)))==null){
            wuliu.setSender("");
        }else{
            String sender = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(5)))).replace(",","");
            wuliu.setSender(sender);
        }
        // 寄件电话
        if(field.get(6).equals("无") || xssfRow.getCell(title.get(field.get(6)))==null){
            wuliu.setShip_phone("");
        }else{
            String ship_phone = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(6)))).replace(",","");
            wuliu.setShip_phone(ship_phone);
        }
        // 寄件手机
        if(field.get(7).equals("无") || xssfRow.getCell(title.get(field.get(7)))==null){
            wuliu.setShip_mobilephone("");
        }else{
            String ship_phone = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(7)))).replace(",","");
            wuliu.setShip_mobilephone(ship_phone);
        }
        // 寄件人=0
        if("0".equals(wuliu.getSender())){
            wuliu.setSender(wuliu.getShip_phone());
        }
        // 寄件电话  为0时
        if("0".equals(wuliu.getShip_phone()) || "".equals(wuliu.getShip_phone())){
            // 手机不为0时
            if(!"0".equals(wuliu.getShip_mobilephone())){
                // 电话=手机
                wuliu.setShip_phone(wuliu.getShip_mobilephone());
            }else{
                // 电话=寄件人
                wuliu.setShip_phone(wuliu.getSender());
            }
            // 手机为""
            if("".equals(wuliu.getShip_mobilephone())){
                // 电话=收件人
                wuliu.setShip_phone(wuliu.getSender());
            }
        }
        // 收件地址
        if(field.get(8).equals("无") || xssfRow.getCell(title.get(field.get(8)))==null){
            wuliu.setSj_address("");
        }else{
            String sj_address = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(8)))).replace(",","");
            if("0".equals(sj_address)){
                sj_address = "";
            }
            wuliu.setSj_address(sj_address);
        }
        // 收件人
        if(field.get(9).equals("无") || xssfRow.getCell(title.get(field.get(9)))==null){
            wuliu.setAddressee("");
        }else{
            String addressee = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(9)))).replace(",","");
            wuliu.setAddressee(addressee);
        }
        // 收件电话
        if(field.get(10).equals("无") || xssfRow.getCell(title.get(field.get(10)))==null){
            wuliu.setSj_phone("");
        }else{
            String sj_phone = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(10)))).replace(",","");
            wuliu.setSj_phone(sj_phone);
        }
        // 收件手机
        if(field.get(11).equals("无") || xssfRow.getCell(title.get(field.get(11)))==null){
            wuliu.setSj_mobilephone("");
        }else{
            String sj_mobilephone = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(11)))).replace(",","");
            wuliu.setSj_mobilephone(sj_mobilephone);
        }
        // 收件人=0
        if(wuliu.getAddressee().equals("0")){
            wuliu.setAddressee(wuliu.getSj_phone());
        }
        // 收件电话 为0时
        if("0".equals(wuliu.getSj_phone()) || "".equals(wuliu.getSj_phone())){
            // 手机 不为0时
            if(!"0".equals(wuliu.getSj_mobilephone())){
                // 电话=手机
                wuliu.setSj_phone(wuliu.getSj_mobilephone());
            }else{
                // 电话=收件人
                wuliu.setSj_phone(wuliu.getAddressee());
            }
            // 手机为""
            if("".equals(wuliu.getSj_mobilephone())){
                // 电话=收件人
                wuliu.setSj_phone(wuliu.getAddressee());
            }
        }
        // 电话<11位
        if(wuliu.getSj_phone().length()<11){
            // 手机>=11位
            if(wuliu.getSj_mobilephone().length()>=11){
                wuliu.setSj_phone(wuliu.getSj_mobilephone());
            }else if(wuliu.getSj_mobilephone().equals("18755457379") || wuliu.getSj_mobilephone().equals("0")){
                // 手机号=18755457379
                // 收件人=收件电话
                wuliu.setAddressee(wuliu.getSj_phone());
            }
        }
        // 收件员
        if(field.get(12).equals("无") || xssfRow.getCell(title.get(field.get(12)))==null){
            wuliu.setCollector("");
        }else{
            String collector = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(12)))).replace(",","");
            wuliu.setCollector(collector);
        }
        // 托寄物
        if(field.get(13).equals("无") || xssfRow.getCell(title.get(field.get(13)))==null){
            wuliu.setTjw("");
        }else{
            String tjw = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(13)))).replace(",","");
            wuliu.setTjw(tjw);
        }
        // 付款方式
        if(field.get(14).equals("无") || xssfRow.getCell(title.get(field.get(14)))==null){
            wuliu.setPayment("");
        }else{
            String payment = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(14)))).replace(",","");
            wuliu.setPayment(payment);
        }
        // 代收货款
        if(field.get(15).equals("无") || xssfRow.getCell(title.get(field.get(15)))==null){
            wuliu.setDshk("");
        }else{
            String dshk = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(15)))).replace(",","");
            wuliu.setDshk(dshk);
        }
        // 计费重量
        if(field.get(16).equals("无") || xssfRow.getCell(title.get(field.get(16)))==null){
            wuliu.setWeight("");
        }else{
            String weight = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(16)))).replace(",","");
            wuliu.setWeight(weight);
        }
        // 件数
        if(field.get(17).equals("无") || xssfRow.getCell(title.get(field.get(17)))==null){
            wuliu.setNumber_cases("");
        }else{
            String number_cases = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(17)))).replace(",","");
            wuliu.setNumber_cases(number_cases);
        }
        // 运费
        if(field.get(18).equals("无") || xssfRow.getCell(title.get(field.get(18)))==null){
            wuliu.setFreight("");
        }else{
            String freight = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(18)))).replace(",","");
            wuliu.setFreight(freight);
        }
        return wuliu;
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
}

