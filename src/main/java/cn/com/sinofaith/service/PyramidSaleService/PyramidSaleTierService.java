package cn.com.sinofaith.service.PyramidSaleService;

import cn.com.sinofaith.bean.pyramidSaleBean.PsHierarchyEntity;
import cn.com.sinofaith.bean.pyramidSaleBean.PyramidSaleEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.dao.pyramidSaleDao.PsHierarchyDao;
import cn.com.sinofaith.dao.pyramidSaleDao.PyramidSaleDao;
import cn.com.sinofaith.form.psForm.PsHierarchyForm;
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
import java.util.Set;

/**
 * 层级信息业务层
 */
@Service
public class PyramidSaleTierService {

    @Autowired
    private PsHierarchyDao psHierarchyDao;
    @Autowired
    private PyramidSaleDao pyramidSaleDao;

    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = new Page();
        List<PsHierarchyEntity> psHierarchyForms = null;
        int rowAll = psHierarchyDao.getRowAll(dc);
        if(rowAll>0){
            psHierarchyForms = psHierarchyDao.getDoPage(currentPage, pageSize,dc);
            if(psHierarchyForms!=null){
                page.setPageSize(pageSize);
                page.setList(psHierarchyForms);
                page.setTotalRecords(rowAll);
                page.setPageNo(currentPage);
            }
        }
        return page;
    }


    /**
     * 直推/下线详情
     * @param currentPage
     * @param pageSize
     * @param seach
     * @return
     */
    public String getPyramidSale(int currentPage, int pageSize, String seach, boolean temp, String psId) {
        Gson gson = new Gson();
        Page page = new Page();
        int rowAll = 0;
        Set<String> p = null;
        if (temp){
            rowAll = pyramidSaleDao.getRowAllBySql(seach);
        }else{
            p = pyramidSaleDao.getRowAllBySqls(seach,psId);
            rowAll = p.size();
        }
        if(rowAll>0){
            List<PyramidSaleEntity> psList = pyramidSaleDao.getDoPageBySql(currentPage,pageSize,seach,temp,p,true);
            for (int i = 0; i < psList.size(); i++) {
                psList.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            page.setPageNo(currentPage);
            page.setTotalRecords(rowAll);
            page.setList(psList);
            page.setPageSize(pageSize);
        }
        return gson.toJson(page);
    }

    /**
     * 数据下载全部数据
     * @param dc
     * @return
     */
    public List<PsHierarchyEntity> download(DetachedCriteria dc) {
        List<PsHierarchyEntity> psList = null;
        int rowAll = psHierarchyDao.getRowAll(dc);
        if(rowAll>0){
            psList = psHierarchyDao.getDoPageAll(dc);
        }
        return psList;
    }

    /***
     * 生成excel文件
     * @param tierList
     * @return
     */
    public HSSFWorkbook createExcel(List<PsHierarchyEntity> tierList) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("传销层级信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("会员号");
        cell = row.createCell(2);
        cell.setCellValue("推荐会员号");
        cell = row.createCell(3);
        cell.setCellValue("姓名");
        cell = row.createCell(4);
        cell.setCellValue("当前层级");
        cell = row.createCell(5);
        cell.setCellValue("包含层级");
        cell = row.createCell(6);
        cell.setCellValue("直推下线");
        cell = row.createCell(7);
        cell.setCellValue("下线会员");
        int b = 1;
        for(int i=0;i<tierList.size();i++) {
            PsHierarchyEntity wl = tierList.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                sheet = wb.createSheet("传销层级信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("会员号");
                cell = row.createCell(2);
                cell.setCellValue("推荐会员号");
                cell = row.createCell(3);
                cell.setCellValue("姓名");
                cell = row.createCell(4);
                cell.setCellValue("当前层级");
                cell = row.createCell(5);
                cell.setCellValue("包含层级");
                cell = row.createCell(6);
                cell.setCellValue("直推下线");
                cell = row.createCell(7);
                cell.setCellValue("下线会员");
                b += 1;
            }
            row = sheet.createRow((i+b)%65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getPsId());
            cell = row.createCell(2);
            cell.setCellValue(wl.getSponsorId());
            cell = row.createCell(3);
            cell.setCellValue(wl.getNick_name());
            cell = row.createCell(4);
            cell.setCellValue(wl.getTier());
            cell = row.createCell(5);
            cell.setCellValue(wl.getContainsTier());
            cell = row.createCell(6);
            cell.setCellValue(wl.getDirectDrive()!=null?wl.getDirectDrive():0);
            cell = row.createCell(7);
            cell.setCellValue(wl.getDirectReferNum()!=null?wl.getDirectReferNum():0);
            if((i+b)%65536==0) {
                for (int a = 0; a < 8; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }

    /**
     * 详情全部数据
     * @param search
     * @param temp
     * @return
     */
    public List<PyramidSaleEntity> downDetailInfo(String search, boolean temp, String psId) {
        List<PyramidSaleEntity> psList = null;
        int rowAll = 0;
        Set<String> p = null;
        if (temp){
            rowAll = pyramidSaleDao.getRowAllBySql(search);
        }else{
            p = pyramidSaleDao.getRowAllBySqls(search,psId);
            rowAll = p.size();
        }
        if(rowAll>0){
            psList = pyramidSaleDao.getDoPageBySql(0,0,search,temp,p,false);
        }
        return psList;
    }

    /**
     * 详情数据生成excel/
     * @param psList
     * @param temp
     * @return
     */
    public HSSFWorkbook createExcelDetails(List<PyramidSaleEntity> psList, boolean temp) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = null;
        if(temp){
            sheet = wb.createSheet("传销层级直推下线信息");
        }else{
            sheet = wb.createSheet("传销层级下线会员信息");
        }
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("会员编号");
        cell = row.createCell(2);
        cell.setCellValue("推荐会员编号");
        cell = row.createCell(3);
        cell.setCellValue("姓名");
        cell = row.createCell(4);
        cell.setCellValue("手机号码");
        cell = row.createCell(5);
        cell.setCellValue("性别");
        cell = row.createCell(6);
        cell.setCellValue("详细地址");
        cell = row.createCell(7);
        cell.setCellValue("身份证号");
        cell = row.createCell(8);
        cell.setCellValue("持卡人");
        cell = row.createCell(9);
        cell.setCellValue("银行名称");
        cell = row.createCell(10);
        cell.setCellValue("银行卡号");
        int b = 1;
        for(int i=0;i<psList.size();i++) {
            PyramidSaleEntity wl = psList.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                if(temp){
                    sheet = wb.createSheet("传销层级直推下线信息(" + b + ")");
                }else{
                    sheet = wb.createSheet("传销层级下线会员信息(" + b + ")");
                }
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("会员编号");
                cell = row.createCell(2);
                cell.setCellValue("推荐会员编号");
                cell = row.createCell(3);
                cell.setCellValue("姓名");
                cell = row.createCell(4);
                cell.setCellValue("手机号码");
                cell = row.createCell(5);
                cell.setCellValue("性别");
                cell = row.createCell(6);
                cell.setCellValue("详细地址");
                cell = row.createCell(7);
                cell.setCellValue("身份证号");
                cell = row.createCell(8);
                cell.setCellValue("持卡人");
                cell = row.createCell(9);
                cell.setCellValue("银行名称");
                cell = row.createCell(10);
                cell.setCellValue("银行卡号");
                b += 1;
            }
            row = sheet.createRow((i+b)%65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getPsId());
            cell = row.createCell(2);
            cell.setCellValue(wl.getSponsorId());
            cell = row.createCell(3);
            cell.setCellValue(wl.getNick_name());
            cell = row.createCell(4);
            cell.setCellValue(wl.getMobile());
            cell = row.createCell(5);
            cell.setCellValue(wl.getSex());
            cell = row.createCell(6);
            cell.setCellValue(wl.getAddress());
            cell = row.createCell(7);
            cell.setCellValue(wl.getSfzhm());
            cell = row.createCell(8);
            cell.setCellValue(wl.getAccountHolder());
            cell = row.createCell(9);
            cell.setCellValue(wl.getBankName());
            cell = row.createCell(10);
            cell.setCellValue(wl.getAccountNumber());
            if((b+1)%65536==0) {
                for (int a = 0; a < 11; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }
}
