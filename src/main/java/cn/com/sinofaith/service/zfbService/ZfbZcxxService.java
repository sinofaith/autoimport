package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbZcxxEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbZcxxDao;
import cn.com.sinofaith.page.Page;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * 支付宝注册信息业务层
 * @author zd
 * create by 2018.11.28
 */
@Service
public class ZfbZcxxService {

    @Autowired
    private ZfbZcxxDao zfbZcxxDao;
    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = null;
        int rowAll = zfbZcxxDao.getRowAll(dc);
        if(rowAll>0){
            List<ZfbZcxxEntity> zcxxList = zfbZcxxDao.getDoPage(currentPage, pageSize, dc);
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
