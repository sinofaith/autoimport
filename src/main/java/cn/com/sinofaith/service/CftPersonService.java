package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.CftPersonEntity;
import cn.com.sinofaith.dao.CftPersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CftPersonService {
    @Autowired
    private CftPersonDao cpd;

    public int save(CftPersonEntity cp){
        return cpd.insert(cp);
    }
}
