package cn.com.sinofaith.service.PyramidSaleService;

import cn.com.sinofaith.dao.pyramidSaleDao.PyramidSalePoltDao;
import cn.com.sinofaith.form.psForm.PsPoltForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 传销层级图表业务层
 * @author zd
 * create by 2018.11.09
 */
@Service
public class PyramidSalePoltService {
    @Autowired
    private PyramidSalePoltDao pyramidSalePoltDao;

    /**
     * 获取数据
     * @param id
     * @return
     */
    public List<PsPoltForm> getTreeData(long id,String psId) {
        List<PsPoltForm> psPoltForms = pyramidSalePoltDao.getTreeData(id,psId);
        List<PsPoltForm> psList = formatTree(psPoltForms);
        return psList;
    }


    public static List<PsPoltForm> formatTree(List<PsPoltForm> list ){
        PsPoltForm root = new PsPoltForm();
        PsPoltForm node = new PsPoltForm();
        List<PsPoltForm> treelist = new ArrayList<>();//拼凑好的Json数据
        List<PsPoltForm> parentNodes = new ArrayList<>(); // 存放所有父节点

        if(list!=null && list.size()>0){
            root = list.get(0); //第一个一定是根节点 0
            for(int i=1; i<list.size(); i++){
                node = list.get(i);
                if(node.getSponsorid().equals(root.getPsid())){ //从根节点开始遍历是不是子节点
                    parentNodes.add(node);
                    root.getChildren().add(node);
                }else{ //获取root子节点的孩子节点
                    getChildrenNodes(parentNodes, node);
                    parentNodes.add(node);
                }
            }
        }
        treelist.add(root);
        return treelist;
    }

    private static void getChildrenNodes(List<PsPoltForm> parentNodes, PsPoltForm node){
        for(int i=parentNodes.size()-1; i>=0; i--){
            PsPoltForm pnode = parentNodes.get(i);
            if(pnode.getPsid().equals(node.getSponsorid())){
                pnode.getChildren().add(node);
                return;
            }
        }
    }
}
