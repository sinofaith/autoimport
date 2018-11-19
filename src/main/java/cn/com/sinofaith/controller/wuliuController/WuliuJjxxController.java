package cn.com.sinofaith.controller.wuliuController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.AjServices;
import cn.com.sinofaith.service.wlService.WuliuJjxxService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 *  物流寄件信息  Controller
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
        httpSession.removeAttribute("JjxxDesc");
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
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
        }
        String seach = "";
        if(seachCode!=null && !seachCode.trim().equals("")){
            dc.add(Restrictions.like(seachCondition,seachCode));
            seach = "and "+seachCondition+" like '"+seachCode+"'";
        }
        // 所属案件
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj == null){
            return "wl/wljjxx";
        }else{
            dc.add(Restrictions.eq("aj_id",aj.getId()));
            seach += " and aj_id="+aj.getId();
        }
        // 排序条件
        String lastOrder = (String) session.getAttribute("JjxxlastOrder");
        // 排序方式(desc降，asc升)
        String desc = (String) session.getAttribute("JjxxDesc");
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("desc")){
                    dc.addOrder(Order.desc(orderby));
                    seach += " order by "+ orderby +" desc";
                    desc = "";
                }else{
                    dc.addOrder(Order.asc(orderby));
                    seach += " order by "+ orderby;
                    desc = "desc";
                }
            }else{
                dc.addOrder(Order.desc(orderby));
                seach += " order by "+ orderby +" desc";
                desc = "";
            }
        }else if("".equals(desc)){
            dc.addOrder(Order.desc(lastOrder));
        }else if("desc".equals(desc)){
            dc.addOrder(Order.asc(lastOrder));
        }
        // 获取分页数据
        Page page = wlService.queryForPage(parseInt(pageNo),10,dc, aj, seach);
        // 将数据存入request域中
        model.addAttribute("seachCode", seachCode);
        model.addAttribute("seachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("JjxxlastOrder",orderby);
        }
        session.setAttribute("JjxxOrder",orderby);
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

    /**
     * 文件导出
     * @param rep
     * @param session
     */
    @RequestMapping("/download")
    public void getJjxxDownload(HttpServletResponse rep, HttpSession session) throws IOException {
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(WuliuEntity.class);
        // 获取session域中对象
        String seachCondition = (String) session.getAttribute("JjxxseachCondition");
        String seachCode = (String) session.getAttribute("JjxxseachCode");
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
        }
        String seach = "";
        if(seachCode!=null && !seachCode.equals("")){
            dc.add(Restrictions.eq(seachCondition,seachCode));
            seach = "and "+seachCondition+" like '"+seachCode+"'";
        }
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        seach += " and aj_id="+aj.getId();
        String orderby = (String) session.getAttribute("JjxxOrder");
        String lastOrder = (String) session.getAttribute("JjxxlastOrder");
        String desc = (String) session.getAttribute("JjxxDesc");
        // 是排序的数据
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("desc")){
                    dc.addOrder(Order.asc(orderby));
                    seach += " order by "+ orderby ;
                    desc = "desc";
                }else{
                    dc.addOrder(Order.desc(orderby));
                    seach += " order by "+ orderby +" desc";
                    desc = "";
                }
            }else{
                dc.addOrder(Order.desc(orderby));
                seach += " order by "+ orderby +" desc";
                desc = "desc";
            }
        }
        // 获取所有数据数据
        List<WuliuEntity> wls = wlService.getWuliuAll(seach,dc,aj);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(wls!=null){
            wb = wlService.createExcel(wls);
        }
        rep.setContentType("application/force-download");
        // 当数据是去重是
        if(aj.getFlg()==1){
            rep.setHeader("Content-Disposition","attachment;filename="+new String(("物流寄件去重信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        }else{
            rep.setHeader("Content-Disposition","attachment;filename="+new String(("物流寄件信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        }
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }
}
