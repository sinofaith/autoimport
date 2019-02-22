package cn.com.sinofaith.bean.zfbBean;

import javax.persistence.*;

@Entity
@Table(name = "zfbzhmx_qxsj")
public class ZfbZhmxQxsjEntity {
    private long id;
    private String jyh;
    private String shddh;
    private String jycjsj;
    private String fksj;
    private String zjxgsj;
    private String jylyd;
    private String lx;
    private String jyzfbzh;
    private String jymc;
    private String dszfbzh;
    private String dsmc;
    private String xfmc;
    private double je;
    private String sz;
    private String jyzt;
    private String bz;
    private long aj_id;

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator="zfb_zhmxxqsjnum")
    @SequenceGenerator(name="zfb_zhmxxqsjnum",sequenceName="SEQ_ZFBZHMXQXSJ_ID",allocationSize=1,initialValue=1)
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
    @Column(name = "jyzfbzh",nullable = true,length = 100)
    public String getJyzfbzh() {
        return jyzfbzh;
    }

    public void setJyzfbzh(String jyzfbzh) {
        this.jyzfbzh = jyzfbzh;
    }

    @Basic
    @Column(name = "jymc",nullable = true,length = 568)
    public String getJymc() {
        return jymc;
    }

    public void setJymc(String jymc) {
        this.jymc = jymc;
    }

    @Basic
    @Column(name = "dszfbzh",nullable = true,length = 100)
    public String getDszfbzh() {
        return dszfbzh;
    }

    public void setDszfbzh(String dszfbzh) {
        this.dszfbzh = dszfbzh;
    }

    @Basic
    @Column(name = "dsmc",nullable = true,length = 568)
    public String getDsmc() {
        return dsmc;
    }

    public void setDsmc(String dsmc) {
        this.dsmc = dsmc;
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
    @Column(name = "aj_id",nullable = false,precision = 0)
    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
    }

    @Override
    public String toString() {
        return "ZfbZhmxQxsjEntity{" +
                "jyh='" + jyh + '\'' +
                ", shddh='" + shddh + '\'' +
                ", jycjsj='" + jycjsj + '\'' +
                ", fksj='" + fksj + '\'' +
                ", zjxgsj='" + zjxgsj + '\'' +
                ", jylyd='" + jylyd + '\'' +
                ", lx='" + lx + '\'' +
                ", jyzfbzh='" + jyzfbzh + '\'' +
                ", jymc='" + jymc + '\'' +
                ", dszfbzh='" + dszfbzh + '\'' +
                ", dsmc='" + dsmc + '\'' +
                ", xfmc='" + xfmc + '\'' +
                ", je=" + je +
                ", sz='" + sz + '\'' +
                ", jyzt='" + jyzt + '\'' +
                ", bz='" + bz + '\'' +
                '}';
    }
}
