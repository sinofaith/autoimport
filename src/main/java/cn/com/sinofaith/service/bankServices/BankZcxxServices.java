package cn.com.sinofaith.service.bankServices;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.BankPersonEntity;
import cn.com.sinofaith.bean.bankBean.BankZcxxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.dao.bankDao.BankPersonDao;
import cn.com.sinofaith.dao.bankDao.BankTjjgDao;
import cn.com.sinofaith.dao.bankDao.BankTjjgsDao;
import cn.com.sinofaith.dao.bankDao.BankZcxxDao;
import cn.com.sinofaith.form.cftForm.CftTjjgForm;
import cn.com.sinofaith.form.cftForm.CftTjjgsForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.cftServices.CftZzxxService;
import cn.com.sinofaith.util.CreatePdfUtils;
import cn.com.sinofaith.util.MappingUtils;
import cn.com.sinofaith.util.WatermarkImageUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class BankZcxxServices {

    @Autowired
    private CftZzxxService cfts;
    @Autowired
    private BankTjjgDao tjjd;
    @Autowired
    private BankZcxxDao bzcd;
    @Autowired
    private BankTjjgsDao banktjsd;
    @Autowired
    private BankPersonDao bpd;

    public String getBq(String yhkh,long ajid){
        String sql = " select to_char(c.zzsum) zzsum ,to_char(r.zzrsum) zzrsum ,to_char(j.tjsum) tjsum" +
                " ,to_char(s.tssum) tssum,to_char(g.gtsum) gtsum from bank_zcxx z left join (" +
                " select yhkkh,count(1) as zzsum from bank_zzxx where aj_id ="+ajid+" group by yhkkh" +
                " ) c on z.yhkkh = c.yhkkh" +
                " left join (" +
                " select dskh,count(1) as zzrsum from bank_zzxx where aj_id ="+ajid+" group by dskh" +
                ") r on z.yhkkh = r.dskh " +
                " left join ( select jyzh,count(1) as tjsum from bank_tjjg  where aj_id="+ajid+" group by jyzh" +
                ") j on z.yhkkh = j.jyzh " +
                " left join ( select jyzh,count(1) as tssum from bank_tjjgs where aj_id="+ajid+" group by jyzh" +
                ") s on z.yhkkh = s.jyzh" +
                " left join (select c.jyzh,count(c.jyzh) as gtsum from bank_tjjgs c " +
                "right join ( select t.dfzh,count(1) as num from bank_tjjgs t " +
                " where t.dfzh not in( select distinct t1.jyzh from bank_tjjgs t1) " +
                "and t.aj_id="+ajid+" group by dfzh having(count(1)>=2) ) a on c.dfzh = a.dfzh    " +
                " where a.num is not null  and aj_id ="+ajid+" and ( 1=1 ) " +
                "group by c.jyzh) g on z.yhkkh = g.jyzh" +
                " where z.aj_id ="+ajid+" and z.yhkkh = '"+yhkh+"'";
        List list = bzcd.findBySQL(sql);
        Gson gson = new GsonBuilder().serializeNulls().create();
        Map<String,String> mapResult = new HashMap<>();
        for(int i=0; i<list.size();i++){
            Map map = (Map)list.get(i);
            mapResult.put("zzsum",(String)map.get("ZZSUM"));
            mapResult.put("zzrsum",(String)map.get("ZZRSUM"));
            mapResult.put("tjsum",(String)map.get("TJSUM"));
            mapResult.put("tssum",(String)map.get("TSSUM"));
            mapResult.put("gtsum",(String)map.get("GTSUM"));
        }

        return gson.toJson(mapResult);
    }

    public String getSeach(String seachCode, String seachCondition,String orderby, String desc, AjEntity aj,long userId){
        StringBuffer seach = new StringBuffer();
        String ajid = cfts.getAjidByAjm(aj,userId);

        if(seachCode!=null){
            seachCode =seachCode.trim().replace("\r\n","").replace("，","").replace("\t","");
            seach = seach.append(" and aj_id in ("+ajid.toString()+") and "+ seachCondition+" like "+"'%"+ seachCode +"%'");
        }else{
            seach = seach.append(" and aj_id in ("+ajid.toString()+") ");
        }
        if(orderby!=null){
            seach.append(" order by " +orderby + desc);
        }
        return seach.toString();
    }

    public Page queryForPage(int pageNo,int pageSize,String seach){
        Page page = new Page();
        int allRow = bzcd.getAllRowCount(seach);
        List<BankZcxxEntity> listZc = new ArrayList<>();
        if(allRow>0){
            int xh = 1;
            listZc = bzcd.getDoPage(seach,pageNo,pageSize);
            for(int i=0; i<listZc.size();i++){
                listZc.get(i).setId(xh+(pageNo-1)*pageSize);
                xh++;
            }
        }
        page.setList(listZc);
        page.setPageNo(pageNo);
        page.setTotalRecords(allRow);
        page.setPageSize(pageSize);

        return page;
    }

    public void downloadFile(String seach, HttpServletResponse rep, String aj) throws Exception{
        List<BankZcxxEntity> listZcxx = bzcd.find("from BankZcxxEntity where 1=1"+seach+" order by inserttime,khzjh nulls last desc");
        HSSFWorkbook wb = createExcel(listZcxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-Disposition","attachment;filename="+new String(("银行卡开户信息\""+aj+".xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public HSSFWorkbook createExcel(List<BankZcxxEntity> listZcxx) throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("银行卡开户信息");
        Row row = createRow(sheet);
        Cell cell = null;
        int i = 1;
        int b = 1;
        for(BankZcxxEntity zcxx:listZcxx){
            if(i>=65536 && i%65536==0){
                sheet = wb.createSheet("银行卡开户信息("+b+")");
                row = createRow(sheet);
                b+=1;
            }
            row = sheet.createRow(i%65536);
            cell = row.createCell(0);
            cell.setCellValue(i);
            cell = row.createCell(1);
            cell.setCellValue(zcxx.getZhzt());
            cell = row.createCell(2);
            cell.setCellValue(zcxx.getYhkkh());
            cell = row.createCell(3);
            cell.setCellValue(zcxx.getYhkzh());
            cell = row.createCell(4);
            cell.setCellValue(zcxx.getKhxm());
            cell = row.createCell(5);
            cell.setCellValue(zcxx.getKhzjh());
            cell = row.createCell(6);
            cell.setCellValue((zcxx.getZhye()==null||"-1".equals(zcxx.getZhye().toString()))? "":zcxx.getZhye().toString());
            cell = row.createCell(7);
            cell.setCellValue((zcxx.getKyye()==null||"-1".equals(zcxx.getKyye().toString()))? "":zcxx.getKyye().toString());
            cell = row.createCell(8);
            cell.setCellValue(zcxx.getKhsj());
            cell = row.createCell(9);
            cell.setCellValue(zcxx.getKhh());
            if(i%65536==0) {
                for (int a = 0; a < 11; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
            i++;
        }
        return wb;
    }

    public Row createRow(Sheet sheet){
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("账户状态");
        cell = row.createCell(2);
        cell.setCellValue("交易卡号");
        cell = row.createCell(3);
        cell.setCellValue("交易账号");
        cell = row.createCell(4);
        cell.setCellValue("开户姓名");
        cell = row.createCell(5);
        cell.setCellValue("开户证件号");
        cell = row.createCell(6);
        cell.setCellValue("账户余额");
        cell = row.createCell(7);
        cell.setCellValue("可用余额");
        cell = row.createCell(8);
        cell.setCellValue("开户时间");
        cell = row.createCell(9);
        cell.setCellValue("开户行");
        return row;
    }

    public Document createPDF(OutputStream op, AjEntity aj) {
        Document document = new Document();
        // 账户统计信息 进账
        String seach1 = " and aj_id="+aj.getId()+"and c.zhlb != '第三方账户' order by c.jzzje desc nulls last";
        List bankList = tjjd.getDoPage(seach1, 1, 10);
        List<CftTjjgForm> bankTjjgs1 = new ArrayList<>();
        CftTjjgForm tjjgForm = new CftTjjgForm();
        for(int i=0;i<bankList.size();i++){
            Map map = (Map)bankList.get(i);
            tjjgForm = tjjgForm.mapToForm(map);
            bankTjjgs1.add(tjjgForm);
        }
        // 账户统计信息 出账
        String seach2 = " and aj_id="+aj.getId()+" and c.zhlb != '第三方账户' order by c.czzje desc nulls last";
        bankList = tjjd.getDoPage(seach2, 1, 10);
        List<CftTjjgForm> bankTjjgs2 = new ArrayList<>();
        for(int i=0;i<bankList.size();i++){
            Map map = (Map)bankList.get(i);
            tjjgForm = tjjgForm.mapToForm(map);
            bankTjjgs2.add(tjjgForm);
        }
        // 账户点对点统计信息 进账
        String seach3 = " and aj_id="+aj.getId()+" and (d.dsfzh != 1 or d.dsfzh is null)  order by c.czzje desc nulls last";
        bankList = banktjsd.getDoPage(seach3, 1, 10);
        List<CftTjjgsForm> bankTjjgss3 = new ArrayList<>();
        CftTjjgsForm cftForm = null;
        for(int i=0;i<bankList.size();i++) {
            Map map = (Map) bankList.get(i);
            cftForm = new CftTjjgsForm();
            cftForm.setName((String) map.get("XM"));
            cftForm.setJyzh((String) map.get("JYZH"));
            cftForm.setDfzh((String) map.get("DFZH"));
            cftForm.setDfxm((String) map.get("DFXMS"));
            cftForm.setJyzcs(new BigDecimal(map.get("JYZCS").toString()));
            cftForm.setJzzcs(new BigDecimal(map.get("JZZCS").toString()));
            cftForm.setJzzje(new BigDecimal(map.get("JZZJE").toString()));
            cftForm.setCzzcs(new BigDecimal(map.get("CZZCS").toString()));
            cftForm.setCzzje(new BigDecimal(map.get("CZZJE").toString()));
            bankTjjgss3.add(cftForm);
        }
        // 账户点对点统计信息 出账
        String seach4 = " and aj_id="+aj.getId()+" and (d.dsfzh != 1 or d.dsfzh is null)  order by c.jzzje desc nulls last";
        bankList = banktjsd.getDoPage(seach4, 1, 10);
        List<CftTjjgsForm> bankTjjgss4 = new ArrayList<>();
        for(int i=0;i<bankList.size();i++) {
            Map map = (Map) bankList.get(i);
            cftForm = new CftTjjgsForm();
            cftForm.setName((String) map.get("XM"));
            cftForm.setJyzh((String) map.get("JYZH"));
            cftForm.setDfzh((String) map.get("DFZH"));
            cftForm.setDfxm((String) map.get("DFXMS"));
            cftForm.setJyzcs(new BigDecimal(map.get("JYZCS").toString()));
            cftForm.setJzzcs(new BigDecimal(map.get("JZZCS").toString()));
            cftForm.setJzzje(new BigDecimal(map.get("JZZJE").toString()));
            cftForm.setCzzcs(new BigDecimal(map.get("CZZCS").toString()));
            cftForm.setCzzje(new BigDecimal(map.get("CZZJE").toString()));
            bankTjjgss4.add(cftForm);
        }
        // 公共账户统计信息
        String seach = "and a.num>4 and aj_id ="+aj.getId()+
                "and (d.dsfzh = 0 or d.dsfzh is null) and ( 1=1 ) order by a.num desc,c.dfzh,c.jyzh";
        bankList  = banktjsd.getDoPageGt(seach, 0, 0, aj.getId(), false);
        List<CftTjjgsForm> bankTjjgss5 = new ArrayList<>();
        for(int i=0;i<bankList.size();i++){
            Map map = (Map)bankList.get(i);
            cftForm = new CftTjjgsForm();
            cftForm.setName((String) map.get("XM"));
            cftForm.setJyzh((String) map.get("JYZH"));
            cftForm.setDfzh((String) map.get("DFZH"));
            cftForm.setDfxm((String) map.get("DFXM"));
            cftForm.setCount(new BigDecimal(map.get("NUM").toString()));
            cftForm.setJyzcs(new BigDecimal(map.get("JYZCS").toString()));
            cftForm.setJzzcs(new BigDecimal(map.get("JZZCS").toString()));
            cftForm.setJzzje(new BigDecimal(map.get("JZZJE").toString()));
            cftForm.setCzzcs(new BigDecimal(map.get("CZZCS").toString()));
            cftForm.setCzzje(new BigDecimal(map.get("CZZJE").toString()));
            bankTjjgss5.add(cftForm);
        }
        try {
            // 创建文件
            Rectangle one = new Rectangle(1050, 1500);
            document.setPageSize(one);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, op);
            // 插入水印
//            WatermarkImageUtils water = new WatermarkImageUtils();
//            pdfWriter.setPageEvent(water);
            // 打开文件
            document.open();
            // 插入表格
            pdfInsertTable(aj, document, bankTjjgs1, bankTjjgs2, bankTjjgss3, bankTjjgss4, bankTjjgss5);
            // 关闭文档
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    private void pdfInsertTable(AjEntity aj, Document document, List<CftTjjgForm> cfttjs1, List<CftTjjgForm> cfttjs2,
                                List<CftTjjgsForm> cfttjs3, List<CftTjjgsForm> cfttjs4, List<CftTjjgsForm> cfttjs5)
            throws DocumentException, IOException {
        // 中文字体,解决中文不能显示问题
//        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        BaseFont bfChinese = BaseFont.createFont("../../resources/font/SIMYOU.TTF", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        Font textFont = new Font(bfChinese, 12, Font.NORMAL); //正常
        Font tableFont = new Font(bfChinese, 10, Font.NORMAL);
        Font tableFont1 = new Font(bfChinese, 12, 1, BaseColor.RED); //正常
        Font boldFont = new Font(bfChinese, 13, Font.BOLD);
        Font blackFont = new Font(bfChinese, 16, Font.BOLD);//标题字体
        Font boldFont1 = new Font(bfChinese, 13, 1, BaseColor.RED);
        blackFont.setColor(BaseColor.BLACK);
        //  财付通账户信息出、进账
        PdfPTable table = null;
        String columnNames1[] = {"序号", "交易卡号", "交易户名", "交易总次数",
                "进账总次数", "进账总金额", "出账总次数", "出账总金额", "账户类别"};
        if (cfttjs1 != null && cfttjs1.size() != 0) {
            CreatePdfUtils.createHead(document, "账户统计信息-<进账>", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(cfttjs1, tableFont, tableFont1, boldFont, boldFont1, columnNames1, 5, 1);
            document.add(table);
        }
        if (cfttjs2 != null && cfttjs2.size() != 0) {
            CreatePdfUtils.createHead(document, "账户统计信息-<出账>", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(cfttjs2, tableFont, tableFont1, boldFont, boldFont1, columnNames1, 7, 1);
            document.add(table);
        }
        //  财付通对手账户信息出、进账
        String columnNames2[] = {"序号", "交易卡号", "交易户名", "对方卡号", "对方户名", "交易总次数",
                "进账总次数", "进账总金额", "出账总次数", "出账总金额"};
        if (cfttjs3 != null && cfttjs3.size() != 0) {
            CreatePdfUtils.createHead(document, "账户点对点统计信息-<进账>", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(cfttjs3, tableFont, tableFont1, boldFont, boldFont1, columnNames2, 7, 2);
            document.add(table);
        }
        if (cfttjs4 != null && cfttjs4.size() != 0) {
            CreatePdfUtils.createHead(document, "账户点对点统计信息-<出账>", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(cfttjs4, tableFont, tableFont1, boldFont, boldFont1, columnNames2, 9, 2);
            document.add(table);
        }
        String columnNames3[] = {"序号", "姓名", "交易卡号", "对方卡号", "对方姓名", "共同联系人数", "交易总次数",
                "进账总次数", "进账总金额", "出账总次数", "出账总金额"};
        if (cfttjs5 != null && cfttjs5.size() != 0) {
            CreatePdfUtils.createHead(document, "公共账户统计信息", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(cfttjs5, tableFont, tableFont1, boldFont, boldFont1, columnNames3, 5, 1);
            document.add(table);
        }

    }

    /**
     * zcxx表数据
     * @param listPath
     * @return
     */
    public List<BankZcxxEntity> getBankZcxxAll(List<String> listPath, List<List<String>> fields) {
        // 读取
        List<BankZcxxEntity> bankZcxxList = null;
        List<BankZcxxEntity> bankZcxxLists = new ArrayList<>();
        for(String path : listPath){
            String excelName = path.substring(path.lastIndexOf(File.separator)+1);
            for(List<String> field : fields){
                if(field.get(0).equals(excelName) && field.get(2).equals("bank_zcxx")){
                    if(path.endsWith(".xlsx")){
                        bankZcxxList = getBy2007ExcelAll(path,excelName,fields);
                    }else if(path.endsWith(".xls")){
                        bankZcxxList = getBy2003ExcelAll(path,excelName,fields);
                    }
                    bankZcxxLists.addAll(bankZcxxList);
                    break;
                }
            }
        }
        BankPersonEntity bpe = new BankPersonEntity();
        for (BankZcxxEntity bce : bankZcxxLists) {
            bpe = bpe.getByZcxx(bce);
            if (bpe.getYhkkh().trim().length() > 0) {
                bpd.insert(bpe);
            }
        }
        return bankZcxxLists;
    }

    /**
     * 03版数据读取
     * @param path
     * @param excelName
     * @param fields
     * @return
     */
    private List<BankZcxxEntity> getBy2003ExcelAll(String path, String excelName, List<List<String>> fields) {
        List<BankZcxxEntity> zcxxList = new ArrayList<>();
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
                                    BankZcxxEntity zcxx = RowToEntity(row, excel, title);
                                    if (zcxx != null) {
                                        zcxxList.add(zcxx);
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
        return zcxxList;
    }

    /**
     * 07版数据读取
     * @param path
     * @param excelName
     * @param fields
     * @return
     */
    private List<BankZcxxEntity> getBy2007ExcelAll(String path, String excelName, List<List<String>> fields) {
        // 用于存放表格中列号
        List<BankZcxxEntity> zcxxList = new ArrayList<>();
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
                                    BankZcxxEntity zcxx = RowToEntity(row, field, title);
                                    if (zcxx != null) {
                                        zcxxList.add(zcxx);
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
        return zcxxList;
    }

    /**
     * 字段映射实体
     * @param xssfRow
     * @param field
     * @param title
     * @return
     */
    private BankZcxxEntity RowToEntity(Row xssfRow, List<String> field, Map<String, Integer> title) {
        BankZcxxEntity bankZcxx = new BankZcxxEntity();
        if(field.get(3).equals("无") || xssfRow.getCell(title.get(field.get(3)))==null){
            bankZcxx.setZhzt("");
        }else{
            String zhzt = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(3)))).replace(",","");
            bankZcxx.setZhzt(zhzt);
        }
        if(field.get(4).equals("无") || xssfRow.getCell(title.get(field.get(4)))==null){
            bankZcxx.setYhkkh("");
        }else{
            String yhkkh = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(4)))).replace(",","");
            bankZcxx.setYhkkh(bankZcxx.remove_(yhkkh));
        }
        if(field.get(5).equals("无") || xssfRow.getCell(title.get(field.get(5)))==null){
            bankZcxx.setKhxm("");
        }else{
            String KHXM = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(5)))).replace(",","");
            bankZcxx.setKhxm(KHXM);
        }
        if(field.get(6).equals("无") || xssfRow.getCell(title.get(field.get(6)))==null){
            bankZcxx.setKhzjh("");
        }else{
            String KHZJH = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(6)))).replace(",","");
            bankZcxx.setKhzjh(KHZJH);
        }
        if(field.get(7).equals("无") || xssfRow.getCell(title.get(field.get(7)))==null){
            bankZcxx.setKhsj("");
        }else{
            String KHSJ = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(7)))).replace(",","");
            bankZcxx.setKhsj(KHSJ);
        }
        if(field.get(8).equals("无") || xssfRow.getCell(title.get(field.get(8)))==null){
            bankZcxx.setKhh("");
        }else{
            String KHH = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(8)))).replace(",","");
            bankZcxx.setKhh(KHH);
        }
        if(field.get(9).equals("无") || xssfRow.getCell(title.get(field.get(9)))==null){
            bankZcxx.setZhye(new BigDecimal(0));
        }else{
            String ZHYE = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(9)))).replace(",","");
            bankZcxx.setZhye(new BigDecimal("".equals(ZHYE)? "0" : ZHYE));
        }
        if(field.get(10).equals("无") || xssfRow.getCell(title.get(field.get(10)))==null){
            bankZcxx.setKyye(new BigDecimal(0));
        }else{
            String KYYE = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(10)))).replace(",","");
            bankZcxx.setKyye(new BigDecimal("".equals(KYYE)? "0" : KYYE));
        }
        if(field.get(11).equals("无") || xssfRow.getCell(title.get(field.get(11)))==null){
            bankZcxx.setYhkzh("");
        }else{
            String YHKZH = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(11)))).replace(",","");
            bankZcxx.setYhkzh(bankZcxx.remove_(YHKZH));
        }
        if(field.get(12).equals("无") || xssfRow.getCell(title.get(field.get(12)))==null){
            bankZcxx.setZhlx(1l);
        }else{
            String ZHLX = MappingUtils.rowValue(xssfRow.getCell(title.get(field.get(12)))).replace(",","");
            bankZcxx.setZhlx(Long.parseLong(ZHLX));
        }
        return bankZcxx;
    }

    public List<String> getBankFileList(String uploadPath) {
        List<String> listPath = new ArrayList<>();
        File dir = new File(uploadPath);
        File[] files = dir.listFiles();
        for (File file : files) {
            listPath.add(file.getAbsolutePath());
        }
        return listPath;
    }
}
