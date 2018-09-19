package cn.com.sinofaith.service.wlService;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.bean.wlBean.WuliuRelationEntity;
import cn.com.sinofaith.dao.AJDao;
import cn.com.sinofaith.dao.wuliuDao.WuliuRelationDao;
import cn.com.sinofaith.form.wlForm.WuliuRelationForm;
import cn.com.sinofaith.page.Page;
import com.google.gson.Gson;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WuliuRelationService {

    @Autowired
    private AJDao ajDao;
    @Autowired
    private WuliuRelationDao wlrDao;

    /**
     * 封装sql语句
     * @param seachCondition
     * @param seachCode
     * @param aj
     * @param orderby
     * @param desc
     * @return
     */
    public String getSeach(String seachCondition, String seachCode, AjEntity aj, String orderby, String desc) {
        // 查询点击的案件id
        String aj_id = getAjidByAjm(aj);
        StringBuffer seach = new StringBuffer(" and aj_id in (" + aj_id + ")");
        // 当查询内容不为空时
        if(seachCode!=null) {
            seachCode = seachCode.replace("\r\n", "").replace("，", "").replace(" ", "").replace(" ", "").replace("\t", "");
            seach.append(" and " + seachCondition + " like '" + seachCode + "'");
        }else{
            seach.append(" and ( 1=1 )");
        }
        if(orderby!=null){
            seach .append(" order by "+orderby).append(desc);
        }
        return seach.toString();
    }

    /**
     * 查询选中的案件ids
     * @param aj
     * @return
     */
    public String getAjidByAjm(AjEntity aj){
        String[] ajm = new String[]{};
        StringBuffer ajid = new StringBuffer();
        if(aj.getAj().contains(",")) {
            ajm = aj.getAj().split(",");
            for (int i = 0; i < ajm.length; i++) {
                ajid.append(ajDao.findFilter(ajm[i]).get(0).getId());
                if (i != ajm.length - 1) {
                    ajid.append(",");
                }
            }
        }else{
            ajid.append(aj.getId());
        }
        return ajid.toString();
    }

    /**
     * 封装分页数据
     * @param currentPage
     * @param pageSize
     * @param seach
     * @return
     */
    public Page queryForPage(int currentPage, int pageSize, String seach, long aj_id) {
        // 创建分页对象
        Page page = new Page();
        // 封装wuliu_relation表
        List<WuliuRelationForm> wlrs = null;
        int allRow = wlrDao.getAllRowCount(seach,aj_id);
        if(allRow>0){
            wlrs = wlrDao.getDoPage(seach, currentPage ,pageSize, aj_id);
            for (int i = 0; i < wlrs.size(); i++) {
                wlrs.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            // 封装page
            page.setPageNo(currentPage);
            page.setList(wlrs);
            page.setPageSize(pageSize);
            page.setTotalRecords(allRow);
        }
        return page;
    }

    /**
     * 物流关系详情
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public String getWuliuRelation(int currentPage, int pageSize, DetachedCriteria dc, String seach) {
        Gson gson = new Gson();
        Page page = new Page();
        List<WuliuEntity> wls = null;
        int rowAll = wlrDao.getRowAll(seach);
        if(rowAll>0){
           wls = wlrDao.getPage(currentPage, pageSize, dc);
            for (int i = 0; i < wls.size(); i++) {
                wls.get(i).setId((currentPage-1)*pageSize+i+1);
            }
           page.setPageNo(currentPage);
           page.setList(wls);
           page.setTotalRecords(rowAll);
           page.setPageSize(pageSize);
        }
        return gson.toJson(page);
    }
}
