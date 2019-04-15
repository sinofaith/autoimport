package cn.com.sinofaith.service.bankServices;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.BankZcxxEntity;
import cn.com.sinofaith.dao.bankDao.BankTjjgDao;
import cn.com.sinofaith.dao.bankDao.BankTjjgsDao;
import cn.com.sinofaith.dao.bankDao.BankZcxxDao;
import cn.com.sinofaith.form.cftForm.CftTjjgForm;
import cn.com.sinofaith.form.cftForm.CftTjjgsForm;
import cn.com.sinofaith.form.cftForm.CftTjjgssForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.cftServices.CftZzxxService;
import cn.com.sinofaith.util.CreatePdfUtils;
import cn.com.sinofaith.util.WatermarkImageUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
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

    public String getSeach(String seachCode, String seachCondition, AjEntity aj){
        StringBuffer seach = new StringBuffer();
        String ajid = cfts.getAjidByAjm(aj);

        if(seachCode!=null){
            seachCode =seachCode.trim().replace("\r\n","").replace("，","").replace("\t","");
            seach = seach.append(" and aj_id in ("+ajid.toString()+") and "+ seachCondition+" like "+"'%"+ seachCode +"%'");
        }else{
            seach = seach.append(" and aj_id in ("+ajid.toString()+") ");
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
            cell.setCellValue(zcxx.getZhye()==null? "":zcxx.getZhye().toString());
            cell = row.createCell(7);
            cell.setCellValue(zcxx.getKyye()==null?"":zcxx.getKyye().toString());
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
        String seach1 = " and aj_id="+aj.getId()+" order by c.jzzje desc nulls last";
        List bankList = tjjd.getDoPage(seach1, 1, 10);
        List<CftTjjgForm> bankTjjgs1 = new ArrayList<>();
        CftTjjgForm tjjgForm = new CftTjjgForm();
        for(int i=0;i<bankList.size();i++){
            Map map = (Map)bankList.get(i);
            tjjgForm = tjjgForm.mapToForm(map);
            bankTjjgs1.add(tjjgForm);
        }
        // 账户统计信息 出账
        String seach2 = " and aj_id="+aj.getId()+" order by c.czzje desc nulls last";
        bankList = tjjd.getDoPage(seach2, 1, 10);
        List<CftTjjgForm> bankTjjgs2 = new ArrayList<>();
        for(int i=0;i<bankList.size();i++){
            Map map = (Map)bankList.get(i);
            tjjgForm = tjjgForm.mapToForm(map);
            tjjgForm.setId(i + 1);
            bankTjjgs2.add(tjjgForm);
        }
        // 账户点对点统计信息 进账
        bankList = banktjsd.getDoPage(seach1, 1, 10);
        List<CftTjjgsForm> bankTjjgss3 = new ArrayList<>();
        CftTjjgsForm cftForm = new CftTjjgsForm();
        for(int i=0;i<bankList.size();i++) {
            Map map = (Map) bankList.get(i);
            cftForm = new CftTjjgsForm();
            cftForm.setName((String) map.get("XM"));
            cftForm.setJyzh((String) map.get("JYZH"));
            cftForm.setDfzh((String) map.get("DFZH"));
            cftForm.setJyzcs( new BigDecimal(map.get("JYZCS").toString()));
            cftForm.setJzzcs( new BigDecimal(map.get("JZZCS").toString()));
            cftForm.setJzzje( new BigDecimal(map.get("JZZJE").toString()));
            cftForm.setCzzcs( new BigDecimal(map.get("CZZCS").toString()));
            cftForm.setCzzje( new BigDecimal(map.get("CZZJE").toString()));
            bankTjjgss3.add(cftForm);
        }
        // 账户点对点统计信息 出账
        bankList = banktjsd.getDoPage(seach2, 1, 10);
        List<CftTjjgsForm> bankTjjgss4 = new ArrayList<>();
        for(int i=0;i<bankList.size();i++) {
            Map map = (Map) bankList.get(i);
            cftForm = new CftTjjgsForm();
            cftForm.setName((String) map.get("XM"));
            cftForm.setJyzh((String) map.get("JYZH"));
            cftForm.setDfzh((String) map.get("DFZH"));
            cftForm.setJyzcs( new BigDecimal(map.get("JYZCS").toString()));
            cftForm.setJzzcs( new BigDecimal(map.get("JZZCS").toString()));
            cftForm.setJzzje( new BigDecimal(map.get("JZZJE").toString()));
            cftForm.setCzzcs( new BigDecimal(map.get("CZZCS").toString()));
            cftForm.setCzzje( new BigDecimal(map.get("CZZJE").toString()));
            bankTjjgss4.add(cftForm);
        }
        // 公共账户统计信息
        String seach = "and a.num>3 and aj_id ="+aj.getId()+" and ( 1=1 ) order by a.num desc,c.dfzh,c.jyzh";
        try {
            // 创建文件
            Rectangle one = new Rectangle(1050, 1500);
            document.setPageSize(one);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, op);
            // 插入水印
            WatermarkImageUtils water = new WatermarkImageUtils();
            pdfWriter.setPageEvent(water);
            // 打开文件
            document.open();
            // 插入表格
            pdfInsertTable(aj, document, bankTjjgs1, bankTjjgs2, bankTjjgss3, bankTjjgss4,null);
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
                                List<CftTjjgsForm> cfttjs3, List<CftTjjgsForm> cfttjs4, List<CftTjjgssForm> cfttjs5)
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
        String columnNames1[] = {"序号", "姓名", "微信账户", "交易类型", "交易总次数",
                "进账总次数", "进账总金额", "出账总次数", "出账总金额"};
        if (cfttjs1 != null && cfttjs1.size() != 0) {
            CreatePdfUtils.createHead(document, "财付通账户信息-<进账>", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(cfttjs1, tableFont, tableFont1, boldFont, boldFont1, columnNames1, 6);
            document.add(table);
        }
        if (cfttjs2 != null && cfttjs2.size() != 0) {
            CreatePdfUtils.createHead(document, "财付通账户信息-<出账>", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(cfttjs2, tableFont, tableFont1, boldFont, boldFont1, columnNames1, 8);
            document.add(table);
        }
        //  财付通对手账户信息出、进账
        columnNames1[3] = "对方账户";
        if (cfttjs3 != null && cfttjs3.size() != 0) {
            CreatePdfUtils.createHead(document, "财付通对手账户信息-<进账>", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(cfttjs3, tableFont, tableFont1, boldFont, boldFont1, columnNames1, 6);
            document.add(table);
        }
        if (cfttjs4 != null && cfttjs4.size() != 0) {
            CreatePdfUtils.createHead(document, "财付通对手账户信息-<出账>", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(cfttjs4, tableFont, tableFont1, boldFont, boldFont1, columnNames1, 8);
            document.add(table);
        }
        String columnNames2[] = {"序号", "姓名", "微信账户", "对方账户", "对方姓名", "共同联系人数", "交易总次数",
                "进账总次数", "进账总金额", "出账总次数", "出账总金额"};
        if (cfttjs5 != null && cfttjs5.size() != 0) {
            CreatePdfUtils.createHead(document, "财付通共同账户信息", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(cfttjs5, tableFont, tableFont1, boldFont, boldFont1, columnNames2, 5);
            document.add(table);
        }

    }
}
