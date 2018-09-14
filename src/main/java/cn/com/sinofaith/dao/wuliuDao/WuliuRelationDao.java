package cn.com.sinofaith.dao.wuliuDao;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.bean.wlBean.WuliuRelationEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.form.wlForm.WuliuRelationForm;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
        String sql = "insert into wuliu_relation(ship_phone,sj_phone,num,aj_id) values(?,?,?,?)";
        PreparedStatement pstm = null;
        WuliuRelationEntity wlr = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<wls.size();i++){
                wlr = wls.get(i);
                pstm.setString(1,wlr.getShip_phone());
                pstm.setString(2,wlr.getSj_phone());
                pstm.setLong(3,wlr.getNum());
                pstm.setLong(4,wlr.getAj_id());
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
    public int getAllRowCount(String seach, long aj_id) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) num from wuliu_relation b left join (select * from (select a.sender, ");
        sql.append(" a.ship_address,a.ship_phone,a.addressee,a.sj_address,a.sj_phone  from (select t.*, ");
        sql.append(" row_number() over(partition by  t.ship_phone,t.sj_phone order by t.id) su from wuliu t where aj_id="+aj_id+") ");
        sql.append(" a where su=1)) a on b.ship_phone=a.ship_phone and b.sj_phone=a.sj_phone ");
        sql.append(" where 1=1 and (sj_address is not null or ship_address is not null) "+seach);
        List list = findBySQL(sql.toString());
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
        sql.append(" select a.sender,a.ship_address,a.addressee,a.sj_address,b.* from wuliu_relation b left join (");
        sql.append(" select * from (select a.sender,a.ship_address,a.ship_phone,a.addressee,a.sj_address,a.sj_phone ");
        sql.append(" from (select t.*,row_number() over(partition by ");
        sql.append(" t.ship_phone,t.sj_phone order by t.id) su from wuliu t where aj_id="+aj_id+" ) a where su=1)) a on b.ship_phone=a.ship_phone and b.sj_phone=a.sj_phone where (sj_address is not null or ship_address is not null) " + seach);
        sql.append(") c ");
        sql.append(" WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        // 获得当前线程session
        Session session = getSession();
        SQLQuery query = null;
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
     * @param dc
     * @return
     */
    public int getRowAll(DetachedCriteria dc) {
        Session session = getSession();
        Long rowAll = 0l;
        try {
            Transaction tx = session.beginTransaction();
            // 将离线查询对象与session绑定
            Criteria criteria = dc.getExecutableCriteria(session);
            // 设置聚合函数
            rowAll = (Long) criteria.setProjection(Projections.countDistinct ("waybill_id")).uniqueResult();
            // 将条件清空
            criteria.setProjection(null);
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return rowAll.intValue();
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
            criteria.setFirstResult((currentPage-1)*pageSize);
            criteria.setMaxResults(pageSize);
            criteria.add(Restrictions.sqlRestriction("id in (select  min(id) from wuliu group by waybill_id,ship_time,ship_address,sender,ship_phone,ship_mobilephone," +
                    "sj_address,addressee,sj_phone,sj_mobilephone," +
                    "tjw,dshk,number_cases,aj_id)"));
            // 创建对象
            wls  = criteria.list();
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
        }
        return wls;
    }
}
