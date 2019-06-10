package cn.com.sinofaith.service.bankServices;

import cn.com.sinofaith.bean.bankBean.MappingBankzzxxEntity;
import cn.com.sinofaith.dao.bankDao.MappingBankzzxxDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MappingBankzzxxService {
    @Autowired
    private MappingBankzzxxDao mbd;

    public List<MappingBankzzxxEntity> getAll(){
        List<MappingBankzzxxEntity> listmb = mbd.getAll();
        return  listmb;
    }
}
