package cn.com.sinofaith.service.cftServices;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.cftDao.CftZzxxDao;
import cn.com.sinofaith.form.cftForm.CftZzxxForm;
import cn.com.sinofaith.page.Page;
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
import java.util.ArrayList;
import java.util.Date;
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
        CftZzxxForm zzf = new CftZzxxForm();
        if(allRow>0){
            List zzList = cftzzd.getDoPage(seach,currentPage,pageSize);
            int xh =1;
            for(int i=0;i<zzList.size();i++){
                Map map = (Map)zzList.get(i);
                zzf = zzf.mapToForm(map);
                zzf.setId(xh+(currentPage-1)*pageSize);
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

    public Row createRow(Sheet sheet){
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
        return row;
    }

    public HSSFWorkbook createExcel(List listZzxx)throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        Cell cell = null;
        int b = 1;
        Sheet sheet = wb.createSheet("财付通转账信息");
            Row row = createRow(sheet);
            for (int i = 0; i<listZzxx.size(); i++) {
                if(i>=65535&& i%65535==0){
                    sheet = wb.createSheet("财付通转账信息("+b+")");
                    row = createRow(sheet);
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
                cell.setCellValue(map.get("FSJE").toString().equals("0") ? "":map.get("FSJE").toString());
                cell = row.createCell(10);
                if (map.get("JSF") != null && map.get("JSF").toString().length() > 0) {
                    cell.setCellValue(map.get("JSF").toString());
                }
                cell = row.createCell(11);
                cell.setCellValue(map.get("JSJE").toString().equals("0") ? "":map.get("JSJE").toString());
                if(i%65536==0){
                    for (int a = 0; a < 13; a++) {
                        sheet.autoSizeColumn(a);
                    }
                }
            }
        return wb;
    }

    public String getSeach(String seachCode, String seachCondition,String orderby,String desc, AjEntity aj){
       String ajid = getAjidByAjm(aj);
        StringBuffer seach = new StringBuffer(" and c.aj_id in ("+ajid+") ");

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
            seach .append(" order by c."+orderby).append(desc).append(",c.id");
        }
        return seach.toString();
    }

    public void deleteByAj_id(long id){
        cftzzd.deleteByAjid(id);
    }

    public String getByJyzhlx(String zh,String jylx,String sum,String type,AjEntity aj,int page,String orders){
        Page pages = new Page();
        Gson gson = new GsonBuilder().serializeNulls().create();
        String ajid = getAjidByAjm(aj);

        String seach ="";
        if("jylx".equals(type)){
            seach=" and c.zh='"+zh+"' and c.jylx='"+jylx+"' ";
            if(jylx.equals("提现")){
                seach=" and c.zh='"+zh+"' and (c.jylx='"+jylx+"' or c.jylx = '微信提现手续费') ";
            }
            if(jylx.equals("转帐(有对手账户)")){
                seach=" and c.zh='"+zh+"' and c.fsf is not null and c.jsf is not null ";
            }
            if(jylx.equals("转帐(无对手账户)")){
                seach=" and c.zh='"+zh+"' and c.fsf is null and c.jsf is null ";
            }
        }else{
            seach=" and c.zh='"+zh+"' and (c.fsf='"+jylx+"' or c.jsf='"+jylx+"') ";
            if(zh.equals(jylx)){
                seach = "and c.fsf = c.jsf and c.zh='"+zh+"' ";
            }
        }
        if(aj.getFlg()==1){
            seach +=" and c.shmc not like'%红包%'";
        }
        seach += "and aj_id in("+ajid+") "+orders;

//        int allRow = cftzzd.getAllRowCount(seach);
        int allRow = Integer.parseInt(sum);
        List zzList = cftzzd.getDoPage(seach,page,100);

        List<CftZzxxForm> zzFs = new ArrayList<>();
        CftZzxxForm zzf = new CftZzxxForm();
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
}