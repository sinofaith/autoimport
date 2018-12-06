package cn.com.sinofaith.form.zfbForm;

public class ZfbJyjlForm {
    private String mjyhid; // 用户id
    private String mjxx; // 账户名称
    private String direction; // 交易方向
    private String mijyhid; // 对方用户Id
    private String mijxx; // 对方账户名称
    private String spmc; // 商品名称
    private String shrdz; // 收货人地址
    private long jyzcs; // 交易总次数
    private double jyzje; // 交易总金额

    public String getMjyhid() {
        return mjyhid;
    }

    public void setMjyhid(String mjyhid) {
        this.mjyhid = mjyhid;
    }

    public String getMjxx() {
        return mjxx;
    }

    public void setMjxx(String mjxx) {
        this.mjxx = mjxx;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getMijyhid() {
        return mijyhid;
    }

    public void setMijyhid(String mijyhid) {
        this.mijyhid = mijyhid;
    }

    public String getMijxx() {
        return mijxx;
    }

    public void setMijxx(String mijxx) {
        this.mijxx = mijxx;
    }

    public String getSpmc() {
        return spmc;
    }

    public void setSpmc(String spmc) {
        this.spmc = spmc;
    }

    public String getShrdz() {
        return shrdz;
    }

    public void setShrdz(String shrdz) {
        this.shrdz = shrdz;
    }

    public long getJyzcs() {
        return jyzcs;
    }

    public void setJyzcs(long jyzcs) {
        this.jyzcs = jyzcs;
    }

    public double getJyzje() {
        return jyzje;
    }

    public void setJyzje(double jyzje) {
        this.jyzje = jyzje;
    }
}
