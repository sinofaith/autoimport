package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.CftTjjgEntity;
import cn.com.sinofaith.bean.CftTjjgsEntity;
import cn.com.sinofaith.bean.CftZzxxEntity;
import cn.com.sinofaith.dao.CftTjjgsDao;
import cn.com.sinofaith.form.CftTjjgsForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Me. on 2018/5/23
 */
@Service
public class CftTjjgsService {
    @Autowired
    private CftTjjgsDao cfttjsd;

    public Page queryForPage(int currentPage, int pageSize, String seach){
        Page page = new Page();
        //总记录数
        int allRow = cfttjsd.getAllRowCount(seach);
        List<CftTjjgsForm> cfttjs = new ArrayList<CftTjjgsForm>();
        CftTjjgsForm cftForm = null;
        List cftList = cfttjsd.getDoPage(seach,currentPage,pageSize);
        if(allRow != 0) {
            int xh = 1;
            for(int i=0;i<cftList.size();i++){
                Map map = (Map)cftList.get(i);
                cftForm = new CftTjjgsForm();
                cftForm.setId(xh+(currentPage-1)*pageSize);
                cftForm.setName((String) map.get("XM"));
                cftForm.setJyzh((String) map.get("JYZH"));
                cftForm.setDfzh((String) map.get("DFZH"));
                cftForm.setJyzcs( new BigDecimal(map.get("JYZCS").toString()));
                cftForm.setJzzcs( new BigDecimal(map.get("JZZCS").toString()));
                cftForm.setJzzje( new BigDecimal(map.get("JZZJE").toString()));
                cftForm.setCzzcs( new BigDecimal(map.get("CZZCS").toString()));
                cftForm.setCzzje( new BigDecimal(map.get("CZZJE").toString()));
                cfttjs.add(cftForm);
                xh++;
            }

        }
        page.setList(cfttjs);
        page.setPageNo(currentPage);
        page.setPageSize(pageSize);
        page.setTotalRecords(allRow);
        return page;
    }

    public Page queryForPageGt(int currentPage, int pageSize, String seach,long ajid){
        Page page = new Page();
        //总记录数
        int allRow = cfttjsd.getGtCount(seach,ajid);
        List<CftTjjgsForm> cfttjs = new ArrayList<CftTjjgsForm>();
        CftTjjgsForm cftForm = null;
        List cftList = cfttjsd.getDoPageGt(seach,currentPage,pageSize,ajid);
        if(allRow != 0) {
            int xh = 1;
            for(int i=0;i<cftList.size();i++){
                Map map = (Map)cftList.get(i);
                cftForm = new CftTjjgsForm();
                cftForm.setId(xh+(currentPage-1)*pageSize);
                cftForm.setName((String) map.get("XM"));
                cftForm.setJyzh((String) map.get("JYZH"));
                cftForm.setDfzh((String) map.get("DFZH"));
                cftForm.setCount(new BigDecimal(map.get("NUM").toString()));
                cftForm.setJyzcs( new BigDecimal(map.get("JYZCS").toString()));
                cftForm.setJzzcs( new BigDecimal(map.get("JZZCS").toString()));
                cftForm.setJzzje( new BigDecimal(map.get("JZZJE").toString()));
                cftForm.setCzzcs( new BigDecimal(map.get("CZZCS").toString()));
                cftForm.setCzzje( new BigDecimal(map.get("CZZJE").toString()));
                cfttjs.add(cftForm);
                xh++;
            }

        }
        page.setList(cfttjs);
        page.setPageNo(currentPage);
        page.setPageSize(pageSize);
        page.setTotalRecords(allRow);
        return page;
    }

    public String getSeach(String seachCondition, String seachCode, String orderby, String desc, AjEntity aj){
        StringBuffer seach = new StringBuffer(" and aj_id ="+aj.getId());
        if(seachCode!=null){
            seachCode=seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if("jzzje".equals(seachCondition)||"czzje".equals(seachCondition)){
                 seach.append(" and c."+ seachCondition + " >= "+seachCode);
            }else if("xm".equals(seachCondition)){
                seach.append(" and s."+ seachCondition+" like "+"'"+ seachCode+"'");
            }
            else{
                seach.append(" and c."+ seachCondition+" like "+"'"+ seachCode +"'");
            }
        }else{
            seach.append(" and ( 1=1 ) ");
        }
        if(orderby!=null){
            if("xm".equals(orderby)){
                seach.append(seach + " order by s." + orderby + desc + " nulls last ,c.jyzcs desc,c.dfzh");
            }else if("num".equals(orderby)){
              seach.append(seach +" order by a." + orderby + desc + " nulls last ,c.jyzcs desc,c.dfzh");
            } else{
                seach.append(seach + " order by c." +orderby + desc);
            }
        }
        return seach.toString();
    }
//    public Page queryForPage(int currentPage,int pageSize,String seach){
//        Page page = new Page();
//        int allRow = cfttjsd.getAllRowCount(seach);
//        List<CftTjjgsEntity> cfttjs = new ArrayList<>();
//        int xh = 1;
//        if(allRow != 0){
//            cfttjs = cfttjsd.getDoPage(seach,currentPage,pageSize);
//            for(int i=0; i<cfttjs.size();i++){
//                cfttjs.get(i).setId(xh+(currentPage-1)*10);
//                xh++;
//            }
//        }
//        page.setList(cfttjs);
//        page.setPageNo(currentPage);
//        page.setPageSize(pageSize);
//        page.setTotalRecords(allRow);
//        return page;
//    }

    public int count(List<CftZzxxEntity> listZzxx,long aj){
        Map<String,CftTjjgsEntity> map = new HashMap();
        CftTjjgsEntity tjjgs = null;
        CftZzxxEntity zzxx = null;
        for(int i=0;i<listZzxx.size();i++){
            zzxx = listZzxx.get(i);
            if(zzxx.getZh().equals(zzxx.getFsf()) && "出".equals(zzxx.getJdlx())){
                if(map.containsKey(zzxx.getZh()+zzxx.getJsf())){
                    tjjgs = map.get(zzxx.getZh()+zzxx.getJsf());
                    tjjgs.setJyzcs(tjjgs.getJyzcs().add(new BigDecimal(1)));
                    tjjgs.setCzzcs(tjjgs.getCzzcs().add(new BigDecimal(1)));
                    tjjgs.setCzzje(tjjgs.getCzzje().add(zzxx.getJyje()));
                }else {
                    CftTjjgsEntity tjs1 = new CftTjjgsEntity();
                    tjs1.setJyzh(zzxx.getZh());
                    tjs1.setDfzh(zzxx.getJsf());
                    tjs1.setJyzcs(new BigDecimal(1));
                    tjs1.setCzzcs(new BigDecimal(1));
                    tjs1.setCzzje(zzxx.getJyje());
                    tjs1.setAj_id(aj);
                    map.put(zzxx.getZh()+zzxx.getJsf(),tjs1);
                }
            }
            if(zzxx.getZh().equals(zzxx.getJsf()) && "入".equals(zzxx.getJdlx())){
                if(map.containsKey(zzxx.getZh()+zzxx.getFsf())){
                    tjjgs = map.get(zzxx.getZh()+zzxx.getFsf());
                    tjjgs.setJyzcs(tjjgs.getJyzcs().add(new BigDecimal(1)));
                    tjjgs.setJzzcs(tjjgs.getJzzcs().add(new BigDecimal(1)));
                    tjjgs.setJzzje(tjjgs.getJzzje().add(zzxx.getJyje()));
                }else{
                    CftTjjgsEntity tjs2 = new CftTjjgsEntity();
                    tjs2.setJyzh(zzxx.getZh());
                    tjs2.setDfzh(zzxx.getFsf());
                    tjs2.setJyzcs(new BigDecimal(1));
                    tjs2.setJzzcs(new BigDecimal(1));
                    tjs2.setJzzje(zzxx.getJyje());
                    tjs2.setAj_id(aj);
                    map.put(zzxx.getZh()+zzxx.getFsf(),tjs2);
                }
            }
        }
        List<CftTjjgsEntity> listTjjgs = new ArrayList<>(map.values());
        int i =0;
        cfttjsd.delAll(aj);
        cfttjsd.save(listTjjgs);
        return i;
    }

    public void downloadFile(String seach, HttpServletResponse rep, String aj, String lx, HttpServletRequest req) throws Exception{
        List listTjjg = null;
        if("共同".equals(lx)){
            AjEntity aje = (AjEntity) req.getSession().getAttribute("aj");
            if(seach.contains("order by")) {
                listTjjg = cfttjsd.findBySQL("select s.xm,c.*,a.num from cft_tjjgs c right join (" +
                        "  select t.dfzh,count(1) as num from cft_tjjgs t " +
                        " where t.dfzh not in( select distinct t1.jyzh from cft_tjjgs t1) and t.aj_id=" +aje.getId()+
                        " group by dfzh " +
                        "  having(count(1)>=2) ) a on c.dfzh = a.dfzh" +
                        " left join cft_person s on c.jyzh = s.zh " +
                        "  where a.num is not null " + seach +",c.dfzh");
            }else{
                listTjjg = cfttjsd.findBySQL("select s.xm,c.*,a.num from cft_tjjgs c right join (" +
                        "  select t.dfzh,count(1) as num from cft_tjjgs t " +
                        " where t.dfzh not in( select distinct t1.jyzh from cft_tjjgs t1) and t.aj_id=" +aje.getId()+
                        " group by dfzh " +
                        "  having(count(1)>=2) ) a on c.dfzh = a.dfzh" +
                        " left join cft_person s on c.jyzh = s.zh " +
                        "  where a.num is not null " + seach +" order by c.dfzh");
            }

        }else {
            listTjjg = cfttjsd.findBySQL("select s.xm,c.* from cft_tjjgs c left join cft_person s on c.jyzh = s.zh where 1=1 "+seach);
        }

        HSSFWorkbook wb = createExcel(listTjjg,lx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String(("财付通"+lx+"账户信息(\""+aj+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }


    public HSSFWorkbook createExcel(List listTjjg,String lx){
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("财付通"+lx+"账户信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        if(!"共同".equals(lx)) {
            cell.setCellValue("序号");
            cell = row.createCell(1);
            cell.setCellValue("姓名");
            cell = row.createCell(2);
            cell.setCellValue("交易账户");
            cell = row.createCell(3);
            cell.setCellValue("对手账户");
            cell = row.createCell(4);
            cell.setCellValue("交易总次数");
            cell = row.createCell(5);
            cell.setCellValue("进账总次数");
            cell = row.createCell(6);
            cell.setCellValue("进账总金额(元)");
            cell = row.createCell(7);
            cell.setCellValue("出账总次数");
            cell = row.createCell(8);
            cell.setCellValue("出账总金额(元)");
            for (int a = 0; a < 10; a++) {
                sheet.autoSizeColumn(a);
            }
        }else{
            cell.setCellValue("序号");
            cell = row.createCell(1);
            cell.setCellValue("姓名");
            cell = row.createCell(2);
            cell.setCellValue("交易账户");
            cell = row.createCell(3);
            cell.setCellValue("对手账户");
            cell = row.createCell(4);
            cell.setCellValue("共同联系人数");
            cell = row.createCell(5);
            cell.setCellValue("交易总次数");
            cell = row.createCell(6);
            cell.setCellValue("进账总次数");
            cell = row.createCell(7);
            cell.setCellValue("进账总金额(元)");
            cell = row.createCell(8);
            cell.setCellValue("出账总次数");
            cell = row.createCell(9);
            cell.setCellValue("出账总金额(元)");
            for (int a = 0; a < 11; a++) {
                sheet.autoSizeColumn(a);
            }
        }
        int b = 1;
        for(int i=0;i<listTjjg.size();i++){
            if(i>=65535&& i%65535==0){
                sheet = wb.createSheet("财付通"+lx+"对手账户信息("+b+")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                if(!"共同".equals(lx)) {
                    cell.setCellValue("序号");
                    cell = row.createCell(1);
                    cell.setCellValue("姓名");
                    cell = row.createCell(2);
                    cell.setCellValue("交易账户");
                    cell = row.createCell(3);
                    cell.setCellValue("对手账户");
                    cell = row.createCell(4);
                    cell.setCellValue("交易总次数");
                    cell = row.createCell(5);
                    cell.setCellValue("进账总次数");
                    cell = row.createCell(6);
                    cell.setCellValue("进账总金额(元)");
                    cell = row.createCell(7);
                    cell.setCellValue("出账总次数");
                    cell = row.createCell(8);
                    cell.setCellValue("出账总金额(元)");
                    b += 1;
                }else{
                    cell.setCellValue("序号");
                    cell = row.createCell(1);
                    cell.setCellValue("姓名");
                    cell = row.createCell(2);
                    cell.setCellValue("交易账户");
                    cell = row.createCell(3);
                    cell.setCellValue("对手账户");
                    cell = row.createCell(4);
                    cell.setCellValue("共同联系人数");
                    cell = row.createCell(5);
                    cell.setCellValue("交易总次数");
                    cell = row.createCell(6);
                    cell.setCellValue("进账总次数");
                    cell = row.createCell(7);
                    cell.setCellValue("进账总金额(元)");
                    cell = row.createCell(8);
                    cell.setCellValue("出账总次数");
                    cell = row.createCell(9);
                    cell.setCellValue("出账总金额(元)");
                    b += 1;
                }
            }
            Map map = (Map)listTjjg.get(i);
            row = sheet.createRow(i%65535 + 1);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            if(!"共同".equals(lx)) {
                cell = row.createCell(1);
                if (map.get("XM") != null && map.get("XM").toString().length() > 0) {
                    cell.setCellValue(map.get("XM").toString());
                }
                cell = row.createCell(2);
                cell.setCellValue(map.get("JYZH").toString());
                cell = row.createCell(3);
                if (map.get("DFZH") != null && map.get("DFZH").toString().length() > 0) {
                    cell.setCellValue(map.get("DFZH").toString());
                }
                cell = row.createCell(4);
                cell.setCellValue(map.get("JYZCS").toString());
                cell = row.createCell(5);
                cell.setCellValue(map.get("JZZCS").toString());
                cell = row.createCell(6);
                cell.setCellValue(map.get("JZZJE").toString());
                cell = row.createCell(7);
                cell.setCellValue(map.get("CZZCS").toString());
                cell = row.createCell(8);
                cell.setCellValue(map.get("CZZJE").toString());
                if (i % 65536 == 0) {
                    for (int a = 0; a < 10; a++) {
                        sheet.autoSizeColumn(a);
                    }
                }
            }else{
                cell = row.createCell(1);
                if (map.get("XM") != null && map.get("XM").toString().length() > 0) {
                    cell.setCellValue(map.get("XM").toString());
                }
                cell = row.createCell(2);
                cell.setCellValue(map.get("JYZH").toString());
                cell = row.createCell(3);
                if (map.get("DFZH") != null && map.get("DFZH").toString().length() > 0) {
                    cell.setCellValue(map.get("DFZH").toString());
                }
                cell = row.createCell(4);
                cell.setCellValue(map.get("NUM").toString());
                cell = row.createCell(5);
                cell.setCellValue(map.get("JYZCS").toString());
                cell = row.createCell(6);
                cell.setCellValue(map.get("JZZCS").toString());
                cell = row.createCell(7);
                cell.setCellValue(map.get("JZZJE").toString());
                cell = row.createCell(8);
                cell.setCellValue(map.get("CZZCS").toString());
                cell = row.createCell(9);
                cell.setCellValue(map.get("CZZJE").toString());
                if (i % 65536 == 0) {
                    for (int a = 0; a < 11; a++) {
                        sheet.autoSizeColumn(a);
                    }
                }
            }
        }
        return wb;
    }
    public void deleteByAjid(long id){
        cfttjsd.delAll(id);
    }
}