package cn.com.sinofaith.bean.zfbBean;


import cn.com.sinofaith.form.zfbForm.ZfbZzmxTjjgsForm;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "ZfbZzmx_Tjjgs")
public class ZfbZzmxTjjgsEntity {
    private long id;
    private String zfbmc;
    private String zfbzh;
    private String dfmc;
    private String dfzh;
    private long jyzcs = 0;
    private long fkzcs = 0;
    private BigDecimal fkzje = new BigDecimal(0);
    private long skzcs = 0;
    private BigDecimal skzje = new BigDecimal(0);
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
    @Column(name = "jyzcs",nullable = false,precision = 0)
    public long getJyzcs() {
        return jyzcs;
    }

    public void setJyzcs(long jyzcs) {
        this.jyzcs = jyzcs;
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
    @Column(name = "skzcs",nullable = false,precision = 0)
    public long getSkzcs() {
        return skzcs;
    }

    public void setSkzcs(long skzcs) {
        this.skzcs = skzcs;
    }

    @Basic
    @Column(name = "skzje",nullable = false,precision = 0)
    public BigDecimal getSkzje() {
        return skzje;
    }

    public void setSkzje(BigDecimal skzje) {
        this.skzje = skzje;
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

    /**
     * 将form数据转化成实体类
     * @param tjjgsForms
     * @param id
     * @return
     */
    public static List<ZfbZzmxTjjgsEntity> FormToList(List<ZfbZzmxTjjgsForm> tjjgsForms, long id) {
        Map<String,ZfbZzmxTjjgsEntity> map = new HashMap<>();
        ZfbZzmxTjjgsEntity tjjgs = null;
        for (ZfbZzmxTjjgsForm tjjgsForm : tjjgsForms) {
            // 是付款方
            if(tjjgsForm.getYhid().equals(tjjgsForm.getFkfzfbzh())){
                if(map.containsKey(tjjgsForm.getYhid()+tjjgsForm.getSkfzfbzh())){
                    // 已添加
                    tjjgs = map.get(tjjgsForm.getYhid() + tjjgsForm.getSkfzfbzh());
                    tjjgs.setJyzcs(tjjgs.getJyzcs()+1);
                    tjjgs.setFkzcs(tjjgs.getFkzcs()+1);
                    tjjgs.setFkzje(tjjgs.getFkzje().add(tjjgsForm.getZzje()));
                }else{
                    // 未添加
                    tjjgs = new ZfbZzmxTjjgsEntity();
                    tjjgs.setZfbzh(tjjgsForm.getYhid());
                    tjjgs.setZfbmc(tjjgsForm.getZhmc());
                    tjjgs.setDfzh(tjjgsForm.getSkfzfbzh());
                    if(tjjgsForm.getJydfxx().equals("")){
                        tjjgs.setDfmc(tjjgsForm.getSkfzfbzh());
                    }else{
                        String jydfxx = null;
                        if(tjjgsForm.getJydfxx().length() > 16){
                            jydfxx = tjjgsForm.getJydfxx().substring(0,16);
                        }
                        if(jydfxx!=null && !jydfxx.equals(tjjgsForm.getYhid())){
                            tjjgs.setDfmc(tjjgsForm.getJydfxx());
                        }else{
                            tjjgs.setDfmc(tjjgsForm.getSkfzfbzh());
                        }
                    }
                    tjjgs.setJyzcs(1);
                    tjjgs.setFkzcs(1);
                    tjjgs.setFkzje(tjjgsForm.getZzje());
                    tjjgs.setAj_id(id);
                    map.put(tjjgsForm.getYhid()+tjjgsForm.getSkfzfbzh(),tjjgs);
                }
            }else if(tjjgsForm.getYhid().equals(tjjgsForm.getSkfzfbzh())){ //收款方
                if(map.containsKey(tjjgsForm.getYhid()+tjjgsForm.getFkfzfbzh())){
                    // 已添加
                    tjjgs = map.get(tjjgsForm.getYhid() + tjjgsForm.getFkfzfbzh());
                    tjjgs.setJyzcs(tjjgs.getJyzcs()+1);
                    tjjgs.setSkzcs(tjjgs.getSkzcs()+1);
                    tjjgs.setSkzje(tjjgs.getSkzje().add(tjjgsForm.getZzje()));
                }else{
                    // 未添加
                    tjjgs = new ZfbZzmxTjjgsEntity();
                    tjjgs.setZfbzh(tjjgsForm.getYhid());
                    tjjgs.setZfbmc(tjjgsForm.getZhmc());
                    tjjgs.setDfzh(tjjgsForm.getFkfzfbzh());
                    if(tjjgsForm.getJydfxx().equals("")){
                        tjjgs.setDfmc(tjjgsForm.getFkfzfbzh());
                    }else {
                        String jydfxx = null;
                        if(tjjgsForm.getJydfxx().length() > 16){
                            jydfxx = tjjgsForm.getJydfxx().substring(0,16);
                        }
                        if (jydfxx!=null && !jydfxx.equals(tjjgsForm.getYhid())) {
                            tjjgs.setDfmc(tjjgsForm.getJydfxx());
                        } else {
                            tjjgs.setDfmc(tjjgsForm.getFkfzfbzh());
                        }
                    }
                    tjjgs.setJyzcs(1);
                    tjjgs.setSkzcs(1);
                    tjjgs.setSkzje(tjjgsForm.getZzje());
                    tjjgs.setAj_id(id);
                    map.put(tjjgsForm.getYhid()+tjjgsForm.getFkfzfbzh(),tjjgs);
                }
            }
        }
        List<ZfbZzmxTjjgsEntity> zzmxTjjgsList = new ArrayList<>(map.values());
        return zzmxTjjgsList;
    }
}
