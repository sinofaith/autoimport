package cn.com.sinofaith.service.customerServices;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.BankCustomerEntity;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.customerDao.CustomerDao;
import cn.com.sinofaith.form.CustomerProForm;
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
public class CustomerService {
    @Autowired
    private CustomerDao bcd;
    @Autowired
    private AJDao ad;


    public String getAjidByAjm(AjEntity aj, long userId){
        String[] ajm = new String[]{};
        StringBuffer ajid = new StringBuffer();
        if(aj.getAj().contains(",")) {
            ajm = aj.getAj().split(",");
            for (int i = 0; i < ajm.length; i++) {
                ajid.append(ad.findFilter(ajm[i],userId).get(0).getId());
                if (i != ajm.length - 1) {
                    ajid.append(",");
                }
            }
        }else{
            ajid.append(aj.getId());
        }
        return ajid.toString();
    }

    public String   getSeach(String seachCode, String seachCondition ,long userId){
//        String ajid = getAjidByAjm(aj,userId);
        StringBuffer seach = new StringBuffer();

        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            seach.append(" and c." + seachCondition + " like " + "'%" + seachCode + "%'");
        }else{
            seach.append(" and ( 1=1 ) ");
        }
        return seach.toString();
    }



    public Page queryForPage(int currentPage, int pageSize, String seach){
        Page page = new Page();
        List<CustomerProForm> zzFs = new ArrayList<>();
        int allRow = bcd.getAllRowCount(seach);
        if(allRow>0){
            List zzList = bcd.getDoPage(seach,currentPage,pageSize);
            for(int i=0;i<zzList.size();i++) {
                CustomerProForm zzf = new CustomerProForm();
                Map map = (Map) zzList.get(i);
                zzf = zzf.mapToObj(map);
                zzf.setXh(i+1);
                zzFs.add(zzf);
            }
        }
        page.setPageSize(pageSize);
        page.setPageNo(currentPage);
        page.setTotalRecords(allRow);
        page.setList(zzFs);
        return page;
    }

    public void downloadFile(String seach, HttpServletResponse rep, String aj) throws Exception{
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT  c.* ");
        sql.append("FROM  bank_customer c right join rel_customer_aj s on c.zjhm = s.zjhm ");
        sql.append(" where 1=1 " + seach );
        List listZzxx = bcd.findBySQL(sql.toString());
        HSSFWorkbook wb = createExcel(listZzxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String(("人员信息信息\""+aj+".xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public HSSFWorkbook createExcel(List listZzxx)throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        Cell cell = null;
        int b = 1;
        Sheet sheet = wb.createSheet("银行卡客户信息");
        Row row = createRow(sheet);
        for (int i = 0; i<listZzxx.size(); i++) {
            if(i>=65535&& i%65535==0){
                sheet = wb.createSheet("银行卡客户信息("+b+")");
                row = createRow(sheet);
                b+=1;
            }
            Map map = (Map) listZzxx.get(i);
            BankCustomerEntity zzf = new BankCustomerEntity();
            zzf = zzf.mapToObj(map);
            row = sheet.createRow(i%65535 + 1);
            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell = row.createCell(1);
            cell.setCellValue(zzf.getName());
            cell = row.createCell(2);
            cell.setCellValue(zzf.getZjlx());
            cell = row.createCell(3);
            cell.setCellValue(zzf.getZjhm());
            cell = row.createCell(4);
            cell.setCellValue(zzf.getXzz_xzqh());
            cell = row.createCell(5);
            cell.setCellValue(zzf.getLxdh());
            cell = row.createCell(6);
            cell.setCellValue(zzf.getLxsj());
            cell = row.createCell(7);
            cell.setCellValue(zzf.getDwdh());
            cell = row.createCell(8);
            cell.setCellValue(zzf.getZzdh());
            cell = row.createCell(9);
            cell.setCellValue(zzf.getGzdw());
            cell = row.createCell(10);
            cell.setCellValue(zzf.getEmail());

            if(i%65536==0){
                for (int a = 0; a < 15; a++) {
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
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("证件类型");
        cell = row.createCell(3);
        cell.setCellValue("证件号码");
        cell = row.createCell(4);
        cell.setCellValue("现住址");
        cell = row.createCell(5);
        cell.setCellValue("联系电话");
        cell = row.createCell(6);
        cell.setCellValue("联系手机");
        cell = row.createCell(7);
        cell.setCellValue("单位电话");
        cell = row.createCell(8);
        cell.setCellValue("住宅电话");
        cell = row.createCell(9);
        cell.setCellValue("工作单位");
        cell = row.createCell(10);
        cell.setCellValue("邮箱");
        return row;
    }
}
