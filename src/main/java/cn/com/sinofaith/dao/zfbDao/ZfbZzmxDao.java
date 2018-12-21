package cn.com.sinofaith.dao.zfbDao;

import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxTjjgForm;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxTjjgsForm;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/***
 * 支付宝转账明细持久层
 * @author zd
 * create by 2018.11.28
 */
@Repository
public class ZfbZzmxDao extends BaseDao<ZfbZzmxEntity> {
    /**
     * 批量插入数据
     * @param zzmxList
     * @param id
     * @return
     */
    public int insertZzmx(List<ZfbZzmxEntity> zzmxList, long id) {
        // 使用原生sql
        Connection conn = DBUtil.getConnection();
        String sql = "insert into ZfbZzmx(jyh,fkfzfbzh,skfzfbzh,skjgxx,dzsj,zzje,zzcpmc,jyfsd,txlsh,dyxcsj,insert_time,aj_id) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?)";
        int a = 0;
        PreparedStatement pstm = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<zzmxList.size();i++){
                ZfbZzmxEntity dlrz = zzmxList.get(i);
                pstm.setString(1,dlrz.getJyh());
                pstm.setString(2,dlrz.getFkfzfbzh());
                pstm.setString(3,dlrz.getSkfzfbzh());
                pstm.setString(4,dlrz.getSkjgxx());
                pstm.setString(5,dlrz.getDzsj());
                pstm.setDouble(6,dlrz.getZzje());
                pstm.setString(7,dlrz.getZzcpmc());
                pstm.setString(8,dlrz.getJyfsd());
                pstm.setString(9,dlrz.getTxlsh());
                pstm.setString(10,dlrz.getDyxcsj());
                pstm.setString(11, TimeFormatUtil.getDate("/"));
                pstm.setLong(12,id);
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
     * 条件筛选查询数据
     * @param search
     * @param id
     * @return
     */
    public List<ZfbZzmxTjjgsForm> selectFilterJyjlBySpmc(String search, long id) {
        List<ZfbZzmxTjjgsForm> zfbZzmxList = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select c.yhid,c.zhmc,a.fkfzfbzh,a.skfzfbzh,h.jydfxx,a.zzje from(");
        sql.append("select j3.* from(select * from (select t.*,row_number() over( ");
        sql.append("partition by t.jyh order by t.id) su from zfbzzmx t where aj_id="+id+") where su=1) j3 left join ");
        sql.append("(select j1.dyxcsj,min(j1.sksj) sksj from zfbjyjl j1 where j1.aj_id="+id+" "+search);
        sql.append(") j4 on j3.dyxcsj = j4.dyxcsj where j3.dzsj>=j4.sksj) a ");
        sql.append("left join(select * from (select t.*,row_number() over(");
        sql.append("partition by t.jyh order by t.id) su from zfbzhmx t where aj_id="+id+") where su=1) h on a.jyh=h.jyh ");
        sql.append("left join zfbzcxx c on c.dyxcsj = a.dyxcsj where c.aj_id="+id);
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            zfbZzmxList = session.createSQLQuery(sql.toString())
                    .addScalar("yhid").addScalar("zhmc")
                    .addScalar("fkfzfbzh").addScalar("skfzfbzh")
                    .addScalar("jydfxx").addScalar("zzje",StandardBasicTypes.BIG_DECIMAL)
                    .setResultTransformer(Transformers.aliasToBean(ZfbZzmxTjjgsForm.class)).list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return zfbZzmxList;
    }

    /**
     * 转账明细统计条数
     * @param seach
     * @param id
     * @return
     */
    /*public int getCountRow(String seach, long id) {
        StringBuffer sql = new StringBuffer();
        sql.append("select to_char(count(1)) NUM from(select t.dyxcsj,count(t.zzcpmc) zzcs,t.zzcpmc,sum(t.zzje) zzje from zfbzzmx t where t.aj_id="+id);
        sql.append(" group by t.dyxcsj,t.zzcpmc) t left join zfbzcxx z on z.dyxcsj=t.dyxcsj where z.aj_id="+id+seach);
        List list = findBySQL(sql.toString());
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }*/

    /**
     * 转账明细分页数据
     * @param currentPage
     * @param pageSize
     * @param seach
     * @param id
     * @return
     */
    /*public List<ZfbZzmxForm> queryForPage(int currentPage, int pageSize, String seach, long id) {
        StringBuffer sql = new StringBuffer();
        List<ZfbZzmxForm> zfbZzmxForms = null;
        sql.append("SELECT * FROM ( ");
        sql.append("SELECT c.*, ROWNUM rn FROM (");
        sql.append("select * from (");
        sql.append("select z.yhid,z.zhmc,z.zjlx,z.zjh,t.* from(select t.dyxcsj,count(t.zzcpmc) zzcs,t.zzcpmc,sum(t.zzje) zzje from zfbzzmx t where t.aj_id="+id);
        sql.append(" group by t.dyxcsj,t.zzcpmc) t left join zfbzcxx z on z.dyxcsj=t.dyxcsj where z.aj_id="+id);
        sql.append(") where (1=1)"+seach+" ) c ");
        sql.append(" WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        // 获取当前线程session
        Session session = getSession();
        try {
            // 开启事务
            Transaction tx = session.beginTransaction();
            zfbZzmxForms = session.createSQLQuery(sql.toString())
                    .addScalar("yhId")
                    .addScalar("zhmc")
                    .addScalar("zjlx")
                    .addScalar("zjh")
                    .addScalar("dyxcsj")
                    .addScalar("zzcs", StandardBasicTypes.LONG)
                    .addScalar("zzcpmc")
                    .addScalar("zzje", StandardBasicTypes.DOUBLE)
                    .setResultTransformer(Transformers.aliasToBean(ZfbZzmxForm.class)).list();
            // 关闭事务
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return zfbZzmxForms;
    }*/
}
