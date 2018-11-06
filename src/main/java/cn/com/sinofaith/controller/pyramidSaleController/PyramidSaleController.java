package cn.com.sinofaith.controller.pyramidSaleController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.pyramidSaleBean.PyramidSaleEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.PyramidSaleService.PyramidSaleService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import static java.lang.Integer.parseInt;

/**
 * 传销数据控制层
 *  @author zd
 *  create by 2018.10.26
 */
@Controller
@RequestMapping("/pyramidSale")
public class PyramidSaleController {

    @Autowired
    private PyramidSaleService pyramidSaleService;

    @RequestMapping()
    public ModelAndView pyramidSale(HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/pyramidSale/seach?pageNo=1");
        session.removeAttribute("psSeachCondition"); //查询条件
        session.removeAttribute("psSeachCode");//查询内容
        session.removeAttribute("psLastOrder");
        session.removeAttribute("psDesc");
        return mav;
    }

    /**
     * 分页数据
     * @param pageNo
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/seach")
    public String seach(int pageNo, String orderby, HttpSession session, Model model){
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(PyramidSaleEntity.class);
        // 从域中取出对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj==null){
            return "/pyramidSale/pyramidSale";
        }
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        // 查询字段
        String seachCondition = (String) session.getAttribute("psSeachCondition");
        // 查询内容
        String seachCode = (String) session.getAttribute("psSeachCode");
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            dc.add(Restrictions.eq(seachCondition,seachCode));
        }
        // 排序字段
        String lastOrder = (String) session.getAttribute("psLastOrder");
        // 排序 desc降 asc升
        String desc = (String) session.getAttribute("psDesc");
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("desc")){
                    dc.addOrder(Order.desc(orderby));
                    desc = "";
                }else{
                    dc.addOrder(Order.asc(orderby));
                    desc = "desc";
                }
            }else{
                dc.addOrder(Order.desc(orderby));
                desc = "";
            }
        }else if("".equals(desc)){
            dc.addOrder(Order.desc(lastOrder));
        }else if("desc".equals(desc)){
            dc.addOrder(Order.asc(lastOrder));
        }
        // 获取分页数据
        Page page = pyramidSaleService.queryForPage(pageNo,10,dc);
        // 将数据存入request域中
        model.addAttribute("psSeachCode", seachCode);
        model.addAttribute("psSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("psLastOrder",orderby);
        }
        session.setAttribute("psOrder",orderby);
        session.setAttribute("psDesc",desc);
        return "/pyramidSale/pyramidSale";
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
            session.removeAttribute("psSeachCondition");
            session.removeAttribute("psSeachCode");
            return "redirect:/pyramidSale/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("psSeachCondition",seachCondition);
        session.setAttribute("psSeachCode",seachCode);
        return "redirect:/pyramidSale/seach?pageNo=1";
    }

}
