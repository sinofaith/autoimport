package cn.com.sinofaith.dao.zfbDao;

import cn.com.sinofaith.bean.zfbBean.ZfbZhmxEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/***
 * 支付宝账户明细持久层
 * @author zd
 * create by 2018.11.28
 */
@Repository
public class ZfbZhmxDao extends BaseDao<ZfbZhmxEntity> {
    /**
     * 批量插入数据
     * @param zhmxList
     * @param id
     * @return
     */
    public int insertZhmx(List<ZfbZhmxEntity> zhmxList, long id) {
        // 使用原生sql
        Connection conn = DBUtil.getConnection();
        String sql = "insert into ZfbZhmx(jyh,shddh,jycjsj,fksj,zjxgsj,jylyd,lx,yhxx,jydfxx,xfmc,je,sz,jyzt,bz,dyxcsj,insert_time,aj_id) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int a = 0;
        PreparedStatement pstm = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<zhmxList.size();i++){
                ZfbZhmxEntity Zhmx = zhmxList.get(i);
                pstm.setString(1,Zhmx.getJyh());
                pstm.setString(2,Zhmx.getShddh());
                pstm.setString(3,Zhmx.getJycjsj());
                pstm.setString(4,Zhmx.getFksj());
                pstm.setString(5,Zhmx.getZjxgsj());
                pstm.setString(6,Zhmx.getJylyd());
                pstm.setString(7,Zhmx.getLx());
                pstm.setString(8,Zhmx.getYhxx());
                pstm.setString(9,Zhmx.getJydfxx());
                pstm.setString(10,Zhmx.getXfmc());
                pstm.setDouble(11,Zhmx.getJe());
                pstm.setString(12,Zhmx.getSz());
                pstm.setString(13,Zhmx.getJyzt());
                pstm.setString(14,Zhmx.getBz());
                pstm.setString(15,Zhmx.getDyxcsj());
                pstm.setString(16, TimeFormatUtil.getDate("/"));
                pstm.setLong(17,id);
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
