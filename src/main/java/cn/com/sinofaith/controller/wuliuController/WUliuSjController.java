package cn.com.sinofaith.controller.wuliuController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.bean.wlBean.WuliuShipEntity;
import cn.com.sinofaith.bean.wlBean.WuliuSjEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.wlService.WuliuSjService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

@Controller
@RequestMapping("/wuliuSj")
public class WUliuSjController {

    @Autowired
    private WuliuSjService wlsjService;

    /**
     * 清空数据跳转页面
     * @param session
     * @return
     */
    @RequestMapping()
    public ModelAndView redirectShip(HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/wuliuSj/seach?pageNo=1");
        // 清空session域中数据
        session.removeAttribute("wuliuSjSeachCondition");
        session.removeAttribute("wuliuSjSeachCode");

        session.removeAttribute("wuliuSjOrder");
        session.removeAttribute("wuliuSjlastOrder");
        session.removeAttribute("wuliuSjDesc");
        return mav;
    }

    @RequestMapping("/seach")
    public String seach(int pageNo, String orderby,HttpSession session, Model model){
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(WuliuSjEntity.class);
        // 获取session域中的数据
        String seachCondition = (String) session.getAttribute("wuliuSjSeachCondition");
        String seachCode = (String) session.getAttribute("wuliuSjSeachCode");
        if(seachCode!=null && !seachCode.trim().equals("")){
            dc.add(Restrictions.like(seachCondition,seachCode));
        }
        // 所属案件
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj == null){
            return "wl/wlSj";
        }else{
            dc.add(Restrictions.eq("aj_id",aj.getId()));
        }
        // 排序
        String lastOrder = (String) session.getAttribute("wuliuSjlastOrder");
        String desc = (String) session.getAttribute("wuliuSjDesc");
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
        }else{
            dc.addOrder(Order.desc("num"));
        }
        // 获取分页数据
        Page page = wlsjService.getDoPage(pageNo,10,dc);
        // 将数据返回
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("wuliuSjlastOrder",orderby);
        }
        session.setAttribute("wuliuSjOrder",orderby);
        session.setAttribute("wuliuSjDesc",desc);
        return "wl/wlSj";
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
            session.removeAttribute("wuliuSjSeachCondition");
            session.removeAttribute("wuliuSjSeachCode");
            return "redirect:/wuliuSj/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("wuliuSjSeachCondition",seachCondition);
        session.setAttribute("wuliuSjSeachCode",seachCode);
        return "redirect:/wuliuSj/seach?pageNo=1";
    }


    @RequestMapping("/getDetails")
    @ResponseBody
    public String getDetails(String sj_phone, String order, int page, HttpSession session){
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(WuliuEntity.class);
        // 添加查询条件
        String seach = "";
        dc.add(Restrictions.eq("sj_phone",sj_phone));
        seach = " and sj_phone='"+sj_phone+"'";
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
        String json = wlsjService.getWuliuRelation(page, 100, dc, seach);
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

    /**
     * 数据导出
     * @param resp
     * @param session
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse resp, HttpSession session) throws IOException {
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(WuliuSjEntity.class);
        // 获得session中对象
        String seachCondition = (String) session.getAttribute("wuliuSjSeachCondition");
        String seachCode = (String) session.getAttribute("wuliuSjSeachCode");
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            dc.add(Restrictions.like(seachCondition,seachCode));
        }
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        String lastOrder = (String) session.getAttribute("wuliuSjlastOrder");
        String desc = (String) session.getAttribute("wuliuSjDesc");
        String orderby = (String) session.getAttribute("wuliuSjOrder");
        // 是排序的数据
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("desc")){
                    dc.addOrder(Order.desc(orderby));
                    desc = "desc";
                }else{
                    dc.addOrder(Order.asc(orderby));
                    desc = "";
                }
            }else{
                dc.addOrder(Order.desc(orderby));
                desc = "desc";
            }
        }
        // 获取所有数据数据
        List<WuliuSjEntity> wls = wlsjService.getWuliuAll(dc);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(wls!=null){
            wb = wlsjService.createExcel(wls);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("物流收件人信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    /**
     * 详情页的数据导出
     * @param sj_phone
     * @param resp
     */
    @RequestMapping("/downDetailInfo")
    public void downDetailInfo(String sj_phone, HttpServletResponse resp, HttpSession session) throws IOException {
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(WuliuEntity.class);
        // 取出域中数据
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String seach = " and aj_id="+aj.getId();
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        seach += " and sj_phone='"+sj_phone+"'";
        dc.add(Restrictions.eq("sj_phone",sj_phone));
        List<WuliuEntity> wls = wlsjService.WuliuAll(dc,seach);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(wls!=null){
            wb = wlsjService.createDetailsExcel(wls);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("物流收件人详情信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }
}
