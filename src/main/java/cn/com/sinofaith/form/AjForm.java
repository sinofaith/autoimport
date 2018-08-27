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
    private String inserttime;

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


    public AjForm mapToForm(Map map){
        AjForm af = new AjForm();
        af.setId((BigDecimal)map.get("ID"));
        af.setAj((String)map.get("AJ"));
        af.setCftnum(map.get("CFTNUM")!=null ? (String)map.get("CFTNUM"):"0");
        af.setBanknum(map.get("BANKNUM")!=null ? (String) map.get("BANKNUM"):"0");
        af.setInserttime((String)map.get("INSERTTIME"));
        return af;
    }
}
