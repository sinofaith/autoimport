package cn.com.sinofaith.dao.bankDao;

import cn.com.sinofaith.bean.bankBean.BankZcxxEntity;
import cn.com.sinofaith.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BankZcxxDao extends BaseDao<BankZcxxEntity>{
    public int getAllRowCount(String seachCode){
        String sql = "select to_char(Count(1)) num from bank_zcxx zc where 1=1 " + seachCode;
        List list = findBySQL(sql);
        Map map =(Map)list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List<BankZcxxEntity> getDoPage(String seachCode, int offset, int length){
        List<BankZcxxEntity> result = doPage("from BankZcxxEntity  where 1=1" +seachCode +"order by id desc,khxm ",offset,length);
        return result;
    }
}
