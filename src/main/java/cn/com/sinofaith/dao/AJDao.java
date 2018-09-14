package cn.com.sinofaith.dao;

import cn.com.sinofaith.bean.AjEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AJDao extends BaseDao<AjEntity>{
    public void insert(AjEntity aj){
         save(aj);
    }

    public List<AjEntity> findFilter(String aj){
        List<AjEntity> listAj= find("from AjEntity where aj = '"+aj+"'");
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
                "select zc.*,b.bankNum,c.cftNum from aj zc " +
                "left join (select aj_id,to_char(count(id)) as bankNum from bank_zzxx group by aj_id) b on zc.id = b.aj_id " +
                "left join (select aj_id,to_char(count(id)) as cftNum from cft_zzxx group by aj_id) c on zc.id = c.aj_id) where 1=1 " + seachCode;
        List list = findBySQL(sql);
        Map map =(Map)list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List getDoPage (String seachCode,int offset,int length){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM (SELECT a.*, ROWNUM rn ");
        sql.append("FROM (SELECT  zc.*,b.bankNum,c.cftNum,d.wuliuNum from aj zc ");
        sql.append(" left join (select aj_id,to_char(count(id)) as bankNum from bank_zzxx group by aj_id) b " +
                " on zc.id = b.aj_id  ");
        sql.append("  left join (select aj_id,to_char(count(id)) as cftNum from cft_zzxx group by aj_id) c " +
                " on zc.id = c.aj_id ");
        sql.append("  left join (select aj_id,to_char(count(id)) as wuliuNum from wuliu group by aj_id) d " +
                " on zc.id = d.aj_id ");
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
}
