package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.dao.zfbDao.ZfbZzmxGtzhDao;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxGtzhForm;
import cn.com.sinofaith.page.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支付宝转账明细共同账户业务层
 * @author zd
 * create by 2018.12.15
 */
@Service
public class ZfbZzmxGtzhService {
    @Autowired
    private ZfbZzmxGtzhDao zfbZzmxGtzhDao;

    /**
     * 共同账户分页/详情分页数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param id
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, String search, long id) {
        Page page = new Page();
        List<ZfbZzmxGtzhForm> jyjlForms = null;
        int rowAll = zfbZzmxGtzhDao.getCountRow(search,id);
        if(rowAll>0){
            jyjlForms = zfbZzmxGtzhDao.queryForPage(currentPage,pageSize,search,id, true);
            for (int i =0;i<jyjlForms.size();i++) {
                jyjlForms.get(i).setId((currentPage-1)*pageSize+i+1);
            }
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
     * 数据导出
     * @param id
     * @param search
     * @return
     */
    public List<ZfbZzmxGtzhForm> getZfbZzmxGtzhAll(long id, String search) {
        List<ZfbZzmxGtzhForm> jyjlForms = null;
        int rowAll = zfbZzmxGtzhDao.getCountRow(search,id);
        if(rowAll>0) {
            jyjlForms = zfbZzmxGtzhDao.queryForPage(0,0,search,id, false);
        }
        return jyjlForms;
    }

    /**
     * 生成excel表
     * @param tjjgs
     * @return
     */
    public HSSFWorkbook createExcel(List<ZfbZzmxGtzhForm> tjjgs) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("支付宝转账共同账户信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("支付宝账号");
        cell = row.createCell(2);
        cell.setCellValue("账号名称");
        cell = row.createCell(3);
        cell.setCellValue("共同账户");
        cell = row.createCell(4);
        cell.setCellValue("共同联系人数");
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
        for(int i=0;i<tjjgs.size();i++) {
            ZfbZzmxGtzhForm wl = tjjgs.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                sheet = wb.createSheet("支付宝转账共同账户信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("支付宝账号");
                cell = row.createCell(2);
                cell.setCellValue("账号名称");
                cell = row.createCell(3);
                cell.setCellValue("共同账户");
                cell = row.createCell(4);
                cell.setCellValue("共同联系人数");
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
            row = sheet.createRow((i+b)%65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getZfbzh());
            cell = row.createCell(2);
            cell.setCellValue(wl.getZfbmc());
            cell = row.createCell(3);
            cell.setCellValue(wl.getDfzh());
            cell = row.createCell(4);
            cell.setCellValue(wl.getGthys());
            cell = row.createCell(5);
            cell.setCellValue(wl.getJyzcs());
            cell = row.createCell(6);
            cell.setCellValue(wl.getFkzcs());
            cell = row.createCell(7);
            cell.setCellValue(wl.getFkzje().toString());
            cell = row.createCell(8);
            cell.setCellValue(wl.getSkzcs());
            cell = row.createCell(9);
            cell.setCellValue(wl.getSkzje().toString());
            if((i+b)%65536==0) {
                for (int a = 0; a < 10; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }

    /**
     * 共同账户详情总数据
     * @param search
     * @param id
     * @return
     */
    public List<ZfbZzmxGtzhForm> getZfbZzmxDetails(String search, long id) {
        List<ZfbZzmxGtzhForm> gtzhForms = null;
        int rowAll = zfbZzmxGtzhDao.getCountRow(search,id);
        if(rowAll>0) {
            gtzhForms = zfbZzmxGtzhDao.queryForPage(0,0,search,id, true);
        }
        return gtzhForms;
    }
}
