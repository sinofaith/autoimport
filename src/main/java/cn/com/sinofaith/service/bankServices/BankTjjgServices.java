package cn.com.sinofaith.service.bankServices;


import cn.afterturn.easypoi.word.WordExportUtil;
import cn.afterturn.easypoi.word.entity.WordImageEntity;
import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.*;
import cn.com.sinofaith.dao.bankDao.BankTjjgDao;
import cn.com.sinofaith.dao.bankDao.BankZcxxDao;
import cn.com.sinofaith.dao.bankDao.BankZzxxDao;
import cn.com.sinofaith.form.cftForm.CftTjjgForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.util.GetBank;
import cn.com.sinofaith.util.TimeFormatUtil;
import cn.com.sinofaith.util.ZipFile;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.com.sinofaith.util.ZipFile.ZipFiles;
import static java.math.BigDecimal.ROUND_UP;

@Service
public class BankTjjgServices {

    @Autowired
    private BankTjjgDao tjjd;

    @Autowired
    private BankZcxxDao bzcd;

    @Autowired
    private BankZzxxDao bzzd;

    @Autowired
    private BankPersonServices bps;

    public CftTjjgForm getByJyzh(String zh,AjEntity aj){
       List temp = tjjd.findBySQL(" select b.*,p.khxm xm from bank_tjjg b left join bank_person p on b.jyzh = p.yhkkh " +
               " where b.jyzh='"+zh+"' and b.aj_id ="+aj.getId() );
       CftTjjgForm cf = new CftTjjgForm();
       if(temp.size()>0) {
           cf = cf.mapToForm((Map) temp.get(0));
       }
       return cf;
    }

    public Page queryForPage(int currentPage, int pageSize, String seach){
        Page page = new Page();
        //总记录数
        int allRow = tjjd.getAllRowCount(seach);
        List<CftTjjgForm> cfttjs = new ArrayList<CftTjjgForm>();
        CftTjjgForm tjjgForm = new CftTjjgForm();;
        List cftList = tjjd.getDoPage(seach,currentPage,pageSize);
        if(allRow != 0) {
            int xh = 1;
            for(int i=0;i<cftList.size();i++){
                Map map = (Map)cftList.get(i);
                tjjgForm = tjjgForm.mapToForm(map);
                tjjgForm.setId(xh+(currentPage-1)*pageSize);
                cfttjs.add(tjjgForm);
                xh++;
            }

        }
        page.setList(cfttjs);
        page.setPageNo(currentPage);
        page.setPageSize(pageSize);
        page.setTotalRecords(allRow);
        return page;
    }

    public String getSeach(String seachCondition, String seachCode, String orderby, String desc, AjEntity aj,int code,int hcode){
        StringBuffer seach = new StringBuffer(" and aj_id ="+aj.getId());
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if("jzzje".equals(seachCondition)||"czzje".equals(seachCondition)){
                seach.append( " and c."+ seachCondition + " >= "+seachCode);
            }else if("khxm".equals(seachCondition)){
                seach.append(" and s."+ seachCondition+" like "+"'"+ seachCode+"'");
            }else{
                seach.append(" and c."+ seachCondition+" like "+"'"+ seachCode +"'");
            }
        }else{
            seach.append(" and ( 1=1 ) ");
        }
        if(code!=-1) {
            seach.append(" and c.zhlx=" + code);
        }
        if(hcode!=0){
            seach.append(" and (s.khxm not like '%财付通%' and s.khxm not like '%支付宝%' and s.khxm not like '%清算%' or s.khxm is null) ");
        }
        if(orderby!=null){
            if("khxm".equals(orderby)){
                seach.append(" order by s." + orderby + desc +"  nulls last ,c.id ");
            }else{
                seach.append(" order by c." +orderby + desc + " ,c.id ");
            }
        }
        return seach.toString();
    }

    public String bankName(String khh,String dskhh){
        String temp ="";
        if("".equals(khh)){
            if(dskhh!=null&&dskhh.contains("银行")){
                temp = dskhh.substring(0,dskhh.indexOf("银行")+2);
            }else if(dskhh!=null&&dskhh.contains("信用社")){
                temp = dskhh.substring(0,dskhh.indexOf("信用社")+3);
            }else {
                temp = "";
            }
        }else{
            temp = khh;
        }
        return temp;
    }

    public int count(List<BankZzxxEntity> listZzxx, long aj){
        List<BankTjjgEntity> listTjjg = null;
        Map<String,BankTjjgEntity> map = new HashMap();
        Map<String,BankTjjgEntity> mapElse = new HashMap<>();
        BankTjjgEntity tjjg = null;
        BankZzxxEntity zzxx = null;

        List<String> bp = bps.getByFilter();

        for(int i=0;i<listZzxx.size();i++){
            zzxx = listZzxx.get(i);
                if ("出".equals(zzxx.getSfbz())) {
                    if (map.containsKey(zzxx.getYhkkh())) {
                        tjjg = map.get(zzxx.getYhkkh());
                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                        tjjg.setCzzcs(tjjg.getCzzcs().add(new BigDecimal(1)));
                        tjjg.setCzzje(tjjg.getCzzje().add(zzxx.getJyje()));
                        if(tjjg.getKhh()==null){
                            tjjg.setKhh(GetBank.getBankname(zzxx.getYhkkh()).split("·")[0]);
                        }
                    } else {
                        BankTjjgEntity tj1 = new BankTjjgEntity();
                        tj1.setJyzh(zzxx.getYhkkh());
                        tj1.setJyzcs(new BigDecimal(1));
                        tj1.setCzzcs(new BigDecimal(1));
                        tj1.setCzzje(zzxx.getJyje());
                        tj1.setAj_id(aj);
                        if(tj1.getKhh()==null){
                            tj1.setKhh(GetBank.getBankname(zzxx.getYhkkh()).split("·")[0]);
                        }
                        map.put(zzxx.getYhkkh(), tj1);
                    }
                    if (zzxx.getDskh() != null && !zzxx.getYhkkh().equals(zzxx.getDskh())) {
                        if (map.containsKey(zzxx.getDskh())) {
                            tjjg = map.get(zzxx.getDskh());
                            tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                            tjjg.setJzzcs(tjjg.getJzzcs().add(new BigDecimal(1)));
                            tjjg.setJzzje(tjjg.getJzzje().add(zzxx.getJyje()));
                            if(tjjg.getKhh()==null){
                                tjjg.setKhh(bankName(GetBank.getBankname(zzxx.getDskh()).split("·")[0],zzxx.getDskhh()));
                            }
                        } else {
                            BankTjjgEntity tj1 = new BankTjjgEntity();
                            tj1.setJyzh(zzxx.getDskh());
                            tj1.setJyzcs(new BigDecimal(1));
                            tj1.setJzzcs(new BigDecimal(1));
                            tj1.setJzzje(zzxx.getJyje());
                            tj1.setAj_id(aj);
                            if(tj1.getKhh()==null){
                                tj1.setKhh(bankName(GetBank.getBankname(zzxx.getDskh()).split("·")[0],zzxx.getDskhh()));
                            }
                            map.put(zzxx.getDskh(), tj1);
                        }

                        if(bp.contains(zzxx.getDskh())){
                            if(mapElse.containsKey(zzxx.getYhkkh())){
                                BankTjjgEntity bt = mapElse.get(zzxx.getYhkkh());
                                bt.setCzzje(bt.getCzzje().add(zzxx.getJyje()));
                            }else {
                                BankTjjgEntity bt = new BankTjjgEntity();
                                bt.setCzzje(zzxx.getJyje());
                                mapElse.put(zzxx.getYhkkh(),bt);
                            }
                        }
                    }
                }
                if ("进".equals(zzxx.getSfbz())) {
                    if (map.containsKey(zzxx.getYhkkh())) {
                        tjjg = map.get(zzxx.getYhkkh());
                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                        tjjg.setJzzcs(tjjg.getJzzcs().add(new BigDecimal(1)));
                        tjjg.setJzzje(tjjg.getJzzje().add(zzxx.getJyje()));
                        if(tjjg.getKhh()==null){
                            tjjg.setKhh(GetBank.getBankname(zzxx.getYhkkh()).split("·")[0]);
                        }
                    } else {
                        BankTjjgEntity tj2 = new BankTjjgEntity();
                        tj2.setJyzh(zzxx.getYhkkh());
                        tj2.setJyzcs(new BigDecimal(1));
                        tj2.setJzzcs(new BigDecimal(1));
                        tj2.setJzzje(zzxx.getJyje());
                        tj2.setAj_id(aj);
                        if(tj2.getKhh()==null) {
                            tj2.setKhh(GetBank.getBankname(zzxx.getYhkkh()).split("·")[0]);
                        }
                        map.put(zzxx.getYhkkh(), tj2);
                    }
                    if (zzxx.getDskh() != null && !zzxx.getYhkkh().equals(zzxx.getDskh())) {
                        if (map.containsKey(zzxx.getDskh())) {
                            tjjg = map.get(zzxx.getDskh());
                            tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                            tjjg.setCzzcs(tjjg.getCzzcs().add(new BigDecimal(1)));
                            tjjg.setCzzje(tjjg.getCzzje().add(zzxx.getJyje()));
                            if(tjjg.getKhh()==null){
                                tjjg.setKhh(bankName(GetBank.getBankname(zzxx.getDskh()).split("·")[0],zzxx.getDskhh()));
                            }
                        } else {
                            BankTjjgEntity tj2 = new BankTjjgEntity();
                            tj2.setJyzh(zzxx.getDskh());
                            tj2.setJyzcs(new BigDecimal(1));
                            tj2.setCzzcs(new BigDecimal(1));
                            tj2.setCzzje(zzxx.getJyje());
                            tj2.setAj_id(aj);
                            if(tj2.getKhh()==null) {
                                tj2.setKhh(bankName(GetBank.getBankname(zzxx.getDskh()).split("·")[0], zzxx.getDskhh()));
                            }
                            map.put(zzxx.getDskh(), tj2);
                        }

                        if(bp.contains(zzxx.getDskh())){
                            if(mapElse.containsKey(zzxx.getYhkkh())){
                                BankTjjgEntity bt = mapElse.get(zzxx.getYhkkh());
                                bt.setJzzje(bt.getJzzje().add(zzxx.getJyje()));
                            }else {
                                BankTjjgEntity bt = new BankTjjgEntity();
                                bt.setJzzje(zzxx.getJyje());
                                mapElse.put(zzxx.getYhkkh(),bt);
                            }
                        }
                    }
                }
            }
//                if (map.containsKey(zzxx.getYhkkh())) {
//                    if ("出".equals(zzxx.getSfbz())) {
//                        tjjg = map.get(zzxx.getYhkkh());
//                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
//                        tjjg.setCzzcs(tjjg.getCzzcs().add(new BigDecimal(1)));
//                        tjjg.setCzzje(tjjg.getCzzje().add(zzxx.getJyje()));
//                    }
//                    if ("进".equals(zzxx.getSfbz())) {
//                        tjjg = map.get(zzxx.getYhkkh());
//                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
//                        tjjg.setJzzcs(tjjg.getJzzcs().add(new BigDecimal(1)));
//                        tjjg.setJzzje(tjjg.getJzzje().add(zzxx.getJyje()));
//                    }
//                } else {
//                    if ("出".equals(zzxx.getSfbz())) {
//                        BankTjjgEntity tj1 = new BankTjjgEntity();
//                        tj1.setJyzh(zzxx.getYhkkh());
//                        tj1.setJyzcs(new BigDecimal(1));
//                        tj1.setCzzcs(new BigDecimal(1));
//                        tj1.setCzzje(zzxx.getJyje());
//                        tj1.setAj_id(aj);
//                        map.put(zzxx.getYhkkh(), tj1);
//                    }
//                    if ("进".equals(zzxx.getSfbz())) {
//                        BankTjjgEntity tj2 = new BankTjjgEntity();
//                        tj2.setJyzh(zzxx.getYhkkh());
//                        tj2.setJyzcs(new BigDecimal(1));
//                        tj2.setJzzcs(new BigDecimal(1));
//                        tj2.setJzzje(zzxx.getJyje());
//                        tj2.setAj_id(aj);
//                        map.put(zzxx.getYhkkh(), tj2);
//                    }
//                }
//                if(zzxx.getDskh()!=null){
//                    String a = zzxx.getDskh();
//                if (map.containsKey(a)) {
//                    if ("出".equals(zzxx.getSfbz())) {
//                        tjjg = map.get(a);
//                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
//                        tjjg.setJzzcs(tjjg.getJzzcs().add(new BigDecimal(1)));
//                        tjjg.setJzzje(tjjg.getJzzje().add(zzxx.getJyje()));
//                    }
//                    if ("进".equals(zzxx.getSfbz())) {
//                        tjjg = map.get(a);
//                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
//                        tjjg.setCzzcs(tjjg.getJzzcs().add(new BigDecimal(1)));
//                        tjjg.setCzzje(tjjg.getJzzje().add(zzxx.getJyje()));
//                    }
//                } else {
//                    if ("出".equals(zzxx.getSfbz())) {
//                        BankTjjgEntity tj1 = new BankTjjgEntity();
//                        tj1.setJyzh(a);
//                        tj1.setJyzcs(new BigDecimal(1));
//                        tj1.setJzzcs(new BigDecimal(1));
//                        tj1.setJzzje(zzxx.getJyje());
//                        tj1.setAj_id(aj);
//                        map.put(a, tj1);
//                    }
//                    if ("进".equals(zzxx.getSfbz())) {
//                        BankTjjgEntity tj2 = new BankTjjgEntity();
//                        tj2.setJyzh(a);
//                        tj2.setJyzcs(new BigDecimal(1));
//                        tj2.setCzzcs(new BigDecimal(1));
//                        tj2.setCzzje(zzxx.getJyje());
//                        tj2.setAj_id(aj);
//                        map.put(a, tj2);
//                    }
//                }
//            }
//        List<BankZcxxEntity> listZcxx =bzcd.getByAjId(" and aj_id = "+aj);
        List<String> zczh = bzzd.getYhkkhDis(aj);
        listTjjg = new ArrayList<>(map.values());
        for (int i =0;i<listTjjg.size();i++){
            BankTjjgEntity bz = listTjjg.get(i);
            if(!zczh.contains(bz.getJyzh())){
                bz.setZhlx(1);
            }else if(zczh.contains(bz.getJyzh())){
                bz.setZhlx(0);
            }
            String temp = "";
            BigDecimal temps = new BigDecimal(0);
            if(bp.contains(bz.getJyzh())) {
                temp = "第三方账户";
            }else if (bz.getCzzje().compareTo(new BigDecimal(0)) == 0) {
                temp = "汇聚账户";
            } else if (bz.getJzzje().compareTo(new BigDecimal(0)) == 0) {
                temp = "来源账户";
            } else {
                if (mapElse.containsKey(bz.getJyzh())) {
                    BankTjjgEntity btt = mapElse.get(bz.getJyzh());
                    if((bz.getCzzje().subtract(btt.getCzzje())).compareTo(new BigDecimal(0)) == 0){
                        temps = new BigDecimal(2);
                    }else if((bz.getJzzje().subtract(btt.getJzzje())).compareTo(new BigDecimal(0)) == 0){
                        temps = new BigDecimal(0.1);
                    }else{
                        temps = (bz.getJzzje().subtract(btt.getJzzje())).divide(bz.getCzzje().subtract(btt.getCzzje()), 5, ROUND_UP);
                    }
                } else {
                    temps = bz.getJzzje().divide(bz.getCzzje(), 5, ROUND_UP);
                }
                if (temps.doubleValue() <= 0.15) {
                    temp = "来源账户";
                } else if (temps.doubleValue() <= 1.35) {
                    temp = "中转账户";
                } else {
                    temp = "汇聚账户";
                }
            }
            bz.setZhlb(temp);

        }
        int i = 0;
        tjjd.delAll(aj);
        tjjd.save(listTjjg);

        return i;
    }

    public void downloadFile(String seach, HttpServletResponse rep, String aj) throws Exception{
        List listTjxx = tjjd.findBySQL("select s.khxm,c.* from bank_tjjg c left join bank_person s on c.jyzh = s.yhkkh where 1=1 "+seach);
        HSSFWorkbook wb = createExcel(listTjxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String(("银行卡账户信息(\""+aj+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public HSSFWorkbook createExcel(List listTjjg){
        HSSFWorkbook wb = new HSSFWorkbook();
        int b = 1;
        Sheet sheet = wb.createSheet("银行卡账户信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("账户类型");
        cell = row.createCell(2);
        cell.setCellValue("姓名");
        cell = row.createCell(3);
        cell.setCellValue("交易账卡号");
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
        for(int i=0;i<listTjjg.size();i++){
            if(i>=65535&& i%65535==0){
                sheet = wb.createSheet("银行卡账户信息("+b+")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("账户类型");
                cell = row.createCell(2);
                cell.setCellValue("姓名");
                cell = row.createCell(3);
                cell.setCellValue("交易账卡号");
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
                b+=1;
            }
            Map map = (Map)listTjjg.get(i);
            row = sheet.createRow(i%65535 + 1);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue("0".equals(map.get("ZHLX").toString()) ? "已调单账户" :"未调单账户");
            cell = row.createCell(2);
            if(map.get("KHXM") != null && map.get("KHXM").toString().length()>0){
                cell.setCellValue(map.get("KHXM").toString());
            }
            cell = row.createCell(3);
            cell.setCellValue(map.get("JYZH").toString());
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
            if(i%65536==0) {
                for (int a = 0; a < 10; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }


        return wb;
    }

    public void downWs(long czje,long jzje,String wstitle,String downPath,AjEntity aj,HttpServletResponse rep){
        String[] time = TimeFormatUtil.getDate("/").split("/");

        StringBuffer seach = new StringBuffer();
        seach.append(" from BankTjjgEntity where 1=1 and aj_id=").append(aj.getId());
        seach.append(" and (czzje >=").append(czje).append(" or jzzje >=").append(jzje).append(") ");
        seach.append(" and zhlb != '第三方账户'");
        List<BankTjjgEntity> list = tjjd.find(seach.toString());
        list = list.stream().filter(u -> u.getKhh()!=null).collect(Collectors.toList());
        Map<String,List<BankTjjgEntity>> map = list.stream().collect(Collectors.groupingBy(BankTjjgEntity::getKhh));

        List listNull = tjjd.findBySQL(" select c.*,p.khxm from bank_tjjg c " +
                " left join bank_person p on c.jyzh=p.yhkkh where 1=1 and aj_id="+aj.getId()+" and (c.czzje>="+czje+" or c.jzzje>="+jzje+") " +
                "and c.zhlb != '第三方账户' and c.khh is null");

        String sfzhmList= "";
        int num = 1;
        List<String> listPath = new ArrayList<String>();
        for (String key : map.keySet()) {
            List<BankTjjgEntity> r = map.get(key);
            for(int i=0;i<r.size();i++){
                sfzhmList+=r.get(i).getJyzh().trim()+"、";
                if((i+1)%5==0 || (i+1)==r.size()){
                    Map<String,Object> mapWord = new HashMap<>();
//                    WordImageEntity image = new WordImageEntity();
//                    image.setHeight((int)(551/2.6));
//                    image.setWidth((int)(1378/2.6));
//                    image.setUrl(downPath+"model/model.png");
//                    image.setType(WordImageEntity.URL);
//                    mapWord.put("wordImg", image);
                    mapWord.put("title", wstitle);
                    mapWord.put("title1", key);
                    mapWord.put("y",time[0]);
                    mapWord.put("m",time[1]);
                    mapWord.put("d",time[2].substring(0,time[2].indexOf(" ")));
                    mapWord.put("sfzhmList",sfzhmList.substring(0,sfzhmList.length()-1));
                    try {
                        String fileName = downPath+"temp/协助查询财产通知书"+num+".docx";
                        XWPFDocument doc = WordExportUtil.exportWord07(
                                downPath+"model/model.docx", mapWord);
                        FileOutputStream fos = new FileOutputStream(fileName);
                        doc.write(fos);
                        fos.flush();
                        fos.close();
                        listPath.add(fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sfzhmList="";
                    num+=1;
                }
            }
        }
        if(listNull.size()>0){
            HSSFWorkbook wb = createExcel(listNull);
            try {
                FileOutputStream fos = new FileOutputStream(downPath+"temp/未知开户行账户.xls");
                wb.write(fos);
                fos.flush();
                fos.close();
                listPath.add(downPath+"temp/未知开户行账户.xls");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        File zip = new File(downPath+"temp/协助调查文书.zip");
        File srcfile[] = new File[listPath.size()];
        for (int j = 0, n1 = listPath.size(); j < n1; j++) {
            srcfile[j] = new File(listPath.get(j));
        }
        ZipFiles(srcfile, zip);
        try {
            rep.setContentType("application/zip");
            rep.setHeader("Location",zip.getName());
            rep.setHeader("Content-Disposition", "attachment; filename=" +new String(("协助调查文书.zip").getBytes(), "ISO8859-1"));
            OutputStream outputStream = rep.getOutputStream();
            InputStream inputStream = new FileInputStream(downPath+"temp/协助调查文书.zip");
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();


            File files = new File(downPath+"temp/");
            String[] filep = files.list();
            File temps = null;
            for (int a = 0; a < filep.length; a++) {
                temps = new File(downPath+"temp/" + filep[a]);
                if (temps.isFile()) {
                    temps.delete();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
