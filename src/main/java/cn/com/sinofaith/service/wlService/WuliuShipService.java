package cn.com.sinofaith.service.wlService;

import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.bean.wlBean.WuliuShipEntity;
import cn.com.sinofaith.dao.wuliuDao.WuliuRelationDao;
import cn.com.sinofaith.dao.wuliuDao.WuliuShipDao;
import cn.com.sinofaith.page.Page;
import com.google.gson.Gson;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 寄件人业务层
 */
@Service
public class WuliuShipService {

    @Autowired
    private WuliuShipDao wlsDao;
    @Autowired
    private WuliuRelationDao wlrDao;
    /**
     * 表添加数据
     * @param id
     */
    public void insertShip(long id) {
        // 查询此时的案件是否有数据
        int rowCount = wlsDao.getRowCount(id);
        if(rowCount>0){
            // 删除此案件的数据
            wlsDao.deleteWuliuShip(id);
        }
        // 获取数据
        List<WuliuShipEntity> wls = wlsDao.getWuliuShip(id);
        // 插入数据
        wlsDao.insertWuliuShip(wls);
    }

    /**
     * 封装分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page getDoPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = new Page();
        // 获取总条数
        int rowCount = wlsDao.getRowAll(dc);
        if(rowCount>0){
            List<WuliuShipEntity> wls = wlsDao.getDoPage(currentPage,pageSize,dc);
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
     * 寄件人详情数据
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

        int rowAll = wlsDao.getCountRow(seach);
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
