package cn.com.sinofaith.bean.zfbBean;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ZFBJYJL_SJDZS")
public class ZfbJyjlSjdzsEntity {
    private long id;
    private String mjyhid;
    private String mjxx;
    private long sjzcs;
    private BigDecimal czzje = new BigDecimal(0);
    private long sjdzs;
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
    @Column(name = "mjyhid",nullable = true,length = 100)
    public String getMjyhid() {
        return mjyhid;
    }

    public void setMjyhid(String mjyhid) {
        this.mjyhid = mjyhid;
    }

    @Basic
    @Column(name = "mjxx",nullable = true,length = 100)
    public String getMjxx() {
        return mjxx;
    }

    public void setMjxx(String mjxx) {
        this.mjxx = mjxx;
    }

    @Basic
    @Column(name = "sjzcs",nullable = false,precision = 0)
    public long getSjzcs() {
        return sjzcs;
    }

    public void setSjzcs(long sjzcs) {
        this.sjzcs = sjzcs;
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
    @Column(name = "sjdzs",nullable = false,precision = 0)
    public long getSjdzs() {
        return sjdzs;
    }

    public void setSjdzs(long sjdzs) {
        this.sjdzs = sjdzs;
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

    @Override
    public String toString() {
        return "ZfbJyjlSjdzsEntity{" +
                "id=" + id +
                ", mjyhid='" + mjyhid + '\'' +
                ", mjxx='" + mjxx + '\'' +
                ", sjzcs=" + sjzcs +
                ", czzje=" + czzje +
                ", sjdzs=" + sjdzs +
                ", insert_time='" + insert_time + '\'' +
                ", aj_id=" + aj_id +
                '}';
    }
}
