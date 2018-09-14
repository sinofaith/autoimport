package cn.com.sinofaith.form.wlForm;

/**
 * 寄件收件关系
 */
public class WuliuRelationForm {
    // id
    private long id;
    // 寄件人
    private String sender;
    // 寄件电话
    private String ship_phone;
    // 寄件地址
    private String ship_address;
    // 收件人
    private String addressee;
    // 收件电话
    private String sj_phone;
    // 收件地址
    private String sj_address;
    // 寄收次数
    private long num;
    // 案件id
    private long aj_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getShip_phone() {
        return ship_phone;
    }

    public void setShip_phone(String ship_phone) {
        this.ship_phone = ship_phone;
    }

    public String getShip_address() {
        return ship_address;
    }

    public void setShip_address(String ship_address) {
        this.ship_address = ship_address;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getSj_phone() {
        return sj_phone;
    }

    public void setSj_phone(String sj_phone) {
        this.sj_phone = sj_phone;
    }

    public String getSj_address() {
        return sj_address;
    }

    public void setSj_address(String sj_address) {
        this.sj_address = sj_address;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
    }

    @Override
    public String toString() {
        return "WuliuRelationForm{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", ship_phone='" + ship_phone + '\'' +
                ", ship_address='" + ship_address + '\'' +
                ", addressee='" + addressee + '\'' +
                ", sj_phone='" + sj_phone + '\'' +
                ", sj_address='" + sj_address + '\'' +
                ", num=" + num +
                ", aj_id=" + aj_id +
                '}';
    }
}
