package cn.com.sinofaith.dao;

import cn.com.sinofaith.bean.RelZjhHmEntity;
import org.springframework.stereotype.Repository;

@Repository
public class RelZjhHmDao extends BaseDao<RelZjhHmEntity> {
    public Object add(RelZjhHmEntity r){
        if(r.getZjh().length()>0&&r.getHm().length()>0){
           return save(r);
        }else{
            return new RelZjhHmEntity();
        }
    }
}
