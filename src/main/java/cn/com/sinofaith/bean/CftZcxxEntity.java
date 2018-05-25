package cn.com.sinofaith.bean;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Me. on 2018/5/21
 */
@Entity
@Table(name = "cft_zcxx" , schema = "",catalog = "")
public class CftZcxxEntity {
    private long id;
    private String zhzt;
    private String zh;
    private String xm;
    private String zcsj;
    private String sfzhm;
    private String bdsj;
    private String khh;
    private String yhzh;
    private String inserttime;

    @Basic
    @Column(name = "ID", nullable = false,precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name="zhzt",nullable = true,length = 20)
    public String getZhzt() {
        return zhzt;
    }

    public void setZhzt(String zhzt) {
        this.zhzt = zhzt;
    }

    @Basic
    @Column(name="zh",nullable = true,length = 20)
    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    @Basic
    @Column(name="xm",nullable = true,length = 20)
    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    @Basic
    @Column(name = "zcsj",nullable = true,length = 19)
    public String getZcsj() {
        return zcsj;
    }

    public void setZcsj(String zcsj) {
        this.zcsj = zcsj;
    }

    @Basic
    @Column(name = "sfzhm",nullable = true,length = 18)
    public String getSfzhm() {
        return sfzhm;
    }

    public void setSfzhm(String sfzhm) {
        this.sfzhm=sfzhm;
    }

    @Basic
    @Column(name = "bdsj",nullable = true,length = 11)
    public String getBdsj() {
        return bdsj;
    }

    public void setBdsj(String bdsj) {
        this.bdsj = bdsj;
    }

    @Basic
    @Column(name = "khh",nullable = true,length = 30)
    public String getKhh() {
        return khh;
    }

    public void setKhh(String khh) {
        this.khh = khh;
    }

    @Id
    @Column(name="yhzh",nullable = false,length = 20)
    public String getYhzh() {
        return yhzh;
    }

    public void setYhzh(String yhzh) {
            this.yhzh=yhzh;
    }

    @Basic
    @Column(name = "inserttime",nullable = true,length = 19)
    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    public void setNull() {
        this.id = 0;
        this.zhzt = null;
        this.zh = null;
        this.xm = null;
        this.zcsj = null;
        this.sfzhm = null;
        this.bdsj = null;
        this.khh = null;
        this.yhzh = null;
        this.inserttime = null;
    }

    @Override
    public String toString() {
        return "Cft_zcxx{" +
                "id='" + id + '\'' +
                ", zhzt='" + zhzt + '\'' +
                ", zh='" + zh + '\'' +
                ", xm='" + xm + '\'' +
                ", zcsj='" + zcsj + '\'' +
                ", sfzhm='" + sfzhm + '\'' +
                ", bdsj='" + bdsj + '\'' +
                ", khh='" + khh + '\'' +
                ", yhzh='" + yhzh + '\'' +
                ", inserttime='"+inserttime+'\''+
                '}';
    }

    public static CftZcxxEntity listToObj(List<String> s){
        CftZcxxEntity a = new CftZcxxEntity();
        a.setZhzt(s.get(0));
        a.setZh(s.get(1));
        a.setXm(s.get(2));
        a.setZcsj(s.get(3));
        a.setSfzhm(s.get(4).replace("[","").replace("]",""));
        a.setBdsj(s.get(5));
        a.setKhh(s.get(6));
        a.setYhzh(s.get(7).replace("[","").replace("]",""));
        return a;
    }
}