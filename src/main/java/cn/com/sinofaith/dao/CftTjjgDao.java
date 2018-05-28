package cn.com.sinofaith.dao;

import cn.com.sinofaith.bean.CftTjjgEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Me. on 2018/5/23
 * 财付通统计结果1
 */
@Repository
public class CftTjjgDao extends BaseDao<CftTjjgEntity>{
    public int getAllRowCount(String seachCode){
        String sql = "select to_char(Count(1)) num from cft_tjjg where 1=1 "+seachCode;
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List<CftTjjgEntity> getDoPage(String seachCode,int offset,int length){
        List<CftTjjgEntity> result = doPage("from CftTjjgEntity si where 1=1 "+seachCode+" order by jyzh desc,czzje desc,jzzje desc",offset,length);
        return result;
    }

    public int insert(CftTjjgEntity cft){
       saveOrUpdate(cft);
        return 0;
    }
}