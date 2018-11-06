package cn.com.sinofaith.dao.wuliuDao;

import cn.com.sinofaith.bean.wlBean.WuliuRelationEntity;
import cn.com.sinofaith.bean.wlBean.WuliuShipEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.DBUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 寄件人持久层
 */
@Repository
public class WuliuShipDao extends BaseDao<WuliuShipEntity> {

    /**
     * 根据案件id获取wuliu_ship表总条数
     * @param id
     * @return
     */
    public int getRowCount(long id) {
        String sql = "select count(*) num from wuliu_ship where aj_id="+id;
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        // 转成String
        BigDecimal num = (BigDecimal) map.get("NUM");
        return Integer.parseInt(num.toString());
    }

    /**
     * 根据案件id删除表中记录
     * @param id
     */
    public void deleteWuliuShip(long id) {
        // 获得session
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            String hql = "Delete From WuliuShipEntity Where aj_id=?";
            Query query = session.createQuery(hql);
            query.setLong(0,id);
            query.executeUpdate();
            tx.commit();
        }catch (Exception e){
            session.close();
            e.printStackTrace();
        }
    }

    /**
     * 获取寄件人表中所需要的数据
     * @param id
     * @return
     */
    public List<WuliuShipEntity> getWuliuShip(long id) {
        List<WuliuShipEntity> wls = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select max(sender) sender,ship_phone,max(ship_address) ship_address,count(*) num from (select * from (select t.*,row_number() ");
        sql.append(" over(partition by t.waybill_id,substr(trim(t.ship_time),1,19), ");
        sql.append(" t.ship_address,t.sender,t.ship_phone,t.ship_mobilephone, ");
        sql.append(" t.sj_address,t.addressee,t.sj_phone,t.sj_mobilephone, ");
        sql.append(" t.tjw,t.dshk,t.number_cases order by t.id) su from wuliu t where aj_id="+id+") where su=1 ");
        sql.append(" )group by ship_phone order by num desc");
        // 获得当前session
        Session session = getSession();
        // 开启事务
        try{
            Transaction tx = session.beginTransaction();
            wls = session.createSQLQuery(sql.toString())
                    .addScalar("sender")
                    .addScalar("ship_phone")
                    .addScalar("ship_address")
                    .addScalar("num", StandardBasicTypes.LONG)
                    .setResultTransformer(Transformers.aliasToBean(WuliuShipEntity.class)).list();
            tx.commit();
        }catch (Exception e){
            session.close();
            e.printStackTrace();
        }
        if(wls!=null){
            for (int i = 0; i < wls.size(); i++) {
                wls.get(i).setAj_id(id);
            }
        }
        return wls;
    }

    /**
     * 插入数据
     * @param wls
     */
    public void insertWuliuShip(List<WuliuShipEntity> wls) {
        // 获取连接
        Connection conn = DBUtil.getConnection();
        String sql = "insert into wuliu_ship(sender,ship_phone,ship_address,num,aj_id) values(?,?,?,?,?)";
        PreparedStatement pstm = null;
        WuliuShipEntity wl = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<wls.size();i++){
                wl = wls.get(i);
                pstm.setString(1,wl.getSender());
                pstm.setString(2,wl.getShip_phone());
                pstm.setString(3,wl.getShip_address());
                pstm.setLong(4,wl.getNum());
                pstm.setLong(5,wl.getAj_id());
                pstm.addBatch();
                // 有10000条添加一次
                if ((i + 1) % 10000 == 0) {
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
     * 分页条数
     * @return
     */
    public int getRowAll(DetachedCriteria dc) {
        Long rowAll = 0l;
        // 获取当前session
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            Criteria criteria = dc.getExecutableCriteria(session);
            rowAll = (long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            // 将条件清空，用于dc查询分页数据
            criteria.setProjection(null);
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return rowAll.intValue();
    }

    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public List<WuliuShipEntity> getDoPage(int currentPage, int pageSize, DetachedCriteria dc) {
        List<WuliuShipEntity> wls = null;
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            Criteria criteria = dc.getExecutableCriteria(session);
            criteria.setFirstResult((currentPage-1)*pageSize);
            criteria.setMaxResults(pageSize);
            wls = criteria.list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return wls;
    }

    /**
     * 详情数据条数
     * @param seach
     * @return
     */
    public int getCountRow(String seach) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) num from (select t.*,row_number() ");
        sql.append(" over(partition by t.waybill_id,substr(trim(t.ship_time),1,19), ");
        sql.append(" t.ship_address,t.sender,t.ship_phone,t.ship_mobilephone, ");
        sql.append(" t.sj_address,t.addressee,t.sj_phone,t.sj_mobilephone, ");
        sql.append(" t.tjw,t.dshk,t.number_cases order by t.id) su from wuliu t where 1=1 "+seach+" ) where su=1");
        List list = findBySQL(sql.toString());
        Map map = (Map) list.get(0);
        // 转成String
        BigDecimal num = (BigDecimal) map.get("NUM");
        return Integer.parseInt(num.toString());
    }

    /**
     * 数据导出
     * @param dc
     */
    public List<WuliuShipEntity> getWuliuShipAll(DetachedCriteria dc) {
        List<WuliuShipEntity> wls = null;
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            Criteria criteria = dc.getExecutableCriteria(session);
            wls = criteria.list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return wls;
    }
}
