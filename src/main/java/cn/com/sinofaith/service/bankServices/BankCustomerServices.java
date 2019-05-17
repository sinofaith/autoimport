package cn.com.sinofaith.service.bankServices;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.BankCustomerEntity;
import cn.com.sinofaith.bean.bankBean.BankZcxxEntity;
import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.bankDao.BankCustomerDao;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.util.MappingUtils;
import cn.com.sinofaith.util.TimeFormatUtil;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BankCustomerServices {
    @Autowired
    private BankCustomerDao bcd;
    @Autowired
    private AJDao ad;


    public String getAjidByAjm(AjEntity aj,long userId){
        String[] ajm = new String[]{};
        StringBuffer ajid = new StringBuffer();
        if(aj.getAj().contains(",")) {
            ajm = aj.getAj().split(",");
            for (int i = 0; i < ajm.length; i++) {
                ajid.append(ad.findFilter(ajm[i],userId).get(0).getId());
                if (i != ajm.length - 1) {
                    ajid.append(",");
                }
            }
        }else{
            ajid.append(aj.getId());
        }
        return ajid.toString();
    }

    public String getSeach(String seachCode, String seachCondition ,AjEntity aj,long userId){
        String ajid = getAjidByAjm(aj,userId);
        StringBuffer seach = new StringBuffer(" and s.aj_id in ("+ajid+") ");

        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            seach.append(" and c." + seachCondition + " like " + "'%" + seachCode + "%'");
        }else{
            seach.append(" and ( 1=1 ) ");
        }
        return seach.toString();
    }



    public Page queryForPage(int currentPage,int pageSize,String seach){
        Page page = new Page();
        List<BankCustomerEntity> zzFs = new ArrayList<>();
        int allRow = bcd.getAllRowCount(seach);
        if(allRow>0){
            List zzList = bcd.getDoPage(seach,currentPage,pageSize);
            for(int i=0;i<zzList.size();i++) {
                BankCustomerEntity zzf = new BankCustomerEntity();
                Map map = (Map) zzList.get(i);
                zzf = zzf.mapToObj(map);
                zzFs.add(zzf);
            }
        }
        page.setPageSize(pageSize);
        page.setPageNo(currentPage);
        page.setTotalRecords(allRow);
        page.setList(zzFs);
        return page;
    }

    public void downloadFile(String seach, HttpServletResponse rep, String aj) throws Exception{
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT  c.* ");
        sql.append("FROM  bank_customer c right join rel_customer_aj s on c.zjhm = s.zjhm ");
        sql.append(" where 1=1 " + seach );
        List listZzxx = bcd.findBySQL(sql.toString());
        HSSFWorkbook wb = createExcel(listZzxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String(("银行卡客户信息\""+aj+".xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }
    public HSSFWorkbook createExcel(List listZzxx)throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        Cell cell = null;
        int b = 1;
        Sheet sheet = wb.createSheet("银行卡客户信息");
        Row row = createRow(sheet);
        for (int i = 0; i<listZzxx.size(); i++) {
            if(i>=65535&& i%65535==0){
                sheet = wb.createSheet("银行卡客户信息("+b+")");
                row = createRow(sheet);
                b+=1;
            }
            Map map = (Map) listZzxx.get(i);
            BankCustomerEntity zzf = new BankCustomerEntity();
            zzf = zzf.mapToObj(map);
            row = sheet.createRow(i%65535 + 1);
            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell = row.createCell(1);
            cell.setCellValue(zzf.getName());
            cell = row.createCell(2);
            cell.setCellValue(zzf.getZjlx());
            cell = row.createCell(3);
            cell.setCellValue(zzf.getZjhm());
            cell = row.createCell(4);
            cell.setCellValue(zzf.getXzz_xzqh());
            cell = row.createCell(5);
            cell.setCellValue(zzf.getLxdh());
            cell = row.createCell(6);
            cell.setCellValue(zzf.getLxsj());
            cell = row.createCell(7);
            cell.setCellValue(zzf.getDwdh());
            cell = row.createCell(8);
            cell.setCellValue(zzf.getZzdh());
            cell = row.createCell(9);
            cell.setCellValue(zzf.getGzdw());
            cell = row.createCell(10);
            cell.setCellValue(zzf.getEmail());

            if(i%65536==0){
                for (int a = 0; a < 15; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }

    public Row createRow(Sheet sheet){
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("证件类型");
        cell = row.createCell(3);
        cell.setCellValue("证件号码");
        cell = row.createCell(4);
        cell.setCellValue("现住址");
        cell = row.createCell(5);
        cell.setCellValue("联系电话");
        cell = row.createCell(6);
        cell.setCellValue("联系手机");
        cell = row.createCell(7);
        cell.setCellValue("单位电话");
        cell = row.createCell(8);
        cell.setCellValue("住宅电话");
        cell = row.createCell(9);
        cell.setCellValue("工作单位");
        cell = row.createCell(10);
        cell.setCellValue("邮箱");
        return row;
    }

    /**
     * bank_customer表数据读取
     * @param listPath
     * @param fields
     * @return
     */
    public List<BankCustomerEntity> getBankCustAll(List<String> listPath, List<List<String>> fields, AjEntity aj) {
        // 读取
        List<BankCustomerEntity> bankCustList = null;
        List<BankCustomerEntity> bankCustLists = new ArrayList<>();
        for(String path : listPath){
            String excelName = path.substring(path.lastIndexOf("\\")+1);
            for(List<String> field : fields){
                if(field.get(0).equals(excelName) && field.get(2).equals("bank_customer")){
                    if(path.endsWith(".xlsx")){
                        bankCustList = getBy2007ExcelAll(path,excelName,fields);
                    }else if(path.endsWith(".xls")){
                        bankCustList = getBy2003ExcelAll(path,excelName,fields);
                    }
                    bankCustLists.addAll(bankCustList);
                    break;
                }
            }
        }
        String inserttime = TimeFormatUtil.getDate("/");
        List<String> zjhm = new ArrayList<>();
        for(BankCustomerEntity c : bankCustLists){
            c.setInserttime(inserttime);
            bcd.saveOrUpdate(c);
            zjhm.add(c.getZjhm());
        }
        bcd.saveRel(zjhm,aj.getId());
        return bankCustLists;
    }

    /**
     * 03版数据读取
     * @param path
     * @param excelName
     * @param fields
     * @return
     */
    private List<BankCustomerEntity> getBy2003ExcelAll(String path, String excelName, List<List<String>> fields) {
        List<BankCustomerEntity> customerList = new ArrayList<>();
        InputStream is = null;
        Map<String,Integer> title = new HashMap<>();
        try {
            is = new FileInputStream(path);
            HSSFWorkbook wb = new HSSFWorkbook(is);
            for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                HSSFSheet sheet = wb.getSheetAt(numSheet);
                for (List<String> excel : fields) {
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
                                        for (int j = 3; j < excel.size(); j++) {
                                            if (cellName.equals(excel.get(j))) {
                                                title.put(excel.get(j), i);
                                            }
                                        }
                                    }
                                    temp = false;
                                } else {
                                    BankCustomerEntity zcxx = RowToEntity(row, excel, title);
                                    if (zcxx != null) {
                                        customerList.add(zcxx);
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
        return customerList;
    }

    /**
     * 07版数据读取
     * @param path
     * @param excelName
     * @param fields
     * @return
     */
    private List<BankCustomerEntity> getBy2007ExcelAll(String path, String excelName, List<List<String>> fields) {
        // 用于存放表格中列号
        List<BankCustomerEntity> custList = new ArrayList<>();
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
                for (List<String> field : fields) {
                    if (sheet == null) {
                        continue;
                    }else if(excelName.equals(field.get(0)) && sheet.getSheetName().equals(field.get(1))) {
                        boolean temp = true;
                        for (Row row : sheet) {
                            int cellNum = row.getLastCellNum();
                            // 字段长度小于3跳出本次循环
                            if (cellNum < 3) {
                                continue;
                            }
                            if (temp) {
                                if (row != null) {
                                    for (int i = 0; i < cellNum; i++) {
                                        Cell cell = row.getCell(i);
                                        String rowValue = MappingUtils.rowValue(cell);
                                        for (int j = 3; j < field.size(); j++) {
                                            if (rowValue.equals(field.get(j))) {
                                                title.put(field.get(j), i);
                                            }
                                        }
                                    }
                                }
                                temp = false;
                            } else {
                                if (row != null) {
                                    BankCustomerEntity cust = RowToEntity(row, field, title);
                                    if (cust != null) {
                                        custList.add(cust);
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
        return custList;
    }


    /**
     * excel行映射实体类
     * @param xssfRow
     * @param field
     * @param title
     * @return
     */
    private BankCustomerEntity RowToEntity(Row xssfRow, List<String> field, Map<String, Integer> title) {
        BankCustomerEntity custZzxx = new BankCustomerEntity();
        custZzxx.setZjhm(MappingUtils.mappingFieldString(xssfRow,field.get(3),title));
        custZzxx.setDwdh(MappingUtils.mappingFieldString(xssfRow,field.get(4),title));
        custZzxx.setDwdz(MappingUtils.mappingFieldString(xssfRow,field.get(5),title));
        custZzxx.setEmail(MappingUtils.mappingFieldString(xssfRow,field.get(6),title));
        custZzxx.setGzdw(MappingUtils.mappingFieldString(xssfRow,field.get(7),title));
        custZzxx.setLxdh(MappingUtils.mappingFieldString(xssfRow,field.get(8),title));
        custZzxx.setLxsj(MappingUtils.mappingFieldString(xssfRow,field.get(9),title));
        custZzxx.setName(MappingUtils.mappingFieldString(xssfRow,field.get(10),title));
        custZzxx.setXzz_xzqh(MappingUtils.mappingFieldString(xssfRow,field.get(11),title));
        custZzxx.setZjlx(MappingUtils.mappingFieldString(xssfRow,field.get(12),title));
        custZzxx.setZzdh(MappingUtils.mappingFieldString(xssfRow,field.get(13),title));
        return custZzxx;
    }
}
