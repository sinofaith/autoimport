package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZhmxEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbJyjlDao;
import cn.com.sinofaith.page.Page;
import com.google.gson.Gson;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * 支付宝交易记录业务层
 * @author zd
 * create by 2018.11.28
 */
@Service
public class ZfbJyjlService {
    @Autowired
    private ZfbJyjlDao zfbJyjlDao;

    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = new Page();
        List<ZfbJyjlEntity> jyjlForms = null;
        int rowAll = zfbJyjlDao.getRowAll(dc);
        if(rowAll>0){
            jyjlForms = zfbJyjlDao.getDoPage(currentPage,pageSize,dc);
            if(jyjlForms!=null){
                page.setPageSize(pageSize);
                page.setList(jyjlForms);
                page.setTotalRecords(rowAll);
                page.setPageNo(currentPage);
            }
        }
        return page;
    }

    /**
     * 支付宝交易记录数据导出
     * @param dc
     * @return
     */
    public List<ZfbJyjlEntity> getZfbJyjlAll(DetachedCriteria dc) {
        List<ZfbJyjlEntity> zhmxs = null;
        int rowAll = zfbJyjlDao.getRowAll(dc);
        if(rowAll>0){
            zhmxs = zfbJyjlDao.getDoPageAll(dc);
        }
        return zhmxs;
    }

    /**
     * 导出excel表
     * @param jyjls
     * @return
     */
    public HSSFWorkbook createExcel(List<ZfbJyjlEntity> jyjls) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("支付宝交易记录");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("交易号");
        cell = row.createCell(2);
        cell.setCellValue("交易状态");
        cell = row.createCell(3);
        cell.setCellValue("收款时间");
        cell = row.createCell(4);
        cell.setCellValue("买家用户Id");
        cell = row.createCell(5);
        cell.setCellValue("买家信息");
        cell = row.createCell(6);
        cell.setCellValue("卖家用户Id");
        cell = row.createCell(7);
        cell.setCellValue("卖家信息");
        cell = row.createCell(8);
        cell.setCellValue("交易金额");
        cell = row.createCell(9);
        cell.setCellValue("商品名称");
        cell = row.createCell(10);
        cell.setCellValue("收货人地址");
        int b = 1;
        for(int i=0;i<jyjls.size();i++) {
            ZfbJyjlEntity wl = jyjls.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                sheet = wb.createSheet("支付宝交易记录(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("交易号");
                cell = row.createCell(2);
                cell.setCellValue("交易状态");
                cell = row.createCell(3);
                cell.setCellValue("收款时间");
                cell = row.createCell(4);
                cell.setCellValue("买家用户Id");
                cell = row.createCell(5);
                cell.setCellValue("买家信息");
                cell = row.createCell(6);
                cell.setCellValue("卖家用户Id");
                cell = row.createCell(7);
                cell.setCellValue("卖家信息");
                cell = row.createCell(8);
                cell.setCellValue("交易金额");
                cell = row.createCell(9);
                cell.setCellValue("商品名称");
                cell = row.createCell(10);
                cell.setCellValue("收货人地址");
                b += 1;
            }
            row = sheet.createRow((i+b) % 65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getJyh());
            cell = row.createCell(2);
            cell.setCellValue(wl.getJyzt());
            cell = row.createCell(3);
            cell.setCellValue(wl.getSksj());
            cell = row.createCell(4);
            cell.setCellValue(wl.getMjyhId());
            cell = row.createCell(5);
            cell.setCellValue(wl.getMjxx());
            cell = row.createCell(6);
            cell.setCellValue(wl.getMijyhId());
            cell = row.createCell(7);
            cell.setCellValue(wl.getMijxx());
            cell = row.createCell(8);
            cell.setCellValue(wl.getJyje());
            cell = row.createCell(9);
            cell.setCellValue(wl.getSpmc());
            cell = row.createCell(10);
            cell.setCellValue(wl.getShrdz());
            if((i+b)%65536==0) {
                for (int a = 0; a < 11; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }
}
