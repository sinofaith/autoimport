package cn.com.sinofaith.bean.bankBean;

import javax.persistence.*;

@Entity
@Table(name="bank_person",schema = "",catalog = "")
public class BankPersonEntity {
    private long id;
    private String yhkkh;
    private String yhkzh;
    private String xm;
//    private String khh;

    @Id
    @Column(name="id",nullable = false,precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name="yhkkh",nullable = false,length = 200)
    public String getYhkkh() {
        return yhkkh;
    }

    public void setYhkkh(String yhkkh) {
        this.yhkkh = yhkkh;
    }
    @Basic
    @Column(name="yhkzh",nullable = true,length = 200)
    public String getYhkzh() {
        return yhkzh;
    }

    public void setYhkzh(String yhkzh) {
        this.yhkzh = yhkzh;
    }
    @Basic
    @Column(name="khxm",nullable = true,length = 200)
    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }
//    @Basic
//    @Column(name="khh",nullable = true,length = 200)
//    public String getKhh() {
//        return khh;
//    }
//
//    public void setKhh(String khh) {
//        this.khh = khh;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankPersonEntity that = (BankPersonEntity) o;

        if (yhkkh != null ? !yhkkh.equals(that.yhkkh) : that.yhkkh != null) return false;
        return xm != null ? xm.equals(that.xm) : that.xm == null;
    }

    @Override
    public int hashCode() {
        int result = yhkkh != null ? yhkkh.hashCode() : 0;
        result = 31 * result + (xm != null ? xm.hashCode() : 0);
        return result;
    }
}
