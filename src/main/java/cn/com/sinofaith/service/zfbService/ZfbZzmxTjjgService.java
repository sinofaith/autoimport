package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbZzmxTjjgDao;
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
 * 支付宝转账明细业务层
 * @author zd
 * create by 2018.12.13
 */
@Service
public class ZfbZzmxTjjgService {
    @Autowired
    private ZfbZzmxTjjgDao zfbZzmxTjjgDao;

    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = null;
        int rowAll = zfbZzmxTjjgDao.getRowAll(dc);
        if(rowAll>0){
            List<ZfbZzmxTjjgEntity> zcxxList = zfbZzmxTjjgDao.getDoPage(currentPage, pageSize, dc);
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
     * 统计结果详情数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param aj_id
     * @return
     */
    public String getZfbZzmxTjjg(int currentPage, int pageSize, String search, long aj_id) {
        Gson gson = new Gson();
        Page page = null;
        int rowAll = zfbZzmxTjjgDao.getRowAllCount(search,aj_id);
        if(rowAll>0){
            List<ZfbZzmxEntity> zzmxList = zfbZzmxTjjgDao.getDoPageTjjg(currentPage, pageSize, search,aj_id,true);
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
     * 数据下载
     * @param dc
     * @return
     */
    public List<ZfbZzmxTjjgEntity> getZfbZzmxTjjgAll(DetachedCriteria dc) {
        List<ZfbZzmxTjjgEntity> wls = null;
        int rowAll = zfbZzmxTjjgDao.getRowAll(dc);
        if(rowAll>0){
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
        for(int i=0;i<tjjgs.size();i++) {
            ZfbZzmxTjjgEntity wl = tjjgs.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
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
            row = sheet.createRow((i+b)%65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
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
            if((i+b)%65536==0) {
                for (int a = 0; a < 9; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }

    /**
     * 页面加载显示数据
     * @param dc
     * @return
     */
    public List<String> onload(DetachedCriteria dc) {
        return zfbZzmxTjjgDao.onload(dc);
    }

    /**
     * 详情页数据导出
     * @param search
     * @param id
     * @return
     */
    public List<ZfbZzmxEntity> getZfbZzmxDetails(String search, long id) {
        List<ZfbZzmxEntity> zzmxs = null;
        int rowAll = zfbZzmxTjjgDao.getRowAllCount(search,id);
        if(rowAll>0){
            // 获取详情总条数
            zzmxs = zfbZzmxTjjgDao.getDoPageTjjg(0, 0, search,id,false);
        }
        return zzmxs;
    }
}
