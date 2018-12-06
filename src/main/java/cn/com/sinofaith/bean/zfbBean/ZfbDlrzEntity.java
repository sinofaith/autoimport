package cn.com.sinofaith.bean.zfbBean;

import javax.persistence.*;

@Entity
@Table(name="ZfbDlrzForm")
public class ZfbDlrzEntity {
    private long id;
    private String dlzh;
    private String zfbyhId;
    private String zhm;
    private String khdIp;
    private String czfssj;
    private String dyxcsj;
    private String insert_time;
    private long aj_id;

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator="zfb_dlrznum")
    @SequenceGenerator(name="zfb_dlrznum",sequenceName="SEQ_ZFBDLRZ_ID",allocationSize=1,initialValue=1)
    @Column(name="id",nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Basic
    @Column(name = "dlzh",nullable = true,length = 100)
    public String getDlzh() {
        return dlzh;
    }

    public void setDlzh(String dlzh) {
        this.dlzh = dlzh;
    }
    @Basic
    @Column(name = "zfbyhId",nullable = true,length = 100)
    public String getZfbyhId() {
        return zfbyhId;
    }

    public void setZfbyhId(String zfbyhId) {
        this.zfbyhId = zfbyhId;
    }
    @Basic
    @Column(name = "zhm",nullable = true,length = 100)
    public String getZhm() {
        return zhm;
    }

    public void setZhm(String zhm) {
        this.zhm = zhm;
    }
    @Basic
    @Column(name = "khdIp",nullable = true,length = 100)
    public String getKhdIp() {
        return khdIp;
    }

    public void setKhdIp(String khdIp) {
        this.khdIp = khdIp;
    }
    @Basic
    @Column(name = "czfssj",nullable = true,length = 100)
    public String getCzfssj() {
        return czfssj;
    }

    public void setCzfssj(String czfssj) {
        this.czfssj = czfssj;
    }
    @Basic
    @Column(name = "dyxcsj",nullable = true,length = 100)
    public String getDyxcsj() {
        return dyxcsj;
    }

    public void setDyxcsj(String dyxcsj) {
        this.dyxcsj = dyxcsj;
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
        return "ZfbDlrzEntity{" +
                "id=" + id +
                ", dlzh='" + dlzh + '\'' +
                ", zfbyhId='" + zfbyhId + '\'' +
                ", zhm='" + zhm + '\'' +
                ", khdIp='" + khdIp + '\'' +
                ", czfssj='" + czfssj + '\'' +
                ", dyxcsj='" + dyxcsj + '\'' +
                ", insert_time='" + insert_time + '\'' +
                ", aj_id=" + aj_id +
                '}';
    }
}
