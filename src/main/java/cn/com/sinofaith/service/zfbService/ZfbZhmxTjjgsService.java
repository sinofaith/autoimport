package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbZhmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZhmxTjjgsEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbZhmxTjjgDao;
import cn.com.sinofaith.dao.zfbDao.ZfbZhmxTjjgsDao;
import cn.com.sinofaith.page.Page;
import com.google.gson.Gson;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支付宝账户明细账户与账户统计业务层
 * @author zd
 * create by 2019.02.18
 */
@Service
public class ZfbZhmxTjjgsService {
    @Autowired
    ZfbZhmxTjjgDao zfbZhmxTjjgDao;
    @Autowired
    ZfbZhmxTjjgsDao zfbZhmxTjjgsDao;

    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = null;
        int rowAll = zfbZhmxTjjgsDao.getRowAll(dc);
        if(rowAll>0){
            List<ZfbZhmxTjjgsEntity> zhmxTjjgs = zfbZhmxTjjgsDao.getDoPage(currentPage, pageSize, dc);
            if(zhmxTjjgs!=null){
                page = new Page();
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(zhmxTjjgs);
                page.setPageSize(pageSize);
            }
        }
        return page;
    }

    public String getZfbZhmxTjjgs(int currentPage, int pageSize, String search, long aj_id) {
        Gson gson = new Gson();
        Page page = null;
        int rowAll = zfbZhmxTjjgDao.getRowAllCount(search, aj_id);
        if (rowAll > 0) {
            List<ZfbZhmxEntity> zhmxList = zfbZhmxTjjgDao.getDoPageTjjg(currentPage, pageSize, search, aj_id, true);
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
