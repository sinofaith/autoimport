package cn.com.sinofaith.service.customerServices;

import cn.com.sinofaith.bean.customerProBean.PersonCompanyEntity;
import cn.com.sinofaith.bean.customerProBean.PersonNumberEntity;
import cn.com.sinofaith.dao.customerDao.PersonNumberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonNumberService {
    @Autowired
    private PersonNumberDao pnd;

    public List<PersonNumberEntity> getByName(String name,long aj_id){
        List<PersonNumberEntity> reuslt = pnd.find(" from PersonNumberEntity " +
                "where name = '"+name+"' and aj_id = "+aj_id );
        return reuslt;
    }

    public List<PersonNumberEntity> getById(long id){
        List<PersonNumberEntity> result = pnd.find(" from PersonNumberEntity " +
                " where id="+id);
        return result;
    }

    public List<PersonNumberEntity> getByaj(long aj_id){
        List<PersonNumberEntity> result = pnd.find(" from PersonNumberEntity "+
        "where aj_id="+aj_id);
        return result;
    }

    public void add(PersonNumberEntity pne){
        pnd.save(pne);
    }
    public void edit(PersonNumberEntity pne) {pnd.update(pne);}
    public void removeNumbers(long id){
        pnd.delete(" delete from PersonNumberEntity where id="+id);
    }
}
