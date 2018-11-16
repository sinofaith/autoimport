package cn.com.sinofaith.form.psForm;

import java.util.ArrayList;
import java.util.List;

public class PsPoltForm {
    private String psid;
    private String sponsorid;
    private String name;
    private Long tier;
    private Long value;
    private List<PsPoltForm> children = new ArrayList<>();

    public String getPsid() {
        return psid;
    }

    public void setPsid(String psid) {
        this.psid = psid;
    }

    public String getSponsorid() {
        return sponsorid;
    }

    public void setSponsorid(String sponsorid) {
        this.sponsorid = sponsorid;
    }

    public Long getTier() {
        return tier;
    }

    public void setTier(Long tier) {
        this.tier = tier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        if(value==null){
            this.value = 0l;
        }else {
            this.value = value;
        }
    }

    public List<PsPoltForm> getChildren() {
        return children;
    }

    public void setChildren(List<PsPoltForm> children) {
        this.children = children;
    }
}
