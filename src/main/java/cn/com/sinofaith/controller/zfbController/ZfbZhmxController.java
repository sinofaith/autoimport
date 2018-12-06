package cn.com.sinofaith.controller.zfbController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZhmxEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.zfbService.ZfbZhmxService;
import org.hibernate.NullPrecedence;
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

/**
 * 支付宝账户明细控制器
 * @author zd
 * create by 2018.11.29
 */
@Controller
@RequestMapping("/zfbZhmx")
public class ZfbZhmxController {
    @Autowired
    private ZfbZhmxService zfbZhmxService;

    @RequestMapping()
    public ModelAndView zfb(HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/zfbZhmx/seach?pageNo=1");
        session.removeAttribute("zhmxSeachCondition"); //查询条件
        session.removeAttribute("zhmxSeachCode");//查询内容
        session.removeAttribute("zhmxLastOrder");
        session.removeAttribute("zhmxDesc");
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
       DetachedCriteria dc = DetachedCriteria.forClass(ZfbZhmxEntity.class);
        // 从域中取出对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj==null){
            return "/zfb/zfbZhmx";
        }
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        // 查询字段
        String seachCondition = (String) session.getAttribute("zhmxSeachCondition");
        // 查询内容
        String seachCode = (String) session.getAttribute("zhmxSeachCode");
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            dc.add(Restrictions.like(seachCondition,seachCode));
        }
        // 排序字段
        String lastOrder = (String) session.getAttribute("zhmxLastOrder");
        // 排序 desc降 asc升
        String desc = (String) session.getAttribute("zhmxDesc");
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("desc")){
                    dc.addOrder(Order.desc(orderby).nulls(NullPrecedence.LAST));
                    desc = "";
                }else{
                    dc.addOrder(Order.asc(orderby));
                    desc = "desc";
                }
            }else{
                dc.addOrder(Order.desc(orderby).nulls(NullPrecedence.LAST));
                desc = "";
            }
        }else if("".equals(desc)){
            dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
        }else if("desc".equals(desc)){
            dc.addOrder(Order.asc(lastOrder));
        }
        // 获取分页数据
        Page page = zfbZhmxService.queryForPage(pageNo,10,dc);
        // 将数据存入request域中
        model.addAttribute("zhmxSeachCode", seachCode);
        model.addAttribute("zhmxSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("zhmxLastOrder",orderby);
        }
        session.setAttribute("zhmxOrder",orderby);
        session.setAttribute("zhmxDesc",desc);
        return "/zfb/zfbZhmx";
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
            session.removeAttribute("zhmxSeachCondition");
            session.removeAttribute("zhmxSeachCode");
            return "redirect:/zfbZhmx/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("zhmxSeachCondition",seachCondition);
        session.setAttribute("zhmxSeachCode",seachCode);
        return "redirect:/zfbZhmx/seach?pageNo=1";
    }
}
