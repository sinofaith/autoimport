package cn.com.sinofaith.service.bankServices;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.BankZcxxEntity;
import cn.com.sinofaith.dao.bankDao.BankZcxxDao;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.cftServices.CftZzxxService;
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


@Service
public class BankZcxxServices {

    @Autowired
    private CftZzxxService cfts;

    @Autowired
    private BankZcxxDao bzcd;

    public String getSeach(String seachCode, String seachCondition, AjEntity aj){
        StringBuffer seach = new StringBuffer();
        String ajid = cfts.getAjidByAjm(aj);

        if(seachCode!=null){
            seachCode =seachCode.trim().replace("\r\n","").replace("，","").replace("\t","");
            seach = seach.append(" and aj_id in ("+ajid.toString()+") and "+ seachCondition+" like "+"'%"+ seachCode +"%'");
        }else{
            seach = seach.append(" and aj_id in ("+ajid.toString()+") ");
        }

        return seach.toString();
    }

    public Page queryForPage(int pageNo,int pageSize,String seach){
        Page page = new Page();
        int allRow = bzcd.getAllRowCount(seach);
        List<BankZcxxEntity> listZc = new ArrayList<>();
        if(allRow>0){
            int xh = 1;
            listZc = bzcd.getDoPage(seach,pageNo,pageSize);
            for(int i=0; i<listZc.size();i++){
                listZc.get(i).setId(xh+(pageNo-1)*pageSize);
                xh++;
            }
        }
        page.setList(listZc);
        page.setPageNo(pageNo);
        page.setTotalRecords(allRow);
        page.setPageSize(pageSize);

        return page;
    }

    public void downloadFile(String seach, HttpServletResponse rep, String aj) throws Exception{
        List<BankZcxxEntity> listZcxx = bzcd.find("from BankZcxxEntity where 1=1"+seach+" order by id desc");
        HSSFWorkbook wb = createExcel(listZcxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-Disposition","attachment;filename="+new String(("银行卡信息(\""+aj+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public HSSFWorkbook createExcel(List<BankZcxxEntity> listZcxx) throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("银行卡开户信息");
        Row row = createRow(sheet);
        Cell cell = null;
        int i = 1;
        int b = 1;
        for(BankZcxxEntity zcxx:listZcxx){
            if(i>=65536 && i%65536==0){
                sheet = wb.createSheet("银行卡开户信息("+b+")");
                row = createRow(sheet);
                b+=1;
            }
            row = sheet.createRow(i%65536);
            cell = row.createCell(0);
            cell.setCellValue(i);
            cell = row.createCell(1);
            cell.setCellValue(zcxx.getZhzt());
            cell = row.createCell(2);
            cell.setCellValue(zcxx.getYhkkh());
            cell = row.createCell(3);
            cell.setCellValue(zcxx.getYhkzh());
            cell = row.createCell(4);
            cell.setCellValue(zcxx.getKhxm());
            cell = row.createCell(5);
            cell.setCellValue(zcxx.getKhzjh());
            cell = row.createCell(6);
            cell.setCellValue(zcxx.getZhye().toString());
            cell = row.createCell(7);
            cell.setCellValue(zcxx.getKyye().toString());
            cell = row.createCell(8);
            cell.setCellValue(zcxx.getKhsj());
            cell = row.createCell(9);
            cell.setCellValue(zcxx.getKhh());
            if(i%65536==0) {
                for (int a = 0; a < 20; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
            i++;
        }
        return wb;
    }

    public Row createRow(Sheet sheet){
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("账户状态");
        cell = row.createCell(2);
        cell.setCellValue("交易卡号");
        cell = row.createCell(3);
        cell.setCellValue("交易帐号");
        cell = row.createCell(4);
        cell.setCellValue("开户姓名");
        cell = row.createCell(5);
        cell.setCellValue("开户证件号");
        cell = row.createCell(6);
        cell.setCellValue("账户余额");
        cell = row.createCell(7);
        cell.setCellValue("可用余额");
        cell = row.createCell(8);
        cell.setCellValue("开户时间");
        cell = row.createCell(9);
        cell.setCellValue("开户行");
        return row;
    }
}
