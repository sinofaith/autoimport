package cn.com.sinofaith.bean.zfbBean;

import javax.persistence.*;

@Entity
@Table(name = "ZfbZcxx")
public class ZfbZcxxEntity {

    private long id;
    private String yhId;//用户Id
    private String dlyx;//登陆邮箱
    private String dlsj;//登陆手机
    private String zhmc;//账户名称
    private String zjlx;//证件类型
    private String zjh;//证件号
    private double kyye;//可用余额
    private String bdsj;//绑定手机
    private String bdyhk;//绑定银行卡
    private String dyxcsj;//对应的协查数据
    private String inserttime;
    private long aj_id;

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator="zfb_zcxxnum")
    @SequenceGenerator(name="zfb_zcxxnum",sequenceName="SEQ_ZFBZCXX_ID",allocationSize=1,initialValue=1)
    @Column(name="id",nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "yhId",nullable = true,length = 100)
    public String getYhId() {
        return yhId;
    }

    public void setYhId(String yhId) {
        this.yhId = yhId;
    }

    @Basic
    @Column(name = "dlyx",nullable = true,length = 100)
    public String getDlyx() {
        return dlyx;
    }

    public void setDlyx(String dlyx) {
        this.dlyx = dlyx;
    }

    @Basic
    @Column(name = "dlsj",nullable = true,length = 100)
    public String getDlsj() {
        return dlsj;
    }

    public void setDlsj(String dlsj) {
        this.dlsj = dlsj;
    }

    @Basic
    @Column(name = "zhmc",nullable = true,length = 100)
    public String getZhmc() {
        return zhmc;
    }

    public void setZhmc(String zhmc) {
        this.zhmc = zhmc;
    }

    @Basic
    @Column(name = "zjlx",nullable = true,length = 100)
    public String getZjlx() {
        return zjlx;
    }

    public void setZjlx(String zjlx) {
        this.zjlx = zjlx;
    }

    @Basic
    @Column(name = "zjh",nullable = true,length = 100)
    public String getZjh() {
        return zjh;
    }

    public void setZjh(String zjh) {
        this.zjh = zjh;
    }

    @Basic
    @Column(name = "kyye",nullable = true,precision = 0)
    public double getKyye() {
        return kyye;
    }

    public void setKyye(double kyye) {
        this.kyye = kyye;
    }

    @Basic
    @Column(name = "bdsj",nullable = true,length = 100)
    public String getBdsj() {
        return bdsj;
    }

    public void setBdsj(String bdsj) {
        this.bdsj = bdsj;
    }

    @Basic
    @Column(name = "bdyhk",nullable = true,length = 500)
    public String getBdyhk() {
        return bdyhk;
    }

    public void setBdyhk(String bdyhk) {
        this.bdyhk = bdyhk;
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
    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
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
        return "ZfbZcxxEntity{" +
                "id=" + id +
                ", yhId='" + yhId + '\'' +
                ", dlyx='" + dlyx + '\'' +
                ", dlsj='" + dlsj + '\'' +
                ", zhmc='" + zhmc + '\'' +
                ", zjlx='" + zjlx + '\'' +
                ", zjh='" + zjh + '\'' +
                ", kyye='" + kyye + '\'' +
                ", bdsj='" + bdsj + '\'' +
                ", bdyhk='" + bdyhk + '\'' +
                ", dyxcsj='" + dyxcsj + '\'' +
                ", inserttime='" + inserttime + '\'' +
                ", aj_id=" + aj_id +
                '}';
    }
}
