package cn.com.sinofaith.service.cftServices;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.cftBean.CftZcxxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlTjjgsEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgsEntity;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.cftDao.CftTjjgDao;
import cn.com.sinofaith.dao.cftDao.CftTjjgsDao;
import cn.com.sinofaith.dao.cftDao.CftZcxxDao;
import cn.com.sinofaith.form.cftForm.CftTjjgForm;
import cn.com.sinofaith.form.cftForm.CftTjjgsForm;
import cn.com.sinofaith.form.cftForm.CftTjjgssForm;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlSjdzsForm;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlTjjgForm;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxGtzhForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.util.CreatePdfUtils;
import cn.com.sinofaith.util.WatermarkImageUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
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

/**
 * Created by Me. on 2018/5/22
 * 财付通注册信息服务层
 */
@Service
public class CftZcxxService {

    @Autowired
    private CftZcxxDao czd;
    @Autowired
    private CftZzxxService cfts;
    @Autowired
    private CftTjjgDao cfttjd;
    @Autowired
    private CftTjjgsDao cfttjsd;

    public String getBq(String yhkh,long ajid){
        String sql = " select to_char(c.zzsum) zzsum ,to_char(j.tjsum) tjsum" +
                " ,to_char(s.tssum) tssum,to_char(g.gtsum) gtsum from cft_zcxx z left join (" +
                " select zh,count(1) as zzsum from cft_zzxx where aj_id ="+ajid+" group by zh" +
                " ) c on z.zh = c.zh" +
                " left join ( select jyzh,count(1) as tjsum from cft_tjjg  where aj_id="+ajid+" group by jyzh" +
                ") j on z.zh = j.jyzh " +
                " left join ( select jyzh,count(1) as tssum from cft_tjjgs where aj_id="+ajid+" group by jyzh" +
                ") s on z.zh = s.jyzh" +
                " left join (select c.jyzh,count(c.jyzh) as gtsum from cft_tjjgs c " +
                "right join ( select t.dfzh,count(1) as num from cft_tjjgs t " +
                " where t.dfzh not in( select distinct t1.jyzh from cft_tjjgs t1) " +
                "and t.aj_id="+ajid+" group by dfzh having(count(1)>=2) ) a on c.dfzh = a.dfzh    " +
                " where a.num is not null  and aj_id ="+ajid+" and ( 1=1 ) " +
                "group by c.jyzh) g on z.zh = g.jyzh" +
                " where z.aj_id ="+ajid+" and z.zh = '"+yhkh+"'";
        List list = czd.findBySQL(sql);
        Gson gson = new GsonBuilder().serializeNulls().create();
        Map<String,String> mapResult = new HashMap<>();
        for(int i=0; i<list.size();i++){
            Map map = (Map)list.get(i);
            mapResult.put("zzsum",(String)map.get("ZZSUM"));
//            mapResult.put("zzrsum",(String)map.get("ZZRSUM"));
            mapResult.put("tjsum",(String)map.get("TJSUM"));
            mapResult.put("tssum",(String)map.get("TSSUM"));
            mapResult.put("gtsum",(String)map.get("GTSUM"));
        }

        return gson.toJson(mapResult);
    }


    public Page queryForPage(int currentPage,int pageSize,String seach){
        Page page = new Page();
        //总记录数
        int allRow = czd.getAllRowCount(seach);
        List<CftZcxxEntity> cftzcxxs = new ArrayList<CftZcxxEntity>();
        if(allRow != 0) {
            int xh = 1;
            cftzcxxs = czd.getDoPage(seach,currentPage,pageSize);
            for(int i=0; i<cftzcxxs.size();i++){
                cftzcxxs.get(i).setId(xh+(currentPage-1)*pageSize);
                xh++;
            }
        }
        page.setList(cftzcxxs);
        page.setPageNo(currentPage);
        page.setPageSize(pageSize);
        page.setTotalRecords(allRow);
        return page;
    }

    public void downloadFile(String seach, HttpServletResponse rep,String aj) throws Exception{
        List<CftZcxxEntity> listZcxx = czd.find("from CftZcxxEntity where 1=1"+seach+" order by id desc ");
        HSSFWorkbook wb = createExcel(listZcxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-Disposition","attachment;filename="+new String(("财付通注册信息(\""+aj+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public HSSFWorkbook createExcel(List<CftZcxxEntity> listZcxx)throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("财付通注册信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("账户状态");
        cell = row.createCell(2);
        cell.setCellValue("微信账户");
        cell = row.createCell(3);
        cell.setCellValue("姓名");
        cell = row.createCell(4);
        cell.setCellValue("注册时间");
        cell = row.createCell(5);
        cell.setCellValue("身份证号");
        cell = row.createCell(6);
        cell.setCellValue("绑定手机");
        cell = row.createCell(7);
        cell.setCellValue("开户行");
        cell = row.createCell(8);
        cell.setCellValue("银行账号");
        int i = 1;
        int b = 1;
        for(CftZcxxEntity czxx:listZcxx){
            if(i>=65536 && i%65536==0){
                sheet = wb.createSheet("财付通注册信息("+b+")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("账户状态");
                cell = row.createCell(2);
                cell.setCellValue("微信账户");
                cell = row.createCell(3);
                cell.setCellValue("姓名");
                cell = row.createCell(4);
                cell.setCellValue("注册时间");
                cell = row.createCell(5);
                cell.setCellValue("身份证号");
                cell = row.createCell(6);
                cell.setCellValue("绑定手机");
                cell = row.createCell(7);
                cell.setCellValue("开户行");
                cell = row.createCell(8);
                cell.setCellValue("银行账号");
                b+=1;
            }
            row = sheet.createRow(i%65536);
            cell = row.createCell(0);
            cell.setCellValue(i);
            cell = row.createCell(1);
            cell.setCellValue(czxx.getZhzt());
            cell = row.createCell(2);
            cell.setCellValue(czxx.getZh());
            cell = row.createCell(3);
            cell.setCellValue(czxx.getXm());
            cell = row.createCell(4);
            cell.setCellValue(czxx.getZcsj());
            cell = row.createCell(5);
            cell.setCellValue(czxx.getSfzhm());
            cell = row.createCell(6);
            cell.setCellValue(czxx.getBdsj());
            cell = row.createCell(7);
            cell.setCellValue(czxx.getKhh());
            cell = row.createCell(8);
            cell.setCellValue(czxx.getYhzh());
            if(i%65536==0) {
                for (int a = 0; a < 9; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
            i++;
        }
        return wb;
    }

    public String getSeach(String seachCode, String seachCondition, AjEntity aj){
        StringBuffer seach = new StringBuffer();
        String ajid = cfts.getAjidByAjm(aj);

        if(seachCode!=null){
            seachCode =seachCode.replace("\r\n","").replace("，","").
                    replace(" ","").replace(" ","").replace("\t","");
            seach = seach.append(" and aj_id in ("+ajid.toString()+") and "+ seachCondition+" like "+"'"+ seachCode +"'");
        }else{
            seach = seach.append(" and aj_id in ("+ajid.toString()+") and ( 1=1 ) ");
        }

        return seach.toString();
    }

    public void deleteByAj_id(long ajid){
        czd.deleteByAj(ajid);
    }

    /**
     * 财付通pdf文件生成
     * @param op
     * @param aj
     * @return
     */
    public Document createPDF(OutputStream op, AjEntity aj) {
        Document document = new Document();
        // 财付通账户信息 进账
        String seach1 = " and aj_id="+aj.getId()+" order by c.jzzje desc nulls last";
        List cftList = cfttjd.getDoPage(seach1, 1, 10);
        List<CftTjjgForm> cfttjs1 = new ArrayList<>();
        CftTjjgForm tjjgForm = new CftTjjgForm();
        for(int i=0;i<cftList.size();i++){
            Map map = (Map)cftList.get(i);
            tjjgForm = tjjgForm.mapToForm(map);
            cfttjs1.add(tjjgForm);
        }
        // 财付通账户信息 出账
        String seach2 = " and aj_id="+aj.getId()+" order by c.czzje desc nulls last";
        cftList = cfttjd.getDoPage(seach2, 1, 10);
        List<CftTjjgForm> cfttjs2 = new ArrayList<>();
        for(int i=0;i<cftList.size();i++){
            Map map = (Map)cftList.get(i);
            tjjgForm = tjjgForm.mapToForm(map);
            tjjgForm.setId(i + 1);
            cfttjs2.add(tjjgForm);
        }
        // 财付通对手账户信息 进账
        cftList = cfttjsd.getDoPage(seach1, 1, 10);
        List<CftTjjgsForm> cfttjs3 = new ArrayList<>();
        CftTjjgsForm cftForm = new CftTjjgsForm();
        for(int i=0;i<cftList.size();i++) {
            Map map = (Map) cftList.get(i);
            cftForm = new CftTjjgsForm();
            cftForm.setName((String) map.get("XM"));
            cftForm.setJyzh((String) map.get("JYZH"));
            cftForm.setDfzh((String) map.get("DFZH"));
            cftForm.setJyzcs( new BigDecimal(map.get("JYZCS").toString()));
            cftForm.setJzzcs( new BigDecimal(map.get("JZZCS").toString()));
            cftForm.setJzzje( new BigDecimal(map.get("JZZJE").toString()));
            cftForm.setCzzcs( new BigDecimal(map.get("CZZCS").toString()));
            cftForm.setCzzje( new BigDecimal(map.get("CZZJE").toString()));
            cfttjs3.add(cftForm);
        }
        // 财付通对手账户信息 出账
        cftList = cfttjsd.getDoPage(seach2, 1, 10);
        List<CftTjjgsForm> cfttjs4 = new ArrayList<>();
        for(int i=0;i<cftList.size();i++) {
            Map map = (Map) cftList.get(i);
            cftForm = new CftTjjgsForm();
            cftForm.setName((String) map.get("XM"));
            cftForm.setJyzh((String) map.get("JYZH"));
            cftForm.setDfzh((String) map.get("DFZH"));
            cftForm.setJyzcs( new BigDecimal(map.get("JYZCS").toString()));
            cftForm.setJzzcs( new BigDecimal(map.get("JZZCS").toString()));
            cftForm.setJzzje( new BigDecimal(map.get("JZZJE").toString()));
            cftForm.setCzzcs( new BigDecimal(map.get("CZZCS").toString()));
            cftForm.setCzzje( new BigDecimal(map.get("CZZJE").toString()));
            cfttjs4.add(cftForm);
        }
        // 财付通共同账户信息
        String seach = "and a.num>9 and aj_id ="+aj.getId()+" and ( 1=1 ) order by a.num desc,c.dfzh,c.jyzh";
        cftList  = cfttjsd.getDoPageGt(seach, 0, 0, aj.getId(), false);
        List<CftTjjgssForm> cfttjs5 = new ArrayList<>();
        for(int i=0;i<cftList.size();i++){
            Map map = (Map)cftList.get(i);
            CftTjjgssForm cftssForm = new CftTjjgssForm();
            cftssForm.setName((String) map.get("XM"));
            cftssForm.setJyzh((String) map.get("JYZH"));
            cftssForm.setDfzh((String) map.get("DFZH"));
            cftssForm.setDfxm((String) map.get("DFXM"));
            cftssForm.setCount(new BigDecimal(map.get("NUM").toString()));
            cftssForm.setJyzcs( new BigDecimal(map.get("JYZCS").toString()));
            cftssForm.setJzzcs( new BigDecimal(map.get("JZZCS").toString()));
            cftssForm.setJzzje( new BigDecimal(map.get("JZZJE").toString()));
            cftssForm.setCzzcs( new BigDecimal(map.get("CZZCS").toString()));
            cftssForm.setCzzje( new BigDecimal(map.get("CZZJE").toString()));
            cfttjs5.add(cftssForm);
        }
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
            pdfInsertTable(aj, document, cfttjs1, cfttjs2, cfttjs3, cfttjs4,cfttjs5);
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