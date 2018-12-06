package cn.com.sinofaith.controller.zfbController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.zfbService.ZfbZzmxService;
import org.hibernate.NullPrecedence;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * 支付宝转账明细控制器
 * @author zd
 * create by 2018.11.30
 */
@Controller
@RequestMapping("/zfbZzmx")
public class ZfbZzmxController {
    @Autowired
    private ZfbZzmxService zfbZzmxService;

    @RequestMapping()
    public ModelAndView zfbZzmx(HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/zfbZzmx/seach?pageNo=1");
        session.removeAttribute("zzmxSeachCondition"); //查询条件
        session.removeAttribute("zzmxSeachCode");//查询内容
        session.removeAttribute("zzmxLastOrder");
        session.removeAttribute("zzmxDesc");
        return mav;
    }

    @RequestMapping("/seach")
    public String seach(int pageNo, String orderby, HttpSession session, Model model){
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj==null){
            return "/zfb/zfbZzmx";
        }
        String seachCondition = (String) session.getAttribute("zzmxSeachCondition");
        String seachCode = (String) session.getAttribute("zzmxSeachCode");
        String lastOrder = (String) session.getAttribute("zzmxLastOrder");
        String desc = (String) session.getAttribute("zzmxDesc");
        // 创建离线查询语句
        String seach = "";
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            seach += " and "+seachCondition+" like '"+seachCode+"'";
        }
        if(orderby==null && desc==null){
            seach += " order by zzje desc";
        }
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("desc")){
                    seach += " order by "+orderby;
                    desc = "";
                }else{
                    seach += " order by "+orderby+" desc nulls last";
                    desc = "desc";
                }
            }else{
                seach += " order by "+orderby;
                desc = "";
            }
        }else if("".equals(desc)){
            seach += " order by "+lastOrder;
        }else if("desc".equals(desc)){
            seach += " order by "+lastOrder+" desc nulls last";
        }
        // 封装分页数据
        // 获取分页数据
        Page page = zfbZzmxService.queryForPage(pageNo,10,seach,aj.getId());
        // 将数据存入request域中
        model.addAttribute("zzmxSeachCode", seachCode);
        model.addAttribute("zzmxSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("zzmxLastOrder",orderby);
        }
        session.setAttribute("zzmxOrder",orderby);
        session.setAttribute("zzmxDesc",desc);
        return "/zfb/zfbZzmx";
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
            session.removeAttribute("zzmxSeachCondition");
            session.removeAttribute("zzmxSeachCode");
            return "redirect:/zfbZzmx/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("zzmxSeachCondition",seachCondition);
        session.setAttribute("zzmxSeachCode",seachCode);
        return "redirect:/zfbZzmx/seach?pageNo=1";
    }

    @RequestMapping("/getDetails")
    public @ResponseBody
    String getPyramSaleDetails(String dyxcsj, String zzcpmc, String order, int page, HttpSession session){
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbZzmxEntity.class);
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        String lastOrder = (String) session.getAttribute("zzmxXQLastOrder");
        String desc = (String) session.getAttribute("zzmxXQDesc");
        dc.add(Restrictions.eq("dyxcsj",dyxcsj));
        dc.add(Restrictions.eq("zzcpmc",zzcpmc));
        // 查询哪一个案件
        if("xxx".equals(order)){
            if("".equals(desc)){
                dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
            }else{
                dc.addOrder(Order.asc(lastOrder));
            }
        }else{
            if(order.equals(lastOrder)){
                if(desc==null || desc.equals("")){
                    dc.addOrder(Order.asc(order));
                    desc = "desc";
                }else{
                    dc.addOrder(Order.desc(order).nulls(NullPrecedence.LAST));
                    desc = "";
                }
            }else{
                dc.addOrder(Order.desc(order).nulls(NullPrecedence.LAST));
                desc = "";
            }
        }
        session.setAttribute("zzmxXQDesc", desc);
        if(!order.equals("xxx")){
            session.setAttribute("zzmxXQLastOrder", order);
        }
        String json= zfbZzmxService.getZfbZzmx(page, 100, dc);
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
        ses.removeAttribute("zzmxXQDesc");
        ses.removeAttribute("zzmxXQLastOrder");
        return "200";
    }
}
