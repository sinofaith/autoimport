package cn.com.sinofaith.service.bankServices;


import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.BankPersonEntity;
import cn.com.sinofaith.bean.bankBean.BankZcxxEntity;
import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.bankDao.BankPersonDao;
import cn.com.sinofaith.dao.bankDao.BankZzxxDao;
import cn.com.sinofaith.form.bankForm.BankZzxxForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.util.ExcelReader;
import cn.com.sinofaith.util.MappingUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Service
public class BankZzxxServices {
    @Autowired
    private BankZzxxDao bankzzd;
    @Autowired
    private BankPersonDao bpd;
    @Autowired
    private AJDao ad;

    public String getSeach(String seachCode, String seachCondition,String orderby,String desc, AjEntity aj,long userId){
        String ajid = getAjidByAjm(aj,userId);
        StringBuffer seach = new StringBuffer(" and c.aj_id in ("+ajid+") ");

        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if("khxm".equals(seachCondition)){
                seach.append(" and (s."+seachCondition + " like '%"+seachCode+"%' or c.jyxm like '%"+seachCode+"%')");
            } else {
                seach.append(" and c." + seachCondition + " like " + "'%" + seachCode + "%'");
            }
        }else{
            seach.append(" and ( 1=1 ) ");
        }
        if(orderby != null){
            seach .append(" order by c."+orderby).append(desc).append(",c.id");
        }
        return seach.toString();
    }

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
    public Page queryForPage(int currentPage,int pageSize,String seach){
        Page page = new Page();
        List<BankZzxxForm> zzFs = new ArrayList<>();
        BankZzxxForm zzf = new BankZzxxForm();
        int allRow = bankzzd.getAllRowCount(seach);
        if(allRow>0){
            List zzList = bankzzd.getDoPage(seach,currentPage,pageSize);
            int xh = 1;
            for(int i=0;i<zzList.size();i++) {
                Map map = (Map) zzList.get(i);
                zzf = zzf.mapToForm(map);
                zzf.setId(xh+(currentPage-1)*pageSize);
                zzFs.add(zzf);
                xh++;
            }
        }
        page.setPageSize(pageSize);
        page.setPageNo(currentPage);
        page.setTotalRecords(allRow);
        page.setList(zzFs);
        return page;
    }

    public void downloadFile(String seach, HttpServletResponse rep, String aj) throws Exception{
        String order = "";
        if(seach.contains("order")){
            seach.substring(seach.indexOf("order")-1,seach.length());
            seach = seach.replace(seach.substring(seach.indexOf("order")-1,seach.length())," ");
        }
        StringBuffer sql = new StringBuffer();
        sql.append("  select s.khxm jyxms,s.khxm dfxms,c.*  from(  ");
        sql.append("  select c.*,row_number() over(partition by c.yhkkh,c.jysj,c.jyje,c.jyye,c.dskh order by c.jysj ) su from bank_zzxx c  ");
        sql.append("  where 1=1"+seach+" ) c ");
        sql.append("  left join bank_person s on c.yhkkh = s.yhkkh ");
        sql.append("   left join bank_person d on c.dskh = d.yhkkh ");
        sql.append(" where su=1 "+order);
        List listZzxx = bankzzd.findBySQL(sql.toString());
        HSSFWorkbook wb = createExcel(listZzxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String(("银行卡转账信息(\""+aj+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public HSSFWorkbook createExcel(List listZzxx)throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        Cell cell = null;
        int b = 1;
        Sheet sheet = wb.createSheet("银行卡转账信息");
        Row row = createRow(sheet);
        for (int i = 0; i<listZzxx.size(); i++) {
            if(i>=65535&& i%65535==0){
                sheet = wb.createSheet("银行卡转账信息("+b+")");
                row = createRow(sheet);
                b+=1;
            }
            Map map = (Map) listZzxx.get(i);
            BankZzxxForm zzf = new BankZzxxForm();
            zzf = zzf.mapToForm(map);
            row = sheet.createRow(i%65535 + 1);
            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell = row.createCell(1);
            cell.setCellValue(zzf.getYhkkh());
            cell = row.createCell(2);
            cell.setCellValue(zzf.getJyxm());
            cell = row.createCell(3);
            cell.setCellValue(zzf.getJysj());
            cell = row.createCell(4);
            cell.setCellValue(zzf.getJyje().toString());
            cell = row.createCell(5);
            cell.setCellValue("-1".equals(zzf.getJyye().toString()) ? "" : zzf.getJyye().toString());
            cell = row.createCell(6);
            cell.setCellValue(zzf.getSfbz());
            cell = row.createCell(7);
            cell.setCellValue(zzf.getDskh());
            cell = row.createCell(8);
            cell.setCellValue(zzf.getDsxm());
            cell = row.createCell(9);
            cell.setCellValue(zzf.getZysm());
            cell = row.createCell(10);
            cell.setCellValue(zzf.getJyfsd());
            cell = row.createCell(11);
            cell.setCellValue(zzf.getJywdmc());
            cell = row.createCell(12);
            cell.setCellValue(zzf.getBz());

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
        cell.setCellValue("交易卡号");
        cell = row.createCell(2);
        cell.setCellValue("交易户名");
        cell = row.createCell(3);
        cell.setCellValue("交易时间");
        cell = row.createCell(4);
        cell.setCellValue("交易金额(元)");
        cell = row.createCell(5);
        cell.setCellValue("交易余额(元)");
        cell = row.createCell(6);
        cell.setCellValue("收付标志");
        cell = row.createCell(7);
        cell.setCellValue("对手卡号");
        cell = row.createCell(8);
        cell.setCellValue("对手户名");
        cell = row.createCell(9);
        cell.setCellValue("摘要说明");
        cell = row.createCell(10);
        cell.setCellValue("交易发生地");
        cell = row.createCell(11);
        cell.setCellValue("交易网点名称");
        cell = row.createCell(12);
        cell.setCellValue("备注");
        return row;
    }

    public String getByYhkkh(String zh,String jylx,String type,String sum,String zhlx,AjEntity aj,int page,String order,long userId){
        Page pages = new Page();
        Gson gson = new GsonBuilder().serializeNulls().create();
        String ajid = getAjidByAjm(aj,userId);

        String seach ="";
        if("tjjg".equals(type)){
            if("0".equals(zhlx)){
                seach="and c.sfbz is not null and (c.yhkkh='"+zh+"') ";
            }else{
                seach="and c.sfbz is not null and (c.dskh='"+zh+"') ";
            }
        }else{
            seach=" and (c.yhkkh='"+zh+"') ";
            seach+=" and (c.dskh = '"+jylx+"' or c.bcsm = '"+jylx+"') ";
        }
//        if(aj.getFlg()==1){
//            seach +=" and c.shmc not like'%红包%'";
//        }
        seach += "and c.aj_id in("+ajid+") "+order;


//        int allRow = bankzzd.getCount(seach);
        int allRow = Integer.parseInt(sum);
        List zzList = bankzzd.getDoPageDis(seach,page,100);



        List<BankZzxxForm> zzFs = new ArrayList<>();
        BankZzxxForm zzf = new BankZzxxForm();
        for(int i=0;i<zzList.size();i++){
            Map map = (Map)zzList.get(i);
            zzf = zzf.mapToForm(map);
            zzf.setId(i+1+(page-1)*100);
            zzFs.add(zzf);
        }
        pages.setTotalRecords(allRow);
        pages.setPageSize(100);
        pages.setPageNo(page);
        pages.setList(zzFs);
        return gson.toJson(pages);
    }

    public List<BankZzxxEntity> getAll(long aj_id){
       List<BankZzxxEntity> zz = bankzzd.find(" from BankZzxxEntity where aj_id = "+aj_id);
       return zz;
    }

    public Map<String,Map<String,List<String>>> readExcel(String uploadPath) {
        Map<String,Map<String,List<String>>> excelMap = new HashMap<>();
        Map<String,List<String>> sheetMap = new HashMap<>();
        // 读取
        List<String> listPath = getFileList(uploadPath);
        String excelName = null;
        for (String path : listPath) {
            excelName = path.substring(path.lastIndexOf("\\")+1);
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

    private List<String> getFileList(String uploadPath) {
        List<String> listPath = new ArrayList<>();
        File dir = new File(uploadPath);
        File[] files = dir.listFiles();
        for (File file : files) {
            listPath.add(file.getAbsolutePath());
        }
        return listPath;
    }

    /**
     * bank_zzxx表数据读取
     * @param listPath
     * @param fields
     * @return
     */
    public int getBankZzxxAll(List<String> listPath, List<List<String>> fields,
                                               AjEntity aj, List<BankZzxxEntity> listZzxx) {
        // 读取
        List<BankZzxxEntity> bankZzxxList = null;
        List<BankZzxxEntity> bankZzxxLists = new ArrayList<>();
        for(String path : listPath){
            String excelName = path.substring(path.lastIndexOf("\\")+1);
            for(List<String> field : fields){
                if(field.get(0).equals(excelName) && field.get(2).equals("bank_zzxx")){
                    if(path.endsWith(".xlsx")){
                        bankZzxxList = getBy2007ExcelAll(path,excelName,fields);
                    }else if(path.endsWith(".xls")){
                        bankZzxxList = getBy2003ExcelAll(path,excelName,fields);
                    }
                    bankZzxxLists.addAll(bankZzxxList);
                    break;
                }
            }
        }
        int num = bankzzd.insertZzxx(bankZzxxLists, aj.getId(), listZzxx);
        List<BankPersonEntity> allbp = bpd.find("from BankPersonEntity");
        Map<String, String> allBp = new HashMap<>();
        for (int j = 0; j < allbp.size(); j++) {
            allBp.put((allbp.get(j).getYhkkh()).replace("null", ""), null);
        }
        Map<String,BankPersonEntity> mapZ= new HashMap<>();
        for (int g = 0; g < listZzxx.size(); g++) {
            BankPersonEntity bp = new BankPersonEntity();
            bp.setYhkkh(listZzxx.get(g).getDskh());
            bp.setYhkzh(String.valueOf(aj.getId()));
            bp.setXm(listZzxx.get(g).getDsxm());
            if (bp.getYhkkh()!=null && bp.getXm()!=null&&bp.getYhkkh().length() > 0 && bp.getXm().length() > 0) {
                if (bp.getXm().contains("支付宝")) {
                    bp.setXm("支付宝（中国）网络技术有限公司");
                } else if (bp.getXm().contains("微信") || bp.getXm().contains("财付通")) {
                    bp.setXm("财付通支付科技有限公司");
                }
                mapZ.put((bp.getYhkkh()).replace("null", ""), bp);
            } else {
                continue;
            }
        }
        List<String> str = new ArrayList<>();
        for (String o : mapZ.keySet()) {
            if (allBp.containsKey(o)) {
                str.add(o);
            }
        }
        for (int s = 0; s < str.size(); s++) {
            mapZ.remove(str.get(s));
        }
        allbp = new ArrayList<>(mapZ.values());
        num += bpd.add(allbp, String.valueOf(aj.getId()));
        return num;
    }

    /**
     * 03版数据读取
     * @param path
     * @param excelName
     * @param fields
     * @return
     */
    private List<BankZzxxEntity> getBy2003ExcelAll(String path, String excelName, List<List<String>> fields) {
        List<BankZzxxEntity> zzxxList = new ArrayList<>();
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
                                    BankZzxxEntity zzxx = RowToEntity(row, excel, title);
                                    if (zzxx != null) {
                                        zzxxList.add(zzxx);
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
        return zzxxList;
    }

    /**
     * 07版数据读取
     * @param path
     * @param excelName
     * @param fields
     * @return
     */
    private List<BankZzxxEntity> getBy2007ExcelAll(String path, String excelName, List<List<String>> fields) {
        // 用于存放表格中列号
        List<BankZzxxEntity> zzxxList = new ArrayList<>();
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
                                    BankZzxxEntity zzxx = RowToEntity(row, field, title);
                                    if (zzxx != null) {
                                        zzxxList.add(zzxx);
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
        return zzxxList;
    }

    /**
     * excel行映射实体类
     * @param xssfRow
     * @param field
     * @param title
     * @return
     */
    private BankZzxxEntity RowToEntity(Row xssfRow, List<String> field, Map<String, Integer> title) {
        BankZzxxEntity bankZzxx = new BankZzxxEntity();
        String YHKKH = MappingUtils.mappingFieldString(xssfRow,field.get(3),title);
        if(YHKKH.indexOf("_")>5){
            bankZzxx.setYhkkh(YHKKH.split("_")[0]);
        }else {bankZzxx.setYhkkh(YHKKH);}
        bankZzxx.setJysj(MappingUtils.mappingFieldString(xssfRow,field.get(4),title));
        bankZzxx.setJyje(MappingUtils.mappingFieldBigDecimal(xssfRow,field.get(5),title));
        bankZzxx.setJyye(MappingUtils.mappingFieldBigDecimal(xssfRow,field.get(6),title));
        bankZzxx.setSfbz(MappingUtils.mappingFieldString(xssfRow,field.get(7),title));
        bankZzxx.setDskh(MappingUtils.mappingFieldString(xssfRow,field.get(14),title));
        bankZzxx.setDsxm(MappingUtils.mappingFieldString(xssfRow,field.get(9),title));
        bankZzxx.setDssfzh(MappingUtils.mappingFieldString(xssfRow,field.get(10),title));
        bankZzxx.setZysm(MappingUtils.mappingFieldString(xssfRow,field.get(11),title));
        bankZzxx.setJysfcg(MappingUtils.mappingFieldString(xssfRow,field.get(12),title));
        bankZzxx.setYhkzh(MappingUtils.mappingFieldString(xssfRow,field.get(13),title));
        bankZzxx.setDszh(MappingUtils.mappingFieldString(xssfRow,field.get(8),title));
        bankZzxx.setDskhh(MappingUtils.mappingFieldString(xssfRow,field.get(15),title));
        bankZzxx.setJywdmc(MappingUtils.mappingFieldString(xssfRow,field.get(16),title));
        bankZzxx.setDsjyye(MappingUtils.mappingFieldBigDecimal(xssfRow,field.get(17),title));
        bankZzxx.setDsye(MappingUtils.mappingFieldBigDecimal(xssfRow,field.get(18),title));
        bankZzxx.setBz(MappingUtils.mappingFieldString(xssfRow,field.get(19),title));
        bankZzxx.setJyzjh(MappingUtils.mappingFieldString(xssfRow,field.get(20),title));
        bankZzxx.setJyfsd(MappingUtils.mappingFieldString(xssfRow,field.get(21),title));
        bankZzxx.setJyxm(MappingUtils.mappingFieldString(xssfRow,field.get(22),title));
        bankZzxx.setBcsm(MappingUtils.mappingFieldString(xssfRow,field.get(23),title));
        if(bankZzxx.getDskh()==null || "".equals(bankZzxx.getDskh())){
            String bcsm = bankZzxx.getYhkkh()+"-";
            if(bankZzxx.getDsxm()!=null && !"".equals(bankZzxx.getDsxm())){
                bcsm+=bankZzxx.getDsxm();
            }else if(bankZzxx.getZysm()!=null && !"".equals(bankZzxx.getZysm())){
                bcsm+=bankZzxx.getZysm();
            }else if(bankZzxx.getBz()!=null && !"".equals(bankZzxx.getBz())){
                bcsm+=bankZzxx.getBz();
            }else {
                bcsm += "空账户";
            }
            bankZzxx.setBcsm(bcsm);
        }
        return bankZzxx;
    }
}
