package cn.com.sinofaith.dao.zfbDao;

import cn.com.sinofaith.bean.zfbBean.ZfbDlrzEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.form.zfbForm.ZfbDlrzForm;
import cn.com.sinofaith.form.zfbForm.ZfbDlrzForms;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.dsig.Transform;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/***
 * 支付宝登陆日志持久层
 * @author zd
 * create by 2018.11.28
 */
@Repository
public class ZfbDlrzDao extends BaseDao<ZfbDlrzEntity>{
    /**
     *TODO 批量插入数据
     * @param dlrzList
     * @param id
     * @return
     */
    public int insertDlrz(List<ZfbDlrzEntity> dlrzList, long id) {
        // 使用原生sql
        Connection conn = DBUtil.getConnection();
        String sql = "insert into ZfbDlrz(dlzh,zfbyhid,zhm,khdip,czfssj,dyxcsj,insert_time,aj_id) " +
                "values(?,?,?,?,?,?,?,?)";
        int a = 0;
        PreparedStatement pstm = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<dlrzList.size();i++){
                ZfbDlrzEntity dlrz = dlrzList.get(i);
                pstm.setString(1,dlrz.getDlzh());
                pstm.setString(2,dlrz.getZfbyhId());
                pstm.setString(3,dlrz.getZhm());
                pstm.setString(4,dlrz.getKhdIp());
                pstm.setString(5,dlrz.getCzfssj());
                pstm.setString(6,dlrz.getDyxcsj());
                pstm.setString(7, TimeFormatUtil.getDate("/"));
                pstm.setLong(8,id);
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
     * TODO 分页总条数
     * @param search
     * @param id
     * @return
     */
    public int getRowAlls(String search, long id) {
        StringBuffer sql = new StringBuffer();
        sql.append("select to_char(count(1)) NUM from(select zfbyhid,aj_id,count(zfbyhid) dlzcs from zfbdlrz group by zfbyhid,aj_id) d ");
        sql.append("left join zfbzcxx z on d.zfbyhid = z.yhid where d.aj_id="+id+" and z.aj_id="+id+search);
        List list = findBySQL(sql.toString());
        Map map = (Map) list.get(0);
        return Integer.parseInt(map.get("NUM").toString());
    }

    /**
     * TODO 分页数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param id
     * @return
     */
    public List<ZfbDlrzForm> queryForPage(int currentPage, int pageSize, String search, long id) {
        List<ZfbDlrzForm> zfbDlrzForms = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ( ");
        sql.append("SELECT c.*, ROWNUM rn FROM (");
        sql.append("select d.zfbyhid,z.zhmc,z.dlsj,z.zjlx,z.zjh,d.dlzcs from(select zfbyhid,aj_id,count(zfbyhid) dlzcs from zfbdlrz  where khdip <> '127.0.0.1' group by zfbyhid,aj_id) d ");
        sql.append("left join zfbzcxx z on d.zfbyhid = z.yhid where d.aj_id="+id+" and z.aj_id="+id+search);
        sql.append(" ) c WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        // 获取当前线程session
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            zfbDlrzForms = session.createSQLQuery(sql.toString())
                    .addScalar("zfbyhId")
                    .addScalar("zhmc")
                    .addScalar("dlsj")
                    .addScalar("zjlx")
                    .addScalar("zjh")
                    .addScalar("dlzcs", StandardBasicTypes.LONG)
                    .setResultTransformer(Transformers.aliasToBean(ZfbDlrzForm.class)).list();
            tx.commit();
        }catch (Exception e){
           e.printStackTrace();
           session.close();
        }
        return zfbDlrzForms;
    }

    /**
     * 获取时序图数据
     * @param zfbyhId
     * @param id
     * @return
     */
    public List<ZfbDlrzForms> getSequenceChart(String zfbyhId, long id) {
        List<ZfbDlrzForms> zfbDlrzForms = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select t.khdip,to_number(substr(trim(t.czfssj),12,2)) hour,count(1) NUM from ZFBDLRZ t where t.aj_id ="+id);
        sql.append(" and t.khdip <> '127.0.0.1' and t.zfbyhid='"+zfbyhId+"' group by t.zfbyhid,t.khdip,substr(trim(t.czfssj),12,2) order by hour");
        // 获取当前线程session
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            zfbDlrzForms = session.createSQLQuery(sql.toString())
                    .addScalar("khdip")
                    .addScalar("hour", StandardBasicTypes.LONG)
                    .addScalar("num", StandardBasicTypes.LONG)
                    .setResultTransformer(Transformers.aliasToBean(ZfbDlrzForms.class)).list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return zfbDlrzForms;
    }

    /**
     * 获取全部数据
     * @param search
     * @param id
     * @return
     */
    public List<ZfbDlrzForm> getZfbDlrzAll(String search, long id) {
        List<ZfbDlrzForm> zfbDlrzForms = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select d.zfbyhid,z.zhmc,z.dlsj,z.zjlx,z.zjh,d.dlzcs from(select zfbyhid,aj_id,count(zfbyhid) dlzcs from zfbdlrz  where khdip <> '127.0.0.1' group by zfbyhid,aj_id) d ");
        sql.append("left join zfbzcxx z on d.zfbyhid = z.yhid where d.aj_id="+id+" and z.aj_id="+id+search);
        // 获取当前线程session
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            zfbDlrzForms = session.createSQLQuery(sql.toString())
                    .addScalar("zfbyhId")
                    .addScalar("zhmc")
                    .addScalar("dlsj")
                    .addScalar("zjlx")
                    .addScalar("zjh")
                    .addScalar("dlzcs", StandardBasicTypes.LONG)
                    .setResultTransformer(Transformers.aliasToBean(ZfbDlrzForm.class)).list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return zfbDlrzForms;
    }
}
