package cn.com.sinofaith.bean.wlBean;

import javax.persistence.*;

@Entity
@Table(name = "wuliu_sj",schema = "",catalog = "")
public class WuliuSjEntity {
    private long id;
    private String addressee;
    private String sj_phone;
    private String sj_address;
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
    @Column(name = "addressee",nullable = true,length = 100)
    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    @Basic
    @Column(name = "sj_phone",nullable = true,length = 100)
    public String getSj_phone() {
        return sj_phone;
    }

    public void setSj_phone(String sj_phone) {
        this.sj_phone = sj_phone;
    }

    @Basic
    @Column(name = "sj_address",nullable = true,length = 300)
    public String getSj_address() {
        return sj_address;
    }

    public void setSj_address(String sj_address) {
        this.sj_address = sj_address;
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
        return "WuliuSjEntity{" +
                "id=" + id +
                ", addressee='" + addressee + '\'' +
                ", sj_phone='" + sj_phone + '\'' +
                ", sj_address='" + sj_address + '\'' +
                ", num=" + num +
                ", aj_id=" + aj_id +
                '}';
    }
}
