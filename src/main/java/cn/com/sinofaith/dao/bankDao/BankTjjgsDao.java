package cn.com.sinofaith.dao.bankDao;

import cn.com.sinofaith.bean.bankBean.BankTjjgsEntity;
import cn.com.sinofaith.bean.cftBean.CftTjjgsEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

/**
 * Created by Me. on 2018/5/23
 */
@Repository
public class BankTjjgsDao extends BaseDao<BankTjjgsEntity> {
    public int getAllRowCount(String seachCode){
        StringBuffer sql = new StringBuffer("select to_char(Count(1)) num from bank_tjjgs c left join " +
                " bank_person s on c.jyzh = s.yhkkh where 1=1 ").append(seachCode);
        List list = findBySQL(sql.toString());
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List getDoPage(String seachCode, int offset, int length){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM (SELECT  s.khxm as xm,c.* ");
        sql.append("FROM  bank_tjjgs c left join bank_person s on c.jyzh = s.yhkkh where 1=1 "+seachCode+") a ");
        sql.append("WHERE ROWNUM <= "+offset*length+") WHERE rn >= "+((offset-1)*length+1));

        return findBySQL(sql.toString());
    }

    public int getGtCount(String seachCode,long ajid){
        StringBuffer sql= new StringBuffer("select count(1) NUM from bank_tjjgs c right join (" +
                "       select t.dfzh,count(1) as num from bank_tjjgs t " +
                "       where t.dfzh not in( select distinct t1.jyzh from bank_tjjgs t1) and t.aj_id=" +ajid+
                "       group by dfzh " +
                "       having(count(1)>=2) ) a on c.dfzh = a.dfzh" +
                "       left join bank_person s on c.jyzh = s.yhkkh" +
                "       where a.num is not null").append(seachCode);
        List list = findBySQL(sql.toString());
        Map map = (Map) list.get(0);
        return Integer.parseInt(String.valueOf(map.get("NUM")));
    }

    public List getDoPageGt(String seachCode,int offset,int length,long ajid){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM (select s.khxm as xm,n.khxm dfxm,c.*,a.num from bank_tjjgs c right join (" +
                "       select t.dfzh,count(1) as num from bank_tjjgs t " +
                "       where t.dfzh not in( select distinct t1.jyzh from bank_tjjgs t1) and t.aj_id=" +ajid+
                "       group by dfzh " +
                "       having(count(1)>=2) ) a on c.dfzh = a.dfzh " +
                "       left join bank_person s on c.jyzh = s.yhkkh " +
                "       left join bank_person n on c.dfzh = n.yhkkh " +
                "       where a.num is not null "+seachCode+") a ");
        sql.append("WHERE ROWNUM <= "+offset*length+") WHERE rn >= "+((offset-1)*length+1));

        return findBySQL(sql.toString());
    }

//    public List<CftTjjgsEntity> getDoPage(String seachCode,int offset,int length){
//        List<CftTjjgsEntity> result = doPage("from CftTjjgsEntity si where 1=1 "+seachCode+" order by czzje desc,jzzje desc ",offset,length);
//        return result;
//    }

    public void delAll(long ajid){
        delete("delete from BankTjjgsEntity where aj_id="+ajid);
    }

    public void insert(BankTjjgsEntity tjs){
        saveOrUpdate(tjs);
    }

    public void save(List<BankTjjgsEntity> tjjgs){
        Connection con = DBUtil.getConnection();
        String sql = "insert into bank_tjjgs(jyzh,dfzh,jyzcs,jzzcs,jzzje,czzcs,czzje,inserttime,aj_id) " +
                "values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement st ;
        BankTjjgsEntity tjjg = new BankTjjgsEntity();
        try {
//            con.setAutoCommit(false);
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
            e.printStackTrace();
        }
    }
}