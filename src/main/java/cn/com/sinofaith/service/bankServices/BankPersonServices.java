package cn.com.sinofaith.service.bankServices;

import cn.com.sinofaith.bean.bankBean.BankPersonEntity;
import cn.com.sinofaith.dao.bankDao.BankPersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankPersonServices {
    @Autowired
    private BankPersonDao bpd;

    public int save(BankPersonEntity bpe){
        return bpd.insert(bpe);
    }


    public List<String> getByFilter(){
        List<String> result = new ArrayList<>();
        List<BankPersonEntity> listbp = bpd.find("from BankPersonEntity where xm like '%财付通%' or xm like '%支付宝%'");
        for (BankPersonEntity bp : listbp){
            result.add(bp.getYhkkh());
        }
        return  result;
    }
}
