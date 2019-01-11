package cn.com.sinofaith.form.zfbForm;

import java.math.BigDecimal;

public class ZfbZzmxTjjgssForm {
    private String fkfzfbzh;
    private String skfzfbzh;
    private long zzcs;
    private BigDecimal zzje = new BigDecimal(0);

    public String getFkfzfbzh() {
        return fkfzfbzh;
    }

    public void setFkfzfbzh(String fkfzfbzh) {
        this.fkfzfbzh = fkfzfbzh;
    }

    public String getSkfzfbzh() {
        return skfzfbzh;
    }

    public void setSkfzfbzh(String skfzfbzh) {
        this.skfzfbzh = skfzfbzh;
    }

    public long getZzcs() {
        return zzcs;
    }

    public void setZzcs(long zzcs) {
        this.zzcs = zzcs;
    }

    public BigDecimal getZzje() {
        return zzje;
    }

    public void setZzje(BigDecimal zzje) {
        this.zzje = zzje;
    }
}
