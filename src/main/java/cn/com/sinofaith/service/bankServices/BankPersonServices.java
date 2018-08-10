package cn.com.sinofaith.service.bankServices;

import cn.com.sinofaith.bean.bankBean.BankPersonEntity;
import cn.com.sinofaith.dao.bankDao.BankPersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankPersonServices {
    @Autowired
    private BankPersonDao bpd;

    public int save(BankPersonEntity bpe){
        return bpd.insert(bpe);
    }
}
