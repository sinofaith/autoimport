package cn.com.sinofaith.dao.pyramidSaleDao;

import cn.com.sinofaith.bean.pyramidSaleBean.PsHierarchyEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.form.psForm.PsHierarchyForm;
import cn.com.sinofaith.util.DBUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 路径表持久层
 * @author zd
 * create by 2018.11.1
 */
@Repository
public class PsHierarchyDao extends BaseDao<PsHierarchyEntity>{
    /**
     * 插入数据
     * @param pshierList
     * @param id
     */
    public int insertpsHierarchy(List<PsHierarchyEntity> pshierList, long id) {
        // 使用原生sql
        Connection conn = DBUtil.getConnection();
        String sql = "insert into ps_hierarchy(PSID,SPONSORID,TIER,PATH,DIRECTREFERNUM,AJ_ID)" +
                "values(?,?,?,?,?,?)";
        int a = 0;
        PreparedStatement pstm = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<pshierList.size();i++){
                PsHierarchyEntity ps = pshierList.get(i);
                pstm.setString(1,ps.getPsId());
                pstm.setString(2,ps.getSponsorId());
                pstm.setLong(3,ps.getTier());
                pstm.setString(4,ps.getPath());
                pstm.setLong(5,ps.getDirectReferNum());
                pstm.setLong(6,id);
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
        return a;
    }

    /**
     * 获取数据条目
     * @param seach
     * @param id
     * @return
     */
    public int getRowPsHierarchy(String seach, long id) {
        StringBuffer sql = new StringBuffer();
        sql.append("select to_char(count(1)) num from (");
        sql.append(" select p1.id,p1.psid,p1.sponsorid,p1.nick_name psAccountholder,p1.sfzhm,p1.mobile,p1.address,p1.accountnumber,h.tier,h.path,h2.sum directDrive,c.containsTier,c.directrefernum from (");
        sql.append(" select p.id,p.psid,p.sponsorid,p.nick_name,p.sfzhm,p.mobile,p.address,p.accountnumber from(select * from (select t.*,row_number() over(partition by t.psid,t.sponsorid order by t.id)");
        sql.append(" su from PYRAMIDSALE t where aj_id="+id+") where su=1)p) p1");
        sql.append(" left join (select * from ps_hierarchy where aj_id="+id+") h on p1.psid=h.psid");
        sql.append(" left join (select h1.sponsorid,count(1) sum from ps_hierarchy h1 where aj_id="+id+" group by h1.sponsorid) h2 on p1.psid=h2.sponsorid");
        sql.append(" left join (select psid,directrefernum,(select max(tier) from ps_hierarchy where aj_id="+id+")-tier containsTier from ps_hierarchy p where aj_id="+id+" and exists");
        sql.append(" (select sponsorid from ps_hierarchy h where p.psid=h.sponsorid and aj_id="+id+")) c on c.psid=p1.psid) where (1=1) "+seach);
        List list = findBySQL(sql.toString());
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    /**
     * 获取分页数据
     * @param currentPage
     * @param pageSize
     * @param seach
     * @param id
     * @return
     */
    public List<PsHierarchyForm> queryForPage(int currentPage, int pageSize, String seach, long id) {
        StringBuffer sql = new StringBuffer();
        List<PsHierarchyForm> psHierarchyForms = null;
        sql.append("SELECT * FROM ( ");
        sql.append("SELECT c.*, ROWNUM rn FROM ( ");//p1.psid,p1.nick_name psNick_name,p1.accountholder psAccountholder,p3.*,h.tier,h.path,h2.sum directDrive,c.containsTier,c.directrefernum
        sql.append("select * from (");
        sql.append(" select p1.id,p1.psid,p1.sponsorid,p1.nick_name psAccountholder,p1.sfzhm,p1.mobile,p1.address,p1.accountnumber,h.tier,h.path,h2.sum directDrive,c.containsTier,c.directrefernum from (");
        sql.append(" select p.id,p.psid,p.sponsorid,p.nick_name,p.sfzhm,p.mobile,p.address,p.accountnumber from(select * from (select t.*,row_number() over(partition by t.psid,t.sponsorid order by t.id)");
        sql.append(" su from PYRAMIDSALE t where aj_id="+id+") where su=1)p) p1");
        sql.append(" left join (select * from ps_hierarchy where aj_id="+id+") h on p1.psid=h.psid");
        sql.append(" left join (select h1.sponsorid,count(1) sum from ps_hierarchy h1 where aj_id="+id+" group by h1.sponsorid) h2 on p1.psid=h2.sponsorid");
        sql.append(" left join (select psid,directrefernum,(select max(tier) from ps_hierarchy where aj_id="+id+")-tier containsTier from ps_hierarchy p where aj_id="+id+" and exists");
        sql.append(" (select sponsorid from ps_hierarchy h where p.psid=h.sponsorid and aj_id="+id+")) c on c.psid=p1.psid) where (1=1) "+seach);
        sql.append(") c ");
        sql.append(" WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        // 获取当前线程session
        Session session = getSession();
        try {
            // 开启事务
            Transaction tx = session.beginTransaction();
            psHierarchyForms = session.createSQLQuery(sql.toString())
                    .addScalar("psId")
                    .addScalar("sponsorid")
                    .addScalar("psAccountholder")
                    .addScalar("sfzhm")
                    .addScalar("mobile")
                    .addScalar("address")
                    .addScalar("accountnumber")
                    .addScalar("tier", StandardBasicTypes.LONG)
                    .addScalar("path")
                    .addScalar("directDrive", StandardBasicTypes.LONG)
                    .addScalar("containsTier", StandardBasicTypes.LONG)
                    .addScalar("directReferNum", StandardBasicTypes.LONG)
                    .setResultTransformer(Transformers.aliasToBean(PsHierarchyForm.class)).list();
            // 关闭事务
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return psHierarchyForms;
    }

    /**
     * 更新数据
     * @param id
     */
    public void updateHierarchy(long id) {
        String sql2 = "select psid from ps_hierarchy t where not exists(select t1.psid from " +
                "ps_hierarchy t1 where t.sponsorid=t1.psid and aj_id="+id+") and aj_id="+id;
        List<PsHierarchyEntity> psh = new ArrayList<>();
        // 获得当前session
        Session session = getSession();
        try {
            // 开启事务
            Transaction tx = session.beginTransaction();
            List<String> psid = (List) session.createSQLQuery(sql2).list();
            // 包含层
            for(String  pd : psid){
                StringBuffer sql3 = new StringBuffer();
                sql3.append("select psid,p1.tier-p.tier containsTier from (");
                sql3.append("select psid,level tier from ps_hierarchy p where p.aj_id = "+id+" and CONNECT_BY_ISLEAF!=1 ");
                sql3.append("start with p.psid = '"+pd+"' connect by prior p.psid =  p.sponsorid ");
                sql3.append(") p,(select max(level) tier from ps_hierarchy p where p.aj_id = "+id+" start with p.psid = '"+pd+"' ");
                sql3.append("connect by prior p.psid =  p.sponsorid) p1 ");
                List<PsHierarchyEntity> ps = session.createSQLQuery(sql3.toString())
                        .addScalar("psId")
                        .addScalar("containsTier", StandardBasicTypes.LONG)
                        .setResultTransformer(Transformers.aliasToBean(PsHierarchyEntity.class)).list();
                if(ps.size()!=0){
                    psh.addAll(ps);
                }
            }
            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        if(psh!=null){
            updateHierarchy(psh,id);
        }
    }

    /**
     * update
     */
    public static int updateHierarchy(List<PsHierarchyEntity> pshierList, long id) {
        // 使用原生sql
        Connection conn = DBUtil.getConnection();
            String sql = "update ps_hierarchy set containsTier=? where aj_id=? and psid=?";
        int a = 0;
        PreparedStatement pstm = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<pshierList.size();i++){
                PsHierarchyEntity ps = pshierList.get(i);
                pstm.setLong(1,ps.getContainsTier());
                pstm.setLong(2,id);
                pstm.setString(3,ps.getPsId());
                pstm.addBatch();
                a++;
                // 有50000条添加一次
                if ((i + 1) % 5000 == 0) {
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
     * 查询出所有会员的包含会员数
     * @param id
     * @return
     */
    /*public List<PsHierarchyEntity> selectPsHierarchyByAj_Id(long id) {
        List<PsHierarchyEntity> psHierarchyList = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select * from (select psid,count(1)-1 directReferNum from (");
        sql.append(" select * from (select t.*,row_number() over(partition by t.psid,t.sponsorid order by t.id) su from PYRAMIDSALE t where aj_id="+id+") where su=1");
        sql.append(" ) connect by psid= prior sponsorid group by psid ) where directReferNum > 0");
        // 获取当前线程session
        Session session = getSession();
        try {
            // 开启事务
            Transaction tx = session.beginTransaction();
            psHierarchyList = session.createSQLQuery(sql.toString())
                    .addScalar("psId")
                    .addScalar("directReferNum", StandardBasicTypes.LONG)
                    .setResultTransformer(Transformers.aliasToBean(PsHierarchyEntity.class)).list();
            // 提交事务
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return psHierarchyList;
    }*/
}
