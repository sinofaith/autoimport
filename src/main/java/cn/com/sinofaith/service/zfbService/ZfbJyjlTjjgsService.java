package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlTjjgsEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgsEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbJyjlTjjgsDao;
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
 * 支付宝交易记录对手账户业务层
 * @author zd
 * create by 2018.12.16
 */
@Service
public class ZfbJyjlTjjgsService {
    @Autowired
    private ZfbJyjlTjjgsDao zfbJyjlTjjgsDao;

    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = null;
        int rowAll = zfbJyjlTjjgsDao.getRowAll(dc);
        if(rowAll>0){
            List<ZfbJyjlTjjgsEntity> zcxxList = zfbJyjlTjjgsDao.getDoPage(currentPage, pageSize, dc);
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
     * 详情分页数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @return
     */
    public String getZfbJyjlTjjgs(int currentPage, int pageSize, String search, AjEntity aj) {
        Gson gson = new Gson();
        Page page = null;
        int rowAll = zfbJyjlTjjgsDao.getRowAllCount(search,aj);
        if(rowAll>0){
            List<ZfbJyjlEntity> zzmxList = zfbJyjlTjjgsDao.getDoPageTjjgs(currentPage, pageSize, search,aj, true);
            for (int i =0;i<zzmxList.size();i++) {
                zzmxList.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            if(zzmxList!=null){
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
     * 数据导出
     * @param dc
     * @return
     */
    public List<ZfbJyjlTjjgsEntity> getZfbjyjlTjjgAll(DetachedCriteria dc) {
        List<ZfbJyjlTjjgsEntity> wls = null;
        int rowAll = zfbJyjlTjjgsDao.getRowAll(dc);
        if(rowAll>0){
            wls = zfbJyjlTjjgsDao.getZfbZzmxTjjgAll(dc);
        }
        return wls;
    }

    /**
     * 生成excel表
     * @param tjjgs
     * @return
     */
    public HSSFWorkbook createExcel(List<ZfbJyjlTjjgsEntity> tjjgs) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("支付宝交易对手信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("支付宝账号");
        cell = row.createCell(2);
        cell.setCellValue("账号名称");
        cell = row.createCell(3);
        cell.setCellValue("交易状态");
        cell = row.createCell(4);
        cell.setCellValue("对方账号");
        cell = row.createCell(5);
        cell.setCellValue("对方信息");
        cell = row.createCell(6);
        cell.setCellValue("出账总次数");
        cell = row.createCell(7);
        cell.setCellValue("出账总金额");
        int b = 1;
        for(int i=0;i<tjjgs.size();i++) {
            ZfbJyjlTjjgsEntity wl = tjjgs.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                sheet = wb.createSheet("支付宝交易对手信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("支付宝账号");
                cell = row.createCell(2);
                cell.setCellValue("账号名称");
                cell = row.createCell(3);
                cell.setCellValue("交易状态");
                cell = row.createCell(4);
                cell.setCellValue("对方账号");
                cell = row.createCell(5);
                cell.setCellValue("对方信息");
                cell = row.createCell(6);
                cell.setCellValue("出账总次数");
                cell = row.createCell(7);
                cell.setCellValue("出账总金额");
                b += 1;
            }
            row = sheet.createRow((i+b)%65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getZfbzh());
            cell = row.createCell(2);
            cell.setCellValue(wl.getZfbmc());
            cell = row.createCell(3);
            cell.setCellValue(wl.getJyzt());
            cell = row.createCell(4);
            cell.setCellValue(wl.getDfzh());
            cell = row.createCell(5);
            cell.setCellValue(wl.getDfmc());
            cell = row.createCell(6);
            cell.setCellValue(wl.getFkzcs());
            cell = row.createCell(7);
            cell.setCellValue(wl.getFkzje().toString());
            if((i+b)%65536==0) {
                for (int a = 0; a < 8; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }

    /**支付宝交易记录对手详情全部数据
     * @param search
     * @param aj
     * @return
     */
    public List<ZfbJyjlEntity> getZfbJyjlDetails(String search, AjEntity aj) {
        List<ZfbJyjlEntity> jyjls = null;
        int rowAll = zfbJyjlTjjgsDao.getRowAllCount(search,aj);
        if(rowAll>0){
            jyjls = zfbJyjlTjjgsDao.getDoPageTjjgs(0, 0, search,aj, false);
        }
        return jyjls;
    }
}
