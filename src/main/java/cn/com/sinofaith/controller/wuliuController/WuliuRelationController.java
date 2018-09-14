package cn.com.sinofaith.controller.wuliuController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.wlService.WuliuRelationService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/wuliuRelation")
public class WuliuRelationController {

    @Autowired
    private WuliuRelationService wlRService;

    /**
     * 页面跳转
     * @param httpSession
     * @return
     */
    @RequestMapping()
    public ModelAndView redirectJjxx(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/wuliuRelation/seach?pageNo=1");
        httpSession.removeAttribute("wuliuSeachCondition"); //查询条件
        httpSession.removeAttribute("wuliuSeachCode");//查询内容

        httpSession.removeAttribute("wuliuRelationOrder");
        httpSession.removeAttribute("wuliuRelationlastOrder");
        httpSession.removeAttribute("wuliuRelationDesc");
        return mav;
    }

    @RequestMapping(value = "/seach")
    public String seach(String pageNo, HttpServletRequest req, Model model){
        // 获得session将域中数据取出
        HttpSession session = req.getSession();
        // 查询字段
        String seachCondition = (String) req.getSession().getAttribute("wuliuSeachCondition");
        // 查询内容
        String seachCode = (String) req.getSession().getAttribute("wuliuSeachCode");
        // 所属案件
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 排序字段
        String orderby = (String) session.getAttribute("wuliuRelationOrder");
        // 排序方式(desc降，asc升)
        String desc = (String) session.getAttribute("wuliuRelationDesc");
        // 封装sql语句
        String seach = wlRService.getSeach(seachCondition, seachCode, aj!=null?aj:new AjEntity(), orderby, desc);
        if(aj == null){
            return "wl/wlShipInfo";
        }
        // 获取分页数据
        Page page = wlRService.queryForPage(parseInt(pageNo),10,seach,aj.getId());
        // 将数据存入request域中
        model.addAttribute("wuliuSeachCode", seachCode);
        model.addAttribute("wuliuSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        model.addAttribute("aj",aj);
        return "wl/wlShipInfo";
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
            session.removeAttribute("wuliuSeachCondition");
            session.removeAttribute("wuliuSeachCode");
            return "redirect:/wuliuRelation/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("wuliuSeachCondition",seachCondition);
        session.setAttribute("wuliuSeachCode",seachCode);
        return "redirect:/wuliuRelation/seach?pageNo=1";
    }

    /**
     * 根据某个字段进行排序
     * @param orderby
     * @param session
     * @return
     */
    @RequestMapping(value = "/order")
    public String order(String orderby,HttpSession session){
        String desc = (String) session.getAttribute("wuliuRelationDesc");
        String lastOrder = (String) session.getAttribute("wuliuRelationlastOrder");
        // 当不是首次点击时该字段时
        if(orderby.equals(lastOrder)){
            if(desc==null || " ,id ".equals(desc)){
                desc = " desc ";
            }else{
                desc = " ,id ";
            }
        }else{
            desc = " desc ";
        }
        // 将数据添加到域中
        session.setAttribute("wuliuRelationDesc",desc);
        session.setAttribute("wuliuRelationlastOrder",orderby);
        session.setAttribute("wuliuRelationOrder",orderby);

        return "redirect:/wuliuRelation/seach?pageNo=1";
    }

    @RequestMapping("/getDetails")
    @ResponseBody
    public String getDetails(String ship_phone, String sj_phone, int page, String order, HttpSession session){
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(WuliuEntity.class);
        // 添加查询条件
//        dc.setProjection(Projections.distinct(Projections.property("waybill_id")));
        dc.add(Restrictions.eq("ship_phone",ship_phone));
        dc.add(Restrictions.eq("sj_phone",sj_phone));
        // 从session域中取出数据
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String lastOrder = (String) session.getAttribute("xqlastOrder");
        String desc = (String) session.getAttribute("xqdesc");
        // 查询哪一个案件
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        if(order.equals(lastOrder)){
            if(desc==null || desc.equals("desc")){
                dc.addOrder(Order.desc(order));
                desc = "";
            }else{
                dc.addOrder(Order.asc(order));
                desc = "desc";
            }
        }else{
            dc.addOrder(Order.asc(order));
            desc = "desc";
        }
        session.setAttribute("xqdesc", desc);
        session.setAttribute("xqlastOrder", order);
        String json = wlRService.getWuliuRelation(page, 100, dc);
        return json;
    }
}
