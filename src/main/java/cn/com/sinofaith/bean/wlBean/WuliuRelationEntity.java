package cn.com.sinofaith.bean.wlBean;

import javax.persistence.*;

@Entity
@Table(name = "wuliu_relation",schema = "",catalog = "")
public class WuliuRelationEntity {
    // id
    private long id;
    // 寄件人
    private String sender;
    // 寄件电话
    private String ship_phone;
    // 收件地址
    private String ship_address;
    // 收件人
    private String addressee;
    // 收件电话
    private String sj_phone;
    // 收件地址
    private String sj_address;
    // 寄收次数
    private long num;
    // 案件id
    private long aj_id;

    @Id
    @Column(name = "id",nullable = false,precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ship_phone",nullable = true,length = 30)
    public String getShip_phone() {
        return ship_phone;
    }

    public void setShip_phone(String ship_phone) {
        this.ship_phone = ship_phone;
    }

    @Basic
    @Column(name = "sj_phone",nullable = true,length = 30)
    public String getSj_phone() {
        return sj_phone;
    }

    public void setSj_phone(String sj_phone) {
        this.sj_phone = sj_phone;
    }

    @Basic
    @Column(name = "num",nullable = true,precision = 0)
    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
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
    @Column(name = "sender",nullable = true,precision = 100)
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Basic
    @Column(name = "ship_address",nullable = true,precision = 100)
    public String getShip_address() {
        return ship_address;
    }

    public void setShip_address(String ship_address) {
        this.ship_address = ship_address;
    }

    @Basic
    @Column(name = "addressee",nullable = true,precision = 100)
    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    @Basic
    @Column(name = "sj_address",nullable = true,precision = 100)
    public String getSj_address() {
        return sj_address;
    }

    public void setSj_address(String sj_address) {
        this.sj_address = sj_address;
    }

    @Override
    public String toString() {
        return "WuliuRelationEntity{" +
                "id=" + id +
                ", ship_phone='" + ship_phone + '\'' +
                ", sj_phone='" + sj_phone + '\'' +
                ", num=" + num +
                ", aj_id=" + aj_id +
                '}';
    }
}
