package cn.com.sinofaith.controller.cftController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.AjServices;
import cn.com.sinofaith.service.cftServices.CftTjjgService;
import cn.com.sinofaith.service.cftServices.CftTjjgsService;
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
    @Autowired
    private AjServices ajs;
    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/cfttjjg/seach?pageNo=1");
        httpSession.removeAttribute("tjseachCondition"); //查询条件
        httpSession.removeAttribute("tjseachCode");//查询内容
        httpSession.setAttribute("orderby","jyzcs");
        httpSession.setAttribute("lastOrder","jyzcs");
        httpSession.setAttribute("desc"," desc ");
        return mav;
    }

    @RequestMapping(value = "/order")
    public ModelAndView order(@RequestParam("orderby") String orderby,HttpSession ses){
        ModelAndView mav = new ModelAndView("redirect:/cfttjjg/seach?pageNo=1");
        String desc = (String) ses.getAttribute("desc");
        String lastOrder = (String) ses.getAttribute("lastOrder");
        if(orderby.equals(lastOrder)){
            if(desc==null||" ,c.jylx ".equals(desc)){
                desc = " desc";
            }else{
                desc = " ,c.jylx ";
            }
        }else{
            desc = " desc ";
        }

        ses.setAttribute("orderby",orderby);
        ses.setAttribute("lastOrder",orderby);
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
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        seach = cfttjs.getSeach(seachCondition,seachCode,orderby,desc,aj!=null? aj : new AjEntity());
        Page page = cfttjs.queryForPage(parseInt(pageNo),10,seach);
        mav.addObject("page",page);
        mav.addObject("tjseachCode",seachCode);
        mav.addObject("tjseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        if(ajs.findByName(aj!=null ? aj.getAj():"").size()>0) {
            mav.addObject("aj", ajs.findByName(aj.getAj()).get(0));
        }
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
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        seach = cfttjs.getSeach(seachCondition,seachCode,orderby,desc,aj!=null? aj : new AjEntity());

        cfttjs.downloadFile(seach, rep,aj!=null?aj.getAj():"");
    }

}