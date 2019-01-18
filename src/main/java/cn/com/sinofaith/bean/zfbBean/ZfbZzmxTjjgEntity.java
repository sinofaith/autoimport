package cn.com.sinofaith.bean.zfbBean;

import cn.com.sinofaith.form.zfbForm.ZfbZzmxTjjgForm;
import cn.com.sinofaith.util.TimeFormatUtil;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ZfbZzmx_Tjjg")
public class ZfbZzmxTjjgEntity {
    private long id;
    private String zfbmc;
    private String zfbzh;
    private String zzcpmc;
    private long jyzcs;
    private long fkzcs;
    private BigDecimal fkzje = new BigDecimal(0);
    private long skzcs;
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
    @Column(name = "zzcpmc",nullable = true,length = 100)
    public String getZzcpmc() {
        return zzcpmc;
    }

    public void setZzcpmc(String zzcpmc) {
        this.zzcpmc = zzcpmc;
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

    @Override
    public String toString() {
        return zfbzh  + "//" + zfbmc+ "//" + zzcpmc + "//" + jyzcs + "//" + fkzcs +
                "//" + fkzje + "//" + skzcs + "//" + skzje;
    }

    /**
     * 转换
     * @param tjjgForms
     * @return
     */
    public static List<ZfbZzmxTjjgEntity> FormToList(List<ZfbZzmxTjjgForm> tjjgForms,long id) {
        List<ZfbZzmxTjjgEntity> zzmxTjjgList = new ArrayList<>();
        for (ZfbZzmxTjjgForm tjjgForm : tjjgForms) {
            ZfbZzmxTjjgEntity tjjg = new ZfbZzmxTjjgEntity();
            if(tjjgForm.getFkfzfbzh()!=null && tjjgForm.getSkfzfbzh()!=null){
                tjjg.setZfbmc(tjjgForm.getFkzhmc());
                tjjg.setZfbzh(tjjgForm.getFkfzfbzh());
                tjjg.setZzcpmc(tjjgForm.getFkzzcpmc());
                if("普通充值".equals(tjjgForm.getFkzzcpmc()) || "提现".equals(tjjgForm.getFkzzcpmc())){
                    tjjg.setJyzcs(tjjgForm.getFkzcs());
                    tjjg.setSkzcs(0);
                    tjjg.setSkzje(new BigDecimal(0));
                }else{
                    tjjg.setJyzcs(tjjgForm.getJyzcs());
                    tjjg.setSkzcs(tjjgForm.getSkzcs());
                    tjjg.setSkzje(tjjgForm.getSkzje());
                }
                tjjg.setFkzcs(tjjgForm.getFkzcs());
                tjjg.setFkzje(tjjgForm.getFkzje());
            }else if(tjjgForm.getFkfzfbzh()!=null && tjjgForm.getSkfzfbzh()==null){
                tjjg.setZfbmc(tjjgForm.getFkzhmc());
                tjjg.setZfbzh(tjjgForm.getFkfzfbzh());
                tjjg.setZzcpmc(tjjgForm.getFkzzcpmc());
                tjjg.setJyzcs(tjjgForm.getFkzcs());
                tjjg.setFkzcs(tjjgForm.getFkzcs());
                tjjg.setFkzje(tjjgForm.getFkzje());
                tjjg.setSkzcs(0);
                tjjg.setSkzje(new BigDecimal(0));
            }else if(tjjgForm.getFkfzfbzh()==null && tjjgForm.getSkfzfbzh()!=null){
                tjjg.setZfbmc(tjjgForm.getSkzhmc());
                tjjg.setZfbzh(tjjgForm.getSkfzfbzh());
                tjjg.setZzcpmc(tjjgForm.getSkzzcpmc());
                tjjg.setJyzcs(tjjgForm.getSkzcs());
                tjjg.setFkzcs(0);
                tjjg.setFkzje(new BigDecimal(0));
                tjjg.setSkzcs(tjjgForm.getSkzcs());
                tjjg.setSkzje(tjjgForm.getSkzje());
            }
            tjjg.setAj_id(id);
            tjjg.setInsert_time(TimeFormatUtil.getDate("/"));
            zzmxTjjgList.add(tjjg);
        }
        return zzmxTjjgList;
    }
}
