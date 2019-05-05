package cn.com.sinofaith.controller.pyramidSaleController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.pyramidSaleBean.PsHierarchyEntity;
import cn.com.sinofaith.bean.pyramidSaleBean.PyramidSaleEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.PyramidSaleService.PyramidSaleTierService;
import cn.com.sinofaith.util.MappingUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.NullPrecedence;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 传销层级关系控制器
 * @author zd
 * create by 2018.10.31
 */
@Controller
@RequestMapping("/pyramidSaleTier")
public class pyramidSaleTierController {
    @Autowired
    private PyramidSaleTierService pyramidSaleTierService;

    @RequestMapping()
    public ModelAndView pyramidSaleTier(HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/pyramidSaleTier/seach?pageNo=1");
        session.removeAttribute("psTierSeachCondition"); //查询条件
        session.removeAttribute("psTierSeachCode");//查询内容
        session.removeAttribute("psTierLastOrder");
        session.removeAttribute("psTierDesc");
        return mav;
    }

    @RequestMapping("/seach")
    public String seach(int pageNo, String orderby, HttpSession session, Model model){
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(PsHierarchyEntity.class);
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj==null){
            return "/pyramidSale/pyramidSaleTier";
        }
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        String seachCondition = (String) session.getAttribute("psTierSeachCondition");
        String seachCode = (String) session.getAttribute("psTierSeachCode");
        String lastOrder = (String) session.getAttribute("psTierLastOrder");
        String desc = (String) session.getAttribute("psTierDesc");
        // 创建离线查询语句
        if(seachCode!=null && !seachCondition.equals("directDrive") && !seachCondition.equals("directReferNum")){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
        }else if(seachCode!=null && (seachCondition.equals("directDrive") || seachCondition.equals("directReferNum"))){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            // 判断字符串中是否为纯数字
            /*Pattern p = Pattern.compile("[0-9]*");
            Matcher isNum = p.matcher(seachCode);
            // 检测字符串是否匹配给定的正则表达式
            if(isNum.matches()){
                long temp = Long.parseLong(seachCode);
                dc.add(Restrictions.gt(seachCondition,temp));
            }else{
                return "/pyramidSale/pyramidSaleTier";
            }*/
            if(StringUtils.isNumeric(seachCode)){
                long temp = Long.parseLong(seachCode);
                dc.add(Restrictions.gt(seachCondition,temp));
            }else{
                return "/pyramidSale/pyramidSaleTier";
            }
        }
        if(orderby==null && desc==null){
            dc.addOrder(Order.asc("tier"));
            dc.addOrder(Order.asc("id"));
        }
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("desc")){
                    dc.addOrder(Order.desc(orderby).nulls(NullPrecedence.LAST));
                    dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
                    desc = "";
                }else{
                    dc.addOrder(Order.asc(orderby));
                    dc.addOrder(Order.asc("id"));
                    desc = "desc";
                }
            }else{
                dc.addOrder(Order.desc(orderby).nulls(NullPrecedence.LAST));
                dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
                desc = "";
            }
        }else if("".equals(desc)){
            dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
            dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
        }else if("desc".equals(desc)){
            dc.addOrder(Order.asc(lastOrder));
            dc.addOrder(Order.asc("id"));
        }
        // 封装分页数据
        Page page = pyramidSaleTierService.queryForPage(pageNo,10,dc);
        // 将数据存入request域中
        model.addAttribute("psTierSeachCode", seachCode);
        model.addAttribute("psTierSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("psTierLastOrder",orderby);
        }
        session.setAttribute("psTierOrder",orderby);
        session.setAttribute("psTierDesc",desc);
        return "/pyramidSale/pyramidSaleTier";
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
            session.removeAttribute("psTierSeachCondition");
            session.removeAttribute("psTierSeachCode");
            return "redirect:/pyramidSaleTier/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("psTierSeachCondition",seachCondition);
        session.setAttribute("psTierSeachCode",seachCode);
        return "redirect:/pyramidSaleTier/seach?pageNo=1";
    }

    @RequestMapping("/getDetails")
    public @ResponseBody String getPyramSaleDetails(String psId, String order, boolean temp, int page, HttpSession session){
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        String seach = "";
        String lastOrder = (String) session.getAttribute("xqPyramSaleLastOrder");
        String desc = (String) session.getAttribute("xqPyramSaleDesc");
        if(temp){
            seach = " aj_id="+aj.getId();
            seach += " and sponsorid='"+psId+"'";
        }else{
            seach = " and aj_id="+aj.getId();
        }
        // 查询哪一个案件
        if("xxx".equals(order)){
            if("".equals(desc)){
                seach += " order by "+lastOrder;
            }else{
                seach += " order by "+lastOrder+" desc nulls last";
            }
        }else{
            if(order.equals(lastOrder)){
                if(desc==null || desc.equals("")){
                    seach += " order by "+order;
                    desc = "desc";
                }else{
                    seach += " order by "+order+" desc nulls last";
                    desc = "";
                }
            }else{
                seach += " order by "+order+" desc nulls last";
                desc = "";
            }
        }
        session.setAttribute("xqPyramSaleDesc", desc);
        if(!order.equals("xxx")){
            session.setAttribute("xqPyramSaleLastOrder", order);
        }
        String json= pyramidSaleTierService.getPyramidSale(page, 100, seach, temp, psId);
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
        ses.removeAttribute("xqPyramSaleDesc");
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
        DetachedCriteria dc = DetachedCriteria.forClass(PsHierarchyEntity.class);
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        // 获取session中的数据
        String seachCondition = (String) session.getAttribute("psTierSeachCondition");
        String seachCode = (String) session.getAttribute("psTierSeachCode");
        String name = "";
        if(seachCode!=null){
            if(seachCondition.equals("directDrive") || seachCondition.equals("directReferNum")){
                if(StringUtils.isNumeric(seachCode)){
                    long temp = Long.parseLong(seachCode);
                    dc.add(Restrictions.gt(seachCondition,temp));
                    if(seachCondition.equals("directDrive")){
                        name = "--直推下线数大于"+temp;
                    }else{
                        name = "--下线会员数大于"+temp;
                    }
                }
            }else{
                dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
            }
        }
        String lastOrder = (String) session.getAttribute("psTierLastOrder");
        String desc = (String) session.getAttribute("psTierDesc");
        if(lastOrder!=null && desc!=null) {
            if ("".equals(desc)) {
                dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
                dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
            } else {
                dc.addOrder(Order.asc(lastOrder));
                dc.addOrder(Order.asc("id"));
            }
        }else{
            dc.addOrder(Order.asc("tier"));
            dc.addOrder(Order.asc("id"));
        }
        List<PsHierarchyEntity> tierList = pyramidSaleTierService.download(dc);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(tierList!=null){
            wb = pyramidSaleTierService.createExcel(tierList);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("传销层级信息(\""+aj.getAj()+name+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    @RequestMapping("/downDetailInfo")
    public void downDetailInfo(String psId, boolean temp, HttpServletResponse resp, HttpSession session) throws IOException{
        String desc = (String) session.getAttribute("xqPyramSaleDesc");
        String lastOrder = (String) session.getAttribute("xqPyramSaleLastOrder");
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String search = "";
        if(temp){
            search = " aj_id="+aj.getId();
            search += " and sponsorid='"+psId+"'";
        }else{
            search = " and aj_id="+aj.getId();
        }
        if("".equals(desc)){
            search += " order by "+lastOrder+" desc nulls last";
        }else{
            search += " order by "+lastOrder;
        }
        List<PyramidSaleEntity> psList = pyramidSaleTierService.downDetailInfo(search, temp, psId);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(psList!=null){
            wb = pyramidSaleTierService.createExcelDetails(psList,temp);
        }
        resp.setContentType("application/force-download");
        if(temp){
            resp.setHeader("Content-Disposition","attachment;filename="+new String(("传销层级直推下线信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        }else{
            resp.setHeader("Content-Disposition","attachment;filename="+new String(("传销层级下线会员信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        }
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }
}