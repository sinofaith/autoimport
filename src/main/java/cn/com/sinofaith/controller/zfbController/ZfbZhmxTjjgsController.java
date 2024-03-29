package cn.com.sinofaith.controller.zfbController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZhmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZhmxTjjgEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZhmxTjjgsEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.zfbService.ZfbZhmxTjjgsService;
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
 * 支付宝账户明细账户与银行卡统计控制器
 * @author zd
 * create by 2019.02.18
 */
@Controller
@RequestMapping("/zfbZhmxTjjgs")
public class ZfbZhmxTjjgsController {
    @Autowired
    private ZfbZhmxTjjgsService zfbZhmxTjjgsService;

    @RequestMapping()
    public ModelAndView zfbZhmxTjjg(String flag, HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/zfbZhmxTjjgs/seach?pageNo=1");
        // 清空session中的相关数据
        session.removeAttribute("zhmxTjjgsSeachCondition"); //查询条件
        session.removeAttribute("zhmxTjjgsSeachCode");//查询内容
        session.removeAttribute("zhmxTjjgsLastOrder");
        session.removeAttribute("zhmxTjjgsDesc");
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
    public String search(int pageNo, String orderby, HttpSession session, Model model){
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbZhmxTjjgsEntity.class);
        // 取出案件对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String code = (String) session.getAttribute("code");
        if(aj == null)
            return "/zfb/zfbZhmxTjjgs";
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        // 查询字段
        String seachCondition = (String) session.getAttribute("zhmxTjjgsSeachCondition");
        // 查询内容
        String seachCode = (String) session.getAttribute("zhmxTjjgsSeachCode");
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if (seachCondition.equals("jzzje") || seachCondition.equals("czzje")) {
                BigDecimal fz = new BigDecimal(seachCode);
                dc.add(Restrictions.gt(seachCondition, fz));
            }else{
                dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
            }
        }
        // 排序字段
        String lastOrder = (String) session.getAttribute("zhmxTjjgsLastOrder");
        // 排序 desc降 asc升
        String desc = (String) session.getAttribute("zhmxTjjgsDesc");
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("")){
                    dc.addOrder(Order.asc(orderby));
                    desc = "desc";
                }else{
                    dc.addOrder(Order.desc(orderby).nulls(NullPrecedence.LAST));
                    desc = "";
                }
            }else{
                dc.addOrder(Order.desc(orderby));
                desc = "";
            }
        }else if("".equals(desc)){
            dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
        }else if("desc".equals(desc)){
            dc.addOrder(Order.asc(lastOrder));
        }else{
            dc.addOrder(Order.desc("czzje").nulls(NullPrecedence.LAST));
            session.setAttribute("zhmxTjjgsLastOrder","czzje");
        }
        // 获取分页数据
        Page page = zfbZhmxTjjgsService.queryForPage(pageNo,10,dc);
        // 将数据存入request域中
        model.addAttribute("zhmxTjjgsSeachCode", seachCode);
        model.addAttribute("zhmxTjjgsSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("zhmxTjjgsLastOrder",orderby);
        }
        session.setAttribute("zhmxTjjgsOrder",orderby);
        session.setAttribute("zhmxTjjgsDesc",desc);
        return "/zfb/zfbZhmxTjjgs";
    }

    @RequestMapping(value = "/SeachCode",method = RequestMethod.POST)
    public String seachCode(String seachCondition, String seachCode, HttpSession session){
        // 若seachCode(查询内容)为空或为null
        if(seachCode==null || seachCode.trim().isEmpty()){
            session.removeAttribute("zhmxTjjgsSeachCondition");
            session.removeAttribute("zhmxTjjgsSeachCode");
            return "redirect:/zfbZhmxTjjgs/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("zhmxTjjgsSeachCondition",seachCondition);
        session.setAttribute("zhmxTjjgsSeachCode",seachCode);
        return "redirect:/zfbZhmxTjjgs/seach?pageNo=1";
    }

    @RequestMapping("/getDetails")
    public @ResponseBody
    String getDetails(String jyzfbzh, String dszfbzh, String zhmc, String dsmc,
                      String order, int page, HttpSession session){
        if (!jyzfbzh.equals("") && !zhmc.equals(""))
            jyzfbzh = jyzfbzh + "(" + zhmc + ")";
        String search = " t.yhxx='"+jyzfbzh+"' and t.jydfxx='"+dszfbzh+"' and t.xfmc='"+dsmc+"'";
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        String lastOrder = (String) session.getAttribute("zhmxXQLastOrder");
        String desc = (String) session.getAttribute("zhmxXQDesc");
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
        session.setAttribute("zhmxXQDesc", desc);
        if(!order.equals("xxx")){
            session.setAttribute("zhmxXQLastOrder", order);
        }
        String json = zfbZhmxTjjgsService.getZfbZhmxTjjgs(page, 100, search, aj.getId());
        return json;
    }

    @RequestMapping("/download")
    public void download(HttpServletResponse resp, HttpSession session) throws IOException {
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbZhmxTjjgsEntity.class);
        // 获得session中对象
        String seachCondition = (String) session.getAttribute("zhmxTjjgsSeachCondition");
        String seachCode = (String) session.getAttribute("zhmxTjjgsSeachCode");
        String name = "";
        if(seachCode!=null) {
            seachCode = seachCode.replace("\r\n", "").replace("，", "").replace(" ", "").replace(" ", "").replace("\t", "");
            if (seachCondition.equals("jzzje") || seachCondition.equals("czzje")) {
                try {
                    BigDecimal fz = new BigDecimal(seachCode);
                    if (seachCondition.equals("czzje")) {
                        name = "--出账总金额大于" + fz;
                    } else {
                        name = "--进账总金额大于" + fz;
                    }
                    dc.add(Restrictions.gt(seachCondition, fz));
                } catch (NumberFormatException e) {
                    dc.add(Restrictions.gt(seachCondition, new BigDecimal(50000)));
                }
            }else{
                dc.add(Restrictions.eq(seachCondition,seachCode));
            }
        }
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        String lastOrder = (String) session.getAttribute("zhmxTjjgsLastOrder");
        String desc = (String) session.getAttribute("zhmxTjjgsDesc");
        if(lastOrder!=null && desc!=null){
            if(desc.equals("desc")){
                dc.addOrder(Order.asc(lastOrder));
                dc.addOrder(Order.asc("id"));
            }else{
                dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
                dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
            }
        }else{
            dc.addOrder(Order.desc("czzje").nulls(NullPrecedence.LAST));
            dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
        }
        // 获取所有数据数据
        List<ZfbZhmxTjjgsEntity> tjjgsList = zfbZhmxTjjgsService.getZfbZhmxTjjgsAll(dc);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(tjjgsList!=null){
            wb = zfbZhmxTjjgsService.createExcel(tjjgsList);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("账户明细账户与银行卡统计(\""+aj.getAj()+")"+name+".xls").getBytes(), "ISO8859-1"));
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
    public void downloadDetails(String jyzfbzh, String dszfbzh, String zhmc, String dsmc,
                                HttpServletResponse resp,HttpSession session) throws IOException {
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        if (!jyzfbzh.equals("") && !zhmc.equals(""))
            jyzfbzh = jyzfbzh + "(" + zhmc + ")";
        String search = " t.yhxx='"+jyzfbzh+"' and t.jydfxx='"+dszfbzh+"' and t.xfmc='"+dsmc+"'";
        String lastOrder = (String) session.getAttribute("zhmxXQLastOrder");
        String desc = (String) session.getAttribute("zhmxXQDesc");
        if("".equals(desc)){
            search += " order by "+lastOrder+" desc  nulls last";
        }else{
            search += " order by "+lastOrder;
        }
        // 获取所有数据数据
        List<ZfbZhmxEntity> zhmxList = zfbZhmxTjjgsService.getZfbZhmxTjjgsDetails(search,aj.getId());
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(zhmxList!=null){
            wb = ZfbZhmxEntity.createExcel(zhmxList,"账户明细账户与银行卡统计");
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝账户账户明细账户与银行卡统计详情信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }
}
