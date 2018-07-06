package cn.com.sinofaith.form.bankForm;

import java.math.BigDecimal;
import java.util.Map;

public class BankZzxxForm {
    private long id;
    private String jyxm;
    private String jyzkh;
    private String jysj;
    private BigDecimal jyje;
    private BigDecimal jyye;
    private String sfbz;
    private String dszh;
    private String dsxm;
    private String zysm;
    private String jysfcg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJyxm() {
        return jyxm;
    }

    public void setJyxm(String jyxm) {
        this.jyxm = jyxm;
    }

    public String getJyzkh() {
        return jyzkh;
    }

    public void setJyzkh(String jyzkh) {
        this.jyzkh = jyzkh;
    }

    public String getJysj() {
        return jysj;
    }

    public void setJysj(String jysj) {
        this.jysj = jysj;
    }

    public BigDecimal getJyje() {
        return jyje;
    }

    public void setJyje(BigDecimal jyje) {
        this.jyje = jyje;
    }

    public BigDecimal getJyye() {
        return jyye;
    }

    public void setJyye(BigDecimal jyye) {
        this.jyye = jyye;
    }

    public String getSfbz() {
        return sfbz;
    }

    public void setSfbz(String sfbz) {
        this.sfbz = sfbz;
    }

    public String getDszh() {
        return dszh;
    }

    public void setDszh(String dszh) {
        this.dszh = dszh;
    }

    public String getDsxm() {
        return dsxm;
    }

    public void setDsxm(String dsxm) {
        this.dsxm = dsxm;
    }

    public String getZysm() {
        return zysm;
    }

    public void setZysm(String zysm) {
        this.zysm = zysm;
    }

    public String getJysfcg() {
        return jysfcg;
    }

    public void setJysfcg(String jysfcg) {
        this.jysfcg = jysfcg;
    }

    public BankZzxxForm mapToForm(Map map){
        BankZzxxForm zzf = new BankZzxxForm();
        zzf.setJyxm((String) map.get("JYXM"));
        zzf.setJyzkh((String) map.get("JYZKH"));
        zzf.setJysj((String)map.get("JYSJ"));
        zzf.setJyje(new BigDecimal(map.get("JYJE").toString()));
        zzf.setJyye(new BigDecimal(map.get("JYYE").toString()));
        zzf.setSfbz((String) map.get("SFBZ"));
        zzf.setDszh((String) map.get("DSZH"));
        zzf.setDsxm(map.get("DSXM") != null? (String)map.get("DSXM"):(String) map.get("DFXM"));
        zzf.setZysm((String) map.get("ZYSM"));
        zzf.setJysfcg((String)map.get("JYSFCG"));
        return zzf;
    }
}
