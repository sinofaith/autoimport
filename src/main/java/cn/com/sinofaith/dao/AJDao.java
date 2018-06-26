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
        String sql = "select to_char(Count(1)) num from aj zc where 1=1 " + seachCode;
        List list = findBySQL(sql);
        Map map =(Map)list.get(0);
        return Integer.parseInt((String)map.get("NUM"));
    }

    public List<AjEntity> getDoPage(String seachCode,int offset,int length){
        List<AjEntity> result = doPage("from AjEntity  where 1=1" +seachCode +"order by inserttime desc ",offset,length);
        return result;
    }

    public void deleteById(long id){
        delete("delete from AjEntity where id = "+id);
    }
}
