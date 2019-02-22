package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlSjdzsEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlTjjgsEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbJyjlSjdzsDao;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlSjdzsForm;
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

/**
 * 支付宝交易记录地址业务层
 * @author zd
 * create by 2018.12.20
 */
@Service
public class ZfbJyjlSjdzsService {
    @Autowired
    private ZfbJyjlSjdzsDao zfbJyjlSjdzsDao;

    /**
     * 收件地址分页
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = null;
        int rowAll = zfbJyjlSjdzsDao.getRowAll(dc);
        if(rowAll>0){
            List<ZfbJyjlSjdzsEntity> sjdzsList = zfbJyjlSjdzsDao.getDoPage(currentPage, pageSize, dc);
            if(sjdzsList!=null){
                page = new Page();
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(sjdzsList);
                page.setPageSize(pageSize);
            }
        }
        return page;
    }

    /**
     * 地址详情数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @return
     */
    public String getZfbJyjlSjdzs(int currentPage, int pageSize, String search, long id) {
        Gson gson = new Gson();
        Page page = null;
        int rowAll = zfbJyjlSjdzsDao.getRowAllCount(search,id);
        if(rowAll>0){
            List<ZfbJyjlSjdzsForm> sjdzsList = zfbJyjlSjdzsDao.getDoPageSjdzs(currentPage, pageSize, search, id, true);
            for (int i =0;i<sjdzsList.size();i++) {
                sjdzsList.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            if(sjdzsList!=null){
                page = new Page();
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(sjdzsList);
                page.setPageSize(pageSize);
            }
        }
        return gson.toJson(page);
    }

    /**
     * 支付宝交易记录地址统计数据导出
     * @param dc
     * @return
     */
    public List<ZfbJyjlSjdzsEntity> getZfbjyjlSjdzsAll(DetachedCriteria dc) {
        List<ZfbJyjlSjdzsEntity> sjdzs = null;
        int rowAll = zfbJyjlSjdzsDao.getRowAll(dc);
        if(rowAll>0){
            sjdzs = zfbJyjlSjdzsDao.getDoPageAll(dc);
        }
        return sjdzs;
    }

    /**
     * 支付宝交易记录地址统计excel生成
     * @param sjdzs
     * @return
     */
    public HSSFWorkbook createExcel(List<ZfbJyjlSjdzsEntity> sjdzs) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("支付宝交易地址信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("买家用户Id");
        cell = row.createCell(2);
        cell.setCellValue("买家信息");
        cell = row.createCell(3);
        cell.setCellValue("收件总次数");
        cell = row.createCell(4);
        cell.setCellValue("出账总金额");
        cell = row.createCell(5);
        cell.setCellValue("收件地址数");
        int b = 1;
        for(int i=0;i<sjdzs.size();i++) {
            ZfbJyjlSjdzsEntity wl = sjdzs.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                sheet = wb.createSheet("支付宝交易地址信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("买家用户Id");
                cell = row.createCell(2);
                cell.setCellValue("买家信息");
                cell = row.createCell(3);
                cell.setCellValue("收件总次数");
                cell = row.createCell(4);
                cell.setCellValue("出账总金额");
                cell = row.createCell(5);
                cell.setCellValue("收件地址数");
                b += 1;
            }
            row = sheet.createRow((i+b)%65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getMjyhid());
            cell = row.createCell(2);
            cell.setCellValue(wl.getMjxx());
            cell = row.createCell(3);
            cell.setCellValue(wl.getSjzcs());
            cell = row.createCell(4);
            cell.setCellValue(wl.getCzzje().toString());
            cell = row.createCell(5);
            cell.setCellValue(wl.getSjdzs());
            if((i+b)%65536==0) {
                for (int a = 0; a < 6; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }

    /**
     * 支付宝地址详情全部数据
     * @param search
     * @return
     */
    public List<ZfbJyjlSjdzsForm> getZfbJyjlDetails(String search, long id) {
        List<ZfbJyjlSjdzsForm> sjdzs = null;
        int rowAll = zfbJyjlSjdzsDao.getRowAllCount(search, id);
        if(rowAll>0){
            sjdzs = zfbJyjlSjdzsDao.getDoPageSjdzs(0, 0, search, id, false);
        }
        return sjdzs;
    }

    /**
     * 单个地址详情数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @return
     */
    public String getSingleDetails(int currentPage, int pageSize, String search) {
        Gson gson = new Gson();
        Page page = null;
        int rowAll = zfbJyjlSjdzsDao.getRowAllCount1(search);
        if(rowAll>0){
            List<ZfbJyjlEntity> jyjlList = zfbJyjlSjdzsDao.getDoPageSjdzs1(currentPage, pageSize, search, true);
            for (int i =0;i<jyjlList.size();i++) {
                jyjlList.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            if(jyjlList!=null){
                page = new Page();
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(jyjlList);
                page.setPageSize(pageSize);
            }
        }
        return gson.toJson(page);
    }

    /**
     * 单个地址详情数据导出
     * @param search
     * @return
     */
    public List<ZfbJyjlEntity> downloadDetails1(String search) {
        List<ZfbJyjlEntity> jyjlList = null;
        int rowAll = zfbJyjlSjdzsDao.getRowAllCount1(search);
        if(rowAll>0) {
            jyjlList = zfbJyjlSjdzsDao.getDoPageSjdzs1(0, 0, search, false);
        }
        return jyjlList;
    }
}
