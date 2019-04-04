package cn.com.sinofaith.bean.pyramidSaleBean;

import javax.persistence.*;

@Entity
@Table(name = "ps_hierarchy",schema = "",catalog = "")
public class PsHierarchyEntity {
    private long id;
    private String psId;
    private String sponsorId;
    private String nick_name;
    private String path;
    private Long tier;
    private long aj_id;
    private Long directReferNum;
    private Long containsTier;
    private Long directDrive;
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
    @Column(name="nick_name",nullable = true,length = 300)
    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
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
    public Long getDirectReferNum() {
        return directReferNum;
    }

    public void setDirectReferNum(Long directReferNum) {
        /*if(directReferNum==null){this.directReferNum = 0l;}else{*/this.directReferNum = directReferNum;/*}*/
    }

    @Basic
    @Column(name = "containsTier",nullable = false,precision = 0)
    public Long getContainsTier() {
        return containsTier;
    }

    public void setContainsTier(Long containsTier) {
        /*if(containsTier==null){this.containsTier = 0l;}else{*/this.containsTier = containsTier;/*}*/
    }

    @Basic
    @Column(name = "aj_id",nullable = false,precision = 0)
    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
    }

    @Basic
    @Column(name = "directDrive",nullable = false,precision = 0)
    public Long getDirectDrive() {
        return directDrive;
    }

    public void setDirectDrive(Long directDrive) {
        /*if(directDrive!=null){*/
            this.directDrive = directDrive;
        /*}else{
            this.directDrive = 0l;
        }*/
    }

    @Override
    public String toString() {
        return "PsHierarchyEntity{" +
                "id=" + id +
                ", psId='" + psId + '\'' +
                ", sponsorId='" + sponsorId + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", path='" + path + '\'' +
                ", tier=" + tier +
                ", aj_id=" + aj_id +
                ", directReferNum=" + directReferNum +
                ", containsTier=" + containsTier +
                ", directDrive=" + directDrive +
                '}';
    }
}
