package cn.com.sinofaith.bean.bankBean;

import cn.com.sinofaith.util.TimeFormatUtil;

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
    private BigDecimal zhye = new BigDecimal(-1);
    private BigDecimal kyye = new BigDecimal(-1);
    private long zhlx =1;
    private long aj_id;
    private String inserttime;
    private String zhlxy;

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

    @Id
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

    @Basic
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
    @Column(name = "zhlx",nullable = false,precision = 0)
    public long getZhlx() {
        return zhlx;
    }

    public void setZhlx(long zhlx) {
        this.zhlx = zhlx;
    }

    @Basic
    @Column(name="aj_id",nullable = true,precision = 0)
    public long getAj_id(){ return aj_id;}
    public void setAj_id(long aj_id){this.aj_id = aj_id;}

    @Basic
    @Column(name = "inserttime",nullable = true,length = 20)
    public String getInserttime(){return inserttime;}
    public void setInserttime(String inserttime){this.inserttime=inserttime;}

    @Basic
    @Column(name = "zhlxy",nullable = true,length = 100)
    public String getZhlxy() {
        return zhlxy;
    }

    public void setZhlxy(String zhlxy) {
        this.zhlxy = zhlxy;
    }
    @Override
    public String toString() {
        return "BankZcxxEntity{" +
                "id=" + id +
                ", zhzt='" + zhzt + '\'' +
                ", yhkkh='" + yhkkh + '\'' +
                ", yhkzh='" + yhkzh + '\'' +
                ", khxm='" + khxm + '\'' +
                ", khzjh='" + khzjh + '\'' +
                ", khsj='" + khsj + '\'' +
                ", khh='" + khh + '\'' +
                ", zhye=" + zhye +
                ", kyye=" + kyye +
                ", zhlx=" + zhlx +
                ", aj_id=" + aj_id +
                ", inserttime='" + inserttime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankZcxxEntity that = (BankZcxxEntity) o;

        if (yhkkh != null ? !yhkkh.equals(that.yhkkh) : that.yhkkh != null) return false;
        if (zhlxy != null ? !zhlxy.equals(that.zhlxy) : that.zhlxy != null&&!that.zhlxy.equals("") ) return false;
        return zhye != null ? zhye.compareTo(that.zhye)==0 : that.zhye != null &&that.zhye.compareTo(new BigDecimal(-1))==0;
    }

    @Override
    public int hashCode() {
        int result = yhkkh != null ? yhkkh.hashCode() : 0;
        result = 31 * result + (zhye != null ? zhye.hashCode() : 0);
        result = 31 * result + (zhlxy != null ? zhlxy.hashCode() : 0);
        return result;
    }

    public  String remove_(String yhkkh){
        if(yhkkh.indexOf("_")>5){
            return yhkkh.split("_")[0];
        }else {
            return yhkkh;
        }
    }

    public static BankZcxxEntity mapToObj(Map<Integer,Object> map, Map<String,Integer> title){
        BankZcxxEntity b = new BankZcxxEntity();
        try{
            b.setZhzt(map.get(title.get("zhzt")).toString());
            b.setYhkkh(b.remove_(map.get(title.get("yhkkh")).toString().trim()));
            b.setYhkzh(b.remove_(map.get(title.get("yhkzh")).toString().trim()));
            if("".equals(b.getYhkkh())){
                b.setYhkkh(b.getYhkzh());
            }
            b.setKhxm(map.get(title.get("khxm")).toString());
            b.setKhzjh(map.get(title.get("khzjh")).toString());
            b.setKhsj(TimeFormatUtil.getDateSwitchTimestamp(map.get(title.get("khsj")).toString()));
            b.setKhh(map.get(title.get("khh")).toString());
            b.setKyye(new BigDecimal(map.get(title.get("kyye")).toString()));
            b.setZhye(new BigDecimal(map.get(title.get("zhye")).toString()));
            b.setZhlxy(map.get(title.get("zhlx")).toString());
        }catch (Exception e){
            e.getStackTrace();
        }
        return b;
    }
}
