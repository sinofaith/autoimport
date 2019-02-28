package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbZhmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZhmxJczzEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbZhmxJczzDao;
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
 * 支付宝账户进出总账统计业务层
 * @author zd
 * create by 2019.02.19
 */
@Service
public class ZfbZhmxJczzService {
    @Autowired
    private ZfbZhmxJczzDao zfbZhmxJczzDao;

    /**
     * 分页数据获取
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = null;
        int rowAll = zfbZhmxJczzDao.getRowAll(dc);
        if(rowAll>0){
            List<ZfbZhmxJczzEntity> jczzList = zfbZhmxJczzDao.getDoPage(currentPage, pageSize, dc);
            if(jczzList!=null){
                page = new Page();
                page.setPageSize(pageSize);
                page.setList(jczzList);
                page.setTotalRecords(rowAll);
                page.setPageNo(currentPage);
            }
        }
        return page;
    }

    /**
     * 详情数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param id
     * @return
     */
    public String getZfbZhmxJczz(int currentPage, int pageSize, String search, long id) {
        Gson gson = new Gson();
        Page page = null;
        int rowAll = zfbZhmxJczzDao.getRowAllCount(search, id);
        if (rowAll > 0) {
            List<ZfbZhmxEntity> zhmxList = zfbZhmxJczzDao.getDoPageJczz(currentPage, pageSize, search, id, true);
            for (int i = 0; i < zhmxList.size(); i++) {
                zhmxList.get(i).setId((currentPage - 1) * pageSize + i + 1);
            }
            if (zhmxList != null) {
                page = new Page();
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(zhmxList);
                page.setPageSize(pageSize);
            }
        }
        return gson.toJson(page);
    }

    /**
     * 支付宝数据导出
     * @param dc
     * @return
     */
    public List<ZfbZhmxJczzEntity> getZfbZhmxJczzAll(DetachedCriteria dc) {
        List<ZfbZhmxJczzEntity> jczzList = null;
        int rowAll = zfbZhmxJczzDao.getRowAll(dc);
        if(rowAll>0){
            jczzList = zfbZhmxJczzDao.getDoPageAll(dc);
        }
        return jczzList;
    }

    /**
     * 生成excel表格
     * @param jczzs
     * @return
     */
    public HSSFWorkbook createExcel(List<ZfbZhmxJczzEntity> jczzs) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("支付宝账户明细进出总账统计信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("交易支付宝账户");
        cell = row.createCell(2);
        cell.setCellValue("账户名称");
        cell = row.createCell(3);
        cell.setCellValue("交易总次数");
        cell = row.createCell(4);
        cell.setCellValue("出账总次数");
        cell = row.createCell(5);
        cell.setCellValue("出账总金额");
        cell = row.createCell(6);
        cell.setCellValue("进账总次数");
        cell = row.createCell(7);
        cell.setCellValue("进账总金额");
        int b = 1;
        for (int i = 0; i < jczzs.size(); i++) {
            ZfbZhmxJczzEntity wl = jczzs.get(i);
            if ((i + b) >= 65536 && (i + b) % 65536 == 0) {
                sheet = wb.createSheet("支付宝账户明细进出总账统计信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("交易支付宝账户");
                cell = row.createCell(2);
                cell.setCellValue("账户名称");
                cell = row.createCell(3);
                cell.setCellValue("交易总次数");
                cell = row.createCell(4);
                cell.setCellValue("出账总次数");
                cell = row.createCell(5);
                cell.setCellValue("出账总金额");
                cell = row.createCell(6);
                cell.setCellValue("进账总次数");
                cell = row.createCell(7);
                cell.setCellValue("进账总金额");
                b += 1;
            }
            row = sheet.createRow((i + b) % 65536);
            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getJyzfbzh());
            cell = row.createCell(2);
            cell.setCellValue(wl.getJymc());
            cell = row.createCell(3);
            cell.setCellValue(wl.getJyzcs());
            cell = row.createCell(4);
            cell.setCellValue(wl.getCzzcs());
            cell = row.createCell(5);
            cell.setCellValue(wl.getCzzje());
            cell = row.createCell(6);
            cell.setCellValue(wl.getJzzcs());
            cell = row.createCell(7);
            cell.setCellValue(wl.getJzzje());
            if ((i + b) % 65536 == 0) {
                for (int a = 0; a < 8; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }

    /**
     * 详情数据全部数据
     * @param search
     * @param id
     * @return
     */
    public List<ZfbZhmxEntity> getZfbZhmxJczzDetails(String search, long id) {
        List<ZfbZhmxEntity> zhmxList = null;
        int rowAll = zfbZhmxJczzDao.getRowAllCount(search, id);
        if(rowAll>0){
            zhmxList = zfbZhmxJczzDao.getDoPageJczz(0,0, search, id,false);
        }
        return zhmxList;
    }
}
