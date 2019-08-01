package cn.com.sinofaith.controller.bankController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.bankServices.BankTjjgsService;
import cn.com.sinofaith.service.cftServices.CftTjjgsService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import java.io.File;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/bankgtzh")
public class BankGtzhController {
    @Autowired
    private BankTjjgsService banktjss;

    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/bankgtzh/seach?pageNo=1");
        //查询条件
        httpSession.removeAttribute("gtseachCondition");
        //查询内容
        httpSession.removeAttribute("gtseachCode");
        httpSession.setAttribute("hcode",0);


        httpSession.setAttribute("gorderby","num");
        httpSession.setAttribute("glastOrder","num");
        httpSession.setAttribute("gdesc"," desc ");
        return mav;
    }

    @RequestMapping(value = "/removeDesc",method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String removeDesc(HttpSession ses){
        String path = (String) ses.getAttribute("cftPath");
        if(path!=null){
            File uploadPathd = new File(path);
            banktjss.deleteFile(uploadPathd);
            ses.removeAttribute("cftPath");
        }
        ses.removeAttribute("dddesc");
        return "200";
    }

    @RequestMapping(value = "/seachByUrl")
    public ModelAndView getByUrl(@RequestParam("yhkkh") String yhkkh,@RequestParam("zhlx") long zhlx, HttpSession ses){
        ModelAndView mav = new ModelAndView("redirect:/bankgtzh/seach?pageNo=1");
        ses.setAttribute("gtseachCondition","jyzh");
        ses.setAttribute("gtseachCode",yhkkh.replace("#",""));
        if(zhlx==1){
            ses.setAttribute("gtseachCondition","dfzh");
        }
        ses.setAttribute("hcode",0);
        return mav;
    }

    @RequestMapping(value = "/order")
    public ModelAndView order(@RequestParam("orderby") String orderby, HttpSession ses){
        ModelAndView mav = new ModelAndView("redirect:/bankgtzh/seach?pageNo=1");
        String desc = (String) ses.getAttribute("gdesc");
        String lastOrder = (String) ses.getAttribute("glastOrder");
        if(orderby.equals(lastOrder)){
            if(desc==null||" ,c.dfzh ".equals(desc)){
                desc = " desc";
            }else{
                desc = " ,c.dfzh ";
            }
        }else{
            desc = " desc ";
        }

        ses.setAttribute("gorderby",orderby);
        ses.setAttribute("glastOrder",orderby);
        ses.setAttribute("gdesc",desc);
        return mav;
    }

    @RequestMapping(value = "/hiddenZfbCft")
    public ModelAndView hiddenZfbCft(int code ,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/bankgtzh/seach?pageNo=1");
        httpSession.setAttribute("hcode",code);
        return mav;
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getcfttj(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("bank/bankgtzh");
        String seachCondition = (String) req.getSession().getAttribute("gtseachCondition");
        String seachCode = (String) req.getSession().getAttribute("gtseachCode");
        String orderby = (String) req.getSession().getAttribute("gorderby");
        String desc = (String) req.getSession().getAttribute("gdesc");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        int hcode = (Integer) req.getSession().getAttribute("hcode");
        String seach = banktjss.getSeach(seachCondition,seachCode,orderby,desc,aj!=null?aj:new AjEntity(),"-99",hcode);
        Page page = banktjss.queryForPageGt(parseInt(pageNo),10,seach,aj!=null ? aj.getId():-1);
        mav.addObject("page",page);
        mav.addObject("gtseachCode",seachCode);
        mav.addObject("gtseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        mav.addObject("ajm",aj);
        mav.addObject("hcode",hcode);
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/bankgtzh/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("gtseachCode");
            httpSession.removeAttribute("gtseachCondition");
            return mav;
        }

        String seach = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
        httpSession.setAttribute("gtseachCondition",seachCondition);
        httpSession.setAttribute("gtseachCode",seachCode);
        return mav;
    }

    @RequestMapping("/download")
    public void getTjjgDownload(HttpServletResponse rep, HttpServletRequest req) throws Exception{
        String seachCondition = (String) req.getSession().getAttribute("gtseachCondition");
        String seachCode = (String) req.getSession().getAttribute("gtseachCode");
        String orderby = (String) req.getSession().getAttribute("gorderby");
        String desc = (String) req.getSession().getAttribute("gdesc");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        int hcode = (Integer) req.getSession().getAttribute("hcode");
        String seach = banktjss.getSeach(seachCondition,seachCode,orderby,desc,aj!=null?aj:new AjEntity(),"-99",hcode);
        banktjss.downloadFile(seach, rep,aj!=null?aj.getAj():"","共同",req);
    }
    @RequestMapping("/getDetails")
    @ResponseBody
    public String getDetails(@RequestParam("dfzh") String dfzh,@RequestParam("order") String order
            ,@RequestParam("page")int page, HttpServletRequest req,HttpSession ses){
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String desc = (String) ses.getAttribute("dddesc");
        String lastOrder = (String) ses.getAttribute("ddlastOrder");
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
            orders = " order by c." + order + desc+" nulls last,c.jyzcs desc ";
        }else{
            orders = " order by c."+lastOrder+desc + " nulls last,c.jyzcs desc ";
            order=lastOrder;
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String seach = " and c.dfzh='"+dfzh+"' and c.aj_id="+aj.getId() + orders;
        ses.setAttribute("ddOrder",order);
        ses.setAttribute("ddlastOrder",order);
        ses.setAttribute("dddesc",desc);
        return gson.toJson(banktjss.queryForPageGt(page,300,seach,aj!=null ? aj.getId():-1)).replace("null","\"\" ");
    }
    @RequestMapping("/downgtlxr")
    public void downGtlxr(@RequestParam("dfzh") String dfzh,HttpServletRequest req,
                          HttpServletResponse rep,HttpSession ses)throws Exception{
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String desc = (String) ses.getAttribute("dddesc");
        String lastOrder = (String) ses.getAttribute("ddlastOrder");
        String seach = " and c.dfzh='"+dfzh+"' and aj_id="+aj.getId()
                + " order by c."+lastOrder+desc + " nulls last,c.jyzcs desc ";
        banktjss.downloadFile(seach,rep,aj.getAj(),"共同",req);
    }
}
