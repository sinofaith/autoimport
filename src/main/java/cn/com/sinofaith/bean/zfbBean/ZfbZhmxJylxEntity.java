package cn.com.sinofaith.bean.zfbBean;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "zfbzhmx_jylx")
public class ZfbZhmxJylxEntity {
    private long id;
    private String jyzfbzh;
    private String jymc;
    private String xfmc;
    private long jyzcs;
    private long czzcs;
    private BigDecimal czzje;
    private long jzzcs;
    private BigDecimal jzzje;
    private String insert_time;
    private long aj_id;

    @Id
    @Column(name="id",nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
    @Column(name = "jymc",nullable = true,length = 100)
    public String getJymc() {
        return jymc;
    }

    public void setJymc(String jymc) {
        this.jymc = jymc;
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
    @Column(name = "jyzcs",nullable = false,precision = 0)
    public long getJyzcs() {
        return jyzcs;
    }

    public void setJyzcs(long jyzcs) {
        this.jyzcs = jyzcs;
    }

    @Basic
    @Column(name = "czzcs",nullable = false,precision = 0)
    public long getCzzcs() {
        return czzcs;
    }

    public void setCzzcs(long czzcs) {
        this.czzcs = czzcs;
    }

    @Basic
    @Column(name = "czzje",nullable = false,precision = 0)
    public BigDecimal getCzzje() {
        return czzje;
    }

    public void setCzzje(BigDecimal czzje) {
        this.czzje = czzje;
    }

    @Basic
    @Column(name = "jzzcs",nullable = false,precision = 0)
    public long getJzzcs() {
        return jzzcs;
    }

    public void setJzzcs(long jzzcs) {
        this.jzzcs = jzzcs;
    }

    @Basic
    @Column(name = "jzzje",nullable = false,precision = 0)
    public BigDecimal getJzzje() {
        return jzzje;
    }

    public void setJzzje(BigDecimal jzzje) {
        this.jzzje = jzzje;
    }

    @Basic
    @Column(name = "insert_time",nullable = true,length = 19)
    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    @Basic
    @Column(name = "aj_id",nullable = false,precision = 0)
    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
    }
}
