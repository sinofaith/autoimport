package cn.com.sinofaith.bean.pyramidSaleBean;

import javax.persistence.*;

@Entity
@Table(name = "pyramidSale",schema = "",catalog = "")
public class PyramidSaleEntity {
    private long id;
    private String psId;
    private String sponsorId;
    private String mobile;
    private String telphone;
    private String nick_name;
    private String sex;
    private String address;
    private String sfzhm;
    private String bankName;
    private String accountHolder;
    private String accountNumber;
    private long aj_id;

    @Id
    @Column(name="id",nullable = false,precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name="psid",nullable = true,length = 300)
    public String getPsId() {
        return psId;
    }

    public void setPsId(String psId) {
        this.psId = psId;
    }

    @Basic
    @Column(name="sponsorid",nullable = true,length = 300)
    public String getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(String sponsorId) {
        this.sponsorId = sponsorId;
    }

    @Basic
    @Column(name="mobile",nullable = true,length = 300)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        /*if(mobile == null){
            this.mobile = "";
        }else{*/
            this.mobile = mobile;
        /*}*/
    }

    @Basic
    @Column(name="telphone",nullable = true,length = 300)
    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        /*if(telphone == null){
            this.telphone = "";
        }else{*/
            this.telphone = telphone;
        /*}*/
    }

    @Basic
    @Column(name="nick_name",nullable = true,length = 300)
    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        /*if(nick_name == null){
            this.nick_name = "";
        }else {*/
            this.nick_name = nick_name;
        /*}*/
    }

    @Basic
    @Column(name="sex",nullable = true,length = 300)
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        /*if(sex == null){
            this.sex = "";
        }else{*/
            this.sex = sex;
        /*}*/
    }

    @Basic
    @Column(name="address",nullable = true,length = 300)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        /*if(address == null){
            this.address = "";
        }else{*/
            this.address = address;
        /*}*/
    }

    @Basic
    @Column(name="sfzhm",nullable = true,length = 300)
    public String getSfzhm() {
        return sfzhm;
    }

    public void setSfzhm(String sfzhm) {
        /*if(sfzhm == null){
            this.sfzhm = "";
        }else {*/
            this.sfzhm = sfzhm;
        /*}*/
    }

    @Basic
    @Column(name="bankName",nullable = true,length = 300)
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        /*if(bankName == null){
            this.bankName = "";
        }else{*/
            this.bankName = bankName;
        /*}*/
    }

    @Basic
    @Column(name="accountHolder",nullable = true,length = 300)
    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
       /* if(accountHolder == null){
            this.accountHolder = "";
        }else{*/
            this.accountHolder = accountHolder;
       /* }*/
    }

    @Basic
    @Column(name="accountNumber",nullable = true,length = 300)
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        /*if(accountNumber == null){
            this.accountNumber = "";
        }else{*/
            this.accountNumber = accountNumber;
        /*}*/
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
        return "PyramidSaleEntity{" +
                "id=" + id +
                ", psId='" + psId + '\'' +
                ", sponsorId='" + sponsorId + '\'' +
                ", mobile='" + mobile + '\'' +
                ", telphone='" + telphone + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                ", sfzhm='" + sfzhm + '\'' +
                ", bankName='" + bankName + '\'' +
                ", accountHolder='" + accountHolder + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", aj_id=" + aj_id +
                '}';
    }
}
