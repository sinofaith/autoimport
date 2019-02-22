package cn.com.sinofaith.dao.zfbDao;

import cn.com.sinofaith.bean.zfbBean.*;
import cn.com.sinofaith.dao.BaseDao;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 支付宝账户明细清洗数据持久层
 * @author zd
 * create by 2019.01.30
 */
@Repository
public class ZfbZhmxQxsjDao extends BaseDao<ZfbZhmxQxsjEntity> {

    /**
     * 插入清洗数据
     * @param id
     */
    public int insertZhmxQxsj(long id) {
        int rowNum = 0;
        StringBuffer sql = new StringBuffer();
        sql.append("insert into zfbzhmx_qxsj(jyh,shddh,jycjsj,fksj,zjxgsj,jylyd,lx,jyzfbzh,jymc,dszfbzh,dsmc,xfmc,je,sz,jyzt,bz,aj_id) ");
        sql.append("select distinct jyh,shddh,h.jycjsj,h.fksj,h.zjxgsj,h.jylyd,h.lx,");
        sql.append("substr(h.yhxx,1,16) jyzfbzh,replace(replace(substr(h.yhxx,17),'('),')') jymc,");
        sql.append("substr(h.jydfxx,1,16) dszfbzh,replace(replace(substr(h.jydfxx,17),'('),')') dsmc,");
        sql.append("h.xfmc,h.je,h.sz,h.jyzt,h.bz,"+id+" aj_id from(select * from (select t.*,row_number() over(");
        sql.append("partition by t.jyh order by t.id) su from zfbzhmx t where aj_id=" + id + ") h where su=1 ");
        sql.append("and (h.jyzt like '%成功%' or h.jyzt like '%SUCCESS%' or h.jyzt='完成')) h ");
        sql.append("where h.jydfxx like '2088%'");
        StringBuffer sql1 = new StringBuffer();
        sql1.append("insert into zfbzhmx_qxsj(jyh,shddh,jycjsj,fksj,zjxgsj,jylyd,lx,jyzfbzh,jymc,dszfbzh,dsmc,xfmc,je,sz,jyzt,bz,aj_id) ");
        sql1.append("select distinct jyh,shddh,h.jycjsj,h.fksj,h.zjxgsj,h.jylyd,h.lx,");
        sql1.append("substr(h.yhxx,1,16) jyzfbzh,replace(replace(substr(h.yhxx,17),'('),')') jymc,");
        sql1.append("h.jydfxx dszfbzh,h.jydfxx dsmc,h.xfmc,h.je,h.sz,h.jyzt,h.bz,"+id+" aj_id ");
        sql1.append("from(select * from (select t.*,row_number() over(");
        sql1.append("partition by t.jyh order by t.id) su from zfbzhmx t where aj_id=" + id + ") h where su=1 ");
        sql1.append("and (h.jyzt like '%成功%' or h.jyzt like '%SUCCESS%' or h.jyzt='完成')) h ");
        sql1.append("where h.jydfxx not like '2088%'");
        // 获得当前线程session]
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            SQLQuery sqlQuery = session.createSQLQuery(sql.toString());
            rowNum = sqlQuery.executeUpdate();
            sqlQuery = session.createSQLQuery(sql1.toString());
            rowNum += sqlQuery.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.close();
        }
        return rowNum;
    }

    /**
     * 账户明细点对点统计(支付宝与支付宝交易)
     * @return
     */
    public List<ZfbZhmxTjjgEntity> selectTjjgList(long id) {
        List<ZfbZhmxTjjgEntity> tjjgList = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select jyzfbzh,jymc,dszfbzh,dsmc,count(*) jyzcs,");
        sql.append("sum(case sz when '支出' then 1 else 0 end) czzcs,");
        sql.append("sum(case sz when '支出' then je else 0 end) czzje,");
        sql.append("sum(case sz when '收入' then 1 else 0 end) jzzcs,");
        sql.append("sum(case sz when '收入' then je else 0 end) jzzje ");
        sql.append("from (select distinct h.jyh,h.zjxgsj,h.jyzfbzh,h.jymc,");
        sql.append("h.dszfbzh,h.dsmc,to_number(h.je) je,h.sz from zfbzhmx_qxsj h ");
        sql.append("where h.aj_id="+id+" and h.dszfbzh not like '%尾号%') group by jyzfbzh,jymc,dszfbzh,dsmc ");
        sql.append("order by czzje desc,jzzje desc");
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            tjjgList = session.createSQLQuery(sql.toString())
                    .addScalar("jyzfbzh").addScalar("jymc")
                    .addScalar("dszfbzh").addScalar("dsmc")
                    .addScalar("jyzcs", StandardBasicTypes.LONG)
                    .addScalar("czzcs", StandardBasicTypes.LONG)
                    .addScalar("czzje", StandardBasicTypes.DOUBLE)
                    .addScalar("jzzcs", StandardBasicTypes.LONG)
                    .addScalar("jzzje", StandardBasicTypes.DOUBLE)
                    .setResultTransformer(Transformers.aliasToBean(ZfbZhmxTjjgEntity.class)).list();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.close();
        }
        return tjjgList;
    }

    /**
     * 账户明细账户与银行账户点对点统计
     * @return
     */
    public List<ZfbZhmxTjjgsEntity> selectTjjgsList(long id) {
        List<ZfbZhmxTjjgsEntity> tjjgsList = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select jyzfbzh,jymc,dszfbzh,xfmc,count(*) jyzcs,");
        sql.append("sum(case sz when '支出' then 1 else 0 end) czzcs,");
        sql.append("sum(case sz when '支出' then je else 0 end) czzje,");
        sql.append("sum(case sz when '收入' then 1 else 0 end) jzzcs,");
        sql.append("sum(case sz when '收入' then je else 0 end) jzzje ");
        sql.append("from (select distinct h.jyh,h.zjxgsj,h.jyzfbzh,jymc,");
        sql.append("h.dszfbzh,to_number(h.je) je,h.sz,h.xfmc from zfbzhmx_qxsj h ");
        sql.append("where h.aj_id="+id+" and h.dszfbzh like '%尾号%') group by jyzfbzh,dszfbzh,jymc,xfmc ");
        sql.append("order by czzje desc,jzzje desc");
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            tjjgsList = session.createSQLQuery(sql.toString())
                    .addScalar("jyzfbzh").addScalar("jymc")
                    .addScalar("dszfbzh").addScalar("xfmc")
                    .addScalar("jyzcs", StandardBasicTypes.LONG)
                    .addScalar("czzcs", StandardBasicTypes.LONG)
                    .addScalar("czzje", StandardBasicTypes.DOUBLE)
                    .addScalar("jzzcs", StandardBasicTypes.LONG)
                    .addScalar("jzzje", StandardBasicTypes.DOUBLE)
                    .setResultTransformer(Transformers.aliasToBean(ZfbZhmxTjjgsEntity.class)).list();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.close();
        }
        return tjjgsList;
    }

    /**
     * 进出总账数据查询
     * @return
     */
    public List<ZfbZhmxJczzEntity> selectJczzList(long id) {
        List<ZfbZhmxJczzEntity> jczzList = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select z1.*,z2.zhmc jymc from(select jyzfbzh,count(*) jyzcs, ");
        sql.append("sum(case sz when '支出' then je else 0 end) czzje, ");
        sql.append("sum(case sz when '支出' then 1 else 0 end) czzcs, ");
        sql.append("sum(case sz when '收入' then je else 0 end) jzzje, ");
        sql.append("sum(case sz when '收入' then 1 else 0 end) jzzcs ");
        sql.append("from (select distinct h.jyh,h.zjxgsj,h.jyzfbzh,h.dszfbzh, ");
        sql.append("to_number(h.je) je,h.sz from zfbzhmx_qxsj h where h.aj_id="+id+") group by jyzfbzh ");
        sql.append("order by czzje desc,jzzje desc) z1 left join(select * from( ");
        sql.append("select t.*,row_number() over(partition by t.yhid order by t.id) ");
        sql.append("su from zfbzcxx t where t.aj_id="+id+") where su=1)z2 on z1.jyzfbzh=z2.yhid");
        // 获取当前线程session
        Session session = getSession();
        try{
            // 开启事务
            Transaction tx = session.beginTransaction();
            jczzList = session.createSQLQuery(sql.toString())
                    .addScalar("jyzfbzh")
                    .addScalar("jyzcs", StandardBasicTypes.LONG)
                    .addScalar("czzcs", StandardBasicTypes.LONG)
                    .addScalar("czzje", StandardBasicTypes.DOUBLE)
                    .addScalar("jzzcs", StandardBasicTypes.LONG)
                    .addScalar("jzzje", StandardBasicTypes.DOUBLE)
                    .addScalar("jymc")
                    .setResultTransformer(Transformers.aliasToBean(ZfbZhmxJczzEntity.class)).list();
            // 提交事务
            tx.commit();
        }catch(Exception e){
            session.close();
            e.printStackTrace();
        }
        return jczzList;
    }

    /**
     * 按交易类型进出总账
     * @return
     */
    public List<ZfbZhmxJylxEntity> selectJylxList(long id) {
        List<ZfbZhmxJylxEntity> jylxList = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select z1.*,z2.zhmc jymc from(select jyzfbzh,xfmc,count(*) jyzcs, ");
        sql.append("sum(case sz when '支出' then 1 else 0 end) czzcs, ");
        sql.append("sum(case sz when '支出' then je else 0 end) czzje, ");
        sql.append("sum(case sz when '收入' then 1 else 0 end) jzzcs， ");
        sql.append("sum(case sz when '收入' then je else 0 end) jzzje ");
        sql.append("from (select distinct h.jyh,h.zjxgsj,h.jyzfbzh,h.dszfbzh,to_number(h.je) je, ");
        sql.append("h.sz,h.xfmc from zfbzhmx_qxsj h where h.aj_id="+id+" and h.dszfbzh like '%尾号%')group by ");
        sql.append("jyzfbzh,xfmc order by czzje desc,jzzje desc) z1 left join(select * from( ");
        sql.append("select t.*,row_number() over(partition by t.yhid order by t.id) ");
        sql.append("su from zfbzcxx t where t.aj_id="+id+") where su=1)z2 on z1.jyzfbzh=z2.yhid ");
        // 获取当前线程session
        Session session = getSession();
        try{
            // 开启事务
            Transaction tx = session.beginTransaction();
            jylxList = session.createSQLQuery(sql.toString())
                    .addScalar("jyzfbzh")
                    .addScalar("xfmc")
                    .addScalar("jyzcs", StandardBasicTypes.LONG)
                    .addScalar("czzcs", StandardBasicTypes.LONG)
                    .addScalar("czzje", StandardBasicTypes.DOUBLE)
                    .addScalar("jzzcs", StandardBasicTypes.LONG)
                    .addScalar("jzzje", StandardBasicTypes.DOUBLE)
                    .addScalar("jymc")
                    .setResultTransformer(Transformers.aliasToBean(ZfbZhmxJylxEntity.class)).list();
            // 提交事务
            tx.commit();
        }catch(Exception e){
            session.close();
            e.printStackTrace();
        }
        return jylxList;
    }

    /**
     * 删除账户明细清洗数据
     */
    public void delAll() {
        delete("delete from ZfbZhmxQxsjEntity");
    }
}
