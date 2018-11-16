package cn.com.sinofaith.controller.pyramidSaleController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.form.psForm.PsPoltForm;
import cn.com.sinofaith.service.PyramidSaleService.PyramidSalePoltService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 传销层级图表
 * @author zd
 * create by 2018.10.08
 */
@Controller
@RequestMapping("/pyramidSalePolt")
public class PyramidSalePoltController {

    @Autowired
    private PyramidSalePoltService pyramidSalePoltService;

    @RequestMapping()
    public String jump(String psId, HttpSession session){
        session.setAttribute("psId",psId);
        return "pyramidSale/pyramidSalePolt";
    }

    @RequestMapping("/tree")
    public @ResponseBody List<PsPoltForm> getTree(HttpSession session){
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String psId = (String) session.getAttribute("psId");
        List<PsPoltForm> psPoltForms = null;
        if(aj==null){
            return psPoltForms;
        }
        // 获取数据
        psPoltForms = pyramidSalePoltService.getTreeData(aj.getId(),psId);
        return psPoltForms;
    }
}
