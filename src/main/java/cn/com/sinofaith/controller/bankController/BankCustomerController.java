package cn.com.sinofaith.controller.bankController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.UserEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.bankServices.BankCustomerServices;
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

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/bankcustomer")
public class BankCustomerController {
    @Autowired
    private BankCustomerServices custs;

    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/bankcustomer/seach?pageNo=1");
        //查询条件
        httpSession.removeAttribute("cuseachCondition");
        //查询内容
        httpSession.removeAttribute("cuseachCode");
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/bankcustomer/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("cuseachCondition");
            httpSession.removeAttribute("cuseachCode");
            return mav;
        }

        String seach = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
        httpSession.setAttribute("cuseachCondition",seachCondition);
        httpSession.setAttribute("cuseachCode",seachCode);
        return mav;
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getCustomer(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("bank/bankcustomer");
        String seachCondition = (String) req.getSession().getAttribute("cuseachCondition");
        String seachCode = (String) req.getSession().getAttribute("cuseachCode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        UserEntity user = (UserEntity) req.getSession().getAttribute("user");
//        String seach = custs.getSeach(seachCode,seachCondition,aj!=null? aj : new AjEntity());
        String seach = custs.getSeach(seachCode,seachCondition,aj!=null ? aj:new AjEntity(),user.getId());
        Page page = custs.queryForPage(parseInt(pageNo),10,seach);
        mav.addObject("page",page);
        mav.addObject("cuseachCode",seachCode);
        mav.addObject("cuseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        mav.addObject("aj",aj);
        return mav;
    }

    @RequestMapping(value = "/download")
    public void getZzxxDownload(HttpServletResponse rep, HttpServletRequest req) throws Exception{
        String seachCondition = (String) req.getSession().getAttribute("cuseachCondition");
        String seachCode = (String) req.getSession().getAttribute("cuseachCode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        UserEntity user = (UserEntity) req.getSession().getAttribute("user");
        String seach = custs.getSeach(seachCode,seachCondition,aj!=null ? aj:new AjEntity(),user.getId());
        custs.downloadFile(seach,rep,aj!=null?aj.getAj():"");
    }
}
