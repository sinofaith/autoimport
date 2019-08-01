package cn.com.sinofaith.bean;

import javax.persistence.*;

@Entity
@Table(name = "rel_zjh_hm",schema = "",catalog = "")
public class RelZjhHmEntity {
    private String zjh;
    private String hm;
    private long hmlx; //1 银行卡号 2 微信 3 支付宝 4 手机号
    private long hmly; //1 银行注册 2 财付通注册信息 3 支付宝注册信息 4 银行人员信息表
    private long aj_id;

    @Id
    @Column(name = "zjh", nullable = false,length = 255)
    public String getZjh() {
        return zjh;
    }

    public void setZjh(String zjh) {
        this.zjh = zjh;
    }

    @Basic
    @Column(name = "hm", nullable = false,length = 255)
    public String getHm() {
        return hm;
    }

    public void setHm(String hm) {
        this.hm = hm;
    }
    @Basic
    @Column(name = "hmlx",nullable = false,precision = 0)
    public long getHmlx() {
        return hmlx;
    }

    public void setHmlx(long hmlx) {
        this.hmlx = hmlx;
    }
    @Basic
    @Column(name = "hmly",nullable = false,precision = 0)
    public long getHmly() {
        return hmly;
    }

    public void setHmly(long hmly) {
        this.hmly = hmly;
    }
    @Basic
    @Column(name = "aj_id")
    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
    }
}
