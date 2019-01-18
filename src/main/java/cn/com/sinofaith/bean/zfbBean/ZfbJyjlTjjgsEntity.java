package cn.com.sinofaith.bean.zfbBean;


import cn.com.sinofaith.form.zfbForm.ZfbJyjlTjjgsForm;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "ZfbJyjl_Tjjgs")
public class ZfbJyjlTjjgsEntity {
    private long id;
    private String zfbmc;
    private String zfbzh;
    private String jyzt;
    private String dfmc;
    private String dfzh;
    private long fkzcs = 0;
    private BigDecimal fkzje = new BigDecimal(0);
    private String insert_time;
    private long aj_id;

    @Id
    @Column(name="id",nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "zfbmc",nullable = true,length = 100)
    public String getZfbmc() {
        return zfbmc;
    }

    public void setZfbmc(String zfbmc) {
        this.zfbmc = zfbmc;
    }

    @Basic
    @Column(name = "zfbzh",nullable = true,length = 100)
    public String getZfbzh() {
        return zfbzh;
    }

    public void setZfbzh(String zfbzh) {
        this.zfbzh = zfbzh;
    }

    @Basic
    @Column(name = "jyzt",nullable = true,length = 100)
    public String getJyzt() {
        return jyzt;
    }

    public void setJyzt(String jyzt) {
        this.jyzt = jyzt;
    }

    @Basic
    @Column(name = "dfmc",nullable = true,length = 100)
    public String getDfmc() {
        return dfmc;
    }

    public void setDfmc(String dfmc) {
        this.dfmc = dfmc;
    }

    @Basic
    @Column(name = "dfzh",nullable = true,length = 100)
    public String getDfzh() {
        return dfzh;
    }

    public void setDfzh(String dfzh) {
        this.dfzh = dfzh;
    }

    @Basic
    @Column(name = "fkzcs",nullable = false,precision = 0)
    public long getFkzcs() {
        return fkzcs;
    }

    public void setFkzcs(long fkzcs) {
        this.fkzcs = fkzcs;
    }

    @Basic
    @Column(name = "fkzje",nullable = false,precision = 0)
    public BigDecimal getFkzje() {
        return fkzje;
    }

    public void setFkzje(BigDecimal fkzje) {
        this.fkzje = fkzje;
    }

    @Basic
    @Column(name = "insert_time",nullable = true,length = 19)
    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    @Basic
    @Column(name = "aj_id",nullable = false,precision = 0)
    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
    }

    @Override
    public String toString() {
        return zfbzh + "//" + zfbmc + "//" + jyzt + "//" + dfzh + "//" +
                dfmc + "//" + fkzcs + "//" + fkzje;
    }

    /**
     * 将form数据转化成实体类
     * @param jyjlTjjgsForms
     * @param id
     * @return
     */
    public static List<ZfbJyjlTjjgsEntity> FormToList(List<ZfbJyjlTjjgsForm> jyjlTjjgsForms, long id) {
        Map<String,ZfbJyjlTjjgsEntity> map = new HashMap<>();
        ZfbJyjlTjjgsEntity tjjgs = null;
        for (ZfbJyjlTjjgsForm form : jyjlTjjgsForms) {
            // 是否已添加
            if(map.containsKey(form.getMjyhid()+form.getMijyhid()+form.getJyzt())){
                tjjgs = map.get(form.getMjyhid()+form.getMijyhid()+form.getJyzt());
                tjjgs.setFkzcs(tjjgs.getFkzcs()+1);
                tjjgs.setFkzje(tjjgs.getFkzje().add(form.getJyje()));
            }else{
                // 未添加
                tjjgs = new ZfbJyjlTjjgsEntity();
                tjjgs.setZfbzh(form.getMjyhid());
                tjjgs.setZfbmc(form.getMjxx());
                tjjgs.setJyzt(form.getJyzt());
                tjjgs.setDfzh(form.getMijyhid());
                tjjgs.setDfmc(form.getMijxx());
                tjjgs.setFkzcs(1);
                tjjgs.setFkzje(form.getJyje());
                tjjgs.setAj_id(id);
                map.put(form.getMjyhid()+form.getMijyhid()+form.getJyzt(),tjjgs);
            }
        }
        List<ZfbJyjlTjjgsEntity> jjgsList = new ArrayList<>(map.values());
        return jjgsList;
    }
}
