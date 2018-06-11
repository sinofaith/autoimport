package cn.com.sinofaith.dao;

import cn.com.sinofaith.bean.CftPersonEntity;
import org.springframework.stereotype.Repository;

@Repository
public class CftPersonDao extends BaseDao<CftPersonEntity> {
    public int insert(CftPersonEntity cft){
        saveOrUpdate(cft);
        return 1;
    }
}
