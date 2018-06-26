package cn.com.sinofaith.dao;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.CftTjjgsEntity;
import cn.com.sinofaith.bean.CftZzxxEntity;
import cn.com.sinofaith.form.CftTjjgsForm;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Me. on 2018/5/23
 */
@Repository
public class CftTjjgsDao extends BaseDao<CftTjjgsEntity>{
    public int getAllRowCount(String seachCode){
        String sql = "select to_char(Count(1)) num from cft_tjjgs c left join " +
                " cft_person s on c.jyzh = s.zh where 1=1 "+seachCode;
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List getDoPage(String seachCode, int offset, int length){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM (SELECT  s.xm,c.* ");
        sql.append("FROM  cft_tjjgs c left join cft_person s on c.jyzh = s.zh where 1=1 "+seachCode+") a ");
        sql.append("WHERE ROWNUM <= "+offset*length+") WHERE rn >= "+((offset-1)*length+1));

        return findBySQL(sql.toString());
    }

//    public List<CftTjjgsEntity> getDoPage(String seachCode,int offset,int length){
//        List<CftTjjgsEntity> result = doPage("from CftTjjgsEntity si where 1=1 "+seachCode+" order by czzje desc,jzzje desc ",offset,length);
//        return result;
//    }

    public void delAll(long ajid){
        delete("delete from CftTjjgsEntity where aj_id="+ajid);
    }

    public void insert(CftTjjgsEntity tjs){
        saveOrUpdate(tjs);
    }

    public void save(List<CftTjjgsEntity> tjjgs){
        Connection con = DBUtil.getConn();
        String sql = "insert into cft_tjjgs(jyzh,dfzh,jyzcs,jzzcs,jzzje,czzcs,czzje,inserttime,aj_id) " +
                "values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement st ;
        CftTjjgsEntity tjjg = new CftTjjgsEntity();
        try {
            con.setAutoCommit(false);
            st = con.prepareStatement(sql);
            for (int i = 0; i < tjjgs.size(); i++) {
                tjjg = tjjgs.get(i);
                st.setString(1,tjjg.getJyzh());
                st.setString(2,tjjg.getDfzh());
                st.setBigDecimal(3,tjjg.getJyzcs());
                st.setBigDecimal(4,tjjg.getJzzcs());
                st.setBigDecimal(5,tjjg.getJzzje());
                st.setBigDecimal(6,tjjg.getCzzcs());
                st.setBigDecimal(7,tjjg.getCzzje());
                st.setString(8, TimeFormatUtil.getDate("/"));
                st.setLong(9,tjjg.getAj_id());
                st.addBatch();
                if((i+1)%1000 == 0){
                    st.executeBatch();
                    con.commit();
                }
            }
            st.executeBatch();
            con.commit();
            st.close();
        }catch (Exception e){
            e.getMessage();
        }
    }
}