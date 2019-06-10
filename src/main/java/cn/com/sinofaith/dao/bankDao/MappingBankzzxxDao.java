package cn.com.sinofaith.dao.bankDao;

import cn.com.sinofaith.bean.bankBean.MappingBankzzxxEntity;
import cn.com.sinofaith.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MappingBankzzxxDao extends BaseDao<MappingBankzzxxEntity> {
    public List<MappingBankzzxxEntity> getAll(){
        List<MappingBankzzxxEntity> listmb = find("from MappingBankzzxxEntity");
       return listmb;
    }
}
