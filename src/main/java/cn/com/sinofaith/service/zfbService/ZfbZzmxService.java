package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbZzmxDao;
import cn.com.sinofaith.page.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * 支付宝转账明细业务层
 * @author zd
 * create by 2018.11.28
 */
@Service
public class ZfbZzmxService {
    @Autowired
    private ZfbZzmxDao zfbZzmxDao;
    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = new Page();
        List<ZfbZzmxEntity> zzmxForms = null;
        int rowAll = zfbZzmxDao.getRowAll(dc);
        if(rowAll>0){
            zzmxForms = zfbZzmxDao.getDoPage(currentPage,pageSize,dc);
            if(zzmxForms!=null){
                page.setPageSize(pageSize);
                page.setList(zzmxForms);
                page.setTotalRecords(rowAll);
                page.setPageNo(currentPage);
            }
        }
        return page;
    }

    /**
     * 支付宝转账明细数据导出
     * @param dc
     * @return
     */
    public List<ZfbZzmxEntity> getZfbZzmxAll(DetachedCriteria dc) {
        List<ZfbZzmxEntity> zzmxs = null;
        int rowAll = zfbZzmxDao.getRowAll(dc);
        if(rowAll>0){
            zzmxs = zfbZzmxDao.getDoPageAll(dc);
        }
        return zzmxs;
    }

    /**
     * 导出excel表
     * @param zzmxs
     * @return
     */
    public HSSFWorkbook createExcel(List<ZfbZzmxEntity> zzmxs) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("支付宝转账明细");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("交易号");
        cell = row.createCell(2);
        cell.setCellValue("付款方账号");
        cell = row.createCell(3);
        cell.setCellValue("收款方账号");
        cell = row.createCell(4);
        cell.setCellValue("收款机构信息");
        cell = row.createCell(5);
        cell.setCellValue("到账时间");
        cell = row.createCell(6);
        cell.setCellValue("转账金额");
        cell = row.createCell(7);
        cell.setCellValue("转账产品名称");
        cell = row.createCell(8);
        cell.setCellValue("交易发生地");
        cell = row.createCell(9);
        cell.setCellValue("提现流水号");
        int b = 1;
        for(int i=0;i<zzmxs.size();i++) {
            ZfbZzmxEntity wl = zzmxs.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                sheet = wb.createSheet("支付宝转账明细(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("交易号");
                cell = row.createCell(2);
                cell.setCellValue("付款方账号");
                cell = row.createCell(3);
                cell.setCellValue("收款方账号");
                cell = row.createCell(4);
                cell.setCellValue("收款机构信息");
                cell = row.createCell(5);
                cell.setCellValue("到账时间");
                cell = row.createCell(6);
                cell.setCellValue("转账金额");
                cell = row.createCell(7);
                cell.setCellValue("转账产品名称");
                cell = row.createCell(8);
                cell.setCellValue("交易发生地");
                cell = row.createCell(9);
                cell.setCellValue("提现流水号");
                b += 1;
            }
            row = sheet.createRow((i+b)%65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getJyh());
            cell = row.createCell(2);
            cell.setCellValue(wl.getFkfzfbzh());
            cell = row.createCell(3);
            cell.setCellValue(wl.getSkfzfbzh());
            cell = row.createCell(4);
            cell.setCellValue(wl.getSkjgxx());
            cell = row.createCell(5);
            cell.setCellValue(wl.getDzsj());
            cell = row.createCell(6);
            cell.setCellValue(wl.getZzje());
            cell = row.createCell(7);
            cell.setCellValue(wl.getZzcpmc());
            cell = row.createCell(8);
            cell.setCellValue(wl.getJyfsd());
            cell = row.createCell(9);
            cell.setCellValue(wl.getTxlsh());
            if((i+b)%65536==0) {
                for (int a = 0; a < 10; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }
}
