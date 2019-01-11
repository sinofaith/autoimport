package cn.com.sinofaith.controller.zfbController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlSjdzsEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlTjjgsEntity;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlSjdzsForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.zfbService.ZfbJyjlSjdzsService;
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
import java.math.BigDecimal;
import java.util.List;


/**
 * 支付宝交易记录地址控制器
 * @author zd
 * create by 2018.12.03
 */

@Controller
@RequestMapping("/zfbJyjlSjdzs")
public class ZfbJyjlSjdzsController {
    @Autowired
    private ZfbJyjlSjdzsService zfbJyjlSjdzsService;

    @RequestMapping()
    public ModelAndView zfbJyjl(String flag, HttpSession session) {
        ModelAndView mav = new ModelAndView("redirect:/zfbJyjlSjdzs/seach?pageNo=1");
        session.removeAttribute("jyjlSjdzsSeachCondition"); //查询条件
        session.removeAttribute("jyjlSjdzsSeachCode");//查询内容
        session.removeAttribute("jyjlSjdzsLastOrder");
        session.removeAttribute("jyjlSjdzsDesc");
        session.setAttribute("flag",flag);
        return mav;
    }

    @RequestMapping("/seach")
    public String seach(int pageNo, String orderby, HttpSession session, Model model) {
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbJyjlSjdzsEntity.class);
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if (aj == null) {
            return "/zfb/zfbJyjlSjdzs";
        }
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        String seachCondition = (String) session.getAttribute("jyjlSjdzsSeachCondition");
        String seachCode = (String) session.getAttribute("jyjlSjdzsSeachCode");
        String lastOrder = (String) session.getAttribute("jyjlSjdzsLastOrder");
        String desc = (String) session.getAttribute("jyjlSjdzsDesc");
        // 创建离线查询语句
        if (seachCode != null) {
            seachCode = seachCode.replace("\r\n", "").replace("，", "").replace(" ", "").replace(" ", "").replace("\t", "");
            dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
        }
        if (orderby == null && desc == null) {
            dc.addOrder(Order.desc("czzje").nulls(NullPrecedence.LAST));
        }
        if (orderby != null) {
            if (orderby.equals(lastOrder)) {
                if (desc == null || desc.equals("desc")) {
                    dc.addOrder(Order.asc(orderby));
                    desc = "";
                } else {
                    dc.addOrder(Order.desc(orderby).nulls(NullPrecedence.LAST));
                    desc = "desc";
                }
            } else {
                dc.addOrder(Order.desc(orderby).nulls(NullPrecedence.LAST));
                desc = "desc";
            }
        } else if ("".equals(desc)) {
            dc.addOrder(Order.asc(lastOrder));
        } else if ("desc".equals(desc)) {
            dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
        }
        // 获取分页数据
        Page page = zfbJyjlSjdzsService.queryForPage(pageNo, 10, dc);
        // 将数据存入request域中
        model.addAttribute("jyjlSjdzsSeachCode", seachCode);
        model.addAttribute("jyjlSjdzsSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("jyjlSjdzsLastOrder",orderby);
        }else{
            session.setAttribute("jyjlSjdzsLastOrder","sjdzs");
        }
        session.setAttribute("jyjlSjdzsOrder",orderby);
        session.setAttribute("jyjlSjdzsDesc",desc);
        return "/zfb/zfbJyjlSjdzs";
    }


    /**
     * 查询字段内容
     * @param seachCondition
     * @param seachCode
     * @param session
     * @return
     */
    @RequestMapping(value = "/SeachCode", method = RequestMethod.POST)
    public String SeachCode(String seachCondition, String seachCode, HttpSession session) {
        // 若seachCode(查询内容)为空或为null
        if (seachCode == null || seachCode.trim().isEmpty()) {
            session.removeAttribute("jyjlSjdzsSeachCondition");
            session.removeAttribute("jyjlSjdzsSeachCode");
            return "redirect:/zfbJyjlSjdzs/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("jyjlSjdzsSeachCondition", seachCondition);
        session.setAttribute("jyjlSjdzsSeachCode", seachCode);
        return "redirect:/zfbJyjlSjdzs/seach?pageNo=1";
    }

    @RequestMapping("/getDetails")
    public @ResponseBody
    String getDetails(String mjyhid, String order,int page, HttpSession session) {
        // 获取Aj
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String search = " aj_id="+aj.getId();
        if(aj.getFilter()!=null){
            search += " and upper(spmc) like '%"+aj.getFilter().toUpperCase()+"%'";
        }
        search += " and mjyhid='"+mjyhid+"' and shrdz is not null and jyzt='交易成功' group by mjyhid,mjxx,shrdz";
        // 获取desc
        String desc = (String) session.getAttribute("jyjlSjdzsXQDesc");
        String lastOrder = (String) session.getAttribute("jyjlSjdzsXQLastOrder");
        // 查询哪一个案件
        if ("xxx".equals(order)) {
            if ("".equals(desc)) {
                search += " order by "+lastOrder+" desc nulls last";
            } else {
                search += " order by "+lastOrder;
            }
        } else {
            if (order.equals(lastOrder)) {
                if (desc == null || desc.equals("")) {
                    search += " order by "+order;
                    desc = "desc";
                } else {
                    search += " order by "+order+" desc nulls last";
                    desc = "";
                }
            } else {
                search += " order by "+order+" desc nulls last";
                desc = "";
            }
        }
        session.setAttribute("jyjlSjdzsXQDesc", desc);
        if (!order.equals("xxx")) {
            session.setAttribute("jyjlSjdzsXQLastOrder", order);
        }
        return zfbJyjlSjdzsService.getZfbJyjlSjdzs(page, 100, search);
    }

    /**
     * 删除desc
     * @param ses
     * @return
     */
    @RequestMapping(value = "/removeDesc", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String removeDesc(HttpSession ses) {
        ses.removeAttribute("jyjlSjdzsXQDesc");
        ses.removeAttribute("jyjlSjdzsXQLastOrder");
        ses.removeAttribute("jyjlSjdzsXQDesc1");
        ses.removeAttribute("jyjlSjdzsXQLastOrder1");
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
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbJyjlSjdzsEntity.class);
        // 获得session中对象
        String seachCondition = (String) session.getAttribute("jyjlSjdzsSeachCondition");
        String seachCode = (String) session.getAttribute("jyjlSjdzsSeachCode");
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
        }
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        String lastOrder = (String) session.getAttribute("jyjlSjdzsLastOrder");
        String desc = (String) session.getAttribute("jyjlSjdzsDesc");
        if(lastOrder!=null && desc!=null){
            if(desc.equals("desc")){
                dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
                dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
            }else{
                dc.addOrder(Order.asc(lastOrder));
                dc.addOrder(Order.asc("id"));
            }
        }else{
            dc.addOrder(Order.desc("sjdzs").nulls(NullPrecedence.LAST));
            dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
        }
        // 获取所有数据数据
        List<ZfbJyjlSjdzsEntity> sjdzs = zfbJyjlSjdzsService.getZfbjyjlSjdzsAll(dc);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(sjdzs!=null){
            wb = zfbJyjlSjdzsService.createExcel(sjdzs);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝交易记录地址统计(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    /**
     * 详情页数据导出
     * @param resp
     * @param session
     * @throws IOException
     */
    @RequestMapping("/downDetailInfo")
    public void downloadDetails(String mjyhid, HttpServletResponse resp,
                                HttpSession session) throws IOException {
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        String lastOrder = (String) session.getAttribute("jyjlSjdzsXQLastOrder");
        String desc = (String) session.getAttribute("jyjlSjdzsXQDesc");
        String search = " aj_id="+aj.getId();
        if(aj.getFilter()!=null){
            search += " and upper(spmc) like '%"+aj.getFilter().toUpperCase()+"%'";
        }
        search += " and mjyhid='"+mjyhid+"' and shrdz is not null and jyzt='交易成功' group by mjyhid,mjxx,shrdz";
        if(desc==null || desc.equals("")){
            search += " order by "+lastOrder+" desc nulls last";
        }else{
            search += " order by "+lastOrder;
        }
        // 获取所有数据数据
        List<ZfbJyjlSjdzsForm> jdszsForms = zfbJyjlSjdzsService.getZfbJyjlDetails(search);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(jdszsForms!=null){
            wb = ZfbJyjlSjdzsForm.createExcel(jdszsForms);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝交易记录地址详情信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    @RequestMapping("/getSingleDetails")
    public @ResponseBody
    String getSingleDetails(String mjyhid, String shrdz, String order, int page, HttpSession session){
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        String lastOrder = (String) session.getAttribute("jyjlXQLastOrder1");
        String desc = (String) session.getAttribute("jyjlXQDesc1");
        String search = "aj_id="+aj.getId();
        search += " and mjyhid='"+mjyhid+"'";
        search += " and shrdz='"+shrdz+"'";
        search += " and jyzt='交易成功'";
        if(aj.getFilter()!=null){
            search += " and upper(spmc) like '%"+aj.getFilter().toUpperCase()+"%'";
        }
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
        session.setAttribute("jyjlXQDesc1", desc);
        if(!order.equals("xxx")){
            session.setAttribute("jyjlXQLastOrder1", order);
        }
        String json= zfbJyjlSjdzsService.getSingleDetails(page, 100, search);
        return json;
    }

    /**
     * 详情页数据导出
     * @param resp
     * @param session
     * @throws IOException
     */
    @RequestMapping("/downDetailInfo1")
    public void downloadDetails1(String mjyhid, String shrdz, HttpServletResponse resp,
                                HttpSession session) throws IOException {
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        String lastOrder = (String) session.getAttribute("jyjlSjdzsXQLastOrder1");
        String desc = (String) session.getAttribute("jyjlSjdzsXQDesc1");
        String search = "aj_id="+aj.getId();
        search += " and mjyhid='"+mjyhid+"'";
        search += " and shrdz='"+shrdz+"'";
        search += " and jyzt='交易成功'";
        if(aj.getFilter()!=null){
            search += " and upper(spmc) like '%"+aj.getFilter().toUpperCase()+"%'";
        }
        if(desc==null || desc.equals("")){
            search += " order by "+lastOrder+" desc nulls last";
        }else{
            search += " order by "+lastOrder;
        }
        // 获取所有数据数据
        List<ZfbJyjlEntity> jyjlList = zfbJyjlSjdzsService.downloadDetails1(search);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(jyjlList!=null){
            wb = ZfbJyjlEntity.createExcel(jyjlList,"支付宝交易单个地址详情信息");
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝交易单个地址详情信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }
}
