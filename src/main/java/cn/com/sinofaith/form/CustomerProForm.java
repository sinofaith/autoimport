package cn.com.sinofaith.form;

import java.math.BigDecimal;
import java.util.Map;

public class CustomerProForm {
    private long xh;
    private long id;
    private String name;
    private String zjhm;
    private BigDecimal aj;
    private BigDecimal sjh;
    private BigDecimal yhkkh;
    private BigDecimal cftzh;
    private BigDecimal zfbzh;

    public long getXh() {
        return xh;
    }

    public void setXh(long xh) {
        this.xh = xh;
    }

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

    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        this.zjhm = zjhm;
    }

    public BigDecimal getAj() {
        return aj;
    }

    public void setAj(BigDecimal aj) {
        this.aj = aj;
    }

    public BigDecimal getSjh() {
        return sjh;
    }

    public void setSjh(BigDecimal sjh) {
        this.sjh = sjh;
    }

    public BigDecimal getYhkkh() {
        return yhkkh;
    }

    public void setYhkkh(BigDecimal yhkkh) {
        this.yhkkh = yhkkh;
    }

    public BigDecimal getCftzh() {
        return cftzh;
    }

    public void setCftzh(BigDecimal cftzh) {
        this.cftzh = cftzh;
    }

    public BigDecimal getZfbzh() {
        return zfbzh;
    }

    public void setZfbzh(BigDecimal zfbzh) {
        this.zfbzh = zfbzh;
    }

    public CustomerProForm mapToObj(Map map){
        CustomerProForm cp = new CustomerProForm();
//        cp.setId(Long.parseLong(map.get("ID").toString()));
        cp.setName((String) map.get("NAME"));
        cp.setZjhm((String)map.get("ZJHM"));
        cp.setAj(new BigDecimal(map.get("AJ")!=null ? map.get("AJ").toString():"0"));
        cp.setSjh(new BigDecimal(map.get("LXSJ")!=null?map.get("LXSJ").toString().split(",").length : 0));
        cp.setYhkkh(new BigDecimal(map.get("YHKKH")!=null ? map.get("YHKKH").toString():"0"));
        cp.setCftzh(new BigDecimal(map.get("CFTZH")!=null ? map.get("CFTZH").toString():"0"));
        cp.setZfbzh(new BigDecimal(map.get("ZFBZH")!=null ? map.get("ZFBZH").toString():"0"));
        return cp;
    }
}
