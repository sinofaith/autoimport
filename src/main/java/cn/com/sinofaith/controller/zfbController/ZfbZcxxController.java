package cn.com.sinofaith.controller.zfbController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlTjjgsEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZcxxEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.zfbService.ZfbZcxxService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

/***
 * 支付宝注册信息控制器
 * @author zd
 * create by 2018.11.28
 */
@Controller
@RequestMapping("/zfb")
public class ZfbZcxxController {
    @Autowired
    private ZfbZcxxService zfbZcxxService;

    @RequestMapping()
    public ModelAndView zfb(String flag, HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/zfb/seach?pageNo=1");
        session.removeAttribute("zcxxSeachCondition"); //查询条件
        session.removeAttribute("zcxxSeachCode");//查询内容
        session.removeAttribute("zcxxLastOrder");
        session.removeAttribute("zcxxDesc");
        if(flag==null){
            flag = "a1";
        }
        session.setAttribute("flag",flag);
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
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbZcxxEntity.class);
        // 从域中取出对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj==null){
            return "/zfb/zfbZcxx";
        }
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        // 查询字段
        String seachCondition = (String) session.getAttribute("zcxxSeachCondition");
        // 查询内容
        String seachCode = (String) session.getAttribute("zcxxSeachCode");
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
        }
        // 排序字段
        String lastOrder = (String) session.getAttribute("zcxxLastOrder");
        // 排序 desc降 asc升
        String desc = (String) session.getAttribute("zcxxDesc");
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
        Page page = zfbZcxxService.queryForPage(pageNo,10,dc);
        // 将数据存入request域中
        model.addAttribute("zcxxSeachCode", seachCode);
        model.addAttribute("zcxxSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("zcxxLastOrder",orderby);
        }
        session.setAttribute("zcxxOrder",orderby);
        session.setAttribute("zcxxDesc",desc);
        return "/zfb/zfbZcxx";
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
            session.removeAttribute("zcxxSeachCondition");
            session.removeAttribute("zcxxSeachCode");
            return "redirect:/zfb/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("zcxxSeachCondition",seachCondition);
        session.setAttribute("zcxxSeachCode",seachCode);
        return "redirect:/zfb/seach?pageNo=1";
    }

    /**
     * 数据导出
     * @param resp
     * @param session
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse resp, HttpSession session) throws IOException {
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbZcxxEntity.class);
        // 获得session中对象
        String seachCondition = (String) session.getAttribute("zcxxSeachCondition");
        String seachCode = (String) session.getAttribute("zcxxSeachCode");
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
        }
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        /*String lastOrder = (String) session.getAttribute("zcxxLastOrder");
        String desc = (String) session.getAttribute("zcxxDesc");
        if(lastOrder!=null && desc!=null){
            if(desc.equals("desc")){
                dc.addOrder(Order.asc(lastOrder));
                dc.addOrder(Order.asc("id"));
            }else{
                dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
                dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
            }
        }else{
            dc.addOrder(Order.desc("fkzcs").nulls(NullPrecedence.LAST));
            dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
        }*/
        // 获取所有数据数据
        List<ZfbZcxxEntity> zcxxs = zfbZcxxService.getZfbZcxxAll(dc);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(zcxxs!=null){
            wb = zfbZcxxService.createExcel(zcxxs);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝注册信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }
}
