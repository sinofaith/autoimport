package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.dao.AJDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AjServices {
    @Autowired
    private AJDao ad;

    public void save(AjEntity aj){
         ad.save(aj);
    }

    public List<AjEntity> findByName(String aj){
        return ad.findFilter(aj);
    }
}
