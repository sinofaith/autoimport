package cn.com.sinofaith.bean.bankBean;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "mapping_bankzzxx", schema = "", catalog = "")
public class MappingBankzzxxEntity {
    private long id;
    private String name="无";
    private String jykh;
    private String jyxm="无";
    private String jyzjh="无";
    private String jyrq;
    private String jysj="无";
    private String jyje;
    private String jyye="无";
    private String sfbz;
    private String dskh;
    private String dsxm="无";
    private String dszjh="无";
    private String dskhh="无";
    private String zysm="无";
    private String jywdmc="无";
    private String jyfsd="无";
    private String bz="无";
    private String inserttime;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Basic
    @Column(name = "name", length = 300)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Basic
    @Column(name = "jykh",nullable = false, length = 300)
    public String getJykh() {
        return jykh;
    }

    public void setJykh(String jykh) {
        this.jykh = jykh;
    }
    @Basic
    @Column(name = "jyxm", length = 300)
    public String getJyxm() {
        return jyxm;
    }

    public void setJyxm(String jyxm) {
        this.jyxm = jyxm;
    }
    @Basic
    @Column(name = "jyzjh", length = 300)
    public String getJyzjh() {
        return jyzjh;
    }

    public void setJyzjh(String jyzjh) {
        this.jyzjh = jyzjh;
    }
    @Basic
    @Column(name = "jyrq",nullable = false, length = 300)
    public String getJyrq() {
        return jyrq;
    }

    public void setJyrq(String jyrq) {
        this.jyrq = jyrq;
    }
    @Basic
    @Column(name = "jysj", length = 300)
    public String getJysj() {
        return jysj;
    }

    public void setJysj(String jysj) {
        this.jysj = jysj;
    }
    @Basic
    @Column(name = "jyje",nullable = false, length = 300)
    public String getJyje() {
        return jyje;
    }

    public void setJyje(String jyje) {
        this.jyje = jyje;
    }
    @Basic
    @Column(name = "jyye", length = 300)
    public String getJyye() {
        return jyye;
    }

    public void setJyye(String jyye) {
        this.jyye = jyye;
    }
    @Basic
    @Column(name = "sfbz",nullable = false, length = 300)
    public String getSfbz() {
        return sfbz;
    }

    public void setSfbz(String sfbz) {
        this.sfbz = sfbz;
    }
    @Basic
    @Column(name = "dskh",nullable = false, length = 300)
    public String getDskh() {
        return dskh;
    }

    public void setDskh(String dskh) {
        this.dskh = dskh;
    }
    @Basic
    @Column(name = "dsxm", length = 300)
    public String getDsxm() {
        return dsxm;
    }

    public void setDsxm(String dsxm) {
        this.dsxm = dsxm;
    }
    @Basic
    @Column(name = "dszjh", length = 300)
    public String getDszjh() {
        return dszjh;
    }

    public void setDszjh(String dszjh) {
        this.dszjh = dszjh;
    }
    @Basic
    @Column(name = "dskhh", length = 300)
    public String getDskhh() {
        return dskhh;
    }

    public void setDskhh(String dskhh) {
        this.dskhh = dskhh;
    }
    @Basic
    @Column(name = "zysm", length = 300)
    public String getZysm() {
        return zysm;
    }

    public void setZysm(String zysm) {
        this.zysm = zysm;
    }
    @Basic
    @Column(name = "jywdmc", length = 300)
    public String getJywdmc() {
        return jywdmc;
    }

    public void setJywdmc(String jywdmc) {
        this.jywdmc = jywdmc;
    }
    @Basic
    @Column(name = "jyfsd", length = 300)
    public String getJyfsd() {
        return jyfsd;
    }

    public void setJyfsd(String jyfsd) {
        this.jyfsd = jyfsd;
    }
    @Basic
    @Column(name = "bz", length = 300)
    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
    @Basic
    @Column(name = "inserttime", length = 300)
    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    @Override
    public String toString() {
        return "MappingBankzzxxEntity{" +
                "jykh='" + jykh + '\'' +
                ", jyxm='" + jyxm + '\'' +
                ", jyzjh='" + jyzjh + '\'' +
                ", jyrq='" + jyrq + '\'' +
                ", jysj='" + jysj + '\'' +
                ", jyje='" + jyje + '\'' +
                ", jyye='" + jyye + '\'' +
                ", sfbz='" + sfbz + '\'' +
                ", dskh='" + dskh + '\'' +
                ", dsxm='" + dsxm + '\'' +
                ", dszjh='" + dszjh + '\'' +
                ", dskhh='" + dskhh + '\'' +
                ", zysm='" + zysm + '\'' +
                ", jywdmc='" + jywdmc + '\'' +
                ", jyfsd='" + jyfsd + '\'' +
                ", bz='" + bz + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MappingBankzzxxEntity that = (MappingBankzzxxEntity) o;

        if (jykh != null ? !jykh.equals(that.jykh) : that.jykh != null) return false;
        if (jyxm != null ? !jyxm.equals(that.jyxm) : that.jyxm != null) return false;
        if (jyzjh != null ? !jyzjh.equals(that.jyzjh) : that.jyzjh != null) return false;
        if (jyrq != null ? !jyrq.equals(that.jyrq) : that.jyrq != null) return false;
        if (jysj != null ? !jysj.equals(that.jysj) : that.jysj != null) return false;
        if (jyje != null ? !jyje.equals(that.jyje) : that.jyje != null) return false;
        if (jyye != null ? !jyye.equals(that.jyye) : that.jyye != null) return false;
        if (sfbz != null ? !sfbz.equals(that.sfbz) : that.sfbz != null) return false;
        if (dskh != null ? !dskh.equals(that.dskh) : that.dskh != null) return false;
        if (dsxm != null ? !dsxm.equals(that.dsxm) : that.dsxm != null) return false;
        if (dszjh != null ? !dszjh.equals(that.dszjh) : that.dszjh != null) return false;
        if (dskhh != null ? !dskhh.equals(that.dskhh) : that.dskhh != null) return false;
        if (zysm != null ? !zysm.equals(that.zysm) : that.zysm != null) return false;
        if (jywdmc != null ? !jywdmc.equals(that.jywdmc) : that.jywdmc != null) return false;
        if (jyfsd != null ? !jyfsd.equals(that.jyfsd) : that.jyfsd != null) return false;
        return bz != null ? bz.equals(that.bz) : that.bz == null;
    }

    @Override
    public int hashCode() {
        int result = jykh != null ? jykh.hashCode() : 0;
        result = 31 * result + (jyxm != null ? jyxm.hashCode() : 0);
        result = 31 * result + (jyzjh != null ? jyzjh.hashCode() : 0);
        result = 31 * result + (jyrq != null ? jyrq.hashCode() : 0);
        result = 31 * result + (jysj != null ? jysj.hashCode() : 0);
        result = 31 * result + (jyje != null ? jyje.hashCode() : 0);
        result = 31 * result + (jyye != null ? jyye.hashCode() : 0);
        result = 31 * result + (sfbz != null ? sfbz.hashCode() : 0);
        result = 31 * result + (dskh != null ? dskh.hashCode() : 0);
        result = 31 * result + (dsxm != null ? dsxm.hashCode() : 0);
        result = 31 * result + (dszjh != null ? dszjh.hashCode() : 0);
        result = 31 * result + (dskhh != null ? dskhh.hashCode() : 0);
        result = 31 * result + (zysm != null ? zysm.hashCode() : 0);
        result = 31 * result + (jywdmc != null ? jywdmc.hashCode() : 0);
        result = 31 * result + (jyfsd != null ? jyfsd.hashCode() : 0);
        result = 31 * result + (bz != null ? bz.hashCode() : 0);
        return result;
    }

    public MappingBankzzxxEntity listToObj(List<String> field){
        MappingBankzzxxEntity mb = new MappingBankzzxxEntity();
        try {
            mb.setJykh(field.get(3));
            mb.setJyxm(field.get(22));
            mb.setJyzjh(field.get(20));
            mb.setJyrq(field.get(4));
            mb.setJysj(field.get(23));
            mb.setJyje(field.get(5));
            mb.setJyye(field.get(6));
            mb.setSfbz(field.get(7));
            mb.setDskh(field.get(14));
            mb.setDsxm(field.get(9));
            mb.setDszjh(field.get(10));
            mb.setDskhh(field.get(15));
            mb.setZysm(field.get(11));
            mb.setJywdmc(field.get(16));
            mb.setBz(field.get(19));
            mb.setJyfsd(field.get(21));
        }catch (Exception e){
            e.printStackTrace();
        }
        return mb;
    }

    public Map<String,String> objToMap(){
        Map<String,String> map = new HashMap<>();
        map.put("jykh",jykh);
        map.put("jyxm",jyxm);
        map.put("jyzjh",jyzjh);
        map.put("jyrq",jyrq);
        map.put("jysj",jysj);
        map.put("jyje",jyje);
        map.put("jyye",jyye);
        map.put("sfbz",sfbz);
        map.put("dskh",dskh);
        map.put("dsxm",dsxm);
        map.put("dszjh",dszjh);
        map.put("dskhh",dskhh);
        map.put("zysm",zysm);
        map.put("jywdmc",jywdmc);
        map.put("jyfsd",jyfsd);
        map.put("bz",bz);
        return map;
    }
}
