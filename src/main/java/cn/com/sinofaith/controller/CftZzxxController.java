package cn.com.sinofaith.controller;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.page.Page;
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
        String seach = cftzzs.getSeach(seachCode,seachCondition,aj!=null ? aj:new AjEntity());
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
        String seach = cftzzs.getSeach(seachCode,seachCondition,aj!=null ? aj:new AjEntity());
        cftzzs.downloadFile(seach,rep,aj.getAj());
    }
}