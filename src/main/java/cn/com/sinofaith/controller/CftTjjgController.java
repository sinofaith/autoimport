package cn.com.sinofaith.controller;

import cn.com.sinofaith.bean.CftZzxxEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.CftTjjgService;
import cn.com.sinofaith.service.CftTjjgsService;
import cn.com.sinofaith.service.CftZzxxService;
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
import java.util.List;

/**
 * Created by Me. on 2018/5/23
 */
@Controller
@RequestMapping("/cfttjjg")
public class CftTjjgController {
    @Autowired
    private CftTjjgService cfttjs;
    @Autowired
    private CftTjjgsService tjss;
    @Autowired
    private CftZzxxService zzs;
    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/cfttjjg/seach?pageNo=1");
        httpSession.removeAttribute("tjseachCondition"); //查询条件
        httpSession.removeAttribute("tjseachCode");//查询内容
        httpSession.removeAttribute("orderby");
        httpSession.removeAttribute("desc");
        return mav;
    }

    @RequestMapping(value = "/order")
    public ModelAndView order(@RequestParam("orderby") String orderby,HttpSession ses){
        ModelAndView mav = new ModelAndView("redirect:/cfttjjg/seach?pageNo=1");
        String desc = (String) ses.getAttribute("desc");
        if(desc==null||" ,c.id ".equals(desc)){
            desc = " desc";
        }else{
            desc = " ,c.id ";
        }
        ses.setAttribute("orderby",orderby);
        ses.setAttribute("desc",desc);
        return mav;
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getcfttj(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("cft/cfttjjg");
        String seachCondition = (String) req.getSession().getAttribute("tjseachCondition");
        String seach = "";
        String seachCode = (String) req.getSession().getAttribute("tjseachCode");
        String orderby = (String) req.getSession().getAttribute("orderby");
        String desc = (String) req.getSession().getAttribute("desc");
        if(seachCondition!=null){
            if("jzzje".equals(seachCondition)||"czzje".equals(seachCondition)){
                seach = " and c."+ seachCondition + " >= "+seachCode;
            }else{
                seach = " and c."+ seachCondition+" like "+"'"+ seachCode +"'";
            }
        }else{
            seach = " and ( 1=1 ) ";
        }
        if(orderby!=null){
            seach =seach + " order by c." +orderby + desc;
        }
        Page page = cfttjs.queryForPage(Integer.valueOf(pageNo),10,seach);
//        System.out.println(req.getSession().getServletContext().getRealPath("/")+"\n");
        mav.addObject("page",page);
        mav.addObject("tjseachCode",seachCode);
        mav.addObject("tjseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/cfttjjg/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("tjseachCode");
            httpSession.removeAttribute("tjseachCondition");
            return mav;
        }

        String seach = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
        httpSession.setAttribute("tjseachCondition",seachCondition);
        httpSession.setAttribute("tjseachCode",seachCode);
        return mav;
    }

    @RequestMapping("/download")
    public void getTjjgDownload(HttpServletResponse rep,HttpServletRequest req) throws Exception{
        String seachCondition = (String) req.getSession().getAttribute("tjseachCondition");
        String seach = "";
        String seachCode = (String) req.getSession().getAttribute("tjseachCode");
        String orderby = (String) req.getSession().getAttribute("orderby");
        String desc = (String) req.getSession().getAttribute("desc");
        if(seachCondition!=null){
            if("jzzje".equals(seachCondition)||"czzje".equals(seachCondition)){
                seach = " and c."+ seachCondition + " >= "+seachCode;
            }else{
                seach = " and c."+ seachCondition+" like "+"'"+ seachCode +"'";
            }
        }else{
            seach = " and ( 1=1 ) ";
        }
        if(orderby!=null){
            seach =seach + " order by c." +orderby + desc;
        }
        cfttjs.downloadFile(seach, rep);
    }

//    @RequestMapping(value = "/refresh")
//    public ModelAndView refresh(){
//        ModelAndView mav = new ModelAndView("redirect:/cfttjjg/seach?pageNo=1");
//        List<CftZzxxEntity> listZzxx =null;
//
//        return mav;
//    }
}