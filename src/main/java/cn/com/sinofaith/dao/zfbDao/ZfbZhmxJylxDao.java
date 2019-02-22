package cn.com.sinofaith.dao.zfbDao;

import cn.com.sinofaith.bean.zfbBean.ZfbZhmxJczzEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZhmxJylxEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 支付宝账户与银行账户按交易类型进出总账统计
 * @author zd
 * create by 2019.02.20
 */
@Repository
public class ZfbZhmxJylxDao extends BaseDao<ZfbZhmxJylxEntity> {
    /**
     * 删除该案件的统计结果
     * @param id
     */
    public void delAll(long id) {
        delete("delete from ZfbZhmxJylxEntity where aj_id="+id);
    }

    /**
     * 插入数据
     * @param zhmxJylxList
     * @param id
     */
    public void insertZhmxJylx(List<ZfbZhmxJylxEntity> zhmxJylxList, long id) {
        // 使用原生sql
        Connection conn = DBUtil.getConnection();
        String sql = "insert into ZfbZhmx_Jylx(jyzfbzh,jymc,xfmc,jyzcs,czzcs,czzje,jzzcs,jzzje,insert_time,aj_id) " +
                "values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstm = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<zhmxJylxList.size();i++){
                ZfbZhmxJylxEntity jczz = zhmxJylxList.get(i);
                pstm.setString(1,jczz.getJyzfbzh());
                pstm.setString(2,jczz.getJymc());
                pstm.setString(3,jczz.getXfmc());
                pstm.setLong(4,jczz.getJyzcs());
                pstm.setLong(5,jczz.getCzzcs());
                pstm.setDouble(6,jczz.getCzzje());
                pstm.setLong(7,jczz.getJzzcs());
                pstm.setDouble(8,jczz.getJzzje());
                pstm.setString(9, TimeFormatUtil.getDate("/"));
                pstm.setLong(10,id);
                pstm.addBatch();
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
    }
}
