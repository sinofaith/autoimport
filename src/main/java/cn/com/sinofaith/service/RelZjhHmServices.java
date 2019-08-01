package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.RelZjhHmEntity;
import cn.com.sinofaith.dao.RelZjhHmDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelZjhHmServices {
    @Autowired
    private RelZjhHmDao zd;

    public void save(RelZjhHmEntity r){
        zd.save(r);
    }
}
