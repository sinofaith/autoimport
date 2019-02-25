package cn.com.sinofaith.dao.zfbDao;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlSjdzsEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlTjjgsEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlSjdzsForm;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.naming.directory.SearchControls;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
/**
 * 支付宝交易记录收件人地址持久层
 * @author zd
 * create by 2018.12.19
 */
@Repository
public class ZfbJyjlSjdzsDao extends BaseDao<ZfbJyjlSjdzsEntity>{
    /**
     * 获取收件人地址数据
     * @param id
     * @param filterInput
     * @return
     */
    public List<ZfbJyjlSjdzsEntity> selectJyjlSjdzs(long id, String filterInput) {
        String search = "";
        if(filterInput!=null&&!"".equals(filterInput)){
            search = " and upper(spmc) like '%"+filterInput.toUpperCase()+"%'";
        }
        StringBuffer sql = new StringBuffer();
        List<ZfbJyjlSjdzsEntity> sjdzsList = null;
        sql.append("select mjyhid,mjxx,sum(sjcs) sjzcs,sum(czje) czzje,sjdzs from(");
        sql.append("select j1.*,j2.sjdzs from (select t.mjyhid,substr(mjxx,0,instr(mjxx,'(',1)-1) mjxx,");
        sql.append("sum(t.jyje) czje,count(1) sjcs from(select * from (select t.*,row_number() over(partition by t.jyh,t.dyxcsj order by t.id) su " +
                "from zfbjyjl t where aj_id="+id+") where su=1) t where ");
        sql.append("t.shrdz is not null and t.jyzt='交易成功' "+search+" group by t.mjyhid,t.mjxx,t.shrdz order by mjyhid) j1 ");
        sql.append("left join(select mjyhid,count(1) sjdzs from(select t.mjyhid ");
        sql.append("from(select * from (select t.*,row_number() over(partition by t.jyh,t.dyxcsj order by " +
                "t.id) su from zfbjyjl t where aj_id="+id+") where su=1) t where t.shrdz is not null ");
        sql.append("and t.jyzt='交易成功' "+search+" group by t.mjyhid,substr(t.mjxx,0,instr(t.mjxx,'(',1)-1),t.shrdz ");
        sql.append("order by mjyhid) group by mjyhid) j2 on j1.mjyhid=j2.mjyhid) group by mjyhid,mjxx,sjdzs");
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            sjdzsList = session.createSQLQuery(sql.toString())
                    .addScalar("mjyhid").addScalar("mjxx").addScalar("sjzcs", StandardBasicTypes.LONG)
                    .addScalar("czzje", StandardBasicTypes.BIG_DECIMAL)
                    .addScalar("sjdzs", StandardBasicTypes.LONG)
                    .setResultTransformer(Transformers.aliasToBean(ZfbJyjlSjdzsEntity.class)).list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return sjdzsList;
    }

    /**
     * 删除原有统计的该案件的数据
     * @param id
     */
    public void delAll(long id) {
        delete("delete from ZfbJyjlSjdzsEntity where aj_id="+id);
    }

    /**
     * 批量插入数据
     * @param jyjlSjdzsForms
     */
    public void insertJyjlSjdzs(List<ZfbJyjlSjdzsEntity> jyjlSjdzsForms,long id) {
        // 使用原生sql
        Connection conn = DBUtil.getConnection();
        String sql = "insert into ZFBJYJL_SJDZS(mjyhid,mjxx,sjzcs,czzje,sjdzs,insert_time,aj_id) " +
                "values(?,?,?,?,?,?,?)";
        PreparedStatement pstm = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<jyjlSjdzsForms.size();i++){
                ZfbJyjlSjdzsEntity tjjg = jyjlSjdzsForms.get(i);
                pstm.setString(1,tjjg.getMjyhid());
                pstm.setString(2,tjjg.getMjxx());
                pstm.setLong(3,tjjg.getSjzcs());
                pstm.setBigDecimal(4,tjjg.getCzzje());
                pstm.setLong(5,tjjg.getSjdzs());
                pstm.setString(6, TimeFormatUtil.getDate("/"));
                pstm.setLong(7,id);
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
     * 收件地址详情条数
     * @param search
     * @return
     */
    public int getRowAllCount(String search, long id) {
        String sql = "select to_char(count(1)) NUM from(select mjyhid,substr(mjxx,0,instr(mjxx,'(',1)-1) mjxx,shrdz," +
                "count(1) sjcs,sum(jyje) czje from(select * from (select t.*,row_number() over(partition by t.jyh,t.dyxcsj " +
                "order by t.id) su from zfbjyjl t where aj_id="+id+") where su=1) where "+search+")";
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    /**
     * 收件地址详情数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param flag
     * @return
     */
    public List<ZfbJyjlSjdzsForm> getDoPageSjdzs(int currentPage, int pageSize, String search,long id, boolean flag) {
        List<ZfbJyjlSjdzsForm> forms = null;
        StringBuffer sql = new StringBuffer();
        if(flag){
            sql.append("SELECT * FROM (");
            sql.append("SELECT c.*, ROWNUM rn FROM (");
        }
        sql.append("select mjyhid,substr(mjxx,0,instr(mjxx,'(',1)-1) mjxx,shrdz,count(1) sjcs,");
        sql.append("sum(jyje) czje from(select * from (select t.*,row_number() over(partition by t.jyh,t.dyxcsj order by t.id) su" +
                " from zfbjyjl t where aj_id="+id+") where su=1) t where "+search);
        if(flag){
            sql.append(") c WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        }
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            forms = session.createSQLQuery(sql.toString())
                    .addScalar("mjyhid").addScalar("mjxx")
                    .addScalar("shrdz").addScalar("sjcs", StandardBasicTypes.LONG)
                    .addScalar("czje", StandardBasicTypes.BIG_DECIMAL)
                    .setResultTransformer(Transformers.aliasToBean(ZfbJyjlSjdzsForm.class)).list();
            tx.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.close();
        }
        return forms;
    }

    /**
     * 单个地址详情数据总条数
     * @param search
     * @return
     */
    public int getRowAllCount1(String search) {
        StringBuffer sql = new StringBuffer();
        sql.append("select to_char(count(1)) NUM from (select t.*,row_number() over(partition by ");
        sql.append("t.jyh order by t.id) su from zfbjyjl t where "+search+") where su=1");
        List list = findBySQL(sql.toString());
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    /**
     * 单个地址详情分页数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param flag
     * @return
     */
    public List<ZfbJyjlEntity> getDoPageSjdzs1(int currentPage, int pageSize, String search, boolean flag) {
        List<ZfbJyjlEntity> jyjlList = null;
        StringBuffer sql = new StringBuffer();
        if(flag){
            sql.append("SELECT * FROM ( ");
            sql.append("SELECT c.*, ROWNUM rn FROM (");
        }
        sql.append("select * from (select t.*,row_number() over(partition by ");
        sql.append("t.jyh order by t.id) su from zfbjyjl t where "+search+") where su=1");
        if(flag){
            sql.append(") c WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        }
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            jyjlList = session.createSQLQuery(sql.toString())
                    .addEntity(ZfbJyjlEntity.class).list();
            tx.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.close();
        }
        return jyjlList;
    }

    /**
     * 获取收件地址数大于等于10个的
     * @param aj
     * @return
     */
    public List<ZfbJyjlSjdzsForm> getDoPageSjdzsGE10(AjEntity aj) {
        List<ZfbJyjlSjdzsForm> jyjlSjdzsForm = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select mjyhid,substr(mjxx,1,instr(mjxx,')',1)) mjxx,shrdz,count(1) sjcs,");
        sql.append("sum(jyje) czje from(select * from (select t.*,row_number() over(partition by t.jyh,t.dyxcsj order by t.id) " +
                "su from zfbjyjl t where aj_id="+aj.getId()+") where su=1) t where mjyhid in(");
        sql.append("select mjyhid from ZFBJYJL_SJDZS t where aj_id="+aj.getId()+" and sjdzs>=10) and shrdz is not null ");
        sql.append("and jyzt='交易成功' group by mjyhid,mjxx,shrdz order by mjyhid desc nulls last,sjcs desc");
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            jyjlSjdzsForm = session.createSQLQuery(sql.toString())
                    .addScalar("mjyhid").addScalar("mjxx")
                    .addScalar("shrdz").addScalar("sjcs", StandardBasicTypes.LONG)
                    .addScalar("czje", StandardBasicTypes.BIG_DECIMAL)
                    .setResultTransformer(Transformers.aliasToBean(ZfbJyjlSjdzsForm.class)).list();
            tx.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.close();
        }
        return jyjlSjdzsForm;
    }
}
