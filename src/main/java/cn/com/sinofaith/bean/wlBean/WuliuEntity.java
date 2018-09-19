package cn.com.sinofaith.bean.wlBean;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *   物流总表
 */
@Entity
@Table(name = "wuliu",schema = "",catalog = "")
public class WuliuEntity {
    public long id;
    // 运单号
    private String waybill_id;
    // 寄件时间
    private String ship_time;
    // 寄件地址
    private String ship_address;
    // 寄件人
    private String sender;
    // 寄件电话
    private String ship_phone;
    // 寄件手机
    private String ship_mobilephone;
    // 收件地址
    private String sj_address;
    // 收件人
    private String addressee;
    // 收件电话
    private String sj_phone;
    // 收件手机
    private String sj_mobilephone;
    // 收件员
    private String collector;
    // 托寄物
    private String tjw;
    // 付款方式
    private String payment;
    // 代收货款
    private String dshk;
    // 计费重量
    private String weight;
    // 件数
    private String number_cases;
    // 运费
    private String freight;
    // 插入时间
    private String insertTime;
    // 案件id
    private long aj_id;

    @Id
    @Column(name = "id",nullable = false,precision = 0)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "waybill_id",nullable = true,length = 100)
    public String getWaybill_id() {
        return waybill_id;
    }
    public void setWaybill_id(String waybill_id) {
        this.waybill_id = waybill_id;
    }

    @Basic
    @Column(name = "ship_time",nullable = true,length = 100)
    public String getShip_time() {
        return ship_time;
    }
    public void setShip_time(String ship_time) {
        this.ship_time = ship_time;
    }

    @Basic
    @Column(name = "ship_address",nullable = true,length = 100)
    public String getShip_address() {
        return ship_address;
    }
    public void setShip_address(String ship_address) {
        this.ship_address = ship_address;
    }

    @Basic
    @Column(name = "sender",nullable = true,length = 100)
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

    @Basic
    @Column(name = "ship_phone",nullable = true,length = 100)
    public String getShip_phone() {
        return ship_phone;
    }
    public void setShip_phone(String ship_phone) {
        this.ship_phone = ship_phone;
    }

    @Basic
    @Column(name = "ship_mobilephone",nullable = true,length = 100)
    public String getShip_mobilephone() {
        return ship_mobilephone;
    }
    public void setShip_mobilephone(String ship_mobilephone) {
        this.ship_mobilephone = ship_mobilephone;
    }

    @Basic
    @Column(name = "sj_address",nullable = true,length = 100)
    public String getSj_address() {
        return sj_address;
    }
    public void setSj_address(String sj_address) {
        this.sj_address = sj_address;
    }

    @Basic
    @Column(name = "addressee",nullable = true,length = 100)
    public String getAddressee() {
        return addressee;
    }
    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    @Basic
    @Column(name = "sj_phone",nullable = true,length = 100)
    public String getSj_phone() {
        return sj_phone;
    }
    public void setSj_phone(String sj_phone) {
        this.sj_phone = sj_phone;
    }

    @Basic
    @Column(name = "sj_mobilephone",nullable = true,length = 100)
    public String getSj_mobilephone() {
        return sj_mobilephone;
    }
    public void setSj_mobilephone(String sj_mobilephone) {
        this.sj_mobilephone = sj_mobilephone;
    }

    @Basic
    @Column(name = "collector",nullable = true,length = 100)
    public String getCollector() {
        return collector;
    }
    public void setCollector(String collector) {
        this.collector = collector;
    }

    @Basic
    @Column(name = "tjw",nullable = true,length = 500)
    public String getTjw() {
        return tjw;
    }
    public void setTjw(String tjw) {
        this.tjw = tjw;
    }

    @Basic
    @Column(name = "payment",nullable = true,length = 100)
    public String getPayment() {
        return payment;
    }
    public void setPayment(String payment) {
        this.payment = payment;
    }

    @Basic
    @Column(name = "dshk",nullable = true,length = 100)
    public String getDshk() {
        return dshk;
    }
    public void setDshk(String dshk) {
        this.dshk = dshk;
    }

    @Basic
    @Column(name = "weight",nullable = true,length = 100)
    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Basic
    @Column(name = "number_cases",nullable = true,length = 100)
    public String getNumber_cases() {
        return number_cases;
    }
    public void setNumber_cases(String number_cases) {
        this.number_cases = number_cases;
    }

    @Basic
    @Column(name = "freight",nullable = true,length = 100)
    public String getFreight() {
        return freight;
    }
    public void setFreight(String freight) {
        this.freight = freight;
    }

    @Basic
    @Column(name = "insert_time",nullable = true,length = 100)
    public String getInsertTime() {
        return insertTime;
    }
    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
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
        return "WuliuEntity{" +
                "id=" + id +
                ", waybill_id='" + waybill_id + '\'' +
                ", ship_time='" + ship_time + '\'' +
                ", ship_address='" + ship_address + '\'' +
                ", sender='" + sender + '\'' +
                ", ship_phone='" + ship_phone + '\'' +
                ", ship_mobilephone='" + ship_mobilephone + '\'' +
                ", sj_address='" + sj_address + '\'' +
                ", addressee='" + addressee + '\'' +
                ", sj_phone='" + sj_phone + '\'' +
                ", sj_mobilephone='" + sj_mobilephone + '\'' +
                ", collector='" + collector + '\'' +
                ", tjw='" + tjw + '\'' +
                ", payment='" + payment + '\'' +
                ", dshk='" + dshk + '\'' +
                ", weight='" + weight + '\'' +
                ", number_cases='" + number_cases + '\'' +
                ", freight='" + freight + '\'' +
                ", insertTime='" + insertTime + '\'' +
                ", aj_id=" + aj_id +
                '}';
    }

    public WuliuEntity() {
        super();
    }

    public WuliuEntity(long id, String waybill_id, String ship_time, String ship_address, String sender, String ship_phone, String ship_mobilephone, String sj_address, String addressee, String sj_phone, String sj_mobilephone, String collector, String tjw, String payment, String dshk, String weight, String number_cases, String freight, String insertTime, long aj_id) {
        this.id = id;
        this.waybill_id = waybill_id;
        this.ship_time = ship_time;
        this.ship_address = ship_address;
        this.sender = sender;
        this.ship_phone = ship_phone;
        this.ship_mobilephone = ship_mobilephone;
        this.sj_address = sj_address;
        this.addressee = addressee;
        this.sj_phone = sj_phone;
        this.sj_mobilephone = sj_mobilephone;
        this.collector = collector;
        this.tjw = tjw;
        this.payment = payment;
        this.dshk = dshk;
        this.weight = weight;
        this.number_cases = number_cases;
        this.freight = freight;
        this.insertTime = insertTime;
        this.aj_id = aj_id;
    }

    public static WuliuEntity listToObj(List<String> list, Map<String,Integer> title){
        WuliuEntity wl = new WuliuEntity();
        // 运单号
        wl.setWaybill_id("".equals(list.get(title.get("waybill_id")).trim()) ? null:list.get(title.get("waybill_id")).trim());
        // 寄件时间
        wl.setShip_time("".equals(list.get(title.get("ship_time")).trim()) ? null:list.get(title.get("ship_time")).trim());
        // 寄件地址
        wl.setShip_address("".equals(list.get(title.get("ship_address")).trim()) ? null:list.get(title.get("ship_address")).trim());
        if(list.get(title.get("ship_address")).trim().equals("0")){
            wl.setShip_address("");
        }
        // 寄件电话  为0时
        if("0".equals(list.get(title.get("ship_phone")).trim())){
            // 手机不为0时
            if(!"0".equals(list.get(title.get("ship_mobilephone")).trim())){
                // 电话=手机
                wl.setShip_phone(list.get(title.get("ship_mobilephone")).trim());
            }else{
                // 电话=寄件人
                wl.setShip_phone(list.get(title.get("sender")).trim());
            }
            // 手机为null时
            if("".equals(list.get(title.get("ship_mobilephone")).trim())){
                // 电话=寄件人
                wl.setShip_phone(list.get(title.get("sender")).trim());
            }
        }else{
            wl.setShip_phone(list.get(title.get("ship_phone")).trim());
        }
        // 寄件人
        wl.setSender("".equals(list.get(title.get("sender")).trim())? null:list.get(title.get("sender")).trim());
        if(list.get(title.get("sender")).trim().equals("0")){
            wl.setSender(wl.getShip_phone());
        }
        // 寄件手机
        wl.setShip_mobilephone("".equals(list.get(title.get("ship_mobilephone")).trim()) ? null:list.get(title.get("ship_mobilephone")).trim());
        // 收件地址
        wl.setSj_address("".equals(list.get(title.get("sj_address")).trim()) ? null:list.get(title.get("sj_address")).trim());
        if(list.get(title.get("sj_address")).trim().equals("0")){
            wl.setSj_address("");
        }
        // 收件电话 为0时
        if("0".equals(list.get(title.get("sj_phone")).trim())){
            // 手机 不为0时
            if(!"0".equals(list.get(title.get("sj_mobilephone")).trim())){
                // 电话=手机
                wl.setSj_phone(list.get(title.get("sj_mobilephone")).trim());
            }else{
                // 电话=收件人
                wl.setSj_phone(list.get(title.get("addressee")).trim());
            }
            // 手机为null
            if("".equals(list.get(title.get("sj_mobilephone")).trim())){
                // 电话=收件人
                wl.setSj_phone(list.get(title.get("addressee")).trim());
            }
        }else{
            wl.setSj_phone(list.get(title.get("sj_phone")).trim());
        }
        // 电话<11位
        if(list.get(title.get("sj_phone")).trim().length()<11){
            // 手机>=11位
            if(list.get(title.get("sj_mobilephone")).trim().length()>=11){
                wl.setSj_phone(list.get(title.get("sj_mobilephone")));
            }else if(list.get(title.get("sj_mobilephone")).trim().equals("18755457379") || list.get(title.get("sj_mobilephone")).trim().equals("0")){
                // 手机号=18755457379
                // 收件人=收件电话
                wl.setAddressee(wl.getSj_phone());
            }
        }else{
            wl.setSj_phone(list.get(title.get("sj_phone")).trim());
        }
        // 收件人
        wl.setAddressee("".equals(list.get(title.get("addressee")).trim()) ? null:list.get(title.get("addressee")).trim());
        if(list.get(title.get("addressee")).trim().equals("0")){
            wl.setAddressee(wl.getSj_phone());
        }
        // 收件手机
        wl.setSj_mobilephone("".equals(list.get(title.get("sj_mobilephone")).trim()) ? null:list.get(title.get("sj_mobilephone")).trim());
        // 收件员collector
        if(title.keySet().contains("collector")){
            wl.setCollector("".equals(list.get(title.get("collector")).trim()) ? null:list.get(title.get("collector")).trim());
        }else{
            wl.setCollector(null);
        }
        // 托寄物
        wl.setTjw("".equals(list.get(title.get("tjw")).trim()) ? null:list.get(title.get("tjw")).trim());
        // 付款方式
        wl.setPayment("".equals(list.get(title.get("payment")).trim()) ? null:list.get(title.get("payment")).trim());
        // 代收货款
        if(title.keySet().contains("dshk")){
            wl.setDshk("".equals(list.get(title.get("dshk")).trim()) ? null:list.get(title.get("dshk")).trim());
        }else{
            wl.setDshk(null);
        }
        // 计费重量
        wl.setWeight("".equals(list.get(title.get("weight")).trim()) ? null:list.get(title.get("weight")).trim());
        // 件数
        wl.setNumber_cases("".equals(list.get(title.get("number_cases")).trim()) ? null:list.get(title.get("number_cases")).trim());
        // 运费
        wl.setFreight("".equals(list.get(title.get("freight")).trim()) ? null:list.get(title.get("freight")).trim());
        return wl;
    }



    /**
     * 将list装成wulius
     * @param list
     */
    public static List<WuliuEntity> listToWulius(List<Object[]> list) {
        List<WuliuEntity> wls = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            WuliuEntity wuliu = new WuliuEntity();
            Object[] str = list.get(i);
            if(str[0]!=null && str[0].toString()!=null){
                wuliu.setWaybill_id(str[0].toString());
            }
            if(str[1]!=null && str[1].toString()!=null){
                wuliu.setShip_time(str[1].toString());
            }
            if(str[2]!=null && str[2].toString()!=null){
                wuliu.setShip_address(str[2].toString());
            }
            if(str[3]!=null && str[3].toString()!=null){
                wuliu.setSender(str[3].toString());
            }
            if(str[4]!=null && str[4].toString()!=null){
                wuliu.setShip_phone(str[4].toString());
            }
            if(str[5]!=null && str[5].toString()!=null){
                wuliu.setShip_mobilephone(str[5].toString());
            }
            if(str[6]!=null && str[6].toString()!=null){
                wuliu.setSj_address(str[6].toString());
            }
            if(str[7]!=null && str[7].toString()!=null){
                wuliu.setAddressee(str[7].toString());
            }
            if(str[8]!=null && str[8].toString()!=null){
                wuliu.setSj_phone(str[8].toString());
            }
            if(str[9]!=null && str[9].toString()!=null){
                wuliu.setSj_mobilephone(str[9].toString());
            }
            if(str[10]!=null && str[10].toString()!=null){
                wuliu.setTjw(str[10].toString());
            }
            if(str[11]!=null && str[11].toString()!=null){
                wuliu.setDshk(str[11].toString());
            }
            if(str[12]!=null && str[12].toString()!=null){
                wuliu.setNumber_cases(str[12].toString());
            }
            if(str[13]!=null && str[13].toString()!=null){
                wuliu.setPayment(str[13].toString());
            }
            if(str[14]!=null && str[14].toString()!=null){
                wuliu.setFreight(str[14].toString());
            }
            wls.add(wuliu);
        }
        return wls;
    }
}
