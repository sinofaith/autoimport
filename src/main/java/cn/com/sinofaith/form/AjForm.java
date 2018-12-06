package cn.com.sinofaith.form;

import java.math.BigDecimal;
import java.util.Map;

public class AjForm {
    private long xh;
    private BigDecimal id;
    private String aj;
    private long flg;
    private String cftnum;
    private String banknum;
    private String wuliunum;
    private String psnum;
    private String zfbnum;
    private String inserttime;

    public String getWuliunum() {
        return wuliunum;
    }

    public void setWuliunum(String wuliunum) {
        this.wuliunum = wuliunum;
    }

    public long getXh() {
        return xh;
    }

    public void setXh(long xh) {
        this.xh = xh;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getAj() {
        return aj;
    }

    public void setAj(String aj) {
        this.aj = aj;
    }

    public long getFlg() {
        return flg;
    }

    public void setFlg(long flg) {
        this.flg = flg;
    }

    public String getCftnum() {
        return cftnum;
    }

    public void setCftnum(String cftnum) {
        this.cftnum = cftnum;
    }

    public String getBanknum() {
        return banknum;
    }

    public void setBanknum(String banknum) {
        this.banknum = banknum;
    }

    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    public String getPsnum() {
        return psnum;
    }

    public void setPsnum(String psnum) {
        this.psnum = psnum;
    }

    public String getZfbnum() {
        return zfbnum;
    }

    public void setZfbnum(String zfbnum) {
        this.zfbnum = zfbnum;
    }

    public AjForm mapToForm(Map map){
        AjForm af = new AjForm();
        af.setId((BigDecimal)map.get("ID"));
        af.setAj((String)map.get("AJ"));
        af.setCftnum(map.get("CFTNUM")!=null ? (String)map.get("CFTNUM"):"0");
        af.setBanknum(map.get("BANKNUM")!=null ? (String) map.get("BANKNUM"):"0");
        af.setWuliunum(map.get("WULIUNUM")!=null ? (String) map.get("WULIUNUM"):"0");
        af.setPsnum(map.get("PSNUM")!=null ? (String) map.get("PSNUM"):"0");
        af.setZfbnum(map.get("ZFBNUM")!=null ? (String) map.get("ZFBNUM"):"0");
        af.setInserttime((String)map.get("INSERTTIME"));
        return af;
    }
}
