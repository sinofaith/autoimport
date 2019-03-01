package cn.com.sinofaith.dao.cftDao;

import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Me. on 2018/5/22
 * 财付通转账信息dao
 */
@Repository
public class CftZzxxDao extends BaseDao<CftZzxxEntity> {
    public int getAllRowCount(String seachCode){
        String sql = "select to_char(count(*)) num from cft_zzxx c left join cft_person s on c.zh = s.zh " +
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
        Connection  con = DBUtil.getConnection();
//        Connection  con = null;
        String sql  = "insert into cft_zzxx(zh,jydh,jdlx,jylx,jyje,jysj,zhye,yhlx,jysm,shmc,fsf,fsje,jsf,jssj,jsje,inserttime,aj_id) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int a = 0;
        PreparedStatement pstm ;

        CftZzxxEntity zzxx = null;
        try{
            con.setAutoCommit(false);
            pstm =con.prepareStatement(sql);
            String time = TimeFormatUtil.getDate("/");
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
                pstm.setString(16,time);
                pstm.setLong(17,aj);
                pstm.addBatch();
                if ((i+1) % 50000 == 0) {
                    pstm.executeBatch();
                    con.commit();
                }
            }
            pstm.executeBatch();
            con.commit();
            DBUtil.closeStatement(pstm);
            DBUtil.closeConnection(con);
            a=listZzxx.size();
//            con.close();
            listZzxx.clear();
//            newList.clear();

//            map = null;
        }catch (Exception e){
            e.getMessage();
        }

        return a;

    }

//    public int insertZzxx(List<CftZzxxEntity> listZzxx,long aj){
//        Map<String,CftZzxxEntity> map1 = new HashMap<>();
//        List<CftZzxxEntity> all  = getAlla(aj,"");
//        List<CftZzxxEntity> newList= new ArrayList<>();
//
//        Map<String, List<CftZzxxEntity>> groupBy = listZzxx.stream().collect(Collectors.groupingBy(CftZzxxEntity::getZh));


//        for(int i=0;i<all.size();i++){
//            CftZzxxEntity x = all.get(i);
//            map1.put(x.getBs(x),null);
//        }
//        all = null;
//        Map<String,CftZzxxEntity> map = new HashMap<>();
//        Iterator<CftZzxxEntity> iter = listZzxx.iterator();
//        for(int i =0;i<listZzxx.size();i++){
//            CftZzxxEntity x = listZzxx.get(i);
//            map.put(x.getBs(x),x);
//        }
//        if(all.size()>0) {
//            Set<CftZzxxEntity> set = new HashSet<>(all);
//            all.clear();
//            for (CftZzxxEntity x : listZzxx) {
//                if (!set.contains(x)) {
//                    newList.add(x);
//                }
//            }
//        }else{
//            newList = listZzxx;
//        }
//        while (iter.hasNext()){
//            CftZzxxEntity x = iter.next();
//            if(map1.containsKey(x.getBs(x))){
//                iter.remove();
//            }
//        }
//    List<String> str = new ArrayList<>();
//        for(String o : map.keySet()){
//            if(map1.containsKey(o)){
//                str.add(o);
//            }
//        }
//
//        for(int i=0;i<str.size();i++){
//            map.remove(str.get(i));
//        }
//        listZzxx = new ArrayList<>(map.values());

//        Connection  con = DBUtil.getConnection();
//        Connection  con = null;
//        String sql  = "insert into cft_zzxx(zh,jydh,jdlx,jylx,jyje,jysj,zhye,yhlx,jysm,shmc,fsf,fsje,jsf,jssj,jsje,inserttime,aj_id) " +
//                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//        int a = 0;
//        PreparedStatement pstm ;
//
//        CftZzxxEntity zzxx = null;
//        try{
//            con.setAutoCommit(false);
//            pstm =con.prepareStatement(sql);
//            String time = TimeFormatUtil.getDate("/");
//            for(int i=0;i<newList.size();i++){
//                zzxx = newList.get(i);
//                pstm.setString(1,zzxx.getZh());
//                pstm.setString(2,zzxx.getJydh());
//                pstm.setString(3,zzxx.getJdlx());
//                pstm.setString(4,zzxx.getJylx());
//                pstm.setBigDecimal(5,zzxx.getJyje());
//                pstm.setString(6,zzxx.getJysj());
//                pstm.setBigDecimal(7,zzxx.getZhye());
//                pstm.setString(8,zzxx.getYhlx());
//                pstm.setString(9,zzxx.getJysm());
//                pstm.setString(10,zzxx.getShmc());
//                pstm.setString(11,zzxx.getFsf());
//                pstm.setBigDecimal(12,zzxx.getFsje());
//                pstm.setString(13,zzxx.getJsf());
//                pstm.setString(14,zzxx.getJssj());
//                pstm.setBigDecimal(15,zzxx.getJsje());
//                pstm.setString(16,time);
//                pstm.setLong(17,aj);
//                pstm.addBatch();
//                if ((i+1) % 50000 == 0) {
//                    pstm.executeBatch();
//                    con.commit();
//                }
//            }
//            pstm.executeBatch();
//            con.commit();
//            DBUtil.closeStatement(pstm);
//            DBUtil.closeConnection(con);
//            a=newList.size();
////            con.close();
//            listZzxx.clear();
//            newList.clear();
//
////            map = null;
//        }catch (Exception e){
//            e.getMessage();
//        }
//
//        return a;
//    }

    public List<CftZzxxEntity> getAlla(long ajid,String seach){
        List<CftZzxxEntity> listZzxx = find("from CftZzxxEntity where aj_id ="+ajid+seach);
        return listZzxx;
    }

    public Set<String> getGroupByZh(long aj){
        Set<String> result = new HashSet<>();
        String sql = " select zh from cft_zzxx where aj_id = "+aj+" group by zh";
        List zh = findBySQL(sql);
        for(int i =0;i<zh.size();i++){
            Map map = (Map)zh.get(i);
            result.add((String) map.get("ZH"));
        }
        return new HashSet<>(result);
    }

    public void deleteByAjid(long ajid){
        delete("delete from CftZzxxEntity where aj_id =" + ajid);
    }

//    public List findByZhlx(String jyzh,String jylx,String type,String ajid){
//        StringBuffer sql = new StringBuffer();
//            sql.append("SELECT * ");
//            sql.append("FROM (SELECT a.*, ROWNUM rn ");
//            sql.append("FROM (SELECT  s.xm,c.* ");
//            sql.append("FROM  cft_zzxx c left join cft_person s on c.zh=s.zh where 1=1 "+seachCode+") a ");
//            sql.append("WHERE ROWNUM <= "+offset*length+") WHERE rn >= "+((offset-1)*length+1));
//        List list = findBySQL(sql.toString());
//        return list;
//    }
}