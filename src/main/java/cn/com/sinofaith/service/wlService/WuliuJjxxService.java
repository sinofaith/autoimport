package cn.com.sinofaith.service.wlService;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.bean.wlBean.WuliuRelationEntity;
import cn.com.sinofaith.dao.wuliuDao.WuliuDao;
import cn.com.sinofaith.dao.wuliuDao.WuliuJjxxDao;
import cn.com.sinofaith.dao.wuliuDao.WuliuRelationDao;
import cn.com.sinofaith.page.Page;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WuliuJjxxService {

    @Autowired
    private WuliuDao wld;
    @Autowired
    private WuliuJjxxDao jjxxDao;
    @Autowired
    private WuliuRelationDao wlDao;
    /**
     * 获取案件id获取物流数据
     * @param id
     * @return
     */
    public List<WuliuEntity> getAll(long id) {
        String hql = "from WuliuEntity where aj_id = "+id;
        List<WuliuEntity> wulius = wld.find(hql);
        return wulius;
    }

    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc, AjEntity aj) {
        Page page = new Page();
        int rowAll = jjxxDao.getRowAll(dc,aj);
        List<WuliuEntity> wls = null;
        if(rowAll>0){
            wls = jjxxDao.getDoPage(currentPage, pageSize, dc, aj);
            for (int i = 0; i <wls.size(); i++) {
                wls.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            page.setPageSize(pageSize);
            page.setTotalRecords(rowAll);
            page.setList(wls);
            page.setPageNo(currentPage);
        }
        return page;
    }

    /**
     * 获取寄收件人关系信息
     * @param id
     * @return
     */
    public void insertRelation(long id) {
        List<WuliuRelationEntity> wlrs = jjxxDao.insertRelation(id);
        wlDao.insertWuliuRelation(wlrs);
    }
}
