package cn.com.sinofaith.service.wlService;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.cftBean.CftZcxxEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.bean.wlBean.WuliuRelationEntity;
import cn.com.sinofaith.dao.wuliuDao.WuliuDao;
import cn.com.sinofaith.dao.wuliuDao.WuliuJjxxDao;
import cn.com.sinofaith.dao.wuliuDao.WuliuRelationDao;
import cn.com.sinofaith.page.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WuliuJjxxService {

    @Autowired
    private WuliuDao wld;
    @Autowired
    private WuliuJjxxDao jjxxDao;
    @Autowired
    private WuliuRelationDao wlDao;
    /**
     * 获取案件id获取物流数据
     * @param id
     * @return
     */
    public List<WuliuEntity> getAll(long id) {
        String hql = "from WuliuEntity where aj_id = "+id;
        List<WuliuEntity> wulius = wld.find(hql);
        return wulius;
    }

    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc, AjEntity aj, String seach) {
        Page page = new Page();
        int rowAll = jjxxDao.getRowAll(seach,aj);
        List<WuliuEntity> wls = null;
        if(rowAll>0){
            wls = jjxxDao.getDoPage(currentPage, pageSize, dc, aj);
            for (int i = 0; i <wls.size(); i++) {
                wls.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            page.setPageSize(pageSize);
            page.setTotalRecords(rowAll);
            page.setList(wls);
            page.setPageNo(currentPage);
        }
        return page;
    }

    /**
     * 获取寄收件人关系信息
     * @param id
     * @return
     */
    public void insertRelation(long id) {
        int rowCount = wlDao.getRowCount(id);
        if(rowCount>0){
            wlDao.deleteWuliuRelation(id);
        }
        List<WuliuRelationEntity> wlrs = jjxxDao.insertRelation(id);
        wlDao.insertWuliuRelation(wlrs);
    }

    /**
     * 获取全部数据
     * @param dc
     * @return
     */
    public List<WuliuEntity> getWuliuAll(String seach,DetachedCriteria dc, AjEntity aj) {
        int rowAll = jjxxDao.getRowAll(seach,aj);
        List<WuliuEntity> wls = null;
        if(rowAll>0){
            wls = jjxxDao.getWuliuAll(dc, aj);
        }
        return wls;
    }

    /**
     * 创建excel文件
     * @param wls
     * @return
     */
    public HSSFWorkbook createExcel(List<WuliuEntity> wls) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("物流寄件信息");
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
                sheet = wb.createSheet("物流寄件信息(" + b + ")");
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
