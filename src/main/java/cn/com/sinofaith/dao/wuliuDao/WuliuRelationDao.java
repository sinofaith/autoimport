package cn.com.sinofaith.dao.wuliuDao;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.bean.wlBean.WuliuRelationEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.form.wlForm.WuliuRelationForm;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
public class WuliuRelationDao extends BaseDao<WuliuRelationEntity> {

    /**
     * WuliuRelation表数据插入
     * @param wls
     */
    public void insertWuliuRelation(List<WuliuRelationEntity> wls) {
        // 获得连接
        Connection conn = DBUtil.getConnection();
        String sql = "insert into wuliu_relation(sender,ship_phone,ship_address,addressee,sj_phone,sj_address,num,aj_id) values(?,?,?,?,?,?,?,?)";
        PreparedStatement pstm = null;
        WuliuRelationEntity wlr = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<wls.size();i++){
                wlr = wls.get(i);
                pstm.setString(1,wlr.getSender());
                pstm.setString(2,wlr.getShip_phone());
                pstm.setString(3,wlr.getShip_address());
                pstm.setString(4,wlr.getAddressee());
                pstm.setString(5,wlr.getSj_phone());
                pstm.setString(6,wlr.getSj_address());
                pstm.setLong(7,wlr.getNum());
                pstm.setLong(8,wlr.getAj_id());
                pstm.addBatch();
                // 有5000条添加一次
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
     * 获取(条件)总数据
     * @param seach
     * @return
     */
    public int getAllRowCount(String seach) {
        String sql = "select count(*) num from wuliu_relation where (1=1) "+seach;
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        // 转成String
        BigDecimal num = (BigDecimal) map.get("NUM");
        return Integer.parseInt(num.toString());
    }

    /**
     * 获取分页数据
     * @param currentPage
     * @param pageSize
     * @return
     */
    public List<WuliuRelationForm> getDoPage(String seach, int currentPage, int pageSize,long aj_id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * FROM ( ");
        sql.append(" SELECT c.*, ROWNUM rn FROM ( ");
        sql.append(" select * from wuliu_relation where aj_id="+aj_id+seach);
        sql.append(") c ");
        sql.append(" WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        // 获得当前线程session
        Session session = getSession();
        List<WuliuRelationForm> wlrs = null;
        try{
            // 开启事务
            Transaction transaction = session.beginTransaction();
            wlrs = session.createSQLQuery(sql.toString())
                    .addScalar("id", StandardBasicTypes.LONG)
                    .addScalar("ship_address")
                    .addScalar("sender")
                    .addScalar("ship_phone")
                    .addScalar("sj_address")
                    .addScalar("addressee")
                    .addScalar("sj_phone")
                    .addScalar("num",StandardBasicTypes.LONG)
                    .addScalar("aj_id",StandardBasicTypes.LONG)
                    .setResultTransformer(Transformers.aliasToBean(WuliuRelationForm.class)).list();
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return wlrs;
    }

    /**
     * 物流关系详情去重总条数
     * @param seach
     * @return
     */
    public int getRowAll(String seach) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) num from (select t.*,row_number() ");
        sql.append(" over(partition by t.waybill_id,substr(trim(t.ship_time),1,19),");
        sql.append(" t.ship_address,t.sender,t.ship_phone,t.ship_mobilephone,");
        sql.append(" t.sj_address,t.addressee,t.sj_phone,t.sj_mobilephone,");
        sql.append(" t.tjw,t.dshk,t.number_cases order by t.id) su from wuliu t where 1=1 "+seach+") where su=1");
        List list = findBySQL(sql.toString());
        Map map = (Map) list.get(0);
        // 转成String
        BigDecimal num = (BigDecimal) map.get("NUM");
        /*Session session = getSession();
        Long rowAll = 0l;
        try {
            Transaction tx = session.beginTransaction();
            // 将离线查询对象与session绑定
            Criteria criteria = dc.getExecutableCriteria(session);
            // 去重数据处理
            ProjectionList proList = Projections.projectionList();
            proList.add(Projections.property("waybill_id"));
            proList.add(Projections.property("ship_time"));
            proList.add(Projections.property("ship_address"));
            proList.add(Projections.property("sender"));
            proList.add(Projections.property("ship_phone"));
            proList.add(Projections.property("ship_mobilephone"));
            proList.add(Projections.property("sj_address"));
            proList.add(Projections.property("addressee"));
            proList.add(Projections.property("sj_phone"));
            proList.add(Projections.property("sj_mobilephone"));
            proList.add(Projections.property("tjw"));
            proList.add(Projections.property("dshk"));
            proList.add(Projections.property("number_cases"));
            proList.add(Projections.property("payment"));
            proList.add(Projections.property("freight"));
            rowAll = Long.valueOf(criteria.setProjection(Projections.distinct(proList)).list().size());
            //rowAll = (Long) criteria.setProjection(Projections.countDistinct ("waybill_id")).uniqueResult();
            // 将条件清空
            criteria.setProjection(null);
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }*/
        return Integer.parseInt(num.toString());
    }

    /**
     * 获取详情信息
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public List<WuliuEntity> getPage(int currentPage, int pageSize, DetachedCriteria dc) {
        List<WuliuEntity> wls = null;
        Session session = getSession();
        try {
            // 开启事务
            Transaction tx = session.beginTransaction();
            // 关联session
            Criteria criteria = dc.getExecutableCriteria(session);
            // 去重数据处理
            ProjectionList proList = Projections.projectionList();
            proList.add(Projections.property("waybill_id"));
            proList.add(Projections.property("ship_time"));
            proList.add(Projections.property("ship_address"));
            proList.add(Projections.property("sender"));
            proList.add(Projections.property("ship_phone"));
            proList.add(Projections.property("ship_mobilephone"));
            proList.add(Projections.property("sj_address"));
            proList.add(Projections.property("addressee"));
            proList.add(Projections.property("sj_phone"));
            proList.add(Projections.property("sj_mobilephone"));
            proList.add(Projections.property("tjw"));
            proList.add(Projections.property("dshk"));
            proList.add(Projections.property("number_cases"));
            proList.add(Projections.property("payment"));
            proList.add(Projections.property("freight"));
            criteria.setProjection(Projections.distinct(proList));
            criteria.setFirstResult((currentPage-1)*pageSize);
            criteria.setMaxResults(pageSize);
            /*criteria.add(Restrictions.sqlRestriction("id in (select  min(id) from wuliu group by waybill_id,ship_time,ship_address,sender,ship_phone,ship_mobilephone," +
                    "sj_address,addressee,sj_phone,sj_mobilephone," +
                    "tjw,dshk,number_cases,aj_id)"));*/
            // 创建对象
            List<Object[]> list = criteria.list();
            wls = WuliuEntity.listToWulius(list);
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        for(int i=0;i<wls.size();i++){
            if(wls.get(i).getShip_address()==null){
               wls.get(i).setShip_address("");
            }
            if(wls.get(i).getSj_address()==null){
               wls.get(i).setShip_address("");
            }
            if(wls.get(i).getTjw()==null){
               wls.get(i).setTjw("");
            }
            if(wls.get(i).getFreight()==null){
               wls.get(i).setFreight("");
            }
            if(wls.get(i).getDshk()==null){
               wls.get(i).setDshk("");
            }
            if(wls.get(i).getPayment()==null){
                wls.get(i).setPayment("");
            }
            if(wls.get(i).getSj_address()==null){
                wls.get(i).setSj_address("");
            }
        }
        return wls;
    }

    /**
     * 表中总条数
     * @param id
     * @return
     */
    public int getRowCount(long id) {
        String sql = "select count(*) num from wuliu_relation where aj_id="+id;
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        // 转成String
        BigDecimal num = (BigDecimal) map.get("NUM");
        return Integer.parseInt(num.toString());
    }

    /**
     * 删除表中数据
     */
    public void deleteWuliuRelation(long id) {
        // 获得session
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            String hql = "Delete From WuliuRelationEntity Where aj_id=?";
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
     * 获取全部数据
     * @param seach
     * @return
     */
    public List<WuliuRelationEntity> getWuliuRelationAll(String seach) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from wuliu_relation where (1=1) "+seach);
        // 获得当前线程session
        Session session = getSession();
        List<WuliuRelationEntity> wlrs = null;
        try{
            // 开启事务
            Transaction transaction = session.beginTransaction();
            wlrs = session.createSQLQuery(sql.toString()).addEntity(WuliuRelationEntity.class).list();
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return wlrs;
    }

    /**
     * 详情数据全部
     * @param dc
     * @return
     */
    public List<WuliuEntity> getPageAll( DetachedCriteria dc) {
        List<WuliuEntity> wls = null;
        Session session = getSession();
        try {
            // 开启事务
            Transaction tx = session.beginTransaction();
            // 关联session
            Criteria criteria = dc.getExecutableCriteria(session);
            // 去重数据处理
            ProjectionList proList = Projections.projectionList();
            proList.add(Projections.property("waybill_id"));
            proList.add(Projections.property("ship_time"));
            proList.add(Projections.property("ship_address"));
            proList.add(Projections.property("sender"));
            proList.add(Projections.property("ship_phone"));
            proList.add(Projections.property("ship_mobilephone"));
            proList.add(Projections.property("sj_address"));
            proList.add(Projections.property("addressee"));
            proList.add(Projections.property("sj_phone"));
            proList.add(Projections.property("sj_mobilephone"));
            proList.add(Projections.property("tjw"));
            proList.add(Projections.property("dshk"));
            proList.add(Projections.property("number_cases"));
            proList.add(Projections.property("payment"));
            proList.add(Projections.property("freight"));
            criteria.setProjection(Projections.distinct(proList));
            // 创建对象
            List<Object[]> list = criteria.list();
            wls = WuliuEntity.listToWulius(list);
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        for(int i=0;i<wls.size();i++){
            if(wls.get(i).getShip_address()==null){
                wls.get(i).setShip_address("");
            }
            if(wls.get(i).getSj_address()==null){
                wls.get(i).setShip_address("");
            }
            if(wls.get(i).getTjw()==null){
                wls.get(i).setTjw("");
            }
            if(wls.get(i).getFreight()==null){
                wls.get(i).setFreight("");
            }
            if(wls.get(i).getDshk()==null){
                wls.get(i).setDshk("");
            }
            if(wls.get(i).getPayment()==null){
                wls.get(i).setPayment("");
            }
            if(wls.get(i).getSj_address()==null){
                wls.get(i).setSj_address("");
            }
        }
        return wls;
    }
}
