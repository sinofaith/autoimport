package cn.com.sinofaith.service.cftServices;

import cn.com.sinofaith.bean.cftBean.CftPersonEntity;
import cn.com.sinofaith.dao.cftDao.CftPersonDao;
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

