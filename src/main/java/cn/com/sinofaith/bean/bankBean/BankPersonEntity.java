package cn.com.sinofaith.bean.bankBean;

import javax.persistence.*;

@Entity
@Table(name="bank_person",schema = "",catalog = "")
public class BankPersonEntity {
    private long id;
    private String yhkkh;
    private String yhkzh;
    private String xm;

    @Id
    @Column(name="id",nullable = false,precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name="yhkkh",nullable = false,length = 50)
    public String getYhkkh() {
        return yhkkh;
    }

    public void setYhkkh(String yhkkh) {
        this.yhkkh = yhkkh;
    }
    @Basic
    @Column(name="yhkzh",nullable = true,length = 50)
    public String getYhkzh() {
        return yhkzh;
    }

    public void setYhkzh(String yhkzh) {
        this.yhkzh = yhkzh;
    }
    @Basic
    @Column(name="khxm",nullable = true,length = 50)
    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }
}
