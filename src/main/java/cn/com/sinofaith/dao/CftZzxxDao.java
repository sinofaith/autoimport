package cn.com.sinofaith.dao;

import cn.com.sinofaith.bean.CftZzxxEntity;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.*;


/**
 * Created by Me. on 2018/5/22
 * 财付通转账信息dao
 */
@Repository
public class CftZzxxDao extends BaseDao<CftZzxxEntity> {
    public int getAllRowCount(String seachCode){
        String sql = "select to_char(Count(1)) num from cft_zzxx c left join cft_person s on c.zh = s.zh " +
                "where 1=1 " + seachCode;
        List list = findBySQL(sql);
        Map map = (Map)list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

//    public List<CftZzxxEntity> getDoPage(String seachCode,int offset,int length){
//        List<CftZzxxEntity> result = doPage("from CftZzxxEntity  where 1=1" +seachCode +"order by id desc ",offset,length);
//        return result;
//    }
    public List getDoPage(String seachCode, int offset, int length){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM (SELECT  s.xm,c.* ");
        sql.append("FROM  cft_zzxx c left join cft_person s on c.zh=s.zh where 1=1 "+seachCode+") a ");
        sql.append("WHERE ROWNUM <= "+offset*length+") WHERE rn >= "+((offset-1)*length+1));

        return findBySQL(sql.toString());
    }

    public int saveZzxx(List<CftZzxxEntity> listZzxx){
        int i = 0;
        for (CftZzxxEntity zzxx:listZzxx){
            zzxx.setInserttime(TimeFormatUtil.getDate("/"));
            save(zzxx);
            i+=1;
        }
        return i;
    }

    public int insertZzxx(List<CftZzxxEntity> listZzxx,long aj){
        Map<String,CftZzxxEntity> map1 = new HashMap<>();
        List<CftZzxxEntity> all  = getAlla(aj);
        for(int i=0;i<all.size();i++){
            CftZzxxEntity x = all.get(i);
            map1.put((x.getZh()+x.getJydh()+x.getJysm()+x.getShmc()+
                    x.getFsf()+x.getJssj()+x.getJysj()+x.getJsf()+
                    x.getJsje().doubleValue()).replace("null",""),x);
        }
        all = null;
        Map<String,CftZzxxEntity> map = new HashMap<>();

        for(int i =0;i<listZzxx.size();i++){
            CftZzxxEntity x = listZzxx.get(i);
            map.put((x.getZh()+x.getJydh()+x.getJysm()+x.getShmc()+
                    x.getFsf()+x.getJssj()+x.getJysj()+x.getJsf()+
                    x.getJsje().doubleValue()).replace("null",""),x);
        }

        List<String> str = new ArrayList<>();
        for(String o : map.keySet()){
            if(map1.containsKey(o)){
                str.add(o);
            }
        }

        for(int i=0;i<str.size();i++){
            map.remove(str.get(i));
        }

        listZzxx = new ArrayList<>(map.values());

        Connection  con = DBUtil.getConn();
        String sql  = "insert into cft_zzxx(zh,jydh,jdlx,jylx,jyje,jysj,zhye,yhlx,jysm,shmc,fsf,fsje,jsf,jssj,jsje,inserttime,aj_id) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int a = 1;
        PreparedStatement pstm ;

        CftZzxxEntity zzxx = null;
        try{
            con.setAutoCommit(false);
            pstm =con.prepareStatement(sql);
            for(int i=0;i<listZzxx.size();i++){
                zzxx = listZzxx.get(i);
                pstm.setString(1,zzxx.getZh());
                pstm.setString(2,zzxx.getJydh());
                pstm.setString(3,zzxx.getJdlx());
                pstm.setString(4,zzxx.getJylx());
                pstm.setBigDecimal(5,zzxx.getJyje());
                pstm.setString(6,zzxx.getJysj());
                pstm.setBigDecimal(7,zzxx.getZhye());
                pstm.setString(8,zzxx.getYhlx());
                pstm.setString(9,zzxx.getJysm());
                pstm.setString(10,zzxx.getShmc());
                pstm.setString(11,zzxx.getFsf());
                pstm.setBigDecimal(12,zzxx.getFsje());
                pstm.setString(13,zzxx.getJsf());
                pstm.setString(14,zzxx.getJssj());
                pstm.setBigDecimal(15,zzxx.getJsje());
                pstm.setString(16,TimeFormatUtil.getDate("/"));
                pstm.setLong(17,aj);
                pstm.addBatch();
                if ((i+1) % 1000 == 0) {
                    pstm.executeBatch();
                    con.commit();
                }
            }
            pstm.executeBatch();
            con.commit();
            pstm.close();
//            con.close();
            listZzxx = null;
            map = null;
        }catch (Exception e){
            e.getMessage();
        }

        return a;
    }

    public List<CftZzxxEntity> getAlla(long ajid){
        List<CftZzxxEntity> listZzxx = find("from CftZzxxEntity where aj_id ="+ajid);
        return listZzxx;
    }

    public void deleteByAjid(long ajid){
        delete("delete from CftZzxxEntity where aj_id =" + ajid);
    }
}