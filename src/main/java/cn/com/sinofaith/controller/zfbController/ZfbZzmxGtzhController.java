package cn.com.sinofaith.controller.zfbController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgsEntity;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxGtzhForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.zfbService.ZfbZzmxGtzhService;
import cn.com.sinofaith.service.zfbService.ZfbZzmxTjjgsService;
import com.google.gson.Gson;
import org.apache.commons.io.filefilter.FalseFileFilter;
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
 * 支付宝转账明细共同账户控制器
 * @author zd
 * create by 2018.12.15
 */
@Controller
@RequestMapping("/zfbZzmxGtzh")
public class ZfbZzmxGtzhController {
    @Autowired
    private ZfbZzmxGtzhService zfbZzmxGtzhService;

    @RequestMapping()
    public ModelAndView zfb(String flag, HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/zfbZzmxGtzh/seach?pageNo=1");
        session.removeAttribute("zzmxGtzhSeachCondition"); //查询条件
        session.removeAttribute("zzmxGtzhSeachCode");//查询内容
        session.removeAttribute("zzmxGtzhLastOrder");
        session.removeAttribute("zzmxGtzhDesc");
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
        // 从域中取出对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj==null){
            return "/zfb/zfbZzmxGtzh";
        }
        String search = " ";
        // 查询字段
        String seachCondition = (String) session.getAttribute("zzmxGtzhSeachCondition");
        // 查询内容
        String seachCode = (String) session.getAttribute("zzmxGtzhSeachCode");
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if(seachCondition.equals("fkzje") || seachCondition.equals("skzje")){
                if(StringUtils.isNumeric(seachCode)) {
                    long fz = Long.parseLong(seachCode);
                    BigDecimal big = BigDecimal.valueOf(fz);
                    search += "where "+seachCondition+" > "+big;
                }else{
                    return "/zfb/zfbZzmxGtzh";
                }
            }else{
                search += "where "+seachCondition+" like '%"+seachCode+"%'";
            }
        }
        // 排序字段
        String lastOrder = (String) session.getAttribute("zzmxGtzhLastOrder");
        // 排序 desc降 asc升
        String desc = (String) session.getAttribute("zzmxGtzhDesc");
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("")){
                    search += " order by "+orderby;
                    desc = "desc";
                }else{
                    search += " order by "+orderby+" desc";
                    desc = "";
                }
            }else{
                search += " order by "+orderby+" desc";;
                desc = "";
            }
        }else if("".equals(desc)){
            search += " order by "+lastOrder+" desc";
        }else if("desc".equals(desc)){
            search += " order by "+lastOrder;
        }else if(desc==null){
            search += " order by gthys desc";
            session.setAttribute("zzmxGLtzhastOrder","gthys");
        }
        // 获取分页数据
        Page page = zfbZzmxGtzhService.queryForPage(pageNo,10,search,aj.getId());
        // 将数据存入request域中
        model.addAttribute("zzmxGtzhSeachCode", seachCode);
        model.addAttribute("zzmxGtzhSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("zzmxGtzhLastOrder",orderby);
        }
        session.setAttribute("zzmxTjjgOrder",orderby);
        session.setAttribute("zzmxGtzhDesc",desc);
        return "/zfb/zfbZzmxGtzh";
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
            session.removeAttribute("zzmxGtzhSeachCondition");
            session.removeAttribute("zzmxGtzhSeachCode");
            return "redirect:/zfbZzmxGtzh/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("zzmxGtzhSeachCondition",seachCondition);
        session.setAttribute("zzmxGtzhSeachCode",seachCode);
        return "redirect:/zfbZzmxGtzh/seach?pageNo=1";
    }

    @RequestMapping("/getDetails")
    public @ResponseBody
    String getPyramSaleDetails(String dfzh, String order, int page, HttpSession session){
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String search = "where dfzh='"+dfzh+"'";
        // 条件语句
        String lastOrder = (String) session.getAttribute("zzmxGtzhXQLastOrder");
        String desc = (String) session.getAttribute("zzmxGtzhXQDesc");
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
        session.setAttribute("zzmxGtzhXQDesc", desc);
        if(!order.equals("xxx")){
            session.setAttribute("zzmxGtzhXQLastOrder", order);
        }
        Page p = zfbZzmxGtzhService.queryForPage(page, 100, search, aj.getId());
        Gson gson = new Gson();
        return gson.toJson(p);
        }

    /**
     * 删除desc
     * @param ses
     * @return
     */
    @RequestMapping(value = "/removeDesc",method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String removeDesc(HttpSession ses){
        ses.removeAttribute("zzmxGtzhXQDesc");
        ses.removeAttribute("zzmxGtzhXQLastOrder");
        return "200";
    }

    /**
     * 数据导出
     * @param resp
     * @param session
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse resp, HttpSession session) throws IOException {
        // 获得session中对象
        String seachCondition = (String) session.getAttribute("zzmxGtzhSeachCondition");
        String seachCode = (String) session.getAttribute("zzmxGtzhSeachCode");
        String name = "";
        String search = "";
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if(seachCondition.equals("fkzje") || seachCondition.equals("skzje")){
                long fz = Long.parseLong(seachCode);
                BigDecimal big = new BigDecimal(fz);
                if(seachCondition.equals("fkzje")){
                    name = "--出账总金额大于"+big;
                }else{
                    name = "--进账总金额大于"+big;
                }
                search += "where "+seachCondition+" > "+big;
            }else{
                search += "where "+seachCondition+" like '%"+seachCode+"%'";
            }
        }
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String lastOrder = (String) session.getAttribute("zzmxGtzhLastOrder");
        String desc = (String) session.getAttribute("zzmxGtzhDesc");
        if(lastOrder!=null && desc!=null){
            if(desc.equals("desc")){
                search += " order by "+lastOrder;
            }else{
                search += " order by "+lastOrder+" desc";
            }
        }else{
            search += " order by gthys desc";
        }
        // 获取所有数据数据
        List<ZfbZzmxGtzhForm> tjjgs = zfbZzmxGtzhService.getZfbZzmxGtzhAll(aj.getId(),search);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(tjjgs!=null){
            wb = zfbZzmxGtzhService.createExcel(tjjgs);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝转账明细共同账户信息(\""+aj.getAj()+")"+name+".xls").getBytes(), "ISO8859-1"));
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
    public void downloadDetails(String dfzh, HttpServletResponse resp,
                                HttpSession session) throws IOException {
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        String lastOrder = (String) session.getAttribute("zzmxGtzhXQLastOrder");
        String desc = (String) session.getAttribute("zzmxGtzhXQDesc");
        String search = "where dfzh='"+dfzh+"'";
        if("".equals(desc)){
            search += " order by "+lastOrder+" desc nulls last";
        }else{
            search +=  " order by "+lastOrder;
        }
        // 获取所有数据数据
        List<ZfbZzmxGtzhForm> zzmxGtzhForms = zfbZzmxGtzhService.getZfbZzmxDetails(search,aj.getId());
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(zzmxGtzhForms!=null){
            wb = zfbZzmxGtzhService.createExcel(zzmxGtzhForms);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝转账明细共同账号信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }
}
