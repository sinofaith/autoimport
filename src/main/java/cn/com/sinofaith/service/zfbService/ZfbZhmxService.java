package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbZcxxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZhmxEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbZhmxDao;
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
 * 支付宝账户明细业务层
 * @author zd
 * create by 2018.11.28
 */
@Service
public class ZfbZhmxService {
    @Autowired
    private ZfbZhmxDao zfbZhmxDao;
    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = null;
        int rowAll = zfbZhmxDao.getRowAll(dc);
        if(rowAll>0){
            List<ZfbZhmxEntity> zcxxList = zfbZhmxDao.getDoPage(currentPage, pageSize, dc);
            if(zcxxList!=null){
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
     * 支付宝账户明细数据导出
     * @param dc
     * @return
     */
    public List<ZfbZhmxEntity> getZfbZhmxAll(DetachedCriteria dc) {
        List<ZfbZhmxEntity> zhmxs = null;
        int rowAll = zfbZhmxDao.getRowAll(dc);
        if(rowAll>0){
            zhmxs = zfbZhmxDao.getDoPageAll(dc);
        }
        return zhmxs;
    }

    /**
     * 导出excel表
     * @param zhmxs
     * @return
     */
    public HSSFWorkbook createExcel(List<ZfbZhmxEntity> zhmxs) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("支付宝账户明细");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("交易号");
        cell = row.createCell(2);
        cell.setCellValue("商户订单号");
        cell = row.createCell(3);
        cell.setCellValue("付款时间");
        cell = row.createCell(4);
        cell.setCellValue("交易来源地");
        cell = row.createCell(5);
        cell.setCellValue("类型");
        cell = row.createCell(6);
        cell.setCellValue("用户信息");
        cell = row.createCell(7);
        cell.setCellValue("交易对方信息");
        cell = row.createCell(8);
        cell.setCellValue("消费名称");
        cell = row.createCell(9);
        cell.setCellValue("金额");
        cell = row.createCell(10);
        cell.setCellValue("收/支");
        cell = row.createCell(11);
        cell.setCellValue("交易状态");
        cell = row.createCell(12);
        cell.setCellValue("备注");
        int b = 1;
        for(int i=0;i<zhmxs.size();i++) {
            ZfbZhmxEntity wl = zhmxs.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                sheet = wb.createSheet("支付宝账户明细(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("交易号");
                cell = row.createCell(2);
                cell.setCellValue("商户订单号");
                cell = row.createCell(3);
                cell.setCellValue("付款时间");
                cell = row.createCell(4);
                cell.setCellValue("交易来源地");
                cell = row.createCell(5);
                cell.setCellValue("类型");
                cell = row.createCell(6);
                cell.setCellValue("用户信息");
                cell = row.createCell(7);
                cell.setCellValue("交易对方信息");
                cell = row.createCell(8);
                cell.setCellValue("消费名称");
                cell = row.createCell(9);
                cell.setCellValue("金额");
                cell = row.createCell(10);
                cell.setCellValue("收/支");
                cell = row.createCell(11);
                cell.setCellValue("交易状态");
                cell = row.createCell(12);
                cell.setCellValue("备注");
                b += 1;
            }
            row = sheet.createRow((i+b)%65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getJyh());
            cell = row.createCell(2);
            cell.setCellValue(wl.getShddh());
            cell = row.createCell(3);
            cell.setCellValue(wl.getFksj());
            cell = row.createCell(4);
            cell.setCellValue(wl.getJylyd());
            cell = row.createCell(5);
            cell.setCellValue(wl.getLx());
            cell = row.createCell(6);
            cell.setCellValue(wl.getYhxx());
            cell = row.createCell(7);
            cell.setCellValue(wl.getJydfxx());
            cell = row.createCell(8);
            cell.setCellValue(wl.getXfmc());
            cell = row.createCell(9);
            cell.setCellValue(wl.getJe());
            cell = row.createCell(10);
            cell.setCellValue(wl.getSz());
            cell = row.createCell(11);
            cell.setCellValue(wl.getJyzt());
            cell = row.createCell(12);
            cell.setCellValue(wl.getBz());
            if((i+b)%65536==0) {
                for (int a = 0; a < 13; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }
}
