package cn.com.sinofaith.bean.bankBean;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bank_zzxx", schema = "",catalog = "")
public class BankZzxxEntity {
    private long id;
    private String jyzkh;
    private String jysj;
    private BigDecimal jyje = new BigDecimal(0);
    private BigDecimal jyye = new BigDecimal(0);
    private String sfbz;
    private String dszh;
    private String dsxm;
    private String dssfzh;
    private String zysm;
    private String jysfcg;
    private long aj_id;
    private String inserttime;

    public BankZzxxEntity() {
        super();
    }

    public BankZzxxEntity(long id, String jyzkh, String jysj, BigDecimal jyje, BigDecimal jyye, String sfbz, String dszh, String dsxm, String dssfzh, String zysm, String jysfcg, long aj_id, String inserttime) {
        this.id = id;
        this.jyzkh = jyzkh;
        this.jysj = jysj;
        this.jyje = jyje;
        this.jyye = jyye;
        this.sfbz = sfbz;
        this.dszh = dszh;
        this.dsxm = dsxm;
        this.dssfzh = dssfzh;
        this.zysm = zysm;
        this.jysfcg = jysfcg;
        this.aj_id = aj_id;
        this.inserttime = inserttime;
    }
    @Id
    @Column(name = "id",nullable = false,precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "jyzkh",length = 50)
    public String getJyzkh() {
        return jyzkh;
    }

    public void setJyzkh(String jyzkh) {
        this.jyzkh = jyzkh;
    }
    @Basic
    @Column(name = "jysj",length = 50)
    public String getJysj() {
        return jysj;
    }

    public void setJysj(String jysj) {
        this.jysj = jysj;
    }
    @Basic
    @Column(name = "jyje",precision = 2)
    public BigDecimal getJyje() {
        return jyje;
    }

    public void setJyje(BigDecimal jyje) {
        this.jyje = jyje;
    }
    @Basic
    @Column(name = "jyye",precision = 2)
    public BigDecimal getJyye() {
        return jyye;
    }

    public void setJyye(BigDecimal jyye) {
        this.jyye = jyye;
    }
    @Basic
    @Column(name = "sfbz",length = 50)
    public String getSfbz() {
        return sfbz;
    }

    public void setSfbz(String sfbz) {
        this.sfbz = sfbz;
    }
    @Basic
    @Column(name = "dszh",length = 50)
    public String getDszh() {
        return dszh;
    }

    public void setDszh(String dszh) {
        this.dszh = dszh;
    }
    @Basic
    @Column(name = "dsxm",length = 50)
    public String getDsxm() {
        return dsxm;
    }

    public void setDsxm(String dsxm) {
        this.dsxm = dsxm;
    }
    @Basic
    @Column(name = "dssfzh",length = 50)
    public String getDssfzh() {
        return dssfzh;
    }

    public void setDssfzh(String dssfzh) {
        this.dssfzh = dssfzh;
    }
    @Basic
    @Column(name = "zysm",length = 50)
    public String getZysm() {
        return zysm;
    }

    public void setZysm(String zysm) {
        this.zysm = zysm;
    }
    @Basic
    @Column(name = "jysfcg",length = 50)
    public String getJysfcg() {
        return jysfcg;
    }

    public void setJysfcg(String jysfcg) {
        this.jysfcg = jysfcg;
    }
    @Basic
    @Column(name = "aj_id",precision = 0)
    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
    }
    @Basic
    @Column(name = "inserttime",length = 50)
    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    @Override
    public String toString() {
        return "BankZzxxEntity{" +
                "id=" + id +
                ", jyzkh='" + jyzkh + '\'' +
                ", jysj='" + jysj + '\'' +
                ", jyje=" + jyje +
                ", jyye=" + jyye +
                ", sfbz='" + sfbz + '\'' +
                ", dszh='" + dszh + '\'' +
                ", dsxm='" + dsxm + '\'' +
                ", dssfzh='" + dssfzh + '\'' +
                ", zysm='" + zysm + '\'' +
                ", jysfcg='" + jysfcg + '\'' +
                ", aj_id=" + aj_id +
                ", inserttime='" + inserttime + '\'' +
                '}';
    }
}
