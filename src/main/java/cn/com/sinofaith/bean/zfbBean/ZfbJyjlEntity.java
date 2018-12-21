package cn.com.sinofaith.bean.zfbBean;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ZfbJyjl")
public class ZfbJyjlEntity {
    private long id;
    private String jyh;
    private String wbjyh;
    private String jyzt;
    private String hzhbId;
    private String mjyhId;
    private String mjxx;
    private String mijyhId;
    private String mijxx;
    private double jyje;
    private String sksj;
    private String zhxgsj;
    private String cjsj;
    private String jylx;
    private String lyd;
    private String spmc;
    private String shrdz;
    private String dyxcsj;
    private String inserttime;
    private long aj_id;

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator="zfb_jyjlnum")
    @SequenceGenerator(name="zfb_jyjlnum",sequenceName="SEQ_ZFBJYJL_ID",allocationSize=1,initialValue=1)
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
    @Column(name = "wbjyh",nullable = true,length = 100)
    public String getWbjyh() {
        return wbjyh;
    }

    public void setWbjyh(String wbjyh) {
        this.wbjyh = wbjyh;
    }

    @Basic
    @Column(name = "jyzt",nullable = true,length = 100)
    public String getJyzt() {
        return jyzt;
    }

    public void setJyzt(String jyzt) {
        this.jyzt = jyzt;
    }

    @Basic
    @Column(name = "hzhbId",nullable = true,length = 100)
    public String getHzhbId() {
        return hzhbId;
    }

    public void setHzhbId(String hzhbId) {
        this.hzhbId = hzhbId;
    }

    @Basic
    @Column(name = "mjyhId",nullable = true,length = 100)
    public String getMjyhId() {
        return mjyhId;
    }

    public void setMjyhId(String mjyhId) {
        this.mjyhId = mjyhId;
    }

    @Basic
    @Column(name = "mjxx",nullable = true,length = 300)
    public String getMjxx() {
        return mjxx;
    }

    public void setMjxx(String mjxx) {
        this.mjxx = mjxx;
    }

    @Basic
    @Column(name = "mijyhId",nullable = true,length = 100)
    public String getMijyhId() {
        return mijyhId;
    }

    public void setMijyhId(String mijyhId) {
        this.mijyhId = mijyhId;
    }

    @Basic
    @Column(name = "mijxx",nullable = true,length = 300)
    public String getMijxx() {
        return mijxx;
    }

    public void setMijxx(String mijxx) {
        this.mijxx = mijxx;
    }

    @Basic
    @Column(name = "jyje",nullable = true,precision = 0)
    public double getJyje() {
        return jyje;
    }

    public void setJyje(double jyje) {
        this.jyje = jyje;
    }

    @Basic
    @Column(name = "sksj",nullable = true,length = 100)
    public String getSksj() {
        return sksj;
    }

    public void setSksj(String sksj) {
        if(sksj!=null){
            this.sksj = sksj;
        }else{
            this.sksj = "";
        }
    }

    @Basic
    @Column(name = "zhxgsj",nullable = true,length = 100)
    public String getZhxgsj() {
        return zhxgsj;
    }

    public void setZhxgsj(String zhxgsj) {
        this.zhxgsj = zhxgsj;
    }

    @Basic
    @Column(name = "cjsj",nullable = true,length = 100)
    public String getCjsj() {
        return cjsj;
    }

    public void setCjsj(String cjsj) {
        this.cjsj = cjsj;
    }

    @Basic
    @Column(name = "jylx",nullable = true,length = 100)
    public String getJylx() {
        return jylx;
    }

    public void setJylx(String jylx) {
        this.jylx = jylx;
    }

    @Basic
    @Column(name = "lyd",nullable = true,length = 300)
    public String getLyd() {
        return lyd;
    }

    public void setLyd(String lyd) {
        this.lyd = lyd;
    }

    @Basic
    @Column(name = "spmc",nullable = true,length = 300)
    public String getSpmc() {
        return spmc;
    }

    public void setSpmc(String spmc) {
        this.spmc = spmc;
    }

    @Basic
    @Column(name = "shrdz",nullable = true,length = 300)
    public String getShrdz() {
        return shrdz;
    }

    public void setShrdz(String shrdz) {
        if(shrdz!=null){
            this.shrdz = shrdz;
        }else{
            this.shrdz = "";
        }
    }

    @Basic
    @Column(name = "dyxcsj",nullable = true,length = 100)
    public String getDyxcsj() {
        return dyxcsj;
    }

    public void setDyxcsj(String dyxcsj) {
        this.dyxcsj = dyxcsj;
    }

    @Basic
    @Column(name = "insert_time",nullable = true,length = 19)
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

    @Override
    public String toString() {
        return "ZfbJyjlEntity{" +
                "id=" + id +
                ", jyh='" + jyh + '\'' +
                ", wbjyh='" + wbjyh + '\'' +
                ", jyzt='" + jyzt + '\'' +
                ", hzhbId='" + hzhbId + '\'' +
                ", mjyhId='" + mjyhId + '\'' +
                ", mjxx='" + mjxx + '\'' +
                ", mijyhId='" + mijyhId + '\'' +
                ", mijxx='" + mijxx + '\'' +
                ", jyje='" + jyje + '\'' +
                ", sksj='" + sksj + '\'' +
                ", zhxgsj='" + zhxgsj + '\'' +
                ", cjsj='" + cjsj + '\'' +
                ", jylx='" + jylx + '\'' +
                ", lyd='" + lyd + '\'' +
                ", spmc='" + spmc + '\'' +
                ", shrdz='" + shrdz + '\'' +
                ", dyxcsj='" + dyxcsj + '\'' +
                ", inserttime='" + inserttime + '\'' +
                ", aj_id=" + aj_id +
                '}';
    }

    /**
     * 转化
     * @param list
     * @return
     */
    public static List<ZfbJyjlEntity> listToZfbJyjls(List<Object[]> list) {
        List<ZfbJyjlEntity> zfbJyjlList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ZfbJyjlEntity zfbJyjl = new ZfbJyjlEntity();
            Object[] str = list.get(i);
            if(str[0]!=null && str[0].toString()!=null){
                zfbJyjl.setJyh(str[0].toString());
            }else{
                zfbJyjl.setJyh("");
            }
            if(str[1]!=null && str[1].toString()!=null){
                zfbJyjl.setMjyhId(str[1].toString());
            }else{
                zfbJyjl.setMjyhId("");
            }
            if(str[2]!=null && str[2].toString()!=null){
                zfbJyjl.setMjxx(str[2].toString());
            }else{
                zfbJyjl.setMjxx("");
            }
            if(str[3]!=null && str[3].toString()!=null){
                zfbJyjl.setMijyhId(str[3].toString());
            }else{
                zfbJyjl.setMijyhId("");
            }
            if(str[4]!=null && str[4].toString()!=null){
                zfbJyjl.setMijxx(str[4].toString());
            }else{
                zfbJyjl.setMijxx("");
            }
            if(str[5]!=null && str[5].toString()!=null){
                zfbJyjl.setJyje(Double.parseDouble(str[5].toString()));
            }else{
                zfbJyjl.setJyje(0l);
            }
            if(str[6]!=null && str[6].toString()!=null){
                zfbJyjl.setSksj(str[6].toString());
            }else{
                zfbJyjl.setSksj("");
            }
            if(str[7]!=null && str[7].toString()!=null){
                zfbJyjl.setJylx(str[7].toString());
            }else{
                zfbJyjl.setJylx("");
            }
            if(str[8]!=null && str[8].toString()!=null){
                zfbJyjl.setSpmc(str[8].toString());
            }else{
                zfbJyjl.setSpmc("");
            }
            if(str[9]!=null && str[9].toString()!=null){
                zfbJyjl.setShrdz(str[9].toString());
            }else{
                zfbJyjl.setShrdz("");
            }
            zfbJyjlList.add(zfbJyjl);
        }
        return zfbJyjlList;
    }
}
