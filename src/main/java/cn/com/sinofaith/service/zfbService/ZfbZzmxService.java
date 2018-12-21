package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbZzmxDao;
import cn.com.sinofaith.form.psForm.PsHierarchyForm;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxForm;
import cn.com.sinofaith.page.Page;
import com.google.gson.Gson;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * 支付宝转账明细业务层
 * @author zd
 * create by 2018.11.28
 */
@Service
public class ZfbZzmxService {
    @Autowired
    private ZfbZzmxDao zfbZzmxDao;
    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = new Page();
        List<ZfbZzmxEntity> zzmxForms = null;
        int rowAll = zfbZzmxDao.getRowAll(dc);
        if(rowAll>0){
            zzmxForms = zfbZzmxDao.getDoPage(currentPage,pageSize,dc);
            if(zzmxForms!=null){
                page.setPageSize(pageSize);
                page.setList(zzmxForms);
                page.setTotalRecords(rowAll);
                page.setPageNo(currentPage);
            }
        }
        return page;
    }

    /**
     * 转账明细详情
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    /*public String getZfbZzmx(int currentPage, int pageSize, DetachedCriteria dc) {
        Gson gson = new Gson();
        Page page = null;
        int rowAll = zfbZzmxDao.getRowAll(dc);
        if(rowAll>0){
            List<ZfbZzmxEntity> zzmxList = zfbZzmxDao.getDoPage(currentPage, pageSize, dc);
            for (int i =0;i<zzmxList.size();i++) {
                zzmxList.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            if(zzmxList!=null){
                page = new Page();
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(zzmxList);
                page.setPageSize(pageSize);
            }
        }
        return gson.toJson(page);
    }*/
}
