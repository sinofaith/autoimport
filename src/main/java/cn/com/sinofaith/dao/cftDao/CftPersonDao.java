package cn.com.sinofaith.dao.cftDao;

import cn.com.sinofaith.bean.cftBean.CftPersonEntity;
import cn.com.sinofaith.dao.BaseDao;
import org.springframework.stereotype.Repository;

@Repository
public class CftPersonDao extends BaseDao<CftPersonEntity> {
    public int insert(CftPersonEntity cft){
        saveOrUpdate(cft);
        return 1;
    }
}
