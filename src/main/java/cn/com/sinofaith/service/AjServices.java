package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlSjdzsEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlTjjgsEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgsEntity;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.cftDao.CftZzxxDao;
import cn.com.sinofaith.dao.zfbDao.*;
import cn.com.sinofaith.form.AjForm;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlTjjgsForm;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxTjjgsForm;
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
    @Autowired
    private ZfbZzmxDao zfbZzmxDao;
    @Autowired
    private ZfbZzmxTjjgsDao zfbZzmxTjjgsDao;
    @Autowired
    private ZfbJyjlDao zfbJyjlDao;
    @Autowired
    private ZfbJyjlTjjgsDao zfbJyjlTjjgsDao;
    @Autowired
    private ZfbJyjlSjdzsDao zfbJyjlSjdzsDao;

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
    public void deleteByAj(long ajid,String[] type){
        Connection con = DBUtil.getConnection();
        Statement st;
        try {
            st = con.createStatement();
            if(type.length==5){
                st.execute("delete aj where id ="+ajid);
                con.commit();
            }
            for(String a:type){
                if(a.equals("1")){
                    st.addBatch("delete  cft_zcxx where aj_id="+ajid);
                    st.addBatch("delete  cft_zzxx where aj_id="+ajid);
                    st.addBatch("delete  cft_tjjg where aj_id="+ajid);
                    st.addBatch("delete  cft_tjjgs where aj_id="+ajid);
                }
                if(a.equals("2")){
                    st.addBatch("delete bank_zcxx where aj_id="+ajid);
                    st.addBatch("delete bank_zzxx where aj_id="+ajid);
                    st.addBatch("delete bank_tjjg where aj_id="+ajid);
                    st.addBatch("delete bank_tjjgs where aj_id="+ajid);
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
                    if(type.length<5){
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

    public int getZfbZzmxList(AjEntity aje,String filterInput) {
        String search = "";
        if(filterInput!=""){
            search += " and upper(j1.spmc) like '%"+filterInput.toUpperCase()+"%'";
        }
        // 转账明细条件筛选
        List<ZfbZzmxTjjgsForm> zfbZzmxList = zfbZzmxDao.selectFilterJyjlBySpmc(search, aje.getId());
        List<ZfbZzmxTjjgsEntity> tjjgsList = ZfbZzmxTjjgsEntity.FormToList(zfbZzmxList, aje.getId());
        // 交易记录条件筛选
        List<ZfbJyjlTjjgsForm> zfbJyjlList = zfbJyjlDao.selectFilterJyjlBySpmc(search, aje.getId());
        List<ZfbJyjlTjjgsEntity> jyjlTjjgsList = ZfbJyjlTjjgsEntity.FormToList(zfbJyjlList, aje.getId());
        // 交易地址条件筛选
        List<ZfbJyjlSjdzsEntity> sjdzsList = zfbJyjlSjdzsDao.selectJyjlSjdzs(aje.getId(), filterInput);

        // 修改数据
        if(tjjgsList.size()>0||jyjlTjjgsList.size()>0||sjdzsList.size()>0){
            zfbZzmxTjjgsDao.delAll(aje.getId());
            zfbZzmxTjjgsDao.insertZzmxTjjgs(tjjgsList);
            zfbJyjlTjjgsDao.delAll(aje.getId());
            zfbJyjlTjjgsDao.insertJyjlTjjgs(jyjlTjjgsList);
            zfbJyjlSjdzsDao.delAll(aje.getId());
            zfbJyjlSjdzsDao.insertJyjlSjdzs(sjdzsList, aje.getId());
            // 修改筛选
            aje.setFilter(filterInput);
            updateAj(aje);
        }
        return tjjgsList.size()>0||jyjlTjjgsList.size()>0||sjdzsList.size()>0?1:0;
    }
}
