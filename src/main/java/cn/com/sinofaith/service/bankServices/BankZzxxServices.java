package cn.com.sinofaith.service.bankServices;


import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.bankDao.BankZzxxDao;
import cn.com.sinofaith.form.bankForm.BankZzxxForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.util.ExcelReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

@Service
public class BankZzxxServices {
    @Autowired
    private BankZzxxDao bankzzd;
    @Autowired
    private AJDao ad;

    public String getSeach(String seachCode, String seachCondition,String orderby,String desc, AjEntity aj){
        String ajid = getAjidByAjm(aj);
        StringBuffer seach = new StringBuffer(" and c.aj_id in ("+ajid+") ");

        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if("khxm".equals(seachCondition)){
                seach.append(" and (s."+seachCondition + " like '"+seachCode+"' or c.jyxm like '"+seachCode+"')");
            } else {
                seach.append(" and c." + seachCondition + " like " + "'" + seachCode + "'");
            }
        }else{
            seach.append(" and ( 1=1 ) ");
        }
        if(orderby != null){
            seach .append(" order by c."+orderby).append(desc).append(",c.id");
        }
        return seach.toString();
    }

    public String getAjidByAjm(AjEntity aj){
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
        return ajid.toString();
    }
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
        List listZzxx = bankzzd.findBySQL("SELECT  s.khxm jyxms,d.khxm dfxms,c.* FROM  bank_zzxx c " +
                "left join bank_person s on c.yhkkh=s.yhkkh  " +
                "left join bank_person d on c.dskh=d.yhkkh where 1=1 "+seach);
        HSSFWorkbook wb = createExcel(listZzxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String(("银行卡转账信息(\""+aj+").xls").getBytes(), "ISO8859-1"));
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
            cell.setCellValue(zzf.getYhkkh());
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
            cell.setCellValue(zzf.getDskh());
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

    public String getByYhkkh(String zh,String jylx,String type,AjEntity aj,int page){
        Page pages = new Page();
        Gson gson = new GsonBuilder().serializeNulls().create();
        String ajid = getAjidByAjm(aj);

        String seach ="";
        if("tjjg".equals(type)){
            seach=" and c.yhkkh='"+zh+"' ";
        }else{
            seach+=" and c.dskh = '"+jylx+"' ";
        }
//        if(aj.getFlg()==1){
//            seach +=" and c.shmc not like'%红包%'";
//        }
        seach += "and aj_id in("+ajid+") order by c.jysj desc ";

        int allRow = bankzzd.getAllRowCount(seach);
        List zzList = bankzzd.getDoPage(seach,page,100);



        List<BankZzxxForm> zzFs = new ArrayList<>();
        BankZzxxForm zzf = new BankZzxxForm();
        for(int i=0;i<zzList.size();i++){
            Map map = (Map)zzList.get(i);
            zzf = zzf.mapToForm(map);
            zzf.setId(i+1+(page-1)*100);
            zzFs.add(zzf);
        }
        pages.setTotalRecords(allRow);
        pages.setPageSize(100);
        pages.setPageNo(page);
        pages.setList(zzFs);
        return gson.toJson(pages);
    }

    public List<BankZzxxEntity> getAll(long aj_id){
       List<BankZzxxEntity> zz = bankzzd.find(" from BankZzxxEntity where aj_id = "+aj_id);
       return zz;
    }

}
