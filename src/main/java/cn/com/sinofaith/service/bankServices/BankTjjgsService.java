package cn.com.sinofaith.service.bankServices;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.BankTjjgsEntity;
import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import cn.com.sinofaith.bean.cftBean.CftTjjgsEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.dao.bankDao.BankTjjgsDao;
import cn.com.sinofaith.dao.cftDao.CftTjjgsDao;
import cn.com.sinofaith.form.cftForm.CftTjjgsForm;
import cn.com.sinofaith.page.Page;
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
public class BankTjjgsService {
    @Autowired
    private BankTjjgsDao banktjsd;

    public Page queryForPage(int currentPage, int pageSize, String seach){
        Page page = new Page();
        //总记录数
        int allRow = banktjsd.getAllRowCount(seach);
        List<CftTjjgsForm> cfttjs = new ArrayList<CftTjjgsForm>();
        CftTjjgsForm cftForm = null;
        List cftList = banktjsd.getDoPage(seach,currentPage,pageSize);
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
        int allRow = banktjsd.getGtCount(seach,ajid);
        List<CftTjjgsForm> cfttjs = new ArrayList<CftTjjgsForm>();
        CftTjjgsForm cftForm = null;
        List cftList = banktjsd.getDoPageGt(seach,currentPage,pageSize,ajid);
        if(allRow != 0) {
            int xh = 1;
            for(int i=0;i<cftList.size();i++){
                Map map = (Map)cftList.get(i);
                cftForm = new CftTjjgsForm();
                cftForm.setId(xh+(currentPage-1)*pageSize);
                cftForm.setName((String) map.get("XM"));
                cftForm.setJyzh((String) map.get("JYZH"));
                cftForm.setDfzh((String) map.get("DFZH"));
                cftForm.setDfxm((String) map.get("DFXM"));
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
            }else if("khxm".equals(seachCondition)){
                seach.append(" and s."+ seachCondition+" like "+"'%"+ seachCode+"%'");
            }
            else{
                seach.append(" and c."+ seachCondition+" like "+"'%"+ seachCode +"%'");
            }
        }else{
            seach.append(" and ( 1=1 ) ");
        }
        if(orderby!=null){
            if("khxm".equals(orderby)){
                seach.append(" order by s." + orderby + desc + " nulls last ,c.jyzcs desc,c.dfzh");
            }else if("num".equals(orderby)){
              seach.append(" order by a." + orderby + desc + ",c.dfzh,c.jyzh");
            } else{
                seach.append( " order by c." +orderby + desc);
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

    public int count(List<BankZzxxEntity> listZzxx, long aj){
        Map<String,BankTjjgsEntity> map = new HashMap();
        BankTjjgsEntity tjjgs = null;
        BankZzxxEntity zzxx = null;
        for(int i=0;i<listZzxx.size();i++) {
            zzxx = listZzxx.get(i);
                if ("出".equals(zzxx.getSfbz())) {
                    if (map.containsKey(zzxx.getYhkkh() + zzxx.getDskh())) {
                        tjjgs = map.get(zzxx.getYhkkh() + zzxx.getDskh());
                        tjjgs.setJyzcs(tjjgs.getJyzcs().add(new BigDecimal(1)));
                        tjjgs.setCzzcs(tjjgs.getCzzcs().add(new BigDecimal(1)));
                        tjjgs.setCzzje(tjjgs.getCzzje().add(zzxx.getJyje()));
                    } else {
                        BankTjjgsEntity tjs1 = new BankTjjgsEntity();
                        tjs1.setJyzh(zzxx.getYhkkh());
                        tjs1.setDfzh(zzxx.getDskh());
                        tjs1.setJyzcs(new BigDecimal(1));
                        tjs1.setCzzcs(new BigDecimal(1));
                        tjs1.setCzzje(zzxx.getJyje());
                        tjs1.setAj_id(aj);
                        map.put(zzxx.getYhkkh() + zzxx.getDskh(), tjs1);
                    }
                }
                if ( "进".equals(zzxx.getSfbz())) {
                    if (map.containsKey(zzxx.getYhkkh() + zzxx.getDskh())) {
                        tjjgs = map.get(zzxx.getYhkkh() + zzxx.getDskh());
                        tjjgs.setJyzcs(tjjgs.getJyzcs().add(new BigDecimal(1)));
                        tjjgs.setJzzcs(tjjgs.getJzzcs().add(new BigDecimal(1)));
                        tjjgs.setJzzje(tjjgs.getJzzje().add(zzxx.getJyje()));
                    } else {
                        BankTjjgsEntity tjs2 = new BankTjjgsEntity();
                        tjs2.setJyzh(zzxx.getYhkkh());
                        tjs2.setDfzh(zzxx.getDskh());
                        tjs2.setJyzcs(new BigDecimal(1));
                        tjs2.setJzzcs(new BigDecimal(1));
                        tjs2.setJzzje(zzxx.getJyje());
                        tjs2.setAj_id(aj);
                        map.put(zzxx.getYhkkh() + zzxx.getDskh(), tjs2);
                    }
            }
        }
        List<BankTjjgsEntity> listTjjgs = new ArrayList<>(map.values());
        int i =0;
        banktjsd.delAll(aj);
        banktjsd.save(listTjjgs);
        return i;
    }

    public void downloadFile(String seach, HttpServletResponse rep, String aj, String lx, HttpServletRequest req) throws Exception{
        List listTjjg = null;
        if("共同".equals(lx)){
            AjEntity aje = (AjEntity) req.getSession().getAttribute("aj");
            if(seach.contains("order by")) {
                listTjjg = banktjsd.findBySQL("select s.khxm,n.khxm dfxm,c.*,a.num from bank_tjjgs c right join (" +
                        "  select t.dfzh,count(1) as num from bank_tjjgs t " +
                        " where t.dfzh not in( select distinct t1.jyzh from bank_tjjgs t1) and t.aj_id=" +aje.getId()+
                        " group by dfzh " +
                        "  having(count(1)>=2) ) a on c.dfzh = a.dfzh" +
                        " left join bank_person s on c.jyzh = s.yhkkh " +
                        " left join bank_person n on c.dfzh = n.yhkkh "+
                        "  where a.num is not null " + seach +",c.dfzh");
            }else{
                listTjjg = banktjsd.findBySQL("select s.khxm,n.khxm dfxm,c.*,a.num from bank_tjjgs c right join (" +
                        "  select t.dfzh,count(1) as num from bank_tjjgs t " +
                        " where t.dfzh not in( select distinct t1.jyzh from bank_tjjgs t1) and t.aj_id=" +aje.getId()+
                        " group by dfzh " +
                        "  having(count(1)>=2) ) a on c.dfzh = a.dfzh" +
                        " left join bank_person s on c.jyzh = s.yhkkh " +
                        " left join bank_person n on c.dfzh = n.yhkkh "+
                        "  where a.num is not null " + seach +" order by c.dfzh");
            }

        }else {
            listTjjg = banktjsd.findBySQL("select s.khxm,c.* from bank_tjjgs c left join bank_person s on c.jyzh = s.yhkkh where 1=1 "+seach);
        }

        HSSFWorkbook wb = createExcel(listTjjg,lx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String(("银行卡"+lx+"账户信息(\""+aj+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }


    public HSSFWorkbook createExcel(List listTjjg,String lx){
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("银行卡"+lx+"账户信息");
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
            cell.setCellValue("对手姓名");
            cell = row.createCell(5);
            cell.setCellValue("共同联系人数");
            cell = row.createCell(6);
            cell.setCellValue("交易总次数");
            cell = row.createCell(7);
            cell.setCellValue("进账总次数");
            cell = row.createCell(8);
            cell.setCellValue("进账总金额(元)");
            cell = row.createCell(9);
            cell.setCellValue("出账总次数");
            cell = row.createCell(10);
            cell.setCellValue("出账总金额(元)");
            for (int a = 0; a < 12; a++) {
                sheet.autoSizeColumn(a);
            }
        }
        int b = 1;
        for(int i=0;i<listTjjg.size();i++){
            if(i>=65535&& i%65535==0){
                sheet = wb.createSheet("银行卡"+lx+"对手账户信息("+b+")");
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
                    cell.setCellValue("对手姓名");
                    cell = row.createCell(5);
                    cell.setCellValue("共同联系人数");
                    cell = row.createCell(6);
                    cell.setCellValue("交易总次数");
                    cell = row.createCell(7);
                    cell.setCellValue("进账总次数");
                    cell = row.createCell(8);
                    cell.setCellValue("进账总金额(元)");
                    cell = row.createCell(9);
                    cell.setCellValue("出账总次数");
                    cell = row.createCell(10);
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
                if (map.get("KHXM") != null && map.get("KHXM").toString().length() > 0) {
                    cell.setCellValue(map.get("KHXM").toString());
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
                if (map.get("KHXM") != null && map.get("KHXM").toString().length() > 0) {
                    cell.setCellValue(map.get("KHXM").toString());
                }
                cell = row.createCell(2);
                cell.setCellValue(map.get("JYZH").toString());
                cell = row.createCell(3);
                if (map.get("DFZH") != null && map.get("DFZH").toString().length() > 0) {
                    cell.setCellValue(map.get("DFZH").toString());
                }
                cell = row.createCell(4);
                if (map.get("DFXM") != null && map.get("DFXM").toString().length() > 0) {
                    cell.setCellValue(map.get("DFXM").toString());
                }
                cell = row.createCell(5);
                cell.setCellValue(map.get("NUM").toString());
                cell = row.createCell(6);
                cell.setCellValue(map.get("JYZCS").toString());
                cell = row.createCell(7);
                cell.setCellValue(map.get("JZZCS").toString());
                cell = row.createCell(8);
                cell.setCellValue(map.get("JZZJE").toString());
                cell = row.createCell(9);
                cell.setCellValue(map.get("CZZCS").toString());
                cell = row.createCell(10);
                cell.setCellValue(map.get("CZZJE").toString());
                if (i % 65536 == 0) {
                    for (int a = 0; a < 12; a++) {
                        sheet.autoSizeColumn(a);
                    }
                }
            }
        }
        return wb;
    }
    public void deleteByAjid(long id){
        banktjsd.delAll(id);
    }
}