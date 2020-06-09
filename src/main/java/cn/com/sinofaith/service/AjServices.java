package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.UserEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.bean.zfbBean.*;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.cftDao.CftZzxxDao;
import cn.com.sinofaith.dao.zfbDao.*;
import cn.com.sinofaith.form.AjForm;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlTjjgsForm;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxTjjgsForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.ExcelReader;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AjServices {
    @Autowired
    public AJDao ad;
    @Autowired
    private CftZzxxDao zzd;
    @Autowired
    private ZfbZzmxDao zfbZzmxDao;
    @Autowired
    private ZfbZcxxDao zfbZcxxDao;
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

    public List<String> getBrandName(String brandName){
        List<AjEntity> l = ad.find("from AjEntity where pinpai like '%"+brandName+"%'");
        List<String> result = new ArrayList<>();
        if(l.size()>0){
            result = new ArrayList<>(l.stream().map(AjEntity::getPinpai).collect(Collectors.toSet()));
        }
        return result;
    }

    public void deleteById(long id){
         ad.deleteById(id);
    }

    public List<AjEntity> findByName(String aj,long userId){
        return ad.findFilter(aj,userId);
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

    public String grandAj(String aj_id, List<String>list, UserEntity user){
        Connection con = DBUtil.getConnection();
        String sql  = "insert into REL_GRAND_AJ(ajid,userid,granduserid,inserttime)" +
                       "values(?,?,?,?)";
        try {
            PreparedStatement pstm ;
            pstm =con.prepareStatement(sql);
            Statement st= con.createStatement();
            st.execute("delete REL_GRAND_AJ where ajid="+aj_id);
            String time = TimeFormatUtil.getDate("/");
            for(int i=0;i<list.size();i++){
                pstm.setLong(1, Long.parseLong(aj_id));
                pstm.setLong(2, Long.parseLong(list.get(i)));
                pstm.setLong(3, user.getId());
                pstm.setString(4, time);
                pstm.addBatch();
            }
            if(list.size()>0) {
                pstm.executeBatch();
            }
            con.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "200";
    }

    public List<CftZzxxEntity> getCftList(AjEntity aj,long userId){
        List<CftZzxxEntity> listZz = new ArrayList<>();
        String [] ajm = aj.getAj().split(",");
        long [] ajid = new long[ajm.length];
        String seach = "and shmc  like '%红包%'";
        if(aj.getCftminsj().length()>1){
            seach += " and jysj >= '"+aj.getCftminsj()+"'";
        }
        if(aj.getCftmaxsj().length()>1){
            seach+=" and jysj <= '"+aj.getCftmaxsj()+"'";
        }
        for(int i=0;i<ajm.length;i++){
            ajid[i] = findByName(ajm[i],userId).get(0).getId();
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

        List<ZfbZcxxEntity> zcxxList = zfbZcxxDao.find("from ZfbZcxxEntity where aj_id = "+aje.getId());
        Map<String,List<ZfbZcxxEntity>> m = zcxxList.stream().collect(Collectors.groupingBy(ZfbZcxxEntity::getYhId));
        List<ZfbZzmxTjjgsForm> zfbZzmxList = zfbZzmxDao.selectFilterJyjlBySpmc(search, aje.getId());
        List<ZfbZzmxTjjgsEntity> tjjgsList = ZfbZzmxTjjgsEntity.FormToList(zfbZzmxList, aje.getId(),m);
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
