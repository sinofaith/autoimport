package cn.com.sinofaith.dao.bankDao;

import cn.com.sinofaith.bean.bankBean.BankCustomerEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.DBUtil;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Repository
public class BankCustomerDao  extends BaseDao<BankCustomerEntity> {

    public int getAllRowCount(String seachCode) {
        String sql = "select to_char(count(c.zjhm)) num from bank_customer c " +
                " right join rel_customer_aj s on c.zjhm = s.zjhm   where 1=1 " + seachCode;
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        return Integer.parseInt((String) map.get("NUM"));
    }

    public List getDoPage(String seach, int offset, int length) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM (SELECT  c.* ");
        sql.append("FROM  bank_customer c left join rel_customer_aj s on c.zjhm = s.zjhm ");
        sql.append(" where 1=1 " + seach + ") a ");
        sql.append("WHERE ROWNUM <= " + offset * length + ") WHERE rn >= " + ((offset - 1) * length + 1));
        return findBySQL(sql.toString());
    }

    public int saveRel(List<String> zjhm, long aj_id){
        Connection con = DBUtil.getConnection();
        String sql = "insert into rel_customer_aj(zjhm,aj_id) values(?,?)";
        int a = 0;
        PreparedStatement pstm = null;
        try {
//            con.setAutoCommit(false);
            pstm = con.prepareStatement(sql);
            for (int j = 0; j < zjhm.size(); j++) {
                try {
                    pstm.setString(1, zjhm.get(j));
                    pstm.setLong(2, aj_id);
                    pstm.execute();
//                    con.commit();
                    a++;
                }catch (Exception e){
                    System.out.println("该案件已存在此人");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
//            DBUtil.closeStatement(pstm);
//            DBUtil.closeConnection(con);
        }finally {
//            DBUtil.closeStatement(pstm);
//            DBUtil.closeConnection(con);
        }
        return a;
    }
}
