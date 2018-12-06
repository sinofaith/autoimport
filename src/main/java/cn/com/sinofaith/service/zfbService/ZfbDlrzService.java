package cn.com.sinofaith.service.zfbService;

import cn.com.sinofaith.dao.zfbDao.ZfbDlrzDao;
import cn.com.sinofaith.form.zfbForm.ZfbDlrzForm;
import cn.com.sinofaith.form.zfbForm.ZfbDlrzForms;
import cn.com.sinofaith.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * 支付宝登录日志业务层
 * @author zd
 * create by 2018.11.28
 */
@Service
public class ZfbDlrzService {
    @Autowired
    private ZfbDlrzDao zfbDlrzDao;

    /**
     *TODO 登陆日志分页数据
     * @param currentPage
     * @param pageSize
     * @param search
     * @param id
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, String search, long id) {
        Page page = new Page();
        // 获取总条数
        int rowAll = zfbDlrzDao.getRowAlls(search,id);
        if(rowAll>0){
            List<ZfbDlrzForm> zfbDlrzForm =  zfbDlrzDao.queryForPage(currentPage,pageSize,search,id);
            if(zfbDlrzForm!=null) {
                page.setPageNo(currentPage);
                page.setTotalRecords(rowAll);
                page.setList(zfbDlrzForm);
                page.setPageSize(pageSize);
            }
        }
        return page;

    }

    /**
     * 获取时序图数据
     * @param zfbyhId
     * @param id
     * @return
     */
    public List<ZfbDlrzForms> getSequenceChart(String zfbyhId, long id) {
        return zfbDlrzDao.getSequenceChart(zfbyhId,id);
    }
}
