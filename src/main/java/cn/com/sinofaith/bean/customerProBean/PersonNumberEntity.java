package cn.com.sinofaith.bean.customerProBean;

import javax.persistence.*;

@Entity
@Table(name = "person_number",schema = "",catalog = "")
public class PersonNumberEntity {
    private long id;
    private String name;
    private String phone;
    private String numbers;
    private String numberName;
    private String sex;
    private String age;
    private String address;
    private String numberType;
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
    @Column(name="phone",length = 100)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    @Basic
    @Column(name="numbers",length = 300)
    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
    @Basic
    @Column(name="numbername",length = 100)
    public String getNumberName() {
        return numberName;
    }

    public void setNumberName(String numberName) {
        this.numberName = numberName;
    }
    @Basic
    @Column(name="sex",length = 100)
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    @Basic
    @Column(name="age",length = 100)
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
    @Basic
    @Column(name="address",length = 300)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @Basic
    @Column(name="numbertype",length = 10)
    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }
    @Basic
    @Column(name="aj_id", nullable = false)
    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
    }
}
