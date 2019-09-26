package cn.com.sinofaith.form.cftForm;

import java.math.BigDecimal;

public class CftTjjgsForm {
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
    private BigDecimal count = new BigDecimal(0);
    private long zhlx;
    private long dsfzh;
    private String  minsj;
    private String maxsj;
    private String jgsj;


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

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
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
    public long getZhlx() {
        return zhlx;
    }

    public void setZhlx(long zhlx) {
        this.zhlx = zhlx;
    }

    public long getDsfzh() {
        return dsfzh;
    }

    public void setDsfzh(long dsfzh) {
        this.dsfzh = dsfzh;
    }

    public String getMinsj() {
        return minsj;
    }

    public void setMinsj(String minsj) {
        this.minsj = minsj;
    }

    public String getMaxsj() {
        return maxsj;
    }

    public void setMaxsj(String maxsj) {
        this.maxsj = maxsj;
    }

    public String getJgsj() {
        return jgsj;
    }

    public void setJgsj(String jgsj) {
        this.jgsj = jgsj;
    }

    @Override
    public String toString() {
        return name + "//" + jyzh + "//" + dfzh + "//" + jyzcs + "//" + jzzcs + "//" + jzzje + "//" + czzcs + "//" + czzje;
    }

    public String toStrings() {
        return name + "//" + jyzh + "//" + dfzh + "//" + dfxm + "//" + count + "//" + jyzcs + "//" +
                jzzcs + "//" + jzzje + "//" + czzcs + "//" + czzje;
    }

    public String toStringss() {
        return jyzh + "//" + name + "//" + dfzh + "//" + dfxm + "//" + jyzcs + "//" +
                jzzcs + "//" + jzzje + "//" + czzcs + "//" + czzje;
    }
}
