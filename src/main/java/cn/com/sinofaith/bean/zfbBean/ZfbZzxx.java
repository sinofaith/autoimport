package cn.com.sinofaith.bean.zfbBean;

import javax.persistence.*;

@Entity
@Table(name = "zfb_zzxx")
public class ZfbZzxx {
    private long id=-1;
    private String jyh="";             //交易号
    private String fkfzfbzh="";        //付款方支付宝账号
    private String skfzfbzh="";        //收款方支付宝账号
    private String skjgxx="";          //收款机构信息
    private String dzsj="";            //到账时间
    private String zzje="";            //转账金额
    private String zzcpmc="";          //转账产品名称
    private String jyfsd="";           //交易发生地
    private String txlsh="";           //提现流水号
    private String dyxcsj="";          //对应协查数据
    private String inserttime="";
    private long aj_id=-1;

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator="zfb_num")
    @SequenceGenerator(name="zfb_num",sequenceName="SEQ_ZFB_ZZXX_ID",allocationSize=1,initialValue=1)
    @Column(name="id",nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Basic
    @Column(name = "jyh",nullable = true,length = 300)
    public String getJyh() {
        return jyh;
    }

    public void setJyh(String jyh) {
        this.jyh = jyh;
    }
    @Basic
    @Column(name = "fkfzfbzh",nullable = true,length = 300)
    public String getFkfzfbzh() {
        return fkfzfbzh;
    }

    public void setFkfzfbzh(String fkfzfbzh) {
        this.fkfzfbzh = fkfzfbzh;
    }
    @Basic
    @Column(name = "skfzfbzh",nullable = true,length = 300)
    public String getSkfzfbzh() {
        return skfzfbzh;
    }

    public void setSkfzfbzh(String skfzfbzh) {
        this.skfzfbzh = skfzfbzh;
    }
    @Basic
    @Column(name = "skjgxx",length = 300)
    public String getSkjgxx() {
        return skjgxx;
    }

    public void setSkjgxx(String skjgxx) {
        this.skjgxx = skjgxx;
    }
    @Basic
    @Column(name = "dzsj",length = 100)
    public String getDzsj() {
        return dzsj;
    }

    public void setDzsj(String dzsj) {
        this.dzsj = dzsj;
    }
    @Basic
    @Column(name="zzje",length = 100)
    public String getZzje() {
        return zzje;
    }

    public void setZzje(String zzje) {
        this.zzje = zzje;
    }
    @Basic
    @Column(name = "zzcpmc",length = 300)
    public String getZzcpmc() {
        return zzcpmc;
    }

    public void setZzcpmc(String zzcpmc) {
        this.zzcpmc = zzcpmc;
    }
    @Basic
    @Column(name = "jyfsd",length = 300)
    public String getJyfsd() {
        return jyfsd;
    }

    public void setJyfsd(String jyfsd) {
        this.jyfsd = jyfsd;
    }
    @Basic
    @Column(name="txlsh",length = 300)
    public String getTxlsh() {
        return txlsh;
    }

    public void setTxlsh(String txlsh) {
        this.txlsh = txlsh;
    }
    @Basic
    @Column(name = "dyxcsj",length = 100)
    public String getDyxcsj() {
        return dyxcsj;
    }

    public void setDyxcsj(String dyxcsj) {
        this.dyxcsj = dyxcsj;
    }
    @Basic
    @Column(name="inserttime",length = 19)
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

    public ZfbZzxx() {
    }

    public ZfbZzxx(long id, String jyh, String fkfzfbzh, String skfzfbzh, String skjgxx, String dzsj, String zzje, String zzcpmc, String jyfsd, String txlsh, String dyxcsj, String inserttime, long aj_id) {
        this.id = id;
        this.jyh = jyh;
        this.fkfzfbzh = fkfzfbzh;
        this.skfzfbzh = skfzfbzh;
        this.skjgxx = skjgxx;
        this.dzsj = dzsj;
        this.zzje = zzje;
        this.zzcpmc = zzcpmc;
        this.jyfsd = jyfsd;
        this.txlsh = txlsh;
        this.dyxcsj = dyxcsj;
        this.inserttime = inserttime;
        this.aj_id = aj_id;
    }

    @Override
    public String toString() {
        return "ZfbZzxx{" +
                "id=" + id +
                ", jyh='" + jyh + '\'' +
                ", fkfzfbzh='" + fkfzfbzh + '\'' +
                ", skfzfbzh='" + skfzfbzh + '\'' +
                ", skjgxx='" + skjgxx + '\'' +
                ", dzsj='" + dzsj + '\'' +
                ", zzje='" + zzje + '\'' +
                ", zzcpmc='" + zzcpmc + '\'' +
                ", jyfsd='" + jyfsd + '\'' +
                ", txlsh='" + txlsh + '\'' +
                ", dyxcsj='" + dyxcsj + '\'' +
                ", inserttime='" + inserttime + '\'' +
                ", aj_id=" + aj_id +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ZfbZzxx zfbZzxx = (ZfbZzxx) o;

        if (jyh != null ? !jyh.equals(zfbZzxx.jyh) : zfbZzxx.jyh != null) return false;
        if (fkfzfbzh != null ? !fkfzfbzh.equals(zfbZzxx.fkfzfbzh) : zfbZzxx.fkfzfbzh != null) return false;
        if (skfzfbzh != null ? !skfzfbzh.equals(zfbZzxx.skfzfbzh) : zfbZzxx.skfzfbzh != null) return false;
        if (skjgxx != null ? !skjgxx.equals(zfbZzxx.skjgxx) : zfbZzxx.skjgxx != null) return false;
        if (dzsj != null ? !dzsj.equals(zfbZzxx.dzsj) : zfbZzxx.dzsj != null) return false;
        if (zzje != null ? !zzje.equals(zfbZzxx.zzje) : zfbZzxx.zzje != null) return false;
        if (zzcpmc != null ? !zzcpmc.equals(zfbZzxx.zzcpmc) : zfbZzxx.zzcpmc != null) return false;
        return jyfsd != null ? jyfsd.equals(zfbZzxx.jyfsd) : zfbZzxx.jyfsd == null;
    }

    @Override
    public int hashCode() {
        int result = jyh != null ? jyh.hashCode() : 0;
        result = 31 * result + (fkfzfbzh != null ? fkfzfbzh.hashCode() : 0);
        result = 31 * result + (skfzfbzh != null ? skfzfbzh.hashCode() : 0);
        result = 31 * result + (skjgxx != null ? skjgxx.hashCode() : 0);
        result = 31 * result + (dzsj != null ? dzsj.hashCode() : 0);
        result = 31 * result + (zzje != null ? zzje.hashCode() : 0);
        result = 31 * result + (zzcpmc != null ? zzcpmc.hashCode() : 0);
        result = 31 * result + (jyfsd != null ? jyfsd.hashCode() : 0);
        return result;
    }
}
