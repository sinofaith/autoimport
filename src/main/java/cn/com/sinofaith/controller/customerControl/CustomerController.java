package cn.com.sinofaith.controller.customerControl;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.UserEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.customerServices.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/customerPro")
public class CustomerController {
    @Autowired
    private CustomerService cs;

    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/customerPro/seach?pageNo=1");
        //查询条件
        httpSession.removeAttribute("cpseachCondition");
        //查询内容
        httpSession.removeAttribute("cpseachCode");
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/customerPro/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("cpseachCondition");
            httpSession.removeAttribute("cpseachCode");
            return mav;
        }

        String seach = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
        httpSession.setAttribute("cpseachCondition",seachCondition);
        httpSession.setAttribute("cpseachCode",seachCode);
        return mav;
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getCustomer(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("customer/customerPro");
        String seachCondition = (String) req.getSession().getAttribute("cpseachCondition");
        String seachCode = (String) req.getSession().getAttribute("cpseachCode");
//        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        UserEntity user = (UserEntity) req.getSession().getAttribute("user");
//        String seach = custs.getSeach(seachCode,seachCondition,aj!=null? aj : new AjEntity());
        String seach = cs.getSeach(seachCode,seachCondition,user.getId());
        Page page = cs.queryForPage(parseInt(pageNo),10,seach);
        mav.addObject("page",page);
        mav.addObject("cpseachCode",seachCode);
        mav.addObject("cpseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        return mav;
    }
}
