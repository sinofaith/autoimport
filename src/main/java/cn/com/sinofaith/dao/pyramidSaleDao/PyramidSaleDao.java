package cn.com.sinofaith.dao.pyramidSaleDao;

import cn.com.sinofaith.bean.pyramidSaleBean.PyramidSaleEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.DBUtil;
import oracle.sql.CLOB;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

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
     * 获取根节点数据
     * @param id
     * @return
     */
    public List<String> getRootNode(long id) {
        String sql = "select distinct(sponsorid) from pyramidsale t where not exists(select t1.psid from " +
                "pyramidsale t1 where t.sponsorid=t1.psid and aj_id="+id+") and aj_id="+id;
        List<String> rootNodes = null;
        // 获得当前session
        Session session = getSession();
        try {
            // 开启事务
            Transaction tx = session.beginTransaction();
            rootNodes = (List) session.createSQLQuery(sql).list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return rootNodes;
    }

    /**
     * 取出路径所需的数据
     * @param id
     * @return
     */
    public int selectPyramidSaleByAj_id(long id) {
        int rowNum = 0;
        // 获取根元素的节点
        String sql1 = "select distinct(sponsorid) from pyramidsale t where not exists(select t1.psid from " +
                "pyramidsale t1 where t.sponsorid=t1.psid and aj_id="+id+") and aj_id="+id;
        // 获得当前session
        Session session = getSession();
        try{
            // 开启事务
            Transaction tx = session.beginTransaction();
            List<String> sponsorid = (List) session.createSQLQuery(sql1).list();
            tx.commit();
            if(sponsorid.size()>1){
                List<PyramidSaleEntity> pList = new ArrayList<>();
                for (String s : sponsorid) {
                    PyramidSaleEntity ps = new PyramidSaleEntity();
                    ps.setPsId(s);
                    ps.setSponsorId("根节点");
//                    ps.setAj_id(id);
                    pList.add(ps);
                    //save(ps);
                }
                if(pList.size()>0)
                    insertPyramidSale(pList,id);
                //delete("delete from PyramidSaleEntity where aj_id="+id);
                //return psHierList;
            }

            if(sponsorid!=null){
                String temp = sponsorid.size()>1?"根节点":sponsorid.get(0);
                String level = sponsorid.size()>1?"level-1":"level";
                session = openSession();
                // 开启事务
                Transaction tx1 = session.beginTransaction();
                StringBuffer sql = new StringBuffer();
                sql.append("insert into ps_hierarchy h(h.psid,h.sponsorid,h.nick_name,h.tier,h.path,h.aj_id,h.directrefernum,h.containstier,h.directDrive) ");
                sql.append("select t.*,p.directReferNum,p1.containsTier,d.directDrive from ( ");
                sql.append("select psid,sponsorid,nick_name,"+level+" tier,sys_connect_by_path(psid,'/') path,aj_id from ( ");
                sql.append("select distinct t.psid,t.sponsorid,t.nick_name,t.aj_id from PYRAMIDSALE t where t.aj_id="+id+") y start with sponsorid = '"+temp+"' connect by prior psid=sponsorid) t ");
                sql.append("left join(select * from (select psid,count(1)-1 directReferNum from ( select * from ( ");
                sql.append("select t.*,row_number() over(partition by t.psid,t.sponsorid order by t.id) su from PYRAMIDSALE t where aj_id="+id+") where su=1 ) ");
                sql.append("connect by NOCYCLE psid=prior sponsorid group by psid ) where directReferNum > 0) p on t.psid=p.psid ");
                sql.append("left join(select h1.sponsorid,count(1) directDrive from pyramidsale h1 where aj_id="+id+" group by h1.sponsorid) d on d.sponsorid=t.psid ");
                sql.append("left join(select psid,max(level)-1 containsTier from(select b.psid,b.sponsorid from pyramidsale b where b.aj_id="+id+") c connect by NOCYCLE c.psid=prior c.sponsorid ");
                sql.append("group by c.psid) p1 on p1.psid=t.psid where t.tier>0");
                SQLQuery sqlQuery = session.createSQLQuery(sql.toString());
                rowNum = sqlQuery.executeUpdate();
//                List<PsHierarchyEntity> phList = session.createSQLQuery(sql.toString())
//                        .addScalar("psId")
//                        .addScalar("sponsorId")
//                        .addScalar("nick_name")
//                        .addScalar("path")
//                        .addScalar("tier",StandardBasicTypes.LONG)
//                        .addScalar("aj_id",StandardBasicTypes.LONG)
//                        .addScalar("directReferNum",StandardBasicTypes.LONG)
//                        .addScalar("containsTier",StandardBasicTypes.LONG)
//                        .addScalar("directDrive",StandardBasicTypes.LONG)
//                        .setResultTransformer(Transformers.aliasToBean(PsHierarchyEntity.class)).list();
//                rowNum = phList.size();
                // 提交事务
                tx1.commit();
                // 删除添加的数据
                if(sponsorid.size()>1){
                    delete("delete from PyramidSaleEntity where sponsorid='根节点' and aj_id="+id);
                }
                session.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return rowNum;
    }

    /**
     * 直推详情数据总和
     * @param seach
     * @return
     */
    public int getRowAllBySql(String seach) {
        String sql = "select to_char(count(1)) num from (select * from (select t.*,row_number() over(partition by t.psid,t.sponsorid order by t.id)su from PYRAMIDSALE t where "+seach+") where su=1)";
            List list = findBySQL(sql);
            Map map = (Map) list.get(0);
            return Integer.parseInt((String)map.get("NUM"));
    }

    /**
     * 下线会员数
     * @param seach
     * @param psId
     * @return
     */
    public Set<String> getRowAllBySqls(String seach, String psId) {
        Set<String> p = null;
        String sql = "select path from ps_hierarchy where psid="+psId + seach;
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            List path = session.createSQLQuery(sql).addScalar("path", StandardBasicTypes.STRING).list();
            String[] paths = ((String)path.get(0)).split("/");
            p = new HashSet<>();
            for (int i = 0; i < paths.length; i++) {
                p.add(paths[i]);
            }
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return p;
    }


    /**
     * 详情分页加载数据
     * @param currentPage
     * @param pageSize
     * @param seach
     * @param flag
     * @return
     */
    public List<PyramidSaleEntity> getDoPageBySql(int currentPage, int pageSize, String seach, boolean temp, Set<String> p, boolean flag) {
        List<PyramidSaleEntity> psList = null;
        StringBuffer sql = new StringBuffer();
        if(flag){
            sql.append("SELECT * FROM ( ");
            sql.append("SELECT c.*, ROWNUM rn FROM ( ");
        }
        if(temp){
            sql.append("select * from (select * from (");
            sql.append("select t.*,row_number() over(partition by t.psid,t.sponsorid order by t.id)su ");
            sql.append("from PYRAMIDSALE t where "+seach+") where su=1)");
        }else{
//            sql.append("select * from PYRAMIDSALE where ");
//            List<String> s = new ArrayList<>();
//            s.addAll(p);
//            int length = (int) Math.ceil((double)s.size() / 1000);
//            for(int i=0;i<length;i++){
//                String str = "";
//                if(i==length-1){
//                    for(int j=i*1000;j<s.size();j++){
//                        str += s.get(j)+",";
//                    }
//                    str = str.substring(0,str.length() - 1);
//                    sql.append("psid in ("+str+") "+ seach);
//                }else{
//                    for(int j=0;j<1000;j++){
//                        str += s.get(i*1000+j)+",";
//                    }
//                    str = str.substring(0,str.length() - 1);
//                    sql.append("psid in ("+str+") or ");
////                    break;
//                }
//            }
            sql.append("select * from (select t.*,row_number() over(");
            sql.append("partition by t.psid,t.sponsorid order by t.id)su from PYRAMIDSALE t where (1=1) "+seach+") where su=1 ");
            sql.append("start with sponsorid = '3' connect by prior psid=sponsorid");
        }
        if(flag){
            sql.append(") c WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        }
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

    /**
     * 总条数
     * @param search
     * @return
     */
    public int getRowAllCount(String search) {
        String sql = "select to_char(count(1)) NUM from pyramidSale where"+search;
        List list = findBySQL(sql);
        Map map = (Map)list.get(0);
        return Integer.parseInt((String) map.get("NUM"));
    }

    /**
     * 获取总数据
     * @param search
     * @return
     */
    public List<PyramidSaleEntity> getPyramidSaleAll(String search) {
        List<PyramidSaleEntity> psList = null;
        String sql = "select * from pyramidSale where"+search;
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            psList = session.createSQLQuery(sql)
                    .addEntity(PyramidSaleEntity.class).list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return psList;
    }
}
