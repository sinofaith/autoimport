package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbJyjlTjjgDao;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlTjjgForm;
import cn.com.sinofaith.page.Page;
import com.google.gson.Gson;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支付宝交易记录统计结果业务层
 * @author zd
 * create by 2018.12.26
 */
@Service
public class ZfbJyjlTjjgService {
    @Autowired
    private ZfbJyjlTjjgDao zfbJyjlTjjgDao;

    /**
     * 统计结果分页数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param id
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, String search, long id) {
        Page page = new Page();
        List<ZfbJyjlTjjgForm> tjjgForms = null;
        int rowAll = zfbJyjlTjjgDao.getRowAlls(search,id);
        if(rowAll>0){
            tjjgForms = zfbJyjlTjjgDao.getPage(currentPage, pageSize,search, id, true);
            for (int i =0;i<tjjgForms.size();i++) {
                tjjgForms.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            page.setPageNo(currentPage);
            page.setTotalRecords(rowAll);
            page.setList(tjjgForms);
            page.setPageSize(pageSize);
        }
        return page;
    }

    /**
     * 分页详情数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @return
     */
    public String getZfbJyjlTjjg(int currentPage, int pageSize, String search) {
        Gson gson = new Gson();
        Page page = new Page();
        int rowAll = zfbJyjlTjjgDao.getRowAllCount(search);
        if(rowAll>0){
            List<ZfbJyjlEntity> jjylList = zfbJyjlTjjgDao.getDoPageTjjgs(currentPage, pageSize, search, true);
            for (int i =0;i<jjylList.size();i++) {
                jjylList.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            if(jjylList!=null){
                page = new Page();
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(jjylList);
                page.setPageSize(pageSize);
            }
        }
        return gson.toJson(page);
    }

    /**
     * 数据导出
     * @param search
     * @param id
     * @return
     */
    public List<ZfbJyjlTjjgForm> getZfbjyjlTjjgAll(String search,long id) {
        List<ZfbJyjlTjjgForm> tjjgForms = null;
        int rowAll = zfbJyjlTjjgDao.getRowAlls(search,id);
        if(rowAll>0){
            tjjgForms = zfbJyjlTjjgDao.getPage(0, 0,search, id, false);
        }
        return tjjgForms;
    }

    /**
     * 生成excel文件
     * @param tjjgs
     * @return
     */
    public HSSFWorkbook createExcel(List<ZfbJyjlTjjgForm> tjjgs) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("支付宝交易卖家信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("店铺名");
        cell = row.createCell(2);
        cell.setCellValue("卖家账号");
        cell = row.createCell(3);
        cell.setCellValue("账户名称");
        cell = row.createCell(4);
        cell.setCellValue("交易总次数");
        cell = row.createCell(5);
        cell.setCellValue("进账总次数");
        cell = row.createCell(6);
        cell.setCellValue("进账总金额");
        cell = row.createCell(7);
        cell.setCellValue("出账总次数");
        cell = row.createCell(8);
        cell.setCellValue("出账总金额");
        int b = 1;
        for(int i=0;i<tjjgs.size();i++) {
            ZfbJyjlTjjgForm wl = tjjgs.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                sheet = wb.createSheet("支付宝交易卖家信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("店铺名");
                cell = row.createCell(2);
                cell.setCellValue("卖家账号");
                cell = row.createCell(3);
                cell.setCellValue("账户名称");
                cell = row.createCell(4);
                cell.setCellValue("交易总次数");
                cell = row.createCell(5);
                cell.setCellValue("进账总次数");
                cell = row.createCell(6);
                cell.setCellValue("进账总金额");
                cell = row.createCell(7);
                cell.setCellValue("出账总次数");
                cell = row.createCell(8);
                cell.setCellValue("出账总金额");
                b += 1;
            }
            row = sheet.createRow((i+b)%65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getDyxcsj());
            cell = row.createCell(2);
            cell.setCellValue(wl.getDfzh());
            cell = row.createCell(3);
            cell.setCellValue(wl.getDfmc());
            cell = row.createCell(4);
            cell.setCellValue(wl.getJyzcs());
            cell = row.createCell(5);
            cell.setCellValue(wl.getSkzcs());
            cell = row.createCell(6);
            cell.setCellValue(wl.getSkzje().toString());
            cell = row.createCell(7);
            cell.setCellValue(wl.getFkzcs());
            cell = row.createCell(8);
            cell.setCellValue(wl.getFkzje().toString());
            if((i+b)%65536==0) {
                for (int a = 0; a < 9; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }

    /**
     * 支付宝统计结果详情数据
     * @param search
     * @return
     */
    public List<ZfbJyjlEntity> getZfbJyjlDetails(String search) {
        List<ZfbJyjlEntity> tjjgs = null;
        int rowAll = zfbJyjlTjjgDao.getRowAllCount(search);
        if(rowAll>0){
            tjjgs = zfbJyjlTjjgDao.getDoPageTjjgs(0, 0, search, false);
        }
        return tjjgs;
    }
}
