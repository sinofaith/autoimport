package cn.com.sinofaith.bean.pyramidSaleBean;

import javax.persistence.*;

@Entity
@Table(name = "ps_hierarchy",schema = "",catalog = "")
public class PsHierarchyEntity {
    private long id;
    private String psId;
    private String sponsorId;
    private String path;
    private long tier;
    private long aj_id;
    private long directReferNum;
    @Id
    @Column(name="id",nullable = false,precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name="psid",nullable = true,length = 300)
    public String getPsId() {
        return psId;
    }

    public void setPsId(String psId) {
        this.psId = psId;
    }

    @Basic
    @Column(name="sponsorid",nullable = true,length = 300)
    public String getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(String sponsorId) {
        this.sponsorId = sponsorId;
    }

    @Basic
    @Column(name="path",nullable = true,length = 1000)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Basic
    @Column(name = "tier",nullable = false,precision = 0)
    public long getTier() {
        return tier;
    }

    public void setTier(long tier) {
        this.tier = tier;
    }

    @Basic
    @Column(name = "directrefernum",nullable = false,precision = 0)
    public long getDirectReferNum() {
        return directReferNum;
    }

    public void setDirectReferNum(long directReferNum) {
        this.directReferNum = directReferNum;
    }

    @Basic
    @Column(name = "aj_id",nullable = false,precision = 0)
    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
    }
}
