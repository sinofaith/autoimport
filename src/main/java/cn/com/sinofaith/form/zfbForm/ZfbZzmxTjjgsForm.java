package cn.com.sinofaith.form.zfbForm;

import java.math.BigDecimal;

public class ZfbZzmxTjjgsForm {
    private String yhid;
    private String zhmc;
    private String fkfzfbzh;
    private String skfzfbzh;
    private String jydfxx;
    private BigDecimal zzje = new BigDecimal(0);

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    public String getZhmc() {
        return zhmc;
    }

    public void setZhmc(String zhmc) {
        this.zhmc = zhmc;
    }

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

    public String getJydfxx() {
        return jydfxx;
    }

    public void setJydfxx(String jydfxx) {
        if(jydfxx!=null){
            this.jydfxx = jydfxx;
        }else{
            this.jydfxx="";
        }
    }

    public BigDecimal getZzje() {
        return zzje;
    }

    public void setZzje(BigDecimal zzje) {
        this.zzje = zzje;
    }
}
