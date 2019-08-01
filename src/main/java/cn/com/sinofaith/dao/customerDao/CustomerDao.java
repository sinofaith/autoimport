package cn.com.sinofaith.dao.customerDao;

import cn.com.sinofaith.bean.bankBean.BankCustomerEntity;
import cn.com.sinofaith.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CustomerDao extends BaseDao<BankCustomerEntity> {
    public int getAllRowCount(String seachCode) {
        String sql = "select to_char(count(c.zjh)) num from customerpro c " +
                " where 1=1 " + seachCode;
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        return Integer.parseInt((String) map.get("NUM"));
    }

    public int getAllRowCountAj(String seachCode) {
        String sql = "select to_char(count(distinct c.zjh)) as num from rel_zjh_hm c " +
                "left join customerpro cp on c.zjh = cp.zjh where 1=1" + seachCode;
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        return Integer.parseInt(map.get("NUM").toString());
    }

    public List getDoPage(String seach, int offset, int length) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM (SELECT  c.*,y.yhkh,w.wx,z.zfb,s.sjh,a.aj ");
        sql.append("FROM  customerpro c left join (select zjh,count(distinct hm) yhkh from rel_zjh_hm r where r.hmlx = 1 group by zjh) y on c.zjh = y.zjh " +
                "left join (select zjh,count(distinct hm) wx from rel_zjh_hm r where r.hmlx = 2 group by zjh) w on c.zjh = w.zjh " +
                "left join (select zjh,count(distinct hm) zfb from rel_zjh_hm r where r.hmlx = 3 group by zjh) z on c.zjh = z.zjh " +
                "left join (select zjh,count(distinct hm) sjh from rel_zjh_hm r where r.hmlx = 4 group by zjh) s on c.zjh = s.zjh " +
                "left join ( select zjh,count(distinct aj_id) aj from rel_zjh_hm r where r.aj_id != -1 group by zjh) a on c.zjh = a.zjh");
        sql.append(" where 1=1 " + seach + "order by a.aj desc nulls last, y.yhkh desc nulls last, w.wx desc nulls last,z.zfb desc nulls last,s.sjh desc nulls last) a ");
        sql.append("WHERE ROWNUM <= " + offset * length + ") WHERE rn >= " + ((offset - 1) * length + 1));
        return findBySQL(sql.toString());
    }
    public List getDoPageAj(String seach, int offset, int length) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM (select distinct c.zjh,cp.name,y.yhkh,w.wx,z.zfb,s.sjh,a.aj from rel_zjh_hm c ");
        sql.append("left join (select * from customerpro ) cp on c.zjh = cp.zjh " +
                "left join(select zjh,count(distinct hm) yhkh from rel_zjh_hm r where r.hmlx = 1 group by zjh) y on c.zjh = y.zjh " +
                "left join (select zjh,count(distinct hm) wx from rel_zjh_hm r where r.hmlx = 2 group by zjh) w on c.zjh = w.zjh " +
                "left join (select zjh,count(distinct hm) zfb from rel_zjh_hm r where r.hmlx = 3 group by zjh) z on c.zjh = z.zjh " +
                "left join (select zjh,count(distinct hm) sjh from rel_zjh_hm r where r.hmlx = 4 group by zjh) s on c.zjh = s.zjh " +
                "left join ( select zjh,count(distinct aj_id) aj from rel_zjh_hm r where r.aj_id != -1 group by zjh) a on c.zjh = a.zjh ");
        sql.append(" where 1=1 " + seach + "order by a.aj desc nulls last, y.yhkh desc nulls last, w.wx desc nulls last,z.zfb desc nulls last,s.sjh desc nulls last) a ");
        sql.append("WHERE ROWNUM <= " + offset * length + ") WHERE rn >= " + ((offset - 1) * length + 1));
        return findBySQL(sql.toString());
    }
}
