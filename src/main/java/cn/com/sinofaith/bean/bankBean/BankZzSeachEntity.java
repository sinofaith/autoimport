package cn.com.sinofaith.bean.bankBean;

import java.util.HashMap;
import java.util.Map;

public class BankZzSeachEntity {
    private String yhkkh;
    private String khxm;
    private String sfbz;
    private String dskh;
    private String dsxm;
    private String zysm;
    private String bz;
    private String jywdmc;

    public BankZzSeachEntity() {
    }

    public BankZzSeachEntity(String yhkkh, String khxm, String sfbz, String dskh, String dsxm, String zysm, String bz, String jywdmc) {
        this.yhkkh = yhkkh;
        this.khxm = khxm;
        this.sfbz = sfbz;
        this.dskh = dskh;
        this.dsxm = dsxm;
        this.zysm = zysm;
        this.bz = bz;
        this.jywdmc = jywdmc;
    }

    public String getYhkkh() {
        return yhkkh;
    }

    public void setYhkkh(String yhkkh) {
        this.yhkkh = yhkkh.replaceAll("\r|\n| |\t", "");
    }

    public String getKhxm() {
        return khxm;
    }

    public void setKhxm(String khxm) {
        this.khxm = khxm.replaceAll("\r|\n| |\t", "");
    }

    public String getSfbz() {
        return sfbz;
    }

    public void setSfbz(String sfbz) {
        this.sfbz = sfbz.replaceAll("\r|\n| |\t", "");
    }

    public String getDskh() {
        return dskh;
    }

    public void setDskh(String dskh) {
        this.dskh = dskh.replaceAll("\r|\n| |\t", "").replace("，",",");
    }

    public String getDsxm() {
        return dsxm;
    }

    public void setDsxm(String dsxm) {
        this.dsxm = dsxm.replaceAll("\r|\n| |\t", "").replace("，",",");;
    }

    public String getZysm() {
        return zysm;
    }

    public void setZysm(String zysm) {
        this.zysm = zysm.replaceAll("\r|\n| |\t", "");
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz.replaceAll("\r|\n| |\t", "");
    }

    public String getJywdmc() {
        return jywdmc;
    }

    public void setJywdmc(String jywdmc) {
        this.jywdmc = jywdmc.replaceAll("\r|\n| |\t", "");
    }

    @Override
    public String toString() {
        return "BankZzSeachEntity{" +
                "yhkkh='" + yhkkh + '\'' +
                ", khxm='" + khxm + '\'' +
                ", dskh='" + dskh + '\'' +
                ", dsxm='" + dsxm + '\'' +
                ", zysm='" + zysm + '\'' +
                ", bz='" + bz + '\'' +
                ", jywdmc='" + jywdmc + '\'' +
                '}';
    }

    public Map<String,String> objToMap(BankZzSeachEntity seachEntity){
        Map<String,String> map = new HashMap<>();
        map.put("yhkkh",yhkkh);
        map.put("khxm",khxm);
        map.put("sfbz",sfbz);
        map.put("dskh",dskh);
        map.put("dsxm",dsxm);
        map.put("zysm",zysm);
        map.put("bz",bz);
        map.put("jywdmc",jywdmc);
        return map;
    }
}
