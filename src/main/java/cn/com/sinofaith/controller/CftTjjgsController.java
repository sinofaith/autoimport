package cn.com.sinofaith.controller;

import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.CftTjjgsService;
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

/**
 * Created by Me. on 2018/5/23
 */
@Controller
@RequestMapping("/cfttjjgs")
public class CftTjjgsController {
    @Autowired
    private CftTjjgsService cfttjss;

    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/cfttjjgs/seach?pageNo=1");
        httpSession.removeAttribute("tjsseachCondition"); //查询条件
        httpSession.removeAttribute("tjsseachCode");//查询内容
        httpSession.removeAttribute("sorderby");
        httpSession.removeAttribute("sdesc");
        return mav;
    }

    @RequestMapping(value = "/order")
    public ModelAndView order(@RequestParam("orderby") String orderby,HttpSession ses){
        ModelAndView mav = new ModelAndView("redirect:/cfttjjgs/seach?pageNo=1");
        String desc = (String) ses.getAttribute("sdesc");
        String lastOrder = (String) ses.getAttribute("slastOrder");
        if(orderby.equals(lastOrder)){
            if(desc==null||" ,c.id ".equals(desc)){
                desc = " desc";
            }else{
                desc = " ,c.id ";
            }
        }else{
            desc = " desc ";
        }

        ses.setAttribute("sorderby",orderby);
        ses.setAttribute("slastOrder",orderby);
        ses.setAttribute("sdesc",desc);
        return mav;
    }
    @RequestMapping(value = "/seach")
    public ModelAndView getcfttj(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("cft/cfttjjgs");
        String seachCondition = (String) req.getSession().getAttribute("tjsseachCondition");
        String seach = "";
        String seachCode = (String) req.getSession().getAttribute("tjsseachCode");
        String orderby = (String) req.getSession().getAttribute("sorderby");
        String desc = (String) req.getSession().getAttribute("sdesc");
        seach = cfttjss.getSeach(seachCondition,seachCode,orderby,desc);
        Page page = cfttjss.queryForPage(Integer.valueOf(pageNo),10,seach);
        mav.addObject("page",page);
        mav.addObject("tjsseachCode",seachCode);
        mav.addObject("tjsseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/cfttjjgs/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("tjsseachCode");
            httpSession.removeAttribute("tjsseachCondition");
            return mav;
        }

        String seach = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
        httpSession.setAttribute("tjsseachCondition",seachCondition);
        httpSession.setAttribute("tjsseachCode",seachCode);
        return mav;
    }

    @RequestMapping("/download")
    public void getTjjgDownload(HttpServletResponse rep, HttpServletRequest req) throws Exception{
        String seachCondition = (String) req.getSession().getAttribute("tjsseachCondition");
        String seach = "";
        String seachCode = (String) req.getSession().getAttribute("tjsseachCode");
        String orderby = (String) req.getSession().getAttribute("sorderby");
        String desc = (String) req.getSession().getAttribute("sdesc");
        seach = cfttjss.getSeach(seachCondition,seachCode,orderby,desc);
        cfttjss.downloadFile(seach, rep);
    }
}