package cn.com.sinofaith.dao;

import cn.com.sinofaith.bean.CftTjjgsEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Me. on 2018/5/23
 */
@Repository
public class CftTjjgsDao extends BaseDao<CftTjjgsEntity>{
    public int getAllRowCount(String seachCode){
        String sql = "select to_char(Count(1)) num from cft_tjjgs where 1=1 "+seachCode;
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List<CftTjjgsEntity> getDoPage(String seachCode,int offset,int length){
        List<CftTjjgsEntity> result = doPage("from CftTjjgsEntity si where 1=1 "+seachCode+" order by jyzcs desc",offset,length);
        return result;
    }

    public void insert(CftTjjgsEntity tjs){
        saveOrUpdate(tjs);
    }
}