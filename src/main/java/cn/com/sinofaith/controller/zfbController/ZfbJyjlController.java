package cn.com.sinofaith.controller.zfbController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.zfbService.ZfbJyjlService;
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
 * 支付宝交易记录控制器
 * @author zd
 * create by 2018.12.03
 */
@Controller
@RequestMapping("/zfbJyjl")
public class ZfbJyjlController {
    @Autowired
    private ZfbJyjlService zfbJyjlService;

    @RequestMapping()
    public ModelAndView zfbJyjl(HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/zfbJyjl/seach?pageNo=1");
        session.removeAttribute("jyjlSeachCondition"); //查询条件
        session.removeAttribute("jyjlSeachCode");//查询内容
        session.removeAttribute("jyjlLastOrder");
        session.removeAttribute("jyjlDesc");
        return mav;
    }

    @RequestMapping("/seach")
    public String seach(int pageNo, String orderby, HttpSession session, Model model){
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj==null){
            return "/zfb/zfbJyjl";
        }
        String seachCondition = (String) session.getAttribute("jyjlSeachCondition");
        String seachCode = (String) session.getAttribute("jyjlSeachCode");
        String lastOrder = (String) session.getAttribute("jyjlLastOrder");
        String desc = (String) session.getAttribute("jyjlDesc");
        // 创建离线查询语句
        String seach = "";
        String[] condition = null;
        if(seachCondition!=null){
            condition = seachCondition.split(",");
        }
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            seach += " and ("+condition[0]+" like '"+seachCode+"' or "+condition[1]+" like '"+seachCode+"')";
        }
        if(orderby==null && desc==null){
            seach += " order by jyzje desc,jyh";
        }
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("desc")){
                    seach += " order by "+orderby+",jyh";
                    desc = "";
                }else{
                    seach += " order by "+orderby+" desc,jyh nulls last";
                    desc = "desc";
                }
            }else{
                seach += " order by "+orderby+",jyh";
                desc = "";
            }
        }else if("".equals(desc)){
            seach += " order by "+lastOrder+",jyh";
        }else if("desc".equals(desc)){
            seach += " order by "+lastOrder+" desc,jyh nulls last";
        }
        // 封装分页数据
        // 获取分页数据
        Page page = zfbJyjlService.queryForPage(pageNo,10,seach,aj.getId());
        // 将数据存入request域中
        model.addAttribute("jyjlSeachCode", seachCode);
        if(condition!=null){
            model.addAttribute("jyjlSeachCondition", condition[0]);
        }
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("jyjlLastOrder",orderby);
        }
        session.setAttribute("jyjlOrder",orderby);
        session.setAttribute("jyjlDesc",desc);
        return "/zfb/zfbJyjl";
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
            session.removeAttribute("jyjlSeachCondition");
            session.removeAttribute("jyjlSeachCode");
            return "redirect:/zfbJyjl/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("jyjlSeachCondition",seachCondition);
        session.setAttribute("jyjlSeachCode",seachCode);
        return "redirect:/zfbJyjl/seach?pageNo=1";
    }

    @RequestMapping("/getDetails")
    public @ResponseBody String getDetails(String mjyhid,String mjxx,String mijyhid,String mijxx,String direction,String spmc,String order,
                                           int page,HttpSession session){
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbJyjlEntity.class);
        // 获取Aj
        AjEntity aj = (AjEntity) session.getAttribute("aj");

        dc.add(Restrictions.eq("aj_id",aj.getId()));
        if(direction.equals("卖家")){
            dc.add(Restrictions.eq("mjyhId",mijyhid));
            dc.add(Restrictions.eq("mjxx",mijxx));
            dc.add(Restrictions.eq("mijyhId",mjyhid));
            dc.add(Restrictions.eq("mijxx",mjxx));
        }else{
            dc.add(Restrictions.eq("mjyhId",mjyhid));
            dc.add(Restrictions.eq("mjxx",mjxx));
            dc.add(Restrictions.eq("mijyhId",mijyhid));
            dc.add(Restrictions.eq("mijxx",mijxx));
        }
        dc.add(Restrictions.eq("spmc",spmc));
        dc.add(Restrictions.eq("jyzt","交易成功"));
        // 获取desc
        String desc = (String) session.getAttribute("jyjlXQDesc");
        String lastOrder = (String) session.getAttribute("jyjlXQLastOrder");
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
        session.setAttribute("jyjlXQDesc", desc);
        if(!order.equals("xxx")){
            session.setAttribute("jyjlXQLastOrder", order);
        }
        return zfbJyjlService.getZfbJyjl(page, 100, dc);
    }

    /**
     * 删除desc
     * @param ses
     * @return
     */
    @RequestMapping(value = "/removeDesc",method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String removeDesc(HttpSession ses){
        ses.removeAttribute("jyjlXQDesc");
        ses.removeAttribute("jyjlXQLastOrder");
        return "200";
    }
}
