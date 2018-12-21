package cn.com.sinofaith.controller.pyramidSaleController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.pyramidSaleBean.PyramidSaleEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.PyramidSaleService.PyramidSaleTierService;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 传销层级关系控制器
 * @author zd
 * create by 2018.10.31
 */
@Controller
@RequestMapping("/pyramidSaleTier")
public class pyramidSaleTierController {
    @Autowired
    private PyramidSaleTierService pyramidSaleTierService;

    @RequestMapping()
    public ModelAndView pyramidSaleTier(HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/pyramidSaleTier/seach?pageNo=1");
        session.removeAttribute("psTierSeachCondition"); //查询条件
        session.removeAttribute("psTierSeachCode");//查询内容
        session.removeAttribute("psTierLastOrder");
        session.removeAttribute("psTierDesc");
        return mav;
    }

    @RequestMapping("/seach")
    public String seach(int pageNo, String orderby, HttpSession session, Model model){
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj==null){
            return "/pyramidSale/pyramidSaleTier";
        }
        String seachCondition = (String) session.getAttribute("psTierSeachCondition");
        String seachCode = (String) session.getAttribute("psTierSeachCode");
        String lastOrder = (String) session.getAttribute("psTierLastOrder");
        String desc = (String) session.getAttribute("psTierDesc");
        // 创建离线查询语句
        String seach = "";

        if(seachCode!=null && !seachCondition.equals("directDrive") && !seachCondition.equals("directReferNum")){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            seach += " and "+seachCondition+" like '"+seachCode+"'";
        }else if(seachCode!=null && (seachCondition.equals("directDrive") || seachCondition.equals("directReferNum"))){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            seach += " and "+seachCondition+">'"+seachCode+"'";
        }
        if(orderby==null && desc==null){
            seach += " order by tier,id";
        }
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("desc")){
                    seach += " order by "+orderby+" desc nulls last,id";
                    desc = "";
                }else{
                    seach += " order by "+orderby+",id";
                    desc = "desc";
                }
            }else{
                seach += " order by "+orderby+" desc nulls last,id";
                desc = "";
            }
        }else if("".equals(desc)){
            seach += " order by "+lastOrder+" desc nulls last,id";
        }else if("desc".equals(desc)){
            seach += " order by "+lastOrder+",id";
        }
        // 封装分页数据
        // 获取分页数据
        Page page = pyramidSaleTierService.queryForPage(pageNo,10,seach,aj.getId());
        // 将数据存入request域中
        model.addAttribute("psTierSeachCode", seachCode);
        model.addAttribute("psTierSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("psTierLastOrder",orderby);
        }
        session.setAttribute("psTierOrder",orderby);
        session.setAttribute("psTierDesc",desc);
        return "/pyramidSale/pyramidSaleTier";
    }

    /**
     * 查询字段内容
     * @param seachCondition
     * @param seachCode
     * @param session
     * @return
     */
    @RequestMapping(value = "/SeachCode",method = RequestMethod.POST)
    public String SeachCode(String seachCondition, String seachCode, HttpSession session){
        // 若seachCode(查询内容)为空或为null
        if(seachCode==null || seachCode.trim().isEmpty()){
            session.removeAttribute("psTierSeachCondition");
            session.removeAttribute("psTierSeachCode");
            return "redirect:/pyramidSaleTier/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("psTierSeachCondition",seachCondition);
        session.setAttribute("psTierSeachCode",seachCode);
        return "redirect:/pyramidSaleTier/seach?pageNo=1";
    }

    @RequestMapping("/getDetails")
    public @ResponseBody String getPyramSaleDetails(String psId, String order, boolean temp, int page, HttpSession session){
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        String seach = " aj_id="+aj.getId();
        String lastOrder = (String) session.getAttribute("xqPyramSaleLastOrder");
        String desc = (String) session.getAttribute("xqPyramSaleDesc");
        if(temp){
            seach += " and sponsorid='"+psId+"'";
        }else{
            seach = " and aj_id="+aj.getId();
        }
        // 查询哪一个案件
        if("xxx".equals(order)){
            if("".equals(desc)){
                seach += " order by "+lastOrder;
            }else{
                seach += " order by "+lastOrder+" desc nulls last";
            }
        }else{
            if(order.equals(lastOrder)){
                if(desc==null || desc.equals("")){
                    seach += " order by "+order;
                    desc = "desc";
                }else{
                    seach += " order by "+order+" desc nulls last";
                    desc = "";
                }
            }else{
                seach += " order by "+order+" desc nulls last";
                desc = "";
            }
        }
        session.setAttribute("xqPyramSaleDesc", desc);
        if(!order.equals("xxx")){
            session.setAttribute("xqPyramSaleLastOrder", order);
        }
        String json= pyramidSaleTierService.getPyramidSale(page, 100, seach, temp, psId);
        return json;
    }

    /**
     * 删除desc
     * @param ses
     * @return
     */
    @RequestMapping(value = "/removeDesc",method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String removeDesc(HttpSession ses){
        ses.removeAttribute("xqPyramSaleDesc");
        return "200";
    }

}
