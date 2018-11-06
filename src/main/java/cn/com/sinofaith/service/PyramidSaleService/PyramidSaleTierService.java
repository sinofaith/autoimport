package cn.com.sinofaith.service.PyramidSaleService;

import cn.com.sinofaith.bean.pyramidSaleBean.PyramidSaleEntity;
import cn.com.sinofaith.dao.pyramidSaleDao.PsHierarchyDao;
import cn.com.sinofaith.dao.pyramidSaleDao.PyramidSaleDao;
import cn.com.sinofaith.form.psForm.PsHierarchyForm;
import cn.com.sinofaith.page.Page;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 层级信息业务层
 */
@Service
public class PyramidSaleTierService {

    @Autowired
    private PsHierarchyDao psHierarchyDao;
    @Autowired
    private PyramidSaleDao pyramidSaleDao;

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
        List<PsHierarchyForm> psHierarchyForms = null;
        int rowAll = psHierarchyDao.getRowPsHierarchy(seach,id);
        if(rowAll>0){
            psHierarchyForms = psHierarchyDao.queryForPage(currentPage,pageSize,seach,id);
            if(psHierarchyForms!=null){
                page.setPageSize(pageSize);
                page.setList(psHierarchyForms);
                page.setTotalRecords(rowAll);
                page.setPageNo(currentPage);
            }
        }
        return page;
    }


    /**
     * 直推下线详情
     * @param currentPage
     * @param pageSize
     * @param seach
     * @return
     */
    public String getPyramidSale(int currentPage, int pageSize, String seach, boolean temp, String psId) {
        Gson gson = new Gson();
        Page page = new Page();
        int rowAll = pyramidSaleDao.getRowAllBySql(seach,temp,psId);
        if(rowAll>0){
            List<PyramidSaleEntity> psList = pyramidSaleDao.getDoPageBySql(currentPage,pageSize,seach,temp,psId);
            for (int i = 0; i < psList.size(); i++) {
                psList.get(i).setId((currentPage-1)*pageSize+i+1);
            }
            page.setPageNo(currentPage);
            page.setTotalRecords(rowAll);
            page.setList(psList);
            page.setPageSize(pageSize);
        }
        return gson.toJson(page);
    }
}
