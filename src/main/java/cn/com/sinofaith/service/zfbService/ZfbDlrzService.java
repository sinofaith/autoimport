package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.dao.zfbDao.ZfbDlrzDao;
import cn.com.sinofaith.form.zfbForm.ZfbDlrzForm;
import cn.com.sinofaith.form.zfbForm.ZfbDlrzForms;
import cn.com.sinofaith.page.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * 支付宝登录日志业务层
 * @author zd
 * create by 2018.11.28
 */
@Service
public class ZfbDlrzService {
    @Autowired
    private ZfbDlrzDao zfbDlrzDao;

    /**
     *TODO 登陆日志分页数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param id
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, String search, long id) {
        Page page = new Page();
        // 获取总条数
        int rowAll = zfbDlrzDao.getRowAlls(search,id);
        if(rowAll>0){
            List<ZfbDlrzForm> zfbDlrzForm = zfbDlrzDao.queryForPage(currentPage,pageSize,search,id);
            if(zfbDlrzForm!=null) {
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(zfbDlrzForm);
                page.setPageSize(pageSize);
            }
        }
        return page;

    }

    /**
     * 获取时序图数据
     * @param zfbyhId
     * @param id
     * @return
     */
    public List<ZfbDlrzForms> getSequenceChart(String zfbyhId, long id) {
        return zfbDlrzDao.getSequenceChart(zfbyhId,id);
    }

    /**
     * 支付宝登陆日志导出
     * @param search
     * @param id
     * @return
     */
    public List<ZfbDlrzForm> getZfbDlrzAll(String search, long id) {
        List<ZfbDlrzForm> dlrzForms = null;
        int rowAll = zfbDlrzDao.getRowAlls(search,id);
        if(rowAll>0){
            dlrzForms = zfbDlrzDao.getZfbDlrzAll(search,id);
        }
        return dlrzForms;
    }

    /**
     * 生成excel表
     * @param dlrzForm
     * @return
     */
    public HSSFWorkbook createExcel(List<ZfbDlrzForm> dlrzForm) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("支付宝登陆日志");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("支付宝用户ID");
        cell = row.createCell(2);
        cell.setCellValue("账户名");
        cell = row.createCell(3);
        cell.setCellValue("登陆手机");
        cell = row.createCell(4);
        cell.setCellValue("证件类型");
        cell = row.createCell(5);
        cell.setCellValue("证件号");
        cell = row.createCell(6);
        cell.setCellValue("登陆总次数");
        int b = 1;
        for(int i=0;i<dlrzForm.size();i++) {
            ZfbDlrzForm wl = dlrzForm.get(i);
            if ((i+1) >= 65536 && (i+1) % 65536 == 0) {
                sheet = wb.createSheet("支付宝登陆日志(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("支付宝用户ID");
                cell = row.createCell(2);
                cell.setCellValue("账户名");
                cell = row.createCell(3);
                cell.setCellValue("登陆手机");
                cell = row.createCell(4);
                cell.setCellValue("证件类型");
                cell = row.createCell(5);
                cell.setCellValue("证件号");
                cell = row.createCell(6);
                cell.setCellValue("登陆总次数");
                b += 1;
            }else{
                row = sheet.createRow((i+1)%65536);
                cell = row.createCell(0);
                cell.setCellValue(i+1);
                cell = row.createCell(1);
                cell.setCellValue(wl.getZfbyhId());
                cell = row.createCell(2);
                cell.setCellValue(wl.getZhmc());
                cell = row.createCell(3);
                cell.setCellValue(wl.getDlsj());
                cell = row.createCell(4);
                cell.setCellValue(wl.getZjlx());
                cell = row.createCell(5);
                cell.setCellValue(wl.getZjh());
                cell = row.createCell(6);
                cell.setCellValue(wl.getDlzcs());
            }
            if((i+1)%65536==0) {
                for (int a = 0; a < 7; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }
}
