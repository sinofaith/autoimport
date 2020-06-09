package cn.com.sinofaith.dao;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.util.DBUtil;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class AJDao extends BaseDao<AjEntity>{
    public void insert(AjEntity aj){
         save(aj);
    }

    public List<AjEntity> findFilter(String aj,long userId){
//        List<AjEntity> listAj= find("from AjEntity where aj = '"+aj+"' and userId = "+userId);
        List list = findBySQL("select a.* from Aj a " +
                "left join rel_grand_aj r on a.id = r.ajid where a.aj='"+aj+"' " +
                " and (a.userid="+userId+" or r.userid="+userId+")");
        List<AjEntity> listAj = new ArrayList<>();
        if(list!=null) {
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                AjEntity aje = new AjEntity();
                aje.setId(new BigDecimal(map.get("ID").toString()).longValue());
                aje.setFlg(new BigDecimal(map.get("FLG").toString()).longValue());
                aje.setFilter((String)map.get("FILTER"));
                aje.setUserId(new BigDecimal(map.get("USERID").toString()).longValue());
                aje.setInserttime((String)map.get("INSERTTIME"));
                aje.setAj((String) map.get("AJ"));
                aje.setZjminsj((String)map.get("ZJMINSJ")==null ? "" : (String)map.get("ZJMINSJ"));
                aje.setZjmaxsj((String)map.get("ZJMAXSJ")==null ? "" : (String)map.get("ZJMAXSJ"));
                aje.setCftminsj(map.get("CFTMINSJ")==null ? "" : (String)map.get("CFTMINSJ"));
                aje.setCftmaxsj(map.get("CFTMAXSJ")==null ? "" : (String)map.get("CFTMAXSJ"));
                listAj.add(aje);
            }
        }
        return listAj;
    }

    public List<AjEntity> findByLike(String aj){
        List<AjEntity> listAj= find("from AjEntity where aj like '%,"+aj+"%' or aj like '%"+aj+",%'");
        return listAj;
    }

    public List<AjEntity> getAllAj(){
        return getAll();
    }

    public int getAllRowCount(String seachCode){
        String sql = "select to_char(count(id)) num from ( " +
                "select zc.*,b.bankNum,c.cftNum,d.wuliuNum,e.psNum,f.zfbNum from aj zc " +
                "left join (select aj_id,to_char(count(id)) as bankNum from bank_zzxx group by aj_id) b on zc.id = b.aj_id " +
                "left join (select aj_id,to_char(count(id)) as cftNum from cft_zzxx group by aj_id) c on zc.id = c.aj_id " +
                "left join (select aj_id,to_char(count(id)) as wuliuNum from wuliu group by aj_id) d on zc.id = d.aj_id " +
                "left join (select aj_id,to_char(count(id)) as psNum from pyramidSale group by aj_id) e on zc.id = e.aj_id " +
                "left join (select aj_id,to_char(count(id)) as zfbNum from ZFBZHMX group by aj_id) f on zc.id = f.aj_id) zc where 1=1 " + seachCode;
        List list = findBySQL(sql);
        Map map =(Map)list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List getDoPage (String seachCode,int offset,int length){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM (SELECT  zc.*,b.bankNum,c.cftNum,d.wuliuNum,e.psNum,f.zfbNum,cp.ry from aj zc ");
        sql.append(" left join (select aj_id,to_char(count(id)) as bankNum from bank_zzxx group by aj_id) b " +
                " on zc.id = b.aj_id  ");
        sql.append("  left join (select aj_id,to_char(count(id)) as cftNum from cft_zzxx group by aj_id) c " +
                " on zc.id = c.aj_id ");
        sql.append("  left join (select aj_id,to_char(count(id)) as wuliuNum from wuliu group by aj_id) d " +
                " on zc.id = d.aj_id ");
        sql.append("  left join (select aj_id,to_char(count(id)) as psNum from pyramidSale group by aj_id) e " +
                " on zc.id = e.aj_id ");
        sql.append("  left join (select aj_id,to_char(count(id)) as zfbNum from ZFBZHMX group by aj_id) f " +
                " on zc.id = f.aj_id ");
        sql.append(" left join (select aj_id,to_char(count(distinct zjh)) as ry from rel_zjh_hm group by aj_id) cp" +
                " on zc.id = cp.aj_id");
        sql.append(" where 1=1 "+seachCode+" order by zc.inserttime desc ) a ");
        sql.append("WHERE ROWNUM <= "+offset*length+") WHERE rn >= "+((offset-1)*length+1));

        return findBySQL(sql.toString());
    }

//    public List<AjEntity> getDoPage(String seachCode,int offset,int length){
//        List<AjEntity> result = doPage("from AjEntity  where 1=1" +seachCode +"order by inserttime desc ",offset,length);
//        return result;
//    }

    public void deleteById(long id){
        delete("delete from AjEntity where id = "+id);
    }

    public void deleteByAj(long ajid,String[] type){
        Connection con = DBUtil.getConnection();
        Statement st;
        try {
            st = con.createStatement();
            if(type.length==4){
                st.execute("delete aj where id ="+ajid);
                con.commit();
            }
            for(String a:type){
                if(a.equals("1")){
                    st.addBatch("delete cft_zcxx where aj_id="+ajid);
                    st.addBatch("delete cft_zzxx where aj_id="+ajid);
                    st.addBatch("delete cft_tjjg where aj_id="+ajid);
                    st.addBatch("delete cft_tjjgs where aj_id="+ajid);
                    st.addBatch("delete rel_zjh_hm  where (hmlx = 1 or hmlx = 2 or hmlx = 4) and  hmly =2 and aj_id="+ajid);
                }
                if(a.equals("2")){
                    st.addBatch("delete bank_zcxx where aj_id="+ajid);
                    st.addBatch("delete bank_zzxx where aj_id="+ajid);
                    st.addBatch("delete bank_tjjg where aj_id="+ajid);
                    st.addBatch("delete bank_tjjgs where aj_id="+ajid);
//                    st.addBatch("delete rel_customer_aj where aj_id="+ajid);
//                    st.addBatch("delete rel_zjh_hm  where  (hmlx = 1 and hmly =1) or (hmlx = 4 and hmly =4) and aj_id="+ajid);
//                    st.addBatch("delete PERSON_RELATION  where  aj_id="+ajid);
                }
                if(a.equals("3")){
                    st.addBatch("DELETE wuliu where aj_id="+ajid);
                    st.addBatch("DELETE wuliu_relation where aj_id="+ajid);
                    st.addBatch("DELETE wuliu_ship where aj_id="+ajid);
                    st.addBatch("DELETE wuliu_sj where aj_id="+ajid);
                }
                if(a.equals("4")){
                    st.addBatch("DELETE pyramidsale where aj_id="+ajid);
                    st.addBatch("DELETE ps_hierarchy where aj_id="+ajid);
                }
                if(a.equals("5")){
                    st.addBatch("DELETE zfbzcxx where aj_id="+ajid);
                    st.addBatch("DELETE zfbzhmx where aj_id="+ajid);
                    st.addBatch("DELETE zfbzzmx where aj_id="+ajid);
                    st.addBatch("DELETE zfbdlrz where aj_id="+ajid);
                    st.addBatch("DELETE zfbjyjl where aj_id="+ajid);
                    st.addBatch("DELETE zfbzhmx_tjjg where aj_id="+ajid);
                    st.addBatch("DELETE zfbzhmx_tjjgs where aj_id="+ajid);
                    st.addBatch("DELETE zfbzhmx_jczz where aj_id="+ajid);
                    st.addBatch("DELETE zfbzhmx_jylx where aj_id="+ajid);
                    st.addBatch("DELETE zfbzhmx_qxsj where aj_id="+ajid);
                    st.addBatch("DELETE zfbzzmx_tjjg where aj_id="+ajid);
                    st.addBatch("DELETE zfbzzmx_tjjgs where aj_id="+ajid);
                    st.addBatch("DELETE zfbjyjl_tjjgs where aj_id="+ajid);
                    st.addBatch("DELETE zfbjyjl_Sjdzs where aj_id="+ajid);
                    st.addBatch("delete rel_zjh_hm  where (hmlx = 1 or hmlx = 3 or hmlx=4) and hmly = 3 and aj_id="+ajid);
                    if(type.length< 5){
                        st.addBatch("UPDATE AJ a SET a.filter='' WHERE ID="+ajid);
                    }
                }
            }
            st.executeBatch();
            con.commit();

            DBUtil.closeStatement(st);
            DBUtil.closeConnection(con);
        }catch (SQLException e){
            e.getMessage();
        }
    }
}
