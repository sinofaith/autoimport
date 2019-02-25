package cn.com.sinofaith.controller.zfbController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZhmxJczzEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.zfbService.ZfbZhmxJczzService;
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
 * 支付宝账户进出总账统计控制器
 * @author zd
 * create by 2019.02.19
 */
@Controller
@RequestMapping("/zfbZhmxJczz")
public class ZfbZhmxJczzController {
    @Autowired
    private ZfbZhmxJczzService zfbZhmxJczzService;

    @RequestMapping()
    public ModelAndView zfbZhmxJczz(String flag, HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/zfbZhmxJczz/seach?pageNo=1");
        // 清空session中的相关数据
        session.removeAttribute("zhmxJczzSeachCondition"); //查询条件
        session.removeAttribute("zhmxJczzSeachCode");//查询内容
        session.removeAttribute("zhmxJczzLastOrder");
        session.removeAttribute("zhmxJczzDesc");
        session.setAttribute("flag",flag);
        return mav;
    }

    @RequestMapping("/seach")
    public String serach(int pageNo, String orderby, HttpSession session,Model model){
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbZhmxJczzEntity.class);
        // 取出案件对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj == null)
            return "/zfb/zfbZhmxJczz";
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        // 查询字段
        String seachCondition = (String) session.getAttribute("zhmxJczzSeachCondition");
        // 查询内容
        String seachCode = (String) session.getAttribute("zhmxJczzSeachCode");
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
        }
        // 排序字段
        String lastOrder = (String) session.getAttribute("zhmxJczzLastOrder");
        // 排序 desc降 asc升
        String desc = (String) session.getAttribute("zhmxJczzDesc");
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("")){
                    dc.addOrder(Order.asc(orderby));
                    desc = "desc";
                }else{
                    dc.addOrder(Order.desc(orderby).nulls(NullPrecedence.LAST));
                    desc = "";
                }
            }else{
                dc.addOrder(Order.desc(orderby));
                desc = "";
            }
        }else if("".equals(desc)){
            dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
        }else if("desc".equals(desc)){
            dc.addOrder(Order.asc(lastOrder));
        }else{
            dc.addOrder(Order.desc("czzje").nulls(NullPrecedence.LAST));
            session.setAttribute("zhmxJczzLastOrder","czzje");
        }
        // 获取分页数据
        Page page = zfbZhmxJczzService.queryForPage(pageNo,10,dc);
        // 将数据存入request域中
        model.addAttribute("zhmxJczzSeachCode", seachCode);
        model.addAttribute("zhmxJczzSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("zhmxJczzLastOrder",orderby);
        }
        session.setAttribute("zhmxJczzOrder",orderby);
        session.setAttribute("zhmxJczzDesc",desc);
        return "/zfb/zfbZhmxJczz";
    }

    @RequestMapping(value = "/SeachCode",method = RequestMethod.POST)
    public String searchCode(String seachCondition, String seachCode, HttpSession session){
        // 若seachCode(查询内容)为空或为null
        if(seachCode==null || seachCode.trim().isEmpty()){
            session.removeAttribute("zhmxJczzSeachCondition");
            session.removeAttribute("zhmxJczzSeachCode");
            return "redirect:/zfbZhmxJczz/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("zhmxJczzSeachCondition",seachCondition);
        session.setAttribute("zhmxJczzSeachCode",seachCode);
        return "redirect:/zfbZhmxJczz/seach?pageNo=1";
    }

    @RequestMapping("/getDetails")
    public @ResponseBody
    String getDetails(String jyzfbzh, String order, int page, HttpSession session){
        String search = "t.yhxx like '"+jyzfbzh+"%' and (t.jyzt like '%成功%' " +
                "or t.jyzt like '%SUCCESS%' or t.jyzt='完成') and t.jydfxx is not null";
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        String lastOrder = (String) session.getAttribute("zhmxXQLastOrder");
        String desc = (String) session.getAttribute("zhmxXQDesc");
        // 查询哪一个案件
        if("xxx".equals(order)){
            if("".equals(desc)){
                search += " order by "+lastOrder+" desc nulls last";
            }else{
                search += " order by "+lastOrder;
            }
        }else{
            if(order.equals(lastOrder)){
                if(desc==null || desc.equals("")){
                    search += " order by "+lastOrder;
                    desc = "desc";
                }else{
                    search += " order by "+order+" desc nulls last";
                    desc = "";
                }
            }else{
                search += " order by "+order+" desc nulls last";
                desc = "";
            }
        }
        session.setAttribute("zhmxXQDesc", desc);
        if(!order.equals("xxx")){
            session.setAttribute("zhmxXQLastOrder", order);
        }
        String json = zfbZhmxJczzService.getZfbZhmxJczz(page, 100, search, aj.getId());
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
        ses.removeAttribute("zhmxXQDesc");
        ses.removeAttribute("zhmxXQLastOrder");
        return "200";
    }
}