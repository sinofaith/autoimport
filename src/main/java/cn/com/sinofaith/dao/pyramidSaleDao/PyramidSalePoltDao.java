package cn.com.sinofaith.dao.pyramidSaleDao;


import cn.com.sinofaith.bean.pyramidSaleBean.PsHierarchyEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.form.psForm.PsPoltForm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 传销层级图表持久层
 * @author zd
 * create by 2018.11.09
 */
@Repository
public class PyramidSalePoltDao extends BaseDao<PsHierarchyEntity> {
    /**
     * 从数据库中获取数据
     * @param id
     * @return
     */
    public List<PsPoltForm> getTreeData(long id,String psId) {
        if(psId==null){
            String sql1 = "select psid from pyramidsale t where not exists(select t1.psid from " +
                    "pyramidsale t1 where t.sponsorid=t1.psid and aj_id="+id+") and aj_id="+id;
            // 获得当前session
            Session session = getSession();
            try{
                // 开启事务
                Transaction tx = session.beginTransaction();
                List<String> psid = (List) session.createSQLQuery(sql1).list();
                psId = psid.get(0);
                tx.commit();
            } catch (Exception e){
                e.printStackTrace();
                session.close();
            }
        }
        List<PsPoltForm> psPoltForms = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select p.*,h.directrefernum value from (select p.psid,p.sponsorid,p.nick_name name,level tier from (");
        sql.append("select * from (select t.*,row_number() over(partition by t.psid,t.sponsorid order by t.id) su from PYRAMIDSALE t where t.aj_id="+id+")) p ");
        sql.append("start with psid = '"+psId+"' connect by prior psid=sponsorid) p ");
        sql.append("left join (select * from ps_hierarchy h where aj_id="+id+") h on p.psid=h.psid and p.sponsorid=h.sponsorid order by h.tier");
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            psPoltForms = session.createSQLQuery(sql.toString())
                    .addScalar("psid")
                    .addScalar("sponsorid")
                    .addScalar("name")
                    .addScalar("tier", StandardBasicTypes.LONG)
                    .addScalar("value",StandardBasicTypes.LONG)
                    .setResultTransformer(Transformers.aliasToBean(PsPoltForm.class))
                    .list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return psPoltForms;
    }

    public List<PsPoltForm> geTreeNode(String psid) {
        String sql = "";
        if(psid==null){
            sql = "select t.psid,t.sponsorid,t.nick_name name from PYRAMIDSALE t where t.sponsorid is null and aj_id=281";
        }else{
            sql = "select t.psid,t.sponsorid,t.nick_name name from PYRAMIDSALE t where t.sponsorid="+psid+" and aj_id=281";
        }
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        List<PsPoltForm> list = session.createSQLQuery(sql)
                .addScalar("psid")
                .addScalar("sponsorid")
                .addScalar("name")
                .setResultTransformer(Transformers.aliasToBean(PsPoltForm.class)).list();
        tx.commit();
        return list;
    }
}
