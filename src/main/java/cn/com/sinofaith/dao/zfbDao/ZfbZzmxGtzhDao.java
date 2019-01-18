package cn.com.sinofaith.dao.zfbDao;

import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgsEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxGtzhForm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 支付宝转账明细共同账户持久层
 * @author zd
 * create by 2018.12.15
 */
@Repository
public class ZfbZzmxGtzhDao extends BaseDao<ZfbZzmxTjjgsEntity>{
    /**
     * 共同账户总条数
     * @param search
     * @param id
     * @return
     */
    public int getCountRow(String search, long id) {
        StringBuffer sql = new StringBuffer();
        sql.append("select to_char(count(1)) NUM from(select t3.zfbmc,t3.zfbzh,t3.dfzh,t4.gthys,t3.jyzcs,t3.fkzcs," +
                "t3.fkzje,t3.skzcs,t3.skzje from zfbzzmx_tjjgs t3 right join(");
        sql.append("select t2.dfzh,count(1) gthys from zfbzzmx_tjjgs t2 where t2.dfzh not in");
        sql.append("(select distinct zfbzh from zfbzzmx_tjjgs where aj_id="+id+") and t2.aj_id="+id);
        sql.append(" group by t2.dfzh having(count(1)>=2)) t4 on t4.dfzh=t3.dfzh where t3.aj_id="+
                    id+" and t3.dfzh<>'转账到银行卡') "+search);
        List list = findBySQL(sql.toString());
        Map map = (Map) list.get(0);
        return Integer.parseInt((String) map.get("NUM"));
    }

    /**
     *  共同账户分页数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param id
     * @return
     */
    public List<ZfbZzmxGtzhForm> queryForPage(int currentPage, int pageSize, String search, long id) {
        List<ZfbZzmxGtzhForm> zzmxGtzhForms = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ( ");
        sql.append("SELECT c.*, ROWNUM rn FROM (");
        sql.append("select * from(");
        sql.append("select t3.zfbmc,t3.zfbzh,t3.dfzh,t4.gthys,t3.jyzcs,t3.fkzcs,t3.fkzje,t3.skzcs,t3.skzje from");
        sql.append(" zfbzzmx_tjjgs t3 right join(select t2.dfzh,count(1) gthys from zfbzzmx_tjjgs t2 where t2.dfzh not in");
        sql.append("(select distinct zfbzh from zfbzzmx_tjjgs where aj_id="+id+") and t2.aj_id="+id);
        sql.append("group by t2.dfzh having(count(1)>=2)) t4 on t4.dfzh=t3.dfzh where t3.aj_id="+id+" and t3.dfzh<>'转账到银行卡') "+search);
        sql.append(")c WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            zzmxGtzhForms = session.createSQLQuery(sql.toString())
                    .addScalar("zfbmc").addScalar("zfbzh")
                    .addScalar("dfzh").addScalar("gthys", StandardBasicTypes.LONG)
                    .addScalar("jyzcs", StandardBasicTypes.LONG)
                    .addScalar("fkzcs", StandardBasicTypes.LONG)
                    .addScalar("fkzje", StandardBasicTypes.BIG_DECIMAL)
                    .addScalar("skzcs", StandardBasicTypes.LONG)
                    .addScalar("skzje", StandardBasicTypes.BIG_DECIMAL)
                    .setResultTransformer(Transformers.aliasToBean(ZfbZzmxGtzhForm.class)).list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return zzmxGtzhForms;
    }

    /**
     * 数据导出
     * @param search
     * @param id
     * @return
     */
    public List<ZfbZzmxGtzhForm> getZfbZzmxGtzhAll(String search, long id) {
        List<ZfbZzmxGtzhForm> zzmxGtzhForms = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select * from(");
        sql.append("select t3.zfbmc,t3.zfbzh,t3.dfzh,t4.gthys,t3.jyzcs,t3.fkzcs,t3.fkzje,t3.skzcs,t3.skzje from");
        sql.append(" zfbzzmx_tjjgs t3 right join(select t2.dfzh,count(1) gthys from zfbzzmx_tjjgs t2 where t2.dfzh not in");
        sql.append("(select distinct zfbzh from zfbzzmx_tjjgs where aj_id="+id+") and t2.aj_id="+id);
        sql.append("group by t2.dfzh having(count(1)>=2)) t4 on t4.dfzh=t3.dfzh where t3.aj_id="+id+") "+search);
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            zzmxGtzhForms = session.createSQLQuery(sql.toString())
                    .addScalar("zfbmc").addScalar("zfbzh")
                    .addScalar("dfzh").addScalar("gthys", StandardBasicTypes.LONG)
                    .addScalar("jyzcs", StandardBasicTypes.LONG)
                    .addScalar("fkzcs", StandardBasicTypes.LONG)
                    .addScalar("fkzje", StandardBasicTypes.BIG_DECIMAL)
                    .addScalar("skzcs", StandardBasicTypes.LONG)
                    .addScalar("skzje", StandardBasicTypes.BIG_DECIMAL)
                    .setResultTransformer(Transformers.aliasToBean(ZfbZzmxGtzhForm.class)).list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return zzmxGtzhForms;
    }
}
