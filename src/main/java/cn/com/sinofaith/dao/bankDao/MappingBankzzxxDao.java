package cn.com.sinofaith.dao.bankDao;

import cn.com.sinofaith.bean.bankBean.MappingBankzzxxEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MappingBankzzxxDao extends BaseDao<MappingBankzzxxEntity> {
    public List<MappingBankzzxxEntity> getAll(){
        List<MappingBankzzxxEntity> listmb = find("from MappingBankzzxxEntity");
       return listmb;
    }

    public int save (List<MappingBankzzxxEntity> mbs){
        int sum = 0;
        String time = TimeFormatUtil.getDate("/");
        List<MappingBankzzxxEntity> all = getAll();
        for(MappingBankzzxxEntity mb : mbs){
            if(!all.contains(mb)){
                mb.setInserttime(time);
                save(mb);
                sum += 1;
            }
        }
        return sum;
    }
}
