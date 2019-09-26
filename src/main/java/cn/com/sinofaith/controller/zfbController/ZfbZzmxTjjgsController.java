package cn.com.sinofaith.controller.zfbController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgsEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.zfbService.ZfbZzmxTjjgService;
import cn.com.sinofaith.service.zfbService.ZfbZzmxTjjgsService;
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
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.List;

/**
 * 支付宝转账明细对手账户控制器
 * @author zd
 * create by 2018.12.13
 */
@Controller
@RequestMapping("/zfbZzmxTjjgs")
public class ZfbZzmxTjjgsController {
    @Autowired
    private ZfbZzmxTjjgsService zfbZzmxTjjgsService;

    @RequestMapping()
    public ModelAndView zfb(String flag, HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/zfbZzmxTjjgs/seach?pageNo=1");
        session.removeAttribute("zzmxTjjgsSeachCondition"); //查询条件
        session.removeAttribute("zzmxTjjgsSeachCode");//查询内容
        session.removeAttribute("zzmxTjjgsLastOrder");
        session.removeAttribute("zzmxTjjgsDesc");
        session.setAttribute("flag",flag);
        session.setAttribute("code","0,1");
        return mav;
    }

    @RequestMapping(value = "/getByZhzt")
    public ModelAndView getByZhzt(String code ,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/zfbZzmxTjjgs/seach?pageNo=1");
        httpSession.setAttribute("code",code);
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
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbZzmxTjjgsEntity.class);
        // 从域中取出对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String code = (String) session.getAttribute("code");

        if(aj==null){
            return "/zfb/zfbZzmxTjjgs";
        }
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        if(!"-99".equals(code)){
            if(code.contains(",")){
                dc.add(Restrictions.or(Restrictions.eq("zhlx",1L),Restrictions.eq("zhlx",0L)));
            }else{
                dc.add(Restrictions.eq("zhlx",Long.parseLong(code)));
            }
        }
        // 查询字段
        String seachCondition = (String) session.getAttribute("zzmxTjjgsSeachCondition");
        // 查询内容
        String seachCode = (String) session.getAttribute("zzmxTjjgsSeachCode");
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if(seachCondition.equals("fkzje") || seachCondition.equals("skzje")){
                if(StringUtils.isNumeric(seachCode)){
                    long fz = Long.parseLong(seachCode);
                    BigDecimal big = BigDecimal.valueOf(fz);
                    dc.add(Restrictions.gt(seachCondition,big));
                }else{
                    return "/zfb/zfbZzmxTjjgs";
                }
            }else if(seachCode.equals("转账到银行卡") && seachCondition.equals("dfzh")){
                dc.add(Restrictions.isNull("dfzh"));
            }else{
                dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
            }
        }
        // 排序字段
        String lastOrder = (String) session.getAttribute("zzmxTjjgsLastOrder");
        // 排序 desc降 asc升
        String desc = (String) session.getAttribute("zzmxTjjgsDesc");
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
            session.setAttribute("zzmxTjjgsLastOrder","fkzje");
        }
        // 获取分页数据
        Page page = zfbZzmxTjjgsService.queryForPage(pageNo,10,dc);
        // 将数据存入request域中
        model.addAttribute("zzmxTjjgsSeachCode", seachCode);
        model.addAttribute("zzmxTjjgsSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("zzmxTjjgsLastOrder",orderby);
        }
        session.setAttribute("zzmxTjjgOrder",orderby);
        session.setAttribute("zzmxTjjgsDesc",desc);
        return "/zfb/zfbZzmxTjjgs";
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
            session.removeAttribute("zzmxTjjgsSeachCondition");
            session.removeAttribute("zzmxTjjgsSeachCode");
            return "redirect:/zfbZzmxTjjgs/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("zzmxTjjgsSeachCondition",seachCondition);
        session.setAttribute("zzmxTjjgsSeachCode",seachCode);
        return "redirect:/zfbZzmxTjjgs/seach?pageNo=1";
    }

    @RequestMapping("/getDetails")
    public @ResponseBody
    String getPyramSaleDetails(String zfbzh, String dfzh, String order, int page, HttpSession session){
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        String lastOrder = (String) session.getAttribute("zzmxXQLastOrder");
        String desc = (String) session.getAttribute("zzmxXQDesc");
        String search = "";
        if(dfzh.equals("转账到银行卡")){
            search += " c.yhid='"+zfbzh+"' and z.fkfzfbzh='"+zfbzh+"' and z.skfzfbzh is null ";
        }else{
            search += " ((z.fkfzfbzh='"+zfbzh+"' and z.skfzfbzh='"+dfzh+"') ";
            search += "or (z.fkfzfbzh='"+dfzh+"' and z.skfzfbzh='"+zfbzh+"')) ";
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
        String json= zfbZzmxTjjgsService.getZfbZzmxTjjgs(page, 100, search, aj);
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
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbZzmxTjjgsEntity.class);
        // 获得session中对象
        String seachCondition = (String) session.getAttribute("zzmxTjjgsSeachCondition");
        String seachCode = (String) session.getAttribute("zzmxTjjgsSeachCode");
        String code = (String) session.getAttribute("code");
        if(!"-99".equals(code)){
            if(code.contains(",")){
                dc.add(Restrictions.or(Restrictions.eq("zhlx",1L),Restrictions.eq("zhlx",0L)));
            }else{
                dc.add(Restrictions.eq("zhlx",Long.parseLong(code)));
            }
        }
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
                dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
            }
        }
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        String lastOrder = (String) session.getAttribute("zzmxTjjgsLastOrder");
        String desc = (String) session.getAttribute("zzmxTjjgsDesc");
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
        List<ZfbZzmxTjjgsEntity> tjjgs = zfbZzmxTjjgsService.getZfbZzmxTjjgAll(dc);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(tjjgs!=null){
            wb = zfbZzmxTjjgsService.createExcel(tjjgs);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝转账明细对手信息(\""+aj.getAj()+")"+name+".xls").getBytes(), "ISO8859-1"));
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
    public void downloadDetails(String zfbzh, String dfzh, HttpServletResponse resp,
                                HttpSession session) throws IOException {
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        String lastOrder = (String) session.getAttribute("zzmxXQLastOrder");
        String desc = (String) session.getAttribute("zzmxXQDesc");
        String search = "";
        if(dfzh.equals("转账到银行卡")){
            search += " c.yhid='"+zfbzh+"' and z.fkfzfbzh='"+zfbzh+"' and z.skfzfbzh is null ";
        }else{
            search += " ((z.fkfzfbzh='"+zfbzh+"' and z.skfzfbzh='"+dfzh+"') ";
            search += "or (z.fkfzfbzh='"+dfzh+"' and z.skfzfbzh='"+zfbzh+"')) ";
        }

        if("".equals(desc)){
            search += " order by "+lastOrder+" desc nulls last";
        }else{
            search += " order by "+lastOrder;
        }
        // 获取所有数据数据
        List<ZfbZzmxEntity> tjjgs = zfbZzmxTjjgsService.getZfbZzmxDetails(search,aj);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(tjjgs!=null){
            wb = ZfbZzmxEntity.createExcel(tjjgs,"支付宝转账对手详情信息");
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝转账明细对手详情信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }
}
