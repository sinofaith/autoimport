package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.CftZcxxEntity;
import cn.com.sinofaith.bean.CftZzxxEntity;
import cn.com.sinofaith.dao.CftZzxxDao;
import cn.com.sinofaith.page.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Me. on 2018/5/22
 */
@Service
public class CftZzxxService {
    @Autowired
    private CftZzxxDao cftzzd;

    public Page queryForPage(int currentPage, int pageSize, String seach){
        Page page = new Page();
        //总记录数
        int allRow = cftzzd.getAllRowCount(seach);
        List<CftZzxxEntity> cftzzxxs = new ArrayList<CftZzxxEntity>();
        int xh = 1;
        if(allRow != 0) {
            cftzzxxs = cftzzd.getDoPage(seach,currentPage,pageSize);
            for(int i=0; i<cftzzxxs.size();i++){
                cftzzxxs.get(i).setId(xh+(currentPage-1)*10);
                xh++;
            }
        }
        page.setList(cftzzxxs);
        page.setPageNo(currentPage);
        page.setPageSize(pageSize);
        page.setTotalRecords(allRow);
        return page;
    }

    public List<CftZzxxEntity> getAll(){
        List<CftZzxxEntity> zzs = cftzzd.getAlla();
        return zzs;
    }

    public void downloadFile(String seach, HttpServletResponse rep) throws Exception{
        List<CftZzxxEntity> listZzxx = cftzzd.find("from CftZzxxEntity where 1=1 "+seach);
        HSSFWorkbook wb = createExcel(listZzxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String("财付通转账信息.xls".getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public HSSFWorkbook createExcel(List<CftZzxxEntity> listZzxx)throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("财付通转账信息");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("微信账户");
        cell = row.createCell(2);
        cell.setCellValue("借贷类型");
        cell = row.createCell(3);
        cell.setCellValue("交易类型");
        cell = row.createCell(4);
        cell.setCellValue("交易金额(元)");
        cell = row.createCell(5);
        cell.setCellValue("交易时间");
        cell = row.createCell(6);
        cell.setCellValue("发送方");
        cell = row.createCell(7);
        cell.setCellValue("发送金额(元)");
        cell = row.createCell(8);
        cell.setCellValue("接收方");
        cell = row.createCell(9);
        cell.setCellValue("接收金额(元)");
        int i = 1;
        for(CftZzxxEntity zzxx: listZzxx){
            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(i);
            cell = row.createCell(1);
            cell.setCellValue(zzxx.getZh());
            cell = row.createCell(2);
            cell.setCellValue(zzxx.getJdlx());
            cell = row.createCell(3);
            cell.setCellValue(zzxx.getJylx());
            cell = row.createCell(4);
            cell.setCellValue(String.valueOf(zzxx.getJyje().doubleValue()));
            cell = row.createCell(5);
            cell.setCellValue(zzxx.getJysj());
            cell = row.createCell(6);
            cell.setCellValue(zzxx.getFsf());
            cell = row.createCell(7);
            cell.setCellValue(String.valueOf(zzxx.getFsje().doubleValue()));
            cell = row.createCell(8);
            cell.setCellValue(zzxx.getJsf());
            cell = row.createCell(9);
            cell.setCellValue(String.valueOf(zzxx.getJsje().doubleValue()));
            i++;
        }
        for(int a=0;a<10;a++){
            sheet.autoSizeColumn(a);
        }

        return wb;
    }
}