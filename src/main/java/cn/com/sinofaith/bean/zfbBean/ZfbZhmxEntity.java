package cn.com.sinofaith.bean.zfbBean;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ZfbZhmx")
public class ZfbZhmxEntity {
    private long id;
    private String jyh;
    private String shddh;
    private String jycjsj;
    private String fksj;
    private String zjxgsj;
    private String jylyd;
    private String lx;
    private String yhxx;
    private String jydfxx;
    private String xfmc;
    private double je;
    private String sz;
    private String jyzt;
    private String bz;
    private String dyxcsj;
    private String inserttime;
    private long aj_id;

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator="zfb_zhmxnum")
    @SequenceGenerator(name="zfb_zhmxnum",sequenceName="SEQ_ZFBZHMX_ID",allocationSize=1,initialValue=1)
    @Column(name="id",nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "jyh",nullable = true,length = 100)
    public String getJyh() {
        return jyh;
    }

    public void setJyh(String jyh) {
        this.jyh = jyh;
    }
    @Basic
    @Column(name = "shddh",nullable = true,length = 100)
    public String getShddh() {
        return shddh;
    }

    public void setShddh(String shddh) {
        this.shddh = shddh;
    }
    @Basic
    @Column(name = "jycjsj",nullable = true,length = 100)
    public String getJycjsj() {
        return jycjsj;
    }

    public void setJycjsj(String jycjsj) {
        this.jycjsj = jycjsj;
    }
    @Basic
    @Column(name = "fksj",nullable = true,length = 100)
    public String getFksj() {
        return fksj;
    }

    public void setFksj(String fksj) {
        this.fksj = fksj;
    }
    @Basic
    @Column(name = "zjxgsj",nullable = true,length = 100)
    public String getZjxgsj() {
        return zjxgsj;
    }

    public void setZjxgsj(String zjxgsj) {
        this.zjxgsj = zjxgsj;
    }
    @Basic
    @Column(name = "jylyd",nullable = true,length = 100)
    public String getJylyd() {
        return jylyd;
    }

    public void setJylyd(String jylyd) {
        this.jylyd = jylyd;
    }
    @Basic
    @Column(name = "lx",nullable = true,length = 100)
    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }
    @Basic
    @Column(name = "yhxx",nullable = true,length = 300)
    public String getYhxx() {
        return yhxx;
    }

    public void setYhxx(String yhxx) {
        this.yhxx = yhxx;
    }
    @Basic
    @Column(name = "jydfxx",nullable = true,length = 300)
    public String getJydfxx() {
        return jydfxx;
    }

    public void setJydfxx(String jydfxx) {
        this.jydfxx = jydfxx;
    }
    @Basic
    @Column(name = "xfmc",nullable = true,length = 300)
    public String getXfmc() {
        return xfmc;
    }

    public void setXfmc(String xfmc) {
        this.xfmc = xfmc;
    }
    @Basic
    @Column(name = "je",nullable = true,precision = 0)
    public double getJe() {
        return je;
    }

    public void setJe(double je) {
        this.je = je;
    }
    @Basic
    @Column(name = "sz",nullable = true,length = 100)
    public String getSz() {
        return sz;
    }

    public void setSz(String sz) {
        this.sz = sz;
    }

    @Basic
    @Column(name = "jyzt",nullable = true,length = 100)
    public String getJyzt() {
        return jyzt;
    }

    public void setJyzt(String jyzt) {
        this.jyzt = jyzt;
    }

    @Basic
    @Column(name = "bz",nullable = true,length = 100)
    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    @Basic
    @Column(name = "dyxcsj",nullable = true,length = 100)
    public String getDyxcsj() {
        return dyxcsj;
    }

    public void setDyxcsj(String dyxcsj) {
        this.dyxcsj = dyxcsj;
    }

    @Basic
    @Column(name = "insert_time",nullable = true,length = 19)
    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    @Basic
    @Column(name = "aj_id",nullable = false,precision = 0)
    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
    }

    @Override
    public String toString() {
        return "ZfbZhmxEntity{" +
                "id=" + id +
                ", jyh='" + jyh + '\'' +
                ", shddh='" + shddh + '\'' +
                ", jycjsj='" + jycjsj + '\'' +
                ", fksj='" + fksj + '\'' +
                ", zjxgsj='" + zjxgsj + '\'' +
                ", jylyd='" + jylyd + '\'' +
                ", lx='" + lx + '\'' +
                ", yhxx='" + yhxx + '\'' +
                ", jydfxx='" + jydfxx + '\'' +
                ", xfmc='" + xfmc + '\'' +
                ", je='" + je + '\'' +
                ", sz='" + sz + '\'' +
                ", jyzt='" + jyzt + '\'' +
                ", bz='" + bz + '\'' +
                ", dyxcsj='" + dyxcsj + '\'' +
                ", inserttime='" + inserttime + '\'' +
                ", aj_id=" + aj_id +
                '}';
    }

    /**
     * 账户明细分析数据excel导出
     * @param zhmxList
     * @param excelName
     * @return
     */
    public static HSSFWorkbook createExcel(List<ZfbZhmxEntity> zhmxList, String excelName) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet(excelName);
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("交易号");
        cell = row.createCell(2);
        cell.setCellValue("商户订单号");
        cell = row.createCell(3);
        cell.setCellValue("付款时间");
        cell = row.createCell(4);
        cell.setCellValue("类型");
        cell = row.createCell(5);
        cell.setCellValue("用户信息");
        cell = row.createCell(6);
        cell.setCellValue("交易对方信息");
        cell = row.createCell(7);
        cell.setCellValue("消费名称");
        cell = row.createCell(8);
        cell.setCellValue("金额");
        cell = row.createCell(9);
        cell.setCellValue("收/支");
        cell = row.createCell(10);
        cell.setCellValue("交易状态");
        int b = 1;
        for (int i = 0; i < zhmxList.size(); i++) {
            ZfbZhmxEntity wl = zhmxList.get(i);
            if ((i + b) >= 65536 && (i + b) % 65536 == 0) {
                sheet = wb.createSheet(excelName + "(" + b + ")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("交易号");
                cell = row.createCell(2);
                cell.setCellValue("商户订单号");
                cell = row.createCell(3);
                cell.setCellValue("付款时间");
                cell = row.createCell(4);
                cell.setCellValue("类型");
                cell = row.createCell(5);
                cell.setCellValue("用户信息");
                cell = row.createCell(6);
                cell.setCellValue("交易对方信息");
                cell = row.createCell(7);
                cell.setCellValue("消费名称");
                cell = row.createCell(8);
                cell.setCellValue("金额");
                cell = row.createCell(9);
                cell.setCellValue("收/支");
                cell = row.createCell(10);
                cell.setCellValue("交易状态");
                b += 1;
            }
            row = sheet.createRow((i + b) % 65536);
            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell = row.createCell(1);
            cell.setCellValue(wl.getJyh());
            cell = row.createCell(2);
            cell.setCellValue(wl.getShddh());
            cell = row.createCell(3);
            cell.setCellValue(wl.getFksj());
            cell = row.createCell(4);
            cell.setCellValue(wl.getLx());
            cell = row.createCell(5);
            cell.setCellValue(wl.getYhxx());
            cell = row.createCell(6);
            cell.setCellValue(wl.getJydfxx());
            cell = row.createCell(7);
            cell.setCellValue(wl.getXfmc());
            cell = row.createCell(8);
            cell.setCellValue(wl.getJe());
            cell = row.createCell(9);
            cell.setCellValue(wl.getSz());
            cell = row.createCell(10);
            cell.setCellValue(wl.getJyzt());
            if ((i + b) % 65536 == 0) {
                for (int a = 0; a < 11; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }
        return wb;
    }
}
