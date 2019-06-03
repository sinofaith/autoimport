package cn.com.sinofaith.dao.customerDao;

import cn.com.sinofaith.bean.bankBean.BankCustomerEntity;
import cn.com.sinofaith.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CustomerDao extends BaseDao<BankCustomerEntity> {
    public int getAllRowCount(String seachCode) {
        String sql = "select to_char(count(c.zjhm)) num from bank_customer c " +
                " where 1=1 " + seachCode;
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        return Integer.parseInt((String) map.get("NUM"));
    }

    public List getDoPage(String seach, int offset, int length) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM (SELECT  c.*,zc.yhkkh,cc.cftzh,zfc.zfbzh,aj.aj ");
        sql.append("FROM  bank_customer c left join (select zc.khzjh zjhm,count(distinct zc.yhkkh) yhkkh from bank_zcxx zc group by zc.khzjh) zc on c.zjhm = zc.zjhm " +
                "left join (select cc.sfzhm zjhm,count(distinct cc.zh) cftzh from cft_zcxx cc group by cc.sfzhm) cc on c.zjhm = cc.zjhm " +
                "left join (select zfc.zjh zjhm,count(distinct zfc.yhid) zfbzh from zfbzcxx zfc group by zfc.zjh) zfc on c.zjhm = zfc.zjhm " +
                "left join (select aj.zjhm zjhm,count(distinct aj.aj_id) aj from rel_customer_aj aj group by aj.zjhm) aj on c.zjhm = aj.zjhm ");
        sql.append(" where 1=1 " + seach + ") a ");
        sql.append("WHERE ROWNUM <= " + offset * length + ") WHERE rn >= " + ((offset - 1) * length + 1));
        return findBySQL(sql.toString());
    }
}
