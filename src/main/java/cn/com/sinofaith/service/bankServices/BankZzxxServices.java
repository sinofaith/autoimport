package cn.com.sinofaith.service.bankServices;


import cn.com.sinofaith.dao.bankDao.BankZzxxDao;
import cn.com.sinofaith.form.bankForm.BankZzxxForm;
import cn.com.sinofaith.page.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BankZzxxServices {
    @Autowired
    private BankZzxxDao bankzzd;
    public Page queryForPage(int currentPage,int pageSize,String seach){
        Page page = new Page();
        List<BankZzxxForm> zzFs = new ArrayList<>();
        BankZzxxForm zzf = new BankZzxxForm();
        int allRow = bankzzd.getAllRowCount(seach);
        if(allRow>0){
            List zzList = bankzzd.getDoPage(seach,currentPage,pageSize);
            int xh = 1;
            for(int i=0;i<zzList.size();i++) {
                Map map = (Map) zzList.get(i);
                zzf = zzf.mapToForm(map);
                zzf.setId(xh+(currentPage-1)*pageSize);
                zzFs.add(zzf);
                xh++;
            }
        }
        page.setPageSize(pageSize);
        page.setPageNo(currentPage);
        page.setTotalRecords(allRow);
        page.setList(zzFs);
        return page;
    }

    public void downloadFile(String seach, HttpServletResponse rep, String aj) throws Exception{
        List listZzxx = bankzzd.findBySQL("SELECT  s.khxm jyxm,d.khxm dfxm,c.* FROM  bank_zzxx c " +
                "left join bank_zcxx s on c.jyzkh=s.yhkzh " +
                "left join bank_zcxx d on c.dszh=d.yhkzh  where 1=1 "+seach);
        HSSFWorkbook wb = createExcel(listZzxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String(("银行卡账信息(\""+aj+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public HSSFWorkbook createExcel(List listZzxx)throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        Cell cell = null;
        int b = 1;
        Sheet sheet = wb.createSheet("银行卡转账信息");
        Row row = createRow(sheet);
        for (int i = 0; i<listZzxx.size(); i++) {
            if(i>=65535&& i%65535==0){
                sheet = wb.createSheet("银行卡转账信息("+b+")");
                row = createRow(sheet);
                b+=1;
            }
            Map map = (Map) listZzxx.get(i);
            BankZzxxForm zzf = new BankZzxxForm();
            zzf = zzf.mapToForm(map);
            row = sheet.createRow(i%65535 + 1);
            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell = row.createCell(1);
            cell.setCellValue(zzf.getJyxm());
            cell = row.createCell(2);
            cell.setCellValue(zzf.getJyzkh());
            cell = row.createCell(3);
            cell.setCellValue(zzf.getJysj());
            cell = row.createCell(4);
            cell.setCellValue(zzf.getJyje().toString());
            cell = row.createCell(5);
            cell.setCellValue(zzf.getJyye().toString());
            cell = row.createCell(6);
            cell.setCellValue(zzf.getSfbz());
            cell = row.createCell(7);
            cell.setCellValue(zzf.getDsxm());
            cell = row.createCell(8);
            cell.setCellValue(zzf.getDszh());
            cell = row.createCell(9);
            cell.setCellValue(zzf.getZysm());
            cell = row.createCell(10);
            cell.setCellValue(zzf.getJysfcg());
            if(i%65536==0){
                for (int a = 0; a < 12; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }

    public Row createRow(Sheet sheet){
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("交易户名");
        cell = row.createCell(2);
        cell.setCellValue("交易账卡号");
        cell = row.createCell(3);
        cell.setCellValue("交易时间");
        cell = row.createCell(4);
        cell.setCellValue("交易金额(元)");
        cell = row.createCell(5);
        cell.setCellValue("交易余额(元)");
        cell = row.createCell(6);
        cell.setCellValue("收付标志");
        cell = row.createCell(7);
        cell.setCellValue("对手户名");
        cell = row.createCell(8);
        cell.setCellValue("对手账卡号");
        cell = row.createCell(9);
        cell.setCellValue("摘要说明");
        cell = row.createCell(10);
        cell.setCellValue("交易状态");
        return row;
    }
}
