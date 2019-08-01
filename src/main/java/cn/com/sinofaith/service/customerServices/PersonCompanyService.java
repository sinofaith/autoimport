package cn.com.sinofaith.service.customerServices;

import cn.com.sinofaith.bean.customerProBean.PersonCompanyEntity;
import cn.com.sinofaith.dao.customerDao.PersonCompanyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonCompanyService {
    @Autowired
    private PersonCompanyDao pcd;

    public List<PersonCompanyEntity> getByName(String name,long aj_id){
        List<PersonCompanyEntity> reuslt = pcd.find(" from PersonCompanyEntity " +
                "where name = '"+name+"' and aj_id = "+ aj_id);
        return reuslt;
    }

    public List<PersonCompanyEntity> getById(long id){
        List<PersonCompanyEntity> result = pcd.find(" from PersonCompanyEntity " +
                " where id="+id);
        return result;
    }

    public List<PersonCompanyEntity> getByaj(long aj_id){
        List<PersonCompanyEntity> result = pcd.find(" from PersonCompanyEntity " +
        " where aj_id="+aj_id);
        return result;
    }

    public void add(PersonCompanyEntity pce){
        pcd.save(pce);
    }
    public void edit(PersonCompanyEntity pce){pcd.update(pce);}
    public void removeCompany(long id){
        pcd.delete(" delete from PersonCompanyEntity where id="+id);
    }
}
