package cn.com.sinofaith.bean;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cft_person",schema = "",catalog = "")
public class CftPersonEntity {
    private long id;
    private String xm;
    private String sfzhm;
    private String zh;

    @Id
    @Column(name = "id",nullable = false,precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Basic
    @Column(name="xm",nullable = true)
    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }
    @Basic
    @Column(name="sfzhm",nullable = true)
    public String getSfzhm() {
        return sfzhm;
    }

    public void setSfzhm(String sfzhm) {
        this.sfzhm = sfzhm;
    }
    @Basic
    @Column(name="zh",nullable = false)
    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public static CftPersonEntity listToObj(List<String> s){
        CftPersonEntity cp = new CftPersonEntity();
        try{
            cp.setZh(s.get(1));
            cp.setXm(s.get(2));
            cp.setSfzhm(s.get(4).replace("[","").replace("]",""));
        }catch (Exception e){
            e.getMessage();
        }
        return cp;
    }
}
