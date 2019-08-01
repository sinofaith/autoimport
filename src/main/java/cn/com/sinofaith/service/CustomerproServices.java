package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.customerProBean.CustomerproEntity;
import cn.com.sinofaith.dao.CustomerproDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerproServices {
    @Autowired
    private CustomerproDao cd;

    public void save(CustomerproEntity ce){
        cd.save(ce);
    }
}
