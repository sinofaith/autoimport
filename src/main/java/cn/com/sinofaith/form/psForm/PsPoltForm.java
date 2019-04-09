package cn.com.sinofaith.form.psForm;

import java.util.ArrayList;
import java.util.List;

public class PsPoltForm {
    private String psid;
    private String sponsorid;
    private String name;
    private Long tier;
    private Long value;
    private Long lineal; // 直系
    private Long contain = 0l; // 包含
    private Long containNum = 0l; // 下线数
    private String path;
    private List<PsPoltForm> children = new ArrayList<>();// 孩子节点
    private PsPoltForm parentNode;// 父节点

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

    public Long getLineal() {
        return lineal;
    }

    public void setLineal(Long lineal) {
        this.lineal = lineal;
    }

    public Long getContain() {
        return contain;
    }

    public void setContain(Long contain) {
        this.contain = contain;
    }

    public Long getContainNum() {
        return containNum;
    }

    public void setContainNum(Long containNum) {
        this.containNum = containNum;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PsPoltForm getParentNode() {
        return parentNode;
    }

    public void setParentNode(PsPoltForm parentNode) {
        this.parentNode = parentNode;
    }

    /**
     * 返回当前节点的所有父辈节点
     */
    public List<PsPoltForm> getElders() {
        List<PsPoltForm> elders = new ArrayList<>();
        PsPoltForm parentNode = this.parentNode;
        if (parentNode == null) {
            return elders;
        } else {
            // 倒序插入
            elders.add(0, parentNode);
            elders.addAll(0, parentNode.getElders());
            return elders;
        }
    }
}
