package cn.com.sinofaith.bean;

import javax.persistence.*;

@Entity
@Table(name = "aj",schema = "",catalog = "")
public class AjEntity {
    private long id = -1;
    private String aj ="";
    private long flg=0;
    private String filter="";
    private String inserttime;
    private long userId;
    private String zjminsj="";
    private String zjmaxsj="";
    @Id
    @Column(name="id",nullable = false,precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Basic
    @Column(name="aj",nullable = false,length = 100)
    public String getAj() {
        return aj;
    }

    public void setAj(String aj) {
        this.aj = aj;
    }
    @Basic
    @Column(name="flg",nullable = false,precision = 0)
    public long getFlg(){return flg;}
    public void setFlg(long flg){this.flg = flg;}
    @Basic
    @Column(name="filter",nullable = false,precision = 0)
    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Basic
    @Column(name = "inserttime",length = 19)
    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    @Basic
    @Column(name="userId",nullable = false,precision = 0)
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    @Basic
    @Column(name="zjminsj",nullable = false)
    public String getZjminsj() {
        return zjminsj;
    }

    public void setZjminsj(String zjminsj) {
        this.zjminsj = zjminsj;
    }
    @Basic
    @Column(name="zjmaxsj",nullable = false)
    public String getZjmaxsj() {
        return zjmaxsj;
    }

    public void setZjmaxsj(String zjmaxsj) {
        this.zjmaxsj = zjmaxsj;
    }
    @Override
    public String toString() {
        return "AjEntity{" +
                "id=" + id +
                ", aj='" + aj + '\'' +
                ", flg=" + flg +
                ", filter='" + filter + '\'' +
                ", inserttime='" + inserttime + '\'' +
                ", userId=" + userId +
                '}';
    }

    public AjEntity(long id, String aj, long flg, String filter, String inserttime, long userId, String zjminsj, String zjmaxsj) {
        this.id = id;
        this.aj = aj;
        this.flg = flg;
        this.filter = filter;
        this.inserttime = inserttime;
        this.userId = userId;
        this.zjminsj = zjminsj;
        this.zjmaxsj = zjmaxsj;
    }

    public AjEntity(){
        super();
    }
}
