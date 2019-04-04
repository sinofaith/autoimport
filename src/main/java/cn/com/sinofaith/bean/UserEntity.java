package cn.com.sinofaith.bean;

import javax.persistence.*;

@Entity
@Table(name = "t_user",schema = "",catalog = "")
public class UserEntity {
    private long id;
    private String username;
    private String password;
    private String inserttime;

    @Id
    @Column(name = "id",precision = 0,nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Basic
    @Column(name = "username",nullable = false,length = 20)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password",nullable = false,length = 20)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "inserttime",nullable = true,length = 20)
    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }
}
