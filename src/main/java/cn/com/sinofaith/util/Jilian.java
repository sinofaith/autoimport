package cn.com.sinofaith.util;

import java.util.List;
import java.util.Map;

public class Jilian {
    private int  parentId;
    private int id;
    private String name;
    private List<Jilian> children;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Jilian> getChildren() {
        return children;
    }

    public void setChildren(List<Jilian> children) {
        this.children = children;
    }
    public static int doCount(Jilian root,Map<String,Object> result){
        int count = 0;
        List<Jilian> list = root.getChildren();
        if(list==null ||list.size()==0){
            return count;
        }

        for (Jilian child : list) {
            //统计当前元素的子节点个数
            count++;

            //统计子节点的孩子总数
            int cur_cnt=doCount( child,result);
            result.put(String.valueOf(child.getId()), cur_cnt);

            count += cur_cnt;
        }

        //返回前记录当前节点的统计个数
        result.put(String.valueOf(root.getId()), count);
        return count;
    }
}
