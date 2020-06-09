package cn.com.sinofaith.bean.cftBean;


import cn.com.sinofaith.util.RemoveMessy;

import javax.persistence.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Me. on 2018/5/22
 */
@Entity
@Table(name = "cft_zzxx",schema = "",catalog = "")
public class CftZzxxEntity {
    private long id;
    private String zh="";
    private String jydh="";
    private String jdlx="";
    private String jylx="";
    private BigDecimal jyje = new BigDecimal(0);
    private BigDecimal zhye = new BigDecimal(0);
    private String jysj="";
    private String yhlx="";
    private String jysm="";
    private String shmc="";
    private String fsf=null;
    private BigDecimal fsje = new BigDecimal(0);
    private String jsf=null;
    private String jssj=null;
    private BigDecimal jsje = new BigDecimal(0);
    private long aj_id;
    private String inserttime="";

    @Id
    @Column(name = "id",nullable = false,precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Basic
    @Column(name = "zh",nullable = true,length = 200)
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
    @Column(name="jylx",nullable = true,length = 200)
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
    @Column(name = "yhlx",nullable = true,length = 100)
    public String getYhlx() {
        return yhlx;
    }

    public void setYhlx(String yhlx) {
        this.yhlx = yhlx;
    }

    @Basic
    @Column(name = "jysm",nullable = true,length = 300)
    public String getJysm() {
        return jysm;
    }

    public void setJysm(String jysm) {
        this.jysm = jysm;
    }

    @Basic
    @Column(name = "shmc",nullable = true,length = 300)
    public String getShmc() {
        return shmc;
    }

    public void setShmc(String shmc) {
        this.shmc = shmc;
    }
    @Basic
    @Column(name = "fsf",nullable = true,length = 200)
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
    @Column(name = "jsf",nullable = true,length = 200)
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

    public static void main(String[] args) {

    }

    public String  getBs(CftZzxxEntity x){
        return (x.getZh()+x.getJydh()+x.getJysm()+x.getShmc()+x.getJylx()+x.getJyje().doubleValue()+
                x.getFsf()+x.getJssj()+x.getJysj()+x.getJsf()+
                x.getJsje().doubleValue()).replace("null","");
    }

    public static CftZzxxEntity listToObj(List<String> s){
        CftZzxxEntity a = new CftZzxxEntity();
        try {
            if(s.size()>=11){
                a.setZh(s.get(0).replace("[","").replace("]",""));
                a.setJydh(s.get(1).replace("[","").replace("]",""));
                a.setJdlx(s.get(2));
                a.setJylx(s.get(3));
                a.setJyje(new BigDecimal(Long.parseLong(s.get(4).replace("null","0"))).divide(new BigDecimal(100)));
                a.setZhye(new BigDecimal(Long.parseLong(s.get(5).replace("null","0"))).divide(new BigDecimal(100)));
                a.setJysj(s.get(6));
                a.setYhlx(s.get(7));
                a.setJysm(RemoveMessy.rMessy(s.get(8)).equals("") ? null: RemoveMessy.rMessy(s.get(8)));
                a.setShmc(s.get(9).equals("") ? null:s.get(9));
                a.setFsf(s.get(10).replace("null","").equals("") ? null:s.get(10).replace("null",""));
                a.setFsje(new BigDecimal(Long.parseLong(s.get(11).replace("null","0"))).divide(new BigDecimal(100)));
                a.setJsf(s.get(12).replace("null","").equals("") ? null:s.get(12).replace("null",""));
                a.setJssj(s.get(13).equals("") ? null:s.get(13));
                a.setJsje(new BigDecimal(Long.parseLong(s.get(14).replace("null","0"))).divide(new BigDecimal(100)));
            }
            if(s.size()<11){
                a.setZh(s.get(0).replace("[","").replace("]",""));
                a.setJydh(s.get(1).replace("[","").replace("]",""));
                a.setJdlx(s.get(2));
                a.setJylx(s.get(3));
                a.setJyje(new BigDecimal(Long.parseLong(s.get(4).replace("null","0"))).divide(new BigDecimal(100)));
                a.setZhye(new BigDecimal(Long.parseLong(s.get(5).replace("null","0"))).divide(new BigDecimal(100)));
                a.setJysj(s.get(6));
                a.setYhlx(s.get(7));
                a.setJysm(RemoveMessy.rMessy(s.get(8)).equals("") ? null: RemoveMessy.rMessy(s.get(8)));
                a.setShmc(s.get(9));
                a.setFsf(null);
                a.setFsje(new BigDecimal(0));
                a.setJsf(null);
                a.setJssj(null);
                a.setJsje(new BigDecimal(0));
            }
            if(null == a.getFsf()&&!a.getZh().equals(a.getJsf())){
                a.setFsf(a.getZh());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CftZzxxEntity that = (CftZzxxEntity) o;

        if (zh != null ? !zh.equals(that.zh) : that.zh != null) return false;
        if (jydh != null ? !jydh.equals(that.jydh) : that.jydh != null&&!that.jydh.equals("") ) return false;
        if (jylx != null ? !jylx.equals(that.jylx) : that.jylx != null&&!that.jylx.equals("") ) return false;
        if (jyje != null ? jyje.compareTo(that.jyje)!=0 : that.jyje != null &&that.jyje.compareTo(new BigDecimal(0))!=0) return false;
        if (jysj != null ? !jysj.equals(that.jysj) : that.jysj != null&&!that.jysj.equals("") ) return false;
        if (jysm != null ? !jysm.equals(that.jysm) : that.jysm != null&&!that.jysm.equals("") ) return false;
        if (shmc != null ? !shmc.equals(that.shmc) : that.shmc != null&&!that.shmc.equals("") ) return false;
        if (fsf != null ? !fsf.equals(that.fsf) : that.fsf != null&&!that.fsf.equals("") ) return false;
        if (jsf != null ? !jsf.equals(that.jsf) : that.jsf != null&&!that.jsf.equals("") ) return false;
        if (jssj != null ? !jssj.equals(that.jssj) : that.jssj != null&&!that.jssj.equals("") ) return false;
        return jsje != null ? jsje.compareTo(that.jsje)==0 : that.jsje == null&&that.jsje.compareTo(new BigDecimal(0))==0;
    }

    @Override
    public int hashCode() {
        int result = zh != null ? zh.hashCode() : 0;
        result = 31 * result + (jydh != null ? jydh.hashCode() : 0);
        result = 31 * result + (jylx != null ? jylx.hashCode() : 0);
        result = 31 * result + (jyje != null ? jyje.hashCode() : 0);
        result = 31 * result + (jysj != null ? jysj.hashCode() : 0);
        result = 31 * result + (jysm != null ? jysm.hashCode() : 0);
        result = 31 * result + (shmc != null ? shmc.hashCode() : 0);
        result = 31 * result + (fsf != null ? fsf.hashCode() : 0);
        result = 31 * result + (jsf != null ? jsf.hashCode() : 0);
        result = 31 * result + (jssj != null ? jssj.hashCode() : 0);
        result = 31 * result + (jsje != null ? jsje.hashCode() : 0);
        return result;
    }
}