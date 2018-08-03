package cn.com.sinofaith.bean.bankBean;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.prefs.NodeChangeEvent;

@Entity
@Table(name = "bank_zzxx", schema = "",catalog = "")
public class BankZzxxEntity {
    private long id;
    private String yhkkh;
    private String yhkzh;
    private String jysj;
    private BigDecimal jyje = new BigDecimal(0);
    private BigDecimal jyye = new BigDecimal(0);
    private String sfbz;
    private String dskh;
    private String dszh;
    private String dsxm;
    private String dssfzh;
    private String dskhh;
    private String zysm;
    private String jysfcg;
    private String jywdmc;
    private BigDecimal dsjyye;
    private BigDecimal dsye;
    private String bz;
    private long aj_id;
    private String inserttime;

    public BankZzxxEntity() {
        super();
    }

    public BankZzxxEntity(long id, String yhkkh, String yhkzh, String jysj, BigDecimal jyje, BigDecimal jyye, String sfbz, String dskh, String dszh, String dsxm, String dssfzh, String dskhh, String zysm, String jysfcg, String jywdmc, BigDecimal dsjyye, BigDecimal dsye, String bz, long aj_id, String inserttime) {
        this.id = id;
        this.yhkkh = yhkkh;
        this.yhkzh = yhkzh;
        this.jysj = jysj;
        this.jyje = jyje;
        this.jyye = jyye;
        this.sfbz = sfbz;
        this.dskh = dskh;
        this.dszh = dszh;
        this.dsxm = dsxm;
        this.dssfzh = dssfzh;
        this.dskhh = dskhh;
        this.zysm = zysm;
        this.jysfcg = jysfcg;
        this.jywdmc = jywdmc;
        this.dsjyye = dsjyye;
        this.dsye = dsye;
        this.bz = bz;
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
    @Column(name = "yhkkh",length = 50)
    public String getYhkkh() {
        return yhkkh;
    }

    public void setYhkkh(String yhkkh) {
        this.yhkkh = yhkkh;
    }
    @Basic
    @Column(name="yhkzh",length = 50)
    public String getYhkzh() {
        return yhkzh;
    }

    public void setYhkzh(String yhkzh) {
        this.yhkzh = yhkzh;
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
    @Column(name="dskh",length = 50)
    public String getDskh(){
        return dskh;
    }
    public void setDskh(String dskh){
        this.dskh = dskh;
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
    @Column(name="dskhh",length = 50)
    public String getDskhh(){
        return dskhh;
    }
    public void setDskhh(String dskhh){
        this.dskhh = dskhh;
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
    @Column(name = "jywdmc",length = 50)
    public String getJywdmc() {
        return jywdmc;
    }
    public void setJywdmc(String jywdmc) {
        this.jywdmc = jywdmc;
    }
    @Basic
    @Column(name = "dsjyye",precision = 2)
    public BigDecimal getDsjyye() {
        return dsjyye;
    }

    public void setDsjyye(BigDecimal dsjyye) {
        this.dsjyye = dsjyye;
    }
    @Basic
    @Column(name = "dsye",precision = 2)
    public BigDecimal getDsye() {
        return dsye;
    }

    public void setDsye(BigDecimal dsye) {
        this.dsye = dsye;
    }
    @Basic
    @Column(name = "bz",length = 50)
    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
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
                ", yhkkh='" + yhkkh + '\'' +
                ", yhkzh='" + yhkzh + '\'' +
                ", jysj='" + jysj + '\'' +
                ", jyje=" + jyje +
                ", jyye=" + jyye +
                ", sfbz='" + sfbz + '\'' +
                ", dskh='" + dskh + '\'' +
                ", dszh='" + dszh + '\'' +
                ", dsxm='" + dsxm + '\'' +
                ", dssfzh='" + dssfzh + '\'' +
                ", dskhh='" + dskhh + '\'' +
                ", zysm='" + zysm + '\'' +
                ", jysfcg='" + jysfcg + '\'' +
                ", jywdmc='" + jywdmc + '\'' +
                ", dsjyye=" + dsjyye +
                ", dsye=" + dsye +
                ", bz='" + bz + '\'' +
                ", aj_id=" + aj_id +
                ", inserttime='" + inserttime + '\'' +
                '}';
    }
}
