package cn.com.sinofaith.dao.bankDao;

import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import cn.com.sinofaith.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BankZzxxDao extends BaseDao<BankZzxxEntity>{
    public int getAllRowCount(String seachCode){
        String sql = "select to_char(count(1)) num from bank_zzxx c " +
                "left join bank_zcxx s on c.jyzkh = s.yhkzh " +
                "left join bank_zcxx d on c.dszh = d.yhkzh where 1=1 " + seachCode;
        List list = findBySQL(sql);
        Map map = (Map)list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List getDoPage(String seach,int offset,int length){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM (SELECT  s.khxm jyxm,d.khxm dfxm,c.* ");
        sql.append("FROM  bank_zzxx c left join bank_zcxx s on c.jyzkh=s.yhkzh ");
        sql.append("left join bank_zcxx d on c.dszh=d.yhkzh  where 1=1 "+seach+") a ");
        sql.append("WHERE ROWNUM <= "+offset*length+") WHERE rn >= "+((offset-1)*length+1));
        return findBySQL(sql.toString());
    }
}
