package cn.com.sinofaith.controller.zfbController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZhmxEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.zfbService.ZfbJyjlService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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
    public ModelAndView zfbJyjl(String flag, HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/zfbJyjl/seach?pageNo=1");
        session.removeAttribute("jyjlSeachCondition"); //查询条件
        session.removeAttribute("jyjlSeachCode");//查询内容
        session.removeAttribute("jyjlLastOrder");
        session.removeAttribute("jyjlDesc");
        session.setAttribute("flag",flag);
        return mav;
    }

    @RequestMapping("/seach")
    public String seach(int pageNo, String orderby, HttpSession session, Model model){
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbJyjlEntity.class);
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj==null){
            return "/zfb/zfbJyjl";
        }
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        String seachCondition = (String) session.getAttribute("jyjlSeachCondition");
        String seachCode = (String) session.getAttribute("jyjlSeachCode");
        String lastOrder = (String) session.getAttribute("jyjlLastOrder");
        String desc = (String) session.getAttribute("jyjlDesc");
        // 创建离线查询语句
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
        }
        if(orderby==null && desc==null){
            dc.addOrder(Order.desc("jyje").nulls(NullPrecedence.LAST));
        }
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("desc")){
                    dc.addOrder(Order.asc(orderby));
                    desc = "";
                }else{
                    dc.addOrder(Order.desc(orderby).nulls(NullPrecedence.LAST));
                    desc = "desc";
                }
            }else{
                dc.addOrder(Order.asc(orderby));
                desc = "";
            }
        }else if("".equals(desc)){
            dc.addOrder(Order.asc(lastOrder));
        }else if("desc".equals(desc)){
            dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
        }
        // 封装分页数据
        // 获取分页数据
        Page page = zfbJyjlService.queryForPage(pageNo,10,dc);
        // 将数据存入request域中
        model.addAttribute("jyjlSeachCode", seachCode);
        model.addAttribute("jyjlSeachCondition", seachCondition);
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

    /**
     * 数据导出
     * @param resp
     * @param session
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse resp, HttpSession session) throws IOException {
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbJyjlEntity.class);
        // 获得session中对象
        String seachCondition = (String) session.getAttribute("jyjlSeachCondition");
        String seachCode = (String) session.getAttribute("jyjlSeachCode");
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
        }
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        String lastOrder = (String) session.getAttribute("jyjlLastOrder");
        String desc = (String) session.getAttribute("jyjlDesc");
        if(lastOrder!=null && desc!=null){
            if(desc.equals("")){
                dc.addOrder(Order.asc(lastOrder));
                dc.addOrder(Order.asc("id"));
            }else{
                dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
                dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
            }
        }else{
            dc.addOrder(Order.desc("jyje").nulls(NullPrecedence.LAST));
            dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
        }
        // 获取所有数据数据
        List<ZfbJyjlEntity> zcxxs = zfbJyjlService.getZfbJyjlAll(dc);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(zcxxs!=null){
            wb = zfbJyjlService.createExcel(zcxxs);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝交易记录(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }
}
