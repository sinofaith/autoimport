package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbZcxxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbZcxxDao;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.util.MappingUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 支付宝注册信息业务层
 * @author zd
 * create by 2018.11.28
 */
@Service
public class ZfbZcxxService {

    @Autowired
    private ZfbZcxxDao zfbZcxxDao;

    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = null;
        int rowAll = zfbZcxxDao.getRowAll(dc);
        if(rowAll>0){
            List<ZfbZcxxEntity> zcxxList = zfbZcxxDao.getDoPage(currentPage, pageSize, dc);
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
     * 支付宝注册信息导出
     * @param dc
     * @return
     */
    public List<ZfbZcxxEntity> getZfbZcxxAll(DetachedCriteria dc) {
        List<ZfbZcxxEntity> zcxxs = null;
        int rowAll = zfbZcxxDao.getRowAll(dc);
        if(rowAll>0){
            zcxxs = zfbZcxxDao.getDoPageAll(dc);
        }
        return zcxxs;
    }

    /**
     * 导出excel表
     * @param zcxxs
     * @return
     */
    public HSSFWorkbook createExcel(List<ZfbZcxxEntity> zcxxs) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("支付宝注册信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("用户Id");
        cell = row.createCell(2);
        cell.setCellValue("登陆邮箱");
        cell = row.createCell(3);
        cell.setCellValue("登陆手机");
        cell = row.createCell(4);
        cell.setCellValue("账户名称");
        cell = row.createCell(5);
        cell.setCellValue("证件类型");
        cell = row.createCell(6);
        cell.setCellValue("证件号");
        cell = row.createCell(7);
        cell.setCellValue("可用余额");
        cell = row.createCell(8);
        cell.setCellValue("绑定手机");
        cell = row.createCell(9);
        cell.setCellValue("绑定银行卡");
        cell = row.createCell(10);
        cell.setCellValue("店铺名");
        int b = 1;
        for(int i=0;i<zcxxs.size();i++) {
            ZfbZcxxEntity wl = zcxxs.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                sheet = wb.createSheet("支付宝注册信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("用户Id");
                cell = row.createCell(2);
                cell.setCellValue("登陆邮箱");
                cell = row.createCell(3);
                cell.setCellValue("登陆手机");
                cell = row.createCell(4);
                cell.setCellValue("账户名称");
                cell = row.createCell(5);
                cell.setCellValue("证件类型");
                cell = row.createCell(6);
                cell.setCellValue("证件号");
                cell = row.createCell(7);
                cell.setCellValue("可用余额");
                cell = row.createCell(8);
                cell.setCellValue("绑定手机");
                cell = row.createCell(9);
                cell.setCellValue("绑定银行卡");
                cell = row.createCell(10);
                cell.setCellValue("店铺名");
                b += 1;
            }
            row = sheet.createRow((i+b)%65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getYhId());
            cell = row.createCell(2);
            cell.setCellValue(wl.getDlyx());
            cell = row.createCell(3);
            cell.setCellValue(wl.getDlsj());
            cell = row.createCell(4);
            cell.setCellValue(wl.getZhmc());
            cell = row.createCell(5);
            cell.setCellValue(wl.getZjlx());
            cell = row.createCell(6);
            cell.setCellValue(wl.getZjh());
            cell = row.createCell(7);
            cell.setCellValue(wl.getKyye());
            cell = row.createCell(8);
            cell.setCellValue(wl.getBdsj());
            cell = row.createCell(9);
            cell.setCellValue(wl.getBdyhk());
            cell = row.createCell(10);
            cell.setCellValue(wl.getDyxcsj());
            if((i+b)%65536==0) {
                for (int a = 0; a < 11; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }
}
