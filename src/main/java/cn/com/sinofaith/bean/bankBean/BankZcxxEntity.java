package cn.com.sinofaith.bean.bankBean;

import javax.persistence.*;

@Entity
@Table(name = "bank_zcxx" , schema = "",catalog = "")
public class BankZcxxEntity {
    private long id;
    private String zhzt;
    private String yhkzh;
    private String khxm;
    private String khzjh;
    private String khsj;
    private String khh;

    public BankZcxxEntity() {
        super();
    }

    public BankZcxxEntity(long id, String zhzt, String yhkzh, String khxm, String khzjh, String khsj, String khh) {
        this.id = id;
        this.zhzt = zhzt;
        this.yhkzh = yhkzh;
        this.khxm = khxm;
        this.khzjh = khzjh;
        this.khsj = khsj;
        this.khh = khh;
    }

    @Basic
    @Column(name = "id",nullable = false,precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "zhzt",nullable = true,length = 50)
    public String getZhzt() {
        return zhzt;
    }

    public void setZhzt(String zhzt) {
        this.zhzt = zhzt;
    }

    @Id
    @Column(name = "yhkzh",nullable = false,length = 50)
    public String getYhkzh() {
        return yhkzh;
    }

    public void setYhkzh(String yhkzh) {
        this.yhkzh = yhkzh;
    }


    @Basic
    @Column(name = "khxm",nullable = true,length = 50)
    public String getKhxm() {
        return khxm;
    }

    public void setKhxm(String khxm) {
        this.khxm = khxm;
    }


    @Basic
    @Column(name = "khzjh",nullable = true,length = 50)
    public String getKhzjh() {
        return khzjh;
    }

    public void setKhzjh(String khzjh) {
        this.khzjh = khzjh;
    }


    @Basic
    @Column(name = "khsj",nullable = true,length = 50)
    public String getKhsj() {
        return khsj;
    }

    public void setKhsj(String khsj) {
        this.khsj = khsj;
    }


    @Basic
    @Column(name = "khh",nullable = true,length = 100)
    public String getKhh() {
        return khh;
    }

    public void setKhh(String khh) {
        this.khh = khh;
    }

    @Override
    public String toString() {
        return "BankZcxxEntity{" +
                "id=" + id +
                ", zhzt='" + zhzt + '\'' +
                ", yhkzh='" + yhkzh + '\'' +
                ", khxm='" + khxm + '\'' +
                ", khzjh='" + khzjh + '\'' +
                ", khsj='" + khsj + '\'' +
                ", khh='" + khh + '\'' +
                '}';
    }
}
