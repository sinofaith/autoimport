package cn.com.sinofaith.service.bankServices;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.BankTjjgsEntity;
import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import cn.com.sinofaith.bean.customerProBean.PersonRelationEntity;
import cn.com.sinofaith.dao.bankDao.BankTjjgsDao;
import cn.com.sinofaith.dao.bankDao.BankZcxxDao;
import cn.com.sinofaith.dao.bankDao.BankZzxxDao;
import cn.com.sinofaith.dao.customerDao.PersonRelationDao;
import cn.com.sinofaith.form.cftForm.CftTjjgsForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.util.Excel2007Export;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Me. on 2018/5/23
 */
@Service
public class BankTjjgsService {
    @Autowired
    private BankTjjgsDao banktjsd;

    @Autowired
    private BankZcxxDao bzcd;

    @Autowired
    private BankZzxxDao bzzd;

    @Autowired
    private PersonRelationDao prd;
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
                cftForm.setDfxm((String) map.get("DFXMS"));
                cftForm.setJyzcs( new BigDecimal(map.get("JYZCS").toString()));
                cftForm.setJzzcs( new BigDecimal(map.get("JZZCS").toString()));
                cftForm.setJzzje( new BigDecimal(map.get("JZZJE").toString()));
                cftForm.setCzzcs( new BigDecimal(map.get("CZZCS").toString()));
                cftForm.setCzzje( new BigDecimal(map.get("CZZJE").toString()));
                cftForm.setZhlx(new BigDecimal(map.get("ZHLX").toString()).longValue());
                cftForm.setDsfzh(new BigDecimal(map.get("DSFZH")!=null ? map.get("DSFZH").toString():"-1").longValue());
                cftForm.setMinsj((String) map.get("MINSJ"));
                cftForm.setMaxsj((String) map.get("MAXSJ"));
                cftForm.setJgsj(TimeFormatUtil.sjjg(cftForm.getMaxsj(),cftForm.getMinsj()));
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
        List cftList = banktjsd.getDoPageGt(seach,currentPage,pageSize,ajid,true);
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
                cftForm.setDsfzh(new BigDecimal(map.get("DSFZH")!=null ? map.get("DSFZH").toString():"-1").longValue());
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

    public String getSeach(String seachCondition, String seachCode, String orderby, String desc, AjEntity aj,String code,int hcode){
        StringBuffer seach = new StringBuffer(" and aj_id ="+aj.getId());
        if(seachCode!=null){
            seachCode=seachCode.replace("\r\n","")
                    .replace("，","").replace(" ","")
                    .replace(" ","").replace("\t","");
            if("jzzje".equals(seachCondition)||"czzje".equals(seachCondition)){
                 seach.append(" and c."+ seachCondition + seachCode.replace("大于等于",">=")
                         .replace("等于","=").replace("小于等于","<=")
                         .replace("大于",">").replace("小于","<"));
            }else if("khxm".equals(seachCondition)){
                seach.append(" and s."+ seachCondition+" like '%"+ seachCode+"%'");
            }else if("dsxm".equals(seachCondition)){
                seach.append(" and d.khxm like '%"+ seachCode+"%'");
            }
            else{
                seach.append(" and c."+ seachCondition+" like '%"+ seachCode +"%'");
            }
        }else{
            seach.append(" and ( 1=1 ) ");
        }
        seach.append(" and length(c.dfzh)>1 ");
        if(!code.equals("-99")) {
           String temp[] = code.split(",");
            seach.append(" and (");
            for(int i =0; i<temp.length;i++){
                seach.append(" c.zhlx=" + temp[i]);
                if(i!=temp.length-1){
                    seach.append(" or ");
                }
            }
            seach.append(" ) ");
        }
        if(hcode!=0){
//            seach.append(" and (d.khxm not like '%财付通%' and d.khxm not like '%支付%' " +
//                         " and d.khxm not like '%清算%' and d.khxm not like '%特约%' " +
//                         " and d.khxm not like '%备付金%' and d.khxm not like '%银行%' " +
//                         " and d.khxm not like '%银联%' and d.khxm not like '%保险%' " +
//                         " and d.khxm not like '%过渡%' or d.khxm is null) ");
            seach.append(" and (d.dsfzh = 0 or d.dsfzh is null)");
        }
        if(orderby!=null){
            if("khxm".equals(orderby)){
                seach.append(" order by s." + orderby + desc + " nulls last ,c.jyzcs desc,c.dfzh");
            }else if("num".equals(orderby)){
              seach.append(" order by a." + orderby + desc + ",c.dfzh,c.jyzh");
            }else if("khxms".equals(orderby)){
                seach.append(" order by d.khxm " + desc +  " nulls last ,c.jyzcs desc,c.dfzh ");
            }else{
                seach.append( " order by c." +orderby + desc+",c.id");
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

    public CftTjjgsForm getJczzByName(String name,String dsxm,long ajid){
        CftTjjgsForm cf = new CftTjjgsForm();
        List list = banktjsd.findBySQL("select bp.khxm as name,pbp.khxm as dfxm,sum(bt.jzzje) jzzje,sum(bt.czzje) czzje from bank_tjjgs bt " +
                "left join bank_person bp on bt.jyzh = bp.yhkkh " +
                "left join bank_person pbp on bt.dfzh = pbp.yhkkh " +
                "where bp.khxm= '"+name+"' and pbp.khxm= '"+dsxm+"' and bt.aj_id = "+ajid+" group by bp.khxm,pbp.khxm");
        Map map = (Map) list.get(0);
        cf.setName(map.get("NAME").toString());
        cf.setDfxm(map.get("DFXM").toString());
        cf.setJzzje(new BigDecimal(map.get("JZZJE").toString()));
        cf.setCzzje(new BigDecimal(map.get("CZZJE").toString()));
        return cf;
    }

    public int count(List<BankZzxxEntity> listZzxx, long aj){
        Map<String,BankTjjgsEntity> map = new HashMap();
        BankTjjgsEntity tjjgs = null;
        BankZzxxEntity zzxx = null;
        for(int i=0;i<listZzxx.size();i++) {
            zzxx = listZzxx.get(i);
                if ("出".equals(zzxx.getSfbz())) {
                    String temp = zzxx.getYhkkh()+zzxx.getDskh();
                    if(zzxx.getDskh() == null || "".equals(zzxx.getDskh().trim())){
                        temp = zzxx.getYhkkh()+zzxx.getBcsm();
                    }
                    if (map.containsKey(temp)) {
                        tjjgs = map.get(temp);
                        tjjgs.setJyzcs(tjjgs.getJyzcs().add(new BigDecimal(1)));
                        tjjgs.setCzzcs(tjjgs.getCzzcs().add(new BigDecimal(1)));
                        tjjgs.setCzzje(tjjgs.getCzzje().add(zzxx.getJyje()));
                        if(TimeFormatUtil.DateFormat(zzxx.getJysj())!=null){
                            if(TimeFormatUtil.DateFormat(zzxx.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjgs.getMinsj()))==-1){
                                tjjgs.setMinsj(zzxx.getJysj());
                            }else if(TimeFormatUtil.DateFormat(zzxx.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjgs.getMaxsj()))==1){
                                tjjgs.setMaxsj(zzxx.getJysj());
                            }
                        }
                    } else {
                        BankTjjgsEntity tjs1 = new BankTjjgsEntity();
                        tjs1.setJyzh(zzxx.getYhkkh());
                        tjs1.setDfzh(zzxx.getDskh());
                        if(zzxx.getDskh() == null || "".equals(zzxx.getDskh().trim())){
                            tjs1.setDfzh(zzxx.getBcsm());
                            tjs1.setZhlx(2);
                        }
                        tjs1.setJyzcs(new BigDecimal(1));
                        tjs1.setCzzcs(new BigDecimal(1));
                        tjs1.setCzzje(zzxx.getJyje());
                        tjs1.setAj_id(aj);
                        if(TimeFormatUtil.DateFormat(zzxx.getJysj())!=null){
                            tjs1.setMinsj(zzxx.getJysj());
                            tjs1.setMaxsj(zzxx.getJysj());
                        }
                        map.put(temp, tjs1);
                    }
                }
                if ("进".equals(zzxx.getSfbz())) {
                    String temp = zzxx.getYhkkh()+zzxx.getDskh();
                    if(zzxx.getDskh() == null || "".equals(zzxx.getDskh().trim())){
                        temp = zzxx.getYhkkh()+zzxx.getBcsm();
                    }
                    if (map.containsKey(temp)) {
                        tjjgs = map.get(temp);
                        tjjgs.setJyzcs(tjjgs.getJyzcs().add(new BigDecimal(1)));
                        tjjgs.setJzzcs(tjjgs.getJzzcs().add(new BigDecimal(1)));
                        tjjgs.setJzzje(tjjgs.getJzzje().add(zzxx.getJyje()));
                        if(TimeFormatUtil.DateFormat(zzxx.getJysj())!=null){
                            if(TimeFormatUtil.DateFormat(zzxx.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjgs.getMinsj()))==-1){
                                tjjgs.setMinsj(zzxx.getJysj());
                            }else if(TimeFormatUtil.DateFormat(zzxx.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjgs.getMaxsj()))==1){
                                tjjgs.setMaxsj(zzxx.getJysj());
                            }
                        }
                    } else {
                        BankTjjgsEntity tjs2 = new BankTjjgsEntity();
                        tjs2.setJyzh(zzxx.getYhkkh());
                        tjs2.setDfzh(zzxx.getDskh());
                        if(zzxx.getDskh() == null || "".equals(zzxx.getDskh().trim())){
                            tjs2.setDfzh(zzxx.getBcsm());
                            tjs2.setZhlx(2);
                        }
                        tjs2.setJyzcs(new BigDecimal(1));
                        tjs2.setJzzcs(new BigDecimal(1));
                        tjs2.setJzzje(zzxx.getJyje());
                        tjs2.setAj_id(aj);

                        if(TimeFormatUtil.DateFormat(zzxx.getJysj())!=null){
                            tjs2.setMinsj(zzxx.getJysj());
                            tjs2.setMaxsj(zzxx.getJysj());
                        }
                        map.put(temp, tjs2);
                    }
            }
        }
//        List<BankZcxxEntity> listZcxx =bzcd.getByAjId(" and aj_id = "+aj);
        Set<String> zczh = bzzd.getYhkkhDis(aj);
        List<BankTjjgsEntity> listTjjgs = new ArrayList<>(map.values());
        for (int i =0;i<listTjjgs.size();i++){
            BankTjjgsEntity bz = listTjjgs.get(i);
            if(!zczh.contains(bz.getDfzh())&&bz.getZhlx()!=2){
                bz.setZhlx(1);
            }else if(zczh.contains(bz.getDfzh())&&bz.getZhlx()!=2){
                bz.setZhlx(0);
            }
        }
        int i =0;
        banktjsd.delAll(aj);
        banktjsd.save(listTjjgs);
        List list = banktjsd.getGroup(aj);
        for(int j=0;j<list.size();j++){
            Map map1 = (Map) list.get(j);
            PersonRelationEntity pr = new PersonRelationEntity();
            pr.setAj_id(aj);
            pr.setRelationName("资金关联");
            pr.setPname(map1.get("PNAME").toString());
            pr.setName(map1.get("NAME").toString());
            if(Double.valueOf(map1.get("JZZJE").toString())>Double.valueOf(map1.get("CZZJE").toString())){
                pr.setRelationShow("下家");
            }else{
                pr.setRelationShow("上家");
            }
//            pr.getRelationShow();
            prd.save(pr);
        }
        return i;
    }

    public void downloadFile(String seach, HttpServletResponse rep, String aj, String lx, HttpServletRequest req) throws Exception{
        List listTjjg = null;
        if("共同".equals(lx)){
            AjEntity aje = (AjEntity) req.getSession().getAttribute("aj");
            if(seach.contains("order by")) {
                listTjjg = banktjsd.findBySQL("select s.khxm,d.khxm dfxm,c.*,a.num from bank_tjjgs c right join (" +
                        "  select t.dfzh,count(1) as num from bank_tjjgs t " +
                        " where t.dfzh not in( select distinct t1.jyzh from bank_tjjgs t1) and t.aj_id=" +aje.getId()+
                        " group by dfzh " +
                        "  having(count(1)>=2) ) a on c.dfzh = a.dfzh" +
                        " left join bank_person s on c.jyzh = s.yhkkh " +
                        " left join bank_person d on c.dfzh = d.yhkkh "+
                        "  where a.num is not null " + seach +",c.dfzh");
            }else{
                listTjjg = banktjsd.findBySQL("select s.khxm,d.khxm dfxm,c.*,a.num from bank_tjjgs c right join (" +
                        "  select t.dfzh,count(1) as num from bank_tjjgs t " +
                        " where t.dfzh not in( select distinct t1.jyzh from bank_tjjgs t1) and t.aj_id=" +aje.getId()+
                        " group by dfzh " +
                        "  having(count(1)>=2) ) a on c.dfzh = a.dfzh" +
                        " left join bank_person s on c.jyzh = s.yhkkh " +
                        " left join bank_person d on c.dfzh = d.yhkkh "+
                        "  where a.num is not null " + seach +" order by c.dfzh");
            }

        }else {
            listTjjg = banktjsd.findBySQL("select s.khxm,c.*,d.khxm dfxm from bank_tjjgs c left join bank_person s on c.jyzh = s.yhkkh" +
                    " left join bank_person d on c.dfzh = d.yhkkh  where 1=1 "+seach);
        }

        XSSFWorkbook wb = createExcel(listTjjg,lx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+
                new String(("银行卡"+lx+"账户信息(\""+aj+").xlsx").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }


    public XSSFWorkbook createExcel(List listTjjg, String lx){
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("银行卡"+lx+"账户信息");
        String[] title = {"序号","姓名","交易卡号","对手卡号","对手姓名","交易总次数","进账总次数","进账总金额",
                "出账总次数","出账总金额","最早交易时间","最晚交易时间","时间间隔"};
        String[] title1 = {"序号","姓名","交易卡号","对手卡号","对手姓名","共同联系人数",
                "交易总次数","进账总次数","进账总金额", "出账总次数","出账总金额"};
        Row row ;
        Cell cell ;
        if(!"共同".equals(lx)) {
            row = Excel2007Export.createRow(sheet,title);
            for (int a = 0; a < 11; a++) {
                sheet.autoSizeColumn(a);
            }
        }else{
            row = Excel2007Export.createRow(sheet,title1);
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
                    row = Excel2007Export.createRow(sheet,title);
                    b += 1;
                }else{
                    row = Excel2007Export.createRow(sheet,title);
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
                if (map.get("DFXM") != null && map.get("DFXM").toString().length() > 0) {
                    cell.setCellValue(map.get("DFXM").toString());
                }
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
                cell = row.createCell(10);
                cell.setCellValue((String)map.get("MINSJ"));
                cell = row.createCell(11);
                cell.setCellValue((String)map.get("MAXSJ"));
                cell = row.createCell(12);
                cell.setCellValue(TimeFormatUtil.sjjg((String)map.get("MAXSJ").toString(),(String) map.get("MINSJ")));
                if (i % 65536 == 0) {
                    for (int a = 0; a < 11; a++) {
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

    /**
     * 导出账户点对点统计信息数据
     * @param search
     */
    public List getbankTjjgsAll(String search) {
        return banktjsd.findBySQL("select s.khxm,c.*,d.khxm dfxm from bank_tjjgs c left join bank_person s on c.jyzh = s.yhkkh" +
                " left join bank_person d on c.dfzh = d.yhkkh  where 1=1 "+search);
    }

    /**
     * 导出账户点对点统计信息数据
     * @param search
     * @param aje
     * @return
     */
    public List getbankGtzhAll(String search, AjEntity aje) {
        return banktjsd.findBySQL("select s.khxm,d.khxm dfxm,c.*,a.num from bank_tjjgs c right join (" +
                "  select t.dfzh,count(1) as num from bank_tjjgs t " +
                " where t.dfzh not in( select distinct t1.jyzh from bank_tjjgs t1) and t.aj_id=" +aje.getId()+
                " group by dfzh " +
                "  having(count(1)>=2) ) a on c.dfzh = a.dfzh" +
                " left join bank_person s on c.jyzh = s.yhkkh " +
                " left join bank_person d on c.dfzh = d.yhkkh "+
                "  where a.num is not null " + search +",c.dfzh");
    }
    /**
     * 删除文件
     * @param uploadPathd
     */
    public void deleteFile(File uploadPathd){
        if(uploadPathd.exists()){
            for(File file : uploadPathd.listFiles()){
                if(file.isFile()){
                    file.delete();
                }else{
                    deleteFile(file);
                }
            }
        }
        uploadPathd.delete();
    }
}