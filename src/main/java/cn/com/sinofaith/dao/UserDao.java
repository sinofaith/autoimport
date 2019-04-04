package cn.com.sinofaith.dao;

import cn.com.sinofaith.bean.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserDao extends BaseDao<UserEntity> {
    public List loging(UserEntity user){
        String hql = "from UserEntity where username = '"+user.getUsername()+
                "' and password='"+user.getPassword()+"'";
        return  find(hql);
    }

    public int getAllRowCount(String seachCode){
        String sql = "select to_char(count(*)) num from w_user c where 1=1 "+seachCode;
        List list = findBySQL(sql);
        Map map =(Map)list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List getDoPage(String seachCode, int offset, int length){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM (SELECT  * from w_user c ");
        sql.append(" where 1=1 "+seachCode+" order by c.inserttime desc ) a ");
        sql.append("WHERE ROWNUM <= "+offset*length+") WHERE rn >= "+((offset-1)*length+1));

        return findBySQL(sql.toString());
    }
}
