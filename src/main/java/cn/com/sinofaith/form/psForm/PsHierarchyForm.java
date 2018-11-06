package cn.com.sinofaith.form.psForm;

public class PsHierarchyForm {
    private String psId;
    private String sponsorid;
    private String psAccountholder;
    private String sfzhm;
    private String mobile;
    private String address;
    private String accountnumber;
    private Long tier;
    private String path;
    private Long directDrive;
    private Long containsTier;
    private Long directReferNum;

    public String getPsId() {
        return psId;
    }

    public void setPsId(String psId) {
        this.psId = psId;
    }

    public String getPsAccountholder() {
        return psAccountholder;
    }

    public void setPsAccountholder(String psAccountholder) {
        this.psAccountholder = psAccountholder;
    }

    public String getSfzhm() {
        return sfzhm;
    }

    public void setSfzhm(String sfzhm) {
        this.sfzhm = sfzhm;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getSponsorid() {
        return sponsorid;
    }

    public void setSponsorid(String sponsorid) {
        this.sponsorid = sponsorid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getTier() {
        return tier;
    }

    public void setTier(Long tier) {
        if(tier==null){
            this.tier = 0l;
        }else{
            this.tier = tier;
        }
    }

    public Long getDirectDrive() {
        return directDrive;
    }

    public void setDirectDrive(Long directDrive) {
        if(directDrive==null){
            this.directDrive = 0l;
        }else{
            this.directDrive = directDrive;
        }
    }

    public Long getContainsTier() {
        return containsTier;
    }

    public void setContainsTier(Long containsTier) {
        if(containsTier==null){
            this.containsTier = 0l;
        }else {
            this.containsTier = containsTier;
        }
    }

    public Long getDirectReferNum() {
        return directReferNum;
    }

    public void setDirectReferNum(Long directReferNum) {
        if(directReferNum==null){
            this.directReferNum = 0l;
        }else {
            this.directReferNum = directReferNum;
        }
    }
}
