package cn.com.sinofaith.controller.cftController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.cftServices.CftZcxxService;
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
 * Created by Me. on 2018/5/21
 * 财付通注册信息
 */
@Controller
@RequestMapping("/cft")
public class CftInfoController {

    @Autowired
    private CftZcxxService cftzcs;

    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/cft/seach?pageNo=1");
        httpSession.removeAttribute("zcseachCondition"); //查询条件
        httpSession.removeAttribute("zcseachCode");//查询内容
        return mav;
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getCftzcxx(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("cft/cftInfo");
        String seachCondition = (String) req.getSession().getAttribute("zcseachCondition");
        String seachCode = (String) req.getSession().getAttribute("zcseachCode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String seach = cftzcs.getSeach(seachCode,seachCondition,aj!=null? aj : new AjEntity());

        Page page = cftzcs.queryForPage(parseInt(pageNo),10,seach);
        mav.addObject("page",page);
        mav.addObject("zcseachCode",seachCode);
        mav.addObject("zcseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        mav.addObject("aj",aj);
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/cft/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("zcseachCode");
            httpSession.removeAttribute("zcseachCondition");
            return mav;
        }
        httpSession.setAttribute("zcseachCondition",seachCondition);
        httpSession.setAttribute("zcseachCode",seachCode);
        return mav;
    }

    @RequestMapping(value = "/download")
    public void getZcxxDownload(HttpServletResponse rep,HttpServletRequest req,HttpSession session) throws Exception{
        String seachCondition = (String)session.getAttribute("zcseachCondition");
        String seachCode = (String) req.getSession().getAttribute("zcseachCode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String seach = cftzcs.getSeach(seachCode,seachCondition,aj!=null? aj : new AjEntity());
        cftzcs.downloadFile(seach,rep,aj.getAj());
    }
}