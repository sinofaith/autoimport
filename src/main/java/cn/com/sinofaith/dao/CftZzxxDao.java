package cn.com.sinofaith.dao;

import cn.com.sinofaith.bean.CftZzxxEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Me. on 2018/5/22
 * 财付通转账信息dao
 */
@Repository
public class CftZzxxDao extends BaseDao<CftZzxxEntity>{
    public int getAllRowCount(String seachCode){
        String sql = "select to_char(Count(1)) num from cft_zzxx where 1=1" + seachCode;
        List list = findBySQL(sql);
        Map map = (Map)list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List<CftZzxxEntity> getDoPage(String seachCode,int offset,int length){
        List<CftZzxxEntity> result = doPage("from CftZzxxEntity  where 1=1" +seachCode +"order by id desc ",offset,length);
        return result;
    }


    public int saveZzxx(List<CftZzxxEntity> listZzxx){
        int i = 0;
        for (CftZzxxEntity zzxx:listZzxx){
            save(zzxx);
            i+=1;
        }
        return i;
    }

    public List<CftZzxxEntity> getAlla(){
        List<CftZzxxEntity> listZzxx = find("from CftZzxxEntity");
        return listZzxx;
    }
}