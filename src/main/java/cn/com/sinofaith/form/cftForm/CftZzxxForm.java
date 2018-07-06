package cn.com.sinofaith.form.cftForm;

import java.math.BigDecimal;
import java.util.Map;

public class CftZzxxForm {
    private long id;
    private String name;
    private String zh;
    private String jydh;
    private String jdlx;
    private String jylx;
    private BigDecimal jyje;
    private BigDecimal zhye;
    private String jysj;
    private String yhlx;
    private String jysm;
    private String shmc;
    private String fsf="";
    private BigDecimal fsje;
    private String jsf="";
    private String jssj;
    private BigDecimal jsje;
    private String inserttime;

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id;
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

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getJydh() {
        return jydh;
    }

    public void setJydh(String jydh) {
        this.jydh = jydh;
    }

    public String getJdlx() {
        return jdlx;
    }

    public void setJdlx(String jdlx) {
        this.jdlx = jdlx;
    }

    public String getJylx() {
        return jylx;
    }

    public void setJylx(String jylx) {
        this.jylx = jylx;
    }

    public BigDecimal getJyje() {
        return jyje;
    }

    public void setJyje(BigDecimal jyje) {
        this.jyje = jyje;
    }

    public BigDecimal getZhye() {
        return zhye;
    }

    public void setZhye(BigDecimal zhye) {
        this.zhye = zhye;
    }

    public String getJysj() {
        return jysj;
    }

    public void setJysj(String jysj) {
        this.jysj = jysj;
    }

    public String getYhlx() {
        return yhlx;
    }

    public void setYhlx(String yhlx) {
        this.yhlx = yhlx;
    }

    public String getJysm() {
        return jysm;
    }

    public void setJysm(String jysm) {
        this.jysm = jysm;
    }

    public String getShmc() {
        return shmc;
    }

    public void setShmc(String shmc) {
        this.shmc = shmc;
    }

    public String getFsf() {
        return fsf;
    }

    public void setFsf(String fsf) {
        if(fsf==null){
            fsf="";
        }
        this.fsf = fsf;
    }

    public BigDecimal getFsje() {
        return fsje;
    }

    public void setFsje(BigDecimal fsje) {
        this.fsje = fsje;
    }

    public String getJsf() {
        return jsf;
    }

    public void setJsf(String jsf) {
        if(jsf==null){
            jsf="";
        }
        this.jsf = jsf;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }

    public BigDecimal getJsje() {
        return jsje;
    }

    public void setJsje(BigDecimal jsje) {
        this.jsje = jsje;
    }

    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    public  CftZzxxForm mapToForm(Map map){
        CftZzxxForm zzf = new CftZzxxForm();
        zzf.setName((String)map.get("XM"));
        zzf.setZh((String)map.get("ZH"));
        zzf.setJdlx((String)map.get("JDLX"));
        zzf.setJylx((String)map.get("JYLX"));
        zzf.setShmc((String)map.get("SHMC"));
        zzf.setJyje(new BigDecimal(map.get("JYJE").toString()));
        zzf.setJysj((String)map.get("JYSJ"));
        zzf.setFsf((String)map.get("FSF"));
        zzf.setFsje(new BigDecimal(map.get("FSJE").toString()));
        zzf.setJsf((String)map.get("JSF"));
        zzf.setJsje(new BigDecimal(map.get("JSJE").toString()));
        return zzf;
    }
}
