package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.CftTjjgEntity;
import cn.com.sinofaith.bean.CftZzxxEntity;
import cn.com.sinofaith.dao.CftTjjgDao;
import cn.com.sinofaith.page.Page;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
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
import java.util.*;

/**
 * Created by Me. on 2018/5/23
 */
@Service
public class CftTjjgService {
    @Autowired
    private CftTjjgDao cfttjd;

    public Page queryForPage(int currentPage, int pageSize, String seach){
        Page page = new Page();
        //总记录数
        int allRow = cfttjd.getAllRowCount(seach);
        List<CftTjjgEntity> cfttj = new ArrayList<CftTjjgEntity>();
        int xh = 1;
        if(allRow != 0) {
            cfttj = cfttjd.getDoPage(seach,currentPage,pageSize);
            for(int i=0; i<cfttj.size();i++){
                cfttj.get(i).setId(xh+(currentPage-1)*10);
                xh++;
            }
        }
        page.setList(cfttj);
        page.setPageNo(currentPage);
        page.setPageSize(pageSize);
        page.setTotalRecords(allRow);
        return page;
    }


    public int count(List<CftZzxxEntity> listZzxx){
        List<CftTjjgEntity> listTjjg = null;
        Map<String,CftTjjgEntity> map = new HashMap();
        CftTjjgEntity tjjg = new CftTjjgEntity();
        CftZzxxEntity zzxx = null;
        for(int i=0;i<listZzxx.size();i++){
            zzxx = listZzxx.get(i);
            if(map.containsKey(zzxx.getZh()+zzxx.getJylx())){
                if(zzxx.getZh().equals(zzxx.getFsf())){
                    tjjg = map.get(zzxx.getZh()+zzxx.getJylx());
                    tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                    tjjg.setCzzcs(tjjg.getCzzcs().add(new BigDecimal(1)));
                    tjjg.setCzzje(tjjg.getCzzje().add(zzxx.getJyje()));
                }
                if(zzxx.getZh().equals(zzxx.getJsf())){
                    tjjg = map.get(zzxx.getJsf()+zzxx.getJylx());
                    tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                    tjjg.setJzzcs(tjjg.getJzzcs().add(new BigDecimal(1)));
                    tjjg.setJzzje(tjjg.getJzzje().add(zzxx.getJyje()));
                }
            }else{
                if(zzxx.getZh().equals(zzxx.getFsf())){
                    CftTjjgEntity tj1 = new CftTjjgEntity();
                    tj1.setJyzh(zzxx.getZh());
                    tj1.setJylx(zzxx.getJylx());
                    tj1.setJyzcs(new BigDecimal(1));
                    tj1.setCzzcs(new BigDecimal(1));
                    tj1.setCzzje(zzxx.getJyje());
                    map.put(zzxx.getFsf()+zzxx.getJylx(),tj1);
                }
                if(zzxx.getZh().equals(zzxx.getJsf())){
                    CftTjjgEntity tj2 = new CftTjjgEntity();
                    tj2.setJyzh(zzxx.getZh());
                    tj2.setJylx(zzxx.getJylx());
                    tj2.setJyzcs(new BigDecimal(1));
                    tj2.setJzzcs(new BigDecimal(1));
                    tj2.setJzzje(zzxx.getJyje());
                    map.put(zzxx.getJsf()+zzxx.getJylx(),tj2);
                }
            }
        }
        listTjjg = new ArrayList<>(map.values());
        int i = 0;
        for(CftTjjgEntity tj:listTjjg){
            cfttjd.insert(tj);
            i++;
        }

        return i;
    }

    public void downloadFile(String seach, HttpServletResponse rep) throws Exception{
        List<CftTjjgEntity> listTjxx = cfttjd.find("from CftTjjgEntity where 1=1 "+seach + " order by jyzh desc ,czzje desc,jzzje desc");
        HSSFWorkbook wb = createExcel(listTjxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String("财付通统计信息.xls".getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public HSSFWorkbook createExcel(List<CftTjjgEntity> listTjjg){
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("财付通统计信息");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("交易账户");
        cell = row.createCell(2);
        cell.setCellValue("交易总次数");
        cell = row.createCell(3);
        cell.setCellValue("进账总次数");
        cell = row.createCell(4);
        cell.setCellValue("进账总金额(元)");
        cell = row.createCell(5);
        cell.setCellValue("出账总次数");
        cell = row.createCell(6);
        cell.setCellValue("出账总金额(元)");
        int i = 1;
        for(CftTjjgEntity tjjg: listTjjg){
            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(i);
            cell = row.createCell(1);
            cell.setCellValue(tjjg.getJyzh());
            cell = row.createCell(2);
            cell.setCellValue(tjjg.getJyzcs().toString());
            cell = row.createCell(3);
            cell.setCellValue(tjjg.getJzzcs().toString());
            cell = row.createCell(4);
            cell.setCellValue(String.valueOf(tjjg.getJzzje().doubleValue()));
            cell = row.createCell(5);
            cell.setCellValue(tjjg.getCzzcs().toString());
            cell = row.createCell(6);
            cell.setCellValue(String.valueOf(tjjg.getCzzje().doubleValue()));
            i++;
        }
        for(int a=0;a<7;a++){
            sheet.autoSizeColumn(a);
        }

        return wb;
    }
}