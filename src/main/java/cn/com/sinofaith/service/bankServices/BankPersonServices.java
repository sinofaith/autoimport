package cn.com.sinofaith.service.bankServices;

import cn.com.sinofaith.bean.bankBean.BankPersonEntity;
import cn.com.sinofaith.dao.bankDao.BankPersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BankPersonServices {
    @Autowired
    private BankPersonDao bpd;

    public int save(BankPersonEntity bpe){
        return bpd.insert(bpe);
    }


    public List<String> getByFilter(){
        List<BankPersonEntity> listbp = bpd.find("from BankPersonEntity where dsfzh=1");
        List<String> result = listbp.stream().map(BankPersonEntity::getYhkkh).collect(Collectors.toList());
        return  result;
    }

    public List<String> getName(String term,long ajid){
        String sql ="select a.khxm from bank_person a left join bank_tjjgs b on a.yhkkh=b.jyzh " +
                " where a.khxm like '%"+term+"%' and b.aj_id="+ajid;
        String sql1 ="select a.khxm from bank_person a left join bank_tjjgs b on a.yhkkh=b.dfzh " +
                " where a.khxm like '%"+term+"%' and b.aj_id="+ajid;
        List list = bpd.findBySQL(sql);
        list.addAll(bpd.findBySQL(sql1));
        List<String> result = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            Map map  = (Map) list.get(i);
            result.add(map.get("KHXM").toString());
        }
        return result.stream().distinct().collect(Collectors.toList());
    }

    public int  update(String yhkkh,long dsfzh){
        return bpd.updateBySql("update bank_person set dsfzh = "+dsfzh+" where yhkkh = '"+yhkkh+"'");
    }
}
