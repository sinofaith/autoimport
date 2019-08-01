package cn.com.sinofaith.bean.customerProBean;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
@Table(name = "person_relation",schema = "",catalog = "")
public class PersonRelationEntity {
    private long id;
    private String name;
    private String pname;
    private String relationName;
    private String relationShow;
    private String relationMark;
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
    @Column(name="name",nullable = false,precision = 0)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Basic
    @Column(name="pname",nullable = false,precision = 0)
    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
    @Basic
    @Column(name="relationname",nullable = false,precision = 0)
    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }
    @Basic
    @Column(name="relationshow",nullable = false,precision = 0)
    public String getRelationShow() {
        return relationShow;
    }

    public void setRelationShow(String relationShow) {
        this.relationShow = relationShow;
    }
    @Basic
    @Column(name="relationmark",nullable = false,precision = 0)
    public String getRelationMark() {
        return relationMark;
    }

    public void setRelationMark(String relationMark) {
        this.relationMark = relationMark;
    }
    @Basic
    @Column(name="aj_id",nullable = false,precision = 0)
    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
    }

    @Override
    public String toString() {
        return "PersonRelationEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pname='" + pname + '\'' +
                ", relationName='" + relationName + '\'' +
                ", relationShow='" + relationShow + '\'' +
                ", relationMark='" + relationMark + '\'' +
                ", aj_id=" + aj_id +
                '}';
    }
}
