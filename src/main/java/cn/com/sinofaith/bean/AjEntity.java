package cn.com.sinofaith.bean;

import javax.persistence.*;

@Entity
@Table(name = "aj",schema = "",catalog = "")
public class AjEntity {
    private long id = -1;
    private String aj ="";
    private long flg=0;
    private String inserttime;

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
    @Column(name = "inserttime",length = 19)
    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    @Override
    public String toString() {
        return "AjEntity{" +
                "id=" + id +
                ", aj='" + aj + '\'' +
                ", inserttime='" + inserttime + '\'' +
                '}';
    }

    public AjEntity(long id, String aj,long flg, String inserttime) {
        this.id = id;
        this.aj = aj;
        this.flg = flg;
        this.inserttime = inserttime;
    }

    public AjEntity(){
        super();
    }
}
