package cn.com.sinofaith.controller.zfbController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlTjjgsEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlTjjgForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.zfbService.ZfbJyjlTjjgService;
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
 * 支付宝交易记录统计结果控制器
 * @author zd
 * create by 2018.12.26
 */
@Controller
@RequestMapping("/zfbJyjlTjjg")
public class ZfbJyjlTjjgController {
    @Autowired
    private ZfbJyjlTjjgService zfbJyjlTjjgService;

    @RequestMapping()
    public ModelAndView zfb(String flag, HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/zfbJyjlTjjg/seach?pageNo=1");
        session.removeAttribute("jyjlTjjgSeachCondition"); //查询条件
        session.removeAttribute("jyjlTjjgSeachCode");//查询内容
        session.removeAttribute("jyjlTjjgLastOrder");
        session.removeAttribute("jyjlTjjgDesc");
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
            return "/zfb/zfbJyjlTjjg";
        }
        // 查询字段
        String seachCondition = (String) session.getAttribute("jyjlTjjgSeachCondition");
        // 查询内容
        String seachCode = (String) session.getAttribute("jyjlTjjgSeachCode");
        String search = "";
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if(seachCondition.equals("skzje")){
                if(StringUtils.isNumeric(seachCode)){
                    long fz = Long.parseLong(seachCode);
                    BigDecimal big = BigDecimal.valueOf(fz);
                    search += "and "+seachCondition+">"+big;
                }else{
                    return "/zfb/zfbJyjlTjjg";
                }

            }else{
                search += "and "+seachCondition+" like '%"+seachCode+"%'";
            }
        }
        // 排序字段
        String lastOrder = (String) session.getAttribute("jyjlTjjgLastOrder");
        // 排序 desc降 asc升
        String desc = (String) session.getAttribute("jyjlTjjgDesc");
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("")){
                    search +=" order by "+orderby;
                    desc = "desc";
                }else{
                    search += " order by "+orderby+" desc nulls last";
                    desc = "";
                }
            }else{
                search +=" order by "+orderby+" desc nulls last";
                desc = "";
            }
        }else if("".equals(desc)){
            search += " order by "+lastOrder+" desc nulls last";
            desc = "";
        }else if("desc".equals(desc)){
            search +=" order by "+lastOrder;
            desc = "desc";
        }else if(desc==null){
            search += " order by skzje desc nulls last";
        }
        // 获取分页数据
        Page page = zfbJyjlTjjgService.queryForPage(pageNo,10,search,aj.getId());
        // 将数据存入request域中
        model.addAttribute("jyjlTjjgSeachCode", seachCode);
        model.addAttribute("jyjlTjjgSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("jyjlTjjgLastOrder",orderby);
        }else{
            session.setAttribute("jyjlTjjgLastOrder","skzje");
        }
        session.setAttribute("jyjlTjjgOrder",orderby);
        session.setAttribute("jyjlTjjgDesc",desc);
        return "/zfb/zfbJyjlTjjg";
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
            session.removeAttribute("jyjlTjjgSeachCondition");
            session.removeAttribute("jyjlTjjgSeachCode");
            return "redirect:/zfbJyjlTjjg/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("jyjlTjjgSeachCondition",seachCondition);
        session.setAttribute("jyjlTjjgSeachCode",seachCode);
        return "redirect:/zfbJyjlTjjg/seach?pageNo=1";
    }

    @RequestMapping("/getDetails")
    public @ResponseBody
    String getDetails(String dfzh, String dfmc, String order, int page, HttpSession session){
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String search = " aj_id="+aj.getId();
        if(aj.getFilter()!=null){
            search += " and jyzt='交易成功'";
            search += " and upper(spmc) like '%"+aj.getFilter().toUpperCase()+"%'";
        }
        // 条件语句
        String lastOrder = (String) session.getAttribute("jyjlXQLastOrder");
        String desc = (String) session.getAttribute("jyjlXQDesc");
        search += " and mijyhid='"+dfzh+"'";
        search += " and mijxx like '%"+dfmc+"%'";
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
        session.setAttribute("jyjlXQDesc", desc);
        if(!order.equals("xxx")){
            session.setAttribute("jyjlXQLastOrder", order);
        }
        String json= zfbJyjlTjjgService.getZfbJyjlTjjg(page, 100, search);
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
        // 获得session中对象
        String seachCondition = (String) session.getAttribute("jyjlTjjgSeachCondition");
        String seachCode = (String) session.getAttribute("jyjlTjjgSeachCode");
        String name = "";
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String search = "";
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if(seachCondition.equals("skzje")){
                if(StringUtils.isNumeric(seachCode)){
                    long fz = Long.parseLong(seachCode);
                    BigDecimal big = new BigDecimal(fz);
                    name = "--进账总金额大于"+big;
                    search += " and skzje>"+big;
                }
            }else{
                search += " and "+seachCondition+" like '%"+seachCode+"%'";
            }
        }
        String lastOrder = (String) session.getAttribute("jyjlTjjgLastOrder");
        String desc = (String) session.getAttribute("jyjlTjjgDesc");
        if(lastOrder!=null && desc!=null){
            if(desc.equals("desc")){
                search += " order by "+lastOrder;
            }else{
                search += " order by "+lastOrder+" desc nulls last";
            }
        }else{
            search += " order by skzje desc nulls last";
        }
        // 获取所有数据数据
        List<ZfbJyjlTjjgForm> tjjgs = zfbJyjlTjjgService.getZfbjyjlTjjgAll(search,aj.getId());
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(tjjgs!=null){
            wb = zfbJyjlTjjgService.createExcel(tjjgs);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝交易记录统计结果(\""+aj.getAj()+")"+name+".xls").getBytes(), "ISO8859-1"));
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
    public void downloadDetails(String dfzh, String dfmc, HttpServletResponse resp,
                                HttpSession session) throws IOException {
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 条件语句
        String search = " aj_id="+aj.getId();
        if(aj.getFilter()!=null){
            search += " and jyzt='交易成功'";
            search += " and upper(spmc) like '%"+aj.getFilter().toUpperCase()+"%'";
        }
        // 条件语句
        String lastOrder = (String) session.getAttribute("jyjlXQLastOrder");
        String desc = (String) session.getAttribute("jyjlXQDesc");
        search += " and mijyhid='"+dfzh+"'";
        search += " and mijxx like '%"+dfmc+"%'";
        if("".equals(desc)){
            search += " order by "+lastOrder+" desc  nulls last";
        }else{
            search += " order by "+lastOrder;
        }
        // 获取所有数据数据
        List<ZfbJyjlEntity> tjjgs = zfbJyjlTjjgService.getZfbJyjlDetails(search);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(tjjgs!=null){
            wb = ZfbJyjlEntity.createExcel(tjjgs,"支付宝交易记录详情信息");
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝交易记录统计详情信息(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }
}
