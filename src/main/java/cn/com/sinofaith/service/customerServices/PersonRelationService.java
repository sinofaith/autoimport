package cn.com.sinofaith.service.customerServices;

import cn.com.sinofaith.bean.customerProBean.PersonRelationEntity;
import cn.com.sinofaith.dao.customerDao.PersonRelationDao;
import cn.com.sinofaith.form.customerForm.PersonRelationForm;
import cn.com.sinofaith.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PersonRelationService {
    @Autowired
    private PersonRelationDao prd;

    public String getSeach(String seachCode, String seachCondition){
        StringBuffer seach = new StringBuffer();

        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            seach.append(" and c." + seachCondition + " like " + "'%" + seachCode + "%'");
        }else{
            seach.append(" and ( 1=1 ) ");
        }
        return seach.toString();
    }

    public Page queryForPage(int currentPage, int pageSize, String seach,long ajid){
        Page page = new Page();
        List<PersonRelationForm> zzFs = new ArrayList<>();
        int allRow = prd.getAllRowCount(seach,ajid);
        if(allRow>0){
            List zzList = prd.getDoPage(seach,currentPage,pageSize,ajid);
            zzFs=mapToObj(zzList);
        }
        page.setPageSize(pageSize);
        page.setPageNo(currentPage);
        page.setTotalRecords(allRow);
        page.setList(zzFs);
        return page;
    }

    public List<PersonRelationForm> getByaj(long ajid,long id){
        List zzList = prd.findBySQL("select c.*,z.jzzje,z.czzje from person_relation c " +
                "left join ( select bp.khxm as name ,pbp.khxm as pname,sum(bt.jzzje) jzzje,sum(bt.czzje) czzje " +
                "  from bank_tjjgs bt left join bank_person bp on bt.jyzh = bp.yhkkh left join bank_person pbp on bt.dfzh = pbp.yhkkh " +
                "  where bt.aj_id = "+ajid+" group by bp.khxm,pbp.khxm " +
                ")z on c.name = z.name and c.pname = z.pname  " +
                " where c.aj_id = "+ajid +" and id ="+id);

        return mapToObj(zzList);
    }

    public List<PersonRelationForm> mapToObj(List list){
        List<PersonRelationForm> zzFs = new ArrayList<>();
        for(int i=0;i<list.size();i++) {
            PersonRelationForm zzf = new PersonRelationForm();
            Map map = (Map) list.get(i);
            zzf = zzf.mapToObj(map);
            zzf.setXh(i+1);
            zzFs.add(zzf);
        }
        return zzFs;
    }

    public List<PersonRelationForm> getByaj(long ajid){
        List zzList = prd.findBySQL("select c.*,z.jzzje,z.czzje from person_relation c " +
                "left join ( select bp.khxm as name ,pbp.khxm as pname,sum(bt.jzzje) jzzje,sum(bt.czzje) czzje " +
                "  from bank_tjjgs bt left join bank_person bp on bt.jyzh = bp.yhkkh left join bank_person pbp on bt.dfzh = pbp.yhkkh " +
                "  where bt.aj_id = "+ajid+" group by bp.khxm,pbp.khxm " +
                ")z on c.name = z.name and c.pname = z.pname  " +
                " where c.aj_id = "+ajid);

        return mapToObj(zzList);
    }

    public void addRelation(PersonRelationEntity pre){
        prd.save(pre);
    }
    public void editRelation(PersonRelationEntity pre){
        prd.update(pre);
    }
    public void removeRelation(long id){
        prd.delete("delete from PersonRelationEntity where id="+id);
    }
}
