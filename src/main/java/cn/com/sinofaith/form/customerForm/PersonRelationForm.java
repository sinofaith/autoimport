package cn.com.sinofaith.form.customerForm;

import java.math.BigDecimal;
import java.util.Map;

public class PersonRelationForm {
    private long xh;
    private BigDecimal id;
    private String name;
    private String pname;
    private String relationName;
    private String relationShow;
    private String relationMark;
    private BigDecimal jzzje = new BigDecimal(0);
    private BigDecimal czzje = new BigDecimal(0);

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getRelationShow() {
        return relationShow;
    }

    public void setRelationShow(String relationShow) {
        this.relationShow = relationShow;
    }

    public String getRelationMark() {
        return relationMark;
    }

    public void setRelationMark(String relationMark) {
        this.relationMark = relationMark;
    }

    public BigDecimal getJzzje() {
        return jzzje;
    }

    public void setJzzje(BigDecimal jzzje) {
        this.jzzje = jzzje;
    }

    public BigDecimal getCzzje() {
        return czzje;
    }

    public void setCzzje(BigDecimal czzje) {
        this.czzje = czzje;
    }

    public PersonRelationForm mapToObj(Map map){
        PersonRelationForm crf = new PersonRelationForm();
        crf.setId((BigDecimal) map.get("ID"));
        crf.setName((String) map.get("NAME"));
        crf.setPname((String) map.get("PNAME"));
        crf.setRelationName((String) map.get("RELATIONNAME"));
        crf.setRelationMark((String) map.get("RELATIONMARK"));
        crf.setRelationShow((String) map.get("RELATIONSHOW"));
        crf.setJzzje((BigDecimal)map.get("JZZJE"));
        crf.setCzzje((BigDecimal) map.get("CZZJE"));
        return crf;
    }
}
