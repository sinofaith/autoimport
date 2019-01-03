package cn.com.sinofaith.controller.zfbController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbJyjlEntity;
import cn.com.sinofaith.form.zfbForm.ZfbDlrzForm;
import cn.com.sinofaith.form.zfbForm.ZfbDlrzForms;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.zfbService.ZfbDlrzService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.NullPrecedence;
import org.hibernate.Session;
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
import java.net.ServerSocket;
import java.util.List;

/**
 * 支付宝登陆日志控制器
 * @author zd
 * create by 2018.12.05
 */
@Controller
@RequestMapping("/zfbDlrz")
public class ZfbDlrzController {
    @Autowired
    private ZfbDlrzService zfbDlrzService;

    @RequestMapping()
    public ModelAndView zfbdlrz(String flag, HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/zfbDlrz/search?pageNo=1");
        session.removeAttribute("dlrzSearchCondition"); //查询条件
        session.removeAttribute("dlrzSearchCode");//查询内容
        session.removeAttribute("dlrzLastOrder");
        session.removeAttribute("dlrzDesc");
        session.setAttribute("flag",flag);
        return mav;
    }

    @RequestMapping("/search")
    public String search(int pageNo, String orderby, HttpSession session, Model model){
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj==null){
            return "/zfb/zfbDlrz";
        }
        String searchCondition = (String) session.getAttribute("dlrzSearchCondition");
        String searchCode = (String) session.getAttribute("dlrzSearchCode");
        String lastOrder = (String) session.getAttribute("dlrzLastOrder");
        String desc = (String) session.getAttribute("dlrzDesc");
        // 创建离线查询语句
        String search = "";
        if(searchCode!=null){
            searchCode = searchCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            search += " and "+searchCondition+" like '%"+searchCode+"%'";
        }
        if(orderby==null && desc==null){
            search += " order by dlzcs desc";
        }
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("desc")){
                    search += " order by "+orderby;
                    desc = "";
                }else{
                    search += " order by "+orderby+" desc nulls last";
                    desc = "desc";
                }
            }else{
                search += " order by "+orderby;
                desc = "";
            }
        }else if("".equals(desc)){
            search += " order by "+lastOrder;
        }else if("desc".equals(desc)){
            search += " order by "+lastOrder+" desc nulls last";
        }
        // 封装分页数据
        // 获取分页数据
        Page page = zfbDlrzService.queryForPage(pageNo,10,search,aj.getId());
        // 将数据存入request域中
        model.addAttribute("dlrzSearchCode", searchCode);
        model.addAttribute("dlrzSearchCondition", searchCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("dlrzLastOrder",orderby);
        }
        session.setAttribute("dlrzOrder",orderby);
        session.setAttribute("dlrzDesc",desc);
        return "/zfb/zfbDlrz";
    }

    /**
     * 查询字段内容
     * @param searchCondition
     * @param searchCode
     * @param session
     * @return
     */
    @RequestMapping(value = "/SearchCode",method = RequestMethod.POST)
    public String SeachCode(String searchCondition, String searchCode, HttpSession session){
        // 若seachCode(查询内容)为空或为null
        if(searchCode==null || searchCode.trim().isEmpty()){
            session.removeAttribute("dlrzSearchCondition");
            session.removeAttribute("dlrzSearchCode");
            return "redirect:/zfbDlrz/search?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("dlrzSearchCondition",searchCondition);
        session.setAttribute("dlrzSearchCode",searchCode);
        return "redirect:/zfbDlrz/search?pageNo=1";
    }

    @RequestMapping(value = "/getDetails",method = RequestMethod.POST)
    public @ResponseBody List<ZfbDlrzForms> getDetails(String zfbyhId,String zhmc,HttpSession session){
        // 获取案件id
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 获取时序图数据
        List<ZfbDlrzForms> zfbDlrzForms = zfbDlrzService.getSequenceChart(zfbyhId,aj.getId());
        for (ZfbDlrzForms zfbDlrzForm : zfbDlrzForms) {
            zfbDlrzForm.setYhm(zhmc);
        }
        return zfbDlrzForms;
    }

    /**
     * 数据导出
     * @param resp
     * @param session
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse resp, HttpSession session) throws IOException {
        // 获得session中对象
        String seachCondition = (String) session.getAttribute("dlrzSearchCondition");
        String seachCode = (String) session.getAttribute("dlrzSearchCode");
        String search = "";
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            search += "and "+seachCondition+" like '%"+seachCode+"%'";
        }
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String lastOrder = (String) session.getAttribute("dlrzLastOrder");
        String desc = (String) session.getAttribute("dlrzDesc");
        if(lastOrder!=null && desc!=null){
            if(desc.equals("")){
                search += " order by "+lastOrder;
            }else{
                search += " order by "+lastOrder+" desc nulls last";
            }
        }else{
            search += " order by dlzcs desc nulls last";
        }
        // 获取所有数据数据
        List<ZfbDlrzForm> dlrzForm = zfbDlrzService.getZfbDlrzAll(search,aj.getId());
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(dlrzForm!=null){
            wb = zfbDlrzService.createExcel(dlrzForm);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝登陆日志(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }
}
