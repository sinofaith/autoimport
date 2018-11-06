package cn.com.sinofaith.dao.wuliuDao;

import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.bean.wlBean.WuliuShipEntity;
import cn.com.sinofaith.bean.wlBean.WuliuSjEntity;
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

@Repository
public class WuliuSjDao extends BaseDao<WuliuSjEntity>{

    /**
     * 插入前查询该案件是否有数据
     * @param id
     * @return
     */
    public int getRowAll(long id) {
        String sql = "select to_char(count(1)) num from wuliu_sj where aj_id="+id;
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    /**
     * 根据案件id删除数据
     * @param id
     */
    public void deleteWuliuSj(long id) {
        // 获得session
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            String hql = "Delete From WuliuSjEntity Where aj_id=?";
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
     * 获取寄件人表所需要的数据
     * @param id
     * @return
     */
    public List<WuliuSjEntity> getWuliuSj(long id) {
        List<WuliuSjEntity> wls = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select max(addressee) addressee,sj_phone,max(sj_address) sj_address,count(*) num from (select * from (select t.*,row_number() ");
        sql.append(" over(partition by t.waybill_id,substr(trim(t.ship_time),1,19), ");
        sql.append(" t.ship_address,t.sender,t.ship_phone,t.ship_mobilephone, ");
        sql.append(" t.sj_address,t.addressee,t.sj_phone,t.sj_mobilephone, ");
        sql.append(" t.tjw,t.dshk,t.number_cases order by t.id) su from wuliu t where aj_id="+id+") where su=1 ");
        sql.append(" )group by sj_phone order by num desc ");
        // 获取当前线程绑定的session
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            wls = session.createSQLQuery(sql.toString())
                    .addScalar("addressee")
                    .addScalar("sj_phone")
                    .addScalar("sj_address")
                    .addScalar("num", StandardBasicTypes.LONG)
                    .setResultTransformer(Transformers.aliasToBean(WuliuSjEntity.class)).list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
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
    public void insertWuliuSj(List<WuliuSjEntity> wls) {
        // 获取连接
        Connection conn = DBUtil.getConnection();
        String sql = "insert into wuliu_sj(addressee,sj_phone,sj_address,num,aj_id) values(?,?,?,?,?)";
        PreparedStatement pstm = null;
        WuliuSjEntity wl = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<wls.size();i++){
                wl = wls.get(i);
                pstm.setString(1,wl.getAddressee());
                pstm.setString(2,wl.getSj_phone());
                pstm.setString(3,wl.getSj_address());
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
     * 分页总条数
     * @param dc
     * @return
     */
    public int getRowCount(DetachedCriteria dc) {
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
    public List<WuliuSjEntity> getDoPage(int currentPage, int pageSize, DetachedCriteria dc) {
        List<WuliuSjEntity> wls = null;
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
     * @return
     */
    public List<WuliuSjEntity> getWuliuSjAll(DetachedCriteria dc) {
        List<WuliuSjEntity> wls = null;
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
