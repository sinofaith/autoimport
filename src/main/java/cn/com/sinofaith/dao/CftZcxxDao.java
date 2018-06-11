package cn.com.sinofaith.dao;

import cn.com.sinofaith.bean.CftZcxxEntity;
import cn.com.sinofaith.bean.CftZzxxEntity;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Me. on 2018/5/21
 * 财付通注册信息 dao
 */
@Repository
public class CftZcxxDao extends BaseDao<CftZcxxEntity>{

    public int getAllRowCount(String seachCode){
        String sql = "select to_char(Count(1)) num from cft_zcxx zc where 1=1 " + seachCode;
        List list = findBySQL(sql);
        Map map =(Map)list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List<CftZcxxEntity> getDoPage(String seachCode,int offset,int length){
        List<CftZcxxEntity> result = doPage("from CftZcxxEntity  where 1=1" +seachCode +"order by id desc ",offset,length);
        return result;
    }

    public int saveZcxx(List<CftZcxxEntity> listZcxx){
        int i = 0;
        for (CftZcxxEntity zcxx:listZcxx){
            zcxx.setInserttime(TimeFormatUtil.getDate("/"));
            save(zcxx);
            i+=1;
        }
        return i;
    }


}