package cn.com.sinofaith.form;

import java.math.BigDecimal;
import java.util.Map;

public class CftTjjgForm {
    private long id;
    private String name;
    private String jyzh;
    private String jylx;
    private BigDecimal jyzcs = new BigDecimal(0);
    private BigDecimal jzzcs = new BigDecimal(0);
    private BigDecimal jzzje = new BigDecimal(0);
    private BigDecimal czzcs = new BigDecimal(0);
    private BigDecimal czzje = new BigDecimal(0);

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
        this.name = name;
    }

    public String getJyzh() {
        return jyzh;
    }

    public void setJyzh(String jyzh) {
        this.jyzh = jyzh;
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

    public String getJylx() {
        return jylx;
    }

    public void setJylx(String jylx) {
        this.jylx = jylx;
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

    public CftTjjgForm mapToForm(Map map){
        CftTjjgForm cftForm = new CftTjjgForm();
        cftForm.setName((String) map.get("XM"));
        cftForm.setJyzh((String) map.get("JYZH"));
        cftForm.setJylx((String) map.get("JYLX"));
        cftForm.setJyzcs( new BigDecimal(map.get("JYZCS").toString()));
        cftForm.setJzzcs( new BigDecimal(map.get("JZZCS").toString()));
        cftForm.setJzzje( new BigDecimal(map.get("JZZJE").toString()));
        cftForm.setCzzcs( new BigDecimal(map.get("CZZCS").toString()));
        cftForm.setCzzje( new BigDecimal(map.get("CZZJE").toString()));
        return cftForm;
    }

}
