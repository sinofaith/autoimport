package cn.com.sinofaith.bean.bankBean;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Me. on 2018/5/22
 */
@Entity
@IdClass(value = BankTjjgsEntity.class)
@Table(name="bank_tjjgs",schema = "",catalog = "")
public class BankTjjgsEntity implements Serializable {
    private long id;
    private String jyzh;
    private String dfzh;
    private BigDecimal jyzcs = new BigDecimal(0);
    private BigDecimal jzzcs = new BigDecimal(0);
    private BigDecimal jzzje = new BigDecimal(0);
    private BigDecimal czzcs = new BigDecimal(0);
    private BigDecimal czzje = new BigDecimal(0);
    private long aj_id;
    private String inserttime;
    private long zhlx = 0;
    private String minsj;
    private String maxsj;

    @Basic
    @Column(name = "id",nullable = true,precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Id
    @Column(name = "jyzh",nullable = false,length = 200)
    public String getJyzh() {
        return jyzh;
    }

    public void setJyzh(String jyzh) {
        this.jyzh = jyzh;
    }

    @Id
    @Column(name="dfzh",nullable = false,length = 500)
    public String getDfzh() { return dfzh; }

    public void setDfzh(String dfzh) { this.dfzh = dfzh; }

    @Basic
    @Column(name="jyzcs",nullable = false,precision = 0)
    public BigDecimal getJyzcs() {
        return jyzcs;
    }

    public void setJyzcs(BigDecimal jyzcs) {
        this.jyzcs = jyzcs;
    }
    @Basic
    @Column(name="jzzcs",nullable = false,precision = 0)
    public BigDecimal getJzzcs() {
        return jzzcs;
    }

    public void setJzzcs(BigDecimal jzzcs) {
        this.jzzcs = jzzcs;
    }

    @Basic
    @Column(name="jzzje",nullable = false,precision = 2)
    public BigDecimal getJzzje() {
        return jzzje;
    }

    public void setJzzje(BigDecimal jzzje) {
        this.jzzje = jzzje;
    }

    @Basic
    @Column(name = "czzcs",nullable = false,precision = 0)
    public BigDecimal getCzzcs() {
        return czzcs;
    }

    public void setCzzcs(BigDecimal czzcs) {
        this.czzcs = czzcs;
    }

    @Basic
    @Column(name="czzje",nullable = false,precision = 2)
    public BigDecimal getCzzje() {
        return czzje;
    }

    public void setCzzje(BigDecimal czzje) {
        this.czzje = czzje;
    }

    @Basic
    @Column(name = "inserttime",nullable = true,length = 30)
    public String getInserttime(){return inserttime;}

    public void setInserttime(String inserttime){this.inserttime=inserttime;}

    @Basic
    @Column(name = "aj_id",nullable = false,precision = 0)
    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
    }
    @Basic
    @Column(name = "zhlx",nullable = false,precision = 0)
    public long getZhlx() {
        return zhlx;
    }

    public void setZhlx(long zhlx) {
        this.zhlx = zhlx;
    }
    @Basic
    @Column(name="minsj")
    public String getMinsj() {
        return minsj;
    }
    public void setMinsj(String minsj) {
        this.minsj = minsj;
    }

    @Basic
    @Column(name = "maxsj")
    public String getMaxsj() {
        return maxsj;
    }

    public void setMaxsj(String maxsj) {
        this.maxsj = maxsj;
    }

    @Override
    public String toString() {
        return "TjjgEntity{" +
                "id=" + id +
                ", jyzh='" + jyzh + '\'' +
                ", dfzh='" + dfzh + '\'' +
                ", jyzcs=" + jyzcs +
                ", jzzcs=" + jzzcs +
                ", jzzje=" + jzzje +
                ", czzcs=" + czzcs +
                ", czzje=" + czzje +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankTjjgsEntity that = (BankTjjgsEntity) o;

        if (id != that.id) return false;
        if (jyzh != null ? !jyzh.equals(that.jyzh) : that.jyzh != null) return false;
        if (dfzh != null ? !dfzh.equals(that.dfzh) : that.dfzh != null) return false;
        if (jyzcs != null ? !jyzcs.equals(that.jyzcs) : that.jyzcs != null) return false;
        if (jzzcs != null ? !jzzcs.equals(that.jzzcs) : that.jzzcs != null) return false;
        if (jzzje != null ? !jzzje.equals(that.jzzje) : that.jzzje != null) return false;
        if (czzcs != null ? !czzcs.equals(that.czzcs) : that.czzcs != null) return false;
        return czzje != null ? czzje.equals(that.czzje) : that.czzje == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (jyzh != null ? jyzh.hashCode() : 0);
        result = 31 * result + (dfzh != null ? dfzh.hashCode() : 0);
        result = 31 * result + (jyzcs != null ? jyzcs.hashCode() : 0);
        result = 31 * result + (jzzcs != null ? jzzcs.hashCode() : 0);
        result = 31 * result + (jzzje != null ? jzzje.hashCode() : 0);
        result = 31 * result + (czzcs != null ? czzcs.hashCode() : 0);
        result = 31 * result + (czzje != null ? czzje.hashCode() : 0);
        return result;
    }
}