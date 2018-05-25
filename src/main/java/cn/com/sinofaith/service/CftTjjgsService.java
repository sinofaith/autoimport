package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.CftTjjgEntity;
import cn.com.sinofaith.bean.CftTjjgsEntity;
import cn.com.sinofaith.bean.CftZzxxEntity;
import cn.com.sinofaith.dao.CftTjjgsDao;
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
        List<CftTjjgsEntity> cfttjs = new ArrayList<CftTjjgsEntity>();
        int xh = 1;
        if(allRow != 0) {
            cfttjs = cfttjsd.getDoPage(seach,currentPage,pageSize);
            for(int i=0; i<cfttjs.size();i++){
                cfttjs.get(i).setId(xh+(currentPage-1)*10);
                xh++;
            }
        }
        page.setList(cfttjs);
        page.setPageNo(currentPage);
        page.setPageSize(pageSize);
        page.setTotalRecords(allRow);
        return page;
    }
    public int count(List<CftZzxxEntity> listZzxx){
        Map<String,CftTjjgsEntity> map = new HashMap();
        CftTjjgsEntity tjjgs = new CftTjjgsEntity();
        CftZzxxEntity zzxx = null;

        for(int i=0;i<listZzxx.size();i++){
            zzxx = listZzxx.get(i);
            if(zzxx.getZh().equals(zzxx.getFsf())){
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
                    map.put(zzxx.getZh()+zzxx.getJsf(),tjs1);
                }
            }
            if(zzxx.getZh().equals(zzxx.getJsf())){
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
                    map.put(zzxx.getZh()+zzxx.getFsf(),tjs2);
                }
            }
        }
        List<CftTjjgsEntity> listTjjgs =null;
        listTjjgs = new ArrayList<>(map.values());
        int i =0;
        for (CftTjjgsEntity tjs:listTjjgs){
            cfttjsd.insert(tjs);
            i++;
        }
        return i;
    }

    public void downloadFile(String seach, HttpServletResponse rep) throws Exception{
        List<CftTjjgsEntity> listTjjg = cfttjsd.find("from CftTjjgsEntity where 1=1 "+seach);
        HSSFWorkbook wb = createExcel(listTjjg);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String("财付通统计信息2.xls".getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public HSSFWorkbook createExcel(List<CftTjjgsEntity> listTjjg){
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("财付通统计信息2");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("交易账户");
        cell = row.createCell(2);
        cell.setCellValue("交易账户");
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
        int i = 1;
        for(CftTjjgsEntity tjjg: listTjjg){
            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(i);
            cell = row.createCell(1);
            cell.setCellValue(tjjg.getJyzh());
            cell = row.createCell(2);
            cell.setCellValue(tjjg.getDfzh());
            cell = row.createCell(3);
            cell.setCellValue(tjjg.getJyzcs().toString());
            cell = row.createCell(4);
            cell.setCellValue(tjjg.getJzzcs().toString());
            cell = row.createCell(5);
            cell.setCellValue(String.valueOf(tjjg.getJzzje().doubleValue()));
            cell = row.createCell(6);
            cell.setCellValue(tjjg.getCzzcs().toString());
            cell = row.createCell(7);
            cell.setCellValue(String.valueOf(tjjg.getCzzje().doubleValue()));
            i++;
        }
        for(int a=0;a<8;a++){
            sheet.autoSizeColumn(a);
        }

        return wb;
    }
}