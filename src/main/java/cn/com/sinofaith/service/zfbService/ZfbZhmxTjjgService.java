package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbZhmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZhmxJczzEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZhmxTjjgEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbZhmxTjjgDao;
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
 * 支付宝账户明细账户与账户统计业务层
 * @author zd
 * create by 2019.02.18
 */
@Service
public class ZfbZhmxTjjgService {
    @Autowired
    ZfbZhmxTjjgDao zfbZhmxTjjgDao;

    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = null;
        int rowAll = zfbZhmxTjjgDao.getRowAll(dc);
        if(rowAll>0){
            List<ZfbZhmxTjjgEntity> zhmxTjjg = zfbZhmxTjjgDao.getDoPage(currentPage, pageSize, dc);
            if(zhmxTjjg!=null){
                for (int i = 0; i < zhmxTjjg.size(); i++) {
                    zhmxTjjg.get(i).setId((currentPage - 1) * pageSize + i + 1);
                }
                page = new Page();
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(zhmxTjjg);
                page.setPageSize(pageSize);
            }
        }
        return page;
    }

    /**
     * 详情数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param aj_id
     * @return
     */
    public String getZfbZhmxTjjg(int currentPage, int pageSize, String search, long aj_id) {
        Gson gson = new Gson();
        Page page = null;
        int rowAll = zfbZhmxTjjgDao.getRowAllCount(search, aj_id);
        if (rowAll > 0) {
            List<ZfbZhmxEntity> zhmxList = zfbZhmxTjjgDao.getDoPageTjjg(currentPage, pageSize, search, aj_id, true);
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
     * 导出数据
     * @param dc
     * @return
     */
    public List<ZfbZhmxTjjgEntity> getZfbZhmxTjjgAll(DetachedCriteria dc) {
        List<ZfbZhmxTjjgEntity> tjjgList = null;
        int rowAll = zfbZhmxTjjgDao.getRowAll(dc);
        if(rowAll>0){
            tjjgList = zfbZhmxTjjgDao.getDoPageAll(dc);
        }
        return tjjgList;
    }

    /**
     * 数据导出
     * @param tjjgList
     * @return
     */
    public HSSFWorkbook createExcel(List<ZfbZhmxTjjgEntity> tjjgList) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("支付宝账户明细账户与账户统计信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("交易支付宝账户");
        cell = row.createCell(2);
        cell.setCellValue("账户名称");
        cell = row.createCell(3);
        cell.setCellValue("对手支付宝账号");
        cell = row.createCell(4);
        cell.setCellValue("对手名称");
        cell = row.createCell(5);
        cell.setCellValue("交易总次数");
        cell = row.createCell(6);
        cell.setCellValue("出账总次数");
        cell = row.createCell(7);
        cell.setCellValue("出账总金额");
        cell = row.createCell(8);
        cell.setCellValue("进账总次数");
        cell = row.createCell(9);
        cell.setCellValue("进账总金额");
        int b = 1;
        for (int i = 0; i < tjjgList.size(); i++) {
            ZfbZhmxTjjgEntity wl = tjjgList.get(i);
            if ((i + b) >= 65536 && (i + b) % 65536 == 0) {
                sheet = wb.createSheet("支付宝账户明细账户与账户统计信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("交易支付宝账户");
                cell = row.createCell(2);
                cell.setCellValue("账户名称");
                cell = row.createCell(3);
                cell.setCellValue("对手支付宝账号");
                cell = row.createCell(4);
                cell.setCellValue("对手名称");
                cell = row.createCell(5);
                cell.setCellValue("交易总次数");
                cell = row.createCell(6);
                cell.setCellValue("出账总次数");
                cell = row.createCell(7);
                cell.setCellValue("出账总金额");
                cell = row.createCell(8);
                cell.setCellValue("进账总次数");
                cell = row.createCell(9);
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
            cell.setCellValue(wl.getDszfbzh());
            cell = row.createCell(4);
            cell.setCellValue(wl.getDsmc());
            cell = row.createCell(5);
            cell.setCellValue(wl.getJyzcs());
            cell = row.createCell(6);
            cell.setCellValue(wl.getCzzcs());
            cell = row.createCell(7);
            cell.setCellValue(wl.getCzzje().toString());
            cell = row.createCell(8);
            cell.setCellValue(wl.getJzzcs());
            cell = row.createCell(9);
            cell.setCellValue(wl.getJzzje().toString());
            if ((i + b) % 65536 == 0) {
                for (int a = 0; a < 10; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }

    /**
     * 详情数据导出
     * @param search
     * @param id
     * @return
     */
    public List<ZfbZhmxEntity> getZfbZhmxTjjgDetails(String search, long id) {
        List<ZfbZhmxEntity> zhmxList = null;
        int rowAll = zfbZhmxTjjgDao.getRowAllCount(search, id);
        if(rowAll>0){
            zhmxList = zfbZhmxTjjgDao.getDoPageTjjg(0,0, search, id,false);
        }
        return zhmxList;
    }
}
