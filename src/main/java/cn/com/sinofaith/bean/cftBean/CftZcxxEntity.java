package cn.com.sinofaith.bean.cftBean;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

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
    private long aj_id;
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

    @Basic
    @Column(name = "aj_id",nullable = true,precision = 0)
    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
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

    public static CftZcxxEntity listToObj(List<String> s, Map map){
        CftZcxxEntity a = new CftZcxxEntity();
        try{
            a.setZhzt(s.get((int)map.get("zhzt")));
            a.setZh(s.get((int)map.get("zh")));
            a.setXm(s.get((int)map.get("xm")));
            a.setZcsj(s.get((int)map.get("zcsj")));
            a.setSfzhm(s.get((int)map.get("sfzhm")).replace("[","").replace("]",""));
            a.setBdsj(s.get((int)map.get("bdsj")));
            a.setKhh(s.get((int)map.get("khh")));
            a.setYhzh(s.get((int)map.get("yhzh")).replace("[","").replace("]",""));
        }catch (Exception e){
            e.getMessage();
            System.out.println(e.getMessage());
        }
        return a;
    }
}