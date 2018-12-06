package cn.com.sinofaith.form.zfbForm;

public class ZfbZzmxForm {
    // 用户Id
    private String yhId;
    // 账户名称
    private String zhmc;
    // 证件类型
    private String zjlx;
    //证件号
    private String zjh;
    //对应的协查数据
    private String dyxcsj;
    // 转账总次数
    private long zzcs;
    // 转账产品名称
    private String zzcpmc;
    // 转账总金额
    private double zzje;

    public String getYhId() {
        return yhId;
    }

    public void setYhId(String yhId) {
        this.yhId = yhId;
    }

    public String getZhmc() {
        return zhmc;
    }

    public void setZhmc(String zhmc) {
        this.zhmc = zhmc;
    }

    public String getZjlx() {
        return zjlx;
    }

    public void setZjlx(String zjlx) {
        this.zjlx = zjlx;
    }

    public String getZjh() {
        return zjh;
    }

    public void setZjh(String zjh) {
        this.zjh = zjh;
    }

    public String getDyxcsj() {
        return dyxcsj;
    }

    public void setDyxcsj(String dyxcsj) {
        this.dyxcsj = dyxcsj;
    }

    public long getZzcs() {
        return zzcs;
    }

    public void setZzcs(long zzcs) {
        this.zzcs = zzcs;
    }

    public String getZzcpmc() {
        return zzcpmc;
    }

    public void setZzcpmc(String zzcpmc) {
        this.zzcpmc = zzcpmc;
    }

    public double getZzje() {
        return zzje;
    }

    public void setZzje(double zzje) {
        this.zzje = zzje;
    }
}
