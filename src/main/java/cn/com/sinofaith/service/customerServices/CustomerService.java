package cn.com.sinofaith.service.customerServices;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.customerProBean.PersonCompanyEntity;
import cn.com.sinofaith.bean.customerProBean.PersonNumberEntity;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.customerDao.CustomerDao;
import cn.com.sinofaith.form.customerForm.CustomerProForm;
import cn.com.sinofaith.form.customerForm.PersonRelationForm;
import cn.com.sinofaith.page.Page;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {
    @Autowired
    private CustomerDao bcd;
    @Autowired
    private AJDao ad;


    public String getAjidByAjm(AjEntity aj, long userId){
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

    public String getSeach(String seachCode, String seachCondition){
//        String ajid = getAjidByAjm(aj,userId);
        StringBuffer seach = new StringBuffer();

        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            seach.append(" and c." + seachCondition + " like " + "'%" + seachCode + "%'");
        }else{
            seach.append(" and ( 1=1 ) ");
        }
        return seach.toString();
    }
    public String getSeachAj(String seachCode, String seachCondition,long ajid){
//        String ajid = getAjidByAjm(aj,userId);
        StringBuffer seach = new StringBuffer();

        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if("name".equals(seachCondition)) {
                seach.append(" and cp." + seachCondition + " like " + "'%" + seachCode + "%'");
            }else {
                seach.append(" and c." + seachCondition + " like " + "'%" + seachCode + "%'");
            }
        }else{
            seach.append(" and ( 1=1 ) ");
        }
        seach.append(" and c.aj_id=" + ajid);
        return seach.toString();
    }



    public Page queryForPage(int currentPage, int pageSize, String seach){
        Page page = new Page();
        List<CustomerProForm> zzFs = new ArrayList<>();
        int allRow = bcd.getAllRowCount(seach);
        if(allRow>0){
            List zzList = bcd.getDoPage(seach,currentPage,pageSize);
            for(int i=0;i<zzList.size();i++) {
                CustomerProForm zzf = new CustomerProForm();
                Map map = (Map) zzList.get(i);
                zzf = zzf.mapToObj(map);
                zzf.setXh(i+1);
                zzFs.add(zzf);
            }
        }
        page.setPageSize(pageSize);
        page.setPageNo(currentPage);
        page.setTotalRecords(allRow);
        page.setList(zzFs);
        return page;
    }

    public Page queryForPageAj(int currentPage, int pageSize, String seach){
        Page page = new Page();
        List<CustomerProForm> zzFs = new ArrayList<>();
        int allRow = bcd.getAllRowCountAj(seach);
        if(allRow>0){
            List zzList = bcd.getDoPageAj(seach,currentPage,pageSize);
            for(int i=0;i<zzList.size();i++) {
                CustomerProForm zzf = new CustomerProForm();
                Map map = (Map) zzList.get(i);
                zzf = zzf.mapToObj(map);
                zzf.setXh(i+1);
                zzFs.add(zzf);
            }
        }
        page.setPageSize(pageSize);
        page.setPageNo(currentPage);
        page.setTotalRecords(allRow);
        page.setList(zzFs);
        return page;
    }

    public void downloadFile(HttpServletResponse rep, List<PersonCompanyEntity> listpce, List<PersonNumberEntity> listpne, List<PersonRelationForm> listpre,AjEntity aj) throws Exception{
        HSSFWorkbook wb = createExcel(listpce,listpne,listpre);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String(("目标团伙及上下家信息\""+aj.getAj()+".xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public HSSFWorkbook createExcel(List<PersonCompanyEntity> listpce, List<PersonNumberEntity> listpne, List<PersonRelationForm> listpre)throws Exception{
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFCell cell = null;
        int b = 1;
        HSSFSheet sheet = wb.createSheet("目标基本信息");
        HSSFRow row = createRow(sheet);
        for (int i = 0; i<listpce.size(); i++) {
            if(i>=65535&& i%65535==0){
                sheet = wb.createSheet("目标基本信息("+b+")");
                row = createRow(sheet);
                b+=1;
            }
            PersonCompanyEntity zzf = listpce.get(i);
            row = sheet.createRow(i%65535 + 1);
            cell = row.createCell(0);
            cell.setCellValue(zzf.getName());
            cell = row.createCell(1);
            cell.setCellValue("公司");
            cell = row.createCell(2);
            cell.setCellValue(zzf.getCompany());
            cell = row.createCell(3);
            cell.setCellValue(zzf.getCompanyAdd());
            cell = row.createCell(4);
            cell.setCellValue(zzf.getCompanyWeb());
            cell = row.createCell(5);
            cell.setCellValue(zzf.getCompanyRemark());
            cell = row.createCell(6);
            cell.setCellValue(zzf.getCompanyPhone());
            cell = row.createCell(7);
            cell.setCellValue(zzf.getCompanyEmail());
        }

        for(int i=0;i<listpne.size();i++){
            if((i+listpce.size())>=65535 && (i+listpce.size())%65535==0){
                sheet = wb.createSheet("目标基本信息("+b+")");
                b+=1;
            }
            PersonNumberEntity pne = listpne.get(i);
            row = sheet.createRow((i+listpce.size())%65535 + 1);
            cell = row.createCell(0);
            cell.setCellValue(pne.getName());
            cell = row.createCell(1);
            cell.setCellValue("手机");
            cell = row.createCell(2);
            cell.setCellValue(pne.getPhone());
            cell = row.createCell(3);
            cell.setCellValue(pne.getNumbers());
            cell = row.createCell(4);
            cell.setCellValue(pne.getNumberName());
            cell = row.createCell(5);
            cell.setCellValue(pne.getSex());
            cell = row.createCell(6);
            cell.setCellValue(pne.getAge());
            cell = row.createCell(7);
            cell.setCellValue(pne.getAddress());
            cell = row.createCell(8);
            cell.setCellValue(pne.getNumberType());
        }
        sheet = wb.createSheet("目标关系");
        row = createRowByRelation(sheet);
        for(int i=0;i<listpre.size();i++){
            if(i>=65535&& i%65535==0){
                sheet = wb.createSheet("目标关系("+b+")");
                row = createRowByRelation(sheet);
                b+=1;
            }
            PersonRelationForm pre = listpre.get(i);
            row = sheet.createRow(i%65535 + 1);
            cell = row.createCell(0);
            cell.setCellValue(pre.getName());
            cell = row.createCell(1);
            cell.setCellValue(pre.getPname());
            cell = row.createCell(2);
            cell.setCellValue(pre.getRelationName());
            cell = row.createCell(3);
            cell.setCellValue(pre.getRelationShow());
            cell = row.createCell(4);
            cell.setCellValue(pre.getRelationMark());
            cell = row.createCell(5);
            cell.setCellValue(pre.getJzzje()==null ? "" : pre.getJzzje().toString());
            cell = row.createCell(6);
            cell.setCellValue(pre.getCzzje()==null ? "" : pre.getCzzje().toString());
            cell = row.createCell(7);
            cell.setCellValue(pre.getJzzje()==null ? "" : pre.getJzzje().subtract(pre.getCzzje()).toString());
        }
        return wb;
    }

    public HSSFRow createRow(HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("目标人物姓名");
        cell = row.createCell(1);
        cell.setCellValue("类型");
        HSSFPatriarch p=sheet.createDrawingPatriarch();

        HSSFComment comment=p.createComment(new HSSFClientAnchor(0,0,0,0,(short)1,5,(short)4,20));

        //输入批注信息
        comment.setString(new HSSFRichTextString("类型为公司:\n" +
                "1C-公司名称\n" +
                "1D-公司地址\n" +
                "1E-公司网址\n" +
                "1F-备注或主营\n" +
                "1G-公司电话\n" +
                "1H-公司邮箱\n" +
                "------------------------\n" +
                "类型为手机:\n" +
                "1C-手机号\n" +
                "1D-关联账号\n" +
                "1E-关联账号昵称\n" +
                "1F-关联账号性别\n" +
                "1G-关联账号年龄\n" +
                "1H-关联账号定位\n" +
                "1I-关联账号类型"));
        //将批注添加到单元格对象中
        cell.setCellComment(comment);
        return row;
    }


    public HSSFRow createRowByRelation(HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("目标人物姓名");
        cell = row.createCell(1);
        cell.setCellValue("对象");
        cell = row.createCell(2);
        cell.setCellValue("关系");
        HSSFPatriarch p=sheet.createDrawingPatriarch();
        HSSFComment comment=p.createComment(new HSSFClientAnchor(0,0,0,0,(short)1,5,(short)4,15));
        //输入批注信息
        comment.setString(new HSSFRichTextString("资金关联:\n" +
                "1E-进账总金额\n" +
                "1F-出账总金额\n" +
                "1G-交易净值(进账-出账)\n" +
                "----------\n" +
                "公司关联:\n" +
                "1E-公司名称"));
        //将批注添加到单元格对象中
        cell.setCellComment(comment);
        cell = row.createCell(3);
        cell.setCellValue("关系说明");
        cell = row.createCell(4);
        cell.setCellValue("备注");
        cell = row.createCell(5);
        cell.setCellValue("进账总金额");
        cell = row.createCell(6);
        cell.setCellValue("出账总金额");
        cell = row.createCell(7);
        cell.setCellValue("交易净值");
        return row;
    }
}
