package cn.com.sinofaith.dao.bankDao;

import cn.com.sinofaith.bean.bankBean.BankTjjgEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Repository
public class BankTjjgDao extends BaseDao<BankTjjgEntity>{
    public int getAllRowCount(String seachCode){
        String sql = "select to_char(Count(1)) num from bank_tjjg c  left join" +
                " bank_person s on c.jyzh = s.yhkkh where 1=1 "+seachCode;
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List getDoPage (String seachCode,int offset,int length){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM (SELECT  s.khxm as xm,c.* ");
        sql.append("FROM  bank_tjjg c left join bank_person s on c.jyzh = s.yhkkh where 1=1 "+seachCode+") a ");
        sql.append("WHERE ROWNUM <= "+offset*length+") WHERE rn >= "+((offset-1)*length+1));

        return findBySQL(sql.toString());
    }

//    public List<CftTjjgEntity> getDoPage(String seachCode,int offset,int length){
//        List<CftTjjgEntity> result = doPage("from CftTjjgEntity si where 1=1 "+seachCode+" order by jyzh desc,czzje desc,jzzje desc",offset,length);
//        return result;
//    }

    public void delAll(long ajid){
        delete("delete from BankTjjgEntity where aj_id="+ajid);
    }

    public int insert(BankTjjgEntity cft){
        saveOrUpdate(cft);
        return 0;
    }

    public void save(List<BankTjjgEntity> tjjgs){
        Connection con = DBUtil.getConnection();
        String sql = "insert into bank_tjjg(jyzh,jyzcs,jzzcs,jzzje,czzcs,czzje,inserttime,aj_id,zhlx,zhlb) " +
                "values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement st ;
        BankTjjgEntity tjjg = new BankTjjgEntity();
        try {
            con.setAutoCommit(false);
            st = con.prepareStatement(sql);
            for (int i = 0; i < tjjgs.size(); i++) {
                tjjg = tjjgs.get(i);
                if (tjjg.getJyzh()==null){
                    continue;
                }
//                tjjg.setInserttime(TimeFormatUtil.getDate("/"));
//                save(tjjg);
                st.setString(1,tjjg.getJyzh());
                st.setBigDecimal(2,tjjg.getJyzcs());
                st.setBigDecimal(3,tjjg.getJzzcs());
                st.setBigDecimal(4,tjjg.getJzzje());
                st.setBigDecimal(5,tjjg.getCzzcs());
                st.setBigDecimal(6,tjjg.getCzzje());
                st.setString(7, TimeFormatUtil.getDate("/"));
                st.setLong(8,tjjg.getAj_id());
                st.setLong(9,tjjg.getZhlx());
                st.setString(10,tjjg.getZhlb());
                st.addBatch();
                if((i+1)%50000 == 0){
                    st.executeBatch();
                    con.commit();
                }
            }
            st.executeBatch();
            con.commit();
            DBUtil.closeStatement(st);
            DBUtil.closeConnection(con);
        }catch (Exception e){
            e.getMessage();
        }
    }
}
