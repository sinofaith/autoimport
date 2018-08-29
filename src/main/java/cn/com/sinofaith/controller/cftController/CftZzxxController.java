package cn.com.sinofaith.controller.cftController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.cftServices.CftZzxxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static java.lang.Integer.parseInt;

/**
 * Created by Me. on 2018/5/22
 * 财付通转账信息
 */
@Controller
@RequestMapping("/cftzzxx")
public class CftZzxxController {
    @Autowired
    private CftZzxxService cftzzs;

    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/cftzzxx/seach?pageNo=1");
        httpSession.removeAttribute("zzseachCondition"); //查询条件
        httpSession.removeAttribute("zzseachCode");//查询内容
        return mav;
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getCftzzxx(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("cft/cftzzxx");
        String seachCondition = (String) req.getSession().getAttribute("zzseachCondition");
        String seachCode = (String) req.getSession().getAttribute("zzseachCode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String orderby = (String) req.getSession().getAttribute("zorderby");
        String desc = (String) req.getSession().getAttribute("zdesc");
        String seach = cftzzs.getSeach(seachCode,seachCondition,orderby,desc,aj!=null ? aj:new AjEntity());
        Page page = cftzzs.queryForPage(parseInt(pageNo),10,seach);
        mav.addObject("page",page);
        mav.addObject("seachCode",seachCode);
        mav.addObject("seachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        mav.addObject("aj",aj);
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/cftzzxx/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("zzseachCode");
            httpSession.removeAttribute("zzseachCondition");
            return mav;
        }

        String seach = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
        httpSession.setAttribute("zzseachCondition",seachCondition);
        httpSession.setAttribute("zzseachCode",seachCode);
        return mav;
    }

    @RequestMapping(value = "/download")
    public void getZzxxDownload(HttpServletResponse rep,HttpServletRequest req) throws Exception{
        String seachCondition = (String) req.getSession().getAttribute("zzseachCondition");
        String seachCode = (String) req.getSession().getAttribute("zzseachCode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String orderby = (String) req.getSession().getAttribute("zorderby");
        String desc = (String) req.getSession().getAttribute("zdesc");
        String seach = cftzzs.getSeach(seachCode,seachCondition,orderby,desc,aj!=null ? aj:new AjEntity());
        cftzzs.downloadFile(seach,rep,aj.getAj());
    }

    @RequestMapping(value = "/order")
    public ModelAndView order(@RequestParam("orderby") String orderby,HttpSession ses){
        ModelAndView mav = new ModelAndView("redirect:/cftzzxx/seach?pageNo=1");
        String desc = (String) ses.getAttribute("zdesc");
        String lastOrder = (String) ses.getAttribute("zlastOrder");
        if(orderby.equals(lastOrder)){
            if(desc==null||" ,c.id ".equals(desc)){
                desc = " desc ";
            }else{
                desc = " ,c.id ";
            }
        }else{
            desc = " desc ";
        }

        ses.setAttribute("zorderby",orderby);
        ses.setAttribute("zlastOrder",orderby);
        ses.setAttribute("zdesc",desc);
        return mav;
    }

    @RequestMapping(value = "/getDetails",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getDetails(@RequestParam("jyzh") String jyzh,@RequestParam("jylx")String jylx,
                             @RequestParam("type") String type,@RequestParam("page")int page,
                             @RequestParam("order") String order, HttpServletRequest req,HttpSession ses){
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");

        String desc = (String) ses.getAttribute("xqdesc");
        String lastOrder = (String) ses.getAttribute("xqlastOrder");
        String orders ="";
        if(!"xx".equals(order)) {
            if (order.equals(lastOrder)) {
                if (desc == null || " ,c.id ".equals(desc)) {
                    desc = " desc ";
                } else {
                    desc = " ,c.id ";
                }
            } else {
                desc = " desc ";
            }
            orders = " order by c." + order + desc+" nulls last,c.jysj desc ";
        }else{
            orders = " order by c."+lastOrder+desc + " nulls last,c.jysj desc ";
            order=lastOrder;
        }
        ses.setAttribute("xqOrder",order);
        ses.setAttribute("xqlastOrder",order);
        ses.setAttribute("xqdesc",desc);


        return cftzzs.getByJyzhlx(jyzh,jylx,type,aj!=null ? aj:new AjEntity(),page,orders);
    }

    @RequestMapping(value = "/downDetailJylx")
    public void downDetailJylx(@RequestParam("zh") String zh,@RequestParam("jylx") String jylx,
                               @RequestParam("type") String type, HttpServletRequest req,HttpServletResponse rep,
                               HttpSession ses)throws Exception{
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String lastOrder = (String) ses.getAttribute("xqlastOrder");
        String desc = (String) ses.getAttribute("xqdesc");
        String seach ="";
        String ajid=cftzzs.getAjidByAjm(aj);
        if("jylx".equals(type)) {
            seach = " and c.zh ='" + zh + "' and c.jylx='" + jylx + "' ";
            if(jylx.equals("提现")){
                seach = " and c.zh ='" + zh + "' and (c.jylx='" + jylx + "' or c.jylx='微信提现手续费') ";
            }
            if(jylx.equals("转帐(有对手账户)")){
                seach=" and c.zh='"+zh+"' and c.fsf is not null and c.jsf is not null ";
            }
            if(jylx.equals("转帐(无对手账户)")){
                seach=" and c.zh='"+zh+"' and c.fsf is null and c.jsf is null ";
            }
        }else{
            seach = " and c.zh ='"+zh+"' and ( c.fsf ='"+jylx+"' or c.jsf='"+jylx+"'"+") ";
            if(zh.equals(jylx)){
                seach = "and c.fsf = c.jsf and c.zh='"+zh+"' ";
            }
        }
        if(aj.getFlg()==1){
            seach +=" and c.shmc not like'%红包%'";
        }
        seach+=" and c.aj_id in("+ajid+") ";
        seach+=" order by c."+lastOrder+desc+" nulls last,c.jysj desc ";
        cftzzs.downloadFile(seach,rep, aj.getAj());
    }
}