package cn.com.sinofaith.bean.customerProBean;

import javax.persistence.*;

@Entity
@Table(name = "person_company",schema = "",catalog = "")
public class PersonCompanyEntity {
    private long id;
    private String name;
    private String company;
    private String companyWeb;
    private String companyAdd;
    private String companyRemark;
    private String companyPhone;
    private String companyEmail;
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
    @Column(name="name",length = 100,nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Basic
    @Column(name="company",length = 300,nullable = false)
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    @Basic
    @Column(name="companyweb",length = 300)
    public String getCompanyWeb() {
        return companyWeb;
    }

    public void setCompanyWeb(String companyWeb) {
        this.companyWeb = companyWeb;
    }
    @Basic
    @Column(name="companyadd",length = 300)
    public String getCompanyAdd() {
        return companyAdd;
    }

    public void setCompanyAdd(String companyAdd) {
        this.companyAdd = companyAdd;
    }
    @Basic
    @Column(name="companyremark",length = 2000)
    public String getCompanyRemark() {
        return companyRemark;
    }

    public void setCompanyRemark(String companyRemark) {
        this.companyRemark = companyRemark;
    }
    @Basic
    @Column(name="aj_id")
    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
    }
    @Basic
    @Column(name="companyPhone")
    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }
    @Basic
    @Column(name="companyEmail")
    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    @Override
    public String toString() {
        return "PersonCompanyEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", companyWeb='" + companyWeb + '\'' +
                ", companyAdd='" + companyAdd + '\'' +
                ", companyRemark='" + companyRemark + '\'' +
                ", companyPhone='" + companyPhone + '\'' +
                ", companyEmail='" + companyEmail + '\'' +
                ", aj_id=" + aj_id +
                '}';
    }
}
