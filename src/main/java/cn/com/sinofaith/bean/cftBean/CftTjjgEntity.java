package cn.com.sinofaith.bean.cftBean;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Me. on 2018/5/22
 */
@Entity
@IdClass(value = CftTjjgEntity.class)
@Table(name="cft_tjjg",schema = "",catalog = "")
public class CftTjjgEntity implements Serializable {
    private long id;
    private String jyzh;
    private String jylx;
    private BigDecimal jyzcs = new BigDecimal(0);
    private BigDecimal jzzcs = new BigDecimal(0);
    private BigDecimal jzzje = new BigDecimal(0);
    private BigDecimal czzcs = new BigDecimal(0);
    private BigDecimal czzje = new BigDecimal(0);
    private long aj_id;
    private String inserttime;
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
    @Column(name = "jyzh",nullable = false,length = 100)
    public String getJyzh() {
        return jyzh;
    }

    public void setJyzh(String jyzh) {
        this.jyzh = jyzh;
    }

    @Id
    @Column(name="jylx",nullable = false,length = 300)
    public String getJylx() {
        return jylx;
    }

    public void setJylx(String jylx) {
        this.jylx = jylx;
    }

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
    @Column(name ="aj_id",nullable = false,precision = 0)
    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
    }

    @Basic
    @Column(name="minsj",nullable = true,length = 200)
    public String getMinsj() {
        return minsj;
    }

    public void setMinsj(String minsj) {
        this.minsj = minsj;
    }
    @Basic
    @Column(name="maxsj",nullable = true,length = 200)
    public String getMaxsj() {
        return maxsj;
    }

    public void setMaxsj(String maxsj) {
        this.maxsj = maxsj;
    }

    @Override
    public String toString() {
        return "CftTjjgEntity{" +
                "id=" + id +
                ", jyzh='" + jyzh + '\'' +
                ", jylx='" + jylx + '\'' +
                ", jyzcs=" + jyzcs +
                ", jzzcs=" + jzzcs +
                ", jzzje=" + jzzje +
                ", czzcs=" + czzcs +
                ", czzje=" + czzje +
                ", aj_id=" + aj_id +
                ", inserttime='" + inserttime + '\'' +
                ", minsj='" + minsj + '\'' +
                ", maxsj='" + maxsj + '\'' +
                '}';
    }
}