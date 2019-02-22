package cn.com.sinofaith.controller.zfbController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.wlBean.WuliuShipEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.zfbService.ZfbZzmxTjjgService;
import com.itextpdf.text.Document;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.NullPrecedence;
import org.hibernate.criterion.*;
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
 * 支付宝转账明细统计结果控制器
 * @author zd
 * create by 2018.12.13
 */
@Controller
@RequestMapping("/zfbZzmxTjjg")
public class ZfbZzmxTjjgController {
    @Autowired
    private ZfbZzmxTjjgService zfbZzmxTjjgService;

    @RequestMapping()
    public ModelAndView zfb(String flag, HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/zfbZzmxTjjg/seach?pageNo=1");
        session.removeAttribute("zzmxTjjgSeachCondition"); //查询条件
        session.removeAttribute("zzmxTjjgSeachCode");//查询内容
        session.removeAttribute("zzmxTjjgLastOrder");
        session.removeAttribute("zzmxTjjgDesc");
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
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbZzmxTjjgEntity.class);
        // 从域中取出对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj==null){
            return "/zfb/zfbZzmxTjjg";
        }
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        // 查询字段
        String seachCondition = (String) session.getAttribute("zzmxTjjgSeachCondition");
        // 查询内容
        String seachCode = (String) session.getAttribute("zzmxTjjgSeachCode");
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if(seachCondition.equals("fkzje") || seachCondition.equals("skzje")){
                if(StringUtils.isNumeric(seachCode)){
                    long fz = Long.parseLong(seachCode);
                    BigDecimal big = new BigDecimal(fz);
                    dc.add(Restrictions.gt(seachCondition,big));
                }else{
                    return "/zfb/zfbZzmxTjjg";
                }
            }else{
                seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
                String[] zzcpmcs = seachCode.split(",");
                // 添加逻辑或的关系
                Disjunction disjunction = Restrictions.disjunction();
                for (int i = 0; i < zzcpmcs.length; i++) {
                    String zzcpmc = zzcpmcs[i];
                    disjunction.add(Restrictions.like(seachCondition, "%" + zzcpmc + "%"));
                }
                dc.add(disjunction);
            }
        }
        // 排序字段
        String lastOrder = (String) session.getAttribute("zzmxTjjgLastOrder");
        // 排序 desc降 asc升
        String desc = (String) session.getAttribute("zzmxTjjgDesc");
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("")){
                    dc.addOrder(Order.asc(orderby));
                    dc.addOrder(Order.asc("id"));
                    desc = "desc";
                }else{
                    dc.addOrder(Order.desc(orderby).nulls(NullPrecedence.LAST));
                    dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
                    desc = "";
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
        }else if(desc==null){
            dc.addOrder(Order.desc("fkzje").nulls(NullPrecedence.LAST));
            dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
            session.setAttribute("zzmxTjjgLastOrder","fkzje");
        }
        // 获取分页数据
        Page page = zfbZzmxTjjgService.queryForPage(pageNo,10,dc);
        // 将数据存入request域中
        model.addAttribute("zzmxTjjgSeachCode", seachCode);
        model.addAttribute("zzmxTjjgSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("zzmxTjjgLastOrder",orderby);
        }
        session.setAttribute("zzmxTjjgOrder",orderby);
        session.setAttribute("zzmxTjjgDesc",desc);
        return "/zfb/zfbZzmxTjjg";
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
        if(seachCode.contains(",")){
            seachCode = seachCode.substring(0, seachCode.length()-1);
        }
        // 若seachCode(查询内容)为空或为null
         if(seachCode==null || seachCode.trim().isEmpty()){
            session.removeAttribute("zzmxTjjgSeachCondition");
            session.removeAttribute("zzmxTjjgSeachCode");
            return "redirect:/zfbZzmxTjjg/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("zzmxTjjgSeachCondition",seachCondition);
        session.setAttribute("zzmxTjjgSeachCode",seachCode);
        return "redirect:/zfbZzmxTjjg/seach?pageNo=1";
    }

    @RequestMapping("/getDetails")
    public @ResponseBody
    String getPyramSaleDetails(String zfbzh, String zzcpmc, String order, int page, HttpSession session){
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        String lastOrder = (String) session.getAttribute("zzmxXQLastOrder");
        String desc = (String) session.getAttribute("zzmxXQDesc");
        String search = " (z.fkfzfbzh = '"+zfbzh+"' or z.skfzfbzh= '"+zfbzh+"')";
        search += " and z.zzcpmc = '"+zzcpmc+"'";
        // 查询哪一个案件
        if("xxx".equals(order)){
            if("".equals(desc)){
                search += " order by "+lastOrder+" desc  nulls last";
            }else{
                search += " order by "+lastOrder;
            }
        }else{
            if(order.equals(lastOrder)){
                if(desc==null || desc.equals("")){
                    search += " order by "+lastOrder;
                    desc = "desc";
                }else{
                    search += " order by "+order+" desc  nulls last";
                    desc = "";
                }
            }else{
                search += " order by "+order+" desc  nulls last";
                desc = "";
            }
        }
        session.setAttribute("zzmxXQDesc", desc);
        if(!order.equals("xxx")){
            session.setAttribute("zzmxXQLastOrder", order);
        }
        String json= zfbZzmxTjjgService.getZfbZzmxTjjg(page, 100, search, aj.getId());
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
        ses.removeAttribute("zzmxXQDesc");
        ses.removeAttribute("zzmxXQLastOrder");
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
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbZzmxTjjgEntity.class);
        // 获得session中对象
        String seachCondition = (String) session.getAttribute("zzmxTjjgSeachCondition");
        String seachCode = (String) session.getAttribute("zzmxTjjgSeachCode");
        String name = "";
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if(seachCondition.equals("fkzje") || seachCondition.equals("skzje")){
                if(StringUtils.isNumeric(seachCode)) {
                    long fz = Long.parseLong(seachCode);
                    BigDecimal big = new BigDecimal(fz);
                    if (seachCondition.equals("fkzje")) {
                        name = "--出账总金额大于" + big;
                    } else {
                        name = "--进账总金额大于" + big;
                    }
                    dc.add(Restrictions.gt(seachCondition, big));
                }
            }else{
                String[] zzcpmcs = seachCode.split(",");
                // 添加逻辑或的关系
                Disjunction disjunction = Restrictions.disjunction();
                for (int i = 0; i < zzcpmcs.length; i++) {
                    String zzcpmc = zzcpmcs[i];
                    disjunction.add(Restrictions.like(seachCondition, "%" + zzcpmc + "%"));
                }
                dc.add(disjunction);
            }
        }
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        String lastOrder = (String) session.getAttribute("zzmxTjjgLastOrder");
        String desc = (String) session.getAttribute("zzmxTjjgDesc");
        if(lastOrder!=null && desc!=null){
            if(desc.equals("desc")){
                dc.addOrder(Order.asc(lastOrder));
                dc.addOrder(Order.asc("id"));
            }else{
                dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
                dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
            }
        }else{
            dc.addOrder(Order.desc("jyzcs").nulls(NullPrecedence.LAST));
            dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
        }
        // 获取所有数据数据
        List<ZfbZzmxTjjgEntity> tjjgs = zfbZzmxTjjgService.getZfbZzmxTjjgAll(dc);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(tjjgs!=null){
            wb = zfbZzmxTjjgService.createExcel(tjjgs);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝转账明细统计信息(\""+aj.getAj()+")"+name+".xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    /**
     * 页面加载显示数据
     * @return
     */
    @RequestMapping("/onload")
    public @ResponseBody List<String> onload(HttpSession session){
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbZzmxEntity.class);
        // 获得session
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        dc.setProjection(Projections.groupProperty("zzcpmc"));
        List<String> zzmcpmcs = zfbZzmxTjjgService.onload(dc);
        return zzmcpmcs;
    }

    /**
     * 详情页数据导出
     * @param resp
     * @param session
     * @throws IOException
     */
    @RequestMapping("/downDetailInfo")
    public void downloadDetails(String zfbzh, String zzcpmc, HttpServletResponse resp,
                                HttpSession session) throws IOException {
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        String lastOrder = (String) session.getAttribute("zzmxXQLastOrder");
        String desc = (String) session.getAttribute("zzmxXQDesc");
        String search = " (z.fkfzfbzh = '"+zfbzh+"' or z.skfzfbzh= '"+zfbzh+"')";
        search += " and z.zzcpmc = '"+zzcpmc+"'";
        if("".equals(desc)){
            search += " order by "+lastOrder+" desc  nulls last";
        }else{
            search += " order by "+lastOrder;
        }
        // 获取所有数据数据
        List<ZfbZzmxEntity> tjjgs = zfbZzmxTjjgService.getZfbZzmxDetails(search,aj.getId());
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(tjjgs!=null){
            wb = ZfbZzmxEntity.createExcel(tjjgs,"支付宝转账统计详情信息");
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝转账明细统计详情信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    @RequestMapping("/createPDF")
    public void createPDF(HttpServletResponse resp,HttpSession session) throws IOException {
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String fileName = "支付宝分析报告("+aj.getAj()+").pdf";
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String((fileName).getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        Document doc = zfbZzmxTjjgService.createPDF(op, aj);
        op.flush();
        op.close();
    }
}
