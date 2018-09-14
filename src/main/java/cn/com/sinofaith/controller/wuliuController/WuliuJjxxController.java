package cn.com.sinofaith.controller.wuliuController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.AjServices;
import cn.com.sinofaith.service.wlService.WuliuJjxxService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

import static java.lang.Integer.parseInt;

/**
 *   物流寄件信息  Controller
 */
@Controller
@RequestMapping("/wuliujjxx")
public class WuliuJjxxController {

    @Autowired
    private WuliuJjxxService wlService;
    @Autowired
    private AjServices ajs;

    /**
     * 页面跳转
     * @param httpSession
     * @return
     */
    @RequestMapping()
    public ModelAndView redirectJjxx(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/wuliujjxx/seach?pageNo=1");
        httpSession.removeAttribute("JjxxseachCondition"); //查询条件
        httpSession.removeAttribute("JjxxseachCode");//查询内容

        httpSession.removeAttribute("JjxxlastOrder");
        httpSession.removeAttribute("Jjxxdesc");
        return mav;
    }

    /**
     * 查询分页结果
     * @param pageNo
     * @param req
     * @return
     */
    @RequestMapping(value = "/seach")
    public String getWlJjxx(String pageNo, String orderby, HttpServletRequest req, Model model){
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(WuliuEntity.class);
        // 获得session将域中数据取出
        HttpSession session = req.getSession();
        // 查询字段
        String seachCondition = (String) req.getSession().getAttribute("JjxxseachCondition");
        // 查询内容
        String seachCode = (String) req.getSession().getAttribute("JjxxseachCode");
        if(seachCode!=null && !seachCode.trim().equals("")){
            dc.add(Restrictions.like(seachCondition,seachCode));
        }
        // 所属案件
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj == null){
            return "wl/wljjxx";
        }
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        // 排序条件
        String lastOrder = (String) session.getAttribute("JjxxlastOrder");
        // 排序方式(desc降，asc升)
        String desc = (String) session.getAttribute("JjxxDesc");
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
        }
        // 获取分页数据
        Page page = wlService.queryForPage(parseInt(pageNo),7,dc, aj);
        // 将数据存入request域中
        model.addAttribute("seachCode", seachCode);
        model.addAttribute("seachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        session.setAttribute("JjxxlastOrder",orderby);
        session.setAttribute("JjxxDesc",desc);
        return "/wl/wljjxx";
    }

    /**
     * 根据字段名的seachCode进行(模糊)查询
     * @param seachCondition
     * @param seachCode
     * @param session
     * @return
     */
    @RequestMapping(value = "/SeachCode",method = RequestMethod.POST)
    public String SeachCode(String seachCondition, String seachCode, HttpSession session){
        // 若seachCode(查询内容)为空或为null
        if(seachCode==null || seachCode.trim().isEmpty()){
            session.removeAttribute("JjxxseachCondition");
            session.removeAttribute("JjxxseachCode");
            return "redirect:/wuliujjxx/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("JjxxseachCondition",seachCondition);
        session.setAttribute("JjxxseachCode",seachCode);
        return "redirect:/wuliujjxx/seach?pageNo=1";
    }

    /**
     * 根据某个字段进行排序
     * @param orderby
     * @param session
     * @return
     */
    /*@RequestMapping(value = "/order")
    public String order(String orderby,HttpSession session){
        String desc = (String) session.getAttribute("JjxxDesc");
        String lastOrder = (String) session.getAttribute("JjxxlastOrder");
        // 当不是首次点击时该字段时
        if(orderby.equals(lastOrder)){
            if(desc==null || " ,w.id ".equals(desc)){
                desc = " desc ";
            }else{
                desc = " ,w.id ";
            }
        }else{
            desc = " desc ";
        }
        // 将数据添加到域中
        session.setAttribute("JjxxDesc",desc);
        session.setAttribute("JjxxlastOrder",orderby);
        session.setAttribute("Jjxxorderby",orderby);

        return "redirect:/wuliujjxx/seach?pageNo=1";
    }
*/
    /**
     * 物流数据去重
     * @param ajm
     * @param flg
     * @param req
     * @return
     */
    @RequestMapping(value = "/distinctCount",method = RequestMethod.GET)
    public String distinctCount(String ajm, int flg, HttpServletRequest req){
        AjEntity aje = ajs.findByName(ajm).get(0);
        // 当flg不相等时
        if(flg!=aje.getFlg()){
            aje.setFlg(flg);
            ajs.updateAj(aje);
            aje = ajs.findByName(ajm).get(0);
            req.getSession().setAttribute("aj",aje);
        }
        return "redirect:/wuliujjxx/seach?pageNo=1";
    }
}
