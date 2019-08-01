package cn.com.sinofaith.bean.customerProBean;

import javax.persistence.*;

@Entity
@Table(name = "customerpro",schema = "",catalog = "")
public class CustomerproEntity {
    private String name;
    private String zjh;

    @Basic
    @Column(name="name",nullable = false,length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Id
    @Column(name="zjh",nullable = false,length = 100)
    public String getZjh() {
        return zjh;
    }

    public void setZjh(String zjh) {
        this.zjh = zjh;
    }

    @Override
    public String toString() {
        return "CustomerproEntity{" +
                "name='" + name + '\'' +
                ", zjh='" + zjh + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerproEntity that = (CustomerproEntity) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return zjh != null ? zjh.equals(that.zjh) : that.zjh == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (zjh != null ? zjh.hashCode() : 0);
        return result;
    }
}
