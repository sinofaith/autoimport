package cn.com.sinofaith.bean.bankBean;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "mapping_bankzzxx", schema = "", catalog = "")
public class MappingBankzzxxEntity {
    private long id;
    private String name;
    private String jykh;
    private String jyxm;
    private String jyzjh;
    private String jyrq;
    private String jysj;
    private String jyje;
    private String jyye;
    private String sfbz;
    private String dskh;
    private String dsxm;
    private String dszjh;
    private String dskhh;
    private String zysm;
    private String jywdmc;
    private String jyfsd;
    private String bz;
    private String inserttime;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Basic
    @Column(name = "name", length = 300)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Basic
    @Column(name = "jykh",nullable = false, length = 300)
    public String getJykh() {
        return jykh;
    }

    public void setJykh(String jykh) {
        this.jykh = jykh;
    }
    @Basic
    @Column(name = "jyxm", length = 300)
    public String getJyxm() {
        return jyxm;
    }

    public void setJyxm(String jyxm) {
        this.jyxm = jyxm;
    }
    @Basic
    @Column(name = "jyzjh", length = 300)
    public String getJyzjh() {
        return jyzjh;
    }

    public void setJyzjh(String jyzjh) {
        this.jyzjh = jyzjh;
    }
    @Basic
    @Column(name = "jyrq",nullable = false, length = 300)
    public String getJyrq() {
        return jyrq;
    }

    public void setJyrq(String jyrq) {
        this.jyrq = jyrq;
    }
    @Basic
    @Column(name = "jysj", length = 300)
    public String getJysj() {
        return jysj;
    }

    public void setJysj(String jysj) {
        this.jysj = jysj;
    }
    @Basic
    @Column(name = "jyje",nullable = false, length = 300)
    public String getJyje() {
        return jyje;
    }

    public void setJyje(String jyje) {
        this.jyje = jyje;
    }
    @Basic
    @Column(name = "jyye", length = 300)
    public String getJyye() {
        return jyye;
    }

    public void setJyye(String jyye) {
        this.jyye = jyye;
    }
    @Basic
    @Column(name = "sfbz",nullable = false, length = 300)
    public String getSfbz() {
        return sfbz;
    }

    public void setSfbz(String sfbz) {
        this.sfbz = sfbz;
    }
    @Basic
    @Column(name = "dskh",nullable = false, length = 300)
    public String getDskh() {
        return dskh;
    }

    public void setDskh(String dskh) {
        this.dskh = dskh;
    }
    @Basic
    @Column(name = "dsxm", length = 300)
    public String getDsxm() {
        return dsxm;
    }

    public void setDsxm(String dsxm) {
        this.dsxm = dsxm;
    }
    @Basic
    @Column(name = "dszjh", length = 300)
    public String getDszjh() {
        return dszjh;
    }

    public void setDszjh(String dszjh) {
        this.dszjh = dszjh;
    }
    @Basic
    @Column(name = "dskhh", length = 300)
    public String getDskhh() {
        return dskhh;
    }

    public void setDskhh(String dskhh) {
        this.dskhh = dskhh;
    }
    @Basic
    @Column(name = "zysm", length = 300)
    public String getZysm() {
        return zysm;
    }

    public void setZysm(String zysm) {
        this.zysm = zysm;
    }
    @Basic
    @Column(name = "jywdmc", length = 300)
    public String getJywdmc() {
        return jywdmc;
    }

    public void setJywdmc(String jywdmc) {
        this.jywdmc = jywdmc;
    }
    @Basic
    @Column(name = "jyfsd", length = 300)
    public String getJyfsd() {
        return jyfsd;
    }

    public void setJyfsd(String jyfsd) {
        this.jyfsd = jyfsd;
    }
    @Basic
    @Column(name = "bz", length = 300)
    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
    @Basic
    @Column(name = "inserttime", length = 300)
    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    @Override
    public String toString() {
        return "MappingBankzzxxEntity{" +
                "jykh='" + jykh + '\'' +
                ", jyxm='" + jyxm + '\'' +
                ", jyzjh='" + jyzjh + '\'' +
                ", jyrq='" + jyrq + '\'' +
                ", jysj='" + jysj + '\'' +
                ", jyje='" + jyje + '\'' +
                ", jyye='" + jyye + '\'' +
                ", sfbz='" + sfbz + '\'' +
                ", dskh='" + dskh + '\'' +
                ", dsxm='" + dsxm + '\'' +
                ", dszjh='" + dszjh + '\'' +
                ", dskhh='" + dskhh + '\'' +
                ", zysm='" + zysm + '\'' +
                ", jywdmc='" + jywdmc + '\'' +
                ", jyfsd='" + jyfsd + '\'' +
                ", bz='" + bz + '\'' +
                '}';
    }

    public Map<String,String> objToMap(){
        Map<String,String> map = new HashMap<>();
        map.put("jykh",jykh);
        map.put("jyxm",jyxm);
        map.put("jyzjh",jyzjh);
        map.put("jyrq",jyrq);
        map.put("jysj",jysj);
        map.put("jyje",jyje);
        map.put("jyye",jyye);
        map.put("sfbz",sfbz);
        map.put("dskh",dskh);
        map.put("dsxm",dsxm);
        map.put("dszjh",dszjh);
        map.put("dskhh",dskhh);
        map.put("zysm",zysm);
        map.put("jywdmc",jywdmc);
        map.put("jyfsd",jyfsd);
        map.put("bz",bz);
        return map;
    }
}
