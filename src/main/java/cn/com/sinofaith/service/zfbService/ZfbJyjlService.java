package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.dao.zfbDao.ZfbJyjlDao;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlForm;
import cn.com.sinofaith.page.Page;
import com.google.gson.Gson;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * 支付宝交易记录业务层
 * @author zd
 * create by 2018.11.28
 */
@Service
public class ZfbJyjlService {
    @Autowired
    private ZfbJyjlDao zfbJyjlDao;

    /**
     * 分页数据
     * @param currentPage
     * @param pageSize
     * @param seach
     * @param id
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, String seach, long id) {
        Page page = new Page();
        List<ZfbJyjlForm> jyjlForms = null;
        int rowAll = zfbJyjlDao.getCountRow(seach,id);
        if(rowAll>0){
            jyjlForms = zfbJyjlDao.queryForPage(currentPage,pageSize,seach,id);
            if(jyjlForms!=null){
                page.setPageSize(pageSize);
                page.setList(jyjlForms);
                page.setTotalRecords(rowAll);
                page.setPageNo(currentPage);
            }
        }
        return page;
    }

    /**
     * 详情页
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public String getZfbJyjl(int currentPage, int pageSize, DetachedCriteria dc) {
        Gson gson = new Gson();
        Page page = new Page();
        // 获取总条数
        int rowAll = zfbJyjlDao.getRowAlls(dc);
        if(rowAll>0){
            List<ZfbJyjlEntity> jyjlList = zfbJyjlDao.getDoPageJyjl(currentPage, pageSize, dc);
            for (int i =0;i<jyjlList.size();i++) {
                jyjlList.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            page.setPageSize(pageSize);
            page.setList(jyjlList);
            page.setTotalRecords(rowAll);
            page.setPageNo(currentPage);
        }
        return gson.toJson(page);
    }
}
