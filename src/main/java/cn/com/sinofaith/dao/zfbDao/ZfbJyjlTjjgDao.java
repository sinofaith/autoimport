package cn.com.sinofaith.dao.zfbDao;

import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlTjjgsEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlTjjgForm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 支付宝交易记录统计结果持久层
 * @author zd
 * create by 2018.12.26
 */
@Repository
public class ZfbJyjlTjjgDao extends BaseDao<ZfbJyjlTjjgsEntity>{
    /**
     * 分页数据总条数
     * @param search
     * @param id
     * @return
     */
    public int getRowAlls(String search, long id) {
        StringBuffer sql = new StringBuffer();
        sql.append("select to_char(count(1)) NUM from (select dfzh,substr(dfmc,1,instr(dfmc,'(',1)-1) dfmc,dyxcsj,sum(fkzcs) ");
        sql.append("skzcs,sum(fkzje) skzje from(select * from zfbjyjl_tjjgs t where t.aj_id="+id+") t ");
        sql.append("inner join(select * from zfbzcxx c where c.aj_id="+id+") c on c.yhid=t.dfzh");
        sql.append(" group by dfzh,substr(dfmc,1,instr(dfmc,'(',1)-1),dyxcsj) where (1=1)"+search);
        List list = findBySQL(sql.toString());
        Map map = (Map)list.get(0);
        return Integer.parseInt((String) map.get("NUM"));
    }

    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param id
     * @return
     */
    public List<ZfbJyjlTjjgForm> getPage(int currentPage, int pageSize, String search, long id) {
        List<ZfbJyjlTjjgForm> tjjgForms = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ( ");
        sql.append("SELECT c.*, ROWNUM rn FROM (select * from(");
        sql.append("select dfzh,substr(dfmc,1,instr(dfmc,'(',1)-1) dfmc,dyxcsj,sum(fkzcs) skzcs,sum(fkzje) skzje from(select * from zfbjyjl_tjjgs t where t.aj_id="+id+") t ");
        sql.append("inner join(select * from zfbzcxx c where c.aj_id="+id+") c on c.yhid=t.dfzh");
        sql.append(" group by dfzh,substr(dfmc,1,instr(dfmc,'(',1)-1),dyxcsj) where (1=1)"+search);
        sql.append(") c WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            tjjgForms = session.createSQLQuery(sql.toString())
                    .addScalar("dfzh").addScalar("dfmc").addScalar("dyxcsj")
                    .addScalar("skzcs", StandardBasicTypes.LONG)
                    .addScalar("skzje", StandardBasicTypes.BIG_DECIMAL)
                    .setResultTransformer(Transformers.aliasToBean(ZfbJyjlTjjgForm.class)).list();
            tx.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.close();
        }
        return tjjgForms;
    }

    /**
     * 详情分页总条数
     * @param search
     * @return
     */
    public int getRowAllCount(String search) {
        StringBuffer sql = new StringBuffer();
        sql.append("select to_char(count(1)) NUM from(select * from (select t.*,row_number() over(");
        sql.append("partition by t.jyh order by t.id) su from zfbjyjl t where "+search+") where su=1)");
        List list = findBySQL(sql.toString());
        Map map = (Map)list.get(0);
        return Integer.parseInt((String) map.get("NUM"));
    }

    /**
     * 详情分页数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param flag
     * @return
     */
    public List<ZfbJyjlEntity> getDoPageTjjgs(int currentPage, int pageSize, String search, boolean flag) {
        List<ZfbJyjlEntity> tjjgs = null;
        StringBuffer sql = new StringBuffer();
        if(flag){
            sql.append("SELECT * FROM ( ");
            sql.append("SELECT c.*, ROWNUM rn FROM (");
        }
        sql.append("select * from(select * from (select t.*,row_number() over(");
        sql.append("partition by t.jyh order by t.id) su from zfbjyjl t where "+search+") where su=1)");
        if(flag){
            sql.append(") c WHERE ROWNUM <= "+currentPage * pageSize+") WHERE rn >= " + ((currentPage - 1) * pageSize + 1));
        }
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            tjjgs = session.createSQLQuery(sql.toString())
                    .addEntity(ZfbJyjlEntity.class).list();
            tx.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.close();
        }
        return tjjgs;
    }

    /**
     * 数据导出
     * @param search
     * @param id
     * @return
     */
    public List<ZfbJyjlTjjgForm> getJyjlTjjgAll(String search,long id) {
        List<ZfbJyjlTjjgForm> tjjgs = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select * from(select dfzh,substr(dfmc,1,instr(dfmc,'(',1)-1) dfmc,dyxcsj,sum(fkzcs) skzcs,sum(fkzje) skzje from(select * ");
        sql.append("from zfbjyjl_tjjgs t where t.aj_id="+id+") t inner join(select * from zfbzcxx c where c.aj_id="+id+") c on c.yhid=t.dfzh");
        sql.append(" group by dfzh,substr(dfmc,1,instr(dfmc,'(',1)-1),dyxcsj) where (1=1)"+search);
        Session session = getSession();
        try{
            Transaction tx = session.beginTransaction();
            tjjgs = session.createSQLQuery(sql.toString())
                    .addScalar("dfzh").addScalar("dfmc").addScalar("dyxcsj")
                    .addScalar("skzcs", StandardBasicTypes.LONG)
                    .addScalar("skzje", StandardBasicTypes.BIG_DECIMAL)
                    .setResultTransformer(Transformers.aliasToBean(ZfbJyjlTjjgForm.class)).list();
            tx.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.close();
        }
        return tjjgs;
    }
}
