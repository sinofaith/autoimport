package cn.com.sinofaith.dao.pyramidSaleDao;

import cn.com.sinofaith.bean.pyramidSaleBean.PsHierarchyEntity;
import cn.com.sinofaith.bean.pyramidSaleBean.PyramidSaleEntity;
import cn.com.sinofaith.bean.wlBean.WuliuRelationEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.DBUtil;
import com.sun.org.apache.bcel.internal.generic.ReturnaddressType;
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

/**
 * 传销dao层
 * @author zd
 * create by 2018.10.28
 */
@Repository
public class PyramidSaleDao extends BaseDao<PyramidSaleEntity>{

    /**
     * 批量插入数据
     * @param psList
     * @param aj_id
     * @return
     */
    public int insertPyramidSale(List<PyramidSaleEntity> psList, long aj_id) {
        // 使用原生sql
        Connection conn = DBUtil.getConnection();
        String sql = "insert into pyramidSale(SPONSORID,MOBILE,TELPHONE,NICK_NAME,SEX,ADDRESS,SFZHM,BANKNAME,ACCOUNTHOLDER,ACCOUNTNUMBER,PSID,AJ_ID)" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?)";
        int a = 0;
        PreparedStatement pstm = null;
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<psList.size();i++){
                PyramidSaleEntity ps = psList.get(i);
                pstm.setString(1,ps.getSponsorId());
                pstm.setString(2,ps.getMobile());
                pstm.setString(3,ps.getTelphone());
                pstm.setString(4,ps.getNick_name());
                pstm.setString(5,ps.getSex());
                pstm.setString(6,ps.getAddress());
                pstm.setString(7,ps.getSfzhm());
                pstm.setString(8,ps.getBankName());
                pstm.setString(9,ps.getAccountHolder());
                pstm.setString(10,ps.getAccountNumber());
                pstm.setString(11,ps.getPsId());
                pstm.setLong(12,aj_id);
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
     * 取出路径所需的数据
     * @param id
     * @return
     */
    public List<PsHierarchyEntity> selectPyramidSaleByAj_id(long id) {
        List<PsHierarchyEntity> psHierList = null;
        // 获取根元素的节点
        String sql1 = "select sponsorid from pyramidsale t where t.sponsorid not in " +
                "(select t1.psid from pyramidsale t1 where aj_id="+id+") and aj_id="+id;
        // 获得当前session
        Session session = getSession();
        try{
            // 开启事务
            Transaction tx = session.beginTransaction();
            List<String> sponsorid = (List) session.createSQLQuery(sql1).list();
            tx.commit();
            if(sponsorid.size()>1){
                delete("delete from PyramidSaleEntity where aj_id="+id);
                return psHierList;
            }
            if(sponsorid!=null){
                session = getSession();
                // 开启事务
                Transaction tx1 = session.beginTransaction();
                StringBuffer sql = new StringBuffer();
                sql.append("select psid,sponsorid,level tier,");
                sql.append("sys_connect_by_path(psid,'/') path ");
                sql.append("from (select distinct t.psid,t.sponsorid from PYRAMIDSALE t where t.aj_id="+id+")");
                sql.append("start with sponsorid = '"+sponsorid.get(0)+"' connect by prior psid=sponsorid");
                psHierList = session.createSQLQuery(sql.toString())
                        .addScalar("psId")
                        .addScalar("sponsorId")
                        .addScalar("tier", StandardBasicTypes.LONG)
                        .addScalar("path").setResultTransformer(Transformers.aliasToBean(PsHierarchyEntity.class)).list();
                // 提交事务
                tx1.commit();
            }
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return psHierList;
    }

    /**
     * 详情数据总和
     * @param seach
     * @return
     */
    public int getRowAllBySql(String seach, boolean temp, String psId) {
        String sql = "";
        if(temp){
            sql = "select to_char(count(1)) num from (select * from (select t.*,row_number() over(partition by t.psid,t.sponsorid order by t.id)su from PYRAMIDSALE t where "+seach+") where su=1)";
        }else {
            sql = "select to_char(count(1)) num from (select * from (select t.*,row_number() over(partition by t.psid,t.sponsorid order by t.id)su " +
                    "from PYRAMIDSALE t where (1=1) "+seach+") where su=1)start with sponsorid = '"+psId+"' connect by prior psid=sponsorid";
        }
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    /**
     * 详情分页加载数据
     * @param currentPage
     * @param pageSize
     * @param seach
     * @return
     */
    public List<PyramidSaleEntity> getDoPageBySql(int currentPage, int pageSize, String seach, boolean temp, String psId) {
        List<PyramidSaleEntity> psList = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ( ");
        sql.append("SELECT c.*, ROWNUM rn FROM ( ");
        if(temp){
            sql.append("select * from (select * from (");
            sql.append("select t.*,row_number() over(partition by t.psid,t.sponsorid order by t.id)su ");
            sql.append("from PYRAMIDSALE t where "+seach+") where su=1)");
        }else{
            sql.append("select * from (select * from (select t.*,row_number() over(");
            sql.append("partition by t.psid,t.sponsorid order by t.id)su from PYRAMIDSALE t where (1=1) "+seach+") where su=1)");
            sql.append("start with sponsorid = '"+psId+"' connect by prior psid=sponsorid");
        }
        sql.append(") c ");
        sql.append(" WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            psList = session.createSQLQuery(sql.toString())
                    .addScalar("id", StandardBasicTypes.LONG)
                    .addScalar("psId")
                    .addScalar("sponsorId")
                    .addScalar("mobile")
                    .addScalar("nick_name")
                    .addScalar("sex")
                    .addScalar("address")
                    .addScalar("sfzhm")
                    .addScalar("bankName")
                    .addScalar("accountHolder")
                    .addScalar("accountNumber")
                    .setResultTransformer(Transformers.aliasToBean(PyramidSaleEntity.class)).list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return psList;
    }
}