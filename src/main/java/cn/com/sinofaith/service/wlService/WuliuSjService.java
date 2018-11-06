package cn.com.sinofaith.service.wlService;

import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.bean.wlBean.WuliuShipEntity;
import cn.com.sinofaith.bean.wlBean.WuliuSjEntity;
import cn.com.sinofaith.dao.wuliuDao.WuliuRelationDao;
import cn.com.sinofaith.dao.wuliuDao.WuliuSjDao;
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


@Service
public class WuliuSjService {

    @Autowired
    private WuliuSjDao wlsjDao;
    @Autowired
    private WuliuRelationDao wlrDao;

    /**
     * 收件人表添加数据
     * @param id
     */
    public void insertSj(long id) {
        int rowAll = wlsjDao.getRowAll(id);
        if(rowAll>0){
            wlsjDao.deleteWuliuSj(id);
        }
        // 获取数据
        List<WuliuSjEntity> wls = wlsjDao.getWuliuSj(id);
        // 插入数据
        wlsjDao.insertWuliuSj(wls);
    }

    /**
     * 分页数据获取
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page getDoPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = new Page();
        // 获取总条数
        int rowCount = wlsjDao.getRowCount(dc);
        if(rowCount>0){
            List<WuliuSjEntity> wls = wlsjDao.getDoPage(currentPage,pageSize,dc);
            for (int i = 0; i < wls.size(); i++) {
                wls.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            if(wls!=null){
                page.setPageNo(currentPage);
                page.setList(wls);
                page.setTotalRecords(rowCount);
                page.setPageSize(pageSize);
            }
        }
        return page;
    }

    /**
     * 收件人详情页面
     * @param currentPage
     * @param pageSize
     * @param dc
     * @param seach
     * @return
     */
    public String getWuliuRelation(int currentPage, int pageSize, DetachedCriteria dc, String seach) {
        Gson gson = new Gson();
        Page page = new Page();
        List<WuliuEntity> wls = null;

        int rowAll = wlsjDao.getCountRow(seach);
        if (rowAll>0) {
            wls = wlrDao.getPage(currentPage, pageSize, dc);

            if(wls!=null){
                for (int i = 0; i < wls.size(); i++) {
                    wls.get(i).setId((currentPage-1)*pageSize+i+1);
                }
                page.setPageSize(pageSize);
                page.setTotalRecords(rowAll);
                page.setList(wls);
                page.setPageNo(currentPage);
            }
        }
        return gson.toJson(page);
    }

    /**
     * 数据导出
     * @param dc
     * @return
     */
    public List<WuliuSjEntity> getWuliuAll(DetachedCriteria dc) {
        List<WuliuSjEntity> wls = null;
        int rowAll = wlsjDao.getRowCount(dc);
        if(rowAll>0){
            wls = wlsjDao.getWuliuSjAll(dc);
        }
        return wls;
    }

    /**
     * 数据导出
     * @param wls
     * @return
     */
    public HSSFWorkbook createExcel(List<WuliuSjEntity> wls) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("物流收件人信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("收件人");
        cell = row.createCell(2);
        cell.setCellValue("收件电话");
        cell = row.createCell(3);
        cell.setCellValue("收件地址");
        cell = row.createCell(4);
        cell.setCellValue("收件次数");
        int i = 1;
        int b = 1;
        for(WuliuSjEntity wl:wls) {
            if (i >= 65536 && i % 65536 == 0) {
                sheet = wb.createSheet("物流收件人信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("收件人");
                cell = row.createCell(2);
                cell.setCellValue("收件电话");
                cell = row.createCell(3);
                cell.setCellValue("收件地址");
                cell = row.createCell(4);
                cell.setCellValue("收件次数");
                b += 1;
            }
            row = sheet.createRow(i%65536);
            cell = row.createCell(0);
            cell.setCellValue(i);
            cell = row.createCell(1);
            cell.setCellValue(wl.getAddressee());
            cell = row.createCell(2);
            cell.setCellValue(wl.getSj_phone());
            cell = row.createCell(3);
            cell.setCellValue(wl.getSj_address());
            cell = row.createCell(4);
            cell.setCellValue(wl.getNum());
            if(i%65536==0) {
                for (int a = 0; a < 5; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
            i++;
        }
        return wb;
    }

    /**
     * 详情数据导出
     * @param dc
     * @return
     */
    public List<WuliuEntity> WuliuAll(DetachedCriteria dc,String seach) {
        int rowAll = wlrDao.getRowAll(seach);
        if(rowAll>0){
            List<WuliuEntity> pageAll = wlrDao.getPageAll(dc);
            return pageAll;
        }
        return null;
    }

    /**
     * 详情数据导出
     * @param wls
     * @return
     */
    public HSSFWorkbook createDetailsExcel(List<WuliuEntity> wls) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("物流收件人详情信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("运单号");
        cell = row.createCell(2);
        cell.setCellValue("寄件时间");
        cell = row.createCell(3);
        cell.setCellValue("寄件人");
        cell = row.createCell(4);
        cell.setCellValue("寄件电话");
        cell = row.createCell(5);
        cell.setCellValue("寄件地址");
        cell = row.createCell(6);
        cell.setCellValue("收件人");
        cell = row.createCell(7);
        cell.setCellValue("收件电话");
        cell = row.createCell(8);
        cell.setCellValue("收件地址");
        cell = row.createCell(9);
        cell.setCellValue("托寄物");
        cell = row.createCell(10);
        cell.setCellValue("付款方式");
        cell = row.createCell(11);
        cell.setCellValue("代收货款");
        cell = row.createCell(12);
        cell.setCellValue("运费");
        int i = 1;
        int b = 1;
        for(WuliuEntity wl:wls) {
            if (i >= 65536 && i % 65536 == 0) {
                sheet = wb.createSheet("物流收件人详情信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("运单号");
                cell = row.createCell(2);
                cell.setCellValue("寄件时间");
                cell = row.createCell(3);
                cell.setCellValue("寄件人");
                cell = row.createCell(4);
                cell.setCellValue("寄件电话");
                cell = row.createCell(5);
                cell.setCellValue("寄件地址");
                cell = row.createCell(6);
                cell.setCellValue("收件人");
                cell = row.createCell(7);
                cell.setCellValue("收件电话");
                cell = row.createCell(8);
                cell.setCellValue("收件地址");
                cell = row.createCell(9);
                cell.setCellValue("托寄物");
                cell = row.createCell(10);
                cell.setCellValue("付款方式");
                cell = row.createCell(11);
                cell.setCellValue("代收货款");
                cell = row.createCell(12);
                cell.setCellValue("运费");
                b += 1;
            }
            row = sheet.createRow(i%65536);
            cell = row.createCell(0);
            cell.setCellValue(i);
            cell = row.createCell(1);
            cell.setCellValue(wl.getWaybill_id());
            cell = row.createCell(2);
            cell.setCellValue(wl.getShip_time());
            cell = row.createCell(3);
            cell.setCellValue(wl.getSender());
            cell = row.createCell(4);
            cell.setCellValue(wl.getShip_phone());
            cell = row.createCell(5);
            cell.setCellValue(wl.getShip_address());
            cell = row.createCell(6);
            cell.setCellValue(wl.getAddressee());
            cell = row.createCell(7);
            cell.setCellValue(wl.getSj_phone());
            cell = row.createCell(8);
            cell.setCellValue(wl.getSj_address());
            cell = row.createCell(9);
            cell.setCellValue(wl.getTjw());
            cell = row.createCell(10);
            cell.setCellValue(wl.getPayment());
            cell = row.createCell(11);
            cell.setCellValue(wl.getDshk());
            cell = row.createCell(12);
            cell.setCellValue(wl.getFreight());
            if(i%65536==0) {
                for (int a = 0; a < 13; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
            i++;
        }
        return wb;
    }
}
