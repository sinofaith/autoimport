package cn.com.sinofaith.dao.zfbDao;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlTjjgsEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlTjjgsForm;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
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
 * 支付宝交易记录对手账户持久层
 * @author ZD
 * create by 2018.12.16
 */
@Repository
public class ZfbJyjlTjjgsDao extends BaseDao<ZfbJyjlTjjgsEntity>{

    /**
     * 获取对手数据
     * @param id
     * @return
     */
    public List<ZfbJyjlTjjgsForm> selectJyjlTjjgs(long id) {
        List<ZfbJyjlTjjgsForm> tjjgsForms = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select a.mjyhid,substr(a.mjxx,1,instr(a.mjxx,')')) mjxx,a.jyzt,a.mijyhid,");
        sql.append("substr(a.mijxx,1,instr(a.mijxx,')')) mijxx,a.jyje from(");
        sql.append("select * from (select t.*,row_number() over(");
        sql.append("partition by t.jyh order by t.id) su from zfbjyjl t where aj_id="+id+") where su=1) a");
        Session session = getSession();
        try {
            // 开启事务
            Transaction tx = session.beginTransaction();
            tjjgsForms = session.createSQLQuery(sql.toString())
                    .addScalar("mjyhid").addScalar("mjxx").addScalar("jyzt")
                    .addScalar("mijyhid").addScalar("mijxx")
                    .addScalar("jyje", StandardBasicTypes.BIG_DECIMAL)
                    .setResultTransformer(Transformers.aliasToBean(ZfbJyjlTjjgsForm.class)).list();
            // 关闭事务
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return tjjgsForms;
    }

    public void delAll(long id) {
        delete("delete from ZfbJyjlTjjgsEntity where aj_id="+id);
    }

    /**
     * 交易记录数据导入
     * @param jyjlTjjgsList
     */
    public void insertJyjlTjjgs(List<ZfbJyjlTjjgsEntity> jyjlTjjgsList) {
        // 使用原生sql
        Connection conn = DBUtil.getConnection();
        String sql = "insert into ZFBJYJL_TJJGS(zfbmc,zfbzh,jyzt,dfmc,dfzh,fkzcs,fkzje,insert_time,aj_id) " +
                "values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstm = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<jyjlTjjgsList.size();i++){
                ZfbJyjlTjjgsEntity tjjg = jyjlTjjgsList.get(i);
                pstm.setString(1,tjjg.getZfbmc());
                pstm.setString(2,tjjg.getZfbzh());
                pstm.setString(3,tjjg.getJyzt());
                pstm.setString(4,tjjg.getDfmc());
                pstm.setString(5,tjjg.getDfzh());
                pstm.setLong(6,tjjg.getFkzcs());
                pstm.setBigDecimal(7,tjjg.getFkzje());
                pstm.setString(8, TimeFormatUtil.getDate("/"));
                pstm.setLong(9,tjjg.getAj_id());
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
     * 交易记录对手账户详情总条数
     * @param search
     * @param aj
     * @return
     */
    public int getRowAllCount(String search,AjEntity aj) {
        StringBuffer sql = new StringBuffer();
        sql.append("select to_char(count(1)) NUM from (select j.*,row_number() over(");
        sql.append("partition by j.jyh order by j.id) su from zfbjyjl j where aj_id="+aj.getId()+search+")");
        List list = findBySQL(sql.toString());
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    /**
     * 交易记录对手账户详情数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param aj
     * @param flag
     * @return
     */
    public List<ZfbJyjlEntity> getDoPageTjjgs(int currentPage, int pageSize, String search, AjEntity aj, boolean flag) {
        List<ZfbJyjlEntity> jyjlTjjgs = null;
        StringBuffer sql = new StringBuffer();
        if(flag){
            sql.append("SELECT * FROM ( ");
            sql.append("SELECT c.*, ROWNUM rn FROM (");
        }
        sql.append("select j.jyh,j.mjyhid,substr(j.mjxx,1,instr(j.mjxx,')')) mjxx,j.jyzt,j.mijyhid,");
        sql.append("substr(j.mijxx,1,instr(j.mijxx,')')) mijxx,j.jyje,j.sksj,j.spmc,j.shrdz from (");
        sql.append("select j.*,row_number() over(");
        sql.append("partition by j.jyh order by j.id) su from zfbjyjl j where aj_id="+aj.getId()+search+") j");
        if(flag){
            sql.append(") c WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        }
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            jyjlTjjgs = session.createSQLQuery(sql.toString())
                    .addScalar("jyh").addScalar("mjyhId").addScalar("mjxx")
                    .addScalar("jyzt").addScalar("mijyhId").addScalar("mijxx")
                    .addScalar("jyje", StandardBasicTypes.DOUBLE).addScalar("sksj").addScalar("spmc")
                    .addScalar("shrdz").setResultTransformer(Transformers.aliasToBean(ZfbJyjlEntity.class)).list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return jyjlTjjgs;
    }

    /**
     * 数据导出
     * @param dc
     * @return
     */
    public List<ZfbJyjlTjjgsEntity> getZfbZzmxTjjgAll(DetachedCriteria dc) {
        Session session = getSession();
        List<ZfbJyjlTjjgsEntity> zhxxs = null;
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
}
