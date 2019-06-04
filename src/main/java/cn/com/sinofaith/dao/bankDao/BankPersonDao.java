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
//        boolean temp = bpe.getXm().contains("财付通")||bpe.getXm().contains("支付")||bpe.getXm().contains("清算")
//                || bpe.getXm().contains("特约") || bpe.getXm().contains("备付金")|| bpe.getXm().contains("银行")
//                || bpe.getXm().contains("银联") || bpe.getXm().contains("保险") || bpe.getXm().contains("过渡")
//                || bpe.getXm().contains("美团");
//        if(temp){
//            bpe.setDsfzh(1);
//        }
        boolean temp = bpe.getXm().contains("财付通") || bpe.getXm().contains("支付") || bpe.getXm().contains("清算")
                || bpe.getXm().contains("特约") || bpe.getXm().contains("备付金") || bpe.getXm().contains("银行")
                || bpe.getXm().contains("银联") || bpe.getXm().contains("保险") || bpe.getXm().contains("过渡")
                || bpe.getXm().contains("美团");
        if (temp) {
            bpe.setDsfzh(1);
        }
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
                if(bpe.getYhkkh()==null || bpe.getXm()==null ||bpe.getYhkkh().length()<1||bpe.getXm().length()<1){
                    continue;
                }
                if (bpe.getXm().contains("支付宝")) {
                    bpe.setXm( "支付宝（中国）网络技术有限公司");
                } else if (bpe.getXm().contains("微信") || bpe.getXm().contains("财付通")) {
                    bpe.setXm("财付通支付科技有限公司");
                }
                boolean temp = bpe.getXm().contains("财付通") || bpe.getXm().contains("支付") || bpe.getXm().contains("清算")
                        || bpe.getXm().contains("特约") || bpe.getXm().contains("备付金") || bpe.getXm().contains("银行")
                        || bpe.getXm().contains("银联") || bpe.getXm().contains("保险") || bpe.getXm().contains("过渡")
                        || bpe.getXm().contains("美团");
                if (temp) {
                    bpe.setDsfzh(1);
                }
                pstm.setString(1, bpe.remove_(bpe.getYhkkh()));
                pstm.setString(2, aj_id);
                pstm.setString(3, bpe.getXm());
                pstm.setLong(4,bpe.getDsfzh());
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
            e.printStackTrace();
            DBUtil.closeStatement(pstm);
            DBUtil.closeConnection(con);
        }
        listbp = null;
        return a;
    }
}
