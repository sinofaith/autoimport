package cn.com.sinofaith.bean.bankBean;

import cn.com.sinofaith.util.DBCSBC;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name="bank_customer",schema = "",catalog = "")
public class BankCustomerEntity {
    private String name;
    private String zjlx; //证件类型
    private String zjhm; //证件号码
    private String xzz_xzqh; //现住址_行政区划
    private String dwdz; //单位地址
    private String lxdh; //联系电话
    private String lxsj; //联系手机
    private String dwdh; //单位电话
    private String zzdh; //住宅电话
    private String gzdw; //工作单位
    private String email; //电子邮箱
    private String inserttime;

    public BankCustomerEntity() {
    }

    public BankCustomerEntity(String name, String zjlx, String zjhm, String xzz_xzqh, String dwdz, String lxdh, String lxsj, String dwdh, String zzdh, String gzdw, String email, String inserttime) {
        this.name = name;
        this.zjlx = zjlx;
        this.zjhm = zjhm;
        this.xzz_xzqh = xzz_xzqh;
        this.dwdz = dwdz;
        this.lxdh = lxdh;
        this.lxsj = lxsj;
        this.dwdh = dwdh;
        this.zzdh = zzdh;
        this.gzdw = gzdw;
        this.email = email;
        this.inserttime = inserttime;
    }

    @Basic
    @Column(name = "name",length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Basic
    @Column(name = "zjlx",length = 100)
    public String getZjlx() {
        return zjlx;
    }

    public void setZjlx(String zjlx) {
        this.zjlx = zjlx;
    }
    @Id
    @Column(name = "zjhm",length = 100)
    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        this.zjhm = zjhm;
    }
    @Basic
    @Column(name = "xzz_xzqh",length = 1000)
    public String getXzz_xzqh() {
        return xzz_xzqh;
    }

    public void setXzz_xzqh(String xzz_xzqh) {
        this.xzz_xzqh = xzz_xzqh;
    }
    @Basic
    @Column(name="dwdz",length = 1000)
    public String getDwdz() {
        return dwdz;
    }

    public void setDwdz(String dwdz) {
        this.dwdz = dwdz;
    }
    @Basic
    @Column(name = "lxdh",length = 100)
    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }
    @Basic
    @Column(name = "lxsj",length = 100)
    public String getLxsj() {
        return lxsj;
    }

    public void setLxsj(String lxsj) {
        this.lxsj = lxsj;
    }
    @Basic
    @Column(name = "dwdh",length = 100)
    public String getDwdh() {
        return dwdh;
    }

    public void setDwdh(String dwdh) {
        this.dwdh = dwdh;
    }
    @Basic
    @Column(name = "zzdh",length = 100)
    public String getZzdh() {
        return zzdh;
    }

    public void setZzdh(String zzdh) {
        this.zzdh = zzdh;
    }
    @Basic
    @Column(name = "gzdw",length = 500)
    public String getGzdw() {
        return gzdw;
    }

    public void setGzdw(String gzdw) {
        this.gzdw = gzdw;
    }
    @Basic
    @Column(name = "email",length = 200)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Basic
    @Column(name = "inserttime",length = 200)
    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankCustomerEntity that = (BankCustomerEntity) o;

//        if (aj_id != that.aj_id) return false;
        return zjhm != null ? zjhm.equals(that.zjhm) : that.zjhm == null;
    }

    @Override
    public int hashCode() {
        int result = zjhm != null ? zjhm.hashCode() : 0;
//        result = 31 * result + (int) (aj_id ^ (aj_id >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "BankCustomerEntity{" +
                "name='" + name + '\'' +
                ", zjlx='" + zjlx + '\'' +
                ", zjhm='" + zjhm + '\'' +
                ", xzz_xzqh='" + xzz_xzqh + '\'' +
                ", dwdz='" + dwdz + '\'' +
                ", lxdh='" + lxdh + '\'' +
                ", lxsj='" + lxsj + '\'' +
                ", dwdh='" + dwdh + '\'' +
                ", zzdh='" + zzdh + '\'' +
                ", gzdw='" + gzdw + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public BankCustomerEntity mapToObj(Map map){
        BankCustomerEntity zzf = new BankCustomerEntity();
        zzf.setName((String)map.get("NAME"));
        zzf.setZjlx((String)map.get("ZJLX"));
        zzf.setXzz_xzqh((String)map.get("XZZ_XZQH"));
        zzf.setZjhm((String)map.get("ZJHM"));
        zzf.setLxdh((String)map.get("LXDH"));
        zzf.setLxsj((String)map.get("LXSJ"));
        zzf.setDwdh((String)map.get("DWDH"));
        zzf.setZzdh((String)map.get("ZZDH"));
        zzf.setGzdw((String)map.get("GZDW"));
        zzf.setEmail((String)map.get("EMAIL"));
        zzf.setDwdz((String)map.get("DWDZ"));
        return zzf;
    }

    public static BankCustomerEntity listToObj(List<String> list, Map<String,Integer> title){
        BankCustomerEntity c = new BankCustomerEntity();
        c.setName(list.get(title.get("name")).trim().replace(" ",""));
        c.setZjlx(list.get(title.get("zjlx")).trim().replace("居民",""));
        c.setZjhm(list.get(title.get("zjhm")).trim());
        c.setXzz_xzqh(DBCSBC.ToDBC(list.get(title.get("xzz")).trim()));
        c.setDwdz(DBCSBC.ToDBC(list.get(title.get("dwdz")).trim()));
        c.setLxdh(list.get(title.get("lxdh")).trim().replace("+86","").replace("-",""));
        c.setLxsj(list.get(title.get("lxsj")).trim().replace("+86","").replace("-",""));
        c.setDwdh(list.get(title.get("dwdh")).trim().replace("+86","").replace("-",""));
        c.setZzdh(list.get(title.get("zzdh")).trim().replace("+86","").replace("-",""));
        c.setGzdw(list.get(title.get("gzdw")).trim().replace("+86","").replace("-",""));
        c.setEmail(list.get(title.get("email")).trim());
        return c;
    }
}
