package cn.com.sinofaith.dao.zfbDao;

import cn.com.sinofaith.bean.zfbBean.ZfbZcxxEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/***
 * 支付宝注册信息持久层
 * @author zd
 * create by 2018.11.28
 */
@Repository
public class ZfbZcxxDao extends BaseDao<ZfbZcxxEntity> {

    /**
     * 批量插入数据
     * @param zcxxList
     * @param id
     * @return
     */
    public int insertZcxx(List<ZfbZcxxEntity> zcxxList, long id) {
        // 使用原生sql
        Connection conn = DBUtil.getConnection();
        String sql = "insert into ZFBZCXX(yhid,dlyx,dlsj,zhmc,zjlx,zjh,kyye,bdsj,bdyhk,dyxcsj,insert_time,aj_id) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?)";
        int a = 0;
        PreparedStatement pstm = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<zcxxList.size();i++){
                ZfbZcxxEntity zcxx = zcxxList.get(i);
                pstm.setString(1,zcxx.getYhId());
                pstm.setString(2,zcxx.getDlyx());
                pstm.setString(3,zcxx.getDlsj());
                pstm.setString(4,zcxx.getZhmc());
                pstm.setString(5,zcxx.getZjlx());
                pstm.setString(6,zcxx.getZjh());
                pstm.setDouble(7,zcxx.getKyye());
                pstm.setString(8,zcxx.getBdsj());
                pstm.setString(9,zcxx.getBdyhk());
                pstm.setString(10,zcxx.getDyxcsj());
                pstm.setString(11, TimeFormatUtil.getDate("/"));
                pstm.setLong(12,id);
                pstm.addBatch();
                a++;
                // 有50000条添加一次
                if ((i + 1) % 50000 == 0) {
                    pstm.executeBatch();
                    pstm.clearParameters();
                    conn.commit();
                }
            }
            pstm.executeBatch();
            conn.commit();
            DBUtil.closeStatement(pstm);
            DBUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }
}
