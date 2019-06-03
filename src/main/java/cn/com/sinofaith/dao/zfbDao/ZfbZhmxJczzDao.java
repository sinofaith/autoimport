package cn.com.sinofaith.dao.zfbDao;

import cn.com.sinofaith.bean.zfbBean.ZfbZhmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZhmxJczzEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 支付宝账户明细进出总账统计持久层
 * @author zd
 * create by 2019.02.19
 */
@Repository
public class ZfbZhmxJczzDao extends BaseDao<ZfbZhmxJczzEntity>{
    /**
     * 删除该案件的统计结果
     * @param id
     */
    public void delAll(long id) {
        delete("delete from ZfbZhmxJczzEntity where aj_id="+id);
    }

    /**
     * 插入数据
     * @param zhmxJczzList
     * @param id
     */
    public void insertZhmxJczz(List<ZfbZhmxJczzEntity> zhmxJczzList, long id) {
        // 使用原生sql
        Connection conn = DBUtil.getConnection();
        String sql = "insert into ZfbZhmx_Jczz(jyzfbzh,jymc,jyzcs,czzcs,czzje,jzzcs,jzzje,insert_time,aj_id) " +
                "values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstm = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<zhmxJczzList.size();i++){
                ZfbZhmxJczzEntity jczz = zhmxJczzList.get(i);
                pstm.setString(1,jczz.getJyzfbzh());
                pstm.setString(2,jczz.getJymc());
                pstm.setLong(3,jczz.getJyzcs());
                pstm.setLong(4,jczz.getCzzcs());
                pstm.setBigDecimal(5,jczz.getCzzje());
                pstm.setLong(6,jczz.getJzzcs());
                pstm.setBigDecimal(7,jczz.getJzzje());
                pstm.setString(8, TimeFormatUtil.getDate("/"));
                pstm.setLong(9,id);
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

    /**
     * 详情页数据总条数
     * @param search
     * @param id
     * @return
     */
    public int getRowAllCount(String search, long id) {
        StringBuffer sql = new StringBuffer();
        sql.append("select to_char(count(1)) NUM from (select t.*,row_number() over(partition by t.jyh");
        sql.append(" order by t.id) su from zfbzhmx t where aj_id="+id+" and "+search+") where su=1");
        List list = findBySQL(sql.toString());
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    /**
     * 详情页分页数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param aj_id
     * @param flag
     * @return
     */
    public List<ZfbZhmxEntity> getDoPageJczz(int currentPage, int pageSize, String search, long aj_id, boolean flag) {
        List<ZfbZhmxEntity> zhmxList = null;
        StringBuffer sql = new StringBuffer();
        if(flag){
            sql.append("SELECT * FROM ( ");
            sql.append("SELECT c.*, ROWNUM rn FROM (");
        }
        sql.append("select * from (select t.*,row_number() over(partition by t.jyh");
        sql.append(" order by t.id) su from zfbzhmx t where aj_id="+aj_id+" and "+search+") where su=1");
        if(flag){
            sql.append(") c WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        }
        // 获取当前线程session
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            zhmxList = session.createSQLQuery(sql.toString())
                    .addEntity(ZfbZhmxEntity.class).list();
            tx.commit();
        }catch (Exception e){
            session.close();
            e.printStackTrace();
        }
        return zhmxList;
    }
}
