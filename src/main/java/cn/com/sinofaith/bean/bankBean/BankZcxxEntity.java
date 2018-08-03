package cn.com.sinofaith.bean.bankBean;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "bank_zcxx" , schema = "",catalog = "")
public class BankZcxxEntity {
    private long id;
    private String zhzt;
    private String yhkkh;
    private String yhkzh;
    private String khxm;
    private String khzjh;
    private String khsj;
    private String khh;
    private BigDecimal zhye;
    private BigDecimal kyye;
    private long aj_id;
    private String inserttime;

    public BankZcxxEntity() {
        super();
    }

    public BankZcxxEntity(long id, String zhzt, String yhkzh, String khxm, String khzjh, String khsj, String khh,long aj_id,String inserttime) {
        this.id = id;
        this.zhzt = zhzt;
        this.yhkzh = yhkzh;
        this.khxm = khxm;
        this.khzjh = khzjh;
        this.khsj = khsj;
        this.khh = khh;
        this.aj_id = aj_id;
        this.inserttime = inserttime;
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
    @Column(name = "yhkkh",nullable = false,length = 50)
    public String getYhkkh() {
        return yhkkh;
    }

    public void setYhkkh(String yhkkh) {
        this.yhkkh = yhkkh;
    }

    @Basic
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

    @Basic
    @Column(name = "zhye",nullable = true,precision = 2)
    public BigDecimal getZhye() {
        return zhye;
    }

    public void setZhye(BigDecimal zhye) {
        this.zhye = zhye;
    }

    @Basic
    @Column(name = "kyye",nullable = true,precision = 2)
    public BigDecimal getKyye() {
        return kyye;
    }

    public void setKyye(BigDecimal kyye) {
        this.kyye = kyye;
    }

    @Basic
    @Column(name="aj_id",nullable = true,precision = 0)
    public long getAj_id(){ return aj_id;}
    public void setAj_id(long aj_id){this.aj_id = aj_id;}

    @Basic
    @Column(name = "inserttime",nullable = true,length = 20)
    public String getInserttime(){return inserttime;}
    public void setInserttime(String inserttime){this.inserttime=inserttime;}
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankZcxxEntity that = (BankZcxxEntity) o;

        if (aj_id != that.aj_id) return false;
        return yhkzh != null ? yhkzh.equals(that.yhkzh) : that.yhkzh == null;
    }

    @Override
    public int hashCode() {
        int result = yhkzh != null ? yhkzh.hashCode() : 0;
        result = 31 * result + (int) (aj_id ^ (aj_id >>> 32));
        return result;
    }

    public static BankZcxxEntity mapToObj(Map<Integer,Object> map, Map<String,Integer> title){
        BankZcxxEntity b = new BankZcxxEntity();
        try{
            b.setZhzt(map.get(title.get("zhzt")).toString());
            b.setYhkkh(map.get(title.get("yhkkh")).toString());
            b.setYhkzh(map.get(title.get("yhkzh")).toString());
            b.setKhxm(map.get(title.get("khxm")).toString());
            b.setKhzjh(map.get(title.get("khzjh")).toString());
            b.setKhsj(map.get(title.get("khsj")).toString());
            b.setKhh(map.get(title.get("khh")).toString());
            b.setKyye(new BigDecimal(map.get(title.get("kyye")).toString()));
            b.setZhye(new BigDecimal(map.get(title.get("zhye")).toString()));
        }catch (Exception e){
            e.getStackTrace();
        }
        return b;
    }
}
