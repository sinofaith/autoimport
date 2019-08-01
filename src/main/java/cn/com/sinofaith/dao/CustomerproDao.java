package cn.com.sinofaith.dao;

import cn.com.sinofaith.bean.customerProBean.CustomerproEntity;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerproDao extends BaseDao<CustomerproEntity> {
    public Object add(CustomerproEntity cme){
        if(cme.getZjh().length()>0&&cme.getName().length()>0) {
            return save(cme);
        }else{
            return new CustomerproEntity();
        }
    }
}
