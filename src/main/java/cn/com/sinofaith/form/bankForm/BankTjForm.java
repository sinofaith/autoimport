package cn.com.sinofaith.form.bankForm;

import java.math.BigDecimal;

public class BankTjForm {
    private long id;
    private String name="";
    private String jyzh;
    private String dfzh;
    private String dfxm="";
    private BigDecimal jyzcs = new BigDecimal(0);
    private BigDecimal jzzcs = new BigDecimal(0);
    private BigDecimal jzzje = new BigDecimal(0);
    private BigDecimal czzcs = new BigDecimal(0);
    private BigDecimal czzje = new BigDecimal(0);
    private String zname ="";
    private BigDecimal zjyzcs = new BigDecimal(0);
    private BigDecimal zjzzcs = new BigDecimal(0);
    private BigDecimal zjzzje = new BigDecimal(0);
    private BigDecimal zczzcs = new BigDecimal(0);
    private BigDecimal zczzje = new BigDecimal(0);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name==null){
            name="";
        }
        this.name = name;
    }

    public String getJyzh() {
        return jyzh;
    }

    public void setJyzh(String jyzh) {
        this.jyzh = jyzh;
    }

    public String getDfzh() {
        return dfzh;
    }

    public void setDfzh(String dfzh) {
        this.dfzh = dfzh;
    }

    public String getDfxm() {
        return dfxm;
    }

    public void setDfxm(String dfxm) {
        if(dfxm==null){
            dfxm="";
        }
        this.dfxm = dfxm;
    }

    public BigDecimal getJyzcs() {
        return jyzcs;
    }

    public void setJyzcs(BigDecimal jyzcs) {
        this.jyzcs = jyzcs;
    }

    public BigDecimal getJzzcs() {
        return jzzcs;
    }

    public void setJzzcs(BigDecimal jzzcs) {
        this.jzzcs = jzzcs;
    }

    public BigDecimal getJzzje() {
        return jzzje;
    }

    public void setJzzje(BigDecimal jzzje) {
        this.jzzje = jzzje;
    }

    public BigDecimal getCzzcs() {
        return czzcs;
    }

    public void setCzzcs(BigDecimal czzcs) {
        this.czzcs = czzcs;
    }

    public BigDecimal getCzzje() {
        return czzje;
    }

    public void setCzzje(BigDecimal czzje) {
        this.czzje = czzje;
    }

    public String getZname() {
        return zname;
    }

    public void setZname(String zname) {
        if(zname==null){
            zname="";
        }
        this.zname = zname;
    }

    public BigDecimal getZjyzcs() {
        return zjyzcs;
    }

    public void setZjyzcs(BigDecimal zjyzcs) {
        this.zjyzcs = zjyzcs;
    }

    public BigDecimal getZjzzcs() {
        return zjzzcs;
    }

    public void setZjzzcs(BigDecimal zjzzcs) {
        this.zjzzcs = zjzzcs;
    }

    public BigDecimal getZjzzje() {
        return zjzzje;
    }

    public void setZjzzje(BigDecimal zjzzje) {
        this.zjzzje = zjzzje;
    }

    public BigDecimal getZczzcs() {
        return zczzcs;
    }

    public void setZczzcs(BigDecimal zczzcs) {
        this.zczzcs = zczzcs;
    }

    public BigDecimal getZczzje() {
        return zczzje;
    }

    public void setZczzje(BigDecimal zczzje) {
        this.zczzje = zczzje;
    }
}
