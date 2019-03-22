package cn.com.sinofaith.service.cftServices;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.cftDao.CftZzxxDao;
import cn.com.sinofaith.form.cftForm.CftZzxxForm;
import cn.com.sinofaith.page.Page;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Me. on 2018/5/22
 */
@Service
public class CftZzxxService {
    @Autowired
    private CftZzxxDao cftzzd;
    @Autowired
    private AJDao ad;

    public Page queryForPage(int currentPage,int pageSize,String seach){
        Page page = new Page();
        int allRow = cftzzd.getAllRowCount(seach);
        List<CftZzxxForm> zzFs = new ArrayList<>();
        CftZzxxForm zzf = new CftZzxxForm();
        if(allRow>0){
            List zzList = cftzzd.getDoPage(seach,currentPage,pageSize);
            int xh =1;
            for(int i=0;i<zzList.size();i++){
                Map map = (Map)zzList.get(i);
                zzf = zzf.mapToForm(map);
                zzf.setId(xh+(currentPage-1)*pageSize);
                zzFs.add(zzf);
                xh++;
            }
        }
        page.setList(zzFs);
        page.setPageNo(currentPage);
        page.setPageSize(pageSize);
        page.setTotalRecords(allRow);
        return page;
    }

    public int getCountBySeach(String filter){
        int sum = cftzzd.getAllRowCount(filter);
        return sum/500000 ==0?sum/500000:sum/500000+1;
    }

//    public Page queryForPage(int currentPage, int pageSize, String seach){
//        Page page = new Page();
//        //总记录数
//        int allRow = cftzzd.getAllRowCount(seach);
//        List<CftZzxxEntity> cftzzxxs = new ArrayList<CftZzxxEntity>();
//        int xh = 1;
//        if(allRow != 0) {
//            cftzzxxs = cftzzd.getDoPage(seach,currentPage,pageSize);
//            for(int i=0; i<cftzzxxs.size();i++){
//                cftzzxxs.get(i).setId(xh+(currentPage-1)*10);
//                xh++;
//            }
//        }
//        page.setList(cftzzxxs);
//        page.setPageNo(currentPage);
//        page.setPageSize(pageSize);
//        page.setTotalRecords(allRow);
//        return page;
//    }

    public List<CftZzxxEntity> getAll(long ajid,long filter){
        String seach = "";
        if(filter==1){
            seach=" and shmc not like '%红包%'";
        }
        List<CftZzxxEntity> zzs = cftzzd.getAlla(ajid,seach);

        return zzs;
    }

    public void downloadFile(String seach, HttpServletResponse rep,String aj) throws Exception{
        List listZzxx = cftzzd.findBySQL("select s.xm,c.* from cft_zzxx c left join cft_person s on c.zh = s.zh where 1=1 "+seach);
        HSSFWorkbook wb = createExcel(listZzxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String(("财付通转账信息(\""+aj+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public Row createRow(Sheet sheet){
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("微信账户");
        cell = row.createCell(3);
        cell.setCellValue("借贷类型");
        cell = row.createCell(4);
        cell.setCellValue("交易类型");
        cell = row.createCell(5);
        cell.setCellValue("商户名称");
        cell = row.createCell(6);
        cell.setCellValue("交易金额(元)");
        cell = row.createCell(7);
        cell.setCellValue("交易时间");
        cell = row.createCell(8);
        cell.setCellValue("发送方");
        cell = row.createCell(9);
        cell.setCellValue("发送金额(元)");
        cell = row.createCell(10);
        cell.setCellValue("接收方");
        cell = row.createCell(11);
        cell.setCellValue("接收金额(元)");
        return row;
    }

    public HSSFWorkbook createExcel(List listZzxx)throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        Cell cell = null;
        int b = 1;
        Sheet sheet = wb.createSheet("财付通转账信息");
            Row row = createRow(sheet);
            for (int i = 0; i<listZzxx.size(); i++) {
                if(i>=65535&& i%65535==0){
                    sheet = wb.createSheet("财付通转账信息("+b+")");
                    row = createRow(sheet);
                    b+=1;
                }
                Map map = (Map) listZzxx.get(i);
                row = sheet.createRow(i%65535 + 1);
                cell = row.createCell(0);
                cell.setCellValue(i + 1);
                cell = row.createCell(1);
                if (map.get("XM") != null && map.get("XM").toString().length() > 0) {
                    cell.setCellValue(map.get("XM").toString());
                }
                cell = row.createCell(2);
                cell.setCellValue(map.get("ZH").toString());
                cell = row.createCell(3);
                cell.setCellValue(map.get("JDLX").toString());
                cell = row.createCell(4);
                cell.setCellValue(map.get("JYLX").toString());
                cell = row.createCell(5);
                cell.setCellValue(map.get("SHMC").toString());
                cell = row.createCell(6);
                cell.setCellValue(map.get("JYJE").toString());
                cell = row.createCell(7);
                cell.setCellValue(map.get("JYSJ").toString());
                cell = row.createCell(8);
                if (map.get("FSF") != null && map.get("FSF").toString().length() > 0) {
                    cell.setCellValue(map.get("FSF").toString());
                }
                cell = row.createCell(9);
                cell.setCellValue(map.get("FSJE").toString().equals("0") ? "":map.get("FSJE").toString());
                cell = row.createCell(10);
                if (map.get("JSF") != null && map.get("JSF").toString().length() > 0) {
                    cell.setCellValue(map.get("JSF").toString());
                }
                cell = row.createCell(11);
                cell.setCellValue(map.get("JSJE").toString().equals("0") ? "":map.get("JSJE").toString());
                if(i%65536==0){
                    for (int a = 0; a < 13; a++) {
                        sheet.autoSizeColumn(a);
                    }
                }
            }
        return wb;
    }

    public String getSeach(String seachCode, String seachCondition,String orderby,String desc, AjEntity aj){
       String ajid = getAjidByAjm(aj);
        StringBuffer seach = new StringBuffer(" and c.aj_id in ("+ajid+") ");

        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if("xm".equals(seachCondition)){
                seach.append(" and s."+seachCondition + " like "+"'"+seachCode+"'");
            }else {
                seach.append(" and c." + seachCondition + " like " + "'" + seachCode + "'");
            }
        }else{
            seach.append(" and ( 1=1 ) ");
        }
        if(orderby != null){
            seach .append(" order by c."+orderby).append(desc).append(",c.id");
        }
        return seach.toString();
    }

    public void deleteByAj_id(long id){
        cftzzd.deleteByAjid(id);
    }

    public String getByJyzhlx(String zh,String jylx,String sum,String type,AjEntity aj,int page,String orders){
        Page pages = new Page();
        Gson gson = new GsonBuilder().serializeNulls().create();
        String ajid = getAjidByAjm(aj);

        String seach ="";
        if("jylx".equals(type)){
            seach=" and c.zh='"+zh+"' and c.jylx='"+jylx+"' ";
            if(jylx.equals("提现")){
                seach=" and c.zh='"+zh+"' and (c.jylx='"+jylx+"' or c.jylx = '微信提现手续费') ";
            }
            if(jylx.equals("转帐(有对手账户)")){
                seach=" and c.zh='"+zh+"' and c.fsf is not null and c.jsf is not null ";
            }
            if(jylx.equals("转帐(无对手账户)")){
                seach=" and c.zh='"+zh+"' and (c.fsf is null or c.jsf is null) ";
            }
        }else{
            seach=" and c.zh='"+zh+"' and (c.fsf='"+jylx+"' or c.jsf='"+jylx+"') ";
            if(zh.equals(jylx)){
                seach = "and c.fsf = c.jsf and c.zh='"+zh+"' ";
            }
        }
        if(aj.getFlg()==1){
            seach +=" and c.shmc not like'%红包%'";
        }
        seach += "and aj_id in("+ajid+") "+orders;

//        int allRow = cftzzd.getAllRowCount(seach);
        int allRow = Integer.parseInt(sum);
        List zzList = cftzzd.getDoPage(seach,page,100);

        List<CftZzxxForm> zzFs = new ArrayList<>();
        CftZzxxForm zzf = new CftZzxxForm();
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

    public String getAjidByAjm(AjEntity aj){
        String[] ajm = new String[]{};
        StringBuffer ajid = new StringBuffer();
        if(aj.getAj().contains(",")) {
            ajm = aj.getAj().split(",");
            for (int i = 0; i < ajm.length; i++) {
                ajid.append(ad.findFilter(ajm[i]).get(0).getId());
                if (i != ajm.length - 1) {
                    ajid.append(",");
                }
            }
        }else{
            ajid.append(aj.getId());
        }
        return ajid.toString();
    }

    /**
     * 财付通数据导入
     * @param uploadPath
     * @param field
     * @param id
     * @return
     */
    public int insertCft(String uploadPath, List<List<String>> field, long id) {
        // 读取
        List<String> listPath = getCftFileList(uploadPath);

        List<CftZzxxEntity> cftAllList = new ArrayList<>();
        int sum = 0;
        for (String path : listPath) {
            List<CftZzxxEntity> cftList = null;
            String excelName = path.substring(path.lastIndexOf("\\")+1);
            if(path.endsWith(".xlsx")){
                cftList = getBy2007ExcelAll(path,excelName,field);
            }else if(path.endsWith(".xls")){
                cftList = getBy2003ExcelAll(path,excelName,field);
            }
            if(cftList.size()>0){
                cftAllList.addAll(cftList);
            }
        }
        cftAllList = new ArrayList<>(new HashSet<>(cftAllList));
        sum += cftzzd.insertZzxx(cftAllList, id);
        return sum;
    }

    private List<CftZzxxEntity> getBy2003ExcelAll(String path, String excelName, List<List<String>> field) {
        List<CftZzxxEntity> zzxxList = new ArrayList<>();
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
                                    CftZzxxEntity zzxxs = RowToEntity(row, excel, title);
                                    if (zzxxs != null) {
                                        zzxxList.add(zzxxs);
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

    private List<CftZzxxEntity> getBy2007ExcelAll(String path, String excelName, List<List<String>> field) {
        // 用于存放表格中列号
        List<CftZzxxEntity> zzxxList = new ArrayList<>();
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
                                    CftZzxxEntity zzxx = RowToEntity(row,excel,title);
                                    if(zzxx!=null){
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

    private CftZzxxEntity RowToEntity(Row xssfRow, List<String> field, Map<String, Integer> title) {
        CftZzxxEntity zzxx = new CftZzxxEntity();
        if(field.get(2).equals("无") || xssfRow.getCell(title.get(field.get(2)))==null){
            zzxx.setZh("");
        }else{
            String zh = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(2)))).replace(",","").replace("-","");
            zzxx.setZh(regexStr(zh));
        }
        if(field.get(3).equals("无") || xssfRow.getCell(title.get(field.get(3)))==null){
            zzxx.setJydh("");
        }else{
            String jydh = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(3)))).replace(",","").replace("-","");
            zzxx.setJydh(regexStr(jydh));
        }
        if(field.get(4).equals("无") || xssfRow.getCell(title.get(field.get(4)))==null){
            zzxx.setJdlx("");
        }else{
            String jdlx = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(4)))).replace(",","").replace("-","");
            if(jdlx.contains("入"))
                jdlx = "入";
            else if(jdlx.contains("出"))
                jdlx = "出";
            zzxx.setJdlx(regexStr(jdlx));
        }
        if(field.get(5).equals("无") || xssfRow.getCell(title.get(field.get(5)))==null){
            zzxx.setJylx("");
        }else{
            String jylx = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(5)))).replace(",","").replace("-","");
            zzxx.setJylx(regexStr(jylx));
        }
        if(field.get(6).equals("无") || xssfRow.getCell(title.get(field.get(6)))==null){
            zzxx.setJyje(new BigDecimal(0));
        }else {
            String jyje = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(6)))).replace(",", "").replace("-", "");
            if (!"".equals(jyje))
                zzxx.setJyje(new BigDecimal(jyje));
        }
        if(field.get(7).equals("无") || xssfRow.getCell(title.get(field.get(7)))==null){
            zzxx.setZhye(new BigDecimal(0));
        }else{
            String zhye = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(7)))).replace(",","").replace("-","");
            if (!"".equals(zhye))
                zzxx.setZhye(new BigDecimal(zhye));
        }
        if(field.get(8).equals("无") || xssfRow.getCell(title.get(field.get(8)))==null){
            zzxx.setJysj("");
        }else{
            String jysj = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(8)))).replace(",","").replace("-","");
            zzxx.setJysj(regexStr(jysj));
        }
        if(field.get(9).equals("无") || xssfRow.getCell(title.get(field.get(9)))==null){
            zzxx.setYhlx("");
        }else{
            String yhlx = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(9)))).replace(",","").replace("-","");
            zzxx.setYhlx(regexStr(yhlx));
        }
        if(field.get(10).equals("无") || xssfRow.getCell(title.get(field.get(10)))==null){
            zzxx.setJysm("");
        }else{
            String jysm = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(10)))).replace(",","").replace("-","");
            zzxx.setJysm(regexStr(jysm));
        }
        if(field.get(11).equals("无") || xssfRow.getCell(title.get(field.get(11)))==null){
            zzxx.setShmc("");
        }else{
            String shmc = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(11)))).replace(",","").replace("-","");
            zzxx.setShmc(regexStr(shmc));
        }
        if(field.get(12).equals("无") || xssfRow.getCell(title.get(field.get(12)))==null){
            zzxx.setFsf("");
        }else{
            String fsf = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(12)))).replace(",","").replace("-","");
            zzxx.setFsf(regexStr(fsf));
        }
        if(field.get(13).equals("无") || xssfRow.getCell(title.get(field.get(13)))==null){
            zzxx.setFsje(new BigDecimal(0));
        }else{
            String fsje = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(13)))).replace(",","").replace("-","");
            if (!"".equals(fsje))
                zzxx.setFsje(new BigDecimal(fsje));
        }
        if(field.get(14).equals("无") || xssfRow.getCell(title.get(field.get(14)))==null){
            zzxx.setJsf("");
        }else{
            String jsf = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(14)))).replace(",","").replace("-","");
            zzxx.setJsf(regexStr(jsf));
        }
        if(field.get(15).equals("无") || xssfRow.getCell(title.get(field.get(15)))==null){
            zzxx.setJssj("");
        }else{
            String jssj = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(15)))).replace(",","").replace("-","");
            zzxx.setJssj(regexStr(jssj));
        }
        if(field.get(16).equals("无") || xssfRow.getCell(title.get(field.get(16)))==null){
            zzxx.setJsje(new BigDecimal(0));
        }else{
            String jsje = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(16)))).replace(",","").replace("-","");
            if (!"".equals(jsje))
                zzxx.setJsje(new BigDecimal(jsje));
        }
        return zzxx;
    }

    /**
     * 正则截取
     * @param regStr
     * @return
     */
    public String regexStr(String regStr){
        if(regStr.contains("：")){
            String reg = "[^.]+：(.*)";
            Pattern pet = Pattern.compile(reg);
            Matcher matcher = pet.matcher(regStr);
            while (matcher.find()) {
                regStr = matcher.group(1);
            }
        }
        return regStr;
    }

    private List<String> getCftFileList(String uploadPath) {
        List<String> listPath = new ArrayList<>();
        File dir = new File(uploadPath);
        File[] files = dir.listFiles();
        for (File file : files) {
            listPath.add(file.getAbsolutePath());
        }
        return listPath;
    }

    /**
     * 删除文件
     * @param uploadPathd
     */
    public void deleteFile(File uploadPathd) {
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