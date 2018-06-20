package cn.com.sinofaith.bean;


import cn.com.sinofaith.util.RemoveMessy;
import sun.security.provider.MD5;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Me. on 2018/5/22
 */
@Entity
@Table(name = "cft_zzxx",schema = "",catalog = "")
public class CftZzxxEntity {
    private long id;
    private String zh;
    private String jydh;
    private String jdlx;
    private String jylx;
    private BigDecimal jyje;
    private BigDecimal zhye;
    private String jysj;
    private String yhlx;
    private String jysm;
    private String shmc;
    private String fsf;
    private BigDecimal fsje;
    private String jsf;
    private String jssj;
    private BigDecimal jsje;
    private long aj_id;
    private String inserttime;

    @Id
    @Column(name = "id",nullable = false,precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Basic
    @Column(name = "zh",nullable = true,length = 30)
    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    @Basic
    @Column(name ="jydh",nullable = true,length = 50)
    public String getJydh() {
        return jydh;
    }

    public void setJydh(String jydh) {
        this.jydh = jydh;
    }

    @Basic
    @Column(name = "jdlx",nullable = true,length = 10)
    public String getJdlx() {
        return jdlx;
    }

    public void setJdlx(String jdlx) {
        this.jdlx = jdlx;
    }
    @Basic
    @Column(name="jylx",nullable = true,length = 20)
    public String getJylx() {
        return jylx;
    }

    public void setJylx(String jylx) {
        this.jylx = jylx;
    }
    @Basic
    @Column(name = "jyje",nullable = true,precision = 2)
    public BigDecimal getJyje() {
        return jyje;
    }

    public void setJyje(BigDecimal jyje) {
        this.jyje = jyje;
    }
    
    @Basic
    @Column(name = "zhye",nullable = true,precision = 2)
    public BigDecimal getZhye() {
        return zhye;
    }

    public void setZhye(BigDecimal zhye) {
        this.zhye = zhye;
    }

    @Basic
    @Column(name = "jysj",nullable = true,length = 19)
    public String getJysj() {
        return jysj;
    }

    public void setJysj(String jysj) {
        this.jysj = jysj;
    }

    @Basic
    @Column(name = "yhlx",nullable = true,length = 20)
    public String getYhlx() {
        return yhlx;
    }

    public void setYhlx(String yhlx) {
        this.yhlx = yhlx;
    }

    @Basic
    @Column(name = "jysm",nullable = true,length = 50)
    public String getJysm() {
        return jysm;
    }

    public void setJysm(String jysm) {
        this.jysm = jysm;
    }

    @Basic
    @Column(name = "shmc",nullable = true,length = 50)
    public String getShmc() {
        return shmc;
    }

    public void setShmc(String shmc) {
        this.shmc = shmc;
    }
    @Basic
    @Column(name = "fsf",nullable = true,length = 30)
    public String getFsf() {
        return fsf;
    }

    public void setFsf(String fsf) {
        this.fsf = fsf;
    }

    @Basic
    @Column(name = "fsje",nullable = true,precision = 2)
    public BigDecimal getFsje() {
        return fsje;
    }

    public void setFsje(BigDecimal fsje) {
        this.fsje = fsje;
    }
    @Basic
    @Column(name = "jsf",nullable = true,length = 30)
    public String getJsf() {
        return jsf;
    }

    public void setJsf(String jsf) {
        this.jsf = jsf;
    }
    @Basic
    @Column(name = "jssj",nullable = true,length = 19)
    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }
    @Basic
    @Column(name = "jsje",nullable = true,precision = 2)
    public BigDecimal getJsje() {
        return jsje;
    }

    public void setJsje(BigDecimal jsje) {
        this.jsje = jsje;
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

    @Override
    public String toString() {
        return "CftZzxxEntity{" +
                "id=" + id +
                ", zh='" + zh + '\'' +
                ", jydh='" + jydh + '\'' +
                ", jdlx='" + jdlx + '\'' +
                ", jylx='" + jylx + '\'' +
                ", jyje='" + jyje + '\'' +
                ", zhye='" + zhye + '\'' +
                ", jysj='" + jysj + '\'' +
                ", yhlx='" + yhlx + '\'' +
                ", jysm='" + jysm + '\'' +
                ", shmc='" + shmc + '\'' +
                ", fsf='" + fsf + '\'' +
                ", fsje='" + fsje + '\'' +
                ", jsf='" + jsf + '\'' +
                ", jssj='" + jssj + '\'' +
                ", jsje='" + jsje + '\'' +
                '}';
    }

    public static CftZzxxEntity listToObj(List<String> s){
        CftZzxxEntity a = new CftZzxxEntity();
        try {
        if(s.size()>=11){
            a.setZh(s.get(0).replace("[","").replace("]",""));
            a.setJydh(s.get(1).replace("[","").replace("]",""));
            a.setJdlx(s.get(2));
            a.setJylx(s.get(3));
            a.setJyje(BigDecimal.valueOf(Long.parseLong(s.get(4).replace("null","0"))/100.00));
            a.setZhye(BigDecimal.valueOf(Long.parseLong(s.get(5).replace("null","0"))/100.00));
            a.setJysj(s.get(6));
            a.setYhlx(s.get(7));
            a.setJysm(RemoveMessy.rMessy(s.get(8)));
            a.setShmc(s.get(9));
            a.setFsf(s.get(10));
            a.setFsje(BigDecimal.valueOf(Long.parseLong(s.get(11).replace("null","0"))/100.00));
            a.setJsf(s.get(12));
            a.setJssj(s.get(13));
            a.setJsje(BigDecimal.valueOf(Long.parseLong(s.get(14).replace("null","0"))/100.00));
        }
        if(s.size()<11){
            a.setZh(s.get(0).replace("[","").replace("]",""));
            a.setJydh(s.get(1).replace("[","").replace("]",""));
            a.setJdlx(s.get(2));
            a.setJylx(s.get(3));
            a.setJyje(BigDecimal.valueOf(Long.parseLong(s.get(4).replace("null","0"))/100.00));
            a.setZhye(BigDecimal.valueOf(Long.parseLong(s.get(5).replace("null","0"))/100.00));
            a.setJysj(s.get(6));
            a.setYhlx(s.get(7));
            a.setJysm(RemoveMessy.rMessy(s.get(8)));
            a.setShmc(s.get(9));
            a.setFsf(null);
            a.setFsje(new BigDecimal(0));
            a.setJsf(null);
            a.setJssj(null);
            a.setJsje(new BigDecimal(0));
        }
        }catch (Exception e){
            e.getMessage();
        }
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CftZzxxEntity that = (CftZzxxEntity) o;

        if (id != that.id) return false;
        if (zh != null ? !zh.equals(that.zh) : that.zh != null) return false;
        if (jydh != null ? !jydh.equals(that.jydh) : that.jydh != null) return false;
        if (jdlx != null ? !jdlx.equals(that.jdlx) : that.jdlx != null) return false;
        if (jylx != null ? !jylx.equals(that.jylx) : that.jylx != null) return false;
        if (jyje != null ? !jyje.equals(that.jyje) : that.jyje != null) return false;
        if (zhye != null ? !zhye.equals(that.zhye) : that.zhye != null) return false;
        if (jysj != null ? !jysj.equals(that.jysj) : that.jysj != null) return false;
        if (yhlx != null ? !yhlx.equals(that.yhlx) : that.yhlx != null) return false;
        if (jysm != null ? !jysm.equals(that.jysm) : that.jysm != null) return false;
        if (shmc != null ? !shmc.equals(that.shmc) : that.shmc != null) return false;
        if (fsf != null ? !fsf.equals(that.fsf) : that.fsf != null) return false;
        if (fsje != null ? !fsje.equals(that.fsje) : that.fsje != null) return false;
        if (jsf != null ? !jsf.equals(that.jsf) : that.jsf != null) return false;
        if (jssj != null ? !jssj.equals(that.jssj) : that.jssj != null) return false;
        return jsje != null ? jsje.equals(that.jsje) : that.jsje == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (zh != null ? zh.hashCode() : 0);
        result = 31 * result + (jydh != null ? jydh.hashCode() : 0);
        result = 31 * result + (jdlx != null ? jdlx.hashCode() : 0);
        result = 31 * result + (jylx != null ? jylx.hashCode() : 0);
        result = 31 * result + (jyje != null ? jyje.hashCode() : 0);
        result = 31 * result + (zhye != null ? zhye.hashCode() : 0);
        result = 31 * result + (jysj != null ? jysj.hashCode() : 0);
        result = 31 * result + (yhlx != null ? yhlx.hashCode() : 0);
        result = 31 * result + (jysm != null ? jysm.hashCode() : 0);
        result = 31 * result + (shmc != null ? shmc.hashCode() : 0);
        result = 31 * result + (fsf != null ? fsf.hashCode() : 0);
        result = 31 * result + (fsje != null ? fsje.hashCode() : 0);
        result = 31 * result + (jsf != null ? jsf.hashCode() : 0);
        result = 31 * result + (jssj != null ? jssj.hashCode() : 0);
        result = 31 * result + (jsje != null ? jsje.hashCode() : 0);
        return result;
    }
}