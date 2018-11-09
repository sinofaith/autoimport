package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.cftDao.CftZzxxDao;
import cn.com.sinofaith.form.AjForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.util.DBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AjServices {
    @Autowired
    private AJDao ad;
    @Autowired
    private CftZzxxDao zzd;

    public void save(AjEntity aj){
         ad.save(aj);
    }

    public void deleteById(long id){
         ad.deleteById(id);
    }

    public List<AjEntity> findByName(String aj){
        return ad.findFilter(aj);
    }

    public List<AjEntity> findByLike(String aj){
        return ad.findByLike(aj);
    }

    public List<AjEntity> getAll(){
        return ad.getAllAj();
    }

    public Page  queryForPage(int currentPage,int pageSize,String seach){
        Page page = new Page();
        int allRow = ad.getAllRowCount(seach);
        List<AjForm> ajlist = new ArrayList<>();
        if(allRow>0){
            AjForm aj = new AjForm();
            List zzList = ad.getDoPage(seach,currentPage,pageSize);
            int xh = 1;
            for(int i=0;i<zzList.size();i++) {
                Map map = (Map) zzList.get(i);
                aj = aj.mapToForm(map);
                aj.setXh(xh+(currentPage-1)*pageSize);
                ajlist.add(aj);
                xh++;
            }
        }
        page.setList(ajlist);
        page.setPageNo(currentPage);
        page.setPageSize(pageSize);
        page.setTotalRecords(allRow);
        return page;
    }
    public void deleteByAj(long ajid){
        Connection con = DBUtil.getConnection();
        String sql = "delete  aj where id ="+ajid;
        Statement st;
        try {
            st = con.createStatement();
            st.execute(sql);
            con.commit();
            st.addBatch("delete  cft_zcxx where aj_id="+ajid);
            st.addBatch("delete  cft_zzxx where aj_id="+ajid);
            st.addBatch("delete  cft_tjjg where aj_id="+ajid);
            st.addBatch("delete  cft_tjjgs where aj_id="+ajid);
//            st.addBatch("delete bank_person where yhkzh='"+ajid+"'");
            st.addBatch("delete bank_zcxx where aj_id="+ajid);
            st.addBatch("delete bank_zzxx where aj_id="+ajid);
            st.addBatch("delete bank_tjjg where aj_id="+ajid);
            st.addBatch("delete bank_tjjgs where aj_id="+ajid);
            st.addBatch("DELETE wuliu where aj_id="+ajid);
            st.addBatch("DELETE wuliu_relation where aj_id="+ajid);
            st.addBatch("DELETE wuliu_ship where aj_id="+ajid);
            st.addBatch("DELETE wuliu_sj where aj_id="+ajid);
            st.addBatch("DELETE pyramidsale where aj_id="+ajid);
            st.addBatch("DELETE ps_hierarchy where aj_id="+ajid);
            st.executeBatch();
            con.commit();

            DBUtil.closeStatement(st);
            DBUtil.closeConnection(con);
        }catch (SQLException e){
            e.getMessage();
        }
    }

    public List<CftZzxxEntity> getCftList(AjEntity aj){
        List<CftZzxxEntity> listZz = new ArrayList<>();
        String [] ajm = aj.getAj().split(",");
        long [] ajid = new long[ajm.length];
        String seach = "";
        if(aj.getFlg()==1){
            seach = "and shmc not like '%红包%'";
        }
        for(int i=0;i<ajm.length;i++){
            ajid[i] = findByName(ajm[i]).get(0).getId();
            listZz.addAll(zzd.getAlla(ajid[i],seach));
        }
        return listZz;
    }

    public void updateAj(AjEntity aj){
        ad.update(aj);
    }
}
