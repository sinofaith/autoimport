package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbZhmxEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbZhmxDao;
import cn.com.sinofaith.page.Page;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * 支付宝账户明细业务层
 * @author zd
 * create by 2018.11.28
 */
@Service
public class ZfbZhmxService {
    @Autowired
    private ZfbZhmxDao zfbZhmxDao;
    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = null;
        int rowAll = zfbZhmxDao.getRowAll(dc);
        if(rowAll>0){
            List<ZfbZhmxEntity> zcxxList = zfbZhmxDao.getDoPage(currentPage, pageSize, dc);
            if(zcxxList!=null){
                page = new Page();
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(zcxxList);
                page.setPageSize(pageSize);
            }
        }
        return page;
    }
}
