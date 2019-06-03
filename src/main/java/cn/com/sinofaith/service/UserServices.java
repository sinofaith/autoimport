package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.UserEntity;
import cn.com.sinofaith.dao.UserDao;
import cn.com.sinofaith.page.Page;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    public int zcpz(long userId,String loginTime,long zcpz){
        return ud.updateBySql("update t_user set zcpz ="+zcpz+" , loginTime = '"+loginTime+"' where id ="+userId);
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

    public Page queryForPage(int currentPage, int pageSize, String seach){
        Page page = new Page();
        int allRow = ud.getAllRowCount(seach);
        List<UserEntity> users = new ArrayList<>();
        if(allRow>0){
            users = ud.getDoPage(seach,currentPage,pageSize);

        }
        page.setList(users);
        page.setPageNo(currentPage);
        page.setPageSize(pageSize);
        page.setTotalRecords(allRow);
        return page;
    }


    public int findUser(String username){
        String seach = " and c.username = '"+username+"'";
        return ud.getAllRowCount(seach);
    }

    public List<UserEntity> getAllUser(){
        return ud.find("from UserEntity where zcpz = 1 order by username ");
    }

    public List<UserEntity> getGrand(long aj_id){
        List list = ud.findBySQL(" select u.id,u.username from rel_grand_aj a " +
                " left join T_User u on a.userid = u.id " +
                " where  a.ajid ="+aj_id+" order by u.username ");
        List<UserEntity> result = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            UserEntity u = new UserEntity();
            u.setId(Long.parseLong(map.get("ID").toString()));
            u.setUsername((String) map.get("USERNAME"));
            u.setName((String) map.get("NAME"));
            result.add(u);
        }
        return result;
    }

    public String getUserGrand(long aj_id){
        List<UserEntity> all = getAllUser();
        all.forEach( x ->{
            x.setRole(-1);
            x.setPassword(null);
            x.setInserttime(null);
        });
        List<UserEntity> grand = getGrand(aj_id);
        Map<String,List<UserEntity>> map = new HashMap<>();
        map.put("all",all);
        map.put("grand",grand);
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    public long saveUser(UserEntity user){
        return (long) ud.save(user);
    }

    public void deleteUserById(long id){
        ud.delete("delete from UserEntity where id="+id);
    }
}
