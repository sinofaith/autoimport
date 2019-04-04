package cn.com.sinofaith.dao.pyramidSaleDao;

import cn.com.sinofaith.bean.pyramidSaleBean.PsHierarchyEntity;
import cn.com.sinofaith.bean.pyramidSaleBean.PyramidSaleEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.form.psForm.PsHierarchyForm;
import cn.com.sinofaith.form.psForm.PsPoltForm;
import cn.com.sinofaith.util.DBUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 路径表持久层
 * @author zd
 * create by 2018.11.1
 */
@Repository
public class PsHierarchyDao extends BaseDao<PsHierarchyEntity>{
    /**
     * 批量插入数据
     * @param psList
     * @param aj_id
     * @return
     */
    public int insertPsHierarchy(List<PsPoltForm> psList, long aj_id) {
        // 使用原生sql path,?,
        Connection conn = DBUtil.getConnection();
        String sql = "insert into PS_HIERARCHY(psid,sponsorid,nick_name,tier,directrefernum,containstier,directdrive,aj_id)" +
                "values(?,?,?,?,?,?,?,?)";
        int a = 0;
        PreparedStatement pstm = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<psList.size();i++){
                PsPoltForm ps = psList.get(i);
                pstm.setString(1,ps.getPsid());
                pstm.setString(2,ps.getSponsorid());
                pstm.setString(3,ps.getName());
                pstm.setLong(4,ps.getTier());// 层级
                // 转成流形式
//                Reader reader = new StringReader(ps.getPath());
//                pstm.setCharacterStream(5,reader,ps.getPath().length());// 路径
                pstm.setLong(5,ps.getContainNum());// 下线会员数
                pstm.setLong(6,ps.getContain());// 包含层级
                pstm.setLong(7,ps.getLineal());// 直推下线数
                pstm.setLong(8,aj_id);
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
