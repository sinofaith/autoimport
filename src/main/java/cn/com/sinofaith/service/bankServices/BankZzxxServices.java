package cn.com.sinofaith.service.bankServices;


import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.*;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.bankDao.*;
import cn.com.sinofaith.form.bankForm.BankZzxxForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.math.BigDecimal.ROUND_UP;

@Service
public class BankZzxxServices {
    @Autowired
    private BankPersonServices bps;
    @Autowired
    private BankZzxxDao bankzzd;
    @Autowired
    private BankZcxxDao bzcd;
    @Autowired
    private BankPersonDao bpd;
    @Autowired
    private BankTjjgDao tjd;
    @Autowired
    private BankTjjgsDao tjsd;
    @Autowired
    private AJDao ad;
    @Autowired
    private MappingBankzzxxDao mbd;

    public String getSeach(BankZzSeachEntity seachE, String orderby, String desc, AjEntity aj, long userId){
        String ajid = getAjidByAjm(aj,userId);
        StringBuffer seach = new StringBuffer(" and c.aj_id in ("+ajid+") ");
        Map<String,String> seachMap = new HashMap<>();
        if(seachE!=null){
            seachMap = seachE.objToMap(seachE);
            for(String entry : seachMap.keySet()) {
                if (!"".equals(seachMap.get(entry))) {
                    if ("khxm".equals(entry)) {
                        seach.append(" and (s." + entry + " like '%" + seachMap.get(entry) + "%' or c.jyxm like '%" + seachMap.get(entry) + "%')");
                    } else if ("dskh".equals(entry) || "dsxm".equals(entry)) {
                        List<String> list = Arrays.asList(seachMap.get(entry).split(","));
                        seach.append("and ( ");
                        for (int i = 0; i < list.size(); i++) {
                            seach.append("  c." + entry + " like " + "'%" + list.get(i) + "%' ");
                            if (i != list.size() - 1) {
                                seach.append(" or ");
                            }
                        }
                        seach.append(" ) ");
                    } else {
                        seach.append(" and c." + entry + " like " + "'%" + seachMap.get(entry) + "%'");
                    }
                }
            }

        }else{
            seach.append(" and ( 1=1 ) ");
        }
        if(orderby != null){
            seach .append(" order by c."+orderby).append(desc).append(",c.id");
        }
        return seach.toString();
    }

    public String getAjidByAjm(AjEntity aj,long userId){
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

    public void doloadFilezz(String seach,HttpServletResponse rep,String aj) throws  Exception{
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT  s.khxm jyxms,d.khxm dfxms,c.* ");
        sql.append("FROM  bank_zzxx c left join bank_person s on c.yhkkh=s.yhkkh ");
        sql.append("left join bank_person d on c.dskh=d.yhkkh  where 1=1 " + seach );
        List listZzxx = bankzzd.findBySQL(sql.toString());
        XSSFWorkbook wb = createExcel(listZzxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String(("银行卡交易明细(\""+aj+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public void downloadFile(String seach, HttpServletResponse rep, String aj) throws Exception{
        String order = "";
        if(seach.contains("order")){
            seach.substring(seach.indexOf("order")-1,seach.length());
            seach = seach.replace(seach.substring(seach.indexOf("order")-1,seach.length())," ");
        }
        StringBuffer sql = new StringBuffer();
        sql.append("  select s.khxm jyxms,s.khxm dfxms,c.*  from(  ");
        sql.append("  select c.*,row_number() over(partition by c.yhkkh,c.jysj,c.jyje,c.jyye,c.sfbz,c.dskh order by c.jysj ) su from bank_zzxx c  ");
        sql.append("  where 1=1"+seach+" ) c ");
        sql.append("  left join bank_person s on c.yhkkh = s.yhkkh ");
        sql.append("   left join bank_person d on c.dskh = d.yhkkh ");
        sql.append(" where su=1 "+order);
        List listZzxx = bankzzd.findBySQL(sql.toString());
        XSSFWorkbook wb = createExcel(listZzxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String(("银行卡交易明细(\""+aj+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public XSSFWorkbook createExcel(List listZzxx)throws Exception{
        XSSFWorkbook wb = new XSSFWorkbook();
        Cell cell = null;
        int b = 1;
        Sheet sheet = wb.createSheet("银行卡交易明细");
        String[] title = {"序号","交易卡号","交易户名","交易时间","交易金额","交易余额","收付标志","对手卡号"
                ,"对手户名","摘要说明","交易发生地","交易网点名称","备注"};
        Row row = Excel2007Export.createRow(sheet,title);
        for (int i = 0; i<listZzxx.size(); i++) {
            if(i>=65535&& i%65535==0){
                sheet = wb.createSheet("银行卡交易明细("+b+")");
                row = Excel2007Export.createRow(sheet,title);
                b+=1;
            }
            Map map = (Map) listZzxx.get(i);
            BankZzxxForm zzf = new BankZzxxForm();
            zzf = zzf.mapToForm(map);
            row = sheet.createRow(i%65535 + 1);
            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell = row.createCell(1);
            cell.setCellValue(zzf.getYhkkh());
            cell = row.createCell(2);
            cell.setCellValue(zzf.getJyxm());
            cell = row.createCell(3);
            cell.setCellValue(zzf.getJysj());
            cell = row.createCell(4);
            cell.setCellValue(zzf.getJyje().toString());
            cell = row.createCell(5);
            cell.setCellValue("-1".equals(zzf.getJyye().toString()) ? "" : zzf.getJyye().toString());
            cell = row.createCell(6);
            cell.setCellValue(zzf.getSfbz());
            cell = row.createCell(7);
            cell.setCellValue(zzf.getDskh());
            cell = row.createCell(8);
            cell.setCellValue(zzf.getDsxm());
            cell = row.createCell(9);
            cell.setCellValue(zzf.getZysm());
            cell = row.createCell(10);
            cell.setCellValue(zzf.getJyfsd());
            cell = row.createCell(11);
            cell.setCellValue(zzf.getJywdmc());
            cell = row.createCell(12);
            cell.setCellValue(zzf.getBz());

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
        cell.setCellValue("交易卡号");
        cell = row.createCell(2);
        cell.setCellValue("交易户名");
        cell = row.createCell(3);
        cell.setCellValue("交易时间");
        cell = row.createCell(4);
        cell.setCellValue("交易金额(元)");
        cell = row.createCell(5);
        cell.setCellValue("交易余额(元)");
        cell = row.createCell(6);
        cell.setCellValue("收付标志");
        cell = row.createCell(7);
        cell.setCellValue("对手卡号");
        cell = row.createCell(8);
        cell.setCellValue("对手户名");
        cell = row.createCell(9);
        cell.setCellValue("摘要说明");
        cell = row.createCell(10);
        cell.setCellValue("交易发生地");
        cell = row.createCell(11);
        cell.setCellValue("交易网点名称");
        cell = row.createCell(12);
        cell.setCellValue("备注");
        return row;
    }

    public String getByYhkkh(String zh,String jylx,String type,String sum,String zhlx,AjEntity aj,int page,String order,long userId){
        Page pages = new Page();
        Gson gson = new GsonBuilder().serializeNulls().create();
        String ajid = getAjidByAjm(aj,userId);

        String seach ="";
        if("tjjg".equals(type)){
            if("0".equals(zhlx)){
                seach="and c.sfbz is not null and (c.yhkkh='"+zh+"') ";
            }else{
                seach="and c.sfbz is not null and (c.dskh='"+zh+"') ";
            }
        }else{
            seach=" and (c.yhkkh='"+zh+"') ";
            seach+=" and (c.dskh = '"+jylx+"' or c.bcsm = '"+jylx+"') ";
        }
        if(aj.getZjminsj().length()>1){
            seach+=" and c.jysj >= '"+aj.getZjminsj()+"' ";
        }
        if(aj.getZjmaxsj().length()>1){
            seach+=" and c.jysj <= '"+aj.getZjmaxsj()+"' ";
        }
//        if(aj.getFlg()==1){
//            seach +=" and c.shmc not like'%红包%'";
//        }
        seach += "and c.aj_id in("+ajid+") "+order;


//        int allRow = bankzzd.getCount(seach);
        int allRow = Integer.parseInt(sum);
        List zzList = bankzzd.getDoPageDis(seach,page,100);



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

    public Map<String,Map<String,List<String>>> readExcel(String uploadPath) {
        Map<String,Map<String,List<String>>> excelMap = new HashMap<>();
        Map<String,List<String>> sheetMap = new HashMap<>();
        // 读取
        List<String> listPath = getFileList(uploadPath);
        String excelName = null;
        for (String path : listPath) {
            excelName = path.substring(path.lastIndexOf(File.separator)+1);
            if(path.endsWith(".xlsx")){
                sheetMap = MappingUtils.getBy2007Excel(path);
            }else if(path.endsWith(".xls")){
                sheetMap = MappingUtils.getBy2003Excel(path);
            }
            // 将单个excel表数据放入map中
            excelMap.put(excelName,sheetMap);
        }
        return excelMap;
    }

    private List<String> getFileList(String uploadPath) {
        List<String> listPath = new ArrayList<>();
        File dir = new File(uploadPath);
        File[] files = dir.listFiles();
        for (File file : files) {
            listPath.add(file.getAbsolutePath());
        }
        return listPath;
    }

    /**
     * bank_zzxx表数据读取
     * @param listPath
     * @param fields
     * @return
     */
    public int getBankZzxxAll(List<String> listPath, List<List<String>> fields,
                              AjEntity aj) {
        // 读取
        List<BankZzxxEntity> bankZzxxList = null;
        List<BankZzxxEntity> bankZzxxLists = new ArrayList<>();
        for(String path : listPath){
            String excelName = path.substring(path.lastIndexOf(File.separator)+1);
            for(List<String> field : fields){
                if((field.get(0).equals(excelName) && field.get(2).equals("bank_zzxx"))||(fields.size()==1)){
                    if(path.endsWith(".xlsx")){
                        bankZzxxList = getBy2007ExcelAll(path,excelName,fields);
                    }else if(path.endsWith(".xls")){
                        bankZzxxList = getBy2003ExcelAll(path,excelName,fields);
                    }
                    bankZzxxLists.addAll(bankZzxxList);
                    break;
                }
            }
        }
        int num = bankzzd.insertZzxx(bankZzxxLists, aj.getId());
        List<MappingBankzzxxEntity> listmbs = new ArrayList<>();
        for(List<String> field : fields){
            MappingBankzzxxEntity mb = new MappingBankzzxxEntity();
            mb = mb.listToObj(field);
            if(!listmbs.contains(mb)){
                listmbs.add(mb);
            }
        }
        mbd.save(listmbs);
        List<BankPersonEntity> allbp = bpd.find("from BankPersonEntity");
        Map<String, String> allBp = new HashMap<>();
        for (int j = 0; j < allbp.size(); j++) {
            allBp.put((allbp.get(j).getYhkkh()).replace("null", ""), null);
        }
        Map<String,BankPersonEntity> mapZ= new HashMap<>();
        for (int g = 0; g < bankZzxxLists.size(); g++) {
            BankPersonEntity dsbp = new BankPersonEntity();
            dsbp.setYhkkh(bankZzxxLists.get(g).getDskh());
            dsbp.setYhkzh(String.valueOf(aj.getId()));
            dsbp.setXm(bankZzxxLists.get(g).getDsxm());

            BankPersonEntity jybp = new BankPersonEntity();
            jybp.setYhkkh(bankZzxxLists.get(g).getYhkkh());
            jybp.setYhkzh(String.valueOf(aj.getId()));
            jybp.setXm(bankZzxxLists.get(g).getJyxm());
//                bp.setKhh(bankName(GetBank.getBankname(bp.getYhkkh()).split("·")[0], listZzxx.get(g).getDskhh()));

            if (dsbp.getYhkkh()!=null && dsbp.getXm()!=null&&dsbp.getYhkkh().length() > 0 && dsbp.getXm().length() > 0) {
                if (dsbp.getXm().contains("支付宝")) {
                    dsbp.setXm("支付宝（中国）网络技术有限公司");
                } else if (dsbp.getXm().contains("微信") || dsbp.getXm().contains("财付通")) {
                    dsbp.setXm("财付通支付科技有限公司");
                }
                mapZ.put((dsbp.getYhkkh()).replace("null", ""), dsbp);
            }else if(jybp.getYhkkh()!=null && jybp.getXm()!=null&&jybp.getYhkkh().length() > 0 && jybp.getXm().length() > 0){
                if (jybp.getXm().contains("支付宝")) {
                    jybp.setXm("支付宝（中国）网络技术有限公司");
                } else if (jybp.getXm().contains("微信") || jybp.getXm().contains("财付通")) {
                    jybp.setXm("财付通支付科技有限公司");
                }
                mapZ.put((jybp.getYhkkh()).replace("null", ""), jybp);
            }
            else {
                continue;
            }
        }
        List<String> str = new ArrayList<>();
        for (String o : mapZ.keySet()) {
            if (allBp.containsKey(new BankPersonEntity().remove_(o))) {
                str.add(o);
            }
        }
        for (int s = 0; s < str.size(); s++) {
            mapZ.remove(str.get(s));
        }
        allbp = new ArrayList<>(mapZ.values());
        num += bpd.add(allbp, String.valueOf(aj.getId()));
        return num;
    }

    /**
     * 03版数据读取
     * @param path
     * @param excelName
     * @param fields
     * @return
     */
    private List<BankZzxxEntity> getBy2003ExcelAll(String path, String excelName, List<List<String>> fields) {
        List<BankZzxxEntity> zzxxList = new ArrayList<>();
        InputStream is = null;
        Map<String,Integer> title = new HashMap<>();
        try {
            is = new FileInputStream(path);
            HSSFWorkbook wb = new HSSFWorkbook(is);
            for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                HSSFSheet sheet = wb.getSheetAt(numSheet);
                for (List<String> excel : fields) {
                    if (sheet == null) {
                        continue;
                    } else if (excelName.equals(excel.get(0)) && sheet.getSheetName().equals(excel.get(1))) {
                        boolean temp = true;
                        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                            HSSFRow row = sheet.getRow(rowNum);
                            if(row!=null){
                                int cellNum = row.getLastCellNum();
                                // 字段长度小于3跳出本次循环
                                if(cellNum<3){
                                    continue;
                                }
                                if (temp) {
                                    for (int i = 0; i < cellNum; i++) {
                                        String cellName = row.getCell(i).getStringCellValue();
                                        for (int j = 3; j < excel.size(); j++) {
                                            if (cellName.equals(excel.get(j))) {
                                                title.put(excel.get(j), i);
                                            }
                                        }
                                    }
                                    temp = false;
                                } else {
                                    BankZzxxEntity zzxx = RowToEntity(row, excel, title);
                                    if (zzxx != null) {
                                        zzxxList.add(zzxx);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println(excelName.substring(13,excelName.length()));
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(excelName.substring(13,excelName.length())+"-------------");
        }finally {
            try {
                if(is!=null) {
                    is.close();
                    new File(path).delete();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 删除文件
        new File(path).delete();
        return zzxxList;
    }

    public int getAllBysj(long aj_id,String minsj,String maxsj){
        String sql = "";
        if(minsj.length()>1){
            sql += " and jysj > '"+minsj+"'";
        }
        if(maxsj.length()>1){
            sql+=" and jysj < '"+maxsj+"'";
        }
        List zz = bankzzd.findBySQL(" select to_char(count(1)) num from bank_zzxx where aj_id =  "+aj_id+sql);
        Map map = (Map) zz.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public void countTjjgAndTjjgs(long ajid,List<BankZcxxEntity> listZcxx,String seach){
        List list = bankzzd.findBySQL("select to_char(count(1)) as num from bank_zzxx where aj_id ="+ajid+seach);
        List<BankZzxxEntity> listZzxx = new ArrayList<>();

        List<Map> listTjjg = new ArrayList<>();
        //账户统计结果
        listTjjg.add(new HashMap());
        //点对点统计结果
        listTjjg.add(new HashMap());
        //保存已经计算的实体哈希值
        listTjjg.add(new HashMap());
        Map map = (Map) list.get(0);
        double sum = Double.parseDouble(map.get("NUM").toString());
        Set<String> zczh = bankzzd.getYhkkhDis(ajid);
        if(sum>300000){
            for (int i =1;i<=((int)Math.ceil(sum/300000));i++){
                listZzxx = bankzzd.doPage("from BankZzxxEntity where aj_id ="+ajid+seach,i,300000);
                listTjjg = count(listZzxx,ajid,listTjjg,listZcxx,zczh);
            }
        }else{
            listZzxx = bankzzd.getAlla(ajid,seach);
            listTjjg=count(listZzxx,ajid,listTjjg,listZcxx,zczh);
        }

        if(listZcxx.size()>0) {
            listZcxx.forEach(zcxx -> {
                if (zczh.contains(zcxx.getYhkkh())) {
                    zcxx.setZhlx(0);
                }
            });
            bzcd.saveZcxx(listZcxx, ajid);
        }
        List<String> bp = bps.getByFilter();
        List<BankTjjgEntity> tjjg = new ArrayList<>(listTjjg.get(0).values());
        for (int i =0;i<tjjg.size();i++){
            BankTjjgEntity bz = tjjg.get(i);
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
            }else {
                temps = bz.getJzzje().divide(bz.getCzzje(), 5, ROUND_UP);
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
        tjd.delAll(ajid);
        tjd.save(tjjg);
        List<BankTjjgsEntity> tjjgs = new ArrayList<>(listTjjg.get(1).values());
        for (int i =0;i<tjjgs.size();i++){
            BankTjjgsEntity bz = tjjgs.get(i);
            if(!zczh.contains(bz.getDfzh())&&bz.getZhlx()!=2){
                bz.setZhlx(1);
            }else if(zczh.contains(bz.getDfzh())&&bz.getZhlx()!=2){
                bz.setZhlx(0);
            }
        }
        tjsd.delAll(ajid);
        tjsd.save(tjjgs);
    }
    public List<Map> count(List<BankZzxxEntity> listZzxx,long aj,List<Map> list,List<BankZcxxEntity> listZcxx,Set<String> zczh){
        Map<String,BankTjjgEntity> mapTjjg = list.get(0);
        Map<String,BankTjjgsEntity> mapTjjgs = list.get(1);
        Map<String,BankZzxxEntity> listHash = list.get(2);
        BankZzxxEntity zzxx = null;
        BankTjjgEntity tjjg = null;
        BankTjjgsEntity tjjgs = null;

        for(int i=0;i<listZzxx.size();i++){
            zzxx = listZzxx.get(i);
            if(listHash.containsKey(zzxx.getHash(zzxx))){
                continue;
            }
            if ("出".equals(zzxx.getSfbz())) {
                if (mapTjjg.containsKey(zzxx.getYhkkh())) {
                    tjjg = mapTjjg.get(zzxx.getYhkkh());
                    tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                    tjjg.setCzzcs(tjjg.getCzzcs().add(new BigDecimal(1)));
                    tjjg.setCzzje(tjjg.getCzzje().add(zzxx.getJyje()));
                    if(TimeFormatUtil.DateFormat(zzxx.getJysj())!=null){
                        if(TimeFormatUtil.DateFormat(zzxx.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjg.getMinsj()))==-1){
                            tjjg.setMinsj(zzxx.getJysj());
                        }else if(TimeFormatUtil.DateFormat(zzxx.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjg.getMaxsj()))==1){
                            tjjg.setMaxsj(zzxx.getJysj());
                        }
                    }
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
                    if (tj1.getKhh() == null) {
                        tj1.setKhh(GetBank.getBankname(zzxx.getYhkkh()).split("·")[0]);
                    }
                    if (TimeFormatUtil.DateFormat(zzxx.getJysj()) != null) {
                        tj1.setMinsj(zzxx.getJysj());
                        tj1.setMaxsj(zzxx.getJysj());
                    }
                    mapTjjg.put(zzxx.getYhkkh(), tj1);
                }
                if (zzxx.getDskh() != null && !zzxx.getYhkkh().equals(zzxx.getDskh()) && !zczh.contains(zzxx.getDskh())) {
                    if (mapTjjg.containsKey(zzxx.getDskh())) {
                        tjjg = mapTjjg.get(zzxx.getDskh());
                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                        tjjg.setJzzcs(tjjg.getJzzcs().add(new BigDecimal(1)));
                        tjjg.setJzzje(tjjg.getJzzje().add(zzxx.getJyje()));
                        if(TimeFormatUtil.DateFormat(zzxx.getJysj())!=null){
                            if(TimeFormatUtil.DateFormat(zzxx.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjg.getMinsj()))==-1){
                                tjjg.setMinsj(zzxx.getJysj());
                            }else if(TimeFormatUtil.DateFormat(zzxx.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjg.getMaxsj()))==1){
                                tjjg.setMaxsj(zzxx.getJysj());
                            }
                        }
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
                        if(TimeFormatUtil.DateFormat(zzxx.getJysj())!=null){
                            tj1.setMinsj(zzxx.getJysj());
                            tj1.setMaxsj(zzxx.getJysj());
                        }
                        mapTjjg.put(zzxx.getDskh(), tj1);
                    }
                }


                String temp = zzxx.getYhkkh()+zzxx.getDskh();
                if(zzxx.getDskh() == null || "".equals(zzxx.getDskh().trim())){
                    temp = zzxx.getYhkkh()+zzxx.getBcsm();
                }
                if (mapTjjgs.containsKey(temp)) {
                    tjjgs = mapTjjgs.get(temp);
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
                    mapTjjgs.put(temp, tjs1);
                }

            }
            if ("进".equals(zzxx.getSfbz())) {
                if (mapTjjg.containsKey(zzxx.getYhkkh())) {
                    tjjg = mapTjjg.get(zzxx.getYhkkh());
                    tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                    tjjg.setJzzcs(tjjg.getJzzcs().add(new BigDecimal(1)));
                    tjjg.setJzzje(tjjg.getJzzje().add(zzxx.getJyje()));
                    if(tjjg.getKhh()==null){
                        tjjg.setKhh(GetBank.getBankname(zzxx.getYhkkh()).split("·")[0]);
                    }
                    if(TimeFormatUtil.DateFormat(zzxx.getJysj())!=null){
                        if(TimeFormatUtil.DateFormat(zzxx.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjg.getMinsj()))==-1){
                            tjjg.setMinsj(zzxx.getJysj());
                        }else if(TimeFormatUtil.DateFormat(zzxx.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjg.getMaxsj()))==1){
                            tjjg.setMaxsj(zzxx.getJysj());
                        }
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
                    if (TimeFormatUtil.DateFormat(zzxx.getJysj()) != null) {
                        tj2.setMinsj(zzxx.getJysj());
                        tj2.setMaxsj(zzxx.getJysj());
                    }
                    mapTjjg.put(zzxx.getYhkkh(), tj2);
                }
                if (zzxx.getDskh() != null && !zzxx.getYhkkh().equals(zzxx.getDskh())&& !zczh.contains(zzxx.getDskh())) {
                    if (mapTjjg.containsKey(zzxx.getDskh())) {
                        tjjg = mapTjjg.get(zzxx.getDskh());
                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                        tjjg.setCzzcs(tjjg.getCzzcs().add(new BigDecimal(1)));
                        tjjg.setCzzje(tjjg.getCzzje().add(zzxx.getJyje()));
                        if(tjjg.getKhh()==null){
                            tjjg.setKhh(bankName(GetBank.getBankname(zzxx.getDskh()).split("·")[0],zzxx.getDskhh()));
                        }
                        if(TimeFormatUtil.DateFormat(zzxx.getJysj())!=null){
                            if(TimeFormatUtil.DateFormat(zzxx.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjg.getMinsj()))==-1){
                                tjjg.setMinsj(zzxx.getJysj());
                            }else if(TimeFormatUtil.DateFormat(zzxx.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjg.getMaxsj()))==1){
                                tjjg.setMaxsj(zzxx.getJysj());
                            }
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
                        if (TimeFormatUtil.DateFormat(zzxx.getJysj()) != null) {
                            tj2.setMinsj(zzxx.getJysj());
                            tj2.setMaxsj(zzxx.getJysj());
                        }
                        mapTjjg.put(zzxx.getDskh(), tj2);
                    }
                }

                String temp = zzxx.getYhkkh()+zzxx.getDskh();
                if(zzxx.getDskh() == null || "".equals(zzxx.getDskh().trim())){
                    temp = zzxx.getYhkkh()+zzxx.getBcsm();
                }
                if (mapTjjgs.containsKey(temp)) {
                    tjjgs = mapTjjgs.get(temp);
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
                    mapTjjgs.put(temp, tjs2);
                }
            }
            listHash.put(zzxx.getHash(zzxx),null);
        }
        list.clear();
        list.add(mapTjjg);
        list.add(mapTjjgs);
        list.add(listHash);
        return list;
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
    /**
     * 07版数据读取
     * @param path
     * @param excelName
     * @param fields
     * @return
     */
    private List<BankZzxxEntity> getBy2007ExcelAll(String path, String excelName, List<List<String>> fields) {
        // 用于存放表格中列号
        List<BankZzxxEntity> zzxxList = new ArrayList<>();
        File file = new File(path);
        Map<String,Integer> title = new HashMap<>();
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(file);
            Workbook wk = StreamingReader.builder()
                    .rowCacheSize(100)  //缓存到内存中的行数，默认是10
                    .bufferSize(512)  //读取资源时，缓存到内存的字节大小，默认是1024
                    .open(fi);  //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
            for (int numSheet = 0; numSheet < wk.getNumberOfSheets(); numSheet++) {
                Sheet sheet = wk.getSheetAt(numSheet);
                for (List<String> field : fields) {
                    if (sheet == null) {
                        continue;
                    }else if(excelName.equals(field.get(0)) && sheet.getSheetName().equals(field.get(1))) {
                        boolean temp = true;
                        for (Row row : sheet) {
                            int cellNum = row.getLastCellNum();
                            // 字段长度小于3跳出本次循环
                            if (cellNum < 3) {
                                continue;
                            }
                            if (temp) {
                                if (row != null) {
                                    for (int i = 0; i < cellNum; i++) {
                                        Cell cell = row.getCell(i);
                                        String rowValue = MappingUtils.rowValue(cell);
                                        for (int j = 3; j < field.size(); j++) {
                                            if (rowValue.equals(field.get(j))) {
                                                title.put(field.get(j), i);
                                            }
                                        }
                                    }
                                }
                                temp = false;
                            } else {
                                if (row != null) {
                                    BankZzxxEntity zzxx = RowToEntity(row, field, title);
                                    if (zzxx != null) {
                                        zzxxList.add(zzxx);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println(excelName.substring(13,excelName.length()));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(excelName.substring(13,excelName.length())+"-------------");
        } finally{
            try {
                if(fi!=null){
                    fi.close();
                    new File(path).delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 删除文件
        new File(path).delete();
        return zzxxList;
    }
    public static boolean isStartWithNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str.charAt(0)+"");
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    /**
     * excel行映射实体类
     * @param xssfRow
     * @param field
     * @param title
     * @return
     */
    private BankZzxxEntity RowToEntity(Row xssfRow, List<String> field, Map<String, Integer> title) {
        BankZzxxEntity bankZzxx = new BankZzxxEntity();
        String YHKKH = bankZzxx.remove_(MappingUtils.mappingFieldString(xssfRow,field.get(3),title));
        bankZzxx.setYhkkh(YHKKH);
        bankZzxx.setJyxm(MappingUtils.mappingFieldString(xssfRow,field.get(22),title));
        bankZzxx.setJyzjh(MappingUtils.mappingFieldString(xssfRow,field.get(20),title));
        String jyrq = MappingUtils.mappingFieldString(xssfRow,field.get(23),title);
        if(jyrq.length()>0){
            bankZzxx.setJysj(MappingUtils.mappingFieldString(xssfRow,field.get(4),title)+" "+jyrq);
        }else{
            bankZzxx.setJysj(MappingUtils.mappingFieldString(xssfRow,field.get(4),title));
        }

        bankZzxx.setJyje(MappingUtils.mappingFieldBigDecimal(xssfRow,field.get(5),title).abs());
        bankZzxx.setJyye(MappingUtils.mappingFieldBigDecimal(xssfRow,field.get(6),title));
        String sfbz = MappingUtils.mappingFieldString(xssfRow,field.get(7),title);
        if("".equals(sfbz)){
            sfbz ="0";
        }
        if(isStartWithNumber(sfbz)){
            if(new BigDecimal(sfbz).abs().compareTo(BigDecimal.ZERO)==0){
                bankZzxx.setSfbz("出");
                if(bankZzxx.getJyje().compareTo(BigDecimal.ZERO)==0){
                    bankZzxx.setSfbz(null);
                }
            }else{
                bankZzxx.setSfbz("进");
                bankZzxx.setJyje(new BigDecimal(sfbz));
            }
        }else{
            bankZzxx.setSfbz(sfbz.replace("收","进").replace("付","出")
                    .replace("贷","进").replace("借","出"));
        }

        bankZzxx.setDskh(bankZzxx.remove_(MappingUtils.mappingFieldString(xssfRow,field.get(14),title)));
        bankZzxx.setDsxm(MappingUtils.mappingFieldString(xssfRow,field.get(9),title));
        bankZzxx.setDssfzh(MappingUtils.mappingFieldString(xssfRow,field.get(10),title));
        bankZzxx.setDskhh(MappingUtils.mappingFieldString(xssfRow,field.get(15),title));

        bankZzxx.setZysm(MappingUtils.mappingFieldString(xssfRow,field.get(11),title));
//        bankZzxx.setJysfcg(MappingUtils.mappingFieldString(xssfRow,field.get(12),title));
//        bankZzxx.setYhkzh(bankZzxx.remove_(MappingUtils.mappingFieldString(xssfRow,field.get(13),title)));
//        bankZzxx.setDszh(bankZzxx.remove_(MappingUtils.mappingFieldString(xssfRow,field.get(8),title)));
        bankZzxx.setJywdmc(MappingUtils.mappingFieldString(xssfRow,field.get(16),title));
//        bankZzxx.setDsjyye(MappingUtils.mappingFieldBigDecimal(xssfRow,field.get(17),title));
//        bankZzxx.setDsye(MappingUtils.mappingFieldBigDecimal(xssfRow,field.get(18),title));
        bankZzxx.setBz(MappingUtils.mappingFieldString(xssfRow,field.get(19),title));
        bankZzxx.setJyfsd(MappingUtils.mappingFieldString(xssfRow,field.get(21),title));
        if(bankZzxx.getDskh()==null || "".equals(bankZzxx.getDskh())){
            String bcsm = bankZzxx.getYhkkh()+"-";
            if(bankZzxx.getDsxm()!=null && !"".equals(bankZzxx.getDsxm())){
                bcsm+=bankZzxx.getDsxm();
            }else if(bankZzxx.getZysm()!=null && !"".equals(bankZzxx.getZysm())){
                bcsm+=bankZzxx.getZysm();
            }else if(bankZzxx.getBz()!=null && !"".equals(bankZzxx.getBz())){
                bcsm+=bankZzxx.getBz();
            }else {
                bcsm += "空账户";
            }
            bankZzxx.setBcsm(bcsm);
        }
        return bankZzxx;
    }
}
