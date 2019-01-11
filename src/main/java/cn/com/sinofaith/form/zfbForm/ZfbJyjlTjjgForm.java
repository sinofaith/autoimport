package cn.com.sinofaith.form.zfbForm;

import java.math.BigDecimal;

public class ZfbJyjlTjjgForm {
    private String dfzh;
    private String dfmc;
    private String dyxcsj;
    private long jyzcs;
    private long skzcs;
    private BigDecimal skzje = new BigDecimal(0);
    private long fkzcs;
    private BigDecimal fkzje = new BigDecimal(0);

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

    public String getDfzh() {
        return dfzh;
    }

    public void setDfzh(String dfzh) {
        this.dfzh = dfzh;
    }

    public String getDfmc() {
        return dfmc;
    }

    public void setDfmc(String dfmc) {
        this.dfmc = dfmc;
    }

    public String getDyxcsj() {
        return dyxcsj;
    }

    public void setDyxcsj(String dyxcsj) {
        this.dyxcsj = dyxcsj;
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
