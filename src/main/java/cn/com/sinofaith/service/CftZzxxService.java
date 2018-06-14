package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.CftZcxxEntity;
import cn.com.sinofaith.bean.CftZzxxEntity;
import cn.com.sinofaith.dao.CftZzxxDao;
import cn.com.sinofaith.form.CftZzxxForm;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Me. on 2018/5/22
 */
@Service
public class CftZzxxService {
    @Autowired
    private CftZzxxDao cftzzd;

    public Page queryForPage(int currentPage,int pageSize,String seach){
        Page page = new Page();
        int allRow = cftzzd.getAllRowCount(seach);
        List<CftZzxxForm> zzFs = new ArrayList<>();
        CftZzxxForm zzf = null;
        List zzList = cftzzd.getDoPage(seach,currentPage,pageSize);
        if(allRow>0){
            int xh =1;
            for(int i=0;i<zzList.size();i++){
                Map map = (Map)zzList.get(i);
                zzf = new CftZzxxForm();
                zzf.setId(xh+(currentPage-1)*pageSize);
                zzf.setName((String)map.get("XM"));
                zzf.setZh((String)map.get("ZH"));
                zzf.setJdlx((String)map.get("JDLX"));
                zzf.setJylx((String)map.get("JYLX"));
                zzf.setJyje(new BigDecimal(map.get("JYJE").toString()));
                zzf.setJysj((String)map.get("JYSJ"));
                zzf.setFsf((String)map.get("FSF"));
                zzf.setFsje(new BigDecimal(map.get("FSJE").toString()));
                zzf.setJsf((String)map.get("JSF"));
                zzf.setJsje(new BigDecimal(map.get("JSJE").toString()));
                zzFs.add(zzf);
                xh++;
            }
        }
        page.setList(zzFs);
        page.setPageNo(currentPage);
        page.setPageSize(pageSize);
        page.setTotalRecords(allRow);
        return page;
    }

//    public Page queryForPage(int currentPage, int pageSize, String seach){
//        Page page = new Page();
//        //总记录数
//        int allRow = cftzzd.getAllRowCount(seach);
//        List<CftZzxxEntity> cftzzxxs = new ArrayList<CftZzxxEntity>();
//        int xh = 1;
//        if(allRow != 0) {
//            cftzzxxs = cftzzd.getDoPage(seach,currentPage,pageSize);
//            for(int i=0; i<cftzzxxs.size();i++){
//                cftzzxxs.get(i).setId(xh+(currentPage-1)*10);
//                xh++;
//            }
//        }
//        page.setList(cftzzxxs);
//        page.setPageNo(currentPage);
//        page.setPageSize(pageSize);
//        page.setTotalRecords(allRow);
//        return page;
//    }

    public List<CftZzxxEntity> getAll(){
        List<CftZzxxEntity> zzs = cftzzd.getAlla();
        return zzs;
    }

    public void downloadFile(String seach, HttpServletResponse rep) throws Exception{
        List listZzxx = cftzzd.findBySQL("select s.xm,c.* from cft_zzxx c ,cft_person s where 1=1 and c.zh = s.zh "+seach);
        HSSFWorkbook wb = createExcel(listZzxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String("财付通转账信息.xls".getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public HSSFWorkbook createExcel(List listZzxx)throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("财付通转账信息");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("微信账户");
        cell = row.createCell(3);
        cell.setCellValue("借贷类型");
        cell = row.createCell(4);
        cell.setCellValue("交易类型");
        cell = row.createCell(5);
        cell.setCellValue("交易金额(元)");
        cell = row.createCell(6);
        cell.setCellValue("交易时间");
        cell = row.createCell(7);
        cell.setCellValue("发送方");
        cell = row.createCell(8);
        cell.setCellValue("发送金额(元)");
        cell = row.createCell(9);
        cell.setCellValue("接收方");
        cell = row.createCell(10);
        cell.setCellValue("接收金额(元)");
        for(int i=0;i<listZzxx.size();i++){
            Map map = (Map)listZzxx.get(i);
            row = sheet.createRow(i+1);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            if(map.get("XM") != null && map.get("XM").toString().length()>0){
                cell.setCellValue(map.get("XM").toString());
            }
            cell = row.createCell(2);
            cell.setCellValue(map.get("ZH").toString());
            cell = row.createCell(3);
            cell.setCellValue(map.get("JDLX").toString());
            cell = row.createCell(4);
            cell.setCellValue(map.get("JYLX").toString());
            cell = row.createCell(5);
            cell.setCellValue(map.get("JYJE").toString());
            cell = row.createCell(6);
            cell.setCellValue(map.get("JYSJ").toString());
            cell = row.createCell(7);
            if(map.get("FSF") != null && map.get("FSF").toString().length()>0){
                cell.setCellValue(map.get("FSF").toString());
            }
            cell = row.createCell(8);
            cell.setCellValue(map.get("FSJE").toString());
            cell = row.createCell(9);
            if(map.get("JSF") != null && map.get("JSF").toString().length()>0){
                cell.setCellValue(map.get("JSF").toString());
            }
            cell = row.createCell(10);
            cell.setCellValue(map.get("JSJE").toString());
        }
        for(int a=0;a<12;a++){
            sheet.autoSizeColumn(a);
        }

        return wb;
    }
}