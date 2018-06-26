package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.CftZcxxEntity;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.CftZcxxDao;
import cn.com.sinofaith.page.Page;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Me. on 2018/5/22
 * 财付通注册信息服务层
 */
@Service
public class CftZcxxService {

    @Autowired
    private CftZcxxDao czd;
    @Autowired
    private AJDao ad;


    public Page queryForPage(int currentPage,int pageSize,String seach){
        Page page = new Page();
        //总记录数
        int allRow = czd.getAllRowCount(seach);
        List<CftZcxxEntity> cftzcxxs = new ArrayList<CftZcxxEntity>();
        int xh = 1;
        if(allRow != 0) {
            cftzcxxs = czd.getDoPage(seach,currentPage,pageSize);
            for(int i=0; i<cftzcxxs.size();i++){
                cftzcxxs.get(i).setId(xh+(currentPage-1)*10);
                xh++;
            }
        }
        page.setList(cftzcxxs);
        page.setPageNo(currentPage);
        page.setPageSize(pageSize);
        page.setTotalRecords(allRow);
        return page;
    }

    public void downloadFile(String seach, HttpServletResponse rep,String aj) throws Exception{
        List<CftZcxxEntity> listZcxx = czd.find("from CftZcxxEntity where 1=1"+seach+" order by id");
        HSSFWorkbook wb = createExcel(listZcxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-Disposition","attachment;filename="+new String(("财付通注册信息(\""+aj+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public HSSFWorkbook createExcel(List<CftZcxxEntity> listZcxx)throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("财付通注册信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("账户状态");
        cell = row.createCell(2);
        cell.setCellValue("微信账户");
        cell = row.createCell(3);
        cell.setCellValue("姓名");
        cell = row.createCell(4);
        cell.setCellValue("注册时间");
        cell = row.createCell(5);
        cell.setCellValue("身份证号");
        cell = row.createCell(6);
        cell.setCellValue("绑定手机");
        cell = row.createCell(7);
        cell.setCellValue("开户行");
        cell = row.createCell(8);
        cell.setCellValue("银行账号");
        int i = 1;
        int b = 1;
        for(CftZcxxEntity czxx:listZcxx){
            if(i>=65536 && i%65536==0){
                sheet = wb.createSheet("财付通注册信息("+b+")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("账户状态");
                cell = row.createCell(2);
                cell.setCellValue("微信账户");
                cell = row.createCell(3);
                cell.setCellValue("姓名");
                cell = row.createCell(4);
                cell.setCellValue("注册时间");
                cell = row.createCell(5);
                cell.setCellValue("身份证号");
                cell = row.createCell(6);
                cell.setCellValue("绑定手机");
                cell = row.createCell(7);
                cell.setCellValue("开户行");
                cell = row.createCell(8);
                cell.setCellValue("银行账号");
                b+=1;
            }
            row = sheet.createRow(i%65536);
            cell = row.createCell(0);
            cell.setCellValue(i);
            cell = row.createCell(1);
            cell.setCellValue(czxx.getZhzt());
            cell = row.createCell(2);
            cell.setCellValue(czxx.getZh());
            cell = row.createCell(3);
            cell.setCellValue(czxx.getXm());
            cell = row.createCell(4);
            cell.setCellValue(czxx.getZcsj());
            cell = row.createCell(5);
            cell.setCellValue(czxx.getSfzhm());
            cell = row.createCell(6);
            cell.setCellValue(czxx.getBdsj());
            cell = row.createCell(7);
            cell.setCellValue(czxx.getKhh());
            cell = row.createCell(8);
            cell.setCellValue(czxx.getYhzh());
            if(i%65536==0) {
                for (int a = 0; a < 9; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
            i++;
        }
        return wb;
    }

    public String getSeach(String seachCode, String seachCondition, AjEntity aj){
        StringBuffer seach = new StringBuffer();
        String[] ajm = new String[]{};
        StringBuffer ajid = new StringBuffer();
        if(aj.getAj().contains(",")) {
            ajm = aj.getAj().split(",");
            for (int i = 0; i < ajm.length; i++) {
                ajid.append(ad.findFilter(ajm[i]).get(0).getId());
                if (i != ajm.length - 1) {
                    ajid.append(",");
                }
            }
        }else{
            ajid.append(aj.getId());
        }

        if(seachCode!=null){
            seachCode =seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            seach = seach.append(" and aj_id in ("+ajid.toString()+") and "+ seachCondition+" like "+"'"+ seachCode +"'");
        }else{
            seach = seach.append(" and aj_id in ("+ajid.toString()+") and ( 1=1 ) ");
        }

        return seach.toString();
    }

    public void deleteByAj_id(long ajid){
        czd.deleteByAj(ajid);
    }
}