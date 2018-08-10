package cn.com.sinofaith.dao.bankDao;

import cn.com.sinofaith.bean.bankBean.BankPersonEntity;
import cn.com.sinofaith.dao.BaseDao;
import org.springframework.stereotype.Repository;

@Repository
public class BankPersonDao extends BaseDao<BankPersonEntity>{
    public int insert(BankPersonEntity bpe){
        saveOrUpdate(bpe);
        return 1;
    }
}
