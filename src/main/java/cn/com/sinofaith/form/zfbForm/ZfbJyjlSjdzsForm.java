package cn.com.sinofaith.form.zfbForm;

import java.math.BigDecimal;

public class ZfbJyjlSjdzsForm {
    private long id;
    private String mjyhid;
    private String mjxx;
    private String shrdz;
    private long sjcs;
    private BigDecimal czje = new BigDecimal(0);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMjyhid() {
        return mjyhid;
    }

    public void setMjyhid(String mjyhid) {
        this.mjyhid = mjyhid;
    }

    public String getMjxx() {
        return mjxx;
    }

    public void setMjxx(String mjxx) {
        this.mjxx = mjxx;
    }

    public String getShrdz() {
        return shrdz;
    }

    public void setShrdz(String shrdz) {
        this.shrdz = shrdz;
    }

    public long getSjcs() {
        return sjcs;
    }

    public void setSjcs(long sjcs) {
        this.sjcs = sjcs;
    }

    public BigDecimal getCzje() {
        return czje;
    }

    public void setCzje(BigDecimal czje) {
        this.czje = czje;
    }
}
