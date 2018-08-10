package cn.com.sinofaith.dao.bankDao;

import cn.com.sinofaith.bean.bankBean.BankZcxxEntity;
import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BankZzxxDao extends BaseDao<BankZzxxEntity> {
    public int getAllRowCount(String seachCode) {
        String sql = "select to_char(count(1)) num from bank_zzxx c " +
                "left join bank_person s on c.yhkkh = s.yhkkh  " +
                "left join bank_person d on c.dskh = d.yhkkh where 1=1 " + seachCode;
        List list = findBySQL(sql);
        Map map = (Map) list.get(0);
        return Integer.parseInt((String) map.get("NUM"));
    }

    public List getDoPage(String seach, int offset, int length) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM (SELECT  s.khxm jyxms,d.khxm dfxms,c.* ");
        sql.append("FROM  bank_zzxx c left join bank_person s on c.yhkkh=s.yhkkh ");
        sql.append("left join bank_person d on c.dskh=d.yhkkh  where 1=1 " + seach + ") a ");
        sql.append("WHERE ROWNUM <= " + offset * length + ") WHERE rn >= " + ((offset - 1) * length + 1));
        return findBySQL(sql.toString());
    }

    public List<BankZzxxEntity> getAlla(long ajid) {
        List<BankZzxxEntity> listZzxx = find("from BankZzxxEntity where aj_id =" + ajid);
        return listZzxx;
    }

    public int insertZzxx(List<BankZzxxEntity> list, long aj_id) {
        int max = (getAllRowCount(" and aj_id="+aj_id )+200000-1)/200000;
        String hql = "from BankZzxxEntity where aj_id = "+aj_id;
        Map<String,BankZzxxEntity> map1 = new HashMap<>();
        for(int l=0;l<max;l++) {
            List<BankZzxxEntity> all = doPage(hql, l+1, 200000);
            for (int i = 0; i < all.size(); i++) {
                BankZzxxEntity x = all.get(i);
                map1.put((x.getYhkkh() + x.getJysj() + x.getJyje() + x.getJyye() +
                        x.getSfbz() + x.getDskh() + x.getDsxm() + x.getDssfzh() +
                        x.getDskhh() + x.getZysm() + x.getBz() + x.getJyzjh() + x.getJyxm() + x.getJyfsd())
                        .replace("null", ""), null);
            }
        }
        Map<String,BankZzxxEntity> map = new HashMap<>();
        List<BankZzxxEntity> b = null;
        if(max!=0) {
            for (int j = 0; j < list.size(); j++) {
                BankZzxxEntity x = list.get(j);
                map.put((x.getYhkkh() + x.getJysj() + x.getJyje() + x.getJyye() +
                        x.getSfbz() + x.getDskh() + x.getDsxm() + x.getDssfzh() +
                        x.getDskhh() + x.getZysm() + x.getBz() + x.getJyzjh() + x.getJyxm() + x.getJyfsd())
                        .replace("null", ""), x);
            }
            List<String> str = new ArrayList<>();
            for (String o : map.keySet()) {
                if (map1.containsKey(o)) {
                    str.add(o);
                }
            }
            map1 = null;

            for (int i = 0; i < str.size(); i++) {
                map.remove(str.get(i));
            }
            b = new ArrayList<>(map.values());
        }
        else{
            b=list;
            list=null;
        }
        Connection con = DBUtil.getConnection();
        String sql = "insert into Bank_zzxx(yhkkh,jysj,jyje,jyye,sfbz,dskh,dsxm,dssfzh,zysm,jysfcg,aj_id,inserttime" +
                " ,yhkzh,dszh,dskhh,jywdmc,dsjyye,dsye,bz,jyzjh,jyfsd,jyxm) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int a = 0;
        PreparedStatement pstm = null;
        BankZzxxEntity zzxx = new BankZzxxEntity();


            try {
                con.setAutoCommit(false);
                pstm = con.prepareStatement(sql);
                for (int j = 0; j < b.size(); j++) {
                    zzxx = b.get(j);
                    pstm.setString(1, zzxx.getYhkkh());
                    pstm.setString(2, zzxx.getJysj());
                    pstm.setBigDecimal(3, zzxx.getJyje());
                    pstm.setBigDecimal(4, zzxx.getJyye());
                    pstm.setString(5, zzxx.getSfbz());
                    pstm.setString(6, zzxx.getDskh());
                    pstm.setString(7, zzxx.getDsxm());
                    pstm.setString(8, zzxx.getDssfzh());
                    pstm.setString(9, zzxx.getZysm());
                    pstm.setString(10, zzxx.getJysfcg());
                    pstm.setLong(11, aj_id);
                    pstm.setString(12, TimeFormatUtil.getDate("/"));
                    pstm.setString(13, zzxx.getYhkzh());
                    pstm.setString(14, zzxx.getDszh());
                    pstm.setString(15, zzxx.getDskhh());
                    pstm.setString(16, zzxx.getJywdmc());
                    pstm.setBigDecimal(17, zzxx.getDsjyye());
                    pstm.setBigDecimal(18, zzxx.getDsye());
                    pstm.setString(19, zzxx.getBz());
                    pstm.setString(20, zzxx.getJyzjh());
                    pstm.setString(21, zzxx.getJyfsd());
                    pstm.setString(22, zzxx.getJyxm());
                    pstm.addBatch();
                    a++;
                    if ((j + 1) % 50000 == 0) {
                        pstm.executeBatch();
                        pstm.clearParameters();
                        con.commit();
                    }
                }
                pstm.executeBatch();
                con.commit();
                DBUtil.closeStatement(pstm);
                DBUtil.closeConnection(con);
            } catch (Exception e) {
                e.getMessage();
                DBUtil.closeStatement(pstm);
                DBUtil.closeConnection(con);
            }
            list = null;
            map = null;
            return a;
        }
    }
