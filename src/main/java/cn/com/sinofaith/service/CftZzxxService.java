package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.CftZcxxEntity;
import cn.com.sinofaith.bean.CftZzxxEntity;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.CftZzxxDao;
import cn.com.sinofaith.form.CftZzxxForm;
import cn.com.sinofaith.page.Page;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jdk.nashorn.internal.parser.JSONParser;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Me. on 2018/5/22
 */
@Service
public class CftZzxxService {
    @Autowired
    private CftZzxxDao cftzzd;
    @Autowired
    private AJDao ad;

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
                zzf.setShmc((String)map.get("SHMC"));
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

    public List<CftZzxxEntity> getAll(long ajid,long filter){
        String seach = "";
        if(filter==1){
            seach=" and shmc not like '%红包%'";
        }
        List<CftZzxxEntity> zzs = cftzzd.getAlla(ajid,seach);

        return zzs;
    }

    public void downloadFile(String seach, HttpServletResponse rep,String aj) throws Exception{
        List listZzxx = cftzzd.findBySQL("select s.xm,c.* from cft_zzxx c left join cft_person s on c.zh = s.zh where 1=1 "+seach);
        HSSFWorkbook wb = createExcel(listZzxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String(("财付通转账信息(\""+aj+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public HSSFWorkbook createExcel(List listZzxx)throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        int b = 1;
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
                cell.setCellValue("商户名称");
                cell = row.createCell(6);
                cell.setCellValue("交易金额(元)");
                cell = row.createCell(7);
                cell.setCellValue("交易时间");
                cell = row.createCell(8);
                cell.setCellValue("发送方");
                cell = row.createCell(9);
                cell.setCellValue("发送金额(元)");
                cell = row.createCell(10);
                cell.setCellValue("接收方");
                cell = row.createCell(11);
                cell.setCellValue("接收金额(元)");
            for (int i = 0; i<listZzxx.size(); i++) {
                if(i>=65535&& i%65535==0){
                    sheet = wb.createSheet("财付通转账信息("+b+")");
                    row = sheet.createRow(0);
                    cell = row.createCell(0);
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
                    cell.setCellValue("商户名称");
                    cell = row.createCell(6);
                    cell.setCellValue("交易金额(元)");
                    cell = row.createCell(7);
                    cell.setCellValue("交易时间");
                    cell = row.createCell(8);
                    cell.setCellValue("发送方");
                    cell = row.createCell(9);
                    cell.setCellValue("发送金额(元)");
                    cell = row.createCell(10);
                    cell.setCellValue("接收方");
                    cell = row.createCell(11);
                    cell.setCellValue("接收金额(元)");
                    b+=1;
                }
                Map map = (Map) listZzxx.get(i);
                row = sheet.createRow(i%65535 + 1);
                cell = row.createCell(0);
                cell.setCellValue(i + 1);
                cell = row.createCell(1);
                if (map.get("XM") != null && map.get("XM").toString().length() > 0) {
                    cell.setCellValue(map.get("XM").toString());
                }
                cell = row.createCell(2);
                cell.setCellValue(map.get("ZH").toString());
                cell = row.createCell(3);
                cell.setCellValue(map.get("JDLX").toString());
                cell = row.createCell(4);
                cell.setCellValue(map.get("JYLX").toString());
                cell = row.createCell(5);
                cell.setCellValue(map.get("SHMC").toString());
                cell = row.createCell(6);
                cell.setCellValue(map.get("JYJE").toString());
                cell = row.createCell(7);
                cell.setCellValue(map.get("JYSJ").toString());
                cell = row.createCell(8);
                if (map.get("FSF") != null && map.get("FSF").toString().length() > 0) {
                    cell.setCellValue(map.get("FSF").toString());
                }
                cell = row.createCell(9);
                cell.setCellValue(map.get("FSJE").toString());
                cell = row.createCell(10);
                if (map.get("JSF") != null && map.get("JSF").toString().length() > 0) {
                    cell.setCellValue(map.get("JSF").toString());
                }
                cell = row.createCell(11);
                cell.setCellValue(map.get("JSJE").toString());
                if(i%65536==0){
                    for (int a = 0; a < 13; a++) {
                        sheet.autoSizeColumn(a);
                    }
                }
            }
        return wb;
    }

    public String getSeach(String seachCode, String seachCondition,String orderby,String desc, AjEntity aj){
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
        StringBuffer seach = new StringBuffer(" and aj_id in ("+ajid+") ");

        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if("xm".equals(seachCondition)){
                seach.append(" and s."+seachCondition + " like "+"'"+seachCode+"'");
            }else {
                seach.append(" and c." + seachCondition + " like " + "'" + seachCode + "'");
            }
        }else{
            seach.append(" and ( 1=1 ) ");
        }
        if(orderby != null){
            seach .append(" order by "+orderby).append(desc);
        }
        return seach.toString();
    }

    public void deleteByAj_id(long id){
        cftzzd.deleteByAjid(id);
    }

    public String getByJyzhlx(String zh,String jylx,String type,AjEntity aj,int page){
        Page pages = new Page();
        Gson gson = new GsonBuilder().serializeNulls().create();
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

        String seach ="";
        if("jylx".equals(type)){
            seach=" and c.zh='"+zh+"' and c.jylx='"+jylx+"' ";
        }else{
            seach=" and c.zh='"+zh+"' and (c.fsf='"+jylx+"' or c.jsf='"+jylx+"') ";
            if(zh.equals(jylx)){
                seach = "and c.fsf = c.jsf and c.zh='"+zh+"' ";
            }
        }
        if(aj.getFlg()==1){
            seach +=" and c.shmc not like'%红包%'";
        }
        seach += "and aj_id in("+ajid.toString()+") order by c.jysj desc";

        int allRow = cftzzd.getAllRowCount(seach);
        List zzList = cftzzd.getDoPage(seach,page,100);



        List<CftZzxxForm> zzFs = new ArrayList<>();
        CftZzxxForm zzf = null;
        for(int i=0;i<zzList.size();i++){
            Map map = (Map)zzList.get(i);
            zzf = new CftZzxxForm();
            zzf.setId(i+1+(page-1)*100);
            zzf.setName((String)map.get("XM"));
            zzf.setZh((String)map.get("ZH"));
            zzf.setJdlx((String)map.get("JDLX"));
            zzf.setJylx((String)map.get("JYLX"));
            zzf.setShmc((String)map.get("SHMC"));
            zzf.setJyje(new BigDecimal(map.get("JYJE").toString()));
            zzf.setJysj((String)map.get("JYSJ"));
            zzf.setFsf((String)map.get("FSF"));
            zzf.setFsje(new BigDecimal(map.get("FSJE").toString()));
            zzf.setJsf((String)map.get("JSF"));
            zzf.setJsje(new BigDecimal(map.get("JSJE").toString()));
            zzFs.add(zzf);
        }
        pages.setTotalRecords(allRow);
        pages.setPageSize(100);
        pages.setPageNo(page);
        pages.setList(zzFs);
        return gson.toJson(pages);
    }
}