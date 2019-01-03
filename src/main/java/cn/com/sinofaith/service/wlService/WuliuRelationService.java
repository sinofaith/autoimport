package cn.com.sinofaith.service.wlService;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.bean.wlBean.WuliuRelationEntity;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.wuliuDao.WuliuRelationDao;
import cn.com.sinofaith.form.wlForm.WuliuRelationForm;
import cn.com.sinofaith.page.Page;
import com.google.gson.Gson;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WuliuRelationService {

    @Autowired
    private AJDao ajDao;
    @Autowired
    private WuliuRelationDao wlrDao;

    /**
     * 封装sql语句
     * @param seachCondition
     * @param seachCode
     * @param aj
     * @param orderby
     * @param desc
     * @return
     */
    public String getSeach(String seachCondition, String seachCode, AjEntity aj, String orderby, String desc) {
        // 查询点击的案件id
        String aj_id = getAjidByAjm(aj);
        StringBuffer seach = new StringBuffer(" and aj_id in (" + aj_id + ")");
        // 当查询内容不为空时
        if(seachCode!=null) {
            seachCode = seachCode.replace("\r\n", "").replace("，", "").replace(" ", "").replace(" ", "").replace("\t", "");
            seach.append(" and " + seachCondition + " like '" + seachCode + "'");
        }
        if(orderby!=null){
            seach .append(" order by "+orderby).append(desc);
        }
        return seach.toString();
    }

    /**
     * 查询选中的案件ids
     * @param aj
     * @return
     */
    public String getAjidByAjm(AjEntity aj){
        String[] ajm = new String[]{};
        StringBuffer ajid = new StringBuffer();
        if(aj.getAj().contains(",")) {
            ajm = aj.getAj().split(",");
            for (int i = 0; i < ajm.length; i++) {
                ajid.append(ajDao.findFilter(ajm[i]).get(0).getId());
                if (i != ajm.length - 1) {
                    ajid.append(",");
                }
            }
        }else{
            ajid.append(aj.getId());
        }
        return ajid.toString();
    }

    /**
     * 封装分页数据
     * @param currentPage
     * @param pageSize
     * @param seach
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, String seach, long aj_id) {
        // 创建分页对象
        Page page = new Page();
        // 封装wuliu_relation表
        List<WuliuRelationForm> wlrs = null;
        int allRow = wlrDao.getAllRowCount(seach);
        if(allRow>0){
            wlrs = wlrDao.getDoPage(seach, currentPage ,pageSize, aj_id);
            for (int i = 0; i < wlrs.size(); i++) {
                wlrs.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            // 封装page
            page.setPageNo(currentPage);
            page.setList(wlrs);
            page.setPageSize(pageSize);
            page.setTotalRecords(allRow);
        }
        return page;
    }

    /**
     * 物流关系详情
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public String getWuliuRelation(int currentPage, int pageSize, DetachedCriteria dc, String seach) {
        Gson gson = new Gson();
        Page page = new Page();
        List<WuliuEntity> wls = null;
        int rowAll = wlrDao.getRowAll(seach);
        if(rowAll>0){
           wls = wlrDao.getPage(currentPage, pageSize, dc);
            for (int i = 0; i < wls.size(); i++) {
                wls.get(i).setId((currentPage-1)*pageSize+i+1);
            }
           page.setPageNo(currentPage);
           page.setList(wls);
           page.setTotalRecords(rowAll);
           page.setPageSize(pageSize);
        }
        return gson.toJson(page);
    }

    /**
     * 数据导出
     * @param seach
     * @return
     */
    public List<WuliuRelationEntity> getWuliuRelationAll(String seach) {
        List<WuliuRelationEntity> wlrs = null;
        int rowAll = wlrDao.getAllRowCount(seach);
        if(rowAll>0){
            wlrs = wlrDao.getWuliuRelationAll(seach);
        }
        return wlrs;
    }

    /**
     * 详情数据导出
     * @param dc
     * @return
     */
    public List<WuliuEntity> WuliuAll(DetachedCriteria dc,String seach) {
        int rowAll = wlrDao.getRowAll(seach);
        if(rowAll>0){
            List<WuliuEntity> pageAll = wlrDao.getPageAll(dc);
            return pageAll;
        }
        return null;
    }

    /**
     * 创建excel
     * @param wlrs
     * @return
     */
    public HSSFWorkbook createExcel(List<WuliuRelationEntity> wlrs) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("物流寄收关系信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("寄件人");
        cell = row.createCell(2);
        cell.setCellValue("寄件电话");
        cell = row.createCell(3);
        cell.setCellValue("寄件地址");
        cell = row.createCell(4);
        cell.setCellValue("收件人");
        cell = row.createCell(5);
        cell.setCellValue("收件电话");
        cell = row.createCell(6);
        cell.setCellValue("收件地址");
        cell = row.createCell(7);
        cell.setCellValue("收发次数");
        int b = 1;
        for(int i=0;i<wlrs.size();i++) {
            WuliuRelationEntity wl = wlrs.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                sheet = wb.createSheet("物流寄收关系信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("寄件人");
                cell = row.createCell(2);
                cell.setCellValue("寄件电话");
                cell = row.createCell(3);
                cell.setCellValue("寄件地址");
                cell = row.createCell(4);
                cell.setCellValue("收件人");
                cell = row.createCell(5);
                cell.setCellValue("收件电话");
                cell = row.createCell(6);
                cell.setCellValue("收件地址");
                cell = row.createCell(7);
                cell.setCellValue("收发次数");
                b += 1;
            }
            row = sheet.createRow((i+b)%65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getSender());
            cell = row.createCell(2);
            cell.setCellValue(wl.getShip_phone());
            cell = row.createCell(3);
            cell.setCellValue(wl.getShip_address());
            cell = row.createCell(4);
            cell.setCellValue(wl.getAddressee());
            cell = row.createCell(5);
            cell.setCellValue(wl.getSj_phone());
            cell = row.createCell(6);
            cell.setCellValue(wl.getSj_address());
            cell = row.createCell(7);
            cell.setCellValue(wl.getNum());
            if((i+b)%65536==0) {
                for (int a = 0; a < 8; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }

    /**
     * 详情数据导出
     * @param wls
     * @return
     */
    public HSSFWorkbook createDetailsExcel(List<WuliuEntity> wls) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("物流寄收关系详情信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("运单号");
        cell = row.createCell(2);
        cell.setCellValue("寄件时间");
        cell = row.createCell(3);
        cell.setCellValue("寄件人");
        cell = row.createCell(4);
        cell.setCellValue("寄件电话");
        cell = row.createCell(5);
        cell.setCellValue("寄件地址");
        cell = row.createCell(6);
        cell.setCellValue("收件人");
        cell = row.createCell(7);
        cell.setCellValue("收件电话");
        cell = row.createCell(8);
        cell.setCellValue("收件地址");
        cell = row.createCell(9);
        cell.setCellValue("托寄物");
        cell = row.createCell(10);
        cell.setCellValue("付款方式");
        cell = row.createCell(11);
        cell.setCellValue("代收货款");
        cell = row.createCell(12);
        cell.setCellValue("运费");
        int b = 1;
        for(int i=0;i<wls.size();i++) {
            WuliuEntity wl = wls.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                sheet = wb.createSheet("物流寄收关系详情信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("运单号");
                cell = row.createCell(2);
                cell.setCellValue("寄件时间");
                cell = row.createCell(3);
                cell.setCellValue("寄件人");
                cell = row.createCell(4);
                cell.setCellValue("寄件电话");
                cell = row.createCell(5);
                cell.setCellValue("寄件地址");
                cell = row.createCell(6);
                cell.setCellValue("收件人");
                cell = row.createCell(7);
                cell.setCellValue("收件电话");
                cell = row.createCell(8);
                cell.setCellValue("收件地址");
                cell = row.createCell(9);
                cell.setCellValue("托寄物");
                cell = row.createCell(10);
                cell.setCellValue("付款方式");
                cell = row.createCell(11);
                cell.setCellValue("代收货款");
                cell = row.createCell(12);
                cell.setCellValue("运费");
                b += 1;
            }
            row = sheet.createRow((i+b)%65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getWaybill_id());
            cell = row.createCell(2);
            cell.setCellValue(wl.getShip_time());
            cell = row.createCell(3);
            cell.setCellValue(wl.getSender());
            cell = row.createCell(4);
            cell.setCellValue(wl.getShip_phone());
            cell = row.createCell(5);
            cell.setCellValue(wl.getShip_address());
            cell = row.createCell(6);
            cell.setCellValue(wl.getAddressee());
            cell = row.createCell(7);
            cell.setCellValue(wl.getSj_phone());
            cell = row.createCell(8);
            cell.setCellValue(wl.getSj_address());
            cell = row.createCell(9);
            cell.setCellValue(wl.getTjw());
            cell = row.createCell(10);
            cell.setCellValue(wl.getPayment());
            cell = row.createCell(11);
            cell.setCellValue(wl.getDshk());
            cell = row.createCell(12);
            cell.setCellValue(wl.getFreight());
            if((i+b)%65536==0) {
                for (int a = 0; a < 13; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }
}
