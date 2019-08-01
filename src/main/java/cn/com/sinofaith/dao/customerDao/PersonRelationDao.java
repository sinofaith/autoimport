package cn.com.sinofaith.dao.customerDao;

import cn.com.sinofaith.bean.customerProBean.PersonRelationEntity;
import cn.com.sinofaith.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class PersonRelationDao extends BaseDao<PersonRelationEntity>{
    public int getAllRowCount(String seachCode,long ajid) {
        String sql = "select to_char(count(1)) num from ( " +
                "select c.*,z.jzzje,z.czzje from person_relation c " +
                "left join (" +
                "  select bp.khxm as name ,pbp.khxm as pname,sum(bt.jzzje) jzzje,sum(bt.czzje) czzje " +
                "  from bank_tjjgs bt " +
                "  left join bank_person bp on bt.jyzh = bp.yhkkh" +
                "  left join bank_person pbp on bt.dfzh = pbp.yhkkh" +
                "  where bt.aj_id ="+ajid+
                "  group by bp.khxm,pbp.khxm " +
                ")z on c.name = z.name and c.pname = z.pname  " +
                " where  1=1 and  c.aj_id ="+ajid+ seachCode+
                " ) ";
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        return Integer.parseInt((String) map.get("NUM"));
    }

    public List getDoPage(String seach,int currentPage,int pageSize,long ajid){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM ( select c.*,z.jzzje,z.czzje from person_relation c  ");
        sql.append(" left join ( select bp.khxm as name ,pbp.khxm as pname,sum(bt.jzzje) jzzje,sum(bt.czzje) czzje ");
        sql.append(" from bank_tjjgs bt left join bank_person bp on bt.jyzh = bp.yhkkh left join bank_person pbp on bt.dfzh = pbp.yhkkh ");
        sql.append(" where bt.aj_id = "+ajid+" group by bp.khxm,pbp.khxm )z ");
        sql.append("  on c.name = z.name and c.pname = z.pname  where c.aj_id ="+ajid+seach+" ) a ");
        sql.append("WHERE ROWNUM <= "+currentPage*pageSize+") WHERE rn >= "+((currentPage-1)*pageSize+1));
        return findBySQL(sql.toString());
    }
}
