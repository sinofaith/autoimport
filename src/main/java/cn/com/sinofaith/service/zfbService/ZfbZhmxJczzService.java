package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbZhmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZhmxJczzEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbZhmxJczzDao;
import cn.com.sinofaith.page.Page;
import com.google.gson.Gson;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支付宝账户进出总账统计业务层
 * @author zd
 * create by 2019.02.19
 */
@Service
public class ZfbZhmxJczzService {
    @Autowired
    private ZfbZhmxJczzDao zfbZhmxJczzDao;

    /**
     * 分页数据获取
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = null;
        int rowAll = zfbZhmxJczzDao.getRowAll(dc);
        if(rowAll>0){
            List<ZfbZhmxJczzEntity> jczzList = zfbZhmxJczzDao.getDoPage(currentPage, pageSize, dc);
            if(jczzList!=null){
                page = new Page();
                page.setPageSize(pageSize);
                page.setList(jczzList);
                page.setTotalRecords(rowAll);
                page.setPageNo(currentPage);
            }
        }
        return page;
    }

    /**
     * 详情数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param id
     * @return
     */
    public String getZfbZhmxJczz(int currentPage, int pageSize, String search, long id) {
        Gson gson = new Gson();
        Page page = null;
        int rowAll = zfbZhmxJczzDao.getRowAllCount(search, id);
        if (rowAll > 0) {
            List<ZfbZhmxEntity> zhmxList = zfbZhmxJczzDao.getDoPageJczz(currentPage, pageSize, search, id, true);
            for (int i = 0; i < zhmxList.size(); i++) {
                zhmxList.get(i).setId((currentPage - 1) * pageSize + i + 1);
            }
            if (zhmxList != null) {
                page = new Page();
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(zhmxList);
                page.setPageSize(pageSize);
            }
        }
        return gson.toJson(page);
    }
}
