package cn.com.sinofaith.dao;

import cn.com.sinofaith.bean.CftTjjgsEntity;
import cn.com.sinofaith.form.CftTjjgsForm;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Me. on 2018/5/23
 */
@Repository
public class CftTjjgsDao extends BaseDao<CftTjjgsEntity>{
    public int getAllRowCount(String seachCode){
        String sql = "select to_char(Count(1)) num from cft_tjjgs c where 1=1 "+seachCode;
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List getDoPage(String seachCode, int offset, int length){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM (SELECT  s.xm,c.* ");
        sql.append("FROM  cft_tjjgs c , cft_person s  where 1=1 and c.jyzh = s.zh "+seachCode+") a ");
        sql.append("WHERE ROWNUM <= "+offset*length+") WHERE rn >= "+((offset-1)*length+1));

        return findBySQL(sql.toString());
    }

//    public List<CftTjjgsEntity> getDoPage(String seachCode,int offset,int length){
//        List<CftTjjgsEntity> result = doPage("from CftTjjgsEntity si where 1=1 "+seachCode+" order by czzje desc,jzzje desc ",offset,length);
//        return result;
//    }

    public void insert(CftTjjgsEntity tjs){
        saveOrUpdate(tjs);
    }
}