package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlTjjgsEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgsEntity;
import cn.com.sinofaith.dao.zfbDao.*;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlSjdzsForm;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlTjjgForm;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxGtzhForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.util.CreatePdfUtils;
import cn.com.sinofaith.util.TimeFormatUtil;
import cn.com.sinofaith.util.WatermarkImageUtils;
import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.hibernate.NullPrecedence;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.itextpdf.text.Font.FontFamily.UNDEFINED;

/**
 * 支付宝转账明细业务层
 *
 * @author zd
 * create by 2018.12.13
 */
@Service
public class ZfbZzmxTjjgService {
    @Autowired
    private ZfbZzmxTjjgDao zfbZzmxTjjgDao;
    @Autowired
    private ZfbZzmxGtzhDao zfbZzmxGtzhDao;
    @Autowired
    private ZfbJyjlTjjgDao zfbJyjlTjjgDao;
    @Autowired
    private ZfbJyjlTjjgsDao zfbJyjlTjjgsDao;
    @Autowired
    private ZfbJyjlSjdzsDao zfbJyjlSjdzsDao;

    /**
     * 分页数据
     *
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = null;
        int rowAll = zfbZzmxTjjgDao.getRowAll(dc);
        if (rowAll > 0) {
            List<ZfbZzmxTjjgEntity> zcxxList = zfbZzmxTjjgDao.getDoPage(currentPage, pageSize, dc);
            for (int i = 0; i < zcxxList.size(); i++) {
                zcxxList.get(i).setId((currentPage - 1) * pageSize + i + 1);
            }
            if (zcxxList != null) {
                page = new Page();
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(zcxxList);
                page.setPageSize(pageSize);
            }
        }
        return page;
    }

    /**
     * 统计结果详情数据
     *
     * @param currentPage
     * @param pageSize
     * @param search
     * @param aj_id
     * @return
     */
    public String getZfbZzmxTjjg(int currentPage, int pageSize, String search, long aj_id) {
        Gson gson = new Gson();
        Page page = null;
        int rowAll = zfbZzmxTjjgDao.getRowAllCount(search, aj_id);
        if (rowAll > 0) {
            List<ZfbZzmxEntity> zzmxList = zfbZzmxTjjgDao.getDoPageTjjg(currentPage, pageSize, search, aj_id, true);
            for (int i = 0; i < zzmxList.size(); i++) {
                zzmxList.get(i).setId((currentPage - 1) * pageSize + i + 1);
            }
            if (zzmxList != null) {
                page = new Page();
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(zzmxList);
                page.setPageSize(pageSize);
            }
        }
        return gson.toJson(page);
    }

    /**
     * 数据下载
     *
     * @param dc
     * @return
     */
    public List<ZfbZzmxTjjgEntity> getZfbZzmxTjjgAll(DetachedCriteria dc) {
        List<ZfbZzmxTjjgEntity> wls = null;
        int rowAll = zfbZzmxTjjgDao.getRowAll(dc);
        if (rowAll > 0) {
            wls = zfbZzmxTjjgDao.getDoPageAll(dc);
        }
        return wls;
    }

    public HSSFWorkbook createExcel(List<ZfbZzmxTjjgEntity> tjjgs) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("支付宝转账统计信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("支付宝账号");
        cell = row.createCell(2);
        cell.setCellValue("账号名称");
        cell = row.createCell(3);
        cell.setCellValue("转账产品名称");
        cell = row.createCell(4);
        cell.setCellValue("交易总次数");
        cell = row.createCell(5);
        cell.setCellValue("出账总次数");
        cell = row.createCell(6);
        cell.setCellValue("出账总金额");
        cell = row.createCell(7);
        cell.setCellValue("进账总次数");
        cell = row.createCell(8);
        cell.setCellValue("进账总金额");
        int b = 1;
        for (int i = 0; i < tjjgs.size(); i++) {
            ZfbZzmxTjjgEntity wl = tjjgs.get(i);
            if ((i + b) >= 65536 && (i + b) % 65536 == 0) {
                sheet = wb.createSheet("支付宝转账统计信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("支付宝账号");
                cell = row.createCell(2);
                cell.setCellValue("账号名称");
                cell = row.createCell(3);
                cell.setCellValue("转账产品名称");
                cell = row.createCell(4);
                cell.setCellValue("交易总次数");
                cell = row.createCell(5);
                cell.setCellValue("出账总次数");
                cell = row.createCell(6);
                cell.setCellValue("出账总金额");
                cell = row.createCell(7);
                cell.setCellValue("进账总次数");
                cell = row.createCell(8);
                cell.setCellValue("进账总金额");
                b += 1;
            }
            row = sheet.createRow((i + b) % 65536);
            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getZfbzh());
            cell = row.createCell(2);
            cell.setCellValue(wl.getZfbmc());
            cell = row.createCell(3);
            cell.setCellValue(wl.getZzcpmc());
            cell = row.createCell(4);
            cell.setCellValue(wl.getJyzcs());
            cell = row.createCell(5);
            cell.setCellValue(wl.getFkzcs());
            cell = row.createCell(6);
            cell.setCellValue(wl.getFkzje().toString());
            cell = row.createCell(7);
            cell.setCellValue(wl.getSkzcs());
            cell = row.createCell(8);
            cell.setCellValue(wl.getSkzje().toString());
            if ((i + b) % 65536 == 0) {
                for (int a = 0; a < 9; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }

    /**
     * 页面加载显示数据
     *
     * @param dc
     * @return
     */
    public List<String> onload(DetachedCriteria dc) {
        return zfbZzmxTjjgDao.onload(dc);
    }

    /**
     * 详情页数据导出
     *
     * @param search
     * @param id
     * @return
     */
    public List<ZfbZzmxEntity> getZfbZzmxDetails(String search, long id) {
        List<ZfbZzmxEntity> zzmxs = null;
        int rowAll = zfbZzmxTjjgDao.getRowAllCount(search, id);
        if (rowAll > 0) {
            // 获取详情总条数
            zzmxs = zfbZzmxTjjgDao.getDoPageTjjg(0, 0, search, id, false);
        }
        return zzmxs;
    }

    /**
     * pdf导出
     *
     * @param op
     * @param aj
     * @return
     */
    public Document createPDF(OutputStream op, AjEntity aj) {
        Document document = new Document();
        // 转账统计出账
        List<ZfbZzmxTjjgEntity> czTjjgList = selectTjjgList(ZfbZzmxTjjgEntity.class, aj, "fkzje");
        // 转账统计进账
        List<ZfbZzmxTjjgEntity> jzTjjgList = selectTjjgList(ZfbZzmxTjjgEntity.class, aj, "skzje");
        // 转账对手出账
        List<ZfbZzmxTjjgsEntity> czTjjgsList = selectTjjgList(ZfbZzmxTjjgsEntity.class, aj, "fkzje");
        // 转账对手进账
        List<ZfbZzmxTjjgsEntity> jzTjjgsList = selectTjjgList(ZfbZzmxTjjgsEntity.class, aj, "skzje");
        // 转账共同账户
        List<ZfbZzmxGtzhForm> gtzhList = selectGtzhList(aj);
        // 交易卖家账户信息
        List<ZfbJyjlTjjgForm> jyjlTjjgForms = selectJyjlList(aj);
        // 交易买家账户信息
        List<ZfbJyjlTjjgsEntity> jyjlTjjgsList = selectJyjlsList(ZfbJyjlTjjgsEntity.class, aj);
        // 交易记录地址信息
        List<ZfbJyjlSjdzsForm> sjdzsForm = zfbJyjlSjdzsDao.getDoPageSjdzsGE10(aj);
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
            pdfInsertTable(aj, document, czTjjgList, jzTjjgList, czTjjgsList,
                    jzTjjgsList, gtzhList, jyjlTjjgForms, jyjlTjjgsList, sjdzsForm);
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

    private void pdfInsertTable(AjEntity aj, Document document, List<ZfbZzmxTjjgEntity> czTjjgList, List<ZfbZzmxTjjgEntity> jzTjjgList,
                                List<ZfbZzmxTjjgsEntity> czTjjgsList, List<ZfbZzmxTjjgsEntity> jzTjjgsList, List<ZfbZzmxGtzhForm> gtzhList,
                                List<ZfbJyjlTjjgForm> jyjlTjjgForms, List<ZfbJyjlTjjgsEntity> jyjlTjjgsList, List<ZfbJyjlSjdzsForm> sjdzsForm)
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
        // 转账统计出、进账
        PdfPTable table = null;
        String columnNames1[] = {"序号", "支付宝账号", "账号名称", "转账产品名称", "交易总次数",
                "出账总次数", "出账总金额", "进账总次数", "进账总金额"};
        if (czTjjgList != null && czTjjgList.size() != 0) {
            CreatePdfUtils.createHead(document, "转账明细统计结果-<出账>", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(czTjjgList, tableFont, tableFont1, boldFont, boldFont1, columnNames1, 6);
            document.add(table);
        }
        if (jzTjjgList != null && jzTjjgList.size() != 0) {
            CreatePdfUtils.createHead(document, "转账明细统计结果-<进账>", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(jzTjjgList, tableFont, tableFont1, boldFont, boldFont1, columnNames1, 8);
            document.add(table);
        }
        // 转账对手出、进账
        String columnNames2[] = {"序号", "支付宝账号", "账号名称", "对方账号", "对方信息", "交易总次数", "出账总次数",
                "出账总金额", "进账总次数", "进账总金额"};
        if (czTjjgsList != null && czTjjgsList.size() != 0) {
            CreatePdfUtils.createHead(document, "转账明细对手账户-<出账>", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(czTjjgsList, tableFont, tableFont1, boldFont, boldFont1, columnNames2, 7);
            document.add(table);
        }
        if (jzTjjgsList != null && jzTjjgsList.size() != 0) {
            CreatePdfUtils.createHead(document, "转账明细对手账户-<进账>", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(jzTjjgsList, tableFont, tableFont1, boldFont, boldFont1, columnNames2, 9);
            document.add(table);
        }
        // 转账共同账户
        String columnNames3[] = {"序号", "支付宝账号", "账号名称", "共同账户", "共同联系人数", "交易总次数", "出账总次数",
                "出账总金额", "进账总次数", "进账总金额"};
        if (gtzhList != null && gtzhList.size() != 0) {
            CreatePdfUtils.createHead(document, "转账共同账户", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(gtzhList, tableFont, tableFont1, boldFont, boldFont1, columnNames3, 4);
            document.add(table);
        }
        // 交易卖家账户信息
        String columnNames4[] = {"序号", "店铺名", "卖家账号", "账户名称", "交易总次数", "进账总次数",
                "进账总金额", "出账总次数", "出账总金额"};
        if (jyjlTjjgForms != null && jyjlTjjgForms.size() != 0) {
            CreatePdfUtils.createHead(document, "交易卖家账户信息", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(jyjlTjjgForms, tableFont, tableFont1, boldFont, boldFont1, columnNames4, -1);
            document.add(table);
        }
        // 交易买家账户信息
        String columnNames5[] = {"序号", "买家用户Id", "买家信息", "交易状态", "卖家用户Id", "卖家信息", "购买总次数",
                "购买总金额"};
        if (jyjlTjjgsList != null && jyjlTjjgsList.size() != 0) {
            CreatePdfUtils.createHead(document, "交易买家账户信息", blackFont, textFont, aj.getAj());
            table = CreatePdfUtils.createTable(jyjlTjjgsList, tableFont, tableFont1, boldFont, boldFont1, columnNames5, 7);
            document.add(table);
        }
        // 交易记录地址信息
        String columnNames6[] = {"序号", "买家用户Id", "买家信息", "收货人地址", "收件次数", "出账金额"};
        if (sjdzsForm != null && sjdzsForm.size() != 0) {
            CreatePdfUtils.createHead(document, "交易记录地址信息", blackFont, textFont, aj.getAj());
                table = CreatePdfUtils.createTable(sjdzsForm, tableFont, tableFont1, boldFont, boldFont1, columnNames6, 4);
            document.add(table);
        }
    }

    private List selectTjjgList(Class clazz, AjEntity aj, String order) {
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(clazz);
        dc.add(Restrictions.eq("aj_id", aj.getId()));
        dc.addOrder(Order.desc(order).nulls(NullPrecedence.LAST));
        return zfbZzmxTjjgDao.getDoPage(1, 10, dc);
    }

    private List<ZfbZzmxGtzhForm> selectGtzhList(AjEntity aj) {
        String search = " order by dfzh desc";
        List<ZfbZzmxGtzhForm> gtzhList = zfbZzmxGtzhDao.queryForPage(0, 0, search, aj.getId(), false);
        return gtzhList;
    }

    private List<ZfbJyjlTjjgForm> selectJyjlList(AjEntity aj) {
        String search = " order by skzje desc nulls last";
        List<ZfbJyjlTjjgForm> TjjgForm = zfbJyjlTjjgDao.getPage(0, 0, search, aj.getId(), false);
        return TjjgForm;
    }

    private List<ZfbJyjlTjjgsEntity> selectJyjlsList(Class clazz, AjEntity aj) {
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(clazz);
        dc.add(Restrictions.eq("aj_id", aj.getId()));
        dc.addOrder(Order.desc("fkzje").nulls(NullPrecedence.LAST));
        List<ZfbJyjlTjjgsEntity> jyjlTjjgs = zfbJyjlTjjgsDao.getDoPage(1, 10, dc);
        return jyjlTjjgs;
    }
}
