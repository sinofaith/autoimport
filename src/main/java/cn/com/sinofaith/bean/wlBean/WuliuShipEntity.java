package cn.com.sinofaith.bean.wlBean;

import javax.persistence.*;

@Entity
@Table(name = "wuliu_ship",schema = "",catalog = "")
public class WuliuShipEntity {
    private long id;
    private String sender;
    private String ship_phone;
    private String ship_address;
    private long num;
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
    @Column(name = "sender",nullable = true,length = 100)
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Basic
    @Column(name = "ship_address",nullable = true,length = 100)
    public String getShip_address() {
        return ship_address;
    }

    public void setShip_address(String ship_address) {
        this.ship_address = ship_address;
    }

    @Basic
    @Column(name = "ship_phone",nullable = true,length = 100)
    public String getShip_phone() {
        return ship_phone;
    }

    public void setShip_phone(String ship_phone) {
        this.ship_phone = ship_phone;
    }

    @Basic
    @Column(name = "num",nullable = false,precision = 0)
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

    @Override
    public String toString() {
        return "WuliuShipEntity{" +
                "id=" + id +
                ", ship_phone='" + ship_phone + '\'' +
                ", num=" + num +
                ", aj_id=" + aj_id +
                '}';
    }
}
