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

        httpSession.setAttribute("gorderby","num");
        httpSession.setAttribute("glastOrder","num");
        httpSession.setAttribute("gdesc"," desc ");
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

    @RequestMapping(value = "/seach")
    public ModelAndView getcfttj(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("bank/bankgtzh");
        String seachCondition = (String) req.getSession().getAttribute("gtseachCondition");
        String seachCode = (String) req.getSession().getAttribute("gtseachCode");
        String orderby = (String) req.getSession().getAttribute("gorderby");
        String desc = (String) req.getSession().getAttribute("gdesc");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String seach = banktjss.getSeach(seachCondition,seachCode,orderby,desc,aj!=null?aj:new AjEntity());
        Page page = banktjss.queryForPageGt(parseInt(pageNo),10,seach,aj!=null ? aj.getId():-1);
        mav.addObject("page",page);
        mav.addObject("gtseachCode",seachCode);
        mav.addObject("gtseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        mav.addObject("ajm",aj);
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
        String seach = banktjss.getSeach(seachCondition,seachCode,orderby,desc,aj!=null?aj:new AjEntity());
        banktjss.downloadFile(seach, rep,aj.getAj(),"共同",req);
    }
    @RequestMapping("/getDetails")
    @ResponseBody
    public String getDetails(@RequestParam("dfzh") String dfzh,@RequestParam("page")int page, HttpServletRequest req){
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        Gson gson = new GsonBuilder().serializeNulls().create();
        String seach = " and c.dfzh='"+dfzh+"' and aj_id="+aj.getId() + " order by jyzcs desc ";
        return gson.toJson(banktjss.queryForPageGt(page,300,seach,aj!=null ? aj.getId():-1)).replace("null","\"\" ");
    }
    @RequestMapping("/downgtlxr")
    public void downGtlxr(@RequestParam("dfzh") String dfzh,HttpServletRequest req,HttpServletResponse rep)throws Exception{
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String seach = " and c.dfzh='"+dfzh+"' and aj_id="+aj.getId();
        banktjss.downloadFile(seach,rep,aj.getAj(),"共同",req);
    }
}
