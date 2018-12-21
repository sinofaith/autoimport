package cn.com.sinofaith.dao.zfbDao;

import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxTjjgForm;
import cn.com.sinofaith.util.DBUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 支付宝转账明细统计结果持久层
 * @author zd
 * create by zd
 */
@Repository
public class ZfbZzmxTjjgDao extends BaseDao<ZfbZzmxTjjgEntity> {

    /**
     * 统计字段数据导入
     * @param zzmxTjjgList
     */
    public void insertZzmxTjjg(List<ZfbZzmxTjjgEntity> zzmxTjjgList) {
        // 使用原生sql
        Connection conn = DBUtil.getConnection();
        String sql = "insert into ZfbZzmx_Tjjg(zfbmc,zfbzh,zzcpmc,jyzcs,fkzcs,fkzje,skzcs,skzje,insert_time,aj_id) " +
                "values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstm = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<zzmxTjjgList.size();i++){
                ZfbZzmxTjjgEntity tjjg = zzmxTjjgList.get(i);
                pstm.setString(1,tjjg.getZfbmc());
                pstm.setString(2,tjjg.getZfbzh());
                pstm.setString(3,tjjg.getZzcpmc());
                pstm.setLong(4,tjjg.getJyzcs());
                pstm.setLong(5,tjjg.getFkzcs());
                pstm.setBigDecimal(6,tjjg.getFkzje());
                pstm.setLong(7,tjjg.getSkzcs());
                pstm.setBigDecimal(8,tjjg.getSkzje());
                pstm.setString(9,tjjg.getInsert_time());
                pstm.setLong(10,tjjg.getAj_id());
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
     * 删除该案件的统计结果
     * @param id
     */
    public void delAll(long id) {
        delete("delete from ZfbZzmxTjjgEntity where aj_id="+id);
    }

    /**
     * 获取统计数据
     * @param id
     * @return
     */
    public List<ZfbZzmxTjjgForm> selectZzmxTjjg(long id) {
        List<ZfbZzmxTjjgForm> zzmxTjjgForms = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select f.*,s.*,f.fkzcs+s.skzcs jyzcs from (");
        sql.append("select c.zhmc fkzhmc,z.fkfzfbzh,z.zzcpmc fkzzcpmc,count(1) fkzcs,sum(z.zzje) fkzje from zfbzzmx z ");
        sql.append("left join zfbzcxx c on c.dyxcsj = z.dyxcsj where c.yhid = z.fkfzfbzh and c.aj_id="+id+" and z.aj_id="+id);
        sql.append(" group by c.zhmc,z.zzcpmc,z.fkfzfbzh) f full join( select c.zhmc skzhmc,z.skfzfbzh,z.zzcpmc skzzcpmc,count(1) skzcs,sum(z.zzje) skzje ");
        sql.append(" from zfbzzmx z left join zfbzcxx c on c.dyxcsj = z.dyxcsj where c.yhid = z.skfzfbzh and c.aj_id="+id);
        sql.append(" and z.aj_id="+id+" group by c.zhmc,z.zzcpmc,z.skfzfbzh) s on f.fkfzfbzh = s.skfzfbzh and f.fkzzcpmc = s.skzzcpmc");
        // 获得当前线程session
        Session session = getSession();
        try{
            // 开启事务
            Transaction tx = session.beginTransaction();
            zzmxTjjgForms = session.createSQLQuery(sql.toString())
                    .addScalar("fkzhmc").addScalar("fkfzfbzh").addScalar("fkzzcpmc")
                    .addScalar("fkzcs", StandardBasicTypes.LONG).addScalar("fkzje", StandardBasicTypes.BIG_DECIMAL)
                    .addScalar("skzhmc").addScalar("skfzfbzh").addScalar("skzzcpmc")
                    .addScalar("skzcs", StandardBasicTypes.LONG).addScalar("skzje", StandardBasicTypes.BIG_DECIMAL)
                    .addScalar("jyzcs", StandardBasicTypes.LONG)
                    .setResultTransformer(Transformers.aliasToBean(ZfbZzmxTjjgForm.class)).list();
            // 提交事务
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return zzmxTjjgForms;
    }

    /**
     * 统计结果详情总条数
     * @param search
     * @param aj_id
     * @return
     */
    public int getRowAllCount(String search, long aj_id) {
        StringBuffer sql = new StringBuffer();
        sql.append("select to_char(count(1)) NUM from (select * from (select t.*,row_number() over( ");
        sql.append("partition by t.jyh order by t.id) su from zfbzzmx t where aj_id = "+aj_id+") where su=1) z ");
        sql.append("where "+search);
        List list = findBySQL(sql.toString());
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List<ZfbZzmxEntity> getDoPageTjjg(int currentPage, int pageSize, String search, long aj_id) {
        List<ZfbZzmxEntity> zzmxTjjgs = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ( ");
        sql.append("SELECT c.*, ROWNUM rn FROM (");
        sql.append("select * from (select * from (select t.*,row_number() over( ");
        sql.append("partition by t.jyh order by t.id) su from zfbzzmx t where aj_id = "+aj_id+") where su=1) z ");
        sql.append("where "+search+") c");
        sql.append(" WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            zzmxTjjgs = session.createSQLQuery(sql.toString())
                    .addEntity(ZfbZzmxEntity.class).list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return zzmxTjjgs;
    }

    /**
     * 详情数据下载
     * @param dc
     * @return
     */
    public List<ZfbZzmxTjjgEntity> getZfbZzmxTjjgAll(DetachedCriteria dc) {
        Session session = getSession();
        List<ZfbZzmxTjjgEntity> zhxxs = null;
        try {
            // 开启事务
            Transaction tx = session.beginTransaction();
            // 关联session
            Criteria criteria = dc.getExecutableCriteria(session);
            // 创建对象
            zhxxs = criteria.list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return zhxxs;
    }

    /**
     * 页面加载显示数据
     * @param dc
     * @return
     */
    public List<String> onload(DetachedCriteria dc) {
        Session session = getSession();
        List<String> zzcpmcs = null;
        try {
            // 开启事务
            Transaction tx = session.beginTransaction();
            // 关联session
            Criteria criteria = dc.getExecutableCriteria(session);
            // 创建对象
            zzcpmcs = criteria.list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return zzcpmcs;
    }
}
