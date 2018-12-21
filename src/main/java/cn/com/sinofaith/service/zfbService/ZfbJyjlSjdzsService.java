package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbJyjlSjdzsEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbJyjlSjdzsDao;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlSjdzsForm;
import cn.com.sinofaith.page.Page;
import com.google.gson.Gson;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支付宝交易记录地址业务层
 * @author zd
 * create by 2018.12.20
 */
@Service
public class ZfbJyjlSjdzsService {
    @Autowired
    private ZfbJyjlSjdzsDao zfbJyjlSjdzsDao;

    /**
     * 收件地址分页
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Page page = null;
        int rowAll = zfbJyjlSjdzsDao.getRowAll(dc);
        if(rowAll>0){
            List<ZfbJyjlSjdzsEntity> sjdzsList = zfbJyjlSjdzsDao.getDoPage(currentPage, pageSize, dc);
            if(sjdzsList!=null){
                page = new Page();
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(sjdzsList);
                page.setPageSize(pageSize);
            }
        }
        return page;
    }

    /**
     * 地址详情数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @return
     */
    public String getZfbJyjlSjdzs(int currentPage, int pageSize, String search) {
        Gson gson = new Gson();
        Page page = null;
        int rowAll = zfbJyjlSjdzsDao.getRowAllCount(search);
        if(rowAll>0){
            List<ZfbJyjlSjdzsForm> sjdzsList = zfbJyjlSjdzsDao.getDoPageSjdzs(currentPage, pageSize, search);
            for (int i =0;i<sjdzsList.size();i++) {
                sjdzsList.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            if(sjdzsList!=null){
                page = new Page();
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(sjdzsList);
                page.setPageSize(pageSize);
            }
        }
        return gson.toJson(page);

    }
}
