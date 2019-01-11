package cn.com.sinofaith.controller.zfbController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlTjjgsEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.zfbService.ZfbJyjlTjjgsService;
import org.apache.commons.lang.StringUtils;
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
 * 支付宝交易记录买家信息控制器
 * @author zd
 * create by 2018.12.16
 */
@Controller
@RequestMapping("/zfbJyjlTjjgs")
public class ZfbJyjlTjjgsController {
    @Autowired
    private ZfbJyjlTjjgsService zfbJyjlTjjgsService;

    @RequestMapping()
    public ModelAndView zfb(String flag, HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/zfbJyjlTjjgs/seach?pageNo=1");
        session.removeAttribute("jyjlTjjgsSeachCondition"); //查询条件
        session.removeAttribute("jyjlTjjgsSeachCode");//查询内容
        session.removeAttribute("jyjlTjjgsLastOrder");
        session.removeAttribute("jyjlTjjgsDesc");
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
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbJyjlTjjgsEntity.class);
        // 从域中取出对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj==null){
            return "/zfb/zfbJyjlTjjgs";
        }
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        // 查询字段
        String seachCondition = (String) session.getAttribute("jyjlTjjgsSeachCondition");
        // 查询内容
        String seachCode = (String) session.getAttribute("jyjlTjjgsSeachCode");
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if(seachCondition.equals("fkzje")){
                if(StringUtils.isNumeric(seachCode)) {
                    long fz = Long.parseLong(seachCode);
                    BigDecimal big = BigDecimal.valueOf(fz);
                    dc.add(Restrictions.gt(seachCondition, big));
                }else{
                    return "/zfb/zfbJyjlTjjgs";
                }
            }else{
                dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
            }
        }
        // 排序字段
        String lastOrder = (String) session.getAttribute("jyjlTjjgsLastOrder");
        // 排序 desc降 asc升
        String desc = (String) session.getAttribute("jyjlTjjgsDesc");
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
        }
        // 获取分页数据
        Page page = zfbJyjlTjjgsService.queryForPage(pageNo,10,dc);
        // 将数据存入request域中
        model.addAttribute("jyjlTjjgsSeachCode", seachCode);
        model.addAttribute("jyjlTjjgsSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("jyjlTjjgsLastOrder",orderby);
        }else{
            session.setAttribute("jyjlTjjgsLastOrder","fkzje");
        }
        session.setAttribute("jyjlTjjgsOrder",orderby);
        session.setAttribute("jyjlTjjgsDesc",desc);
        return "/zfb/zfbJyjlTjjgs";
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
            session.removeAttribute("jyjlTjjgsSeachCondition");
            session.removeAttribute("jyjlTjjgsSeachCode");
            return "redirect:/zfbJyjlTjjgs/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("jyjlTjjgsSeachCondition",seachCondition);
        session.setAttribute("jyjlTjjgsSeachCode",seachCode);
        return "redirect:/zfbJyjlTjjgs/seach?pageNo=1";
    }

    @RequestMapping("/getDetails")
    public @ResponseBody
    String getDetails(String zfbzh,String jyzt, String dfzh, String order, int page, HttpSession session){
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        String lastOrder = (String) session.getAttribute("jyjlXQLastOrder");
        String desc = (String) session.getAttribute("jyjlXQDesc");
        String search = "";
        search += " and j.mjyhid='"+zfbzh+"' and j.mijyhid='"+dfzh+"'";
        search += " and j.jyzt='"+jyzt+"'";
        // 查询哪一个案件
        if("xxx".equals(order)){
            if("".equals(desc)){
                search += " order by j."+lastOrder+" desc  nulls last";
            }else{
                search += " order by j."+lastOrder;
            }
        }else{
            if(order.equals(lastOrder)){
                if(desc==null || desc.equals("")){
                    search += " order by j."+lastOrder;
                    desc = "desc";
                }else{
                    search += " order by j."+order+" desc  nulls last";
                    desc = "";
                }
            }else{
                search += " order by j."+order+" desc  nulls last";
                desc = "";
            }
        }
        session.setAttribute("jyjlXQDesc", desc);
        if(!order.equals("xxx")){
            session.setAttribute("jyjlXQLastOrder", order);
        }
        String json= zfbJyjlTjjgsService.getZfbJyjlTjjgs(page, 100, search, aj);
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
        ses.removeAttribute("jyjlXQDesc");
        ses.removeAttribute("jyjlXQLastOrder");
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
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbJyjlTjjgsEntity.class);
        // 获得session中对象
        String seachCondition = (String) session.getAttribute("jyjlTjjgsSeachCondition");
        String seachCode = (String) session.getAttribute("jyjlTjjgsSeachCode");
        String name = "";
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if(seachCondition.equals("fkzje") || seachCondition.equals("skzje")){
                if(StringUtils.isNumeric(seachCode)){
                    long fz = Long.parseLong(seachCode);
                    BigDecimal big = new BigDecimal(fz);
                    if(seachCondition.equals("fkzje")){
                        name = "--出账总金额大于"+big;
                    }else{
                        name = "--进账总金额大于"+big;
                    }
                    dc.add(Restrictions.gt(seachCondition,big));
                }
            }else{
                dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
            }
        }
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        String lastOrder = (String) session.getAttribute("jyjlTjjgsLastOrder");
        String desc = (String) session.getAttribute("jyjlTjjgsDesc");
        if(lastOrder!=null && desc!=null){
            if(desc.equals("desc")){
                dc.addOrder(Order.asc(lastOrder));
                dc.addOrder(Order.asc("id"));
            }else{
                dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
                dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
            }
        }else{
            dc.addOrder(Order.desc("fkzje").nulls(NullPrecedence.LAST));
            dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
        }
        // 获取所有数据数据
        List<ZfbJyjlTjjgsEntity> tjjgs = zfbJyjlTjjgsService.getZfbjyjlTjjgAll(dc);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(tjjgs!=null){
            wb = zfbJyjlTjjgsService.createExcel(tjjgs);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝交易记录买家账户信息(\""+aj.getAj()+")"+name+".xls").getBytes(), "ISO8859-1"));
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
    public void downloadDetails(String zfbzh, String jyzt, String dfzh, HttpServletResponse resp,
                                HttpSession session) throws IOException {
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        String lastOrder = (String) session.getAttribute("jyjlXQLastOrder");
        String desc = (String) session.getAttribute("jyjlXQDesc");
        String search = "";
        search += " and j.mjyhid='"+zfbzh+"' and j.mijyhid='"+dfzh+"'";
        search += " and j.jyzt='"+jyzt+"'";
        if(desc==null || desc.equals("")){
            search += " order by j."+lastOrder+" desc  nulls last";
        }else{
            search += " order by j."+lastOrder;
        }
        // 获取所有数据数据
        List<ZfbJyjlEntity> tjjgs = zfbJyjlTjjgsService.getZfbJyjlDetails(search,aj);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(tjjgs!=null){
            wb = ZfbJyjlEntity.createExcel(tjjgs,"支付宝交易记录对手详情信息");
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝交易记录买家详情信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }
}
