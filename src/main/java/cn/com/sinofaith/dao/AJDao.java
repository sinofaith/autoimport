package cn.com.sinofaith.dao;

import cn.com.sinofaith.bean.AjEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AJDao extends BaseDao<AjEntity>{
    public void insert(AjEntity aj){
         save(aj);
    }

    public List<AjEntity> findFilter(String aj){
        List<AjEntity> listAj= find("from AjEntity where aj = '"+aj+"'");
        return listAj;
    }
}
