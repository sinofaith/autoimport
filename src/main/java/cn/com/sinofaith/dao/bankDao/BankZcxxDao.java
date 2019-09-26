package cn.com.sinofaith.dao.bankDao;

import cn.com.sinofaith.bean.bankBean.BankZcxxEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class BankZcxxDao extends BaseDao<BankZcxxEntity>{
    public int getAllRowCount(String seachCode){
        String sql = "select to_char(Count(1)) num from bank_zcxx zc where 1=1 " + seachCode;
        List list = findBySQL(sql);
        Map map =(Map)list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List<BankZcxxEntity> getDoPage(String seachCode, int offset, int length){
        List<BankZcxxEntity> result = doPage("from BankZcxxEntity  where 1=1" +seachCode,offset,length);
        return result;
    }

    public List<BankZcxxEntity> getByAjId(String seachCode){
        List<BankZcxxEntity> result = find("from BankZcxxEntity where 1=1 "+seachCode);
        return result;
    }

    public int saveZcxx(List<BankZcxxEntity> listZcxx,long aj_id){
        int i = 0;
        List<BankZcxxEntity> all = find("from BankZcxxEntity where aj_id = "+aj_id);

        listZcxx = listZcxx.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
        for(BankZcxxEntity zc:listZcxx){
            if(!all.contains(zc)) {
                zc.setAj_id(aj_id);
                if (zc.getKhzjh().contains("@")) {
                    zc.setKhzjh("");
                }
                zc.setInserttime(TimeFormatUtil.getDate("/"));
                save(zc);
                i++;
            }
        }
        return i;
    }
}
