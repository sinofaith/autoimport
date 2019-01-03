package cn.com.sinofaith.form.zfbForm;

import cn.com.sinofaith.bean.zfbBean.ZfbJyjlSjdzsEntity;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.math.BigDecimal;
import java.util.List;

public class ZfbJyjlSjdzsForm {
    private long id;
    private String mjyhid;
    private String mjxx;
    private String shrdz;
    private long sjcs;
    private BigDecimal czje = new BigDecimal(0);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMjyhid() {
        return mjyhid;
    }

    public void setMjyhid(String mjyhid) {
        this.mjyhid = mjyhid;
    }

    public String getMjxx() {
        return mjxx;
    }

    public void setMjxx(String mjxx) {
        this.mjxx = mjxx;
    }

    public String getShrdz() {
        return shrdz;
    }

    public void setShrdz(String shrdz) {
        this.shrdz = shrdz;
    }

    public long getSjcs() {
        return sjcs;
    }

    public void setSjcs(long sjcs) {
        this.sjcs = sjcs;
    }

    public BigDecimal getCzje() {
        return czje;
    }

    public void setCzje(BigDecimal czje) {
        this.czje = czje;
    }

    /**
     * 详情数据生成excel表
     * @param jdszsForms
     * @return
     */
    public static HSSFWorkbook createExcel(List<ZfbJyjlSjdzsForm> jdszsForms) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("支付宝交易记录地址详情信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("买家用户Id");
        cell = row.createCell(2);
        cell.setCellValue("买家信息");
        cell = row.createCell(3);
        cell.setCellValue("收货人地址");
        cell = row.createCell(4);
        cell.setCellValue("收件次数");
        cell = row.createCell(5);
        cell.setCellValue("出账金额");
        int b = 1;
        for(int i=0;i<jdszsForms.size();i++) {
            ZfbJyjlSjdzsForm wl = jdszsForms.get(i);
            if ((i+b) >= 65536 && (i+b) % 65536 == 0) {
                sheet = wb.createSheet("支付宝交易记录地址详情信息(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("买家用户Id");
                cell = row.createCell(2);
                cell.setCellValue("买家信息");
                cell = row.createCell(3);
                cell.setCellValue("收货人地址");
                cell = row.createCell(4);
                cell.setCellValue("收件次数");
                cell = row.createCell(5);
                cell.setCellValue("出账金额");
                b += 1;
            }
            row = sheet.createRow((i+b) % 65536);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getMjyhid());
            cell = row.createCell(2);
            cell.setCellValue(wl.getMjxx());
            cell = row.createCell(3);
            cell.setCellValue(wl.getShrdz());
            cell = row.createCell(4);
            cell.setCellValue(wl.getSjcs());
            cell = row.createCell(5);
            cell.setCellValue(wl.getCzje().toString());
            if((i+b)%65536==0) {
                for (int a = 0; a < 6; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }
}
