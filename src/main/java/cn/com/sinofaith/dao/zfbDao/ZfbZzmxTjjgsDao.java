package cn.com.sinofaith.dao.zfbDao;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgsEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxTjjgsForm;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxTjjgssForm;
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
 * 支付宝转账明细对手账户持久层
 * @author zd
 * create by 2018.12.14
 */
@Repository
public class ZfbZzmxTjjgsDao extends BaseDao<ZfbZzmxTjjgsEntity> {

    /**
     * 支付宝对手账户数据
     * @param id
     * @return
     */
    public List<ZfbZzmxTjjgsForm> selectZzmxTjjgs(long id) {
        List<ZfbZzmxTjjgsForm> tjjgsForms= null;
        StringBuffer sql = new StringBuffer();
        sql.append("select c.yhid,c.zhmc,a.fkfzfbzh,a.skfzfbzh,h.jydfxx,a.zzje from(");
        sql.append("select * from (select t.*,row_number() over(");
        sql.append("partition by t.jyh order by t.id) su from zfbzzmx t where aj_id="+id+") where su=1) a ");
        sql.append("left join(select * from (select t.*,row_number() over(");
        sql.append("partition by t.jyh order by t.id) su from zfbzhmx t where aj_id="+id+") where su=1) h on a.jyh=h.jyh ");
        sql.append("left join zfbzcxx c on c.dyxcsj = a.dyxcsj and c.aj_id="+id);
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            tjjgsForms = session.createSQLQuery(sql.toString())
                    .addScalar("yhid").addScalar("zhmc")
                    .addScalar("fkfzfbzh").addScalar("skfzfbzh")
                    .addScalar("jydfxx").addScalar("zzje", StandardBasicTypes.BIG_DECIMAL)
                    .setResultTransformer(Transformers.aliasToBean(ZfbZzmxTjjgsForm.class)).list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return tjjgsForms;
    }

    public void delAll(long id) {
        delete("delete from ZfbZzmxTjjgsEntity where aj_id="+id);
    }

    /**
     * 统计字段数据导入
     * @param zzmxTjjgsList
     */
    public void insertZzmxTjjgs(List<ZfbZzmxTjjgsEntity> zzmxTjjgsList) {
        // 使用原生sql
        Connection conn = DBUtil.getConnection();
        String sql = "insert into ZfbZzmx_Tjjgs(zfbmc,zfbzh,dfmc,dfzh,jyzcs,fkzcs,fkzje,skzcs,skzje,insert_time,aj_id) " +
                "values(?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstm = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<zzmxTjjgsList.size();i++){
                ZfbZzmxTjjgsEntity tjjg = zzmxTjjgsList.get(i);
                pstm.setString(1,tjjg.getZfbmc());
                pstm.setString(2,tjjg.getZfbzh());
                pstm.setString(3,tjjg.getDfmc());
                pstm.setString(4,tjjg.getDfzh());
                pstm.setLong(5,tjjg.getJyzcs());
                pstm.setLong(6,tjjg.getFkzcs());
                pstm.setBigDecimal(7,tjjg.getFkzje());
                pstm.setLong(8,tjjg.getSkzcs());
                pstm.setBigDecimal(9,tjjg.getSkzje());
                pstm.setString(10, TimeFormatUtil.getDate("/"));
                pstm.setLong(11,tjjg.getAj_id());
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
     * 统计结果详情总条数
     * @param search
     * @param aj
     * @return
     */
    public int getRowAllCount(String search, AjEntity aj) {
        StringBuffer sql = new StringBuffer();
        sql.append("select to_char(count(1)) NUM from(select z.* from(select * from (");
        sql.append("select t.*,row_number() over( partition by t.jyh order by t.id) su from zfbzzmx t where aj_id="+aj.getId()+") ");
        sql.append("where su=1) z left join(");
        sql.append("select f.yhid,f.dyxcsj from zfbzcxx f where aj_id="+aj.getId()+") c on c.dyxcsj = z.dyxcsj ");
        sql.append("where "+search+") z1");
        if(aj.getFilter()!=null){
            sql.append(" left join (select j.dyxcsj,min(sksj) sksj  from (select * from (select t.*,row_number() over(");
            sql.append("partition by t.jyh order by t.id) su from zfbjyjl t where aj_id="+aj.getId()+") where su=1) ");
            sql.append("j where upper(spmc) like '%"+aj.getFilter().toUpperCase()+"%' and j.jyzt='交易成功' group by j.dyxcsj) j1 on j1.dyxcsj=z1.dyxcsj where z1.dzsj>=j1.sksj");
        }
        List list = findBySQL(sql.toString());
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    /**
     * 统计结果详情分页数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param aj
     * @return
     */
    public List<ZfbZzmxEntity> getDoPageTjjgs(int currentPage, int pageSize, String search, AjEntity aj,boolean flag) {
        List<ZfbZzmxEntity> zzmxTjjgs = null;
        StringBuffer sql = new StringBuffer();
        if(flag){
            sql.append("SELECT * FROM ( ");
            sql.append("SELECT c.*, ROWNUM rn FROM (");
        }
        sql.append("select * from(select z.* from(select * from (");
        sql.append("select t.*,row_number() over( partition by t.jyh order by t.id) su from zfbzzmx t where aj_id="+aj.getId()+") ");
        sql.append("where su=1) z left join(");
        sql.append("select f.yhid,f.dyxcsj from zfbzcxx f where aj_id="+aj.getId()+") c on c.dyxcsj = z.dyxcsj ");
        sql.append("where "+search+") z1");
        if(aj.getFilter()!=null){
            sql.append(" left join (select j.dyxcsj,min(sksj) sksj  from (select * from (select t.*,row_number() over(");
            sql.append("partition by t.jyh order by t.id) su from zfbjyjl t where aj_id="+aj.getId()+") where su=1) ");
            sql.append("j where upper(spmc) like '%"+aj.getFilter().toUpperCase()+"%' and j.jyzt='交易成功' group by j.dyxcsj) j1 on j1.dyxcsj=z1.dyxcsj where z1.dzsj>=j1.sksj");
        }
        if(flag){
            sql.append(") c WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        }
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
     * 单表数据统计
     * @param id
     * @return
     */
    public List<ZfbZzmxTjjgssForm> selectZzmxTjjgss(long id) {
        List<ZfbZzmxTjjgssForm> tjjgsForms = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select fkfzfbzh,skfzfbzh,count(1) zzcs,sum(zzje) zzje from(select t.*,row_number() over(");
        sql.append("partition by t.jyh order by t.id) su from zfbzzmx t where aj_id=144) where su=1 group by fkfzfbzh,skfzfbzh");
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            tjjgsForms = session.createSQLQuery(sql.toString())
                    .addScalar("fkfzfbzh").addScalar("skfzfbzh")
                    .addScalar("zzcs", StandardBasicTypes.LONG)
                    .addScalar("zzje", StandardBasicTypes.BIG_DECIMAL)
                    .setResultTransformer(Transformers.aliasToBean(ZfbZzmxTjjgssForm.class)).list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return tjjgsForms;
    }
}
