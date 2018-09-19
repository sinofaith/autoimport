package cn.com.sinofaith.service.wlService;

import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.bean.wlBean.WuliuShipEntity;
import cn.com.sinofaith.bean.wlBean.WuliuSjEntity;
import cn.com.sinofaith.dao.wuliuDao.WuliuRelationDao;
import cn.com.sinofaith.dao.wuliuDao.WuliuSjDao;
import cn.com.sinofaith.page.Page;
import com.google.gson.Gson;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WuliuSjService {

    @Autowired
    private WuliuSjDao wlsjDao;
    @Autowired
    private WuliuRelationDao wlrDao;

    /**
     * 收件人表添加数据
     * @param id
     */
    public void insertSj(long id) {
        int rowAll = wlsjDao.getRowAll(id);
        if(rowAll>0){
            wlsjDao.deleteWuliuSj(id);
        }
        // 获取数据
        List<WuliuSjEntity> wls = wlsjDao.getWuliuSj(id);
        // 插入数据
        wlsjDao.insertWuliuSj(wls);
    }

    /**
     * 分页数据获取
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page getDoPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = new Page();
        // 获取总条数
        int rowCount = wlsjDao.getRowCount(dc);
        if(rowCount>0){
            List<WuliuSjEntity> wls = wlsjDao.getDoPage(currentPage,pageSize,dc);
            for (int i = 0; i < wls.size(); i++) {
                wls.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            if(wls!=null){
                page.setPageNo(currentPage);
                page.setList(wls);
                page.setTotalRecords(rowCount);
                page.setPageSize(pageSize);
            }
        }
        return page;
    }

    /**
     * 收件人详情页面
     * @param currentPage
     * @param pageSize
     * @param dc
     * @param seach
     * @return
     */
    public String getWuliuRelation(int currentPage, int pageSize, DetachedCriteria dc, String seach) {
        Gson gson = new Gson();
        Page page = new Page();
        List<WuliuEntity> wls = null;

        int rowAll = wlsjDao.getCountRow(seach);
        if (rowAll>0) {
            wls = wlrDao.getPage(currentPage, pageSize, dc);

            if(wls!=null){
                for (int i = 0; i < wls.size(); i++) {
                    wls.get(i).setId((currentPage-1)*pageSize+i+1);
                }
                page.setPageSize(pageSize);
                page.setTotalRecords(rowAll);
                page.setList(wls);
                page.setPageNo(currentPage);
            }
        }
        return gson.toJson(page);
    }
}
