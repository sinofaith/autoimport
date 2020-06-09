package cn.com.sinofaith.controller.cftController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.UserEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.AjServices;
import cn.com.sinofaith.service.cftServices.CftTjjgsService;
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
 * Created by Me. on 2018/5/23
 */
@Controller
@RequestMapping("/cfttjjgs")
public class CftTjjgsController {
    @Autowired
    private CftTjjgsService cfttjss;
    @Autowired
    private AjServices ajs;

    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/cfttjjgs/seach?pageNo=1");
        //查询条件
        httpSession.removeAttribute("tjsseachCondition");
        //查询内容
        httpSession.removeAttribute("tjsseachCode");

        httpSession.setAttribute("sorderby","jyzcs");
        httpSession.setAttribute("slastOrder","jyzcs");
        httpSession.setAttribute("sdesc"," desc ");
        httpSession.setAttribute("code","0,1");
        return mav;
    }

    @RequestMapping(value = "/seachByUrl")
    public ModelAndView getByUrl(@RequestParam("wxzh") String wxzh,HttpSession ses){
        ModelAndView mav = new ModelAndView("redirect:/cfttjjgs/seach?pageNo=1");
        ses.setAttribute("tjsseachCondition","jyzh");
        ses.setAttribute("tjsseachCode",wxzh.replace("#",""));
        ses.setAttribute("sorderby","jyzcs");
        ses.setAttribute("slastOrder","jyzcs");
        ses.setAttribute("sdesc"," desc ");
        ses.setAttribute("code","0,1");
        return mav;
    }

    @RequestMapping(value = "/getByZhzt")
    public ModelAndView getByZhzt(String code ,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/cfttjjgs/seach?pageNo=1");
        httpSession.setAttribute("code",code);
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
        UserEntity user =(UserEntity) req.getSession().getAttribute("user");
        String seachCondition = (String) req.getSession().getAttribute("tjsseachCondition");
        String seachCode = (String) req.getSession().getAttribute("tjsseachCode");
        String orderby = (String) req.getSession().getAttribute("sorderby");
        String desc = (String) req.getSession().getAttribute("sdesc");
        String code = (String) req.getSession().getAttribute("code");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String seach = cfttjss.getSeach(seachCondition,seachCode,orderby,desc,aj!=null?aj:new AjEntity(),code);
        Page page = cfttjss.queryForPage(parseInt(pageNo),10,seach);
        mav.addObject("page",page);
        mav.addObject("tjsseachCode",seachCode);
        mav.addObject("tjsseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        if(ajs.findByName(aj!=null ? aj.getAj():"",user.getId()).size()>0) {
            mav.addObject("aj", ajs.findByName(aj.getAj(),user.getId()).get(0));
        }
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
        String seachCode = (String) req.getSession().getAttribute("tjsseachCode");
        String orderby = (String) req.getSession().getAttribute("sorderby");
        String desc = (String) req.getSession().getAttribute("sdesc");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String code = (String) req.getSession().getAttribute("code");
        String seach = cfttjss.getSeach(seachCondition,seachCode,orderby,desc,aj!=null?aj:new AjEntity(),code);
        cfttjss.downloadFile(seach, rep,aj!=null?aj.getAj():"","对手",req);
    }


}