package cn.com.sinofaith.controller.wuliuController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.bean.wlBean.WuliuRelationEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.wlService.WuliuRelationService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
        String seach = "";
        dc.add(Restrictions.eq("ship_phone",ship_phone));
        seach = " and ship_phone='"+ship_phone+"'";
        dc.add(Restrictions.eq("sj_phone",sj_phone));
        seach += " and sj_phone='"+sj_phone+"'";
        // 从session域中取出数据
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        // 查询哪一个案件
        seach += " and aj_id="+aj.getId();
        String lastOrder = (String) session.getAttribute("xqlastOrder");
        String desc = (String) session.getAttribute("xqdesc");
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
        String json = wlRService.getWuliuRelation(page, 100, dc, seach);
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
     * 文件导出
     * @param resp
     * @param session
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse resp, HttpSession session) throws IOException {
        // 获得session中对象
        // 查询字段
        String seachCondition = (String) session.getAttribute("wuliuSeachCondition");
        // 查询内容
        String seachCode = (String) session.getAttribute("wuliuSeachCode");
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 排序字段
        String orderby = (String) session.getAttribute("wuliuRelationOrder");
        // 排序方式(desc降，asc升)
        String desc = (String) session.getAttribute("wuliuRelationDesc");
        // 封装sql语句
        String seach = wlRService.getSeach(seachCondition, seachCode, aj!=null?aj:new AjEntity(), orderby, desc);
        List<WuliuRelationEntity> wlrs = wlRService.getWuliuRelationAll(seach);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(wlrs!=null){
            wb = wlRService.createExcel(wlrs);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("物流寄收关系信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    /**
     * 详情页的数据导出
     * @param ship_phone
     * @param sj_phone
     * @param resp
     */
    @RequestMapping("/downDetailInfo")
    public void downDetailInfo(String ship_phone, String sj_phone, HttpServletResponse resp, HttpSession session) throws IOException {
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(WuliuEntity.class);
        // 取出域中数据
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String seach = " and aj_id="+aj.getId();
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        dc.add(Restrictions.eq("ship_phone",ship_phone));
        seach += " and ship_phone='"+ship_phone+"'";
        dc.add(Restrictions.eq("sj_phone",sj_phone));
        seach += " and sj_phone='"+sj_phone+"'";
        List<WuliuEntity> wls = wlRService.WuliuAll(dc,seach);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(wls!=null){
            wb = wlRService.createDetailsExcel(wls);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("物流寄收关系详情信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }
}
