package cn.com.sinofaith.form.zfbForm;

import java.math.BigDecimal;

public class ZfbZzmxGtzhForm {
    private long id;
    private String zfbmc;
    private String zfbzh;
    private String dfzh;
    private long gthys;
    private long jyzcs;
    private long fkzcs;
    private BigDecimal fkzje = new BigDecimal(0);
    private long skzcs;
    private BigDecimal skzje = new BigDecimal(0);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getZfbmc() {
        return zfbmc;
    }

    public void setZfbmc(String zfbmc) {
        this.zfbmc = zfbmc;
    }

    public String getZfbzh() {
        return zfbzh;
    }

    public void setZfbzh(String zfbzh) {
        this.zfbzh = zfbzh;
    }

    public String getDfzh() {
        return dfzh;
    }

    public void setDfzh(String dfzh) {
        this.dfzh = dfzh;
    }

    public long getGthys() {
        return gthys;
    }

    public void setGthys(long gthys) {
        this.gthys = gthys;
    }

    public long getJyzcs() {
        return jyzcs;
    }

    public void setJyzcs(long jyzcs) {
        this.jyzcs = jyzcs;
    }

    public long getFkzcs() {
        return fkzcs;
    }

    public void setFkzcs(long fkzcs) {
        this.fkzcs = fkzcs;
    }

    public BigDecimal getFkzje() {
        return fkzje;
    }

    public void setFkzje(BigDecimal fkzje) {
        this.fkzje = fkzje;
    }

    public long getSkzcs() {
        return skzcs;
    }

    public void setSkzcs(long skzcs) {
        this.skzcs = skzcs;
    }

    public BigDecimal getSkzje() {
        return skzje;
    }

    public void setSkzje(BigDecimal skzje) {
        this.skzje = skzje;
    }
}
