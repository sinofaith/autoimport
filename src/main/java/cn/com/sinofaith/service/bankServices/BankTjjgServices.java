package cn.com.sinofaith.service.bankServices;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.BankTjjgEntity;
import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import cn.com.sinofaith.dao.bankDao.BankTjjgDao;
import cn.com.sinofaith.form.cftForm.CftTjjgForm;
import cn.com.sinofaith.page.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BankTjjgServices {

    @Autowired
    private BankTjjgDao tjjd;

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

    public String getSeach(String seachCondition, String seachCode, String orderby, String desc, AjEntity aj){
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
        if(orderby!=null){
            if("khxm".equals(orderby)){
                seach.append(" order by s." + orderby + desc +"  nulls last ,c.id");
            }else{
                seach.append(" order by c." +orderby + desc);
            }
        }
        return seach.toString();
    }

    public int count(List<BankZzxxEntity> listZzxx, long aj){
        List<BankTjjgEntity> listTjjg = null;
        Map<String,BankTjjgEntity> map = new HashMap();
        BankTjjgEntity tjjg = null;
        BankZzxxEntity zzxx = null;
        for(int i=0;i<listZzxx.size();i++){
            zzxx = listZzxx.get(i);
            if(map.containsKey(zzxx.getYhkkh())){
                if("出".equals(zzxx.getSfbz())){
                    tjjg = map.get(zzxx.getYhkkh());
                    tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                    tjjg.setCzzcs(tjjg.getCzzcs().add(new BigDecimal(1)));
                    tjjg.setCzzje(tjjg.getCzzje().add(zzxx.getJyje()));
                }
                if("进".equals(zzxx.getSfbz())){
                    tjjg = map.get(zzxx.getYhkkh());
                    tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                    tjjg.setJzzcs(tjjg.getJzzcs().add(new BigDecimal(1)));
                    tjjg.setJzzje(tjjg.getJzzje().add(zzxx.getJyje()));
                }
            }else{
                if("出".equals(zzxx.getSfbz())){
                    BankTjjgEntity tj1 = new BankTjjgEntity();
                    tj1.setJyzh(zzxx.getYhkkh());
                    tj1.setJyzcs(new BigDecimal(1));
                    tj1.setCzzcs(new BigDecimal(1));
                    tj1.setCzzje(zzxx.getJyje());
                    tj1.setAj_id(aj);
                    map.put(zzxx.getYhkkh(),tj1);
                }
                if("进".equals(zzxx.getSfbz())){
                    BankTjjgEntity tj2 = new BankTjjgEntity();
                    tj2.setJyzh(zzxx.getYhkkh());
                    tj2.setJyzcs(new BigDecimal(1));
                    tj2.setJzzcs(new BigDecimal(1));
                    tj2.setJzzje(zzxx.getJyje());
                    tj2.setAj_id(aj);
                    map.put(zzxx.getYhkkh(),tj2);
                }
            }
            String a = zzxx.getDskh();
            if(a==null) {
                a=zzxx.getBcsm();
            }
                if (map.containsKey(a)) {
                    if ("出".equals(zzxx.getSfbz())) {
                        tjjg = map.get(a);
                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                        tjjg.setJzzcs(tjjg.getJzzcs().add(new BigDecimal(1)));
                        tjjg.setJzzje(tjjg.getJzzje().add(zzxx.getJyje()));
                    }
                    if ("进".equals(zzxx.getSfbz())) {
                        tjjg = map.get(a);
                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                        tjjg.setCzzcs(tjjg.getJzzcs().add(new BigDecimal(1)));
                        tjjg.setCzzje(tjjg.getJzzje().add(zzxx.getJyje()));
                    }
                } else {
                    if ("出".equals(zzxx.getSfbz())) {
                        BankTjjgEntity tj1 = new BankTjjgEntity();
                        tj1.setJyzh(a);
                        tj1.setJyzcs(new BigDecimal(1));
                        tj1.setJzzcs(new BigDecimal(1));
                        tj1.setJzzje(zzxx.getJyje());
                        tj1.setAj_id(aj);
                        map.put(a, tj1);
                    }
                    if ("进".equals(zzxx.getSfbz())) {
                        BankTjjgEntity tj2 = new BankTjjgEntity();
                        tj2.setJyzh(a);
                        tj2.setJyzcs(new BigDecimal(1));
                        tj2.setCzzcs(new BigDecimal(1));
                        tj2.setCzzje(zzxx.getJyje());
                        tj2.setAj_id(aj);
                        map.put(a, tj2);
                    }
                }
        }
        listTjjg = new ArrayList<>(map.values());
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
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("交易账卡号");
        cell = row.createCell(3);
        cell.setCellValue("交易总次数");
        cell = row.createCell(4);
        cell.setCellValue("进账总次数");
        cell = row.createCell(5);
        cell.setCellValue("进账总金额(元)");
        cell = row.createCell(6);
        cell.setCellValue("出账总次数");
        cell = row.createCell(7);
        cell.setCellValue("出账总金额(元)");
        for(int i=0;i<listTjjg.size();i++){
            if(i>=65535&& i%65535==0){
                sheet = wb.createSheet("银行卡账户信息("+b+")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("姓名");
                cell = row.createCell(2);
                cell.setCellValue("交易账卡号");
                cell = row.createCell(3);
                cell.setCellValue("交易总次数");
                cell = row.createCell(4);
                cell.setCellValue("进账总次数");
                cell = row.createCell(5);
                cell.setCellValue("进账总金额(元)");
                cell = row.createCell(6);
                cell.setCellValue("出账总次数");
                cell = row.createCell(7);
                cell.setCellValue("出账总金额(元)");
                b+=1;
            }
            Map map = (Map)listTjjg.get(i);
            row = sheet.createRow(i%65535 + 1);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            if(map.get("KHXM") != null && map.get("KHXM").toString().length()>0){
                cell.setCellValue(map.get("KHXM").toString());
            }
            cell = row.createCell(2);
            cell.setCellValue(map.get("JYZH").toString());
            cell = row.createCell(3);
            cell.setCellValue(map.get("JYZCS").toString());
            cell = row.createCell(4);
            cell.setCellValue(map.get("JZZCS").toString());
            cell = row.createCell(5);
            cell.setCellValue(map.get("JZZJE").toString());
            cell = row.createCell(6);
            cell.setCellValue(map.get("CZZCS").toString());
            cell = row.createCell(7);
            cell.setCellValue(map.get("CZZJE").toString());
            if(i%65536==0) {
                for (int a = 0; a < 10; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }


        return wb;
    }
}
