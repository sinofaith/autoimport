package cn.com.sinofaith.form.zfbForm;

import cn.com.sinofaith.page.Page;

import java.math.BigDecimal;

public class ZfbZzmxTjjgForm {
    private String fkzhmc;
    private String fkfzfbzh;
    private String fkzzcpmc;
    private Long fkzcs;
    private BigDecimal fkzje = new BigDecimal(0);
    private String skzhmc;
    private String skfzfbzh;
    private String skzzcpmc;
    private Long skzcs;
    private BigDecimal skzje = new BigDecimal(0);
    private Long jyzcs;

    public String getFkzhmc() {
        return fkzhmc;
    }

    public void setFkzhmc(String fkzhmc) {
        this.fkzhmc = fkzhmc;
    }

    public String getSkzhmc() {
        return skzhmc;
    }

    public void setSkzhmc(String skzhmc) {
        this.skzhmc = skzhmc;
    }

    public String getFkfzfbzh() {
        return fkfzfbzh;
    }

    public void setFkfzfbzh(String fkfzfbzh) {
        this.fkfzfbzh = fkfzfbzh;
    }

    public String getFkzzcpmc() {
        return fkzzcpmc;
    }

    public void setFkzzcpmc(String fkzzcpmc) {
        this.fkzzcpmc = fkzzcpmc;
    }

    public Long getFkzcs() {
        return fkzcs;
    }

    public void setFkzcs(Long fkzcs) {
        if(fkzcs==null){
            this.fkzcs = 0l;
        }else{
            this.fkzcs = fkzcs;
        }
    }

    public BigDecimal getFkzje() {
        return fkzje;
    }

    public void setFkzje(BigDecimal fkzje) {
        if(fkzje!=null){
            this.fkzje = fkzje;
        }else{
            this.fkzje = new BigDecimal(0);
        }

    }

    public String getSkfzfbzh() {
        return skfzfbzh;
    }

    public void setSkfzfbzh(String skfzfbzh) {
        this.skfzfbzh = skfzfbzh;
    }

    public String getSkzzcpmc() {
        return skzzcpmc;
    }

    public void setSkzzcpmc(String skzzcpmc) {
        this.skzzcpmc = skzzcpmc;
    }

    public Long getSkzcs() {
        return skzcs;
    }

    public void setSkzcs(Long skzcs) {
        if(skzcs!=null){
            this.skzcs = skzcs;
        }else{
            this.skzcs = 0l;
        }
    }

    public BigDecimal getSkzje() {
        return skzje;
    }

    public void setSkzje(BigDecimal skzje) {
        if(skzje!=null){
            this.skzje = skzje;
        }else{
            this.skzje = new BigDecimal(0);
        }
    }

    public Long getJyzcs() {
        return jyzcs;
    }

    public void setJyzcs(Long jyzcs) {
        if(jyzcs!=null){
            this.jyzcs = jyzcs;
        }else{
            this.jyzcs = 0l;
        }
    }
}
