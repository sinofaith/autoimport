package cn.com.sinofaith.bean.bankBean;

import javax.persistence.*;

@Entity
@Table(name="bank_person",schema = "",catalog = "")
public class BankPersonEntity {
    private long id;
    private String yhkkh;
    private String yhkzh;
    private String xm;
    private long dsfzh = 0;

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

    @Basic
    @Column(name = "dsfzh",nullable = false,precision = 0)
    public long getDsfzh() {
        return dsfzh;
    }

    public void setDsfzh(long dsfzh) {
        this.dsfzh = dsfzh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankPersonEntity that = (BankPersonEntity) o;

        return yhkkh != null ? yhkkh.equals(that.yhkkh) : that.yhkkh == null;
    }

    @Override
    public int hashCode() {
        return yhkkh != null ? yhkkh.hashCode() : 0;
    }

    public BankPersonEntity getByZcxx(BankZcxxEntity bce){
        BankPersonEntity bpe = new BankPersonEntity();
        bpe.setXm(bce.getKhxm());
        bpe.setYhkkh(bce.getYhkkh());
        bpe.setYhkzh(bce.getYhkzh());
        boolean temp = bce.getKhxm().contains("财付通")||bce.getKhxm().contains("支付")||bce.getKhxm().contains("清算")
                || bce.getKhxm().contains("特约") || bce.getKhxm().contains("备付金")|| bce.getKhxm().contains("银行")
                || bce.getKhxm().contains("银联") || bce.getKhxm().contains("保险") || bce.getKhxm().contains("过渡")
                || bce.getKhxm().contains("美团");
        if(temp){
            bpe.setDsfzh(1);
        }
        return bpe;
    }
}
