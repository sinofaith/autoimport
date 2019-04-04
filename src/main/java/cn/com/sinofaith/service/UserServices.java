package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.UserEntity;
import cn.com.sinofaith.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {
    @Autowired
    private UserDao ud;

    public UserEntity login(UserEntity user){
        UserEntity userinfo = null;
        List list  = ud.loging(user);
        if(list.size()>0){
            userinfo=(UserEntity) list.get(0);
        }
        return userinfo;
    }

    public String getSeach(String seachCode, String seachCondition){
        StringBuffer seach = new StringBuffer();

        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","")
                    .replace(" ","").replace(" ","").replace("\t","");
            seach.append(" and c." + seachCondition + " = " + "'" + seachCode + "'");
        }else{
            seach.append(" and ( 1=1 ) ");
        }
//        if(orderby != null){
//            seach .append(" order by "+orderby).append(desc);
//        }
        return seach.toString();
    }


    public int findUser(String username){
        String seach = " and c.username = '"+username+"'";
        return ud.getAllRowCount(seach);
    }

    public long saveUser(UserEntity user){
        return (long) ud.save(user);
    }

    public void deleteUserById(long id){
        ud.delete("delete from UserEntity where id="+id);
    }
}
