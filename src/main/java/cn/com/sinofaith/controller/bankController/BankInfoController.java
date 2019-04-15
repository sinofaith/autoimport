package cn.com.sinofaith.controller.bankController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.bankServices.BankCustomerServices;
import cn.com.sinofaith.service.bankServices.BankZcxxServices;
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
@RequestMapping("/bank")
public class BankInfoController {

    @Autowired
    private BankZcxxServices bankzcs;
    @Autowired
    private BankCustomerServices cs;

    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/bank/seach?pageNo=1");
        httpSession.removeAttribute("bzcseachCondition"); //查询条件
        httpSession.removeAttribute("bzcseachCode");//查询内容
        return mav;
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getCftzcxx(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("bank/bankInfo");
        String seachCondition = (String) req.getSession().getAttribute("bzcseachCondition");
        String seachCode = (String) req.getSession().getAttribute("bzcseachCode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String seach = bankzcs.getSeach(seachCode,seachCondition,aj!=null? aj : new AjEntity());
        Page page = bankzcs.queryForPage(parseInt(pageNo),10,seach);
        mav.addObject("page",page);
        mav.addObject("bzcseachCode",seachCode);
        mav.addObject("bzcseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        mav.addObject("aj",aj);
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/bank/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("bzcseachCode");
            httpSession.removeAttribute("bzcseachCondition");
            return mav;
        }
        httpSession.setAttribute("bzcseachCondition",seachCondition);
        httpSession.setAttribute("bzcseachCode",seachCode);
        return mav;
    }

    @RequestMapping(value = "/download")
    public void getZcxxDownload(HttpServletResponse rep, HttpServletRequest req, HttpSession session) throws Exception{
        String seachCondition = (String)session.getAttribute("bzcseachCondition");
        String seachCode = (String) req.getSession().getAttribute("bzcseachCode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String seach = bankzcs.getSeach(seachCode,seachCondition,aj!=null? aj : new AjEntity());
        bankzcs.downloadFile(seach,rep,aj!=null?aj.getAj():"");
    }

    @RequestMapping(value =  "/getBq",method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getBq(@RequestParam("yhkh") String yhkh,HttpSession ses){
        AjEntity aj = (AjEntity) ses.getAttribute("aj");

        return bankzcs.getBq(yhkh,aj.getId());
    }
}
