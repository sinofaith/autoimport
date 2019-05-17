package cn.com.sinofaith.dao.bankDao;

import cn.com.sinofaith.bean.bankBean.BankPersonEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.apache.batik.css.engine.value.StringValue;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class BankPersonDao extends BaseDao<BankPersonEntity>{
    public int insert(BankPersonEntity bpe){
        saveOrUpdate(bpe);
        return 1;
    }

    public int add(List<BankPersonEntity> listbp,String aj_id){
        Connection con = DBUtil.getConnection();
        String sql = "insert into Bank_Person(yhkkh,yhkzh,khxm,dsfzh) values(?,?,?,?)";
        int a = 0;
        PreparedStatement pstm = null;
        BankPersonEntity bpe = new BankPersonEntity();
        try {
            con.setAutoCommit(false);
            pstm = con.prepareStatement(sql);
            for (int j = 0; j < listbp.size(); j++) {

                bpe = listbp.get(j);
                pstm.setString(1, bpe.getYhkkh().replace("_156_1","").trim());
                pstm.setString(2, aj_id);
                pstm.setString(3, bpe.getXm());
                pstm.addBatch();
                a++;
                if ((j + 1) % 50000 == 0) {
                    pstm.executeBatch();
                    pstm.clearParameters();
                    con.commit();
                }
            }
            pstm.executeBatch();
            con.commit();
            DBUtil.closeStatement(pstm);
            DBUtil.closeConnection(con);
        } catch (Exception e) {
            e.getMessage();
            DBUtil.closeStatement(pstm);
            DBUtil.closeConnection(con);
        }
        listbp = null;
        return a;
    }
}
