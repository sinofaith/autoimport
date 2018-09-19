package cn.com.sinofaith.controller.wuliuController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.bean.wlBean.WuliuShipEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.wlService.WuliuShipService;
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

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/wuliuShip")
public class WuliuShipController {

    @Autowired
    private WuliuShipService wlsService;

    @RequestMapping()
    public ModelAndView redirectShip(HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/wuliuShip/seach?pageNo=1");
        // 清空session域中数据
        session.removeAttribute("wuliuShipSeachCondition");
        session.removeAttribute("wuliuShipSeachCode");

        session.removeAttribute("wuliuShipOrder");
        session.removeAttribute("wuliuShiplastOrder");
        session.removeAttribute("wuliuShipDesc");
        return mav;
    }

    @RequestMapping("/seach")
    public String seach(int pageNo, String orderby,HttpSession session, Model model){
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(WuliuShipEntity.class);
        // 获取session域中的数据
        String seachCondition = (String) session.getAttribute("wuliuShipSeachCondition");
        String seachCode = (String) session.getAttribute("wuliuShipSeachCode");
        if(seachCode!=null && !seachCode.trim().equals("")){
            dc.add(Restrictions.like(seachCondition,seachCode));
        }
        // 所属案件
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj == null){
            return "wl/wlShip";
        }else{
            dc.add(Restrictions.eq("aj_id",aj.getId()));
        }
        // 排序
        String lastOrder = (String) session.getAttribute("wuliuShiplastOrder");
        String desc = (String) session.getAttribute("wuliuShipDesc");
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("")){
                    dc.addOrder(Order.desc(orderby));
                    desc = "desc";
                }else{
                    dc.addOrder(Order.asc(orderby));
                    desc = "";
                }
            }else{
                dc.addOrder(Order.asc(orderby));
                desc = "";
            }
        }else if("".equals(desc)){
            dc.addOrder(Order.asc(lastOrder));
        }else if("desc".equals(desc)){
            dc.addOrder(Order.desc(lastOrder));
        }
        // 获取分页数据
        Page page = wlsService.getDoPage(pageNo,10,dc);
        // 将数据返回
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("wuliuShiplastOrder",orderby);
        }
        session.setAttribute("wuliuShipOrder",orderby);
        session.setAttribute("wuliuShipDesc",desc);
        return "wl/wlShip";
    }

    /**
     * 根据字段名的seachCode进行(模糊)查询
     * @param seachCondition
     * @param seachCode
     * @param session
     * @return
     */
    @RequestMapping(value = "/SeachCode",method = RequestMethod.POST)
    public String seachCode(String seachCondition,String seachCode,HttpSession session){
        // 若seachCode(查询内容)为空或为null
        if(seachCode==null || seachCode.trim().isEmpty()){
            session.removeAttribute("wuliuShipSeachCondition");
            session.removeAttribute("wuliuShipSeachCode");
            return "redirect:/wuliuShip/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("wuliuShipSeachCondition",seachCondition);
        session.setAttribute("wuliuShipSeachCode",seachCode);
        return "redirect:/wuliuShip/seach?pageNo=1";
    }


    @RequestMapping("/getDetails")
    @ResponseBody
    public String getDetails(String ship_phone, String order, int page, HttpSession session){
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(WuliuEntity.class);
        // 添加查询条件
        String seach = "";
        dc.add(Restrictions.eq("ship_phone",ship_phone));
        seach = " and ship_phone='"+ship_phone+"'";
        // 从session域中取出数据
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        seach += " and aj_id="+aj.getId();
        String lastOrder = (String) session.getAttribute("xqlastOrder");
        String desc = (String) session.getAttribute("xqdesc");
        // 查询哪一个案件
        if("xxx".equals(order)){
            if("".equals(desc)){
                dc.addOrder(Order.desc("ship_time"));
                seach += " order by ship_time desc";
            }else{
                dc.addOrder(Order.asc("ship_time"));
                seach += " order by ship_time";
            }
        }else{
            if(order.equals(lastOrder)){
                if(desc==null || desc.equals("desc")){
                    dc.addOrder(Order.desc(order));
                    seach += " order by ship_time desc";
                    desc = "";
                }else{
                    dc.addOrder(Order.asc(order));
                    seach += " order by ship_time";
                    desc = "desc";
                }
            }else{
                dc.addOrder(Order.asc(order));
                seach += " order by ship_time";
                desc = "desc";
            }
        }

        session.setAttribute("xqdesc", desc);
        if(!order.equals("xxx")){
            session.setAttribute("xqlastOrder", order);
        }
        String json = wlsService.getWuliuRelation(page, 100, dc, seach);
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
        ses.removeAttribute("xqdesc");
        return "200";
    }
}
