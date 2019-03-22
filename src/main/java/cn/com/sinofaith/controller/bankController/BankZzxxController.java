package cn.com.sinofaith.controller.bankController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.bankServices.BankZzxxServices;
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

@Controller
@RequestMapping("/bankzzxx")
public class BankZzxxController {
    @Autowired
    private BankZzxxServices bankzzs;
    @Autowired
    private CftZzxxService cftzzs;

    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/bankzzxx/seach?pageNo=1");
        httpSession.removeAttribute("bzzseachCondition"); //查询条件
        httpSession.removeAttribute("bzzseachCode");//查询内容

        httpSession.removeAttribute("bzorderby");
        httpSession.removeAttribute("bzlastOrder");
        httpSession.removeAttribute("bzdesc");
        return mav;
    }

    @RequestMapping(value = "/seachByUrl")
    public ModelAndView getByUrl(@RequestParam("yhkkh") String yhkkh,HttpSession ses){
        ModelAndView mav = new ModelAndView("redirect:/bankzzxx/seach?pageNo=1");
        ses.setAttribute("bzzseachCondition","yhkkh");
        ses.setAttribute("bzzseachCode",yhkkh.replace("#",""));
        return mav;
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getCftzzxx(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("bank/bankzzxx");
        String seachCondition = (String) req.getSession().getAttribute("bzzseachCondition");
        String seachCode = (String) req.getSession().getAttribute("bzzseachCode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String orderby = (String) req.getSession().getAttribute("bzorderby");
        String desc = (String) req.getSession().getAttribute("bzdesc");
        String seach = bankzzs.getSeach(seachCode,seachCondition,orderby,desc,aj!=null ? aj:new AjEntity());
        Page page = bankzzs.queryForPage(parseInt(pageNo),10,seach);
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
        ModelAndView mav = new ModelAndView("redirect:/bankzzxx/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("bzzseachCode");
            httpSession.removeAttribute("bzzseachCondition");
            return mav;
        }

        httpSession.setAttribute("bzzseachCondition",seachCondition);
        httpSession.setAttribute("bzzseachCode",seachCode);
        return mav;
    }

    @RequestMapping(value = "/order")
    public ModelAndView order(@RequestParam("orderby") String orderby,HttpSession ses){
        ModelAndView mav = new ModelAndView("redirect:/bankzzxx/seach?pageNo=1");
        String desc = (String) ses.getAttribute("bzdesc");
        String lastOrder = (String) ses.getAttribute("bzlastOrder");
        if(orderby.equals(lastOrder)){
            if(desc==null||" ,c.id ".equals(desc)){
                desc = " desc ";
            }else{
                desc = " ,c.id ";
            }
        }else{
            desc = " desc ";
        }

        ses.setAttribute("bzorderby",orderby);
        ses.setAttribute("bzlastOrder",orderby);
        ses.setAttribute("bzdesc",desc);
        return mav;
    }

    @RequestMapping(value = "/download")
    public void getZzxxDownload(HttpServletResponse rep, HttpServletRequest req) throws Exception{
        String seachCondition = (String) req.getSession().getAttribute("bzzseachCondition");
        String seachCode = (String) req.getSession().getAttribute("bzzseachCode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String orderby = (String) req.getSession().getAttribute("bzorderby");
        String desc = (String) req.getSession().getAttribute("bzdesc");
        String seach = cftzzs.getSeach(seachCode,seachCondition,orderby,desc,aj!=null ? aj:new AjEntity());
        bankzzs.downloadFile(seach,rep,aj!=null?aj.getAj():"");
    }

    @RequestMapping(value = "/removeDesc",method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String removeDesc(HttpSession ses){
        ses.removeAttribute("xqdesc");
        return "200";
    }


    @RequestMapping(value = "/getDetails",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getDetails(@RequestParam("yhkkh") String yhkkh,@RequestParam("dfkh")String dfkh,
                             @RequestParam("type") String type,@RequestParam("sum") String sum, @RequestParam("page")int page,
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
             orders = " order by c." + order + desc+" nulls last ";
        }else{
            orders = " order by c."+lastOrder+desc+" nulls last";
            order=lastOrder;
        }
        ses.setAttribute("xqOrder",order);
        ses.setAttribute("xqlastOrder",order);
        ses.setAttribute("xqdesc",desc);
        return bankzzs.getByYhkkh(yhkkh.replace("\n","").trim(),dfkh.replace("\n","").trim(),type,sum,aj!=null ? aj:new AjEntity(),page,orders);
    }

    @RequestMapping(value = "/downDetailZh")
    public void downDetailZh(@RequestParam("yhkkh") String yhkkh,@RequestParam("dskh") String dskh,
                             HttpServletRequest req,HttpServletResponse rep,HttpSession ses)throws Exception{
        dskh = dskh.replace("\n","").trim();
        yhkkh = yhkkh.replace("\n","").trim();
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String ajid=cftzzs.getAjidByAjm(aj);
        String seach = " and (c.yhkkh = '"+yhkkh+"'  or c.dskh = '"+yhkkh+"' or c.bcsm like '"+yhkkh+"')";
        String lastOrder = (String) ses.getAttribute("xqlastOrder");
        String desc = (String) ses.getAttribute("xqdesc");
        if(!"".equals(dskh)){
            seach += " and ( c.dskh='"+dskh+"' or c.bcsm = '"+dskh+"')";
        }
        seach+=" and c.aj_id in("+ajid+") ";
        seach+=" order by c."+lastOrder+desc +" nulls last ";
        bankzzs.downloadFile(seach,rep, aj!=null?aj.getAj():"");
    }
}
