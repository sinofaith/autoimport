package cn.com.sinofaith.form.bankForm;

import java.math.BigDecimal;
import java.util.Map;

public class BankZzxxForm {
    private long id;
    private String jyxm;
    private String yhkkh;
    private String jysj;
    private BigDecimal jyje = new BigDecimal(0);
    private BigDecimal jyye = new BigDecimal(0);
    private String sfbz;
    private String dszh;
    private String dskh;
    private String dsxm;
    private String dssfzh;
    private String dskhh;
    private String zysm;
    private String jysfcg;
    private String jywdmc;
    private BigDecimal dsjyye;
    private BigDecimal dsye;
    private String bz;
    private String jyzjh;
    private String inserttime;
    private String jyfsd;

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
        if(jyxm==null){jyxm ="";} this.jyxm = jyxm;
    }

    public String getYhkkh() {
        return yhkkh;
    }

    public void setYhkkh(String yhkkh) {
        if(yhkkh==null){yhkkh="";}this.yhkkh = yhkkh;
    }

    public String getJysj() {
        return jysj;
    }

    public void setJysj(String jysj) {
        if(jysj==null) {
            jysj="";
        }
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

    public String getDskh() {
        return dskh;
    }

    public void setDskh(String dskh) {if(dskh==null){dskh="";}
        this.dskh = dskh;
    }

    public String getDsxm() {
        return dsxm;
    }

    public void setDsxm(String dsxm) {
        if(dsxm==null){dsxm="";}this.dsxm = dsxm;
    }

    public String getDssfzh() {
        return dssfzh;
    }

    public void setDssfzh(String dssfzh) {
        this.dssfzh = dssfzh;
    }

    public String getDskhh() {
        return dskhh;
    }

    public void setDskhh(String dskhh) {
        this.dskhh = dskhh;
    }

    public String getZysm() {
        return zysm;
    }

    public void setZysm(String zysm) {
        if(zysm==null){zysm="";}
        this.zysm = zysm;
    }

    public String getJysfcg() {
        return jysfcg;
    }

    public void setJysfcg(String jysfcg) {
        if(jysfcg==null){jysfcg="";}
        this.jysfcg = jysfcg;
    }

    public String getJywdmc() {
        return jywdmc;
    }

    public void setJywdmc(String jywdmc) {
        if(jywdmc==null){jywdmc="";}
        this.jywdmc = jywdmc;
    }

    public BigDecimal getDsjyye() {
        return dsjyye;
    }

    public void setDsjyye(BigDecimal dsjyye) {
        this.dsjyye = dsjyye;
    }

    public BigDecimal getDsye() {
        return dsye;
    }

    public void setDsye(BigDecimal dsye) {
        this.dsye = dsye;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        if(bz==null){bz="";}

        this.bz = bz;
    }

    public String getJyfsd() {
        return jyfsd;
    }

    public void setJyfsd(String jyfsd) {
        if(jyfsd==null){jyfsd="";}
        this.jyfsd = jyfsd;
    }

    public BankZzxxForm mapToForm(Map map){
        BankZzxxForm zzf = new BankZzxxForm();
        zzf.setJyxm(map.get("JYXM") != null? (String)map.get("JYXM"):(String) map.get("JYXMS"));
        zzf.setYhkkh((String) map.get("YHKKH"));
        zzf.setJysj((String)map.get("JYSJ"));
        zzf.setJyje(new BigDecimal(map.get("JYJE").toString()));
        zzf.setJyye(new BigDecimal(map.get("JYYE").toString()));
        zzf.setSfbz((String) map.get("SFBZ"));
        zzf.setDskh((String) map.get("DSKH"));
        zzf.setDsxm((String)map.get("DSXM"));
        zzf.setZysm((String) map.get("ZYSM"));
        zzf.setBz((String)map.get("BZ"));
        zzf.setJyfsd((String)map.get("JYFSD"));
        zzf.setJywdmc((String)map.get("JYWDMC"));
        return zzf;
    }
}
