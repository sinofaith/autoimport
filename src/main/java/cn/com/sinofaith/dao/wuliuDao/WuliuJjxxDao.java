package cn.com.sinofaith.dao.wuliuDao;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
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
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WuliuJjxxDao extends BaseDao<WuliuEntity> {

    /**
     * 获取所有条目
     * @return
     */
    public int getRowAll(String seach, AjEntity aj) {
        StringBuffer sql = new StringBuffer();
        int rowAll = 0;
        if(aj.getFlg()==1){
            sql.append("select count(*) num from (select t.*,row_number() ");
            sql.append(" over(partition by t.waybill_id,substr(trim(t.ship_time),1,19),");
            sql.append(" t.ship_address,t.sender,t.ship_phone,t.ship_mobilephone,");
            sql.append(" t.sj_address,t.addressee,t.sj_phone,t.sj_mobilephone,");
            sql.append(" t.tjw,t.dshk,t.number_cases order by t.id) su from wuliu t where 1=1 "+seach+") where su=1");
        }else{
            sql.append("select count(*) num from wuliu where 1=1 "+seach);
        }

        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            List list = session.createSQLQuery(sql.toString()).list();
            BigDecimal num = (BigDecimal) list.get(0);
            rowAll = Integer.parseInt(num.toString());
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        /*Session session = getSession();
        Long rowAll = 0l;
        try {
            Transaction tx = session.beginTransaction();
            Criteria criteria = dc.getExecutableCriteria(session);
            // 设置聚合查询函数
            if(aj.getFlg()==1){
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
            }else{
                criteria.setProjection(Projections.rowCount());
                rowAll = (Long) criteria.uniqueResult();
            }
            // 将条件清空，用于dc查询分页数据
            criteria.setProjection(null);
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }*/
        return rowAll;
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public List<WuliuEntity> getDoPage(int currentPage, int pageSize, DetachedCriteria dc, AjEntity aj) {
        Session session = getSession();
        List<WuliuEntity> zhxxs = null;
        try {
            // 开启事务
            Transaction tx = session.beginTransaction();
            // 关联session
            Criteria criteria = dc.getExecutableCriteria(session);
            if(aj.getFlg()==1){
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
                //criteria.add(Restrictions.sqlRestriction("id in (select  min(id) from wuliu group by waybill_id)"));
            }
            criteria.setFirstResult((currentPage-1)*pageSize);
            criteria.setMaxResults(pageSize);
            // 创建对象
            if(aj.getFlg()==1){
                List<Object[]> list = criteria.list();
                zhxxs = WuliuEntity.listToWulius(list);
            }else{
                zhxxs = criteria.list();
            }
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return zhxxs;
    }

    /**
     * 物流数据导入
     * @param listJjxx
     * @param aj_id
     * @param all
     * @return
     */
    public int insertJjxx(List<WuliuEntity> listJjxx, long aj_id, List<WuliuEntity> all) {
        List<WuliuEntity> w = null;
        // 数据去重
        // 根据运单号+寄件时间去重
//        if(all!=null && all.size()>0){
//            Map<String,WuliuEntity> map1 = new HashMap<>();
//            for (int i=0;i<all.size();i++){
//                WuliuEntity wuliu = all.get(i);
//                map1.put(wuliu.getWaybill_id()+wuliu.getShip_time().replace("null", ""),null);
//            }
//            Map<String,WuliuEntity> map = new HashMap<>();
//            for(int i=0;i<listJjxx.size();i++){
//                WuliuEntity wuliu = listJjxx.get(i);
//                map.put(wuliu.getWaybill_id()+wuliu.getShip_time().replace("null", ""),wuliu);
//            }
//
//            // 将相同key添加
//            List<String> str = new ArrayList<>();
//            for(String key : map.keySet()){
//                if(map1.containsKey(key)){
//                    str.add(key);
//                }
//            }
//            map1 = null;
//            if(map.size()==str.size()){
//                return -1;
//            }
//
//            for(int i=0;i<str.size();i++){
//                // 去重
//                map.remove(str.get(i));
//            }
//            w = new ArrayList<>(map.values());
//            map = null;
//        }else{
            w = listJjxx;
//        }

        // 使用原生sql
        Connection conn = DBUtil.getConnection();
        String sql = "insert into wuliu(waybill_id,ship_time,ship_address,sender,ship_phone,ship_mobilephone,sj_address," +
                "addressee,sj_phone,sj_mobilephone,collector,tjw,payment,dshk,weight,number_cases,freight,insert_time,aj_id) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int a = 0;
        PreparedStatement pstm = null;
        WuliuEntity zzxx = new WuliuEntity();
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<w.size();i++){
                WuliuEntity wl = w.get(i);
                pstm.setString(1,wl.getWaybill_id());
                pstm.setString(2,wl.getShip_time());
                pstm.setString(3,wl.getShip_address());
                pstm.setString(4,wl.getSender());
                pstm.setString(5,wl.getShip_phone());
                pstm.setString(6,wl.getShip_mobilephone());
                pstm.setString(7,wl.getSj_address());
                pstm.setString(8,wl.getAddressee());
                pstm.setString(9,wl.getSj_phone());
                pstm.setString(10,wl.getSj_mobilephone());
                pstm.setString(11,wl.getCollector());
                pstm.setString(12,wl.getTjw());
                pstm.setString(13,wl.getPayment());
                pstm.setString(14,wl.getDshk());
                pstm.setString(15,wl.getWeight());
                pstm.setString(16,wl.getNumber_cases());
                pstm.setString(17,wl.getFreight());
                pstm.setString(18,TimeFormatUtil.getDate("/"));
                pstm.setLong(19,aj_id);
                pstm.addBatch();
                a++;
                // 有5000条添加一次
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
        listJjxx = null;
        return a;
    }

    /**
     *
     * @param id
     * @return
     */
    public List<WuliuRelationEntity> insertRelation(long id) {
        StringBuffer sql = new StringBuffer();
        sql.append("select max(t.sender) sender,t.ship_phone,max(t.ship_address) ship_address,max(t.addressee) addressee, ");
        sql.append(" t.sj_phone,max(sj_address) sj_address,count(*) num,max(t.aj_id) aj_id ");
        sql.append(" from (select * from (select t.*,row_number() ");
        sql.append(" over(partition by t.waybill_id,substr(trim(t.ship_time),1,19), ");
        sql.append(" t.ship_address,t.sender,t.ship_phone,t.ship_mobilephone, ");
        sql.append(" t.sj_address,t.addressee,t.sj_phone,t.sj_mobilephone, ");
        sql.append(" t.tjw,t.dshk,t.number_cases order by t.id) su from wuliu t where aj_id="+id+") where su=1) t ");
        sql.append(" group by t.ship_phone,t.sj_phone order by num desc");
        List<WuliuRelationEntity> list = null;
        // 获得当前session
        Session session = getSession();
        // 开启事务
        try{
            Transaction tx = session.beginTransaction();
            list = session.createSQLQuery(sql.toString())
                    .addScalar("sender")
                    .addScalar("ship_phone")
                    .addScalar("ship_address")
                    .addScalar("addressee")
                    .addScalar("sj_phone")
                    .addScalar("sj_address")
                    .addScalar("num", StandardBasicTypes.LONG)
                    .addScalar("aj_id",StandardBasicTypes.LONG)
                    .setResultTransformer(Transformers.aliasToBean(WuliuRelationEntity.class)).list();
            tx.commit();
        }catch (Exception e){
            session.close();
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 获取所有信息
     * @param dc
     * @param aj
     * @return
     */
    public List<WuliuEntity> getWuliuAll(DetachedCriteria dc, AjEntity aj) {
        Session session = getSession();
        List<WuliuEntity> zhxxs = null;
        try {
            // 开启事务
            Transaction tx = session.beginTransaction();
            // 关联session
            Criteria criteria = dc.getExecutableCriteria(session);
            if(aj.getFlg()==1){
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
            }
            // 创建对象
            if(aj.getFlg()==1){
                List<Object[]> list = criteria.list();
                zhxxs = WuliuEntity.listToWulius(list);
            }else{
                zhxxs = criteria.list();
            }
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return zhxxs;
    }
}
