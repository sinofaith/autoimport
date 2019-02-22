package cn.com.sinofaith.dao.zfbDao;

import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlTjjgsForm;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxTjjgsForm;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/***
 * 支付宝交易记录持久层
 * @author zd
 * create by 2018.11.28
 */
@Repository
public class ZfbJyjlDao extends BaseDao<ZfbJyjlEntity>{
    /**
     * 批量插入数据
     * @param jyjlList
     * @param id
     * @return
     */
    public int insertJyjl(List<ZfbJyjlEntity> jyjlList, long id) {
        // 使用原生sql
        Connection conn = DBUtil.getConnection();
        String sql = "insert into ZfbJyjl(jyh,wbjyh,jyzt,hzhbid,mjyhid,mjxx,mijyhid,mijxx,jyje,sksj,zhxgsj,cjsj,jylx,lyd,spmc,shrdz,dyxcsj,insert_time,aj_id) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int a = 0;
        PreparedStatement pstm = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<jyjlList.size();i++){
                ZfbJyjlEntity Jyjl = jyjlList.get(i);
                pstm.setString(1,Jyjl.getJyh());
                pstm.setString(2,Jyjl.getWbjyh());
                pstm.setString(3,Jyjl.getJyzt());
                pstm.setString(4,Jyjl.getHzhbId());
                pstm.setString(5,Jyjl.getMjyhId());
                pstm.setString(6,Jyjl.getMjxx());
                pstm.setString(7,Jyjl.getMijyhId());
                pstm.setString(8,Jyjl.getMijxx());
                pstm.setDouble(9,Jyjl.getJyje());
                pstm.setString(10,Jyjl.getSksj());
                pstm.setString(11,Jyjl.getZhxgsj());
                pstm.setString(12,Jyjl.getCjsj());
                pstm.setString(13,Jyjl.getJylx());
                pstm.setString(14,Jyjl.getLyd());
                pstm.setString(15,Jyjl.getSpmc());
                pstm.setString(16,Jyjl.getShrdz());
                pstm.setString(17,Jyjl.getDyxcsj());
                pstm.setString(18, TimeFormatUtil.getDate("/"));
                pstm.setLong(19,id);
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

    /**
     * 分页总数
     * @param seach
     * @param id
     * @return
     *//*
    public int getCountRow(String seach, long id) {
        StringBuffer sql = new StringBuffer();
        sql.append("select to_char(count(1)) NUM from (");
        sql.append("select min(jyh) jyh,mjyhid,mjxx,mijyhid,mijxx,spmc,shrdz,count(jyje) jyzcs,sum(jyje) jyzje from (");
        sql.append("select distinct j.jyh,j.jyzt,j.mjyhid,j.mjxx,j.mijyhid,j.mijxx,j.sksj,j.jyje,j.spmc,j.shrdz ");
        sql.append("from zfbjyjl j where j.jyzt='交易成功' and (");
        sql.append("(j.mijyhid in (select trim(z.yhid) from zfbzcxx z where z.aj_id="+id+") and j.aj_id="+id+") or ");
        sql.append("(j.mjyhid in (select trim(z.yhid) from zfbzcxx z where z.aj_id="+id+") and j.aj_id="+id+") ");
        sql.append("))group by mjyhid,mjxx,mijyhid,mijxx,spmc,shrdz) where (1=1) "+ seach);
        List list = findBySQL(sql.toString());
        Map map = (Map) list.get(0);
        return Integer.parseInt((String) map.get("NUM"));
    }

    *//**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param seach
     * @param id
     * @return
     *//*
    public List<ZfbJyjlForm> queryForPage(int currentPage, int pageSize, String seach, long id) {
        List<ZfbJyjlForm> zfbJyjlForms = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ( ");
        sql.append("SELECT c.*, ROWNUM rn FROM (");
        sql.append("select mjyhid,mjxx,mijyhid,mijxx,spmc,shrdz,jyzcs,jyzje from(select min(jyh) jyh,mjyhid,mjxx,mijyhid,mijxx,spmc,shrdz,count(jyje) jyzcs,sum(jyje) jyzje from (");
        sql.append("select distinct j.jyh,j.jyzt,j.mjyhid,j.mjxx,j.mijyhid,j.mijxx,j.sksj,j.jyje,j.spmc,j.shrdz ");
        sql.append("from zfbjyjl j where j.jyzt='交易成功' and (");
        sql.append("(j.mijyhid in (select trim(z.yhid) from zfbzcxx z where z.aj_id="+id+") and j.aj_id="+id+") or ");
        sql.append("(j.mjyhid in (select trim(z.yhid) from zfbzcxx z where z.aj_id="+id+") and j.aj_id="+id+")");
        sql.append(")) group by mjyhid,mjxx,mijyhid,mijxx,spmc,shrdz) where (1=1) "+seach+") c ");
        sql.append(" WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        String sql1 = "select yhid from zfbzcxx z where z.aj_id="+id;
        List<String> yhidList = null;
        // 获取当前线程session
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            zfbJyjlForms = session.createSQLQuery(sql.toString())
                    .addScalar("mjyhid")
                    .addScalar("mjxx")
                    .addScalar("mijyhid")
                    .addScalar("mijxx")
                    .addScalar("spmc")
                    .addScalar("shrdz")
                    .addScalar("jyzcs", StandardBasicTypes.LONG)
                    .addScalar("jyzje", StandardBasicTypes.DOUBLE)
                    .setResultTransformer(Transformers.aliasToBean(ZfbJyjlForm.class)).list();
            yhidList = session.createSQLQuery(sql1.toString()).list();
            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        if(zfbJyjlForms!=null){
            for (ZfbJyjlForm zfbJyjl : zfbJyjlForms) {
                boolean flag = true;
                for (String yhid : yhidList) {
                    if(yhid.equals(zfbJyjl.getMjyhid())){
                        if(flag){
                            zfbJyjl.setDirection("买家");
                        }*//*else{
                            zfbJyjl.setDirection("互通");
                        }*//*
                        flag = false;
                    }
                    if(yhid.equals(zfbJyjl.getMijyhid())){
                        if(flag){
                            zfbJyjl.setDirection("卖家");
                            String mjxx = zfbJyjl.getMjxx();
                            zfbJyjl.setMjxx(zfbJyjl.getMijxx());
                            zfbJyjl.setMijxx(mjxx);
                            String mjyhid = zfbJyjl.getMjyhid();
                            zfbJyjl.setMjyhid(zfbJyjl.getMijyhid());
                            zfbJyjl.setMijyhid(mjyhid);
                        }*//*else{
                            if(!"买家".equals(zfbJyjl.getDirection())){
                                String mjxx = zfbJyjl.getMjxx();
                                zfbJyjl.setMjxx(zfbJyjl.getMijxx());
                                zfbJyjl.setMijxx(mjxx);
                                String mjyhid = zfbJyjl.getMjyhid();
                                zfbJyjl.setMjyhid(zfbJyjl.getMijyhid());
                                zfbJyjl.setMijyhid(mjyhid);
                            }
                            zfbJyjl.setDirection("互通");
                        }*//*
                        flag = false;
                    }
                }
            }
        }
        return zfbJyjlForms;
    }*/

    /**
     * 去重详情条数
     * @param dc
     * @return
     */
    public int getRowAlls(DetachedCriteria dc) {
        Session session = getSession();
        Long rowAll = 0l;
        try {
            Transaction tx = session.beginTransaction();
            Criteria criteria = dc.getExecutableCriteria(session);
            rowAll = (Long) criteria.setProjection(Projections.countDistinct ("jyh")).uniqueResult();
            criteria.setProjection(null);
            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return rowAll.intValue();
    }

    /**
     * 去重详情数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public List<ZfbJyjlEntity> getDoPageJyjl(int currentPage, int pageSize, DetachedCriteria dc) {
        Session session = getSession();
        List<ZfbJyjlEntity> zhxxs = null;
        try {
            // 开启事务
            Transaction tx = session.beginTransaction();
            // 关联session
            Criteria criteria = dc.getExecutableCriteria(session);
            ProjectionList proList = Projections.projectionList();
            proList.add(Projections.property("jyh"));
            proList.add(Projections.property("mjyhId"));
            proList.add(Projections.property("mjxx"));
            proList.add(Projections.property("mijyhId"));
            proList.add(Projections.property("mijxx"));
            proList.add(Projections.property("jyje"));
            proList.add(Projections.property("sksj"));
            proList.add(Projections.property("jylx"));
            proList.add(Projections.property("spmc"));
            proList.add(Projections.property("shrdz"));
            criteria.setProjection(Projections.distinct(proList));
            criteria.setFirstResult((currentPage-1)*pageSize);
            criteria.setMaxResults(pageSize);
            // 创建对象
            List<Object[]> list = criteria.list();
            zhxxs = ZfbJyjlEntity.listToZfbJyjls(list);
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return zhxxs;
    }

    /**
     * 条件筛选查询数据
     * @param search
     * @param id
     * @return
     */
    public List<ZfbJyjlTjjgsForm> selectFilterJyjlBySpmc(String search, long id) {
        List<ZfbJyjlTjjgsForm> zfbJyjlList = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select j.mjyhid,substr(j.mjxx,1,instr(j.mjxx,')')) mjxx,j.jyzt,j.mijyhid,");
        sql.append("substr(j.mijxx,1,instr(j.mijxx,')')) mijxx,j.jyje from(select * from (select j1.*,row_number() ");
        sql.append("over( partition by j1.jyh order by j1.id) su from zfbjyjl j1 where aj_id="+id+" and j1.jyzt='交易成功'");
        if(!search.equals("")){
            sql.append(" "+search);
        }
        sql.append(") where su=1) j left join(select * from (select t.*,row_number() over(partition by t.yhid order by t.id) " +
                "su from zfbzcxx t where aj_id="+id+") where su=1) c on c.dyxcsj=j.dyxcsj and (j.mjyhid=c.yhid or j.mijyhid=c.yhid)");
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            zfbJyjlList = session.createSQLQuery(sql.toString())
                    .addScalar("mjyhid").addScalar("mjxx")
                    .addScalar("jyzt").addScalar("mijyhid")
                    .addScalar("mijxx").addScalar("jyje", StandardBasicTypes.BIG_DECIMAL)
                    .setResultTransformer(Transformers.aliasToBean(ZfbJyjlTjjgsForm.class)).list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return zfbJyjlList;
    }
}
